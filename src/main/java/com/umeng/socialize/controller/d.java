package com.umeng.socialize.controller;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.utils.Log;

/* compiled from: UMSubServiceFactory */
public final class d {
    private static final String a = UMServiceFactory.class.getName();

    /* compiled from: UMSubServiceFactory */
    public enum a {
        AUTH,
        COMMENT,
        SHARE,
        LIKE,
        USER_CENTER;

        private static final String f = "com.umeng.socialize.controller.impl.CommentServiceImpl";
        private static final String g = "com.umeng.socialize.controller.impl.LikeServiceImpl";
        private static final String h = "com.umeng.socialize.controller.impl.UserCenterServiceImpl";

        public Object a(SocializeEntity socializeEntity, Object... objArr) {
            return null;
        }

        protected Object b(SocializeEntity socializeEntity, Object... objArr) {
            return null;
        }

        public Object a(String str, SocializeEntity socializeEntity, Object... objArr) {
            try {
                Class cls = Class.forName(str);
                if (this == USER_CENTER) {
                    return cls.getConstructor(new Class[]{SocializeEntity.class, AuthService
                            .class}).newInstance(new Object[]{socializeEntity, objArr[0]});
                }
                return cls.getConstructor(new Class[]{SocializeEntity.class}).newInstance(new
                        Object[]{socializeEntity});
            } catch (Exception e) {
                return b(socializeEntity, objArr);
            }
        }

        protected void a(String str) {
            Log.w(d.a, str);
        }
    }

    public static final Object a(SocializeEntity socializeEntity, a aVar, Object... objArr) {
        return aVar.a(socializeEntity, objArr);
    }
}
