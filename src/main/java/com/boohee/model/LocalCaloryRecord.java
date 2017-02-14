package com.boohee.model;

public class LocalCaloryRecord {
    public String record_on;
    public String totalDietCalory;
    public String totalSportCalory;

    public LocalCaloryRecord(String record_on, String totalDietCalory, String totalSportCalory) {
        this.record_on = record_on;
        this.totalDietCalory = totalDietCalory;
        this.totalSportCalory = totalSportCalory;
    }
}
