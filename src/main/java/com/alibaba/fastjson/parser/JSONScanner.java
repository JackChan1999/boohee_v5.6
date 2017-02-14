package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.Base64;
import com.alipay.sdk.sys.a;
import com.boohee.one.cache.FileCache;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

public final class JSONScanner extends JSONLexerBase {
    protected static final char[] typeFieldName = (a.e + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    public final int ISO8601_LEN_0;
    public final int ISO8601_LEN_1;
    public final int ISO8601_LEN_2;
    private final String text;

    public JSONScanner(String input) {
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features) {
        this.ISO8601_LEN_0 = "0000-00-00".length();
        this.ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
        this.ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();
        this.features = features;
        this.text = input;
        this.bp = -1;
        next();
        if (this.ch == '﻿') {
            next();
        }
    }

    public final char charAt(int index) {
        if (index >= this.text.length()) {
            return '\u001a';
        }
        return this.text.charAt(index);
    }

    public final char next() {
        int i = this.bp + 1;
        this.bp = i;
        char charAt = charAt(i);
        this.ch = charAt;
        return charAt;
    }

    public JSONScanner(char[] input, int inputLength) {
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features) {
        this(new String(input, 0, inputLength), features);
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        this.text.getChars(offset, offset + count, dest, 0);
    }

    public final int scanType(String type) {
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, typeFieldName)) {
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

    static final boolean charArrayCompare(String src, int offset, char[] dest) {
        int destLen = dest.length;
        if (destLen + offset > src.length()) {
            return false;
        }
        for (int i = 0; i < destLen; i++) {
            if (dest[i] != src.charAt(offset + i)) {
                return false;
            }
        }
        return true;
    }

    public final boolean charArrayCompare(char[] chars) {
        return charArrayCompare(this.text, this.bp, chars);
    }

    public final int indexOf(char ch, int startIndex) {
        return this.text.indexOf(ch, startIndex);
    }

    public final String addSymbol(int offset, int len, int hash, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, offset, len, hash);
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(this.text, this.np + 1, this.sp);
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    public final String subString(int offset, int count) {
        if (!ASMUtils.isAndroid()) {
            return this.text.substring(offset, offset + count);
        }
        char[] chars = new char[count];
        for (int i = offset; i < offset + count; i++) {
            chars[i - offset] = this.text.charAt(i);
        }
        return new String(chars);
    }

    public final String numberString() {
        char chLocal = charAt((this.np + this.sp) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        return subString(this.np, sp);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = this.text.length() - this.bp;
        if (!strict && rest > 13) {
            char c0 = charAt(this.bp);
            char c1 = charAt(this.bp + 1);
            char c2 = charAt(this.bp + 2);
            char c3 = charAt(this.bp + 3);
            char c4 = charAt(this.bp + 4);
            char c5 = charAt(this.bp + 5);
            char c_r0 = charAt((this.bp + rest) - 1);
            char c_r1 = charAt((this.bp + rest) - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/' && c_r1 == ')') {
                int plusIndex = -1;
                for (int i = 6; i < rest; i++) {
                    char c = charAt(this.bp + i);
                    if (c != '+') {
                        if (c < '0' || c > '9') {
                            break;
                        }
                    } else {
                        plusIndex = i;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = this.bp + 6;
                long millis = Long.parseLong(subString(offset, plusIndex - offset));
                this.calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                this.calendar.setTimeInMillis(millis);
                this.token = 5;
                return true;
            }
        }
        char y0;
        char y1;
        char y2;
        char y3;
        char M0;
        char M1;
        char d0;
        char d1;
        char h0;
        char h1;
        char m0;
        char m1;
        char s0;
        char s1;
        char S0;
        char S1;
        char S2;
        int millis2;
        int minute;
        int seconds;
        if (rest == 8 || rest == 14 || rest == 17) {
            if (strict) {
                return false;
            }
            y0 = charAt(this.bp);
            y1 = charAt(this.bp + 1);
            y2 = charAt(this.bp + 2);
            y3 = charAt(this.bp + 3);
            M0 = charAt(this.bp + 4);
            M1 = charAt(this.bp + 5);
            d0 = charAt(this.bp + 6);
            d1 = charAt(this.bp + 7);
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            int hour;
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            if (rest != 8) {
                h0 = charAt(this.bp + 8);
                h1 = charAt(this.bp + 9);
                m0 = charAt(this.bp + 10);
                m1 = charAt(this.bp + 11);
                s0 = charAt(this.bp + 12);
                s1 = charAt(this.bp + 13);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                if (rest == 17) {
                    S0 = charAt(this.bp + 14);
                    S1 = charAt(this.bp + 15);
                    S2 = charAt(this.bp + 16);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    if (S1 < '0' || S1 > '9') {
                        return false;
                    }
                    if (S2 < '0' || S2 > '9') {
                        return false;
                    }
                    millis2 = ((digits[S0] * 100) + (digits[S1] * 10)) + digits[S2];
                } else {
                    millis2 = 0;
                }
                hour = (digits[h0] * 10) + digits[h1];
                minute = (digits[m0] * 10) + digits[m1];
                seconds = (digits[s0] * 10) + digits[s1];
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis2 = 0;
            }
            this.calendar.set(11, hour);
            this.calendar.set(12, minute);
            this.calendar.set(13, seconds);
            this.calendar.set(14, millis2);
            this.token = 5;
            return true;
        } else if (rest < this.ISO8601_LEN_0) {
            return false;
        } else {
            if (charAt(this.bp + 4) != '-') {
                return false;
            }
            if (charAt(this.bp + 7) != '-') {
                return false;
            }
            y0 = charAt(this.bp);
            y1 = charAt(this.bp + 1);
            y2 = charAt(this.bp + 2);
            y3 = charAt(this.bp + 3);
            M0 = charAt(this.bp + 5);
            M1 = charAt(this.bp + 6);
            d0 = charAt(this.bp + 8);
            d1 = charAt(this.bp + 9);
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            char t = charAt(this.bp + 10);
            int i2;
            if (t == 'T' || (t == ' ' && !strict)) {
                if (rest < this.ISO8601_LEN_1) {
                    return false;
                }
                if (charAt(this.bp + 13) != ':') {
                    return false;
                }
                if (charAt(this.bp + 16) != ':') {
                    return false;
                }
                h0 = charAt(this.bp + 11);
                h1 = charAt(this.bp + 12);
                m0 = charAt(this.bp + 14);
                m1 = charAt(this.bp + 15);
                s0 = charAt(this.bp + 17);
                s1 = charAt(this.bp + 18);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                minute = (digits[m0] * 10) + digits[m1];
                seconds = (digits[s0] * 10) + digits[s1];
                this.calendar.set(11, (digits[h0] * 10) + digits[h1]);
                this.calendar.set(12, minute);
                this.calendar.set(13, seconds);
                if (charAt(this.bp + 19) != '.') {
                    this.calendar.set(14, 0);
                    i2 = this.bp + 19;
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.token = 5;
                    return true;
                } else if (rest < this.ISO8601_LEN_2) {
                    return false;
                } else {
                    S0 = charAt(this.bp + 20);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    millis2 = digits[S0];
                    int millisLen = 1;
                    S1 = charAt(this.bp + 21);
                    if (S1 >= '0' && S1 <= '9') {
                        millis2 = (millis2 * 10) + digits[S1];
                        millisLen = 2;
                    }
                    if (millisLen == 2) {
                        S2 = charAt(this.bp + 22);
                        if (S2 >= '0' && S2 <= '9') {
                            millis2 = (millis2 * 10) + digits[S2];
                            millisLen = 3;
                        }
                    }
                    this.calendar.set(14, millis2);
                    int timzeZoneLength = 0;
                    char timeZoneFlag = charAt((this.bp + 20) + millisLen);
                    if (timeZoneFlag == '+' || timeZoneFlag == '-') {
                        char t0 = charAt(((this.bp + 20) + millisLen) + 1);
                        if (t0 < '0' || t0 > '1') {
                            return false;
                        }
                        char t1 = charAt(((this.bp + 20) + millisLen) + 2);
                        if (t1 < '0' || t1 > '9') {
                            return false;
                        }
                        char t2 = charAt(((this.bp + 20) + millisLen) + 3);
                        if (t2 == ':') {
                            if (charAt(((this.bp + 20) + millisLen) + 4) != '0') {
                                return false;
                            }
                            if (charAt(((this.bp + 20) + millisLen) + 5) != '0') {
                                return false;
                            }
                            timzeZoneLength = 6;
                        } else if (t2 != '0') {
                            timzeZoneLength = 3;
                        } else if (charAt(((this.bp + 20) + millisLen) + 4) != '0') {
                            return false;
                        } else {
                            timzeZoneLength = 5;
                        }
                        int timeZoneOffset = (((digits[t0] * 10) + digits[t1]) * FileCache.TIME_HOUR) * 1000;
                        if (timeZoneFlag == '-') {
                            timeZoneOffset = -timeZoneOffset;
                        }
                        if (this.calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
                            String[] timeZoneIDs = TimeZone.getAvailableIDs(timeZoneOffset);
                            if (timeZoneIDs.length > 0) {
                                this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
                            }
                        }
                    }
                    char end = charAt(this.bp + ((millisLen + 20) + timzeZoneLength));
                    if (end != '\u001a' && end != '\"') {
                        return false;
                    }
                    i2 = this.bp + ((millisLen + 20) + timzeZoneLength);
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.token = 5;
                    return true;
                }
            } else if (t != '\"' && t != '\u001a') {
                return false;
            } else {
                this.calendar.set(11, 0);
                this.calendar.set(12, 0);
                this.calendar.set(13, 0);
                this.calendar.set(14, 0);
                i2 = this.bp + 10;
                this.bp = i2;
                this.ch = charAt(i2);
                this.token = 5;
                return true;
            }
        }
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 != '2' || h1 < '0') {
            return false;
        } else {
            if (h1 > '4') {
                return false;
            }
        }
        if (m0 < '0' || m0 > '5') {
            if (m0 != '6') {
                return false;
            }
            if (m1 != '0') {
                return false;
            }
        } else if (m1 < '0' || m1 > '9') {
            return false;
        }
        if (s0 < '0' || s0 > '5') {
            if (s0 != '6') {
                return false;
            }
            if (s1 != '0') {
                return false;
            }
        } else if (s1 < '0' || s1 > '9') {
            return false;
        }
        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        this.calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        int month = ((digits[M0] * 10) + digits[M1]) - 1;
        int day = (digits[d0] * 10) + digits[d1];
        this.calendar.set(1, (((digits[y0] * 1000) + (digits[y1] * 100)) + (digits[y2] * 10)) + digits[y3]);
        this.calendar.set(2, month);
        this.calendar.set(5, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if ((y0 != '1' && y0 != '2') || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return false;
        }
        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else if (M0 != '1') {
            return false;
        } else {
            if (!(M1 == '0' || M1 == '1' || M1 == '2')) {
                return false;
            }
        }
        if (d0 == 48) {
            if (d1 < 49 || d1 > 57) {
                return false;
            }
        } else if (d0 == 49 || d0 == 50) {
            if (d1 < 48) {
                return false;
            }
            if (d1 > 57) {
                return false;
            }
        } else if (d0 != 51) {
            return false;
        } else {
            if (!(d1 == 48 || d1 == 49)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEOF() {
        return this.bp == this.text.length() || (this.ch == '\u001a' && this.bp + 1 == this.text.length());
    }

    public int scanFieldInt(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            if (ch < '0' || ch > '9') {
                this.matchStat = -1;
                return 0;
            }
            int value = digits[ch];
            index = index2;
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch >= '0' && ch <= '9') {
                    value = (value * 10) + digits[ch];
                    index = index2;
                }
            }
            if (ch == '.') {
                this.matchStat = -1;
                return 0;
            }
            this.bp = index2 - 1;
            if (value < 0) {
                this.matchStat = -1;
                return 0;
            } else if (ch == ',') {
                r6 = this.bp + 1;
                this.bp = r6;
                this.ch = charAt(r6);
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (ch != '}') {
                return value;
            } else {
                int i = this.bp + 1;
                this.bp = i;
                ch = charAt(i);
                if (ch == ',') {
                    this.token = 16;
                    r6 = this.bp + 1;
                    this.bp = r6;
                    this.ch = charAt(r6);
                } else if (ch == ']') {
                    this.token = 15;
                    r6 = this.bp + 1;
                    this.bp = r6;
                    this.ch = charAt(r6);
                } else if (ch == '}') {
                    this.token = 13;
                    r6 = this.bp + 1;
                    this.bp = r6;
                    this.ch = charAt(r6);
                } else if (ch == '\u001a') {
                    this.token = 20;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                return value;
            }
        }
        this.matchStat = -2;
        return 0;
    }

    public String scanFieldString(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            if (charAt(index) != '\"') {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            boolean hasSpecial = false;
            int startIndex = index2;
            int endIndex = this.text.indexOf(34, startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }
            String stringVal = subString(startIndex, endIndex - startIndex);
            for (int i = 0; i < stringVal.length(); i++) {
                if (stringVal.charAt(i) == '\\') {
                    hasSpecial = true;
                    break;
                }
            }
            if (hasSpecial) {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.bp = endIndex + 1;
            char ch = charAt(this.bp);
            this.ch = ch;
            String strVal = stringVal;
            int i2;
            if (ch == ',') {
                i2 = this.bp + 1;
                this.bp = i2;
                this.ch = charAt(i2);
                this.matchStat = 3;
                return strVal;
            } else if (ch == '}') {
                i2 = this.bp + 1;
                this.bp = i2;
                ch = charAt(i2);
                if (ch == ',') {
                    this.token = 16;
                    i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = charAt(i2);
                } else if (ch == ']') {
                    this.token = 15;
                    i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = charAt(i2);
                } else if (ch == '}') {
                    this.token = 13;
                    i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = charAt(i2);
                } else if (ch == '\u001a') {
                    this.token = 20;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
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

    public String scanFieldSymbol(char[] fieldName, SymbolTable symbolTable) {
        this.matchStat = 0;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            if (charAt(index) != '\"') {
                this.matchStat = -1;
                return null;
            }
            char ch;
            int start = index2;
            int hash = 0;
            index = index2;
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch == '\"') {
                    break;
                }
                hash = (hash * 31) + ch;
                if (ch == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                index = index2;
            }
            this.bp = index2;
            ch = charAt(this.bp);
            this.ch = ch;
            String strVal = symbolTable.addSymbol(this.text, start, (index2 - start) - 1, hash);
            int i;
            if (ch == ',') {
                i = this.bp + 1;
                this.bp = i;
                this.ch = charAt(i);
                this.matchStat = 3;
                return strVal;
            } else if (ch == '}') {
                int i2 = this.bp + 1;
                this.bp = i2;
                ch = charAt(i2);
                if (ch == ',') {
                    this.token = 16;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == ']') {
                    this.token = 15;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '}') {
                    this.token = 13;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '\u001a') {
                    this.token = 20;
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

    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        this.matchStat = 0;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
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
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            if (charAt(index) != '[') {
                this.matchStat = -1;
                return null;
            }
            index = index2 + 1;
            char ch = charAt(index2);
            while (ch == '\"') {
                int start = index;
                while (true) {
                    index2 = index + 1;
                    ch = charAt(index);
                    if (ch == '\"') {
                        break;
                    } else if (ch == '\\') {
                        this.matchStat = -1;
                        return null;
                    } else {
                        index = index2;
                    }
                }
                list.add(subString(start, (index2 - start) - 1));
                index = index2 + 1;
                ch = charAt(index2);
                if (ch == ',') {
                    index2 = index + 1;
                    ch = charAt(index);
                    index = index2;
                } else if (ch == ']') {
                    index2 = index + 1;
                    ch = charAt(index);
                    this.bp = index2;
                    if (ch == ',') {
                        this.ch = charAt(this.bp);
                        this.matchStat = 3;
                        return list;
                    } else if (ch == '}') {
                        ch = charAt(this.bp);
                        int i;
                        if (ch == ',') {
                            this.token = 16;
                            i = this.bp + 1;
                            this.bp = i;
                            this.ch = charAt(i);
                        } else if (ch == ']') {
                            this.token = 15;
                            i = this.bp + 1;
                            this.bp = i;
                            this.ch = charAt(i);
                        } else if (ch == '}') {
                            this.token = 13;
                            i = this.bp + 1;
                            this.bp = i;
                            this.ch = charAt(i);
                        } else if (ch == '\u001a') {
                            this.token = 20;
                            this.ch = ch;
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

    public long scanFieldLong(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            if (ch < '0' || ch > '9') {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return 0;
            }
            long value = (long) digits[ch];
            index = index2;
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch >= '0' && ch <= '9') {
                    value = (10 * value) + ((long) digits[ch]);
                    index = index2;
                }
            }
            if (ch == '.') {
                this.matchStat = -1;
                return 0;
            }
            this.bp = index2 - 1;
            if (value < 0) {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return 0;
            } else if (ch == ',') {
                r5 = this.bp + 1;
                this.bp = r5;
                ch = charAt(r5);
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (ch == '}') {
                r5 = this.bp + 1;
                this.bp = r5;
                ch = charAt(r5);
                if (ch == ',') {
                    this.token = 16;
                    r5 = this.bp + 1;
                    this.bp = r5;
                    this.ch = charAt(r5);
                } else if (ch == ']') {
                    this.token = 15;
                    r5 = this.bp + 1;
                    this.bp = r5;
                    this.ch = charAt(r5);
                } else if (ch == '}') {
                    this.token = 13;
                    r5 = this.bp + 1;
                    this.bp = r5;
                    this.ch = charAt(r5);
                } else if (ch == '\u001a') {
                    this.token = 20;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
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

    public boolean scanFieldBoolean(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            boolean value;
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            if (ch == 't') {
                index = index2 + 1;
                if (charAt(index2) != 'r') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'u') {
                    this.matchStat = -1;
                    return false;
                }
                index = index2 + 1;
                if (charAt(index2) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                this.bp = index;
                ch = charAt(this.bp);
                value = true;
            } else if (ch == 'f') {
                index = index2 + 1;
                if (charAt(index2) != 'a') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'l') {
                    this.matchStat = -1;
                    return false;
                }
                index = index2 + 1;
                if (charAt(index2) != 's') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                this.bp = index2;
                ch = charAt(this.bp);
                value = false;
                index = index2;
            } else {
                this.matchStat = -1;
                return false;
            }
            int i;
            if (ch == ',') {
                i = this.bp + 1;
                this.bp = i;
                this.ch = charAt(i);
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (ch == '}') {
                int i2 = this.bp + 1;
                this.bp = i2;
                ch = charAt(i2);
                if (ch == ',') {
                    this.token = 16;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == ']') {
                    this.token = 15;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '}') {
                    this.token = 13;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '\u001a') {
                    this.token = 20;
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

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        this.text.getChars(srcPos, srcPos + length, dest, destPos);
    }
}
