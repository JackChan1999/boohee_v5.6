package com.boohee.nice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.NiceApi;
import com.boohee.main.GestureActivity;
import com.boohee.modeldao.UserDao;
import com.boohee.nice.adapter.AdviserAdapter;
import com.boohee.nice.model.NiceMessage;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class NiceAdviserActivity extends GestureActivity {
    public static final String KEY_CAN_SEND = "KEY_CAN_SEND";
    @InjectView(2131427619)
    CircleImageView avatar;
    private boolean canSend;
    @InjectView(2131427817)
    EditText etInput;
    private AdviserAdapter mAdapter;
    private String         mAdviserAvatar;
    private String         mAdviserName;
    private String         mAdviserWechat;
    private final List<NiceMessage> mDataList = new ArrayList();
    private String mMessageId;
    private String mUserAvatar;
    private String mUserName;
    @InjectView(2131427813)
    TextView              tvAdviser;
    @InjectView(2131427814)
    TextView              tvWechat;
    @InjectView(2131427816)
    LinearLayout          viewBottom;
    @InjectView(2131427815)
    PullToRefreshListView viewRefresh;

    @OnClick({2131427818})
    public void onClick() {
        sendMessage();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cb);
        ButterKnife.inject((Activity) this);
        this.canSend = getIntent().getBooleanExtra(KEY_CAN_SEND, true);
        initView();
        init();
    }

    private void initView() {
        if (!this.canSend) {
            this.viewBottom.setVisibility(8);
        }
    }

    private void init() {
        this.mAdapter = new AdviserAdapter(this, this.mDataList);
        this.viewRefresh.setAdapter(this.mAdapter);
        this.viewRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                NiceAdviserActivity.this.loadData(true);
            }
        });
        loadData(false);
    }

    private void loadData(final boolean isOld) {
        if (!isOld) {
            this.mMessageId = "";
        }
        NiceApi.getNiceMessage(this.mMessageId, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                JSONObject info = object.optJSONObject("info");
                if (info != null) {
                    NiceAdviserActivity.this.mUserName = info.optString(UserDao.USER_NAME);
                    NiceAdviserActivity.this.mUserAvatar = info.optString("user_avatar");
                    NiceAdviserActivity.this.mAdapter.setUserInfo(NiceAdviserActivity.this
                            .mUserName, NiceAdviserActivity.this.mUserAvatar);
                    NiceAdviserActivity.this.mAdviserName = info.optString("advisor_name");
                    NiceAdviserActivity.this.mAdviserAvatar = info.optString("advisor_avatar");
                    NiceAdviserActivity.this.mAdviserWechat = info.optString("advisor_wechat");
                    NiceAdviserActivity.this.mAdapter.setAdviserInfo(NiceAdviserActivity.this
                            .mAdviserName, NiceAdviserActivity.this.mAdviserAvatar);
                    NiceAdviserActivity.this.tvAdviser.setText("顾问： " + NiceAdviserActivity.this
                            .mAdviserName);
                    NiceAdviserActivity.this.tvWechat.setText("微信：" + NiceAdviserActivity.this
                            .mAdviserWechat);
                    NiceAdviserActivity.this.imageLoader.displayImage(NiceAdviserActivity.this
                            .mAdviserAvatar, NiceAdviserActivity.this.avatar, ImageLoaderOptions
                            .avatar());
                }
                String item = object.optJSONArray("items").toString();
                if (!TextUtils.isEmpty(item)) {
                    final List<NiceMessage> dataList = FastJsonUtils.parseList(item, NiceMessage
                            .class);
                    if (dataList != null && dataList.size() > 0) {
                        Collections.reverse(dataList);
                        if (!isOld) {
                            NiceAdviserActivity.this.mDataList.clear();
                        }
                        NiceAdviserActivity.this.mDataList.addAll(0, dataList);
                        NiceAdviserActivity.this.mAdapter.notifyDataSetChanged();
                        NiceAdviserActivity.this.mMessageId = String.valueOf(((NiceMessage)
                                NiceAdviserActivity.this.mDataList.get(0)).id);
                        new Handler().post(new Runnable() {
                            public void run() {
                                ((ListView) NiceAdviserActivity.this.viewRefresh
                                        .getRefreshableView()).setSelection(dataList.size());
                            }
                        });
                    }
                }
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                NiceAdviserActivity.this.viewRefresh.onRefreshComplete();
            }
        });
    }

    private void sendMessage() {
        String msg = this.etInput.getEditableText().toString();
        if (TextUtils.isEmpty(msg)) {
            Helper.showToast((CharSequence) "输入不能为空哦~");
            return;
        }
        showLoading();
        NiceApi.sendNiceMessage(msg, this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                Helper.showToast((CharSequence) "发送成功");
                NiceAdviserActivity.this.etInput.setText("");
                NiceAdviserActivity.this.loadData(false);
            }

            public void fail(String message) {
                Helper.showToast((CharSequence) message);
            }

            public void onFinish() {
                NiceAdviserActivity.this.dismissLoading();
            }
        });
    }

    public static void startActivity(Context context, boolean canSend) {
        Intent i = new Intent(context, NiceAdviserActivity.class);
        i.putExtra(KEY_CAN_SEND, canSend);
        context.startActivity(i);
    }
}
