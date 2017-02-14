package com.mob.tools.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.socialize.common.SocializeConstants;

public class Ln {
    private static BaseConfig config = new BaseConfig();
    private static Print      print  = new Print();

    private static class BaseConfig {
        public int    minimumLogLevel = 2;
        public String packageName     = "";
        public String scope           = "";

        public void setContext(Context context) {
            if (context != null) {
                this.packageName = context.getPackageName();
                if (TextUtils.isEmpty(this.packageName)) {
                    this.packageName = "";
                } else {
                    this.scope = this.packageName.toUpperCase();
                }
            }
        }
    }

    private static class Print {
        public Context context;
        public String packageName = "";

        protected static String getScope(int i) {
            if (Ln.config.minimumLogLevel <= 3) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                if (i >= 0 && i < stackTrace.length) {
                    StackTraceElement stackTraceElement = stackTrace[i];
                    String fileName = stackTraceElement.getFileName();
                    fileName = (fileName == null || fileName.length() <= 0) ? stackTraceElement
                            .getClassName() : Ln.config.scope + "/" + fileName;
                    int lineNumber = stackTraceElement.getLineNumber();
                    String valueOf = String.valueOf(lineNumber);
                    if (lineNumber < 0) {
                        valueOf = stackTraceElement.getMethodName();
                        if (valueOf == null || valueOf.length() <= 0) {
                            valueOf = "Unknown Source";
                        }
                    }
                    return fileName + SocializeConstants.OP_OPEN_PAREN + valueOf +
                            SocializeConstants.OP_CLOSE_PAREN;
                }
            }
            return Ln.config.scope;
        }

        public int broadcast(int i, String str) {
            if (this.context != null) {
                try {
                    Intent intent = new Intent();
                    intent.setAction("cn.sharesdk.log");
                    intent.putExtra("package", this.packageName);
                    intent.putExtra("priority", i);
                    intent.putExtra("msg", str);
                    this.context.sendBroadcast(intent);
                } catch (Throwable th) {
                }
            }
            return 0;
        }

        public int println(int i, String str) {
            return Log.println(i, getScope(5), processMessage(str));
        }

        protected String processMessage(String str) {
            if (Ln.config.minimumLogLevel > 3) {
                return str;
            }
            return String.format("%s %s", new Object[]{Thread.currentThread().getName(), str});
        }

        public void setContext(Context context) {
            if (context != null) {
                this.context = context;
                this.packageName = context.getPackageName();
                if (TextUtils.isEmpty(this.packageName)) {
                    this.packageName = "";
                }
            }
        }
    }

    private Ln() {
    }

    public static void close() {
        config.minimumLogLevel = 7;
    }

    public static int d(Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 3) {
            obj2 = obj.toString();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(3, obj2);
        }
        obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(3, obj2);
    }

    public static int d(Throwable th) {
        return config.minimumLogLevel <= 3 ? print.println(3, Log.getStackTraceString(th)) :
                print.broadcast(3, Log.getStackTraceString(th));
    }

    public static int d(Throwable th, Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 3) {
            obj2 = obj.toString();
            StringBuilder stringBuilder = new StringBuilder();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(3, stringBuilder.append(obj2).append('\n').append(Log
                    .getStackTraceString(th)).toString());
        }
        obj2 = obj.toString();
        stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(3, stringBuilder.append(obj2).append('\n').append(Log
                .getStackTraceString(th)).toString());
    }

    public static int e(Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 6) {
            obj2 = obj.toString();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(6, obj2);
        }
        obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(6, obj2);
    }

    public static int e(Throwable th) {
        return config.minimumLogLevel <= 6 ? print.println(6, Log.getStackTraceString(th)) :
                print.broadcast(6, Log.getStackTraceString(th));
    }

    public static int e(Throwable th, Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 6) {
            obj2 = obj.toString();
            StringBuilder stringBuilder = new StringBuilder();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(6, stringBuilder.append(obj2).append('\n').append(Log
                    .getStackTraceString(th)).toString());
        }
        obj2 = obj.toString();
        stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(6, stringBuilder.append(obj2).append('\n').append(Log
                .getStackTraceString(th)).toString());
    }

    public static int i(Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 4) {
            obj2 = obj.toString();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(4, obj2);
        }
        obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(4, obj2);
    }

    public static int i(Throwable th) {
        return config.minimumLogLevel <= 4 ? print.println(4, Log.getStackTraceString(th)) :
                print.broadcast(4, Log.getStackTraceString(th));
    }

    public static int i(Throwable th, Object obj, Object... objArr) {
        String obj2;
        if (config.minimumLogLevel > 4) {
            obj2 = obj.toString();
            StringBuilder stringBuilder = new StringBuilder();
            if (objArr.length > 0) {
                obj2 = String.format(obj2, objArr);
            }
            return print.broadcast(4, stringBuilder.append(obj2).append('\n').append(Log
                    .getStackTraceString(th)).toString());
        }
        obj2 = obj.toString();
        stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(4, stringBuilder.append(obj2).append('\n').append(Log
                .getStackTraceString(th)).toString());
    }

    public static boolean isDebugEnabled() {
        return config.minimumLogLevel <= 3;
    }

    public static boolean isVerboseEnabled() {
        return config.minimumLogLevel <= 2;
    }

    public static String logLevelToString(int i) {
        switch (i) {
            case 2:
                return "VERBOSE";
            case 3:
                return "DEBUG";
            case 4:
                return "INFO";
            case 5:
                return "WARN";
            case 6:
                return "ERROR";
            case 7:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }

    public static void setContext(Context context) {
        config.setContext(context.getApplicationContext());
        print.setContext(context.getApplicationContext());
    }

    public static int v(Object obj, Object... objArr) {
        if (config.minimumLogLevel > 2) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(2, obj2);
    }

    public static int v(Throwable th) {
        return config.minimumLogLevel <= 2 ? print.println(2, Log.getStackTraceString(th)) : 0;
    }

    public static int v(Throwable th, Object obj, Object... objArr) {
        if (config.minimumLogLevel > 2) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(2, stringBuilder.append(obj2).append('\n').append(Log
                .getStackTraceString(th)).toString());
    }

    public static int w(Object obj, Object... objArr) {
        if (config.minimumLogLevel > 5) {
            return 0;
        }
        String obj2 = obj.toString();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(5, obj2);
    }

    public static int w(Throwable th) {
        return config.minimumLogLevel <= 5 ? print.println(5, Log.getStackTraceString(th)) : 0;
    }

    public static int w(Throwable th, Object obj, Object... objArr) {
        if (config.minimumLogLevel > 5) {
            return 0;
        }
        String obj2 = obj.toString();
        StringBuilder stringBuilder = new StringBuilder();
        if (objArr.length > 0) {
            obj2 = String.format(obj2, objArr);
        }
        return print.println(5, stringBuilder.append(obj2).append('\n').append(Log
                .getStackTraceString(th)).toString());
    }
}
