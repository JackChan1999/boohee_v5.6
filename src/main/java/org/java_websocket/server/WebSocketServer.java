package org.java_websocket.server;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketFactory;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;

public abstract class WebSocketServer extends WebSocketAdapter implements Runnable {
    public static int DECODERS = Runtime.getRuntime().availableProcessors();
    private final InetSocketAddress address;
    private BlockingQueue<ByteBuffer> buffers;
    private final Collection<WebSocket> connections;
    private List<WebSocketWorker> decoders;
    private List<Draft> drafts;
    private List<WebSocketImpl> iqueue;
    private volatile AtomicBoolean isclosed;
    private int queueinvokes;
    private AtomicInteger queuesize;
    private Selector selector;
    private Thread selectorthread;
    private ServerSocketChannel server;
    private WebSocketServerFactory wsf;

    public interface WebSocketServerFactory extends WebSocketFactory {
        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, List<Draft> list, Socket socket);

        WebSocketImpl createWebSocket(WebSocketAdapter webSocketAdapter, Draft draft, Socket socket);

        ByteChannel wrapChannel(SocketChannel socketChannel, SelectionKey selectionKey) throws IOException;
    }

    public class WebSocketWorker extends Thread {
        static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketServer.class.desiredAssertionStatus());
        private BlockingQueue<WebSocketImpl> iqueue = new LinkedBlockingQueue();

        public WebSocketWorker() {
            setName("WebSocketWorker-" + getId());
            setUncaughtExceptionHandler(new UncaughtExceptionHandler(WebSocketServer.this) {
                public void uncaughtException(Thread t, Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e);
                }
            });
        }

        public void put(WebSocketImpl ws) throws InterruptedException {
            this.iqueue.put(ws);
        }

        public void run() {
            WebSocketImpl ws = null;
            while (true) {
                ByteBuffer buf;
                try {
                    ws = (WebSocketImpl) this.iqueue.take();
                    buf = (ByteBuffer) ws.inQueue.poll();
                    if ($assertionsDisabled || buf != null) {
                        ws.decode(buf);
                        WebSocketServer.this.pushBuffer(buf);
                    } else {
                        throw new AssertionError();
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException e2) {
                    WebSocketServer.this.handleFatal(ws, e2);
                    return;
                } catch (Throwable th) {
                    WebSocketServer.this.pushBuffer(buf);
                }
            }
        }
    }

    public abstract void onClose(WebSocket webSocket, int i, String str, boolean z);

    public abstract void onError(WebSocket webSocket, Exception exception);

    public abstract void onMessage(WebSocket webSocket, String str);

    public abstract void onOpen(WebSocket webSocket, ClientHandshake clientHandshake);

    public WebSocketServer() throws UnknownHostException {
        this(new InetSocketAddress(80), DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address) {
        this(address, DECODERS, null);
    }

    public WebSocketServer(InetSocketAddress address, int decoders) {
        this(address, decoders, null);
    }

    public WebSocketServer(InetSocketAddress address, List<Draft> drafts) {
        this(address, DECODERS, drafts);
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts) {
        this(address, decodercount, drafts, new HashSet());
    }

    public WebSocketServer(InetSocketAddress address, int decodercount, List<Draft> drafts, Collection<WebSocket> connectionscontainer) {
        this.isclosed = new AtomicBoolean(false);
        this.queueinvokes = 0;
        this.queuesize = new AtomicInteger(0);
        this.wsf = new DefaultWebSocketServerFactory();
        if (address == null || decodercount < 1 || connectionscontainer == null) {
            throw new IllegalArgumentException("address and connectionscontainer must not be null and you need at least 1 decoder");
        }
        if (drafts == null) {
            this.drafts = Collections.emptyList();
        } else {
            this.drafts = drafts;
        }
        this.address = address;
        this.connections = connectionscontainer;
        this.iqueue = new LinkedList();
        this.decoders = new ArrayList(decodercount);
        this.buffers = new LinkedBlockingQueue();
        for (int i = 0; i < decodercount; i++) {
            WebSocketWorker ex = new WebSocketWorker();
            this.decoders.add(ex);
            ex.start();
        }
    }

    public void start() {
        if (this.selectorthread != null) {
            throw new IllegalStateException(getClass().getName() + " can only be started once.");
        }
        new Thread(this).start();
    }

    public void stop(int timeout) throws IOException, InterruptedException {
        if (this.isclosed.compareAndSet(false, true)) {
            synchronized (this.connections) {
                for (WebSocket ws : this.connections) {
                    ws.close(1001);
                }
            }
            synchronized (this) {
                if (this.selectorthread != null) {
                    if (Thread.currentThread() != this.selectorthread) {
                    }
                    if (this.selectorthread != Thread.currentThread()) {
                        this.selectorthread.interrupt();
                        this.selectorthread.join();
                    }
                }
                if (this.decoders != null) {
                    for (WebSocketWorker w : this.decoders) {
                        w.interrupt();
                    }
                }
                if (this.server != null) {
                    this.server.close();
                }
            }
        }
    }

    public void stop() throws IOException, InterruptedException {
        stop(0);
    }

    public Collection<WebSocket> connections() {
        return this.connections;
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }

    public int getPort() {
        int port = getAddress().getPort();
        if (port != 0 || this.server == null) {
            return port;
        }
        return this.server.socket().getLocalPort();
    }

    public List<Draft> getDraft() {
        return Collections.unmodifiableList(this.drafts);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r18 = this;
        monitor-enter(r18);
        r0 = r18;
        r13 = r0.selectorthread;	 Catch:{ all -> 0x0029 }
        if (r13 == 0) goto L_0x002c;
    L_0x0007:
        r13 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0029 }
        r14 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0029 }
        r14.<init>();	 Catch:{ all -> 0x0029 }
        r15 = r18.getClass();	 Catch:{ all -> 0x0029 }
        r15 = r15.getName();	 Catch:{ all -> 0x0029 }
        r14 = r14.append(r15);	 Catch:{ all -> 0x0029 }
        r15 = " can only be started once.";
        r14 = r14.append(r15);	 Catch:{ all -> 0x0029 }
        r14 = r14.toString();	 Catch:{ all -> 0x0029 }
        r13.<init>(r14);	 Catch:{ all -> 0x0029 }
        throw r13;	 Catch:{ all -> 0x0029 }
    L_0x0029:
        r13 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x0029 }
        throw r13;
    L_0x002c:
        r13 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0029 }
        r0 = r18;
        r0.selectorthread = r13;	 Catch:{ all -> 0x0029 }
        r0 = r18;
        r13 = r0.isclosed;	 Catch:{ all -> 0x0029 }
        r13 = r13.get();	 Catch:{ all -> 0x0029 }
        if (r13 == 0) goto L_0x0040;
    L_0x003e:
        monitor-exit(r18);	 Catch:{ all -> 0x0029 }
    L_0x003f:
        return;
    L_0x0040:
        monitor-exit(r18);	 Catch:{ all -> 0x0029 }
        r0 = r18;
        r13 = r0.selectorthread;
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "WebsocketSelector";
        r14 = r14.append(r15);
        r0 = r18;
        r15 = r0.selectorthread;
        r16 = r15.getId();
        r0 = r16;
        r14 = r14.append(r0);
        r14 = r14.toString();
        r13.setName(r14);
        r13 = java.nio.channels.ServerSocketChannel.open();	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r0.server = r13;	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r13 = r0.server;	 Catch:{ IOException -> 0x00ec }
        r14 = 0;
        r13.configureBlocking(r14);	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r13 = r0.server;	 Catch:{ IOException -> 0x00ec }
        r11 = r13.socket();	 Catch:{ IOException -> 0x00ec }
        r13 = org.java_websocket.WebSocketImpl.RCVBUF;	 Catch:{ IOException -> 0x00ec }
        r11.setReceiveBufferSize(r13);	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r13 = r0.address;	 Catch:{ IOException -> 0x00ec }
        r11.bind(r13);	 Catch:{ IOException -> 0x00ec }
        r13 = java.nio.channels.Selector.open();	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r0.selector = r13;	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r13 = r0.server;	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r14 = r0.selector;	 Catch:{ IOException -> 0x00ec }
        r0 = r18;
        r15 = r0.server;	 Catch:{ IOException -> 0x00ec }
        r15 = r15.validOps();	 Catch:{ IOException -> 0x00ec }
        r13.register(r14, r15);	 Catch:{ IOException -> 0x00ec }
    L_0x00a5:
        r0 = r18;
        r13 = r0.selectorthread;	 Catch:{ RuntimeException -> 0x0140 }
        r13 = r13.isInterrupted();	 Catch:{ RuntimeException -> 0x0140 }
        if (r13 != 0) goto L_0x003f;
    L_0x00af:
        r9 = 0;
        r5 = 0;
        r0 = r18;
        r13 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13.select();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r13 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r10 = r13.selectedKeys();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r8 = r10.iterator();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
    L_0x00c4:
        r13 = r8.hasNext();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x01c0;
    L_0x00ca:
        r13 = r8.next();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r13;
        r0 = (java.nio.channels.SelectionKey) r0;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r9 = r0;
        r13 = r9.isValid();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x00c4;
    L_0x00d8:
        r13 = r9.isAcceptable();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x0149;
    L_0x00de:
        r0 = r18;
        r13 = r0.onConnect(r9);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 != 0) goto L_0x00f5;
    L_0x00e6:
        r9.cancel();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        goto L_0x00c4;
    L_0x00ea:
        r13 = move-exception;
        goto L_0x00a5;
    L_0x00ec:
        r7 = move-exception;
        r13 = 0;
        r0 = r18;
        r0.handleFatal(r13, r7);
        goto L_0x003f;
    L_0x00f5:
        r0 = r18;
        r13 = r0.server;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r4 = r13.accept();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = 0;
        r4.configureBlocking(r13);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r13 = r0.wsf;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r14 = r0.drafts;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r15 = r4.socket();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r12 = r13.createWebSocket(r0, r14, r15);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r13 = r0.selector;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r14 = 1;
        r13 = r4.register(r13, r14, r12);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r12.key = r13;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r13 = r0.wsf;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r14 = r12.key;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = r13.wrapChannel(r4, r14);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r12.channel = r13;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r8.remove();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r18;
        r0.allocateBuffers(r12);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        goto L_0x00c4;
    L_0x0133:
        r7 = move-exception;
        if (r9 == 0) goto L_0x0139;
    L_0x0136:
        r9.cancel();	 Catch:{ RuntimeException -> 0x0140 }
    L_0x0139:
        r0 = r18;
        r0.handleIOException(r9, r5, r7);	 Catch:{ RuntimeException -> 0x0140 }
        goto L_0x00a5;
    L_0x0140:
        r6 = move-exception;
        r13 = 0;
        r0 = r18;
        r0.handleFatal(r13, r6);
        goto L_0x003f;
    L_0x0149:
        r13 = r9.isReadable();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x0187;
    L_0x014f:
        r13 = r9.attachment();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r13;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r5 = r0;
        r2 = r18.takeBuffer();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = r5.channel;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13 = org.java_websocket.SocketChannelIOHelper.read(r2, r5, r13);	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x01ac;
    L_0x0163:
        r13 = r5.inQueue;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13.put(r2);	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r0 = r18;
        r0.queue(r5);	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r8.remove();	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13 = r5.channel;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13 = r13 instanceof org.java_websocket.WrappedByteChannel;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x0187;
    L_0x0176:
        r13 = r5.channel;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13 = (org.java_websocket.WrappedByteChannel) r13;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13 = r13.isNeedRead();	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x0187;
    L_0x0180:
        r0 = r18;
        r13 = r0.iqueue;	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        r13.add(r5);	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
    L_0x0187:
        r13 = r9.isWritable();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x00c4;
    L_0x018d:
        r13 = r9.attachment();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r13;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r5 = r0;
        r13 = r5.channel;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = org.java_websocket.SocketChannelIOHelper.batch(r5, r13);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x00c4;
    L_0x019d:
        r13 = r9.isValid();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 == 0) goto L_0x00c4;
    L_0x01a3:
        r13 = 1;
        r9.interestOps(r13);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        goto L_0x00c4;
    L_0x01a9:
        r6 = move-exception;
        goto L_0x003f;
    L_0x01ac:
        r0 = r18;
        r0.pushBuffer(r2);	 Catch:{ IOException -> 0x01b2, RuntimeException -> 0x01b9, CancelledKeyException -> 0x00ea, InterruptedException -> 0x01a9 }
        goto L_0x0187;
    L_0x01b2:
        r6 = move-exception;
        r0 = r18;
        r0.pushBuffer(r2);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        throw r6;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
    L_0x01b9:
        r6 = move-exception;
        r0 = r18;
        r0.pushBuffer(r2);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        throw r6;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
    L_0x01c0:
        r0 = r18;
        r13 = r0.iqueue;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = r13.isEmpty();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        if (r13 != 0) goto L_0x00a5;
    L_0x01ca:
        r0 = r18;
        r13 = r0.iqueue;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r14 = 0;
        r13 = r13.remove(r14);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r0 = r13;
        r0 = (org.java_websocket.WebSocketImpl) r0;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r5 = r0;
        r3 = r5.channel;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r3 = (org.java_websocket.WrappedByteChannel) r3;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r2 = r18.takeBuffer();	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        r13 = org.java_websocket.SocketChannelIOHelper.readMore(r2, r5, r3);	 Catch:{ all -> 0x01fc }
        if (r13 == 0) goto L_0x01ec;
    L_0x01e5:
        r0 = r18;
        r13 = r0.iqueue;	 Catch:{ all -> 0x01fc }
        r13.add(r5);	 Catch:{ all -> 0x01fc }
    L_0x01ec:
        r13 = r5.inQueue;	 Catch:{ all -> 0x01fc }
        r13.put(r2);	 Catch:{ all -> 0x01fc }
        r0 = r18;
        r0.queue(r5);	 Catch:{ all -> 0x01fc }
        r0 = r18;
        r0.pushBuffer(r2);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        goto L_0x01c0;
    L_0x01fc:
        r13 = move-exception;
        r0 = r18;
        r0.pushBuffer(r2);	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        throw r13;	 Catch:{ CancelledKeyException -> 0x00ea, IOException -> 0x0133, InterruptedException -> 0x01a9 }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.java_websocket.server.WebSocketServer.run():void");
    }

    protected void allocateBuffers(WebSocket c) throws InterruptedException {
        if (this.queuesize.get() < (this.decoders.size() * 2) + 1) {
            this.queuesize.incrementAndGet();
            this.buffers.put(createBuffer());
        }
    }

    protected void releaseBuffers(WebSocket c) throws InterruptedException {
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(WebSocketImpl.RCVBUF);
    }

    private void queue(WebSocketImpl ws) throws InterruptedException {
        if (ws.workerThread == null) {
            ws.workerThread = (WebSocketWorker) this.decoders.get(this.queueinvokes % this.decoders.size());
            this.queueinvokes++;
        }
        ws.workerThread.put(ws);
    }

    private ByteBuffer takeBuffer() throws InterruptedException {
        return (ByteBuffer) this.buffers.take();
    }

    private void pushBuffer(ByteBuffer buf) throws InterruptedException {
        if (this.buffers.size() <= this.queuesize.intValue()) {
            this.buffers.put(buf);
        }
    }

    private void handleIOException(SelectionKey key, WebSocket conn, IOException ex) {
        onWebsocketError(conn, ex);
        if (conn != null) {
            conn.closeConnection(CloseFrame.ABNORMAL_CLOSE, ex.getMessage());
        } else if (key != null) {
            SelectableChannel channel = key.channel();
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                }
                if (WebSocketImpl.DEBUG) {
                    System.out.println("Connection closed because of" + ex);
                }
            }
        }
    }

    private void handleFatal(WebSocket conn, Exception e) {
        onError(conn, e);
        try {
            stop();
        } catch (IOException e1) {
            onError(null, e1);
        } catch (InterruptedException e12) {
            Thread.currentThread().interrupt();
            onError(null, e12);
        }
    }

    protected String getFlashSecurityPolicy() {
        return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + getPort() + "\" /></cross-domain-policy>";
    }

    public final void onWebsocketMessage(WebSocket conn, String message) {
        onMessage(conn, message);
    }

    public final void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, blob);
    }

    public final void onWebsocketOpen(WebSocket conn, Handshakedata handshake) {
        if (addConnection(conn)) {
            onOpen(conn, (ClientHandshake) handshake);
        }
    }

    public final void onWebsocketClose(WebSocket conn, int code, String reason, boolean remote) {
        this.selector.wakeup();
        try {
            if (removeConnection(conn)) {
                onClose(conn, code, reason, remote);
            }
        } finally {
            try {
                releaseBuffers(conn);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    protected boolean removeConnection(WebSocket ws) {
        boolean remove;
        synchronized (this.connections) {
            remove = this.connections.remove(ws);
        }
        return remove;
    }

    protected boolean addConnection(WebSocket ws) {
        boolean add;
        synchronized (this.connections) {
            add = this.connections.add(ws);
        }
        return add;
    }

    public final void onWebsocketError(WebSocket conn, Exception ex) {
        onError(conn, ex);
    }

    public final void onWriteDemand(WebSocket w) {
        WebSocketImpl conn = (WebSocketImpl) w;
        try {
            conn.key.interestOps(5);
        } catch (CancelledKeyException e) {
            conn.outQueue.clear();
        }
        this.selector.wakeup();
    }

    public void onWebsocketCloseInitiated(WebSocket conn, int code, String reason) {
        onCloseInitiated(conn, code, reason);
    }

    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
        onClosing(conn, code, reason, remote);
    }

    public void onCloseInitiated(WebSocket conn, int code, String reason) {
    }

    public void onClosing(WebSocket conn, int code, String reason, boolean remote) {
    }

    public final void setWebSocketFactory(WebSocketServerFactory wsf) {
        this.wsf = wsf;
    }

    public final WebSocketFactory getWebSocketFactory() {
        return this.wsf;
    }

    protected boolean onConnect(SelectionKey key) {
        return true;
    }

    private Socket getSocket(WebSocket conn) {
        return ((SocketChannel) ((WebSocketImpl) conn).key.channel()).socket();
    }

    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getLocalSocketAddress();
    }

    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return (InetSocketAddress) getSocket(conn).getRemoteSocketAddress();
    }

    public void onMessage(WebSocket conn, ByteBuffer message) {
    }
}
