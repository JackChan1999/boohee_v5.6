package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDeserializer implements ObjectDeserializer {
    public static final MapDeserializer instance = new MapDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        Map<Object, Object> map = createMap(type);
        ParseContext context = parser.getContext();
        try {
            parser.setContext(context, map, fieldName);
            T deserialze = deserialze(parser, type, fieldName, map);
            return deserialze;
        } finally {
            parser.setContext(context);
        }
    }

    protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (!(type instanceof ParameterizedType)) {
            return parser.parseObject(map, fieldName);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class keyType = parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];
        if (String.class == keyType) {
            return parseMap(parser, map, valueType, fieldName);
        }
        return parseMap(parser, map, keyType, valueType, fieldName);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r12, java.util.Map<java.lang.String, java.lang.Object> r13, java.lang.reflect.Type r14, java.lang.Object r15) {
        /*
        r5 = r12.getLexer();
        r9 = r5.token();
        r10 = 12;
        if (r9 == r10) goto L_0x002a;
    L_0x000c:
        r9 = new com.alibaba.fastjson.JSONException;
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "syntax error, expect {, actual ";
        r10 = r10.append(r11);
        r11 = r5.token();
        r10 = r10.append(r11);
        r10 = r10.toString();
        r9.<init>(r10);
        throw r9;
    L_0x002a:
        r2 = r12.getContext();
    L_0x002e:
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        r9 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x0083 }
        r9 = r12.isEnabled(r9);	 Catch:{ all -> 0x0083 }
        if (r9 == 0) goto L_0x004c;
    L_0x003d:
        r9 = 44;
        if (r0 != r9) goto L_0x004c;
    L_0x0041:
        r5.next();	 Catch:{ all -> 0x0083 }
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        goto L_0x003d;
    L_0x004c:
        r9 = 34;
        if (r0 != r9) goto L_0x0088;
    L_0x0050:
        r9 = r12.getSymbolTable();	 Catch:{ all -> 0x0083 }
        r10 = 34;
        r4 = r5.scanSymbol(r9, r10);	 Catch:{ all -> 0x0083 }
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        r9 = 58;
        if (r0 == r9) goto L_0x0130;
    L_0x0065:
        r9 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0083 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0083 }
        r10.<init>();	 Catch:{ all -> 0x0083 }
        r11 = "expect ':' at ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r11 = r5.pos();	 Catch:{ all -> 0x0083 }
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r10 = r10.toString();	 Catch:{ all -> 0x0083 }
        r9.<init>(r10);	 Catch:{ all -> 0x0083 }
        throw r9;	 Catch:{ all -> 0x0083 }
    L_0x0083:
        r9 = move-exception;
        r12.setContext(r2);
        throw r9;
    L_0x0088:
        r9 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r0 != r9) goto L_0x009b;
    L_0x008c:
        r5.next();	 Catch:{ all -> 0x0083 }
        r5.resetStringPosition();	 Catch:{ all -> 0x0083 }
        r9 = 16;
        r5.nextToken(r9);	 Catch:{ all -> 0x0083 }
        r12.setContext(r2);
    L_0x009a:
        return r13;
    L_0x009b:
        r9 = 39;
        if (r0 != r9) goto L_0x00e3;
    L_0x009f:
        r9 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes;	 Catch:{ all -> 0x0083 }
        r9 = r12.isEnabled(r9);	 Catch:{ all -> 0x0083 }
        if (r9 != 0) goto L_0x00b0;
    L_0x00a7:
        r9 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0083 }
        r10 = "syntax error";
        r9.<init>(r10);	 Catch:{ all -> 0x0083 }
        throw r9;	 Catch:{ all -> 0x0083 }
    L_0x00b0:
        r9 = r12.getSymbolTable();	 Catch:{ all -> 0x0083 }
        r10 = 39;
        r4 = r5.scanSymbol(r9, r10);	 Catch:{ all -> 0x0083 }
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        r9 = 58;
        if (r0 == r9) goto L_0x0130;
    L_0x00c5:
        r9 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0083 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0083 }
        r10.<init>();	 Catch:{ all -> 0x0083 }
        r11 = "expect ':' at ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r11 = r5.pos();	 Catch:{ all -> 0x0083 }
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r10 = r10.toString();	 Catch:{ all -> 0x0083 }
        r9.<init>(r10);	 Catch:{ all -> 0x0083 }
        throw r9;	 Catch:{ all -> 0x0083 }
    L_0x00e3:
        r9 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames;	 Catch:{ all -> 0x0083 }
        r9 = r12.isEnabled(r9);	 Catch:{ all -> 0x0083 }
        if (r9 != 0) goto L_0x00f4;
    L_0x00eb:
        r9 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0083 }
        r10 = "syntax error";
        r9.<init>(r10);	 Catch:{ all -> 0x0083 }
        throw r9;	 Catch:{ all -> 0x0083 }
    L_0x00f4:
        r9 = r12.getSymbolTable();	 Catch:{ all -> 0x0083 }
        r4 = r5.scanSymbolUnQuoted(r9);	 Catch:{ all -> 0x0083 }
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        r9 = 58;
        if (r0 == r9) goto L_0x0130;
    L_0x0107:
        r9 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0083 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0083 }
        r10.<init>();	 Catch:{ all -> 0x0083 }
        r11 = "expect ':' at ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r11 = r5.pos();	 Catch:{ all -> 0x0083 }
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r11 = ", actual ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0083 }
        r10 = r10.append(r0);	 Catch:{ all -> 0x0083 }
        r10 = r10.toString();	 Catch:{ all -> 0x0083 }
        r9.<init>(r10);	 Catch:{ all -> 0x0083 }
        throw r9;	 Catch:{ all -> 0x0083 }
    L_0x0130:
        r5.next();	 Catch:{ all -> 0x0083 }
        r5.skipWhitespace();	 Catch:{ all -> 0x0083 }
        r0 = r5.getCurrent();	 Catch:{ all -> 0x0083 }
        r5.resetStringPosition();	 Catch:{ all -> 0x0083 }
        r9 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x0083 }
        if (r4 != r9) goto L_0x0192;
    L_0x0141:
        r9 = r12.getSymbolTable();	 Catch:{ all -> 0x0083 }
        r10 = 34;
        r7 = r5.scanSymbol(r9, r10);	 Catch:{ all -> 0x0083 }
        r1 = com.alibaba.fastjson.util.TypeUtils.loadClass(r7);	 Catch:{ all -> 0x0083 }
        r9 = r13.getClass();	 Catch:{ all -> 0x0083 }
        if (r1 != r9) goto L_0x016c;
    L_0x0155:
        r9 = 16;
        r5.nextToken(r9);	 Catch:{ all -> 0x0083 }
        r9 = r5.token();	 Catch:{ all -> 0x0083 }
        r10 = 13;
        if (r9 != r10) goto L_0x002e;
    L_0x0162:
        r9 = 16;
        r5.nextToken(r9);	 Catch:{ all -> 0x0083 }
        r12.setContext(r2);
        goto L_0x009a;
    L_0x016c:
        r9 = r12.getConfig();	 Catch:{ all -> 0x0083 }
        r3 = r9.getDeserializer(r1);	 Catch:{ all -> 0x0083 }
        r9 = 16;
        r5.nextToken(r9);	 Catch:{ all -> 0x0083 }
        r9 = 2;
        r12.setResolveStatus(r9);	 Catch:{ all -> 0x0083 }
        if (r2 == 0) goto L_0x0186;
    L_0x017f:
        r9 = r15 instanceof java.lang.Integer;	 Catch:{ all -> 0x0083 }
        if (r9 != 0) goto L_0x0186;
    L_0x0183:
        r12.popContext();	 Catch:{ all -> 0x0083 }
    L_0x0186:
        r9 = r3.deserialze(r12, r1, r15);	 Catch:{ all -> 0x0083 }
        r9 = (java.util.Map) r9;	 Catch:{ all -> 0x0083 }
        r12.setContext(r2);
        r13 = r9;
        goto L_0x009a;
    L_0x0192:
        r5.nextToken();	 Catch:{ all -> 0x0083 }
        r9 = r5.token();	 Catch:{ all -> 0x0083 }
        r10 = 8;
        if (r9 != r10) goto L_0x01bb;
    L_0x019d:
        r8 = 0;
        r5.nextToken();	 Catch:{ all -> 0x0083 }
    L_0x01a1:
        r13.put(r4, r8);	 Catch:{ all -> 0x0083 }
        r12.checkMapResolve(r13, r4);	 Catch:{ all -> 0x0083 }
        r12.setContext(r2, r8, r4);	 Catch:{ all -> 0x0083 }
        r6 = r5.token();	 Catch:{ all -> 0x0083 }
        r9 = 20;
        if (r6 == r9) goto L_0x01b6;
    L_0x01b2:
        r9 = 15;
        if (r6 != r9) goto L_0x01c0;
    L_0x01b6:
        r12.setContext(r2);
        goto L_0x009a;
    L_0x01bb:
        r8 = r12.parseObject(r14);	 Catch:{ all -> 0x0083 }
        goto L_0x01a1;
    L_0x01c0:
        r9 = 13;
        if (r6 != r9) goto L_0x002e;
    L_0x01c4:
        r5.nextToken();	 Catch:{ all -> 0x0083 }
        r12.setContext(r2);
        goto L_0x009a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 12 || lexer.token() == 16) {
            ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
            ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
            lexer.nextToken(keyDeserializer.getFastMatchToken());
            ParseContext context = parser.getContext();
            while (lexer.token() != 13) {
                try {
                    if (lexer.token() == 4 && lexer.isRef()) {
                        Map<Object, Object> object = null;
                        lexer.nextTokenWithColon(4);
                        if (lexer.token() == 4) {
                            String ref = lexer.stringVal();
                            if ("..".equals(ref)) {
                                object = context.getParentContext().getObject();
                            } else if ("$".equals(ref)) {
                                ParseContext rootContext = context;
                                while (rootContext.getParentContext() != null) {
                                    rootContext = rootContext.getParentContext();
                                }
                                object = rootContext.getObject();
                            } else {
                                parser.addResolveTask(new ResolveTask(context, ref));
                                parser.setResolveStatus(1);
                            }
                            lexer.nextToken(13);
                            if (lexer.token() != 13) {
                                throw new JSONException("illegal ref");
                            }
                            lexer.nextToken(16);
                            parser.setContext(context);
                            return object;
                        }
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }
                    if (map.size() == 0 && lexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal())) {
                        lexer.nextTokenWithColon(4);
                        lexer.nextToken(16);
                        if (lexer.token() == 13) {
                            lexer.nextToken();
                            return map;
                        }
                        lexer.nextToken(keyDeserializer.getFastMatchToken());
                    }
                    Object key = keyDeserializer.deserialze(parser, keyType, null);
                    if (lexer.token() != 17) {
                        throw new JSONException("syntax error, expect :, actual " + lexer.token());
                    }
                    lexer.nextToken(valueDeserializer.getFastMatchToken());
                    map.put(key, valueDeserializer.deserialze(parser, valueType, key));
                    if (lexer.token() == 16) {
                        lexer.nextToken(keyDeserializer.getFastMatchToken());
                    }
                } finally {
                    parser.setContext(context);
                }
            }
            lexer.nextToken(16);
            parser.setContext(context);
            return map;
        }
        throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
    }

    protected Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            return createMap(((ParameterizedType) type).getRawType());
        }
        Class<?> clazz = (Class) type;
        if (clazz.isInterface()) {
            throw new JSONException("unsupport type " + type);
        }
        try {
            return (Map) clazz.newInstance();
        } catch (Exception e) {
            throw new JSONException("unsupport type " + type, e);
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
