package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ApiUrls;
import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.more.SportRemindReceiver;
import com.boohee.myview.CommonLineView;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.utils.FileUtil;
import com.boohee.utils.TextUtil;
import com.boohee.widgets.AnimCheckBox;
import com.boohee.widgets.AnimCheckBox.OnCheckedChangeListener;
import com.boohee.widgets.LightAlertDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;

import java.util.Calendar;

public class SportSettingActivity extends GestureActivity {
    @InjectView(2131427904)
    AnimCheckBox   checkBox;
    @InjectView(2131427906)
    CommonLineView clvCleanCache;
    private int remindHour   = 0;
    private int remindMinute = 0;
    OnTimeSetListener timeSetListener = new OnTimeSetListener() {
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
            SportSettingActivity.this.tvTime.setText(String.format("每天%d点 %d分", new
                    Object[]{Integer.valueOf(hourOfDay), Integer.valueOf(minute)}));
            SportSettingActivity.this.remindHour = hourOfDay;
            SportSettingActivity.this.remindMinute = minute;
            if (SportSettingActivity.this.remindHour != 0) {
                OnePreference.setPrefSportRemindTime(String.format("%d#%d", new Object[]{Integer
                        .valueOf(SportSettingActivity.this.remindHour), Integer.valueOf
                        (SportSettingActivity.this.remindMinute)}));
                SportRemindReceiver.start(SportSettingActivity.this.activity);
            }
        }
    };
    @InjectView(2131427905)
    TextView tvTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d9);
        ButterKnife.inject((Activity) this);
        initView();
        getCacheSize();
    }

    private void initView() {
        this.checkBox.setChecked(OnePreference.getPrefSportRemind());
        this.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onChange(boolean checked) {
                OnePreference.setPrefSoprtRemind(checked);
            }
        });
        if (!TextUtil.isEmpty(OnePreference.getPrefSportRemindTime())) {
            if (OnePreference.getPrefSportRemindTime().split("#").length == 2) {
                this.tvTime.setText(String.format("每天 %s点 %s分", new Object[]{times[0], times[1]}));
            }
        }
    }

    private void getCacheSize() {
        new Thread() {
            public void run() {
                final long size = FileUtil.getFolderSize(SportSettingActivity.this.ctx
                        .getExternalFilesDir(null));
                SportSettingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        SportSettingActivity.this.clvCleanCache.setRightText((size / 1000) + "."
                                + ((size / 100) % 10) + "M");
                    }
                });
            }
        }.start();
    }

    public void cleanDownloadCahce() {
        LightAlertDialog.create((Context) this, (int) R.string.fp).setNegativeButton((int) R
                .string.eq, null).setPositiveButton((int) R.string.y8, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FileUtil.deleteAllFilesOfDir(SportSettingActivity.this.ctx.getExternalFilesDir
                        (null));
                SportSettingActivity.this.clvCleanCache.setRightText("0.0M");
            }
        }).show();
    }

    @OnClick({2131427902, 2131427903, 2131427906, 2131427901})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clv_sport_history:
                VideoHistoryActivity.comeOn(this.ctx);
                return;
            case R.id.clv_sport_test:
                sportQuestions();
                return;
            case R.id.rl_remind:
                showTimePickerDialog();
                return;
            case R.id.clv_clean_cache:
                cleanDownloadCahce();
                return;
            default:
                return;
        }
    }

    private void sportQuestions() {
        final LightAlertDialog dialog = LightAlertDialog.create(this.ctx, (int) R.string.a69);
        dialog.setNegativeButton((int) R.string.eq, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton((CharSequence) "好的", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BrowserActivity.comeOnBaby(SportSettingActivity.this.ctx, "评测", BooheeClient
                        .build(BooheeClient.BINGO).getDefaultURL(ApiUrls.SPORT_QUESTIONS_URL_V2));
                SportSettingActivity.this.finish();
            }
        });
        dialog.show();
    }

    private void showTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this.timeSetListener, now.get(11),
                now.get(12), true);
        tpd.vibrate(false);
        tpd.setAccentColor(getResources().getColor(R.color.gn));
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, SportSettingActivity.class));
        }
    }
}
