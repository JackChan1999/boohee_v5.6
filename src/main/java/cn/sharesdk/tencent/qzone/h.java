package cn.sharesdk.tencent.qzone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import cn.sharesdk.framework.authorize.e;
import cn.sharesdk.framework.authorize.f;
import com.tencent.connect.common.Constants;
import com.tencent.open.utils.SystemUtils;
import org.json.JSONObject;

public class h extends f {
    private String d;
    private String e;

    public h(e eVar) {
        super(eVar);
    }

    public void a() {
        PackageInfo packageInfo;
        String str = "com.tencent.mobileqq";
        try {
            packageInfo = this.a.getContext().getPackageManager().getPackageInfo(str, 0);
        } catch (Throwable th) {
            this.a.finish();
            if (this.c != null) {
                this.c.onFailed(new TencentSSOClientNotInstalledException());
                return;
            }
            return;
        }
        if (packageInfo == null) {
            this.a.finish();
            if (this.c != null) {
                this.c.onFailed(new TencentSSOClientNotInstalledException());
                return;
            }
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(str, "com.tencent.open.agent.AgentActivity");
        if (this.a.getContext().getPackageManager().resolveActivity(intent, 0) == null) {
            this.a.finish();
            if (this.c != null) {
                this.c.onFailed(new TencentSSOClientNotInstalledException());
                return;
            }
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("scope", this.e);
        bundle.putString(Constants.PARAM_CLIENT_ID, this.d);
        bundle.putString(Constants.PARAM_PLATFORM_ID, Constants.DEFAULT_PF);
        bundle.putString("need_pay", "1");
        intent.putExtra(Constants.KEY_PARAMS, bundle);
        intent.putExtra(Constants.KEY_REQUEST_CODE, this.b);
        intent.putExtra(Constants.KEY_ACTION, SystemUtils.ACTION_LOGIN);
        try {
            this.a.startActivityForResult(intent, this.b);
        } catch (Throwable th2) {
            this.a.finish();
            if (this.c != null) {
                this.c.onFailed(th2);
            }
        }
    }

    public void a(int i, int i2, Intent intent) {
        this.a.finish();
        if (i2 == 0) {
            if (this.c != null) {
                this.c.onCancel();
            }
        } else if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                if (this.c != null) {
                    this.c.onFailed(new Throwable("response is empty"));
                }
            } else if (extras.containsKey(Constants.KEY_RESPONSE)) {
                String string = extras.getString(Constants.KEY_RESPONSE);
                if (string != null && string.length() > 0) {
                    try {
                        JSONObject jSONObject = new JSONObject(string);
                        extras = new Bundle();
                        extras.putInt("ret", jSONObject.optInt("ret"));
                        extras.putString("pay_token", jSONObject.optString("pay_token"));
                        extras.putString(Constants.PARAM_PLATFORM_ID, jSONObject.optString(Constants.PARAM_PLATFORM_ID));
                        extras.putString("open_id", jSONObject.optString("openid"));
                        extras.putString("expires_in", jSONObject.optString("expires_in"));
                        extras.putString("pfkey", jSONObject.optString("pfkey"));
                        extras.putString("msg", jSONObject.optString("msg"));
                        extras.putString("access_token", jSONObject.optString("access_token"));
                        if (this.c != null) {
                            this.c.onComplete(extras);
                        }
                    } catch (Throwable th) {
                        if (this.c != null) {
                            this.c.onFailed(th);
                        }
                    }
                } else if (this.c != null) {
                    this.c.onFailed(new Throwable("response is empty"));
                }
            } else if (this.c != null) {
                this.c.onFailed(new Throwable("response is empty"));
            }
        } else if (this.c != null) {
            this.c.onFailed(new Throwable("response is empty"));
        }
    }

    public void a(String str, String str2) {
        this.d = str;
        this.e = str2;
    }
}
