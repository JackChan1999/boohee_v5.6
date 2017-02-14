package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.alipay.sdk.sys.a;
import com.tencent.tinker.android.dx.instruction.Opcodes;
import java.io.Closeable;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class JSONLexerBase implements JSONLexer, Closeable {
    private static final Map<String, Integer> DEFAULT_KEYWORDS;
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final int INT_N_MULTMAX_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    protected static final long N_MULTMAX_RADIX_TEN = -922337203685477580L;
    private static final ThreadLocal<SoftReference<char[]>> SBUF_REF_LOCAL = new ThreadLocal();
    protected static final int[] digits = new int[103];
    protected static final char[] typeFieldName = (a.e + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected static boolean[] whitespaceFlags = new boolean[256];
    protected int bp;
    protected Calendar calendar = null;
    protected char ch;
    protected int eofPos;
    protected int features = JSON.DEFAULT_PARSER_FEATURE;
    protected boolean hasSpecial;
    protected Map<String, Integer> keywods = DEFAULT_KEYWORDS;
    public int matchStat = 0;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected int token;

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    protected abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    public abstract byte[] bytesValue();

    protected abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    protected abstract void copyTo(int i, int i2, char[] cArr);

    public abstract int indexOf(char c, int i);

    public abstract boolean isEOF();

    public abstract char next();

    public abstract String numberString();

    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    static {
        int i;
        Map<String, Integer> map = new HashMap();
        map.put("null", Integer.valueOf(8));
        map.put("new", Integer.valueOf(9));
        map.put("true", Integer.valueOf(6));
        map.put("false", Integer.valueOf(7));
        map.put("undefined", Integer.valueOf(23));
        DEFAULT_KEYWORDS = map;
        whitespaceFlags[32] = true;
        whitespaceFlags[10] = true;
        whitespaceFlags[13] = true;
        whitespaceFlags[9] = true;
        whitespaceFlags[12] = true;
        whitespaceFlags[8] = true;
        for (i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (i = 97; i <= 102; i++) {
            digits[i] = (i - 97) + 10;
        }
        for (i = 65; i <= 70; i++) {
            digits[i] = (i - 65) + 10;
        }
    }

    protected void lexError(String key, Object... args) {
        this.token = 1;
    }

    public JSONLexerBase() {
        SoftReference<char[]> sbufRef = (SoftReference) SBUF_REF_LOCAL.get();
        if (sbufRef != null) {
            this.sbuf = (char[]) sbufRef.get();
            SBUF_REF_LOCAL.set(null);
        }
        if (this.sbuf == null) {
            this.sbuf = new char[64];
        }
    }

    public final int matchStat() {
        return this.matchStat;
    }

    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch == '\"') {
                scanString();
                return;
            } else if (this.ch == ',') {
                next();
                this.token = 16;
                return;
            } else if (this.ch >= '0' && this.ch <= '9') {
                scanNumber();
                return;
            } else if (this.ch == '-') {
                scanNumber();
                return;
            } else {
                switch (this.ch) {
                    case '\b':
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        next();
                    case '\'':
                        if (isEnabled(Feature.AllowSingleQuotes)) {
                            scanStringSingleQuote();
                            return;
                        }
                        throw new JSONException("Feature.AllowSingleQuotes is false");
                    case '(':
                        next();
                        this.token = 10;
                        return;
                    case ')':
                        next();
                        this.token = 11;
                        return;
                    case ':':
                        next();
                        this.token = 17;
                        return;
                    case 'S':
                        scanSet();
                        return;
                    case 'T':
                        scanTreeSet();
                        return;
                    case '[':
                        next();
                        this.token = 14;
                        return;
                    case ']':
                        next();
                        this.token = 15;
                        return;
                    case 'f':
                        scanFalse();
                        return;
                    case 'n':
                        scanNullOrNew();
                        return;
                    case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                        scanTrue();
                        return;
                    case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                        scanUndefined();
                        return;
                    case Opcodes.NEG_INT /*123*/:
                        next();
                        this.token = 12;
                        return;
                    case Opcodes.NEG_LONG /*125*/:
                        next();
                        this.token = 13;
                        return;
                    default:
                        if (!isEOF()) {
                            lexError("illegal.char", String.valueOf(this.ch));
                            next();
                            return;
                        } else if (this.token == 20) {
                            throw new JSONException("EOF error");
                        } else {
                            this.token = 20;
                            int i = this.eofPos;
                            this.bp = i;
                            this.pos = i;
                            return;
                        }
                }
            }
        }
    }

    public final void nextToken(int expect) {
        this.sp = 0;
        while (true) {
            switch (expect) {
                case 2:
                    if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 4:
                    if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 12:
                    if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    }
                    break;
                case 14:
                    if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 15:
                    if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    }
                    break;
                case 16:
                    if (this.ch == ',') {
                        this.token = 16;
                        next();
                        return;
                    } else if (this.ch == '}') {
                        this.token = 13;
                        next();
                        return;
                    } else if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    } else if (this.ch == '\u001a') {
                        this.token = 20;
                        return;
                    }
                    break;
                case 18:
                    nextIdent();
                    return;
                case 20:
                    break;
            }
            if (this.ch == '\u001a') {
                this.token = 20;
                return;
            }
            if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (this.ch == '_' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithComma() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char expect) {
        this.sp = 0;
        while (this.ch != expect) {
            if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b') {
                next();
            } else {
                throw new JSONException("not match " + expect + " - " + this.ch);
            }
        }
        next();
        nextToken();
    }

    public final int token() {
        return this.token;
    }

    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    public final int pos() {
        return this.pos;
    }

    public final int getBufferPosition() {
        return this.bp;
    }

    public final String stringDefaultValue() {
        if (isEnabled(Feature.InitStringFieldAsEmpty)) {
            return "";
        }
        return null;
    }

    public final Number integerValue() throws NumberFormatException {
        long limit;
        int i;
        long result = 0;
        boolean negative = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i2 = this.np;
        int max = this.np + this.sp;
        char type = ' ';
        switch (charAt(max - 1)) {
            case 'B':
                max--;
                type = 'B';
                break;
            case 'L':
                max--;
                type = 'L';
                break;
            case 'S':
                max--;
                type = 'S';
                break;
        }
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i2 + 1;
        } else {
            limit = -9223372036854775807L;
            i = i2;
        }
        long multmin = negative ? -922337203685477580L : -922337203685477580L;
        if (i < max) {
            result = (long) (-digits[charAt(i)]);
            i++;
        }
        while (i < max) {
            i2 = i + 1;
            int digit = digits[charAt(i)];
            if (result < multmin) {
                return new BigInteger(numberString());
            }
            result *= 10;
            if (result < ((long) digit) + limit) {
                return new BigInteger(numberString());
            }
            result -= (long) digit;
            i = i2;
        }
        if (!negative) {
            result = -result;
            if (result > 2147483647L || type == 'L') {
                i2 = i;
                return Long.valueOf(result);
            } else if (type == 'S') {
                i2 = i;
                return Short.valueOf((short) ((int) result));
            } else if (type == 'B') {
                i2 = i;
                return Byte.valueOf((byte) ((int) result));
            } else {
                i2 = i;
                return Integer.valueOf((int) result);
            }
        } else if (i <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (result < -2147483648L || type == 'L') {
            i2 = i;
            return Long.valueOf(result);
        } else if (type == 'S') {
            i2 = i;
            return Short.valueOf((short) ((int) result));
        } else if (type == 'B') {
            i2 = i;
            return Byte.valueOf((byte) ((int) result));
        } else {
            i2 = i;
            return Integer.valueOf((int) result);
        }
    }

    public final void nextTokenWithColon(int expect) {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithComma(int expect) {
        nextTokenWithChar(',');
    }

    public final void nextTokenWithChar(char seperator, int expect) {
        this.sp = 0;
        while (this.ch != seperator) {
            if (isWhitespace(this.ch)) {
                next();
            } else {
                throw new JSONException("not match " + expect + " - " + this.ch);
            }
        }
        next();
        while (true) {
            if (expect == 2) {
                if (this.ch >= '0' && this.ch <= '9') {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                } else if (this.ch == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                }
            } else if (expect == 4) {
                if (this.ch == '\"') {
                    this.pos = this.bp;
                    scanString();
                    return;
                } else if (this.ch >= '0' && this.ch <= '9') {
                    this.pos = this.bp;
                    scanNumber();
                    return;
                }
            } else if (expect == 12) {
                if (this.ch == '{') {
                    this.token = 12;
                    next();
                    return;
                } else if (this.ch == '[') {
                    this.token = 14;
                    next();
                    return;
                }
            } else if (expect == 14) {
                if (this.ch == '[') {
                    this.token = 14;
                    next();
                    return;
                } else if (this.ch == '{') {
                    this.token = 12;
                    next();
                    return;
                }
            }
            if (isWhitespace(this.ch)) {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public float floatValue() {
        return Float.parseFloat(numberString());
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean state) {
        this.features = Feature.config(this.features, feature, state);
    }

    public final boolean isEnabled(Feature feature) {
        return Feature.isEnabled(this.features, feature);
    }

    public final char getCurrent() {
        return this.ch;
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (this.ch == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        } else if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        } else if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        } else if (this.ch == '\u001a') {
            this.token = 20;
            return null;
        } else if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable, char quote) {
        int hash = 0;
        this.np = this.bp;
        this.sp = 0;
        boolean hasSpecial = false;
        while (true) {
            char chLocal = next();
            if (chLocal == quote) {
                String value;
                this.token = 4;
                if (hasSpecial) {
                    value = symbolTable.addSymbol(this.sbuf, 0, this.sp, hash);
                } else {
                    int offset;
                    if (this.np == -1) {
                        offset = 0;
                    } else {
                        offset = this.np + 1;
                    }
                    value = addSymbol(offset, this.sp, hash, symbolTable);
                }
                this.sp = 0;
                next();
                return value;
            } else if (chLocal == '\u001a') {
                throw new JSONException("unclosed.str");
            } else if (chLocal == '\\') {
                if (!hasSpecial) {
                    hasSpecial = true;
                    if (this.sp >= this.sbuf.length) {
                        int newCapcity = this.sbuf.length * 2;
                        if (this.sp > newCapcity) {
                            newCapcity = this.sp;
                        }
                        char[] newsbuf = new char[newCapcity];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                }
                chLocal = next();
                switch (chLocal) {
                    case '\"':
                        hash = (hash * 31) + 34;
                        putChar('\"');
                        break;
                    case '\'':
                        hash = (hash * 31) + 39;
                        putChar('\'');
                        break;
                    case '/':
                        hash = (hash * 31) + 47;
                        putChar('/');
                        break;
                    case '0':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0000');
                        break;
                    case '1':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0001');
                        break;
                    case '2':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0002');
                        break;
                    case '3':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0003');
                        break;
                    case '4':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0004');
                        break;
                    case '5':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0005');
                        break;
                    case '6':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0006');
                        break;
                    case '7':
                        hash = (hash * 31) + chLocal;
                        putChar('\u0007');
                        break;
                    case 'F':
                    case 'f':
                        hash = (hash * 31) + 12;
                        putChar('\f');
                        break;
                    case '\\':
                        hash = (hash * 31) + 92;
                        putChar('\\');
                        break;
                    case 'b':
                        hash = (hash * 31) + 8;
                        putChar('\b');
                        break;
                    case 'n':
                        hash = (hash * 31) + 10;
                        putChar('\n');
                        break;
                    case Opcodes.INVOKE_INTERFACE /*114*/:
                        hash = (hash * 31) + 13;
                        putChar('\r');
                        break;
                    case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                        hash = (hash * 31) + 9;
                        putChar('\t');
                        break;
                    case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                        char c1 = next();
                        char c2 = next();
                        char c3 = next();
                        char c4 = next();
                        int val = Integer.parseInt(new String(new char[]{c1, c2, c3, c4}), 16);
                        hash = (hash * 31) + val;
                        putChar((char) val);
                        break;
                    case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
                        hash = (hash * 31) + 11;
                        putChar('\u000b');
                        break;
                    case 'x':
                        char x1 = next();
                        this.ch = x1;
                        char x2 = next();
                        this.ch = x2;
                        char x_char = (char) ((digits[x1] * 16) + digits[x2]);
                        hash = (hash * 31) + x_char;
                        putChar(x_char);
                        break;
                    default:
                        this.ch = chLocal;
                        throw new JSONException("unclosed.str.lit");
                }
            } else {
                hash = (hash * 31) + chLocal;
                if (hasSpecial) {
                    if (this.sp == this.sbuf.length) {
                        putChar(chLocal);
                    } else {
                        char[] cArr = this.sbuf;
                        int i = this.sp;
                        this.sp = i + 1;
                        cArr[i] = chLocal;
                    }
                } else {
                    this.sp++;
                }
            }
        }
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean[] firstIdentifierFlags = IOUtils.firstIdentifierFlags;
        char first = this.ch;
        boolean firstFlag = this.ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
        if (firstFlag) {
            boolean[] identifierFlags = IOUtils.identifierFlags;
            int hash = first;
            this.np = this.bp;
            this.sp = 1;
            while (true) {
                char chLocal = next();
                if (chLocal < identifierFlags.length && !identifierFlags[chLocal]) {
                    break;
                }
                hash = (hash * 31) + chLocal;
                this.sp++;
            }
            this.ch = charAt(this.bp);
            this.token = 18;
            if (this.sp == 4 && hash == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
                return null;
            }
            return addSymbol(this.np, this.sp, hash, symbolTable);
        }
        throw new JSONException("illegal identifier : " + this.ch);
    }

    public final void scanString() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char ch = next();
            if (ch == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            } else if (ch == '\u001a') {
                throw new JSONException("unclosed string : " + ch);
            } else if (ch == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp >= this.sbuf.length) {
                        int newCapcity = this.sbuf.length * 2;
                        if (this.sp > newCapcity) {
                            newCapcity = this.sp;
                        }
                        char[] newsbuf = new char[newCapcity];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                ch = next();
                switch (ch) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar('\u0000');
                        break;
                    case '1':
                        putChar('\u0001');
                        break;
                    case '2':
                        putChar('\u0002');
                        break;
                    case '3':
                        putChar('\u0003');
                        break;
                    case '4':
                        putChar('\u0004');
                        break;
                    case '5':
                        putChar('\u0005');
                        break;
                    case '6':
                        putChar('\u0006');
                        break;
                    case '7':
                        putChar('\u0007');
                        break;
                    case 'F':
                    case 'f':
                        putChar('\f');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case Opcodes.INVOKE_INTERFACE /*114*/:
                        putChar('\r');
                        break;
                    case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                        putChar('\t');
                        break;
                    case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                        char u1 = next();
                        char u2 = next();
                        char u3 = next();
                        char u4 = next();
                        putChar((char) Integer.parseInt(new String(new char[]{u1, u2, u3, u4}), 16));
                        break;
                    case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
                        putChar('\u000b');
                        break;
                    case 'x':
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = ch;
                        throw new JSONException("unclosed string : " + ch);
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(ch);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = ch;
            }
        }
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public final int intValue() {
        int limit;
        int i;
        if (this.np == -1) {
            this.np = 0;
        }
        int result = 0;
        boolean negative = false;
        int i2 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i = i2 + 1;
        } else {
            limit = -2147483647;
            i = i2;
        }
        char chLocal;
        int digit;
        if (negative) {
            if (i < max) {
                result = -digits[charAt(i)];
                i++;
            }
            while (i < max) {
                i2 = i + 1;
                chLocal = charAt(i);
                if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                    break;
                }
                digit = digits[chLocal];
                if (result >= -214748364) {
                    throw new NumberFormatException(numberString());
                }
                result *= 10;
                if (result >= limit + digit) {
                    throw new NumberFormatException(numberString());
                }
                result -= digit;
                i = i2;
            }
            i2 = i;
            if (negative) {
                return -result;
            }
            if (i2 > this.np + 1) {
                return result;
            }
            throw new NumberFormatException(numberString());
        }
        if (i < max) {
            result = -digits[charAt(i)];
            i++;
        }
        while (i < max) {
            i2 = i + 1;
            chLocal = charAt(i);
            digit = digits[chLocal];
            if (result >= -214748364) {
                result *= 10;
                if (result >= limit + digit) {
                    result -= digit;
                    i = i2;
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
            throw new NumberFormatException(numberString());
        }
        i2 = i;
        if (negative) {
            return -result;
        }
        if (i2 > this.np + 1) {
            return result;
        }
        throw new NumberFormatException(numberString());
    }

    public void close() {
        if (this.sbuf.length <= 8192) {
            SBUF_REF_LOCAL.set(new SoftReference(this.sbuf));
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f') {
            return true;
        }
        return false;
    }

    public int scanType(String type) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int bpLocal = this.bp + typeFieldName.length;
        int typeLength = type.length();
        for (int i = 0; i < typeLength; i++) {
            if (type.charAt(i) != charAt(bpLocal + i)) {
                return -1;
            }
        }
        bpLocal += typeLength;
        if (charAt(bpLocal) != '\"') {
            return -1;
        }
        bpLocal++;
        this.ch = charAt(bpLocal);
        if (this.ch == ',') {
            bpLocal++;
            this.ch = charAt(bpLocal);
            this.bp = bpLocal;
            this.token = 16;
            return 3;
        }
        if (this.ch == '}') {
            bpLocal++;
            this.ch = charAt(bpLocal);
            if (this.ch == ',') {
                this.token = 16;
                bpLocal++;
                this.ch = charAt(bpLocal);
            } else if (this.ch == ']') {
                this.token = 15;
                bpLocal++;
                this.ch = charAt(bpLocal);
            } else if (this.ch == '}') {
                this.token = 13;
                bpLocal++;
                this.ch = charAt(bpLocal);
            } else if (this.ch != '\u001a') {
                return -1;
            } else {
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.bp = bpLocal;
        return this.matchStat;
    }

    public final boolean matchField(char[] fieldName) {
        if (!charArrayCompare(fieldName)) {
            return false;
        }
        this.bp += fieldName.length;
        this.ch = charAt(this.bp);
        if (this.ch == '{') {
            next();
            this.token = 12;
        } else if (this.ch == '[') {
            next();
            this.token = 14;
        } else {
            nextToken();
        }
        return true;
    }

    public String scanFieldString(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            if (charAt(this.bp + offset) != '\"') {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            boolean hasSpecial = false;
            int endIndex = indexOf('\"', (this.bp + fieldName.length) + 1);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }
            int startIndex2 = (this.bp + fieldName.length) + 1;
            String stringVal = subString(startIndex2, endIndex - startIndex2);
            for (int i = (this.bp + fieldName.length) + 1; i < endIndex; i++) {
                if (charAt(i) == '\\') {
                    hasSpecial = true;
                    break;
                }
            }
            if (hasSpecial) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            offset = offset2 + ((endIndex - ((this.bp + fieldName.length) + 1)) + 1);
            offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            String strVal = stringVal;
            if (chLocal == ',') {
                this.bp += offset2 - 1;
                next();
                this.matchStat = 3;
                return strVal;
            } else if (chLocal == '}') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return stringDefaultValue();
                }
                this.matchStat = 4;
                return strVal;
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
        }
        this.matchStat = -2;
        return stringDefaultValue();
    }

    public String scanString(char expectNextChar) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        int offset2;
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt((this.bp + 1) + 1) == 'l' && charAt((this.bp + 1) + 2) == 'l') {
                offset2 = (offset + 3) + 1;
                if (charAt(this.bp + 4) == expectNextChar) {
                    this.bp += 4;
                    next();
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            offset2 = offset;
            return null;
        } else if (chLocal != '\"') {
            this.matchStat = -1;
            offset2 = offset;
            return stringDefaultValue();
        } else {
            boolean hasSpecial = false;
            int startIndex = this.bp + 1;
            int endIndex = indexOf('\"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }
            String stringVal = subString(this.bp + 1, endIndex - startIndex);
            for (int i = this.bp + 1; i < endIndex; i++) {
                if (charAt(i) == '\\') {
                    hasSpecial = true;
                    break;
                }
            }
            if (hasSpecial) {
                this.matchStat = -1;
                offset2 = offset;
                return stringDefaultValue();
            }
            offset2 = ((endIndex - (this.bp + 1)) + 1) + 1;
            offset = offset2 + 1;
            String strVal = stringVal;
            if (charAt(this.bp + offset2) == expectNextChar) {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                offset2 = offset;
                return strVal;
            }
            this.matchStat = -1;
            offset2 = offset;
            return strVal;
        }
    }

    public String scanFieldSymbol(char[] fieldName, SymbolTable symbolTable) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            if (charAt(this.bp + offset) != '\"') {
                this.matchStat = -1;
                return null;
            }
            char chLocal;
            int hash = 0;
            offset = offset2;
            while (true) {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal == '\"') {
                    break;
                }
                hash = (hash * 31) + chLocal;
                if (chLocal == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                offset = offset2;
            }
            int start = (this.bp + fieldName.length) + 1;
            String strVal = addSymbol(start, ((this.bp + offset2) - start) - 1, hash, symbolTable);
            offset = offset2 + 1;
            chLocal = charAt(this.bp + offset2);
            if (chLocal == ',') {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                return strVal;
            } else if (chLocal == '}') {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset2 - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return null;
                }
                this.matchStat = 4;
                return strVal;
            } else {
                this.matchStat = -1;
                return null;
            }
        }
        this.matchStat = -2;
        return null;
    }

    public Enum<?> scanEnum(Class<?> enumClass, SymbolTable symbolTable, char serperator) {
        String name = scanSymbolWithSeperator(symbolTable, serperator);
        if (name == null) {
            return null;
        }
        return Enum.valueOf(enumClass, name);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String scanSymbolWithSeperator(com.alibaba.fastjson.parser.SymbolTable r13, char r14) {
        /*
        r12 = this;
        r11 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r8 = 34;
        r10 = 3;
        r6 = 0;
        r9 = -1;
        r7 = 0;
        r12.matchStat = r7;
        r3 = 0;
        r7 = r12.bp;
        r4 = r3 + 1;
        r7 = r7 + r3;
        r0 = r12.charAt(r7);
        r7 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r0 != r7) goto L_0x005d;
    L_0x0018:
        r7 = r12.bp;
        r7 = r7 + 1;
        r7 = r12.charAt(r7);
        r8 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        if (r7 != r8) goto L_0x0056;
    L_0x0024:
        r7 = r12.bp;
        r7 = r7 + 1;
        r7 = r7 + 1;
        r7 = r12.charAt(r7);
        if (r7 != r11) goto L_0x0056;
    L_0x0030:
        r7 = r12.bp;
        r7 = r7 + 1;
        r7 = r7 + 2;
        r7 = r12.charAt(r7);
        if (r7 != r11) goto L_0x0056;
    L_0x003c:
        r3 = r4 + 3;
        r7 = r12.bp;
        r3 = r3 + 1;
        r7 = r7 + 4;
        r0 = r12.charAt(r7);
        if (r0 != r14) goto L_0x005a;
    L_0x004a:
        r7 = r12.bp;
        r7 = r7 + 4;
        r12.bp = r7;
        r12.next();
        r12.matchStat = r10;
    L_0x0055:
        return r6;
    L_0x0056:
        r12.matchStat = r9;
        r3 = r4;
        goto L_0x0055;
    L_0x005a:
        r12.matchStat = r9;
        goto L_0x0055;
    L_0x005d:
        if (r0 == r8) goto L_0x0063;
    L_0x005f:
        r12.matchStat = r9;
        r3 = r4;
        goto L_0x0055;
    L_0x0063:
        r1 = 0;
        r3 = r4;
    L_0x0065:
        r7 = r12.bp;
        r4 = r3 + 1;
        r7 = r7 + r3;
        r0 = r12.charAt(r7);
        if (r0 != r8) goto L_0x0098;
    L_0x0070:
        r7 = r12.bp;
        r7 = r7 + 0;
        r5 = r7 + 1;
        r7 = r12.bp;
        r7 = r7 + r4;
        r7 = r7 - r5;
        r2 = r7 + -1;
        r6 = r12.addSymbol(r5, r2, r1, r13);
        r7 = r12.bp;
        r3 = r4 + 1;
        r7 = r7 + r4;
        r0 = r12.charAt(r7);
        if (r0 != r14) goto L_0x00a4;
    L_0x008b:
        r7 = r12.bp;
        r8 = r3 + -1;
        r7 = r7 + r8;
        r12.bp = r7;
        r12.next();
        r12.matchStat = r10;
        goto L_0x0055;
    L_0x0098:
        r7 = r1 * 31;
        r1 = r7 + r0;
        r7 = 92;
        if (r0 != r7) goto L_0x00a7;
    L_0x00a0:
        r12.matchStat = r9;
        r3 = r4;
        goto L_0x0055;
    L_0x00a4:
        r12.matchStat = r9;
        goto L_0x0055;
    L_0x00a7:
        r3 = r4;
        goto L_0x0065;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanSymbolWithSeperator(com.alibaba.fastjson.parser.SymbolTable, char):java.lang.String");
    }

    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            Collection<String> list;
            if (type.isAssignableFrom(HashSet.class)) {
                list = new HashSet();
            } else if (type.isAssignableFrom(ArrayList.class)) {
                list = new ArrayList();
            } else {
                try {
                    list = (Collection) type.newInstance();
                } catch (Exception e) {
                    throw new JSONException(e.getMessage(), e);
                }
            }
            int offset = fieldName.length;
            int offset2 = offset + 1;
            if (charAt(this.bp + offset) != '[') {
                this.matchStat = -1;
                return null;
            }
            offset = offset2 + 1;
            char chLocal = charAt(this.bp + offset2);
            while (chLocal == '\"') {
                int startOffset = offset;
                while (true) {
                    offset2 = offset + 1;
                    chLocal = charAt(this.bp + offset);
                    if (chLocal == '\"') {
                        break;
                    } else if (chLocal == '\\') {
                        this.matchStat = -1;
                        return null;
                    } else {
                        offset = offset2;
                    }
                }
                int start = this.bp + startOffset;
                list.add(subString(start, ((this.bp + offset2) - start) - 1));
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == ',') {
                    offset2 = offset + 1;
                    chLocal = charAt(this.bp + offset);
                    offset = offset2;
                } else if (chLocal == ']') {
                    offset2 = offset + 1;
                    chLocal = charAt(this.bp + offset);
                    if (chLocal == ',') {
                        this.bp += offset2 - 1;
                        next();
                        this.matchStat = 3;
                        return list;
                    } else if (chLocal == '}') {
                        offset = offset2 + 1;
                        chLocal = charAt(this.bp + offset2);
                        if (chLocal == ',') {
                            this.token = 16;
                            this.bp += offset - 1;
                            next();
                        } else if (chLocal == ']') {
                            this.token = 15;
                            this.bp += offset - 1;
                            next();
                        } else if (chLocal == '}') {
                            this.token = 13;
                            this.bp += offset - 1;
                            next();
                        } else if (chLocal == '\u001a') {
                            this.bp += offset - 1;
                            this.token = 20;
                            this.ch = '\u001a';
                        } else {
                            this.matchStat = -1;
                            return null;
                        }
                        this.matchStat = 4;
                        return list;
                    } else {
                        this.matchStat = -1;
                        return null;
                    }
                } else {
                    this.matchStat = -1;
                    return null;
                }
            }
            this.matchStat = -1;
            return null;
        }
        this.matchStat = -2;
        return null;
    }

    public Collection<String> scanStringArray(Class<?> type, char seperator) {
        Collection<String> list;
        this.matchStat = 0;
        if (type.isAssignableFrom(HashSet.class)) {
            list = new HashSet();
        } else if (type.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList();
        } else {
            try {
                list = (Collection) type.newInstance();
            } catch (Exception e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        int offset2;
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt((this.bp + 1) + 1) == 'l' && charAt((this.bp + 1) + 2) == 'l') {
                offset2 = (offset + 3) + 1;
                if (charAt(this.bp + 4) == seperator) {
                    this.bp += 4;
                    next();
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            offset2 = offset;
            return null;
        } else if (chLocal != '[') {
            this.matchStat = -1;
            offset2 = offset;
            return null;
        } else {
            offset2 = offset + 1;
            chLocal = charAt(this.bp + 1);
            while (true) {
                if (chLocal == 'n' && charAt(this.bp + offset2) == 'u' && charAt((this.bp + offset2) + 1) == 'l' && charAt((this.bp + offset2) + 2) == 'l') {
                    offset2 += 3;
                    offset = offset2 + 1;
                    chLocal = charAt(this.bp + offset2);
                } else if (chLocal != '\"') {
                    this.matchStat = -1;
                    return null;
                } else {
                    int startOffset = offset2;
                    while (true) {
                        offset = offset2 + 1;
                        chLocal = charAt(this.bp + offset2);
                        if (chLocal == '\"') {
                            break;
                        } else if (chLocal == '\\') {
                            this.matchStat = -1;
                            offset2 = offset;
                            return null;
                        } else {
                            offset2 = offset;
                        }
                    }
                    int start = this.bp + startOffset;
                    list.add(subString(start, ((this.bp + offset) - start) - 1));
                    offset2 = offset + 1;
                    chLocal = charAt(this.bp + offset);
                    offset = offset2;
                }
                if (chLocal != ',') {
                    break;
                }
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
            }
            if (chLocal == ']') {
                offset2 = offset + 1;
                if (charAt(this.bp + offset) == seperator) {
                    this.bp += offset2 - 1;
                    next();
                    this.matchStat = 3;
                    return list;
                }
                this.matchStat = -1;
                return list;
            }
            this.matchStat = -1;
            offset2 = offset;
            return null;
        }
    }

    public int scanFieldInt(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            if (chLocal < '0' || chLocal > '9') {
                this.matchStat = -1;
                return 0;
            }
            int value = digits[chLocal];
            offset = offset2;
            while (true) {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    value = (value * 10) + digits[chLocal];
                    offset = offset2;
                }
            }
            if (chLocal == '.') {
                this.matchStat = -1;
                return 0;
            } else if (value < 0) {
                this.matchStat = -1;
                return 0;
            } else if (chLocal == ',') {
                this.bp += offset2 - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (chLocal == '}') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return 0;
            }
        }
        this.matchStat = -2;
        return 0;
    }

    public boolean scanBoolean(char expectNext) {
        int offset;
        this.matchStat = 0;
        int offset2 = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        boolean value = false;
        if (chLocal == 't') {
            if (charAt(this.bp + 1) == 'r' && charAt((this.bp + 1) + 1) == 'u' && charAt((this.bp + 1) + 2) == 'e') {
                offset = (offset2 + 3) + 1;
                chLocal = charAt(this.bp + 4);
                value = true;
            } else {
                this.matchStat = -1;
                offset = offset2;
                return false;
            }
        } else if (chLocal != 'f') {
            offset = offset2;
        } else if (charAt(this.bp + 1) == 'a' && charAt((this.bp + 1) + 1) == 'l' && charAt((this.bp + 1) + 2) == 's' && charAt((this.bp + 1) + 3) == 'e') {
            offset = (offset2 + 4) + 1;
            chLocal = charAt(this.bp + 5);
            value = false;
        } else {
            this.matchStat = -1;
            offset = offset2;
            return false;
        }
        if (chLocal == expectNext) {
            this.bp += offset - 1;
            next();
            this.matchStat = 3;
            return value;
        }
        this.matchStat = -1;
        return value;
    }

    public int scanInt(char expectNext) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            int i = offset;
            return 0;
        }
        int value = digits[chLocal];
        i = offset;
        while (true) {
            offset = i + 1;
            chLocal = charAt(this.bp + i);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (value * 10) + digits[chLocal];
                i = offset;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            i = offset;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            i = offset;
            return 0;
        } else if (chLocal == expectNext) {
            this.bp += offset - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            i = offset;
            return value;
        } else {
            this.matchStat = -1;
            i = offset;
            return value;
        }
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            boolean value;
            int offset = fieldName.length;
            int offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            if (chLocal == 't') {
                offset = offset2 + 1;
                if (charAt(this.bp + offset2) != 'r') {
                    this.matchStat = -1;
                    return false;
                }
                offset2 = offset + 1;
                if (charAt(this.bp + offset) != 'u') {
                    this.matchStat = -1;
                    return false;
                }
                offset = offset2 + 1;
                if (charAt(this.bp + offset2) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                value = true;
            } else if (chLocal == 'f') {
                offset = offset2 + 1;
                if (charAt(this.bp + offset2) != 'a') {
                    this.matchStat = -1;
                    return false;
                }
                offset2 = offset + 1;
                if (charAt(this.bp + offset) != 'l') {
                    this.matchStat = -1;
                    return false;
                }
                offset = offset2 + 1;
                if (charAt(this.bp + offset2) != 's') {
                    this.matchStat = -1;
                    return false;
                }
                offset2 = offset + 1;
                if (charAt(this.bp + offset) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                value = false;
                offset = offset2;
            } else {
                this.matchStat = -1;
                return false;
            }
            offset2 = offset + 1;
            chLocal = charAt(this.bp + offset);
            if (chLocal == ',') {
                this.bp += offset2 - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (chLocal == '}') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return false;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return false;
            }
        }
        this.matchStat = -2;
        return false;
    }

    public long scanFieldLong(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            if (chLocal < '0' || chLocal > '9') {
                this.matchStat = -1;
                return 0;
            }
            long value = (long) digits[chLocal];
            offset = offset2;
            while (true) {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    value = (10 * value) + ((long) digits[chLocal]);
                    offset = offset2;
                }
            }
            if (chLocal == '.') {
                this.matchStat = -1;
                return 0;
            } else if (value < 0) {
                this.matchStat = -1;
                return 0;
            } else if (chLocal == ',') {
                this.bp += offset2 - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (chLocal == '}') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return 0;
            }
        }
        this.matchStat = -2;
        return 0;
    }

    public long scanLong(char expectNextChar) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            int i = offset;
            return 0;
        }
        long value = (long) digits[chLocal];
        i = offset;
        while (true) {
            offset = i + 1;
            chLocal = charAt(this.bp + i);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (10 * value) + ((long) digits[chLocal]);
                i = offset;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            i = offset;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            i = offset;
            return 0;
        } else if (chLocal == expectNextChar) {
            this.bp += offset - 1;
            next();
            this.matchStat = 3;
            this.token = 16;
            i = offset;
            return value;
        } else {
            this.matchStat = -1;
            i = offset;
            return value;
        }
    }

    public final float scanFieldFloat(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            if (chLocal < '0' || chLocal > '9') {
                this.matchStat = -1;
                return 0.0f;
            }
            offset = offset2;
            while (true) {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    offset = offset2;
                }
            }
            if (chLocal == '.') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal >= '0' && chLocal <= '9') {
                    while (true) {
                        offset2 = offset + 1;
                        chLocal = charAt(this.bp + offset);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        offset = offset2;
                    }
                } else {
                    this.matchStat = -1;
                    return 0.0f;
                }
            }
            offset = offset2;
            int start = this.bp + fieldName.length;
            float value = Float.parseFloat(subString(start, ((this.bp + offset) - start) - 1));
            if (chLocal == ',') {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (chLocal == '}') {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.bp += offset2 - 1;
                    this.token = 20;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return 0.0f;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        }
        this.matchStat = -2;
        return 0.0f;
    }

    public final float scanFloat(char seperator) {
        float f = 0.0f;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        int i;
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            i = offset;
        } else {
            i = offset;
            while (true) {
                offset = i + 1;
                chLocal = charAt(this.bp + i);
                if (chLocal >= '0' && chLocal <= '9') {
                    i = offset;
                }
            }
            if (chLocal == '.') {
                i = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    while (true) {
                        offset = i + 1;
                        chLocal = charAt(this.bp + i);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        i = offset;
                    }
                } else {
                    this.matchStat = -1;
                }
            }
            i = offset;
            int start = this.bp;
            f = Float.parseFloat(subString(start, ((this.bp + i) - start) - 1));
            if (chLocal == seperator) {
                this.bp += i - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
            } else {
                this.matchStat = -1;
            }
        }
        return f;
    }

    public final double scanFieldDouble(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(fieldName)) {
            int offset = fieldName.length;
            int offset2 = offset + 1;
            char chLocal = charAt(this.bp + offset);
            if (chLocal < '0' || chLocal > '9') {
                this.matchStat = -1;
                return 0.0d;
            }
            offset = offset2;
            while (true) {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    offset = offset2;
                }
            }
            if (chLocal == '.') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal >= '0' && chLocal <= '9') {
                    while (true) {
                        offset2 = offset + 1;
                        chLocal = charAt(this.bp + offset);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        offset = offset2;
                    }
                } else {
                    this.matchStat = -1;
                    return 0.0d;
                }
            }
            if (chLocal == 'e' || chLocal == 'E') {
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal == '+' || chLocal == '-') {
                    offset2 = offset + 1;
                    chLocal = charAt(this.bp + offset);
                } else {
                    offset2 = offset;
                }
                while (chLocal >= '0' && chLocal <= '9') {
                    offset = offset2 + 1;
                    chLocal = charAt(this.bp + offset2);
                    offset2 = offset;
                }
            }
            offset = offset2;
            int start = this.bp + fieldName.length;
            double value = Double.parseDouble(subString(start, ((this.bp + offset) - start) - 1));
            if (chLocal == ',') {
                this.bp += offset - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (chLocal == '}') {
                offset2 = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal == ',') {
                    this.token = 16;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == ']') {
                    this.token = 15;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '}') {
                    this.token = 13;
                    this.bp += offset2 - 1;
                    next();
                } else if (chLocal == '\u001a') {
                    this.token = 20;
                    this.bp += offset2 - 1;
                    this.ch = '\u001a';
                } else {
                    this.matchStat = -1;
                    return 0.0d;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return 0.0d;
            }
        }
        this.matchStat = -2;
        return 0.0d;
    }

    public final double scanFieldDouble(char seperator) {
        double d = 0.0d;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        int i;
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            i = offset;
        } else {
            i = offset;
            while (true) {
                offset = i + 1;
                chLocal = charAt(this.bp + i);
                if (chLocal >= '0' && chLocal <= '9') {
                    i = offset;
                }
            }
            if (chLocal == '.') {
                i = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal >= '0' && chLocal <= '9') {
                    while (true) {
                        offset = i + 1;
                        chLocal = charAt(this.bp + i);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        i = offset;
                    }
                } else {
                    this.matchStat = -1;
                }
            }
            if (chLocal == 'e' || chLocal == 'E') {
                i = offset + 1;
                chLocal = charAt(this.bp + offset);
                if (chLocal == '+' || chLocal == '-') {
                    offset = i + 1;
                    chLocal = charAt(this.bp + i);
                } else {
                    offset = i;
                }
                while (chLocal >= '0' && chLocal <= '9') {
                    i = offset + 1;
                    chLocal = charAt(this.bp + offset);
                    offset = i;
                }
            }
            i = offset;
            int start = this.bp;
            d = Double.parseDouble(subString(start, ((this.bp + i) - start) - 1));
            if (chLocal == seperator) {
                this.bp += i - 1;
                next();
                this.matchStat = 3;
                this.token = 16;
            } else {
                this.matchStat = -1;
            }
        }
        return d;
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 6;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanTreeSet() {
        if (this.ch != 'T') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 22;
            return;
        }
        throw new JSONException("scan set error");
    }

    public final void scanNullOrNew() {
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        if (this.ch == 'u') {
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse true");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
                this.token = 8;
                return;
            }
            throw new JSONException("scan true error");
        } else if (this.ch != 'e') {
            throw new JSONException("error parse e");
        } else {
            next();
            if (this.ch != 'w') {
                throw new JSONException("error parse w");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
                this.token = 9;
                return;
            }
            throw new JSONException("scan true error");
        }
    }

    public final void scanUndefined() {
        if (this.ch != 'u') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'i') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'n') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'd') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 23;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\u001a' || this.ch == '\f' || this.ch == '\b') {
            this.token = 7;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        Integer tok = (Integer) this.keywods.get(stringVal());
        if (tok != null) {
            this.token = tok.intValue();
        } else {
            this.token = 18;
        }
    }

    public final boolean isBlankInput() {
        int i = 0;
        while (true) {
            char chLocal = charAt(i);
            if (chLocal == '\u001a') {
                return true;
            }
            if (!isWhitespace(chLocal)) {
                return false;
            }
            i++;
        }
    }

    public final void skipWhitespace() {
        while (this.ch < whitespaceFlags.length && whitespaceFlags[this.ch]) {
            next();
        }
    }

    private final void scanStringSingleQuote() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char chLocal = next();
            if (chLocal == '\'') {
                this.token = 4;
                next();
                return;
            } else if (chLocal == '\u001a') {
                throw new JSONException("unclosed single-quote string");
            } else if (chLocal == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp > this.sbuf.length) {
                        char[] newsbuf = new char[(this.sp * 2)];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                chLocal = next();
                switch (chLocal) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar('\u0000');
                        break;
                    case '1':
                        putChar('\u0001');
                        break;
                    case '2':
                        putChar('\u0002');
                        break;
                    case '3':
                        putChar('\u0003');
                        break;
                    case '4':
                        putChar('\u0004');
                        break;
                    case '5':
                        putChar('\u0005');
                        break;
                    case '6':
                        putChar('\u0006');
                        break;
                    case '7':
                        putChar('\u0007');
                        break;
                    case 'F':
                    case 'f':
                        putChar('\f');
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'b':
                        putChar('\b');
                        break;
                    case 'n':
                        putChar('\n');
                        break;
                    case Opcodes.INVOKE_INTERFACE /*114*/:
                        putChar('\r');
                        break;
                    case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                        putChar('\t');
                        break;
                    case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                        char c1 = next();
                        char c2 = next();
                        char c3 = next();
                        char c4 = next();
                        putChar((char) Integer.parseInt(new String(new char[]{c1, c2, c3, c4}), 16));
                        break;
                    case Opcodes.INVOKE_DIRECT_RANGE /*118*/:
                        putChar('\u000b');
                        break;
                    case 'x':
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = chLocal;
                        throw new JSONException("unclosed single-quote string");
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(chLocal);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = chLocal;
            }
        }
    }

    public final void scanSet() {
        if (this.ch != 'S') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == '\n' || this.ch == '\r' || this.ch == '\t' || this.ch == '\f' || this.ch == '\b' || this.ch == '[' || this.ch == '(') {
            this.token = 21;
            return;
        }
        throw new JSONException("scan set error");
    }

    protected final void putChar(char ch) {
        if (this.sp == this.sbuf.length) {
            char[] newsbuf = new char[(this.sbuf.length * 2)];
            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
            this.sbuf = newsbuf;
        }
        char[] cArr = this.sbuf;
        int i = this.sp;
        this.sp = i + 1;
        cArr[i] = ch;
    }

    public final void scanNumber() {
        this.np = this.bp;
        if (this.ch == '-') {
            this.sp++;
            next();
        }
        while (this.ch >= '0' && this.ch <= '9') {
            this.sp++;
            next();
        }
        boolean isDouble = false;
        if (this.ch == '.') {
            this.sp++;
            next();
            isDouble = true;
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
        }
        if (this.ch == 'L') {
            this.sp++;
            next();
        } else if (this.ch == 'S') {
            this.sp++;
            next();
        } else if (this.ch == 'B') {
            this.sp++;
            next();
        } else if (this.ch == 'F') {
            this.sp++;
            next();
            isDouble = true;
        } else if (this.ch == 'D') {
            this.sp++;
            next();
            isDouble = true;
        } else if (this.ch == 'e' || this.ch == 'E') {
            this.sp++;
            next();
            if (this.ch == '+' || this.ch == '-') {
                this.sp++;
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
            if (this.ch == 'D' || this.ch == 'F') {
                this.sp++;
                next();
            }
            isDouble = true;
        }
        if (isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public final long longValue() throws NumberFormatException {
        long limit;
        int i;
        long result = 0;
        boolean negative = false;
        int i2 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i2 + 1;
        } else {
            limit = -9223372036854775807L;
            i = i2;
        }
        char chLocal;
        int digit;
        if (negative) {
            if (i < max) {
                result = (long) (-digits[charAt(i)]);
                i++;
            }
            while (i < max) {
                i2 = i + 1;
                chLocal = charAt(i);
                if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                    break;
                }
                digit = digits[chLocal];
                if (result >= -922337203685477580L) {
                    throw new NumberFormatException(numberString());
                }
                result *= 10;
                if (result >= ((long) digit) + limit) {
                    throw new NumberFormatException(numberString());
                }
                result -= (long) digit;
                i = i2;
            }
            i2 = i;
            if (negative) {
                return -result;
            }
            if (i2 > this.np + 1) {
                return result;
            }
            throw new NumberFormatException(numberString());
        }
        if (i < max) {
            result = (long) (-digits[charAt(i)]);
            i++;
        }
        while (i < max) {
            i2 = i + 1;
            chLocal = charAt(i);
            digit = digits[chLocal];
            if (result >= -922337203685477580L) {
                result *= 10;
                if (result >= ((long) digit) + limit) {
                    result -= (long) digit;
                    i = i2;
                } else {
                    throw new NumberFormatException(numberString());
                }
            }
            throw new NumberFormatException(numberString());
        }
        i2 = i;
        if (negative) {
            return -result;
        }
        if (i2 > this.np + 1) {
            return result;
        }
        throw new NumberFormatException(numberString());
    }

    public final Number decimalValue(boolean decimal) {
        char chLocal = charAt((this.np + this.sp) - 1);
        if (chLocal == 'F') {
            return Float.valueOf(Float.parseFloat(numberString()));
        }
        if (chLocal == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        }
        if (decimal) {
            return decimalValue();
        }
        return Double.valueOf(doubleValue());
    }

    public final BigDecimal decimalValue() {
        return new BigDecimal(numberString());
    }

    public static final boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t' || ch == '\f' || ch == '\b';
    }
}
