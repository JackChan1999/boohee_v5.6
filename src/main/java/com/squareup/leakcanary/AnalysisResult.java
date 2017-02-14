package com.squareup.leakcanary;

import java.io.Serializable;

public final class AnalysisResult implements Serializable {
    public final long      analysisDurationMs;
    public final String    className;
    public final boolean   excludedLeak;
    public final Exception failure;
    public final boolean   leakFound;
    public final LeakTrace leakTrace;

    public static AnalysisResult noLeak(long analysisDurationMs) {
        return new AnalysisResult(false, false, null, null, null, analysisDurationMs);
    }

    public static AnalysisResult leakDetected(boolean excludedLeak, String className, LeakTrace
            leakTrace, long analysisDurationMs) {
        return new AnalysisResult(true, excludedLeak, className, leakTrace, null,
                analysisDurationMs);
    }

    public static AnalysisResult failure(Exception exception, long analysisDurationMs) {
        return new AnalysisResult(false, false, null, null, exception, analysisDurationMs);
    }

    private AnalysisResult(boolean leakFound, boolean excludedLeak, String className, LeakTrace
            leakTrace, Exception failure, long analysisDurationMs) {
        this.leakFound = leakFound;
        this.excludedLeak = excludedLeak;
        this.className = className;
        this.leakTrace = leakTrace;
        this.failure = failure;
        this.analysisDurationMs = analysisDurationMs;
    }
}
