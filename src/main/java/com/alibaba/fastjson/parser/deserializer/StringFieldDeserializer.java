package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.Type;
import java.util.Map;

public class StringFieldDeserializer extends FieldDeserializer {
    private final ObjectDeserializer fieldValueDeserilizer;

    public StringFieldDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
        this.fieldValueDeserilizer = config.getDeserializer(fieldInfo);
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        String value;
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 4) {
            value = lexer.stringVal();
            lexer.nextToken(16);
        } else {
            Object obj = parser.parse();
            if (obj == null) {
                value = null;
            } else {
                value = obj.toString();
            }
        }
        if (object == null) {
            fieldValues.put(this.fieldInfo.getName(), value);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        return this.fieldValueDeserilizer.getFastMatchToken();
    }
}
