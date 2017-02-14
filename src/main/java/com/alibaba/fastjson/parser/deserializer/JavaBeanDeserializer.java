package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.FilterUtils;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JavaBeanDeserializer implements ObjectDeserializer {
    private DeserializeBeanInfo beanInfo;
    private final Class<?> clazz;
    private final Map<String, FieldDeserializer> feildDeserializerMap;
    private final List<FieldDeserializer> fieldDeserializers;
    private final List<FieldDeserializer> sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz) {
        this(config, clazz, clazz);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type) {
        this.feildDeserializerMap = new IdentityHashMap();
        this.fieldDeserializers = new ArrayList();
        this.sortedFieldDeserializers = new ArrayList();
        this.clazz = clazz;
        this.beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);
        for (FieldInfo fieldInfo : this.beanInfo.getFieldList()) {
            addFieldDeserializer(config, clazz, fieldInfo);
        }
        for (FieldInfo fieldInfo2 : this.beanInfo.getSortedFieldList()) {
            this.sortedFieldDeserializers.add((FieldDeserializer) this.feildDeserializerMap.get(fieldInfo2.getName().intern()));
        }
    }

    public Map<String, FieldDeserializer> getFieldDeserializerMap() {
        return this.feildDeserializerMap;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    private void addFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        String interName = fieldInfo.getName().intern();
        FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);
        this.feildDeserializerMap.put(interName, fieldDeserializer);
        this.fieldDeserializers.add(fieldDeserializer);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public Object createInstance(DefaultJSONParser parser, Type type) {
        if ((type instanceof Class) && this.clazz.isInterface()) {
            Class<?> clazz = (Class) type;
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new JSONObject());
        } else if (this.beanInfo.getDefaultConstructor() == null) {
            return null;
        } else {
            try {
                Object object;
                Constructor<?> constructor = this.beanInfo.getDefaultConstructor();
                if (constructor.getParameterTypes().length == 0) {
                    object = constructor.newInstance(new Object[0]);
                } else {
                    object = constructor.newInstance(new Object[]{parser.getContext().getObject()});
                }
                if (parser.isEnabled(Feature.InitStringFieldAsEmpty)) {
                    for (FieldInfo fieldInfo : this.beanInfo.getFieldList()) {
                        if (fieldInfo.getFieldClass() == String.class) {
                            try {
                                fieldInfo.set(object, "");
                            } catch (Exception e) {
                                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                            }
                        }
                    }
                }
                return object;
            } catch (Exception e2) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e2);
            }
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null);
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() != 14) {
            throw new JSONException("error");
        }
        object = createInstance(parser, type);
        int size = this.sortedFieldDeserializers.size();
        int i = 0;
        while (i < size) {
            char seperator = i == size + -1 ? ']' : ',';
            FieldDeserializer fieldDeser = (FieldDeserializer) this.sortedFieldDeserializers.get(i);
            Class<?> fieldClass = fieldDeser.getFieldClass();
            if (fieldClass == Integer.TYPE) {
                fieldDeser.setValue(object, lexer.scanInt(seperator));
            } else if (fieldClass == String.class) {
                fieldDeser.setValue(object, lexer.scanString(seperator));
            } else if (fieldClass == Long.TYPE) {
                fieldDeser.setValue(object, lexer.scanLong(seperator));
            } else if (fieldClass.isEnum()) {
                fieldDeser.setValue(object, lexer.scanEnum(fieldClass, parser.getSymbolTable(), seperator));
            } else {
                lexer.nextToken(14);
                fieldDeser.setValue(object, parser.parseObject(fieldDeser.getFieldType()));
                if (seperator == ']') {
                    if (lexer.token() != 15) {
                        throw new JSONException("syntax error");
                    }
                    lexer.nextToken(16);
                } else if (seperator == ',' && lexer.token() != 16) {
                    throw new JSONException("syntax error");
                }
            }
            i++;
        }
        lexer.nextToken(16);
        return object;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r28, java.lang.reflect.Type r29, java.lang.Object r30, java.lang.Object r31) {
        /*
        r27 = this;
        r18 = r28.getLexer();
        r3 = r18.token();
        r4 = 8;
        if (r3 != r4) goto L_0x0015;
    L_0x000c:
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);
        r3 = 0;
    L_0x0014:
        return r3;
    L_0x0015:
        r11 = r28.getContext();
        if (r31 == 0) goto L_0x0021;
    L_0x001b:
        if (r11 == 0) goto L_0x0021;
    L_0x001d:
        r11 = r11.getParentContext();
    L_0x0021:
        r10 = 0;
        r8 = 0;
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 13;
        if (r3 != r4) goto L_0x0047;
    L_0x002b:
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x00b9 }
        if (r31 != 0) goto L_0x0038;
    L_0x0034:
        r31 = r27.createInstance(r28, r29);	 Catch:{ all -> 0x00b9 }
    L_0x0038:
        if (r10 == 0) goto L_0x003f;
    L_0x003a:
        r0 = r31;
        r10.setObject(r0);
    L_0x003f:
        r0 = r28;
        r0.setContext(r11);
        r3 = r31;
        goto L_0x0014;
    L_0x0047:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 14;
        if (r3 != r4) goto L_0x006a;
    L_0x004f:
        r0 = r27;
        r1 = r18;
        r3 = r0.isSupportArrayToBean(r1);	 Catch:{ all -> 0x00b9 }
        if (r3 == 0) goto L_0x006a;
    L_0x0059:
        r3 = r27.deserialzeArrayMapping(r28, r29, r30, r31);	 Catch:{ all -> 0x00b9 }
        if (r10 == 0) goto L_0x0064;
    L_0x005f:
        r0 = r31;
        r10.setObject(r0);
    L_0x0064:
        r0 = r28;
        r0.setContext(r11);
        goto L_0x0014;
    L_0x006a:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 12;
        if (r3 == r4) goto L_0x00c7;
    L_0x0072:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 16;
        if (r3 == r4) goto L_0x00c7;
    L_0x007a:
        r3 = new java.lang.StringBuffer;	 Catch:{ all -> 0x00b9 }
        r3.<init>();	 Catch:{ all -> 0x00b9 }
        r4 = "syntax error, expect {, actual ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00b9 }
        r4 = r18.tokenName();	 Catch:{ all -> 0x00b9 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x00b9 }
        r4 = ", pos ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00b9 }
        r4 = r18.pos();	 Catch:{ all -> 0x00b9 }
        r9 = r3.append(r4);	 Catch:{ all -> 0x00b9 }
        r0 = r30;
        r3 = r0 instanceof java.lang.String;	 Catch:{ all -> 0x00b9 }
        if (r3 == 0) goto L_0x00af;
    L_0x00a3:
        r3 = ", fieldName ";
        r3 = r9.append(r3);	 Catch:{ all -> 0x00b9 }
        r0 = r30;
        r3.append(r0);	 Catch:{ all -> 0x00b9 }
    L_0x00af:
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00b9 }
        r4 = r9.toString();	 Catch:{ all -> 0x00b9 }
        r3.<init>(r4);	 Catch:{ all -> 0x00b9 }
        throw r3;	 Catch:{ all -> 0x00b9 }
    L_0x00b9:
        r3 = move-exception;
    L_0x00ba:
        if (r10 == 0) goto L_0x00c1;
    L_0x00bc:
        r0 = r31;
        r10.setObject(r0);
    L_0x00c1:
        r0 = r28;
        r0.setContext(r11);
        throw r3;
    L_0x00c7:
        r3 = r28.getResolveStatus();	 Catch:{ all -> 0x00b9 }
        r4 = 2;
        if (r3 != r4) goto L_0x00d4;
    L_0x00ce:
        r3 = 0;
        r0 = r28;
        r0.setResolveStatus(r3);	 Catch:{ all -> 0x00b9 }
    L_0x00d4:
        r16 = r8;
    L_0x00d6:
        r3 = r28.getSymbolTable();	 Catch:{ all -> 0x0166 }
        r0 = r18;
        r5 = r0.scanSymbol(r3);	 Catch:{ all -> 0x0166 }
        if (r5 != 0) goto L_0x0129;
    L_0x00e2:
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 13;
        if (r3 != r4) goto L_0x0117;
    L_0x00ea:
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x0166 }
        r8 = r16;
    L_0x00f3:
        if (r31 != 0) goto L_0x036f;
    L_0x00f5:
        if (r8 != 0) goto L_0x0328;
    L_0x00f7:
        r31 = r27.createInstance(r28, r29);	 Catch:{ all -> 0x00b9 }
        if (r10 != 0) goto L_0x0107;
    L_0x00fd:
        r0 = r28;
        r1 = r31;
        r2 = r30;
        r10 = r0.setContext(r11, r1, r2);	 Catch:{ all -> 0x00b9 }
    L_0x0107:
        if (r10 == 0) goto L_0x010e;
    L_0x0109:
        r0 = r31;
        r10.setObject(r0);
    L_0x010e:
        r0 = r28;
        r0.setContext(r11);
        r3 = r31;
        goto L_0x0014;
    L_0x0117:
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 16;
        if (r3 != r4) goto L_0x0129;
    L_0x011f:
        r3 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r3 = r0.isEnabled(r3);	 Catch:{ all -> 0x0166 }
        if (r3 != 0) goto L_0x00d6;
    L_0x0129:
        r3 = "$ref";
        if (r3 != r5) goto L_0x0229;
    L_0x012e:
        r3 = 4;
        r0 = r18;
        r0.nextTokenWithColon(r3);	 Catch:{ all -> 0x0166 }
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 4;
        if (r3 != r4) goto L_0x01e7;
    L_0x013b:
        r22 = r18.stringVal();	 Catch:{ all -> 0x0166 }
        r3 = "@";
        r0 = r22;
        r3 = r3.equals(r0);	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x016b;
    L_0x014a:
        r31 = r11.getObject();	 Catch:{ all -> 0x0166 }
    L_0x014e:
        r3 = 13;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x0166 }
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 13;
        if (r3 == r4) goto L_0x0209;
    L_0x015d:
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0166 }
        r4 = "illegal ref";
        r3.<init>(r4);	 Catch:{ all -> 0x0166 }
        throw r3;	 Catch:{ all -> 0x0166 }
    L_0x0166:
        r3 = move-exception;
        r8 = r16;
        goto L_0x00ba;
    L_0x016b:
        r3 = "..";
        r0 = r22;
        r3 = r3.equals(r0);	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x019a;
    L_0x0176:
        r21 = r11.getParentContext();	 Catch:{ all -> 0x0166 }
        r3 = r21.getObject();	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x0185;
    L_0x0180:
        r31 = r21.getObject();	 Catch:{ all -> 0x0166 }
        goto L_0x014e;
    L_0x0185:
        r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x0166 }
        r0 = r21;
        r1 = r22;
        r3.<init>(r0, r1);	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r0.addResolveTask(r3);	 Catch:{ all -> 0x0166 }
        r3 = 1;
        r0 = r28;
        r0.setResolveStatus(r3);	 Catch:{ all -> 0x0166 }
        goto L_0x014e;
    L_0x019a:
        r3 = "$";
        r0 = r22;
        r3 = r3.equals(r0);	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x01d3;
    L_0x01a5:
        r23 = r11;
    L_0x01a7:
        r3 = r23.getParentContext();	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x01b2;
    L_0x01ad:
        r23 = r23.getParentContext();	 Catch:{ all -> 0x0166 }
        goto L_0x01a7;
    L_0x01b2:
        r3 = r23.getObject();	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x01bd;
    L_0x01b8:
        r31 = r23.getObject();	 Catch:{ all -> 0x0166 }
        goto L_0x014e;
    L_0x01bd:
        r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x0166 }
        r0 = r23;
        r1 = r22;
        r3.<init>(r0, r1);	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r0.addResolveTask(r3);	 Catch:{ all -> 0x0166 }
        r3 = 1;
        r0 = r28;
        r0.setResolveStatus(r3);	 Catch:{ all -> 0x0166 }
        goto L_0x014e;
    L_0x01d3:
        r3 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x0166 }
        r0 = r22;
        r3.<init>(r11, r0);	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r0.addResolveTask(r3);	 Catch:{ all -> 0x0166 }
        r3 = 1;
        r0 = r28;
        r0.setResolveStatus(r3);	 Catch:{ all -> 0x0166 }
        goto L_0x014e;
    L_0x01e7:
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0166 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0166 }
        r4.<init>();	 Catch:{ all -> 0x0166 }
        r6 = "illegal ref, ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x0166 }
        r6 = r18.token();	 Catch:{ all -> 0x0166 }
        r6 = com.alibaba.fastjson.parser.JSONToken.name(r6);	 Catch:{ all -> 0x0166 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x0166 }
        r4 = r4.toString();	 Catch:{ all -> 0x0166 }
        r3.<init>(r4);	 Catch:{ all -> 0x0166 }
        throw r3;	 Catch:{ all -> 0x0166 }
    L_0x0209:
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r1 = r31;
        r2 = r30;
        r0.setContext(r11, r1, r2);	 Catch:{ all -> 0x0166 }
        if (r10 == 0) goto L_0x0220;
    L_0x021b:
        r0 = r31;
        r10.setObject(r0);
    L_0x0220:
        r0 = r28;
        r0.setContext(r11);
        r3 = r31;
        goto L_0x0014;
    L_0x0229:
        r3 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x0166 }
        if (r3 != r5) goto L_0x029a;
    L_0x022d:
        r3 = 4;
        r0 = r18;
        r0.nextTokenWithColon(r3);	 Catch:{ all -> 0x0166 }
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 4;
        if (r3 != r4) goto L_0x0291;
    L_0x023a:
        r25 = r18.stringVal();	 Catch:{ all -> 0x0166 }
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x0166 }
        r0 = r29;
        r3 = r0 instanceof java.lang.Class;	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x026b;
    L_0x024b:
        r0 = r29;
        r0 = (java.lang.Class) r0;	 Catch:{ all -> 0x0166 }
        r3 = r0;
        r3 = r3.getName();	 Catch:{ all -> 0x0166 }
        r0 = r25;
        r3 = r0.equals(r3);	 Catch:{ all -> 0x0166 }
        if (r3 == 0) goto L_0x026b;
    L_0x025c:
        r3 = r18.token();	 Catch:{ all -> 0x0166 }
        r4 = 13;
        if (r3 != r4) goto L_0x00d6;
    L_0x0264:
        r18.nextToken();	 Catch:{ all -> 0x0166 }
        r8 = r16;
        goto L_0x00f3;
    L_0x026b:
        r26 = com.alibaba.fastjson.util.TypeUtils.loadClass(r25);	 Catch:{ all -> 0x0166 }
        r3 = r28.getConfig();	 Catch:{ all -> 0x0166 }
        r0 = r26;
        r12 = r3.getDeserializer(r0);	 Catch:{ all -> 0x0166 }
        r0 = r28;
        r1 = r26;
        r2 = r30;
        r3 = r12.deserialze(r0, r1, r2);	 Catch:{ all -> 0x0166 }
        if (r10 == 0) goto L_0x028a;
    L_0x0285:
        r0 = r31;
        r10.setObject(r0);
    L_0x028a:
        r0 = r28;
        r0.setContext(r11);
        goto L_0x0014;
    L_0x0291:
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0166 }
        r4 = "syntax error";
        r3.<init>(r4);	 Catch:{ all -> 0x0166 }
        throw r3;	 Catch:{ all -> 0x0166 }
    L_0x029a:
        if (r31 != 0) goto L_0x03eb;
    L_0x029c:
        if (r16 != 0) goto L_0x03eb;
    L_0x029e:
        r31 = r27.createInstance(r28, r29);	 Catch:{ all -> 0x0166 }
        if (r31 != 0) goto L_0x03e7;
    L_0x02a4:
        r8 = new java.util.HashMap;	 Catch:{ all -> 0x0166 }
        r0 = r27;
        r3 = r0.fieldDeserializers;	 Catch:{ all -> 0x0166 }
        r3 = r3.size();	 Catch:{ all -> 0x0166 }
        r8.<init>(r3);	 Catch:{ all -> 0x0166 }
    L_0x02b1:
        r0 = r28;
        r1 = r31;
        r2 = r30;
        r10 = r0.setContext(r11, r1, r2);	 Catch:{ all -> 0x00b9 }
    L_0x02bb:
        r3 = r27;
        r4 = r28;
        r6 = r31;
        r7 = r29;
        r19 = r3.parseField(r4, r5, r6, r7, r8);	 Catch:{ all -> 0x00b9 }
        if (r19 != 0) goto L_0x02d6;
    L_0x02c9:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 13;
        if (r3 != r4) goto L_0x00d4;
    L_0x02d1:
        r18.nextToken();	 Catch:{ all -> 0x00b9 }
        goto L_0x00f3;
    L_0x02d6:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 16;
        if (r3 != r4) goto L_0x02e2;
    L_0x02de:
        r16 = r8;
        goto L_0x00d6;
    L_0x02e2:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 13;
        if (r3 != r4) goto L_0x02f3;
    L_0x02ea:
        r3 = 16;
        r0 = r18;
        r0.nextToken(r3);	 Catch:{ all -> 0x00b9 }
        goto L_0x00f3;
    L_0x02f3:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 18;
        if (r3 == r4) goto L_0x0302;
    L_0x02fb:
        r3 = r18.token();	 Catch:{ all -> 0x00b9 }
        r4 = 1;
        if (r3 != r4) goto L_0x0324;
    L_0x0302:
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00b9 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b9 }
        r4.<init>();	 Catch:{ all -> 0x00b9 }
        r6 = "syntax error, unexpect token ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r6 = r18.token();	 Catch:{ all -> 0x00b9 }
        r6 = com.alibaba.fastjson.parser.JSONToken.name(r6);	 Catch:{ all -> 0x00b9 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b9 }
        r3.<init>(r4);	 Catch:{ all -> 0x00b9 }
        throw r3;	 Catch:{ all -> 0x00b9 }
    L_0x0324:
        r16 = r8;
        goto L_0x00d6;
    L_0x0328:
        r0 = r27;
        r3 = r0.beanInfo;	 Catch:{ all -> 0x00b9 }
        r15 = r3.getFieldList();	 Catch:{ all -> 0x00b9 }
        r24 = r15.size();	 Catch:{ all -> 0x00b9 }
        r0 = r24;
        r0 = new java.lang.Object[r0];	 Catch:{ all -> 0x00b9 }
        r20 = r0;
        r17 = 0;
    L_0x033c:
        r0 = r17;
        r1 = r24;
        if (r0 >= r1) goto L_0x0357;
    L_0x0342:
        r0 = r17;
        r14 = r15.get(r0);	 Catch:{ all -> 0x00b9 }
        r14 = (com.alibaba.fastjson.util.FieldInfo) r14;	 Catch:{ all -> 0x00b9 }
        r3 = r14.getName();	 Catch:{ all -> 0x00b9 }
        r3 = r8.get(r3);	 Catch:{ all -> 0x00b9 }
        r20[r17] = r3;	 Catch:{ all -> 0x00b9 }
        r17 = r17 + 1;
        goto L_0x033c;
    L_0x0357:
        r0 = r27;
        r3 = r0.beanInfo;	 Catch:{ all -> 0x00b9 }
        r3 = r3.getCreatorConstructor();	 Catch:{ all -> 0x00b9 }
        if (r3 == 0) goto L_0x03a6;
    L_0x0361:
        r0 = r27;
        r3 = r0.beanInfo;	 Catch:{ Exception -> 0x037f }
        r3 = r3.getCreatorConstructor();	 Catch:{ Exception -> 0x037f }
        r0 = r20;
        r31 = r3.newInstance(r0);	 Catch:{ Exception -> 0x037f }
    L_0x036f:
        if (r10 == 0) goto L_0x0376;
    L_0x0371:
        r0 = r31;
        r10.setObject(r0);
    L_0x0376:
        r0 = r28;
        r0.setContext(r11);
        r3 = r31;
        goto L_0x0014;
    L_0x037f:
        r13 = move-exception;
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00b9 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b9 }
        r4.<init>();	 Catch:{ all -> 0x00b9 }
        r6 = "create instance error, ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r0 = r27;
        r6 = r0.beanInfo;	 Catch:{ all -> 0x00b9 }
        r6 = r6.getCreatorConstructor();	 Catch:{ all -> 0x00b9 }
        r6 = r6.toGenericString();	 Catch:{ all -> 0x00b9 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b9 }
        r3.<init>(r4, r13);	 Catch:{ all -> 0x00b9 }
        throw r3;	 Catch:{ all -> 0x00b9 }
    L_0x03a6:
        r0 = r27;
        r3 = r0.beanInfo;	 Catch:{ all -> 0x00b9 }
        r3 = r3.getFactoryMethod();	 Catch:{ all -> 0x00b9 }
        if (r3 == 0) goto L_0x036f;
    L_0x03b0:
        r0 = r27;
        r3 = r0.beanInfo;	 Catch:{ Exception -> 0x03c0 }
        r3 = r3.getFactoryMethod();	 Catch:{ Exception -> 0x03c0 }
        r4 = 0;
        r0 = r20;
        r31 = r3.invoke(r4, r0);	 Catch:{ Exception -> 0x03c0 }
        goto L_0x036f;
    L_0x03c0:
        r13 = move-exception;
        r3 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00b9 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b9 }
        r4.<init>();	 Catch:{ all -> 0x00b9 }
        r6 = "create factory method error, ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r0 = r27;
        r6 = r0.beanInfo;	 Catch:{ all -> 0x00b9 }
        r6 = r6.getFactoryMethod();	 Catch:{ all -> 0x00b9 }
        r6 = r6.toString();	 Catch:{ all -> 0x00b9 }
        r4 = r4.append(r6);	 Catch:{ all -> 0x00b9 }
        r4 = r4.toString();	 Catch:{ all -> 0x00b9 }
        r3.<init>(r4, r13);	 Catch:{ all -> 0x00b9 }
        throw r3;	 Catch:{ all -> 0x00b9 }
    L_0x03e7:
        r8 = r16;
        goto L_0x02b1;
    L_0x03eb:
        r8 = r16;
        goto L_0x02bb;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):T");
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.getLexer();
        FieldDeserializer fieldDeserializer = (FieldDeserializer) this.feildDeserializerMap.get(key);
        if (fieldDeserializer == null) {
            for (Entry<String, FieldDeserializer> entry : this.feildDeserializerMap.entrySet()) {
                if (((String) entry.getKey()).equalsIgnoreCase(key)) {
                    fieldDeserializer = (FieldDeserializer) entry.getValue();
                    break;
                }
            }
        }
        if (fieldDeserializer == null) {
            parseExtra(parser, object, key);
            return false;
        }
        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
        fieldDeserializer.parseField(parser, object, objectType, fieldValues);
        return true;
    }

    void parseExtra(DefaultJSONParser parser, Object object, String key) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.isEnabled(Feature.IgnoreNotMatch)) {
            Object value;
            lexer.nextTokenWithColon();
            Type type = FilterUtils.getExtratype(parser, object, key);
            if (type == null) {
                value = parser.parse();
            } else {
                value = parser.parseObject(type);
            }
            FilterUtils.processExtra(parser, object, key, value);
            return;
        }
        throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + key);
    }

    public int getFastMatchToken() {
        return 12;
    }

    public List<FieldDeserializer> getSortedFieldDeserializers() {
        return this.sortedFieldDeserializers;
    }

    public final boolean isSupportArrayToBean(JSONLexer lexer) {
        return Feature.isEnabled(this.beanInfo.getParserFeatures(), Feature.SupportArrayToBean) || lexer.isEnabled(Feature.SupportArrayToBean);
    }
}
