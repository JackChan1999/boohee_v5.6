package com.boohee.one.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.database.OnePreference;
import com.boohee.model.Alarm;
import com.boohee.model.Splash;
import com.boohee.model.User;
import com.boohee.modeldao.AlarmDao;
import com.boohee.more.PasscodeActivity;
import com.boohee.more.RemindService;
import com.boohee.more.SportRemindReceiver;
import com.boohee.one.R;
import com.boohee.one.http.IPCheck;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.utility.Const;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.FastJsonUtils;

import java.util.ArrayList;

import org.json.JSONObject;

public class SplashActivity extends BaseNoToolbarActivity {
    static final String  TAG        = SplashActivity.class.getSimpleName();
    private      int     MAX_TIME   = 3;
    private      String  SPALSH_API = "/api/v1/app_square/start_up_with_ad";
    private      int     count      = this.MAX_TIME;
    private      Handler handler    = new Handler();
    @InjectView(2131427877)
    ImageView imgStartLogo;
    private boolean isAd;
    @InjectView(2131427878)
    ImageView ivAdContent;
    @InjectView(2131427880)
    ImageView ivThirdLogo;
    Runnable r = new Runnable() {
        public void run() {
            if (SplashActivity.this.count > 0) {
                if (SplashActivity.this.isAd) {
                    SplashActivity.this.tvTime.setVisibility(0);
                    SplashActivity.this.tvTime.setText(String.valueOf(SplashActivity.this.count));
                }
                SplashActivity.this.count = SplashActivity.this.count - 1;
                SplashActivity.this.handler.postDelayed(SplashActivity.this.r, 1000);
                return;
            }
            SplashActivity.this.finishToJump();
        }
    };
    private Splash splash;
    @InjectView(2131427879)
    TextView tvAdTitle;
    @InjectView(2131427736)
    TextView tvTime;
    private User user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.d2);
        ButterKnife.inject((Activity) this);
        getWindow().getDecorView().post(new Runnable() {
            public void run() {
                SplashActivity.this.init();
            }
        });
    }

    private void init() {
        BooheeClient.build("status").get(this.SPALSH_API, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                super.ok(object);
                SplashActivity.this.splash = (Splash) FastJsonUtils.fromJson(object, Splash.class);
                if (SplashActivity.this.splash != null) {
                    SplashActivity.this.isAd = SplashActivity.this.splash.is_ad;
                    SplashActivity.this.imageLoader.displayImage(SplashActivity.this.splash
                            .start_up_url, SplashActivity.this.ivAdContent, ImageLoaderOptions
                            .global((int) R.drawable.x1));
                    if (TextUtils.isEmpty(SplashActivity.this.splash.link) || !SplashActivity
                            .this.isAd) {
                        SplashActivity.this.tvAdTitle.setVisibility(8);
                        return;
                    }
                    SplashActivity.this.tvAdTitle.setVisibility(0);
                    SplashActivity.this.tvAdTitle.setText(SplashActivity.this.splash.text);
                    final Uri uri = Uri.parse(SplashActivity.this.splash.link);
                    SplashActivity.this.tvAdTitle.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(uri);
                            SplashActivity.this.startActivity(intent);
                            SplashActivity.this.handler.removeCallbacks(SplashActivity.this.r);
                        }
                    });
                    SplashActivity.this.tvTime.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SplashActivity.this.finishToJump();
                        }
                    });
                }
            }

            public void fail(String message) {
            }
        }, this.ctx);
        appInit();
    }

    private void appInit() {
        IPCheck.ipTest();
        if (OnePreference.getPrefSportRemind()) {
            SportRemindReceiver.start(this.ctx);
        }
    }

    private void finishToJump() {
        this.handler.removeCallbacks(this.r);
        if (getPwd() != null) {
            Intent intent = new Intent(this, PasscodeActivity.class);
            intent.setAction(PasscodeActivity.ACTION_PASSWORD_INPUT);
            startActivity(intent);
            finish();
        } else if (AccountUtils.isReleaseUser()) {
            AccountUtils.login(this);
        } else {
            WelcomeActivity.comeOnBaby(this);
            finish();
        }
    }

    protected void onStart() {
        super.onStart();
        this.handler.post(this.r);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacks(this.r);
    }

    private void openRemind() {
        AlarmDao alarmDao = new AlarmDao(this);
        ArrayList<Alarm> alarms = alarmDao.getAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            RemindService.start((Alarm) alarms.get(i), this);
        }
        alarmDao.closeDB();
    }

    private String getPwd() {
        return new OnePreference(this).getString(Const.PASSWORD);
    }
}
