package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class TimestampDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {
    public static final TimestampDeserializer instance = new TimestampDeserializer();

    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Date) {
            return new Timestamp(((Date) val).getTime());
        }
        if (val instanceof Number) {
            return new Timestamp(((Number) val).longValue());
        }
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            try {
                return new Timestamp(parser.getDateFormat().parse(strVal).getTime());
            } catch (ParseException e) {
                return new Timestamp(Long.parseLong(strVal));
            }
        }
        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return 2;
    }
}
