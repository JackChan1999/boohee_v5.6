package com.tencent.tinker.commons.dexpatcher;

public final class DexPatcherLogger {
    private IDexPatcherLogger loggerImpl = null;

    public interface IDexPatcherLogger {
        void d(String str);

        void e(String str);

        void i(String str);

        void v(String str);

        void w(String str);
    }

    public void setLoggerImpl(IDexPatcherLogger dexPatcherLogger) {
        this.loggerImpl = dexPatcherLogger;
    }

    public void v(String tag, String fmt, Object... vals) {
        if (this.loggerImpl != null) {
            fmt = "[V][" + tag + "] " + fmt;
            IDexPatcherLogger iDexPatcherLogger = this.loggerImpl;
            String format = (vals == null || vals.length == 0) ? fmt : String.format(fmt, vals);
            iDexPatcherLogger.v(format);
        }
    }

    public void d(String tag, String fmt, Object... vals) {
        if (this.loggerImpl != null) {
            fmt = "[D][" + tag + "] " + fmt;
            IDexPatcherLogger iDexPatcherLogger = this.loggerImpl;
            String format = (vals == null || vals.length == 0) ? fmt : String.format(fmt, vals);
            iDexPatcherLogger.d(format);
        }
    }

    public void i(String tag, String fmt, Object... vals) {
        if (this.loggerImpl != null) {
            fmt = "[I][" + tag + "] " + fmt;
            IDexPatcherLogger iDexPatcherLogger = this.loggerImpl;
            String format = (vals == null || vals.length == 0) ? fmt : String.format(fmt, vals);
            iDexPatcherLogger.i(format);
        }
    }

    public void w(String tag, String fmt, Object... vals) {
        if (this.loggerImpl != null) {
            fmt = "[W][" + tag + "] " + fmt;
            IDexPatcherLogger iDexPatcherLogger = this.loggerImpl;
            String format = (vals == null || vals.length == 0) ? fmt : String.format(fmt, vals);
            iDexPatcherLogger.w(format);
        }
    }

    public void e(String tag, String fmt, Object... vals) {
        if (this.loggerImpl != null) {
            fmt = "[E][" + tag + "] " + fmt;
            IDexPatcherLogger iDexPatcherLogger = this.loggerImpl;
            String format = (vals == null || vals.length == 0) ? fmt : String.format(fmt, vals);
            iDexPatcherLogger.e(format);
        }
    }
}
