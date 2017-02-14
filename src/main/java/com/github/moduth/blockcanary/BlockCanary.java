package com.github.moduth.blockcanary;

import android.content.Context;
import android.util.Log;

public final class BlockCanary {
    private static final String      TAG       = "BlockCanary-no-op";
    private static       BlockCanary sInstance = null;

    private BlockCanary() {
    }

    public static BlockCanary install(Context context, BlockCanaryContext blockCanaryContext) {
        BlockCanaryContext.init(context, blockCanaryContext);
        return get();
    }

    public static BlockCanary get() {
        if (sInstance == null) {
            synchronized (BlockCanary.class) {
                if (sInstance == null) {
                    sInstance = new BlockCanary();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        Log.i(TAG, "start");
    }

    public void stop() {
        Log.i(TAG, "stop");
    }

    public void upload() {
        Log.i(TAG, "upload");
    }

    public void recordStartTime() {
        Log.i(TAG, "recordStartTime");
    }

    public boolean isMonitorDurationEnd() {
        return true;
    }
}
