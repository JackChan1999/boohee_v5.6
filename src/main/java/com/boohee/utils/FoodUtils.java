package com.boohee.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.database.OnePreference;
import com.boohee.model.FoodIngredient;
import com.boohee.model.FoodLights;
import com.boohee.model.IngredientInfo;
import com.boohee.model.RecentFoodUnit;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.cache.FileCache;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class FoodUtils {
    public static final String[] INGREDIENT = new String[]{"calory@热量@千卡@500",
            "protein@蛋白质@克@505", "fat@脂肪@克@510", "carbohydrate@碳水化合物@克@515",
            "fiber_dietary@膳食纤维@克@520", "vitamin_a@维生素A@微克@525", "vitamin_c@维生素C@毫克@530",
            "vitamin_e@维生素E@毫克@535", "carotene@胡罗卜素@微克@540", "thiamine@维生素B1@毫克@545",
            "lactoflavin@维生素B2@毫克@550", "niacin@烟酸@毫克@555", "cholesterol@胆固醇@毫克@560",
            "magnesium@镁@毫克@565", "calcium@钙@毫克@570", "iron@铁@毫克@575", "zinc@锌@毫克@580",
            "copper@铜@毫克@585", "manganese@锰@毫克@590", "kalium@钾@毫克@595", "phosphor@磷@毫克@600",
            "natrium@钠@毫克@605", "selenium@硒@微克@615"};
    private static List<RecentFoodUnit> recentFoodUnitList;

    public static class FoodUnitMaxAndMin {
        public float currentValue = 100.0f;
        public float maxValue     = 999.0f;
        public int   microUnit    = 10;
        public float minValue     = 1.0f;
        public float unit         = 10.0f;
    }

    public static String changeUnitAndWeight(String weight, boolean isLiquid, boolean synChange) {
        String srcString = MyApplication.getContext().getString(R.string.ma);
        Object[] objArr = new Object[3];
        objArr[0] = showUnitString(synChange);
        objArr[1] = weight;
        objArr[2] = isLiquid ? "毫升" : "克";
        return String.format(srcString, objArr);
    }

    public static String showUnitValue(String calory, boolean synChange) {
        if (synChange) {
            try {
                float tmpCalory = Float.parseFloat(calory);
                return String.valueOf((int) (OnePreference.isShowCaloryUnit() ? tmpCalory :
                        calory2Joule(tmpCalory)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TextUtils.isEmpty(calory) ? "" : calory;
    }

    public static String showUnitString(boolean synChange) {
        int resId = R.string.aak;
        if (synChange) {
            resId = OnePreference.isShowCaloryUnit() ? R.string.aak : R.string.aao;
        }
        return MyApplication.getContext().getResources().getString(resId);
    }

    public static void changeShowUnit() {
        OnePreference.setShowUnit(!OnePreference.isShowCaloryUnit());
    }

    public static boolean isShowCaloryUnit() {
        return OnePreference.isShowCaloryUnit();
    }

    public static float calory2Joule(float source) {
        return (float) (((double) source) * 4.184d);
    }

    public static float kjoule2Calory(float source) {
        return (float) (((double) source) / 4.184d);
    }

    public static void switchToLightWithText(int light, ImageView ivLight) {
        switch (light) {
            case 0:
                ivLight.setBackgroundResource(R.drawable.sd);
                return;
            case 1:
                ivLight.setBackgroundResource(R.drawable.sh);
                return;
            case 2:
                ivLight.setBackgroundResource(R.drawable.sj);
                return;
            case 3:
                ivLight.setBackgroundResource(R.drawable.si);
                return;
            default:
                ivLight.setBackgroundResource(R.drawable.sd);
                return;
        }
    }

    public static void initIngredient(List<IngredientInfo> dataList, FoodIngredient ingredient) {
        if (dataList != null && ingredient != null) {
            dataList.clear();
            try {
                Field[] field = ingredient.getClass().getDeclaredFields();
                for (int i = 0; i < field.length; i++) {
                    IngredientInfo info = getIngredientInfo(field[i].getName());
                    if (info != null) {
                        String value = field[i].get(ingredient);
                        if (value == null || "".equals(value.toString()) || "null".equals(value)) {
                            value = "--";
                            info.unit = "";
                        }
                        info.value = value.toString();
                        dataList.add(info);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static IngredientInfo getIngredientInfo(String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return null;
        }
        for (String split : INGREDIENT) {
            String[] itemValue = split.split("@");
            if (fieldName.equals(itemValue[0])) {
                IngredientInfo result = new IngredientInfo();
                String name = itemValue[1];
                String unit = itemValue[2];
                result.order = Integer.parseInt(itemValue[3]);
                result.name = name;
                result.code = fieldName;
                result.unit = unit;
                return result;
            }
        }
        return null;
    }

    public static void setIngredientInfo(List<IngredientInfo> dataList, FoodLights lights) {
        if (dataList != null && dataList.size() != 0 && lights != null) {
            try {
                Field[] field = lights.getClass().getDeclaredFields();
                for (int i = 0; i < field.length; i++) {
                    String fileName = field[i].getName();
                    for (IngredientInfo item : dataList) {
                        if (fileName.equals(item.code)) {
                            item.memo = String.valueOf(field[i].get(lights));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sortIngredientInfo(List<IngredientInfo> dataList) {
        if (dataList != null && dataList.size() != 0) {
            Collections.sort(dataList, new Comparator<IngredientInfo>() {
                public int compare(IngredientInfo lhs, IngredientInfo rhs) {
                    return lhs.order - rhs.order;
                }
            });
        }
    }

    public static void filterIngredientInfo(List<IngredientInfo> showList, List<IngredientInfo>
            dataList) {
        if (showList != null && dataList != null && dataList.size() != 0) {
            showList.clear();
            if (dataList.size() < 6) {
                showList.addAll(dataList);
                return;
            }
            for (int i = 0; i < 5; i++) {
                showList.add(dataList.get(i));
            }
        }
    }

    public static void switchToLight(int health_light, ImageView iv_health_light) {
        switch (health_light) {
            case 0:
                iv_health_light.setVisibility(8);
                return;
            case 1:
                iv_health_light.setBackgroundResource(R.drawable.a24);
                return;
            case 2:
                iv_health_light.setBackgroundResource(R.drawable.a26);
                return;
            case 3:
                iv_health_light.setBackgroundResource(R.drawable.a25);
                return;
            default:
                iv_health_light.setVisibility(8);
                return;
        }
    }

    public static void switchToLight(int health_light, ImageView iv_health_light, TextView
            tv_light) {
        switch (health_light) {
            case 0:
                iv_health_light.setVisibility(8);
                return;
            case 1:
                iv_health_light.setBackgroundResource(R.drawable.a24);
                tv_light.setText("推荐");
                return;
            case 2:
                iv_health_light.setBackgroundResource(R.drawable.a26);
                tv_light.setText("适量");
                return;
            case 3:
                iv_health_light.setBackgroundResource(R.drawable.a25);
                tv_light.setText("少吃");
                return;
            default:
                iv_health_light.setVisibility(8);
                return;
        }
    }

    public static String getDietName(Context context, int timeType) {
        String dietName = "";
        if (context == null) {
            return dietName;
        }
        if (timeType == 1) {
            dietName = context.getResources().getString(R.string.dp);
        } else if (timeType == 2) {
            dietName = context.getResources().getString(R.string.ri);
        } else if (timeType == 3) {
            dietName = context.getResources().getString(R.string.a7m);
        } else if (timeType == 6) {
            dietName = context.getResources().getString(R.string.dq);
        } else if (timeType == 7) {
            dietName = context.getResources().getString(R.string.rj);
        } else if (timeType == 8) {
            dietName = context.getResources().getString(R.string.a7n);
        }
        return dietName;
    }

    public static boolean isKM(String foodName) {
        return TextUtils.equals("公里", foodName);
    }

    public static String formatPoint(String rateStr) {
        if (rateStr.indexOf(".") == -1) {
            return rateStr;
        }
        int num = rateStr.indexOf(".");
        if (rateStr.length() - num > 2) {
            return rateStr.substring(0, num + 2);
        }
        return rateStr;
    }

    public static String subPoint(String str) {
        if (str.indexOf(".") != -1) {
            return str.substring(0, str.indexOf("."));
        }
        return str;
    }

    public static void saveRecentFoodUnit(Context context, int foodId, float num, int unitId) {
        RecentFoodUnit unit = new RecentFoodUnit();
        unit.food_id = foodId;
        unit.unit_id = unitId;
        unit.amount = num;
        saveRecentFoodUnit(context, unit);
    }

    public static void saveRecentFoodUnit(Context context, RecentFoodUnit foodUnit) {
        if (foodUnit != null) {
            try {
                if (recentFoodUnitList == null) {
                    recentFoodUnitList = new ArrayList();
                } else {
                    for (int i = 0; i < recentFoodUnitList.size(); i++) {
                        if (foodUnit.food_id == ((RecentFoodUnit) recentFoodUnitList.get(i))
                                .food_id) {
                            recentFoodUnitList.remove(i);
                            recentFoodUnitList.add(0, foodUnit);
                        }
                    }
                }
                if (!recentFoodUnitList.contains(foodUnit)) {
                    recentFoodUnitList.add(0, foodUnit);
                }
                saveRecentFoodUnitToCache(context, recentFoodUnitList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static RecentFoodUnit getRecentFoodUnit(Context context, int foodId) {
        if (recentFoodUnitList == null) {
            try {
                recentFoodUnitList = FastJsonUtils.parseList(FileCache.get(context)
                        .getAsJSONObject(CacheKey.EATING_RECENT).getString("recent_foods"),
                        RecentFoodUnit.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (recentFoodUnitList == null) {
            return null;
        }
        for (int i = 0; i < recentFoodUnitList.size(); i++) {
            RecentFoodUnit unit = (RecentFoodUnit) recentFoodUnitList.get(i);
            if (foodId == unit.food_id) {
                return unit;
            }
        }
        return null;
    }

    private static void saveRecentFoodUnitToCache(Context context, List<RecentFoodUnit> unitList) {
        try {
            FileCache cache = FileCache.get(context);
            JSONObject object = new JSONObject();
            object.put("recent_foods", new Gson().toJson((Object) unitList));
            cache.put(CacheKey.EATING_RECENT, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static FoodUnitMaxAndMin getMinValueWithFoodUnit(String unitName) {
        FoodUnitMaxAndMin unit = new FoodUnitMaxAndMin();
        if (TextUtils.equals(unitName, "克") || TextUtils.equals(unitName, "毫升")) {
            unit.maxValue = 999.0f;
            unit.minValue = 1.0f;
            unit.currentValue = 100.0f;
        } else {
            unit.maxValue = 99.0f;
            unit.minValue = 0.0f;
            unit.currentValue = 1.0f;
            unit.unit = 1.0f;
            unit.microUnit = 2;
        }
        return unit;
    }
}
