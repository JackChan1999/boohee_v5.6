package com.umeng.socialize.view;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMShareBoardListener;
import com.umeng.socialize.view.abs.b;
import com.umeng.socialize.view.wigets.a;

/* compiled from: ShareBoard */
public class aj extends PopupWindow {
    private Context a = null;
    private b       b = null;
    private UMSocialService c;
    private SocializeConfig d = SocializeConfig.getSocializeConfig();
    private UMShareBoardListener e;

    public aj(Context context, b bVar, UMSocialService uMSocialService) {
        super(bVar, -1, -1, false);
        this.a = context;
        this.b = bVar;
        this.c = uMSocialService;
        this.b.a(a());
        setAnimationStyle(ResContainer.getResourceId(this.a, ResType.STYLE,
                "umeng_socialize_shareboard_animation"));
    }

    private a a() {
        return new ak(this, this.d.getAllPlatforms(this.a, this.c));
    }

    public void a(UMShareBoardListener uMShareBoardListener) {
        this.e = uMShareBoardListener;
    }

    public void showAtLocation(View view, int i, int i2, int i3) {
        try {
            super.showAtLocation(view, i, i2, i3);
            if (this.e != null) {
                this.e.onShow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.e != null) {
            this.e.onDismiss();
        }
    }
}
