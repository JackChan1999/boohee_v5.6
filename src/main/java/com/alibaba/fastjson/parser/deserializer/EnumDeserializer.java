package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class EnumDeserializer implements ObjectDeserializer {
    private final Class<?> enumClass;
    private final Map<String, Enum> nameMap = new HashMap();
    private final Map<Integer, Enum> ordinalMap = new HashMap();

    public EnumDeserializer(Class<?> enumClass) {
        this.enumClass = enumClass;
        try {
            for (Object value : (Object[]) enumClass.getMethod("values", new Class[0]).invoke(null, new Object[0])) {
                Enum e = (Enum) value;
                this.ordinalMap.put(Integer.valueOf(e.ordinal()), e);
                this.nameMap.put(e.name(), e);
            }
        } catch (Exception e2) {
            throw new JSONException("init enum values error, " + enumClass.getName());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            JSONLexer lexer = parser.getLexer();
            if (lexer.token() == 2) {
                Integer value = Integer.valueOf(lexer.intValue());
                lexer.nextToken(16);
                T t = this.ordinalMap.get(value);
                if (t != null) {
                    return t;
                }
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + value);
            } else if (lexer.token() == 4) {
                String strVal = lexer.stringVal();
                lexer.nextToken(16);
                if (strVal.length() == 0) {
                    return null;
                }
                Object value2 = this.nameMap.get(strVal);
                return Enum.valueOf(this.enumClass, strVal);
            } else if (lexer.token() == 8) {
                lexer.nextToken(16);
                return null;
            } else {
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + parser.parse());
            }
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e2) {
            JSONException jSONException = new JSONException(e2.getMessage(), e2);
        }
    }

    public int getFastMatchToken() {
        return 2;
    }
}
