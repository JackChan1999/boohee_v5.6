package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz) {
        super(mapping, clazz);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r16, java.lang.reflect.Type r17, java.lang.Object r18) {
        /*
        r15 = this;
        r9 = r16.getLexer();
        r13 = r9.token();
        r14 = 8;
        if (r13 != r14) goto L_0x0011;
    L_0x000c:
        r9.nextToken();
        r4 = 0;
    L_0x0010:
        return r4;
    L_0x0011:
        r13 = r16.getResolveStatus();
        r14 = 2;
        if (r13 != r14) goto L_0x0061;
    L_0x0018:
        r13 = 0;
        r0 = r16;
        r0.setResolveStatus(r13);
    L_0x001e:
        r1 = 0;
        r6 = 0;
        if (r17 == 0) goto L_0x0035;
    L_0x0022:
        r0 = r17;
        r13 = r0 instanceof java.lang.Class;
        if (r13 == 0) goto L_0x0035;
    L_0x0028:
        r2 = r17;
        r2 = (java.lang.Class) r2;
        r13 = java.lang.Throwable.class;
        r13 = r13.isAssignableFrom(r2);
        if (r13 == 0) goto L_0x0035;
    L_0x0034:
        r6 = r2;
    L_0x0035:
        r10 = 0;
        r12 = 0;
        r11 = new java.util.HashMap;
        r11.<init>();
    L_0x003c:
        r13 = r16.getSymbolTable();
        r8 = r9.scanSymbol(r13);
        if (r8 != 0) goto L_0x0082;
    L_0x0046:
        r13 = r9.token();
        r14 = 13;
        if (r13 != r14) goto L_0x0072;
    L_0x004e:
        r13 = 16;
        r9.nextToken(r13);
    L_0x0053:
        r4 = 0;
        if (r6 != 0) goto L_0x0116;
    L_0x0056:
        r4 = new java.lang.Exception;
        r4.<init>(r10, r1);
    L_0x005b:
        if (r12 == 0) goto L_0x0010;
    L_0x005d:
        r4.setStackTrace(r12);
        goto L_0x0010;
    L_0x0061:
        r13 = r9.token();
        r14 = 12;
        if (r13 == r14) goto L_0x001e;
    L_0x0069:
        r13 = new com.alibaba.fastjson.JSONException;
        r14 = "syntax error";
        r13.<init>(r14);
        throw r13;
    L_0x0072:
        r13 = r9.token();
        r14 = 16;
        if (r13 != r14) goto L_0x0082;
    L_0x007a:
        r13 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;
        r13 = r9.isEnabled(r13);
        if (r13 != 0) goto L_0x003c;
    L_0x0082:
        r13 = 4;
        r9.nextTokenWithColon(r13);
        r13 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;
        r13 = r13.equals(r8);
        if (r13 == 0) goto L_0x00b9;
    L_0x008e:
        r13 = r9.token();
        r14 = 4;
        if (r13 != r14) goto L_0x00b0;
    L_0x0095:
        r7 = r9.stringVal();
        r6 = com.alibaba.fastjson.util.TypeUtils.loadClass(r7);
        r13 = 16;
        r9.nextToken(r13);
    L_0x00a2:
        r13 = r9.token();
        r14 = 13;
        if (r13 != r14) goto L_0x003c;
    L_0x00aa:
        r13 = 16;
        r9.nextToken(r13);
        goto L_0x0053;
    L_0x00b0:
        r13 = new com.alibaba.fastjson.JSONException;
        r14 = "syntax error";
        r13.<init>(r14);
        throw r13;
    L_0x00b9:
        r13 = "message";
        r13 = r13.equals(r8);
        if (r13 == 0) goto L_0x00e4;
    L_0x00c2:
        r13 = r9.token();
        r14 = 8;
        if (r13 != r14) goto L_0x00cf;
    L_0x00ca:
        r10 = 0;
    L_0x00cb:
        r9.nextToken();
        goto L_0x00a2;
    L_0x00cf:
        r13 = r9.token();
        r14 = 4;
        if (r13 != r14) goto L_0x00db;
    L_0x00d6:
        r10 = r9.stringVal();
        goto L_0x00cb;
    L_0x00db:
        r13 = new com.alibaba.fastjson.JSONException;
        r14 = "syntax error";
        r13.<init>(r14);
        throw r13;
    L_0x00e4:
        r13 = "cause";
        r13 = r13.equals(r8);
        if (r13 == 0) goto L_0x00fa;
    L_0x00ed:
        r13 = 0;
        r14 = "cause";
        r0 = r16;
        r1 = r15.deserialze(r0, r13, r14);
        r1 = (java.lang.Throwable) r1;
        goto L_0x00a2;
    L_0x00fa:
        r13 = "stackTrace";
        r13 = r13.equals(r8);
        if (r13 == 0) goto L_0x010e;
    L_0x0103:
        r13 = java.lang.StackTraceElement[].class;
        r0 = r16;
        r12 = r0.parseObject(r13);
        r12 = (java.lang.StackTraceElement[]) r12;
        goto L_0x00a2;
    L_0x010e:
        r13 = r16.parse();
        r11.put(r8, r13);
        goto L_0x00a2;
    L_0x0116:
        r4 = r15.createException(r10, r1, r6);	 Catch:{ Exception -> 0x0124 }
        if (r4 != 0) goto L_0x005b;
    L_0x011c:
        r5 = new java.lang.Exception;	 Catch:{ Exception -> 0x0124 }
        r5.<init>(r10, r1);	 Catch:{ Exception -> 0x0124 }
        r4 = r5;
        goto L_0x005b;
    L_0x0124:
        r3 = move-exception;
        r13 = new com.alibaba.fastjson.JSONException;
        r14 = "create instance error";
        r13.<init>(r14, r3);
        throw r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):T");
    }

    private Throwable createException(String message, Throwable cause, Class<?> exClass) throws Exception {
        Constructor<?> defaultConstructor = null;
        Constructor<?> messageConstructor = null;
        Constructor<?> causeConstructor = null;
        for (Constructor<?> constructor : exClass.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
            } else if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == String.class) {
                messageConstructor = constructor;
            } else if (constructor.getParameterTypes().length == 2 && constructor.getParameterTypes()[0] == String.class && constructor.getParameterTypes()[1] == Throwable.class) {
                causeConstructor = constructor;
            }
        }
        if (causeConstructor != null) {
            return (Throwable) causeConstructor.newInstance(new Object[]{message, cause});
        } else if (messageConstructor != null) {
            return (Throwable) messageConstructor.newInstance(new Object[]{message});
        } else if (defaultConstructor != null) {
            return (Throwable) defaultConstructor.newInstance(new Object[0]);
        } else {
            return null;
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
