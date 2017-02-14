package com.tencent.tinker.android.dex;

import com.tencent.tinker.android.dex.TableOfContents.Section.Item;
import com.tencent.tinker.android.dex.util.CompareUtils;

public final class ClassData extends Item<ClassData> {
    public Method[] directMethods;
    public Field[]  instanceFields;
    public Field[]  staticFields;
    public Method[] virtualMethods;

    public static class Field implements Comparable<Field> {
        public int accessFlags;
        public int fieldIndex;

        public Field(int fieldIndex, int accessFlags) {
            this.fieldIndex = fieldIndex;
            this.accessFlags = accessFlags;
        }

        public int compareTo(Field other) {
            int res = CompareUtils.uCompare(this.fieldIndex, other.fieldIndex);
            return res != 0 ? res : CompareUtils.sCompare(this.accessFlags, other.accessFlags);
        }
    }

    public static class Method implements Comparable<Method> {
        public int accessFlags;
        public int codeOffset;
        public int methodIndex;

        public Method(int methodIndex, int accessFlags, int codeOffset) {
            this.methodIndex = methodIndex;
            this.accessFlags = accessFlags;
            this.codeOffset = codeOffset;
        }

        public int compareTo(Method other) {
            int res = CompareUtils.uCompare(this.methodIndex, other.methodIndex);
            if (res != 0) {
                return res;
            }
            res = CompareUtils.sCompare(this.accessFlags, other.accessFlags);
            if (res != 0) {
                return res;
            }
            return CompareUtils.sCompare(this.codeOffset, other.codeOffset);
        }
    }

    public ClassData(int off, Field[] staticFields, Field[] instanceFields, Method[]
            directMethods, Method[] virtualMethods) {
        super(off);
        this.staticFields = staticFields;
        this.instanceFields = instanceFields;
        this.directMethods = directMethods;
        this.virtualMethods = virtualMethods;
    }

    public int compareTo(ClassData other) {
        int res = CompareUtils.aArrCompare(this.staticFields, other.staticFields);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.aArrCompare(this.instanceFields, other.instanceFields);
        if (res != 0) {
            return res;
        }
        res = CompareUtils.aArrCompare(this.directMethods, other.directMethods);
        if (res != 0) {
            return res;
        }
        return CompareUtils.aArrCompare(this.virtualMethods, other.virtualMethods);
    }

    public int byteCountInDex() {
        return ((((((Leb128.unsignedLeb128Size(this.staticFields.length) + Leb128
                .unsignedLeb128Size(this.instanceFields.length)) + Leb128.unsignedLeb128Size(this
                .directMethods.length)) + Leb128.unsignedLeb128Size(this.virtualMethods.length))
                + calcFieldsSize(this.staticFields)) + calcFieldsSize(this.instanceFields)) +
                calcMethodsSize(this.directMethods)) + calcMethodsSize(this.virtualMethods);
    }

    private int calcFieldsSize(Field[] fields) {
        int res = 0;
        int prevFieldIndex = 0;
        for (Field field : fields) {
            int fieldIndexDelta = field.fieldIndex - prevFieldIndex;
            prevFieldIndex = field.fieldIndex;
            res += Leb128.unsignedLeb128Size(fieldIndexDelta) + Leb128.unsignedLeb128Size(field
                    .accessFlags);
        }
        return res;
    }

    private int calcMethodsSize(Method[] methods) {
        int res = 0;
        int prevMethodIndex = 0;
        for (Method method : methods) {
            int methodIndexDelta = method.methodIndex - prevMethodIndex;
            prevMethodIndex = method.methodIndex;
            res += (Leb128.unsignedLeb128Size(methodIndexDelta) + Leb128.unsignedLeb128Size
                    (method.accessFlags)) + Leb128.unsignedLeb128Size(method.codeOffset);
        }
        return res;
    }
}
