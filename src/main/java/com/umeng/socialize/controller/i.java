package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.d.a;
import com.umeng.socialize.controller.impl.m;

/* compiled from: UMSubServiceFactory */
enum i extends a {
    i(String str,
    int i
    )

    {
        super(str, i);
    }

    public Object a(SocializeEntity socializeEntity, Object... objArr) {
        return new m(socializeEntity);
    }

    protected Object b(SocializeEntity socializeEntity, Object... objArr) {
        return new m(socializeEntity);
    }
    }
