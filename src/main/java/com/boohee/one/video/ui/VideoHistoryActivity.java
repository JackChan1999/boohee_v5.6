package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.entity.VideoHistory;
import com.boohee.utils.DateHelper;
import com.boohee.utils.FastJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class VideoHistoryActivity extends GestureActivity {
    private VideoHistoryAdapter adapter;
    int page = 1;
    @InjectView(2131427552)
    PullToRefreshListView refreshLayout;
    private List<VideoHistory> videoHistoryList = new ArrayList();

    protected class VideoHistoryAdapter extends SimpleBaseAdapter<VideoHistory> {
        public VideoHistoryAdapter(Context context, List<VideoHistory> data) {
            super(context, data);
        }

        public int getItemResource() {
            return R.layout.jk;
        }

        public View getItemView(int position, View convertView, ViewHolder holder) {
            final VideoHistory videoHistory = (VideoHistory) this.data.get(position);
            TextView tvName = (TextView) holder.getView(R.id.tv_lesson_name);
            TextView tvDes = (TextView) holder.getView(R.id.tv_des);
            TextView tvTime = (TextView) holder.getView(R.id.tv_time);
            TextView btn = (TextView) holder.getView(R.id.tv_send_status);
            this.imageLoader.displayImage(videoHistory.pic_url, (ImageView) holder.getView(R.id
                    .iv_img));
            tvName.setText(videoHistory.name);
            tvDes.setText(String.format(this.context.getResources().getString(R.string.nx), new
                    Object[]{Integer.valueOf(videoHistory.calorie)}));
            tvTime.setText(DateHelper.timezoneFormat(videoHistory.created_at, "MM-dd HH:mm"));
            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    StatusPostTextActivity.comeWithExtraText(VideoHistoryActivity.this.ctx,
                            String.format(VideoHistoryActivity.this.getString(R.string.a6h), new
                                    Object[]{videoHistory.name, Integer.valueOf(videoHistory
                                    .calorie)}));
                }
            });
            return convertView;
        }
    }

    public static void comeOn(Context context) {
        context.startActivity(new Intent(context, VideoHistoryActivity.class));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.du);
        ButterKnife.inject((Activity) this);
        initView();
        initData();
    }

    private void initView() {
        this.refreshLayout.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                VideoHistoryActivity.this.videoHistoryList.clear();
                VideoHistoryActivity.this.page = 1;
                VideoHistoryActivity.this.initData();
            }
        });
        this.refreshLayout.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                VideoHistoryActivity videoHistoryActivity = VideoHistoryActivity.this;
                videoHistoryActivity.page++;
                VideoHistoryActivity.this.initData();
            }
        });
        this.adapter = new VideoHistoryAdapter(this.ctx, this.videoHistoryList);
        this.refreshLayout.setAdapter(this.adapter);
    }

    private void initData() {
        showLoading();
        SportApi.getVideoHistory(this.ctx, this.page, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                super.ok(object);
                VideoHistoryActivity.this.handleData(object);
            }

            public void onFinish() {
                super.onFinish();
                VideoHistoryActivity.this.dismissLoading();
                VideoHistoryActivity.this.refreshLayout.onRefreshComplete();
            }
        });
    }

    private void handleData(JSONObject object) {
        if (object != null) {
            List<VideoHistory> tmpList = FastJsonUtils.parseList(object.optString
                    ("sport_progresses"), VideoHistory.class);
            if (tmpList != null && tmpList.size() > 0) {
                this.videoHistoryList.addAll(tmpList);
                this.adapter.notifyDataSetChanged();
            }
        }
    }
}
