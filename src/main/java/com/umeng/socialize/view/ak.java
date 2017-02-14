package com.umeng.socialize.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsPlatform;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;
import com.umeng.socialize.utils.ListenerUtils;
import com.umeng.socialize.view.wigets.a;

import java.util.List;

/* compiled from: ShareBoard */
class ak extends a {
    final /* synthetic */ List a;
    final /* synthetic */ aj   b;

    ak(aj ajVar, List list) {
        this.b = ajVar;
        this.a = list;
    }

    public View a(int i, ViewGroup viewGroup) {
        SnsPlatform snsPlatform = (SnsPlatform) this.a.get(i);
        a(snsPlatform);
        View inflate = View.inflate(this.b.a, ResContainer.getResourceId(this.b.a, ResType
                .LAYOUT, "umeng_socialize_shareboard_item"), null);
        a(inflate, snsPlatform);
        inflate.setOnClickListener(new al(this, snsPlatform));
        inflate.setOnTouchListener(new am(this, inflate));
        inflate.setFocusable(true);
        return inflate;
    }

    private void a(View view, SnsPlatform snsPlatform) {
        ((ImageView) view.findViewById(ResContainer.getResourceId(this.b.a, ResType.ID,
                "umeng_socialize_shareboard_image"))).setImageResource(snsPlatform.mIcon);
        ((TextView) view.findViewById(ResContainer.getResourceId(this.b.a, ResType.ID,
                "umeng_socialize_shareboard_pltform_name"))).setText(snsPlatform.mShowWord);
    }

    private void a(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
        if (share_media != null) {
            this.b.c.getEntity().addStatisticsData(this.b.a, share_media, 14);
        }
        if (snsPlatform != null) {
            SocializeConfig.setSelectedPlatfrom(share_media);
            snsPlatform.performClick(this.b.a, this.b.c.getEntity(), ListenerUtils
                    .createSnsPostListener());
        }
    }

    private void a(SnsPlatform snsPlatform) {
        if (snsPlatform.mIcon == -1 && snsPlatform.mPlatform == SHARE_MEDIA.EMAIL) {
            snsPlatform.mIcon = ResContainer.getResourceId(this.b.a, ResType.DRAWABLE,
                    "umeng_socialize_gmail_on");
        } else if (snsPlatform.mIcon == -1 && snsPlatform.mPlatform == SHARE_MEDIA.SMS) {
            snsPlatform.mIcon = ResContainer.getResourceId(this.b.a, ResType.DRAWABLE,
                    "umeng_socialize_sms_on");
        }
    }

    public Object a(int i) {
        return this.a == null ? null : (SnsPlatform) this.a.get(i);
    }

    public int a() {
        return this.a == null ? 0 : this.a.size();
    }
}
