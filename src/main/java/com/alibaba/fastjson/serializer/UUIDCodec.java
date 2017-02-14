package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDCodec implements ObjectSerializer, ObjectDeserializer {
    public static final UUIDCodec instance = new UUIDCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((UUID) object).toString());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String name = (String) parser.parse();
        if (name == null) {
            return null;
        }
        return UUID.fromString(name);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
