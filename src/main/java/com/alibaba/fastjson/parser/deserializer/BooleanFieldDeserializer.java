package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Map;

public class BooleanFieldDeserializer extends FieldDeserializer {
    public BooleanFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        boolean booleanValue = true;
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 6) {
            lexer.nextToken(16);
            if (object == null) {
                fieldValues.put(this.fieldInfo.getName(), Boolean.TRUE);
            } else {
                setValue(object, true);
            }
        } else if (lexer.token() == 2) {
            int val = lexer.intValue();
            lexer.nextToken(16);
            if (val != 1) {
                booleanValue = false;
            }
            if (object == null) {
                fieldValues.put(this.fieldInfo.getName(), Boolean.valueOf(booleanValue));
            } else {
                setValue(object, booleanValue);
            }
        } else if (lexer.token() == 8) {
            lexer.nextToken(16);
            if (getFieldClass() != Boolean.TYPE && object != null) {
                setValue(object, null);
            }
        } else if (lexer.token() == 7) {
            lexer.nextToken(16);
            if (object == null) {
                fieldValues.put(this.fieldInfo.getName(), Boolean.FALSE);
            } else {
                setValue(object, false);
            }
        } else {
            Boolean value = TypeUtils.castToBoolean(parser.parse());
            if (value != null || getFieldClass() != Boolean.TYPE) {
                if (object == null) {
                    fieldValues.put(this.fieldInfo.getName(), value);
                } else {
                    setValue(object, (Object) value);
                }
            }
        }
    }

    public int getFastMatchToken() {
        return 6;
    }
}
