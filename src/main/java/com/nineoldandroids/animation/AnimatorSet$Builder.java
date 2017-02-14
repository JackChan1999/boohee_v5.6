package com.nineoldandroids.animation;

public class AnimatorSet$Builder {
    private               AnimatorSet$Node mCurrentNode;
    final /* synthetic */ AnimatorSet      this$0;

    AnimatorSet$Builder(AnimatorSet animatorSet, Animator anim) {
        this.this$0 = animatorSet;
        this.mCurrentNode = (AnimatorSet$Node) AnimatorSet.access$100(animatorSet).get(anim);
        if (this.mCurrentNode == null) {
            this.mCurrentNode = new AnimatorSet$Node(anim);
            AnimatorSet.access$100(animatorSet).put(anim, this.mCurrentNode);
            AnimatorSet.access$400(animatorSet).add(this.mCurrentNode);
        }
    }

    public AnimatorSet$Builder with(Animator anim) {
        AnimatorSet$Node node = (AnimatorSet$Node) AnimatorSet.access$100(this.this$0).get(anim);
        if (node == null) {
            node = new AnimatorSet$Node(anim);
            AnimatorSet.access$100(this.this$0).put(anim, node);
            AnimatorSet.access$400(this.this$0).add(node);
        }
        node.addDependency(new AnimatorSet$Dependency(this.mCurrentNode, 0));
        return this;
    }

    public AnimatorSet$Builder before(Animator anim) {
        AnimatorSet$Node node = (AnimatorSet$Node) AnimatorSet.access$100(this.this$0).get(anim);
        if (node == null) {
            node = new AnimatorSet$Node(anim);
            AnimatorSet.access$100(this.this$0).put(anim, node);
            AnimatorSet.access$400(this.this$0).add(node);
        }
        node.addDependency(new AnimatorSet$Dependency(this.mCurrentNode, 1));
        return this;
    }

    public AnimatorSet$Builder after(Animator anim) {
        AnimatorSet$Node node = (AnimatorSet$Node) AnimatorSet.access$100(this.this$0).get(anim);
        if (node == null) {
            node = new AnimatorSet$Node(anim);
            AnimatorSet.access$100(this.this$0).put(anim, node);
            AnimatorSet.access$400(this.this$0).add(node);
        }
        this.mCurrentNode.addDependency(new AnimatorSet$Dependency(node, 1));
        return this;
    }

    public AnimatorSet$Builder after(long delay) {
        Animator anim = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        anim.setDuration(delay);
        after(anim);
        return this;
    }
}
