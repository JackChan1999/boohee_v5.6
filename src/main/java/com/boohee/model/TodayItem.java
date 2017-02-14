package com.boohee.model;

public class TodayItem {
    public String description;
    public float  percentage;
    public float  weight;

    public enum DESCRIPTION_TYPE {
        less,
        good,
        much
    }
}
