package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.d.a;

/* compiled from: UMSubServiceFactory */
enum g extends a {
    g(String str,
    int i
    )

    {
        super(str, i);
    }

    public Object a(SocializeEntity socializeEntity, Object... objArr) {
        return a("com.umeng.socialize.controller.impl.CommentServiceImpl", socializeEntity, objArr);
    }

    protected Object b(SocializeEntity socializeEntity, Object... objArr) {
        return new h(this);
    }
    }
