package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.ref.SoftReference;

public final class JSONReaderScanner extends JSONLexerBase {
    public static int BUF_INIT_LEN = 8192;
    private static final ThreadLocal<SoftReference<char[]>> BUF_REF_LOCAL = new ThreadLocal();
    private char[] buf;
    private int bufLength;
    private Reader reader;

    public JSONReaderScanner(String input) {
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(String input, int features) {
        this(new StringReader(input), features);
    }

    public JSONReaderScanner(char[] input, int inputLength) {
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader) {
        this(reader, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONReaderScanner(Reader reader, int features) {
        this.reader = reader;
        this.features = features;
        SoftReference<char[]> bufRef = (SoftReference) BUF_REF_LOCAL.get();
        if (bufRef != null) {
            this.buf = (char[]) bufRef.get();
            BUF_REF_LOCAL.set(null);
        }
        if (this.buf == null) {
            this.buf = new char[BUF_INIT_LEN];
        }
        try {
            this.bufLength = reader.read(this.buf);
            this.bp = -1;
            next();
            if (this.ch == '﻿') {
                next();
            }
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public JSONReaderScanner(char[] input, int inputLength, int features) {
        this(new CharArrayReader(input, 0, inputLength), features);
    }

    public final char charAt(int index) {
        if (index >= this.bufLength) {
            if (this.bufLength != -1) {
                int rest = this.bufLength - this.bp;
                if (rest > 0) {
                    System.arraycopy(this.buf, this.bp, this.buf, 0, rest);
                }
                try {
                    this.bufLength = this.reader.read(this.buf, rest, this.buf.length - rest);
                    if (this.bufLength == 0) {
                        throw new JSONException("illegal stat, textLength is zero");
                    } else if (this.bufLength == -1) {
                        return '\u001a';
                    } else {
                        this.bufLength += rest;
                        index -= this.bp;
                        this.np -= this.bp;
                        this.bp = 0;
                    }
                } catch (IOException e) {
                    throw new JSONException(e.getMessage(), e);
                }
            } else if (index < this.sp) {
                return this.buf[index];
            } else {
                return '\u001a';
            }
        }
        return this.buf[index];
    }

    public final int indexOf(char ch, int startIndex) {
        int offset = startIndex - this.bp;
        while (ch != charAt(this.bp + offset)) {
            if (ch == '\u001a') {
                return -1;
            }
            offset++;
        }
        return this.bp + offset;
    }

    public final String addSymbol(int offset, int len, int hash, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.buf, offset, len, hash);
    }

    public final char next() {
        int index = this.bp + 1;
        this.bp = index;
        if (index >= this.bufLength) {
            if (this.bufLength == -1) {
                return '\u001a';
            }
            if (this.sp > 0) {
                int offset = this.bufLength - this.sp;
                if (this.ch == '\"') {
                    offset--;
                }
                System.arraycopy(this.buf, offset, this.buf, 0, this.sp);
            }
            this.np = -1;
            index = this.sp;
            this.bp = index;
            try {
                int startPos = this.bp;
                int readLength = this.buf.length - startPos;
                if (readLength == 0) {
                    char[] newBuf = new char[(this.buf.length * 2)];
                    System.arraycopy(this.buf, 0, newBuf, 0, this.buf.length);
                    this.buf = newBuf;
                    readLength = this.buf.length - startPos;
                }
                this.bufLength = this.reader.read(this.buf, this.bp, readLength);
                if (this.bufLength == 0) {
                    throw new JSONException("illegal stat, textLength is zero");
                } else if (this.bufLength == -1) {
                    this.ch = '\u001a';
                    return '\u001a';
                } else {
                    this.bufLength += this.bp;
                }
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
        char c = this.buf[index];
        this.ch = c;
        return c;
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        System.arraycopy(this.buf, offset, dest, 0, count);
    }

    public final boolean charArrayCompare(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (charAt(this.bp + i) != chars[i]) {
                return false;
            }
        }
        return true;
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(this.buf, this.np + 1, this.sp);
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        System.arraycopy(this.buf, srcPos, dest, destPos, length);
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.sp);
        }
        int offset = this.np + 1;
        if (offset < 0) {
            throw new IllegalStateException();
        } else if (offset <= this.buf.length - this.sp) {
            return new String(this.buf, offset, this.sp);
        } else {
            throw new IllegalStateException();
        }
    }

    public final String subString(int offset, int count) {
        if (count >= 0) {
            return new String(this.buf, offset, count);
        }
        throw new StringIndexOutOfBoundsException(count);
    }

    public final String numberString() {
        int offset = this.np;
        if (offset == -1) {
            offset = 0;
        }
        char chLocal = charAt((this.sp + offset) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        return new String(this.buf, offset, sp);
    }

    public void close() {
        super.close();
        BUF_REF_LOCAL.set(new SoftReference(this.buf));
        this.buf = null;
        IOUtils.close(this.reader);
    }

    public boolean isEOF() {
        return this.bufLength == -1 || this.bp == this.buf.length || (this.ch == '\u001a' && this.bp + 1 == this.buf.length);
    }
}
