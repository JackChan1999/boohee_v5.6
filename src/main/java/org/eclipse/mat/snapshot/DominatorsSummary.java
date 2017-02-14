package org.eclipse.mat.snapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.eclipse.mat.collect.SetInt;

public final class DominatorsSummary {
    public static final Comparator<Object> COMPARE_BY_DOMINATED = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            int c1;
            int c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatedCount();
                c2 = ((ClassDominatorRecord) o2).getDominatedCount();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatedCount();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatedCount();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_DOMINATED_HEAP_SIZE = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            long c1;
            long c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatedNetSize();
                c2 = ((ClassDominatorRecord) o2).getDominatedNetSize();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatedNetSize();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatedNetSize();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_DOMINATED_RETAINED_HEAP_SIZE = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            long c1;
            long c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatedRetainedSize();
                c2 = ((ClassDominatorRecord) o2).getDominatedRetainedSize();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatedRetainedSize();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatedRetainedSize();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_DOMINATORS = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            int c1;
            int c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatorCount();
                c2 = ((ClassDominatorRecord) o2).getDominatorCount();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatorCount();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatorCount();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_DOMINATOR_HEAP_SIZE = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            long c1;
            long c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatorNetSize();
                c2 = ((ClassDominatorRecord) o2).getDominatorNetSize();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatorNetSize();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatorNetSize();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_DOMINATOR_RETAINED_HEAP_SIZE = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            long c1;
            long c2;
            if (o1 instanceof ClassDominatorRecord) {
                c1 = ((ClassDominatorRecord) o1).getDominatorRetainedSize();
                c2 = ((ClassDominatorRecord) o2).getDominatorRetainedSize();
            } else {
                c1 = ((ClassloaderDominatorRecord) o1).getDominatorRetainedSize();
                c2 = ((ClassloaderDominatorRecord) o2).getDominatorRetainedSize();
            }
            if (c1 > c2) {
                return 1;
            }
            return c1 == c2 ? 0 : -1;
        }
    };
    public static final Comparator<Object> COMPARE_BY_NAME = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof ClassDominatorRecord) {
                return ((ClassDominatorRecord) o1).getClassName().compareTo(((ClassDominatorRecord) o2).getClassName());
            }
            return ((ClassloaderDominatorRecord) o1).getName().compareTo(((ClassloaderDominatorRecord) o2).getName());
        }
    };
    private ClassDominatorRecord[] classDominatorRecords;
    private ClassloaderDominatorRecord[] classloaderDominatorRecords;
    private Object data;
    private ISnapshot snapshot;

    public static class ClassDominatorRecord {
        int classId;
        String className;
        int classloaderId;
        SetInt dominated = new SetInt(500);
        long dominatedNetSize;
        long dominatedRetainedSize;
        SetInt dominator = new SetInt(500);
        long dominatorNetSize;
        long dominatorRetainedSize;
        DominatorsSummary summary;

        public String getClassName() {
            return this.className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getClassId() {
            return this.classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public long getDominatedNetSize() {
            return this.dominatedNetSize;
        }

        public long getDominatorNetSize() {
            return this.dominatorNetSize;
        }

        public long getDominatedRetainedSize() {
            return this.dominatedRetainedSize;
        }

        public void setDominatedRetainedSize(long dominatedRetainedSize) {
            this.dominatedRetainedSize = dominatedRetainedSize;
        }

        public long getDominatorRetainedSize() {
            return this.dominatorRetainedSize;
        }

        public void setDominatorRetainedSize(long dominatorRetainedSize) {
            this.dominatorRetainedSize = dominatorRetainedSize;
        }

        public int getDominatedCount() {
            return this.dominated.size();
        }

        public int getDominatorCount() {
            return this.dominator.size();
        }

        public int getClassloaderId() {
            return this.classloaderId;
        }

        public void setClassloaderId(int classloaderId) {
            this.classloaderId = classloaderId;
        }

        public boolean addDominated(int objectId) {
            return this.dominated.add(objectId);
        }

        public boolean addDominator(int objectId) {
            return this.dominator.add(objectId);
        }

        public void addDominatedNetSize(long size) {
            this.dominatedNetSize += size;
        }

        public void addDominatorNetSize(long size) {
            this.dominatorNetSize += size;
        }

        public int[] getDominated() {
            return this.dominated.toArray();
        }

        public int[] getDominators() {
            return this.dominator.toArray();
        }

        public DominatorsSummary getSummary() {
            return this.summary;
        }
    }

    public static class ClassloaderDominatorRecord {
        int dominated;
        long dominatedNetSize;
        long dominatedRetainedSize;
        int dominator;
        long dominatorNetSize;
        long dominatorRetainedSize;
        protected int id;
        protected String name;
        protected List<ClassDominatorRecord> records = new ArrayList();

        public String getName() {
            return this.name;
        }

        public long getDominatedNetSize() {
            return this.dominatedNetSize;
        }

        public int getDominatedCount() {
            return this.dominated;
        }

        public int getDominatorCount() {
            return this.dominator;
        }

        public long getDominatorNetSize() {
            return this.dominatorNetSize;
        }

        public List<ClassDominatorRecord> getRecords() {
            return this.records;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getDominatedRetainedSize() {
            return this.dominatedRetainedSize;
        }

        public void setDominatedRetainedSize(long dominatedRetainedSize) {
            this.dominatedRetainedSize = dominatedRetainedSize;
        }

        public long getDominatorRetainedSize() {
            return this.dominatorRetainedSize;
        }

        public void setDominatorRetainedSize(long dominatorRetainedSize) {
            this.dominatorRetainedSize = dominatorRetainedSize;
        }
    }

    public DominatorsSummary(ClassDominatorRecord[] classDominatorRecords, ISnapshot snapshot) {
        this.classDominatorRecords = classDominatorRecords;
        this.snapshot = snapshot;
        for (ClassDominatorRecord record : classDominatorRecords) {
            record.summary = this;
        }
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ClassDominatorRecord[] getClassDominatorRecords() {
        return this.classDominatorRecords;
    }

    public ClassloaderDominatorRecord[] getClassloaderDominatorRecords() {
        return getClassloaderDominatorRecords(ClassloaderDominatorRecord.class);
    }

    public <C extends ClassloaderDominatorRecord> C[] getClassloaderDominatorRecords(Class<C> factoryClass) {
        synchronized (this) {
            if (this.classloaderDominatorRecords == null) {
                this.classloaderDominatorRecords = load(factoryClass);
            }
        }
        return this.classloaderDominatorRecords;
    }

    private org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord[] load(java.lang.Class<org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord> r13) {
        /* JADX: method processing error */
/*
Error: java.lang.IndexOutOfBoundsException: bitIndex < 0: -1
	at java.util.BitSet.get(BitSet.java:623)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.usedArgAssign(CodeShrinker.java:138)
	at jadx.core.dex.visitors.CodeShrinker$ArgsInfo.access$300(CodeShrinker.java:43)
	at jadx.core.dex.visitors.CodeShrinker.canMoveBetweenBlocks(CodeShrinker.java:282)
	at jadx.core.dex.visitors.CodeShrinker.shrinkBlock(CodeShrinker.java:230)
	at jadx.core.dex.visitors.CodeShrinker.shrinkMethod(CodeShrinker.java:38)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkArrayForEach(LoopRegionVisitor.java:196)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.checkForIndexedLoop(LoopRegionVisitor.java:119)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.processLoopRegion(LoopRegionVisitor.java:65)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.enterRegion(LoopRegionVisitor.java:52)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:56)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:58)
	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:18)
	at jadx.core.dex.visitors.regions.LoopRegionVisitor.visit(LoopRegionVisitor.java:46)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r12 = this;
        r5 = new java.util.HashMap;	 Catch:{ Exception -> 0x0086 }
        r5.<init>();	 Catch:{ Exception -> 0x0086 }
        r0 = r12.classDominatorRecords;	 Catch:{ Exception -> 0x0086 }
        r4 = r0.length;	 Catch:{ Exception -> 0x0086 }
        r3 = 0;	 Catch:{ Exception -> 0x0086 }
    L_0x0009:
        if (r3 >= r4) goto L_0x008d;	 Catch:{ Exception -> 0x0086 }
    L_0x000b:
        r7 = r0[r3];	 Catch:{ Exception -> 0x0086 }
        r8 = r7.getClassloaderId();	 Catch:{ Exception -> 0x0086 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x0086 }
        r1 = r5.get(r8);	 Catch:{ Exception -> 0x0086 }
        r1 = (org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord) r1;	 Catch:{ Exception -> 0x0086 }
        if (r1 != 0) goto L_0x0041;	 Catch:{ Exception -> 0x0086 }
    L_0x001d:
        r8 = r7.getClassloaderId();	 Catch:{ Exception -> 0x0086 }
        r8 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x0086 }
        r1 = r13.newInstance();	 Catch:{ Exception -> 0x0086 }
        r1 = (org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord) r1;	 Catch:{ Exception -> 0x0086 }
        r5.put(r8, r1);	 Catch:{ Exception -> 0x0086 }
        r8 = r7.getClassloaderId();	 Catch:{ Exception -> 0x0086 }
        r1.setId(r8);	 Catch:{ Exception -> 0x0086 }
        r8 = r1.getId();	 Catch:{ Exception -> 0x0086 }
        r9 = -1;	 Catch:{ Exception -> 0x0086 }
        if (r8 != r9) goto L_0x006d;	 Catch:{ Exception -> 0x0086 }
    L_0x003c:
        r8 = "<ROOT>";	 Catch:{ Exception -> 0x0086 }
        r1.name = r8;	 Catch:{ Exception -> 0x0086 }
    L_0x0041:
        r8 = r1.dominated;	 Catch:{ Exception -> 0x0086 }
        r9 = r7.getDominatedCount();	 Catch:{ Exception -> 0x0086 }
        r8 = r8 + r9;	 Catch:{ Exception -> 0x0086 }
        r1.dominated = r8;	 Catch:{ Exception -> 0x0086 }
        r8 = r1.dominator;	 Catch:{ Exception -> 0x0086 }
        r9 = r7.getDominatorCount();	 Catch:{ Exception -> 0x0086 }
        r8 = r8 + r9;	 Catch:{ Exception -> 0x0086 }
        r1.dominator = r8;	 Catch:{ Exception -> 0x0086 }
        r8 = r1.dominatedNetSize;	 Catch:{ Exception -> 0x0086 }
        r10 = r7.getDominatedNetSize();	 Catch:{ Exception -> 0x0086 }
        r8 = r8 + r10;	 Catch:{ Exception -> 0x0086 }
        r1.dominatedNetSize = r8;	 Catch:{ Exception -> 0x0086 }
        r8 = r1.dominatorNetSize;	 Catch:{ Exception -> 0x0086 }
        r10 = r7.getDominatorNetSize();	 Catch:{ Exception -> 0x0086 }
        r8 = r8 + r10;	 Catch:{ Exception -> 0x0086 }
        r1.dominatorNetSize = r8;	 Catch:{ Exception -> 0x0086 }
        r8 = r1.records;	 Catch:{ Exception -> 0x0086 }
        r8.add(r7);	 Catch:{ Exception -> 0x0086 }
        r3 = r3 + 1;	 Catch:{ Exception -> 0x0086 }
        goto L_0x0009;	 Catch:{ Exception -> 0x0086 }
    L_0x006d:
        r8 = r12.snapshot;	 Catch:{ Exception -> 0x0086 }
        r9 = r1.id;	 Catch:{ Exception -> 0x0086 }
        r6 = r8.getObject(r9);	 Catch:{ Exception -> 0x0086 }
        r8 = r6.getClassSpecificName();	 Catch:{ Exception -> 0x0086 }
        r1.name = r8;	 Catch:{ Exception -> 0x0086 }
        r8 = r1.name;	 Catch:{ Exception -> 0x0086 }
        if (r8 != 0) goto L_0x0041;	 Catch:{ Exception -> 0x0086 }
    L_0x007f:
        r8 = r6.getTechnicalName();	 Catch:{ Exception -> 0x0086 }
        r1.name = r8;	 Catch:{ Exception -> 0x0086 }
        goto L_0x0041;
    L_0x0086:
        r2 = move-exception;
        r8 = new java.lang.RuntimeException;
        r8.<init>(r2);
        throw r8;
    L_0x008d:
        r8 = r5.values();	 Catch:{ Exception -> 0x0086 }
        r9 = r5.size();	 Catch:{ Exception -> 0x0086 }
        r9 = new org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord[r9];	 Catch:{ Exception -> 0x0086 }
        r8 = r8.toArray(r9);	 Catch:{ Exception -> 0x0086 }
        r8 = (org.eclipse.mat.snapshot.DominatorsSummary.ClassloaderDominatorRecord[]) r8;	 Catch:{ Exception -> 0x0086 }
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.mat.snapshot.DominatorsSummary.load(java.lang.Class):org.eclipse.mat.snapshot.DominatorsSummary$ClassloaderDominatorRecord[]");
    }

    public static Comparator<Object> reverseComparator(final Comparator<Object> comparator) {
        return new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return comparator.compare(o2, o1);
            }
        };
    }
}
