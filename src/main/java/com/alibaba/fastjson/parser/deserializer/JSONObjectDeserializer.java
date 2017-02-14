package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;

public class JSONObjectDeserializer implements ObjectDeserializer {
    public static final JSONObjectDeserializer instance = new JSONObjectDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return parser.parseObject();
    }

    public int getFastMatchToken() {
        return 12;
    }
}
