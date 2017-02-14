package com.alipay.a.a;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.alipay.a;

public final class k implements i {
    public final Object a(Object obj, Type type) {
        int i = 0;
        if (!obj.getClass().equals(a.class)) {
            return null;
        }
        a aVar = (a) obj;
        Collection hashSet = new HashSet();
        Type type2 = type instanceof ParameterizedType ? ((ParameterizedType) type).getActualTypeArguments()[0] : Object.class;
        while (i < aVar.a()) {
            hashSet.add(e.a(aVar.a(i), type2));
            i++;
        }
        return hashSet;
    }

    public final boolean a(Class<?> cls) {
        return Set.class.isAssignableFrom(cls);
    }
}
