package com.zxinsight.common.http;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class ad extends BufferedOutputStream {
    private final CharsetEncoder a;

    public ad(OutputStream outputStream, String str, int i) {
        super(outputStream, i);
        this.a = Charset.forName(f.b(str)).newEncoder();
    }

    public ad a(String str) {
        ByteBuffer encode = this.a.encode(CharBuffer.wrap(str));
        super.write(encode.array(), 0, encode.limit());
        return this;
    }
}
