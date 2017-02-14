package com.zxinsight.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import cn.sharesdk.framework.ShareSDK;

import com.zxinsight.MWConfiguration;
import com.zxinsight.common.base.MWActivity;
import com.zxinsight.common.util.c;
import com.zxinsight.common.util.e;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.common.util.o;
import com.zxinsight.share.activity.ActivityFactory;
import com.zxinsight.share.activity.MWWXEntryActivity;
import com.zxinsight.share.activity.MWWXHandlerActivity;
import com.zxinsight.share.domain.BMPlatform;
import com.zxinsight.share.domain.b;

import java.io.IOException;
import java.util.HashMap;

public class a {
    private static HashMap<BMPlatform, com.zxinsight.share.b.a> b = new HashMap();
    private static Handler    f;
    private static BMPlatform g;
    private        Activity   a;
    private HashMap<BMPlatform, b> c = new HashMap();
    private b      d;
    private String e;

    public a(Activity activity, String str) {
        this.a = activity;
        this.e = str;
        f = new d(activity);
    }

    public b a(BMPlatform bMPlatform) {
        return (b) this.c.get(bMPlatform);
    }

    public void a(BMPlatform bMPlatform, com.zxinsight.share.b.a aVar) {
        c.c("**1*** " + bMPlatform + " --- " + aVar);
        b.put(bMPlatform, aVar);
    }

    public static com.zxinsight.share.b.a b(BMPlatform bMPlatform) {
        return (com.zxinsight.share.b.a) b.get(bMPlatform);
    }

    public void a() {
        if (this.a == null) {
            return;
        }
        if (l.b(BMPlatform.getOpenedShare())) {
            try {
                new com.zxinsight.share.d.a(this.a, this, this.d, this.e).show();
                return;
            } catch (Exception e) {
                return;
            }
        }
        c.a(o.a("此活动后台分享平台已全部关闭", "There is no share platform on WeChat"));
    }

    public void a(b bVar) {
        this.d = bVar;
    }

    public void a(BMPlatform bMPlatform, b bVar) {
        switch (b.a[bMPlatform.ordinal()]) {
            case 1:
                if (a(BMPlatform.PLATFORM_WXSESSION) != null) {
                    b(BMPlatform.PLATFORM_WXSESSION, a(BMPlatform.PLATFORM_WXSESSION));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_WXSESSION, bVar);
                    return;
                }
            case 2:
                c.c("***** " + b.size());
                c.c("***** " + b(BMPlatform.PLATFORM_WXTIMELINE));
                if (a(BMPlatform.PLATFORM_WXTIMELINE) != null) {
                    b(BMPlatform.PLATFORM_WXTIMELINE, a(BMPlatform.PLATFORM_WXTIMELINE));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_WXTIMELINE, bVar);
                    return;
                }
            case 3:
                if (a(BMPlatform.PLATFORM_QQ) != null) {
                    b(BMPlatform.PLATFORM_QQ, a(BMPlatform.PLATFORM_QQ));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_QQ, bVar);
                    return;
                }
            case 4:
                if (a(BMPlatform.PLATFORM_QZONE) != null) {
                    b(BMPlatform.PLATFORM_QZONE, a(BMPlatform.PLATFORM_QZONE));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_QZONE, bVar);
                    return;
                }
            case 5:
                if (a(BMPlatform.PLATFORM_TENCENTWEIBO) != null) {
                    b(BMPlatform.PLATFORM_TENCENTWEIBO, a(BMPlatform.PLATFORM_TENCENTWEIBO));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_TENCENTWEIBO, bVar);
                    return;
                }
            case 6:
                if (a(BMPlatform.PLATFORM_SINAWEIBO) != null) {
                    b(BMPlatform.PLATFORM_SINAWEIBO, a(BMPlatform.PLATFORM_SINAWEIBO));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_SINAWEIBO, bVar);
                    return;
                }
            case 7:
                if (a(BMPlatform.PLATFORM_RENN) != null) {
                    b(BMPlatform.PLATFORM_RENN, a(BMPlatform.PLATFORM_RENN));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_RENN, bVar);
                    return;
                }
            case 8:
                if (a(BMPlatform.PLATFORM_EMAIL) != null) {
                    b(BMPlatform.PLATFORM_EMAIL, a(BMPlatform.PLATFORM_EMAIL));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_EMAIL, bVar);
                    return;
                }
            case 9:
                if (a(BMPlatform.PLATFORM_MESSAGE) != null) {
                    b(BMPlatform.PLATFORM_MESSAGE, a(BMPlatform.PLATFORM_MESSAGE));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_MESSAGE, bVar);
                    return;
                }
            case 10:
                if (a(BMPlatform.PLATFORM_COPYLINK) != null) {
                    b(BMPlatform.PLATFORM_COPYLINK, a(BMPlatform.PLATFORM_COPYLINK));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_COPYLINK, bVar);
                    return;
                }
            case 11:
                if (a(BMPlatform.PLATFORM_MORE_SHARE) != null) {
                    b(BMPlatform.PLATFORM_MORE_SHARE, a(BMPlatform.PLATFORM_MORE_SHARE));
                    return;
                } else {
                    b(BMPlatform.PLATFORM_MORE_SHARE, bVar);
                    return;
                }
            default:
                return;
        }
    }

    private void b(BMPlatform bMPlatform, b bVar) {
        g = bMPlatform;
        o.a(this.a, "Waiting...", false);
        if (b(bMPlatform) != null) {
            b(bMPlatform).b(BMPlatform.getIDByPlatform(bMPlatform));
        }
        new c(bVar).execute(new Void[0]);
    }

    private static void b(Context context, BMPlatform bMPlatform, b bVar) {
        String e = bVar.e();
        if (m.a().o()) {
            c.d("aaron share sdk");
            ShareSDK.initSDK(context);
            if (bMPlatform == BMPlatform.PLATFORM_WXSESSION) {
                com.zxinsight.share.c.a.a(context, bVar);
            } else if (bMPlatform == BMPlatform.PLATFORM_WXTIMELINE) {
                com.zxinsight.share.c.a.b(context, bVar);
            }
        } else if (bMPlatform == BMPlatform.PLATFORM_WXSESSION || bMPlatform == BMPlatform
                .PLATFORM_WXTIMELINE) {
            ActivityFactory activityFactory = new ActivityFactory(context, MWActivity
                    .ACTIVITY_TYPE_WX);
            Intent intent = activityFactory.getIntent();
            MWWXHandlerActivity.setListener(b(bMPlatform));
            intent.putExtra("platform", bMPlatform);
            intent.putExtra("fromShare", true);
            intent.putExtra("realUrl", e);
            MWWXEntryActivity.shareData = bVar;
            activityFactory.startActivity();
        }
    }

    private static b c(b bVar) {
        String d = bVar.d();
        if (l.b(d)) {
            if (d.matches(".*\\?.*")) {
                d = d.substring(0, d.lastIndexOf("?"));
            }
            String substring = d.substring(d.lastIndexOf("/") + 1, d.length());
            d = null;
            try {
                d = e.a(bVar.d(), substring);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bVar.c(e.a(MWConfiguration.getContext()) + substring);
            if (d == null || !d.equalsIgnoreCase("SUCCESS")) {
                f.sendEmptyMessage(1);
            } else {
                f.sendMessage(Message.obtain(f, 0, bVar));
            }
        } else {
            f.sendEmptyMessage(1);
        }
        return bVar;
    }
}
