package cn.sharesdk.framework;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.statistics.b;
import cn.sharesdk.framework.statistics.b.c;
import cn.sharesdk.framework.statistics.b.f;
import com.boohee.status.UserTimelineActivity;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Ln;
import com.qiniu.android.common.Constants;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;
import com.zxinsight.share.domain.BMPlatform;
import java.util.HashMap;

public class a implements PlatformActionListener {
    private PlatformActionListener a;

    a() {
    }

    private String a(Platform platform) {
        try {
            return a(platform.getDb(), new String[]{UserTimelineActivity.NICK_NAME, SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "snsUserUrl", "resume", "secretType", "secret", "birthday", "followerCount", "favouriteCount", "shareCount", "snsregat", "snsUserLevel", "educationJSONArrayStr", "workJSONArrayStr"});
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private String a(PlatformDb platformDb, String[] strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            if (i > 0) {
                stringBuilder2.append('|');
                stringBuilder.append('|');
            }
            i++;
            String str2 = platformDb.get(str2);
            if (!TextUtils.isEmpty(str2)) {
                stringBuilder.append(str2);
                stringBuilder2.append(Data.urlEncode(str2, Constants.UTF_8));
            }
        }
        Ln.e("======UserData: " + stringBuilder.toString(), new Object[0]);
        return stringBuilder2.toString();
    }

    private void a(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.a = new b(this, this.a, i, hashMap);
        platform.showUser(null);
    }

    private String b(Platform platform) {
        try {
            return a(platform.getDb(), new String[]{SocializeProtocolConstants.PROTOCOL_KEY_GENDER, "birthday", "secretType", "educationJSONArrayStr", "workJSONArrayStr"});
        } catch (Throwable th) {
            Ln.e(th);
            return null;
        }
    }

    private void b(Platform platform, int i, HashMap<String, Object> hashMap) {
        ShareParams shareParams = hashMap != null ? (ShareParams) hashMap.remove("ShareParams") : null;
        try {
            HashMap hashMap2 = (HashMap) hashMap.clone();
        } catch (Throwable th) {
            Ln.e(th);
            HashMap<String, Object> hashMap3 = hashMap;
        }
        if (shareParams != null) {
            c fVar = new f();
            fVar.o = shareParams.getCustomFlag();
            fVar.b = BMPlatform.NAME_TENCENTWEIBO.equals(platform.getName()) ? platform.getDb().get("name") : platform.getDb().getUserId();
            fVar.a = platform.getPlatformId();
            cn.sharesdk.framework.statistics.b.f.a filterShareContent = platform.filterShareContent(shareParams, hashMap2);
            if (filterShareContent != null) {
                fVar.c = filterShareContent.a;
                fVar.d = filterShareContent;
            }
            fVar.n = b(platform);
            b.a(platform.getContext()).a(fVar);
        }
        if (this.a != null) {
            try {
                this.a.onComplete(platform, i, hashMap);
            } catch (Throwable th2) {
                Ln.w(th2);
            }
        }
    }

    PlatformActionListener a() {
        return this.a;
    }

    void a(Platform platform, int i, Object obj) {
        this.a = new c(this, this.a, i, obj);
        platform.doAuthorize(null);
    }

    void a(PlatformActionListener platformActionListener) {
        this.a = platformActionListener;
    }

    public void onCancel(Platform platform, int i) {
        if (this.a != null) {
            this.a.onCancel(platform, i);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (!(platform instanceof CustomPlatform)) {
            switch (i) {
                case 1:
                    a(platform, i, (HashMap) hashMap);
                    return;
                case 9:
                    b(platform, i, hashMap);
                    return;
                default:
                    if (this.a != null) {
                        this.a.onComplete(platform, i, hashMap);
                        return;
                    }
                    return;
            }
        } else if (this.a != null) {
            this.a.onComplete(platform, i, hashMap);
        }
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.a != null) {
            this.a.onError(platform, i, th);
        }
    }
}
