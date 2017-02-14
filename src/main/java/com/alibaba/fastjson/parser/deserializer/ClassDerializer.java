package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

public class ClassDerializer implements ObjectDeserializer {
    public static final ClassDerializer instance = new ClassDerializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken();
            return null;
        } else if (lexer.token() != 4) {
            throw new JSONException("expect className");
        } else {
            String className = lexer.stringVal();
            lexer.nextToken(16);
            return TypeUtils.loadClass(className);
        }
    }

    public int getFastMatchToken() {
        return 4;
    }
}
