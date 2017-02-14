package com.boohee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.boohee.account.NewUserInitActivity;
import com.boohee.api.StatusApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.User;
import com.boohee.model.status.UserConnection;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.modeldao.UserDao;
import com.boohee.one.MyApplication;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.MainActivity;
import com.boohee.push.PushManager;
import com.boohee.utility.Const;
import com.boohee.utility.SportDataBindHelper;
import com.kitnew.ble.QNApiManager;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

public class AccountUtils {

    public interface OnGetUserProfile {
        void onGetUserProfile(User user);

        void onGetUserProfileFinish();
    }

    public static void saveTokenAndUserKey(Context ctx, User user) {
        if (user != null) {
            UserPreference user_prefs = UserPreference.getInstance(ctx);
            user_prefs.putString("token", user.token);
            user_prefs.putString("user_key", user.user_key);
            user_prefs.putInt("user_type", user.user_type);
            new UserDao(ctx).add(user);
            PushManager.getInstance().bindRegId(ctx);
        }
    }

    public static User getUserProfileLocal(Context context) {
        return new UserDao(context).queryWithToken(UserPreference.getToken(context));
    }

    public static void getUserProfile(final Context context, final OnGetUserProfile
            onGetUserProfile) {
        if (context != null) {
            BooheeClient.build("status").get(StatusApi.USER_PROFILE_DETAIL, new JsonCallback
                    (context) {
                public void onFinish() {
                    super.onFinish();
                    if (onGetUserProfile != null) {
                        onGetUserProfile.onGetUserProfileFinish();
                    }
                }

                public void ok(JSONObject object) {
                    User userData;
                    super.ok(object);
                    try {
                        userData = User.parseUser(object.optJSONObject("user_profile"));
                        if (userData != null) {
                            new UserDao(context).add(userData);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        userData = AccountUtils.getUserProfileLocal(context);
                    }
                    if (userData != null) {
                        if (userData.latest_weight > 0.0f) {
                            OnePreference.setLatestWeight(userData.latest_weight);
                        }
                        if (onGetUserProfile != null) {
                            onGetUserProfile.onGetUserProfile(userData);
                        }
                    }
                }
            }, context);
        }
    }

    public static void getUserProfile(Context context) {
        getUserProfile(context, null);
    }

    public static void getUserProfileAndCheck(final Context context) {
        getUserProfile(context, new OnGetUserProfile() {
            public void onGetUserProfile(User user) {
            }

            public void onGetUserProfileFinish() {
                AccountUtils.login(context);
            }
        });
    }

    public static void login(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (new UserDao(context).queryWithToken(UserPreference.getToken(context)).hasProfile
                    ()) {
                goHome(activity, MainActivity.class);
                return;
            }
            activity.startActivity(new Intent(activity, NewUserInitActivity.class));
            activity.finish();
        }
    }

    public static String getIdentity(Context ctx) {
        UserPreference userPrefs = UserPreference.getInstance(ctx);
        String identity = userPrefs.getString("identity");
        if (identity != null) {
            return identity;
        }
        identity = UUID.randomUUID().toString();
        userPrefs.putString("identity", identity);
        return identity;
    }

    public static void logout() {
        UserPreference userPrefs = UserPreference.getInstance(MyApplication.getContext());
        if (!TextUtils.isEmpty(userPrefs.getString("user_key"))) {
            QNApiManager.getApi(MyApplication.getContext()).deleteUser(userPrefs.getString
                    ("user_key"));
        }
        PushManager.getInstance().unBindRegId(MyApplication.getContext());
        userPrefs.remove("token");
        userPrefs.remove("user_key");
        userPrefs.remove(Const.PASSWORD);
        userPrefs.remove("sex_type");
        userPrefs.remove("budget_s_points");
        userPrefs.remove(SportRecordDao.DURATION);
        userPrefs.remove("cycle");
        userPrefs.remove("POS");
        FilterDataSyncUtil.removeFilterData(MyApplication.getContext());
        SportDataBindHelper.clearData();
        qqLogout();
        OnePreference.getInstance(MyApplication.getContext()).clearAll();
        FileCache.get(MyApplication.getContext()).clear();
    }

    private static void resetPlan(Activity activity, User user) {
        activity.startActivity(new Intent(activity, NewUserInitActivity.class));
        activity.finish();
    }

    public static void goHome(Activity activity, Class<?> homeActivityClass) {
        Intent intent = new Intent(activity, homeActivityClass);
        intent.addFlags(603979776);
        activity.startActivity(intent);
        activity.finish();
    }

    public static boolean isVisitorAccount(Context context) {
        return UserPreference.getInstance(context).getInt("user_type", 2) >= 2;
    }

    public static void setUserTypeSns(Context context) {
        UserPreference.getInstance(context).getEditor().putInt("user_type", 1).commit();
    }

    public static void setUserTypeBoohee(Context context) {
        UserPreference.getInstance(context).getEditor().putInt("user_type", 0).commit();
    }

    public static boolean isReleaseUser() {
        return hasUserKeyAndToken(MyApplication.getContext()) && !isVisitorAccount(MyApplication
                .getContext());
    }

    public static boolean hasUserKeyAndToken(Context ctx) {
        return (TextUtils.isEmpty(UserPreference.getToken(ctx)) || TextUtils.isEmpty
                (UserPreference.getUserKey(ctx))) ? false : true;
    }

    public static void saveQQOpenIDAndAccessToken(Context context, List<UserConnection>
            connections) {
        if (connections != null && connections.size() != 0) {
            for (UserConnection conn : connections) {
                if (SNSLogin.KEY_QQ_ZONE.equals(conn.provider)) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    long expiresIn = currentTime + 2592000;
                    if (!TextUtils.isEmpty(conn.expires_in)) {
                        expiresIn = currentTime + Long.parseLong(conn.expires_in);
                    }
                    UserPreference.getInstance(context).getEditor().putString(Const.QQ_OPEN_ID,
                            conn.identity).putString(Const.QQ_ACCESS_TOKEN, conn.access_token)
                            .putLong(Const.QQ_EXPIRES_IN, expiresIn).commit();
                    return;
                }
            }
        }
    }

    public static void qqLogout() {
        UserPreference userPrefs = UserPreference.getInstance(MyApplication.getContext());
        userPrefs.remove(Const.QQ_OPEN_ID);
        userPrefs.remove(Const.QQ_ACCESS_TOKEN);
        userPrefs.remove(Const.QQ_EXPIRES_IN);
        SNSLogin.qqLogout();
    }
}
