package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicLongArrayCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AtomicLongArrayCodec instance = new AtomicLongArrayCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            AtomicLongArray array = (AtomicLongArray) object;
            int len = array.length();
            out.append('[');
            for (int i = 0; i < len; i++) {
                long val = array.get(i);
                if (i != 0) {
                    out.write(',');
                }
                out.writeLong(val);
            }
            out.append(']');
        } else if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            out.write("[]");
        } else {
            out.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (parser.getLexer().token() == 8) {
            parser.getLexer().nextToken(16);
            return null;
        }
        Collection array = new JSONArray();
        parser.parseArray(array);
        T atomicArray = new AtomicLongArray(array.size());
        for (int i = 0; i < array.size(); i++) {
            atomicArray.set(i, array.getLong(i).longValue());
        }
        return atomicArray;
    }

    public int getFastMatchToken() {
        return 14;
    }
}
