package com.umeng.socialize.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.socialize.common.ResContainer;
import com.umeng.socialize.common.ResContainer.ResType;

/* compiled from: LoginAgent */
class a extends com.umeng.socialize.view.abs.SocialPopupDialog.a {
    final /* synthetic */ Context    a;
    final /* synthetic */ LoginAgent b;

    a(LoginAgent loginAgent, Context context, Context context2) {
        this.b = loginAgent;
        this.a = context2;
        super(context);
    }

    public void a(View view) {
        view.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                "umeng_socialize_title_bar_leftBt")).setOnClickListener(new b(this));
        view.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                "umeng_socialize_title_bar_rightBt")).setVisibility(8);
        ((Button) view.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                "umeng_socialize_title_bar_leftBt"))).setBackgroundResource(ResContainer
                .getResourceId(this.a, ResType.DRAWABLE, "umeng_socialize_action_back"));
        ((TextView) view.findViewById(ResContainer.getResourceId(this.a, ResType.ID,
                "umeng_socialize_title_bar_middleTv"))).setText(ResContainer.getResourceId(this
                .a, ResType.STRING, "umeng_socialize_login"));
    }
}
