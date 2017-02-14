package com.zxinsight.common.http;

import com.loopj.android.http.HttpDelete;
import com.tencent.connect.common.Constants;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Request implements Comparable<Request> {
    protected Priority a = Priority.NORMAL;
    protected int      b = 0;
    protected boolean  c = false;
    protected u d;
    private String              e = "";
    private HttpMethod          f = HttpMethod.GET;
    private Map<String, String> g = new HashMap();
    private JSONObject          h = new JSONObject();
    private boolean             i = false;
    private int                 j = 2;
    private boolean             k = false;
    private int                 l = ac.b;
    private int                 m = ac.a;

    public enum HttpMethod {
        GET("GET"),
        POST(Constants.HTTP_POST),
        PUT("PUT"),
        DELETE(HttpDelete.METHOD_NAME);

        private String mHttpMethod;

        private HttpMethod(String str) {
            this.mHttpMethod = "";
            this.mHttpMethod = str;
        }

        public String toString() {
            return this.mHttpMethod;
        }
    }

    public /* synthetic */ int compareTo(Object obj) {
        return a((Request) obj);
    }

    public String b() {
        return this.e;
    }

    public HttpMethod c() {
        return this.f;
    }

    public Priority a() {
        return this.a;
    }

    public int d() {
        return this.b;
    }

    public void a(int i) {
        this.b = i;
    }

    public Map<String, String> e() {
        return this.g;
    }

    public void a(JSONObject jSONObject) {
        this.h = jSONObject;
    }

    public JSONObject f() {
        return this.h;
    }

    public boolean g() {
        return this.i;
    }

    public boolean h() {
        return this.c;
    }

    public int i() {
        return this.j;
    }

    public void b(int i) {
        this.j = i;
    }

    public boolean j() {
        return this.k;
    }

    public void a(boolean z) {
        this.k = z;
    }

    public int k() {
        return this.l;
    }

    public void c(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("connect timeoutMillis < 0");
        }
        this.l = i;
    }

    public int l() {
        return this.m;
    }

    public void d(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("timeoutMillis < 0");
        }
        this.m = i;
    }

    public void a(String str, String str2) {
        this.g.put(str, str2);
    }

    public Request(HttpMethod httpMethod, String str, u uVar) {
        this.f = httpMethod;
        this.e = str;
        this.d = uVar;
    }

    public void a(byte[] bArr) {
        if (this.d != null) {
            this.d.a((Object) bArr);
        }
    }

    public void m() {
        if (this.d != null) {
            this.d.a(new Exception());
        }
    }

    public int a(Request request) {
        Priority a = a();
        Priority a2 = request.a();
        if (a.equals(a2)) {
            return d() - request.d();
        }
        return a.ordinal() - a2.ordinal();
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.i ? 1231 : 1237) + (((this.a == null ? 0 : this.a.hashCode()) + ((
                (this.h == null ? 0 : this.h.hashCode()) + (((this.f == null ? 0 : this.f
                        .hashCode()) + (((this.g == null ? 0 : this.g.hashCode()) + 31) * 31)) *
                        31)) * 31)) * 31)) * 31;
        if (this.e != null) {
            i = this.e.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Request request = (Request) obj;
        if (this.g == null) {
            if (request.g != null) {
                return false;
            }
        } else if (!this.g.equals(request.g)) {
            return false;
        }
        if (this.f != request.f) {
            return false;
        }
        if (this.h == null) {
            if (request.h != null) {
                return false;
            }
        } else if (!this.h.equals(request.h)) {
            return false;
        }
        if (this.a != request.a) {
            return false;
        }
        if (this.i != request.i) {
            return false;
        }
        if (this.e == null) {
            if (request.e != null) {
                return false;
            }
            return true;
        } else if (this.e.equals(request.e)) {
            return true;
        } else {
            return false;
        }
    }

    protected String b(byte[] bArr) {
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(bArr);
        }
    }
}
