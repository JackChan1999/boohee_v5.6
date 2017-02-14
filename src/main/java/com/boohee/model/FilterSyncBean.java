package com.boohee.model;

import android.text.TextUtils;

import com.boohee.utils.DateHelper;
import com.google.gson.Gson;

import java.util.Date;

public class FilterSyncBean {
    private FilterSyncFigureBean[] figures;
    private FilterSyncFoodBean     food;
    private String                 recordOn;
    private FilterSyncSportBean    sport;
    private String                 tag;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public FilterSyncFoodBean getFood() {
        return this.food;
    }

    public void setFood(FilterSyncFoodBean food) {
        this.food = food;
    }

    public FilterSyncSportBean getSport() {
        return this.sport;
    }

    public void setSport(FilterSyncSportBean sport) {
        this.sport = sport;
    }

    public static FilterSyncBean parse(String json) {
        return (FilterSyncBean) new Gson().fromJson(json, FilterSyncBean.class);
    }

    public String toString() {
        return new Gson().toJson((Object) this);
    }

    public String getRecordOn() {
        return TextUtils.isEmpty(this.recordOn) ? DateHelper.format(new Date()) : this.recordOn;
    }

    public void setRecordOn(String recordOn) {
        this.recordOn = recordOn;
    }

    public FilterSyncFigureBean[] getFigures() {
        return this.figures;
    }

    public void setFigures(FilterSyncFigureBean[] figures) {
        this.figures = figures;
    }

    public boolean isNull() {
        return this.figures == null && this.sport == null && this.food == null;
    }
}
