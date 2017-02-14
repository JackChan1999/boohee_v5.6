package com.boohee.one.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.ApiUrls;
import com.boohee.api.StatusApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.status.Milestone;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.adapter.MyAdapter;
import com.boohee.utility.BuilderIntent;
import com.boohee.utils.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class MilestoneActivity extends GestureActivity {
    static final String TAG = MilestoneActivity.class.getSimpleName();
    private MilestoneAdapter     mAdapter;
    private ListView             mListView;
    private ArrayList<Milestone> stones;

    private class MilestoneAdapter extends MyAdapter {
        protected MilestoneAdapter(Activity activity, List<? extends Object> items) {
            super(activity, items);
        }

        protected View getItemView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.activity).inflate(R.layout.k4, parent,
                        false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Milestone stone = (Milestone) getItem(position);
            holder.title.setText(stone.title);
            holder.subtitle.setText(stone.key_note);
            holder.lockText.setText(stone.achieved ? "已解锁" : "未解锁");
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView lockText;
        public TextView subtitle;
        public TextView title;

        public ViewHolder(View view) {
            this.title = (TextView) view.findViewById(R.id.title);
            this.subtitle = (TextView) view.findViewById(R.id.subtitle);
            this.lockText = (TextView) view.findViewById(R.id.lockText);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c3);
        setTitle(R.string.je);
        this.mListView = (ListView) findViewById(R.id.listview);
        this.mListView.setEmptyView(findViewById(R.id.tv_empty));
        this.mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (MilestoneActivity.this.stones.get(position) == null || !((Milestone)
                        MilestoneActivity.this.stones.get(position)).achieved) {
                    Helper.showToast(MilestoneActivity.this.activity, (CharSequence) "任务未解锁，请先解锁");
                    return;
                }
                new BuilderIntent(MilestoneActivity.this.activity, BrowserActivity.class)
                        .putExtra("url", String.format(BooheeClient.build("status").getDefaultURL
                                (ApiUrls.MILESTONE_ITEM), new Object[]{Integer.valueOf((
                                (Milestone) MilestoneActivity.this.stones.get(position)).order),
                                UserPreference.getToken(MilestoneActivity.this.activity)}))
                        .putExtra("title", "任务详细").startActivity();
            }
        });
        requestData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "全部重置").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        reset();
        return true;
    }

    private void reset() {
        showLoading();
        StatusApi.deleteMileStonesReset(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                if (object.optBoolean("result") && MilestoneActivity.this.stones != null &&
                        MilestoneActivity.this.stones.size() != 0) {
                    Iterator it = MilestoneActivity.this.stones.iterator();
                    while (it.hasNext()) {
                        ((Milestone) it.next()).achieved = false;
                    }
                    MilestoneActivity.this.mAdapter.notifyDataSetChanged();
                }
            }

            public void onFinish() {
                super.onFinish();
                MilestoneActivity.this.dismissLoading();
            }
        });
    }

    private void requestData() {
        StatusApi.getMileStonesFullMenu(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                MilestoneActivity.this.stones = Milestone.parseLists(object);
                MilestoneActivity.this.mAdapter = new MilestoneAdapter(MilestoneActivity.this,
                        MilestoneActivity.this.stones);
                MilestoneActivity.this.mListView.setAdapter(MilestoneActivity.this.mAdapter);
            }
        });
    }
}
