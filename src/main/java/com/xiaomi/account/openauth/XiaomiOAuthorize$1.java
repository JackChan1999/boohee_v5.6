package com.xiaomi.account.openauth;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

class XiaomiOAuthorize$1 extends AsyncTask<Void, Void, Bundle> {
    private                       boolean          retryWebViewWay;
    final /* synthetic */         XiaomiOAuthorize this$0;
    private final /* synthetic */ Activity         val$activity;
    private final /* synthetic */ long             val$clientId;
    private final /* synthetic */ Bundle           val$options;
    private final /* synthetic */ String           val$redirecURI;
    private final /* synthetic */ int              val$requestCode;
    private final /* synthetic */ String           val$responseType;

    XiaomiOAuthorize$1(XiaomiOAuthorize xiaomiOAuthorize, Activity activity, long j, String str,
                       String str2, Bundle bundle, int i) {
        this.this$0 = xiaomiOAuthorize;
        this.val$activity = activity;
        this.val$clientId = j;
        this.val$redirecURI = str;
        this.val$responseType = str2;
        this.val$options = bundle;
        this.val$requestCode = i;
    }

    protected Bundle doInBackground(Void... params) {
        try {
            return this.this$0.tryAddXiaomiAccount(this.val$activity);
        } catch (SecurityException e) {
            e.printStackTrace();
            this.retryWebViewWay = true;
            return null;
        } catch (OperationCanceledException e2) {
            e2.printStackTrace();
            return null;
        } catch (AuthenticatorException e3) {
            e3.printStackTrace();
            this.retryWebViewWay = true;
            return null;
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bundle result) {
        if (result != null) {
            if (result.containsKey("authAccount")) {
                this.this$0.startGetOAuthorizeFromAccount(this.val$activity, this.val$clientId,
                        this.val$redirecURI, this.val$responseType, this.val$options, this
                                .val$requestCode);
            } else if (result.containsKey("intent")) {
                throw new IllegalStateException("XiaomiAuthoricator.addAccount() returns intent " +
                        "for UI action, but we don't exptect this because activity is not null");
            } else {
                Log.v("XiaomiOAuthorize", "do nothing after trying to add account, because no " +
                        "valid content in result bundle.");
            }
        } else if (this.retryWebViewWay) {
            this.this$0.startAuthorizeActivityFroResult(this.val$activity, this.val$clientId,
                    this.val$redirecURI, this.val$responseType, this.val$options, this
                            .val$requestCode);
        } else {
            Log.v("XiaomiOAuthorize", "do nothing after trying to add account.");
        }
    }
}
