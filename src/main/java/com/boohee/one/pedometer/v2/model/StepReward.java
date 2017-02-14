package com.boohee.one.pedometer.v2.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class StepReward {
    private String description;
    private int    id;
    private String reward_date;
    private String reward_type;
    private int    reward_value;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReward_type() {
        return this.reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public String getReward_date() {
        return this.reward_date;
    }

    public void setReward_date(String reward_date) {
        this.reward_date = reward_date;
    }

    public int getReward_value() {
        return this.reward_value;
    }

    public void setReward_value(int reward_value) {
        this.reward_value = reward_value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static List<StepReward> parseArray(String rewards) {
        if (TextUtils.isEmpty(rewards)) {
            return null;
        }
        return JSON.parseArray(rewards, StepReward.class);
    }
}
