package com.boohee.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.record.EvaluationActivity;
import com.boohee.user.view.UserBirthdayView;
import com.boohee.user.view.UserGenderView;
import com.boohee.user.view.UserHeightView;
import com.boohee.user.view.UserTargetDateView;
import com.boohee.user.view.UserTargetView;
import com.boohee.user.view.UserTargetWeightView;
import com.boohee.user.view.UserWeightView;
import com.boohee.utils.Helper;

import de.greenrobot.event.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class UserInitActivity extends GestureActivity {
    public static        int    ACTION_NEXT  = 0;
    public static        int    ACTION_SAVE  = -1;
    private static final int    KEY_NEXT     = 1;
    private static final int    KEY_PREVIOUS = 0;
    private static final int    KEY_SAVE     = 2;
    static final         String TAG          = UserInitActivity.class.getName();
    private UserBirthdayView birthdayView;
    private int              count;
    private int currentIndex = 0;
    private UserGenderView genderView;
    private UserHeightView heightView;
    private boolean isSendRequest = false;
    private Boolean keepWeight    = Boolean.valueOf(false);
    public  Handler mHandler      = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    Helper.showLog(UserInitActivity.TAG, "keep_weight");
                    UserInitActivity.this.keepWeight = Boolean.valueOf(true);
                    UserInitActivity.this.menu.getItem(0).setVisible(true);
                    UserInitActivity.this.menu.getItem(1).setVisible(false);
                    UserInitActivity.this.menu.getItem(2).setVisible(true);
                    return;
                case 0:
                    UserInitActivity.this.keepWeight = Boolean.valueOf(false);
                    UserInitActivity.this.menu.getItem(0).setVisible(true);
                    UserInitActivity.this.menu.getItem(1).setVisible(true);
                    UserInitActivity.this.menu.getItem(2).setVisible(false);
                    return;
                default:
                    return;
            }
        }
    };
    private User    mUser         = new User();
    private Menu                 menu;
    private UserTargetDateView   targetDateView;
    private UserTargetView       targetView;
    private UserTargetWeightView targetWeightView;
    private ViewFlipper          viewFlipper;
    private UserWeightView       weightView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ny);
        setTitle(R.string.gm);
        EventBus.getDefault().register(this);
        findView();
    }

    private void findView() {
        this.viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        this.count = this.viewFlipper.getChildCount();
        this.genderView = (UserGenderView) findViewById(R.id.gender_view);
        this.birthdayView = (UserBirthdayView) findViewById(R.id.birthday_view);
        this.heightView = (UserHeightView) findViewById(R.id.height_view);
        this.weightView = (UserWeightView) findViewById(R.id.weight_view);
        this.targetView = (UserTargetView) findViewById(R.id.target_view);
        this.targetView.setHandler(this.mHandler);
        this.targetWeightView = (UserTargetWeightView) findViewById(R.id.target_weight_view);
        this.targetDateView = (UserTargetDateView) findViewById(R.id.target_date_view);
    }

    private void refreshMenuItem() {
        if (this.currentIndex == 0) {
            this.menu.getItem(0).setVisible(false);
            this.menu.getItem(1).setVisible(true);
            this.menu.getItem(2).setVisible(false);
        } else if (this.currentIndex == this.count - 1 || (this.keepWeight.booleanValue() && this
                .currentIndex == 4)) {
            this.menu.getItem(0).setVisible(true);
            this.menu.getItem(1).setVisible(false);
            this.menu.getItem(2).setVisible(true);
        } else {
            this.menu.getItem(0).setVisible(true);
            this.menu.getItem(1).setVisible(true);
            this.menu.getItem(2).setVisible(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.add(0, 0, 0, R.string.a03).setShowAsAction(2);
        menu.add(0, 1, 1, R.string.x3).setShowAsAction(2);
        menu.add(0, 2, 2, R.string.ge).setShowAsAction(2);
        refreshMenuItem();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                previousClick();
                return true;
            case 1:
                nextClick();
                return true;
            case 2:
                saveClick();
                return true;
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveClick() {
        if (!this.isSendRequest) {
            this.isSendRequest = true;
            this.mUser.sex_type = this.genderView.getSexType();
            this.mUser.birthday = this.birthdayView.getBirthday();
            this.mUser.height = (float) this.heightView.getUserHeight();
            this.mUser.begin_weight = this.weightView.getUserWeight();
            this.mUser.target_weight = this.targetWeightView.getUserTargetWeight();
            this.mUser.target_date = this.targetDateView.getTargetDate();
            changeProfileRequest();
        }
    }

    private void nextClick() {
        this.viewFlipper.setInAnimation(this.ctx, R.anim.a_);
        this.viewFlipper.setOutAnimation(this.ctx, R.anim.aa);
        this.viewFlipper.showNext();
        this.currentIndex = this.viewFlipper.getDisplayedChild();
        refreshMenuItem();
    }

    private void previousClick() {
        this.viewFlipper.setInAnimation(this.ctx, R.anim.a2);
        this.viewFlipper.setOutAnimation(this.ctx, R.anim.a3);
        this.viewFlipper.showPrevious();
        this.currentIndex = this.viewFlipper.getDisplayedChild();
        refreshMenuItem();
    }

    private void changeProfileRequest() {
        JsonParams params = new JsonParams();
        params.put("passport_token", UserPreference.getToken(this.ctx));
        params.put("sex_type", this.mUser.sex_type);
        params.put("birthday", this.mUser.birthday);
        params.put("height", String.valueOf(this.mUser.height));
        params.put(UserDao.BEGIN_WEIGHT, String.valueOf(this.mUser.begin_weight));
        if (this.keepWeight.booleanValue()) {
            params.put("target_weight", "-1");
        } else {
            params.put("target_weight", String.valueOf(this.mUser.target_weight));
            params.put("target_date", this.mUser.target_date);
        }
        showLoading();
        RecordApi.createUsersChangeProfile(this.activity, params, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    JSONObject jsonObj = object.optJSONObject("profile");
                    jsonObj.put("token", UserPreference.getToken(UserInitActivity.this.ctx));
                    UserInitActivity.this.mUser = User.parseUser(jsonObj);
                    new UserDao(UserInitActivity.this.ctx).add(UserInitActivity.this.mUser);
                    OnePreference.setLatestWeight(UserInitActivity.this.mUser.latest_weight);
                    Intent intent = new Intent(UserInitActivity.this.ctx, EvaluationActivity.class);
                    intent.putExtra(EvaluationActivity.GO_HOME, true);
                    UserInitActivity.this.startActivity(intent);
                    UserInitActivity.this.finish();
                    EventBus.getDefault().post(new UserIntEvent());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                UserInitActivity.this.isSendRequest = false;
                UserInitActivity.this.dismissLoading();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onBackPressed() {
        finish();
    }

    public void onEventMainThread(UserIntEvent userIntEvent) {
    }
}
