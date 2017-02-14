package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.ApiUrls;
import com.boohee.main.GestureActivity;
import com.boohee.model.SuccessStory;
import com.boohee.model.SuccessStory.ItemsEntity;
import com.boohee.model.SuccessStory.SlidersEntity;
import com.boohee.one.BuildConfig;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.utility.BooheeScheme;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.utils.WheelUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SuccessStoryActivity extends GestureActivity {
    public static final String TAGS = "tags";
    private             String API  = "/api/v1/stories?page=%d";
    private SuccessStoryAdapter adapter;
    ImageView ivHeader;
    private int page = 1;
    @InjectView(2131427552)
    PullToRefreshListView pullToRefreshListView;
    private List<ItemsEntity> storyList = new ArrayList();
    private SuccessStory successStory;
    private String       tag;

    public class SuccessStoryAdapter extends SimpleBaseAdapter<ItemsEntity> {
        public SuccessStoryAdapter(Context context, List<ItemsEntity> data) {
            super(context, data);
        }

        public int getItemResource() {
            return R.layout.jc;
        }

        public View getItemView(int position, View convertView, ViewHolder holder) {
            ItemsEntity itemsEntity = (ItemsEntity) getItem(position);
            TextView tvTitle = (TextView) holder.getView(R.id.tv_title);
            TextView tvTags = (TextView) holder.getView(R.id.tv_tags);
            this.imageLoader.displayImage(itemsEntity.pic, (ImageView) holder.getView(R.id.iv));
            tvTitle.setText(itemsEntity.title);
            if (itemsEntity != null && itemsEntity.tags.size() > 0) {
                StringBuilder tagString = new StringBuilder();
                for (String tag : itemsEntity.tags) {
                    tagString.append(tag + " ");
                }
                tvTags.setText(tagString.toString());
            }
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.di);
        ButterKnife.inject((Activity) this);
        this.tag = getIntent().getStringExtra(TAGS);
        if (!TextUtils.isEmpty(this.tag)) {
            if (BuildConfig.PLATFORM.equals(this.tag)) {
                setTitle("成功故事");
            } else {
                setTitle("成功故事-" + this.tag);
            }
        }
        initView();
        requestData();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.tag = intent.getStringExtra(TAGS);
        if (!TextUtils.isEmpty(this.tag)) {
            if (BuildConfig.PLATFORM.equals(this.tag)) {
                setTitle("成功故事");
            } else {
                setTitle("成功故事-" + this.tag);
            }
        }
        this.page = 1;
        requestData();
    }

    @OnClick({2131427954})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom:
                BrowserActivity.comeOnBaby(this.ctx, null, BooheeClient.build(BooheeClient.ONE)
                        .getDefaultURL(ApiUrls.UPLOAD_STORY_URL));
                return;
            default:
                return;
        }
    }

    private void initView() {
        View headerView = LayoutInflater.from(this.ctx).inflate(R.layout.h6, null);
        this.ivHeader = (ImageView) headerView.findViewById(R.id.iv_header);
        ViewUtils.setViewScaleHeight(this.ctx, this.ivHeader, 2, 1);
        ((ListView) this.pullToRefreshListView.getRefreshableView()).addHeaderView(headerView);
        this.pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                SuccessStoryActivity.this.page = 1;
                SuccessStoryActivity.this.requestData();
            }
        });
        this.pullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            public void onLastItemVisible() {
                SuccessStoryActivity.this.page = SuccessStoryActivity.this.page + 1;
                SuccessStoryActivity.this.requestData();
            }
        });
        this.pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemsEntity itemsEntity = (ItemsEntity) adapterView.getAdapter().getItem(i);
                if (itemsEntity != null) {
                    BooheeScheme.handleUrl(SuccessStoryActivity.this.ctx, itemsEntity.url);
                }
            }
        });
    }

    private void requestData() {
        showLoading();
        String url = String.format(this.API, new Object[]{Integer.valueOf(this.page)});
        if (!(TextUtils.isEmpty(this.tag) || BuildConfig.PLATFORM.equals(this.tag))) {
            url = url + "&tag=" + this.tag;
        }
        BooheeClient.build("status").get(url, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                super.ok(object);
                SuccessStoryActivity.this.handleData(object);
            }

            public void onFinish() {
                super.onFinish();
                SuccessStoryActivity.this.pullToRefreshListView.onRefreshComplete();
                SuccessStoryActivity.this.dismissLoading();
            }
        }, this.ctx);
    }

    private void handleData(JSONObject object) {
        if (object != null) {
            this.successStory = (SuccessStory) FastJsonUtils.fromJson(object, SuccessStory.class);
            if (this.page == 1) {
                refreshHeader();
                if (this.successStory != null) {
                    if (this.adapter == null) {
                        this.storyList.clear();
                        this.storyList.addAll(this.successStory.items);
                        this.adapter = new SuccessStoryAdapter(this.ctx, this.storyList);
                        this.pullToRefreshListView.setAdapter(this.adapter);
                    } else {
                        this.adapter.replaceAll(this.successStory.items);
                    }
                    this.page++;
                    return;
                }
                return;
            }
            this.storyList.addAll(this.successStory.items);
            this.adapter.notifyDataSetChanged();
            this.page++;
        }
    }

    private void refreshHeader() {
        if (this.successStory.sliders == null || this.successStory.sliders.size() <= 0) {
            this.ivHeader.setVisibility(8);
            return;
        }
        this.ivHeader.setVisibility(0);
        this.imageLoader.displayImage(((SlidersEntity) this.successStory.sliders.get(0)).pic_url,
                this.ivHeader);
        this.ivHeader.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BooheeScheme.handleUrl(SuccessStoryActivity.this.ctx, ((SlidersEntity)
                        SuccessStoryActivity.this.successStory.sliders.get(0)).url, (
                        (SlidersEntity) SuccessStoryActivity.this.successStory.sliders.get(0))
                        .title);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.x, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_tag:
                if (this.successStory == null || this.successStory.tags == null) {
                    return true;
                }
                SuccessStoryTagActivity.comeOn(this.ctx, this.successStory.tags);
                overridePendingTransition(R.anim.p, R.anim.l);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void comeOnWithTag(Context context, String tag) {
        Intent intent = new Intent(context, SuccessStoryActivity.class);
        intent.putExtra(TAGS, tag);
        intent.addFlags(67108864);
        context.startActivity(intent);
    }

    public static void comeOn(Context context) {
        Intent intent = new Intent(context, SuccessStoryActivity.class);
        intent.addFlags(67108864);
        context.startActivity(intent);
    }
}
