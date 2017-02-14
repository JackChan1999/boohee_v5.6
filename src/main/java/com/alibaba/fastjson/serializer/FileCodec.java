package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class FileCodec implements ObjectSerializer, ObjectDeserializer {
    public static FileCodec instance = new FileCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
        } else {
            serializer.write(((File) object).getPath());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String value = parser.parse();
        if (value == null) {
            return null;
        }
        return new File(value);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
