package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.meiqia.core.MQScheduleRule;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.uilimageloader.UILImageLoader;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQConfig.ui;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import java.util.HashMap;

public class MeiQiaHelper {
    private static String APP_KEY                         = "d001a740c9c1987883af0dcf7d26640c";
    public static  String SCHEDULED_GROUP_ID_FEEDBACK     = "a63791646909d7ed2866c8f9bd009b85";
    public static  String SCHEDULED_GROUP_ID_NUTRITIONIST = "845ade7874073ee3f8cb05f30af2d6c6";
    private static boolean isInitSuccess;
    private static int retryTimes = 0;

    static /* synthetic */ int access$108() {
        int i = retryTimes;
        retryTimes = i + 1;
        return i;
    }

    public static void initSDK() {
        MQConfig.init(MyApplication.getContext(), APP_KEY, new UILImageLoader(), new
                OnInitCallback() {
            public void onSuccess(String clientId) {
                MeiQiaHelper.isInitSuccess = true;
            }

            public void onFailure(int code, String message) {
                MeiQiaHelper.isInitSuccess = false;
                if (MeiQiaHelper.retryTimes < 3) {
                    MeiQiaHelper.access$108();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            MeiQiaHelper.initSDK();
                        }
                    }, (long) (MeiQiaHelper.retryTimes * 2000));
                }
            }
        });
    }

    public static void startChat(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            if (isInitSuccess) {
                MQConfig.isSoundSwitchOpen = true;
                MQConfig.isVoiceSwitchOpen = true;
                MQConfig.isLoadMessagesFromNativeOpen = false;
                ui.backArrowIconResId = R.drawable.jr;
                ui.titleTextColorResId = R.color.bt;
                String user_key = UserPreference.getUserKey(MyApplication.getContext());
                if (user_key != null) {
                    activity.startActivity(new MQIntentBuilder(activity).setClientInfo
                            (getClientInfo(activity)).setScheduledGroup
                            (SCHEDULED_GROUP_ID_FEEDBACK).setScheduleRule(MQScheduleRule
                            .REDIRECT_GROUP).setCustomizedId(user_key).build());
                    return;
                }
                return;
            }
            Toast.makeText(activity, "客服启动失败，请稍后重试~~", 0).show();
        }
    }

    private static HashMap<String, String> getClientInfo(Context context) {
        User user = new UserDao(context).queryWithToken(UserPreference.getToken(context));
        HashMap<String, String> userInfo = new HashMap();
        if (user != null) {
            userInfo.put("name", user.user_name);
            userInfo.put("tel", user.cellphone);
            userInfo.put("age", user.birthday);
            userInfo.put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, user.isMale() ? "男" : "女");
            userInfo.put("email", user.email);
            userInfo.put(UserDao.AVATAR, user.avatar_url);
            userInfo.put("comment", "");
            userInfo.put("user_key", UserPreference.getUserKey(MyApplication.getContext()));
            userInfo.put("目标", user.target_weight == -1.0f ? "保持" : "减重");
            userInfo.put("身高", user.height + SocializeProtocolConstants.PROTOCOL_KEY_COMMENT_COUNT);
            userInfo.put("初始体重", user.begin_weight + "kg");
            userInfo.put("最新体重", user.latest_weight + "kg");
            userInfo.put("应用渠道", AppUtils.getChannel(MyApplication.getContext()));
            userInfo.put("目标体重", user.target_weight > 0.0f ? user.target_weight + "kg" : "");
            userInfo.put("目标卡路里", user.target_calory + "大卡");
            userInfo.put("账号类型", user.user_type == 0 ? "薄荷账号" : "社交账号");
        }
        return userInfo;
    }
}
