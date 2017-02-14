package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;

public class TimeZoneCodec implements ObjectSerializer, ObjectDeserializer {
    public static final TimeZoneCodec instance = new TimeZoneCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((TimeZone) object).getID());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        String id = (String) parser.parse();
        if (id == null) {
            return null;
        }
        return TimeZone.getTimeZone(id);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
