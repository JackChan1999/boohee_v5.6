package com.boohee.utils;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.model.FilterSyncBean;
import com.boohee.model.FilterSyncFigureBean;
import com.boohee.model.FilterSyncFoodBean;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.one.MyApplication;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.ui.FilterFigureFragment;
import com.boohee.one.ui.ImageFilterActivity;

public class FilterDataSyncUtil {
    public static void saveFood(Context ctx, FilterSyncBean data) {
        FilterSyncFoodBean food = data.getFood();
        if (food != null && !TextUtils.isEmpty(food.getTag())) {
        }
    }

    public static void saveSport(Context ctx, FilterSyncBean data) {
        if (data.getSport() != null) {
        }
    }

    private static void saveWeight(Context ctx, FilterSyncFigureBean weightFigure, String
            recordOn) {
        if (weightFigure != null && weightFigure.getValue() >= 25.0f && weightFigure.getValue()
                <= 200.0f) {
        }
    }

    public static void saveFigure(Context ctx, FilterSyncBean data) {
        FilterSyncFigureBean[] figures = data.getFigures();
        if (figures != null && figures.length != 0) {
            JsonParams params = createFigureParam(ctx, figures, data.getRecordOn());
            if (params != null) {
                RecordApi.sendMeasure(params, ctx, new JsonCallback(ctx) {
                });
            }
        }
    }

    public static JsonParams createFigureParam(Context ctx, FilterSyncFigureBean[] figures,
                                               String recordOn) {
        JsonParams params = new JsonParams();
        boolean isNeedSync = false;
        for (FilterSyncFigureBean figure : figures) {
            if (figure != null) {
                float value = figure.getValue();
                if (FilterFigureFragment.FIGURE_RES_TEXTS[0].equals(figure.getName())) {
                    saveWeight(ctx, figure, recordOn);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[2].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value >= IntFloatWheelView.DEFAULT_VALUE && value <= 150.0f;
                    }
                    r7 = "waist";
                    r4 = (value < IntFloatWheelView.DEFAULT_VALUE || value > 150.0f) ? "" : value
                            + "";
                    params.put(r7, r4);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[3].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value > IntFloatWheelView.DEFAULT_VALUE && value <= 150.0f;
                    }
                    r7 = "brass";
                    r4 = (value < IntFloatWheelView.DEFAULT_VALUE || value > 150.0f) ? "" : value
                            + "";
                    params.put(r7, r4);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[4].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value >= IntFloatWheelView.DEFAULT_VALUE && value <= 150.0f;
                    }
                    r7 = "hip";
                    r4 = (value < IntFloatWheelView.DEFAULT_VALUE || value > 150.0f) ? "" : value
                            + "";
                    params.put(r7, r4);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[5].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value >= 15.0f && value <= 100.0f;
                    }
                    r7 = "arm";
                    r4 = (value < 15.0f || value > 100.0f) ? "" : value + "";
                    params.put(r7, r4);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[6].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value >= 30.0f && value <= 150.0f;
                    }
                    r7 = "thigh";
                    r4 = (value < 30.0f || value > 150.0f) ? "" : value + "";
                    params.put(r7, r4);
                } else if (FilterFigureFragment.FIGURE_RES_TEXTS[7].equals(figure.getName())) {
                    if (!isNeedSync) {
                        isNeedSync = value >= 15.0f && value <= 150.0f;
                    }
                    r7 = "shank";
                    r4 = (value < 15.0f || value > 150.0f) ? "" : value + "";
                    params.put(r7, r4);
                }
            }
        }
        params.put("record_on", recordOn);
        return isNeedSync ? params : null;
    }

    public static void removeFilterData(Context ctx) {
        removeFilterTag(ctx);
        UserPreference.getInstance(ctx).remove(ImageFilterActivity.KEY_POST_DATA);
    }

    public static void removeFilterTag(Context ctx) {
        UserPreference.getInstance(ctx).remove(ImageFilterActivity.KEY_POST_TAG);
    }

    public static String getTagData() {
        String tag = UserPreference.getInstance(MyApplication.getContext()).getString
                (ImageFilterActivity.KEY_POST_TAG);
        return TextUtils.isEmpty(tag) ? "" : "#" + tag + "# ";
    }

    public static void syncData(Context ctx, boolean needSync) {
        if (needSync) {
            syncData(ctx, needSync, UserPreference.getInstance(ctx).getString(ImageFilterActivity
                    .KEY_POST_DATA));
        }
    }

    public static void syncData(Context ctx, boolean needSync, String syncData) {
        if (needSync && !TextUtils.isEmpty(syncData)) {
            FilterSyncBean data = FilterSyncBean.parse(syncData);
            if (data != null) {
                saveFood(ctx, data);
                saveSport(ctx, data);
                saveFigure(ctx, data);
            }
        }
    }

    public static String getSyncData() {
        return UserPreference.getInstance(MyApplication.getContext()).getString
                (ImageFilterActivity.KEY_POST_DATA);
    }
}
