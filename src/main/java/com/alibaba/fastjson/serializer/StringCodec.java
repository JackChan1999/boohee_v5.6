package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;

public class StringCodec implements ObjectSerializer, ObjectDeserializer {
    public static StringCodec instance = new StringCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.getWriter();
        if (value != null) {
            out.writeString(value);
        } else if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
            out.writeString("");
        } else {
            out.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return deserialze(parser);
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        JSONLexer lexer = parser.getLexer();
        String val;
        if (lexer.token() == 4) {
            val = lexer.stringVal();
            lexer.nextToken(16);
            return val;
        } else if (lexer.token() == 2) {
            val = lexer.numberString();
            lexer.nextToken(16);
            return val;
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            return value.toString();
        }
    }

    public int getFastMatchToken() {
        return 4;
    }
}
