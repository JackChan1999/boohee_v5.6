package com.nineoldandroids.util;

import com.umeng.socialize.common.SocializeConstants;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectiveProperty<T, V> extends Property<T, V> {
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_IS  = "is";
    private static final String PREFIX_SET = "set";
    private Field  mField;
    private Method mGetter;
    private Method mSetter;

    public ReflectiveProperty(Class<T> propertyHolder, Class<V> valueType, String name) {
        super(valueType, name);
        char firstLetter = Character.toUpperCase(name.charAt(0));
        String capitalizedName = firstLetter + name.substring(1);
        String getterName = PREFIX_GET + capitalizedName;
        try {
            this.mGetter = propertyHolder.getMethod(getterName, (Class[]) null);
        } catch (NoSuchMethodException e) {
            try {
                this.mGetter = propertyHolder.getDeclaredMethod(getterName, (Class[]) null);
                this.mGetter.setAccessible(true);
            } catch (NoSuchMethodException e2) {
                getterName = PREFIX_IS + capitalizedName;
                try {
                    this.mGetter = propertyHolder.getMethod(getterName, (Class[]) null);
                } catch (NoSuchMethodException e3) {
                    try {
                        this.mGetter = propertyHolder.getDeclaredMethod(getterName, (Class[]) null);
                        this.mGetter.setAccessible(true);
                    } catch (NoSuchMethodException e4) {
                        this.mField = propertyHolder.getField(name);
                        Class fieldType = this.mField.getType();
                        if (!typesMatch(valueType, fieldType)) {
                            throw new NoSuchPropertyException("Underlying type (" + fieldType +
                                    ") " + "does not match Property type (" + valueType +
                                    SocializeConstants.OP_CLOSE_PAREN);
                        }
                        return;
                    } catch (NoSuchFieldException e5) {
                        throw new NoSuchPropertyException("No accessor method or field found for " +
                                "property with name " + name);
                    }
                }
            }
        }
        Class getterType = this.mGetter.getReturnType();
        if (typesMatch(valueType, getterType)) {
            String setterName = PREFIX_SET + capitalizedName;
            try {
                this.mSetter = propertyHolder.getDeclaredMethod(setterName, new
                        Class[]{getterType});
                this.mSetter.setAccessible(true);
                return;
            } catch (NoSuchMethodException e6) {
                return;
            }
        }
        throw new NoSuchPropertyException("Underlying type (" + getterType + ") " + "does not " +
                "match Property type (" + valueType + SocializeConstants.OP_CLOSE_PAREN);
    }

    private boolean typesMatch(Class<V> valueType, Class getterType) {
        if (getterType == valueType) {
            return true;
        }
        if (!getterType.isPrimitive()) {
            return false;
        }
        if ((getterType == Float.TYPE && valueType == Float.class) || ((getterType == Integer
                .TYPE && valueType == Integer.class) || ((getterType == Boolean.TYPE && valueType
                == Boolean.class) || ((getterType == Long.TYPE && valueType == Long.class) || (
                (getterType == Double.TYPE && valueType == Double.class) || ((getterType == Short
                        .TYPE && valueType == Short.class) || ((getterType == Byte.TYPE &&
                        valueType == Byte.class) || (getterType == Character.TYPE && valueType ==
                        Character.class)))))))) {
            return true;
        }
        return false;
    }

    public void set(T object, V value) {
        if (this.mSetter != null) {
            try {
                this.mSetter.invoke(object, new Object[]{value});
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        } else if (this.mField != null) {
            try {
                this.mField.set(object, value);
            } catch (IllegalAccessException e3) {
                throw new AssertionError();
            }
        } else {
            throw new UnsupportedOperationException("Property " + getName() + " is read-only");
        }
    }

    public V get(T object) {
        V invoke;
        if (this.mGetter != null) {
            try {
                invoke = this.mGetter.invoke(object, (Object[]) null);
            } catch (IllegalAccessException e) {
                throw new AssertionError();
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        } else if (this.mField != null) {
            try {
                invoke = this.mField.get(object);
            } catch (IllegalAccessException e3) {
                throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
        return invoke;
    }

    public boolean isReadOnly() {
        return this.mSetter == null && this.mField == null;
    }
}
