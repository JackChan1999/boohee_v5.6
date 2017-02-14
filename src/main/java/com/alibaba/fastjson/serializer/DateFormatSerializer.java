package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class DateFormatSerializer implements ObjectSerializer {
    public static final DateFormatSerializer instance = new DateFormatSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        String pattern = ((SimpleDateFormat) object).toPattern();
        if (!out.isEnabled(SerializerFeature.WriteClassName) || object.getClass() == fieldType) {
            out.writeString(pattern);
            return;
        }
        out.write('{');
        out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
        serializer.write(object.getClass().getName());
        out.writeFieldValue(',', "val", pattern);
        out.write('}');
    }
}
