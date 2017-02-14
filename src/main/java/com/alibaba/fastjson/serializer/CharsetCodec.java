package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class CharsetCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CharsetCodec instance = new CharsetCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((Charset) object).toString());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String value = parser.parse();
        if (value == null) {
            return null;
        }
        return Charset.forName(value);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
