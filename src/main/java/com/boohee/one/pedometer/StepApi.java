package com.boohee.one.pedometer;

import android.content.Context;
import android.text.TextUtils;

import com.boohee.database.StepsPreference;
import com.boohee.modeldao.StepCounterDao;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.pedometer.v2.model.StepReward;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.TextUtil;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;

public class StepApi {
    private static final String POST_STEPS           = "/api/v2/steps";
    private static final String STEPS_ALL_HISTORY    = "/api/v2/steps/history";
    private static final String STEPS_HISTORY        = "/api/v2/steps";
    private static final String STEPS_MONTH          = "/api/v2/steps/month";
    private static final String STEPS_REQUEST_REWARD = "/api/v2/step_rewards/receive";

    public static void postSteps(Context context) {
        Exception e;
        JSONArray jSONArray;
        JsonParams params = new JsonParams();
        final StepCounterDao dao = new StepCounterDao(context);
        try {
            JSONArray jsonArray = new JSONArray(FastJsonUtils.toJson(dao.getWeekSteps()));
            if (jsonArray != null) {
                try {
                    if (jsonArray.length() > 0) {
                        params.put("data", jsonArray);
                        BooheeClient.build("record").post("/api/v2/steps", params, new
                                JsonCallback(context) {
                            public void fail(String message) {
                            }

                            public void ok(String response) {
                                dao.deleteWeekSteps();
                                StepsPreference.putPrefSyncDate(DateHelper.today());
                            }
                        }, context);
                    }
                } catch (Exception e2) {
                    e = e2;
                    jSONArray = jsonArray;
                    e.printStackTrace();
                }
            }
            jSONArray = jsonArray;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
        }
    }

    public static void getStepsHistory(Context context, int page, JsonCallback callback) {
        JsonParams params = new JsonParams();
        params.put("page", page);
        BooheeClient.build("record").get("/api/v2/steps", params, callback, context);
    }

    public static void getStepsMonth(Context context, String date, JsonCallback callback) {
        if (TextUtils.isEmpty(date) || isCurrentMonth(date)) {
            BooheeClient.build("record").get(STEPS_MONTH, callback, context);
        } else {
            JsonParams params = new JsonParams();
            params.put("date", date);
            BooheeClient.build("record").get(STEPS_MONTH, params, callback, context);
        }
        if (TextUtil.isEmpty(date)) {
            BooheeClient.build("record").get(STEPS_MONTH, callback, context);
        }
    }

    public static boolean isCurrentMonth(String date) {
        String format = "yyyy-MM";
        return DateFormatUtils.date2string(new Date(), format).equals(DateFormatUtils
                .string2String(date, format));
    }

    public static void requestRewards(Context context, JsonCallback callback, List<StepReward>
            rewardList) {
        if (rewardList != null || rewardList.size() != 0) {
            StringBuilder builder = new StringBuilder();
            int dataSize = rewardList.size();
            for (int i = 0; i < dataSize; i++) {
                builder.append(((StepReward) rewardList.get(i)).getId());
                if (i < dataSize - 1) {
                    builder.append(",");
                }
            }
            JsonParams params = new JsonParams();
            params.put("rewards", builder.toString());
            BooheeClient.build("record").post(STEPS_REQUEST_REWARD, params, callback, context);
        }
    }

    public static void requestStepHistory(Context context, JsonCallback callback) {
        BooheeClient.build("record").get(STEPS_ALL_HISTORY, callback, context);
    }
}
