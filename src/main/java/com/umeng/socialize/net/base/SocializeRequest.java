package com.umeng.socialize.net.base;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.ImageFormat;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.BaseMediaObject;
import com.umeng.socialize.media.UMediaObject;
import com.umeng.socialize.net.utils.AesHelper;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.umeng.socialize.net.utils.URequest;
import com.umeng.socialize.net.utils.URequest.FilePair;
import com.umeng.socialize.utils.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public abstract class SocializeRequest extends URequest {
    protected static String TAG = SocializeRequest.class.getName();
    protected Context         mContext;
    protected SocializeEntity mEntity;
    private Map<String, FilePair> mFileMap = new HashMap();
    private   RequestMethod                       mMethod;
    protected int                                 mOpId;
    protected Class<? extends SocializeReseponse> mResponseClz;

    protected enum FILE_TYPE {
        IMAGE,
        VEDIO
    }

    protected enum RequestMethod {
        GET {
            public String toString() {
                return SocializeRequest.GET;
            }
        },
        POST {
            public String toString() {
                return SocializeRequest.POST;
            }
        };
    }

    protected abstract Map<String, Object> addSelfParams(Map<String, Object> map);

    protected abstract String getPath();

    public SocializeRequest(Context context, String str, Class<? extends SocializeReseponse> cls,
                            SocializeEntity socializeEntity, int i, RequestMethod requestMethod) {
        super("");
        this.mResponseClz = cls;
        this.mOpId = i;
        this.mContext = context;
        this.mMethod = requestMethod;
        this.mEntity = socializeEntity;
        TAG = getClass().getName();
    }

    public boolean addFileBody(byte[] bArr, FILE_TYPE file_type, String str) {
        if (FILE_TYPE.IMAGE != file_type) {
            return false;
        }
        String checkFormat = ImageFormat.checkFormat(bArr);
        if (TextUtils.isEmpty(checkFormat)) {
            checkFormat = "png";
        }
        if (TextUtils.isEmpty(str)) {
            str = System.currentTimeMillis() + "";
        }
        this.mFileMap.put(SocializeProtocolConstants.PROTOCOL_KEY_IMAGE, new FilePair(str + "." +
                checkFormat, bArr));
        return true;
    }

    public Map<String, Object> getBodyPair() {
        return addSelfParams(SocializeNetUtils.getBaseQuery(this.mContext, this.mEntity, this
                .mOpId));
    }

    public Map<String, FilePair> getFilePair() {
        return this.mFileMap;
    }

    public void addMedia(UMediaObject uMediaObject, Map<String, Object> map) {
        if (uMediaObject != null) {
            if (uMediaObject.isUrlMedia()) {
                map.putAll(uMediaObject.toUrlExtraParams());
            } else {
                byte[] toByte = uMediaObject.toByte();
                if (toByte != null) {
                    addFileBody(toByte, FILE_TYPE.IMAGE, null);
                }
            }
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

    protected byte[] fileToByte(String str) {
        FileInputStream fileInputStream;
        Exception e;
        Throwable th;
        byte[] bArr = null;
        try {
            fileInputStream = new FileInputStream(str);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream
                        (fileInputStream.available());
                byte[] bArr2 = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr2);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr2, 0, read);
                }
                bArr = byteArrayOutputStream.toByteArray();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return bArr;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = bArr;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return bArr;
        } catch (Throwable th3) {
            fileInputStream = bArr;
            th = th3;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
        return bArr;
    }

    public JSONObject toJson() {
        return null;
    }

    public String toGetUrl() {
        return SocializeNetUtils.generateGetURL(getBaseUrl(), addSelfParams(SocializeNetUtils
                .getBaseQuery(this.mContext, this.mEntity, this.mOpId)));
    }

    public void setBaseUrl(String str) {
        try {
            super.setBaseUrl(new URL(new URL(str), getPath()).toString());
        } catch (Throwable e) {
            throw new SocializeException("Can not generate correct url in SocializeRequest [" +
                    getBaseUrl() + "]", e);
        }
    }

    protected Map<String, Object> packParamsMap(String str, String str2) {
        Map<String, Object> hashMap = new HashMap();
        try {
            String encryptNoPadding = AesHelper.encryptNoPadding(str2, "UTF-8");
            if (this.mMethod == RequestMethod.POST) {
                hashMap.put("ud_post", encryptNoPadding);
            }
        } catch (Exception e) {
            Log.d(str, str + "数据加密失败");
            e.printStackTrace();
        }
        return hashMap;
    }

    protected JSONObject addParamsToJson(JSONObject jSONObject, Map<String, Object> map) {
        if (!(map == null || jSONObject == null || map.size() == 0)) {
            for (Entry entry : map.entrySet()) {
                try {
                    jSONObject.put((String) entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONObject;
    }

    protected String getHttpMethod() {
        switch (this.mMethod) {
            case POST:
                return POST;
            default:
                return GET;
        }
    }
}
