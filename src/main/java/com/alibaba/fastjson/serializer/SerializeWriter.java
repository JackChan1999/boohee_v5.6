package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;
import com.tencent.tinker.android.dex.DexFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.nio.charset.Charset;

public final class SerializeWriter extends Writer {
    private static final ThreadLocal<SoftReference<char[]>> bufLocal = new ThreadLocal();
    protected char[] buf;
    protected int count;
    private int features;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer) {
        this.writer = writer;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;
        SoftReference<char[]> ref = (SoftReference) bufLocal.get();
        if (ref != null) {
            this.buf = (char[]) ref.get();
            bufLocal.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... features) {
        this(null, features);
    }

    public SerializeWriter(Writer writer, SerializerFeature... features) {
        this.writer = writer;
        SoftReference<char[]> ref = (SoftReference) bufLocal.get();
        if (ref != null) {
            this.buf = (char[]) ref.get();
            bufLocal.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[1024];
        }
        int featuresValue = 0;
        for (SerializerFeature feature : features) {
            featuresValue |= feature.getMask();
        }
        this.features = featuresValue;
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int initialSize) {
        this(null, initialSize);
    }

    public SerializeWriter(Writer writer, int initialSize) {
        this.writer = writer;
        if (initialSize <= 0) {
            throw new IllegalArgumentException("Negative initial size: " + initialSize);
        }
        this.buf = new char[initialSize];
    }

    public void config(SerializerFeature feature, boolean state) {
        if (state) {
            this.features |= feature.getMask();
        } else {
            this.features &= feature.getMask() ^ -1;
        }
    }

    public boolean isEnabled(SerializerFeature feature) {
        return SerializerFeature.isEnabled(this.features, feature);
    }

    public void write(int c) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        this.buf[this.count] = (char) c;
        this.count = newcount;
    }

    public void write(char c) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        this.buf[this.count] = c;
        this.count = newcount;
    }

    public void write(char[] c, int off, int len) {
        if (off < 0 || off > c.length || len < 0 || off + len > c.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len != 0) {
            int newcount = this.count + len;
            if (newcount > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(newcount);
                } else {
                    do {
                        int rest = this.buf.length - this.count;
                        System.arraycopy(c, off, this.buf, this.count, rest);
                        this.count = this.buf.length;
                        flush();
                        len -= rest;
                        off += rest;
                    } while (len > this.buf.length);
                    newcount = len;
                }
            }
            System.arraycopy(c, off, this.buf, this.count, len);
            this.count = newcount;
        }
    }

    public void expandCapacity(int minimumCapacity) {
        int newCapacity = ((this.buf.length * 3) / 2) + 1;
        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }
        char[] newValue = new char[newCapacity];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        this.buf = newValue;
    }

    public void write(String str, int off, int len) {
        int newcount = this.count + len;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                do {
                    int rest = this.buf.length - this.count;
                    str.getChars(off, off + rest, this.buf, this.count);
                    this.count = this.buf.length;
                    flush();
                    len -= rest;
                    off += rest;
                } while (len > this.buf.length);
                newcount = len;
            }
        }
        str.getChars(off, off + len, this.buf, this.count);
        this.count = newcount;
    }

    public void writeTo(Writer out) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        out.write(this.buf, 0, this.count);
    }

    public void writeTo(OutputStream out, String charsetName) throws IOException {
        writeTo(out, Charset.forName(charsetName));
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        out.write(new String(this.buf, 0, this.count).getBytes(charset));
    }

    public SerializeWriter append(CharSequence csq) {
        String s = csq == null ? "null" : csq.toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        String s = csq.subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write(c);
        return this;
    }

    public void reset() {
        this.count = 0;
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] newValue = new char[this.count];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        return newValue;
    }

    public byte[] toBytes(String charsetName) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        if (charsetName == null) {
            charsetName = "UTF-8";
        }
        return new SerialWriterStringEncoder(Charset.forName(charsetName)).encode(this.buf, 0, this.count);
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 8192) {
            bufLocal.set(new SoftReference(this.buf));
        }
        this.buf = null;
    }

    public void write(String text) {
        if (text == null) {
            writeNull();
        } else {
            write(text, 0, text.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int newcount = this.count + size;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }
        IOUtils.getChars(i, newcount, this.buf);
        this.count = newcount;
    }

    public void writeByteArray(byte[] bytes) {
        int bytesLen = bytes.length;
        boolean singleQuote = isEnabled(SerializerFeature.UseSingleQuotes);
        char quote = singleQuote ? '\'' : '\"';
        if (bytesLen == 0) {
            write(singleQuote ? "''" : "\"\"");
            return;
        }
        int s;
        int i;
        int left;
        char[] CA = Base64.CA;
        int eLen = (bytesLen / 3) * 3;
        int charsLen = (((bytesLen - 1) / 3) + 1) << 2;
        int offset = this.count;
        int newcount = (this.count + charsLen) + 2;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(quote);
                s = 0;
                while (s < eLen) {
                    int s2 = s + 1;
                    s = s2 + 1;
                    s2 = s + 1;
                    i = (((bytes[s] & 255) << 16) | ((bytes[s2] & 255) << 8)) | (bytes[s] & 255);
                    write(CA[(i >>> 18) & 63]);
                    write(CA[(i >>> 12) & 63]);
                    write(CA[(i >>> 6) & 63]);
                    write(CA[i & 63]);
                    s = s2;
                }
                left = bytesLen - eLen;
                if (left > 0) {
                    i = ((bytes[eLen] & 255) << 10) | (left == 2 ? (bytes[bytesLen - 1] & 255) << 2 : 0);
                    write(CA[i >> 12]);
                    write(CA[(i >>> 6) & 63]);
                    write(left == 2 ? CA[i & 63] : '=');
                    write('=');
                }
                write(quote);
                return;
            }
            expandCapacity(newcount);
        }
        this.count = newcount;
        int offset2 = offset + 1;
        this.buf[offset] = quote;
        int d = offset2;
        s = 0;
        while (s < eLen) {
            s2 = s + 1;
            s = s2 + 1;
            s2 = s + 1;
            i = (((bytes[s] & 255) << 16) | ((bytes[s2] & 255) << 8)) | (bytes[s] & 255);
            int i2 = d + 1;
            this.buf[d] = CA[(i >>> 18) & 63];
            d = i2 + 1;
            this.buf[i2] = CA[(i >>> 12) & 63];
            i2 = d + 1;
            this.buf[d] = CA[(i >>> 6) & 63];
            d = i2 + 1;
            this.buf[i2] = CA[i & 63];
            s = s2;
        }
        left = bytesLen - eLen;
        if (left > 0) {
            i = ((bytes[eLen] & 255) << 10) | (left == 2 ? (bytes[bytesLen - 1] & 255) << 2 : 0);
            this.buf[newcount - 5] = CA[i >> 12];
            this.buf[newcount - 4] = CA[(i >>> 6) & 63];
            this.buf[newcount - 3] = left == 2 ? CA[i & 63] : '=';
            this.buf[newcount - 2] = '=';
        }
        this.buf[newcount - 1] = quote;
    }

    public void writeFloatAndChar(float value, char c) {
        String text = Float.toString(value);
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        write(text);
        write(c);
    }

    public void writeDoubleAndChar(double value, char c) {
        String text = Double.toString(value);
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        write(text);
        write(c);
    }

    public void writeBooleanAndChar(boolean value, char c) {
        if (value) {
            if (c == ',') {
                write("true,");
            } else if (c == ']') {
                write("true]");
            } else {
                write("true");
                write(c);
            }
        } else if (c == ',') {
            write("false,");
        } else if (c == ']') {
            write("false]");
        } else {
            write("false");
            write(c);
        }
    }

    public void writeCharacterAndChar(char value, char c) {
        writeString(Character.toString(value));
        write(c);
    }

    public void writeEnum(Enum<?> value, char c) {
        if (value == null) {
            writeNull();
            write(',');
        } else if (!isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            writeIntAndChar(value.ordinal(), c);
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            write('\'');
            write(value.name());
            write('\'');
            write(c);
        } else {
            write('\"');
            write(value.name());
            write('\"');
            write(c);
        }
    }

    public void writeIntAndChar(int i, char c) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            write(c);
            return;
        }
        int newcount0 = this.count + (i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i));
        int newcount1 = newcount0 + 1;
        if (newcount1 > this.buf.length) {
            if (this.writer != null) {
                writeInt(i);
                write(c);
                return;
            }
            expandCapacity(newcount1);
        }
        IOUtils.getChars(i, newcount0, this.buf);
        this.buf[newcount0] = c;
        this.count = newcount1;
    }

    public void writeLongAndChar(long i, char c) throws IOException {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            write(c);
            return;
        }
        int newcount0 = this.count + (i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i));
        int newcount1 = newcount0 + 1;
        if (newcount1 > this.buf.length) {
            if (this.writer != null) {
                writeLong(i);
                write(c);
                return;
            }
            expandCapacity(newcount1);
        }
        IOUtils.getChars(i, newcount0, this.buf);
        this.buf[newcount0] = c;
        this.count = newcount1;
    }

    public void writeLong(long i) {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }
        int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int newcount = this.count + size;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }
        IOUtils.getChars(i, newcount, this.buf);
        this.count = newcount;
    }

    public void writeNull() {
        write("null");
    }

    private void writeStringWithDoubleQuote(String text, char seperator) {
        writeStringWithDoubleQuote(text, seperator, true);
    }

    private void writeStringWithDoubleQuote(String text, char seperator, boolean checkSpecial) {
        if (text == null) {
            writeNull();
            if (seperator != '\u0000') {
                write(seperator);
                return;
            }
            return;
        }
        int i;
        char ch;
        int len = text.length();
        int newcount = (this.count + len) + 2;
        if (seperator != '\u0000') {
            newcount++;
        }
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write('\"');
                for (i = 0; i < text.length(); i++) {
                    ch = text.charAt(i);
                    if (!isEnabled(SerializerFeature.BrowserCompatible)) {
                        if ((ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 0) || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write('\\');
                            write(IOUtils.replaceChars[ch]);
                        }
                        write(ch);
                    } else if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\"' || ch == '/' || ch == '\\') {
                        write('\\');
                        write(IOUtils.replaceChars[ch]);
                    } else if (ch < ' ') {
                        write('\\');
                        write('u');
                        write('0');
                        write('0');
                        write(IOUtils.ASCII_CHARS[ch * 2]);
                        write(IOUtils.ASCII_CHARS[(ch * 2) + 1]);
                    } else {
                        if (ch >= '') {
                            write('\\');
                            write('u');
                            write(IOUtils.DIGITS[(ch >>> 12) & 15]);
                            write(IOUtils.DIGITS[(ch >>> 8) & 15]);
                            write(IOUtils.DIGITS[(ch >>> 4) & 15]);
                            write(IOUtils.DIGITS[ch & 15]);
                        }
                        write(ch);
                    }
                }
                write('\"');
                if (seperator != '\u0000') {
                    write(seperator);
                    return;
                }
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        int end = start + len;
        this.buf[this.count] = '\"';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        int lastSpecialIndex;
        if (isEnabled(SerializerFeature.BrowserCompatible)) {
            lastSpecialIndex = -1;
            for (i = start; i < end; i++) {
                ch = this.buf[i];
                if (ch == '\"' || ch == '/' || ch == '\\') {
                    lastSpecialIndex = i;
                    newcount++;
                } else if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t') {
                    lastSpecialIndex = i;
                    newcount++;
                } else if (ch < ' ') {
                    lastSpecialIndex = i;
                    newcount += 5;
                } else if (ch >= '') {
                    lastSpecialIndex = i;
                    newcount += 5;
                }
            }
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            this.count = newcount;
            for (i = lastSpecialIndex; i >= start; i--) {
                ch = this.buf[i];
                if (ch == '\b' || ch == '\f' || ch == '\n' || ch == '\r' || ch == '\t') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = IOUtils.replaceChars[ch];
                    end++;
                } else if (ch == '\"' || ch == '/' || ch == '\\') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = ch;
                    end++;
                } else if (ch < ' ') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 6, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = 'u';
                    this.buf[i + 2] = '0';
                    this.buf[i + 3] = '0';
                    this.buf[i + 4] = IOUtils.ASCII_CHARS[ch * 2];
                    this.buf[i + 5] = IOUtils.ASCII_CHARS[(ch * 2) + 1];
                    end += 5;
                } else if (ch >= '') {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 6, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = 'u';
                    this.buf[i + 2] = IOUtils.DIGITS[(ch >>> 12) & 15];
                    this.buf[i + 3] = IOUtils.DIGITS[(ch >>> 8) & 15];
                    this.buf[i + 4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                    this.buf[i + 5] = IOUtils.DIGITS[ch & 15];
                    end += 5;
                }
            }
            if (seperator != '\u0000') {
                this.buf[this.count - 2] = '\"';
                this.buf[this.count - 1] = seperator;
                return;
            }
            this.buf[this.count - 1] = '\"';
            return;
        }
        int specialCount = 0;
        lastSpecialIndex = -1;
        int firstSpecialIndex = -1;
        char lastSpecial = '\u0000';
        if (checkSpecial) {
            for (i = start; i < end; i++) {
                ch = this.buf[i];
                if (ch == ' ') {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;
                    newcount += 4;
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                } else if (ch >= ']') {
                    if (ch >= '' && ch <= ' ') {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = i;
                        }
                        specialCount++;
                        lastSpecialIndex = i;
                        lastSpecial = ch;
                        newcount += 4;
                    }
                } else if (isSpecial(ch, this.features)) {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;
                    if (ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                        newcount += 4;
                    }
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                }
            }
            if (specialCount > 0) {
                newcount += specialCount;
                if (newcount > this.buf.length) {
                    expandCapacity(newcount);
                }
                this.count = newcount;
                int i2;
                int i3;
                if (specialCount == 1) {
                    int srcPos;
                    int destPos;
                    int LengthOfCopy;
                    if (lastSpecial == ' ') {
                        srcPos = lastSpecialIndex + 1;
                        destPos = lastSpecialIndex + 6;
                        LengthOfCopy = (end - lastSpecialIndex) - 1;
                        System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                        this.buf[lastSpecialIndex] = '\\';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = 'u';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '0';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        this.buf[lastSpecialIndex + 1] = '8';
                    } else {
                        ch = lastSpecial;
                        if (ch >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 4) {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 2;
                            LengthOfCopy = (end - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            this.buf[lastSpecialIndex] = '\\';
                            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[ch];
                        } else {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 6;
                            LengthOfCopy = (end - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            i2 = lastSpecialIndex;
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            i2 = i3 + 1;
                            this.buf[i3] = 'u';
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch >>> 12) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(ch >>> 8) & 15];
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch >>> 4) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[ch & 15];
                        }
                    }
                } else if (specialCount > 1) {
                    i2 = firstSpecialIndex;
                    for (i = firstSpecialIndex - start; i < text.length(); i++) {
                        ch = text.charAt(i);
                        if ((ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 0) || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            if (IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                                i2 = i3 + 1;
                                this.buf[i3] = 'u';
                                i3 = i2 + 1;
                                this.buf[i2] = IOUtils.DIGITS[(ch >>> 12) & 15];
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[(ch >>> 8) & 15];
                                i3 = i2 + 1;
                                this.buf[i2] = IOUtils.DIGITS[(ch >>> 4) & 15];
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.DIGITS[ch & 15];
                                end += 5;
                            } else {
                                i2 = i3 + 1;
                                this.buf[i3] = IOUtils.replaceChars[ch];
                                end++;
                            }
                        } else if (ch == ' ') {
                            i3 = i2 + 1;
                            this.buf[i2] = '\\';
                            i2 = i3 + 1;
                            this.buf[i3] = 'u';
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch >>> 12) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[(ch >>> 8) & 15];
                            i3 = i2 + 1;
                            this.buf[i2] = IOUtils.DIGITS[(ch >>> 4) & 15];
                            i2 = i3 + 1;
                            this.buf[i3] = IOUtils.DIGITS[ch & 15];
                            end += 5;
                        } else {
                            i3 = i2 + 1;
                            this.buf[i2] = ch;
                            i2 = i3;
                        }
                    }
                }
            }
        }
        if (seperator != '\u0000') {
            this.buf[this.count - 2] = '\"';
            this.buf[this.count - 1] = seperator;
            return;
        }
        this.buf[this.count - 1] = '\"';
    }

    public void writeFieldNull(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        writeNull();
    }

    public void writeFieldEmptyList(char seperator, String key) {
        write(seperator);
        writeFieldName(key);
        write("[]");
    }

    public void writeFieldNullString(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
            writeString("");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullBoolean(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullBooleanAsFalse)) {
            write("false");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullList(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            write("[]");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullNumber(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            write('0');
        } else {
            writeNull();
        }
    }

    public void writeFieldValue(char seperator, String name, char value) {
        write(seperator);
        writeFieldName(name);
        if (value == '\u0000') {
            writeString(DexFormat.MAGIC_SUFFIX);
        } else {
            writeString(Character.toString(value));
        }
    }

    public void writeFieldValue(char seperator, String name, boolean value) {
        int intSize;
        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
        if (value) {
            intSize = 4;
        } else {
            intSize = 5;
        }
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(seperator);
                writeString(name);
                write(':');
                write(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = keySeperator;
        if (value) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, nameEnd + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, nameEnd + 2, 6);
        }
    }

    public void write(boolean value) {
        if (value) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char seperator, String name, int value) {
        if (value == Integer.MIN_VALUE || !isEnabled(SerializerFeature.QuoteFieldNames)) {
            writeFieldValue1(seperator, name, value);
            return;
        }
        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                writeFieldValue1(seperator, name, value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue1(char seperator, String name, int value) {
        write(seperator);
        writeFieldName(name);
        writeInt(value);
    }

    public void writeFieldValue(char seperator, String name, long value) {
        if (value == Long.MIN_VALUE || !isEnabled(SerializerFeature.QuoteFieldNames)) {
            writeFieldValue1(seperator, name, value);
            return;
        }
        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = ((this.count + nameLen) + 4) + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(seperator);
                writeFieldName(name);
                writeLong(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = (start + nameLen) + 1;
        this.buf[start + 1] = keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue1(char seperator, String name, long value) {
        write(seperator);
        writeFieldName(name);
        writeLong(value);
    }

    public void writeFieldValue(char seperator, String name, float value) {
        write(seperator);
        writeFieldName(name);
        if (value == 0.0f) {
            write('0');
        } else if (Float.isNaN(value)) {
            writeNull();
        } else if (Float.isInfinite(value)) {
            writeNull();
        } else {
            String text = Float.toString(value);
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            write(text);
        }
    }

    public void writeFieldValue(char seperator, String name, double value) {
        write(seperator);
        writeFieldName(name);
        if (value == 0.0d) {
            write('0');
        } else if (Double.isNaN(value)) {
            writeNull();
        } else if (Double.isInfinite(value)) {
            writeNull();
        } else {
            String text = Double.toString(value);
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            write(text);
        }
    }

    public void writeFieldValue(char seperator, String name, String value) {
        if (!isEnabled(SerializerFeature.QuoteFieldNames)) {
            write(seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            write(seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (isEnabled(SerializerFeature.BrowserCompatible)) {
            write(seperator);
            writeStringWithDoubleQuote(name, ':');
            writeStringWithDoubleQuote(value, '\u0000');
        } else {
            writeFieldValueStringWithDoubleQuote(seperator, name, value, true);
        }
    }

    private void writeFieldValueStringWithDoubleQuote(char seperator, String name, String value, boolean checkSpecial) {
        int valueLen;
        int nameLen = name.length();
        int newcount = this.count;
        if (value == null) {
            valueLen = 4;
            newcount += nameLen + 8;
        } else {
            valueLen = value.length();
            newcount += (nameLen + valueLen) + 6;
        }
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write(seperator);
                writeStringWithDoubleQuote(name, ':', checkSpecial);
                writeStringWithDoubleQuote(value, '\u0000', checkSpecial);
                return;
            }
            expandCapacity(newcount);
        }
        this.buf[this.count] = seperator;
        int nameStart = this.count + 2;
        int nameEnd = nameStart + nameLen;
        this.buf[this.count + 1] = '\"';
        name.getChars(0, nameLen, this.buf, nameStart);
        this.count = newcount;
        this.buf[nameEnd] = '\"';
        int i = nameEnd + 1;
        int i2 = i + 1;
        this.buf[i] = ':';
        if (value == null) {
            i = i2 + 1;
            this.buf[i2] = 'n';
            i2 = i + 1;
            this.buf[i] = 'u';
            i = i2 + 1;
            this.buf[i2] = 'l';
            i2 = i + 1;
            this.buf[i] = 'l';
            return;
        }
        i = i2 + 1;
        this.buf[i2] = '\"';
        int valueStart = i;
        int valueEnd = valueStart + valueLen;
        value.getChars(0, valueLen, this.buf, valueStart);
        if (checkSpecial && !isEnabled(SerializerFeature.DisableCheckSpecialChar)) {
            int i3;
            char ch;
            int specialCount = 0;
            int lastSpecialIndex = -1;
            int firstSpecialIndex = -1;
            char lastSpecial = '\u0000';
            for (i3 = valueStart; i3 < valueEnd; i3++) {
                ch = this.buf[i3];
                if (ch == ' ') {
                    specialCount++;
                    lastSpecialIndex = i3;
                    lastSpecial = ch;
                    newcount += 4;
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i3;
                    }
                } else if (ch >= ']') {
                    if (ch >= '' && ch <= ' ') {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = i3;
                        }
                        specialCount++;
                        lastSpecialIndex = i3;
                        lastSpecial = ch;
                        newcount += 4;
                    }
                } else if (isSpecial(ch, this.features)) {
                    specialCount++;
                    lastSpecialIndex = i3;
                    lastSpecial = ch;
                    if (ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                        newcount += 4;
                    }
                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i3;
                    }
                }
            }
            if (specialCount > 0) {
                newcount += specialCount;
                if (newcount > this.buf.length) {
                    expandCapacity(newcount);
                }
                this.count = newcount;
                int i4;
                int i5;
                if (specialCount == 1) {
                    int srcPos;
                    int destPos;
                    int LengthOfCopy;
                    if (lastSpecial == ' ') {
                        srcPos = lastSpecialIndex + 1;
                        destPos = lastSpecialIndex + 6;
                        LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                        System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                        this.buf[lastSpecialIndex] = '\\';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = 'u';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '0';
                        lastSpecialIndex++;
                        this.buf[lastSpecialIndex] = '2';
                        this.buf[lastSpecialIndex + 1] = '8';
                    } else {
                        ch = lastSpecial;
                        if (ch >= IOUtils.specicalFlags_doubleQuotes.length || IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 4) {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 2;
                            LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            this.buf[lastSpecialIndex] = '\\';
                            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[ch];
                        } else {
                            srcPos = lastSpecialIndex + 1;
                            destPos = lastSpecialIndex + 6;
                            LengthOfCopy = (valueEnd - lastSpecialIndex) - 1;
                            System.arraycopy(this.buf, srcPos, this.buf, destPos, LengthOfCopy);
                            i4 = lastSpecialIndex;
                            i5 = i4 + 1;
                            this.buf[i4] = '\\';
                            i4 = i5 + 1;
                            this.buf[i5] = 'u';
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[ch & 15];
                        }
                    }
                } else if (specialCount > 1) {
                    i4 = firstSpecialIndex;
                    for (i3 = firstSpecialIndex - valueStart; i3 < value.length(); i3++) {
                        ch = value.charAt(i3);
                        if ((ch < IOUtils.specicalFlags_doubleQuotes.length && IOUtils.specicalFlags_doubleQuotes[ch] != (byte) 0) || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            i5 = i4 + 1;
                            this.buf[i4] = '\\';
                            if (IOUtils.specicalFlags_doubleQuotes[ch] == (byte) 4) {
                                i4 = i5 + 1;
                                this.buf[i5] = 'u';
                                i5 = i4 + 1;
                                this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                                i4 = i5 + 1;
                                this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                                i5 = i4 + 1;
                                this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                                i4 = i5 + 1;
                                this.buf[i5] = IOUtils.DIGITS[ch & 15];
                                valueEnd += 5;
                            } else {
                                i4 = i5 + 1;
                                this.buf[i5] = IOUtils.replaceChars[ch];
                                valueEnd++;
                            }
                        } else if (ch == ' ') {
                            i5 = i4 + 1;
                            this.buf[i4] = '\\';
                            i4 = i5 + 1;
                            this.buf[i5] = 'u';
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 12) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[(ch >>> 8) & 15];
                            i5 = i4 + 1;
                            this.buf[i4] = IOUtils.DIGITS[(ch >>> 4) & 15];
                            i4 = i5 + 1;
                            this.buf[i5] = IOUtils.DIGITS[ch & 15];
                            valueEnd += 5;
                        } else {
                            i5 = i4 + 1;
                            this.buf[i4] = ch;
                            i4 = i5;
                        }
                    }
                }
            }
        }
        this.buf[this.count - 1] = '\"';
    }

    static final boolean isSpecial(char ch, int features) {
        if (ch == ' ') {
            return false;
        }
        if (ch == '/' && SerializerFeature.isEnabled(features, SerializerFeature.WriteSlashAsSpecial)) {
            return true;
        }
        if (ch > '#' && ch != '\\') {
            return false;
        }
        if (ch <= '\u001f' || ch == '\\' || ch == '\"') {
            return true;
        }
        return false;
    }

    public void writeFieldValue(char seperator, String name, Enum<?> value) {
        if (value == null) {
            write(seperator);
            writeFieldName(name);
            writeNull();
        } else if (!isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            writeFieldValue(seperator, name, value.ordinal());
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeFieldValue(seperator, name, value.name());
        } else {
            writeFieldValueStringWithDoubleQuote(seperator, name, value.name(), false);
        }
    }

    public void writeFieldValue(char seperator, String name, BigDecimal value) {
        write(seperator);
        writeFieldName(name);
        if (value == null) {
            writeNull();
        } else {
            write(value.toString());
        }
    }

    public void writeString(String text, char seperator) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeStringWithSingleQuote(text);
            write(seperator);
            return;
        }
        writeStringWithDoubleQuote(text, seperator);
    }

    public void writeString(String text) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeStringWithSingleQuote(text);
        } else {
            writeStringWithDoubleQuote(text, '\u0000');
        }
    }

    private void writeStringWithSingleQuote(String text) {
        int newcount;
        if (text == null) {
            newcount = this.count + 4;
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            "null".getChars(0, 4, this.buf, this.count);
            this.count = newcount;
            return;
        }
        int i;
        char ch;
        int len = text.length();
        newcount = (this.count + len) + 2;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write('\'');
                for (i = 0; i < text.length(); i++) {
                    ch = text.charAt(i);
                    if (ch <= '\r' || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write('\\');
                        write(IOUtils.replaceChars[ch]);
                    } else {
                        write(ch);
                    }
                }
                write('\'');
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        int end = start + len;
        this.buf[this.count] = '\'';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        int specialCount = 0;
        int lastSpecialIndex = -1;
        char lastSpecial = '\u0000';
        for (i = start; i < end; i++) {
            ch = this.buf[i];
            if (ch <= '\r' || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                specialCount++;
                lastSpecialIndex = i;
                lastSpecial = ch;
            }
        }
        newcount += specialCount;
        if (newcount > this.buf.length) {
            expandCapacity(newcount);
        }
        this.count = newcount;
        if (specialCount == 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[lastSpecial];
        } else if (specialCount > 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            lastSpecialIndex++;
            this.buf[lastSpecialIndex] = IOUtils.replaceChars[lastSpecial];
            end++;
            for (i = lastSpecialIndex - 2; i >= start; i--) {
                ch = this.buf[i];
                if (ch <= '\r' || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, (end - i) - 1);
                    this.buf[i] = '\\';
                    this.buf[i + 1] = IOUtils.replaceChars[ch];
                    end++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String key) {
        writeFieldName(key, false);
    }

    public void writeFieldName(String key, boolean checkSpecial) {
        if (key == null) {
            write("null:");
        } else if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            if (isEnabled(SerializerFeature.QuoteFieldNames)) {
                writeStringWithSingleQuote(key);
                write(':');
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(key);
        } else if (isEnabled(SerializerFeature.QuoteFieldNames)) {
            writeStringWithDoubleQuote(key, ':', checkSpecial);
        } else {
            writeKeyWithDoubleQuoteIfHasSpecial(key);
        }
    }

    private void writeKeyWithDoubleQuoteIfHasSpecial(String text) {
        boolean hasSpecial;
        int i;
        char ch;
        byte[] specicalFlags_doubleQuotes = IOUtils.specicalFlags_doubleQuotes;
        int len = text.length();
        int newcount = (this.count + len) + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else if (len == 0) {
                write('\"');
                write('\"');
                write(':');
                return;
            } else {
                hasSpecial = false;
                for (i = 0; i < len; i++) {
                    ch = text.charAt(i);
                    if (ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != (byte) 0) {
                        hasSpecial = true;
                        break;
                    }
                }
                if (hasSpecial) {
                    write('\"');
                }
                for (i = 0; i < len; i++) {
                    ch = text.charAt(i);
                    if (ch >= specicalFlags_doubleQuotes.length || specicalFlags_doubleQuotes[ch] == (byte) 0) {
                        write(ch);
                    } else {
                        write('\\');
                        write(IOUtils.replaceChars[ch]);
                    }
                }
                if (hasSpecial) {
                    write('\"');
                }
                write(':');
                return;
            }
        }
        if (len == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\"';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\"';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = ':';
            return;
        }
        int start = this.count;
        int end = start + len;
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        hasSpecial = false;
        i = start;
        while (i < end) {
            ch = this.buf[i];
            if (ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != (byte) 0) {
                if (hasSpecial) {
                    newcount++;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, end - i);
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end++;
                } else {
                    newcount += 3;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 3, (end - i) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i);
                    this.buf[start] = '\"';
                    i++;
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end += 2;
                    this.buf[this.count - 2] = '\"';
                    hasSpecial = true;
                }
            }
            i++;
        }
        this.buf[this.count - 1] = ':';
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String text) {
        boolean hasSpecial;
        int i;
        char ch;
        byte[] specicalFlags_singleQuotes = IOUtils.specicalFlags_singleQuotes;
        int len = text.length();
        int newcount = (this.count + len) + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else if (len == 0) {
                write('\'');
                write('\'');
                write(':');
                return;
            } else {
                hasSpecial = false;
                for (i = 0; i < len; i++) {
                    ch = text.charAt(i);
                    if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != (byte) 0) {
                        hasSpecial = true;
                        break;
                    }
                }
                if (hasSpecial) {
                    write('\'');
                }
                for (i = 0; i < len; i++) {
                    ch = text.charAt(i);
                    if (ch >= specicalFlags_singleQuotes.length || specicalFlags_singleQuotes[ch] == (byte) 0) {
                        write(ch);
                    } else {
                        write('\\');
                        write(IOUtils.replaceChars[ch]);
                    }
                }
                if (hasSpecial) {
                    write('\'');
                }
                write(':');
                return;
            }
        }
        if (len == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\'';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = '\'';
            cArr = this.buf;
            i2 = this.count;
            this.count = i2 + 1;
            cArr[i2] = ':';
            return;
        }
        int start = this.count;
        int end = start + len;
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        hasSpecial = false;
        i = start;
        while (i < end) {
            ch = this.buf[i];
            if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != (byte) 0) {
                if (hasSpecial) {
                    newcount++;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 2, end - i);
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end++;
                } else {
                    newcount += 3;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i + 1, this.buf, i + 3, (end - i) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i);
                    this.buf[start] = '\'';
                    i++;
                    this.buf[i] = '\\';
                    i++;
                    this.buf[i] = IOUtils.replaceChars[ch];
                    end += 2;
                    this.buf[this.count - 2] = '\'';
                    hasSpecial = true;
                }
            }
            i++;
        }
        this.buf[newcount - 1] = ':';
    }

    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }
}
