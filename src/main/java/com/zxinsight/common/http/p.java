package com.zxinsight.common.http;

import com.zxinsight.common.util.i;

class p extends i<String, byte[]> {
    final /* synthetic */ o a;

    p(o oVar, int i) {
        this.a = oVar;
        super(i);
    }

    protected int a(String str, byte[] bArr) {
        return bArr.length / 1024;
    }
}
