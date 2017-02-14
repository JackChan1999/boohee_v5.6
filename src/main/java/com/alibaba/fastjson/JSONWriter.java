package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import org.java_websocket.framing.CloseFrame;

public class JSONWriter implements Closeable, Flushable {
    private JSONStreamContext context;
    private JSONSerializer serializer = new JSONSerializer(this.writer);
    private SerializeWriter writer;

    public JSONWriter(Writer out) {
        this.writer = new SerializeWriter(out);
    }

    public void config(SerializerFeature feature, boolean state) {
        this.writer.config(feature, state);
    }

    public void startObject() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, 1001);
        this.writer.write('{');
    }

    public void endObject() {
        this.writer.write('}');
        endStructure();
    }

    public void writeKey(String key) {
        writeObject(key);
    }

    public void writeValue(Object object) {
        writeObject(object);
    }

    public void writeObject(String object) {
        beforeWrite();
        this.serializer.write(object);
        afterWriter();
    }

    public void writeObject(Object object) {
        beforeWrite();
        this.serializer.write(object);
        afterWriter();
    }

    public void startArray() {
        if (this.context != null) {
            beginStructure();
        }
        this.context = new JSONStreamContext(this.context, 1004);
        this.writer.write('[');
    }

    private void beginStructure() {
        int state = this.context.getState();
        switch (state) {
            case 1001:
            case 1004:
                return;
            case 1002:
                this.writer.write(':');
                return;
            case CloseFrame.NOCODE /*1005*/:
                this.writer.write(',');
                return;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    public void endArray() {
        this.writer.write(']');
        endStructure();
    }

    private void endStructure() {
        this.context = this.context.getParent();
        if (this.context != null) {
            int newState = -1;
            switch (this.context.getState()) {
                case 1001:
                    newState = 1002;
                    break;
                case 1002:
                    newState = CloseFrame.REFUSE;
                    break;
                case 1004:
                    newState = CloseFrame.NOCODE;
                    break;
            }
            if (newState != -1) {
                this.context.setState(newState);
            }
        }
    }

    private void beforeWrite() {
        if (this.context != null) {
            switch (this.context.getState()) {
                case 1001:
                case 1004:
                    return;
                case 1002:
                    this.writer.write(':');
                    return;
                case CloseFrame.REFUSE /*1003*/:
                    this.writer.write(',');
                    return;
                case CloseFrame.NOCODE /*1005*/:
                    this.writer.write(',');
                    return;
                default:
                    return;
            }
        }
    }

    private void afterWriter() {
        if (this.context != null) {
            int newState = -1;
            switch (this.context.getState()) {
                case 1001:
                case CloseFrame.REFUSE /*1003*/:
                    newState = 1002;
                    break;
                case 1002:
                    newState = CloseFrame.REFUSE;
                    break;
                case 1004:
                    newState = CloseFrame.NOCODE;
                    break;
            }
            if (newState != -1) {
                this.context.setState(newState);
            }
        }
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }

    @Deprecated
    public void writeStartObject() {
        startObject();
    }

    @Deprecated
    public void writeEndObject() {
        endObject();
    }

    @Deprecated
    public void writeStartArray() {
        startArray();
    }

    @Deprecated
    public void writeEndArray() {
        endArray();
    }
}
