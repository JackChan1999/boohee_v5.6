package com.zxinsight.common.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class b {
    private final Object a;
    private final boolean b = true;

    private b(Class<?> cls) {
        this.a = cls;
    }

    private b(Object obj) {
        this.a = obj;
    }

    public static b a(String str) {
        return a(f(str));
    }

    public static b a(Class<?> cls) {
        return new b((Class) cls);
    }

    public static b a(Object obj) {
        return new b(obj);
    }

    public static <T extends AccessibleObject> T a(T t) {
        if (t == null) {
            return null;
        }
        if (t instanceof Member) {
            Member member = (Member) t;
            if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(member
                    .getDeclaringClass().getModifiers())) {
                return t;
            }
        }
        if (t.isAccessible()) {
            return t;
        }
        t.setAccessible(true);
        return t;
    }

    public <T> T a() {
        return this.a;
    }

    public <T> T b(String str) {
        return c(str).a();
    }

    public b c(String str) {
        try {
            return a(e(str).get(this.a));
        } catch (Throwable e) {
            throw new ReflectException(e);
        }
    }

    private Field e(String str) {
        Field field;
        Class b = b();
        try {
            field = b.getField(str);
        } catch (Throwable e) {
            Class cls = b;
            while (true) {
                try {
                    field = (Field) a(cls.getDeclaredField(str));
                    break;
                } catch (NoSuchFieldException e2) {
                    b = cls.getSuperclass();
                    if (b == null) {
                        throw new ReflectException(e);
                    }
                    cls = b;
                }
            }
        }
        return field;
    }

    public b d(String str) {
        return a(str, new Object[0]);
    }

    public b a(String str, Object... objArr) {
        b a;
        Class[] a2 = a(objArr);
        try {
            a = a(a(str, a2), this.a, objArr);
        } catch (NoSuchMethodException e) {
            try {
                a = a(b(str, a2), this.a, objArr);
            } catch (Throwable e2) {
                throw new ReflectException(e2);
            }
        }
        return a;
    }

    private Method a(String str, Class<?>[] clsArr) {
        Method method;
        Class b = b();
        try {
            method = b.getMethod(str, clsArr);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    method = b.getDeclaredMethod(str, clsArr);
                } catch (NoSuchMethodException e2) {
                    b = b.getSuperclass();
                    if (b != null) {
                        throw new NoSuchMethodException();
                    }
                }
            } while (b != null);
            throw new NoSuchMethodException();
        }
        return method;
    }

    private Method b(String str, Class<?>[] clsArr) {
        Class b = b();
        for (Method method : b.getMethods()) {
            if (a(method, str, (Class[]) clsArr)) {
                return method;
            }
        }
        do {
            for (Method method2 : b.getDeclaredMethods()) {
                if (a(method2, str, (Class[]) clsArr)) {
                    return method2;
                }
            }
            b = b.getSuperclass();
        } while (b != null);
        throw new NoSuchMethodException("No similar method " + str + " with params " + Arrays
                .toString(clsArr) + " could be found on type " + b() + ".");
    }

    private boolean a(Method method, String str, Class<?>[] clsArr) {
        return method.getName().equals(str) && a(method.getParameterTypes(), (Class[]) clsArr);
    }

    private boolean a(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        int i = 0;
        while (i < clsArr2.length) {
            if (clsArr2[i] != a.class && !b(clsArr[i]).isAssignableFrom(b(clsArr2[i]))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof b) {
            return this.a.equals(((b) obj).a());
        }
        return false;
    }

    public String toString() {
        return this.a.toString();
    }

    private static b a(Method method, Object obj, Object... objArr) {
        try {
            a((AccessibleObject) method);
            if (method.getReturnType() != Void.TYPE) {
                return a(method.invoke(obj, objArr));
            }
            method.invoke(obj, objArr);
            return a(obj);
        } catch (Throwable e) {
            throw new ReflectException(e);
        }
    }

    private static Class<?>[] a(Object... objArr) {
        int i = 0;
        if (objArr == null) {
            return new Class[0];
        }
        Class<?>[] clsArr = new Class[objArr.length];
        while (i < objArr.length) {
            Object obj = objArr[i];
            clsArr[i] = obj == null ? a.class : obj.getClass();
            i++;
        }
        return clsArr;
    }

    private static Class<?> f(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new ReflectException(e);
        }
    }

    public Class<?> b() {
        if (this.b) {
            return (Class) this.a;
        }
        return this.a.getClass();
    }

    public static Class<?> b(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        if (!cls.isPrimitive()) {
            return cls;
        }
        if (Boolean.TYPE == cls) {
            return Boolean.class;
        }
        if (Integer.TYPE == cls) {
            return Integer.class;
        }
        if (Long.TYPE == cls) {
            return Long.class;
        }
        if (Short.TYPE == cls) {
            return Short.class;
        }
        if (Byte.TYPE == cls) {
            return Byte.class;
        }
        if (Double.TYPE == cls) {
            return Double.class;
        }
        if (Float.TYPE == cls) {
            return Float.class;
        }
        if (Character.TYPE == cls) {
            return Character.class;
        }
        if (Void.TYPE == cls) {
            return Void.class;
        }
        return cls;
    }
}
