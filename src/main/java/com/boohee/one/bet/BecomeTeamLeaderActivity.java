package com.boohee.one.bet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.myview.IntFloatWheelView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.uploader.QiniuConfig.Prefix;
import com.boohee.uploader.QiniuModel;
import com.boohee.uploader.QiniuUploader;
import com.boohee.uploader.UploadHandler;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.Helper;
import com.boohee.utils.PhotoPickerHelper;
import com.boohee.utils.TextUtil;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.constants.ConstantsAPI.Token;
import com.umeng.socialize.net.utils.SocializeProtocolConstants;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import org.json.JSONObject;

public class BecomeTeamLeaderActivity extends GestureActivity {
    private static final int SELECT_PHOTOS = 2;
    @InjectView(2131427494)
    EditText et_contact;
    @InjectView(2131427482)
    EditText et_experience;
    @InjectView(2131427434)
    EditText et_name;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    @InjectView(2131427481)
    CircleImageView iv_icon;
    private QiniuModel  mQiniuModel;
    private PopupWindow mSelector;
    @InjectView(2131427491)
    RadioGroup rg_isjoin;
    @InjectView(2131427488)
    RadioGroup rg_model;
    @InjectView(2131427483)
    RadioGroup rg_money;

    @OnClick({2131427481, 2131427495})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                showSelector();
                return;
            case R.id.bt_submit:
                postData();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ag);
        ButterKnife.inject((Activity) this);
    }

    private void collectData(JsonParams params) {
        float money = IntFloatWheelView.DEFAULT_VALUE;
        switch (this.rg_money.getCheckedRadioButtonId()) {
            case R.id.rb_20:
                money = 20.0f;
                break;
            case R.id.rb_50:
                money = IntFloatWheelView.DEFAULT_VALUE;
                break;
            case R.id.rb_70:
                money = 70.0f;
                break;
            case R.id.rb_100:
                money = 100.0f;
                break;
        }
        int model = 1;
        switch (this.rg_model.getCheckedRadioButtonId()) {
            case R.id.rb_basic:
                model = 1;
                break;
            case R.id.rb_advanced:
                model = 2;
                break;
        }
        boolean isJoin = true;
        switch (this.rg_isjoin.getCheckedRadioButtonId()) {
            case R.id.rb_join_no:
                isJoin = false;
                break;
            case R.id.rb_join_yes:
                isJoin = true;
                break;
        }
        params.put("entry_fee", money);
        params.put("genre", model);
        params.put("join", isJoin);
    }

    private void postData() {
        if (this.mQiniuModel == null) {
            Snackbar.make(this.iv_icon, (int) R.string.dx, 0).show();
            return;
        }
        JsonParams icon = new JsonParams();
        icon.put("qiniu_key", this.mQiniuModel.key);
        icon.put("qiniu_hash", this.mQiniuModel.hash);
        String name = this.et_name.getText().toString();
        String experience = this.et_experience.getText().toString();
        String contact = this.et_contact.getText().toString();
        if (!TextUtil.isEmpty(name)) {
            if (!TextUtil.isEmpty(experience)) {
                if (!TextUtil.isEmpty(contact)) {
                    JsonParams params = new JsonParams();
                    params.put("name", name);
                    params.put("experience", experience);
                    params.put(Token.WX_TOKEN_PLATFORMID_VALUE, contact);
                    params.put(SocializeProtocolConstants.PROTOCOL_KEY_USER_ICON2, icon);
                    collectData(params);
                    JsonParams jsonParams = new JsonParams();
                    jsonParams.put("team", params);
                    showLoading();
                    StatusApi.becomeTeamLeader(this.activity, jsonParams, new JsonCallback(this
                            .activity) {
                        public void ok(JSONObject object) {
                            BecomeTeamLeaderActivity.this.showSuccessDialog();
                        }

                        public void fail(String message) {
                            Snackbar.make(BecomeTeamLeaderActivity.this.iv_icon, (CharSequence)
                                    message, 0).show();
                        }

                        public void onFinish() {
                            BecomeTeamLeaderActivity.this.dismissLoading();
                        }
                    });
                    return;
                }
            }
        }
        Snackbar.make(this.iv_icon, (int) R.string.dv, 0).show();
    }

    private void showSuccessDialog() {
        LightAlertDialog.create(this.ctx, getString(R.string.dy)).setNegativeButton(getString(R
                .string.y8), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BecomeTeamLeaderActivity.this.ctx, BetMainActivity
                        .class);
                intent.setFlags(67108864);
                BecomeTeamLeaderActivity.this.ctx.startActivity(intent);
            }
        }).show();
    }

    private void showSelector() {
        if (this.mSelector == null) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(this.ctx).inflate(R.layout.o2,
                    null);
            this.mSelector = new PopupWindow(view, -1, -2, true);
            this.mSelector.setOutsideTouchable(true);
            this.mSelector.setAnimationStyle(R.style.f0);
            this.mSelector.setBackgroundDrawable(new BitmapDrawable(getResources()));
            View.OnClickListener click = new View.OnClickListener() {
                public void onClick(View v) {
                    BecomeTeamLeaderActivity.this.dismissSelector();
                    switch (v.getId()) {
                        case R.id.cancel:
                            BecomeTeamLeaderActivity.this.dismissSelector();
                            return;
                        case R.id.select_photos:
                            PhotoPickerHelper.show(BecomeTeamLeaderActivity.this.activity, 2);
                            return;
                        default:
                            return;
                    }
                }
            };
            view.findViewById(R.id.select_photos).setOnClickListener(click);
            view.findViewById(R.id.cancel).setOnClickListener(click);
        }
        this.mSelector.showAtLocation(this.iv_icon, 80, 0, 0);
    }

    private void dismissSelector() {
        if (this.mSelector != null && this.mSelector.isShowing()) {
            this.mSelector.dismiss();
        }
    }

    private void uploadPicture(String path) {
        showLoading();
        QiniuUploader.upload(Prefix.status, new UploadHandler() {
            public void onSuccess(List<QiniuModel> infos) {
                BecomeTeamLeaderActivity.this.mQiniuModel = (QiniuModel) infos.get(0);
                BecomeTeamLeaderActivity.this.imageLoader.displayImage("file://" +
                        BecomeTeamLeaderActivity.this.mQiniuModel.path, BecomeTeamLeaderActivity
                        .this.iv_icon, ImageLoaderOptions.avatar());
            }

            public void onError(String msg) {
                Helper.showToast((CharSequence) msg);
            }

            public void onFinish() {
                BecomeTeamLeaderActivity.this.dismissLoading();
            }
        }, path);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == -1) {
                    List<String> pathList = data.getStringArrayListExtra
                            (MultiImageSelectorActivity.EXTRA_RESULT);
                    if (pathList != null && pathList.size() > 0) {
                        if (!TextUtil.isEmpty((String) pathList.get(0))) {
                            uploadPicture((String) pathList.get(0));
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, BecomeTeamLeaderActivity.class));
        }
    }
}
