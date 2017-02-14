package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;

public class EnumerationSeriliazer implements ObjectSerializer {
    public static EnumerationSeriliazer instance = new EnumerationSeriliazer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerialContext context;
        Throwable th;
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            Type elementType = null;
            if (serializer.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType)) {
                elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            }
            Enumeration<?> e = (Enumeration) object;
            context = serializer.getContext();
            serializer.setContext(context, object, fieldName, 0);
            try {
                out.append('[');
                int i = 0;
                while (e.hasMoreElements()) {
                    int i2;
                    try {
                        Object item = e.nextElement();
                        i2 = i + 1;
                        if (i != 0) {
                            out.append(',');
                        }
                        if (item == null) {
                            out.writeNull();
                            i = i2;
                        } else {
                            serializer.getObjectWriter(item.getClass()).write(serializer, item, Integer.valueOf(i2 - 1), elementType);
                            i = i2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        i2 = i;
                    }
                }
                out.append(']');
                serializer.setContext(context);
                return;
            } catch (Throwable th3) {
                th = th3;
            }
        } else if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            out.write("[]");
            return;
        } else {
            out.writeNull();
            return;
        }
        serializer.setContext(context);
        throw th;
    }
}
