package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.io.RealConnection;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

import okio.Sink;

public final class StreamAllocation {
    public final  Address        address;
    private       boolean        canceled;
    private       RealConnection connection;
    private final ConnectionPool connectionPool;
    private       boolean        released;
    private       RouteSelector  routeSelector;
    private       HttpStream     stream;

    public StreamAllocation(ConnectionPool connectionPool, Address address) {
        this.connectionPool = connectionPool;
        this.address = address;
    }

    public HttpStream newStream(int connectTimeout, int readTimeout, int writeTimeout, boolean
            connectionRetryEnabled, boolean doExtensiveHealthChecks) throws RouteException,
            IOException {
        try {
            HttpStream resultStream;
            RealConnection resultConnection = findHealthyConnection(connectTimeout, readTimeout,
                    writeTimeout, connectionRetryEnabled, doExtensiveHealthChecks);
            if (resultConnection.framedConnection != null) {
                resultStream = new Http2xStream(this, resultConnection.framedConnection);
            } else {
                resultConnection.getSocket().setSoTimeout(readTimeout);
                resultConnection.source.timeout().timeout((long) readTimeout, TimeUnit
                        .MILLISECONDS);
                resultConnection.sink.timeout().timeout((long) writeTimeout, TimeUnit.MILLISECONDS);
                resultStream = new Http1xStream(this, resultConnection.source, resultConnection
                        .sink);
            }
            synchronized (this.connectionPool) {
                resultConnection.streamCount++;
                this.stream = resultStream;
            }
            return resultStream;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int
            writeTimeout, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws
            IOException, RouteException {
        RealConnection candidate;
        while (true) {
            candidate = findConnection(connectTimeout, readTimeout, writeTimeout,
                    connectionRetryEnabled);
            synchronized (this.connectionPool) {
                if (candidate.streamCount != 0) {
                    if (candidate.isHealthy(doExtensiveHealthChecks)) {
                        break;
                    }
                    connectionFailed();
                } else {
                    break;
                }
            }
        }
        return candidate;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.squareup.okhttp.internal.io.RealConnection findConnection(int r10, int r11, int
            r12, boolean r13) throws java.io.IOException, com.squareup.okhttp.internal.http
            .RouteException {
        /*
        r9 = this;
        r2 = r9.connectionPool;
        monitor-enter(r2);
        r1 = r9.released;	 Catch:{ all -> 0x0010 }
        if (r1 == 0) goto L_0x0013;
    L_0x0007:
        r1 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0010 }
        r3 = "released";
        r1.<init>(r3);	 Catch:{ all -> 0x0010 }
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0010 }
        throw r1;
    L_0x0013:
        r1 = r9.stream;	 Catch:{ all -> 0x0010 }
        if (r1 == 0) goto L_0x0020;
    L_0x0017:
        r1 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0010 }
        r3 = "stream != null";
        r1.<init>(r3);	 Catch:{ all -> 0x0010 }
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0020:
        r1 = r9.canceled;	 Catch:{ all -> 0x0010 }
        if (r1 == 0) goto L_0x002d;
    L_0x0024:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x0010 }
        r3 = "Canceled";
        r1.<init>(r3);	 Catch:{ all -> 0x0010 }
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x002d:
        r6 = r9.connection;	 Catch:{ all -> 0x0010 }
        if (r6 == 0) goto L_0x0037;
    L_0x0031:
        r1 = r6.noNewStreams;	 Catch:{ all -> 0x0010 }
        if (r1 != 0) goto L_0x0037;
    L_0x0035:
        monitor-exit(r2);	 Catch:{ all -> 0x0010 }
    L_0x0036:
        return r6;
    L_0x0037:
        r1 = com.squareup.okhttp.internal.Internal.instance;	 Catch:{ all -> 0x0010 }
        r3 = r9.connectionPool;	 Catch:{ all -> 0x0010 }
        r4 = r9.address;	 Catch:{ all -> 0x0010 }
        r7 = r1.get(r3, r4, r9);	 Catch:{ all -> 0x0010 }
        if (r7 == 0) goto L_0x0048;
    L_0x0043:
        r9.connection = r7;	 Catch:{ all -> 0x0010 }
        monitor-exit(r2);	 Catch:{ all -> 0x0010 }
        r6 = r7;
        goto L_0x0036;
    L_0x0048:
        r1 = r9.routeSelector;	 Catch:{ all -> 0x0010 }
        if (r1 != 0) goto L_0x0059;
    L_0x004c:
        r1 = new com.squareup.okhttp.internal.http.RouteSelector;	 Catch:{ all -> 0x0010 }
        r3 = r9.address;	 Catch:{ all -> 0x0010 }
        r4 = r9.routeDatabase();	 Catch:{ all -> 0x0010 }
        r1.<init>(r3, r4);	 Catch:{ all -> 0x0010 }
        r9.routeSelector = r1;	 Catch:{ all -> 0x0010 }
    L_0x0059:
        monitor-exit(r2);	 Catch:{ all -> 0x0010 }
        r1 = r9.routeSelector;
        r8 = r1.next();
        r0 = new com.squareup.okhttp.internal.io.RealConnection;
        r0.<init>(r8);
        r9.acquire(r0);
        r2 = r9.connectionPool;
        monitor-enter(r2);
        r1 = com.squareup.okhttp.internal.Internal.instance;	 Catch:{ all -> 0x0081 }
        r3 = r9.connectionPool;	 Catch:{ all -> 0x0081 }
        r1.put(r3, r0);	 Catch:{ all -> 0x0081 }
        r9.connection = r0;	 Catch:{ all -> 0x0081 }
        r1 = r9.canceled;	 Catch:{ all -> 0x0081 }
        if (r1 == 0) goto L_0x0084;
    L_0x0078:
        r1 = new java.io.IOException;	 Catch:{ all -> 0x0081 }
        r3 = "Canceled";
        r1.<init>(r3);	 Catch:{ all -> 0x0081 }
        throw r1;	 Catch:{ all -> 0x0081 }
    L_0x0081:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0081 }
        throw r1;
    L_0x0084:
        monitor-exit(r2);	 Catch:{ all -> 0x0081 }
        r1 = r9.address;
        r4 = r1.getConnectionSpecs();
        r1 = r10;
        r2 = r11;
        r3 = r12;
        r5 = r13;
        r0.connect(r1, r2, r3, r4, r5);
        r1 = r9.routeDatabase();
        r2 = r0.getRoute();
        r1.connected(r2);
        r6 = r0;
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp" +
                ".internal.http.StreamAllocation.findConnection(int, int, int, boolean):com" +
                ".squareup.okhttp.internal.io.RealConnection");
    }

    public void streamFinished(HttpStream stream) {
        synchronized (this.connectionPool) {
            if (stream != null) {
                if (stream == this.stream) {
                }
            }
            throw new IllegalStateException("expected " + this.stream + " but was " + stream);
        }
        deallocate(false, false, true);
    }

    public HttpStream stream() {
        HttpStream httpStream;
        synchronized (this.connectionPool) {
            httpStream = this.stream;
        }
        return httpStream;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void release() {
        deallocate(false, true, false);
    }

    public void noNewStreams() {
        deallocate(true, false, false);
    }

    private void deallocate(boolean noNewStreams, boolean released, boolean streamFinished) {
        RealConnection connectionToClose = null;
        synchronized (this.connectionPool) {
            if (streamFinished) {
                this.stream = null;
            }
            if (released) {
                this.released = true;
            }
            if (this.connection != null) {
                if (noNewStreams) {
                    this.connection.noNewStreams = true;
                }
                if (this.stream == null && (this.released || this.connection.noNewStreams)) {
                    release(this.connection);
                    if (this.connection.streamCount > 0) {
                        this.routeSelector = null;
                    }
                    if (this.connection.allocations.isEmpty()) {
                        this.connection.idleAtNanos = System.nanoTime();
                        if (Internal.instance.connectionBecameIdle(this.connectionPool, this
                                .connection)) {
                            connectionToClose = this.connection;
                        }
                    }
                    this.connection = null;
                }
            }
        }
        if (connectionToClose != null) {
            Util.closeQuietly(connectionToClose.getSocket());
        }
    }

    public void cancel() {
        synchronized (this.connectionPool) {
            this.canceled = true;
            HttpStream streamToCancel = this.stream;
            RealConnection connectionToCancel = this.connection;
        }
        if (streamToCancel != null) {
            streamToCancel.cancel();
        } else if (connectionToCancel != null) {
            connectionToCancel.cancel();
        }
    }

    private void connectionFailed(IOException e) {
        synchronized (this.connectionPool) {
            if (this.routeSelector != null) {
                if (this.connection.streamCount == 0) {
                    this.routeSelector.connectFailed(this.connection.getRoute(), e);
                } else {
                    this.routeSelector = null;
                }
            }
        }
        connectionFailed();
    }

    public void connectionFailed() {
        deallocate(true, false, true);
    }

    public void acquire(RealConnection connection) {
        connection.allocations.add(new WeakReference(this));
    }

    private void release(RealConnection connection) {
        int size = connection.allocations.size();
        for (int i = 0; i < size; i++) {
            if (((Reference) connection.allocations.get(i)).get() == this) {
                connection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    public boolean recover(RouteException e) {
        if (this.connection != null) {
            connectionFailed(e.getLastConnectException());
        }
        if ((this.routeSelector == null || this.routeSelector.hasNext()) && isRecoverable(e)) {
            return true;
        }
        return false;
    }

    public boolean recover(IOException e, Sink requestBodyOut) {
        if (this.connection != null) {
            int streamCount = this.connection.streamCount;
            connectionFailed(e);
            if (streamCount == 1) {
                return false;
            }
        }
        boolean canRetryRequestBody;
        if (requestBodyOut == null || (requestBodyOut instanceof RetryableSink)) {
            canRetryRequestBody = true;
        } else {
            canRetryRequestBody = false;
        }
        if ((this.routeSelector == null || this.routeSelector.hasNext()) && isRecoverable(e) &&
                canRetryRequestBody) {
            return true;
        }
        return false;
    }

    private boolean isRecoverable(IOException e) {
        if ((e instanceof ProtocolException) || (e instanceof InterruptedIOException)) {
            return false;
        }
        return true;
    }

    private boolean isRecoverable(RouteException e) {
        IOException ioe = e.getLastConnectException();
        if (ioe instanceof ProtocolException) {
            return false;
        }
        if (ioe instanceof InterruptedIOException) {
            return ioe instanceof SocketTimeoutException;
        }
        if (((ioe instanceof SSLHandshakeException) && (ioe.getCause() instanceof
                CertificateException)) || (ioe instanceof SSLPeerUnverifiedException)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return this.address.toString();
    }
}
