package com.zxinsight.analytics.domain.response;

public class ServiceConfig {
    public long dpt = -1;
    public int           e;
    public int           lbs;
    public SharePlatform sp;
    public int           ss;
    public SendStrategy  st;

    public SharePlatform getSp() {
        return this.sp != null ? this.sp : new SharePlatform();
    }

    public SendStrategy getSt() {
        return this.st != null ? this.st : new SendStrategy();
    }
}
