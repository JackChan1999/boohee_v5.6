package com.boohee.model.home;

import com.boohee.model.HomeWallpager;

import java.util.List;

public class Home {
    public DietPlanEntity   diet_plan;
    public boolean          is_bind_mi;
    public SportsPlanEntity sports_plan;
    public WelcomeImgEntity welcome_img;

    public static class DietPlanData {
        public int                calory;
        public String             content;
        public List<DetailEntity> detail;
        public String             name;
        public String             time_span;

        public static class DetailEntity {
            public int    amount;
            public String image_url;
            public String name;
            public String unit;
        }
    }

    public static class DietPlanEntity {
        public List<DietPlanData> data;
        public String             super_model_link;
        public String             user_link;
    }

    public static class SportsPlanEntity {
        public String date;
        public String link_to;
        public String name;
    }

    public static class WelcomeImgEntity {
        public String              back_img;
        public String              back_img_small;
        public String              date;
        public String              hello_text;
        public List<HomeWallpager> week_images;
    }
}
