package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanSerializer implements ObjectSerializer {
    private int features;
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;

    public FieldSerializer[] getGetters() {
        return this.getters;
    }

    public JavaBeanSerializer(Class<?> clazz) {
        this((Class) clazz, (Map) null);
    }

    public JavaBeanSerializer(Class<?> clazz, String... aliasList) {
        this((Class) clazz, createAliasMap(aliasList));
    }

    static Map<String, String> createAliasMap(String... aliasList) {
        Map<String, String> aliasMap = new HashMap();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }
        return aliasMap;
    }

    public JavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap) {
        this.features = 0;
        this.features = TypeUtils.getSerializeFeatures(clazz);
        List<FieldSerializer> getterList = new ArrayList();
        for (FieldInfo fieldInfo : TypeUtils.computeGetters(clazz, aliasMap, false)) {
            getterList.add(createFieldSerializer(fieldInfo));
        }
        this.getters = (FieldSerializer[]) getterList.toArray(new FieldSerializer[getterList.size()]);
        getterList = new ArrayList();
        for (FieldInfo fieldInfo2 : TypeUtils.computeGetters(clazz, aliasMap, true)) {
            getterList.add(createFieldSerializer(fieldInfo2));
        }
        this.sortedGetters = (FieldSerializer[]) getterList.toArray(new FieldSerializer[getterList.size()]);
    }

    protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName) {
        return serializer.isWriteClassName(fieldType, obj);
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            out.writeNull();
        } else if (!writeReference(serializer, object)) {
            FieldSerializer[] getters;
            if (out.isEnabled(SerializerFeature.SortField)) {
                getters = this.sortedGetters;
            } else {
                getters = this.getters;
            }
            SerialContext parent = serializer.getContext();
            serializer.setContext(parent, object, fieldName, this.features);
            boolean writeAsArray = isWriteAsArray(serializer);
            char startSeperator = writeAsArray ? '[' : '{';
            char endSeperator = writeAsArray ? ']' : '}';
            try {
                out.append(startSeperator);
                if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                    serializer.incrementIndent();
                    serializer.println();
                }
                boolean commaFlag = false;
                if (isWriteClassName(serializer, object, fieldType, fieldName) && object.getClass() != fieldType) {
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    serializer.write((Object) object.getClass());
                    commaFlag = true;
                }
                commaFlag = FilterUtils.writeBefore(serializer, object, commaFlag ? ',' : '\u0000') == ',';
                for (FieldSerializer fieldSerializer : getters) {
                    if (serializer.isEnabled(SerializerFeature.SkipTransientField)) {
                        Field field = fieldSerializer.getField();
                        if (field != null && Modifier.isTransient(field.getModifiers())) {
                        }
                    }
                    if (FilterUtils.applyName(serializer, object, fieldSerializer.getName())) {
                        Object propertyValue = fieldSerializer.getPropertyValue(object);
                        if (FilterUtils.apply(serializer, object, fieldSerializer.getName(), propertyValue)) {
                            String key = FilterUtils.processKey(serializer, object, fieldSerializer.getName(), propertyValue);
                            Object originalValue = propertyValue;
                            propertyValue = FilterUtils.processValue(serializer, object, fieldSerializer.getName(), propertyValue);
                            if (propertyValue != null || writeAsArray || fieldSerializer.isWriteNull() || serializer.isEnabled(SerializerFeature.WriteMapNullValue)) {
                                if (propertyValue != null && serializer.isEnabled(SerializerFeature.NotWriteDefaultValue)) {
                                    Class<?> fieldCLass = fieldSerializer.fieldInfo.getFieldClass();
                                    if (fieldCLass == Byte.TYPE) {
                                        if ((propertyValue instanceof Byte) && ((Byte) propertyValue).byteValue() == (byte) 0) {
                                        }
                                    }
                                    if (fieldCLass == Short.TYPE) {
                                        if ((propertyValue instanceof Short) && ((Short) propertyValue).shortValue() == (short) 0) {
                                        }
                                    }
                                    if (fieldCLass == Integer.TYPE) {
                                        if ((propertyValue instanceof Integer) && ((Integer) propertyValue).intValue() == 0) {
                                        }
                                    }
                                    if (fieldCLass == Long.TYPE) {
                                        if ((propertyValue instanceof Long) && ((Long) propertyValue).longValue() == 0) {
                                        }
                                    }
                                    if (fieldCLass == Float.TYPE) {
                                        if ((propertyValue instanceof Float) && ((Float) propertyValue).floatValue() == 0.0f) {
                                        }
                                    }
                                    if (fieldCLass == Double.TYPE) {
                                        if ((propertyValue instanceof Double) && ((Double) propertyValue).doubleValue() == 0.0d) {
                                        }
                                    }
                                    if (fieldCLass == Boolean.TYPE && (propertyValue instanceof Boolean) && !((Boolean) propertyValue).booleanValue()) {
                                    }
                                }
                                if (commaFlag) {
                                    out.append(',');
                                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                                        serializer.println();
                                    }
                                }
                                if (key != fieldSerializer.getName()) {
                                    if (!writeAsArray) {
                                        out.writeFieldName(key);
                                    }
                                    serializer.write(propertyValue);
                                } else if (originalValue != propertyValue) {
                                    if (!writeAsArray) {
                                        fieldSerializer.writePrefix(serializer);
                                    }
                                    serializer.write(propertyValue);
                                } else if (writeAsArray) {
                                    fieldSerializer.writeValue(serializer, propertyValue);
                                } else {
                                    fieldSerializer.writeProperty(serializer, propertyValue);
                                }
                                commaFlag = true;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                FilterUtils.writeAfter(serializer, object, commaFlag ? ',' : '\u0000');
                if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                    serializer.decrementIdent();
                    serializer.println();
                }
                out.append(endSeperator);
                serializer.setContext(parent);
            } catch (Exception e) {
                throw new JSONException("write javaBean error", e);
            } catch (Throwable th) {
                serializer.setContext(parent);
            }
        }
    }

    public boolean writeReference(JSONSerializer serializer, Object object) {
        SerialContext context = serializer.getContext();
        if ((context != null && context.isEnabled(SerializerFeature.DisableCircularReferenceDetect)) || !serializer.containsReference(object)) {
            return false;
        }
        serializer.writeReference(object);
        return true;
    }

    public FieldSerializer createFieldSerializer(FieldInfo fieldInfo) {
        if (fieldInfo.getFieldClass() == Number.class) {
            return new NumberFieldSerializer(fieldInfo);
        }
        return new ObjectFieldSerializer(fieldInfo);
    }

    public boolean isWriteAsArray(JSONSerializer serializer) {
        if (SerializerFeature.isEnabled(this.features, SerializerFeature.BeanToArray)) {
            return true;
        }
        if (serializer.isEnabled(SerializerFeature.BeanToArray)) {
            return true;
        }
        return false;
    }
}
