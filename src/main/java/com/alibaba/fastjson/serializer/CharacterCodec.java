package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.tencent.tinker.android.dex.DexFormat;
import java.io.IOException;
import java.lang.reflect.Type;

public class CharacterCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CharacterCodec instance = new CharacterCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Character value = (Character) object;
        if (value == null) {
            out.writeString("");
        } else if (value.charValue() == '\u0000') {
            out.writeString(DexFormat.MAGIC_SUFFIX);
        } else {
            out.writeString(value.toString());
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object value = parser.parse();
        if (value == null) {
            return null;
        }
        return TypeUtils.castToChar(value);
    }

    public int getFastMatchToken() {
        return 4;
    }
}
