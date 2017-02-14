package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.CollectionResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ListResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.MapResolveFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class DefaultJSONParser extends AbstractJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected final Object input;
    protected final JSONLexer lexer;
    private int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    protected final SymbolTable symbolTable;

    public static class ResolveTask {
        private final ParseContext context;
        private FieldDeserializer fieldDeserializer;
        private ParseContext ownerContext;
        private final String referenceValue;

        public ResolveTask(ParseContext context, String referenceValue) {
            this.context = context;
            this.referenceValue = referenceValue;
        }

        public ParseContext getContext() {
            return this.context;
        }

        public String getReferenceValue() {
            return this.referenceValue;
        }

        public FieldDeserializer getFieldDeserializer() {
            return this.fieldDeserializer;
        }

        public void setFieldDeserializer(FieldDeserializer fieldDeserializer) {
            this.fieldDeserializer = fieldDeserializer;
        }

        public ParseContext getOwnerContext() {
            return this.ownerContext;
        }

        public void setOwnerContext(ParseContext ownerContext) {
            this.ownerContext = ownerContext;
        }
    }

    static {
        primitiveClasses.add(Boolean.TYPE);
        primitiveClasses.add(Byte.TYPE);
        primitiveClasses.add(Short.TYPE);
        primitiveClasses.add(Integer.TYPE);
        primitiveClasses.add(Long.TYPE);
        primitiveClasses.add(Float.TYPE);
        primitiveClasses.add(Double.TYPE);
        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);
        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern);
        }
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String input) {
        this(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String input, ParserConfig config) {
        this((Object) input, new JSONScanner(input, JSON.DEFAULT_PARSER_FEATURE), config);
    }

    public DefaultJSONParser(String input, ParserConfig config, int features) {
        this((Object) input, new JSONScanner(input, features), config);
    }

    public DefaultJSONParser(char[] input, int length, ParserConfig config, int features) {
        this((Object) input, new JSONScanner(input, length, features), config);
    }

    public DefaultJSONParser(JSONLexer lexer) {
        this(lexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer lexer, ParserConfig config) {
        this(null, lexer, config);
    }

    public DefaultJSONParser(Object input, JSONLexer lexer, ParserConfig config) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArray = new ParseContext[8];
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.lexer = lexer;
        this.input = input;
        this.config = config;
        this.symbolTable = config.getSymbolTable();
        lexer.nextToken(12);
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        if (this.input instanceof char[]) {
            return new String((char[]) this.input);
        }
        return this.input.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r32, java.lang.Object r33) {
        /*
        r31 = this;
        r0 = r31;
        r14 = r0.lexer;
        r28 = r14.token();
        r29 = 8;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0016;
    L_0x0010:
        r14.next();
        r32 = 0;
    L_0x0015:
        return r32;
    L_0x0016:
        r28 = r14.token();
        r29 = 12;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x004c;
    L_0x0022:
        r28 = r14.token();
        r29 = 16;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x004c;
    L_0x002e:
        r28 = new com.alibaba.fastjson.JSONException;
        r29 = new java.lang.StringBuilder;
        r29.<init>();
        r30 = "syntax error, expect {, actual ";
        r29 = r29.append(r30);
        r30 = r14.tokenName();
        r29 = r29.append(r30);
        r29 = r29.toString();
        r28.<init>(r29);
        throw r28;
    L_0x004c:
        r5 = r31.getContext();
        r22 = 0;
    L_0x0052:
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r28 = r0.isEnabled(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x0076;
    L_0x0065:
        r28 = 44;
        r0 = r28;
        if (r3 != r0) goto L_0x0076;
    L_0x006b:
        r14.next();	 Catch:{ all -> 0x00c5 }
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        goto L_0x0065;
    L_0x0076:
        r11 = 0;
        r28 = 34;
        r0 = r28;
        if (r3 != r0) goto L_0x00cc;
    L_0x007d:
        r0 = r31;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        r29 = 34;
        r0 = r28;
        r1 = r29;
        r13 = r14.scanSymbol(r0, r1);	 Catch:{ all -> 0x00c5 }
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = 58;
        r0 = r28;
        if (r3 == r0) goto L_0x01d0;
    L_0x009a:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "expect ':' at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = ", name ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r0 = r29;
        r29 = r0.append(r13);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x00c5:
        r28 = move-exception;
        r0 = r31;
        r0.setContext(r5);
        throw r28;
    L_0x00cc:
        r28 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r28;
        if (r3 != r0) goto L_0x00e2;
    L_0x00d2:
        r14.next();	 Catch:{ all -> 0x00c5 }
        r14.resetStringPosition();	 Catch:{ all -> 0x00c5 }
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x00e2:
        r28 = 39;
        r0 = r28;
        if (r3 != r0) goto L_0x0138;
    L_0x00e8:
        r28 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes;	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r28 = r0.isEnabled(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 != 0) goto L_0x00fd;
    L_0x00f4:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x00fd:
        r0 = r31;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        r29 = 39;
        r0 = r28;
        r1 = r29;
        r13 = r14.scanSymbol(r0, r1);	 Catch:{ all -> 0x00c5 }
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = 58;
        r0 = r28;
        if (r3 == r0) goto L_0x01d0;
    L_0x011a:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "expect ':' at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0138:
        r28 = 26;
        r0 = r28;
        if (r3 != r0) goto L_0x0147;
    L_0x013e:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0147:
        r28 = 44;
        r0 = r28;
        if (r3 != r0) goto L_0x0156;
    L_0x014d:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0156:
        r28 = 48;
        r0 = r28;
        if (r3 < r0) goto L_0x0162;
    L_0x015c:
        r28 = 57;
        r0 = r28;
        if (r3 <= r0) goto L_0x0168;
    L_0x0162:
        r28 = 45;
        r0 = r28;
        if (r3 != r0) goto L_0x01bc;
    L_0x0168:
        r14.resetStringPosition();	 Catch:{ all -> 0x00c5 }
        r14.scanNumber();	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 2;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x01b3;
    L_0x017a:
        r13 = r14.integerValue();	 Catch:{ all -> 0x00c5 }
    L_0x017e:
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = 58;
        r0 = r28;
        if (r3 == r0) goto L_0x01d0;
    L_0x0188:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "expect ':' at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = ", name ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r0 = r29;
        r29 = r0.append(r13);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x01b3:
        r28 = 1;
        r0 = r28;
        r13 = r14.decimalValue(r0);	 Catch:{ all -> 0x00c5 }
        goto L_0x017e;
    L_0x01bc:
        r28 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r28;
        if (r3 == r0) goto L_0x01c8;
    L_0x01c2:
        r28 = 91;
        r0 = r28;
        if (r3 != r0) goto L_0x0208;
    L_0x01c8:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r13 = r31.parse();	 Catch:{ all -> 0x00c5 }
        r11 = 1;
    L_0x01d0:
        if (r11 != 0) goto L_0x01d8;
    L_0x01d2:
        r14.next();	 Catch:{ all -> 0x00c5 }
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
    L_0x01d8:
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r14.resetStringPosition();	 Catch:{ all -> 0x00c5 }
        r28 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        if (r13 != r0) goto L_0x0305;
    L_0x01e5:
        r0 = r31;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        r29 = 34;
        r0 = r28;
        r1 = r29;
        r26 = r14.scanSymbol(r0, r1);	 Catch:{ all -> 0x00c5 }
        r4 = com.alibaba.fastjson.util.TypeUtils.loadClass(r26);	 Catch:{ all -> 0x00c5 }
        if (r4 != 0) goto L_0x0261;
    L_0x01fb:
        r28 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00c5 }
        r0 = r32;
        r1 = r28;
        r2 = r26;
        r0.put(r1, r2);	 Catch:{ all -> 0x00c5 }
        goto L_0x0052;
    L_0x0208:
        r28 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames;	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r28 = r0.isEnabled(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 != 0) goto L_0x021d;
    L_0x0214:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x021d:
        r0 = r31;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        r0 = r28;
        r13 = r14.scanSymbolUnQuoted(r0);	 Catch:{ all -> 0x00c5 }
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = 58;
        r0 = r28;
        if (r3 == r0) goto L_0x01d0;
    L_0x0236:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "expect ':' at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = ", actual ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r0 = r29;
        r29 = r0.append(r3);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0261:
        r28 = 16;
        r0 = r28;
        r14.nextToken(r0);	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 13;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x02ce;
    L_0x0274:
        r28 = 16;
        r0 = r28;
        r14.nextToken(r0);	 Catch:{ all -> 0x00c5 }
        r10 = 0;
        r0 = r31;
        r0 = r0.config;	 Catch:{ Exception -> 0x02c0 }
        r28 = r0;
        r0 = r28;
        r7 = r0.getDeserializer(r4);	 Catch:{ Exception -> 0x02c0 }
        r0 = r7 instanceof com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;	 Catch:{ Exception -> 0x02c0 }
        r28 = r0;
        if (r28 == 0) goto L_0x02ac;
    L_0x028e:
        r7 = (com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer) r7;	 Catch:{ Exception -> 0x02c0 }
        r0 = r31;
        r10 = r7.createInstance(r0, r4);	 Catch:{ Exception -> 0x02c0 }
    L_0x0296:
        if (r10 != 0) goto L_0x02a3;
    L_0x0298:
        r28 = java.lang.Cloneable.class;
        r0 = r28;
        if (r4 != r0) goto L_0x02bb;
    L_0x029e:
        r10 = new java.util.HashMap;	 Catch:{ Exception -> 0x02c0 }
        r10.<init>();	 Catch:{ Exception -> 0x02c0 }
    L_0x02a3:
        r0 = r31;
        r0.setContext(r5);
        r32 = r10;
        goto L_0x0015;
    L_0x02ac:
        r0 = r7 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;	 Catch:{ Exception -> 0x02c0 }
        r28 = r0;
        if (r28 == 0) goto L_0x0296;
    L_0x02b2:
        r7 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r7;	 Catch:{ Exception -> 0x02c0 }
        r0 = r31;
        r10 = r7.createInstance(r0, r4);	 Catch:{ Exception -> 0x02c0 }
        goto L_0x0296;
    L_0x02bb:
        r10 = r4.newInstance();	 Catch:{ Exception -> 0x02c0 }
        goto L_0x02a3;
    L_0x02c0:
        r8 = move-exception;
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "create instance error";
        r0 = r28;
        r1 = r29;
        r0.<init>(r1, r8);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x02ce:
        r28 = 2;
        r0 = r31;
        r1 = r28;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0 = r0.context;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        if (r28 == 0) goto L_0x02ea;
    L_0x02df:
        r0 = r33;
        r0 = r0 instanceof java.lang.Integer;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        if (r28 != 0) goto L_0x02ea;
    L_0x02e7:
        r31.popContext();	 Catch:{ all -> 0x00c5 }
    L_0x02ea:
        r0 = r31;
        r0 = r0.config;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        r0 = r28;
        r7 = r0.getDeserializer(r4);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r33;
        r32 = r7.deserialze(r0, r4, r1);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x0305:
        r28 = "$ref";
        r0 = r28;
        if (r13 != r0) goto L_0x044a;
    L_0x030c:
        r28 = 4;
        r0 = r28;
        r14.nextToken(r0);	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 4;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0428;
    L_0x031f:
        r19 = r14.stringVal();	 Catch:{ all -> 0x00c5 }
        r28 = 13;
        r0 = r28;
        r14.nextToken(r0);	 Catch:{ all -> 0x00c5 }
        r20 = 0;
        r28 = "@";
        r0 = r28;
        r1 = r19;
        r28 = r0.equals(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x037f;
    L_0x0339:
        r28 = r31.getContext();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x0416;
    L_0x033f:
        r24 = r31.getContext();	 Catch:{ all -> 0x00c5 }
        r25 = r24.getObject();	 Catch:{ all -> 0x00c5 }
        r0 = r25;
        r0 = r0 instanceof java.lang.Object[];	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        if (r28 != 0) goto L_0x0357;
    L_0x034f:
        r0 = r25;
        r0 = r0 instanceof java.util.Collection;	 Catch:{ all -> 0x00c5 }
        r28 = r0;
        if (r28 == 0) goto L_0x0370;
    L_0x0357:
        r20 = r25;
    L_0x0359:
        r32 = r20;
    L_0x035b:
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 13;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x041a;
    L_0x0367:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0370:
        r28 = r24.getParentContext();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x0359;
    L_0x0376:
        r28 = r24.getParentContext();	 Catch:{ all -> 0x00c5 }
        r20 = r28.getObject();	 Catch:{ all -> 0x00c5 }
        goto L_0x0359;
    L_0x037f:
        r28 = "..";
        r0 = r28;
        r1 = r19;
        r28 = r0.equals(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x03b9;
    L_0x038c:
        r17 = r5.getParentContext();	 Catch:{ all -> 0x00c5 }
        r28 = r17.getObject();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x039d;
    L_0x0396:
        r20 = r17.getObject();	 Catch:{ all -> 0x00c5 }
    L_0x039a:
        r32 = r20;
        goto L_0x035b;
    L_0x039d:
        r28 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        r1 = r17;
        r2 = r19;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00c5 }
        r28 = 1;
        r0 = r31;
        r1 = r28;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00c5 }
        goto L_0x039a;
    L_0x03b9:
        r28 = "$";
        r0 = r28;
        r1 = r19;
        r28 = r0.equals(r1);	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x03fd;
    L_0x03c6:
        r21 = r5;
    L_0x03c8:
        r28 = r21.getParentContext();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x03d3;
    L_0x03ce:
        r21 = r21.getParentContext();	 Catch:{ all -> 0x00c5 }
        goto L_0x03c8;
    L_0x03d3:
        r28 = r21.getObject();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x03e1;
    L_0x03d9:
        r20 = r21.getObject();	 Catch:{ all -> 0x00c5 }
    L_0x03dd:
        r32 = r20;
        goto L_0x035b;
    L_0x03e1:
        r28 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        r1 = r21;
        r2 = r19;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00c5 }
        r28 = 1;
        r0 = r31;
        r1 = r28;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00c5 }
        goto L_0x03dd;
    L_0x03fd:
        r28 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        r1 = r19;
        r0.<init>(r5, r1);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00c5 }
        r28 = 1;
        r0 = r31;
        r1 = r28;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00c5 }
    L_0x0416:
        r32 = r20;
        goto L_0x035b;
    L_0x041a:
        r28 = 16;
        r0 = r28;
        r14.nextToken(r0);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x0428:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "illegal ref, ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.token();	 Catch:{ all -> 0x00c5 }
        r30 = com.alibaba.fastjson.parser.JSONToken.name(r30);	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x044a:
        if (r22 != 0) goto L_0x0451;
    L_0x044c:
        r31.setContext(r32, r33);	 Catch:{ all -> 0x00c5 }
        r22 = 1;
    L_0x0451:
        r28 = r32.getClass();	 Catch:{ all -> 0x00c5 }
        r29 = com.alibaba.fastjson.JSONObject.class;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0462;
    L_0x045d:
        if (r13 != 0) goto L_0x04ac;
    L_0x045f:
        r13 = "null";
    L_0x0462:
        r28 = 34;
        r0 = r28;
        if (r3 != r0) goto L_0x04b1;
    L_0x0468:
        r14.scanString();	 Catch:{ all -> 0x00c5 }
        r23 = r14.stringVal();	 Catch:{ all -> 0x00c5 }
        r27 = r23;
        r28 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        r28 = r14.isEnabled(r0);	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x0493;
    L_0x047b:
        r12 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x00c5 }
        r0 = r23;
        r12.<init>(r0);	 Catch:{ all -> 0x00c5 }
        r28 = r12.scanISO8601DateIfMatch();	 Catch:{ all -> 0x00c5 }
        if (r28 == 0) goto L_0x0490;
    L_0x0488:
        r28 = r12.getCalendar();	 Catch:{ all -> 0x00c5 }
        r27 = r28.getTime();	 Catch:{ all -> 0x00c5 }
    L_0x0490:
        r12.close();	 Catch:{ all -> 0x00c5 }
    L_0x0493:
        r0 = r32;
        r1 = r27;
        r0.put(r13, r1);	 Catch:{ all -> 0x00c5 }
    L_0x049a:
        r14.skipWhitespace();	 Catch:{ all -> 0x00c5 }
        r3 = r14.getCurrent();	 Catch:{ all -> 0x00c5 }
        r28 = 44;
        r0 = r28;
        if (r3 != r0) goto L_0x0658;
    L_0x04a7:
        r14.next();	 Catch:{ all -> 0x00c5 }
        goto L_0x0052;
    L_0x04ac:
        r13 = r13.toString();	 Catch:{ all -> 0x00c5 }
        goto L_0x0462;
    L_0x04b1:
        r28 = 48;
        r0 = r28;
        if (r3 < r0) goto L_0x04bd;
    L_0x04b7:
        r28 = 57;
        r0 = r28;
        if (r3 <= r0) goto L_0x04c3;
    L_0x04bd:
        r28 = 45;
        r0 = r28;
        if (r3 != r0) goto L_0x04ef;
    L_0x04c3:
        r14.scanNumber();	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 2;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x04de;
    L_0x04d2:
        r27 = r14.integerValue();	 Catch:{ all -> 0x00c5 }
    L_0x04d6:
        r0 = r32;
        r1 = r27;
        r0.put(r13, r1);	 Catch:{ all -> 0x00c5 }
        goto L_0x049a;
    L_0x04de:
        r28 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r28;
        r28 = r0.isEnabled(r1);	 Catch:{ all -> 0x00c5 }
        r0 = r28;
        r27 = r14.decimalValue(r0);	 Catch:{ all -> 0x00c5 }
        goto L_0x04d6;
    L_0x04ef:
        r28 = 91;
        r0 = r28;
        if (r3 != r0) goto L_0x0536;
    L_0x04f5:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r15 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x00c5 }
        r15.<init>();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.parseArray(r15, r13);	 Catch:{ all -> 0x00c5 }
        r27 = r15;
        r0 = r32;
        r1 = r27;
        r0.put(r13, r1);	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 13;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0521;
    L_0x0517:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x0521:
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 16;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x0052;
    L_0x052d:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = "syntax error";
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0536:
        r28 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r28;
        if (r3 != r0) goto L_0x05ed;
    L_0x053c:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        if (r33 == 0) goto L_0x05b8;
    L_0x0541:
        r28 = r33.getClass();	 Catch:{ all -> 0x00c5 }
        r29 = java.lang.Integer.class;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x05b8;
    L_0x054d:
        r18 = 1;
    L_0x054f:
        r9 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x00c5 }
        r9.<init>();	 Catch:{ all -> 0x00c5 }
        r6 = 0;
        if (r18 != 0) goto L_0x055d;
    L_0x0557:
        r0 = r31;
        r6 = r0.setContext(r5, r9, r13);	 Catch:{ all -> 0x00c5 }
    L_0x055d:
        r0 = r31;
        r16 = r0.parseObject(r9, r13);	 Catch:{ all -> 0x00c5 }
        if (r6 == 0) goto L_0x056e;
    L_0x0565:
        r0 = r16;
        if (r9 == r0) goto L_0x056e;
    L_0x0569:
        r0 = r32;
        r6.setObject(r0);	 Catch:{ all -> 0x00c5 }
    L_0x056e:
        r28 = r13.toString();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r1 = r32;
        r2 = r28;
        r0.checkMapResolve(r1, r2);	 Catch:{ all -> 0x00c5 }
        r28 = r32.getClass();	 Catch:{ all -> 0x00c5 }
        r29 = com.alibaba.fastjson.JSONObject.class;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x05bb;
    L_0x0587:
        r28 = r13.toString();	 Catch:{ all -> 0x00c5 }
        r0 = r32;
        r1 = r28;
        r2 = r16;
        r0.put(r1, r2);	 Catch:{ all -> 0x00c5 }
    L_0x0594:
        if (r18 == 0) goto L_0x059d;
    L_0x0596:
        r0 = r31;
        r1 = r16;
        r0.setContext(r5, r1, r13);	 Catch:{ all -> 0x00c5 }
    L_0x059d:
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 13;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x05c3;
    L_0x05a9:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x05b8:
        r18 = 0;
        goto L_0x054f;
    L_0x05bb:
        r0 = r32;
        r1 = r16;
        r0.put(r13, r1);	 Catch:{ all -> 0x00c5 }
        goto L_0x0594;
    L_0x05c3:
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 16;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x0052;
    L_0x05cf:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "syntax error, ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.tokenName();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x05ed:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r27 = r31.parse();	 Catch:{ all -> 0x00c5 }
        r28 = r32.getClass();	 Catch:{ all -> 0x00c5 }
        r29 = com.alibaba.fastjson.JSONObject.class;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0604;
    L_0x0600:
        r13 = r13.toString();	 Catch:{ all -> 0x00c5 }
    L_0x0604:
        r0 = r32;
        r1 = r27;
        r0.put(r13, r1);	 Catch:{ all -> 0x00c5 }
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 13;
        r0 = r28;
        r1 = r29;
        if (r0 != r1) goto L_0x0621;
    L_0x0617:
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x0621:
        r28 = r14.token();	 Catch:{ all -> 0x00c5 }
        r29 = 16;
        r0 = r28;
        r1 = r29;
        if (r0 == r1) goto L_0x0052;
    L_0x062d:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "syntax error, position at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = ", name ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r0 = r29;
        r29 = r0.append(r13);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
    L_0x0658:
        r28 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r28;
        if (r3 != r0) goto L_0x0671;
    L_0x065e:
        r14.next();	 Catch:{ all -> 0x00c5 }
        r14.resetStringPosition();	 Catch:{ all -> 0x00c5 }
        r14.nextToken();	 Catch:{ all -> 0x00c5 }
        r31.setContext(r32, r33);	 Catch:{ all -> 0x00c5 }
        r0 = r31;
        r0.setContext(r5);
        goto L_0x0015;
    L_0x0671:
        r28 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00c5 }
        r29 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c5 }
        r29.<init>();	 Catch:{ all -> 0x00c5 }
        r30 = "syntax error, position at ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = r14.pos();	 Catch:{ all -> 0x00c5 }
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r30 = ", name ";
        r29 = r29.append(r30);	 Catch:{ all -> 0x00c5 }
        r0 = r29;
        r29 = r0.append(r13);	 Catch:{ all -> 0x00c5 }
        r29 = r29.toString();	 Catch:{ all -> 0x00c5 }
        r28.<init>(r29);	 Catch:{ all -> 0x00c5 }
        throw r28;	 Catch:{ all -> 0x00c5 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig config) {
        this.config = config;
    }

    public <T> T parseObject(Class<T> clazz) {
        return parseObject((Type) clazz);
    }

    public <T> T parseObject(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (this.lexer.token() == 4) {
            type = TypeUtils.unwrap(type);
            if (type == byte[].class) {
                T bytes = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytes;
            } else if (type == char[].class) {
                String strVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return strVal.toCharArray();
            }
        }
        try {
            return this.config.getDeserializer(type).deserialze(this, type, null);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e2) {
            JSONException jSONException = new JSONException(e2.getMessage(), e2);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        Collection array = new ArrayList();
        parseArray((Class) clazz, array);
        return array;
    }

    public void parseArray(Class<?> clazz, Collection array) {
        parseArray((Type) clazz, array);
    }

    public void parseArray(Type type, Collection array) {
        parseArray(type, array, null);
    }

    public void parseArray(Type type, Collection array, Object fieldName) {
        if (this.lexer.token() == 21 || this.lexer.token() == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token()));
        }
        ObjectDeserializer deserializer;
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(deserializer.getFastMatchToken());
        }
        ParseContext context = getContext();
        setContext(array, fieldName);
        int i = 0;
        while (true) {
            if (isEnabled(Feature.AllowArbitraryCommas)) {
                while (this.lexer.token() == 16) {
                    this.lexer.nextToken();
                }
            }
            try {
                if (this.lexer.token() == 15) {
                    break;
                }
                if (Integer.TYPE == type) {
                    array.add(IntegerCodec.instance.deserialze(this, null, null));
                } else if (String.class == type) {
                    String value;
                    if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        Object obj = parse();
                        if (obj == null) {
                            value = null;
                        } else {
                            value = obj.toString();
                        }
                    }
                    array.add(value);
                } else {
                    Object obj2;
                    if (this.lexer.token() == 8) {
                        this.lexer.nextToken();
                        obj2 = null;
                    } else {
                        obj2 = deserializer.deserialze(this, type, Integer.valueOf(i));
                    }
                    array.add(obj2);
                    checkListResolve(array);
                }
                if (this.lexer.token() == 16) {
                    this.lexer.nextToken(deserializer.getFastMatchToken());
                }
                i++;
            } finally {
                setContext(context);
            }
        }
        this.lexer.nextToken(16);
    }

    public Object[] parseArray(Type[] types) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        } else if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        } else {
            Object[] list = new Object[types.length];
            if (types.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() != 15) {
                    throw new JSONException("syntax error");
                }
                this.lexer.nextToken(16);
                return new Object[0];
            }
            this.lexer.nextToken(2);
            int i = 0;
            while (i < types.length) {
                Object value;
                if (this.lexer.token() == 8) {
                    value = null;
                    this.lexer.nextToken(16);
                } else {
                    Type type = types[i];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            value = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            value = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type != String.class) {
                        boolean isArray = false;
                        Type componentType = null;
                        if (i == types.length - 1 && (type instanceof Class)) {
                            Class<?> clazz = (Class) type;
                            isArray = clazz.isArray();
                            componentType = clazz.getComponentType();
                        }
                        if (!isArray || this.lexer.token() == 14) {
                            value = this.config.getDeserializer(type).deserialze(this, type, null);
                        } else {
                            Object varList = new ArrayList();
                            ObjectDeserializer derializer = this.config.getDeserializer(componentType);
                            int fastMatch = derializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    varList.add(derializer.deserialze(this, type, null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatch);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            value = TypeUtils.cast(varList, type, this.config);
                        }
                    } else if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        value = TypeUtils.cast(parse(), type, this.config);
                    }
                }
                list[i] = value;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() != 16) {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                } else {
                    if (i == types.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i++;
                }
            }
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return list;
        }
    }

    public void parseObject(Object object) {
        Class<?> clazz = object.getClass();
        Map<String, FieldDeserializer> setters = this.config.getFieldDeserializers(clazz);
        if (this.lexer.token() == 12 || this.lexer.token() == 16) {
            while (true) {
                String key = this.lexer.scanSymbol(this.symbolTable);
                if (key == null) {
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    } else if (this.lexer.token() == 16 && isEnabled(Feature.AllowArbitraryCommas)) {
                    }
                }
                FieldDeserializer fieldDeser = (FieldDeserializer) setters.get(key);
                if (fieldDeser == null && key != null) {
                    for (Entry<String, FieldDeserializer> entry : setters.entrySet()) {
                        if (key.equalsIgnoreCase((String) entry.getKey())) {
                            fieldDeser = (FieldDeserializer) entry.getValue();
                            break;
                        }
                    }
                }
                if (fieldDeser != null) {
                    Object fieldValue;
                    Class<?> fieldClass = fieldDeser.getFieldClass();
                    Type fieldType = fieldDeser.getFieldType();
                    if (fieldClass == Integer.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        fieldValue = IntegerCodec.instance.deserialze(this, fieldType, null);
                    } else if (fieldClass == String.class) {
                        this.lexer.nextTokenWithColon(4);
                        fieldValue = StringCodec.deserialze(this);
                    } else if (fieldClass == Long.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        fieldValue = LongCodec.instance.deserialze(this, fieldType, null);
                    } else {
                        ObjectDeserializer fieldValueDeserializer = this.config.getDeserializer(fieldClass, fieldType);
                        this.lexer.nextTokenWithColon(fieldValueDeserializer.getFastMatchToken());
                        fieldValue = fieldValueDeserializer.deserialze(this, fieldType, null);
                    }
                    fieldDeser.setValue(object, fieldValue);
                    if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    }
                } else if (isEnabled(Feature.IgnoreNotMatch)) {
                    this.lexer.nextTokenWithColon();
                    parse();
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken();
                        return;
                    }
                } else {
                    throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
                }
            }
        }
        throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
    }

    public Object parseArrayWithType(Type collectionType) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypes = ((ParameterizedType) collectionType).getActualTypeArguments();
        if (actualTypes.length != 1) {
            throw new JSONException("not support type " + collectionType);
        }
        Type actualTypeArgument = actualTypes[0];
        Collection array;
        if (actualTypeArgument instanceof Class) {
            array = new ArrayList();
            parseArray((Class) actualTypeArgument, array);
            return array;
        } else if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;
            Type upperBoundType = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(upperBoundType)) {
                array = new ArrayList();
                parseArray((Class) upperBoundType, array);
                return array;
            } else if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            } else {
                throw new JSONException("not support type : " + collectionType);
            }
        } else {
            if (actualTypeArgument instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable) actualTypeArgument;
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length != 1) {
                    throw new JSONException("not support : " + typeVariable);
                }
                Type boundType = bounds[0];
                if (boundType instanceof Class) {
                    array = new ArrayList();
                    parseArray((Class) boundType, array);
                    return array;
                }
            }
            if (actualTypeArgument instanceof ParameterizedType) {
                Type parameterizedType = (ParameterizedType) actualTypeArgument;
                array = new ArrayList();
                parseArray(parameterizedType, array);
                return array;
            }
            throw new JSONException("TODO : " + collectionType);
        }
    }

    public void acceptType(String typeName) {
        JSONLexer lexer = this.lexer;
        lexer.nextTokenWithColon();
        if (lexer.token() != 4) {
            throw new JSONException("type not match error");
        } else if (typeName.equals(lexer.stringVal())) {
            lexer.nextToken();
            if (lexer.token() == 16) {
                lexer.nextToken();
            }
        } else {
            throw new JSONException("type not match error");
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public Object getObject(String path) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (path.equals(this.contextArray[i].getPath())) {
                return this.contextArray[i].getObject();
            }
        }
        return null;
    }

    public void checkListResolve(Collection array) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (array instanceof List) {
            int index = array.size() - 1;
            List list = (List) array;
            ResolveTask task = getLastResolveTask();
            task.setFieldDeserializer(new ListResolveFieldDeserializer(this, list, index));
            task.setOwnerContext(this.context);
            setResolveStatus(0);
            return;
        }
        task = getLastResolveTask();
        task.setFieldDeserializer(new CollectionResolveFieldDeserializer(this, array));
        task.setOwnerContext(this.context);
        setResolveStatus(0);
    }

    public void checkMapResolve(Map object, String fieldName) {
        if (this.resolveStatus == 1) {
            MapResolveFieldDeserializer fieldResolver = new MapResolveFieldDeserializer(object, fieldName);
            ResolveTask task = getLastResolveTask();
            task.setFieldDeserializer(fieldResolver);
            task.setOwnerContext(this.context);
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map object) {
        return parseObject(object, null);
    }

    public JSONObject parseObject() {
        Map object = new JSONObject();
        parseObject(object);
        return object;
    }

    public final void parseArray(Collection array) {
        parseArray(array, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(java.util.Collection r13, java.lang.Object r14) {
        /*
        r12 = this;
        r11 = 4;
        r10 = 16;
        r4 = r12.getLexer();
        r8 = r4.token();
        r9 = 21;
        if (r8 == r9) goto L_0x0017;
    L_0x000f:
        r8 = r4.token();
        r9 = 22;
        if (r8 != r9) goto L_0x001a;
    L_0x0017:
        r4.nextToken();
    L_0x001a:
        r8 = r4.token();
        r9 = 14;
        if (r8 == r9) goto L_0x0053;
    L_0x0022:
        r8 = new com.alibaba.fastjson.JSONException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "syntax error, expect [, actual ";
        r9 = r9.append(r10);
        r10 = r4.token();
        r10 = com.alibaba.fastjson.parser.JSONToken.name(r10);
        r9 = r9.append(r10);
        r10 = ", pos ";
        r9 = r9.append(r10);
        r10 = r4.pos();
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
    L_0x0053:
        r4.nextToken(r11);
        r0 = r12.getContext();
        r12.setContext(r13, r14);
        r1 = 0;
    L_0x005e:
        r8 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x0070 }
        r8 = r12.isEnabled(r8);	 Catch:{ all -> 0x0070 }
        if (r8 == 0) goto L_0x0075;
    L_0x0066:
        r8 = r4.token();	 Catch:{ all -> 0x0070 }
        if (r8 != r10) goto L_0x0075;
    L_0x006c:
        r4.nextToken();	 Catch:{ all -> 0x0070 }
        goto L_0x0066;
    L_0x0070:
        r8 = move-exception;
        r12.setContext(r0);
        throw r8;
    L_0x0075:
        r8 = r4.token();	 Catch:{ all -> 0x0070 }
        switch(r8) {
            case 2: goto L_0x0093;
            case 3: goto L_0x009d;
            case 4: goto L_0x00b6;
            case 5: goto L_0x007c;
            case 6: goto L_0x00e2;
            case 7: goto L_0x00ea;
            case 8: goto L_0x010f;
            case 9: goto L_0x007c;
            case 10: goto L_0x007c;
            case 11: goto L_0x007c;
            case 12: goto L_0x00f2;
            case 13: goto L_0x007c;
            case 14: goto L_0x0100;
            case 15: goto L_0x011d;
            case 16: goto L_0x007c;
            case 17: goto L_0x007c;
            case 18: goto L_0x007c;
            case 19: goto L_0x007c;
            case 20: goto L_0x0126;
            case 21: goto L_0x007c;
            case 22: goto L_0x007c;
            case 23: goto L_0x0116;
            default: goto L_0x007c;
        };	 Catch:{ all -> 0x0070 }
    L_0x007c:
        r7 = r12.parse();	 Catch:{ all -> 0x0070 }
    L_0x0080:
        r13.add(r7);	 Catch:{ all -> 0x0070 }
        r12.checkListResolve(r13);	 Catch:{ all -> 0x0070 }
        r8 = r4.token();	 Catch:{ all -> 0x0070 }
        if (r8 != r10) goto L_0x0090;
    L_0x008c:
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
    L_0x0090:
        r1 = r1 + 1;
        goto L_0x005e;
    L_0x0093:
        r7 = r4.integerValue();	 Catch:{ all -> 0x0070 }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x009d:
        r8 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x0070 }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x0070 }
        if (r8 == 0) goto L_0x00b0;
    L_0x00a5:
        r8 = 1;
        r7 = r4.decimalValue(r8);	 Catch:{ all -> 0x0070 }
    L_0x00aa:
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00b0:
        r8 = 0;
        r7 = r4.decimalValue(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x00aa;
    L_0x00b6:
        r6 = r4.stringVal();	 Catch:{ all -> 0x0070 }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        r8 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x0070 }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x0070 }
        if (r8 == 0) goto L_0x00e0;
    L_0x00c7:
        r2 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x0070 }
        r2.<init>(r6);	 Catch:{ all -> 0x0070 }
        r8 = r2.scanISO8601DateIfMatch();	 Catch:{ all -> 0x0070 }
        if (r8 == 0) goto L_0x00de;
    L_0x00d2:
        r8 = r2.getCalendar();	 Catch:{ all -> 0x0070 }
        r7 = r8.getTime();	 Catch:{ all -> 0x0070 }
    L_0x00da:
        r2.close();	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00de:
        r7 = r6;
        goto L_0x00da;
    L_0x00e0:
        r7 = r6;
        goto L_0x0080;
    L_0x00e2:
        r7 = java.lang.Boolean.TRUE;	 Catch:{ all -> 0x0070 }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00ea:
        r7 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x0070 }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x00f2:
        r5 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x0070 }
        r5.<init>();	 Catch:{ all -> 0x0070 }
        r8 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x0070 }
        r7 = r12.parseObject(r5, r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x0100:
        r3 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x0070 }
        r3.<init>();	 Catch:{ all -> 0x0070 }
        r8 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x0070 }
        r12.parseArray(r3, r8);	 Catch:{ all -> 0x0070 }
        r7 = r3;
        goto L_0x0080;
    L_0x010f:
        r7 = 0;
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x0116:
        r7 = 0;
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        goto L_0x0080;
    L_0x011d:
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x0070 }
        r12.setContext(r0);
        return;
    L_0x0126:
        r8 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0070 }
        r9 = "unclosed jsonArray";
        r8.<init>(r9);	 Catch:{ all -> 0x0070 }
        throw r8;	 Catch:{ all -> 0x0070 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.util.Collection, java.lang.Object):void");
    }

    public ParseContext getContext() {
        return this.context;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public List<ResolveTask> getResolveTaskListDirect() {
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask task) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(task);
    }

    public ResolveTask getLastResolveTask() {
        return (ResolveTask) this.resolveTaskList.get(this.resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraProcessor> getExtraProcessorsDirect() {
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public List<ExtraTypeProvider> getExtraTypeProvidersDirect() {
        return this.extraTypeProviders;
    }

    public void setContext(ParseContext context) {
        if (!isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = context;
        }
    }

    public void popContext() {
        if (!isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.getParentContext();
            this.contextArray[this.contextArrayIndex - 1] = null;
            this.contextArrayIndex--;
        }
    }

    public ParseContext setContext(Object object, Object fieldName) {
        if (isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, object, fieldName);
    }

    public ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        if (isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parent, object, fieldName);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext context) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        if (i >= this.contextArray.length) {
            ParseContext[] newArray = new ParseContext[((this.contextArray.length * 3) / 2)];
            System.arraycopy(this.contextArray, 0, newArray, 0, this.contextArray.length);
            this.contextArray = newArray;
        }
        this.contextArray[i] = context;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse(null);
        }
        String value = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return value;
    }

    public Object parse(Object fieldName) {
        HashSet<Object> set = null;
        JSONLexer lexer = getLexer();
        switch (lexer.token()) {
            case 2:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue;
            case 3:
                Number value = lexer.decimalValue(isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return value;
            case 4:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(16);
                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                    try {
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            set = iso8601Lexer.getCalendar().getTime();
                            return set;
                        }
                        iso8601Lexer.close();
                    } finally {
                        iso8601Lexer.close();
                    }
                }
                return stringLiteral;
            case 6:
                lexer.nextToken();
                return Boolean.TRUE;
            case 7:
                lexer.nextToken();
                return Boolean.FALSE;
            case 8:
                lexer.nextToken();
                return null;
            case 9:
                lexer.nextToken(18);
                if (lexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(10);
                accept(10);
                long time = lexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(time);
            case 12:
                return parseObject(new JSONObject(), fieldName);
            case 14:
                Collection array = new JSONArray();
                parseArray(array, fieldName);
                return array;
            case 20:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, pos " + lexer.getBufferPosition());
            case 21:
                lexer.nextToken();
                Collection set2 = new HashSet();
                parseArray(set2, fieldName);
                return set2;
            case 22:
                lexer.nextToken();
                Collection treeSet = new TreeSet();
                parseArray(treeSet, fieldName);
                return treeSet;
            case 23:
                lexer.nextToken();
                return null;
            default:
                throw new JSONException("syntax error, pos " + lexer.getBufferPosition());
        }
    }

    public void config(Feature feature, boolean state) {
        getLexer().config(feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return getLexer().isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int token) {
        JSONLexer lexer = getLexer();
        if (lexer.token() == token) {
            lexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(lexer.token()));
    }

    public final void accept(int token, int nextExpectToken) {
        JSONLexer lexer = getLexer();
        if (lexer.token() == token) {
            lexer.nextToken(nextExpectToken);
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(lexer.token()));
    }

    public void close() {
        JSONLexer lexer = getLexer();
        try {
            if (!isEnabled(Feature.AutoCloseSource) || lexer.token() == 20) {
                lexer.close();
                return;
            }
            throw new JSONException("not close json text, token : " + JSONToken.name(lexer.token()));
        } catch (Throwable th) {
            lexer.close();
        }
    }

    public void handleResovleTask(Object value) {
        if (this.resolveTaskList != null) {
            int size = this.resolveTaskList.size();
            for (int i = 0; i < size; i++) {
                ResolveTask task = (ResolveTask) this.resolveTaskList.get(i);
                FieldDeserializer fieldDeser = task.getFieldDeserializer();
                if (fieldDeser != null) {
                    Object refValue;
                    Object object = null;
                    if (task.getOwnerContext() != null) {
                        object = task.getOwnerContext().getObject();
                    }
                    String ref = task.getReferenceValue();
                    if (ref.startsWith("$")) {
                        refValue = getObject(ref);
                    } else {
                        refValue = task.getContext().getObject();
                    }
                    fieldDeser.setValue(object, refValue);
                }
            }
        }
    }
}
