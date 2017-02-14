package com.boohee.one.pedometer.manager;

import android.content.Context;
import android.os.Handler;

import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.pedometer.StepModel;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;

import org.json.JSONObject;

public class BandStepManager extends AbstractStepManager {
    public BandStepManager(Context context) {
        super(context);
    }

    public StepModel getCurrentStep() {
        return this.stepModel;
    }

    public void getCurrentStepAsyncs() {
        BooheeClient.build("record").post("/api/v2/cling_records/touch", null, new JsonCallback
                (this.context) {
            public void ok(String response) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        BooheeClient.build("record").get("/api/v2/cling_records", new
                                JsonCallback(BandStepManager.this.context) {
                            public void ok(JSONObject object) {
                                if (object != null) {
                                    BandStepManager.this.stepModel = (StepModel) FastJsonUtils
                                            .fromJson(object, StepModel.class);
                                    BandStepManager.this.stepModel.record_on = DateHelper.today();
                                    BandStepManager.this.saveSteps();
                                    BandStepManager.this.notifyDataChanged();
                                }
                            }
                        }, BandStepManager.this.context);
                    }
                }, 3000);
            }
        }, this.context);
    }
}
