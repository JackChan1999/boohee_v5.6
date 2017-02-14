package com.boohee.one.sport;

import android.content.Context;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;

public class SportV3Api {
    private static final String COURSES_HISTORY        = "/api/v1/history_courses/%1$d";
    private static final String SPORTS_COURSES         = "/api/v1/sports_courses";
    private static final String SPORTS_COURSES_CHOOSE  = "/api/v1/sports_user_records/%1$d/choose";
    private static final String SPORTS_COURSES_DAYS    = "/api/v1/sports_courses/%1$d/sports_days";
    private static final String SPORTS_COURSES_HISTORY = "/api/v1/history_courses";
    private static final String SPORTS_COURSES_RECORDS = "/api/v1/sports_courses/%1$d/user_records";
    private static final String SPORTS_COURSES_RESET   = "/api/v1/sports_user_records/%1$d/reset";
    private static final String SPORTS_DAYS_DETAIL     = "/api/v1/sports_days/%1$d";
    private static final String SPORTS_USER_RECORDS    = "/api/v1/sports_user_records/%1$d";
    public static final  String SPORT_DETAIL           = "/api/v1/sports_days/%d";
    public static final  String SPORT_RECORD           = "/api/v1/sports_user_records/%d";

    public static void requestCourse(JsonCallback callback, Context context) {
        requestCourse(0, callback, context);
    }

    public static void requestCourse(int courseID, JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).get(String.format(SPORTS_COURSES_DAYS, new
                Object[]{Integer.valueOf(courseID)}), callback, context);
    }

    public static void requestCourseRecord(boolean isHistory, int courseID, JsonCallback
            callback, Context context) {
        String url;
        if (isHistory) {
            url = String.format(COURSES_HISTORY, new Object[]{Integer.valueOf(courseID)});
        } else {
            url = String.format(SPORTS_COURSES_RECORDS, new Object[]{Integer.valueOf(courseID)});
        }
        BooheeClient.build(BooheeClient.BINGO).get(url, callback, context);
    }

    public static void requestCourseHistory(JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).get(SPORTS_COURSES_HISTORY, callback, context);
    }

    public static void requestCourseList(JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).get(SPORTS_COURSES, callback, context);
    }

    public static void chooseCourse(int courseID, JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).post(String.format(SPORTS_COURSES_CHOOSE, new
                Object[]{Integer.valueOf(courseID)}), null, callback, context);
    }

    public static void resetCourse(int courseID, JsonCallback callback, Context context) {
        BooheeClient.build(BooheeClient.BINGO).post(String.format(SPORTS_COURSES_RESET, new
                Object[]{Integer.valueOf(courseID)}), null, callback, context);
    }

    public static void getSportDetail(Context context, int id, JsonCallback callback) {
        BooheeClient.build(BooheeClient.BINGO).get(String.format(SPORT_DETAIL, new
                Object[]{Integer.valueOf(id)}), callback, context);
    }

    public static void addSportRecord(Context context, int id, JsonCallback callback) {
        BooheeClient.build(BooheeClient.BINGO).post(String.format(SPORT_RECORD, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }

    public static void deleteSportRecord(Context context, int id, JsonCallback callback) {
        BooheeClient.build(BooheeClient.BINGO).delete(String.format(SPORT_RECORD, new
                Object[]{Integer.valueOf(id)}), null, callback, context);
    }
}
