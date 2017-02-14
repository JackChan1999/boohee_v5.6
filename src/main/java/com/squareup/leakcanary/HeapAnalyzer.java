package com.squareup.leakcanary;

import android.support.v7.widget.ActivityChooserView.ActivityChooserViewAdapter;
import android.util.Log;

import com.squareup.leakcanary.ExcludedRefs.Builder;
import com.squareup.leakcanary.LeakTraceElement.Holder;
import com.squareup.leakcanary.LeakTraceElement.Type;
import com.umeng.socialize.common.SocializeConstants;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.parser.internal.SnapshotFactory;
import org.eclipse.mat.snapshot.IPathsFromGCRootsComputer;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.snapshot.PathsFromGCRootsTree;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.IArray;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IInstance;
import org.eclipse.mat.snapshot.model.IObject;
import org.eclipse.mat.snapshot.model.IObjectArray;
import org.eclipse.mat.snapshot.model.NamedReference;
import org.eclipse.mat.snapshot.model.ObjectReference;
import org.eclipse.mat.snapshot.model.PrettyPrinter;
import org.eclipse.mat.snapshot.model.ThreadToLocalReference;
import org.eclipse.mat.util.VoidProgressListener;

public final class HeapAnalyzer {
    private static final String ANONYMOUS_CLASS_NAME_PATTERN = "^.+\\$\\d+$";
    private static final String TAG                          = "HeapAnalyzer";
    private final ExcludedRefs baseExcludedRefs;
    private final ExcludedRefs excludedRefs;

    public HeapAnalyzer(ExcludedRefs excludedRefs) {
        this(new Builder().build(), excludedRefs);
    }

    public HeapAnalyzer(ExcludedRefs baseExcludedRefs, ExcludedRefs excludedRefs) {
        this.baseExcludedRefs = baseExcludedRefs;
        this.excludedRefs = excludedRefs;
    }

    public AnalysisResult checkForLeak(File heapDumpFile, String referenceKey) {
        long analysisStartNanoTime = System.nanoTime();
        if (!heapDumpFile.exists()) {
            return AnalysisResult.failure(new IllegalArgumentException("File does not exist: " +
                    heapDumpFile), since(analysisStartNanoTime));
        }
        ISnapshot iSnapshot = null;
        AnalysisResult noLeak;
        try {
            iSnapshot = openSnapshot(heapDumpFile);
            IObject leakingRef = findLeakingReference(referenceKey, iSnapshot);
            if (leakingRef == null) {
                noLeak = AnalysisResult.noLeak(since(analysisStartNanoTime));
                return noLeak;
            }
            String className = leakingRef.getClazz().getName();
            noLeak = findLeakTrace(analysisStartNanoTime, iSnapshot, leakingRef, className, true);
            if (!noLeak.leakFound) {
                noLeak = findLeakTrace(analysisStartNanoTime, iSnapshot, leakingRef, className,
                        false);
            }
            cleanup(heapDumpFile, iSnapshot);
            return noLeak;
        } catch (Exception e) {
            noLeak = AnalysisResult.failure(e, since(analysisStartNanoTime));
            return noLeak;
        } finally {
            cleanup(heapDumpFile, iSnapshot);
        }
    }

    private AnalysisResult findLeakTrace(long analysisStartNanoTime, ISnapshot snapshot, IObject
            leakingRef, String className, boolean excludingKnownLeaks) throws SnapshotException {
        ExcludedRefs excludedRefs = excludingKnownLeaks ? this.excludedRefs : this.baseExcludedRefs;
        PathsFromGCRootsTree gcRootsTree = shortestPathToGcRoots(snapshot, leakingRef,
                excludedRefs);
        if (gcRootsTree == null) {
            return AnalysisResult.noLeak(since(analysisStartNanoTime));
        }
        return AnalysisResult.leakDetected(!excludingKnownLeaks, className, buildLeakTrace
                (snapshot, gcRootsTree, excludedRefs), since(analysisStartNanoTime));
    }

    private ISnapshot openSnapshot(File heapDumpFile) throws SnapshotException {
        return new SnapshotFactory().openSnapshot(heapDumpFile, Collections.emptyMap(), new
                VoidProgressListener());
    }

    private IObject findLeakingReference(String key, ISnapshot snapshot) throws SnapshotException {
        Collection<IClass> refClasses = snapshot.getClassesByName(KeyedWeakReference.class
                .getName(), false);
        if (refClasses.size() != 1) {
            throw new IllegalStateException("Expecting one class for " + KeyedWeakReference.class
                    .getName() + " in " + refClasses);
        }
        for (int weakRefInstanceId : ((IClass) refClasses.iterator().next()).getObjectIds()) {
            IObject weakRef = snapshot.getObject(weakRefInstanceId);
            if (PrettyPrinter.objectAsString((IObject) weakRef.resolveValue("key"), 100).equals
                    (key)) {
                return (IObject) weakRef.resolveValue("referent");
            }
        }
        throw new IllegalStateException("Could not find weak reference with key " + key);
    }

    private PathsFromGCRootsTree shortestPathToGcRoots(ISnapshot snapshot, IObject leakingRef,
                                                       ExcludedRefs excludedRefs) throws
            SnapshotException {
        return shortestValidPath(snapshot, snapshot.getPathsFromGCRoots(leakingRef.getObjectId(),
                buildClassExcludeMap(snapshot, excludedRefs.excludeFieldMap)), excludedRefs);
    }

    private Map<IClass, Set<String>> buildClassExcludeMap(ISnapshot snapshot, Map<String,
            Set<String>> excludeMap) throws SnapshotException {
        Map<IClass, Set<String>> classExcludeMap = new LinkedHashMap();
        for (Entry<String, Set<String>> entry : excludeMap.entrySet()) {
            Collection<IClass> refClasses = snapshot.getClassesByName((String) entry.getKey(),
                    false);
            if (refClasses != null && refClasses.size() == 1) {
                classExcludeMap.put((IClass) refClasses.iterator().next(), entry.getValue());
            }
        }
        return classExcludeMap;
    }

    private PathsFromGCRootsTree shortestValidPath(ISnapshot snapshot, IPathsFromGCRootsComputer
            pathComputer, ExcludedRefs excludedRefs) throws SnapshotException {
        PathsFromGCRootsTree tree;
        Map<IClass, Set<String>> excludedStaticFields = buildClassExcludeMap(snapshot,
                excludedRefs.excludeStaticFieldMap);
        do {
            int[] shortestPath = pathComputer.getNextShortestPath();
            if (shortestPath == null) {
                return null;
            }
            tree = pathComputer.getTree(Collections.singletonList(shortestPath));
        } while (!validPath(snapshot, tree, excludedStaticFields, excludedRefs));
        return tree;
    }

    private boolean validPath(ISnapshot snapshot, PathsFromGCRootsTree tree, Map<IClass,
            Set<String>> excludedStaticFields, ExcludedRefs excludedRefs) throws SnapshotException {
        if (excludedStaticFields.isEmpty() && excludedRefs.excludedThreads.isEmpty()) {
            return true;
        }
        IObject held = null;
        while (tree != null) {
            IObject holder = snapshot.getObject(tree.getOwnId());
            if (holder instanceof IClass) {
                Set<String> childClassExcludedFields = (Set) excludedStaticFields.get((IClass)
                        holder);
                if (childClassExcludedFields != null) {
                    NamedReference ref = findHeldInHolder(held, holder, excludedRefs);
                    if (ref != null && childClassExcludedFields.contains(ref.getName())) {
                        return false;
                    }
                }
            } else if (holder.getClazz().doesExtend(Thread.class.getName()) && excludedRefs
                    .excludedThreads.contains(getThreadName(holder))) {
                return false;
            }
            held = holder;
            int[] branchIds = tree.getObjectIds();
            tree = branchIds.length > 0 ? tree.getBranch(branchIds[0]) : null;
        }
        return true;
    }

    private String getThreadName(IObject thread) throws SnapshotException {
        return PrettyPrinter.objectAsString((IObject) thread.resolveValue("name"),
                ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    private NamedReference findHeldInHolder(IObject held, IObject holder, ExcludedRefs
            excludedRefs) throws SnapshotException {
        if (held == null) {
            return null;
        }
        Set<String> excludedFields = (Set) excludedRefs.excludeFieldMap.get(holder.getClazz()
                .getName());
        for (NamedReference holdingRef : holder.getOutboundReferences()) {
            if (holdingRef.getObjectId() == held.getObjectId()) {
                if (excludedFields == null) {
                    return holdingRef;
                }
                if (!excludedFields.contains(holdingRef.getName())) {
                    return holdingRef;
                }
            }
        }
        return null;
    }

    private LeakTrace buildLeakTrace(ISnapshot snapshot, PathsFromGCRootsTree tree, ExcludedRefs
            excludedRefs) throws SnapshotException {
        List<LeakTraceElement> elements = new ArrayList();
        IObject held = null;
        while (tree != null) {
            IObject holder = snapshot.getObject(tree.getOwnId());
            elements.add(0, buildLeakElement(held, holder, excludedRefs));
            held = holder;
            int[] branchIds = tree.getObjectIds();
            tree = branchIds.length > 0 ? tree.getBranch(branchIds[0]) : null;
        }
        return new LeakTrace(elements);
    }

    private LeakTraceElement buildLeakElement(IObject held, IObject holder, ExcludedRefs
            excludedRefs) throws SnapshotException {
        Holder holderType;
        String className;
        Type type = null;
        String referenceName = null;
        NamedReference holdingRef = findHeldInHolder(held, holder, excludedRefs);
        if (holdingRef != null) {
            referenceName = holdingRef.getName();
            if (holder instanceof IClass) {
                type = Type.STATIC_FIELD;
            } else if (holdingRef instanceof ThreadToLocalReference) {
                type = Type.LOCAL;
            } else {
                type = Type.INSTANCE_FIELD;
            }
        }
        String extra = null;
        List<String> fields = new ArrayList();
        IClass clazz;
        if (holder instanceof IClass) {
            clazz = (IClass) holder;
            holderType = Holder.CLASS;
            className = clazz.getName();
            for (Field staticField : clazz.getStaticFields()) {
                fields.add("static " + fieldToString(staticField));
            }
        } else if (holder instanceof IArray) {
            holderType = Holder.ARRAY;
            className = holder.getClazz().getName();
            if (holder instanceof IObjectArray) {
                IObjectArray array = (IObjectArray) holder;
                int i = 0;
                ISnapshot snapshot = holder.getSnapshot();
                for (long address : array.getReferenceArray()) {
                    if (address == 0) {
                        fields.add("[" + i + "] = null");
                    } else {
                        fields.add("[" + i + "] = " + snapshot.getObject(snapshot.mapAddressToId
                                (address)));
                    }
                    i++;
                }
            }
        } else {
            IInstance instance = (IInstance) holder;
            clazz = holder.getClazz();
            for (Field staticField2 : clazz.getStaticFields()) {
                fields.add("static " + fieldToString(staticField2));
            }
            for (Field field : instance.getFields()) {
                fields.add(fieldToString(field));
            }
            className = clazz.getName();
            if (clazz.doesExtend(Thread.class.getName())) {
                holderType = Holder.THREAD;
                extra = "(named '" + getThreadName(holder) + "')";
            } else if (className.matches(ANONYMOUS_CLASS_NAME_PATTERN)) {
                String parentClassName = clazz.getSuperClass().getName();
                if (Object.class.getName().equals(parentClassName)) {
                    holderType = Holder.OBJECT;
                    try {
                        extra = "(anonymous class implements " + Class.forName(clazz.getName())
                                .getInterfaces()[0].getName() + SocializeConstants.OP_CLOSE_PAREN;
                    } catch (ClassNotFoundException e) {
                    }
                } else {
                    holderType = Holder.OBJECT;
                    extra = "(anonymous class extends " + parentClassName + SocializeConstants
                            .OP_CLOSE_PAREN;
                }
            } else {
                holderType = Holder.OBJECT;
            }
        }
        return new LeakTraceElement(referenceName, type, holderType, className, extra, fields);
    }

    private String fieldToString(Field field) throws SnapshotException {
        Object value = field.getValue();
        if (value instanceof ObjectReference) {
            value = ((ObjectReference) value).getObject();
        }
        return field.getName() + " = " + value;
    }

    private void cleanup(File heapDumpFile, ISnapshot snapshot) {
        if (snapshot != null) {
            snapshot.dispose();
        }
        final String heapDumpFileName = heapDumpFile.getName();
        final String prefix = heapDumpFileName.substring(0, heapDumpFile.getName().length() - ("" +
                ".hprof").length());
        File[] toRemove = heapDumpFile.getParentFile().listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isDirectory() || !file.getName().startsWith(prefix) || file.getName
                        ().equals(heapDumpFileName)) ? false : true;
            }
        });
        if (toRemove != null) {
            for (File file : toRemove) {
                file.delete();
            }
            return;
        }
        Log.d(TAG, "Could not find HAHA files to cleanup.");
    }

    private long since(long analysisStartNanoTime) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - analysisStartNanoTime);
    }
}
