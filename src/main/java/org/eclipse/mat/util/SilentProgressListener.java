package org.eclipse.mat.util;

import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.util.IProgressListener.Severity;

public class SilentProgressListener implements IProgressListener {
    IProgressListener delegate;

    public SilentProgressListener(IProgressListener delegate) {
        this.delegate = delegate;
    }

    public void beginTask(String name, int totalWork) {
    }

    public final void beginTask(Messages name, int totalWork) {
        beginTask(name.pattern, totalWork);
    }

    public void done() {
    }

    public boolean isCanceled() {
        return this.delegate.isCanceled();
    }

    public void sendUserMessage(Severity severity, String message, Throwable exception) {
        this.delegate.sendUserMessage(severity, message, exception);
    }

    public void setCanceled(boolean value) {
        this.delegate.setCanceled(value);
    }

    public void subTask(String name) {
    }

    public void worked(int work) {
    }
}
