package com.boohee.model.scale;

import com.boohee.one.R;
import com.boohee.record.DietChartActivity;
import com.boohee.utils.BitmapUtil;
import com.loopj.android.http.AsyncHttpClient;

import org.java_websocket.framing.CloseFrame;

public class BmrIndex extends ScaleIndex {
    private float bmr;

    public BmrIndex(float bmr, int sex, int age) {
        this.bmr = bmr;
        int standard;
        if (sex == 1) {
            if (age <= 17) {
                standard = BitmapUtil.MAX_HEIGHT;
            } else if (age <= 29) {
                standard = 1550;
            } else if (age <= 49) {
                standard = AsyncHttpClient.DEFAULT_RETRY_SLEEP_TIME_MILLIS;
            } else if (age <= 69) {
                standard = 1350;
            } else {
                standard = 1220;
            }
            this.division = new float[]{500.0f, (float) standard, DietChartActivity.maxCaloryLimit};
        } else {
            if (age <= 17) {
                standard = 1300;
            } else if (age <= 29) {
                standard = 1210;
            } else if (age <= 49) {
                standard = 1170;
            } else if (age <= 69) {
                standard = 1110;
            } else {
                standard = CloseFrame.EXTENSION;
            }
            this.division = new float[]{500.0f, (float) standard, DietChartActivity.maxCaloryLimit};
        }
        this.LEVEL_NAME = new String[]{"不达标", "达标"};
    }

    public String getUnit() {
        return "kCal";
    }

    public float getValue() {
        return this.bmr;
    }

    public String getName() {
        return "基础代谢";
    }

    public int getColor() {
        if (getLevel() == 1) {
            return ScaleIndex.COLOR_STANDARD;
        }
        return ScaleIndex.COLOR_FAIL;
    }

    public int getDes() {
        return R.string.a2p;
    }
}
