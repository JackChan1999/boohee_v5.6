package com.boohee.one.sync;

import com.boohee.api.RecordApi;
import com.boohee.model.RecordFood;
import com.boohee.model.RecordSport;
import com.boohee.model.mine.WeightRecord;
import com.boohee.modeldao.FoodRecordDao;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.one.MyApplication;
import com.boohee.one.event.RefreshDietEvent;
import com.boohee.one.event.RefreshSportEvent;
import com.boohee.one.event.RefreshWeightEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;

import de.greenrobot.event.EventBus;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SyncHelper {
    public static void syncWeight(final boolean isRefreshWeight) {
        if (HttpUtils.isNetworkAvailable(MyApplication.getContext())) {
            final WeightRecordDao dao = new WeightRecordDao(MyApplication.getContext());
            for (final WeightRecord record : dao.getList()) {
                JSONArray photos = null;
                try {
                    photos = new JSONArray(record.cachePhotos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RecordApi.postWeight(record.weight, record.record_on, photos, MyApplication
                        .getContext(), new JsonCallback(MyApplication.getContext()) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        dao.delete(record.record_on);
                        if (isRefreshWeight) {
                            EventBus.getDefault().post(new RefreshWeightEvent());
                        }
                    }

                    public void fail(String message) {
                        Helper.showToast((CharSequence) message);
                    }
                });
            }
        }
    }

    public static void syncAllEatings() {
        if (HttpUtils.isNetworkAvailable(MyApplication.getContext())) {
            final FoodRecordDao dao = new FoodRecordDao(MyApplication.getContext());
            List<RecordFood> records = dao.getList();
            if (records != null && records.size() != 0) {
                RecordApi.batchCreateEatings(records, MyApplication.getContext(), new
                        JsonCallback(MyApplication.getContext()) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        dao.deleteAll();
                        EventBus.getDefault().post(new RefreshDietEvent());
                    }

                    public void fail(String message) {
                    }
                });
            }
        }
    }

    public static void syncDailyEating(String record_on) {
        if (HttpUtils.isNetworkAvailable(MyApplication.getContext())) {
            final FoodRecordDao dao = new FoodRecordDao(MyApplication.getContext());
            for (final RecordFood record : dao.getList(record_on)) {
                RecordApi.createEating(paramsWithFoodRecord(record), MyApplication.getContext(),
                        new JsonCallback(MyApplication.getContext()) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        dao.delete(record);
                    }

                    public void fail(String message) {
                    }
                });
            }
        }
    }

    private static JsonParams paramsWithFoodRecord(RecordFood record) {
        JsonParams params = new JsonParams();
        params.put("record_on", record.record_on);
        params.put("unit_name", record.unit_name);
        params.put(FoodRecordDao.AMOUNT, record.amount);
        params.put("code", record.code);
        params.put("food_name", record.food_name);
        params.put(FoodRecordDao.FOOD_UNIT_ID, record.food_unit_id);
        params.put("calory", record.calory);
        params.put("time_type", record.time_type);
        return params;
    }

    public static void syncAllSports() {
        if (HttpUtils.isNetworkAvailable(MyApplication.getContext())) {
            final SportRecordDao dao = new SportRecordDao(MyApplication.getContext());
            List<RecordSport> records = dao.getList();
            if (records != null && records.size() != 0) {
                RecordApi.batchcreateActivities(records, MyApplication.getContext(), new
                        JsonCallback(MyApplication.getContext()) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        dao.deleteAll();
                        EventBus.getDefault().post(new RefreshSportEvent());
                    }

                    public void fail(String message) {
                    }
                });
            }
        }
    }
}
