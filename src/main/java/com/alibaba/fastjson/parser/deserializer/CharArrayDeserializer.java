package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;

public class CharArrayDeserializer implements ObjectDeserializer {
    public static final CharArrayDeserializer instance = new CharArrayDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return deserialze(parser);
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 4) {
            String val = lexer.stringVal();
            lexer.nextToken(16);
            return val.toCharArray();
        } else if (lexer.token() == 2) {
            Number val2 = lexer.integerValue();
            lexer.nextToken(16);
            return val2.toString().toCharArray();
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            return JSON.toJSONString(value).toCharArray();
        }
    }

    public int getFastMatchToken() {
        return 4;
    }
}
