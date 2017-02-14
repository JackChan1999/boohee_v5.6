package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerSerializer implements ObjectSerializer {
    public static final AtomicIntegerSerializer instance = new AtomicIntegerSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        serializer.getWriter().writeInt(((AtomicInteger) object).get());
    }
}
