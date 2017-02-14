package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionSerializer implements ObjectSerializer {
    public static final CollectionSerializer instance = new CollectionSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        Throwable th;
        SerializeWriter out = serializer.getWriter();
        if (object != null) {
            Type elementType = null;
            if (serializer.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType)) {
                elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            }
            Collection<?> collection = (Collection) object;
            SerialContext context = serializer.getContext();
            serializer.setContext(context, object, fieldName, 0);
            if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
                if (HashSet.class == collection.getClass()) {
                    out.append((CharSequence) "Set");
                } else if (TreeSet.class == collection.getClass()) {
                    out.append((CharSequence) "TreeSet");
                }
            }
            out.append('[');
            int i = 0;
            for (Object item : collection) {
                int i2;
                try {
                    i2 = i + 1;
                    if (i != 0) {
                        out.append(',');
                    }
                    if (item == null) {
                        out.writeNull();
                        i = i2;
                    } else {
                        try {
                            Class<?> clazz = item.getClass();
                            if (clazz == Integer.class) {
                                out.writeInt(((Integer) item).intValue());
                                i = i2;
                            } else if (clazz == Long.class) {
                                out.writeLong(((Long) item).longValue());
                                if (out.isEnabled(SerializerFeature.WriteClassName)) {
                                    out.write('L');
                                    i = i2;
                                } else {
                                    i = i2;
                                }
                            } else {
                                serializer.getObjectWriter(clazz).write(serializer, item, Integer.valueOf(i2 - 1), elementType);
                                i = i2;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    i2 = i;
                }
            }
            out.append(']');
            serializer.setContext(context);
            return;
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
