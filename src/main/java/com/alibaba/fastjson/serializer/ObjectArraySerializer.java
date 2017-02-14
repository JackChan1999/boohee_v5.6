package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class ObjectArraySerializer implements ObjectSerializer {
    public static final ObjectArraySerializer instance = new ObjectArraySerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Object[] array = (Object[]) object;
        if (object != null) {
            int size = array.length;
            int end = size - 1;
            if (end == -1) {
                out.append((CharSequence) "[]");
                return;
            }
            SerialContext context = serializer.getContext();
            serializer.setContext(context, object, fieldName, 0);
            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;
            out.append('[');
            int i;
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.incrementIndent();
                serializer.println();
                for (i = 0; i < size; i++) {
                    if (i != 0) {
                        out.write(',');
                        serializer.println();
                    }
                    serializer.write(array[i]);
                }
                serializer.decrementIdent();
                serializer.println();
                out.write(']');
                return;
            }
            Object item;
            i = 0;
            while (i < end) {
                try {
                    item = array[i];
                    if (item == null) {
                        out.append((CharSequence) "null,");
                    } else {
                        if (serializer.containsReference(item)) {
                            serializer.writeReference(item);
                        } else {
                            Class<?> clazz = item.getClass();
                            if (clazz == preClazz) {
                                preWriter.write(serializer, item, null, null);
                            } else {
                                preClazz = clazz;
                                preWriter = serializer.getObjectWriter(clazz);
                                preWriter.write(serializer, item, null, null);
                            }
                        }
                        out.append(',');
                    }
                    i++;
                } finally {
                    serializer.setContext(context);
                }
            }
            item = array[end];
            if (item == null) {
                out.append((CharSequence) "null]");
            } else {
                if (serializer.containsReference(item)) {
                    serializer.writeReference(item);
                } else {
                    serializer.writeWithFieldName(item, Integer.valueOf(end));
                }
                out.append(']');
            }
            serializer.setContext(context);
        } else if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            out.write("[]");
        } else {
            out.writeNull();
        }
    }
}
