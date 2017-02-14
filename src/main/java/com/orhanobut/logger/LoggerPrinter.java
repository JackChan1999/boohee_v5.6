package com.orhanobut.logger;

import android.text.TextUtils;

import com.umeng.socialize.common.SocializeConstants;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class LoggerPrinter implements Printer {
    private static final int                  ASSERT                 = 7;
    private static final String               BOTTOM_BORDER          =
            "╚════════════════════════════════════════════════════════════════════════════════════════";
    private static final char                 BOTTOM_LEFT_CORNER     = '╚';
    private static final int                  CHUNK_SIZE             = 4000;
    private static final int                  DEBUG                  = 3;
    private static final String               DOUBLE_DIVIDER         =
            "════════════════════════════════════════════";
    private static final int                  ERROR                  = 6;
    private static final char                 HORIZONTAL_DOUBLE_LINE = '║';
    private static final int                  INFO                   = 4;
    private static final int                  JSON_INDENT            = 4;
    private static final String               MIDDLE_BORDER          =
            "╟────────────────────────────────────────────────────────────────────────────────────────";
    private static final char                 MIDDLE_CORNER          = '╟';
    private static final int                  MIN_STACK_OFFSET       = 3;
    private static final String               SINGLE_DIVIDER         =
            "────────────────────────────────────────────";
    private static final String               TOP_BORDER             =
            "╔════════════════════════════════════════════════════════════════════════════════════════";
    private static final char                 TOP_LEFT_CORNER        = '╔';
    private static final int                  VERBOSE                = 2;
    private static final int                  WARN                   = 5;
    private final        ThreadLocal<Integer> localMethodCount       = new ThreadLocal();
    private final        ThreadLocal<String>  localTag               = new ThreadLocal();
    private Settings settings;
    private String   tag;

    LoggerPrinter() {
    }

    public Settings init(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag may not be null");
        } else if (tag.trim().length() == 0) {
            throw new IllegalStateException("tag may not be empty");
        } else {
            this.tag = tag;
            this.settings = new Settings();
            return this.settings;
        }
    }

    public Settings getSettings() {
        return this.settings;
    }

    public Printer t(String tag, int methodCount) {
        if (tag != null) {
            this.localTag.set(tag);
        }
        this.localMethodCount.set(Integer.valueOf(methodCount));
        return this;
    }

    public void d(String message, Object... args) {
        log(3, message, args);
    }

    public void e(String message, Object... args) {
        e(null, message, args);
    }

    public void e(Throwable throwable, String message, Object... args) {
        if (!(throwable == null || message == null)) {
            message = message + " : " + throwable.toString();
        }
        if (throwable != null && message == null) {
            message = throwable.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(6, message, args);
    }

    public void w(String message, Object... args) {
        log(5, message, args);
    }

    public void i(String message, Object... args) {
        log(4, message, args);
    }

    public void v(String message, Object... args) {
        log(2, message, args);
    }

    public void wtf(String message, Object... args) {
        log(7, message, args);
    }

    public void json(String json) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content", new Object[0]);
            return;
        }
        try {
            if (json.startsWith("{")) {
                d(new JSONObject(json).toString(4), new Object[0]);
            } else if (json.startsWith("[")) {
                d(new JSONArray(json).toString(4), new Object[0]);
            }
        } catch (JSONException e) {
            e(e.getCause().getMessage() + "\n" + json, new Object[0]);
        }
    }

    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content", new Object[0]);
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"), new Object[0]);
        } catch (TransformerException e) {
            e(e.getCause().getMessage() + "\n" + xml, new Object[0]);
        }
    }

    public void clear() {
        this.settings = null;
    }

    private synchronized void log(int logType, String msg, Object... args) {
        if (this.settings.getLogLevel() != LogLevel.NONE) {
            String tag = getTag();
            String message = createMessage(msg, args);
            int methodCount = getMethodCount();
            logTopBorder(logType, tag);
            logHeaderContent(logType, tag, methodCount);
            byte[] bytes = message.getBytes();
            int length = bytes.length;
            if (length <= CHUNK_SIZE) {
                if (methodCount > 0) {
                    logDivider(logType, tag);
                }
                logContent(logType, tag, message);
                logBottomBorder(logType, tag);
            } else {
                if (methodCount > 0) {
                    logDivider(logType, tag);
                }
                for (int i = 0; i < length; i += CHUNK_SIZE) {
                    logContent(logType, tag, new String(bytes, i, Math.min(length - i,
                            CHUNK_SIZE)));
                }
                logBottomBorder(logType, tag);
            }
        }
    }

    private void logTopBorder(int logType, String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    private void logHeaderContent(int logType, String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (this.settings.isShowThreadInfo()) {
            logChunk(logType, tag, "║ Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";
        int stackOffset = getStackOffset(trace) + this.settings.getMethodOffset();
        if (methodCount + stackOffset > trace.length) {
            methodCount = (trace.length - stackOffset) - 1;
        }
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex < trace.length) {
                StringBuilder builder = new StringBuilder();
                builder.append("║ ").append(level).append(getSimpleClassName(trace[stackIndex]
                        .getClassName())).append(".").append(trace[stackIndex].getMethodName())
                        .append(" ").append(" (").append(trace[stackIndex].getFileName()).append
                        (":").append(trace[stackIndex].getLineNumber()).append(SocializeConstants
                        .OP_CLOSE_PAREN);
                level = level + "   ";
                logChunk(logType, tag, builder.toString());
            }
        }
    }

    private void logBottomBorder(int logType, String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, String tag, String chunk) {
        for (String line : chunk.split(System.getProperty("line.separator"))) {
            logChunk(logType, tag, "║ " + line);
        }
    }

    private void logChunk(int logType, String tag, String chunk) {
        String finalTag = formatTag(tag);
        switch (logType) {
            case 2:
                this.settings.getLogTool().v(finalTag, chunk);
                return;
            case 4:
                this.settings.getLogTool().i(finalTag, chunk);
                return;
            case 5:
                this.settings.getLogTool().w(finalTag, chunk);
                return;
            case 6:
                this.settings.getLogTool().e(finalTag, chunk);
                return;
            case 7:
                this.settings.getLogTool().wtf(finalTag, chunk);
                return;
            default:
                this.settings.getLogTool().d(finalTag, chunk);
                return;
        }
    }

    private String getSimpleClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    private String formatTag(String tag) {
        if (TextUtils.isEmpty(tag) || TextUtils.equals(this.tag, tag)) {
            return this.tag;
        }
        return this.tag + SocializeConstants.OP_DIVIDER_MINUS + tag;
    }

    private String getTag() {
        String tag = (String) this.localTag.get();
        if (tag == null) {
            return this.tag;
        }
        this.localTag.remove();
        return tag;
    }

    private String createMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    private int getMethodCount() {
        Integer count = (Integer) this.localMethodCount.get();
        int result = this.settings.getMethodCount();
        if (count != null) {
            this.localMethodCount.remove();
            result = count.intValue();
        }
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("methodCount cannot be negative");
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = 3; i < trace.length; i++) {
            String name = trace[i].getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return i - 1;
            }
        }
        return -1;
    }
}
