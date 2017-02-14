package com.loopj.android.http;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.RequestParams.FileWrapper;
import com.loopj.android.http.RequestParams.StreamWrapper;
import com.tencent.tinker.android.dx.instruction.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonStreamerEntity implements HttpEntity {
    private static final int                           BUFFER_SIZE          = 4096;
    private static final UnsupportedOperationException ERR_UNSUPPORTED      = new
            UnsupportedOperationException("Unsupported operation in this implementation.");
    private static final Header                        HEADER_GZIP_ENCODING = new BasicHeader
            (AsyncHttpClient.HEADER_CONTENT_ENCODING, AsyncHttpClient.ENCODING_GZIP);
    private static final Header                        HEADER_JSON_CONTENT  = new BasicHeader
            ("Content-Type", "application/json");
    private static final byte[]                        JSON_FALSE           = "false".getBytes();
    private static final byte[]                        JSON_NULL            = "null".getBytes();
    private static final byte[]                        JSON_TRUE            = "true".getBytes();
    private static final String                        LOG_TAG              = "JsonStreamerEntity";
    private static final byte[]                        STREAM_CONTENTS      = escape("contents");
    private static final byte[]                        STREAM_NAME          = escape("name");
    private static final byte[]                        STREAM_TYPE          = escape("type");
    private final        byte[]                        buffer               = new byte[4096];
    private final Header contentEncoding;
    private final byte[] elapsedField;
    private final Map<String, Object> jsonParams = new HashMap();
    private final ResponseHandlerInterface progressHandler;

    public JsonStreamerEntity(ResponseHandlerInterface progressHandler, boolean
            useGZipCompression, String elapsedField) {
        Header header;
        byte[] bArr = null;
        this.progressHandler = progressHandler;
        if (useGZipCompression) {
            header = HEADER_GZIP_ENCODING;
        } else {
            header = null;
        }
        this.contentEncoding = header;
        if (!TextUtils.isEmpty(elapsedField)) {
            bArr = escape(elapsedField);
        }
        this.elapsedField = bArr;
    }

    public void addPart(String key, Object value) {
        this.jsonParams.put(key, value);
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public long getContentLength() {
        return -1;
    }

    public Header getContentEncoding() {
        return this.contentEncoding;
    }

    public Header getContentType() {
        return HEADER_JSON_CONTENT;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw ERR_UNSUPPORTED;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalStateException("Output stream cannot be null.");
        }
        OutputStream os;
        long now = System.currentTimeMillis();
        if (this.contentEncoding != null) {
            os = new GZIPOutputStream(out, 4096);
        } else {
            os = out;
        }
        os.write(Opcodes.NEG_INT);
        Set<String> keys = this.jsonParams.keySet();
        int keysCount = keys.size();
        if (keysCount > 0) {
            int keysProcessed = 0;
            for (String key : keys) {
                keysProcessed++;
                Object value = this.jsonParams.get(key);
                os.write(escape(key));
                os.write(58);
                if (value == null) {
                    os.write(JSON_NULL);
                } else {
                    try {
                        boolean isFileWrapper = value instanceof FileWrapper;
                        if (isFileWrapper || (value instanceof StreamWrapper)) {
                            os.write(Opcodes.NEG_INT);
                            if (isFileWrapper) {
                                writeToFromFile(os, (FileWrapper) value);
                            } else {
                                writeToFromStream(os, (StreamWrapper) value);
                            }
                            os.write(Opcodes.NEG_LONG);
                        } else if (value instanceof JsonValueInterface) {
                            os.write(((JsonValueInterface) value).getEscapedJsonValue());
                        } else if (value instanceof JSONObject) {
                            os.write(value.toString().getBytes());
                        } else if (value instanceof JSONArray) {
                            os.write(value.toString().getBytes());
                        } else if (value instanceof Boolean) {
                            os.write(((Boolean) value).booleanValue() ? JSON_TRUE : JSON_FALSE);
                        } else if (value instanceof Long) {
                            os.write((((Number) value).longValue() + "").getBytes());
                        } else if (value instanceof Double) {
                            os.write((((Number) value).doubleValue() + "").getBytes());
                        } else if (value instanceof Float) {
                            os.write((((Number) value).floatValue() + "").getBytes());
                        } else if (value instanceof Integer) {
                            os.write((((Number) value).intValue() + "").getBytes());
                        } else {
                            os.write(escape(value.toString()));
                        }
                    } catch (Throwable th) {
                        if (this.elapsedField != null || keysProcessed < keysCount) {
                            os.write(44);
                        }
                    }
                }
                if (this.elapsedField != null || keysProcessed < keysCount) {
                    os.write(44);
                }
            }
            long elapsedTime = System.currentTimeMillis() - now;
            if (this.elapsedField != null) {
                os.write(this.elapsedField);
                os.write(58);
                os.write((elapsedTime + "").getBytes());
            }
            Log.i(LOG_TAG, "Uploaded JSON in " + Math.floor((double) (elapsedTime / 1000)) + " " +
                    "seconds");
        }
        os.write(Opcodes.NEG_LONG);
        os.flush();
        AsyncHttpClient.silentCloseOutputStream(os);
    }

    private void writeToFromStream(OutputStream os, StreamWrapper entry) throws IOException {
        writeMetaData(os, entry.name, entry.contentType);
        Base64OutputStream bos = new Base64OutputStream(os, 18);
        while (true) {
            int bytesRead = entry.inputStream.read(this.buffer);
            if (bytesRead == -1) {
                break;
            }
            bos.write(this.buffer, 0, bytesRead);
        }
        AsyncHttpClient.silentCloseOutputStream(bos);
        endMetaData(os);
        if (entry.autoClose) {
            AsyncHttpClient.silentCloseInputStream(entry.inputStream);
        }
    }

    private void writeToFromFile(OutputStream os, FileWrapper wrapper) throws IOException {
        writeMetaData(os, wrapper.file.getName(), wrapper.contentType);
        long bytesWritten = 0;
        long totalSize = wrapper.file.length();
        FileInputStream in = new FileInputStream(wrapper.file);
        Base64OutputStream bos = new Base64OutputStream(os, 18);
        while (true) {
            int bytesRead = in.read(this.buffer);
            if (bytesRead != -1) {
                bos.write(this.buffer, 0, bytesRead);
                bytesWritten += (long) bytesRead;
                this.progressHandler.sendProgressMessage(bytesWritten, totalSize);
            } else {
                AsyncHttpClient.silentCloseOutputStream(bos);
                endMetaData(os);
                AsyncHttpClient.silentCloseInputStream(in);
                return;
            }
        }
    }

    private void writeMetaData(OutputStream os, String name, String contentType) throws
            IOException {
        os.write(STREAM_NAME);
        os.write(58);
        os.write(escape(name));
        os.write(44);
        os.write(STREAM_TYPE);
        os.write(58);
        os.write(escape(contentType));
        os.write(44);
        os.write(STREAM_CONTENTS);
        os.write(58);
        os.write(34);
    }

    private void endMetaData(OutputStream os) throws IOException {
        os.write(34);
    }

    static byte[] escape(String string) {
        if (string == null) {
            return JSON_NULL;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append('\"');
        int length = string.length();
        int pos = -1;
        while (true) {
            pos++;
            if (pos < length) {
                char ch = string.charAt(pos);
                switch (ch) {
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    default:
                        if ((ch >= '\u0000' && ch <= '\u001f') || ((ch >= '' && ch <= '') ||
                                (ch >= ' ' && ch <= '⃿'))) {
                            String intString = Integer.toHexString(ch);
                            sb.append("\\u");
                            int intLength = 4 - intString.length();
                            for (int zero = 0; zero < intLength; zero++) {
                                sb.append('0');
                            }
                            sb.append(intString.toUpperCase(Locale.US));
                            break;
                        }
                        sb.append(ch);
                        break;
                    break;
                }
            }
            sb.append('\"');
            return sb.toString().getBytes();
        }
    }
}
