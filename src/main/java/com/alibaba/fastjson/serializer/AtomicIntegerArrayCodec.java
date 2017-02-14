package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AtomicIntegerArrayCodec instance = new AtomicIntegerArrayCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            AtomicIntegerArray array = (AtomicIntegerArray) object;
            int len = array.length();
            out.append('[');
            for (int i = 0; i < len; i++) {
                int val = array.get(i);
                if (i != 0) {
                    out.write(',');
                }
                out.writeInt(val);
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
        T atomicArray = new AtomicIntegerArray(array.size());
        for (int i = 0; i < array.size(); i++) {
            atomicArray.set(i, array.getInteger(i).intValue());
        }
        return atomicArray;
    }

    public int getFastMatchToken() {
        return 14;
    }
}
