package com.boohee.model;

import com.boohee.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.Date;

import org.json.JSONObject;

public class TabbarSetting {
    public String service_updated_at;
    public String shop_updated_at;

    public enum TabbarType {
        service,
        shop
    }

    public boolean isBadgeVisible(String lastest_clicked, TabbarType type) {
        Date lastest = TimeUtils.parseDateTime(lastest_clicked);
        Date updatedAt = null;
        switch (type) {
            case service:
                updatedAt = TimeUtils.parseDateTime(this.service_updated_at);
                break;
            case shop:
                updatedAt = TimeUtils.parseDateTime(this.shop_updated_at);
                break;
        }
        if (updatedAt == null) {
            return false;
        }
        if (lastest == null || (lastest != null && lastest.before(updatedAt))) {
            return true;
        }
        return false;
    }

    public static TabbarSetting parseSelf(JSONObject object) {
        if (object == null) {
            return null;
        }
        return (TabbarSetting) new Gson().fromJson(object.toString(), TabbarSetting.class);
    }
}
