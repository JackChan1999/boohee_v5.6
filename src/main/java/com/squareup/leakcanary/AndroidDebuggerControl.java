package com.squareup.leakcanary;

import android.os.Debug;

public final class AndroidDebuggerControl implements DebuggerControl {
    public boolean isDebuggerAttached() {
        return Debug.isDebuggerConnected();
    }
}
