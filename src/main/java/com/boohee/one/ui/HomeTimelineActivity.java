package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.api.MessengerApi;
import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.myview.BadgeView;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.HomeTimelineFragment;
import com.boohee.status.MsgCategoryActivity;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Event;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class HomeTimelineActivity extends GestureActivity {
    private FloatingActionButton mFabButton;
    private BadgeView            mMessageBadge;
    private int                  messageCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bn);
        setTitle(R.string.d9);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, new
                HomeTimelineFragment()).commitAllowingStateLoss();
        this.mFabButton = (FloatingActionButton) findViewById(R.id.fab_button);
        this.mFabButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new BuilderIntent(HomeTimelineActivity.this, StatusPostTextActivity.class)
                        .startActivity();
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.messageCount = 0;
        refreshUnreadMsg();
        getApnUnread();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.n, menu);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HomeTimelineActivity.this.mMessageBadge = new BadgeView(HomeTimelineActivity
                        .this, HomeTimelineActivity.this.findViewById(R.id.action_message));
                HomeTimelineActivity.this.mMessageBadge.setBadgeBackgroundColor
                        (HomeTimelineActivity.this.getResources().getColor(R.color.ju));
                HomeTimelineActivity.this.mMessageBadge.setTextColor(HomeTimelineActivity.this
                        .getResources().getColor(R.color.he));
                HomeTimelineActivity.this.messageCount = 0;
                HomeTimelineActivity.this.refreshUnreadMsg();
                HomeTimelineActivity.this.getApnUnread();
            }
        }, 100);
        return true;
    }

    private void refreshUnreadMsg() {
        if (this.mMessageBadge != null) {
            StatusApi.getUnread(this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    HomeTimelineActivity.this.messageCount = HomeTimelineActivity.this
                            .messageCount + object.optInt("count");
                    HomeTimelineActivity.this.updateMessageBager();
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void getApnUnread() {
        if (this.mMessageBadge != null) {
            MessengerApi.v2CheckUnread(this, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    HomeTimelineActivity.this.messageCount = HomeTimelineActivity.this
                            .messageCount + object.optInt("count");
                    HomeTimelineActivity.this.updateMessageBager();
                }

                public void fail(String message) {
                }
            });
        }
    }

    private void updateMessageBager() {
        if (this.mMessageBadge != null) {
            if (this.messageCount > 0) {
                this.mMessageBadge.setText(this.messageCount > 99 ? "99+" : this.messageCount + "");
                this.mMessageBadge.show();
                return;
            }
            this.mMessageBadge.hide();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_message:
                MobclickAgent.onEvent(this.ctx, Event.OTHER_CLICKMSGPAGE);
                new BuilderIntent(this.activity, MsgCategoryActivity.class).startActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, HomeTimelineActivity.class));
        }
    }
}
