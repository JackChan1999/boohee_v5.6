package com.umeng.socialize.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.common.SocialSNSHelper;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.LoginListener;
import com.umeng.socialize.utils.LoginInfoHelp;
import com.umeng.socialize.utils.SocializeUtils;
import com.umeng.socialize.view.abs.SocialPopupDialog;
import com.umeng.socialize.view.abs.SocialPopupDialog.a;
import com.umeng.socialize.view.abs.SocialPopupDialog.b;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LoginAgent {
    private SocialPopupDialog a;
    private Context           b;
    private UMSocialService   c;
    private List<SnsPlatform> d;
    private Map<SnsPlatform, View> e = a(this.d);
    private LoginListener f;

    public LoginAgent(Context context, String str, LoginListener loginListener) {
        this.b = context;
        this.f = loginListener;
        this.c = UMServiceFactory.getUMSocialService(str);
        this.d = SocialSNSHelper.getSupprotCloudPlatforms(context, this.c.getConfig());
        a aVar = new a(this, context, context);
        Set<SnsPlatform> keySet = this.e.keySet();
        if (keySet == null || keySet.size() == 0) {
            aVar.a(8);
        } else {
            for (SnsPlatform snsPlatform : keySet) {
                aVar.a((View) this.e.get(snsPlatform), null);
            }
        }
        aVar.a(context.getResources().getString(ResContainer.getResourceId(context, ResType
                .STRING, "umeng_socialize_text_choose_account")));
        aVar.b(0);
        aVar.b("");
        b bVar = new b(context);
        bVar.a(ResContainer.getResourceId(context, ResType.DRAWABLE,
                "umeng_socialize_default_avatar"));
        bVar.a(context.getResources().getString(ResContainer.getResourceId(context, ResType
                .STRING, "umeng_socialize_text_visitor")));
        bVar.a(new c(this));
        aVar.b(bVar.a(), null);
        if (SocializeUtils.isFloatWindowStyle(context)) {
            int[] floatWindowSize = SocializeUtils.getFloatWindowSize(context);
            aVar.a(floatWindowSize[0], floatWindowSize[1]);
        }
        this.a = aVar.a();
        if (context instanceof Activity) {
            this.a.setOwnerActivity((Activity) context);
        }
        this.a.a(new d(this));
    }

    public void showLoginDialog() {
        if (!LoginInfoHelp.isPlatformLogin(this.b) && !LoginInfoHelp.isCustomLogin(this.b)) {
            SocializeUtils.safeShowDialog(this.a);
        } else if (this.f != null) {
            this.f.loginSuccessed(LoginInfoHelp.getLoginInfo(this.b), true);
        }
    }

    public void dismissLoginDialog() {
        SocializeUtils.safeCloseDialog(this.a);
    }

    private Map<SnsPlatform, View> a(List<SnsPlatform> list) {
        Map<SnsPlatform, View> orderMap = getOrderMap();
        for (SnsPlatform snsPlatform : list) {
            b bVar = new b(this.b);
            if (snsPlatform.mKeyword.equals("qzone")) {
                bVar.a(ResContainer.getResourceId(this.b, ResType.DRAWABLE,
                        "umeng_socialize_qzone_on"));
                bVar.a(this.b.getResources().getString(ResContainer.getResourceId(this.b, ResType
                        .STRING, "umeng_socialize_login_qq")));
            } else {
                bVar.a(snsPlatform.mIcon);
                bVar.a(snsPlatform.mShowWord);
            }
            bVar.a(new e(this, snsPlatform));
            orderMap.put(snsPlatform, bVar.a());
        }
        return orderMap;
    }

    private void a(SHARE_MEDIA share_media) {
        if (share_media != null) {
            this.c.login(this.b, share_media, new g(this, share_media));
        } else {
            this.c.loginout(this.b, new h(this));
        }
    }

    public Map<SnsPlatform, View> getOrderMap() {
        return new TreeMap(new i(this));
    }
}
