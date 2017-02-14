package com.boohee.nice.model;

public class NiceDetail {
    public AdvisorBean  advisor;
    public NiceUrlsBean nice_urls;
    public PeriodBean   period;
    public int          report_status;
    public int          survey_status;

    public static class AdvisorBean {
        public String name;
        public int    status;
        public int    unread_count;
    }

    public static class NiceUrlsBean {
        public String activity_page;
        public String diet_page;
        public String knowledge;
        public String solution_report;
        public String survey;
        public String survey_report;
    }

    public static class PeriodBean {
        public String end_on;
        public int    payment_state;
        public int    period_index;
        public String period_index_desc;
        public String start_at;
        public String title;
    }
}
