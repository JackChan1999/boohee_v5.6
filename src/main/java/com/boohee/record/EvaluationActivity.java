package com.boohee.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.account.NewUserInitActivity;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.modeldao.WeightRecordDao;
import com.boohee.myview.BooheeCircleProgressBar;
import com.boohee.one.R;
import com.boohee.one.ui.HelpInfoActivity;
import com.boohee.one.ui.MainActivity;
import com.boohee.status.FriendShipActivity;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.DateHelper;

public class EvaluationActivity extends GestureActivity implements OnClickListener {
    public static final String GO_HOME = "GO_HOME";
    static final        String TAG     = EvaluationActivity.class.getName();
    private String   begin_date;
    private boolean  goHome;
    private TextView lastUpdatedText;
    private User     user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        setTitle(R.string.l8);
        setContentView(R.layout.f5);
        getUserAndBeginDate();
        setLastUpdated();
        setBmi();
        setHealthyWeight();
        setTargetCalory();
        setHeartRate();
        setBodyAge();
        setBodyFatRate();
        addListener();
    }

    private void handleIntent() {
        this.goHome = getIntent().getBooleanExtra(GO_HOME, false);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, R.string.ot).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                if (this.goHome) {
                    AccountUtils.goHome(this.activity, MainActivity.class);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addListener() {
        findViewById(R.id.mine_report_whatis_bmi_area).setOnClickListener(this);
        findViewById(R.id.mine_report_whatis_body_age_area).setOnClickListener(this);
        findViewById(R.id.mine_report_whatis_bodyfat_area).setOnClickListener(this);
        findViewById(R.id.mine_report_whatis_budget_hot_area).setOnClickListener(this);
        findViewById(R.id.mine_report_whatis_heartRate_area).setOnClickListener(this);
        findViewById(R.id.view_retest).setOnClickListener(this);
    }

    private void getUserAndBeginDate() {
        this.user = AccountUtils.getUserProfileLocal(this.ctx);
        if (this.user == null) {
            finish();
        }
        this.begin_date = this.user.updated_at;
    }

    private void setLastUpdated() {
        this.lastUpdatedText = (TextView) findViewById(R.id.last_updated);
        this.lastUpdatedText.setVisibility(4);
        this.lastUpdatedText.setText(getString(R.string.qe) + this.begin_date);
    }

    private void setBmi() {
        float bmi = this.user.calcBmi(new WeightRecordDao(this).getLastestWeight());
        ((TextView) findViewById(R.id.bmi_text)).setText(String.valueOf(bmi));
        ImageView bmiWheel = (ImageView) findViewById(R.id.bmi_wheel);
        Animation rotateAnim = new RotateAnimation(-180.0f, (float) (((double) ((bmi - 27.5f) *
                360.0f)) / 50.0d), 1, 0.5f, 1, 0.5f);
        rotateAnim.setDuration(3000);
        rotateAnim.setInterpolator(new OvershootInterpolator());
        rotateAnim.setFillAfter(true);
        bmiWheel.setAnimation(rotateAnim);
        rotateAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    private void setHealthyWeight() {
        double standardWeight;
        float[] h = this.user.calcHealthyWeight();
        TextView healthyWeight = (TextView) findViewById(R.id.healthy_weight_text);
        if (this.user.isMale()) {
            standardWeight = ((double) (this.user.height - 100.0f)) * 0.9d;
        } else {
            standardWeight = (((double) (this.user.height - 100.0f)) * 0.9d) - 2.5d;
        }
        healthyWeight.setText(String.format("%.1f", new Object[]{Double.valueOf(standardWeight)}));
        ((TextView) findViewById(R.id.healthy_weight_range_text)).setText(h[0] + "~" + h[1] + "kg");
    }

    private void setTargetCalory() {
        TextView targetCaloryText = (TextView) findViewById(R.id.target_calory_text);
        int targetCalory = this.user.target_calory;
        targetCaloryText.setText(targetCalory + "");
        ((TextView) findViewById(R.id.recommend_calory_range)).setText((targetCalory - 50) +
                getString(R.string.pw) + "~" + (targetCalory + 50) + getString(R.string.pw));
    }

    private void setBodyAge() {
        BooheeCircleProgressBar bodyAgeBar = (BooheeCircleProgressBar) findViewById(R.id
                .mine_report_body_age_progressBar);
        TextView bodyAgeText = (TextView) findViewById(R.id.mine_report_body_age_textView);
        int bodyAge = this.user.calcBodyAge(new WeightRecordDao(this.ctx).getLastestWeight());
        bodyAgeBar.setProgress(bodyAge);
        bodyAgeText.setText(bodyAge + "");
        bodyAgeBar.startAnimation(AnimationUtils.loadAnimation(this, 17432576));
    }

    private void setBodyFatRate() {
        ImageView bodyFatRateImage = (ImageView) findViewById(R.id.mine_report_body_fat_imageView);
        double fatRate = Math.floor((double) this.user.calcBodyFat(new WeightRecordDao(this.ctx)
                .getLastestWeight())) / 100.0d;
        ((TextView) findViewById(R.id.mine_report_body_fat_textView)).setText(((int) (fatRate *
                100.0d)) + "%");
        try {
            if (DateHelper.getAge(DateHelper.parseString(this.user.birthday)) >= 40) {
                if (this.user.isMale()) {
                    if (fatRate < 0.2d) {
                        setBodyFatRateImage(R.drawable.m0, bodyFatRateImage);
                    } else if (fatRate > 0.24d) {
                        setBodyFatRateImage(R.drawable.lz, bodyFatRateImage);
                    } else {
                        setBodyFatRateImage(R.drawable.m1, bodyFatRateImage);
                    }
                } else if (fatRate < 0.27d) {
                    setBodyFatRateImage(R.drawable.mk, bodyFatRateImage);
                } else if (fatRate > 0.32d) {
                    setBodyFatRateImage(R.drawable.mj, bodyFatRateImage);
                } else {
                    setBodyFatRateImage(R.drawable.ml, bodyFatRateImage);
                }
            } else if (this.user.isMale()) {
                if (fatRate < 0.14d) {
                    setBodyFatRateImage(R.drawable.n_, bodyFatRateImage);
                } else if (fatRate > 0.19d) {
                    setBodyFatRateImage(R.drawable.n9, bodyFatRateImage);
                } else {
                    setBodyFatRateImage(R.drawable.na, bodyFatRateImage);
                }
            } else if (fatRate < 0.17d) {
                setBodyFatRateImage(R.drawable.ld, bodyFatRateImage);
            } else if (fatRate > 0.26d) {
                setBodyFatRateImage(R.drawable.lc, bodyFatRateImage);
            } else {
                setBodyFatRateImage(R.drawable.le, bodyFatRateImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBodyFatRateImage(int id, ImageView bodyFatImage) {
        this.imageLoader.displayImage(null, bodyFatImage, ImageLoaderOptions.global(id));
    }

    private void setHeartRate() {
        int[] heartRate = this.user.calcHeartRate();
        ((TextView) findViewById(R.id.heart_rate_text)).setText(heartRate[0] + "~" + heartRate[1]);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_report_whatis_bmi_area:
                startActivity(new Intent(this.ctx, HelpInfoActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 0));
                return;
            case R.id.view_retest:
                startActivity(new Intent(this.ctx, NewUserInitActivity.class));
                finish();
                return;
            case R.id.mine_report_whatis_heartRate_area:
                startActivity(new Intent(this.ctx, HelpInfoActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 4));
                return;
            case R.id.mine_report_whatis_body_age_area:
                startActivity(new Intent(this.ctx, HelpInfoActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 2));
                return;
            case R.id.mine_report_whatis_bodyfat_area:
                startActivity(new Intent(this.ctx, HelpInfoActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 3));
                return;
            case R.id.mine_report_whatis_budget_hot_area:
                startActivity(new Intent(this.ctx, HelpInfoActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 1));
                return;
            default:
                return;
        }
    }
}
