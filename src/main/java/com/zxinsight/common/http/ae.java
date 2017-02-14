package com.zxinsight.common.http;

import com.zxinsight.common.http.Request.HttpMethod;

public class ae extends Request {
    public ae(HttpMethod httpMethod, String str, u<String> uVar) {
        super(httpMethod, str, uVar);
        this.d = uVar;
    }

    public void a(byte[] bArr) {
        if (this.d != null) {
            this.d.a(b(bArr));
        }
    }
}
