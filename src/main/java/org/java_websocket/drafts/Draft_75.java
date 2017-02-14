package org.java_websocket.drafts;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.java_websocket.drafts.Draft.CloseHandshakeType;
import org.java_websocket.drafts.Draft.HandshakeState;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.NotSendableException;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.util.Charsetfunctions;

public class Draft_75 extends Draft {
    public static final byte CR = (byte) 13;
    public static final byte END_OF_FRAME = (byte) -1;
    public static final byte LF = (byte) 10;
    public static final byte START_OF_FRAME = (byte) 0;
    protected ByteBuffer currentFrame;
    private boolean inframe = false;
    protected boolean readingState = false;
    protected List<Framedata> readyframes = new LinkedList();
    private final Random reuseableRandom = new Random();

    public HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) {
        return (request.getFieldValue("WebSocket-Origin").equals(response.getFieldValue("Origin")) && basicAccept(response)) ? HandshakeState.MATCHED : HandshakeState.NOT_MATCHED;
    }

    public HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) {
        if (handshakedata.hasFieldValue("Origin") && basicAccept(handshakedata)) {
            return HandshakeState.MATCHED;
        }
        return HandshakeState.NOT_MATCHED;
    }

    public ByteBuffer createBinaryFrame(Framedata framedata) {
        if (framedata.getOpcode() != Opcode.TEXT) {
            throw new RuntimeException("only text frames supported");
        }
        ByteBuffer pay = framedata.getPayloadData();
        ByteBuffer b = ByteBuffer.allocate(pay.remaining() + 2);
        b.put((byte) 0);
        pay.mark();
        b.put(pay);
        pay.reset();
        b.put((byte) -1);
        b.flip();
        return b;
    }

    public List<Framedata> createFrames(ByteBuffer binary, boolean mask) {
        throw new RuntimeException("not yet implemented");
    }

    public List<Framedata> createFrames(String text, boolean mask) {
        FrameBuilder frame = new FramedataImpl1();
        try {
            frame.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(text)));
            frame.setFin(true);
            frame.setOptcode(Opcode.TEXT);
            frame.setTransferemasked(mask);
            return Collections.singletonList(frame);
        } catch (Throwable e) {
            throw new NotSendableException(e);
        }
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) throws InvalidHandshakeException {
        request.put("Upgrade", "WebSocket");
        request.put("Connection", "Upgrade");
        if (!request.hasFieldValue("Origin")) {
            request.put("Origin", "random" + this.reuseableRandom.nextInt());
        }
        return request;
    }

    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.setHttpStatusMessage("Web Socket Protocol Handshake");
        response.put("Upgrade", "WebSocket");
        response.put("Connection", request.getFieldValue("Connection"));
        response.put("WebSocket-Origin", request.getFieldValue("Origin"));
        response.put("WebSocket-Location", "ws://" + request.getFieldValue("Host") + request.getResourceDescriptor());
        return response;
    }

    protected List<Framedata> translateRegularFrame(ByteBuffer buffer) throws InvalidDataException {
        while (buffer.hasRemaining()) {
            byte newestByte = buffer.get();
            if (newestByte == (byte) 0) {
                if (this.readingState) {
                    return null;
                }
                this.readingState = true;
            } else if (newestByte == (byte) -1) {
                if (!this.readingState) {
                    return null;
                }
                if (this.currentFrame != null) {
                    this.currentFrame.flip();
                    FramedataImpl1 curframe = new FramedataImpl1();
                    curframe.setPayload(this.currentFrame);
                    curframe.setFin(true);
                    curframe.setOptcode(this.inframe ? Opcode.CONTINUOUS : Opcode.TEXT);
                    this.readyframes.add(curframe);
                    this.currentFrame = null;
                    buffer.mark();
                }
                this.readingState = false;
                this.inframe = false;
            } else if (!this.readingState) {
                return null;
            } else {
                if (this.currentFrame == null) {
                    this.currentFrame = createBuffer();
                } else if (!this.currentFrame.hasRemaining()) {
                    this.currentFrame = increaseBuffer(this.currentFrame);
                }
                this.currentFrame.put(newestByte);
            }
        }
        if (this.readingState) {
            curframe = new FramedataImpl1();
            this.currentFrame.flip();
            curframe.setPayload(this.currentFrame);
            curframe.setFin(false);
            curframe.setOptcode(this.inframe ? Opcode.CONTINUOUS : Opcode.TEXT);
            this.inframe = true;
            this.readyframes.add(curframe);
        }
        List<Framedata> frames = this.readyframes;
        this.readyframes = new LinkedList();
        this.currentFrame = null;
        return frames;
    }

    public List<Framedata> translateFrame(ByteBuffer buffer) throws InvalidDataException {
        List<Framedata> frames = translateRegularFrame(buffer);
        if (frames != null) {
            return frames;
        }
        throw new InvalidDataException(1002);
    }

    public void reset() {
        this.readingState = false;
        this.currentFrame = null;
    }

    public CloseHandshakeType getCloseHandshakeType() {
        return CloseHandshakeType.NONE;
    }

    public ByteBuffer createBuffer() {
        return ByteBuffer.allocate(INITIAL_FAMESIZE);
    }

    public ByteBuffer increaseBuffer(ByteBuffer full) {
        full.flip();
        ByteBuffer newbuffer = ByteBuffer.allocate(full.capacity() * 2);
        newbuffer.put(full);
        return newbuffer;
    }

    public Draft copyInstance() {
        return new Draft_75();
    }
}
