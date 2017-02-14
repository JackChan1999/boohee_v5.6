package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.DateDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CalendarCodec instance = new CalendarCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        serializer.write(((Calendar) object).getTime());
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Date value = DateDeserializer.instance.deserialze(parser, type, fieldName);
        if (value instanceof Calendar) {
            return value;
        }
        Date date = value;
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public int getFastMatchToken() {
        return 2;
    }
}
