package com.nineoldandroids.animation;

class AnimatorSet$Dependency {
    static final int AFTER = 1;
    static final int WITH  = 0;
    public AnimatorSet$Node node;
    public int              rule;

    public AnimatorSet$Dependency(AnimatorSet$Node node, int rule) {
        this.node = node;
        this.rule = rule;
    }
}
