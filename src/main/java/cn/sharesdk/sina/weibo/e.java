package cn.sharesdk.sina.weibo;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.boohee.status.UserTimelineActivity;
import com.mob.tools.utils.R;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

class e implements AuthorizeListener {
    final /* synthetic */ i a;
    final /* synthetic */ SinaWeibo b;

    e(SinaWeibo sinaWeibo, i iVar) {
        this.b = sinaWeibo;
        this.a = iVar;
    }

    public void onCancel() {
        if (this.b.listener != null) {
            this.b.listener.onCancel(this.b, 1);
        }
    }

    public void onComplete(Bundle bundle) {
        long parseLong;
        String string = bundle.getString(SocializeProtocolConstants.PROTOCOL_KEY_UID);
        String string2 = bundle.getString("access_token");
        String string3 = bundle.getString("expires_in");
        this.b.db.put(UserTimelineActivity.NICK_NAME, bundle.getString("userName"));
        this.b.db.put("remind_in", bundle.getString("remind_in"));
        this.b.db.putToken(string2);
        try {
            parseLong = R.parseLong(string3);
        } catch (Throwable th) {
            parseLong = 0;
        }
        this.b.db.putExpiresIn(parseLong);
        this.b.db.putUserId(string);
        this.a.b(string2);
        this.b.afterRegister(1, null);
    }

    public void onError(Throwable th) {
        if (this.b.listener != null) {
            this.b.listener.onError(this.b, 1, th);
        }
    }
}
