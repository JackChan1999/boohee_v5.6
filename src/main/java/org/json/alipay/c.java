package org.json.alipay;

import com.tencent.tinker.android.dx.instruction.Opcodes;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

public final class c {
    private int a;
    private Reader b;
    private char c;
    private boolean d;

    private c(Reader reader) {
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader);
        }
        this.b = reader;
        this.d = false;
        this.a = 0;
    }

    public c(String str) {
        this(new StringReader(str));
    }

    private String a(int i) {
        int i2 = 0;
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        if (this.d) {
            this.d = false;
            cArr[0] = this.c;
            i2 = 1;
        }
        while (i2 < i) {
            try {
                int read = this.b.read(cArr, i2, i - i2);
                if (read == -1) {
                    break;
                }
                i2 += read;
            } catch (Throwable e) {
                throw new JSONException(e);
            }
        }
        this.a += i2;
        if (i2 < i) {
            throw a("Substring bounds error");
        }
        this.c = cArr[i - 1];
        return new String(cArr);
    }

    public final JSONException a(String str) {
        return new JSONException(str + toString());
    }

    public final void a() {
        if (this.d || this.a <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.a--;
        this.d = true;
    }

    public final char b() {
        if (this.d) {
            this.d = false;
            if (this.c != '\u0000') {
                this.a++;
            }
            return this.c;
        }
        try {
            int read = this.b.read();
            if (read <= 0) {
                this.c = '\u0000';
                return '\u0000';
            }
            this.a++;
            this.c = (char) read;
            return this.c;
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final char c() {
        /*
        r5 = this;
        r4 = 13;
        r3 = 10;
        r0 = 47;
    L_0x0006:
        r1 = r5.b();
        if (r1 != r0) goto L_0x003d;
    L_0x000c:
        r1 = r5.b();
        switch(r1) {
            case 42: goto L_0x002f;
            case 47: goto L_0x0017;
            default: goto L_0x0013;
        };
    L_0x0013:
        r5.a();
    L_0x0016:
        return r0;
    L_0x0017:
        r1 = r5.b();
        if (r1 == r3) goto L_0x0006;
    L_0x001d:
        if (r1 == r4) goto L_0x0006;
    L_0x001f:
        if (r1 != 0) goto L_0x0017;
    L_0x0021:
        goto L_0x0006;
    L_0x0022:
        r2 = 42;
        if (r1 != r2) goto L_0x002f;
    L_0x0026:
        r1 = r5.b();
        if (r1 == r0) goto L_0x0006;
    L_0x002c:
        r5.a();
    L_0x002f:
        r1 = r5.b();
        if (r1 != 0) goto L_0x0022;
    L_0x0035:
        r0 = "Unclosed comment";
        r0 = r5.a(r0);
        throw r0;
    L_0x003d:
        r2 = 35;
        if (r1 != r2) goto L_0x004c;
    L_0x0041:
        r1 = r5.b();
        if (r1 == r3) goto L_0x0006;
    L_0x0047:
        if (r1 == r4) goto L_0x0006;
    L_0x0049:
        if (r1 != 0) goto L_0x0041;
    L_0x004b:
        goto L_0x0006;
    L_0x004c:
        if (r1 == 0) goto L_0x0052;
    L_0x004e:
        r2 = 32;
        if (r1 <= r2) goto L_0x0006;
    L_0x0052:
        r0 = r1;
        goto L_0x0016;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.json.alipay.c.c():char");
    }

    public final Object d() {
        char c = c();
        switch (c) {
            case '\"':
            case '\'':
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    char b = b();
                    switch (b) {
                        case '\u0000':
                        case '\n':
                        case '\r':
                            throw a("Unterminated string");
                        case '\\':
                            b = b();
                            switch (b) {
                                case 'b':
                                    stringBuffer.append('\b');
                                    break;
                                case 'f':
                                    stringBuffer.append('\f');
                                    break;
                                case 'n':
                                    stringBuffer.append('\n');
                                    break;
                                case Opcodes.INVOKE_INTERFACE /*114*/:
                                    stringBuffer.append('\r');
                                    break;
                                case Opcodes.INVOKE_VIRTUAL_RANGE /*116*/:
                                    stringBuffer.append('\t');
                                    break;
                                case Opcodes.INVOKE_SUPER_RANGE /*117*/:
                                    stringBuffer.append((char) Integer.parseInt(a(4), 16));
                                    break;
                                case 'x':
                                    stringBuffer.append((char) Integer.parseInt(a(2), 16));
                                    break;
                                default:
                                    stringBuffer.append(b);
                                    break;
                            }
                        default:
                            if (b != c) {
                                stringBuffer.append(b);
                                break;
                            }
                            return stringBuffer.toString();
                    }
                }
            case '(':
            case '[':
                a();
                return new a(this);
            case Opcodes.NEG_INT /*123*/:
                a();
                return new b(this);
            default:
                StringBuffer stringBuffer2 = new StringBuffer();
                char c2 = c;
                while (c2 >= ' ' && ",:]}/\\\"[{;=#".indexOf(c2) < 0) {
                    stringBuffer2.append(c2);
                    c2 = b();
                }
                a();
                String trim = stringBuffer2.toString().trim();
                if (trim.equals("")) {
                    throw a("Missing value");
                } else if (trim.equalsIgnoreCase("true")) {
                    return Boolean.TRUE;
                } else {
                    if (trim.equalsIgnoreCase("false")) {
                        return Boolean.FALSE;
                    }
                    if (trim.equalsIgnoreCase("null")) {
                        return b.a;
                    }
                    if ((c < '0' || c > '9') && c != '.' && c != '-' && c != '+') {
                        return trim;
                    }
                    if (c == '0') {
                        if (trim.length() <= 2 || !(trim.charAt(1) == 'x' || trim.charAt(1) == 'X')) {
                            try {
                                return new Integer(Integer.parseInt(trim, 8));
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                return new Integer(Integer.parseInt(trim.substring(2), 16));
                            } catch (Exception e2) {
                            }
                        }
                    }
                    try {
                        return new Integer(trim);
                    } catch (Exception e3) {
                        try {
                            return new Long(trim);
                        } catch (Exception e4) {
                            try {
                                return new Double(trim);
                            } catch (Exception e5) {
                                return trim;
                            }
                        }
                    }
                }
        }
    }

    public final String toString() {
        return " at character " + this.a;
    }
}
