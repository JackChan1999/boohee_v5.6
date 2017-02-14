package com.xiaomi.account.openauth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;

import com.xiaomi.auth.AuthConstants;
import com.xiaomi.auth.XiaomiAuthUtil;

import java.io.IOException;

public class XiaomiOAuthorize {
    protected static final String           TAG        = "XiaomiOAuthorize";
    private static final   String           TYPE_CODE  = "code";
    private static final   String           TYPE_TOKEN = "token";
    private static         XiaomiOAuthorize instance   = new XiaomiOAuthorize();

    @Deprecated
    public interface OnOAuthInterface {
        void onGetAccessTokenDirectly(Bundle bundle);
    }

    @Deprecated
    public static void setOnOAuthInterface(OnOAuthInterface onOAuthInterface) {
    }

    protected void startGetOAuthorize(Activity activity, long clientId, String redirecURI, String
            responseType, Bundle options, int requestCode) {
        if (!doesXiaomiAuthServiceExist(activity)) {
            startAuthorizeActivityFroResult(activity, clientId, redirecURI, responseType,
                    options, requestCode);
        } else if (getXiaomiAccounts(activity).length == 0) {
            tryAddAccountAndGetOAuthorize(activity, clientId, redirecURI, responseType, options,
                    requestCode);
        } else {
            startGetOAuthorizeFromAccount(activity, clientId, redirecURI, responseType, options,
                    requestCode);
        }
    }

    private void tryAddAccountAndGetOAuthorize(Activity activity, long clientId, String
            redirecURI, String responseType, Bundle options, int requestCode) {
        new 1 (this, activity, clientId, redirecURI, responseType, options, requestCode).
        execute(new Void[0]);
    }

    protected void startGetOAuthorizeFromAccount(Activity activity, long clientId, String
            redirecURI, String responseType, Bundle options, int requestCode) {
        new 2
        (this, clientId, redirecURI, responseType, options, activity, getXiaomiAccounts(activity)
                , requestCode).
        execute(new Bundle[0]);
    }

    protected boolean doesXiaomiAuthServiceExist(Activity activity) {
        return XiaomiAuthUtil.isServiceSupport(activity);
    }

    protected void startAuthorizeActivityFroResult(Activity activity, long clientId, String
            redirecURI, String responseType, Bundle options, int requestCode) {
        Activity activity2 = activity;
        long j = clientId;
        String str = redirecURI;
        String str2 = responseType;
        AuthorizeHelper.startAuthorizeActivityForResult(activity2, j, str, str2, options
                .getString(AuthConstants.EXTRA_SCOPE), options.getString(AuthConstants
                .EXTRA_STATE), requestCode);
    }

    protected Account[] getXiaomiAccounts(Activity activity) {
        return AccountManager.get(activity).getAccountsByType("com.xiaomi");
    }

    protected Bundle getAccessToken(Activity activity, Account[] accounts, Bundle optionBundle) {
        return XiaomiAuthUtil.getAccessToken(activity, accounts[0], optionBundle);
    }

    protected Bundle tryAddXiaomiAccount(Activity activity) throws SecurityException,
            OperationCanceledException, IOException, AuthenticatorException {
        return (Bundle) AccountManager.get(activity).addAccount("com.xiaomi", null, null, null,
                activity, null, null).getResult();
    }

    public static void startGetAccessToken(Activity activity, long clientId, String redirecURI,
                                           Bundle options, int requestCode) {
        if (options == null) {
            options = new Bundle();
        }
        getInstance().startGetOAuthorize(activity, clientId, redirecURI, "token", options,
                requestCode);
    }

    public static void startGetOAuthCode(Activity activity, long clientId, String redirecURI,
                                         Bundle options, int requestCode) {
        if (options == null) {
            options = new Bundle();
        }
        getInstance().startGetOAuthorize(activity, clientId, redirecURI, "code", options,
                requestCode);
    }

    private static XiaomiOAuthorize getInstance() {
        return instance;
    }
}
