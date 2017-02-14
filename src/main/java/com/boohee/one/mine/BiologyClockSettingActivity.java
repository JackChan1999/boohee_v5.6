package com.boohee.one.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.ChangeProfileActivity2;
import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.mine.McSummary;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import org.json.JSONObject;

public class BiologyClockSettingActivity extends GestureActivity {
    private int mcCircle;
    private int mcCount;
    private int mcDay;
    @InjectView(2131428691)
    TextView tvCircle;
    @InjectView(2131428693)
    TextView tvCount;
    @InjectView(2131428689)
    TextView tvDays;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.kk);
        ButterKnife.inject((Activity) this);
        setTitle(R.string.ap);
    }

    protected void onResume() {
        super.onResume();
        getMcStatus();
    }

    private void getMcStatus() {
        RecordApi.getMcPeriodsLatest(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!BiologyClockSettingActivity.this.isFinishing()) {
                    McSummary mcSummary = McSummary.parseSelf(object.optJSONObject("mc_summary"));
                    if (mcSummary != null && mcSummary.cycle != 0) {
                        UserPreference userPrefernce = UserPreference.getInstance
                                (BiologyClockSettingActivity.this.ctx);
                        userPrefernce.putInt(SportRecordDao.DURATION, mcSummary.duration);
                        userPrefernce.putInt("cycle", mcSummary.cycle);
                        BiologyClockSettingActivity.this.mcCircle = mcSummary.cycle;
                        BiologyClockSettingActivity.this.mcDay = mcSummary.duration;
                        BiologyClockSettingActivity.this.mcCount = mcSummary.count;
                        BiologyClockSettingActivity.this.initMc();
                    }
                }
            }
        });
    }

    private void initMc() {
        if (this.mcDay != 0) {
            this.tvDays.setText(this.mcDay + "");
        } else {
            this.tvDays.setText(getString(R.string.zn));
        }
        if (this.mcCircle != 0) {
            this.tvCircle.setText(this.mcCircle + "");
        } else {
            this.tvCircle.setText(getString(R.string.zn));
        }
        if (this.mcCount != 0) {
            this.tvCount.setText(this.mcCount + "");
        }
    }

    @OnClick({2131428690, 2131428688, 2131428692})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mc_days_click:
                startChangeProfileActiivty2(11, SportRecordDao.DURATION, this.mcDay);
                return;
            case R.id.mc_circle_click:
                if (this.mcDay == 0) {
                    Helper.showToast(this.ctx, getString(R.string.mc_day_first));
                    return;
                } else {
                    startChangeProfileActiivty2(10, "cycle", this.mcDay);
                    return;
                }
            case R.id.mc_history:
                McListActivity.comeOnBaby(this.activity);
                return;
            default:
                return;
        }
    }

    private void startChangeProfileActiivty2(int index, String code, int defaultMc) {
        Intent intent = new Intent(this.ctx, ChangeProfileActivity2.class);
        intent.putExtra("code", code);
        intent.putExtra("index", index);
        intent.putExtra("default_mc", defaultMc);
        startActivityForResult(intent, 0);
    }
}
