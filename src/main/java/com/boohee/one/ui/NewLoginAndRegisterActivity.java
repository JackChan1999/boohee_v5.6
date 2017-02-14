package com.boohee.one.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.account.ForgetPasspordActivity;
import com.boohee.api.PassportApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.ApiError;
import com.boohee.model.User;
import com.boohee.model.status.UserConnection;
import com.boohee.myview.SettingItemView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AppUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.Keyboard;
import com.boohee.utils.SNSLogin;
import com.boohee.utils.TextUtil;
import com.boohee.utils.ThirdLoginHelper;
import com.tencent.tinker.android.dx.instruction.Opcodes;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONObject;

public class NewLoginAndRegisterActivity extends GestureActivity {
    public static final  String LAU_KEY           = "LAU_KEY";
    public static final  int    LAU_TYPE_LOGIN    = 1;
    public static final  int    LAU_TYPE_REGISTER = 2;
    private static final int    UPDATE_TIME       = 272;
    @InjectView(2131427794)
    EditText accountEdit;
    private int actionType = 0;
    @InjectView(2131427795)
    Button          btCaptcha;
    @InjectView(2131427808)
    SettingItemView changeEnvironment;
    @InjectView(2131427796)
    LinearLayout    dealLayout;
    @InjectView(2131427800)
    TextView        forgetPwdText;
    @InjectView(2131427807)
    TextView        leyuLoginText;
    @InjectView(2131427563)
    Button          loginBtn;
    private Captcha         mCaptcha;
    private Handler         mCheckHandler;
    private Context         mContext;
    private UMSocialService mController;
    private int mNumber = 60;
    private SNSLogin              mSNSLogin;
    private UploadSNSInfoListener mUploadSNSInfoListener;
    private User                  mUser;
    @InjectView(2131427806)
    TextView moreLoginText;
    @InjectView(2131427538)
    EditText passWordEdit;
    private String token = "";
    @InjectView(2131427797)
    TextView tvVoiceCaptcha;

    enum Captcha {
        sms,
        voice
    }

    class CheckTimeHandler extends Handler {
        CheckTimeHandler() {
        }

        public void handleMessage(Message msg) {
            NewLoginAndRegisterActivity.this.mNumber = NewLoginAndRegisterActivity.this.mNumber - 1;
            NewLoginAndRegisterActivity.this.refreshTimeView();
        }
    }

    private class UploadSNSInfoListener extends JsonCallback {
        public UploadSNSInfoListener(Context context) {
            super(context);
        }

        public void ok(JSONObject object) {
            super.ok(object);
            NewLoginAndRegisterActivity.this.mUser = User.parsePassportUser(object);
            AccountUtils.saveTokenAndUserKey(NewLoginAndRegisterActivity.this.ctx,
                    NewLoginAndRegisterActivity.this.mUser);
            AccountUtils.saveQQOpenIDAndAccessToken(NewLoginAndRegisterActivity.this.ctx,
                    UserConnection.parseUserConnections(object));
            AccountUtils.getUserProfileAndCheck(NewLoginAndRegisterActivity.this.activity);
        }

        public void onFinish() {
            super.onFinish();
            NewLoginAndRegisterActivity.this.dismissLoading();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.r9);
        setContentView(R.layout.c9);
        ButterKnife.inject((Activity) this);
        this.mContext = this;
        addListener();
        init();
        initTimeCheck();
        initChangeEnvironment();
    }

    private void initChangeEnvironment() {
        this.changeEnvironment.setVisibility(8);
        this.changeEnvironment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ChangeEnvironmentActivity.comeOnBaby(NewLoginAndRegisterActivity.this);
            }
        });
    }

    private void init() {
        this.mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        this.actionType = getIntent().getIntExtra(LAU_KEY, -1);
        setUpUi();
        this.mUploadSNSInfoListener = new UploadSNSInfoListener(this);
        this.mSNSLogin = new SNSLogin(this, this.mController, this.mUploadSNSInfoListener);
    }

    private void initTimeCheck() {
        this.mCheckHandler = new CheckTimeHandler();
    }

    private void addListener() {
        this.passWordEdit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && NewLoginAndRegisterActivity.this.accountEdit
                        .getText().toString().length() > 0) {
                    NewLoginAndRegisterActivity.this.loginBtn.setEnabled(true);
                }
            }
        });
    }

    @OnClick({2131427563, 2131427800, 2131427803, 2131427804, 2131427805, 2131427806, 2131427807,
            2131427795, 2131427797})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                loginAction();
                return;
            case R.id.bt_captcha:
                break;
            case R.id.tv_voice_captcha:
                this.mCaptcha = Captcha.voice;
                requestCaptcha();
                return;
            case R.id.forgetPwdText:
                ForgetPasspordActivity.comeOnBaby(this.activity);
                return;
            case R.id.WeiXinLoginBtn:
                if (AppUtils.isAppInstalled(this, "com.tencent.mm")) {
                    ThirdLoginHelper.weixinLogin(this.mSNSLogin);
                    return;
                } else {
                    Helper.showToast((CharSequence) "请先安装微信");
                    return;
                }
            case R.id.qqLoginBtn:
                SNSLogin.qqLogin(this, "/api/v1/user_connections/authenticate_sns.json", this
                        .mUploadSNSInfoListener);
                return;
            case R.id.weiboLoginBtn:
                ThirdLoginHelper.weiboLogin(this.mSNSLogin);
                return;
            case R.id.moreLoginBtn:
                moreLoginAnim(v);
                return;
            case R.id.leyuLoginBtn:
                showLeyunDialog();
                break;
            default:
                return;
        }
        this.mCaptcha = Captcha.sms;
        requestCaptcha();
    }

    private void refreshTimeView() {
        if (this.mNumber <= 0) {
            this.btCaptcha.setText(R.string.u2);
            this.btCaptcha.setEnabled(true);
            this.tvVoiceCaptcha.setText(R.string.u5);
            this.tvVoiceCaptcha.setEnabled(true);
            this.mCheckHandler.removeMessages(UPDATE_TIME);
            return;
        }
        this.btCaptcha.setTextColor(getResources().getColor(R.color.ju));
        this.btCaptcha.setText(String.valueOf(this.mNumber));
        this.tvVoiceCaptcha.setEnabled(false);
        this.mCheckHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 11) {
            AccountUtils.login(this.activity);
            return;
        }
        UMSsoHandler ssoHandler = this.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, this.actionType == 1 ? R.string.a23 : R.string.r9).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            cancleAction();
        } else if (item.getItemId() == 1) {
            this.actionType = this.actionType == 1 ? 2 : 1;
            if (this.actionType == 2) {
                item.setTitle(R.string.r9);
            } else {
                item.setTitle(R.string.a23);
            }
            setUpUi();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancleAction() {
        Keyboard.closeAll(this.ctx);
        finish();
    }

    private void loginAction() {
        String accountStr = this.accountEdit.getText().toString();
        String pwdStr = this.passWordEdit.getText().toString();
        Keyboard.closeAll(this.ctx);
        if (this.actionType == 1) {
            ThirdLoginHelper.doLogin(this.activity, accountStr, pwdStr);
        } else if (this.actionType == 2) {
            checkCaptcha();
        }
    }

    private void setUpUi() {
        switch (this.actionType) {
            case 1:
                setTitle(R.string.r9);
                this.forgetPwdText.setVisibility(0);
                this.loginBtn.setText(R.string.r9);
                this.dealLayout.setVisibility(8);
                this.passWordEdit.setHint(R.string.yr);
                this.loginBtn.setEnabled(true);
                this.btCaptcha.setVisibility(8);
                this.accountEdit.setHint(R.string.an);
                this.accountEdit.setInputType(1);
                this.passWordEdit.setInputType(Opcodes.INT_TO_LONG);
                return;
            case 2:
                setTitle(R.string.a23);
                this.forgetPwdText.setVisibility(8);
                this.loginBtn.setText(R.string.x4);
                this.dealLayout.setVisibility(0);
                this.leyuLoginText.setVisibility(8);
                this.passWordEdit.setHint(R.string.f0);
                this.loginBtn.setEnabled(false);
                this.btCaptcha.setVisibility(0);
                this.accountEdit.setHint("手机号");
                this.accountEdit.setInputType(2);
                this.passWordEdit.setInputType(2);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        WelcomeActivity.comeOnBaby(this);
        finish();
    }

    public static void comeOnBaby(boolean isLogin, Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, NewLoginAndRegisterActivity.class);
            intent.putExtra(LAU_KEY, isLogin ? 1 : 2);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    private void showLeyunDialog() {
        Builder builder = new Builder(this.mContext);
        builder.setTitle(R.string.qi);
        builder.setNegativeButton(R.string.eq, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View contentView = View.inflate(this.mContext, R.layout.pi, null);
        builder.setView(contentView);
        final EditText accountEdit = (EditText) contentView.findViewById(R.id.accountEdit);
        final EditText passwordEdit = (EditText) contentView.findViewById(R.id.pwdEdit);
        builder.setPositiveButton(R.string.r9, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ThirdLoginHelper.leyuLogin(NewLoginAndRegisterActivity.this.activity, accountEdit
                        .getText().toString(), passwordEdit.getText().toString(),
                        NewLoginAndRegisterActivity.this.mUploadSNSInfoListener);
            }
        });
        builder.create().show();
    }

    private void moreLoginAnim(View v) {
        float translateY;
        float alpha;
        if (v.getTag() == null) {
            translateY = 20.0f;
            alpha = 1.0f;
            v.setTag("clicked");
        } else {
            v.setTag(null);
            translateY = -20.0f;
            alpha = 0.0f;
        }
        ObjectAnimator animTranslateY = ObjectAnimator.ofFloat(this.leyuLoginText,
                "translationY", new float[]{this.leyuLoginText.getTranslationY(), translateY});
        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(this.leyuLoginText, "alpha", new
                float[]{this.leyuLoginText.getAlpha(), alpha});
        final float tempAlpha = alpha;
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                NewLoginAndRegisterActivity.this.leyuLoginText.setVisibility(0);
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                NewLoginAndRegisterActivity.this.leyuLoginText.setVisibility(tempAlpha > 0.0f ? 0
                        : 8);
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        set.playTogether(new Animator[]{animTranslateY, animAlpha});
        set.setDuration(200);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void requestCaptcha() {
        String cellPhone = this.accountEdit.getText().toString().trim();
        if (TextUtil.isPhoneNumber(cellPhone)) {
            showLoading();
            PassportApi.getCellphoneCaptcha(this.activity, cellPhone, this.mCaptcha.name(), new
                    JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (object.has("token")) {
                        NewLoginAndRegisterActivity.this.token = object.opt("token").toString();
                        NewLoginAndRegisterActivity.this.runTimer();
                    }
                }

                public void ok(JSONObject object, boolean hasError) {
                    super.ok(object, hasError);
                    Helper.showLog("NewLoginAndRegisterActivity : ", object.toString());
                    if (object.has("errors") && ApiError.getErrorCode(object) == 107) {
                        NewLoginAndRegisterActivity.this.showAlertDialog();
                    }
                }

                public void onFinish() {
                    NewLoginAndRegisterActivity.this.dismissLoading();
                }

                public void fail(String message) {
                    Helper.showToast((CharSequence) message);
                }
            });
            return;
        }
        Helper.showToast((int) R.string.u3);
    }

    private void runTimer() {
        this.mNumber = 60;
        this.btCaptcha.setTextColor(getResources().getColor(R.color.ju));
        this.btCaptcha.setText(String.valueOf(this.mNumber));
        this.btCaptcha.setEnabled(false);
        this.mCheckHandler.sendEmptyMessageDelayed(UPDATE_TIME, 1000);
    }

    private void checkCaptcha() {
        this.loginBtn.setEnabled(false);
        final String cellPhone = this.accountEdit.getText().toString().trim();
        if (TextUtil.isPhoneNumber(cellPhone)) {
            String captcha = this.passWordEdit.getText().toString().trim();
            if (TextUtils.isEmpty(captcha)) {
                Helper.showToast((int) R.string.u4);
                return;
            }
            showLoading();
            Helper.showLog("check token : ", this.token);
            PassportApi.checkCellphoneCaptcha(this.activity, cellPhone, captcha, this.token, new
                    JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (object.has("token")) {
                        CompleteRegisterActivity.comeOnBaby(NewLoginAndRegisterActivity.this
                                .activity, cellPhone, object.opt("token").toString());
                        NewLoginAndRegisterActivity.this.finish();
                    }
                }

                public void onFinish() {
                    NewLoginAndRegisterActivity.this.dismissLoading();
                    NewLoginAndRegisterActivity.this.loginBtn.setEnabled(true);
                }

                public void fail(String message) {
                    Helper.showToast((CharSequence) message);
                }
            });
            return;
        }
        Helper.showToast((int) R.string.u3);
    }

    private void showAlertDialog() {
        Builder builder = new Builder(this.ctx);
        builder.setTitle("提示").setMessage("此手机号已与其他账号绑定，是否找回密码？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ForgetPasspordActivity.comeOnBaby(NewLoginAndRegisterActivity.this.activity);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mCheckHandler != null) {
            this.mCheckHandler.removeMessages(UPDATE_TIME);
        }
    }
}
