package com.hanyou.leyusdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.connect.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LEYUApplication {
    private static final String          APP_CONFIG       = "LEYUconfig";
    public static final  int             NETTYPE_CMNET    = 3;
    public static final  int             NETTYPE_CMWAP    = 2;
    public static final  int             NETTYPE_WIFI     = 1;
    public static final  int             REQ_LOGIN        = 21;
    private static       String          _app_id          = "";
    private static       String          _app_key         = "";
    private static       String          access_token     = "";
    private static       String          dev_access_token = "";
    private static       LEYUApplication leyuappConfig    = null;
    private static       boolean         login            = false;
    private static       int             loginUid         = 0;
    private static       Context         mContext         = null;
    private static final String          wwwhost          = "http://m.miao.cn";
    public ICallBack _callback;
    private Handler xHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String smsg = msg.obj.toString();
            if (msg.what == 0) {
                try {
                    LEYUApplication.dev_access_token = ((JSONObject) new JSONTokener(smsg)
                            .nextValue()).getString("access_token");
                    LEYUApplication.set("leyu_dev_access_token", LEYUApplication.dev_access_token);
                    LEYUApplication.this._callback.ReturnAccessToken(LEYUApplication
                            .dev_access_token);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    LEYUApplication.this._callback.onFailed(ex.getMessage());
                }
            } else if (msg.what == 1) {
                LEYUApplication.this._callback.OnCompleted(smsg);
            } else if (msg.what == 2) {
                LEYUApplication.this._callback.onFailed(smsg);
            }
        }
    };

    class AnonymousClass4 extends HashMap<String, Object> {
        AnonymousClass4(String str, String str2, String str3, int i, int i2, double d, double d2,
                        String str4) {
            put("action", "passometer");
            put("deviceid", str);
            put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(LEYUApplication
                    .loginUid));
            put("uploadtime", str2);
            put("measuretime", str3);
            put("movementsteps", Integer.valueOf(i));
            put("kcal", Integer.valueOf(i2));
            put("mileage", Double.valueOf(d));
            put("fat", Double.valueOf(d2));
            put("sport_type", str4);
            put("access_token", LEYUApplication.access_token);
        }
    }

    class AnonymousClass5 extends HashMap<String, Object> {
        AnonymousClass5(String str, String str2, String str3, String str4, String str5, double d,
                        String str6) {
            put("action", "sleep");
            put("deviceid", str);
            put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(LEYUApplication
                    .loginUid));
            put("uploadtime", str2);
            put("measuretime", str3);
            put("s_sleeptime", str4);
            put("e_sleeptime", str5);
            put("effectivesleep", Double.valueOf(d));
            put("sleepquality", str6);
            put("access_token", LEYUApplication.access_token);
        }
    }

    class AnonymousClass6 extends HashMap<String, Object> {
        AnonymousClass6(String str, String str2, String str3, int i, int i2, int i3, int i4, int
                i5) {
            put("action", "bloodpressure");
            put("deviceid", str);
            put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(LEYUApplication
                    .loginUid));
            put("uploadtime", str2);
            put("measuretime", str3);
            put("systolicpressure", Integer.valueOf(i));
            put("diastolicpressure", Integer.valueOf(i2));
            put("pmean", Integer.valueOf(i3));
            put("pulserate", Integer.valueOf(i4));
            put("is_day", Integer.valueOf(i5));
            put("access_token", LEYUApplication.access_token);
        }
    }

    class AnonymousClass7 extends HashMap<String, Object> {
        AnonymousClass7(String str, String str2, String str3, double d, int i) {
            put("action", "bloodglucose");
            put("deviceid", str);
            put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(LEYUApplication
                    .loginUid));
            put("uploadtime", str2);
            put("measuretime", str3);
            put("bloodglucosevalue", Double.valueOf(d));
            put("timetype", Integer.valueOf(i));
            put("access_token", LEYUApplication.access_token);
        }
    }

    class AnonymousClass8 extends HashMap<String, Object> {
        AnonymousClass8(String str, String str2, String str3, int i, int i2, int i3, int i4, int
                i5) {
            put("action", "physical");
            put("deviceid", str);
            put(SocializeProtocolConstants.PROTOCOL_KEY_UID, Integer.valueOf(LEYUApplication
                    .loginUid));
            put("uploadtime", str2);
            put("measuretime", str3);
            put("height", Integer.valueOf(i));
            put("weight", Integer.valueOf(i2));
            put("waistline", Integer.valueOf(i3));
            put("hipline", Integer.valueOf(i4));
            put("bustline", Integer.valueOf(i5));
            put("access_token", LEYUApplication.access_token);
        }
    }

    public interface ICallBack {
        void OnCompleted(String str);

        void ReturnAccessToken(String str);

        void onFailed(String str);
    }

    public static LEYUApplication getAppConfig(Context context) {
        if (leyuappConfig == null) {
            leyuappConfig = new LEYUApplication(context);
        }
        return leyuappConfig;
    }

    public LEYUApplication(Context context) {
        mContext = context;
        initWithApiKey(context);
        getLocalDevAccessToken();
        initLoginInfo();
    }

    public static boolean isNetworkConnected() {
        NetworkInfo ni = ((ConnectivityManager) mContext.getSystemService("connectivity"))
                .getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static int getNetworkType() {
        NetworkInfo networkInfo = ((ConnectivityManager) mContext.getSystemService
                ("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null) {
            return 0;
        }
        int nType = networkInfo.getType();
        if (nType == 0) {
            String extraInfo = networkInfo.getExtraInfo();
            if (isEmpty(extraInfo)) {
                return 0;
            }
            if (extraInfo.toLowerCase().equals("cmnet")) {
                return 3;
            }
            return 2;
        } else if (nType == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    private void initWithApiKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), 128);
            Object leyu_app_id = appInfo.metaData.get("leyu_app_id");
            Object leyu_app_key = appInfo.metaData.get("leyu_app_key");
            _app_id = String.valueOf(leyu_app_id);
            _app_key = String.valueOf(leyu_app_key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            this._callback.onFailed(e.getMessage());
        }
    }

    public void LEYULoginview() {
        Intent intent = new Intent(mContext, HelloWebView.class);
        Bundle bundle = new Bundle();
        bundle.putString("appid", _app_id);
        bundle.putString("accesstoken", dev_access_token);
        intent.putExtras(bundle);
        ((Activity) mContext).startActivityForResult(intent, 21);
    }

    public boolean isLogin() {
        return login;
    }

    public int getLoginUid() {
        return loginUid;
    }

    public void Logout() {
        remove("leyu_user");
        login = false;
        loginUid = 0;
    }

    public void initLoginInfo() {
        LEYUUser loginUser = getLoginInfo();
        if (loginUser == null || loginUser.getUid() <= 0) {
            Logout();
            return;
        }
        login = true;
        loginUid = loginUser.getUid();
        access_token = loginUser.getaccess_token();
    }

    public LEYUUser getLoginInfo() {
        LEYUUser lu = null;
        try {
            String userInfo = get("leyu_user");
            if (!(userInfo == null || "".equals(userInfo) || userInfo.length() <= 0)) {
                lu = LEYUUser.fromJSONString(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        }
        return lu;
    }

    public String getLocalDevAccessToken() {
        if (isEmpty(dev_access_token)) {
            dev_access_token = get("leyu_dev_access_token");
        }
        return dev_access_token;
    }

    public static int toInt(String str, int defValue) {
        try {
            defValue = Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static void saveLoginInfo(LEYUUser user) {
        loginUid = user.getUid();
        login = true;
        set("leyu_user", user.toJSONString());
    }

    private String GetDate(String url) {
        String resultString = "";
        try {
            return EntityUtils.toString(new DefaultHttpClient().execute(new HttpGet(wwwhost +
                    url)).getEntity());
        } catch (Exception e) {
            this._callback.onFailed(e.getMessage());
            return resultString;
        }
    }

    private void GetDate2(final String url) {
        if (isLogin()) {
            new Thread(new Runnable() {
                public void run() {
                    Message m = new Message();
                    m.what = 1;
                    m.obj = LEYUApplication.this.GetDate(url);
                    LEYUApplication.this.xHandler.sendMessage(m);
                }
            }).start();
        } else {
            this._callback.onFailed("Not Login");
        }
    }

    public void GetDeveloperAccessToken() {
        new Thread(new Runnable() {
            public void run() {
                Message m = new Message();
                m.what = 0;
                m.obj = LEYUApplication.this.GetDate(LEYUApplication._MakeURL
                        ("/action/devapi/verify.jsp", new HashMap<String, Object>() {
                    {
                        put("action", "verify");
                        put("appid", LEYUApplication._app_id);
                        put("appsecret", LEYUApplication._app_key);
                    }
                }));
                LEYUApplication.this.xHandler.sendMessage(m);
            }
        }).start();
    }

    public void passometer(String deviceid, String uploadtime, String measuretime, int
            movementsteps, int kcal, double mileage, double fat, String sport_type) {
        GetDate2(_MakeURL("/action/devapi/uploaddata.jsp", new AnonymousClass4(deviceid,
                uploadtime, measuretime, movementsteps, kcal, mileage, fat, sport_type)));
    }

    public void sleep(String deviceid, String uploadtime, String measuretime, String s_sleeptime,
                      String e_sleeptime, double effectivesleep, String sleepquality) {
        GetDate2(_MakeURL("/action/devapi/uploaddata.jsp", new AnonymousClass5(deviceid,
                uploadtime, measuretime, s_sleeptime, e_sleeptime, effectivesleep, sleepquality)));
    }

    public void bloodpressure(String deviceid, String uploadtime, String measuretime, int
            systolicpressure, int diastolicpressure, int pmean, int pulserate, int is_day) {
        GetDate2(_MakeURL("/action/devapi/uploaddata.jsp", new AnonymousClass6(deviceid,
                uploadtime, measuretime, systolicpressure, diastolicpressure, pmean, pulserate,
                is_day)));
    }

    public void bloodglucose(String deviceid, String uploadtime, String measuretime, double
            bloodglucosevalue, int timetype) {
        GetDate2(_MakeURL("/action/devapi/uploaddata.jsp", new AnonymousClass7(deviceid,
                uploadtime, measuretime, bloodglucosevalue, timetype)));
    }

    public void physical(String deviceid, String uploadtime, String measuretime, int height, int
            weight, int waistline, int hipline, int bustline) {
        GetDate2(_MakeURL("/action/devapi/uploaddata.jsp", new AnonymousClass8(deviceid,
                uploadtime, measuretime, height, weight, waistline, hipline, bustline)));
    }

    private String get(String key) {
        Properties props = get();
        return props != null ? props.getProperty(key) : null;
    }

    private static Properties get() {
        Throwable th;
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            FileInputStream fis2 = new FileInputStream(new StringBuilder(String.valueOf(mContext
                    .getDir(APP_CONFIG, 0).getPath())).append(File.separator).append(APP_CONFIG)
                    .toString());
            try {
                props.load(fis2);
                try {
                    fis2.close();
                    fis = fis2;
                } catch (Exception e) {
                    fis = fis2;
                }
            } catch (Exception e2) {
                fis = fis2;
                try {
                    fis.close();
                } catch (Exception e3) {
                }
                return props;
            } catch (Throwable th2) {
                th = th2;
                fis = fis2;
                try {
                    fis.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (Exception e5) {
            fis.close();
            return props;
        } catch (Throwable th3) {
            th = th3;
            fis.close();
            throw th;
        }
        return props;
    }

    private static void setProps(Properties p) {
        Exception e;
        Throwable th;
        FileOutputStream fos = null;
        try {
            FileOutputStream fos2 = new FileOutputStream(new File(mContext.getDir(APP_CONFIG, 0),
                    APP_CONFIG));
            try {
                p.store(fos2, null);
                fos2.flush();
                try {
                    fos2.close();
                    fos = fos2;
                } catch (Exception e2) {
                    fos = fos2;
                }
            } catch (Exception e3) {
                e = e3;
                fos = fos2;
                try {
                    e.printStackTrace();
                    try {
                        fos.close();
                    } catch (Exception e4) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        fos.close();
                    } catch (Exception e5) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                fos.close();
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            e.printStackTrace();
            fos.close();
        }
    }

    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0) {
            url.append('?');
        }
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
        }
        return url.toString().replace("?&", "?").replace(" ", "%20");
    }

    private Doc toDoc(String fanhui) {
        Doc map = new Doc();
        try {
            String[] tits = fanhui.split(",");
            if (fanhui.indexOf(",") > -1) {
                for (String split : tits) {
                    String[] ddd = split.split(":");
                    map.put(ddd[0].trim(), ddd[1].trim());
                }
            } else {
                map.put("error_code", Constants.DEFAULT_UIN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    private static void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    private void remove(String... key) {
        Properties props = get();
        for (String k : key) {
            props.remove(k);
        }
        setProps(props);
    }
}
