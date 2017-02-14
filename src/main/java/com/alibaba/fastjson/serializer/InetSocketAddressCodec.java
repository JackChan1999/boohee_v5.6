package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class InetSocketAddressCodec implements ObjectSerializer, ObjectDeserializer {
    public static InetSocketAddressCodec instance = new InetSocketAddressCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }
        SerializeWriter out = serializer.getWriter();
        InetSocketAddress address = (InetSocketAddress) object;
        Object inetAddress = address.getAddress();
        out.write('{');
        if (inetAddress != null) {
            out.writeFieldName("address");
            serializer.write(inetAddress);
            out.write(',');
        }
        out.writeFieldName("port");
        out.writeInt(address.getPort());
        out.write('}');
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken();
            return null;
        }
        parser.accept(12);
        InetAddress address = null;
        int port = 0;
        while (true) {
            String key = lexer.stringVal();
            lexer.nextToken(17);
            if (key.equals("address")) {
                parser.accept(17);
                address = (InetAddress) parser.parseObject(InetAddress.class);
            } else if (key.equals("port")) {
                parser.accept(17);
                if (lexer.token() != 2) {
                    throw new JSONException("port is not int");
                }
                port = lexer.intValue();
                lexer.nextToken();
            } else {
                parser.accept(17);
                parser.parse();
            }
            if (lexer.token() == 16) {
                lexer.nextToken();
            } else {
                parser.accept(13);
                return new InetSocketAddress(address, port);
            }
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
