package com.tencent.tinker.commons.dexpatcher.struct;

public final class PatchOperation<T> {
    public static final int OP_ADD     = 1;
    public static final int OP_DEL     = 0;
    public static final int OP_REPLACE = 2;
    public int index;
    public T   newItem;
    public int op;

    public PatchOperation(int op, int index) {
        this(op, index, null);
    }

    public PatchOperation(int op, int index, T newItem) {
        this.op = op;
        this.index = index;
        this.newItem = newItem;
    }

    public static String translateOpToString(int op) {
        switch (op) {
            case 0:
                return "OP_DEL";
            case 1:
                return "OP_ADD";
            case 2:
                return "OP_REPLACE";
            default:
                return "OP_UNKNOWN";
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String opDesc = translateOpToString(this.op);
        sb.append('{');
        sb.append("op: ").append(opDesc).append(", index: ").append(this.index).append(", " +
                "newItem: ").append(this.newItem);
        sb.append('}');
        return sb.toString();
    }
}
