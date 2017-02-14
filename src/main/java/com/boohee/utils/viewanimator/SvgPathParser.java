package com.boohee.utils.viewanimator;

import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

public class SvgPathParser {

    private static class ParserHelper {
        private static final double[] pow10 = new double[128];
        private char         current;
        private int          length;
        private int          position;
        private CharSequence s;

        public ParserHelper(CharSequence s, int position) {
            this.s = s;
            this.position = position;
            this.length = s.length();
            this.current = s.charAt(position);
        }

        private char read() {
            if (this.position < this.length) {
                this.position++;
            }
            if (this.position == this.length) {
                return '\u0000';
            }
            return this.s.charAt(this.position);
        }

        public int getPosition() {
            return this.position;
        }

        public void skipWhitespace() {
            while (this.position < this.length && Character.isWhitespace(this.s.charAt(this
                    .position))) {
                advance();
            }
        }

        public void skipNumberSeparator() {
            while (this.position < this.length) {
                switch (this.s.charAt(this.position)) {
                    case '\t':
                    case '\n':
                    case ' ':
                    case ',':
                        advance();
                    default:
                        return;
                }
            }
        }

        public void advance() {
            this.current = read();
        }

        public float parseFloat() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r12 = this;
            r11 = 9;
            r8 = 0;
            r4 = 0;
            r5 = 0;
            r6 = 1;
            r7 = 0;
            r0 = 0;
            r2 = 0;
            r1 = 0;
            r3 = 1;
            r9 = r12.current;
            switch(r9) {
                case 43: goto L_0x0019;
                case 44: goto L_0x0010;
                case 45: goto L_0x0018;
                default: goto L_0x0010;
            };
        L_0x0010:
            r9 = r12.current;
            switch(r9) {
                case 46: goto L_0x002d;
                case 47: goto L_0x0015;
                case 48: goto L_0x0020;
                case 49: goto L_0x0046;
                case 50: goto L_0x0046;
                case 51: goto L_0x0046;
                case 52: goto L_0x0046;
                case 53: goto L_0x0046;
                case 54: goto L_0x0046;
                case 55: goto L_0x0046;
                case 56: goto L_0x0046;
                case 57: goto L_0x0046;
                default: goto L_0x0015;
            };
        L_0x0015:
            r8 = 2143289344; // 0x7fc00000 float:NaN double:1.058925634E-314;
        L_0x0017:
            return r8;
        L_0x0018:
            r6 = 0;
        L_0x0019:
            r9 = r12.read();
            r12.current = r9;
            goto L_0x0010;
        L_0x0020:
            r7 = 1;
        L_0x0021:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 46: goto L_0x002d;
                case 48: goto L_0x0021;
                case 49: goto L_0x0046;
                case 50: goto L_0x0046;
                case 51: goto L_0x0046;
                case 52: goto L_0x0046;
                case 53: goto L_0x0046;
                case 54: goto L_0x0046;
                case 55: goto L_0x0046;
                case 56: goto L_0x0046;
                case 57: goto L_0x0046;
                case 69: goto L_0x002d;
                case 101: goto L_0x002d;
                default: goto L_0x002c;
            };
        L_0x002c:
            goto L_0x0017;
        L_0x002d:
            r9 = r12.current;
            r10 = 46;
            if (r9 != r10) goto L_0x0073;
        L_0x0033:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 48: goto L_0x0062;
                case 49: goto L_0x0084;
                case 50: goto L_0x0084;
                case 51: goto L_0x0084;
                case 52: goto L_0x0084;
                case 53: goto L_0x0084;
                case 54: goto L_0x0084;
                case 55: goto L_0x0084;
                case 56: goto L_0x0084;
                case 57: goto L_0x0084;
                default: goto L_0x003e;
            };
        L_0x003e:
            if (r7 != 0) goto L_0x0073;
        L_0x0040:
            r9 = r12.current;
            r12.reportUnexpectedCharacterError(r9);
            goto L_0x0017;
        L_0x0046:
            r7 = 1;
        L_0x0047:
            if (r5 >= r11) goto L_0x005f;
        L_0x0049:
            r5 = r5 + 1;
            r9 = r4 * 10;
            r10 = r12.current;
            r10 = r10 + -48;
            r4 = r9 + r10;
        L_0x0053:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 48: goto L_0x0047;
                case 49: goto L_0x0047;
                case 50: goto L_0x0047;
                case 51: goto L_0x0047;
                case 52: goto L_0x0047;
                case 53: goto L_0x0047;
                case 54: goto L_0x0047;
                case 55: goto L_0x0047;
                case 56: goto L_0x0047;
                case 57: goto L_0x0047;
                default: goto L_0x005e;
            };
        L_0x005e:
            goto L_0x002d;
        L_0x005f:
            r1 = r1 + 1;
            goto L_0x0053;
        L_0x0062:
            if (r5 != 0) goto L_0x0084;
        L_0x0064:
            r9 = r12.read();
            r12.current = r9;
            r1 = r1 + -1;
            r9 = r12.current;
            switch(r9) {
                case 48: goto L_0x0064;
                case 49: goto L_0x0084;
                case 50: goto L_0x0084;
                case 51: goto L_0x0084;
                case 52: goto L_0x0084;
                case 53: goto L_0x0084;
                case 54: goto L_0x0084;
                case 55: goto L_0x0084;
                case 56: goto L_0x0084;
                case 57: goto L_0x0084;
                default: goto L_0x0071;
            };
        L_0x0071:
            if (r7 == 0) goto L_0x0017;
        L_0x0073:
            r9 = r12.current;
            switch(r9) {
                case 69: goto L_0x009e;
                case 101: goto L_0x009e;
                default: goto L_0x0078;
            };
        L_0x0078:
            if (r3 != 0) goto L_0x007b;
        L_0x007a:
            r0 = -r0;
        L_0x007b:
            r0 = r0 + r1;
            if (r6 != 0) goto L_0x007f;
        L_0x007e:
            r4 = -r4;
        L_0x007f:
            r8 = buildFloat(r4, r0);
            goto L_0x0017;
        L_0x0084:
            if (r5 >= r11) goto L_0x0092;
        L_0x0086:
            r5 = r5 + 1;
            r9 = r4 * 10;
            r10 = r12.current;
            r10 = r10 + -48;
            r4 = r9 + r10;
            r1 = r1 + -1;
        L_0x0092:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 48: goto L_0x0084;
                case 49: goto L_0x0084;
                case 50: goto L_0x0084;
                case 51: goto L_0x0084;
                case 52: goto L_0x0084;
                case 53: goto L_0x0084;
                case 54: goto L_0x0084;
                case 55: goto L_0x0084;
                case 56: goto L_0x0084;
                case 57: goto L_0x0084;
                default: goto L_0x009d;
            };
        L_0x009d:
            goto L_0x0073;
        L_0x009e:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 43: goto L_0x00b1;
                case 44: goto L_0x00a9;
                case 45: goto L_0x00b0;
                case 46: goto L_0x00a9;
                case 47: goto L_0x00a9;
                case 48: goto L_0x00c3;
                case 49: goto L_0x00c3;
                case 50: goto L_0x00c3;
                case 51: goto L_0x00c3;
                case 52: goto L_0x00c3;
                case 53: goto L_0x00c3;
                case 54: goto L_0x00c3;
                case 55: goto L_0x00c3;
                case 56: goto L_0x00c3;
                case 57: goto L_0x00c3;
                default: goto L_0x00a9;
            };
        L_0x00a9:
            r9 = r12.current;
            r12.reportUnexpectedCharacterError(r9);
            goto L_0x0017;
        L_0x00b0:
            r3 = 0;
        L_0x00b1:
            r9 = r12.read();
            r12.current = r9;
            r9 = r12.current;
            switch(r9) {
                case 48: goto L_0x00c3;
                case 49: goto L_0x00c3;
                case 50: goto L_0x00c3;
                case 51: goto L_0x00c3;
                case 52: goto L_0x00c3;
                case 53: goto L_0x00c3;
                case 54: goto L_0x00c3;
                case 55: goto L_0x00c3;
                case 56: goto L_0x00c3;
                case 57: goto L_0x00c3;
                default: goto L_0x00bc;
            };
        L_0x00bc:
            r9 = r12.current;
            r12.reportUnexpectedCharacterError(r9);
            goto L_0x0017;
        L_0x00c3:
            r8 = r12.current;
            switch(r8) {
                case 48: goto L_0x00c9;
                case 49: goto L_0x00d5;
                case 50: goto L_0x00d5;
                case 51: goto L_0x00d5;
                case 52: goto L_0x00d5;
                case 53: goto L_0x00d5;
                case 54: goto L_0x00d5;
                case 55: goto L_0x00d5;
                case 56: goto L_0x00d5;
                case 57: goto L_0x00d5;
                default: goto L_0x00c8;
            };
        L_0x00c8:
            goto L_0x0078;
        L_0x00c9:
            r8 = r12.read();
            r12.current = r8;
            r8 = r12.current;
            switch(r8) {
                case 48: goto L_0x00c9;
                case 49: goto L_0x00d5;
                case 50: goto L_0x00d5;
                case 51: goto L_0x00d5;
                case 52: goto L_0x00d5;
                case 53: goto L_0x00d5;
                case 54: goto L_0x00d5;
                case 55: goto L_0x00d5;
                case 56: goto L_0x00d5;
                case 57: goto L_0x00d5;
                default: goto L_0x00d4;
            };
        L_0x00d4:
            goto L_0x0078;
        L_0x00d5:
            r8 = 3;
            if (r2 >= r8) goto L_0x00e2;
        L_0x00d8:
            r2 = r2 + 1;
            r8 = r0 * 10;
            r9 = r12.current;
            r9 = r9 + -48;
            r0 = r8 + r9;
        L_0x00e2:
            r8 = r12.read();
            r12.current = r8;
            r8 = r12.current;
            switch(r8) {
                case 48: goto L_0x00d5;
                case 49: goto L_0x00d5;
                case 50: goto L_0x00d5;
                case 51: goto L_0x00d5;
                case 52: goto L_0x00d5;
                case 53: goto L_0x00d5;
                case 54: goto L_0x00d5;
                case 55: goto L_0x00d5;
                case 56: goto L_0x00d5;
                case 57: goto L_0x00d5;
                default: goto L_0x00ed;
            };
        L_0x00ed:
            goto L_0x0078;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.boohee.utils" +
                    ".viewanimator.SvgPathParser.ParserHelper.parseFloat():float");
        }

        private void reportUnexpectedCharacterError(char c) {
            throw new RuntimeException("Unexpected char '" + c + "'.");
        }

        public static float buildFloat(int mant, int exp) {
            if (exp < -125 || mant == 0) {
                return 0.0f;
            }
            if (exp >= 128) {
                return mant > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
            } else {
                if (exp == 0) {
                    return (float) mant;
                }
                if (mant >= 67108864) {
                    mant++;
                }
                return (float) (exp > 0 ? ((double) mant) * pow10[exp] : ((double) mant) /
                        pow10[-exp]);
            }
        }

        static {
            for (int i = 0; i < pow10.length; i++) {
                pow10[i] = Math.pow(10.0d, (double) i);
            }
        }

        public float nextFloat() {
            skipWhitespace();
            float f = parseFloat();
            skipNumberSeparator();
            return f;
        }
    }

    @Nullable
    @WorkerThread
    public static Path tryParsePath(String dAttributeOfPath) {
        Path path = null;
        try {
            path = parsePath(dAttributeOfPath);
        } catch (Exception e) {
            Log.e(path, "parse svg path error", e);
        }
        return path;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.support.annotation.WorkerThread
    public static android.graphics.Path parsePath(java.lang.String r30) throws java.lang.Exception {
        /*
        r23 = r30.length();
        r20 = new com.boohee.utils.viewanimator.SvgPathParser$ParserHelper;
        r9 = 0;
        r0 = r20;
        r1 = r30;
        r0.<init>(r1, r9);
        r20.skipWhitespace();
        r2 = new android.graphics.Path;
        r2.<init>();
        r10 = 0;
        r11 = 0;
        r21 = 0;
        r22 = 0;
        r25 = 0;
        r26 = 0;
        r24 = 0;
    L_0x0022:
        r9 = r20.getPosition();
        r0 = r23;
        if (r9 >= r0) goto L_0x02c3;
    L_0x002a:
        r9 = r20.getPosition();
        r0 = r30;
        r19 = r0.charAt(r9);
        switch(r19) {
            case 43: goto L_0x004b;
            case 44: goto L_0x0037;
            case 45: goto L_0x004b;
            case 46: goto L_0x0037;
            case 47: goto L_0x0037;
            case 48: goto L_0x004b;
            case 49: goto L_0x004b;
            case 50: goto L_0x004b;
            case 51: goto L_0x004b;
            case 52: goto L_0x004b;
            case 53: goto L_0x004b;
            case 54: goto L_0x004b;
            case 55: goto L_0x004b;
            case 56: goto L_0x004b;
            case 57: goto L_0x004b;
            default: goto L_0x0037;
        };
    L_0x0037:
        r20.advance();
        r24 = r19;
    L_0x003c:
        r27 = 0;
        switch(r19) {
            case 65: goto L_0x0251;
            case 67: goto L_0x0183;
            case 72: goto L_0x0129;
            case 76: goto L_0x00ef;
            case 77: goto L_0x007b;
            case 83: goto L_0x01f2;
            case 86: goto L_0x0156;
            case 90: goto L_0x00bb;
            case 97: goto L_0x0251;
            case 99: goto L_0x0183;
            case 104: goto L_0x0129;
            case 108: goto L_0x00ef;
            case 109: goto L_0x007b;
            case 115: goto L_0x01f2;
            case 118: goto L_0x0156;
            case 122: goto L_0x00bb;
            default: goto L_0x0041;
        };
    L_0x0041:
        if (r27 != 0) goto L_0x0047;
    L_0x0043:
        r21 = r10;
        r22 = r11;
    L_0x0047:
        r20.skipWhitespace();
        goto L_0x0022;
    L_0x004b:
        r9 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r0 = r24;
        if (r0 == r9) goto L_0x0057;
    L_0x0051:
        r9 = 77;
        r0 = r24;
        if (r0 != r9) goto L_0x005d;
    L_0x0057:
        r9 = r24 + -1;
        r0 = (char) r9;
        r19 = r0;
        goto L_0x003c;
    L_0x005d:
        r9 = 99;
        r0 = r24;
        if (r0 == r9) goto L_0x0069;
    L_0x0063:
        r9 = 67;
        r0 = r24;
        if (r0 != r9) goto L_0x006c;
    L_0x0069:
        r19 = r24;
        goto L_0x003c;
    L_0x006c:
        r9 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r0 = r24;
        if (r0 == r9) goto L_0x0078;
    L_0x0072:
        r9 = 76;
        r0 = r24;
        if (r0 != r9) goto L_0x0037;
    L_0x0078:
        r19 = r24;
        goto L_0x003c;
    L_0x007b:
        r7 = r20.nextFloat();
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "move to: [%s,%s]";
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        r0 = r19;
        if (r0 != r9) goto L_0x00b1;
    L_0x00a7:
        r25 = r25 + r7;
        r26 = r26 + r8;
        r2.rMoveTo(r7, r8);
        r10 = r10 + r7;
        r11 = r11 + r8;
        goto L_0x0041;
    L_0x00b1:
        r25 = r7;
        r26 = r8;
        r2.moveTo(r7, r8);
        r10 = r7;
        r11 = r8;
        goto L_0x0041;
    L_0x00bb:
        r9 = 0;
        r12 = "close, move to: [%s,%s]";
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r25);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r26);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r2.close();
        r0 = r25;
        r1 = r26;
        r2.moveTo(r0, r1);
        r10 = r25;
        r11 = r26;
        r21 = r25;
        r22 = r26;
        r27 = 1;
        goto L_0x0041;
    L_0x00ef:
        r7 = r20.nextFloat();
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "line to: [%s,%s]";
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        r0 = r19;
        if (r0 != r9) goto L_0x0122;
    L_0x011b:
        r2.rLineTo(r7, r8);
        r10 = r10 + r7;
        r11 = r11 + r8;
        goto L_0x0041;
    L_0x0122:
        r2.lineTo(r7, r8);
        r10 = r7;
        r11 = r8;
        goto L_0x0041;
    L_0x0129:
        r7 = r20.nextFloat();
        r9 = 0;
        r12 = "horizontal line to: [%s]";
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        r0 = r19;
        if (r0 != r9) goto L_0x0150;
    L_0x0149:
        r9 = 0;
        r2.rLineTo(r7, r9);
        r10 = r10 + r7;
        goto L_0x0041;
    L_0x0150:
        r2.lineTo(r7, r11);
        r10 = r7;
        goto L_0x0041;
    L_0x0156:
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "vertical line to: [%s]";
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 118; // 0x76 float:1.65E-43 double:5.83E-322;
        r0 = r19;
        if (r0 != r9) goto L_0x017d;
    L_0x0176:
        r9 = 0;
        r2.rLineTo(r9, r8);
        r11 = r11 + r8;
        goto L_0x0041;
    L_0x017d:
        r2.lineTo(r10, r8);
        r11 = r8;
        goto L_0x0041;
    L_0x0183:
        r27 = 1;
        r3 = r20.nextFloat();
        r4 = r20.nextFloat();
        r5 = r20.nextFloat();
        r6 = r20.nextFloat();
        r7 = r20.nextFloat();
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "cubic to: [%s,%s][%s,%s][%s,%s]";
        r13 = 6;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r3);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r4);
        r13[r28] = r29;
        r28 = 2;
        r29 = java.lang.Float.valueOf(r5);
        r13[r28] = r29;
        r28 = 3;
        r29 = java.lang.Float.valueOf(r6);
        r13[r28] = r29;
        r28 = 4;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r28 = 5;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 99;
        r0 = r19;
        if (r0 != r9) goto L_0x01e7;
    L_0x01e1:
        r3 = r3 + r10;
        r5 = r5 + r10;
        r7 = r7 + r10;
        r4 = r4 + r11;
        r6 = r6 + r11;
        r8 = r8 + r11;
    L_0x01e7:
        r2.cubicTo(r3, r4, r5, r6, r7, r8);
        r21 = r5;
        r22 = r6;
        r10 = r7;
        r11 = r8;
        goto L_0x0041;
    L_0x01f2:
        r27 = 1;
        r5 = r20.nextFloat();
        r6 = r20.nextFloat();
        r7 = r20.nextFloat();
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "cubic to: [%s,%s][%s,%s]";
        r13 = 4;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r5);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r6);
        r13[r28] = r29;
        r28 = 2;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r28 = 3;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        r0 = r19;
        if (r0 != r9) goto L_0x023c;
    L_0x0238:
        r5 = r5 + r10;
        r7 = r7 + r10;
        r6 = r6 + r11;
        r8 = r8 + r11;
    L_0x023c:
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r9 = r9 * r10;
        r3 = r9 - r21;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r9 = r9 * r11;
        r4 = r9 - r22;
        r2.cubicTo(r3, r4, r5, r6, r7, r8);
        r21 = r5;
        r22 = r6;
        r10 = r7;
        r11 = r8;
        goto L_0x0041;
    L_0x0251:
        r14 = r20.nextFloat();
        r15 = r20.nextFloat();
        r16 = r20.nextFloat();
        r9 = r20.nextFloat();
        r0 = (int) r9;
        r17 = r0;
        r9 = r20.nextFloat();
        r0 = (int) r9;
        r18 = r0;
        r7 = r20.nextFloat();
        r8 = r20.nextFloat();
        r9 = 0;
        r12 = "arc to: [%s,%s][%s][%s,%s][%s,%s]";
        r13 = 7;
        r13 = new java.lang.Object[r13];
        r28 = 0;
        r29 = java.lang.Float.valueOf(r14);
        r13[r28] = r29;
        r28 = 1;
        r29 = java.lang.Float.valueOf(r15);
        r13[r28] = r29;
        r28 = 2;
        r29 = java.lang.Float.valueOf(r16);
        r13[r28] = r29;
        r28 = 3;
        r29 = java.lang.Integer.valueOf(r17);
        r13[r28] = r29;
        r28 = 4;
        r29 = java.lang.Integer.valueOf(r18);
        r13[r28] = r29;
        r28 = 5;
        r29 = java.lang.Float.valueOf(r7);
        r13[r28] = r29;
        r28 = 6;
        r29 = java.lang.Float.valueOf(r8);
        r13[r28] = r29;
        r12 = java.lang.String.format(r12, r13);
        android.util.Log.d(r9, r12);
        r9 = r2;
        r12 = r7;
        r13 = r8;
        drawArc(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r10 = r7;
        r11 = r8;
        goto L_0x0041;
    L_0x02c3:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.boohee.utils" +
                ".viewanimator.SvgPathParser.parsePath(java.lang.String):android.graphics.Path");
    }

    private static void drawArc(Path p, float lastX, float lastY, float x, float y, float rx,
                                float ry, float theta, int largeArc, int sweepArc) {
    }
}
