package com.alibaba.fastjson.parser.deserializer;

public class StackTraceElementDeserializer implements ObjectDeserializer {
    public static final StackTraceElementDeserializer instance = new StackTraceElementDeserializer();

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r11, java.lang.reflect.Type r12, java.lang.Object r13) {
        /*
        r10 = this;
        r4 = r11.getLexer();
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x0011;
    L_0x000c:
        r4.nextToken();
        r7 = 0;
    L_0x0010:
        return r7;
    L_0x0011:
        r7 = r4.token();
        r8 = 12;
        if (r7 == r8) goto L_0x0043;
    L_0x0019:
        r7 = r4.token();
        r8 = 16;
        if (r7 == r8) goto L_0x0043;
    L_0x0021:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error: ";
        r8 = r8.append(r9);
        r9 = r4.token();
        r9 = com.alibaba.fastjson.parser.JSONToken.name(r9);
        r8 = r8.append(r9);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0043:
        r0 = 0;
        r6 = 0;
        r2 = 0;
        r5 = 0;
    L_0x0047:
        r7 = r11.getSymbolTable();
        r3 = r4.scanSymbol(r7);
        if (r3 != 0) goto L_0x0074;
    L_0x0051:
        r7 = r4.token();
        r8 = 13;
        if (r7 != r8) goto L_0x0064;
    L_0x0059:
        r7 = 16;
        r4.nextToken(r7);
    L_0x005e:
        r7 = new java.lang.StackTraceElement;
        r7.<init>(r0, r6, r2, r5);
        goto L_0x0010;
    L_0x0064:
        r7 = r4.token();
        r8 = 16;
        if (r7 != r8) goto L_0x0074;
    L_0x006c:
        r7 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;
        r7 = r4.isEnabled(r7);
        if (r7 != 0) goto L_0x0047;
    L_0x0074:
        r7 = 4;
        r4.nextTokenWithColon(r7);
        r7 = "className";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00ad;
    L_0x0081:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x0098;
    L_0x0089:
        r0 = 0;
    L_0x008a:
        r7 = r4.token();
        r8 = 13;
        if (r7 != r8) goto L_0x0047;
    L_0x0092:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x005e;
    L_0x0098:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00a4;
    L_0x009f:
        r0 = r4.stringVal();
        goto L_0x008a;
    L_0x00a4:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00ad:
        r7 = "methodName";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00d5;
    L_0x00b6:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x00c0;
    L_0x00be:
        r6 = 0;
        goto L_0x008a;
    L_0x00c0:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00cc;
    L_0x00c7:
        r6 = r4.stringVal();
        goto L_0x008a;
    L_0x00cc:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00d5:
        r7 = "fileName";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x00fd;
    L_0x00de:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x00e8;
    L_0x00e6:
        r2 = 0;
        goto L_0x008a;
    L_0x00e8:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x00f4;
    L_0x00ef:
        r2 = r4.stringVal();
        goto L_0x008a;
    L_0x00f4:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x00fd:
        r7 = "lineNumber";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x0127;
    L_0x0106:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x0111;
    L_0x010e:
        r5 = 0;
        goto L_0x008a;
    L_0x0111:
        r7 = r4.token();
        r8 = 2;
        if (r7 != r8) goto L_0x011e;
    L_0x0118:
        r5 = r4.intValue();
        goto L_0x008a;
    L_0x011e:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x0127:
        r7 = "nativeMethod";
        r7 = r7.equals(r3);
        if (r7 == 0) goto L_0x0164;
    L_0x0130:
        r7 = r4.token();
        r8 = 8;
        if (r7 != r8) goto L_0x013f;
    L_0x0138:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x008a;
    L_0x013f:
        r7 = r4.token();
        r8 = 6;
        if (r7 != r8) goto L_0x014d;
    L_0x0146:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x008a;
    L_0x014d:
        r7 = r4.token();
        r8 = 7;
        if (r7 != r8) goto L_0x015b;
    L_0x0154:
        r7 = 16;
        r4.nextToken(r7);
        goto L_0x008a;
    L_0x015b:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x0164:
        r7 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;
        if (r3 != r7) goto L_0x01a7;
    L_0x0168:
        r7 = r4.token();
        r8 = 4;
        if (r7 != r8) goto L_0x0196;
    L_0x016f:
        r1 = r4.stringVal();
        r7 = "java.lang.StackTraceElement";
        r7 = r1.equals(r7);
        if (r7 != 0) goto L_0x008a;
    L_0x017c:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error : ";
        r8 = r8.append(r9);
        r8 = r8.append(r1);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
    L_0x0196:
        r7 = r4.token();
        r8 = 8;
        if (r7 == r8) goto L_0x008a;
    L_0x019e:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = "syntax error";
        r7.<init>(r8);
        throw r7;
    L_0x01a7:
        r7 = new com.alibaba.fastjson.JSONException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "syntax error : ";
        r8 = r8.append(r9);
        r8 = r8.append(r3);
        r8 = r8.toString();
        r7.<init>(r8);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):T");
    }

    public int getFastMatchToken() {
        return 12;
    }
}
