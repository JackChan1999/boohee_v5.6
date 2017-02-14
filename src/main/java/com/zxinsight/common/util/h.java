package com.zxinsight.common.util;

import com.alipay.sdk.sys.a;
import com.zxinsight.common.reflect.b;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class h {
    public static <T> T a(JSONObject jSONObject, Class<T> cls) {
        Object a = a((Class) cls);
        T newInstance = cls.newInstance();
        if (l.b(a)) {
            for (Field field : a) {
                field.setAccessible(true);
                String name = field.getName();
                if (field.getType() != null && l.b(name)) {
                    if (field.getType().equals(Long.class) || field.getType().equals(Long.TYPE)) {
                        if (l.b(jSONObject.optString(name))) {
                            field.set(newInstance, Long.valueOf(Long.parseLong(jSONObject
                                    .optString(name))));
                        }
                    } else if (field.getType().equals(String.class)) {
                        if (l.b(jSONObject.optString(name))) {
                            field.set(newInstance, jSONObject.optString(name));
                        }
                    } else if (field.getType().equals(Double.class) || field.getType().equals
                            (Double.TYPE)) {
                        field.set(newInstance, Double.valueOf(Double.parseDouble(jSONObject
                                .optString(name))));
                    } else if (field.getType().equals(Integer.class) || field.getType().equals
                            (Integer.TYPE)) {
                        field.set(newInstance, Integer.valueOf(Integer.parseInt(jSONObject
                                .optString(name))));
                    } else if (field.getType().equals(Date.class)) {
                        field.set(newInstance, Long.valueOf(Date.parse(jSONObject.optString(name)
                        )));
                    } else if (field.getType().equals(Map.class)) {
                        if (jSONObject.has(name)) {
                            field.set(newInstance, a(jSONObject.optJSONObject(name)));
                        }
                    } else if (field.getType().equals(List.class)) {
                        Class cls2;
                        Type genericType = field.getGenericType();
                        if (genericType instanceof ParameterizedType) {
                            Type[] actualTypeArguments = ((ParameterizedType) genericType)
                                    .getActualTypeArguments();
                            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                                cls2 = (Class) actualTypeArguments[0];
                                if (!(cls2 == null || jSONObject.optJSONArray(name) == null)) {
                                    field.set(newInstance, a(jSONObject.optJSONArray(name), cls2));
                                }
                            }
                        }
                        cls2 = null;
                        field.set(newInstance, a(jSONObject.optJSONArray(name), cls2));
                    } else if (jSONObject.optJSONObject(name) != null) {
                        field.set(newInstance, a(jSONObject.optJSONObject(name), field.getType()));
                    }
                }
            }
        }
        return newInstance;
    }

    public static Map<String, String> a(JSONObject jSONObject) {
        Map<String, String> map = null;
        if (jSONObject == null) {
            return null;
        }
        Map<String, String> hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            try {
                map = jSONObject.getString(str);
            } catch (JSONException e) {
            }
            hashMap.put(str, map);
        }
        return hashMap;
    }

    public static <T> List<T> a(JSONArray jSONArray, Class<T> cls) {
        List arrayList = new ArrayList();
        if (jSONArray != null) {
            int i = 0;
            while (i < jSONArray.length()) {
                try {
                    arrayList.add(a(jSONArray.getJSONObject(i), (Class) cls));
                    i++;
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (JSONException e3) {
                    e3.printStackTrace();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    public static String a(Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        if (obj == null) {
            stringBuilder.append("\"\"");
        } else if ((obj instanceof String) || (obj instanceof Integer) || (obj instanceof Float)
                || (obj instanceof Boolean) || (obj instanceof Short) || (obj instanceof Double)
                || (obj instanceof Long) || (obj instanceof BigDecimal) || (obj instanceof
                BigInteger) || (obj instanceof Byte)) {
            stringBuilder.append(a.e).append(a(obj.toString())).append(a.e);
        } else if (obj instanceof Object[]) {
            stringBuilder.append(a((Object[]) obj));
        } else if (obj instanceof List) {
            stringBuilder.append(a((List) obj));
        } else if (obj instanceof Map) {
            stringBuilder.append(a((Map) obj));
        } else {
            stringBuilder.append(b(obj));
        }
        return stringBuilder.toString();
    }

    private static String a(String str) {
        if (l.a(str)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case '\b':
                    stringBuilder.append("\\b");
                    break;
                case '\t':
                    stringBuilder.append("\\t");
                    break;
                case '\n':
                    stringBuilder.append("\\n");
                    break;
                case '\f':
                    stringBuilder.append("\\f");
                    break;
                case '\r':
                    stringBuilder.append("\\r");
                    break;
                case '\"':
                    stringBuilder.append("\\\"");
                    break;
                case '/':
                    stringBuilder.append("\\/");
                    break;
                case '\\':
                    stringBuilder.append("\\\\");
                    break;
                default:
                    if (charAt > '\u001f') {
                        stringBuilder.append(charAt);
                        break;
                    }
                    String toHexString = Integer.toHexString(charAt);
                    stringBuilder.append("\\u");
                    for (int i2 = 0; i2 < 4 - toHexString.length(); i2++) {
                        stringBuilder.append('0');
                    }
                    stringBuilder.append(toHexString.toUpperCase());
                    break;
            }
        }
        return stringBuilder.toString();
    }

    private static String a(Object[] objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        if (l.b(objArr)) {
            for (Object a : objArr) {
                stringBuilder.append(a(a));
                stringBuilder.append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, ']');
        } else {
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    private static String a(List<?> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        if (l.b(list)) {
            for (Object a : list) {
                stringBuilder.append(a(a));
                stringBuilder.append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, ']');
        } else {
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    private static String a(Map<?, ?> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        if (l.b(map)) {
            for (Object next : map.keySet()) {
                stringBuilder.append(a(next));
                stringBuilder.append(":");
                stringBuilder.append(a(map.get(next)));
                stringBuilder.append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '}');
        } else {
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    private static String b(Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        Object a = a(obj.getClass());
        if (l.b(a)) {
            for (Field field : a) {
                try {
                    String name = field.getName();
                    if (field.getType() != null && l.b(name)) {
                        if (field.getType().equals(Long.class) || field.getType().equals(Long
                                .TYPE)) {
                            long longValue = ((Long) b.a(obj).b(name)).longValue();
                            stringBuilder.append(a.e);
                            stringBuilder.append(name);
                            stringBuilder.append(a.e);
                            stringBuilder.append(":");
                            stringBuilder.append(longValue);
                            stringBuilder.append(",");
                        } else if (field.getType().equals(String.class)) {
                            r0 = (String) b.a(obj).b(name);
                            if (l.b(r0)) {
                                stringBuilder.append(a.e);
                                stringBuilder.append(name);
                                stringBuilder.append(a.e);
                                stringBuilder.append(":");
                                stringBuilder.append(a.e);
                                stringBuilder.append(r0);
                                stringBuilder.append(a.e);
                                stringBuilder.append(",");
                            }
                        } else if (field.getType().equals(Double.class) || field.getType().equals
                                (Double.TYPE)) {
                            double doubleValue = ((Double) b.a(obj).b(name)).doubleValue();
                            stringBuilder.append(a.e);
                            stringBuilder.append(name);
                            stringBuilder.append(a.e);
                            stringBuilder.append(":");
                            stringBuilder.append(doubleValue);
                            stringBuilder.append(",");
                        } else if (field.getType().equals(Integer.class) || field.getType()
                                .equals(Integer.TYPE)) {
                            int intValue = ((Integer) b.a(obj).b(name)).intValue();
                            stringBuilder.append(a.e);
                            stringBuilder.append(name);
                            stringBuilder.append(a.e);
                            stringBuilder.append(":");
                            stringBuilder.append(intValue);
                            stringBuilder.append(",");
                        } else if (!field.getType().equals(Date.class)) {
                            if (field.getType().equals(List.class)) {
                                stringBuilder.append(a.e);
                                stringBuilder.append(name);
                                stringBuilder.append(a.e);
                                stringBuilder.append(":");
                                stringBuilder.append(a((List) b.a(obj).b(name)));
                                stringBuilder.append(",");
                            } else if (field.getType().equals(Map.class)) {
                                stringBuilder.append(a.e);
                                stringBuilder.append(name);
                                stringBuilder.append(a.e);
                                stringBuilder.append(":");
                                stringBuilder.append(a((Map) b.a(obj).b(name)));
                                stringBuilder.append(",");
                            } else {
                                r0 = b(b.a(obj).b(name));
                                if (l.b(r0)) {
                                    stringBuilder.append(a.e);
                                    stringBuilder.append(name);
                                    stringBuilder.append(a.e);
                                    stringBuilder.append(":");
                                    stringBuilder.append(r0);
                                    stringBuilder.append(",");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '}');
        } else {
            stringBuilder.append("}");
        }
        return stringBuilder.toString();
    }

    private static Field[] a(Class<?> cls) {
        ArrayList arrayList = new ArrayList();
        Object declaredFields = cls.getDeclaredFields();
        if (l.b(declaredFields)) {
            arrayList.addAll(Arrays.asList(declaredFields));
        }
        for (Class superclass = cls.getSuperclass(); superclass != Object.class; superclass =
                superclass.getSuperclass()) {
            Object a = a(superclass);
            if (l.b(a)) {
                for (Field field : a) {
                    if (!a(arrayList, field)) {
                        arrayList.add(field);
                    }
                }
            }
        }
        Field[] fieldArr = new Field[arrayList.size()];
        arrayList.toArray(fieldArr);
        return fieldArr;
    }

    private static boolean a(ArrayList<Field> arrayList, Field field) {
        if (l.b(arrayList)) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((Field) it.next()).getName().equals(field.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean b(JSONObject jSONObject) {
        return jSONObject == null || jSONObject.length() == 0;
    }

    public static boolean c(JSONObject jSONObject) {
        return !b(jSONObject);
    }
}
