package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSerializer implements ObjectSerializer {
    public static final DateSerializer instance = new DateSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
        } else if (!out.isEnabled(SerializerFeature.WriteClassName) || object.getClass() == fieldType) {
            Date date = (Date) object;
            if (out.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
                DateFormat format = serializer.getDateFormat();
                if (format == null) {
                    format = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT);
                }
                out.writeString(format.format(date));
                return;
            }
            long time = date.getTime();
            if (serializer.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
                char[] buf;
                if (serializer.isEnabled(SerializerFeature.UseSingleQuotes)) {
                    out.append('\'');
                } else {
                    out.append('\"');
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                int year = calendar.get(1);
                int month = calendar.get(2) + 1;
                int day = calendar.get(5);
                int hour = calendar.get(11);
                int minute = calendar.get(12);
                int second = calendar.get(13);
                int millis = calendar.get(14);
                if (millis != 0) {
                    buf = "0000-00-00T00:00:00.000".toCharArray();
                    IOUtils.getChars(millis, 23, buf);
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                }
                out.write(buf);
                if (serializer.isEnabled(SerializerFeature.UseSingleQuotes)) {
                    out.append('\'');
                    return;
                } else {
                    out.append('\"');
                    return;
                }
            }
            out.writeLong(time);
        } else if (object.getClass() == Date.class) {
            out.write("new Date(");
            out.writeLongAndChar(((Date) object).getTime(), ')');
        } else {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            serializer.write(object.getClass().getName());
            out.writeFieldValue(',', "val", ((Date) object).getTime());
            out.write('}');
        }
    }
}
