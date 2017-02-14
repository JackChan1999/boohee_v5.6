package com.boohee.one.bet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.model.UploadFood;
import com.boohee.one.R;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.WheelUtils;

import org.json.JSONObject;

public class BetMineFragment extends BetListFragment {
    @InjectView(2131428206)
    TextView btnMsg;
    private String msg = "我的组队申请：%s";
    private String msgLink;
    private String msgState;
    private String msgTime;
    @InjectView(2131428205)
    RelativeLayout msgView;
    @InjectView(2131428204)
    TextView       tvMsg;
    @InjectView(2131427736)
    TextView       tvTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.url = "/api/v1/bet_order_nats/history?page=%d";
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fj, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    protected void handleData(JSONObject object) {
        super.handleData(object);
        try {
            JSONObject applies = object.optJSONObject("applies");
            this.msgState = applies.optString(UploadFood.STATE);
            this.msgTime = applies.optString("created_at");
            this.msgLink = applies.optString("def_link");
            if (TextUtils.isEmpty(this.msgState)) {
                this.msgView.setVisibility(8);
                return;
            }
            this.msgView.setVisibility(0);
            if (TextUtils.equals(this.msgState, "reviewing")) {
                this.tvMsg.setText(String.format(this.msg, new Object[]{"审核中..."}));
                this.tvTime.setText(String.format("提交时间：%s", new Object[]{this.msgTime}));
                this.tvTime.setVisibility(0);
                this.btnMsg.setVisibility(8);
            } else if (TextUtils.equals(this.msgState, "succeeded")) {
                this.tvMsg.setText(String.format(this.msg, new Object[]{"已通过"}));
                this.tvTime.setVisibility(8);
                this.btnMsg.setVisibility(0);
            } else if (TextUtils.equals(this.msgState, "failed")) {
                this.tvMsg.setText(String.format(this.msg, new Object[]{"审核失败"}));
                this.tvTime.setText(this.msgTime);
                this.tvTime.setVisibility(0);
                this.btnMsg.setVisibility(8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({2131428206})
    public void onClick(View view) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.btn_msg:
                    BooheeScheme.handleUrl(getActivity(), this.msgLink);
                    return;
                default:
                    return;
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
