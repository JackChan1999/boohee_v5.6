package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Type;

public class ColorCodec implements ObjectSerializer, ObjectDeserializer {
    public static final ColorCodec instance = new ColorCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Color color = (Color) object;
        if (color == null) {
            out.writeNull();
            return;
        }
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Color.class.getName());
            sep = ',';
        }
        out.writeFieldValue(sep, "r", color.getRed());
        out.writeFieldValue(',', "g", color.getGreen());
        out.writeFieldValue(',', "b", color.getBlue());
        if (color.getAlpha() > 0) {
            out.writeFieldValue(',', "alpha", color.getAlpha());
        }
        out.write('}');
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 12 || lexer.token() == 16) {
            lexer.nextToken();
            int r = 0;
            int g = 0;
            int b = 0;
            int alpha = 0;
            while (lexer.token() != 13) {
                if (lexer.token() == 4) {
                    String key = lexer.stringVal();
                    lexer.nextTokenWithColon(2);
                    if (lexer.token() == 2) {
                        int val = lexer.intValue();
                        lexer.nextToken();
                        if (key.equalsIgnoreCase("r")) {
                            r = val;
                        } else if (key.equalsIgnoreCase("g")) {
                            g = val;
                        } else if (key.equalsIgnoreCase("b")) {
                            b = val;
                        } else if (key.equalsIgnoreCase("alpha")) {
                            alpha = val;
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
            return new Color(r, g, b, alpha);
        }
        throw new JSONException("syntax error");
    }

    public int getFastMatchToken() {
        return 12;
    }
}
