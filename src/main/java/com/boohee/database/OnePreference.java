package com.boohee.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.boohee.model.WeightScale;
import com.boohee.one.MyApplication;
import com.boohee.utility.Config;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.FastJsonUtils;

import java.util.Date;

import org.json.JSONObject;

public class OnePreference {
    public static final int    NO_CAN_CANCALORY               = -99999;
    public static final String PREFS_LATEST_WEIGHT            = "prefs_latest_weight";
    public static final String PREFS_NAME                     = OnePreference.class.getSimpleName();
    public static final String PREFS_SHOP_UPDATE_AT           = "prefs_shop_update_at";
    public static final String PREF_ACCEPT_PUSH               = "pref_accept_push";
    public static final String PREF_ADD_FOOD_GUIDE            = "pref_add_food_guide";
    public static final String PREF_CANCALORY                 = "pref_cancalory";
    public static final String PREF_CHANNEL                   = "pref_channel";
    public static final String PREF_COOK_FOOD_SEARCH          = "pref_cook_food_search";
    public static final String PREF_COURSE_LEVEL              = "pref_course_level";
    public static final String PREF_DIAMOND_SIGN_REMIND       = "pref_diamond_sign_remind";
    public static final String PREF_DIET_FOOD_GUIDE           = "pref_diet_food_guide";
    public static final String PREF_ESTIMATE_FOOD_GUIDE       = "pref_estimate_food_guide";
    public static final String PREF_FIRST_ONEKEY              = "pref_first_onekey";
    public static final String PREF_FOOD_SEARCH_HISTORY       = "pref_food_search_history";
    public static final String PREF_GUIDE_DIET                = "pref_guide_diet";
    public static final String PREF_GUIDE_DIET_CHART          = "pref_guide_diet_chart";
    public static final String PREF_GUIDE_HOME                = "pref_guide_home";
    public static final String PREF_GUIDE_MINE                = "pref_guide_mine";
    public static final String PREF_GUIDE_PARTNER             = "pref_guide_partner";
    public static final String PREF_GUIDE_TOOL                = "pref_guide_tool";
    public static final String PREF_HOME_MENU                 = "pref_home_menu";
    public static final String PREF_HOME_MY_PLAN              = "pref_home_My_plan";
    public static final String PREF_HOME_NEW_GUIDE            = "pref_home_new_guide";
    public static final String PREF_HOME_NEW_GUIDE_5_5_2      = "pref_home_new_guide_2";
    public static final String PREF_NICE_SERVICE_GUIDE        = "pref_nice_service_guide";
    public static final String PREF_NICE_SERVICE_GUIDE_SECOND = "pref_nice_service_guide_second";
    public static final String PREF_NICE_SERVICE_GUIDE_THIRD  = "pref_nice_service_guide_third";
    public static final String PREF_SEARCH_FOOD_GUIDE         = "pref_search_food_guide";
    public static final String PREF_SHOW_UNIT                 = "pref_show_unit";
    public static final String PREF_SIGN_RECORD               = "pref_diamond_sign_record";
    public static final String PREF_SPORT_REMIND              = "pref_sport_remind";
    public static final String PREF_SPORT_REMIND_TIME         = "pref_sport_remind_time";
    public static final String PREF_SPORT_SEARCH_HISTORY      = "pref_sport_search_history";
    public static final String PREF_START_UP_URL              = "pref_start_up_url";
    public static final String PREF_TOOL_CHOOSE               = "pref_tool_choose";
    public static final String PREF_VERSION_CODE              = "pref_version_code";
    public static final String PREF_WEIGHT_SCALE              = "pref_weight_scale";
    private static OnePreference     preference;
    private        SharedPreferences setting;

    public OnePreference(Context ctx) {
        this.setting = ctx.getSharedPreferences(PREFS_NAME, 0);
    }

    public static OnePreference getInstance(Context context) {
        if (preference == null) {
            preference = new OnePreference(context);
        }
        return preference;
    }

    public Editor getEditor() {
        return this.setting.edit();
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return this.setting.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean value) {
        return this.setting.getBoolean(key, value);
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public long getLong(String key) {
        return this.setting.getLong(key, 0);
    }

    public void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public float getFloat(String key) {
        return this.setting.getFloat(key, 0.0f);
    }

    public void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    public String getString(String key) {
        return this.setting.getString(key, null);
    }

    public int getInt(String key) {
        return this.setting.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return this.setting.getInt(key, defaultValue);
    }

    public String getString(String key, String default_value) {
        return this.setting.getString(key, default_value);
    }

    public static int getReceivePush(Context context) {
        return getInstance(context).getInt("isReceivePush", 1);
    }

    public static void putReceivePush(Context context, int value) {
        getInstance(context).putInt("isReceivePush", value);
    }

    public boolean contains(String key) {
        return this.setting.contains(key);
    }

    public String getShopUpdateAt() {
        return getString(PREFS_SHOP_UPDATE_AT, "");
    }

    public void setShopUpdateAt(String shopUpdateAt) {
        putString(PREFS_SHOP_UPDATE_AT, shopUpdateAt);
    }

    public static void setLatestWeight(float weight) {
        getInstance(MyApplication.getContext()).putFloat(PREFS_LATEST_WEIGHT, weight);
    }

    public static float getLatestWeight() {
        return getInstance(MyApplication.getContext()).getFloat(PREFS_LATEST_WEIGHT);
    }

    public static boolean isShowCaloryUnit() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_SHOW_UNIT, true);
    }

    public static void setShowUnit(boolean isCalory) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_SHOW_UNIT, isCalory);
    }

    public String getSearchHistory() {
        return getString(PREF_FOOD_SEARCH_HISTORY, "");
    }

    public void setSearchHistory(String historyString) {
        putString(PREF_FOOD_SEARCH_HISTORY, historyString);
    }

    public void clearSearchHistory() {
        remove(PREF_FOOD_SEARCH_HISTORY);
    }

    public String getSportHistory() {
        return getString(PREF_SPORT_SEARCH_HISTORY, "");
    }

    public void setSportHistory(String historyString) {
        putString(PREF_SPORT_SEARCH_HISTORY, historyString);
    }

    public void clearSportHistory() {
        remove(PREF_SPORT_SEARCH_HISTORY);
    }

    public int getCanCalory() {
        return getInt(PREF_CANCALORY, NO_CAN_CANCALORY);
    }

    public void setCanCalory(int canCalory) {
        putInt(PREF_CANCALORY, canCalory);
    }

    public boolean isGuideTool() {
        return getBoolean(PREF_GUIDE_TOOL, false);
    }

    public void setGuideTool(boolean status) {
        putBoolean(PREF_GUIDE_TOOL, status);
    }

    public boolean isGuideMine() {
        return getBoolean(PREF_GUIDE_MINE, false);
    }

    public void setGuideMine(boolean status) {
        putBoolean(PREF_GUIDE_MINE, status);
    }

    public boolean isGuideDiet() {
        return getBoolean(PREF_GUIDE_DIET, false);
    }

    public void setGuideDiet(boolean status) {
        putBoolean(PREF_GUIDE_DIET, status);
    }

    public boolean isFirstOneKey() {
        return getBoolean(PREF_FIRST_ONEKEY);
    }

    public void setFirstOneKey(boolean status) {
        putBoolean(PREF_FIRST_ONEKEY, status);
    }

    public boolean isGuideHome() {
        return getBoolean(PREF_GUIDE_HOME);
    }

    public void setGuideHome(boolean status) {
        putBoolean(PREF_GUIDE_HOME, status);
    }

    public boolean isGuidePartner() {
        return getBoolean(PREF_GUIDE_PARTNER, false);
    }

    public void setGuidePartner(boolean status) {
        putBoolean(PREF_GUIDE_PARTNER, status);
    }

    public static int getVersionCode() {
        return getInstance(MyApplication.getContext()).getInt(PREF_VERSION_CODE);
    }

    public static void updateVersionCode() {
        getInstance(MyApplication.getContext()).putInt(PREF_VERSION_CODE, Config.getVersionCode());
    }

    public static void setStartUpUrl(String splashAds) {
        getInstance(MyApplication.getContext()).putString(PREF_START_UP_URL, splashAds);
    }

    public static String getStartUpUrl() {
        try {
            return new JSONObject(getInstance(MyApplication.getContext()).getString
                    (PREF_START_UP_URL, "")).optString("start_up_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setPrefToolChoose(int index) {
        getInstance(MyApplication.getContext()).putInt(PREF_TOOL_CHOOSE, index);
    }

    public static int getPrefToolChoose() {
        return getInstance(MyApplication.getContext()).getInt(PREF_TOOL_CHOOSE);
    }

    public static void setPrefDiamondSignRemind(boolean bool) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_DIAMOND_SIGN_REMIND, bool);
    }

    public static boolean getPrefDiamondSignRemind() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_DIAMOND_SIGN_REMIND, true);
    }

    public static void setPrefSignRecord() {
        getInstance(MyApplication.getContext()).putString(PREF_SIGN_RECORD, DateFormatUtils
                .date2string(new Date(), "yyyy-MM-dd"));
    }

    public static String getPrefSignRecord() {
        return getInstance(MyApplication.getContext()).getString(PREF_SIGN_RECORD);
    }

    public static void setPrefSoprtRemind(boolean bool) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_SPORT_REMIND, bool);
    }

    public static boolean getPrefSportRemind() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_SPORT_REMIND, false);
    }

    public static void setPrefSportRemindTime(String date) {
        getInstance(MyApplication.getContext()).putString(PREF_SPORT_REMIND_TIME, date);
    }

    public static String getPrefSportRemindTime() {
        return getInstance(MyApplication.getContext()).getString(PREF_SPORT_REMIND_TIME);
    }

    public static boolean isGuideDietChart() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_GUIDE_DIET_CHART, false);
    }

    public static void setGuideDietChart(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_GUIDE_DIET_CHART, status);
    }

    public static boolean isAcceptPush() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_ACCEPT_PUSH, true);
    }

    public static void setPrefAcceptPush(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_ACCEPT_PUSH, status);
    }

    public static boolean isNewHomeGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_HOME_NEW_GUIDE_5_5_2, false);
    }

    public static void setNewHomeGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_HOME_NEW_GUIDE_5_5_2, status);
    }

    public static boolean isEstimateFoodGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_ESTIMATE_FOOD_GUIDE, false);
    }

    public static void setEstimateFoodGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_ESTIMATE_FOOD_GUIDE, status);
    }

    public static boolean isSearchFoodGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_SEARCH_FOOD_GUIDE, false);
    }

    public static void setSearchFoodGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_SEARCH_FOOD_GUIDE, status);
    }

    public static boolean isDietFoodGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_DIET_FOOD_GUIDE, false);
    }

    public static void setDietFoodGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_DIET_FOOD_GUIDE, status);
    }

    public static boolean isAddFoodGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_ADD_FOOD_GUIDE, false);
    }

    public static void setAddFoodGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_ADD_FOOD_GUIDE, status);
    }

    public static void setPrefCookFoodSearch(String history) {
        getInstance(MyApplication.getContext()).putString(PREF_COOK_FOOD_SEARCH, history);
    }

    public static String getPrefCookFoodSearch() {
        return getInstance(MyApplication.getContext()).getString(PREF_COOK_FOOD_SEARCH);
    }

    public static void clearCookFoodSearchHistory() {
        getInstance(MyApplication.getContext()).remove(PREF_COOK_FOOD_SEARCH);
    }

    public static String getChannel() {
        return getInstance(MyApplication.getContext()).getString(PREF_CHANNEL, "");
    }

    public static void setChannel(String channel) {
        getInstance(MyApplication.getContext()).putString(PREF_CHANNEL, channel);
    }

    public static WeightScale getWeightScale() {
        String s = getInstance(MyApplication.getContext()).getString(PREF_WEIGHT_SCALE, "");
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        return (WeightScale) FastJsonUtils.fromJson(s, WeightScale.class);
    }

    public static void setWeightScale(WeightScale weightScale) {
        getInstance(MyApplication.getContext()).putString(PREF_WEIGHT_SCALE, FastJsonUtils.toJson
                (weightScale));
    }

    public static void clearWeightScale() {
        getInstance(MyApplication.getContext()).remove(PREF_WEIGHT_SCALE);
    }

    public static boolean isNiceServiceGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_NICE_SERVICE_GUIDE, false);
    }

    public static void setNiceServiceGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_NICE_SERVICE_GUIDE, status);
    }

    public static boolean isNiceServiceGuideSecond() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_NICE_SERVICE_GUIDE_SECOND,
                false);
    }

    public static void setNiceServiceGuideSecond(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_NICE_SERVICE_GUIDE_SECOND, status);
    }

    public static boolean isNiceServiceGuideThird() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_NICE_SERVICE_GUIDE_THIRD,
                false);
    }

    public static void setNiceServiceGuideThird(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_NICE_SERVICE_GUIDE_THIRD, status);
    }

    public static void setHomeMyPlanGuide(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_HOME_MY_PLAN, status);
    }

    public static boolean isHomeMyPlanGuide() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_HOME_MY_PLAN, false);
    }

    public static void setPrefHomeMenu(boolean status) {
        getInstance(MyApplication.getContext()).putBoolean(PREF_HOME_MENU, status);
    }

    public static boolean getPrefHomeMenu() {
        return getInstance(MyApplication.getContext()).getBoolean(PREF_HOME_MENU, false);
    }

    public void clearAll() {
        this.setting.edit().clear().commit();
    }
}
