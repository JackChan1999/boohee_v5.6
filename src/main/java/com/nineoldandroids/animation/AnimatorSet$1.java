package com.nineoldandroids.animation;

import java.util.ArrayList;

class AnimatorSet$1 extends AnimatorListenerAdapter {
    boolean canceled = false;
    final /* synthetic */ AnimatorSet this$0;
    final /* synthetic */ ArrayList   val$nodesToStart;

    AnimatorSet$1(AnimatorSet animatorSet, ArrayList arrayList) {
        this.this$0 = animatorSet;
        this.val$nodesToStart = arrayList;
    }

    public void onAnimationCancel(Animator anim) {
        this.canceled = true;
    }

    public void onAnimationEnd(Animator anim) {
        if (!this.canceled) {
            int numNodes = this.val$nodesToStart.size();
            for (int i = 0; i < numNodes; i++) {
                AnimatorSet$Node node = (AnimatorSet$Node) this.val$nodesToStart.get(i);
                node.animation.start();
                AnimatorSet.access$000(this.this$0).add(node.animation);
            }
        }
    }
}
