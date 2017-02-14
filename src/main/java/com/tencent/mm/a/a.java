package com.tencent.mm.a;

import android.util.Base64;

import javax.crypto.Cipher;

public final class a {
    private Cipher j;

    public final String h(String str) {
        try {
            return new String(this.j.doFinal(Base64.decode(str, 0)), "UTF8");
        } catch (Exception e) {
            return "[des]" + str + "|" + e.toString();
        }
    }
}
