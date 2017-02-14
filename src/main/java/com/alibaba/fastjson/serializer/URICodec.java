package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;

public class URICodec implements ObjectSerializer, ObjectDeserializer {
    public static final URICodec instance = new URICodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((URI) object).toString());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String uri = (String) parser.parse();
        if (uri == null) {
            return null;
        }
        return URI.create(uri);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
