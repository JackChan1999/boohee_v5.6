package org.eclipse.mat.parser.internal;

import org.eclipse.mat.snapshot.model.IStackFrame;

class StackFrameImpl implements IStackFrame {
    private int[] localObjectIds;
    private String text;

    public StackFrameImpl(String text, int[] localObjectIds) {
        this.text = text;
        this.localObjectIds = localObjectIds;
    }

    public int[] getLocalObjectsIds() {
        return this.localObjectIds == null ? new int[0] : this.localObjectIds;
    }

    public String getText() {
        return this.text;
    }
}
