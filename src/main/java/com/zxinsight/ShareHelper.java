package com.zxinsight;

import android.app.Activity;

import com.zxinsight.common.util.c;
import com.zxinsight.common.util.l;
import com.zxinsight.common.util.m;
import com.zxinsight.share.b.a;
import com.zxinsight.share.domain.BMPlatform;
import com.zxinsight.share.domain.b;

public class ShareHelper {
    static void share(Activity activity, String str, String str2) {
        if (activity != null) {
            if (!m.a().p()) {
                c.a("please input and open share platform on website");
            } else if (MarketingHelper.currentMarketing(activity).isActive(str)) {
                if (l.a(str2)) {
                    str2 = MarketingHelper.currentMarketing(activity).getSharedURL(str);
                }
                String str3 = "Share";
                String str4 = "Shared by MagicWindow";
                if (l.b(MarketingHelper.currentMarketing(activity).getShareTitle(str))) {
                    str3 = MarketingHelper.currentMarketing(activity).getShareTitle(str);
                    str4 = MarketingHelper.currentMarketing(activity).getShareTitle(str);
                }
                if (l.b(MarketingHelper.currentMarketing(activity).getShareText(str))) {
                    str4 = MarketingHelper.currentMarketing(activity).getShareText(str);
                }
                b bVar = new b();
                bVar.a(str3);
                bVar.b(str4);
                bVar.e(str2);
                bVar.d(MarketingHelper.currentMarketing(activity).getThumbURL(str));
                a yVar = new y(str);
                com.zxinsight.share.a aVar = new com.zxinsight.share.a(activity, str);
                aVar.a(bVar);
                aVar.a(BMPlatform.PLATFORM_WXSESSION, yVar);
                aVar.a(BMPlatform.PLATFORM_WXTIMELINE, yVar);
                aVar.a(BMPlatform.PLATFORM_SINAWEIBO, yVar);
                aVar.a(BMPlatform.PLATFORM_QQ, yVar);
                aVar.a(BMPlatform.PLATFORM_QZONE, yVar);
                aVar.a();
            } else {
                c.d("ShareHelper share failed,the windowKey:" + str + " is not closed");
            }
        }
    }

    public static void share(Activity activity, String str) {
        share(activity, str, null);
    }
}
