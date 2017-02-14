package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;

public class JavaObjectDeserializer implements ObjectDeserializer {
    public static final JavaObjectDeserializer instance = new JavaObjectDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (!(type instanceof GenericArrayType)) {
            return parser.parse(fieldName);
        }
        Type componentType = ((GenericArrayType) type).getGenericComponentType();
        if (componentType instanceof TypeVariable) {
            componentType = ((TypeVariable) componentType).getBounds()[0];
        }
        Object list = new ArrayList();
        parser.parseArray(componentType, (Collection) list);
        if (!(componentType instanceof Class)) {
            return list.toArray();
        }
        Class<?> componentClass = (Class) componentType;
        if (componentClass == Boolean.TYPE) {
            return TypeUtils.cast(list, boolean[].class, parser.getConfig());
        }
        if (componentClass == Short.TYPE) {
            return TypeUtils.cast(list, short[].class, parser.getConfig());
        }
        if (componentClass == Integer.TYPE) {
            return TypeUtils.cast(list, int[].class, parser.getConfig());
        }
        if (componentClass == Long.TYPE) {
            return TypeUtils.cast(list, long[].class, parser.getConfig());
        }
        if (componentClass == Float.TYPE) {
            return TypeUtils.cast(list, float[].class, parser.getConfig());
        }
        if (componentClass == Double.TYPE) {
            return TypeUtils.cast(list, double[].class, parser.getConfig());
        }
        Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
        list.toArray(array);
        return array;
    }

    public int getFastMatchToken() {
        return 12;
    }
}
