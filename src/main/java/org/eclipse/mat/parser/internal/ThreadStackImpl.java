package org.eclipse.mat.parser.internal;

import org.eclipse.mat.snapshot.model.IStackFrame;
import org.eclipse.mat.snapshot.model.IThreadStack;

class ThreadStackImpl implements IThreadStack {
    private IStackFrame[] stackFrames;
    private int threadId;

    public ThreadStackImpl(int threadId, StackFrameImpl[] stackFrames) {
        this.threadId = threadId;
        this.stackFrames = stackFrames;
    }

    public IStackFrame[] getStackFrames() {
        return this.stackFrames;
    }

    public int getThreadId() {
        return this.threadId;
    }
}
