package org.java_websocket.drafts;

import com.tencent.tinker.android.dx.instruction.Opcodes;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.java_websocket.WebSocket.Role;
import org.java_websocket.drafts.Draft.CloseHandshakeType;
import org.java_websocket.drafts.Draft.HandshakeState;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.exceptions.LimitExedeedException;
import org.java_websocket.exceptions.NotSendableException;
import org.java_websocket.framing.CloseFrameBuilder;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.java_websocket.util.Base64;
import org.java_websocket.util.Charsetfunctions;

public class Draft_10 extends Draft {
    static final /* synthetic */ boolean $assertionsDisabled = (!Draft_10.class.desiredAssertionStatus());
    private Framedata fragmentedframe = null;
    private ByteBuffer incompleteframe;
    private final Random reuseableRandom = new Random();

    private class IncompleteException extends Throwable {
        private static final long serialVersionUID = 7330519489840500997L;
        private int preferedsize;

        public IncompleteException(int preferedsize) {
            this.preferedsize = preferedsize;
        }

        public int getPreferedSize() {
            return this.preferedsize;
        }
    }

    public static int readVersion(Handshakedata handshakedata) {
        int i = -1;
        String vers = handshakedata.getFieldValue("Sec-WebSocket-Version");
        if (vers.length() > 0) {
            try {
                i = new Integer(vers.trim()).intValue();
            } catch (NumberFormatException e) {
            }
        }
        return i;
    }

    public HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) throws InvalidHandshakeException {
        if (!request.hasFieldValue("Sec-WebSocket-Key") || !response.hasFieldValue("Sec-WebSocket-Accept")) {
            return HandshakeState.NOT_MATCHED;
        }
        if (generateFinalKey(request.getFieldValue("Sec-WebSocket-Key")).equals(response.getFieldValue("Sec-WebSocket-Accept"))) {
            return HandshakeState.MATCHED;
        }
        return HandshakeState.NOT_MATCHED;
    }

    public HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) throws InvalidHandshakeException {
        int v = readVersion(handshakedata);
        if (v == 7 || v == 8) {
            return basicAccept(handshakedata) ? HandshakeState.MATCHED : HandshakeState.NOT_MATCHED;
        } else {
            return HandshakeState.NOT_MATCHED;
        }
    }

    public ByteBuffer createBinaryFrame(Framedata framedata) {
        int i;
        ByteBuffer mes = framedata.getPayloadData();
        boolean mask = this.role == Role.CLIENT;
        int sizebytes = mes.remaining() <= Opcodes.NEG_LONG ? 1 : mes.remaining() <= 65535 ? 2 : 8;
        if (sizebytes > 1) {
            i = sizebytes + 1;
        } else {
            i = sizebytes;
        }
        ByteBuffer buf = ByteBuffer.allocate(((mask ? 4 : 0) + (i + 1)) + mes.remaining());
        buf.put((byte) (((byte) (framedata.isFin() ? -128 : 0)) | fromOpcode(framedata.getOpcode())));
        byte[] payloadlengthbytes = toByteArray((long) mes.remaining(), sizebytes);
        if ($assertionsDisabled || payloadlengthbytes.length == sizebytes) {
            if (sizebytes == 1) {
                buf.put((byte) ((mask ? -128 : 0) | payloadlengthbytes[0]));
            } else if (sizebytes == 2) {
                buf.put((byte) ((mask ? -128 : 0) | 126));
                buf.put(payloadlengthbytes);
            } else if (sizebytes == 8) {
                buf.put((byte) ((mask ? -128 : 0) | 127));
                buf.put(payloadlengthbytes);
            } else {
                throw new RuntimeException("Size representation not supported/specified");
            }
            if (mask) {
                ByteBuffer maskkey = ByteBuffer.allocate(4);
                maskkey.putInt(this.reuseableRandom.nextInt());
                buf.put(maskkey.array());
                for (int i2 = 0; i2 < mes.limit(); i2++) {
                    buf.put((byte) (mes.get() ^ maskkey.get(i2 % 4)));
                }
            } else {
                buf.put(mes);
            }
            if ($assertionsDisabled || buf.remaining() == 0) {
                buf.flip();
                return buf;
            }
            throw new AssertionError(buf.remaining());
        }
        throw new AssertionError();
    }

    public List<Framedata> createFrames(ByteBuffer binary, boolean mask) {
        FrameBuilder curframe = new FramedataImpl1();
        try {
            curframe.setPayload(binary);
            curframe.setFin(true);
            curframe.setOptcode(Opcode.BINARY);
            curframe.setTransferemasked(mask);
            return Collections.singletonList(curframe);
        } catch (Throwable e) {
            throw new NotSendableException(e);
        }
    }

    public List<Framedata> createFrames(String text, boolean mask) {
        FrameBuilder curframe = new FramedataImpl1();
        try {
            curframe.setPayload(ByteBuffer.wrap(Charsetfunctions.utf8Bytes(text)));
            curframe.setFin(true);
            curframe.setOptcode(Opcode.TEXT);
            curframe.setTransferemasked(mask);
            return Collections.singletonList(curframe);
        } catch (Throwable e) {
            throw new NotSendableException(e);
        }
    }

    private byte fromOpcode(Opcode opcode) {
        if (opcode == Opcode.CONTINUOUS) {
            return (byte) 0;
        }
        if (opcode == Opcode.TEXT) {
            return (byte) 1;
        }
        if (opcode == Opcode.BINARY) {
            return (byte) 2;
        }
        if (opcode == Opcode.CLOSING) {
            return (byte) 8;
        }
        if (opcode == Opcode.PING) {
            return (byte) 9;
        }
        if (opcode == Opcode.PONG) {
            return (byte) 10;
        }
        throw new RuntimeException("Don't know how to handle " + opcode.toString());
    }

    private String generateFinalKey(String in) {
        String acc = in.trim() + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        try {
            return Base64.encodeBytes(MessageDigest.getInstance("SHA1").digest(acc.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        request.put("Upgrade", "websocket");
        request.put("Connection", "Upgrade");
        request.put("Sec-WebSocket-Version", "8");
        byte[] random = new byte[16];
        this.reuseableRandom.nextBytes(random);
        request.put("Sec-WebSocket-Key", Base64.encodeBytes(random));
        return request;
    }

    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.put("Upgrade", "websocket");
        response.put("Connection", request.getFieldValue("Connection"));
        response.setHttpStatusMessage("Switching Protocols");
        String seckey = request.getFieldValue("Sec-WebSocket-Key");
        if (seckey == null) {
            throw new InvalidHandshakeException("missing Sec-WebSocket-Key");
        }
        response.put("Sec-WebSocket-Accept", generateFinalKey(seckey));
        return response;
    }

    private byte[] toByteArray(long val, int bytecount) {
        byte[] buffer = new byte[bytecount];
        int highest = (bytecount * 8) - 8;
        for (int i = 0; i < bytecount; i++) {
            buffer[i] = (byte) ((int) (val >>> (highest - (i * 8))));
        }
        return buffer;
    }

    private Opcode toOpcode(byte opcode) throws InvalidFrameException {
        switch (opcode) {
            case (byte) 0:
                return Opcode.CONTINUOUS;
            case (byte) 1:
                return Opcode.TEXT;
            case (byte) 2:
                return Opcode.BINARY;
            case (byte) 8:
                return Opcode.CLOSING;
            case (byte) 9:
                return Opcode.PING;
            case (byte) 10:
                return Opcode.PONG;
            default:
                throw new InvalidFrameException("unknow optcode " + ((short) opcode));
        }
    }

    public List<Framedata> translateFrame(ByteBuffer buffer) throws LimitExedeedException, InvalidDataException {
        List<Framedata> frames = new LinkedList();
        if (this.incompleteframe != null) {
            try {
                buffer.mark();
                int available_next_byte_count = buffer.remaining();
                int expected_next_byte_count = this.incompleteframe.remaining();
                if (expected_next_byte_count > available_next_byte_count) {
                    this.incompleteframe.put(buffer.array(), buffer.position(), available_next_byte_count);
                    buffer.position(buffer.position() + available_next_byte_count);
                    return Collections.emptyList();
                }
                this.incompleteframe.put(buffer.array(), buffer.position(), expected_next_byte_count);
                buffer.position(buffer.position() + expected_next_byte_count);
                frames.add(translateSingleFrame((ByteBuffer) this.incompleteframe.duplicate().position(0)));
                this.incompleteframe = null;
            } catch (IncompleteException e) {
                int oldsize = this.incompleteframe.limit();
                extendedframe = ByteBuffer.allocate(checkAlloc(e.getPreferedSize()));
                if ($assertionsDisabled || extendedframe.limit() > this.incompleteframe.limit()) {
                    ByteBuffer extendedframe;
                    this.incompleteframe.rewind();
                    extendedframe.put(this.incompleteframe);
                    this.incompleteframe = extendedframe;
                    return translateFrame(buffer);
                }
                throw new AssertionError();
            }
        }
        while (buffer.hasRemaining()) {
            buffer.mark();
            try {
                frames.add(translateSingleFrame(buffer));
            } catch (IncompleteException e2) {
                buffer.reset();
                this.incompleteframe = ByteBuffer.allocate(checkAlloc(e2.getPreferedSize()));
                this.incompleteframe.put(buffer);
                return frames;
            }
        }
        return frames;
    }

    public Framedata translateSingleFrame(ByteBuffer buffer) throws IncompleteException, InvalidDataException {
        int maxpacketsize = buffer.remaining();
        int realpacketsize = 2;
        if (maxpacketsize < 2) {
            throw new IncompleteException(2);
        }
        byte b1 = buffer.get();
        boolean FIN = (b1 >> 8) != 0;
        byte rsv = (byte) ((b1 & 127) >> 4);
        if (rsv != (byte) 0) {
            throw new InvalidFrameException("bad rsv " + rsv);
        }
        byte b2 = buffer.get();
        boolean MASK = (b2 & -128) != 0;
        int payloadlength = (byte) (b2 & 127);
        Opcode optcode = toOpcode((byte) (b1 & 15));
        if (FIN || !(optcode == Opcode.PING || optcode == Opcode.PONG || optcode == Opcode.CLOSING)) {
            int i;
            if (payloadlength < (byte) 0 || payloadlength > (byte) 125) {
                if (optcode == Opcode.PING || optcode == Opcode.PONG || optcode == Opcode.CLOSING) {
                    throw new InvalidFrameException("more than 125 octets");
                } else if (payloadlength == 126) {
                    realpacketsize = 2 + 2;
                    if (maxpacketsize < realpacketsize) {
                        throw new IncompleteException(realpacketsize);
                    }
                    byte[] sizebytes = new byte[3];
                    sizebytes[1] = buffer.get();
                    sizebytes[2] = buffer.get();
                    payloadlength = new BigInteger(sizebytes).intValue();
                } else {
                    realpacketsize = 2 + 8;
                    if (maxpacketsize < realpacketsize) {
                        throw new IncompleteException(realpacketsize);
                    }
                    byte[] bytes = new byte[8];
                    for (i = 0; i < 8; i++) {
                        bytes[i] = buffer.get();
                    }
                    long length = new BigInteger(bytes).longValue();
                    if (length > 2147483647L) {
                        throw new LimitExedeedException("Payloadsize is to big...");
                    }
                    payloadlength = (int) length;
                }
            }
            realpacketsize = (realpacketsize + (MASK ? 4 : 0)) + payloadlength;
            if (maxpacketsize < realpacketsize) {
                throw new IncompleteException(realpacketsize);
            }
            FrameBuilder frame;
            ByteBuffer payload = ByteBuffer.allocate(checkAlloc(payloadlength));
            if (MASK) {
                byte[] maskskey = new byte[4];
                buffer.get(maskskey);
                for (i = 0; i < payloadlength; i++) {
                    payload.put((byte) (buffer.get() ^ maskskey[i % 4]));
                }
            } else {
                payload.put(buffer.array(), buffer.position(), payload.limit());
                buffer.position(buffer.position() + payload.limit());
            }
            if (optcode == Opcode.CLOSING) {
                frame = new CloseFrameBuilder();
            } else {
                frame = new FramedataImpl1();
                frame.setFin(FIN);
                frame.setOptcode(optcode);
            }
            payload.flip();
            frame.setPayload(payload);
            return frame;
        }
        throw new InvalidFrameException("control frames may no be fragmented");
    }

    public void reset() {
        this.incompleteframe = null;
    }

    public Draft copyInstance() {
        return new Draft_10();
    }

    public CloseHandshakeType getCloseHandshakeType() {
        return CloseHandshakeType.TWOWAY;
    }
}
