package com.boohee.one.video.api;

import android.content.Context;

import com.boohee.database.UserPreference;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;

public class SportApi {
    public static final String DASH_BOARD                = "/api/v1/lessons/dashboard";
    public static final String EXERCIZE_DETAIL           = "/api/v1/exercise_plans/";
    public static final String GET_SPORT_PLAN_V3         = "/api/v1/lessons/courses";
    public static final String GIVE_UP_LESSON            = "/api/v1/sport_progresses/giveup";
    public static final String POST_JOURNAL_FINISH       = "/api/v1/sports_journals/finish";
    public static final String POST_LESSON_AGAIN         = "/api/v1/lessons/again";
    public static final String POST_NEXT_LESSON          = "/api/v1/lessons/next";
    public static final String POST_SPORTS_JOURNALS      = "/api/v1/sports_journals";
    public static final String SAVE_PROGRESS             = "/api/v1/sport_progresses";
    public static final String SPECIAL_LESSON            = "/api/v1/trainings";
    public static final String URL_LESSONS_SECTION       = "/api/v1/sections/";
    public static final String URL_LESSON_AGAIN          = "/api/v1/sport_progresses/again";
    public static final String URL_LESSON_PROGRESS_BEGIN = "/api/v1/sport_progresses/begin";
    public static final String URL_LSEEON_COMPLETED      = "/api/v1/sport_progresses/finish";
    public static final String URL_QUESTION              = "http://shop.boohee" +
            ".com/store/pages/befit";
    public static final String URL_TODAY_LESSONS         = "/api/v1/lessons/today";
    public static final String URL_VIDEO_HISTORY         = "/api/v1/sport_progresses";

    public static void postGiveUpLesson(Context context, int section_id, JsonCallback
            jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("section_id", section_id);
        new JsonParams().put("sport_progress", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(GIVE_UP_LESSON, sport_progress, jsonCallback,
                context);
    }

    public static void postLessonAgain(Context context, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).post(POST_LESSON_AGAIN, null, jsonCallback, context);
    }

    public static void postLessonNext(Context context, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).post(POST_NEXT_LESSON, null, jsonCallback, context);
    }

    public static void getExerciseDetail(Context context, int id, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).get(EXERCIZE_DETAIL + id, jsonCallback, context);
    }

    public static void getSpecialLessonDetail(Context context, int id, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).get("/api/v1/trainings/" + id, jsonCallback,
                context);
    }

    public static void postSportsJournals(Context ctx, int section_id, JsonCallback jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("id", section_id);
        new JsonParams().put("sports_journals", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(POST_SPORTS_JOURNALS, sport_progress,
                jsonCallback, ctx);
    }

    public static void postJournalFinish(Context context, int section_id, JsonCallback
            jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("section_id", section_id);
        new JsonParams().put("sports_journals", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(POST_JOURNAL_FINISH, sport_progress,
                jsonCallback, context);
    }

    public static void getTodayLessons(Context context, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).get(URL_TODAY_LESSONS, null, jsonCallback, context);
    }

    public static void getSportPlan(Context context, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).get(GET_SPORT_PLAN_V3, jsonCallback, context);
    }

    public static void getLessonDetail(Context context, int sectionId, JsonCallback jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).get(URL_LESSONS_SECTION + sectionId, null,
                jsonCallback, context);
    }

    public static void postSportProgress(Context context, JsonParams jsonParams, JsonCallback
            jsonCallback) {
        BooheeClient.build(BooheeClient.BINGO).post("/api/v1/sport_progresses", jsonParams,
                jsonCallback, context);
    }

    public static void postLessonAgain(Context context, int lesson_id, JsonCallback jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("lesson_id", lesson_id);
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("sport_progress", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(URL_LESSON_AGAIN, jsonParams, jsonCallback,
                context);
    }

    public static void postLessonCompleted(Context context, int lesson_id, JsonCallback
            jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("lesson_id", lesson_id);
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("sport_progress", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(URL_LSEEON_COMPLETED, jsonParams,
                jsonCallback, context);
    }

    public static void getVideoHistory(Context context, int index, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        jsonParams.put("page", index);
        BooheeClient.build(BooheeClient.BINGO).get("/api/v1/sport_progresses", jsonParams,
                callback, context);
    }

    public static void postLessonProgress(Context context, int lesson_id, JsonCallback
            jsonCallback) {
        JsonParams sport_progress = new JsonParams();
        sport_progress.put("section_id", lesson_id);
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("sport_progress", sport_progress);
        BooheeClient.build(BooheeClient.BINGO).post(URL_LESSON_PROGRESS_BEGIN, jsonParams, jsonCallback, context);
    }
}
