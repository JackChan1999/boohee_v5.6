package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerCodec implements ObjectSerializer, ObjectDeserializer {
    public static IntegerCodec instance = new IntegerCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Number value = (Number) object;
        if (value != null) {
            out.writeInt(value.intValue());
            if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                Class<?> clazz = value.getClass();
                if (clazz == Byte.class) {
                    out.write('B');
                } else if (clazz == Short.class) {
                    out.write('S');
                }
            }
        } else if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            out.write('0');
        } else {
            out.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        T intObj;
        if (lexer.token() == 2) {
            int val = lexer.intValue();
            lexer.nextToken(16);
            intObj = Integer.valueOf(val);
        } else if (lexer.token() == 3) {
            BigDecimal decimalValue = lexer.decimalValue();
            lexer.nextToken(16);
            intObj = Integer.valueOf(decimalValue.intValue());
        } else {
            intObj = TypeUtils.castToInt(parser.parse());
        }
        if (clazz == AtomicInteger.class) {
            return new AtomicInteger(intObj.intValue());
        }
        return intObj;
    }

    public int getFastMatchToken() {
        return 2;
    }
}
