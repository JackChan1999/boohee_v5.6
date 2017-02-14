package com.boohee.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.boohee.api.ApiUrls;
import com.boohee.api.RecordApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.model.LocalWeightRecord;
import com.boohee.model.User;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.event.LatestWeightEvent;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.JumpBrowserActivity;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.FastJsonUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewUserInitActivity extends BaseActivity {
    private int                REPLACE_FRAGMENT_NEXT = 1;
    private int                REPLACE_FRAGMENT_NONE = -1;
    private int                REPLACE_FRAGMENT_PREV = 2;
    private List<BaseFragment> fragments             = new ArrayList();
    private int       index;
    private boolean   isSendRequest;
    private FileCache mCache;
    private User      user;

    public static void comeOn(Context context) {
        context.startActivity(new Intent(context, NewUserInitActivity.class));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.NewUserInitActivity);
        this.mCache = FileCache.get((Context) this);
        init();
    }

    private void init() {
        this.user = AccountUtils.getUserProfileLocal(this.ctx);
        this.fragments.add(ProfileInitOneFragment.newInstance(this.user));
        this.fragments.add(ProfileInitTwoFragment.newInstance(this.user));
        this.fragments.add(ProfileInitThreeFragment.newInstance(this.user));
        this.fragments.add(ProfileInitFourFragment.newInstance(this.user));
        this.fragments.add(ProfileInitFiveFragment.newInstance(this.user));
        this.index = 0;
        replaceFragment(this.REPLACE_FRAGMENT_NONE);
    }

    public void nextStep() {
        this.index++;
        replaceFragment(this.REPLACE_FRAGMENT_NEXT);
    }

    private void prevStep() {
        this.index--;
        replaceFragment(this.REPLACE_FRAGMENT_PREV);
    }

    private void replaceFragment(int order) {
        setTitle("完善资料" + String.valueOf(this.index + 1) + "/5");
        BaseFragment fragment = (BaseFragment) this.fragments.get(this.index);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (order == this.REPLACE_FRAGMENT_NEXT) {
            transaction.setCustomAnimations(R.anim.a_, R.anim.aa);
        } else if (order == this.REPLACE_FRAGMENT_PREV) {
            transaction.setCustomAnimations(R.anim.a2, R.anim.a3);
        }
        transaction.replace(R.id.content_layout, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void savaChange() {
        if (!this.isSendRequest) {
            this.isSendRequest = true;
            showLoading();
            JsonParams params = new JsonParams();
            params.put("passport_token", UserPreference.getToken(this.ctx));
            params.put("sex_type", this.user.sex_type);
            params.put("birthday", this.user.birthday);
            params.put("height", String.valueOf(this.user.height));
            params.put(UserDao.BEGIN_WEIGHT, String.valueOf(this.user.begin_weight));
            params.put("target_weight", this.user.target_weight);
            if (!TextUtils.isEmpty(this.user.target_date)) {
                params.put("target_date", this.user.target_date);
            }
            if (this.user.diseases != null && this.user.diseases.size() > 0) {
                params.put(UserDao.DISEASES, getDiseaseParams());
            }
            if (!TextUtils.isEmpty(this.user.begin_date)) {
                params.put("begin_date", this.user.begin_date);
            }
            params.put(UserDao.SPORT_CONDITION, this.user.sport_condition);
            RecordApi.createUsersChangeProfile(this.activity, params, new JsonCallback(this
                    .activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    try {
                        JSONObject jsonObj = object.optJSONObject("profile");
                        jsonObj.put("token", UserPreference.getToken(NewUserInitActivity.this.ctx));
                        NewUserInitActivity.this.user = User.parseUser(jsonObj);
                        NewUserInitActivity.this.user.need_test = false;
                        new UserDao(NewUserInitActivity.this.ctx).add(NewUserInitActivity.this
                                .user);
                        OnePreference.setLatestWeight(NewUserInitActivity.this.user.latest_weight);
                        NewUserInitActivity.this.mCache.put("latest_weight", FastJsonUtils.toJson
                                (new LocalWeightRecord(String.valueOf(NewUserInitActivity.this
                                        .user.latest_weight), "")));
                        EventBus.getDefault().post(new LatestWeightEvent());
                        MobclickAgent.onEvent(NewUserInitActivity.this.ctx, Event
                                .MINE_CLICKHEALTHREPORT);
                        JumpBrowserActivity.comeOnBaby(NewUserInitActivity.this.ctx,
                                NewUserInitActivity.this.getString(R.string.t_), BooheeClient
                                        .build(BooheeClient.BINGO).getDefaultURL(ApiUrls
                                                .REPORT_URL));
                        NewUserInitActivity.this.finish();
                        EventBus.getDefault().post(new UserIntEvent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    NewUserInitActivity.this.isSendRequest = false;
                    NewUserInitActivity.this.dismissLoading();
                }
            });
        }
    }

    private JSONArray getDiseaseParams() {
        return new JSONArray(this.user.diseases);
    }

    public void onBackPressed() {
        if (this.index == 0) {
            finish();
        } else {
            prevStep();
        }
    }
}
