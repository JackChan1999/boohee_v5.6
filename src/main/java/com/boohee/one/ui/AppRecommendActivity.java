package com.boohee.one.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.boohee.api.OneApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.AppRecommend;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppRecommendActivity extends GestureActivity {
    static final String TAG = AppRecommendActivity.class.getSimpleName();
    ArrayList<AppRecommend> apps;

    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.e5);
        setTitle(R.string.c4);
        getAppRecommends();
    }

    private void getAppRecommends() {
        showLoading();
        OneApi.getAndroidRecommends(this.activity, new JsonCallback(this.activity) {
            public void ok(String response) {
                super.ok(response);
                try {
                    JSONArray recommends = new JSONObject(response).getJSONArray
                            ("android_recommends");
                    AppRecommendActivity.this.apps = AppRecommend.parseAppRecommends(recommends
                            .toString());
                    AppRecommendActivity.this.initAppRecommendView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                AppRecommendActivity.this.dismissLoading();
            }
        });
    }

    private void initAppRecommendView() {
        if (this.apps != null) {
            final AppRecommendListAdapter adapter = new AppRecommendListAdapter(this.ctx, this
                    .apps);
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long
                        id) {
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(adapter.getItem(position).url));
                    AppRecommendActivity.this.startActivity(intent);
                }
            });
        }
    }
}
