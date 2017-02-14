package com.nineoldandroids.animation;

import com.nineoldandroids.animation.Animator.AnimatorListener;

import java.util.ArrayList;

class AnimatorSet$AnimatorSetListener implements AnimatorListener {
    private               AnimatorSet mAnimatorSet;
    final /* synthetic */ AnimatorSet this$0;

    AnimatorSet$AnimatorSetListener(AnimatorSet animatorSet, AnimatorSet animatorSet2) {
        this.this$0 = animatorSet;
        this.mAnimatorSet = animatorSet2;
    }

    public void onAnimationCancel(Animator animation) {
        if (!this.this$0.mTerminated && AnimatorSet.access$000(this.this$0).size() == 0 && this
                .this$0.mListeners != null) {
            int numListeners = this.this$0.mListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) this.this$0.mListeners.get(i)).onAnimationCancel(this
                        .mAnimatorSet);
            }
        }
    }

    public void onAnimationEnd(Animator animation) {
        animation.removeListener(this);
        AnimatorSet.access$000(this.this$0).remove(animation);
        ((AnimatorSet$Node) AnimatorSet.access$100(this.mAnimatorSet).get(animation)).done = true;
        if (!this.this$0.mTerminated) {
            int i;
            ArrayList<AnimatorSet$Node> sortedNodes = AnimatorSet.access$200(this.mAnimatorSet);
            boolean allDone = true;
            int numSortedNodes = sortedNodes.size();
            for (i = 0; i < numSortedNodes; i++) {
                if (!((AnimatorSet$Node) sortedNodes.get(i)).done) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                if (this.this$0.mListeners != null) {
                    ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.this$0.mListeners
                            .clone();
                    int numListeners = tmpListeners.size();
                    for (i = 0; i < numListeners; i++) {
                        ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this.mAnimatorSet);
                    }
                }
                AnimatorSet.access$302(this.mAnimatorSet, false);
            }
        }
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
    }
}
