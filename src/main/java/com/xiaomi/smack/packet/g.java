package com.xiaomi.smack.packet;

import com.umeng.socialize.common.SocializeConstants;

public class g {
    private String a;

    public g(String str) {
        this.a = str;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("stream:error (").append(this.a).append(SocializeConstants
                .OP_CLOSE_PAREN);
        return stringBuilder.toString();
    }
}
