package com.tencent.stat.a;

import java.util.Arrays;
import java.util.Properties;

public class c {
    String   a;
    String[] b;
    Properties c = null;

    public c(String str, String[] strArr, Properties properties) {
        this.a = str;
        this.b = strArr;
        this.c = properties;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        boolean z = this.a.equals(cVar.a) && Arrays.equals(this.b, cVar.b);
        return this.c != null ? z && this.c.equals(cVar.c) : z && cVar.c == null;
    }

    public int hashCode() {
        int i = 0;
        if (this.a != null) {
            i = this.a.hashCode();
        }
        if (this.b != null) {
            i ^= Arrays.hashCode(this.b);
        }
        return this.c != null ? i ^ this.c.hashCode() : i;
    }

    public String toString() {
        String str = this.a;
        String str2 = "";
        if (this.b != null) {
            String str3 = this.b[0];
            for (int i = 1; i < this.b.length; i++) {
                str3 = str3 + "," + this.b[i];
            }
            str2 = "[" + str3 + "]";
        }
        if (this.c != null) {
            str2 = str2 + this.c.toString();
        }
        return str + str2;
    }
}
