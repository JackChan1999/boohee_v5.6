package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanSerializer implements ObjectSerializer {
    public static final AtomicBooleanSerializer instance = new AtomicBooleanSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (((AtomicBoolean) object).get()) {
            out.append((CharSequence) "true");
        } else {
            out.append((CharSequence) "false");
        }
    }
}
