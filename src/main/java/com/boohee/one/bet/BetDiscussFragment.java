package com.boohee.one.bet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.ui.fragment.GoodsPostsFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.Utils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BetDiscussFragment extends GoodsPostsFragment {
    protected static final String  ARG_PARAM_BET = "arg_parm_bt";
    private final          String  MSG_URL       = "/api/v1/bet_order_nats/state?bet_id=%d";
    private                int     betId         = -1;
    private                boolean isShowMsg     = false;
    @InjectView(2131428203)
    LinearLayout llMsg;
    private String msgLink;
    private String msgStr;
    @InjectView(2131428204)
    TextView tvMsg;

    public static GoodsPostsFragment newInstance(int betId) {
        GoodsPostsFragment fragment = new BetDiscussFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_BET, betId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.betId = getArguments().getInt(ARG_PARAM_BET, -1);
        this.mSlug = String.format("bets_%d", new Object[]{Integer.valueOf(this.betId)});
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fi, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    protected void addListener() {
        super.addListener();
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                BetDiscussFragment.this.getCurrentTopic();
                BetDiscussFragment.this.getMsgData();
            }
        });
    }

    public void onResume() {
        super.onResume();
        getMsgData();
    }

    private void getMsgData() {
        BooheeClient.build("status").get(String.format("/api/v1/bet_order_nats/state?bet_id=%d",
                new Object[]{Integer.valueOf(this.betId)}), new JsonCallback(getActivity()) {
            public void onFinish() {
                super.onFinish();
                BetDiscussFragment.this.refreshMsg();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    BetDiscussFragment.this.isShowMsg = object.getBoolean("show");
                    if (BetDiscussFragment.this.isShowMsg) {
                        BetDiscussFragment.this.msgStr = object.getJSONObject(Utils
                                .RESPONSE_CONTENT).getString("title");
                        BetDiscussFragment.this.msgLink = object.getJSONObject(Utils
                                .RESPONSE_CONTENT).getString("link");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, getActivity());
    }

    private void refreshMsg() {
        if (this.isShowMsg) {
            this.llMsg.setVisibility(0);
            this.tvMsg.setText(this.msgStr);
            return;
        }
        this.llMsg.setVisibility(8);
    }

    @OnClick({2131428203, 2131427531})
    public void onClick(View view) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.fab_button:
                    if (this.betId >= 0) {
                        StatusPostTextActivity.comeWithExtraText(getActivity(), "#我赌我会瘦#");
                        return;
                    }
                    return;
                case R.id.ll_msg:
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
