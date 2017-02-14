package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ASMJavaBeanDeserializer implements ObjectDeserializer {
    protected InnerJavaBeanDeserializer serializer;

    public final class InnerJavaBeanDeserializer extends JavaBeanDeserializer {
        private InnerJavaBeanDeserializer(ParserConfig mapping, Class<?> clazz) {
            super(mapping, clazz);
        }

        public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map<String, Object> fieldValues) {
            return ASMJavaBeanDeserializer.this.parseField(parser, key, object, objectType, fieldValues);
        }

        public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
            return ASMJavaBeanDeserializer.this.createFieldDeserializer(mapping, clazz, fieldInfo);
        }
    }

    public abstract Object createInstance(DefaultJSONParser defaultJSONParser, Type type);

    public ASMJavaBeanDeserializer(ParserConfig mapping, Class<?> clazz) {
        this.serializer = new InnerJavaBeanDeserializer(mapping, clazz);
        this.serializer.getFieldDeserializerMap();
    }

    public InnerJavaBeanDeserializer getInnterSerializer() {
        return this.serializer;
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return this.serializer.deserialze(parser, type, fieldName);
    }

    public int getFastMatchToken() {
        return this.serializer.getFastMatchToken();
    }

    public Object createInstance(DefaultJSONParser parser) {
        return this.serializer.createInstance(parser, this.serializer.getClazz());
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public FieldDeserializer getFieldDeserializer(String name) {
        return (FieldDeserializer) this.serializer.getFieldDeserializerMap().get(name);
    }

    public Type getFieldType(String name) {
        return ((FieldDeserializer) this.serializer.getFieldDeserializerMap().get(name)).getFieldType();
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.getLexer();
        Map<String, FieldDeserializer> feildDeserializerMap = this.serializer.getFieldDeserializerMap();
        FieldDeserializer fieldDeserializer = (FieldDeserializer) feildDeserializerMap.get(key);
        if (fieldDeserializer == null) {
            for (Entry<String, FieldDeserializer> entry : feildDeserializerMap.entrySet()) {
                if (((String) entry.getKey()).equalsIgnoreCase(key)) {
                    fieldDeserializer = (FieldDeserializer) entry.getValue();
                    break;
                }
            }
        }
        if (fieldDeserializer == null) {
            this.serializer.parseExtra(parser, object, key);
            return false;
        }
        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
        fieldDeserializer.parseField(parser, object, objectType, fieldValues);
        return true;
    }

    public boolean isSupportArrayToBean(JSONLexer lexer) {
        return this.serializer.isSupportArrayToBean(lexer);
    }

    public Object parseRest(DefaultJSONParser parser, Type type, Object fieldName, Object instance) {
        return this.serializer.deserialze(parser, type, fieldName, instance);
    }
}
