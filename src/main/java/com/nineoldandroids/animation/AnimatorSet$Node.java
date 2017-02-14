package com.nineoldandroids.animation;

import java.util.ArrayList;

class AnimatorSet$Node implements Cloneable {
    public Animator animation;
    public ArrayList<AnimatorSet$Dependency> dependencies     = null;
    public boolean                           done             = false;
    public ArrayList<AnimatorSet$Node>       nodeDependencies = null;
    public ArrayList<AnimatorSet$Node>       nodeDependents   = null;
    public ArrayList<AnimatorSet$Dependency> tmpDependencies  = null;

    public AnimatorSet$Node(Animator animation) {
        this.animation = animation;
    }

    public void addDependency(AnimatorSet$Dependency dependency) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList();
            this.nodeDependencies = new ArrayList();
        }
        this.dependencies.add(dependency);
        if (!this.nodeDependencies.contains(dependency.node)) {
            this.nodeDependencies.add(dependency.node);
        }
        AnimatorSet$Node dependencyNode = dependency.node;
        if (dependencyNode.nodeDependents == null) {
            dependencyNode.nodeDependents = new ArrayList();
        }
        dependencyNode.nodeDependents.add(this);
    }

    public AnimatorSet$Node clone() {
        try {
            AnimatorSet$Node node = (AnimatorSet$Node) super.clone();
            node.animation = this.animation.clone();
            return node;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
