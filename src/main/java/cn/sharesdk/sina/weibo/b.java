package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import android.text.TextUtils;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.boohee.status.UserTimelineActivity;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

class b implements AuthorizeListener {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void onCancel() {
        if (this.a.f != null) {
            this.a.f.onCancel(this.a.g, 1);
        }
        this.a.finish();
    }

    public void onComplete(Bundle bundle) {
        Object string = bundle.getString("access_token");
        if (TextUtils.isEmpty(string)) {
            String string2 = bundle.getString("code");
            if (this.a.f != null) {
                this.a.f.onError(this.a.g, 1, new Throwable("Error. Obtained the code: " + string2));
            }
        } else {
            this.a.g.getDb().putToken(string);
            this.a.g.getDb().putUserId(bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID));
            this.a.g.getDb().putExpiresIn(bundle.getLong("expires_in", 0));
            this.a.g.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN, bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_REFRESH_TOKEN));
            this.a.g.getDb().put(UserTimelineActivity.NICK_NAME, bundle.getString("userName"));
            this.a.g.getDb().put("remind_in", bundle.getString("remind_in"));
            if (this.a.f != null) {
                this.a.f.onComplete(this.a.g, 1, null);
            }
        }
        this.a.finish();
    }

    public void onError(Throwable th) {
        if (this.a.f != null) {
            this.a.f.onError(this.a.g, 1, th);
        }
        this.a.finish();
    }
}
