package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;

public abstract class AbstractDateDeserializer implements ObjectDeserializer {
    protected abstract <T> T cast(DefaultJSONParser defaultJSONParser, Type type, Object obj, Object obj2);

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Long val;
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 2) {
            val = Long.valueOf(lexer.longValue());
            lexer.nextToken(16);
        } else if (lexer.token() == 4) {
            String strVal = lexer.stringVal();
            val = strVal;
            lexer.nextToken(16);
            if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                JSONScanner iso8601Lexer = new JSONScanner(strVal);
                if (iso8601Lexer.scanISO8601DateIfMatch()) {
                    val = iso8601Lexer.getCalendar().getTime();
                }
                iso8601Lexer.close();
            }
        } else if (lexer.token() == 8) {
            lexer.nextToken();
            val = null;
        } else if (lexer.token() == 12) {
            lexer.nextToken();
            if (lexer.token() == 4) {
                if (JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal())) {
                    lexer.nextToken();
                    parser.accept(17);
                    Class<?> type = TypeUtils.loadClass(lexer.stringVal());
                    if (type != null) {
                        clazz = type;
                    }
                    parser.accept(4);
                    parser.accept(16);
                }
                lexer.nextTokenWithColon(2);
                if (lexer.token() == 2) {
                    long timeMillis = lexer.longValue();
                    lexer.nextToken();
                    val = Long.valueOf(timeMillis);
                    parser.accept(13);
                } else {
                    throw new JSONException("syntax error : " + lexer.tokenName());
                }
            }
            throw new JSONException("syntax error");
        } else if (parser.getResolveStatus() == 2) {
            parser.setResolveStatus(0);
            parser.accept(16);
            if (lexer.token() != 4) {
                throw new JSONException("syntax error");
            } else if ("val".equals(lexer.stringVal())) {
                lexer.nextToken();
                parser.accept(17);
                val = parser.parse();
                parser.accept(13);
            } else {
                throw new JSONException("syntax error");
            }
        } else {
            val = parser.parse();
        }
        return cast(parser, clazz, fieldName, val);
    }
}
