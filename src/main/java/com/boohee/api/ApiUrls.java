package com.boohee.api;

public interface ApiUrls {
    public static final String BADGE_URL                   = "/api/v1/badges";
    public static final String COUPON_EXCHANGE             = "/api/v1/coupon_code_redeems/new";
    public static final String DIAMOND_CHECHIN             = "/api/v1/checkin/diamonds";
    public static final String FREE_GATE                   =
            "/api/v1/milestones/free_gate?token=%1$s";
    public static final String HOST                        = "";
    public static final String MILESTONES                  = "/api/v1/milestones/full_menu.json";
    public static final String MILESTONE_ITEM              = "/api/v1/milestones/%1$d?token=%2$s";
    public static final String MINE_ADVERTISEMENT          = "/api/v1/advertisements";
    public static final String MINE_BANK                   = "/api/v1/checkin/diamond_bank" +
            ".html?token=%1$s";
    public static final String MINE_BROADCAST_UNREAD_COUNT = "/api/v1/broadcasts/unread_count";
    public static final String MINE_NOTICE_UNREAD_COUNT    = "/api/v1/broadcasts/unread_count";
    public static final String MINE_USERINFO               = "/api/v1/users/profile";
    public static final String QA                          = "";
    public static final String REPORT_URL                  = "/api/v1/health/report" +
            ".html?favorite=false";
    public static final String RESET                       = "/api/v1/milestones/reset.json";
    public static final String SPORT_QUESTIONS_URL         = "/api/v1/questions/init_v2" +
            ".html?quiz_type=sport";
    public static final String SPORT_QUESTIONS_URL_V2      = "/api/v1/questions/sports_v2.html";
    public static final String SUCCESS_STORY_URL           = "/events/new_stories?share=true";
    public static final String UPLOAD_STORY_URL            = "/events/new_stories/case";
    public static final String URL_PAY_SUCCESS             = "http://shop.boohee.com/store/pages/notice";
}
