package com.orhanobut.logger;

public final class Settings {
    private LogLevel logLevel = LogLevel.FULL;
    private LogTool logTool;
    private int     methodCount    = 2;
    private int     methodOffset   = 0;
    private boolean showThreadInfo = true;

    public Settings hideThreadInfo() {
        this.showThreadInfo = false;
        return this;
    }

    @Deprecated
    public Settings setMethodCount(int methodCount) {
        return methodCount(methodCount);
    }

    public Settings methodCount(int methodCount) {
        if (methodCount < 0) {
            methodCount = 0;
        }
        this.methodCount = methodCount;
        return this;
    }

    @Deprecated
    public Settings setLogLevel(LogLevel logLevel) {
        return logLevel(logLevel);
    }

    public Settings logLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    @Deprecated
    public Settings setMethodOffset(int offset) {
        return methodOffset(offset);
    }

    public Settings methodOffset(int offset) {
        this.methodOffset = offset;
        return this;
    }

    public Settings logTool(LogTool logTool) {
        this.logTool = logTool;
        return this;
    }

    public int getMethodCount() {
        return this.methodCount;
    }

    public boolean isShowThreadInfo() {
        return this.showThreadInfo;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public int getMethodOffset() {
        return this.methodOffset;
    }

    public LogTool getLogTool() {
        if (this.logTool == null) {
            this.logTool = new AndroidLogTool();
        }
        return this.logTool;
    }
}
