package com.squareup.okhttp.internal;

import com.boohee.utils.Coder;
import com.squareup.okhttp.HttpUrl;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.ByteString;
import okio.Source;

public final class Util {
    public static final byte[]   EMPTY_BYTE_ARRAY   = new byte[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Charset  UTF_8              = Charset.forName("UTF-8");

    public static boolean skipAll(okio.Source r12, int r13, java.util.concurrent.TimeUnit r14)
            throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x006c in
list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = java.lang.System.nanoTime();
        r8 = r12.timeout();
        r8 = r8.hasDeadline();
        if (r8 == 0) goto L_0x0052;
    L_0x0013:
        r8 = r12.timeout();
        r8 = r8.deadlineNanoTime();
        r4 = r8 - r2;
    L_0x001d:
        r8 = r12.timeout();
        r10 = (long) r13;
        r10 = r14.toNanos(r10);
        r10 = java.lang.Math.min(r4, r10);
        r10 = r10 + r2;
        r8.deadlineNanoTime(r10);
        r1 = new okio.Buffer;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
        r1.<init>();	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
    L_0x0033:
        r8 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;	 Catch:{ InterruptedIOException ->
         0x0043, all -> 0x0076 }
        r8 = r12.read(r1, r8);	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
        r10 = -1;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
        r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));	 Catch:{ InterruptedIOException -> 0x0043, all
         -> 0x0076 }
        if (r8 == 0) goto L_0x0054;	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
    L_0x003f:
        r1.clear();	 Catch:{ InterruptedIOException -> 0x0043, all -> 0x0076 }
        goto L_0x0033;
    L_0x0043:
        r0 = move-exception;
        r8 = 0;
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x006c;
    L_0x0049:
        r6 = r12.timeout();
        r6.clearDeadline();
    L_0x0050:
        r6 = r8;
    L_0x0051:
        return r6;
    L_0x0052:
        r4 = r6;
        goto L_0x001d;
    L_0x0054:
        r8 = 1;
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0062;
    L_0x0059:
        r6 = r12.timeout();
        r6.clearDeadline();
    L_0x0060:
        r6 = r8;
        goto L_0x0051;
    L_0x0062:
        r6 = r12.timeout();
        r10 = r2 + r4;
        r6.deadlineNanoTime(r10);
        goto L_0x0060;
    L_0x006c:
        r6 = r12.timeout();
        r10 = r2 + r4;
        r6.deadlineNanoTime(r10);
        goto L_0x0050;
    L_0x0076:
        r8 = move-exception;
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0083;
    L_0x007b:
        r6 = r12.timeout();
        r6.clearDeadline();
    L_0x0082:
        throw r8;
    L_0x0083:
        r6 = r12.timeout();
        r10 = r2 + r4;
        r6.deadlineNanoTime(r10);
        goto L_0x0082;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp" +
                ".internal.Util.skipAll(okio.Source, int, java.util.concurrent.TimeUnit):boolean");
    }

    private Util() {
    }

    public static void checkOffsetAndCount(long arrayLength, long offset, long count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (AssertionError e) {
                if (!isAndroidGetsocknameError(e)) {
                    throw e;
                }
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e2) {
            }
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }

    public static void closeAll(Closeable a, Closeable b) throws IOException {
        Throwable thrown = null;
        try {
            a.close();
        } catch (Throwable e) {
            thrown = e;
        }
        try {
            b.close();
        } catch (Throwable e2) {
            if (thrown == null) {
                thrown = e2;
            }
        }
        if (thrown != null) {
            if (thrown instanceof IOException) {
                throw ((IOException) thrown);
            } else if (thrown instanceof RuntimeException) {
                throw ((RuntimeException) thrown);
            } else if (thrown instanceof Error) {
                throw ((Error) thrown);
            } else {
                throw new AssertionError(thrown);
            }
        }
    }

    public static boolean discard(Source source, int timeout, TimeUnit timeUnit) {
        try {
            return skipAll(source, timeout, timeUnit);
        } catch (IOException e) {
            return false;
        }
    }

    public static String md5Hex(String s) {
        Exception e;
        try {
            return ByteString.of(MessageDigest.getInstance(Coder.KEY_MD5).digest(s.getBytes
                    ("UTF-8"))).hex();
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            throw new AssertionError(e);
        }
    }

    public static String shaBase64(String s) {
        Exception e;
        try {
            return ByteString.of(MessageDigest.getInstance(Coder.KEY_SHA).digest(s.getBytes
                    ("UTF-8"))).base64();
        } catch (NoSuchAlgorithmException e2) {
            e = e2;
            throw new AssertionError(e);
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            throw new AssertionError(e);
        }
    }

    public static ByteString sha1(ByteString s) {
        try {
            return ByteString.of(MessageDigest.getInstance(Coder.KEY_SHA).digest(s.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList(list));
    }

    public static <T> List<T> immutableList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList((Object[]) elements.clone()));
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap(map));
    }

    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    public static <T> T[] intersect(Class<T> arrayType, T[] first, T[] second) {
        List<T> result = intersect(first, second);
        return result.toArray((Object[]) Array.newInstance(arrayType, result.size()));
    }

    private static <T> List<T> intersect(T[] first, T[] second) {
        List<T> result = new ArrayList();
        for (T a : first) {
            for (T b : second) {
                if (a.equals(b)) {
                    result.add(b);
                    break;
                }
            }
        }
        return result;
    }

    public static String hostHeader(HttpUrl url) {
        if (url.port() != HttpUrl.defaultPort(url.scheme())) {
            return url.host() + ":" + url.port();
        }
        return url.host();
    }

    public static String toHumanReadableAscii(String s) {
        int i = 0;
        int length = s.length();
        while (i < length) {
            int c = s.codePointAt(i);
            if (c <= 31 || c >= 127) {
                Buffer buffer = new Buffer();
                buffer.writeUtf8(s, 0, i);
                int j = i;
                while (j < length) {
                    c = s.codePointAt(j);
                    int i2 = (c <= 31 || c >= 127) ? 63 : c;
                    buffer.writeUtf8CodePoint(i2);
                    j += Character.charCount(c);
                }
                return buffer.readUtf8();
            }
            i += Character.charCount(c);
        }
        return s;
    }

    public static boolean isAndroidGetsocknameError(AssertionError e) {
        return (e.getCause() == null || e.getMessage() == null || !e.getMessage().contains
                ("getsockname failed")) ? false : true;
    }

    public static boolean contains(String[] array, String value) {
        return Arrays.asList(array).contains(value);
    }

    public static String[] concat(String[] array, String value) {
        String[] result = new String[(array.length + 1)];
        System.arraycopy(array, 0, result, 0, array.length);
        result[result.length - 1] = value;
        return result;
    }
}
