package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final DateDeserializer instance = new DateDeserializer();

    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        Calendar calendar = null;
        if (val == null) {
            return null;
        }
        if (val instanceof Date) {
            return val;
        }
        if (val instanceof Number) {
            return new Date(((Number) val).longValue());
        }
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    calendar = dateLexer.getCalendar();
                    if (clazz == Calendar.class) {
                        return calendar;
                    }
                    val = calendar.getTime();
                    dateLexer.close();
                    return val;
                }
                dateLexer.close();
                try {
                    return parser.getDateFormat().parse(strVal);
                } catch (ParseException e) {
                    return new Date(Long.parseLong(strVal));
                }
            } finally {
                dateLexer.close();
            }
        } else {
            throw new JSONException("parse error");
        }
    }

    public int getFastMatchToken() {
        return 2;
    }
}
