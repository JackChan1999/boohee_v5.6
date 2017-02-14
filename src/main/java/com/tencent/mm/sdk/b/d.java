package com.tencent.mm.sdk.b;

import com.tencent.mm.a.a;

public final class d {
    private final a                 E;
    private       c<String, String> F;

    public final String i(String str) {
        try {
            if (str.startsWith("!")) {
                if (this.F.a(str)) {
                    return (String) this.F.get(str);
                }
                String substring = str.substring(1);
                try {
                    String[] split = substring.split("@");
                    if (split.length > 1) {
                        String str2 = split[0];
                        int intValue = Integer.valueOf(split[0]).intValue();
                        String substring2 = substring.substring(str2.length() + 1, (str2.length()
                                + 1) + intValue);
                        String str3 = this.E.h(substring2) + substring.substring(intValue + (str2
                                .length() + 1));
                        this.F.put(str, str3);
                        return str3;
                    }
                    str = substring;
                } catch (Exception e) {
                    str = substring;
                    Exception exception = e;
                    exception.printStackTrace();
                    str = "[td]" + str;
                    return str;
                }
            }
        } catch (Exception e2) {
            exception = e2;
            exception.printStackTrace();
            str = "[td]" + str;
            return str;
        }
        return str;
    }
}
