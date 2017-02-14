package com.meiqia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.meiqia.core.b.h;
import com.meiqia.core.b.j;
import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnClientInfoCallback;
import com.meiqia.core.callback.OnClientOnlineCallback;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnFailureCallBack;
import com.meiqia.core.callback.OnGetMQClientIdCallBackOn;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.core.callback.OnMessageSendCallback;
import com.meiqia.core.callback.OnProgressCallback;
import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;
import com.meiqia.core.callback.SimpleCallback;
import com.meiqia.core.callback.SuccessCallback;
import com.meiqia.meiqiasdk.util.ErrorCode;

import java.util.List;
import java.util.Map;

public class MQManager {
    protected static String    a;
    private static   MQManager b;
    private static   b         c;
    private static   h         d;
    private static   boolean   l;
    private          bc        e;
    private          Handler   f;
    private boolean        g = true;
    private String         h = "";
    private String         i = "";
    private MQScheduleRule j = MQScheduleRule.REDIRECT_ENTERPRISE;
    private Context k;

    private MQManager(Context context) {
        d = new h(context);
        this.e = new bc(context);
        this.f = new Handler(Looper.getMainLooper());
        c = new b(context, d, this.e, this.f);
        this.k = context;
    }

    private void a(OnClientOnlineCallback onClientOnlineCallback) {
        c.a(this.e, this.h, this.i, this.j, new bk(this, onClientOnlineCallback));
    }

    private void a(SuccessCallback successCallback, OnFailureCallBack onFailureCallBack) {
        if (l) {
            successCallback.onSuccess();
        } else {
            init(this.k, a, new be(this, successCallback, onFailureCallBack));
        }
    }

    private void a(String str) {
        c.a(j.a(str, d));
        closeMeiqiaService();
    }

    private void a(String str, String str2, MQScheduleRule mQScheduleRule) {
        Object obj = 1;
        Object obj2 = ((TextUtils.isEmpty(this.i) && TextUtils.isEmpty(str)) || TextUtils.equals
                (this.i, str)) ? null : 1;
        Object obj3 = ((TextUtils.isEmpty(this.h) && TextUtils.isEmpty(str2)) || TextUtils.equals
                (this.h, str2)) ? null : 1;
        if (this.j == mQScheduleRule) {
            obj = null;
        }
        if (obj2 != null || obj3 != null || r2 != null) {
            c();
        }
    }

    private boolean a(OnFailureCallBack onFailureCallBack) {
        if (l) {
            return true;
        }
        if (onFailureCallBack != null) {
            onFailureCallBack.onFailure(ErrorCode.INIT_FAILED, "meiqia sdk init failed");
        }
        return false;
    }

    private boolean a(String str, String str2, String str3, OnMessageSendCallback
            onMessageSendCallback) {
        if (!l) {
            MQMessage mQMessage = new MQMessage(str);
            mQMessage.setContent(str3);
            mQMessage.setMedia_url(str2);
            mQMessage.setFrom_type("client");
            mQMessage.setStatus("failed");
            onMessageSendCallback.onFailure(mQMessage, ErrorCode.INIT_FAILED, "meiqia sdk init " +
                    "failed");
        }
        return true;
    }

    private void c() {
        a(null);
    }

    public static MQManager getInstance(Context context) {
        if (b == null) {
            synchronized (MQManager.class) {
                if (b == null) {
                    b = new MQManager(context.getApplicationContext());
                }
            }
        }
        return b;
    }

    public static String getMeiqiaSDKVersion() {
        return "3.1.6";
    }

    public static void init(Context context, String str, OnInitCallback onInitCallback) {
        b = getInstance(context.getApplicationContext());
        if (TextUtils.isEmpty(str)) {
            str = d.a();
        } else {
            d.a(str);
        }
        a = str;
        c.a(new bd(onInitCallback));
    }

    public static void setDebugMode(boolean z) {
        MeiQiaService.a = z;
    }

    protected void a(MQAgent mQAgent) {
        c.a(mQAgent);
    }

    protected void a(OnGetMessageListCallback onGetMessageListCallback) {
        c.a(new bo(this, onGetMessageListCallback));
    }

    public void cancelDownload(String str) {
        c.b(str);
    }

    public void closeMeiqiaService() {
        setClientOffline();
    }

    public void createMQClient(OnGetMQClientIdCallBackOn onGetMQClientIdCallBackOn) {
        c.a(onGetMQClientIdCallBackOn);
    }

    public void deleteAllMessage() {
        c.b();
    }

    public void deleteMessage(long j) {
        c.a(j);
    }

    public void downloadFile(MQMessage mQMessage, OnProgressCallback onProgressCallback) {
        if (a((OnFailureCallBack) onProgressCallback)) {
            c.a(mQMessage, onProgressCallback);
        }
    }

    public void endCurrentConversation(OnEndConversationCallback onEndConversationCallback) {
        if (a((OnFailureCallBack) onEndConversationCallback)) {
            c.a(onEndConversationCallback);
        }
    }

    public void executeEvaluate(String str, int i, String str2, SimpleCallback simpleCallback) {
        if (a((OnFailureCallBack) simpleCallback)) {
            c.a(str, i, str2, simpleCallback);
        }
    }

    public MQAgent getCurrentAgent() {
        return c.e();
    }

    public String getCurrentClientId() {
        return !l ? null : c.c();
    }

    public String getLeaveMessageTemplete() {
        return d.i();
    }

    public void getMQMessageFromDatabase(long j, int i, OnGetMessageListCallback
            onGetMessageListCallback) {
        if (a((OnFailureCallBack) onGetMessageListCallback)) {
            this.e.a(j, i, new bm(this, onGetMessageListCallback));
        }
    }

    public void getMQMessageFromService(long j, int i, OnGetMessageListCallback
            onGetMessageListCallback) {
        if (a((OnFailureCallBack) onGetMessageListCallback)) {
            c.a(i, 0, j, 2, onGetMessageListCallback);
        }
    }

    public void getUnreadMessages(OnGetMessageListCallback onGetMessageListCallback) {
        getUnreadMessages(null, onGetMessageListCallback);
    }

    public void getUnreadMessages(String str, OnGetMessageListCallback onGetMessageListCallback) {
        if (a((OnFailureCallBack) onGetMessageListCallback)) {
            c.a(str, onGetMessageListCallback);
        }
    }

    public void openMeiqiaService() {
        if (l) {
            c.a(this.k);
        }
    }

    public void refreshEnterpriseConfig(SimpleCallback simpleCallback) {
        if (a((OnFailureCallBack) simpleCallback)) {
            c.a(simpleCallback);
        }
    }

    public void registerDeviceToken(String str, OnRegisterDeviceTokenCallback
            onRegisterDeviceTokenCallback) {
        if (a((OnFailureCallBack) onRegisterDeviceTokenCallback)) {
            c.a(str, onRegisterDeviceTokenCallback);
        }
    }

    public void saveConversationOnStopTime(long j) {
        d.c(j);
    }

    public void sendClientInputtingWithContent(String str) {
        if (!TextUtils.isEmpty(str) && l && this.g) {
            this.g = false;
            c.a(str);
            this.f.postDelayed(new bn(this), 1000);
        }
    }

    public void sendMQPhotoMessage(String str, OnMessageSendCallback onMessageSendCallback) {
        if (a("photo", str, "", onMessageSendCallback)) {
            c.a("", "photo", str, onMessageSendCallback);
        }
    }

    public void sendMQTextMessage(String str, OnMessageSendCallback onMessageSendCallback) {
        if (a("text", "", str, onMessageSendCallback)) {
            c.a(str, "text", null, onMessageSendCallback);
        }
    }

    public void sendMQVoiceMessage(String str, OnMessageSendCallback onMessageSendCallback) {
        if (a("audio", str, "", onMessageSendCallback)) {
            c.a("", "audio", str, onMessageSendCallback);
        }
    }

    public void setClientInfo(Map<String, String> map, OnClientInfoCallback onClientInfoCallback) {
        if (a((OnFailureCallBack) onClientInfoCallback)) {
            c.a((Map) map, onClientInfoCallback);
        }
    }

    public void setClientOffline() {
        MeiQiaService.b = true;
        Intent intent = new Intent(this.k, MeiQiaService.class);
        intent.setAction("ACTION_CLOSE_SOCKET");
        this.k.startService(intent);
    }

    public void setClientOnlineWithClientId(String str, OnClientOnlineCallback
            onClientOnlineCallback) {
        a(new bi(this, str, onClientOnlineCallback), (OnFailureCallBack) onClientOnlineCallback);
    }

    public void setClientOnlineWithCustomizedId(String str, OnClientOnlineCallback
            onClientOnlineCallback) {
        a(new bg(this, str, onClientOnlineCallback), (OnFailureCallBack) onClientOnlineCallback);
    }

    public void setCurrentClient(String str, SimpleCallback simpleCallback) {
        if (!a((OnFailureCallBack) simpleCallback)) {
            return;
        }
        if (TextUtils.isEmpty(str)) {
            if (simpleCallback != null) {
                simpleCallback.onFailure(ErrorCode.PARAMETER_ERROR, "parameter error");
            }
        } else if (j.a(str, d) == null) {
            String n = d.n(str);
            if (TextUtils.isEmpty(n)) {
                c.a(str, new bl(this, simpleCallback));
                return;
            }
            a(n);
            if (simpleCallback != null) {
                simpleCallback.onSuccess();
            }
        } else {
            a(str);
            if (simpleCallback != null) {
                simpleCallback.onSuccess();
            }
        }
    }

    public void setCurrentClientOnline(OnClientOnlineCallback onClientOnlineCallback) {
        a(new bf(this, onClientOnlineCallback), (OnFailureCallBack) onClientOnlineCallback);
    }

    public void setScheduledAgentOrGroupWithId(String str, String str2) {
        setScheduledAgentOrGroupWithId(str, str2, this.j);
    }

    public void setScheduledAgentOrGroupWithId(String str, String str2, MQScheduleRule
            mQScheduleRule) {
        a(str, str2, mQScheduleRule);
        this.i = str;
        this.h = str2;
        this.j = mQScheduleRule;
        c.a(str, str2, mQScheduleRule);
    }

    public void submitMessageForm(String str, List<String> list, Map<String, String> map,
                                  SimpleCallback simpleCallback) {
        if (a((OnFailureCallBack) simpleCallback)) {
            c.a(str, (List) list, (Map) map, simpleCallback);
        }
    }

    public void updateMessage(long j, boolean z) {
        c.a(j, z);
    }
}
