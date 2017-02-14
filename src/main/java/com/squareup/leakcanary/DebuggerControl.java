package com.squareup.leakcanary;

public interface DebuggerControl {
    public static final DebuggerControl NONE = new DebuggerControl() {
        public boolean isDebuggerAttached() {
            return false;
        }
    };

    boolean isDebuggerAttached();
}
