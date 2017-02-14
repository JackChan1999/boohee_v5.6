package org.eclipse.mat.hprof;

import java.lang.ref.SoftReference;

class ArrayDescription {

    static class Offline extends ArrayDescription {
        int arraySize;
        int elementSize;
        boolean isPrimitive;
        SoftReference<Object> lazyReadContent = new SoftReference(null);
        long position;

        public Offline(boolean isPrimitive, long position, int elementSize, int arraySize) {
            this.isPrimitive = isPrimitive;
            this.position = position;
            this.elementSize = elementSize;
            this.arraySize = arraySize;
        }

        public boolean isPrimitive() {
            return this.isPrimitive;
        }

        public long getPosition() {
            return this.position;
        }

        public int getArraySize() {
            return this.arraySize;
        }

        public int getElementSize() {
            return this.elementSize;
        }

        public Object getLazyReadContent() {
            return this.lazyReadContent.get();
        }

        public void setLazyReadContent(Object content) {
            this.lazyReadContent = new SoftReference(content);
        }
    }

    static class Raw extends ArrayDescription {
        byte[] content;

        public Raw(byte[] content) {
            this.content = content;
        }

        public byte[] getContent() {
            return this.content;
        }
    }

    ArrayDescription() {
    }
}
