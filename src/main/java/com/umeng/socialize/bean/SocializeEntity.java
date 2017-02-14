package com.umeng.socialize.bean;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.umeng.socialize.controller.impl.v;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.SimpleShareContent;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.utils.StatisticsDataUtils;

import java.util.HashMap;
import java.util.Map;

public class SocializeEntity {
    public static  String                   mAppName = "";
    private static Map<SHARE_MEDIA, String> n        = new HashMap();
    private int        a;
    private int        b;
    private int        c;
    private int        d;
    private boolean    e;
    private String     f;
    private LIKESTATUS g;
    private Map<SHARE_MEDIA, UMediaObject> h = new HashMap();
    private String i;
    private SocializeConfig j = null;
    private RequestType k;
    private UMShareMsg l = null;
    private ShareType  m = ShareType.NORMAL;
    public String mCustomID;
    public String mDescriptor;
    public  String     mEntityKey   = "-1";
    public  boolean    mInitialized = false;
    public  String     mSessionID   = "";
    public  SnsAccount mSnsAccount  = null;
    private boolean    o            = false;
    private Bundle     p            = new Bundle();
    private String     q            = "";
    private String     r            = "";

    public SocializeEntity(String str, RequestType requestType) {
        this.mDescriptor = str;
        this.k = requestType;
        v.g.put(str + requestType, this);
    }

    public UMediaObject getMedia() {
        return getMedia(a());
    }

    public UMediaObject getMedia(SHARE_MEDIA share_media) {
        UMediaObject uMediaObject = (UMediaObject) this.h.get(share_media);
        if (uMediaObject == null) {
            return (UMediaObject) this.h.get(SHARE_MEDIA.GENERIC);
        }
        return uMediaObject;
    }

    public <T extends BaseMediaObject> T getMedia(Class<T> cls) {
        UMediaObject uMediaObject = (UMediaObject) this.h.get(a());
        if (uMediaObject == null || cls == null || !uMediaObject.getClass().equals(cls)) {
            return null;
        }
        return (BaseMediaObject) uMediaObject;
    }

    public void setMedia(UMediaObject uMediaObject) {
        Object obj = SHARE_MEDIA.GENERIC;
        if (uMediaObject != null) {
            obj = uMediaObject.getTargetPlatform();
        }
        if (this.h.containsKey(obj)) {
            this.h.remove(obj);
        }
        this.h.put(obj, uMediaObject);
    }

    public String getNickName() {
        return this.f;
    }

    public boolean isNew() {
        return this.e;
    }

    public void setNew(boolean z) {
        this.e = z;
    }

    public void setNickName(String str) {
        this.f = str;
    }

    public String getShareContent() {
        String str = "";
        UMediaObject uMediaObject = (UMediaObject) this.h.get(a());
        if (!(uMediaObject instanceof SimpleShareContent)) {
            return this.i;
        }
        Object shareContent = ((SimpleShareContent) uMediaObject).getShareContent();
        if (TextUtils.isEmpty(shareContent)) {
            return str;
        }
        return shareContent;
    }

    public void setShareContent(String str) {
        this.i = str;
    }

    public String getExtra(String str) {
        if (this.p.containsKey(str)) {
            return this.p.getString(str);
        }
        return "";
    }

    public void putExtra(String str, String str2) {
        this.p.putString(str, str2);
    }

    public int getPv() {
        return this.a;
    }

    public void setPv(int i) {
        this.a = i;
    }

    public int getCommentCount() {
        return this.b;
    }

    public void setCommentCount(int i) {
        this.b = i;
    }

    public int getLikeCount() {
        return this.c;
    }

    public void setLikeCount(int i) {
        this.c = i;
    }

    public int getShareCount() {
        return this.d;
    }

    public void setShareCount(int i) {
        this.d = i;
    }

    public LIKESTATUS getLikeStatus() {
        return this.g;
    }

    public ShareType getShareType() {
        return this.m;
    }

    public void setShareType(ShareType shareType) {
        this.m = shareType;
    }

    public void setIlikey(LIKESTATUS likestatus) {
        this.g = likestatus;
    }

    public synchronized void changeILike() {
        if (this.g == LIKESTATUS.LIKE) {
            this.c--;
            this.g = LIKESTATUS.UNLIKE;
        } else {
            this.c++;
            this.g = LIKESTATUS.LIKE;
        }
    }

    public synchronized void incrementSc() {
        this.d++;
    }

    public synchronized void incrementCc() {
        this.b++;
    }

    public void setConfig(SocializeConfig socializeConfig) {
        this.j = socializeConfig;
    }

    public SocializeConfig getEntityConfig() {
        return this.j;
    }

    public RequestType getRequestType() {
        return this.k;
    }

    public static String buildPoolKey(String str, RequestType requestType) {
        return str + requestType.toString();
    }

    public static SocializeEntity cloneNew(SocializeEntity socializeEntity, RequestType
            requestType) {
        SocializeEntity socializeEntity2 = new SocializeEntity(socializeEntity.mDescriptor,
                requestType);
        socializeEntity2.mEntityKey = socializeEntity.mEntityKey;
        socializeEntity2.mSessionID = socializeEntity.mSessionID;
        socializeEntity2.mCustomID = socializeEntity.mCustomID;
        socializeEntity2.a = socializeEntity.a;
        socializeEntity2.b = socializeEntity.a;
        socializeEntity2.c = socializeEntity.c;
        socializeEntity2.d = socializeEntity.d;
        socializeEntity2.e = socializeEntity.e;
        socializeEntity2.f = socializeEntity.f;
        socializeEntity2.g = socializeEntity.g;
        socializeEntity2.mInitialized = socializeEntity.mInitialized;
        return socializeEntity2;
    }

    public UMShareMsg getShareMsg() {
        return this.l;
    }

    public void setShareMsg(UMShareMsg uMShareMsg) {
        this.l = uMShareMsg;
    }

    public Map<SHARE_MEDIA, StringBuilder> getStatisticsData() {
        try {
            return StatisticsDataUtils.getStatisticsData();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    public Map<String, Integer> getOauthStatisticsData() {
        try {
            return StatisticsDataUtils.getOauthStatisticsData();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    public Map<String, Integer> getSharkStatisticsData(Context context) {
        try {
            return StatisticsDataUtils.getSharkStatisticsData(context);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    public void addShakeStatisticsData(Context context) {
        try {
            StatisticsDataUtils.saveSharkStatisticsData(context);
        } catch (Exception e) {
        }
    }

    public void addStatisticsData(Context context, SHARE_MEDIA share_media, int i) {
        try {
            StatisticsDataUtils.addStatisticsData(context, share_media, i);
        } catch (Exception e) {
        }
    }

    public void addOauthData(Context context, SHARE_MEDIA share_media, int i) {
        try {
            StatisticsDataUtils.addOauthData(context, share_media, i);
        } catch (Exception e) {
        }
    }

    public void cleanStatisticsData(Context context, boolean z) {
        try {
            StatisticsDataUtils.cleanStatisticsData(context, z);
        } catch (Exception e) {
        }
    }

    private SHARE_MEDIA a() {
        return SocializeConfig.getSelectedPlatfrom();
    }

    public static String getAppWebSite(SHARE_MEDIA share_media) {
        String str = (String) n.get(share_media);
        return !TextUtils.isEmpty(str) ? str : (String) n.get(SHARE_MEDIA.GENERIC);
    }

    public static void setAppWebSite(SHARE_MEDIA share_media, String str) {
        n.put(share_media, str);
    }

    public boolean isFireCallback() {
        return this.o;
    }

    public void setFireCallback(boolean z) {
        this.o = z;
    }

    public String getAdapterSDKVersion() {
        return this.r;
    }

    public String getAdapterSDK() {
        return this.q;
    }

    public void setAdapterSDKInfo(String str, String str2) {
        this.q = str;
        this.r = str2;
    }
}
