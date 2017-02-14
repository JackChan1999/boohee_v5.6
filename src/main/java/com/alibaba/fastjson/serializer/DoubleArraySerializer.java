package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class DoubleArraySerializer implements ObjectSerializer {
    public static final DoubleArraySerializer instance = new DoubleArraySerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            double[] array = (double[]) object;
            int end = array.length - 1;
            if (end == -1) {
                out.append((CharSequence) "[]");
                return;
            }
            double item;
            out.append('[');
            for (int i = 0; i < end; i++) {
                item = array[i];
                if (Double.isNaN(item)) {
                    out.writeNull();
                } else {
                    out.append(Double.toString(item));
                }
                out.append(',');
            }
            item = array[end];
            if (Double.isNaN(item)) {
                out.writeNull();
            } else {
                out.append(Double.toString(item));
            }
            out.append(']');
        } else if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            out.write("[]");
        } else {
            out.writeNull();
        }
    }
}
