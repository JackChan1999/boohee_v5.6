package com.zxinsight.common.http;

import com.zxinsight.common.http.Request.HttpMethod;

/* synthetic */ class s {
    static final /* synthetic */ int[] a = new int[HttpMethod.values().length];

    static {
        try {
            a[HttpMethod.GET.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[HttpMethod.POST.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
    }
}
