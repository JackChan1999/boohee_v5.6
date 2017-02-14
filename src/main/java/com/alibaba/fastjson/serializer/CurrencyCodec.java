package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Currency;

public class CurrencyCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CurrencyCodec instance = new CurrencyCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
        } else {
            out.writeString(((Currency) object).getCurrencyCode());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String text = (String) parser.parse();
        if (text == null || text.length() == 0) {
            return null;
        }
        return Currency.getInstance(text);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
