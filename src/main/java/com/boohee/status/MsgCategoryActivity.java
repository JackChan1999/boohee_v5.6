package com.boohee.status;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.MessengerApi;
import com.boohee.api.StatusApi;
import com.boohee.apn.ApnActivity;
import com.boohee.main.GestureActivity;
import com.boohee.model.MsgCategory;
import com.boohee.model.status.Notification;
import com.boohee.model.status.UnreadCount;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.mine.BroadcastListActivity;
import com.boohee.one.ui.adapter.MsgCategoryAdapter;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.boohee.widgets.CheckAccountPopwindow;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

public class MsgCategoryActivity extends GestureActivity implements OnItemClickListener {
    private static final int[]  ICON_IDS = new int[]{R.drawable.a3r, R.drawable.a3i, R.drawable
            .a3n, R.drawable.a3t, R.drawable.a4_, R.drawable.a3h, R.drawable.a3u};
    static final         String TAG      = MsgCategoryActivity.class.getSimpleName();
    private MsgCategoryAdapter     mAdapter;
    private ArrayList<MsgCategory> mCategories;
    private ListView               mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4);
        MobclickAgent.onEvent(this, Event.STATUS_VIEW_MESSAGE_CATE);
        setTitle(R.string.su);
        findViews();
        initData();
    }

    public void onResume() {
        super.onResume();
        getStatusUnread();
        getApnUnread();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.bk).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                allReaded();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void findViews() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.mListView.setOnItemClickListener(this);
    }

    private void initData() {
        String[] mTitles = getResources().getStringArray(R.array.a);
        this.mCategories = new ArrayList();
        this.mCategories.add(new MsgCategory(ICON_IDS[0], mTitles[0], Notification.MENTION));
        this.mCategories.add(new MsgCategory(ICON_IDS[1], mTitles[1], "comment"));
        this.mCategories.add(new MsgCategory(ICON_IDS[2], mTitles[2], Notification.FEEDBACK));
        this.mCategories.add(new MsgCategory(ICON_IDS[3], mTitles[3], Notification.FRIENDSHIP));
        this.mCategories.add(new MsgCategory(ICON_IDS[4], mTitles[4], Notification.REPOST));
        this.mCategories.add(new MsgCategory(ICON_IDS[5], mTitles[5]));
        this.mCategories.add(new MsgCategory(ICON_IDS[6], mTitles[6]));
        this.mAdapter = new MsgCategoryAdapter(this.activity, this.mCategories);
        this.mListView.setAdapter(this.mAdapter);
    }

    private void getStatusUnread() {
        StatusApi.getUnread(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                UnreadCount unread = UnreadCount.parseSelf(object);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(0)).setCount(unread
                        .unread_mention_notification_count);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(1)).setCount(unread
                        .unread_comment_notification_count);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(2)).setCount(unread
                        .unread_feedback_notification_count);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(3)).setCount(unread
                        .unread_friendship_notification_count);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(4)).setCount(unread
                        .unread_repost_notification_count);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(5)).setCount(unread
                        .unread_broadcast_notification_count);
                MsgCategoryActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void fail(String message) {
            }
        });
    }

    private void getApnUnread() {
        MessengerApi.v2CheckUnread(this, new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                ((MsgCategory) MsgCategoryActivity.this.mCategories.get(6)).setCount(object
                        .optInt("count"));
                MsgCategoryActivity.this.mAdapter.notifyDataSetChanged();
            }

            public void fail(String message) {
            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 5:
                MobclickAgent.onEvent(this.ctx, Event.OTHER_VIEWBROADCASTSLIST);
                new BuilderIntent(this.activity, BroadcastListActivity.class).startActivity();
                return;
            case 6:
                MobclickAgent.onEvent(this.ctx, Event.OTHER_CLICKAPNPAGE);
                ApnActivity.comeOnBaby(this.activity, true);
                return;
            default:
                if (AccountUtils.isVisitorAccount(this)) {
                    CheckAccountPopwindow.showVisitorPopWindow(this);
                    return;
                } else {
                    new BuilderIntent(this.activity, NotificationActivity.class).putExtra("type",
                            ((MsgCategory) this.mCategories.get(position)).code).putExtra
                            (NotificationActivity.EXTRA_TITLE, ((MsgCategory) this.mCategories
                                    .get(position)).title).startActivity();
                    return;
                }
        }
    }

    public void allReaded() {
        StatusApi.clearNotification(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Iterator it = MsgCategoryActivity.this.mCategories.iterator();
                while (it.hasNext()) {
                    ((MsgCategory) it.next()).setCount(0);
                }
                MsgCategoryActivity.this.mAdapter.notifyDataSetChanged();
                Helper.showToast((CharSequence) "置为全部已读成功");
            }
        });
    }
}
