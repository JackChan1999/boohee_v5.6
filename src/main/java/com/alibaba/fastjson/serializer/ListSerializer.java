package com.alibaba.fastjson.serializer;

public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    public final void write(com.alibaba.fastjson.serializer.JSONSerializer r20, java.lang.Object r21, java.lang.Object r22, java.lang.reflect.Type r23) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r19 = this;
        r17 = com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName;
        r0 = r20;
        r1 = r17;
        r16 = r0.isEnabled(r1);
        r12 = r20.getWriter();
        r6 = 0;
        if (r16 == 0) goto L_0x0025;
    L_0x0011:
        r0 = r23;
        r0 = r0 instanceof java.lang.reflect.ParameterizedType;
        r17 = r0;
        if (r17 == 0) goto L_0x0025;
    L_0x0019:
        r13 = r23;
        r13 = (java.lang.reflect.ParameterizedType) r13;
        r17 = r13.getActualTypeArguments();
        r18 = 0;
        r6 = r17[r18];
    L_0x0025:
        if (r21 != 0) goto L_0x003e;
    L_0x0027:
        r17 = com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
        r0 = r17;
        r17 = r12.isEnabled(r0);
        if (r17 == 0) goto L_0x003a;
    L_0x0031:
        r17 = "[]";
        r0 = r17;
        r12.write(r0);
    L_0x0039:
        return;
    L_0x003a:
        r12.writeNull();
        goto L_0x0039;
    L_0x003e:
        r11 = r21;
        r11 = (java.util.List) r11;
        r17 = r11.size();
        if (r17 != 0) goto L_0x0051;
    L_0x0048:
        r17 = "[]";
        r0 = r17;
        r12.append(r0);
        goto L_0x0039;
    L_0x0051:
        r5 = r20.getContext();
        r17 = 0;
        r0 = r20;
        r1 = r21;
        r2 = r22;
        r3 = r17;
        r0.setContext(r5, r1, r2, r3);
        r10 = 0;
        r17 = com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;	 Catch:{ all -> 0x00ce }
        r0 = r17;	 Catch:{ all -> 0x00ce }
        r17 = r12.isEnabled(r0);	 Catch:{ all -> 0x00ce }
        if (r17 == 0) goto L_0x00f1;	 Catch:{ all -> 0x00ce }
    L_0x006d:
        r17 = 91;	 Catch:{ all -> 0x00ce }
        r0 = r17;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
        r20.incrementIndent();	 Catch:{ all -> 0x00ce }
        r7 = 0;	 Catch:{ all -> 0x00ce }
        r17 = r11.iterator();	 Catch:{ all -> 0x00ce }
    L_0x007c:
        r18 = r17.hasNext();	 Catch:{ all -> 0x00ce }
        if (r18 == 0) goto L_0x00dd;	 Catch:{ all -> 0x00ce }
    L_0x0082:
        r8 = r17.next();	 Catch:{ all -> 0x00ce }
        if (r7 == 0) goto L_0x008f;	 Catch:{ all -> 0x00ce }
    L_0x0088:
        r18 = 44;	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
    L_0x008f:
        r20.println();	 Catch:{ all -> 0x00ce }
        if (r8 == 0) goto L_0x00d5;	 Catch:{ all -> 0x00ce }
    L_0x0094:
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r18 = r0.containsReference(r8);	 Catch:{ all -> 0x00ce }
        if (r18 == 0) goto L_0x00a4;	 Catch:{ all -> 0x00ce }
    L_0x009c:
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r0.writeReference(r8);	 Catch:{ all -> 0x00ce }
    L_0x00a1:
        r7 = r7 + 1;	 Catch:{ all -> 0x00ce }
        goto L_0x007c;	 Catch:{ all -> 0x00ce }
    L_0x00a4:
        r18 = r8.getClass();	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r1 = r18;	 Catch:{ all -> 0x00ce }
        r10 = r0.getObjectWriter(r1);	 Catch:{ all -> 0x00ce }
        r9 = new com.alibaba.fastjson.serializer.SerialContext;	 Catch:{ all -> 0x00ce }
        r18 = 0;	 Catch:{ all -> 0x00ce }
        r0 = r21;	 Catch:{ all -> 0x00ce }
        r1 = r22;	 Catch:{ all -> 0x00ce }
        r2 = r18;	 Catch:{ all -> 0x00ce }
        r9.<init>(r5, r0, r1, r2);	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r0.setContext(r9);	 Catch:{ all -> 0x00ce }
        r18 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r1 = r18;	 Catch:{ all -> 0x00ce }
        r10.write(r0, r8, r1, r6);	 Catch:{ all -> 0x00ce }
        goto L_0x00a1;
    L_0x00ce:
        r17 = move-exception;
        r0 = r20;
        r0.setContext(r5);
        throw r17;
    L_0x00d5:
        r18 = r20.getWriter();	 Catch:{ all -> 0x00ce }
        r18.writeNull();	 Catch:{ all -> 0x00ce }
        goto L_0x00a1;	 Catch:{ all -> 0x00ce }
    L_0x00dd:
        r20.decrementIdent();	 Catch:{ all -> 0x00ce }
        r20.println();	 Catch:{ all -> 0x00ce }
        r17 = 93;	 Catch:{ all -> 0x00ce }
        r0 = r17;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
        r0 = r20;
        r0.setContext(r5);
        goto L_0x0039;
    L_0x00f1:
        r17 = 91;
        r0 = r17;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
        r7 = 0;	 Catch:{ all -> 0x00ce }
        r17 = r11.iterator();	 Catch:{ all -> 0x00ce }
    L_0x00fd:
        r18 = r17.hasNext();	 Catch:{ all -> 0x00ce }
        if (r18 == 0) goto L_0x0185;	 Catch:{ all -> 0x00ce }
    L_0x0103:
        r8 = r17.next();	 Catch:{ all -> 0x00ce }
        if (r7 == 0) goto L_0x0110;	 Catch:{ all -> 0x00ce }
    L_0x0109:
        r18 = 44;	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
    L_0x0110:
        if (r8 != 0) goto L_0x011d;	 Catch:{ all -> 0x00ce }
    L_0x0112:
        r18 = "null";	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
    L_0x011a:
        r7 = r7 + 1;	 Catch:{ all -> 0x00ce }
        goto L_0x00fd;	 Catch:{ all -> 0x00ce }
    L_0x011d:
        r4 = r8.getClass();	 Catch:{ all -> 0x00ce }
        r18 = java.lang.Integer.class;	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        if (r4 != r0) goto L_0x0133;	 Catch:{ all -> 0x00ce }
    L_0x0127:
        r8 = (java.lang.Integer) r8;	 Catch:{ all -> 0x00ce }
        r18 = r8.intValue();	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        r12.writeInt(r0);	 Catch:{ all -> 0x00ce }
        goto L_0x011a;	 Catch:{ all -> 0x00ce }
    L_0x0133:
        r18 = java.lang.Long.class;	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        if (r4 != r0) goto L_0x014d;	 Catch:{ all -> 0x00ce }
    L_0x0139:
        r8 = (java.lang.Long) r8;	 Catch:{ all -> 0x00ce }
        r14 = r8.longValue();	 Catch:{ all -> 0x00ce }
        if (r16 == 0) goto L_0x0149;	 Catch:{ all -> 0x00ce }
    L_0x0141:
        r18 = 76;	 Catch:{ all -> 0x00ce }
        r0 = r18;	 Catch:{ all -> 0x00ce }
        r12.writeLongAndChar(r14, r0);	 Catch:{ all -> 0x00ce }
        goto L_0x011a;	 Catch:{ all -> 0x00ce }
    L_0x0149:
        r12.writeLong(r14);	 Catch:{ all -> 0x00ce }
        goto L_0x011a;	 Catch:{ all -> 0x00ce }
    L_0x014d:
        r9 = new com.alibaba.fastjson.serializer.SerialContext;	 Catch:{ all -> 0x00ce }
        r18 = 0;	 Catch:{ all -> 0x00ce }
        r0 = r21;	 Catch:{ all -> 0x00ce }
        r1 = r22;	 Catch:{ all -> 0x00ce }
        r2 = r18;	 Catch:{ all -> 0x00ce }
        r9.<init>(r5, r0, r1, r2);	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r0.setContext(r9);	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r18 = r0.containsReference(r8);	 Catch:{ all -> 0x00ce }
        if (r18 == 0) goto L_0x016d;	 Catch:{ all -> 0x00ce }
    L_0x0167:
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r0.writeReference(r8);	 Catch:{ all -> 0x00ce }
        goto L_0x011a;	 Catch:{ all -> 0x00ce }
    L_0x016d:
        r18 = r8.getClass();	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r1 = r18;	 Catch:{ all -> 0x00ce }
        r10 = r0.getObjectWriter(r1);	 Catch:{ all -> 0x00ce }
        r18 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x00ce }
        r0 = r20;	 Catch:{ all -> 0x00ce }
        r1 = r18;	 Catch:{ all -> 0x00ce }
        r10.write(r0, r8, r1, r6);	 Catch:{ all -> 0x00ce }
        goto L_0x011a;	 Catch:{ all -> 0x00ce }
    L_0x0185:
        r17 = 93;	 Catch:{ all -> 0x00ce }
        r0 = r17;	 Catch:{ all -> 0x00ce }
        r12.append(r0);	 Catch:{ all -> 0x00ce }
        r0 = r20;
        r0.setContext(r5);
        goto L_0x0039;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ListSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type):void");
    }
}
