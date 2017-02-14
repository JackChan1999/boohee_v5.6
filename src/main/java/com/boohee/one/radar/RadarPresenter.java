package com.boohee.one.radar;

import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.boohee.model.UploadFood;
import com.boohee.model.User;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.radar.entity.Balance;
import com.boohee.one.radar.entity.Dietary;
import com.boohee.one.radar.entity.Element;
import com.boohee.one.radar.entity.Radar;
import com.boohee.one.radar.entity.Social;
import com.boohee.one.radar.entity.Spirit;
import com.boohee.one.radar.entity.Sports;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;

import java.util.Map;

import org.json.JSONObject;

public class RadarPresenter {
    private final String URL = "/api/v2/smart_radars/current";
    private RadarActivity mActivity;
    private Handler mHandler = new Handler();
    private int    retryTime;
    private String status;

    public RadarPresenter(RadarActivity activity) {
        this.mActivity = activity;
    }

    public void onCreate(String status, String type) {
        this.status = status;
        if (User.WEEK_RESPORT_USER_NEW.equalsIgnoreCase(type)) {
            this.mActivity.showNew();
            return;
        }
        this.mActivity.showLoadingView();
        initData();
    }

    public void onStop() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void initData() {
        BooheeClient.build("record").get("/api/v2/smart_radars/current", new JsonCallback(this
                .mActivity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object != null) {
                    if (User.WEEK_RESPORT_USER_NEW.equalsIgnoreCase(object.optString(UploadFood
                            .STATE))) {
                        RadarPresenter.this.mActivity.showNew();
                    } else if ("loading".equalsIgnoreCase(object.optString(UploadFood.STATE))) {
                        RadarPresenter.this.retryRequest();
                    } else {
                        RadarPresenter.this.initView(object);
                    }
                }
            }
        }, this.mActivity);
    }

    private void retryRequest() {
        if (this.retryTime >= 5) {
            Helper.showToast((CharSequence) "获取数据失败，请稍后再试");
            return;
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                RadarPresenter.this.initData();
            }
        }, 2000);
        this.retryTime++;
    }

    private void initView(JSONObject jsonObject) {
        if (jsonObject != null) {
            this.mActivity.hideEmptyView();
            Radar radar = (Radar) extractData(jsonObject.optString("radar"), Radar.class);
            if (User.WEEK_REPORT_STATUS_UPDATED.equals(this.status)) {
                this.mActivity.setRadar(radar, true);
            } else {
                this.mActivity.setRadar(radar, false);
            }
            this.mActivity.setDietary((Dietary) extractData(jsonObject.optJSONObject("nutrition")
                    .optString("dietary"), Dietary.class));
            this.mActivity.setElement((Element) extractData(jsonObject.optJSONObject("nutrition")
                    .optString("element"), Element.class));
            this.mActivity.setSpirit((Spirit) extractData(jsonObject.optString("spirit"), Spirit
                    .class));
            this.mActivity.setBalance((Balance) extractData(jsonObject.optString("balance"),
                    Balance.class));
            this.mActivity.setSports((Sports) extractData(jsonObject.optString("sports"), Sports
                    .class));
            this.mActivity.setSocial((Social) extractData(jsonObject.optString("social"), Social
                    .class));
            this.mActivity.setSummary((Map) FastJsonUtils.fromJson(jsonObject.optString
                    ("summary"), new TypeReference<Map<String, String>>() {
            }));
        }
    }

    private <T> T extractData(String s, Class<T> clazz) {
        if ("{}".equals(s) || TextUtils.isEmpty(s)) {
            return null;
        }
        return FastJsonUtils.fromJson(s, (Class) clazz);
    }
}
