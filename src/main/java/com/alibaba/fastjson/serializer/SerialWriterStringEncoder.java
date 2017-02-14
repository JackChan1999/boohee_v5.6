package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ThreadLocalCache;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class SerialWriterStringEncoder {
    private final CharsetEncoder encoder;

    public SerialWriterStringEncoder(Charset cs) {
        this(cs.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE));
    }

    public SerialWriterStringEncoder(CharsetEncoder encoder) {
        this.encoder = encoder;
    }

    public byte[] encode(char[] chars, int off, int len) {
        if (len == 0) {
            return new byte[0];
        }
        this.encoder.reset();
        return encode(chars, off, len, ThreadLocalCache.getBytes(scale(len, this.encoder.maxBytesPerChar())));
    }

    public CharsetEncoder getEncoder() {
        return this.encoder;
    }

    public byte[] encode(char[] chars, int off, int len, byte[] bytes) {
        ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
        try {
            CoderResult cr = this.encoder.encode(CharBuffer.wrap(chars, off, len), byteBuf, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = this.encoder.flush(byteBuf);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            int bytesLength = byteBuf.position();
            byte[] copy = new byte[bytesLength];
            System.arraycopy(bytes, 0, copy, 0, bytesLength);
            return copy;
        } catch (CharacterCodingException x) {
            throw new JSONException(x.getMessage(), x);
        }
    }

    private static int scale(int len, float expansionFactor) {
        return (int) (((double) len) * ((double) expansionFactor));
    }
}
