package com.facebook.rebound;

public class SteppingLooper extends SpringLooper {
    private long    mLastTime;
    private boolean mStarted;

    public void start() {
        this.mStarted = true;
        this.mLastTime = 0;
    }

    public boolean step(long interval) {
        if (this.mSpringSystem == null || !this.mStarted) {
            return false;
        }
        long currentTime = this.mLastTime + interval;
        this.mSpringSystem.loop((double) currentTime);
        this.mLastTime = currentTime;
        return this.mSpringSystem.getIsIdle();
    }

    public void stop() {
        this.mStarted = false;
    }
}
