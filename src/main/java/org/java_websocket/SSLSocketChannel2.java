package org.java_websocket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class SSLSocketChannel2 implements ByteChannel, WrappedByteChannel {
    static final /* synthetic */ boolean $assertionsDisabled;
    protected static ByteBuffer emptybuffer = ByteBuffer.allocate(0);
    protected SSLEngineResult engineResult;
    private Status engineStatus = Status.BUFFER_UNDERFLOW;
    protected ExecutorService exec;
    protected ByteBuffer inCrypt;
    protected ByteBuffer inData;
    protected ByteBuffer outCrypt;
    protected SelectionKey selectionKey;
    protected SocketChannel socketChannel;
    protected SSLEngine sslEngine;
    protected List<Future<?>> tasks;

    static {
        boolean z;
        if (SSLSocketChannel2.class.desiredAssertionStatus()) {
            z = false;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
    }

    public SSLSocketChannel2(SocketChannel channel, SSLEngine sslEngine, ExecutorService exec, SelectionKey key) throws IOException {
        if (channel == null || sslEngine == null || exec == null) {
            throw new IllegalArgumentException("parameter must not be null");
        }
        this.socketChannel = channel;
        this.sslEngine = sslEngine;
        this.exec = exec;
        this.tasks = new ArrayList(3);
        if (key != null) {
            key.interestOps(key.interestOps() | 4);
            this.selectionKey = key;
        }
        createBuffers(sslEngine.getSession());
        this.socketChannel.write(wrap(emptybuffer));
        processHandshake();
    }

    private void consumeFutureUninterruptible(Future<?> f) {
        boolean interrupted = false;
        while (true) {
            try {
                f.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
        if (interrupted) {
            try {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private synchronized void processHandshake() throws IOException {
        if (this.engineResult.getHandshakeStatus() != HandshakeStatus.NOT_HANDSHAKING) {
            if (!this.tasks.isEmpty()) {
                Iterator<Future<?>> it = this.tasks.iterator();
                while (it.hasNext()) {
                    Future<?> f = (Future) it.next();
                    if (f.isDone()) {
                        it.remove();
                    } else if (isBlocking()) {
                        consumeFutureUninterruptible(f);
                    }
                }
            }
            if (this.engineResult.getHandshakeStatus() == HandshakeStatus.NEED_UNWRAP) {
                if (!isBlocking() || this.engineStatus == Status.BUFFER_UNDERFLOW) {
                    this.inCrypt.compact();
                    if (this.socketChannel.read(this.inCrypt) == -1) {
                        throw new IOException("connection closed unexpectedly by peer");
                    }
                    this.inCrypt.flip();
                }
                this.inData.compact();
                unwrap();
                if (this.engineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    createBuffers(this.sslEngine.getSession());
                }
            }
            consumeDelegatedTasks();
            if (!$assertionsDisabled && this.engineResult.getHandshakeStatus() == HandshakeStatus.NOT_HANDSHAKING) {
                throw new AssertionError();
            } else if (this.tasks.isEmpty() || this.engineResult.getHandshakeStatus() == HandshakeStatus.NEED_WRAP) {
                this.socketChannel.write(wrap(emptybuffer));
                if (this.engineResult.getHandshakeStatus() == HandshakeStatus.FINISHED) {
                    createBuffers(this.sslEngine.getSession());
                }
            }
        }
    }

    private synchronized ByteBuffer wrap(ByteBuffer b) throws SSLException {
        this.outCrypt.compact();
        this.engineResult = this.sslEngine.wrap(b, this.outCrypt);
        this.outCrypt.flip();
        return this.outCrypt;
    }

    private synchronized ByteBuffer unwrap() throws SSLException {
        while (true) {
            int rem = this.inData.remaining();
            this.engineResult = this.sslEngine.unwrap(this.inCrypt, this.inData);
            this.engineStatus = this.engineResult.getStatus();
            if (this.engineStatus != Status.OK || (rem == this.inData.remaining() && this.engineResult.getHandshakeStatus() != HandshakeStatus.NEED_UNWRAP)) {
                this.inData.flip();
            }
        }
        this.inData.flip();
        return this.inData;
    }

    protected void consumeDelegatedTasks() {
        while (true) {
            Runnable task = this.sslEngine.getDelegatedTask();
            if (task != null) {
                this.tasks.add(this.exec.submit(task));
            } else {
                return;
            }
        }
    }

    protected void createBuffers(SSLSession session) {
        int appBufferMax = session.getApplicationBufferSize();
        int netBufferMax = session.getPacketBufferSize();
        if (this.inData == null) {
            this.inData = ByteBuffer.allocate(appBufferMax);
            this.outCrypt = ByteBuffer.allocate(netBufferMax);
            this.inCrypt = ByteBuffer.allocate(netBufferMax);
        } else {
            if (this.inData.capacity() != appBufferMax) {
                this.inData = ByteBuffer.allocate(appBufferMax);
            }
            if (this.outCrypt.capacity() != netBufferMax) {
                this.outCrypt = ByteBuffer.allocate(netBufferMax);
            }
            if (this.inCrypt.capacity() != netBufferMax) {
                this.inCrypt = ByteBuffer.allocate(netBufferMax);
            }
        }
        this.inData.rewind();
        this.inData.flip();
        this.inCrypt.rewind();
        this.inCrypt.flip();
        this.outCrypt.rewind();
        this.outCrypt.flip();
    }

    public int write(ByteBuffer src) throws IOException {
        if (isHandShakeComplete()) {
            return this.socketChannel.write(wrap(src));
        }
        processHandshake();
        return 0;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (!dst.hasRemaining()) {
            return 0;
        }
        if (!isHandShakeComplete()) {
            if (isBlocking()) {
                while (!isHandShakeComplete()) {
                    processHandshake();
                }
            } else {
                processHandshake();
                if (!isHandShakeComplete()) {
                    return 0;
                }
            }
        }
        int purged = readRemaining(dst);
        if (purged != 0) {
            return purged;
        }
        if ($assertionsDisabled || this.inData.position() == 0) {
            this.inData.clear();
            if (this.inCrypt.hasRemaining()) {
                this.inCrypt.compact();
            } else {
                this.inCrypt.clear();
            }
            if (((isBlocking() && this.inCrypt.position() == 0) || this.engineStatus == Status.BUFFER_UNDERFLOW) && this.socketChannel.read(this.inCrypt) == -1) {
                return -1;
            }
            this.inCrypt.flip();
            unwrap();
            int transfered = transfereTo(this.inData, dst);
            return (transfered == 0 && isBlocking()) ? read(dst) : transfered;
        } else {
            throw new AssertionError();
        }
    }

    private int readRemaining(ByteBuffer dst) throws SSLException {
        if (this.inData.hasRemaining()) {
            return transfereTo(this.inData, dst);
        }
        if (!this.inData.hasRemaining()) {
            this.inData.clear();
        }
        if (this.inCrypt.hasRemaining()) {
            unwrap();
            int amount = transfereTo(this.inData, dst);
            if (amount > 0) {
                return amount;
            }
        }
        return 0;
    }

    public boolean isConnected() {
        return this.socketChannel.isConnected();
    }

    public void close() throws IOException {
        this.sslEngine.closeOutbound();
        this.sslEngine.getSession().invalidate();
        if (this.socketChannel.isOpen()) {
            this.socketChannel.write(wrap(emptybuffer));
        }
        this.socketChannel.close();
    }

    private boolean isHandShakeComplete() {
        HandshakeStatus status = this.engineResult.getHandshakeStatus();
        return status == HandshakeStatus.FINISHED || status == HandshakeStatus.NOT_HANDSHAKING;
    }

    public SelectableChannel configureBlocking(boolean b) throws IOException {
        return this.socketChannel.configureBlocking(b);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return this.socketChannel.connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return this.socketChannel.finishConnect();
    }

    public Socket socket() {
        return this.socketChannel.socket();
    }

    public boolean isInboundDone() {
        return this.sslEngine.isInboundDone();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen();
    }

    public boolean isNeedWrite() {
        return this.outCrypt.hasRemaining() || !isHandShakeComplete();
    }

    public void writeMore() throws IOException {
        write(this.outCrypt);
    }

    public boolean isNeedRead() {
        return this.inData.hasRemaining() || (this.inCrypt.hasRemaining() && this.engineResult.getStatus() != Status.BUFFER_UNDERFLOW);
    }

    public int readMore(ByteBuffer dst) throws SSLException {
        return readRemaining(dst);
    }

    private int transfereTo(ByteBuffer from, ByteBuffer to) {
        int fremain = from.remaining();
        int toremain = to.remaining();
        if (fremain > toremain) {
            int min = Math.min(fremain, toremain);
            for (int i = 0; i < min; i++) {
                to.put(from.get());
            }
            return min;
        }
        to.put(from);
        return fremain;
    }

    public boolean isBlocking() {
        return this.socketChannel.isBlocking();
    }
}
