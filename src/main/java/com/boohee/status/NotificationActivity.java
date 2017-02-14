package com.boohee.status;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Notification;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.JsonParams;
import com.boohee.one.ui.PullToRefreshHelper;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import lecho.lib.hellocharts.BuildConfig;

import org.json.JSONObject;

public class NotificationActivity extends GestureActivity {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    static final        String TAG         = NotificationActivity.class.getSimpleName();
    boolean isRead = true;
    private NotificationAdapter     mAdapter;
    private TextView                mEmptyView;
    private ListView                mListView;
    private ArrayList<Notification> mNotifications;
    private PullToRefreshListView   mPullRefreshListView;
    private String                  mType;
    int mUserId;
    int page = 1;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.kj);
        MobclickAgent.onEvent(this, Event.STATUS_VIEW_MESSAGE_LST);
        setTitle(getStringExtra(EXTRA_TITLE));
        this.mUserId = getIntent().getIntExtra("user_id", 0);
        this.isRead = getIntent().getBooleanExtra("isRead", true);
        this.mType = getStringExtra("type");
        init();
        PullToRefreshHelper.loadFirst(this);
    }

    private void init() {
        this.mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        this.mListView = (ListView) this.mPullRefreshListView.getRefreshableView();
        this.mEmptyView = (TextView) findViewById(R.id.empty_view);
        this.mListView.setEmptyView(this.mEmptyView);
        if (this.mType.equals(Notification.FRIENDSHIP)) {
            Drawable topDrawable = getResources().getDrawable(R.drawable.a5o);
            topDrawable.setBounds(0, 0, ViewUtils.dip2px(this.activity, 120.0f), ViewUtils.dip2px
                    (this.activity, 120.0f));
            this.mEmptyView.setCompoundDrawables(null, topDrawable, null, null);
        }
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                NotificationActivity.this.getNotifications();
            }
        });
        this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                NotificationActivity.this.getPreviousNotifications();
            }
        });
    }

    private JsonParams getParams(boolean isNext) {
        JsonParams params = new JsonParams();
        params.put("status_api_version", BuildConfig.VERSION_NAME);
        if (!TextUtils.isEmpty(this.mType)) {
            params.put("type", this.mType);
        }
        if (!this.isRead) {
            params.put("read", "false");
        }
        if (isNext) {
            params.put("page", this.page + "");
        }
        return params;
    }

    private void getNotifications() {
        this.page = 1;
        StatusApi.getNotifications(this.activity, getParams(false), new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                NotificationActivity.this.mNotifications = Notification.parseNotifications(object);
                Helper.showLog(NotificationActivity.TAG, NotificationActivity.this.mNotifications
                        .size() + "");
                NotificationActivity.this.mAdapter = new NotificationAdapter(NotificationActivity
                        .this.ctx, NotificationActivity.this.mNotifications, NotificationActivity
                        .this.mUserId);
                NotificationActivity.this.mListView.setAdapter(NotificationActivity.this.mAdapter);
                NotificationActivity notificationActivity = NotificationActivity.this;
                notificationActivity.page++;
            }

            public void onFinish() {
                super.onFinish();
                NotificationActivity.this.mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    private void getPreviousNotifications() {
        StatusApi.getNotificationsPrevious(this.activity, getParams(true), new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                NotificationActivity.this.mNotifications.addAll(Notification.parseNotifications
                        (object));
                NotificationActivity.this.mAdapter.notifyDataSetChanged();
                NotificationActivity notificationActivity = NotificationActivity.this;
                notificationActivity.page++;
            }
        });
    }
}
