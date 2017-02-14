package org.json.alipay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class a {
    private ArrayList a;

    public a() {
        this.a = new ArrayList();
    }

    public a(Object obj) {
        this();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                this.a.add(Array.get(obj, i));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }

    public a(String str) {
        this(new c(str));
    }

    public a(Collection collection) {
        this.a = collection == null ? new ArrayList() : new ArrayList(collection);
    }

    public a(c cVar) {
        this();
        char c = cVar.c();
        if (c == '[') {
            c = ']';
        } else if (c == '(') {
            c = ')';
        } else {
            throw cVar.a("A JSONArray text must start with '['");
        }
        if (cVar.c() != ']') {
            cVar.a();
            while (true) {
                if (cVar.c() == ',') {
                    cVar.a();
                    this.a.add(null);
                } else {
                    cVar.a();
                    this.a.add(cVar.d());
                }
                char c2 = cVar.c();
                switch (c2) {
                    case ')':
                    case ']':
                        if (c != c2) {
                            throw cVar.a("Expected a '" + new Character(c) + "'");
                        }
                        return;
                    case ',':
                    case ';':
                        if (cVar.c() != ']') {
                            cVar.a();
                        } else {
                            return;
                        }
                    default:
                        throw cVar.a("Expected a ',' or ']'");
                }
            }
        }
    }

    private String a(String str) {
        int size = this.a.size();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuffer.append(str);
            }
            stringBuffer.append(b.a(this.a.get(i)));
        }
        return stringBuffer.toString();
    }

    public final int a() {
        return this.a.size();
    }

    public final Object a(int i) {
        Object obj = (i < 0 || i >= this.a.size()) ? null : this.a.get(i);
        if (obj != null) {
            return obj;
        }
        throw new JSONException("JSONArray[" + i + "] not found.");
    }

    public String toString() {
        try {
            return "[" + a(",") + ']';
        } catch (Exception e) {
            return null;
        }
    }
}
