package cn.sharesdk.wechat.utils;

import android.text.TextUtils;
import cn.sharesdk.framework.PlatformActionListener;
import com.boohee.status.UserTimelineActivity;
import com.boohee.utils.Utils;
import com.mob.tools.network.KVPair;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import java.util.ArrayList;
import java.util.HashMap;

class i extends Thread {
    final /* synthetic */ PlatformActionListener a;
    final /* synthetic */ g b;

    i(g gVar, PlatformActionListener platformActionListener) {
        this.b = gVar;
        this.a = platformActionListener;
    }

    public void run() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KVPair("access_token", this.b.d.getDb().getToken()));
        arrayList.add(new KVPair("openid", this.b.d.getDb().get("openid")));
        String a = this.b.c.a("https://api.weixin.qq.com/sns/userinfo", arrayList, "/sns/userinfo", this.b.e);
        if (!TextUtils.isEmpty(a)) {
            Ln.d("getUserInfo ==>>" + a, new Object[0]);
            HashMap fromJson = new Hashon().fromJson(a);
            if (!fromJson.containsKey(Utils.RESPONSE_ERRCODE) || ((Integer) fromJson.get(Utils.RESPONSE_ERRCODE)).intValue() == 0) {
                String valueOf;
                String valueOf2;
                int parseInt;
                try {
                    valueOf = String.valueOf(fromJson.get("openid"));
                    valueOf2 = String.valueOf(fromJson.get(UserTimelineActivity.NICK_NAME));
                    parseInt = R.parseInt(String.valueOf(fromJson.get("sex")));
                } catch (Throwable th) {
                    Ln.e(th);
                    return;
                }
                String valueOf3 = String.valueOf(fromJson.get("province"));
                String valueOf4 = String.valueOf(fromJson.get("city"));
                String valueOf5 = String.valueOf(fromJson.get("country"));
                String valueOf6 = String.valueOf(fromJson.get("headimgurl"));
                String valueOf7 = String.valueOf(fromJson.get("unionid"));
                this.b.d.getDb().put(UserTimelineActivity.NICK_NAME, valueOf2);
                if (parseInt == 1) {
                    this.b.d.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "0");
                } else if (parseInt == 2) {
                    this.b.d.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "1");
                } else {
                    this.b.d.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "2");
                }
                this.b.d.getDb().putUserId(valueOf);
                this.b.d.getDb().put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, valueOf6);
                this.b.d.getDb().put("province", valueOf3);
                this.b.d.getDb().put("city", valueOf4);
                this.b.d.getDb().put("country", valueOf5);
                this.b.d.getDb().put("openid", valueOf);
                this.b.d.getDb().put("unionid", valueOf7);
                this.a.onComplete(this.b.d, 8, fromJson);
            } else if (this.a != null) {
                this.a.onError(this.b.d, 8, new Throwable(new Hashon().fromHashMap(fromJson)));
            }
        } else if (this.a != null) {
            this.a.onError(this.b.d, 8, new Throwable());
        }
    }
}
