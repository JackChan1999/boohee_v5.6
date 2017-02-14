package com.boohee.cipher;

import android.content.Context;

public class BooheeCipher {
    public static native String qiniuRequestBody(String str);

    public static native void setModule(Context context, boolean z);

    static {
        System.loadLibrary("BooheeCipher");
    }
}
