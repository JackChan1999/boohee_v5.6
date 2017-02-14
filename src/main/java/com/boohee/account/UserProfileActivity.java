package com.boohee.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.model.mine.McSummary;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.modeldao.UserDao;
import com.boohee.one.R;
import com.boohee.one.event.AvatarEvent;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.CheckPhoneActivity;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.Const;
import com.boohee.utility.Event;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AccountUtils.OnGetUserProfile;
import com.boohee.utils.BlackTech;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.PhotoPickerHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

public class UserProfileActivity extends GestureActivity {
    public static final  int    CHANGE_PROFILE = 0;
    private static final int    SELECT_PHOTOS  = 2;
    public static        String SPACE_DOMAIN   = (BlackTech.isApiProduction().booleanValue() ?
            "http://one.boohee.cn/" : "http://77g98s.com1.z0.glb.clouddn.com/");
    static final         String TAG            = UserProfileActivity.class.getSimpleName();
    public static User            user;
    private       CircleImageView avatarImage;
    private       TextView        birthday;
    private       TextView        cellphone;
    private int cycle = 0;
    private TextView description;
    private int duration = 0;
    private TextView            gender;
    private TextView            height;
    private LinearLayout        ll_mc;
    private LinearLayout        ll_target;
    private TextView            mcCircle;
    private TextView            mcDays;
    private PopupWindow         menu;
    private DisplayImageOptions options;
    private TextView            targetDate;
    private TextView            targetWeight;
    private TextView            userName;
    private UserPreference      userPreference;
    private TextView            weight;
    private TextView            weight_mode_switch;

    public static void comeOn(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        context.startActivity(intent);
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.zb);
        setContentView(R.layout.o1);
        this.options = ImageLoaderOptions.noImage();
        this.userPreference = UserPreference.getInstance(this.ctx);
        findView();
        MobclickAgent.onEvent(this.ctx, Event.MINE_ACCOUNT);
        getUserProfile();
        EventBus.getDefault().register(this);
    }

    private void findView() {
        this.avatarImage = (CircleImageView) findViewById(R.id.avatar_image);
        this.userName = (TextView) findViewById(R.id.txt_nickname);
        this.description = (TextView) findViewById(R.id.txt_persional_statement);
        this.cellphone = (TextView) findViewById(R.id.cellphone_text);
        this.gender = (TextView) findViewById(R.id.gender_text);
        this.birthday = (TextView) findViewById(R.id.birthday_text);
        this.height = (TextView) findViewById(R.id.height_text);
        this.weight = (TextView) findViewById(R.id.weight_text);
        this.targetWeight = (TextView) findViewById(R.id.target_weight_text);
        this.targetDate = (TextView) findViewById(R.id.target_date_text);
        this.weight_mode_switch = (TextView) findViewById(R.id.weight_mode_switch);
        this.mcDays = (TextView) findViewById(R.id.mc_day_text);
        this.mcCircle = (TextView) findViewById(R.id.mc_circle_text);
        this.ll_mc = (LinearLayout) findViewById(R.id.ll_mc);
        this.ll_target = (LinearLayout) findViewById(R.id.ll_target);
    }

    private void initPopupBtnListener(View view) {
        view.findViewById(R.id.select_photos).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PhotoPickerHelper.show(UserProfileActivity.this.activity, 2);
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserProfileActivity.this.menu.dismiss();
            }
        });
    }

    private void getUserProfile() {
        showLoading();
        AccountUtils.getUserProfile(this, new OnGetUserProfile() {
            public void onGetUserProfile(User tmpUser) {
                if (tmpUser != null) {
                    UserProfileActivity.user = tmpUser;
                } else {
                    UserProfileActivity.user = AccountUtils.getUserProfileLocal
                            (UserProfileActivity.this.ctx);
                }
                UserProfileActivity.this.initUI();
            }

            public void onGetUserProfileFinish() {
                UserProfileActivity.this.dismissLoading();
            }
        });
    }

    private void initUI() {
        if (user == null) {
            Helper.showLog(TAG, "--> user is null");
            return;
        }
        user.user_key = UserPreference.getUserKey(this.ctx);
        user.token = UserPreference.getToken(this.ctx);
        if (TextUtils.isEmpty(user.avatar_url)) {
            this.avatarImage.setBackgroundResource(R.drawable.aa0);
        } else {
            this.imageLoader.displayImage(user.avatar_url + "?imageView2/1/w/70/h/70", this
                    .avatarImage, this.options);
        }
        this.userName.setText(user.user_name);
        if (user.description != null) {
            this.description.setText(user.description);
        }
        if (user.cellphone == null) {
            this.cellphone.setText("请填写");
        } else if (user.cellphone_state) {
            this.cellphone.setText(user.cellphone + "(已验证)");
            this.cellphone.setTextColor(getResources().getColor(R.color.h5));
        } else {
            this.cellphone.setText(user.cellphone + "(未验证)");
            this.cellphone.setTextColor(SupportMenu.CATEGORY_MASK);
        }
        if (user.sex_type != null) {
            this.gender.setText(user.sexName());
        }
        if (user.birthday != null) {
            this.birthday.setText(user.birthday());
        }
        if (user.height > 0.0f) {
            this.height.setText(user.height() + " cm");
        }
        if (user.begin_weight > 0.0f) {
            this.weight.setText(user.beginWeight() + " kg");
        }
        if (user.target_weight == -1.0f) {
            this.weight_mode_switch.setText(getString(R.string.px));
            this.ll_target.setVisibility(8);
        } else {
            this.weight_mode_switch.setText(getString(R.string.rh));
            this.ll_target.setVisibility(0);
            if (user.target_weight > 0.0f) {
                this.targetWeight.setText(user.targetWeight() + " kg");
            }
            if (user.target_date != null) {
                this.targetDate.setText(user.targetDate());
            }
        }
        if (user.isMale()) {
            this.ll_mc.setVisibility(8);
            return;
        }
        this.ll_mc.setVisibility(0);
        initMc();
    }

    public void onClick(View v) {
        int i = 1;
        if (HttpUtils.isNetworkAvailable(this.ctx)) {
            Intent intent = new Intent(this.ctx, ChangeProfileActivity1.class);
            switch (v.getId()) {
                case R.id.cellphone_layout:
                    if (user != null) {
                        Intent checkIntent = new Intent(this, CheckPhoneActivity.class);
                        String str = CheckPhoneActivity.KEY;
                        if (!user.cellphone_state) {
                            i = 2;
                        }
                        checkIntent.putExtra(str, i);
                        if (!(user == null || TextUtils.isEmpty(user.cellphone))) {
                            checkIntent.putExtra(CheckPhoneActivity.KEY_PHONE, user
                                    .cellphone_state ? user.cellphone + "(已验证)" : user.cellphone
                                    + "(未验证)");
                        }
                        startActivityForResult(checkIntent, 0);
                        return;
                    }
                    return;
                case R.id.rl_avatar_image:
                    if (this.menu == null) {
                        LinearLayout view = (LinearLayout) LayoutInflater.from(this.ctx).inflate
                                (R.layout.o2, null);
                        this.menu = new PopupWindow(view, -1, -2, true);
                        this.menu.setOutsideTouchable(true);
                        this.menu.setAnimationStyle(R.style.f0);
                        this.menu.setBackgroundDrawable(new BitmapDrawable(getResources()));
                        initPopupBtnListener(view);
                    }
                    this.menu.showAtLocation(findViewById(R.id.main_layout), 80, 0, 0);
                    return;
                case R.id.rl_nickname:
                    intent.putExtra("code", UserDao.USER_NAME);
                    intent.putExtra(ChangeProfileActivity1.EXTRA_CODE_TEXT, getString(R.string.xl));
                    if (!(user == null || user.user_name == null)) {
                        intent.putExtra(ChangeProfileActivity1.EXTRA_DEFAULT_CONTENT, user
                                .user_name);
                    }
                    startActivityForResult(intent, 0);
                    return;
                case R.id.rl_persional_statement:
                    intent.putExtra("code", "description");
                    intent.putExtra(ChangeProfileActivity1.EXTRA_CODE_TEXT, getString(R.string.za));
                    if (!(user == null || user.description == null)) {
                        intent.putExtra(ChangeProfileActivity1.EXTRA_DEFAULT_CONTENT, user
                                .description);
                    }
                    startActivityForResult(intent, 0);
                    return;
                case R.id.gender_layout:
                    startChangeProfileActiivty2(3, "sex_type");
                    return;
                case R.id.birthday_layout:
                    startChangeProfileActiivty2(4, "birthday");
                    return;
                case R.id.height_layout:
                    startChangeProfileActiivty2(5, "height");
                    return;
                case R.id.weight_layout:
                    startChangeProfileActiivty2(6, UserDao.BEGIN_WEIGHT);
                    return;
                case R.id.weight_mode_switch_layout:
                    startChangeProfileActiivty2(9, "target_weight");
                    return;
                case R.id.target_weight_layout:
                    startChangeProfileActiivty2(7, "target_weight");
                    return;
                case R.id.target_date_layout:
                    startChangeProfileActiivty2(8, "target_date");
                    return;
                case R.id.btn_init:
                    NewUserInitActivity.comeOn(this.ctx);
                    return;
                case R.id.mc_day_layout:
                    startChangeProfileActiivty2(11, SportRecordDao.DURATION);
                    return;
                case R.id.mc_circle_layout:
                    if (this.duration == 0) {
                        Helper.showToast(this.ctx, getString(R.string.mc_day_first));
                        return;
                    } else {
                        startChangeProfileActiivty2(10, "cycle");
                        return;
                    }
                default:
                    return;
            }
        }
        Helper.showToast(this.ctx, (int) R.string.r6);
    }

    private void startChangeProfileActiivty2(int index, String code) {
        Intent intent = new Intent(this.ctx, ChangeProfileActivity2.class);
        intent.putExtra("index", index);
        intent.putExtra(Const.USER, user);
        intent.putExtra("code", code);
        intent.putExtra("default_mc", this.duration);
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.menu != null) {
            this.menu.dismiss();
        }
        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    getUserProfile();
                }
                EventBus.getDefault().post(new UserIntEvent());
                return;
            case 2:
                if (resultCode == -1) {
                    List<String> mSelectPath = data.getStringArrayListExtra
                            (MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        uploadAvatar(Uri.fromFile(new File((String) mSelectPath.get(0))));
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void uploadAvatar(Uri uri) {
        if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
            showLoading();
            QiniuUploader.upload(Prefix.one, new UploadHandler() {
                public void onSuccess(List<QiniuModel> infos) {
                    if (infos != null && infos.size() > 0) {
                        UserProfileActivity.this.changeAvatar((QiniuModel) infos.get(0));
                    }
                }

                public void onError(String msg) {
                    Helper.showToast((CharSequence) msg);
                }

                public void onFinish() {
                    UserProfileActivity.this.dismissLoading();
                }
            }, uri.getPath());
        }
    }

    private void changeAvatar(final QiniuModel info) {
        RecordApi.updateUsersChangeProfile(this.activity, "avatar_url", SPACE_DOMAIN + info.key,
                new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (!TextUtils.isEmpty(info.path)) {
                    UserProfileActivity.this.imageLoader.displayImage("file://" + info.path,
                            UserProfileActivity.this.avatarImage, UserProfileActivity.this.options);
                    EventBus.getDefault().post(new UserIntEvent());
                    EventBus.getDefault().post(new AvatarEvent().setAvatar(info.path));
                }
            }

            public void onFinish() {
                UserProfileActivity.this.dismissLoading();
            }
        });
    }

    private void initMc() {
        RecordApi.getMcPeriodsLatest(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                McSummary mcSummary = McSummary.parseSelf(object.optJSONObject("mc_summary"));
                if (mcSummary != null) {
                    UserProfileActivity.this.duration = mcSummary.duration;
                    UserProfileActivity.this.cycle = mcSummary.cycle;
                    if (UserProfileActivity.this.duration > 0) {
                        UserProfileActivity.this.mcDays.setText(String.valueOf
                                (UserProfileActivity.this.duration));
                    } else {
                        UserProfileActivity.this.mcDays.setText(UserProfileActivity.this
                                .getString(R.string.zn));
                    }
                    if (UserProfileActivity.this.cycle > 0) {
                        UserProfileActivity.this.mcCircle.setText(String.valueOf
                                (UserProfileActivity.this.cycle));
                    } else {
                        UserProfileActivity.this.mcCircle.setText(UserProfileActivity.this
                                .getString(R.string.zn));
                    }
                }
            }
        });
    }

    public void onEventMainThread(UserIntEvent event) {
        getUserProfile();
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
