package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.boohee.api.ShopApi;
import com.boohee.model.Order;
import com.boohee.model.UchoiceOrder;
import com.boohee.one.R;
import com.boohee.one.event.OrderEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.pay.PayService;
import com.boohee.one.pay.PayService.OnFinishPayListener;
import com.boohee.one.ui.BaseActivity;
import com.boohee.uchoice.OrderDetailsActivity;
import com.boohee.uchoice.OrderListAdapter;
import com.boohee.uchoice.OrderListAdapter.onPayListener;
import com.boohee.uchoice.PaySuccessActivity;
import com.boohee.utils.ShopUtils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class OrderListFragment extends BaseFragment implements OnClickListener,
        OnItemClickListener, onPayListener, OnFinishPayListener {
    public static String KEY_LAST_PAGE  = "key_last_page";
    public static String KEY_PAGE       = "key_page";
    public static String KEY_STATE_TYPE = "key_state_type";
    public static int    REQ_DEL        = 1;
    private OrderListAdapter mAdapter;
    private int mLastPage = this.mPage;
    private ListView mListView;
    private List<Order> mOrders = new ArrayList();
    private int                   mPage;
    private PayService            mPayService;
    private PullToRefreshListView mRefreshView;
    private String                mStateType;
    private String                mSuccessUrl;
    private RelativeLayout        rl_cart_hint;

    public enum StateType {
        initial,
        payed,
        sent,
        finished
    }

    public static OrderListFragment newInstance(StateType stateType) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle params = new Bundle();
        params.putString(KEY_STATE_TYPE, stateType.name());
        fragment.setArguments(params);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mStateType = getArguments().getString(KEY_STATE_TYPE);
        EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.g8, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mRefreshView = (PullToRefreshListView) view.findViewById(R.id.lv_orders);
        this.mListView = (ListView) this.mRefreshView.getRefreshableView();
        this.rl_cart_hint = (RelativeLayout) view.findViewById(R.id.rl_cart_hint);
        view.findViewById(R.id.btn_cart_go).setOnClickListener(this);
        this.mListView.setOnItemClickListener(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            this.mStateType = savedInstanceState.getString(KEY_STATE_TYPE);
            this.mPage = savedInstanceState.getInt(KEY_PAGE);
            this.mLastPage = savedInstanceState.getInt(KEY_LAST_PAGE);
        }
        init();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putString(KEY_STATE_TYPE, this.mStateType);
        outState.putInt(KEY_PAGE, this.mPage);
        outState.putInt(KEY_LAST_PAGE, this.mLastPage);
        super.onSaveInstanceState(outState);
    }

    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        int index = position - 1;
        if (!((Order) this.mOrders.get(index)).state.equals("canceled") && !((Order) this.mOrders
                .get(index)).state.equals(UchoiceOrder.EXPIRED) && ((Order) this.mOrders.get
                (index)).type.equals("GoodsOrder")) {
            Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
            intent.putExtra("order_id", ((Order) this.mOrders.get(index)).id);
            intent.putExtra("index", index);
            startActivityForResult(intent, REQ_DEL);
        }
    }

    public void onClick(View v) {
        if (!isDetached() && !WheelUtils.isFastDoubleClick()) {
            Activity activity = getActivity();
            if (activity != null) {
                ShopUtils.scanAnyWhere(activity);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == REQ_DEL) {
                int index = data.getIntExtra("index", -1);
                if (this.mAdapter != null && index >= 0) {
                    this.mOrders.remove(index);
                    this.mAdapter.notifyDataSetChanged();
                    if (this.mOrders.size() == 0 && this.mRefreshView != null && this
                            .rl_cart_hint != null) {
                        this.rl_cart_hint.setVisibility(0);
                        this.mRefreshView.setVisibility(8);
                    }
                }
            } else if (requestCode == 168 && this.mPayService != null) {
                this.mPayService.onPaymentResult(data);
            }
        }
    }

    public void payListener(int order_id, String successUrl) {
        this.mPayService = new PayService(getActivity());
        this.mPayService.setOnFinishPayLinstener(this);
        this.mPayService.startPayWithDialog(order_id);
        this.mSuccessUrl = successUrl;
    }

    public void onPaySuccess() {
        PaySuccessActivity.comeOnBaby(getActivity(), this.mSuccessUrl);
    }

    public void onPayFinished() {
    }

    private void init() {
        this.mOrders.clear();
        this.mOrders.addAll(this.mOrders);
        this.mAdapter = new OrderListAdapter(getActivity(), this.mOrders, (BaseActivity)
                getActivity(), this.mListView);
        this.mAdapter.setOnPayListener(this);
        this.mListView.setAdapter(this.mAdapter);
        initRefreshView();
    }

    private void initRefreshView() {
        this.mRefreshView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                OrderListFragment.this.pullLoadData();
            }
        });
        this.mRefreshView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                OrderListFragment.this.loadMoreData();
            }
        });
    }

    public void initLoadData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (OrderListFragment.this.mRefreshView != null) {
                    OrderListFragment.this.mRefreshView.setRefreshing();
                }
            }
        }, 500);
    }

    private void pullLoadData() {
        this.mPage = 1;
        this.mLastPage = this.mPage;
        loadData();
    }

    private void loadMoreData() {
        this.mLastPage = this.mPage;
        this.mPage++;
        loadData();
    }

    private void loadData() {
        if (getActivity() != null && !isDetached()) {
            ShopApi.getOrders(this.mStateType, this.mPage, getActivity(), new JsonCallback
                    (getActivity()) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    OrderListFragment.this.handleOrders(object);
                }

                public void fail(String message) {
                    super.fail(message);
                    OrderListFragment.this.handleError();
                }

                public void onFinish() {
                    super.onFinish();
                    OrderListFragment.this.mRefreshView.onRefreshComplete();
                }
            });
        }
    }

    private boolean isLoadMore() {
        return this.mPage > this.mLastPage;
    }

    private void handleOrders(JSONObject response) {
        List<Order> orders = Order.parseOrders(response);
        if (isLoadMore()) {
            if (orders != null && orders.size() > 0) {
                this.mOrders.addAll(orders);
                this.mAdapter.notifyDataSetChanged();
            }
        } else if (orders == null || orders.size() == 0) {
            this.mOrders.clear();
            this.mAdapter.notifyDataSetChanged();
            this.mRefreshView.setVisibility(8);
            this.rl_cart_hint.setVisibility(0);
        } else {
            this.mOrders.clear();
            this.mOrders.addAll(orders);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void handleError() {
        if (isLoadMore()) {
            this.mPage = this.mLastPage;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(OrderEvent orderEvent) {
        if (orderEvent != null) {
            pullLoadData();
        }
    }
}
