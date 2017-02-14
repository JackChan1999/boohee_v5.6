package com.boohee.model.home;

import java.util.List;

public class HomeSportPlan {
    public int                    calorie;
    public String                 difficulty;
    public int                    level;
    public String                 name;
    public String                 pic_url;
    public List<ProgressesEntity> progresses;

    public static class ProgressesEntity {
        public String number;
        public String status;
    }
}
