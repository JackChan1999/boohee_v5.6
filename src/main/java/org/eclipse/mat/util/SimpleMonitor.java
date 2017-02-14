package org.eclipse.mat.util;

import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.util.IProgressListener.Severity;

public class SimpleMonitor {
    int currentMonitor;
    IProgressListener delegate;
    int[] percentages;
    String task;

    public class Listener implements IProgressListener {
        long counter;
        boolean isSmaller;
        int majorUnits;
        int unitsReported;
        long workDone;
        long workPerUnit;

        public Listener(int majorUnits) {
            this.majorUnits = majorUnits;
        }

        public final void beginTask(Messages name, int totalWork) {
            beginTask(name.pattern, totalWork);
        }

        public void beginTask(String name, int totalWork) {
            if (name != null) {
                SimpleMonitor.this.delegate.subTask(name);
            }
            if (totalWork != 0) {
                boolean z;
                if (totalWork < this.majorUnits) {
                    z = true;
                } else {
                    z = false;
                }
                this.isSmaller = z;
                this.workPerUnit = this.isSmaller ? (long) (this.majorUnits / totalWork) : (long) (totalWork / this.majorUnits);
                this.unitsReported = 0;
            }
        }

        public void subTask(String name) {
            SimpleMonitor.this.delegate.subTask(name);
        }

        public void done() {
            if (this.majorUnits - this.unitsReported > 0) {
                SimpleMonitor.this.delegate.worked(this.majorUnits - this.unitsReported);
            }
        }

        public boolean isCanceled() {
            return SimpleMonitor.this.delegate.isCanceled();
        }

        public boolean isProbablyCanceled() {
            long j = this.counter;
            this.counter = 1 + j;
            return j % 5000 == 0 ? isCanceled() : false;
        }

        public void totalWorkDone(long work) {
            if (this.workDone != work && this.workPerUnit != 0) {
                this.workDone = work;
                int unitsToReport = (this.isSmaller ? (int) (this.workPerUnit * work) : (int) (work / this.workPerUnit)) - this.unitsReported;
                if (unitsToReport > 0) {
                    SimpleMonitor.this.delegate.worked(unitsToReport);
                    this.unitsReported += unitsToReport;
                }
            }
        }

        public void worked(int work) {
            totalWorkDone(this.workDone + ((long) work));
        }

        public void setCanceled(boolean value) {
            SimpleMonitor.this.delegate.setCanceled(value);
        }

        public void sendUserMessage(Severity severity, String message, Throwable exception) {
            SimpleMonitor.this.delegate.sendUserMessage(severity, message, exception);
        }

        public long getWorkDone() {
            return this.workDone;
        }
    }

    public SimpleMonitor(String task, IProgressListener monitor, int[] percentages) {
        this.task = task;
        this.delegate = monitor;
        this.percentages = percentages;
    }

    public IProgressListener nextMonitor() {
        if (this.currentMonitor == 0) {
            int total = 0;
            for (int ii : this.percentages) {
                total += ii;
            }
            this.delegate.beginTask(this.task, total);
        }
        int[] iArr = this.percentages;
        int i = this.currentMonitor;
        this.currentMonitor = i + 1;
        return new Listener(iArr[i]);
    }
}
