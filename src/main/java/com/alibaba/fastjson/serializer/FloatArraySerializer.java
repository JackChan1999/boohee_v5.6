package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class FloatArraySerializer implements ObjectSerializer {
    public static final FloatArraySerializer instance = new FloatArraySerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            float[] array = (float[]) object;
            int end = array.length - 1;
            if (end == -1) {
                out.append((CharSequence) "[]");
                return;
            }
            float item;
            out.append('[');
            for (int i = 0; i < end; i++) {
                item = array[i];
                if (Float.isNaN(item)) {
                    out.writeNull();
                } else {
                    out.append(Float.toString(item));
                }
                out.append(',');
            }
            item = array[end];
            if (Float.isNaN(item)) {
                out.writeNull();
            } else {
                out.append(Float.toString(item));
            }
            out.append(']');
        } else if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            out.write("[]");
        } else {
            out.writeNull();
        }
    }
}
