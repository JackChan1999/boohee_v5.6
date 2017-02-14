package com.umeng.socialize.net;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.ImageFormat;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.base.SocializeRequest;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UploadImageRequest */
public class v extends SocializeRequest {
    private static final String a = "/api/upload_pic/";
    private static final int    b = 23;
    private Context      c;
    private String       d;
    private UMediaObject e;

    public v(Context context, SocializeEntity socializeEntity, UMediaObject uMediaObject, String
            str) {
        super(context, "", w.class, socializeEntity, 23, RequestMethod.POST);
        this.c = context;
        this.d = str;
        this.e = uMediaObject;
    }

    protected String getPath() {
        return a + SocializeUtils.getAppkey(this.c) + "/";
    }

    protected Map<String, Object> addSelfParams(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("usid", this.d);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> packParamsMap = packParamsMap(TAG, addParamsToJson(jSONObject, map)
                .toString());
        if (this.e != null) {
            if (this.e.isUrlMedia()) {
                addMedia(this.e, packParamsMap);
            } else if (this.e instanceof UMImage) {
                a(this.e, packParamsMap);
            }
        }
        return packParamsMap;
    }

    public Map<String, FilePair> getFilePair() {
        if (this.e == null || this.e.isUrlMedia()) {
            return super.getFilePair();
        }
        Map<String, FilePair> filePair = super.getFilePair();
        if (this.e instanceof UMImage) {
            byte[] fileToByte = fileToByte(((UMImage) this.e).getImageCachePath());
            String checkFormat = ImageFormat.checkFormat(fileToByte);
            if (TextUtils.isEmpty(checkFormat)) {
                checkFormat = "png";
            }
            filePair.put(SocializeProtocolConstants.PROTOCOL_KEY_IMAGE, new FilePair((System
                    .currentTimeMillis() + "") + "." + checkFormat, fileToByte));
        }
        return filePair;
    }

    private void a(UMediaObject uMediaObject, Map<String, Object> map) {
        try {
            if (uMediaObject instanceof BaseMediaObject) {
                BaseMediaObject baseMediaObject = (BaseMediaObject) uMediaObject;
                CharSequence title = baseMediaObject.getTitle();
                CharSequence thumb = baseMediaObject.getThumb();
                if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(thumb)) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_TITLE, title);
                    jSONObject.put(SocializeProtocolConstants.PROTOCOL_KEY_THUMB, thumb);
                    map.put(SocializeProtocolConstants.PROTOCOL_KEY_EXTEND, jSONObject);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "can`t add qzone title & thumb.", e);
        }
    }
}
