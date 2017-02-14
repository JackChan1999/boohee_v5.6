package com.mob.tools.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hashon {
    private ArrayList<?> arrayToList(Object obj) {
        int i = 0;
        ArrayList<?> arrayList;
        int length;
        if (obj instanceof byte[]) {
            arrayList = new ArrayList();
            byte[] bArr = (byte[]) obj;
            length = bArr.length;
            while (i < length) {
                arrayList.add(Byte.valueOf(bArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof short[]) {
            arrayList = new ArrayList();
            short[] sArr = (short[]) obj;
            length = sArr.length;
            while (i < length) {
                arrayList.add(Short.valueOf(sArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof int[]) {
            arrayList = new ArrayList();
            int[] iArr = (int[]) obj;
            length = iArr.length;
            while (i < length) {
                arrayList.add(Integer.valueOf(iArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof long[]) {
            arrayList = new ArrayList();
            long[] jArr = (long[]) obj;
            length = jArr.length;
            while (i < length) {
                arrayList.add(Long.valueOf(jArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof float[]) {
            arrayList = new ArrayList();
            float[] fArr = (float[]) obj;
            length = fArr.length;
            while (i < length) {
                arrayList.add(Float.valueOf(fArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof double[]) {
            arrayList = new ArrayList();
            double[] dArr = (double[]) obj;
            length = dArr.length;
            while (i < length) {
                arrayList.add(Double.valueOf(dArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof char[]) {
            arrayList = new ArrayList();
            char[] cArr = (char[]) obj;
            length = cArr.length;
            while (i < length) {
                arrayList.add(Character.valueOf(cArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof boolean[]) {
            arrayList = new ArrayList();
            boolean[] zArr = (boolean[]) obj;
            length = zArr.length;
            while (i < length) {
                arrayList.add(Boolean.valueOf(zArr[i]));
                i++;
            }
            return arrayList;
        } else if (!(obj instanceof String[])) {
            return null;
        } else {
            arrayList = new ArrayList();
            String[] strArr = (String[]) obj;
            length = strArr.length;
            while (i < length) {
                arrayList.add(strArr[i]);
                i++;
            }
            return arrayList;
        }
    }

    private String format(String str, ArrayList<Object> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[\n");
        String str2 = str + "\t";
        Iterator it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Object next = it.next();
            if (i > 0) {
                stringBuffer.append(",\n");
            }
            stringBuffer.append(str2);
            if (next instanceof HashMap) {
                stringBuffer.append(format(str2, (HashMap) next));
            } else if (next instanceof ArrayList) {
                stringBuffer.append(format(str2, (ArrayList) next));
            } else if (next instanceof String) {
                stringBuffer.append('\"').append(next).append('\"');
            } else {
                stringBuffer.append(next);
            }
            i++;
        }
        stringBuffer.append('\n').append(str).append(']');
        return stringBuffer.toString();
    }

    private String format(String str, HashMap<String, Object> hashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{\n");
        String str2 = str + "\t";
        int i = 0;
        for (Entry entry : hashMap.entrySet()) {
            if (i > 0) {
                stringBuffer.append(",\n");
            }
            stringBuffer.append(str2).append('\"').append((String) entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                stringBuffer.append(format(str2, (HashMap) value));
            } else if (value instanceof ArrayList) {
                stringBuffer.append(format(str2, (ArrayList) value));
            } else if (value instanceof String) {
                stringBuffer.append('\"').append(value).append('\"');
            } else {
                stringBuffer.append(value);
            }
            i++;
        }
        stringBuffer.append('\n').append(str).append('}');
        return stringBuffer.toString();
    }

    private ArrayList<Object> fromJson(JSONArray jSONArray) throws JSONException {
        ArrayList<Object> arrayList = new ArrayList();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            Object opt = jSONArray.opt(i);
            if (opt instanceof JSONObject) {
                opt = fromJson((JSONObject) opt);
            } else if (opt instanceof JSONArray) {
                opt = fromJson((JSONArray) opt);
            }
            arrayList.add(opt);
        }
        return arrayList;
    }

    private HashMap<String, Object> fromJson(JSONObject jSONObject) throws JSONException {
        HashMap<String, Object> hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            Object opt = jSONObject.opt(str);
            if (JSONObject.NULL.equals(opt)) {
                opt = null;
            }
            if (opt != null) {
                if (opt instanceof JSONObject) {
                    opt = fromJson((JSONObject) opt);
                } else if (opt instanceof JSONArray) {
                    opt = fromJson((JSONArray) opt);
                }
                hashMap.put(str, opt);
            }
        }
        return hashMap;
    }

    private JSONArray getJSONArray(ArrayList<Object> arrayList) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof HashMap) {
                next = getJSONObject((HashMap) next);
            } else if (next instanceof ArrayList) {
                next = getJSONArray((ArrayList) next);
            }
            jSONArray.put(next);
        }
        return jSONArray;
    }

    private JSONObject getJSONObject(HashMap<String, Object> hashMap) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        for (Entry entry : hashMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                value = getJSONObject((HashMap) value);
            } else if (value instanceof ArrayList) {
                value = getJSONArray((ArrayList) value);
            } else if (isBasicArray(value)) {
                value = getJSONArray(arrayToList(value));
            }
            jSONObject.put((String) entry.getKey(), value);
        }
        return jSONObject;
    }

    private boolean isBasicArray(Object obj) {
        return (obj instanceof byte[]) || (obj instanceof short[]) || (obj instanceof int[]) ||
                (obj instanceof long[]) || (obj instanceof float[]) || (obj instanceof double[])
                || (obj instanceof char[]) || (obj instanceof boolean[]) || (obj instanceof
                String[]);
    }

    public String format(String str) {
        try {
            return format("", fromJson(str));
        } catch (Throwable th) {
            Ln.w(th);
            return "";
        }
    }

    public String fromHashMap(HashMap<String, Object> hashMap) {
        try {
            JSONObject jSONObject = getJSONObject(hashMap);
            return jSONObject == null ? "" : jSONObject.toString();
        } catch (Throwable th) {
            Ln.w(th);
            return "";
        }
    }

    public HashMap<String, Object> fromJson(String str) {
        if (TextUtils.isEmpty(str)) {
            return new HashMap();
        }
        try {
            if (str.startsWith("[") && str.endsWith("]")) {
                str = "{\"fakelist\":" + str + "}";
            }
            return fromJson(new JSONObject(str));
        } catch (Throwable th) {
            Ln.e(str, new Object[0]);
            Ln.w(th);
            return new HashMap();
        }
    }
}
