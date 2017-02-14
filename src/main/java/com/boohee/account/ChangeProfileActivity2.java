package com.boohee.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.event.PeriodEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.MainActivity;
import com.boohee.user.view.UserBirthdayView;
import com.boohee.user.view.UserGenderView;
import com.boohee.user.view.UserHeightView;
import com.boohee.user.view.UserMcCircleView;
import com.boohee.user.view.UserMcDaysView;
import com.boohee.user.view.UserTargetDateView;
import com.boohee.user.view.UserTargetView;
import com.boohee.user.view.UserTargetWeightView;
import com.boohee.user.view.UserWeightView;
import com.boohee.utility.Const;
import com.boohee.utils.DateFormatUtils;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeProfileActivity2 extends GestureActivity {
    static final String TAG = ChangeProfileActivity2.class.getName();
    private String code;
    String content = null;
    int defaultMc;
    private int         index;
    private int         mcCircle;
    private int         mcDay;
    private User        user;
    private FrameLayout userView;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.f9);
        setContentView(R.layout.ee);
        handleIntent();
        init();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        this.index = intent.getIntExtra("index", -1);
        this.code = intent.getStringExtra("code");
        this.defaultMc = intent.getIntExtra("default_mc", -1);
        this.user = (User) intent.getSerializableExtra(Const.USER);
    }

    private void init() {
        ViewFlipper contentLayout = (ViewFlipper) findViewById(R.id.content);
        switch (this.index) {
            case 3:
                this.userView = new UserGenderView(this.ctx, this.user);
                break;
            case 4:
                this.userView = new UserBirthdayView(this.ctx, this.user);
                break;
            case 5:
                this.userView = new UserHeightView(this.ctx, this.user);
                break;
            case 6:
                this.userView = new UserWeightView(this.ctx, this.user);
                break;
            case 7:
                this.userView = new UserTargetWeightView(this.ctx, this.user);
                break;
            case 8:
                this.userView = new UserTargetDateView(this.ctx, this.user);
                break;
            case 9:
                this.userView = new UserTargetView(this.ctx, true);
                break;
            case 10:
                this.userView = new UserMcCircleView(this.ctx, 1, this.defaultMc);
                break;
            case 11:
                if (this.defaultMc == 0) {
                    this.defaultMc = 5;
                }
                this.userView = new UserMcDaysView(this.ctx, this.defaultMc);
                break;
        }
        contentLayout.addView(this.userView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.y8).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        onSave();
        return true;
    }

    private void onSave() {
        if (this.userView != null) {
            if (this.index == 10) {
                this.mcCircle = ((UserMcCircleView) this.userView).getMcCircle();
                this.content = this.mcCircle + "";
                updateMc();
            } else if (this.index == 11) {
                this.mcDay = ((UserMcDaysView) this.userView).getMcDays();
                this.content = this.mcDay + "";
                updateMc();
            } else {
                updateUser();
            }
        }
    }

    private void updateUser() {
        if (this.user != null) {
            switch (this.index) {
                case 3:
                    this.user.sex_type = ((UserGenderView) this.userView).getSexType();
                    this.content = this.user.sex_type + "";
                    break;
                case 4:
                    this.user.birthday = ((UserBirthdayView) this.userView).getBirthday();
                    this.content = this.user.birthday;
                    break;
                case 5:
                    this.user.height = (float) ((UserHeightView) this.userView).getUserHeight();
                    this.content = this.user.height + "";
                    break;
                case 6:
                    this.user.begin_weight = ((UserWeightView) this.userView).getUserWeight();
                    this.content = this.user.begin_weight + "";
                    break;
                case 7:
                    this.user.target_weight = ((UserTargetWeightView) this.userView)
                            .getUserTargetWeight();
                    if (this.user.begin_weight <= 0.0f || this.user.begin_weight >= this.user
                            .target_weight) {
                        this.content = this.user.target_weight + "";
                        break;
                    } else {
                        Helper.showToast((CharSequence) "目标体重要比初始体重小哦~");
                        return;
                    }
                    break;
                case 8:
                    this.user.target_date = ((UserTargetDateView) this.userView).getTargetDate();
                    if (!new Date().after(DateFormatUtils.string2date(this.user.target_date,
                            "yyyy-MM-dd"))) {
                        this.content = this.user.target_date;
                        break;
                    } else {
                        Helper.showToast((CharSequence) "目标日期至少要在今天之后哦~");
                        return;
                    }
                case 9:
                    this.user.target_weight = ((UserTargetView) this.userView)
                            .getUserTargetWeight();
                    this.content = this.user.target_weight + "";
                    break;
            }
            RecordApi.updateUsersChangeProfile(this.activity, this.code, this.content, new
                    JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    Intent intent = new Intent();
                    try {
                        User user = User.parseUser(object.getJSONObject("profile"));
                        new UserDao(ChangeProfileActivity2.this.ctx).add(user);
                        intent.putExtra(Const.USER, user);
                        ChangeProfileActivity2.this.setResult(-1, intent);
                        UserPreference userPreference = UserPreference.getInstance
                                (ChangeProfileActivity2.this.ctx);
                        if (user.sex_type != null) {
                            userPreference.putString("sex_type", user.sex_type);
                        }
                        if (9 == ChangeProfileActivity2.this.index) {
                            ChangeProfileActivity2.this.setTargetWeight();
                        }
                        if (7 == ChangeProfileActivity2.this.index) {
                            ChangeProfileActivity2.this.setTargetDate();
                        }
                        if (8 == ChangeProfileActivity2.this.index) {
                            ChangeProfileActivity2.this.isGoHome();
                        }
                        if (10 == ChangeProfileActivity2.this.index || 11 ==
                                ChangeProfileActivity2.this.index) {
                            EventBus.getDefault().post(new PeriodEvent());
                        }
                        ChangeProfileActivity2.this.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void updateMc() {
        UserPreference.getInstance(this.activity).putInt(this.code, Integer.parseInt(this.content));
        RecordApi.updateMcUpdateSummaries(this.activity, this.code, this.content, new
                JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Intent intent = new Intent();
                intent.putExtra(ChangeProfileActivity2.this.code, ChangeProfileActivity2.this
                        .content);
                ChangeProfileActivity2.this.setResult(-1, intent);
                ChangeProfileActivity2.this.finish();
            }
        });
    }

    private void setTargetWeight() {
        if (9 == this.index && !this.content.equals("-1.0")) {
            startChangeProfileActivity2(7, "target_weight");
        } else if (9 == this.index && this.content.equals("-1.0") && !MyApplication
                .getIsMainOpened()) {
            MainActivity.comeOnBaby(this);
        }
    }

    private void setTargetDate() {
        if (7 == this.index) {
            startChangeProfileActivity2(8, "target_date");
        }
    }

    private void isGoHome() {
        if (!MyApplication.getIsMainOpened()) {
            MainActivity.comeOnBaby(this);
        }
    }

    public void onBackPressed() {
        finish();
    }

    private void startChangeProfileActivity2(int index, String code) {
        Intent intent = new Intent(this.ctx, ChangeProfileActivity2.class);
        intent.putExtra("index", index);
        intent.putExtra(Const.USER, this.user);
        intent.putExtra("code", code);
        intent.putExtra("default_mc", 0);
        startActivity(intent);
    }
}
