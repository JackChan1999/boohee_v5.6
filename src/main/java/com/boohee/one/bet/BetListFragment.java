package com.boohee.one.bet;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.one.bet.adapter.BetAdapter;
import com.boohee.one.bet.model.Bet;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.fragment.BaseFragment;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BetListFragment extends BaseFragment {
    private static final String ARG_TYPE     = "param1";
    private static final String BET_ALL_TYPE = "bet_all_type";
    public static final  int    TYPE_ALL     = 1;
    public static final  int    TYPE_HISTORY = 2;
    private BetAdapter adapter;
    private String     betAllType;
    private List<Bet> bets = new ArrayList();
    @InjectView(2131427552)
    PullToRefreshListView listview;
    private int page = 1;
    private   int    type;
    protected String url;

    public static BetListFragment newInstance(int type) {
        BetListFragment fragment = new BetListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static BetListFragment newInstance(int type, String betAllType) {
        BetListFragment fragment = new BetListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        args.putString(BET_ALL_TYPE, betAllType);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.type = getArguments().getInt(ARG_TYPE);
            if (this.type == 1) {
                this.betAllType = getArguments().getString(BET_ALL_TYPE);
                this.url = "/api/v1/bet_nats/list?type=" + this.betAllType + "&page=%d";
            } else if (this.type == 2) {
                this.url = "/api/v1/bet_order_nats/history?page=%d";
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fk, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        showLoading();
        loadData();
    }

    private void initView() {
        this.listview.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                BetListFragment.this.page = 1;
                BetListFragment.this.loadData();
            }
        });
        this.listview.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                if (BetListFragment.this.type == 1) {
                    BetListFragment.this.page = BetListFragment.this.page + 1;
                    BetListFragment.this.loadData();
                }
            }
        });
        this.adapter = new BetAdapter(getActivity(), this.bets);
        this.listview.setAdapter(this.adapter);
    }

    private void loadData() {
        BooheeClient.build("status").get(String.format(this.url, new Object[]{Integer.valueOf
                (this.page)}), new JsonCallback(getActivity()) {
            public void ok(JSONObject object) {
                super.ok(object);
                BetListFragment.this.handleData(object);
            }

            public void onFinish() {
                super.onFinish();
                BetListFragment.this.listview.onRefreshComplete();
                BetListFragment.this.dismissLoading();
            }
        }, getActivity());
    }

    protected void handleData(JSONObject object) {
        if (object != null) {
            try {
                if (this.page == 1) {
                    this.bets.clear();
                }
                this.bets.addAll(FastJsonUtils.parseList(object.optString(BooheeScheme.BETS), Bet
                        .class));
                this.adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadFirst() {
        super.loadFirst();
        if (this.listview != null) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BetListFragment.this.listview.setRefreshing();
                }
            }, 500);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
