package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public final class ListResolveFieldDeserializer extends FieldDeserializer {
    private final int index;
    private final List list;
    private final DefaultJSONParser parser;

    public ListResolveFieldDeserializer(DefaultJSONParser parser, List list, int index) {
        super(null, null);
        this.parser = parser;
        this.index = index;
        this.list = list;
    }

    public void setValue(Object object, Object value) {
        this.list.set(this.index, value);
        if (this.list instanceof JSONArray) {
            JSONArray jsonArray = this.list;
            Object array = jsonArray.getRelatedArray();
            if (array != null && Array.getLength(array) > this.index) {
                Object item;
                if (jsonArray.getComponentType() != null) {
                    item = TypeUtils.cast(value, jsonArray.getComponentType(), this.parser.getConfig());
                } else {
                    item = value;
                }
                Array.set(array, this.index, item);
            }
        }
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> map) {
    }
}
