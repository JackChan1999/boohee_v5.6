package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ArrayDeserializer implements ObjectDeserializer {
    public static final ArrayDeserializer instance = new ArrayDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        } else if (lexer.token() == 4) {
            T bytes = lexer.bytesValue();
            lexer.nextToken(16);
            return bytes;
        } else {
            Class componentClass;
            if (type instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) type).getGenericComponentType();
                if (componentType instanceof TypeVariable) {
                    TypeVariable typeVar = (TypeVariable) componentType;
                    Type objType = parser.getContext().getType();
                    if (objType instanceof ParameterizedType) {
                        ParameterizedType objParamType = (ParameterizedType) objType;
                        Type objRawType = objParamType.getRawType();
                        Type actualType = null;
                        if (objRawType instanceof Class) {
                            TypeVariable[] objTypeParams = ((Class) objRawType).getTypeParameters();
                            for (int i = 0; i < objTypeParams.length; i++) {
                                if (objTypeParams[i].getName().equals(typeVar.getName())) {
                                    actualType = objParamType.getActualTypeArguments()[i];
                                }
                            }
                        }
                        if (actualType instanceof Class) {
                            componentClass = (Class) actualType;
                        } else {
                            componentClass = Object.class;
                        }
                    } else {
                        componentClass = Object.class;
                    }
                } else {
                    componentClass = (Class) componentType;
                }
            } else {
                componentClass = ((Class) type).getComponentType();
                Class cls = componentClass;
            }
            JSONArray array = new JSONArray();
            parser.parseArray(componentClass, array, fieldName);
            return toObjectArray(parser, componentClass, array);
        }
    }

    private <T> T toObjectArray(DefaultJSONParser parser, Class<?> componentType, JSONArray array) {
        if (array == null) {
            return null;
        }
        int size = array.size();
        T objArray = Array.newInstance(componentType, size);
        for (int i = 0; i < size; i++) {
            Object value = array.get(i);
            if (value == array) {
                Array.set(objArray, i, objArray);
            } else if (componentType.isArray()) {
                if (componentType.isInstance(value)) {
                    element = value;
                } else {
                    element = toObjectArray(parser, componentType, (JSONArray) value);
                }
                Array.set(objArray, i, element);
            } else {
                element = null;
                if (value instanceof JSONArray) {
                    boolean contains = false;
                    JSONArray valueArray = (JSONArray) value;
                    int valueArraySize = valueArray.size();
                    for (int y = 0; y < valueArraySize; y++) {
                        if (valueArray.get(y) == array) {
                            valueArray.set(i, objArray);
                            contains = true;
                        }
                    }
                    if (contains) {
                        element = valueArray.toArray();
                    }
                }
                if (element == null) {
                    element = TypeUtils.cast(value, (Class) componentType, parser.getConfig());
                }
                Array.set(objArray, i, element);
            }
        }
        array.setRelatedArray(objArray);
        array.setComponentType(componentType);
        return objArray;
    }

    public int getFastMatchToken() {
        return 14;
    }
}
