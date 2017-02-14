package com.meiqia.meiqiasdk.model;

public class EvaluateMessage extends BaseMessage {
    public static final int EVALUATE_BAD    = 0;
    public static final int EVALUATE_GOOD   = 2;
    public static final int EVALUATE_MEDIUM = 1;
    private int level;

    public EvaluateMessage(int level, String content) {
        this.level = level;
        setContent(content);
        setItemViewType(4);
    }

    public int getLevel() {
        return this.level;
    }
}
