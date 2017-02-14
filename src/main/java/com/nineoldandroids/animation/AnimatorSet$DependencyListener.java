package com.nineoldandroids.animation;

import com.nineoldandroids.animation.Animator.AnimatorListener;

class AnimatorSet$DependencyListener implements AnimatorListener {
    private AnimatorSet      mAnimatorSet;
    private AnimatorSet$Node mNode;
    private int              mRule;

    public AnimatorSet$DependencyListener(AnimatorSet animatorSet, AnimatorSet$Node node, int
            rule) {
        this.mAnimatorSet = animatorSet;
        this.mNode = node;
        this.mRule = rule;
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        if (this.mRule == 1) {
            startIfReady(animation);
        }
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
        if (this.mRule == 0) {
            startIfReady(animation);
        }
    }

    private void startIfReady(Animator dependencyAnimation) {
        if (!this.mAnimatorSet.mTerminated) {
            AnimatorSet$Dependency dependencyToRemove = null;
            int numDependencies = this.mNode.tmpDependencies.size();
            for (int i = 0; i < numDependencies; i++) {
                AnimatorSet$Dependency dependency = (AnimatorSet$Dependency) this.mNode
                        .tmpDependencies.get(i);
                if (dependency.rule == this.mRule && dependency.node.animation ==
                        dependencyAnimation) {
                    dependencyToRemove = dependency;
                    dependencyAnimation.removeListener(this);
                    break;
                }
            }
            this.mNode.tmpDependencies.remove(dependencyToRemove);
            if (this.mNode.tmpDependencies.size() == 0) {
                this.mNode.animation.start();
                AnimatorSet.access$000(this.mAnimatorSet).add(this.mNode.animation);
            }
        }
    }
}
