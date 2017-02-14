package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IdentityHashMap;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class SerializeConfig extends IdentityHashMap<Type, ObjectSerializer> {
    private static final SerializeConfig globalInstance = new SerializeConfig();
    private boolean asm;
    private ASMSerializerFactory asmFactory;
    private String typeKey;

    public String getTypeKey() {
        return this.typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public final ObjectSerializer createASMSerializer(Class<?> clazz) throws Exception {
        return this.asmFactory.createJavaBeanSerializer(clazz);
    }

    public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return new JavaBeanSerializer(clazz);
        }
        boolean asm = this.asm;
        if ((asm && this.asmFactory.isExternalClass(clazz)) || clazz == Serializable.class || clazz == Object.class) {
            asm = false;
        }
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (!(annotation == null || annotation.asm())) {
            asm = false;
        }
        if (asm && !ASMUtils.checkName(clazz.getName())) {
            asm = false;
        }
        if (asm) {
            try {
                ObjectSerializer asmSerializer = createASMSerializer(clazz);
                if (asmSerializer != null) {
                    return asmSerializer;
                }
            } catch (ClassCastException e) {
            } catch (Throwable e2) {
                JSONException jSONException = new JSONException("create asm serializer error, class " + clazz, e2);
            }
        }
        return new JavaBeanSerializer(clazz);
    }

    public boolean isAsmEnable() {
        return this.asm;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asm = asmEnable;
    }

    public static final SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig() {
        this(1024);
    }

    public SerializeConfig(int tableSize) {
        super(tableSize);
        this.asm = !ASMUtils.isAndroid();
        this.typeKey = JSON.DEFAULT_TYPE_KEY;
        try {
            this.asmFactory = new ASMSerializerFactory();
        } catch (ExceptionInInitializerError e) {
            this.asm = false;
        }
        put(Boolean.class, BooleanCodec.instance);
        put(Character.class, CharacterCodec.instance);
        put(Byte.class, IntegerCodec.instance);
        put(Short.class, IntegerCodec.instance);
        put(Integer.class, IntegerCodec.instance);
        put(Long.class, LongCodec.instance);
        put(Float.class, FloatCodec.instance);
        put(Double.class, DoubleSerializer.instance);
        put(BigDecimal.class, BigDecimalCodec.instance);
        put(BigInteger.class, BigIntegerCodec.instance);
        put(String.class, StringCodec.instance);
        put(byte[].class, ByteArraySerializer.instance);
        put(short[].class, ShortArraySerializer.instance);
        put(int[].class, IntArraySerializer.instance);
        put(long[].class, LongArraySerializer.instance);
        put(float[].class, FloatArraySerializer.instance);
        put(double[].class, DoubleArraySerializer.instance);
        put(boolean[].class, BooleanArraySerializer.instance);
        put(char[].class, CharArraySerializer.instance);
        put(Object[].class, ObjectArraySerializer.instance);
        put(Class.class, ClassSerializer.instance);
        put(SimpleDateFormat.class, DateFormatSerializer.instance);
        put(Locale.class, LocaleCodec.instance);
        put(Currency.class, CurrencyCodec.instance);
        put(TimeZone.class, TimeZoneCodec.instance);
        put(UUID.class, UUIDCodec.instance);
        put(InetAddress.class, InetAddressCodec.instance);
        put(Inet4Address.class, InetAddressCodec.instance);
        put(Inet6Address.class, InetAddressCodec.instance);
        put(InetSocketAddress.class, InetSocketAddressCodec.instance);
        put(File.class, FileCodec.instance);
        put(URI.class, URICodec.instance);
        put(URL.class, URLCodec.instance);
        put(Appendable.class, AppendableSerializer.instance);
        put(StringBuffer.class, AppendableSerializer.instance);
        put(StringBuilder.class, AppendableSerializer.instance);
        put(Pattern.class, PatternCodec.instance);
        put(Charset.class, CharsetCodec.instance);
        put(AtomicBoolean.class, AtomicBooleanSerializer.instance);
        put(AtomicInteger.class, AtomicIntegerSerializer.instance);
        put(AtomicLong.class, AtomicLongSerializer.instance);
        put(AtomicReference.class, ReferenceCodec.instance);
        put(AtomicIntegerArray.class, AtomicIntegerArrayCodec.instance);
        put(AtomicLongArray.class, AtomicLongArrayCodec.instance);
        put(WeakReference.class, ReferenceCodec.instance);
        put(SoftReference.class, ReferenceCodec.instance);
        try {
            put(Class.forName("java.awt.Color"), ColorCodec.instance);
            put(Class.forName("java.awt.Font"), FontCodec.instance);
            put(Class.forName("java.awt.Point"), PointCodec.instance);
            put(Class.forName("java.awt.Rectangle"), RectangleCodec.instance);
        } catch (Throwable th) {
        }
        try {
            put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
            put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
        } catch (Throwable th2) {
        }
    }
}
