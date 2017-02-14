package com.meiqia.core.a.a.a;

import com.meiqia.core.a.a.b.f;
import com.meiqia.core.a.a.d;
import com.meiqia.core.a.a.e;
import com.meiqia.core.a.a.e.b;
import com.meiqia.core.a.a.e.h;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

public abstract class a extends d implements com.meiqia.core.a.a.a, Runnable {
    static final /* synthetic */ boolean c = (!a.class.desiredAssertionStatus());
    private   e                       a;
    protected URI                     b;
    private   Socket                  d;
    private   InputStream             e;
    private   OutputStream            f;
    private   Proxy                   g;
    private   Thread                  h;
    private   com.meiqia.core.a.a.b.a i;
    private   Map<String, String>     j;
    private   CountDownLatch          k;
    private   CountDownLatch          l;
    private   int                     m;

    public a(URI uri) {
        this(uri, new f());
    }

    public a(URI uri, com.meiqia.core.a.a.b.a aVar) {
        this(uri, aVar, null, 0);
    }

    public a(URI uri, com.meiqia.core.a.a.b.a aVar, Map<String, String> map, int i) {
        this.b = null;
        this.a = null;
        this.d = null;
        this.g = Proxy.NO_PROXY;
        this.k = new CountDownLatch(1);
        this.l = new CountDownLatch(1);
        this.m = 0;
        if (uri == null) {
            throw new IllegalArgumentException();
        } else if (aVar == null) {
            throw new IllegalArgumentException("null as draft is permitted for `WebSocketServer` " +
                    "only!");
        } else {
            this.b = uri;
            this.i = aVar;
            this.j = map;
            this.m = i;
            this.a = new e(this, aVar);
        }
    }

    private int g() {
        int port = this.b.getPort();
        if (port != -1) {
            return port;
        }
        String scheme = this.b.getScheme();
        if (scheme.equals("wss")) {
            return WebSocket.DEFAULT_WSS_PORT;
        }
        if (scheme.equals("ws")) {
            return 80;
        }
        throw new RuntimeException("unkonow scheme" + scheme);
    }

    private void h() {
        String path = this.b.getPath();
        String query = this.b.getQuery();
        if (path == null || path.length() == 0) {
            path = "/";
        }
        if (query != null) {
            path = path + "?" + query;
        }
        int g = g();
        query = this.b.getHost() + (g != 80 ? ":" + g : "");
        b dVar = new com.meiqia.core.a.a.e.d();
        dVar.a(path);
        dVar.a("Host", query);
        if (this.j != null) {
            for (Entry entry : this.j.entrySet()) {
                dVar.a((String) entry.getKey(), (String) entry.getValue());
            }
        }
        this.a.a(dVar);
    }

    public InetSocketAddress a() {
        return this.a.a();
    }

    public void a(int i, String str) {
    }

    public abstract void a(int i, String str, boolean z);

    public void a(com.meiqia.core.a.a.a aVar, int i, String str) {
        a(i, str);
    }

    public final void a(com.meiqia.core.a.a.a aVar, int i, String str, boolean z) {
        this.k.countDown();
        this.l.countDown();
        if (this.h != null) {
            this.h.interrupt();
        }
        try {
            if (this.d != null) {
                this.d.close();
            }
        } catch (Exception e) {
            a((com.meiqia.core.a.a.a) this, e);
        }
        a(i, str, z);
    }

    public void a(com.meiqia.core.a.a.a aVar, com.meiqia.core.a.a.d.d dVar) {
        b(dVar);
    }

    public final void a(com.meiqia.core.a.a.a aVar, com.meiqia.core.a.a.e.f fVar) {
        this.k.countDown();
        a((h) fVar);
    }

    public final void a(com.meiqia.core.a.a.a aVar, Exception exception) {
        a(exception);
    }

    public final void a(com.meiqia.core.a.a.a aVar, String str) {
        a(str);
    }

    public final void a(com.meiqia.core.a.a.a aVar, ByteBuffer byteBuffer) {
        a(byteBuffer);
    }

    public void a(com.meiqia.core.a.a.d.d dVar) {
        this.a.a(dVar);
    }

    public abstract void a(h hVar);

    public abstract void a(Exception exception);

    public abstract void a(String str);

    public void a(ByteBuffer byteBuffer) {
    }

    public void b(int i, String str, boolean z) {
    }

    public final void b(com.meiqia.core.a.a.a aVar) {
    }

    public void b(com.meiqia.core.a.a.a aVar, int i, String str, boolean z) {
        b(i, str, z);
    }

    public void b(com.meiqia.core.a.a.d.d dVar) {
    }

    public void b(String str) {
        this.a.a(str);
    }

    public boolean b() {
        return this.a.b();
    }

    public InetSocketAddress c(com.meiqia.core.a.a.a aVar) {
        return this.d != null ? (InetSocketAddress) this.d.getLocalSocketAddress() : null;
    }

    public void c() {
        if (this.h != null) {
            throw new IllegalStateException("WebSocketClient objects are not reuseable");
        }
        this.h = new Thread(this);
        this.h.start();
    }

    public void d() {
        if (this.h != null) {
            this.a.a(1000);
        }
    }

    public com.meiqia.core.a.a.a e() {
        return this.a;
    }

    public boolean f() {
        return this.a.f();
    }

    public void run() {
        try {
            if (this.d == null) {
                this.d = new Socket(this.g);
            } else if (this.d.isClosed()) {
                throw new IOException();
            }
            if (!this.d.isBound()) {
                this.d.connect(new InetSocketAddress(this.b.getHost(), g()), this.m);
            }
            this.e = this.d.getInputStream();
            this.f = this.d.getOutputStream();
            h();
            this.h = new Thread(new c(this, null));
            this.h.start();
            byte[] bArr = new byte[e.a];
            while (!f()) {
                try {
                    int read = this.e.read(bArr);
                    if (read != -1) {
                        this.a.a(ByteBuffer.wrap(bArr, 0, read));
                    }
                } catch (IOException e) {
                    this.a.c();
                } catch (Exception e2) {
                    a(e2);
                    this.a.b(CloseFrame.ABNORMAL_CLOSE, e2.getMessage());
                }
            }
            this.a.c();
            if (!c && !this.d.isClosed()) {
                throw new AssertionError();
            }
        } catch (Exception e22) {
            a(this.a, e22);
            this.a.b(-1, e22.getMessage());
        }
    }
}
