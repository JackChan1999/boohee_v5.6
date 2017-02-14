package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DeserializeBeanInfo {
    private final Class<?> clazz;
    private Constructor<?> creatorConstructor;
    private Constructor<?> defaultConstructor;
    private Method factoryMethod;
    private final List<FieldInfo> fieldList = new ArrayList();
    private int parserFeatures = 0;
    private final List<FieldInfo> sortedFieldList = new ArrayList();

    public DeserializeBeanInfo(Class<?> clazz) {
        this.clazz = clazz;
        this.parserFeatures = TypeUtils.getParserFeatures(clazz);
    }

    public Constructor<?> getDefaultConstructor() {
        return this.defaultConstructor;
    }

    public void setDefaultConstructor(Constructor<?> defaultConstructor) {
        this.defaultConstructor = defaultConstructor;
    }

    public Constructor<?> getCreatorConstructor() {
        return this.creatorConstructor;
    }

    public void setCreatorConstructor(Constructor<?> createConstructor) {
        this.creatorConstructor = createConstructor;
    }

    public Method getFactoryMethod() {
        return this.factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public List<FieldInfo> getFieldList() {
        return this.fieldList;
    }

    public List<FieldInfo> getSortedFieldList() {
        return this.sortedFieldList;
    }

    public FieldInfo getField(String propertyName) {
        for (FieldInfo item : this.fieldList) {
            if (item.getName().equals(propertyName)) {
                return item;
            }
        }
        return null;
    }

    public boolean add(FieldInfo field) {
        for (FieldInfo item : this.fieldList) {
            if (item.getName().equals(field.getName()) && (!item.isGetOnly() || field.isGetOnly())) {
                return false;
            }
        }
        this.fieldList.add(field);
        this.sortedFieldList.add(field);
        Collections.sort(this.sortedFieldList);
        return true;
    }

    public static DeserializeBeanInfo computeSetters(Class<?> clazz, Type type) {
        JSONField fieldAnnotation;
        String propertyName;
        DeserializeBeanInfo deserializeBeanInfo = new DeserializeBeanInfo(clazz);
        Constructor<?> defaultConstructor = getDefaultConstructor(clazz);
        if (defaultConstructor != null) {
            TypeUtils.setAccessible(defaultConstructor);
            deserializeBeanInfo.setDefaultConstructor(defaultConstructor);
        } else if (!(defaultConstructor != null || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))) {
            Constructor<?> creatorConstructor = getCreatorConstructor(clazz);
            int i;
            if (creatorConstructor != null) {
                TypeUtils.setAccessible(creatorConstructor);
                deserializeBeanInfo.setCreatorConstructor(creatorConstructor);
                for (i = 0; i < creatorConstructor.getParameterTypes().length; i++) {
                    fieldAnnotation = null;
                    for (Annotation paramAnnotation : creatorConstructor.getParameterAnnotations()[i]) {
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation;
                            break;
                        }
                    }
                    if (fieldAnnotation == null) {
                        throw new JSONException("illegal json creator");
                    }
                    deserializeBeanInfo.add(new FieldInfo(fieldAnnotation.name(), (Class) clazz, creatorConstructor.getParameterTypes()[i], creatorConstructor.getGenericParameterTypes()[i], TypeUtils.getField(clazz, fieldAnnotation.name()), fieldAnnotation.ordinal(), SerializerFeature.of(fieldAnnotation.serialzeFeatures())));
                }
            } else {
                Method factoryMethod = getFactoryMethod(clazz);
                if (factoryMethod != null) {
                    TypeUtils.setAccessible(factoryMethod);
                    deserializeBeanInfo.setFactoryMethod(factoryMethod);
                    for (i = 0; i < factoryMethod.getParameterTypes().length; i++) {
                        fieldAnnotation = null;
                        for (Annotation paramAnnotation2 : factoryMethod.getParameterAnnotations()[i]) {
                            if (paramAnnotation2 instanceof JSONField) {
                                fieldAnnotation = (JSONField) paramAnnotation2;
                                break;
                            }
                        }
                        if (fieldAnnotation == null) {
                            throw new JSONException("illegal json creator");
                        }
                        deserializeBeanInfo.add(new FieldInfo(fieldAnnotation.name(), (Class) clazz, factoryMethod.getParameterTypes()[i], factoryMethod.getGenericParameterTypes()[i], TypeUtils.getField(clazz, fieldAnnotation.name()), fieldAnnotation.ordinal(), SerializerFeature.of(fieldAnnotation.serialzeFeatures())));
                    }
                } else {
                    throw new JSONException("default constructor not found. " + clazz);
                }
            }
            return deserializeBeanInfo;
        }
        for (Method method : clazz.getMethods()) {
            JSONField annotation;
            Field field;
            int i2 = 0;
            int i3 = 0;
            String methodName = method.getName();
            if (methodName.length() >= 4 && !Modifier.isStatic(method.getModifiers()) && ((method.getReturnType().equals(Void.TYPE) || method.getReturnType().equals(clazz)) && method.getParameterTypes().length == 1)) {
                annotation = (JSONField) method.getAnnotation(JSONField.class);
                if (annotation == null) {
                    annotation = TypeUtils.getSupperMethodAnnotation(clazz, method);
                }
                if (annotation != null) {
                    if (annotation.deserialize()) {
                        i2 = annotation.ordinal();
                        i3 = SerializerFeature.of(annotation.serialzeFeatures());
                        if (annotation.name().length() != 0) {
                            deserializeBeanInfo.add(new FieldInfo(annotation.name(), method, null, (Class) clazz, type, i2, i3));
                            TypeUtils.setAccessible(method);
                        }
                    }
                }
                if (methodName.startsWith("set")) {
                    String decapitalize;
                    char c3 = methodName.charAt(3);
                    if (Character.isUpperCase(c3)) {
                        if (TypeUtils.compatibleWithJavaBean) {
                            decapitalize = TypeUtils.decapitalize(methodName.substring(3));
                        } else {
                            decapitalize = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                        }
                    } else if (c3 == '_') {
                        decapitalize = methodName.substring(4);
                    } else if (c3 == 'f') {
                        decapitalize = methodName.substring(3);
                    } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                        decapitalize = TypeUtils.decapitalize(methodName.substring(3));
                    }
                    field = TypeUtils.getField(clazz, decapitalize);
                    if (field == null && method.getParameterTypes()[0] == Boolean.TYPE) {
                        field = TypeUtils.getField(clazz, "is" + Character.toUpperCase(decapitalize.charAt(0)) + decapitalize.substring(1));
                    }
                    if (field != null) {
                        fieldAnnotation = (JSONField) field.getAnnotation(JSONField.class);
                        if (fieldAnnotation != null) {
                            i2 = fieldAnnotation.ordinal();
                            i3 = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                            if (fieldAnnotation.name().length() != 0) {
                                deserializeBeanInfo.add(new FieldInfo(fieldAnnotation.name(), method, field, (Class) clazz, type, i2, i3));
                            }
                        }
                    }
                    deserializeBeanInfo.add(new FieldInfo(decapitalize, method, null, (Class) clazz, type, i2, i3));
                    TypeUtils.setAccessible(method);
                }
            }
        }
        for (Field field2 : clazz.getFields()) {
            if (!Modifier.isStatic(field2.getModifiers())) {
                boolean contains = false;
                for (FieldInfo item : deserializeBeanInfo.getFieldList()) {
                    if (item.getName().equals(field2.getName())) {
                        contains = true;
                    }
                }
                if (!contains) {
                    i2 = 0;
                    i3 = 0;
                    propertyName = field2.getName();
                    fieldAnnotation = (JSONField) field2.getAnnotation(JSONField.class);
                    if (fieldAnnotation != null) {
                        i2 = fieldAnnotation.ordinal();
                        i3 = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                        if (fieldAnnotation.name().length() != 0) {
                            propertyName = fieldAnnotation.name();
                        }
                    }
                    deserializeBeanInfo.add(new FieldInfo(propertyName, null, field2, (Class) clazz, type, i2, i3));
                }
            }
        }
        for (Method method2 : clazz.getMethods()) {
            methodName = method2.getName();
            if (methodName.length() >= 4 && !Modifier.isStatic(method2.getModifiers())) {
                if (methodName.startsWith("get") && Character.isUpperCase(methodName.charAt(3)) && method2.getParameterTypes().length == 0 && (Collection.class.isAssignableFrom(method2.getReturnType()) || Map.class.isAssignableFrom(method2.getReturnType()) || AtomicBoolean.class == method2.getReturnType() || AtomicInteger.class == method2.getReturnType() || AtomicLong.class == method2.getReturnType())) {
                    annotation = (JSONField) method2.getAnnotation(JSONField.class);
                    if (annotation == null || annotation.name().length() <= 0) {
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    } else {
                        propertyName = annotation.name();
                    }
                    if (deserializeBeanInfo.getField(propertyName) == null) {
                        deserializeBeanInfo.add(new FieldInfo(propertyName, method2, null, (Class) clazz, type));
                        TypeUtils.setAccessible(method2);
                    }
                }
            }
        }
        return deserializeBeanInfo;
    }

    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return null;
        }
        Constructor<?> defaultConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
                break;
            }
        }
        if (defaultConstructor != null || !clazz.isMemberClass() || Modifier.isStatic(clazz.getModifiers())) {
            return defaultConstructor;
        }
        for (Constructor<?> constructor2 : clazz.getDeclaredConstructors()) {
            if (constructor2.getParameterTypes().length == 1 && constructor2.getParameterTypes()[0].equals(clazz.getDeclaringClass())) {
                return constructor2;
            }
        }
        return defaultConstructor;
    }

    public static Constructor<?> getCreatorConstructor(Class<?> clazz) {
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        int length = declaredConstructors.length;
        int i = 0;
        while (i < length) {
            Constructor<?> constructor = declaredConstructors[i];
            if (((JSONCreator) constructor.getAnnotation(JSONCreator.class)) == null) {
                i++;
            } else if (null == null) {
                return constructor;
            } else {
                throw new JSONException("multi-json creator");
            }
        }
        return null;
    }

    public static Method getFactoryMethod(Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        int length = declaredMethods.length;
        int i = 0;
        while (i < length) {
            Method method = declaredMethods[i];
            if (!Modifier.isStatic(method.getModifiers()) || !clazz.isAssignableFrom(method.getReturnType()) || ((JSONCreator) method.getAnnotation(JSONCreator.class)) == null) {
                i++;
            } else if (null == null) {
                return method;
            } else {
                throw new JSONException("multi-json creator");
            }
        }
        return null;
    }

    public int getParserFeatures() {
        return this.parserFeatures;
    }
}
