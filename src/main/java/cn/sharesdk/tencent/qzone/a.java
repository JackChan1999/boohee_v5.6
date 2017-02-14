package cn.sharesdk.tencent.qzone;

import android.os.Bundle;
import cn.sharesdk.framework.authorize.AuthorizeListener;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;

class a implements AuthorizeListener {
    final /* synthetic */ f a;
    final /* synthetic */ QZone b;

    a(QZone qZone, f fVar) {
        this.b = qZone;
        this.a = fVar;
    }

    public void onCancel() {
        if (this.b.listener != null) {
            this.b.listener.onCancel(this.b, 1);
        }
    }

    public void onComplete(Bundle bundle) {
        String string = bundle.getString("open_id");
        String string2 = bundle.getString("access_token");
        String string3 = bundle.getString("expires_in");
        this.b.db.putToken(string2);
        this.b.db.putTokenSecret("");
        try {
            this.b.db.putExpiresIn(R.parseLong(string3));
        } catch (Throwable th) {
            Ln.e(th);
        }
        this.b.db.putUserId(string);
        this.a.b(string);
        this.a.c(string2);
        this.b.afterRegister(1, null);
    }

    public void onError(Throwable th) {
        if (this.b.listener != null) {
            this.b.listener.onError(this.b, 1, th);
        }
    }
}
