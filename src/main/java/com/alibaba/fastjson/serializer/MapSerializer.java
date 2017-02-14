package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapSerializer implements ObjectSerializer {
    public static MapSerializer instance = new MapSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
            return;
        }
        Map<?, ?> map = (Map) object;
        if (!(!out.isEnabled(SerializerFeature.SortField) || (map instanceof SortedMap) || (map instanceof LinkedHashMap))) {
            try {
                map = new TreeMap(map);
            } catch (Exception e) {
            }
        }
        if (serializer.containsReference(object)) {
            serializer.writeReference(object);
            return;
        }
        SerialContext parent = serializer.getContext();
        serializer.setContext(parent, object, fieldName, 0);
        try {
            out.write('{');
            serializer.incrementIndent();
            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;
            boolean first = true;
            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                out.writeString(object.getClass().getName());
                first = false;
            }
            for (Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                Object entryKey = entry.getKey();
                List<PropertyPreFilter> preFilters = serializer.getPropertyPreFiltersDirect();
                if (preFilters != null && preFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        if (!FilterUtils.applyName(serializer, object, (String) entryKey)) {
                        }
                    } else if ((entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) && !FilterUtils.applyName(serializer, object, JSON.toJSONString(entryKey))) {
                    }
                }
                List<PropertyFilter> propertyFilters = serializer.getPropertyFiltersDirect();
                if (propertyFilters != null && propertyFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        if (!FilterUtils.apply(serializer, object, (String) entryKey, value)) {
                        }
                    } else if ((entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) && !FilterUtils.apply(serializer, object, JSON.toJSONString(entryKey), value)) {
                    }
                }
                List<NameFilter> nameFilters = serializer.getNameFiltersDirect();
                if (nameFilters != null && nameFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        entryKey = FilterUtils.processKey(serializer, object, (String) entryKey, value);
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        entryKey = FilterUtils.processKey(serializer, object, JSON.toJSONString(entryKey), value);
                    }
                }
                List<ValueFilter> valueFilters = serializer.getValueFiltersDirect();
                if (valueFilters != null && valueFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        value = FilterUtils.processValue(serializer, object, (String) entryKey, value);
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        value = FilterUtils.processValue(serializer, object, JSON.toJSONString(entryKey), value);
                    }
                }
                if (value != null || serializer.isEnabled(SerializerFeature.WriteMapNullValue)) {
                    if (entryKey instanceof String) {
                        String key = (String) entryKey;
                        if (!first) {
                            out.write(',');
                        }
                        if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                            serializer.println();
                        }
                        out.writeFieldName(key, true);
                    } else {
                        if (!first) {
                            out.write(',');
                        }
                        if (out.isEnabled(SerializerFeature.BrowserCompatible) || out.isEnabled(SerializerFeature.WriteNonStringKeyAsString)) {
                            serializer.write(JSON.toJSONString(entryKey));
                        } else {
                            serializer.write(entryKey);
                        }
                        out.write(':');
                    }
                    first = false;
                    if (value == null) {
                        out.writeNull();
                    } else {
                        Class<?> clazz = value.getClass();
                        if (clazz == preClazz) {
                            preWriter.write(serializer, value, entryKey, null);
                        } else {
                            preClazz = clazz;
                            preWriter = serializer.getObjectWriter(clazz);
                            preWriter.write(serializer, value, entryKey, null);
                        }
                    }
                }
            }
            serializer.decrementIdent();
            if (out.isEnabled(SerializerFeature.PrettyFormat) && map.size() > 0) {
                serializer.println();
            }
            out.write('}');
        } finally {
            serializer.setContext(parent);
        }
    }
}
