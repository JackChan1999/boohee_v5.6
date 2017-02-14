package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerCodec implements ObjectSerializer, ObjectDeserializer {
    public static final BigIntegerCodec instance = new BigIntegerCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            out.write(((BigInteger) object).toString());
        } else if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
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
            return new BigInteger(val);
        }
        Object value = parser.parse();
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBigInteger(value);
    }

    public int getFastMatchToken() {
        return 2;
    }
}
