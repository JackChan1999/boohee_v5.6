package com.boohee.api;

import android.content.Context;

import com.boohee.database.UserPreference;
import com.boohee.model.CopyFood;
import com.boohee.model.CopySport;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordSport;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Const;
import com.boohee.utils.FastJsonUtils;
import com.tencent.open.SocialConstants;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class RecordApi {
    public static final String ACTIVITIES_BATCH          = "/api/v2/activities/batch";
    public static final String ADD_WEIGHT                = "/api/v2/weights";
    public static final String BATCH_CREATE_ACTIVITIES   = "/api/v2/activities/batch";
    public static final String BATCH_CREATE_EATINGS      = "/api/v2/eatings/batch";
    public static final String CREATE_ACTIVITY           = "/api/v2/activities";
    public static final String CREATE_EATING             = "/api/v2/eatings";
    public static final String DELETE_ACTIVITY           = "/api/v2/activities/%1$d?token=%2$s";
    public static final String DELETE_DIET_PHOTOS        = "/api/v2/diet_photos/%d";
    public static final String DELETE_EATING             = "/api/v2/eatings/%1$d?token=%2$s";
    public static final String DELETE_VIDEO_SPORT_RECORD =
            "/api/v2/boohee_sport_items/%1$d?token=%2$s";
    public static final String DIET_PHOTOS               = "/api/v2/diet_photos";
    public static final String EATINGS_BATCH             = "/api/v2/eatings/batch";
    public static final String GET_ACTIVITIES            =
            "/api/v2/activities?token=%1$s&record_on=%2$s&boohee_sport_item=1";
    public static final String GET_ACTIVITIES_HOT        = "/api/v2/activities/hot?page=%1$d";
    public static final String GET_CAN_RECORDS_DATES     = "/api/v2/can_records/dates";
    public static final String GET_CAN_RECORDS_HISTORY   = "/api/v2/can_records";
    public static final String GET_DAILY_ANALYSIS        = "/api/v2/users/daily_analysis";
    public static final String GET_DAILY_SUMMARY         = "/api/v2/eatings/daily_summary.json";
    public static final String GET_EATINGS               =
            "/api/v2/eatings?token=%1$s&record_on=%2$s";
    public static final String GET_EATINGS_HOT           = "/api/v2/eatings/hot?page=%1$d";
    public static final String GET_RECORD_HOME           = "/api/v2/home?token=%1$s";
    public static final String MEASURE_MONTH_LIST        = "/api/v1/measures/%1$s.json";
    public static final String POST_MEASURE              = "/api/v2/measures.json";
    public static final String REMOVE_PHOTOS             = "/api/v2/photos/remove.json";
    public static final String UPDATE_ACTIVITY           = "/api/v2/activities/%1$d";
    public static final String UPDATE_EATING             = "/api/v2/eatings/%1$d";
    public static final String URL_BINDS                 = "/api/v2/user_bands";
    public static final String URL_DELETE_BIND           = "/api/v2/user_bands/%d";
    public static final String URL_MC_PERIODS            = "/mc/api/v1/mc_periods";
    public static final String URL_MC_PERIODS_LATEST     = "/mc/api/v1/mc_periods/latest";
    public static final String URL_MC_PERIODS_MONTH      = "/mc/api/v1/mc_periods/month";
    public static final String URL_MC_RECORDS            = "/mc/api/v1/records";
    public static final String URL_MC_RECORDS_DELETE     = "/mc/api/v1/records/delete";
    public static final String URL_MC_UPDATE_SUMMARIES   = "/mc/api/v1/summaries";
    public static final String URL_PHOTOS_DELETE         = "/api/v2/photos/delete";
    public static final String URL_USERS_CHANGE_PROFILE  = "/api/v1/users/one_change_profile";
    public static final String URL_USERS_PROFILE         = "/api/v1/users/one_profile";
    public static final String URL_WEIGHT_DATE           = "/api/v2/weights/date?record_on=%s";
    public static final String URL_WEIGHT_DELETE         = "/api/v2/weights/delete";
    public static final String URL_WEIGHT_RECENT         = "/api/v2/weights/recent";
    public static final String WEIGHTS_LATEST            = "/api/v2/weights/latest";
    public static final String WEIGHT_MONTH_LIST         = "/api/v2/weights/month";
    public static final String WEIGHT_PROGRESS           = "/api/v2/weights/progress";

    public static void getEatings(String record_on, Context context, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(GET_EATINGS, new Object[]{UserPreference
                .getToken(context), record_on}), callback, context);
    }

    public static void getEatingsHot(Context context, int page, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(GET_EATINGS_HOT, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void createEating(JsonParams jsonParams, Context context, JsonCallback callback) {
        BooheeClient.build("record").post(CREATE_EATING, jsonParams, callback, context);
    }

    public static void batchCreateEatings(List<RecordFood> foodRecords, Context context,
                                          JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(FastJsonUtils.toJson(foodRecords));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            jsonParams.put("eatings", jsonArray);
        }
        BooheeClient.build("record").post("/api/v2/eatings/batch", jsonParams, callback, context);
    }

    public static void batchCopyEating(ArrayList<CopyFood> copyFoods, Context context,
                                       JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(FastJsonUtils.toJson(copyFoods));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            jsonParams.put("eatings", jsonArray);
        }
        BooheeClient.build("record").post("/api/v2/eatings/batch", jsonParams, callback, context);
    }

    public static void updateEating(int id, JsonParams jsonParams, Context context, JsonCallback
            callback) {
        BooheeClient.build("record").put(String.format(UPDATE_EATING, new Object[]{Integer
                .valueOf(id)}), jsonParams, callback, context);
    }

    public static void deleteEating(int id, Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        BooheeClient.build("record").delete(String.format(DELETE_EATING, new Object[]{Integer
                .valueOf(id), UserPreference.getToken(context)}), jsonParams, callback, context);
    }

    public static void getActivities(String record_on, Context context, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(GET_ACTIVITIES, new
                Object[]{UserPreference.getToken(context), record_on}), callback, context);
    }

    public static void getActivitiesHot(Context context, int page, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(GET_ACTIVITIES_HOT, new Object[]{Integer
                .valueOf(page)}), callback, context);
    }

    public static void createActivity(RecordSport record, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(context));
        params.put(SportRecordDao.DURATION, record.duration);
        params.put("record_on", record.record_on);
        params.put(SportRecordDao.ACTIVITY_ID, record.activity_id);
        BooheeClient.build("record").post(CREATE_ACTIVITY, params, callback, context);
    }

    public static void batchcreateActivities(List<RecordSport> sportRecords, Context context,
                                             JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(FastJsonUtils.toJson(sportRecords));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            jsonParams.put("activities", jsonArray);
        }
        BooheeClient.build("record").post("/api/v2/activities/batch", jsonParams, callback,
                context);
    }

    public static void createCustomActivity(RecordSport record, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(context));
        params.put("record_on", record.record_on);
        params.put(SportRecordDao.DURATION, record.duration);
        params.put(SportRecordDao.ACTIVITY_NAME, record.activity_name);
        params.put("unit_name", record.unit_name);
        params.put("calory", record.calory);
        BooheeClient.build("record").post(CREATE_ACTIVITY, params, callback, context);
    }

    public static void batchCopyActivity(ArrayList<CopySport> sportList, Context context,
                                         JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(FastJsonUtils.toJson(sportList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            jsonParams.put("activities", jsonArray);
        }
        BooheeClient.build("record").post("/api/v2/activities/batch", jsonParams, callback,
                context);
    }

    public static void updateCustomActivity(RecordSport record, Context context, JsonCallback
            callback) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(context));
        params.put("record_on", record.record_on);
        params.put(SportRecordDao.DURATION, record.duration);
        params.put(SportRecordDao.ACTIVITY_NAME, record.activity_name);
        params.put("calory", record.calory);
        params.put("unit_name", record.unit_name);
        BooheeClient.build("record").put(String.format(UPDATE_ACTIVITY, new Object[]{Integer
                .valueOf(record.id)}), params, callback, context);
    }

    public static void updateActivity(RecordSport record, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("token", UserPreference.getToken(context));
        params.put(SportRecordDao.DURATION, record.duration);
        params.put("record_on", record.record_on);
        params.put(SportRecordDao.ACTIVITY_ID, record.activity_id);
        BooheeClient.build("record").put(String.format(UPDATE_ACTIVITY, new Object[]{Integer
                .valueOf(record.id)}), params, callback, context);
    }

    public static void deleteActivity(int id, Context context, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        BooheeClient.build("record").delete(String.format(DELETE_ACTIVITY, new Object[]{Integer
                .valueOf(id), UserPreference.getToken(context)}), jsonParams, callback, context);
    }

    public static void getRecordHome(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(GET_RECORD_HOME, new
                Object[]{UserPreference.getToken(context)}), callback, context);
    }

    public static void postMcRecords(Context context, JsonParams jsonParams, JsonCallback
            callback) {
        BooheeClient.build("record").post(URL_MC_RECORDS, jsonParams, callback, context);
    }

    public static void deleteMcRecords(Context context, String record_on, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("record_on", record_on);
        BooheeClient.build("record").delete(URL_MC_RECORDS_DELETE, jsonParams, callback, context);
    }

    public static void getMcPeriods(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(URL_MC_PERIODS, new JsonParams(), callback, context);
    }

    public static void getMcPeriodsMonth(Context context, String year_month, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("year_month", year_month);
        BooheeClient.build("record").get(URL_MC_PERIODS_MONTH, jsonParams, callback, context);
    }

    public static void getMcPeriodsLatest(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(URL_MC_PERIODS_LATEST, new JsonParams(), callback,
                context);
    }

    public static void updateMcUpdateSummaries(Context context, String code, String content,
                                               JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put(code, content);
        BooheeClient.build("record").put(URL_MC_UPDATE_SUMMARIES, jsonParams, callback, context);
    }

    public static void updateMcUpdateSummaries(Context context, int duration, int cycle, String
            record_on, String end_on, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put(SportRecordDao.DURATION, duration);
        jsonParams.put("cycle", cycle);
        jsonParams.put("record_on", record_on);
        jsonParams.put("end_on", end_on);
        BooheeClient.build("record").put(URL_MC_UPDATE_SUMMARIES, jsonParams, callback, context);
    }

    public static void getMeasureMonthList(String code, String year_month, Context context,
                                           JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("year_month", year_month);
        BooheeClient.build("record").get(String.format(MEASURE_MONTH_LIST, new Object[]{code}),
                params, callback, context);
    }

    public static void getWeightsList(String year_month, Context context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("year_month", year_month);
        BooheeClient.build("record").get(WEIGHT_MONTH_LIST, params, callback, context);
    }

    public static void sendMeasure(JsonParams params, Context context, JsonCallback callback) {
        BooheeClient.build("record").post(POST_MEASURE, params, callback, context);
    }

    public static void getCanRecordsDates(Context context, String record_on, JsonCallback
            callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("record_on", record_on);
        BooheeClient.build("record").get(GET_CAN_RECORDS_DATES, jsonParams, callback, context);
    }

    public static void getGetCanRecordsHistory(Context context, String page, String per_page,
                                               JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("page", page);
        jsonParams.put("per_page", per_page);
        BooheeClient.build("record").get(GET_CAN_RECORDS_HISTORY, jsonParams, callback, context);
    }

    public static void getDailyAnalysis(Context context, String record_on, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        jsonParams.put(Const.RECORD_DATE, record_on);
        BooheeClient.build("record").get(GET_DAILY_ANALYSIS, jsonParams, callback, context);
    }

    public static void getDailyNutrition(Context context, String record_on, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        jsonParams.put("record_on", record_on);
        BooheeClient.build("record").get(GET_DAILY_SUMMARY, jsonParams, callback, context);
    }

    public static void postDietPhotos(Context context, int timeType, String record_on, String
            qiniu_key, boolean isAnalysis, String name, int calory, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        jsonParams.put("record_on", record_on);
        jsonParams.put("time_type", timeType);
        jsonParams.put("qiniu_key", qiniu_key);
        jsonParams.put("analysis", isAnalysis);
        jsonParams.put("calory", calory);
        jsonParams.put("name", name);
        BooheeClient.build("record").post(DIET_PHOTOS, jsonParams, callback, context);
    }

    public static void deleteDietPhotos(Context context, int id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        BooheeClient.build("record").delete(String.format(DELETE_DIET_PHOTOS, new
                Object[]{Integer.valueOf(id)}), jsonParams, callback, context);
    }

    public static void getWeightsLatest(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(WEIGHTS_LATEST, callback, context);
    }

    public static void getWeight(Context context, String record_on, JsonCallback callback) {
        BooheeClient.build("record").get(String.format(URL_WEIGHT_DATE, new Object[]{record_on}),
                callback, context);
    }

    public static void postWeight(String weight, String record_on, JSONArray photos, Context
            context, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("weight", weight);
        params.put("record_on", record_on);
        params.put(SocialConstants.PARAM_SOURCE, 0);
        if (photos != null) {
            params.put(WeightRecordDao.PHOTOS, photos);
        }
        BooheeClient.build("record").post(ADD_WEIGHT, params, callback, context);
    }

    public static void postWeight(WeightRecord record, JSONArray photos, Context context,
                                  JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("weight", record.weight);
        params.put("record_on", record.record_on);
        params.put("bmi", record.bmi);
        params.put("bodyfat", record.bodyfat);
        params.put("subfat", record.subfat);
        params.put("visfat", record.visfat);
        params.put("water", record.water);
        params.put("bmr", record.bmr);
        params.put("muscle", record.muscle);
        params.put("bone", record.bone);
        params.put("protein", record.protein);
        params.put(SocialConstants.PARAM_SOURCE, record.source);
        params.put("measure_time", record.measure_time);
        if (photos != null) {
            params.put(WeightRecordDao.PHOTOS, photos);
        }
        BooheeClient.build("record").post(ADD_WEIGHT, params, callback, context);
    }

    public static void deleteWeightPhoto(Context context, int id, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("id", String.valueOf(id));
        BooheeClient.build("record").delete(REMOVE_PHOTOS, jsonParams, callback, context);
    }

    public static void deleteRecord(Context context, String record_on, JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("record_on", record_on);
        BooheeClient.build("record").delete(URL_WEIGHT_DELETE, jsonParams, callback, context);
    }

    public static void deleteWeightPhoto(Context context, String record_on, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("record_on", record_on);
        BooheeClient.build("record").delete(URL_PHOTOS_DELETE, params, callback, context);
    }

    public static void deleteVideoSportRecord(Context context, int id, JsonCallback jsonCallback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put("token", UserPreference.getToken(context));
        BooheeClient.build("record").delete(String.format(DELETE_VIDEO_SPORT_RECORD, new
                Object[]{Integer.valueOf(id), UserPreference.getToken(context)}), jsonParams,
                jsonCallback, context);
    }

    public static void updateUsersChangeProfile(Context context, String code, String content,
                                                JsonCallback callback) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.put(code, content);
        BooheeClient.build("record").post(URL_USERS_CHANGE_PROFILE, jsonParams, callback, context);
    }

    public static void createUsersChangeProfile(Context context, JsonParams jsonParams,
                                                JsonCallback callback) {
        BooheeClient.build("record").post(URL_USERS_CHANGE_PROFILE, jsonParams, callback, context);
    }

    @Deprecated
    public static void getUsersProfile(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(URL_USERS_PROFILE, callback, context);
    }

    public static void getBands(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(URL_BINDS, callback, context);
    }

    public static void unbindBand(Context context, int id, String provider, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("provider", provider);
        BooheeClient.build("record").delete(String.format(URL_DELETE_BIND, new Object[]{Integer.valueOf(id)}), params, callback, context);
    }

    public static void getRecentWeight(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(URL_WEIGHT_RECENT, callback, context);
    }
}
