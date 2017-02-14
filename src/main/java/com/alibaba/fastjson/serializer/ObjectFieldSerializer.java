package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.util.Collection;

public class ObjectFieldSerializer extends FieldSerializer {
    private String format;
    private RuntimeSerializerInfo runtimeInfo;
    boolean writeEnumUsingToString = false;
    boolean writeNullBooleanAsFalse = false;
    boolean writeNullListAsEmpty = false;
    boolean writeNullStringAsEmpty = false;
    boolean writeNumberAsZero = false;

    static class RuntimeSerializerInfo {
        ObjectSerializer fieldSerializer;
        Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass) {
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }

    public ObjectFieldSerializer(FieldInfo fieldInfo) {
        int i = 0;
        super(fieldInfo);
        JSONField annotation = (JSONField) fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            this.format = annotation.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            SerializerFeature[] serialzeFeatures = annotation.serialzeFeatures();
            int length = serialzeFeatures.length;
            while (i < length) {
                SerializerFeature feature = serialzeFeatures[i];
                if (feature == SerializerFeature.WriteNullNumberAsZero) {
                    this.writeNumberAsZero = true;
                } else if (feature == SerializerFeature.WriteNullStringAsEmpty) {
                    this.writeNullStringAsEmpty = true;
                } else if (feature == SerializerFeature.WriteNullBooleanAsFalse) {
                    this.writeNullBooleanAsFalse = true;
                } else if (feature == SerializerFeature.WriteNullListAsEmpty) {
                    this.writeNullListAsEmpty = true;
                } else if (feature == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                }
                i++;
            }
        }
    }

    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        writePrefix(serializer);
        writeValue(serializer, propertyValue);
    }

    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (this.format != null) {
            serializer.writeWithFormat(propertyValue, this.format);
            return;
        }
        if (this.runtimeInfo == null) {
            Class<?> runtimeFieldClass;
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.getFieldClass();
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }
            this.runtimeInfo = new RuntimeSerializerInfo(serializer.getObjectWriter(runtimeFieldClass), runtimeFieldClass);
        }
        RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        if (propertyValue == null) {
            if (this.writeNumberAsZero && Number.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write('0');
            } else if (this.writeNullStringAsEmpty && String.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write("\"\"");
            } else if (this.writeNullBooleanAsFalse && Boolean.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write("false");
            } else if (this.writeNullListAsEmpty && Collection.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write("[]");
            } else {
                runtimeInfo.fieldSerializer.write(serializer, null, this.fieldInfo.getName(), null);
            }
        } else if (this.writeEnumUsingToString && runtimeInfo.runtimeFieldClass.isEnum()) {
            serializer.getWriter().writeString(((Enum) propertyValue).name());
        } else {
            Class<?> valueClass = propertyValue.getClass();
            if (valueClass == runtimeInfo.runtimeFieldClass) {
                runtimeInfo.fieldSerializer.write(serializer, propertyValue, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
            } else {
                serializer.getObjectWriter(valueClass).write(serializer, propertyValue, this.fieldInfo.getName(), this.fieldInfo.getFieldType());
            }
        }
    }
}
