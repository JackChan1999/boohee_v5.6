package org.eclipse.mat.hprof;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.collect.HashMapIntObject;
import org.eclipse.mat.collect.HashMapLongObject;
import org.eclipse.mat.collect.HashMapLongObject.Entry;
import org.eclipse.mat.collect.IteratorLong;
import org.eclipse.mat.hprof.IHprofParserHandler.HeapObject;
import org.eclipse.mat.parser.IPreliminaryIndex;
import org.eclipse.mat.parser.index.IIndexReader.IOne2LongIndex;
import org.eclipse.mat.parser.index.IndexManager.Index;
import org.eclipse.mat.parser.index.IndexWriter;
import org.eclipse.mat.parser.index.IndexWriter.Identifier;
import org.eclipse.mat.parser.index.IndexWriter.IntArray1NWriter;
import org.eclipse.mat.parser.index.IndexWriter.IntIndexCollector;
import org.eclipse.mat.parser.index.IndexWriter.IntIndexCollectorUncompressed;
import org.eclipse.mat.parser.index.IndexWriter.LongIndexCollector;
import org.eclipse.mat.parser.model.ClassImpl;
import org.eclipse.mat.parser.model.XGCRootInfo;
import org.eclipse.mat.parser.model.XSnapshotInfo;
import org.eclipse.mat.snapshot.model.Field;
import org.eclipse.mat.snapshot.model.FieldDescriptor;
import org.eclipse.mat.snapshot.model.IClass;
import org.eclipse.mat.snapshot.model.IPrimitiveArray;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.IProgressListener.Severity;
import org.eclipse.mat.util.MessageUtil;

public class HprofParserHandlerImpl implements IHprofParserHandler {
    private IntIndexCollectorUncompressed array2size = null;
    private HashMapLongObject<ClassImpl> classesByAddress = new HashMapLongObject();
    private Map<String, List<ClassImpl>> classesByName = new HashMap();
    private HashMapLongObject<String> constantPool = new HashMapLongObject(10000);
    private HashMapLongObject<List<XGCRootInfo>> gcRoots = new HashMapLongObject(200);
    private Identifier identifiers = null;
    private XSnapshotInfo info = new XSnapshotInfo();
    private IntIndexCollector object2classId = null;
    private LongIndexCollector object2position = null;
    private IntArray1NWriter outbound = null;
    private Set<Long> requiredArrayClassIDs = new HashSet();
    private Set<Integer> requiredPrimitiveArrays = new HashSet();
    private HashMapLongObject<HashMapLongObject<List<XGCRootInfo>>> threadAddressToLocals = new HashMapLongObject();
    private Version version;

    public void beforePass1(XSnapshotInfo snapshotInfo) throws IOException {
        this.info = snapshotInfo;
        this.identifiers = new Identifier();
    }

    public void beforePass2(IProgressListener monitor) throws IOException, SnapshotException {
        this.identifiers.add(0);
        this.identifiers.sort();
        if (!(this.requiredArrayClassIDs.isEmpty() && this.requiredPrimitiveArrays.isEmpty())) {
            createRequiredFakeClasses();
        }
        IProgressListener iProgressListener = monitor;
        iProgressListener.sendUserMessage(Severity.INFO, MessageUtil.format(Messages.HprofParserHandlerImpl_HeapContainsObjects, this.info.getPath(), Integer.valueOf(this.identifiers.size())), null);
        int maxClassId = 0;
        Iterator<?> e = this.classesByAddress.values();
        while (e.hasNext()) {
            ClassImpl clazz = (ClassImpl) e.next();
            int index = this.identifiers.reverse(clazz.getObjectAddress());
            clazz.setObjectId(index);
            maxClassId = Math.max(maxClassId, index);
            clazz.setHeapSizePerInstance(calculateInstanceSize(clazz));
            clazz.setUsedHeapSize(calculateClassSize(clazz));
        }
        this.outbound = new IntArray1NWriter(this.identifiers.size(), Index.OUTBOUND.getFile(this.info.getPrefix() + "temp."));
        this.object2classId = new IntIndexCollector(this.identifiers.size(), IndexWriter.mostSignificantBit(maxClassId));
        this.object2position = new LongIndexCollector(this.identifiers.size(), IndexWriter.mostSignificantBit(new File(this.info.getPath()).length()));
        this.array2size = new IntIndexCollectorUncompressed(this.identifiers.size());
        ClassImpl javaLangClass = (ClassImpl) ((List) this.classesByName.get("java.lang.Class")).get(0);
        javaLangClass.setObjectId(this.identifiers.reverse(javaLangClass.getObjectAddress()));
        e = this.classesByAddress.values();
        while (e.hasNext()) {
            clazz = (ClassImpl) e.next();
            clazz.setSuperClassIndex(this.identifiers.reverse(clazz.getSuperClassAddress()));
            clazz.setClassLoaderIndex(this.identifiers.reverse(clazz.getClassLoaderAddress()));
            if (clazz.getClassLoaderId() < 0) {
                clazz.setClassLoaderAddress(0);
                clazz.setClassLoaderIndex(this.identifiers.reverse(0));
            }
            clazz.setClassInstance(javaLangClass);
            javaLangClass.addInstance(clazz.getUsedHeapSize());
            ClassImpl superclass = lookupClass(clazz.getSuperClassAddress());
            if (superclass != null) {
                superclass.addSubClass(clazz);
            }
            this.object2classId.set(clazz.getObjectId(), clazz.getClazz().getObjectId());
            this.outbound.log(this.identifiers, clazz.getObjectId(), clazz.getReferences());
        }
        ClassImpl classLoaderClass = (ClassImpl) ((List) this.classesByName.get(IClass.JAVA_LANG_CLASSLOADER)).get(0);
        HeapObject heapObject = new HeapObject(this.identifiers.reverse(0), 0, classLoaderClass, classLoaderClass.getHeapSizePerInstance());
        heapObject.references.add(classLoaderClass.getObjectAddress());
        addObject(heapObject, 0);
        this.constantPool = null;
    }

    private void createRequiredFakeClasses() throws IOException, SnapshotException {
        if (!this.requiredArrayClassIDs.isEmpty()) {
            for (Long longValue : this.requiredArrayClassIDs) {
                long arrayClassID = longValue.longValue();
                if (lookupClass(arrayClassID) == null) {
                    if (this.identifiers.reverse(arrayClassID) >= 0) {
                        throw new SnapshotException(MessageUtil.format(Messages.HprofParserHandlerImpl_Error_ExpectedClassSegment, Long.toHexString(arrayClassID)));
                    }
                    addClass((ClassImpl) new ClassImpl(arrayClassID, "unknown-class[]", 0, 0, new Field[0], new FieldDescriptor[0]), -1);
                }
            }
        }
        this.requiredArrayClassIDs = null;
        long nextObjectAddress;
        if (this.requiredPrimitiveArrays.isEmpty()) {
            nextObjectAddress = 0;
        } else {
            nextObjectAddress = 0;
            for (Integer arrayType : this.requiredPrimitiveArrays) {
                String name = IPrimitiveArray.TYPE[arrayType.intValue()];
                if (lookupClassByName(name, true) == null) {
                    do {
                        nextObjectAddress++;
                    } while (this.identifiers.reverse(nextObjectAddress) >= 0);
                    addClass((ClassImpl) new ClassImpl(nextObjectAddress, name, 0, 0, new Field[0], new FieldDescriptor[0]), -1);
                }
            }
        }
        this.identifiers.sort();
    }

    private int calculateInstanceSize(ClassImpl clazz) {
        if (clazz.isArrayType()) {
            return this.info.getIdentifierSize();
        }
        return alignUpToX(calculateSizeRecursive(clazz), 8);
    }

    private int calculateSizeRecursive(ClassImpl clazz) {
        if (clazz.getSuperClassAddress() == 0) {
            return this.info.getIdentifierSize() * 2;
        }
        ClassImpl superClass = (ClassImpl) this.classesByAddress.get(clazz.getSuperClassAddress());
        int ownFieldsSize = 0;
        for (FieldDescriptor field : clazz.getFieldDescriptors()) {
            ownFieldsSize += sizeOf(field);
        }
        return alignUpToX(calculateSizeRecursive(superClass) + ownFieldsSize, this.info.getIdentifierSize());
    }

    private int calculateClassSize(ClassImpl clazz) {
        int staticFieldsSize = 0;
        for (Field field : clazz.getStaticFields()) {
            staticFieldsSize += sizeOf(field);
        }
        return alignUpToX(staticFieldsSize, 8);
    }

    private int sizeOf(FieldDescriptor field) {
        int type = field.getType();
        if (type == 2) {
            return this.info.getIdentifierSize();
        }
        return IPrimitiveArray.ELEMENT_SIZE[type];
    }

    private int alignUpToX(int n, int x) {
        int r = n % x;
        return r == 0 ? n : (n + x) - r;
    }

    public IOne2LongIndex fillIn(IPreliminaryIndex index) throws IOException {
        ClassImpl clazz;
        for (ClassImpl clazz2 : (ClassImpl[]) this.classesByAddress.getAllValues(new ClassImpl[0])) {
            if (!(clazz2.getClassLoaderAddress() != 0 || clazz2.isArrayType() || this.gcRoots.containsKey(clazz2.getObjectAddress()))) {
                addGCRoot(clazz2.getObjectAddress(), 0, 2);
            }
        }
        HashMapIntObject<ClassImpl> classesById = new HashMapIntObject(this.classesByAddress.size());
        Iterator<ClassImpl> iter = this.classesByAddress.values();
        while (iter.hasNext()) {
            clazz2 = (ClassImpl) iter.next();
            classesById.put(clazz2.getObjectId(), clazz2);
        }
        index.setClassesById(classesById);
        index.setGcRoots(map2ids(this.gcRoots));
        HashMapIntObject<HashMapIntObject<List<XGCRootInfo>>> thread2objects2roots = new HashMapIntObject();
        Iterator<Entry<HashMapLongObject<List<XGCRootInfo>>>> iter2 = this.threadAddressToLocals.entries();
        while (iter2.hasNext()) {
            Entry<HashMapLongObject<List<XGCRootInfo>>> entry = (Entry) iter2.next();
            int threadId = this.identifiers.reverse(entry.getKey());
            if (threadId >= 0) {
                HashMapIntObject<List<XGCRootInfo>> objects2roots = map2ids((HashMapLongObject) entry.getValue());
                if (!objects2roots.isEmpty()) {
                    thread2objects2roots.put(threadId, objects2roots);
                }
            }
        }
        index.setThread2objects2roots(thread2objects2roots);
        index.setIdentifiers(this.identifiers);
        index.setArray2size(this.array2size.writeTo(Index.A2SIZE.getFile(this.info.getPrefix() + "temp.")));
        index.setObject2classId(this.object2classId);
        index.setOutbound(this.outbound.flush());
        return this.object2position.writeTo(new File(this.info.getPrefix() + "temp.o2hprof.index"));
    }

    private HashMapIntObject<List<XGCRootInfo>> map2ids(HashMapLongObject<List<XGCRootInfo>> source) {
        HashMapIntObject<List<XGCRootInfo>> sink = new HashMapIntObject();
        Iterator<Entry<List<XGCRootInfo>>> iter = source.entries();
        while (iter.hasNext()) {
            Entry<List<XGCRootInfo>> entry = (Entry) iter.next();
            int idx = this.identifiers.reverse(entry.getKey());
            if (idx >= 0) {
                Iterator<XGCRootInfo> roots = ((List) entry.getValue()).iterator();
                while (roots.hasNext()) {
                    XGCRootInfo root = (XGCRootInfo) roots.next();
                    root.setObjectId(idx);
                    if (root.getContextAddress() != 0) {
                        int contextId = this.identifiers.reverse(root.getContextAddress());
                        if (contextId < 0) {
                            roots.remove();
                        } else {
                            root.setContextId(contextId);
                        }
                    }
                }
                sink.put(idx, entry.getValue());
            }
        }
        return sink;
    }

    public void cancel() {
        if (this.constantPool != null) {
            this.constantPool.clear();
        }
        if (this.outbound != null) {
            this.outbound.cancel();
        }
    }

    public void addProperty(String name, String value) throws IOException {
        if (IHprofParserHandler.VERSION.equals(name)) {
            this.version = Version.valueOf(value);
            this.info.setProperty(HprofHeapObjectReader.VERSION_PROPERTY, this.version.name());
        } else if (IHprofParserHandler.IDENTIFIER_SIZE.equals(name)) {
            this.info.setIdentifierSize(Integer.parseInt(value));
        } else if (IHprofParserHandler.CREATION_DATE.equals(name)) {
            this.info.setCreationDate(new Date(Long.parseLong(value)));
        }
    }

    public void addGCRoot(long id, long referrer, int rootType) {
        if (referrer != 0) {
            HashMapLongObject localAddressToRootInfo = (HashMapLongObject) this.threadAddressToLocals.get(referrer);
            if (localAddressToRootInfo == null) {
                localAddressToRootInfo = new HashMapLongObject();
                this.threadAddressToLocals.put(referrer, localAddressToRootInfo);
            }
            List<XGCRootInfo> gcRootInfo = (List) localAddressToRootInfo.get(id);
            if (gcRootInfo == null) {
                gcRootInfo = new ArrayList(1);
                localAddressToRootInfo.put(id, gcRootInfo);
            }
            gcRootInfo.add(new XGCRootInfo(id, referrer, rootType));
            return;
        }
        List<XGCRootInfo> r = (List) this.gcRoots.get(id);
        if (r == null) {
            HashMapLongObject hashMapLongObject = this.gcRoots;
            r = new ArrayList(3);
            hashMapLongObject.put(id, r);
        }
        r.add(new XGCRootInfo(id, referrer, rootType));
    }

    public void addClass(ClassImpl clazz, long filePosition) throws IOException {
        this.identifiers.add(clazz.getObjectAddress());
        this.classesByAddress.put(clazz.getObjectAddress(), clazz);
        List<ClassImpl> list = (List) this.classesByName.get(clazz.getName());
        if (list == null) {
            Map map = this.classesByName;
            String name = clazz.getName();
            list = new ArrayList();
            map.put(name, list);
        }
        list.add(clazz);
    }

    public void addObject(HeapObject object, long filePosition) throws IOException {
        int index = object.objectId;
        HashMapLongObject<List<XGCRootInfo>> localVars = (HashMapLongObject) this.threadAddressToLocals.get(object.objectAddress);
        if (localVars != null) {
            IteratorLong e = localVars.keys();
            while (e.hasNext()) {
                object.references.add(e.next());
            }
        }
        this.outbound.log(this.identifiers, index, object.references);
        int classIndex = object.clazz.getObjectId();
        object.clazz.addInstance(object.usedHeapSize);
        this.object2classId.set(index, classIndex);
        this.object2position.set(index, filePosition);
        if (object.isArray) {
            this.array2size.set(index, object.usedHeapSize);
        }
    }

    public void reportInstance(long id, long filePosition) {
        this.identifiers.add(id);
    }

    public void reportRequiredObjectArray(long arrayClassID) {
        this.requiredArrayClassIDs.add(Long.valueOf(arrayClassID));
    }

    public void reportRequiredPrimitiveArray(int arrayType) {
        this.requiredPrimitiveArrays.add(Integer.valueOf(arrayType));
    }

    public int getIdentifierSize() {
        return this.info.getIdentifierSize();
    }

    public HashMapLongObject<String> getConstantPool() {
        return this.constantPool;
    }

    public ClassImpl lookupClass(long classId) {
        return (ClassImpl) this.classesByAddress.get(classId);
    }

    public IClass lookupClassByName(String name, boolean failOnMultipleInstances) {
        List<ClassImpl> list = (List) this.classesByName.get(name);
        if (list == null) {
            return null;
        }
        if (!failOnMultipleInstances || list.size() == 1) {
            return (IClass) list.get(0);
        }
        throw new RuntimeException(MessageUtil.format(Messages.HprofParserHandlerImpl_Error_MultipleClassInstancesExist, name));
    }

    public IClass lookupClassByIndex(int objIndex) {
        return lookupClass(this.identifiers.get(objIndex));
    }

    public List<IClass> resolveClassHierarchy(long classId) {
        List<IClass> answer = new ArrayList();
        ClassImpl clazz = (ClassImpl) this.classesByAddress.get(classId);
        answer.add(clazz);
        while (clazz.hasSuperClass()) {
            clazz = (ClassImpl) this.classesByAddress.get(clazz.getSuperClassAddress());
            answer.add(clazz);
        }
        return answer;
    }

    public int mapAddressToId(long address) {
        return this.identifiers.reverse(address);
    }

    public XSnapshotInfo getSnapshotInfo() {
        return this.info;
    }
}
