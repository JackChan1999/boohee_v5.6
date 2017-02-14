package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.StatusApi;
import com.boohee.main.GestureActivity;
import com.boohee.model.SearcherArticle;
import com.boohee.myview.DividerItemDecoration;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.status.TopicActivity;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.OnRecyclerLoadMoreListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SearcherOtherActivity extends GestureActivity {
    private static final String KEY_TYPE = "key_type";
    private EditText      etSearch;
    private SearchAdapter mAdapter;
    private List<SearcherArticle> mDataList  = new ArrayList();
    private int                   mPageIndex = 1;
    private String mType;
    @InjectView(2131427874)
    RecyclerView rvSearch;
    private View viewClear;
    @InjectView(2131427873)
    TextView viewNoResult;

    private class SearchAdapter extends Adapter<ViewHolder> {
        private List<SearcherArticle> mDataList;
        private String                mType;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
                implements OnClickListener {
            public TextView tvContent;

            public ViewHolder(View view) {
                super(view);
                this.tvContent = (TextView) view.findViewById(R.id.tv_content);
                this.tvContent.setOnClickListener(this);
            }

            public void onClick(View v) {
                SearcherArticle article = (SearcherArticle) v.getTag();
                if (SearchAdapter.this.mType.equals(Type.article.name())) {
                    BrowserActivity.comeOnBaby(SearcherOtherActivity.this.ctx, article.title,
                            article.link_url);
                } else if (SearchAdapter.this.mType.equals(Type.topic.name())) {
                    TopicActivity.comeOnBaby(SearcherOtherActivity.this.ctx, article.title);
                }
            }
        }

        public SearchAdapter(String type, List<SearcherArticle> dataList) {
            this.mType = type;
            this.mDataList = dataList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.iu,
                    parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            SearcherArticle article = (SearcherArticle) this.mDataList.get(position);
            if (article != null) {
                holder.tvContent.setTag(article);
                holder.tvContent.setText(article.title);
            }
        }

        public int getItemCount() {
            return this.mDataList.size();
        }
    }

    public enum Type {
        topic,
        article
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cx);
        ButterKnife.inject((Activity) this);
        initToolbar();
        initSearch();
    }

    private void initToolbar() {
        View toolbar = LayoutInflater.from(this).inflate(R.layout.mn, null);
        this.etSearch = (EditText) toolbar.findViewById(R.id.et_search);
        showKeyboard(this.etSearch);
        this.viewClear = toolbar.findViewById(R.id.view_clear);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(toolbar, new LayoutParams(-1, -1));
        this.viewClear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearcherOtherActivity.this.etSearch.setText("");
                SearcherOtherActivity.this.mDataList.clear();
                SearcherOtherActivity.this.viewNoResult.setVisibility(8);
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                boolean hasContent;
                int i = 0;
                if (TextUtils.isEmpty(s.toString())) {
                    hasContent = false;
                } else {
                    hasContent = true;
                }
                View access$200 = SearcherOtherActivity.this.viewClear;
                if (!hasContent) {
                    i = 4;
                }
                access$200.setVisibility(i);
            }
        });
        this.etSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SearcherOtherActivity.this.sendSearchRequest(false);
                return true;
            }
        });
    }

    private void showKeyboard(final EditText etSearch) {
        etSearch.postDelayed(new Runnable() {
            public void run() {
                etSearch.requestFocus();
            }
        }, 300);
    }

    private void initSearch() {
        this.mType = getIntent().getStringExtra(KEY_TYPE);
        if (TextUtils.isEmpty(this.mType)) {
            Helper.showToast((CharSequence) "参数错误！");
            finish();
            return;
        }
        if (this.mType.equals(Type.article.name())) {
            this.etSearch.setHint("请输入文章名称");
        } else if (this.mType.equals(Type.topic.name())) {
            this.etSearch.setHint("请输入话题名称");
        }
        this.rvSearch.setHasFixedSize(true);
        this.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        this.mAdapter = new SearchAdapter(this.mType, this.mDataList);
        this.rvSearch.setAdapter(this.mAdapter);
        this.rvSearch.addItemDecoration(new DividerItemDecoration(this, 1));
        this.rvSearch.addOnScrollListener(new OnRecyclerLoadMoreListener() {
            public void onLoadMore() {
                SearcherOtherActivity.this.sendSearchRequest(true);
            }
        });
    }

    private void sendSearchRequest(boolean isLoadMore) {
        String searchKey = this.etSearch.getText().toString();
        if (!TextUtils.isEmpty(searchKey)) {
            String encodeKey = searchKey;
            try {
                encodeKey = URLEncoder.encode(searchKey, "UTF-8");
                if (!isLoadMore) {
                    this.mPageIndex = 1;
                    this.mDataList.clear();
                    this.mAdapter.notifyDataSetChanged();
                    this.viewNoResult.setVisibility(8);
                    this.rvSearch.setVisibility(0);
                }
                if (this.mType.equals(Type.topic.name())) {
                    sendTopicRequest(encodeKey, isLoadMore);
                } else if (this.mType.equals(Type.article.name())) {
                    sendArticleRequest(encodeKey, isLoadMore);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendTopicRequest(String encodeKey, final boolean isLoadMore) {
        showLoading();
        StatusApi.searchTopic(encodeKey, this.mPageIndex, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                List<String> topicList = FastJsonUtils.parseList(object.optString("topics"),
                        String.class);
                if (topicList == null || topicList.size() <= 0) {
                    SearcherOtherActivity.this.showView(isLoadMore);
                    return;
                }
                for (String topic : topicList) {
                    SearcherArticle article = new SearcherArticle();
                    article.title = topic;
                    SearcherOtherActivity.this.mDataList.add(article);
                }
                SearcherOtherActivity.this.mAdapter.notifyDataSetChanged();
                SearcherOtherActivity.this.mPageIndex = SearcherOtherActivity.this.mPageIndex + 1;
            }

            public void onFinish() {
                SearcherOtherActivity.this.dismissLoading();
            }
        }, this.ctx);
    }

    private void sendArticleRequest(String encodeKey, final boolean isLoadMore) {
        String url = String.format("/api/v1/knowledges/search?title=%1$s&page=%2$d", new
                Object[]{encodeKey, Integer.valueOf(this.mPageIndex)});
        showLoading();
        BooheeClient.build(BooheeClient.BINGO).get(url, new JsonCallback(this.ctx) {
            public void ok(JSONObject object) {
                List<SearcherArticle> articleList = FastJsonUtils.parseList(object.optString
                        ("knowledges"), SearcherArticle.class);
                if (articleList == null || articleList.size() <= 0) {
                    SearcherOtherActivity.this.showView(isLoadMore);
                    return;
                }
                SearcherOtherActivity.this.mDataList.addAll(articleList);
                SearcherOtherActivity.this.mAdapter.notifyDataSetChanged();
                SearcherOtherActivity.this.mPageIndex = SearcherOtherActivity.this.mPageIndex + 1;
            }

            public void onFinish() {
                SearcherOtherActivity.this.dismissLoading();
            }
        }, this.ctx);
    }

    private void showView(boolean isLoadMore) {
        boolean hasResult;
        int i;
        int i2 = 0;
        if (isLoadMore) {
            Helper.showToast((CharSequence) "没有更多了!");
        }
        if (this.mDataList.size() > 0) {
            hasResult = true;
        } else {
            hasResult = false;
        }
        TextView textView = this.viewNoResult;
        if (hasResult) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        RecyclerView recyclerView = this.rvSearch;
        if (!hasResult) {
            i2 = 8;
        }
        recyclerView.setVisibility(i2);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mDataList.clear();
    }

    public static void comeOnBaby(Context context, Type type) {
        if (context == null) {
            Helper.showToast((CharSequence) "Start SearcherActivity fail, context is null!");
            return;
        }
        Intent intent = new Intent(context, SearcherOtherActivity.class);
        intent.putExtra(KEY_TYPE, type.name());
        context.startActivity(intent);
    }
}
