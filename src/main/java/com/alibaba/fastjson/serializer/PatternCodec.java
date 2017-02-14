package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class PatternCodec implements ObjectSerializer, ObjectDeserializer {
    public static final PatternCodec instance = new PatternCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((Pattern) object).pattern());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String value = parser.parse();
        if (value == null) {
            return null;
        }
        return Pattern.compile(value);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
