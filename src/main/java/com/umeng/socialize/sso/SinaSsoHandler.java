package com.umeng.socialize.sso;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.boohee.model.ModelName;
import com.sina.sso.RemoteSSO;
import com.sina.sso.RemoteSSO.Stub;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.Log;

import java.util.List;

public class SinaSsoHandler extends UMSsoHandler {
    private static final String  AUTH_SERVICE_NAME = "com.sina.weibo.business.RemoteSSOService";
    private static       String  REDIRECT_URL      = "http://sns.whalecloud.com";
    private static final String  WEIBO_SIGNATURE   =
            "30820295308201fea00302010202044b4ef1bf300d06092a864886f70d010105050030818d310b300906035504061302434e3110300e060355040813074265694a696e673110300e060355040713074265694a696e67312c302a060355040a132353696e612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c7464312c302a060355040b132353696e612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c74643020170d3130303131343130323831355a180f32303630303130323130323831355a30818d310b300906035504061302434e3110300e060355040813074265694a696e673110300e060355040713074265694a696e67312c302a060355040a132353696e612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c7464312c302a060355040b132353696e612e436f6d20546563686e6f6c6f677920284368696e612920436f2e204c746430819f300d06092a864886f70d010101050003818d00308189028181009d367115bc206c86c237bb56c8e9033111889b5691f051b28d1aa8e42b66b7413657635b44786ea7e85d451a12a82a331fced99c48717922170b7fc9bc1040753c0d38b4cf2b22094b1df7c55705b0989441e75913a1a8bd2bc591aa729a1013c277c01c98cbec7da5ad7778b2fad62b85ac29ca28ced588638c98d6b7df5a130203010001300d06092a864886f70d0101050500038181000ad4b4c4dec800bd8fd2991adfd70676fce8ba9692ae50475f60ec468d1b758a665e961a3aedbece9fd4d7ce9295cd83f5f19dc441a065689d9820faedbb7c4a4c4635f5ba1293f6da4b72ed32fb8795f736a20c95cda776402099054fccefb4a1a558664ab8d637288feceba9508aa907fc1fe2b1ae5a0dec954ed831c0bea4";
    private static       String  ssoActivityName   = "";
    private static       String  ssoPackageName    = "";
    private              String  TAG               = "SinaSsoHandler";
    private              boolean isAlive           = true;
    private              String  mAppId            = "";
    private UMAuthListener mAuthListener;
    private SHARE_MEDIA       mPlatform          = SHARE_MEDIA.SINA;
    private ServiceConnection mServiceConnection = null;

    public SinaSsoHandler(Context context) {
        super(context);
    }

    public void authorize(Activity act, final UMAuthListener listener) {
        final Activity activity = act;
        this.mContext = act.getApplicationContext();
        this.mAppId = (String) this.mExtraData.get(UMSsoHandler.APPKEY);
        this.mServiceConnection = new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
                if (listener != null) {
                    listener.onError(new SocializeException("无法连接新浪客户端"), SinaSsoHandler.this
                            .mPlatform);
                }
                SinaSsoHandler.this.isAlive = false;
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                SinaSsoHandler.this.isAlive = true;
                RemoteSSO remoteSSOservice = Stub.asInterface(service);
                try {
                    SinaSsoHandler.ssoPackageName = remoteSSOservice.getPackageName();
                    SinaSsoHandler.ssoActivityName = remoteSSOservice.getActivityName();
                    if (!SinaSsoHandler.this.startSingleSignOn(activity, SinaSsoHandler.this
                            .mAppId, new String[0], HandlerRequestCode.SINA_REQUEST_CODE) &&
                            listener != null) {
                        listener.onError(new SocializeException("can`t start singel sign on. "),
                                SinaSsoHandler.this.mPlatform);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        authorize((int) HandlerRequestCode.SINA_REQUEST_CODE, listener);
    }

    private void authorize(int activityCode, UMAuthListener listener) {
        this.mAuthListener = listener;
        if (!(bindRemoteSSOService(this.mContext) || listener == null)) {
            listener.onError(new SocializeException("start sina remote service failed."), this
                    .mPlatform);
        }
        SocializeConfig.setSelectedPlatfrom(SHARE_MEDIA.SINA);
    }

    private boolean bindRemoteSSOService(Context context) {
        Context ctx = context.getApplicationContext();
        Intent intent = new Intent("com.sina.weibo.remotessoservice");
        List<ResolveInfo> infos = context.getPackageManager().queryIntentServices(intent, 0);
        ComponentName componentName = null;
        if (infos != null && infos.size() > 0) {
            ResolveInfo info = (ResolveInfo) infos.get(0);
            componentName = new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name);
        }
        intent.setComponent(componentName);
        return ctx.bindService(intent, this.mServiceConnection, 1);
    }

    private boolean startSingleSignOn(Activity activity, String applicationId, String[]
            permissions, int activityCode) {
        boolean didSucceed = true;
        Intent intent = new Intent();
        intent.setClassName(ssoPackageName, ssoActivityName);
        intent.putExtra(UMSsoHandler.APPKEY, applicationId);
        String redirectUrl = SocializeConfig.getSocializeConfig().getSinaCallbackUrl();
        if (!TextUtils.isEmpty(redirectUrl)) {
            REDIRECT_URL = redirectUrl;
        }
        intent.putExtra("redirectUri", REDIRECT_URL);
        if (permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }
        if (!validateAppSignatureForIntent(activity, intent)) {
            return false;
        }
        try {
            activity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
        }
        if (this.isAlive) {
            this.isAlive = isServiceAlive(activity);
            if (this.isAlive) {
                activity.getApplication().unbindService(this.mServiceConnection);
            }
        }
        return didSucceed;
    }

    private boolean isServiceAlive(Context context) {
        List<RunningServiceInfo> serviceList = ((ActivityManager) context.getSystemService
                (ModelName.ACTIVITY)).getRunningServices(100);
        if (serviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (((RunningServiceInfo) serviceList.get(i)).service.getClassName().equals
                    (AUTH_SERVICE_NAME)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateAppSignatureForIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }
        try {
            for (Signature signature : pm.getPackageInfo(resolveInfo.activityInfo.packageName,
                    64).signatures) {
                if (WEIBO_SIGNATURE.equals(signature.toCharsString())) {
                    return true;
                }
            }
            return false;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        Log.i(this.TAG, "had been sina sso authorizeCallBack...");
        if (requestCode != HandlerRequestCode.SINA_REQUEST_CODE) {
            return;
        }
        if (resultCode == -1) {
            Log.v("10.12", "intent=" + data.toString());
            String error = data.getStringExtra("error");
            if (error == null) {
                error = data.getStringExtra("error_type");
            }
            if (error != null) {
                if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException")) {
                    Log.d("Weibo-authorize", "Login canceled by user.");
                    this.mAuthListener.onCancel(this.mPlatform);
                    return;
                }
                String description = data.getStringExtra("error_description");
                if (description != null) {
                    error = error + ":" + description;
                }
                Log.d("Weibo-authorize", "Login failed: " + error);
                this.mAuthListener.onError(new SocializeException(resultCode, description), this
                        .mPlatform);
            } else if (this.mAuthListener != null) {
                this.mAuthListener.onComplete(data.getExtras(), this.mPlatform);
            }
        } else if (resultCode != 0) {
        } else {
            if (data != null) {
                Log.d("Weibo-authorize", "Login failed: " + data.getStringExtra("error"));
                StringBuilder msg = new StringBuilder();
                msg.append(data.getStringExtra("error"));
                msg.append(" : ");
                msg.append(data.getStringExtra("failing_url"));
                this.mAuthListener.onError(new SocializeException(data.getIntExtra("error_code",
                        -1), msg.toString()), this.mPlatform);
            } else if (this.mAuthListener != null) {
                Log.d("Weibo-authorize", "Login canceled by user.");
                this.mAuthListener.onCancel(this.mPlatform);
            }
        }
    }

    public int getRequstCode() {
        return HandlerRequestCode.SINA_REQUEST_CODE;
    }

    protected CustomPlatform createNewPlatform() {
        return null;
    }

    protected void handleOnClick(CustomPlatform customPlatform, SocializeEntity entity,
                                 SnsPostListener listener) {
    }

    public boolean isClientInstalled() {
        return DeviceConfig.isAppInstalled("com.sina.weibo", this.mContext);
    }

    public boolean shareTo() {
        return true;
    }

    protected void sendReport(boolean result) {
    }
}
