package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

public class RectangleCodec implements ObjectSerializer, ObjectDeserializer {
    public static final RectangleCodec instance = new RectangleCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Rectangle rectangle = (Rectangle) object;
        if (rectangle == null) {
            out.writeNull();
            return;
        }
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Rectangle.class.getName());
            sep = ',';
        }
        out.writeFieldValue(sep, "x", rectangle.getX());
        out.writeFieldValue(',', "y", rectangle.getY());
        out.writeFieldValue(',', "width", rectangle.getWidth());
        out.writeFieldValue(',', "height", rectangle.getHeight());
        out.write('}');
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken();
            return null;
        } else if (lexer.token() == 12 || lexer.token() == 16) {
            lexer.nextToken();
            int x = 0;
            int y = 0;
            int width = 0;
            int height = 0;
            while (lexer.token() != 13) {
                if (lexer.token() == 4) {
                    String key = lexer.stringVal();
                    lexer.nextTokenWithColon(2);
                    if (lexer.token() == 2) {
                        int val = lexer.intValue();
                        lexer.nextToken();
                        if (key.equalsIgnoreCase("x")) {
                            x = val;
                        } else if (key.equalsIgnoreCase("y")) {
                            y = val;
                        } else if (key.equalsIgnoreCase("width")) {
                            width = val;
                        } else if (key.equalsIgnoreCase("height")) {
                            height = val;
                        } else {
                            throw new JSONException("syntax error, " + key);
                        }
                        if (lexer.token() == 16) {
                            lexer.nextToken(4);
                        }
                    } else {
                        throw new JSONException("syntax error");
                    }
                }
                throw new JSONException("syntax error");
            }
            lexer.nextToken();
            return new Rectangle(x, y, width, height);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
