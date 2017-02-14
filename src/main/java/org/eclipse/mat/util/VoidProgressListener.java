package org.eclipse.mat.util;

import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.util.IProgressListener.Severity;

public class VoidProgressListener implements IProgressListener {
    private boolean cancelled = false;

    public void beginTask(String name, int totalWork) {
    }

    public final void beginTask(Messages name, int totalWork) {
        beginTask(name.pattern, totalWork);
    }

    public void done() {
    }

    public boolean isCanceled() {
        return this.cancelled;
    }

    public void setCanceled(boolean value) {
        this.cancelled = value;
    }

    public void subTask(String name) {
    }

    public void worked(int work) {
    }

    public void sendUserMessage(Severity severity, String message, Throwable exception) {
    }
}
