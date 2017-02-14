package com.alipay.a.a;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;
import org.json.alipay.b;

public final class g implements i, j {
    public final Object a(Object obj) {
        Map treeMap = new TreeMap();
        Class cls = obj.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        while (!cls.equals(Object.class)) {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    Object obj2;
                    if (field == null || obj == null) {
                        obj2 = null;
                    } else {
                        if ("this$0".equals(field.getName())) {
                            obj2 = null;
                        } else {
                            boolean isAccessible = field.isAccessible();
                            field.setAccessible(true);
                            Object obj3 = field.get(obj);
                            if (obj3 == null) {
                                obj2 = null;
                            } else {
                                field.setAccessible(isAccessible);
                                obj2 = f.b(obj3);
                            }
                        }
                    }
                    if (obj2 != null) {
                        treeMap.put(field.getName(), obj2);
                    }
                }
            }
            cls = cls.getSuperclass();
            declaredFields = cls.getDeclaredFields();
        }
        return treeMap;
    }

    public final Object a(Object obj, Type type) {
        if (!obj.getClass().equals(b.class)) {
            return null;
        }
        b bVar = (b) obj;
        Class cls = (Class) type;
        Object newInstance = cls.newInstance();
        while (!cls.equals(Object.class)) {
            Field[] declaredFields = cls.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    String name = field.getName();
                    Type genericType = field.getGenericType();
                    if (bVar.b(name)) {
                        field.setAccessible(true);
                        field.set(newInstance, e.a(bVar.a(name), genericType));
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return newInstance;
    }

    public final boolean a(Class<?> cls) {
        return true;
    }
}
