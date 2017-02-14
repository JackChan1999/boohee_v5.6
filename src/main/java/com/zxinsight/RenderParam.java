package com.zxinsight;

import android.view.View;

import org.json.JSONObject;

public class RenderParam {
    private JSONObject     dt;
    private RenderListener listener;
    private JSONObject     lp;
    private View           parent;
    private String         windowKey;

    public RenderParam(RenderParamBuilder renderParamBuilder) {
        this.windowKey = renderParamBuilder.windowKey;
        this.parent = renderParamBuilder.parent;
        this.dt = renderParamBuilder.dt;
        this.lp = renderParamBuilder.lp;
        this.listener = renderParamBuilder.listener;
    }

    public String getWindowKey() {
        return this.windowKey;
    }

    public void setWindowKey(String str) {
        this.windowKey = str;
    }

    public View getParent() {
        return this.parent;
    }

    public void setParent(View view) {
        this.parent = view;
    }

    public JSONObject getDt() {
        return this.dt;
    }

    public void setDt(JSONObject jSONObject) {
        this.dt = jSONObject;
    }

    public JSONObject getLp() {
        return this.lp;
    }

    public void setLp(JSONObject jSONObject) {
        this.lp = jSONObject;
    }

    public RenderListener getListener() {
        return this.listener;
    }

    public void setListener(RenderListener renderListener) {
        this.listener = renderListener;
    }
}
