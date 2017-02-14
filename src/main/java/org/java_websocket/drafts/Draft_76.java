package org.java_websocket.drafts;

import com.boohee.utils.Coder;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.java_websocket.WebSocket.Role;
import org.java_websocket.drafts.Draft.CloseHandshakeType;
import org.java_websocket.drafts.Draft.HandshakeState;
import org.java_websocket.exceptions.IncompleteHandshakeException;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.exceptions.InvalidFrameException;
import org.java_websocket.exceptions.InvalidHandshakeException;
import org.java_websocket.framing.CloseFrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.HandshakeBuilder;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

public class Draft_76 extends Draft_75 {
    private static final byte[] closehandshake = new byte[]{(byte) -1, (byte) 0};
    private boolean failed = false;
    private final Random reuseableRandom = new Random();

    public static byte[] createChallenge(String key1, String key2, byte[] key3) throws InvalidHandshakeException {
        byte[] part1 = getPart(key1);
        byte[] part2 = getPart(key2);
        try {
            return MessageDigest.getInstance(Coder.KEY_MD5).digest(new byte[]{part1[0], part1[1], part1[2], part1[3], part2[0], part2[1], part2[2], part2[3], key3[0], key3[1], key3[2], key3[3], key3[4], key3[5], key3[6], key3[7]});
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateKey() {
        int i;
        Random r = new Random();
        long spaces = (long) (r.nextInt(12) + 1);
        String key = Long.toString(((long) (r.nextInt(Math.abs(new Long(4294967295L / spaces).intValue())) + 1)) * spaces);
        int numChars = r.nextInt(12) + 1;
        for (i = 0; i < numChars; i++) {
            int position = Math.abs(r.nextInt(key.length()));
            char randChar = (char) (r.nextInt(95) + 33);
            if (randChar >= '0' && randChar <= '9') {
                randChar = (char) (randChar - 15);
            }
            key = position;
        }
        for (i = 0; ((long) i) < spaces; i++) {
            key = Math.abs(r.nextInt(key.length() - 1) + 1);
        }
        return key;
    }

    private static byte[] getPart(String key) throws InvalidHandshakeException {
        try {
            long keyNumber = Long.parseLong(key.replaceAll("[^0-9]", ""));
            long keySpace = (long) (key.split(" ").length - 1);
            if (keySpace == 0) {
                throw new InvalidHandshakeException("invalid Sec-WebSocket-Key (/key2/)");
            }
            long part = new Long(keyNumber / keySpace).longValue();
            return new byte[]{(byte) ((int) (part >> 24)), (byte) ((int) ((part << 8) >> 24)), (byte) ((int) ((part << 16) >> 24)), (byte) ((int) ((part << 24) >> 24))};
        } catch (NumberFormatException e) {
            throw new InvalidHandshakeException("invalid Sec-WebSocket-Key (/key1/ or /key2/)");
        }
    }

    public HandshakeState acceptHandshakeAsClient(ClientHandshake request, ServerHandshake response) {
        if (this.failed) {
            return HandshakeState.NOT_MATCHED;
        }
        try {
            if (!response.getFieldValue("Sec-WebSocket-Origin").equals(request.getFieldValue("Origin")) || !basicAccept(response)) {
                return HandshakeState.NOT_MATCHED;
            }
            byte[] content = response.getContent();
            if (content == null || content.length == 0) {
                throw new IncompleteHandshakeException();
            } else if (Arrays.equals(content, createChallenge(request.getFieldValue("Sec-WebSocket-Key1"), request.getFieldValue("Sec-WebSocket-Key2"), request.getContent()))) {
                return HandshakeState.MATCHED;
            } else {
                return HandshakeState.NOT_MATCHED;
            }
        } catch (InvalidHandshakeException e) {
            throw new RuntimeException("bad handshakerequest", e);
        }
    }

    public HandshakeState acceptHandshakeAsServer(ClientHandshake handshakedata) {
        if (handshakedata.getFieldValue("Upgrade").equals("WebSocket") && handshakedata.getFieldValue("Connection").contains("Upgrade") && handshakedata.getFieldValue("Sec-WebSocket-Key1").length() > 0 && !handshakedata.getFieldValue("Sec-WebSocket-Key2").isEmpty() && handshakedata.hasFieldValue("Origin")) {
            return HandshakeState.MATCHED;
        }
        return HandshakeState.NOT_MATCHED;
    }

    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        request.put("Upgrade", "WebSocket");
        request.put("Connection", "Upgrade");
        request.put("Sec-WebSocket-Key1", generateKey());
        request.put("Sec-WebSocket-Key2", generateKey());
        if (!request.hasFieldValue("Origin")) {
            request.put("Origin", "random" + this.reuseableRandom.nextInt());
        }
        byte[] key3 = new byte[8];
        this.reuseableRandom.nextBytes(key3);
        request.setContent(key3);
        return request;
    }

    public HandshakeBuilder postProcessHandshakeResponseAsServer(ClientHandshake request, ServerHandshakeBuilder response) throws InvalidHandshakeException {
        response.setHttpStatusMessage("WebSocket Protocol Handshake");
        response.put("Upgrade", "WebSocket");
        response.put("Connection", request.getFieldValue("Connection"));
        response.put("Sec-WebSocket-Origin", request.getFieldValue("Origin"));
        response.put("Sec-WebSocket-Location", "ws://" + request.getFieldValue("Host") + request.getResourceDescriptor());
        String key1 = request.getFieldValue("Sec-WebSocket-Key1");
        String key2 = request.getFieldValue("Sec-WebSocket-Key2");
        byte[] key3 = request.getContent();
        if (key1 == null || key2 == null || key3 == null || key3.length != 8) {
            throw new InvalidHandshakeException("Bad keys");
        }
        response.setContent(createChallenge(key1, key2, key3));
        return response;
    }

    public Handshakedata translateHandshake(ByteBuffer buf) throws InvalidHandshakeException {
        HandshakeBuilder bui = Draft.translateHandshakeHttp(buf, this.role);
        if ((bui.hasFieldValue("Sec-WebSocket-Key1") || this.role == Role.CLIENT) && !bui.hasFieldValue("Sec-WebSocket-Version")) {
            byte[] key3 = new byte[(this.role == Role.SERVER ? 8 : 16)];
            try {
                buf.get(key3);
                bui.setContent(key3);
            } catch (BufferUnderflowException e) {
                throw new IncompleteHandshakeException(buf.capacity() + 16);
            }
        }
        return bui;
    }

    public List<Framedata> translateFrame(ByteBuffer buffer) throws InvalidDataException {
        buffer.mark();
        List<Framedata> frames = super.translateRegularFrame(buffer);
        if (frames != null) {
            return frames;
        }
        buffer.reset();
        frames = this.readyframes;
        this.readingState = true;
        if (this.currentFrame == null) {
            this.currentFrame = ByteBuffer.allocate(2);
            if (buffer.remaining() > this.currentFrame.remaining()) {
                throw new InvalidFrameException();
            }
            this.currentFrame.put(buffer);
            if (this.currentFrame.hasRemaining()) {
                this.readyframes = new LinkedList();
                return frames;
            } else if (Arrays.equals(this.currentFrame.array(), closehandshake)) {
                frames.add(new CloseFrameBuilder(1000));
                return frames;
            } else {
                throw new InvalidFrameException();
            }
        }
        throw new InvalidFrameException();
    }

    public ByteBuffer createBinaryFrame(Framedata framedata) {
        if (framedata.getOpcode() == Opcode.CLOSING) {
            return ByteBuffer.wrap(closehandshake);
        }
        return super.createBinaryFrame(framedata);
    }

    public CloseHandshakeType getCloseHandshakeType() {
        return CloseHandshakeType.ONEWAY;
    }

    public Draft copyInstance() {
        return new Draft_76();
    }
}
