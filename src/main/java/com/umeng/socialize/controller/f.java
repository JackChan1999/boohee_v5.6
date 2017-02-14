package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.d.a;

/* compiled from: UMSubServiceFactory */
enum f extends a {
    f(String str,
    int i
    )

    {
        super(str, i);
    }

    public Object a(SocializeEntity socializeEntity, Object... objArr) {
        return new com.umeng.socialize.controller.impl.a(socializeEntity);
    }

    protected Object b(SocializeEntity socializeEntity, Object... objArr) {
        return new com.umeng.socialize.controller.impl.a(socializeEntity);
    }
    }
