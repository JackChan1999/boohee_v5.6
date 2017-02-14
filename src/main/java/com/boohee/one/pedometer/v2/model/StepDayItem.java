package com.boohee.one.pedometer.v2.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class StepDayItem {
    private int                       distance;
    private List<ReceivedRewardsBean> received_rewards;
    private String                    record_on;
    private int                       step;

    public static class ReceivedRewardsBean {
        private int    id;
        private String reward;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReward() {
            return this.reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getRecord_on() {
        return this.record_on;
    }

    public void setRecord_on(String record_on) {
        this.record_on = record_on;
    }

    public List<ReceivedRewardsBean> getReceived_rewards() {
        return this.received_rewards;
    }

    public void setReceived_rewards(List<ReceivedRewardsBean> received_rewards) {
        this.received_rewards = received_rewards;
    }

    public static List<StepDayItem> parseArray(String steps) {
        if (TextUtils.isEmpty(steps)) {
            return null;
        }
        return JSON.parseArray(steps, StepDayItem.class);
    }
}
