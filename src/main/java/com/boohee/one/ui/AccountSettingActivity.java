package com.boohee.one.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.boohee.account.ChangePasswordActivity;
import com.boohee.account.ChangeProfileActivity1;
import com.boohee.api.PassportApi;
import com.boohee.database.OnePreference;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.User;
import com.boohee.model.status.UserConnection;
import com.boohee.modeldao.UserDao;
import com.boohee.more.PasscodeActivity;
import com.boohee.one.R;
import com.boohee.one.event.UserIntEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.push.PushManager;
import com.boohee.uchoice.IdCardInfoActivity;
import com.boohee.utility.Const;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AccountUtils.OnGetUserProfile;
import com.boohee.utils.Helper;
import com.boohee.utils.SNSLogin;
import com.boohee.utils.ThirdLoginHelper;
import com.boohee.utils.ThirdLoginHelper.OnQQAuthFinishListener;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

public class AccountSettingActivity extends GestureActivity implements OnClickListener {
    static final String TAG            = AccountSettingActivity.class.getName();
    private      int    CHANGE_PROFILE = 0;
    ArrayList<UserConnection> connections;
    UMSocialService           mController;
    private SNSLogin mSNSLogin;
    UserConnection qqConn;
    TextView       qqScreenName;
    TextView       settingPwdText;
    UserConnection sinaConn;
    TextView       sinaScreenName;
    TextView       tvCellPhone;
    TextView       tvPushSetting;
    TextView       tvSecretStatus;
    private User user;
    UserConnection weixinConn;
    TextView       weixinScreenName;

    static /* synthetic */ class AnonymousClass7 {
        static final /* synthetic */ int[] $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA = new
                int[SHARE_MEDIA.values().length];

        static {
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.SINA.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.QZONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[SHARE_MEDIA.WEIXIN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.a6);
        setTitle(R.string.am);
        this.mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        initAccountSetting();
    }

    private void initAccountSetting() {
        this.settingPwdText = this.finder.textView(R.id.tv_set_password);
        this.sinaScreenName = this.finder.textView(R.id.tv_sina_nickname);
        this.weixinScreenName = this.finder.textView(R.id.tv_weixin_nickname);
        this.qqScreenName = this.finder.textView(R.id.tv_qq_nickname);
        this.tvSecretStatus = this.finder.textView(R.id.tv_secret);
        this.tvPushSetting = this.finder.textView(R.id.tv_push);
        this.tvCellPhone = this.finder.textView(R.id.cellphone_text);
        refreshUserView();
        getUserConnections();
        this.mSNSLogin = new SNSLogin(this, this.mController, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                UserPreference.getInstance(AccountSettingActivity.this.ctx).remove("identity");
                AccountSettingActivity.this.getUserConnections();
            }

            public void onFinish() {
                super.onFinish();
                AccountSettingActivity.this.dismissLoading();
            }
        });
    }

    private void refreshUserView() {
        AccountUtils.getUserProfile(this.ctx, new OnGetUserProfile() {
            public void onGetUserProfile(User usr) {
                AccountSettingActivity.this.user = usr;
                if (AccountSettingActivity.this.user == null) {
                    return;
                }
                if (TextUtils.isEmpty(AccountSettingActivity.this.user.cellphone)) {
                    AccountSettingActivity.this.tvCellPhone.setText("请填写");
                } else if (AccountSettingActivity.this.user.cellphone_state) {
                    AccountSettingActivity.this.tvCellPhone.setText(AccountSettingActivity.this
                            .user.cellphone + "(已验证)");
                    AccountSettingActivity.this.tvCellPhone.setTextColor(AccountSettingActivity
                            .this.getResources().getColor(R.color.h5));
                } else {
                    AccountSettingActivity.this.tvCellPhone.setText(AccountSettingActivity.this
                            .user.cellphone + "(未验证)");
                    AccountSettingActivity.this.tvCellPhone.setTextColor(SupportMenu.CATEGORY_MASK);
                }
            }

            public void onGetUserProfileFinish() {
            }
        });
    }

    private void initPrivacyStatus() {
        if (TextUtils.isEmpty(new OnePreference(this.activity).getString(Const.PASSWORD))) {
            this.tvSecretStatus.setText(getString(R.string.a3s));
        } else {
            this.tvSecretStatus.setText(getString(R.string.a3t));
        }
    }

    private void initPushStatus() {
        if (OnePreference.isAcceptPush()) {
            this.tvPushSetting.setText(getString(R.string.a3t));
        } else {
            this.tvPushSetting.setText(getString(R.string.a3s));
        }
    }

    private void getUserConnections() {
        showLoading();
        PassportApi.getUserConnections(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                AccountSettingActivity.this.connections = UserConnection.parseUserConnections
                        (object);
                AccountSettingActivity.this.initSocialAccount();
            }

            public void onFinish() {
                super.onFinish();
                AccountSettingActivity.this.dismissLoading();
            }
        });
    }

    private void initSocialAccount() {
        Iterator it = this.connections.iterator();
        while (it.hasNext()) {
            UserConnection connection = (UserConnection) it.next();
            if (SNSLogin.KEY_SINA_WEIBO.equals(connection.provider)) {
                this.sinaConn = connection;
                this.sinaScreenName.setText(connection.nickname);
                this.sinaScreenName.setTextColor(getResources().getColor(R.color.hk));
            } else if ("weixin".equals(connection.provider)) {
                this.weixinConn = connection;
                this.weixinScreenName.setText(connection.nickname);
                this.weixinScreenName.setTextColor(getResources().getColor(R.color.hk));
            } else if (SNSLogin.KEY_QQ_ZONE.equals(connection.provider)) {
                this.qqConn = connection;
                this.qqScreenName.setText(connection.nickname);
                this.qqScreenName.setTextColor(getResources().getColor(R.color.hk));
            }
        }
        AccountUtils.saveQQOpenIDAndAccessToken(this.ctx, this.connections);
    }

    public void onResume() {
        super.onResume();
        initSettingPwdText();
        initPrivacyStatus();
        initPushStatus();
    }

    private void initSettingPwdText() {
        if (UserPreference.getInstance(this.ctx).getInt("user_type", -1) != 0) {
            this.settingPwdText.setText(R.string.a4h);
        } else {
            this.settingPwdText.setText(R.string.f8);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set_username:
                Intent intent = new Intent(this.ctx, ChangeProfileActivity1.class);
                intent.putExtra(ChangeProfileActivity1.EXTRA_CODE_TEXT, getString(R.string.xl));
                intent.putExtra("code", UserDao.USER_NAME);
                startActivity(intent);
                return;
            case R.id.tv_set_password:
                startActivity(new Intent(this.ctx, ChangePasswordActivity.class));
                return;
            case R.id.cellphone_layout:
                if (this.user != null) {
                    Intent checkIntent = new Intent(this, CheckPhoneActivity.class);
                    checkIntent.putExtra(CheckPhoneActivity.KEY, this.user.cellphone_state ? 1 : 2);
                    if (!(this.user == null || TextUtils.isEmpty(this.user.cellphone))) {
                        checkIntent.putExtra(CheckPhoneActivity.KEY_PHONE, this.user
                                .cellphone_state ? this.user.cellphone + "(已验证)" : this.user
                                .cellphone + "(未验证)");
                    }
                    startActivityForResult(checkIntent, 0);
                    return;
                }
                return;
            case R.id.tv_id_card:
                startActivity(new Intent(this.ctx, IdCardInfoActivity.class));
                return;
            case R.id.ll_weixin:
                clickAuthStatusItem(this.weixinConn, SHARE_MEDIA.WEIXIN);
                return;
            case R.id.ll_sina:
                clickAuthStatusItem(this.sinaConn, SHARE_MEDIA.SINA);
                return;
            case R.id.ll_qq:
                clickAuthStatusItem(this.qqConn, SHARE_MEDIA.QZONE);
                return;
            case R.id.ll_secret:
                settingPrivacy();
                return;
            case R.id.ll_push:
                settingPush();
                return;
            default:
                return;
        }
    }

    private void clickAuthStatusItem(final UserConnection conn, SHARE_MEDIA args) {
        int alert = 0;
        String key = null;
        switch (AnonymousClass7.$SwitchMap$com$umeng$socialize$bean$SHARE_MEDIA[args.ordinal()]) {
            case 1:
                key = SNSLogin.KEY_SINA_WEIBO;
                alert = R.string.aah;
                break;
            case 2:
                key = SNSLogin.KEY_QQ_ZONE;
                alert = R.string.aag;
                break;
            case 3:
                key = "weixin";
                alert = R.string.aai;
                break;
        }
        if (conn != null) {
            final String tmpKey = key;
            LightAlertDialog.create(this.ctx, (int) R.string.aae, alert).setPositiveButton((int)
                    R.string.eq, null).setNegativeButton((int) R.string.i6, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AccountSettingActivity.this.deleteConn(tmpKey, conn);
                }
            }).show();
        } else if (args == SHARE_MEDIA.QZONE) {
            ThirdLoginHelper.doQQAuth(true, false, this, new OnQQAuthFinishListener() {
                public void onQQAuthFinish(boolean isSuccess) {
                    if (isSuccess) {
                        AccountSettingActivity.this.getUserConnections();
                    }
                }
            });
        } else {
            this.mSNSLogin.doRequest(args, "/api/v1/user_connections.json");
        }
    }

    void deleteConn(final String provider, UserConnection conn) {
        showLoading();
        PassportApi.deleteUserConnection(conn.id, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                Helper.showToast((int) R.string.d1);
                if (SNSLogin.KEY_SINA_WEIBO.equals(provider)) {
                    AccountSettingActivity.this.sinaScreenName.setText(R.string.aad);
                    AccountSettingActivity.this.sinaScreenName.setTextColor
                            (AccountSettingActivity.this.getResources().getColor(R.color.dv));
                    AccountSettingActivity.this.sinaConn = null;
                } else if ("weixin".equals(provider)) {
                    AccountSettingActivity.this.weixinScreenName.setText(R.string.aad);
                    AccountSettingActivity.this.weixinScreenName.setTextColor
                            (AccountSettingActivity.this.getResources().getColor(R.color.dv));
                    AccountSettingActivity.this.weixinConn = null;
                } else if (SNSLogin.KEY_QQ_ZONE.equals(provider)) {
                    AccountSettingActivity.this.qqScreenName.setText(R.string.aad);
                    AccountSettingActivity.this.qqScreenName.setTextColor(AccountSettingActivity
                            .this.getResources().getColor(R.color.dv));
                    AccountUtils.qqLogout();
                    AccountSettingActivity.this.qqConn = null;
                }
            }

            public void onFinish() {
                super.onFinish();
                AccountSettingActivity.this.dismissLoading();
            }
        });
    }

    public void onEventMainThread(UserIntEvent mUserIntEvent) {
        refreshUserView();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = this.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (requestCode == this.CHANGE_PROFILE) {
            if (resultCode == -1) {
                refreshUserView();
            }
            EventBus.getDefault().post(new UserIntEvent());
        }
    }

    private void settingPrivacy() {
        if (this.tvSecretStatus.getText().toString().equals(getString(R.string.a3t))) {
            Intent intent = new Intent(this.activity, PasscodeActivity.class);
            intent.setAction(PasscodeActivity.ACTION_PASSWORD_CLOSE);
            startActivity(intent);
            return;
        }
        intent = new Intent(this.activity, PasscodeActivity.class);
        intent.setAction(PasscodeActivity.ACTION_PASSWORD_OPEN);
        startActivity(intent);
    }

    private void settingPush() {
        if (this.tvPushSetting.getText().toString().equals(getString(R.string.a3t))) {
            OnePreference.setPrefAcceptPush(false);
            PushManager.getInstance().pausePush();
            this.tvPushSetting.setText(getString(R.string.a3s));
            return;
        }
        OnePreference.setPrefAcceptPush(true);
        PushManager.getInstance().resumePush();
        this.tvPushSetting.setText(getString(R.string.a3t));
    }
}
