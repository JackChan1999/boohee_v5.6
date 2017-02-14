package org.eclipse.mat.parser.internal;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.eclipse.mat.SnapshotException;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.IIndexBuilder;
import org.eclipse.mat.parser.internal.util.ParserRegistry;
import org.eclipse.mat.parser.internal.util.ParserRegistry.Parser;
import org.eclipse.mat.parser.model.XSnapshotInfo;
import org.eclipse.mat.snapshot.ISnapshot;
import org.eclipse.mat.util.IProgressListener;
import org.eclipse.mat.util.MessageUtil;

public class SnapshotFactory {
    private Map<File, SnapshotEntry> snapshotCache = new HashMap();

    private static class SnapshotEntry {
        private WeakReference<ISnapshot> snapshot;
        private int usageCount;

        public SnapshotEntry(int usageCount, ISnapshot snapshot) {
            this.usageCount = usageCount;
            this.snapshot = new WeakReference(snapshot);
        }
    }

    public ISnapshot openSnapshot(File file, Map<String, String> args, IProgressListener listener) throws SnapshotException {
        ISnapshot answer;
        SnapshotEntry entry = (SnapshotEntry) this.snapshotCache.get(file);
        if (entry != null) {
            answer = (ISnapshot) entry.snapshot.get();
            if (answer != null) {
                entry.usageCount = entry.usageCount + 1;
                return answer;
            }
        }
        String name = file.getAbsolutePath();
        int p = name.lastIndexOf(46);
        String prefix = p >= 0 ? name.substring(0, p + 1) : name + ".";
        deleteIndexFiles(file);
        answer = parse(file, prefix, args, listener);
        this.snapshotCache.put(file, new SnapshotEntry(1, answer));
        return answer;
    }

    public synchronized void dispose(ISnapshot snapshot) {
        Iterator<SnapshotEntry> iter = this.snapshotCache.values().iterator();
        while (iter.hasNext()) {
            SnapshotEntry entry = (SnapshotEntry) iter.next();
            ISnapshot s = (ISnapshot) entry.snapshot.get();
            if (s == null) {
                iter.remove();
            } else if (s == snapshot) {
                entry.usageCount = entry.usageCount - 1;
                if (entry.usageCount == 0) {
                    snapshot.dispose();
                    iter.remove();
                }
            }
        }
        if (snapshot != null) {
            snapshot.dispose();
        }
    }

    private ISnapshot parse(File file, String prefix, Map<String, String> args, IProgressListener listener) throws SnapshotException {
        List<Parser> parsers = ParserRegistry.matchParser(file.getName());
        if (parsers.isEmpty()) {
            parsers.addAll(ParserRegistry.allParsers());
        }
        List<IOException> errors = new ArrayList();
        for (Parser parser : parsers) {
            IIndexBuilder indexBuilder = parser.getIndexBuilder();
            if (indexBuilder != null) {
                try {
                    indexBuilder.init(file, prefix);
                    XSnapshotInfo snapshotInfo = new XSnapshotInfo();
                    snapshotInfo.setPath(file.getAbsolutePath());
                    snapshotInfo.setPrefix(prefix);
                    snapshotInfo.setProperty("$heapFormat", parser.getId());
                    if (Boolean.parseBoolean((String) args.get("keep_unreachable_objects"))) {
                        snapshotInfo.setProperty("keep_unreachable_objects", Integer.valueOf(2048));
                    }
                    PreliminaryIndexImpl idx = new PreliminaryIndexImpl(snapshotInfo);
                    indexBuilder.fill(idx, listener);
                    SnapshotImplBuilder builder = new SnapshotImplBuilder(idx.getSnapshotInfo());
                    indexBuilder.clean(GarbageCleaner.clean(idx, builder, args, listener), listener);
                    SnapshotImpl snapshot = builder.create(parser);
                    snapshot.calculateDominatorTree(listener);
                    return snapshot;
                } catch (IOException ioe) {
                    errors.add(ioe);
                    indexBuilder.cancel();
                } catch (Exception e) {
                    indexBuilder.cancel();
                    throw SnapshotException.rethrow(e);
                }
            }
        }
        throw new SnapshotException(MessageUtil.format(Messages.SnapshotFactoryImpl_Error_NoParserRegistered, file.getName()));
    }

    private void deleteIndexFiles(File file) {
        String fragment;
        File directory = file.getParentFile();
        if (directory == null) {
            directory = new File(".");
        }
        String filename = file.getName();
        int p = filename.lastIndexOf(46);
        if (p >= 0) {
            fragment = filename.substring(0, p);
        } else {
            fragment = filename;
        }
        final Pattern indexPattern = Pattern.compile("\\.(.*\\.)?index$");
        final Pattern logPattern = Pattern.compile("\\.inbound\\.index.*\\.log$");
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return false;
                }
                String name = f.getName();
                if (!name.startsWith(fragment)) {
                    return false;
                }
                if (indexPattern.matcher(name).matches() || logPattern.matcher(name).matches()) {
                    return true;
                }
                return false;
            }
        });
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
