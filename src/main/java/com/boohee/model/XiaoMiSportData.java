package com.boohee.model;

import com.boohee.utils.FastJsonUtils;

import java.util.List;

public class XiaoMiSportData {
    public static String KEY_SUCCESS = "success";
    public int             code;
    public List<SportData> data;
    public String          message;

    public class SportData {
        public int    calorie;
        public String date;
        public int    deepSleepTime;
        public int    runCalorie;
        public int    runDistance;
        public int    runTime;
        public int    shallowSleepTime;
        public int    sleepEndTime;
        public int    sleepStartTime;
        public int    step;
        public int    wakeTime;
        public int    walkDistance;
        public int    walkTime;

        public String toString() {
            return FastJsonUtils.toJson(this);
        }
    }

    public String toString() {
        return FastJsonUtils.toJson(this);
    }
}
