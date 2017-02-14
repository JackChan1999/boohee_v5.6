package com.zxinsight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zxinsight.common.http.d;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class MWImageView extends ImageView {
    private String     a;
    private JSONObject b;
    private JSONObject c;

    public MWImageView(Context context) {
        super(context);
    }

    public MWImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void display(Context context, String str, JSONObject jSONObject) {
        if (MarketingHelper.currentMarketing(context).isActive(this.a)) {
            setOnClickListener(new g(this, str, jSONObject));
            d.a(context.getApplicationContext()).b().a(MarketingHelper.currentMarketing(context)
                    .getImageURL(this.a), new h(this));
        }
    }

    public void bindEvent(String str) {
        this.a = str;
        display(getContext(), null, null);
    }

    public void bindEventWithMLinkCallback(String str, String str2) {
        this.a = str;
        display(getContext(), str2, null);
    }

    public void bindEventWithMLinkCallback(String str, String str2, JSONObject jSONObject) {
        this.a = str;
        display(getContext(), str2, jSONObject);
    }

    public void bindEventWithMLinkCallback(String str, String str2, JSONObject jSONObject,
                                           JSONObject jSONObject2, JSONObject jSONObject3) {
        this.a = str;
        this.b = jSONObject2;
        this.c = jSONObject3;
        display(getContext(), str2, jSONObject);
    }

    public void bindEventWithMLink(String str, JSONObject jSONObject, JSONObject jSONObject2) {
        this.a = str;
        this.b = jSONObject;
        this.c = jSONObject2;
        display(getContext(), null, null);
    }

    public void bindEventWithMLink(String str, Map<String, String> map, Map<String, String> map2) {
        if (map == null) {
            map = new HashMap();
        }
        if (map2 == null) {
            map2 = new HashMap();
        }
        bindEventWithMLink(str, new JSONObject(map), new JSONObject(map2));
    }

    @Deprecated
    public void bindEvent(Context context, String str) {
        this.a = str;
        display(context, null, null);
    }
}
