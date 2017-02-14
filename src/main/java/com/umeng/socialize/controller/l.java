package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.d.a;

/* compiled from: UMSubServiceFactory */
enum l extends a {
    l(String str,
    int i
    )

    {
        super(str, i);
    }

    public Object a(SocializeEntity socializeEntity, Object... objArr) {
        return a("com.umeng.socialize.controller.impl.UserCenterServiceImpl", socializeEntity,
                objArr);
    }

    protected Object b(SocializeEntity socializeEntity, Object... objArr) {
        String str = "init LikeService failed,please add SocialSDK_ucenter.jar file";
        return new m(this);
    }
    }
