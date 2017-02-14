package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Type;

public class PointCodec implements ObjectSerializer, ObjectDeserializer {
    public static final PointCodec instance = new PointCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Point font = (Point) object;
        if (font == null) {
            out.writeNull();
            return;
        }
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Point.class.getName());
            sep = ',';
        }
        out.writeFieldValue(sep, "x", font.getX());
        out.writeFieldValue(',', "y", font.getY());
        out.write('}');
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        } else if (lexer.token() == 12 || lexer.token() == 16) {
            lexer.nextToken();
            int x = 0;
            int y = 0;
            while (lexer.token() != 13) {
                if (lexer.token() == 4) {
                    String key = lexer.stringVal();
                    if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                        parser.acceptType("java.awt.Point");
                    } else {
                        lexer.nextTokenWithColon(2);
                        if (lexer.token() == 2) {
                            int val = lexer.intValue();
                            lexer.nextToken();
                            if (key.equalsIgnoreCase("x")) {
                                x = val;
                            } else if (key.equalsIgnoreCase("y")) {
                                y = val;
                            } else {
                                throw new JSONException("syntax error, " + key);
                            }
                            if (lexer.token() == 16) {
                                lexer.nextToken(4);
                            }
                        } else {
                            throw new JSONException("syntax error : " + lexer.tokenName());
                        }
                    }
                }
                throw new JSONException("syntax error");
            }
            lexer.nextToken();
            return new Point(x, y);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
