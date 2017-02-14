package com.alipay.security.mobile.module.commonutils.crypto;

import com.alipay.security.mobile.module.commonutils.CommonUtils;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static Method getSetter(String str, Class<?> cls, Class<?> cls2) {
        String str2;
        Method method = null;
        if (!(CommonUtils.isBlank(str) || cls == null || cls2 == null)) {
            try {
                str2 = "set" + Character.toTitleCase(str.charAt(0)) + str.substring(1, str.length());
                try {
                    method = cls.getMethod(str2, new Class[]{cls2});
                } catch (NoSuchMethodException e) {
                    new StringBuilder("[-] Not Found Method ").append(str2).append(" in class ").append(cls.getName());
                    return method;
                }
            } catch (NoSuchMethodException e2) {
                str2 = method;
                new StringBuilder("[-] Not Found Method ").append(str2).append(" in class ").append(cls.getName());
                return method;
            }
        }
        return method;
    }

    public static Object invokeMethod(Object obj, Method method, Object[] objArr) {
        Object obj2 = null;
        if (!(obj == null || method == null || objArr == null)) {
            try {
                method.setAccessible(true);
                obj2 = method.invoke(obj, objArr);
            } catch (Exception e) {
                new StringBuilder("[-] can't invoke method ").append(method.getName()).append(" on target ").append(obj).append(", ").append(e);
            }
        }
        return obj2;
    }
}
