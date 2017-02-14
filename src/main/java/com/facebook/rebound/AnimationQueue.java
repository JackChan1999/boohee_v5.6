package com.facebook.rebound;

import com.facebook.rebound.ChoreographerCompat.FrameCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AnimationQueue {
    private final Queue<Double>       mAnimationQueue        = new LinkedList();
    private final List<Callback>      mCallbacks             = new ArrayList();
    private final ChoreographerCompat mChoreographer         = ChoreographerCompat.getInstance();
    private final FrameCallback       mChoreographerCallback = new FrameCallback() {
        public void doFrame(long frameTimeNanos) {
            AnimationQueue.this.onFrame(frameTimeNanos);
        }
    };
    private final Queue<Double>       mPendingQueue          = new LinkedList();
    private boolean mRunning;
    private final ArrayList<Double> mTempValues = new ArrayList();

    public interface Callback {
        void onFrame(Double d);
    }

    public void addValue(Double value) {
        this.mPendingQueue.add(value);
        runIfIdle();
    }

    public void addAllValues(Collection<Double> values) {
        this.mPendingQueue.addAll(values);
        runIfIdle();
    }

    public void clearValues() {
        this.mPendingQueue.clear();
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public void clearCallbacks() {
        this.mCallbacks.clear();
    }

    private void runIfIdle() {
        if (!this.mRunning) {
            this.mRunning = true;
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }
    }

    private void onFrame(long frameTimeNanos) {
        int drainingOffset;
        Double nextPendingValue = (Double) this.mPendingQueue.poll();
        if (nextPendingValue != null) {
            this.mAnimationQueue.offer(nextPendingValue);
            drainingOffset = 0;
        } else {
            drainingOffset = Math.max(this.mCallbacks.size() - this.mAnimationQueue.size(), 0);
        }
        this.mTempValues.addAll(this.mAnimationQueue);
        for (int i = this.mTempValues.size() - 1; i > -1; i--) {
            Double val = (Double) this.mTempValues.get(i);
            int cbIdx = ((this.mTempValues.size() - 1) - i) + drainingOffset;
            if (this.mCallbacks.size() > cbIdx) {
                ((Callback) this.mCallbacks.get(cbIdx)).onFrame(val);
            }
        }
        this.mTempValues.clear();
        while (this.mAnimationQueue.size() + drainingOffset >= this.mCallbacks.size()) {
            this.mAnimationQueue.poll();
        }
        if (this.mAnimationQueue.isEmpty() && this.mPendingQueue.isEmpty()) {
            this.mRunning = false;
        } else {
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }
    }
}
