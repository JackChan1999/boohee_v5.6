package com.squareup.okhttp.internal;

import android.util.Log;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.tls.AndroidTrustRootIndex;
import com.squareup.okhttp.internal.tls.RealTrustRootIndex;
import com.squareup.okhttp.internal.tls.TrustRootIndex;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okio.Buffer;

public class Platform {
    private static final Platform PLATFORM = findPlatform();

    private static class Android extends Platform {
        private static final int MAX_LOG_LENGTH = 4000;
        private final OptionalMethod<Socket> getAlpnSelectedProtocol;
        private final OptionalMethod<Socket> setAlpnProtocols;
        private final OptionalMethod<Socket> setHostname;
        private final OptionalMethod<Socket> setUseSessionTickets;
        private final Class<?>               sslParametersClass;
        private final Method                 trafficStatsTagSocket;
        private final Method                 trafficStatsUntagSocket;

        public Android(Class<?> sslParametersClass, OptionalMethod<Socket> setUseSessionTickets,
                       OptionalMethod<Socket> setHostname, Method trafficStatsTagSocket, Method
                               trafficStatsUntagSocket, OptionalMethod<Socket>
                               getAlpnSelectedProtocol, OptionalMethod<Socket> setAlpnProtocols) {
            this.sslParametersClass = sslParametersClass;
            this.setUseSessionTickets = setUseSessionTickets;
            this.setHostname = setHostname;
            this.trafficStatsTagSocket = trafficStatsTagSocket;
            this.trafficStatsUntagSocket = trafficStatsUntagSocket;
            this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
            this.setAlpnProtocols = setAlpnProtocols;
        }

        public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout)
                throws IOException {
            try {
                socket.connect(address, connectTimeout);
            } catch (AssertionError e) {
                if (Util.isAndroidGetsocknameError(e)) {
                    throw new IOException(e);
                }
                throw e;
            } catch (SecurityException e2) {
                IOException ioException = new IOException("Exception in connect");
                ioException.initCause(e2);
                throw ioException;
            }
        }

        public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
            Object context = Platform.readFieldOrNull(sslSocketFactory, this.sslParametersClass,
                    "sslParameters");
            if (context == null) {
                try {
                    context = Platform.readFieldOrNull(sslSocketFactory, Class.forName("com" +
                            ".google.android.gms.org.conscrypt.SSLParametersImpl", false,
                            sslSocketFactory.getClass().getClassLoader()), "sslParameters");
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            X509TrustManager x509TrustManager = (X509TrustManager) Platform.readFieldOrNull
                    (context, X509TrustManager.class, "x509TrustManager");
            return x509TrustManager != null ? x509TrustManager : (X509TrustManager) Platform
                    .readFieldOrNull(context, X509TrustManager.class, "trustManager");
        }

        public TrustRootIndex trustRootIndex(X509TrustManager trustManager) {
            TrustRootIndex result = AndroidTrustRootIndex.get(trustManager);
            return result != null ? result : super.trustRootIndex(trustManager);
        }

        public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol>
                protocols) {
            if (hostname != null) {
                this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket,
                        Boolean.valueOf(true));
                this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, hostname);
            }
            if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sslSocket)) {
                this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, Platform
                        .concatLengthPrefixed(protocols));
            }
        }

        public String getSelectedProtocol(SSLSocket socket) {
            if (this.getAlpnSelectedProtocol == null || !this.getAlpnSelectedProtocol.isSupported
                    (socket)) {
                return null;
            }
            byte[] alpnResult = (byte[]) this.getAlpnSelectedProtocol
                    .invokeWithoutCheckedException(socket, new Object[0]);
            return alpnResult != null ? new String(alpnResult, Util.UTF_8) : null;
        }

        public void tagSocket(Socket socket) throws SocketException {
            if (this.trafficStatsTagSocket != null) {
                try {
                    this.trafficStatsTagSocket.invoke(null, new Object[]{socket});
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }

        public void untagSocket(Socket socket) throws SocketException {
            if (this.trafficStatsUntagSocket != null) {
                try {
                    this.trafficStatsUntagSocket.invoke(null, new Object[]{socket});
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }

        public void log(String message) {
            int i = 0;
            int length = message.length();
            while (i < length) {
                int newline = message.indexOf(10, i);
                if (newline == -1) {
                    newline = length;
                }
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    Log.d("OkHttp", message.substring(i, end));
                    i = end;
                } while (i < newline);
                i++;
            }
        }
    }

    private static class JdkPlatform extends Platform {
        private final Class<?> sslContextClass;

        public JdkPlatform(Class<?> sslContextClass) {
            this.sslContextClass = sslContextClass;
        }

        public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
            Object context = Platform.readFieldOrNull(sslSocketFactory, this.sslContextClass,
                    "context");
            if (context == null) {
                return null;
            }
            return (X509TrustManager) Platform.readFieldOrNull(context, X509TrustManager.class,
                    "trustManager");
        }
    }

    private static class JdkWithJettyBootPlatform extends JdkPlatform {
        private final Class<?> clientProviderClass;
        private final Method   getMethod;
        private final Method   putMethod;
        private final Method   removeMethod;
        private final Class<?> serverProviderClass;

        public JdkWithJettyBootPlatform(Class<?> sslContextClass, Method putMethod, Method
                getMethod, Method removeMethod, Class<?> clientProviderClass, Class<?>
                serverProviderClass) {
            super(sslContextClass);
            this.putMethod = putMethod;
            this.getMethod = getMethod;
            this.removeMethod = removeMethod;
            this.clientProviderClass = clientProviderClass;
            this.serverProviderClass = serverProviderClass;
        }

        public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol>
                protocols) {
            ReflectiveOperationException e;
            List<String> names = new ArrayList(protocols.size());
            int size = protocols.size();
            for (int i = 0; i < size; i++) {
                Protocol protocol = (Protocol) protocols.get(i);
                if (protocol != Protocol.HTTP_1_0) {
                    names.add(protocol.toString());
                }
            }
            try {
                Object provider = Proxy.newProxyInstance(Platform.class.getClassLoader(), new
                        Class[]{this.clientProviderClass, this.serverProviderClass}, new
                        JettyNegoProvider(names));
                this.putMethod.invoke(null, new Object[]{sslSocket, provider});
            } catch (InvocationTargetException e2) {
                e = e2;
                throw new AssertionError(e);
            } catch (IllegalAccessException e3) {
                e = e3;
                throw new AssertionError(e);
            }
        }

        public void afterHandshake(SSLSocket sslSocket) {
            try {
                this.removeMethod.invoke(null, new Object[]{sslSocket});
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new AssertionError();
            }
        }

        public String getSelectedProtocol(SSLSocket socket) {
            String str = null;
            try {
                JettyNegoProvider provider = (JettyNegoProvider) Proxy.getInvocationHandler(this
                        .getMethod.invoke(null, new Object[]{socket}));
                if (!provider.unsupported && provider.selected == null) {
                    Internal.logger.log(Level.INFO, "ALPN callback dropped: SPDY and HTTP/2 are " +
                            "disabled. Is alpn-boot on the boot class path?");
                } else if (!provider.unsupported) {
                    str = provider.selected;
                }
                return str;
            } catch (InvocationTargetException e) {
                throw new AssertionError();
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }
    }

    private static class JettyNegoProvider implements InvocationHandler {
        private final List<String> protocols;
        private       String       selected;
        private       boolean      unsupported;

        public JettyNegoProvider(List<String> protocols) {
            this.protocols = protocols;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            if (args == null) {
                args = Util.EMPTY_STRING_ARRAY;
            }
            if (methodName.equals("supports") && Boolean.TYPE == returnType) {
                return Boolean.valueOf(true);
            }
            if (methodName.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            } else if (methodName.equals("protocols") && args.length == 0) {
                return this.protocols;
            } else {
                if ((methodName.equals("selectProtocol") || methodName.equals("select")) &&
                        String.class == returnType && args.length == 1 && (args[0] instanceof
                        List)) {
                    String str;
                    List<String> peerProtocols = args[0];
                    int size = peerProtocols.size();
                    for (int i = 0; i < size; i++) {
                        if (this.protocols.contains(peerProtocols.get(i))) {
                            str = (String) peerProtocols.get(i);
                            this.selected = str;
                            return str;
                        }
                    }
                    str = (String) this.protocols.get(0);
                    this.selected = str;
                    return str;
                } else if ((!methodName.equals("protocolSelected") && !methodName.equals
                        ("selected")) || args.length != 1) {
                    return method.invoke(this, args);
                } else {
                    this.selected = (String) args[0];
                    return null;
                }
            }
        }
    }

    public static Platform get() {
        return PLATFORM;
    }

    public String getPrefix() {
        return "OkHttp";
    }

    public void logW(String warning) {
        System.out.println(warning);
    }

    public void tagSocket(Socket socket) throws SocketException {
    }

    public void untagSocket(Socket socket) throws SocketException {
    }

    public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        return null;
    }

    public TrustRootIndex trustRootIndex(X509TrustManager trustManager) {
        return new RealTrustRootIndex(trustManager.getAcceptedIssuers());
    }

    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> list) {
    }

    public void afterHandshake(SSLSocket sslSocket) {
    }

    public String getSelectedProtocol(SSLSocket socket) {
        return null;
    }

    public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout)
            throws IOException {
        socket.connect(address, connectTimeout);
    }

    public void log(String message) {
        System.out.println(message);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.squareup.okhttp.internal.Platform findPlatform() {
        /*
        r2 = "com.android.org.conscrypt.SSLParametersImpl";
        r3 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x00aa }
    L_0x0007:
        r4 = new com.squareup.okhttp.internal.OptionalMethod;	 Catch:{ ClassNotFoundException ->
         0x00b4 }
        r2 = 0;
        r10 = "setUseSessionTickets";
        r24 = 1;
        r0 = r24;
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r24 = r0;
        r25 = 0;
        r26 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r24[r25] = r26;	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r0 = r24;
        r4.<init>(r2, r10, r0);	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r5 = new com.squareup.okhttp.internal.OptionalMethod;	 Catch:{ ClassNotFoundException ->
         0x00b4 }
        r2 = 0;
        r10 = "setHostname";
        r24 = 1;
        r0 = r24;
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r24 = r0;
        r25 = 0;
        r26 = java.lang.String.class;
        r24[r25] = r26;	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r0 = r24;
        r5.<init>(r2, r10, r0);	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r2 = "android.net.TrafficStats";
        r23 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x0164,
        NoSuchMethodException -> 0x0167 }
        r2 = "tagSocket";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x0164,
        NoSuchMethodException -> 0x0167 }
        r24 = 0;
        r25 = java.net.Socket.class;
        r10[r24] = r25;	 Catch:{ ClassNotFoundException -> 0x0164, NoSuchMethodException ->
        0x0167 }
        r0 = r23;
        r6 = r0.getMethod(r2, r10);	 Catch:{ ClassNotFoundException -> 0x0164,
        NoSuchMethodException -> 0x0167 }
        r2 = "untagSocket";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x0164,
        NoSuchMethodException -> 0x0167 }
        r24 = 0;
        r25 = java.net.Socket.class;
        r10[r24] = r25;	 Catch:{ ClassNotFoundException -> 0x0164, NoSuchMethodException ->
        0x0167 }
        r0 = r23;
        r7 = r0.getMethod(r2, r10);	 Catch:{ ClassNotFoundException -> 0x0164,
        NoSuchMethodException -> 0x0167 }
        r2 = "android.net.Network";
        java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x016f,
        NoSuchMethodException -> 0x0167 }
        r18 = new com.squareup.okhttp.internal.OptionalMethod;	 Catch:{ ClassNotFoundException ->
         0x016f, NoSuchMethodException -> 0x0167 }
        r2 = byte[].class;
        r10 = "getAlpnSelectedProtocol";
        r24 = 0;
        r0 = r24;
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x016f,
        NoSuchMethodException -> 0x0167 }
        r24 = r0;
        r0 = r18;
        r1 = r24;
        r0.<init>(r2, r10, r1);	 Catch:{ ClassNotFoundException -> 0x016f, NoSuchMethodException
        -> 0x0167 }
        r22 = new com.squareup.okhttp.internal.OptionalMethod;	 Catch:{ ClassNotFoundException ->
         0x0172, NoSuchMethodException -> 0x016a }
        r2 = 0;
        r10 = "setAlpnProtocols";
        r24 = 1;
        r0 = r24;
        r0 = new java.lang.Class[r0];	 Catch:{ ClassNotFoundException -> 0x0172,
        NoSuchMethodException -> 0x016a }
        r24 = r0;
        r25 = 0;
        r26 = byte[].class;
        r24[r25] = r26;	 Catch:{ ClassNotFoundException -> 0x0172, NoSuchMethodException ->
        0x016a }
        r0 = r22;
        r1 = r24;
        r0.<init>(r2, r10, r1);	 Catch:{ ClassNotFoundException -> 0x0172, NoSuchMethodException
        -> 0x016a }
        r9 = r22;
        r8 = r18;
    L_0x00a3:
        r2 = new com.squareup.okhttp.internal.Platform$Android;	 Catch:{ ClassNotFoundException ->
         0x00b4 }
        r2.<init>(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ ClassNotFoundException -> 0x00b4 }
        r10 = r2;
    L_0x00a9:
        return r10;
    L_0x00aa:
        r17 = move-exception;
        r2 = "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl";
        r3 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x00b4 }
        goto L_0x0007;
    L_0x00b4:
        r2 = move-exception;
        r2 = "sun.security.ssl.SSLContextImpl";
        r11 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x015a }
        r20 = "org.eclipse.jetty.alpn.ALPN";
        r19 = java.lang.Class.forName(r20);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2.<init>();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r20;
        r2 = r2.append(r0);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r10 = "$Provider";
        r2 = r2.append(r10);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException
        -> 0x0162 }
        r2 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r21 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2.<init>();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r20;
        r2 = r2.append(r0);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r10 = "$ClientProvider";
        r2 = r2.append(r10);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException
        -> 0x0162 }
        r2 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r15 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2.<init>();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r20;
        r2 = r2.append(r0);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r10 = "$ServerProvider";
        r2 = r2.append(r10);	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException
        -> 0x0162 }
        r2 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r16 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = "put";
        r10 = 2;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r24 = 0;
        r25 = javax.net.ssl.SSLSocket.class;
        r10[r24] = r25;	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r24 = 1;
        r10[r24] = r21;	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r19;
        r12 = r0.getMethod(r2, r10);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = "get";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r24 = 0;
        r25 = javax.net.ssl.SSLSocket.class;
        r10[r24] = r25;	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r19;
        r13 = r0.getMethod(r2, r10);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r2 = "remove";
        r10 = 1;
        r10 = new java.lang.Class[r10];	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r24 = 0;
        r25 = javax.net.ssl.SSLSocket.class;
        r10[r24] = r25;	 Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException ->
        0x0162 }
        r0 = r19;
        r14 = r0.getMethod(r2, r10);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        r10 = new com.squareup.okhttp.internal.Platform$JdkWithJettyBootPlatform;	 Catch:{
        ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0162 }
        r10.<init>(r11, r12, r13, r14, r15, r16);	 Catch:{ ClassNotFoundException -> 0x0152,
        NoSuchMethodException -> 0x0162 }
        goto L_0x00a9;
    L_0x0152:
        r2 = move-exception;
    L_0x0153:
        r10 = new com.squareup.okhttp.internal.Platform$JdkPlatform;	 Catch:{
        ClassNotFoundException -> 0x015a }
        r10.<init>(r11);	 Catch:{ ClassNotFoundException -> 0x015a }
        goto L_0x00a9;
    L_0x015a:
        r2 = move-exception;
        r10 = new com.squareup.okhttp.internal.Platform;
        r10.<init>();
        goto L_0x00a9;
    L_0x0162:
        r2 = move-exception;
        goto L_0x0153;
    L_0x0164:
        r2 = move-exception;
        goto L_0x00a3;
    L_0x0167:
        r2 = move-exception;
        goto L_0x00a3;
    L_0x016a:
        r2 = move-exception;
        r8 = r18;
        goto L_0x00a3;
    L_0x016f:
        r2 = move-exception;
        goto L_0x00a3;
    L_0x0172:
        r2 = move-exception;
        r8 = r18;
        goto L_0x00a3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp" +
                ".internal.Platform.findPlatform():com.squareup.okhttp.internal.Platform");
    }

    static byte[] concatLengthPrefixed(List<Protocol> protocols) {
        Buffer result = new Buffer();
        int size = protocols.size();
        for (int i = 0; i < size; i++) {
            Protocol protocol = (Protocol) protocols.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                result.writeByte(protocol.toString().length());
                result.writeUtf8(protocol.toString());
            }
        }
        return result.readByteArray();
    }

    static <T> T readFieldOrNull(Object instance, Class<T> fieldType, String fieldName) {
        Class<?> c = instance.getClass();
        while (c != Object.class) {
            try {
                Field field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(instance);
                if (value == null || !fieldType.isInstance(value)) {
                    return null;
                }
                return fieldType.cast(value);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }
        if (fieldName.equals("delegate")) {
            return null;
        }
        Object delegate = readFieldOrNull(instance, Object.class, "delegate");
        if (delegate != null) {
            return readFieldOrNull(delegate, fieldType, fieldName);
        }
        return null;
    }
}
