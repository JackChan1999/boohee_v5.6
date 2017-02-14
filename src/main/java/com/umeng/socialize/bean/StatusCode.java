package com.umeng.socialize.bean;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.utils.Log;

public class StatusCode {
    public static final int ST_CODE_ACCESS_EXPIRED         = 5027;
    public static final int ST_CODE_ACCESS_EXPIRED2        = 5028;
    public static final int ST_CODE_CONTENT_REPEAT         = 5016;
    public static final int ST_CODE_ERROR                  = 40002;
    public static final int ST_CODE_ERROR_CANCEL           = 40000;
    public static final int ST_CODE_ERROR_INVALID_DATA     = 40001;
    public static final int ST_CODE_ERROR_WEIXIN           = 5029;
    public static final int ST_CODE_NO_AUTH                = 5014;
    public static final int ST_CODE_NO_SMS                 = 10086;
    public static final int ST_CODE_RESERVE_CODE           = 5037;
    public static final int ST_CODE_SDK_INITQUEUE_FAILED   = -104;
    public static final int ST_CODE_SDK_NORESPONSE         = -103;
    public static final int ST_CODE_SDK_NO_OAUTH           = -101;
    public static final int ST_CODE_SDK_SHARE_PARAMS_ERROR = -105;
    public static final int ST_CODE_SDK_UNKNOW             = -102;
    public static final int ST_CODE_SUCCESSED              = 200;
    public static final int ST_CODE_USER_BANNED            = 505;

    public static int showErrMsg(Context context, int i, String str) {
        if (i == 200) {
            Log.w("com.umeng.socialize", "call show error msg but error_code is " + i);
        } else if (i == ST_CODE_USER_BANNED) {
            Toast.makeText(context, context.getString(ResContainer.getResourceId(context, ResType
                    .STRING, "umeng_socialize_tip_blacklist")) + (SocializeConstants
                    .SHOW_ERROR_CODE ? " [" + i + "]" : ""), 1).show();
        } else if (i == ST_CODE_ACCESS_EXPIRED || ST_CODE_ACCESS_EXPIRED2 == i) {
            Toast.makeText(context, "授权失效，请重新授权...", 0).show();
        } else if (i == ST_CODE_CONTENT_REPEAT) {
            Toast.makeText(context, "分享内容重复...", 0).show();
        } else if (i == ST_CODE_ERROR_CANCEL) {
            Toast.makeText(context, "取消分享", 0).show();
        } else if (!TextUtils.isEmpty(str)) {
            Toast.makeText(context, str + " [" + i + "]", 1).show();
        }
        return i;
    }
}
