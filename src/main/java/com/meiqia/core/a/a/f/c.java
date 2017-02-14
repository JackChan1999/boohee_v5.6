package com.meiqia.core.a.a.f;

import com.meiqia.core.a.a.c.b;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import org.java_websocket.framing.CloseFrame;

public class c {
    public static CodingErrorAction a = CodingErrorAction.REPORT;

    public static String a(ByteBuffer byteBuffer) {
        CharsetDecoder newDecoder = Charset.forName("UTF8").newDecoder();
        newDecoder.onMalformedInput(a);
        newDecoder.onUnmappableCharacter(a);
        try {
            byteBuffer.mark();
            String charBuffer = newDecoder.decode(byteBuffer).toString();
            byteBuffer.reset();
            return charBuffer;
        } catch (Throwable e) {
            throw new b((int) CloseFrame.NO_UTF8, e);
        }
    }

    public static String a(byte[] bArr, int i, int i2) {
        try {
            return new String(bArr, i, i2, "ASCII");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] a(String str) {
        try {
            return str.getBytes("UTF8");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] b(String str) {
        try {
            return str.getBytes("ASCII");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
