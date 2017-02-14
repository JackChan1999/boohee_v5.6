package cn.sharesdk.framework;

import cn.sharesdk.framework.statistics.b.c;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.Ln;
import com.zxinsight.share.domain.BMPlatform;
import java.util.HashMap;

class b implements PlatformActionListener {
    final /* synthetic */ PlatformActionListener a;
    final /* synthetic */ int b;
    final /* synthetic */ HashMap c;
    final /* synthetic */ a d;

    b(a aVar, PlatformActionListener platformActionListener, int i, HashMap hashMap) {
        this.d = aVar;
        this.a = platformActionListener;
        this.b = i;
        this.c = hashMap;
    }

    public void onCancel(Platform platform, int i) {
        this.d.a = this.a;
        if (this.d.a != null) {
            this.d.a.onComplete(platform, this.b, this.c);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.d.a = this.a;
        if (this.d.a != null) {
            this.d.a.onComplete(platform, this.b, this.c);
        }
        c bVar = new cn.sharesdk.framework.statistics.b.b();
        bVar.a = platform.getPlatformId();
        bVar.b = BMPlatform.NAME_TENCENTWEIBO.equals(platform.getName()) ? platform.getDb().get("name") : platform.getDb().getUserId();
        bVar.c = new Hashon().fromHashMap(hashMap);
        bVar.d = this.d.a(platform);
        cn.sharesdk.framework.statistics.b.a(platform.getContext()).a(bVar);
    }

    public void onError(Platform platform, int i, Throwable th) {
        Ln.e(th);
        this.d.a = this.a;
        if (this.d.a != null) {
            this.d.a.onComplete(platform, this.b, this.c);
        }
    }
}
