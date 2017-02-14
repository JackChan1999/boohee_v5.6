package com.zxinsight;

import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class RenderParamBuilder {
    protected JSONObject     dt;
    protected RenderListener listener;
    protected JSONObject     lp;
    protected View           parent;
    protected String         windowKey;

    public RenderParamBuilder windowKey(String str) {
        this.windowKey = str;
        return this;
    }

    public RenderParamBuilder parent(View view) {
        this.parent = view;
        return this;
    }

    public RenderParamBuilder dt(JSONObject jSONObject) {
        this.dt = jSONObject;
        return this;
    }

    public RenderParamBuilder lp(JSONObject jSONObject) {
        this.lp = jSONObject;
        return this;
    }

    public RenderParamBuilder dt(Map<String, String> map) {
        if (map == null) {
            map = new HashMap();
        }
        this.dt = new JSONObject(map);
        return this;
    }

    public RenderParamBuilder lp(Map<String, String> map) {
        if (map == null) {
            map = new HashMap();
        }
        this.dt = new JSONObject(map);
        return this;
    }

    public RenderParamBuilder renderListener(RenderListener renderListener) {
        this.listener = renderListener;
        return this;
    }

    public RenderParam build() {
        if (!TextUtils.isEmpty(this.windowKey) && this.parent != null && this.listener != null) {
            return new RenderParam(this);
        }
        throw new IllegalArgumentException("windowKey, parent and listener must not be null");
    }
}
