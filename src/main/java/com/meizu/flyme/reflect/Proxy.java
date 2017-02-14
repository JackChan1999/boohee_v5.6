package com.meizu.flyme.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Proxy {
    protected static Method getMethod(Method method, Class<?> clazz, String name, Class<?>...
            parameterTypes) {
        if (method == null) {
            try {
                method = clazz.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    protected static boolean invoke(Method method, Object obj, Object... args) {
        if (method != null) {
            try {
                method.invoke(obj, args);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return false;
    }
}
