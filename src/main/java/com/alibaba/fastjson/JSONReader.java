package com.alibaba.fastjson;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import org.java_websocket.framing.CloseFrame;

public class JSONReader implements Closeable {
    private JSONStreamContext context;
    private final DefaultJSONParser parser;

    public JSONReader(Reader reader) {
        this(new JSONReaderScanner(reader));
    }

    public JSONReader(JSONLexer lexer) {
        this(new DefaultJSONParser(lexer));
    }

    public JSONReader(DefaultJSONParser parser) {
        this.parser = parser;
    }

    public void config(Feature feature, boolean state) {
        this.parser.config(feature, state);
    }

    public void startObject() {
        if (this.context == null) {
            this.context = new JSONStreamContext(null, 1001);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, 1001);
        }
        this.parser.accept(12, 18);
    }

    public void endObject() {
        this.parser.accept(13);
        endStructure();
    }

    public void startArray() {
        if (this.context == null) {
            this.context = new JSONStreamContext(null, 1004);
        } else {
            startStructure();
            this.context = new JSONStreamContext(this.context, 1004);
        }
        this.parser.accept(14);
    }

    public void endArray() {
        this.parser.accept(15);
        endStructure();
    }

    private void startStructure() {
        switch (this.context.getState()) {
            case 1001:
            case 1004:
                return;
            case 1002:
                this.parser.accept(17);
                return;
            case CloseFrame.REFUSE /*1003*/:
            case CloseFrame.NOCODE /*1005*/:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + this.context.getState());
        }
    }

    private void endStructure() {
        this.context = this.context.getParent();
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

    public boolean hasNext() {
        if (this.context == null) {
            throw new JSONException("context is null");
        }
        int token = this.parser.getLexer().token();
        int state = this.context.getState();
        switch (state) {
            case 1001:
            case CloseFrame.REFUSE /*1003*/:
                if (token == 13) {
                    return false;
                }
                return true;
            case 1004:
            case CloseFrame.NOCODE /*1005*/:
                if (token != 15) {
                    return true;
                }
                return false;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    public void close() {
        IOUtils.close(this.parser);
    }

    public Integer readInteger() {
        Object object;
        if (this.context == null) {
            object = this.parser.parse();
        } else {
            readBefore();
            object = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToInt(object);
    }

    public Long readLong() {
        Object object;
        if (this.context == null) {
            object = this.parser.parse();
        } else {
            readBefore();
            object = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToLong(object);
    }

    public String readString() {
        Object object;
        if (this.context == null) {
            object = this.parser.parse();
        } else {
            readBefore();
            object = this.parser.parse();
            readAfter();
        }
        return TypeUtils.castToString(object);
    }

    public <T> T readObject(TypeReference<T> typeRef) {
        return readObject(typeRef.getType());
    }

    public <T> T readObject(Type type) {
        if (this.context == null) {
            return this.parser.parseObject(type);
        }
        readBefore();
        T object = this.parser.parseObject(type);
        readAfter();
        return object;
    }

    public <T> T readObject(Class<T> type) {
        if (this.context == null) {
            return this.parser.parseObject((Class) type);
        }
        readBefore();
        T object = this.parser.parseObject((Class) type);
        readAfter();
        return object;
    }

    public void readObject(Object object) {
        if (this.context == null) {
            this.parser.parseObject(object);
            return;
        }
        readBefore();
        this.parser.parseObject(object);
        readAfter();
    }

    public Object readObject() {
        if (this.context == null) {
            return this.parser.parse();
        }
        Object parseKey;
        readBefore();
        switch (this.context.getState()) {
            case 1001:
            case CloseFrame.REFUSE /*1003*/:
                parseKey = this.parser.parseKey();
                break;
            default:
                parseKey = this.parser.parse();
                break;
        }
        readAfter();
        return parseKey;
    }

    public Object readObject(Map object) {
        if (this.context == null) {
            return this.parser.parseObject(object);
        }
        readBefore();
        Object value = this.parser.parseObject(object);
        readAfter();
        return value;
    }

    private void readBefore() {
        int state = this.context.getState();
        switch (state) {
            case 1001:
            case 1004:
                return;
            case 1002:
                this.parser.accept(17);
                return;
            case CloseFrame.REFUSE /*1003*/:
                this.parser.accept(16, 18);
                return;
            case CloseFrame.NOCODE /*1005*/:
                this.parser.accept(16);
                return;
            default:
                throw new JSONException("illegal state : " + state);
        }
    }

    private void readAfter() {
        int state = this.context.getState();
        int newStat = -1;
        switch (state) {
            case 1001:
                newStat = 1002;
                break;
            case 1002:
                newStat = CloseFrame.REFUSE;
                break;
            case CloseFrame.REFUSE /*1003*/:
                newStat = 1002;
                break;
            case 1004:
                newStat = CloseFrame.NOCODE;
                break;
            case CloseFrame.NOCODE /*1005*/:
                break;
            default:
                throw new JSONException("illegal state : " + state);
        }
        if (newStat != -1) {
            this.context.setState(newStat);
        }
    }
}
