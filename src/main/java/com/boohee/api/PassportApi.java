package com.boohee.api;

import android.content.Context;

import com.boohee.modeldao.UserDao;
import com.boohee.one.MyApplication;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.PassportClient;
import com.boohee.utility.Const;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.TextUtil;

public class PassportApi {
    public static final String AUTHENTICATE_GUEST                    =
            "/api/v1/user_connections/authenticate_guest.json";
    public static final String AUTHENTICATE_SNS                      =
            "/api/v1/user_connections/authenticate_sns.json";
    public static final String CHANGE_PASSWORD                       =
            "/api/v1/users/change_password";
    public static final String CREATE_REFERRER                       =
            "/api/v1/recommendships/create_referrer";
    public static final String DEL_USER_CONNECTIONS                  =
            "/api/v1/user_connections/%1d.json";
    public static final String FINISH_RESET_PASSWORD                 =
            "/api/v1/users/finish_reset_password";
    public static final String FORGET_PASSWORD_BY_EMAIL              =
            "/api/v1/users/reset_password_by_email";
    public static final String FORGET_PASSWORD_BY_PHONE              =
            "/api/v1/users/reset_password";
    public static final String GET_USER_CONNECTIONS                  = "/api/v1/user_connections" +
            ".json";
    public static final String LOGIN                                 = "/api/v1/users/login";
    public static final String REGISTER                              =
            "/api/v1/register/create_user";
    public static final String REGISTER_CELLPHONE_VERIFICATION       =
            "/api/v1/register/send_cellphone_verification";
    public static final String REGISTER_CELLPHONE_VERIFICATION_CHECK =
            "/api/v1/register/verify_cellphone";
    public static final String SEND_CELLPHONE_VERIFICATION           =
            "/api/v1/users/send_cellphone_verification";
    public static final String SET_PASSWORD                          = "/api/v1/users/set_password";
    public static final String UPGRADE_TO_BOOHEE                     =
            "/api/v1/users/upgrade_to_boohee";
    public static final String VERIFY_CELLPHONE                      =
            "/api/v1/users/verify_cellphone";

    public static void getUserConnections(Context context, JsonCallback callback) {
        PassportClient.get("/api/v1/user_connections.json", context, callback);
    }

    public static void deleteUserConnection(int conId, Context context, JsonCallback callback) {
        PassportClient.delete(String.format(DEL_USER_CONNECTIONS, new Object[]{Integer.valueOf
                (conId)}), null, context, callback);
    }

    public static void changePassword(String oldPassword, String newPassword, Context context,
                                      JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("old_password", oldPassword);
        params.put("new_password", newPassword);
        PassportClient.post(CHANGE_PASSWORD, params, context, callback);
    }

    public static void setPassword(String password, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put(Const.PASSWORD, password);
        PassportClient.post(SET_PASSWORD, params, context, callback);
    }

    public static void verifyCellphone(String cellphone, String code, int force, Context context,
                                       JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("cellphone", cellphone);
        params.put("force", force);
        params.put("code", code);
        PassportClient.post(VERIFY_CELLPHONE, params, context, callback);
    }

    public static void sendCellphoneVerification(String cellphone, int force, Context context,
                                                 JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("cellphone", cellphone);
        if (force == 1) {
            params.put("force", 1);
        }
        PassportClient.post(SEND_CELLPHONE_VERIFICATION, params, context, callback);
    }

    public static void login(String login, String password, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("login", login);
        params.put(Const.PASSWORD, password);
        PassportClient.post(LOGIN, params, context, callback);
    }

    public static void createReferrer(String referrer_cellphone, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("referrer_cellphone", referrer_cellphone);
        PassportClient.post(CREATE_REFERRER, params, context, callback);
    }

    public static void forgetPasswordByEmail(String email, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("email", email);
        PassportClient.post(FORGET_PASSWORD_BY_EMAIL, params, context, callback);
    }

    public static void forgetPasswordByPhone(String cellphone, String method, Context context,
                                             JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("cellphone", cellphone);
        params.put("method", method);
        PassportClient.post(FORGET_PASSWORD_BY_PHONE, params, context, callback);
    }

    public static void finishResetPassword(String password, String token, Context context,
                                           JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put(Const.PASSWORD, password);
        params.put("token", token);
        PassportClient.post(FINISH_RESET_PASSWORD, params, context, callback);
    }

    public static void upgradeToBoohee(String user_name, String phoneEmail, String password,
                                       Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put(UserDao.USER_NAME, user_name);
        if (TextUtil.isEmail(phoneEmail)) {
            params.put("email", phoneEmail);
        } else {
            params.put("cellphone", phoneEmail);
        }
        params.put(Const.PASSWORD, password);
        PassportClient.post(UPGRADE_TO_BOOHEE, params, context, callback);
    }

    public static void authenticateGuest(Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("provider", "device");
        params.put("identity", AccountUtils.getIdentity(MyApplication.getContext()));
        PassportClient.post(AUTHENTICATE_GUEST, params.with("user_connection"), context, callback);
    }

    public static void getCellphoneCaptcha(Context context, String cellPhone, String method,
                                           JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("cellphone", cellPhone);
        params.put("method", method);
        PassportClient.post(REGISTER_CELLPHONE_VERIFICATION, params, context, callback);
    }

    public static void checkCellphoneCaptcha(Context context, String cellPhone, String code,
                                             String token, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("cellphone", cellPhone);
        params.put("code", code);
        params.put("token", token);
        PassportClient.post(REGISTER_CELLPHONE_VERIFICATION_CHECK, params, context, callback);
    }

    public static void register(Context context, String user_name, String password, String
            cellphone, String token, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put(UserDao.USER_NAME, user_name);
        params.put(Const.PASSWORD, password);
        params.put("cellphone", cellphone);
        params.put("token", token);
        PassportClient.post(REGISTER, params, context, callback);
    }
}
