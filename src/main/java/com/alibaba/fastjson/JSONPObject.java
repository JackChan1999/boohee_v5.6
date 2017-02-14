package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONPObject implements JSONSerializable {
    private String function;
    private final List<Object> parameters = new ArrayList();

    public JSONPObject(String function) {
        this.function = function;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public String toJSONString() {
        return null;
    }

    public void write(JSONSerializer serializer, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter writer = serializer.getWriter();
        writer.write(this.function);
        writer.write('(');
        for (int i = 0; i < this.parameters.size(); i++) {
            if (i != 0) {
                writer.write(',');
            }
            serializer.write(this.parameters.get(i));
        }
        writer.write(')');
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
