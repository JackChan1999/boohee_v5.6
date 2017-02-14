package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONObject;
import com.wdullaer.materialdatetimepicker.date.MonthView;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class JSONLibDataFormatSerializer implements ObjectSerializer {
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.getWriter().writeNull();
            return;
        }
        Date date = (Date) object;
        Object json = new JSONObject();
        json.put("date", Integer.valueOf(date.getDate()));
        json.put("day", Integer.valueOf(date.getDay()));
        json.put("hours", Integer.valueOf(date.getHours()));
        json.put("minutes", Integer.valueOf(date.getMinutes()));
        json.put(MonthView.VIEW_PARAMS_MONTH, Integer.valueOf(date.getMonth()));
        json.put("seconds", Integer.valueOf(date.getSeconds()));
        json.put("time", Long.valueOf(date.getTime()));
        json.put("timezoneOffset", Integer.valueOf(date.getTimezoneOffset()));
        json.put(MonthView.VIEW_PARAMS_YEAR, Integer.valueOf(date.getYear()));
        serializer.write(json);
    }
}
