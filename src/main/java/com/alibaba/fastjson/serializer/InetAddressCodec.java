package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressCodec implements ObjectSerializer, ObjectDeserializer {
    public static InetAddressCodec instance = new InetAddressCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
        } else {
            serializer.write(((InetAddress) object).getHostAddress());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        T t = null;
        String host = (String) parser.parse();
        if (!(host == null || host.length() == 0)) {
            try {
                t = InetAddress.getByName(host);
            } catch (UnknownHostException e) {
                throw new JSONException("deserialize error", e);
            }
        }
        return t;
    }

    public int getFastMatchToken() {
        return 4;
    }
}
