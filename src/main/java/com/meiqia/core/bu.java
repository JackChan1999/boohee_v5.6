package com.meiqia.core;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alipay.sdk.cons.b;
import com.boohee.utils.Utils;
import com.meiqia.core.b.a;
import com.meiqia.core.b.c;
import com.meiqia.core.b.e;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnFailureCallBack;
import com.meiqia.core.callback.OnGetClientCallback;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.core.callback.OnProgressCallback;
import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;
import com.meiqia.core.callback.SimpleCallback;
import com.meiqia.meiqiasdk.util.ErrorCode;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xiaomi.account.openauth.utils.Network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class bu {
    public static final MediaType a = MediaType.parse("application/json; charset=utf-8");
    private static bu b;
    private static final OkHttpClient c = new OkHttpClient();
    private              Handler      d = new Handler(Looper.getMainLooper());

    static {
        c.setConnectTimeout(30, TimeUnit.SECONDS);
    }

    private bu() {
    }

    public static bu a() {
        if (b == null) {
            synchronized (bu.class) {
                if (b == null) {
                    b = new bu();
                }
            }
        }
        return b;
    }

    private String a(Map<String, Object> map) {
        return a.a(b.a != null ? b.a.getAESKey() : "", c.a((Map) map).toString());
    }

    private void a(Request request, cw cwVar, OnFailureCallBack onFailureCallBack) {
        c.newCall(request).enqueue(new cj(this, onFailureCallBack, cwVar));
    }

    private void a(File file, Response response, OnProgressCallback onProgressCallback) {
        IOException e;
        Throwable th;
        InputStream inputStream = null;
        byte[] bArr = new byte[2048];
        FileOutputStream fileOutputStream;
        try {
            InputStream byteStream = response.body().byteStream();
            try {
                long contentLength = response.body().contentLength();
                long j = 0;
                fileOutputStream = new FileOutputStream(file);
                while (true) {
                    try {
                        int read = byteStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        j += (long) read;
                        fileOutputStream.write(bArr, 0, read);
                        if (onProgressCallback != null) {
                            read = (int) (((((float) j) * 1.0f) / ((float) contentLength)) * 100
                            .0f);
                            onProgressCallback.onProgress(read);
                            if (read == 100) {
                                onProgressCallback.onSuccess();
                            }
                        }
                    } catch (IOException e2) {
                        e = e2;
                        inputStream = byteStream;
                    } catch (Throwable th2) {
                        th = th2;
                        inputStream = byteStream;
                    }
                }
                fileOutputStream.flush();
                if (byteStream != null) {
                    try {
                        byteStream.close();
                    } catch (IOException e3) {
                        if (onProgressCallback != null) {
                            onProgressCallback.onFailure(20000, "download file failed");
                        }
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e4) {
                        if (onProgressCallback != null) {
                            onProgressCallback.onFailure(20000, "download file failed");
                        }
                    }
                }
            } catch (IOException e5) {
                e = e5;
                fileOutputStream = null;
                inputStream = byteStream;
                if (onProgressCallback != null) {
                    try {
                        if (!TextUtils.isEmpty(e.getMessage())) {
                        }
                        onProgressCallback.onFailure(20000, "download file failed");
                    } catch (Throwable th3) {
                        th = th3;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e6) {
                                if (onProgressCallback != null) {
                                    onProgressCallback.onFailure(20000, "download file failed");
                                }
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e7) {
                                if (onProgressCallback != null) {
                                    onProgressCallback.onFailure(20000, "download file failed");
                                }
                            }
                        }
                        throw th;
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e8) {
                        if (onProgressCallback != null) {
                            onProgressCallback.onFailure(20000, "download file failed");
                        }
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e9) {
                        if (onProgressCallback != null) {
                            onProgressCallback.onFailure(20000, "download file failed");
                        }
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                inputStream = byteStream;
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (IOException e10) {
            e = e10;
            fileOutputStream = null;
            if (onProgressCallback != null) {
                if (TextUtils.isEmpty(e.getMessage()) || !e.getMessage().toLowerCase().contains
                        ("cancel")) {
                    onProgressCallback.onFailure(20000, "download file failed");
                } else {
                    onProgressCallback.onFailure(ErrorCode.DOWNLOAD_IS_CANCEL,
                            "download_is_cancel");
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Throwable th5) {
            th = th5;
            fileOutputStream = null;
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    private void a(String str, Map<String, Object> map, cw cwVar, OnFailureCallBack
            onFailureCallBack) {
        a(true, str, (Map) map, cwVar, onFailureCallBack);
    }

    private void a(boolean z, String str, String str2, Map<String, Object> map, cw cwVar,
                   OnFailureCallBack onFailureCallBack) {
        try {
            RequestBody create;
            Builder c = c(str);
            if (z) {
                create = RequestBody.create(a, a((Map) map));
            } else {
                create = RequestBody.create(a, c.a((Map) map).toString());
                c.removeHeader("Authorization");
            }
            c.url(str2).post(create);
            a(c.build(), cwVar, onFailureCallBack);
        } catch (GeneralSecurityException e) {
            if (onFailureCallBack != null) {
                this.d.post(new bv(this, onFailureCallBack));
            }
        }
    }

    private void a(boolean z, String str, Map<String, Object> map, cw cwVar, OnFailureCallBack
            onFailureCallBack) {
        a(z, b(), str, map, cwVar, onFailureCallBack);
    }

    private String b() {
        return b.a != null ? b.a.getTrackId() : "0";
    }

    private void b(String str, Map<String, String> map, cw cwVar, OnFailureCallBack
            onFailureCallBack) {
        String a = c.a(str, map);
        Builder c = c();
        if (cwVar != null && (cwVar instanceof cx)) {
            c.removeHeader("Authorization");
        }
        c.tag(str);
        a(c.url(str + a).get().build(), cwVar, onFailureCallBack);
    }

    private Builder c() {
        return c(b());
    }

    private Builder c(String str) {
        Builder builder = new Builder();
        String str2 = MQManager.a;
        str2 = str2 + ":" + str + ":" + (((System.currentTimeMillis() / 1000) + 60) + "");
        String replaceAll = ("Mozilla/5.0 (Linux; Android " + VERSION.RELEASE + "; " + Build
                .MODEL + " " + Build.DEVICE + ") MeiqiaSDK/ " + "Source/SDK " + MQManager
                .getMeiqiaSDKVersion() + " Language/" + Locale.getDefault().getLanguage())
                .replaceAll("[^\\x00-\\x7F]", "");
        builder.addHeader("Authorization", str2);
        builder.addHeader(Network.USER_AGENT, replaceAll);
        builder.addHeader("app_version", MQManager.getMeiqiaSDKVersion());
        builder.addHeader("app_platform", "android_sdk");
        builder.addHeader("app_channel", "sdk");
        return builder;
    }

    private void c(String str, Map<String, Object> map, cw cwVar, OnFailureCallBack
            onFailureCallBack) {
        try {
            a(c().url(str).put(RequestBody.create(a, a((Map) map))).build(), cwVar,
                    onFailureCallBack);
        } catch (GeneralSecurityException e) {
            this.d.post(new cg(this, onFailureCallBack));
        }
    }

    public void a(long j, long j2, String str, long j3, cy cyVar) {
        Map hashMap = new HashMap();
        hashMap.put("conversation_id", Long.valueOf(j));
        hashMap.put("msg_id", Long.valueOf(j2));
        hashMap.put("track_id", str);
        hashMap.put("ent_id", Long.valueOf(j3));
        a("https://eco-api.meiqia.com/client/file_downloaded?test=true", hashMap, new ct(this,
                cyVar), (OnFailureCallBack) cyVar);
    }

    public void a(MQMessage mQMessage, File file, OnProgressCallback onProgressCallback) {
        b(mQMessage.getMedia_url(), null, new cu(this, file, onProgressCallback),
                onProgressCallback);
    }

    public void a(OnGetClientCallback onGetClientCallback) {
        a(null, onGetClientCallback);
    }

    public void a(SimpleCallback simpleCallback) {
        Map hashMap = new HashMap();
        hashMap.put("track_id", b.a.getTrackId());
        hashMap.put("ent_id", b.a.getEnterpriseId());
        a("https://eco-api.meiqia.com/client/end_conversation", hashMap, new ca(this,
                simpleCallback), (OnFailureCallBack) simpleCallback);
    }

    public void a(File file, cw cwVar, OnFailureCallBack onFailureCallBack) {
        a(new Builder().url("https://eco-api-upload.meiqia.com/upload").post(new MultipartBuilder
                ().type(MultipartBuilder.FORM).addFormDataPart("file", "file.jpeg", RequestBody
                .create(MediaType.parse("image/jpeg"), file)).build()).build(), new cr(this,
                cwVar), onFailureCallBack);
    }

    public void a(String str) {
        c.cancel(str);
    }

    public void a(String str, int i, int i2, int i3, String str2, int i4,
                  OnGetMessageListCallback onGetMessageListCallback) {
        String str3 = "https://eco-api.meiqia.com/conversation/" + str + "/messages_streams";
        Map hashMap = new HashMap();
        hashMap.put("limit", i + "");
        hashMap.put("ent_id", i3 + "");
        hashMap.put("last_message_created_on", str2);
        hashMap.put("ascending", i4 + "");
        hashMap.put("_time", String.valueOf(System.currentTimeMillis()));
        b(str3, hashMap, new bx(this, onGetMessageListCallback), onGetMessageListCallback);
    }

    public void a(String str, int i, String str2, SimpleCallback simpleCallback) {
        String str3 = "https://eco-api.meiqia.com/conversation/" + str + "/evaluation";
        Map hashMap = new HashMap();
        hashMap.put(b.h, MQManager.a);
        hashMap.put("level", Integer.valueOf(i));
        hashMap.put(Utils.RESPONSE_CONTENT, str2);
        a(false, str3, hashMap, new cf(this, simpleCallback), (OnFailureCallBack) simpleCallback);
    }

    public void a(String str, OnGetClientCallback onGetClientCallback) {
        String str2 = "https://eco-api.meiqia.com/sdk/init_sdk_user";
        Map hashMap = new HashMap();
        hashMap.put(b.h, MQManager.a);
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("track_id", str);
        }
        a(false, str2, hashMap, new by(this, onGetClientCallback), (OnFailureCallBack)
                onGetClientCallback);
    }

    public void a(String str, OnRegisterDeviceTokenCallback onRegisterDeviceTokenCallback) {
        Map hashMap = new HashMap();
        hashMap.put("device_token", str);
        a("https://eco-api.meiqia.com/client/device_token", hashMap, new cb(this,
                onRegisterDeviceTokenCallback), (OnFailureCallBack) onRegisterDeviceTokenCallback);
    }

    public void a(String str, String str2, String str3, SimpleCallback simpleCallback) {
        File file = new File(str2, str3);
        c.newCall(new Builder().url(str).build()).enqueue(new ce(this, simpleCallback, file));
    }

    public void a(String str, Map<String, Object> map, SimpleCallback simpleCallback) {
        e.b("DvcInfo " + str);
        a(true, str, "https://eco-api.meiqia.com/sdk/statistics", map, new cd(this,
                simpleCallback), simpleCallback);
    }

    public void a(String str, Map<String, Object> map, da daVar) {
        a(str, (Map) map, new cp(this, daVar), (OnFailureCallBack) daVar);
    }

    public void a(Map<String, Object> map, OnClientInfoCallback onClientInfoCallback) {
        e.b("setAttrs");
        c("https://eco-api.meiqia.com/client/attrs", map, new cc(this, onClientInfoCallback),
                onClientInfoCallback);
    }

    public void a(Map<String, Object> map, cv cvVar) {
        e.b("scheduler " + map.get("track_id"));
        a("https://eco-api.meiqia.com/scheduler", (Map) map, new bw(this, cvVar),
                (OnFailureCallBack) cvVar);
    }

    public void a(Map<String, String> map, cz czVar) {
        b("https://eco-api.meiqia.com/client/ent_config", map, new ci(this, czVar), czVar);
    }

    public void a(Map<String, Object> map, da daVar) {
        a("https://eco-api.meiqia.com/client/tickets", (Map) map, new cq(this, daVar),
                (OnFailureCallBack) daVar);
    }

    public void a(Map<String, Object> map, db dbVar) {
        a("https://eco-api.meiqia.com/client/tickets/sdk", (Map) map, new ch(this, dbVar),
                (OnFailureCallBack) dbVar);
    }

    public void b(File file, cw cwVar, OnFailureCallBack onFailureCallBack) {
        file.exists();
        a(new Builder().url("https://eco-api-upload.meiqia.com/upload").post(new MultipartBuilder
                ().type(MultipartBuilder.FORM).addFormDataPart("file", "file.amr", RequestBody
                .create(MediaType.parse("audio/amr"), file)).build()).build(), new cs(this,
                cwVar), onFailureCallBack);
    }

    public void b(String str) {
        String trackId = b.a.getTrackId();
        String enterpriseId = b.a.getEnterpriseId();
        Map hashMap = new HashMap();
        hashMap.put("track_id", trackId);
        hashMap.put("ent_id", enterpriseId);
        hashMap.put("type", "text");
        hashMap.put(Utils.RESPONSE_CONTENT, str);
        a("https://eco-api.meiqia.com/client/inputting", hashMap, null, null);
    }

    public void b(String str, OnGetClientCallback onGetClientCallback) {
        Map hashMap = new HashMap();
        hashMap.put("dev_client_id", str);
        a("https://eco-api.meiqia.com/sdk/get_dev_client_id", hashMap, new bz(this,
                onGetClientCallback), (OnFailureCallBack) onGetClientCallback);
    }
}
