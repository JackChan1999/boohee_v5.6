package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.awt.Font;
import java.io.IOException;
import java.lang.reflect.Type;

public class FontCodec implements ObjectSerializer, ObjectDeserializer {
    public static final FontCodec instance = new FontCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Font font = (Font) object;
        if (font == null) {
            out.writeNull();
            return;
        }
        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Font.class.getName());
            sep = ',';
        }
        out.writeFieldValue(sep, "name", font.getName());
        out.writeFieldValue(',', "style", font.getStyle());
        out.writeFieldValue(',', "size", font.getSize());
        out.write('}');
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        } else if (lexer.token() == 12 || lexer.token() == 16) {
            lexer.nextToken();
            int size = 0;
            int style = 0;
            String name = null;
            while (lexer.token() != 13) {
                if (lexer.token() == 4) {
                    String key = lexer.stringVal();
                    lexer.nextTokenWithColon(2);
                    if (key.equalsIgnoreCase("name")) {
                        if (lexer.token() == 4) {
                            name = lexer.stringVal();
                            lexer.nextToken();
                        } else {
                            throw new JSONException("syntax error");
                        }
                    } else if (key.equalsIgnoreCase("style")) {
                        if (lexer.token() == 2) {
                            style = lexer.intValue();
                            lexer.nextToken();
                        } else {
                            throw new JSONException("syntax error");
                        }
                    } else if (!key.equalsIgnoreCase("size")) {
                        throw new JSONException("syntax error, " + key);
                    } else if (lexer.token() == 2) {
                        size = lexer.intValue();
                        lexer.nextToken();
                    } else {
                        throw new JSONException("syntax error");
                    }
                    if (lexer.token() == 16) {
                        lexer.nextToken(4);
                    }
                } else {
                    throw new JSONException("syntax error");
                }
            }
            lexer.nextToken();
            return new Font(name, style, size);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
