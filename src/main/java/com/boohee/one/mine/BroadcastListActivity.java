package com.boohee.one.mine;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Broadcast;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.ui.adapter.BroadcastAdapter;
import com.boohee.utility.BuilderIntent;
import com.boohee.utility.Event;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

public class BroadcastListActivity extends GestureActivity implements OnItemClickListener {
    private static final String TAG = BroadcastListActivity.class.getSimpleName();
    private ArrayList<Broadcast> broadcasts;
    private BroadcastAdapter     mAdapter;
    private ListView             mListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.am);
        setTitle(R.string.dr);
        this.mListView = (ListView) this.finder.find(R.id.listView);
        this.mListView.setOnItemClickListener(this);
        getBroadcasts();
    }

    public void onResume() {
        super.onResume();
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void getBroadcasts() {
        StatusApi.getBroadcasts(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                BroadcastListActivity.this.broadcasts = Broadcast.parseBroadacasts(object
                        .optJSONArray("broadcasts"));
                BroadcastListActivity.this.mAdapter = new BroadcastAdapter(BroadcastListActivity
                        .this.activity, BroadcastListActivity.this.broadcasts);
                BroadcastListActivity.this.mListView.setAdapter(BroadcastListActivity.this
                        .mAdapter);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.bk).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        allReaded();
        return true;
    }

    private void allReaded() {
        StatusApi.clearBroadcasts(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (BroadcastListActivity.this.broadcasts != null && BroadcastListActivity.this
                        .broadcasts.size() > 0) {
                    Iterator it = BroadcastListActivity.this.broadcasts.iterator();
                    while (it.hasNext()) {
                        ((Broadcast) it.next()).read = true;
                    }
                    BroadcastListActivity.this.mAdapter.notifyDataSetChanged();
                    Helper.showToast((CharSequence) "置为全部已读成功");
                }
            }
        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MobclickAgent.onEvent(this.ctx, Event.OTHER_VIEWBROADCASTSCONTENT);
        ((Broadcast) this.broadcasts.get(position)).read = true;
        new BuilderIntent(this.activity, BrowserActivity.class).putExtra("url", String.format
                (BooheeClient.build(BooheeClient.ONE).getDefaultURL(StatusApi
                        .URL_BROADCASTS_DETAIL), new Object[]{Integer.valueOf(((Broadcast) this
                        .broadcasts.get(position)).id), UserPreference.getToken(this.activity)}))
                .putExtra("title", "广播详细").startActivity();
    }
}
