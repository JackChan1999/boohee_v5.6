package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobSeriliazer implements ObjectSerializer {
    public static final ClobSeriliazer instance = new ClobSeriliazer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            try {
                serializer.writeNull();
                return;
            } catch (SQLException e) {
                throw new IOException("write clob error", e);
            }
        }
        Reader reader = ((Clob) object).getCharacterStream();
        StringWriter writer = new StringWriter();
        char[] buf = new char[1024];
        while (true) {
            int len = reader.read(buf);
            if (len != -1) {
                writer.write(buf, 0, len);
            } else {
                reader.close();
                serializer.write(writer.toString());
                return;
            }
        }
    }
}
