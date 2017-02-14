package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMShareMsg;
import com.umeng.socialize.common.ImageFormat;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.base.SocializeReseponse;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: SharePostRequest */
public class q extends SocializeRequest {
    private static final String a = "/share/add/";
    private static final int    b = 9;
    private String     c;
    private String     d;
    private UMShareMsg e;

    public q(Context context, SocializeEntity socializeEntity, String str, String str2,
             UMShareMsg uMShareMsg) {
        super(context, "", SocializeReseponse.class, socializeEntity, 9, RequestMethod.POST);
        this.mContext = context;
        this.mEntity = socializeEntity;
        this.c = str;
        this.d = str2;
        this.e = uMShareMsg;
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_SHARE_TO, this.c);
            if (!TextUtils.isEmpty(this.e.mText)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_TEXT, this.e.mText);
            }
            jSONObject.put("usid", this.d);
            jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, SocializeUtils.getAppkey
                    (this.mContext));
            if (!TextUtils.isEmpty(this.e.mWeiBoId)) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_WEIBOID, this.e.mWeiBoId);
            }
            if (this.e.mLocation != null) {
                jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_LOCATION, this.e.mLocation
                        .toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> packParamsMap = packParamsMap(TAG, addParamsToJson(jSONObject, map)
                .toString());
        if (this.e.getMedia() != null && this.e.getMedia().isUrlMedia()) {
            addMedia(this.e.getMedia(), packParamsMap);
        }
        return packParamsMap;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.mContext) + "/" + this.mEntity.mEntityKey + "/";
    }

    public Map<String, FilePair> getFilePair() {
        if (this.e == null || this.e.getMedia() == null || this.e.getMedia().isUrlMedia()) {
            return super.getFilePair();
        }
        Map<String, FilePair> filePair = super.getFilePair();
        if (this.e.getMedia() instanceof UMImage) {
            byte[] fileToByte = fileToByte(((UMImage) this.e.getMedia()).getImageCachePath());
            String checkFormat = ImageFormat.checkFormat(fileToByte);
            if (TextUtils.isEmpty(checkFormat)) {
                checkFormat = "png";
            }
            filePair.put(SocializeProtocolConstants.PROTOCOL_KEY_IMAGE, new FilePair((System
                    .currentTimeMillis() + "") + "." + checkFormat, fileToByte));
        }
        return filePair;
    }
}
