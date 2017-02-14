package com.umeng.socialize.controller;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.impl.v;

public abstract class UMServiceFactory {
    @Deprecated
    public static UMSocialService getUMSocialService(String str, RequestType requestType) {
        if (requestType == null) {
            requestType = RequestType.SOCIAL;
        }
        String str2 = str + requestType;
        if (v.g.containsKey(str2)) {
            return new v((SocializeEntity) v.g.get(str2));
        }
        SocializeEntity cloneNew;
        str2 = str + RequestType.SOCIAL;
        if (requestType == RequestType.ANALYTICS && v.g.containsKey(str2)) {
            cloneNew = SocializeEntity.cloneNew((SocializeEntity) v.g.get(str2), requestType);
        } else {
            cloneNew = new SocializeEntity(str, requestType);
        }
        return new v(cloneNew);
    }

    public static UMSocialService getUMSocialService(String str) {
        return getUMSocialService(str, null);
    }
}
