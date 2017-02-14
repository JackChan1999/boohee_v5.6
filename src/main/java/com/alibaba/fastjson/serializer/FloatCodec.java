package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;

public class FloatCodec implements ObjectSerializer, ObjectDeserializer {
    public static FloatCodec instance = new FloatCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            float floatValue = ((Float) object).floatValue();
            if (Float.isNaN(floatValue)) {
                out.writeNull();
            } else if (Float.isInfinite(floatValue)) {
                out.writeNull();
            } else {
                String floatText = Float.toString(floatValue);
                if (floatText.endsWith(".0")) {
                    floatText = floatText.substring(0, floatText.length() - 2);
                }
                out.write(floatText);
                if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                    out.write('F');
                }
            }
        } else if (serializer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            out.write('0');
        } else {
            out.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return deserialze(parser);
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 2) {
            String val = lexer.numberString();
            lexer.nextToken(16);
            return Float.valueOf(Float.parseFloat(val));
        } else if (lexer.token() == 3) {
            float val2 = lexer.floatValue();
            lexer.nextToken(16);
            return Float.valueOf(val2);
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            return TypeUtils.castToFloat(value);
        }
    }

    public int getFastMatchToken() {
        return 2;
    }
}
