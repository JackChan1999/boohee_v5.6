package com.umeng.socialize.view;

import com.umeng.socialize.bean.SnsPlatform;

import java.util.Comparator;

/* compiled from: LoginAgent */
class i implements Comparator<SnsPlatform> {
    final /* synthetic */ LoginAgent a;

    i(LoginAgent loginAgent) {
        this.a = loginAgent;
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((SnsPlatform) obj, (SnsPlatform) obj2);
    }

    public int a(SnsPlatform snsPlatform, SnsPlatform snsPlatform2) {
        return snsPlatform.mIndex - snsPlatform2.mIndex;
    }
}
