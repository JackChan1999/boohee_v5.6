package com.umeng.socialize.net.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alipay.sdk.sys.a;
import com.boohee.utility.TimeLinePatterns;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SocializeNetUtils {
    private static final String TAG = SocializeNetUtils.class.getName();

    public static String generateGetURL(String str, Map<String, Object> map) {
        if (TextUtils.isEmpty(str) || map == null || map.size() == 0) {
            return str;
        }
        if (!str.endsWith("?")) {
            str = str + "?";
        }
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = stringBuilder;
        for (String str2 : map.keySet()) {
            if (map.get(str2) != null) {
                stringBuilder2 = stringBuilder2.append(str2 + "=" + URLEncoder.encode(map.get
                        (str2).toString()) + a.b);
            }
        }
        StringBuilder stringBuilder3 = new StringBuilder(str);
        try {
            stringBuilder3.append("ud_get=" + AesHelper.encryptNoPadding(stringBuilder2.substring
                    (0, stringBuilder2.length() - 1).toString(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder3.toString();
    }

    public static Map<String, Object> getBaseQuery(Context context, SocializeEntity
            socializeEntity, int i) {
        Map<String, Object> hashMap = new HashMap();
        Object deviceId = DeviceConfig.getDeviceId(context);
        if (!TextUtils.isEmpty(deviceId)) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_IMEI, deviceId);
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_MD5IMEI, AesHelper.md5(deviceId));
        }
        CharSequence mac = DeviceConfig.getMac(context);
        if (TextUtils.isEmpty(mac)) {
            Log.w(TAG, "Get MacAddress failed. Check permission android.permission" +
                    ".ACCESS_WIFI_STATE [" + DeviceConfig.checkPermission(context, "android" +
                    ".permission.ACCESS_WIFI_STATE") + "]");
        } else {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_MAC, mac);
        }
        if (!TextUtils.isEmpty(SocializeConstants.UID)) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_UID, SocializeConstants.UID);
        }
        try {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_EN, DeviceConfig
                    .getNetworkAccessMode(context)[0]);
        } catch (Exception e) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_EN, "Unknown");
        }
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_DE, Build.MODEL);
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_VERSION, SocializeConstants
                .SDK_VERSION);
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_OS, "Android");
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_DT, Long.valueOf(System
                .currentTimeMillis()));
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_OPID, Integer.valueOf(i));
        mac = SocializeUtils.getAppkey(context);
        if (TextUtils.isEmpty(mac)) {
            throw new SocializeException("No found appkey.");
        }
        hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_AK, mac);
        if (!TextUtils.isEmpty(socializeEntity.mEntityKey)) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_ENTITY_KEY, socializeEntity
                    .mEntityKey);
        }
        if (!TextUtils.isEmpty(socializeEntity.mSessionID)) {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_SID, socializeEntity.mSessionID);
        }
        hashMap.put(SocializeProtocolConstants.PROTOCOL_VERSION, "2.0");
        try {
            hashMap.put(SocializeProtocolConstants.PROTOCOL_KEY_REQUEST_TYPE, socializeEntity
                    .getRequestType().toString());
        } catch (Exception e2) {
        }
        return hashMap;
    }

    public static byte[] getNetData(String str) {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        Throwable th2;
        InputStream inputStream = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                InputStream inputStream2 = (InputStream) new URL(str).openConnection().getContent();
                try {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = inputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    bArr = byteArrayOutputStream.toByteArray();
                    if (inputStream2 != null) {
                        try {
                            inputStream2.close();
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e) {
                                }
                            }
                        } catch (IOException e2) {
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e3) {
                                }
                            }
                        } catch (Throwable th3) {
                            if (byteArrayOutputStream != null) {
                                try {
                                    byteArrayOutputStream.close();
                                } catch (IOException e4) {
                                }
                            }
                        }
                    }
                    return bArr;
                } catch (Throwable e5) {
                    th = e5;
                    inputStream = inputStream2;
                    th2 = th;
                    try {
                        throw new RuntimeException(th2);
                    } catch (Throwable th4) {
                        th2 = th4;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (IOException e6) {
                                    }
                                }
                            } catch (IOException e7) {
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (IOException e8) {
                                    }
                                }
                            } catch (Throwable th5) {
                                if (byteArrayOutputStream != null) {
                                    try {
                                        byteArrayOutputStream.close();
                                    } catch (IOException e9) {
                                    }
                                }
                            }
                        }
                        throw th2;
                    }
                } catch (Throwable e52) {
                    th = e52;
                    inputStream = inputStream2;
                    th2 = th;
                    if (inputStream != null) {
                        inputStream.close();
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                    }
                    throw th2;
                }
            } catch (Exception e10) {
                th2 = e10;
                throw new RuntimeException(th2);
            }
        } catch (Exception e11) {
            th2 = e11;
            byteArrayOutputStream = null;
            throw new RuntimeException(th2);
        } catch (Throwable th6) {
            th2 = th6;
            byteArrayOutputStream = null;
            if (inputStream != null) {
                inputStream.close();
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            }
            throw th2;
        }
    }

    public static boolean startWithHttp(String str) {
        return str.startsWith(TimeLinePatterns.WEB_SCHEME) || str.startsWith("https://");
    }
}
