package com.xiaomi.account.openauth;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.xiaomi.auth.AuthConstants;

class XiaomiOAuthorize$2 extends AsyncTask<Bundle, Bundle, Bundle> {
    final /* synthetic */         XiaomiOAuthorize this$0;
    private final /* synthetic */ Account[]        val$accounts;
    private final /* synthetic */ Activity         val$activity;
    private final /* synthetic */ long             val$clientId;
    private final /* synthetic */ Bundle           val$options;
    private final /* synthetic */ String           val$redirecURI;
    private final /* synthetic */ int              val$requestCode;
    private final /* synthetic */ String           val$responseType;

    XiaomiOAuthorize$2(XiaomiOAuthorize xiaomiOAuthorize, long j, String str, String str2, Bundle
            bundle, Activity activity, Account[] accountArr, int i) {
        this.this$0 = xiaomiOAuthorize;
        this.val$clientId = j;
        this.val$redirecURI = str;
        this.val$responseType = str2;
        this.val$options = bundle;
        this.val$activity = activity;
        this.val$accounts = accountArr;
        this.val$requestCode = i;
    }

    protected Bundle doInBackground(Bundle... notUsed) {
        Bundle optionBundle = new Bundle();
        optionBundle.putString(AuthConstants.EXTRA_CLIENT_ID, String.valueOf(this.val$clientId));
        optionBundle.putString(AuthConstants.EXTRA_REDIRECT_URI, this.val$redirecURI);
        optionBundle.putString(AuthConstants.EXTRA_RESPONSE_TYPE, this.val$responseType);
        if (this.val$options != null) {
            optionBundle.putAll(this.val$options);
        }
        return this.this$0.getAccessToken(this.val$activity, this.val$accounts, optionBundle);
    }

    protected void onPostExecute(Bundle bundle) {
        if (bundle.getInt(AuthConstants.EXTRA_ERROR_CODE) == -1001) {
            this.val$activity.startActivityForResult((Intent) bundle.getParcelable(AuthConstants
                    .EXTRA_INTENT), this.val$requestCode);
            return;
        }
        this.this$0.startAuthorizeActivityFroResult(this.val$activity, this.val$clientId, this
                .val$redirecURI, this.val$responseType, this.val$options, this.val$requestCode);
    }
}
