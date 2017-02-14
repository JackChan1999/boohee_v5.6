package com.boohee.more;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.account.UserProfileActivity;
import com.boohee.api.ApiUrls;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.MsgCategory;
import com.boohee.one.R;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.adapter.MsgCategoryAdapter;
import com.boohee.status.FriendShipActivity;
import com.boohee.status.MyTimelineActivity;
import com.boohee.uchoice.OrderListActivity;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.widgets.CheckAccountPopwindow;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class MyBooheeActivity extends GestureActivity implements OnItemClickListener {
    private static final int[]  ICON_IDS = new int[]{R.drawable.p5, R.drawable.oq, R.drawable.qk,
            R.drawable.qj};
    static final         String TAG      = MyBooheeActivity.class.getSimpleName();
    private MsgCategoryAdapter     mAdapter;
    private ArrayList<MsgCategory> mCategories;
    private ListView               mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c5);
        setTitle(R.string.wg);
        findViews();
        initData();
    }

    private void findViews() {
        this.mListView = (ListView) findViewById(R.id.listView);
        this.mListView.setOnItemClickListener(this);
    }

    private void initData() {
        String[] mTitles = getResources().getStringArray(R.array.b);
        this.mCategories = new ArrayList();
        this.mCategories.add(new MsgCategory(ICON_IDS[0], mTitles[0]));
        this.mCategories.add(new MsgCategory(ICON_IDS[1], mTitles[1]));
        this.mCategories.add(new MsgCategory(ICON_IDS[2], mTitles[2]));
        this.mCategories.add(new MsgCategory(ICON_IDS[3], mTitles[3]));
        this.mAdapter = new MsgCategoryAdapter(this.activity, this.mCategories);
        this.mListView.setAdapter(this.mAdapter);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                new BuilderIntent(this.activity, UserProfileActivity.class).startActivity();
                return;
            case 1:
                MobclickAgent.onEvent(this, Event.MINE_CLICKTHINPLAN);
                if (AccountUtils.isVisitorAccount(this)) {
                    CheckAccountPopwindow.showVisitorPopWindow(this);
                    return;
                }
                new BuilderIntent(this, BrowserActivity.class).putExtra("url", BooheeClient.build
                        ("status").getDefaultURL(String.format(ApiUrls.FREE_GATE, new
                        Object[]{UserPreference.getToken(this)}))).putExtra("title", getString(R
                        .string.m4)).startActivity();
                return;
            case 2:
                MobclickAgent.onEvent(this, Event.MINE_CLICKMYTIMELINE);
                new BuilderIntent(this.activity, MyTimelineActivity.class).startActivity();
                return;
            case 3:
                MobclickAgent.onEvent(this, Event.MINE_CLICKFRIENDPAGE);
                new BuilderIntent(this.activity, FriendShipActivity.class).putExtra
                        (FriendShipActivity.FRIENDSHIP_POSITION, 0).startActivity();
                return;
            case 4:
                MobclickAgent.onEvent(this, Event.MINE_CLICKORDERPAGE);
                new BuilderIntent(this.activity, OrderListActivity.class).startActivity();
                return;
            default:
                return;
        }
    }
}
