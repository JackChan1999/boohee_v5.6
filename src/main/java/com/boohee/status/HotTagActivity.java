package com.boohee.status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.HotTag;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.sport.DownloadService;

import java.util.ArrayList;

import org.json.JSONObject;

public class HotTagActivity extends GestureActivity {
    static final String TAG = HotTagActivity.class.getSimpleName();
    private HotTag            hotTag;
    private ArrayList<String> hotTags;
    private ListView          lv_hot_tag;
    private ListView          lv_recent_tag;
    private ArrayList<String> recentTags;
    private TextView          txt_none_recent;

    private class TagAdapter extends BaseAdapter {
        private Context           mContext;
        private ArrayList<String> tags;

        public TagAdapter(Context ctx, ArrayList<String> list) {
            this.mContext = ctx;
            this.tags = list;
        }

        public int getCount() {
            return this.tags.size();
        }

        public Object getItem(int position) {
            return this.tags.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.my, null);
                holder.txt_tag_name = (TextView) convertView.findViewById(R.id.txt_tag_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_tag_name.setText("#" + ((String) this.tags.get(position)) + "#");
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView txt_tag_name;

        ViewHolder() {
        }
    }

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.a41);
        setContentView(R.layout.hb);
        findView();
        addListener();
        init();
    }

    void findView() {
        this.lv_recent_tag = (ListView) findViewById(R.id.lv_recent_tag);
        this.lv_hot_tag = (ListView) findViewById(R.id.lv_hot_tag);
        this.txt_none_recent = (TextView) findViewById(R.id.txt_none_recent);
    }

    void addListener() {
        this.lv_recent_tag.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Intent intent = new Intent();
                intent.putExtra(DownloadService.EXTRA_TAG, "#" + ((String) HotTagActivity.this
                        .recentTags.get(arg2)) + "#");
                HotTagActivity.this.setResult(-1, intent);
                HotTagActivity.this.finish();
            }
        });
        this.lv_hot_tag.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                Intent intent = new Intent();
                intent.putExtra(DownloadService.EXTRA_TAG, "#" + ((String) HotTagActivity.this
                        .hotTags.get(arg2)) + "#");
                HotTagActivity.this.setResult(-1, intent);
                HotTagActivity.this.finish();
            }
        });
    }

    private void init() {
        StatusApi.getTopicsRecent(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                JSONObject jsonObject = object.optJSONObject("topic");
                if (jsonObject != null) {
                    HotTagActivity.this.hotTag = HotTag.parseHotTagFromJson(jsonObject);
                    if (HotTagActivity.this.hotTag != null) {
                        HotTagActivity.this.hotTags = HotTagActivity.this.hotTag.getHot();
                        if (HotTagActivity.this.hotTags != null && HotTagActivity.this.hotTags
                                .size() > 0) {
                            HotTagActivity.this.lv_hot_tag.setAdapter(new TagAdapter
                                    (HotTagActivity.this.ctx, HotTagActivity.this.hotTags));
                        }
                        HotTagActivity.this.recentTags = HotTagActivity.this.hotTag.getRecent();
                        if (HotTagActivity.this.recentTags == null || HotTagActivity.this
                                .recentTags.size() <= 0) {
                            HotTagActivity.this.txt_none_recent.setVisibility(0);
                            return;
                        }
                        HotTagActivity.this.lv_recent_tag.setAdapter(new TagAdapter
                                (HotTagActivity.this.ctx, HotTagActivity.this.recentTags));
                        HotTagActivity.this.txt_none_recent.setVisibility(8);
                    }
                }
            }
        });
    }
}
