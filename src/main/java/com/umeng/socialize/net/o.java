package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SNSPair;
import com.umeng.socialize.bean.ShareType;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.ImageFormat;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ShareMultiReqeust */
public class o extends SocializeRequest {
    public static        boolean a = false;
    private static final String  b = "/share/multi_add/";
    private static final int     c = 17;
    private SocializeEntity d;
    private SNSPair[]       e;
    private UMShareMsg      f;

    public o(Context context, SocializeEntity socializeEntity, SNSPair[] sNSPairArr, UMShareMsg
            uMShareMsg) {
        super(context, "", p.class, socializeEntity, 17, RequestMethod.POST);
        this.mContext = context;
        this.d = socializeEntity;
        this.f = uMShareMsg;
        this.e = sNSPairArr;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        if (this.e != null && this.e.length > 0) {
            for (SNSPair sNSPair : this.e) {
                if (!TextUtils.isEmpty(sNSPair.mPaltform)) {
                    try {
                        jSONObject.put(sNSPair.mPaltform, TextUtils.isEmpty(sNSPair.mUsid) ? "" :
                                sNSPair.mUsid);
                    } catch (JSONException e) {
                    }
                }
            }
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("sns", jSONObject.toString());
            if (!TextUtils.isEmpty(this.f.mText)) {
                jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_TEXT, this.f.mText);
            }
            jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, SocializeUtils.getAppkey
                    (this.mContext));
            if (this.f.mLocation != null) {
                jSONObject2.put(SocializeProtocolConstants.PROTOCOL_KEY_LOCATION, this.f
                        .mLocation.toString());
            }
            jSONObject2.put("type", this.d.getShareType());
            CharSequence adapterSDKVersion = this.d.getAdapterSDKVersion();
            if (!TextUtils.isEmpty(adapterSDKVersion)) {
                jSONObject2.put(this.d.getAdapterSDK(), adapterSDKVersion);
            }
            this.d.setShareType(ShareType.NORMAL);
        } catch (Exception e2) {
        }
        Map<String, Object> packParamsMap = packParamsMap(TAG, addParamsToJson(jSONObject2, map)
                .toString());
        addMedia(this.f.getMedia(), packParamsMap);
        return packParamsMap;
    }

    protected String getPath() {
        return b + SocializeUtils.getAppkey(this.mContext) + "/" + this.d.mEntityKey + "/";
    }

    public Map<String, FilePair> getFilePair() {
        if (this.f == null || this.f.getMedia() == null || this.f.getMedia().isUrlMedia()) {
            return super.getFilePair();
        }
        Map<String, FilePair> filePair = super.getFilePair();
        if (this.f.getMedia() instanceof UMImage) {
            byte[] fileToByte = fileToByte(((UMImage) this.f.getMedia()).getImageCachePath());
            String checkFormat = ImageFormat.checkFormat(fileToByte);
            if (TextUtils.isEmpty(checkFormat)) {
                checkFormat = "png";
            }
            filePair.put(SocializeProtocolConstants.PROTOCOL_KEY_IMAGE, new FilePair((System
                    .currentTimeMillis() + "") + "." + checkFormat, fileToByte));
        }
        return filePair;
    }

    public void addMedia(UMediaObject uMediaObject, Map<String, Object> map) {
        SHARE_MEDIA convertToEmun;
        CharSequence appWebSite;
        byte[] toByte;
        Map toUrlExtraParams;
        Object title;
        CharSequence thumb;
        CharSequence charSequence;
        JSONObject jSONObject;
        Object obj;
        String str;
        String str2 = "";
        String str3 = "";
        String str4 = "";
        if (this.e != null) {
            if (this.e.length == 1) {
                convertToEmun = SHARE_MEDIA.convertToEmun(this.e[0].mPaltform);
            } else if (this.e.length > 1) {
                convertToEmun = SHARE_MEDIA.GENERIC;
            }
            appWebSite = SocializeEntity.getAppWebSite(convertToEmun);
            if (uMediaObject != null) {
                if (uMediaObject.isUrlMedia()) {
                    toByte = uMediaObject.toByte();
                    if (toByte != null) {
                        addFileBody(toByte, FILE_TYPE.IMAGE, null);
                    }
                } else {
                    toUrlExtraParams = uMediaObject.toUrlExtraParams();
                    if (toUrlExtraParams != null) {
                        map.putAll(toUrlExtraParams);
                    }
                }
                if (uMediaObject instanceof BaseMediaObject) {
                    BaseMediaObject baseMediaObject = (BaseMediaObject) uMediaObject;
                    title = baseMediaObject.getTitle();
                    thumb = baseMediaObject.getThumb();
                    if (TextUtils.isEmpty(SocializeEntity.mAppName) || this.mContext == null) {
                        charSequence = SocializeEntity.mAppName;
                    } else {
                        charSequence = this.mContext.getApplicationInfo().loadLabel(this.mContext
                                .getPackageManager());
                        if (TextUtils.isEmpty(charSequence)) {
                            Object obj2 = str4;
                        } else {
                            charSequence = charSequence.toString();
                            SocializeEntity.mAppName = charSequence;
                        }
                    }
                    jSONObject = new JSONObject();
                    if (!TextUtils.isEmpty(charSequence)) {
                        jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME,
                                charSequence);
                    }
                    if (!(TextUtils.isEmpty(title) || title.equals("未知"))) {
                        jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, title);
                    }
                    if (!TextUtils.isEmpty(thumb)) {
                        jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_THUMB, thumb);
                    }
                    if (!TextUtils.isEmpty(appWebSite)) {
                        jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_WEBSITE,
                                appWebSite);
                    }
                    map.put(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, jSONObject);
                }
            }
            obj = str3;
            str = str2;
            if (TextUtils.isEmpty(SocializeEntity.mAppName)) {
            }
            charSequence = SocializeEntity.mAppName;
            jSONObject = new JSONObject();
            if (TextUtils.isEmpty(charSequence)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME, charSequence);
            }
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, title);
            if (TextUtils.isEmpty(thumb)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_THUMB, thumb);
            }
            if (TextUtils.isEmpty(appWebSite)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_WEBSITE, appWebSite);
            }
            map.put(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, jSONObject);
        }
        convertToEmun = null;
        appWebSite = SocializeEntity.getAppWebSite(convertToEmun);
        if (uMediaObject != null) {
            if (uMediaObject.isUrlMedia()) {
                toByte = uMediaObject.toByte();
                if (toByte != null) {
                    addFileBody(toByte, FILE_TYPE.IMAGE, null);
                }
            } else {
                toUrlExtraParams = uMediaObject.toUrlExtraParams();
                if (toUrlExtraParams != null) {
                    map.putAll(toUrlExtraParams);
                }
            }
            if (uMediaObject instanceof BaseMediaObject) {
                BaseMediaObject baseMediaObject2 = (BaseMediaObject) uMediaObject;
                title = baseMediaObject2.getTitle();
                thumb = baseMediaObject2.getThumb();
                if (TextUtils.isEmpty(SocializeEntity.mAppName)) {
                }
                charSequence = SocializeEntity.mAppName;
                jSONObject = new JSONObject();
                if (TextUtils.isEmpty(charSequence)) {
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME, charSequence);
                }
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, title);
                if (TextUtils.isEmpty(thumb)) {
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_THUMB, thumb);
                }
                if (TextUtils.isEmpty(appWebSite)) {
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_WEBSITE, appWebSite);
                }
                map.put(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, jSONObject);
            }
        }
        obj = str3;
        str = str2;
        if (TextUtils.isEmpty(SocializeEntity.mAppName)) {
        }
        charSequence = SocializeEntity.mAppName;
        try {
            jSONObject = new JSONObject();
            if (TextUtils.isEmpty(charSequence)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_NAME, charSequence);
            }
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, title);
            if (TextUtils.isEmpty(thumb)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_THUMB, thumb);
            }
            if (TextUtils.isEmpty(appWebSite)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_APP_WEBSITE, appWebSite);
            }
            map.put(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
