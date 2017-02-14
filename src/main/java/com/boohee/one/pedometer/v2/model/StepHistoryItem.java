package com.boohee.one.pedometer.v2.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class StepHistoryItem {
    private String date;
    private String rewards;
    private int    steps;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return this.steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getRewards() {
        return this.rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public static List<StepHistoryItem> parseArray(String monthly) {
        if (TextUtils.isEmpty(monthly)) {
            return null;
        }
        return JSON.parseArray(monthly, StepHistoryItem.class);
    }
}
