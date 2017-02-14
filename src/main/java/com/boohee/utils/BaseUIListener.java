package com.boohee.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.status.UserTimelineActivity;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/* compiled from: SNSLogin */
class BaseUIListener implements IUiListener {
    private static final int ON_CANCEL   = 2;
    private static final int ON_COMPLETE = 0;
    private static final int ON_ERROR    = 1;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 0:
                        JSONObject response = msg.obj;
                        if (response != null) {
                            String openID = BaseUIListener.this.mValues.optString("openid");
                            String accessToken = BaseUIListener.this.mValues.optString
                                    ("access_token");
                            long expiresAt = BaseUIListener.this.mValues.optLong("expires_in");
                            SNSLogin.snsLogin(BaseUIListener.this.mUrl, openID, accessToken,
                                    response.optString(UserTimelineActivity.NICK_NAME), response
                                            .optString("figureurl"), expiresAt, BaseUIListener
                                            .this.mContext, BaseUIListener.this.snsInfoListener);
                            return;
                        }
                        return;
                    case 1:
                        Helper.showToast((int) R.string.ca);
                        return;
                    case 2:
                        Helper.showToast((int) R.string.c_);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            e.printStackTrace();
        }
    };
    private boolean      mIsCaneled;
    private String       mUrl;
    private JSONObject   mValues;
    private JsonCallback snsInfoListener;

    public BaseUIListener(String url, Context context, JSONObject values, JsonCallback callback) {
        this.mUrl = url;
        this.mValues = values;
        this.snsInfoListener = callback;
        this.mContext = context;
    }

    public void cancel() {
        this.mIsCaneled = true;
    }

    public void onComplete(Object response) {
        if (!this.mIsCaneled) {
            Message msg = this.mHandler.obtainMessage();
            msg.what = 0;
            msg.obj = response;
            this.mHandler.sendMessage(msg);
        }
    }

    public void onError(UiError e) {
        if (!this.mIsCaneled) {
            Message msg = this.mHandler.obtainMessage();
            msg.what = 1;
            msg.obj = e;
            this.mHandler.sendMessage(msg);
        }
    }

    public void onCancel() {
        if (!this.mIsCaneled) {
            Message msg = this.mHandler.obtainMessage();
            msg.what = 2;
            this.mHandler.sendMessage(msg);
        }
    }
}
