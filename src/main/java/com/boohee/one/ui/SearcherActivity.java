package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.boohee.api.FoodApi;
import com.boohee.food.FoodDetailActivity;
import com.boohee.food.UploadFoodActivity;
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodInfo;
import com.boohee.model.FoodWithUnit;
import com.boohee.myview.DividerItemDecoration;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.SearcherOtherActivity.Type;
import com.boohee.status.SearchFriendsActivity;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.OnRecyclerLoadMoreListener;
import com.boohee.utils.TextUtil;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SearcherActivity extends GestureActivity {
    private static final int RECOMMEND_SPAN_COUNT = 3;
    private EditText etSearch;
    private boolean  isQuickSearch;
    private int mPageIndex = 1;
    private RecommendAdapter mRecommendAdapter;
    private List<String> mRecommendList = new ArrayList();
    private GridLayoutManager mRecommendManager;
    private SearchAdapter     mSearchAdapter;
    private List<FoodWithUnit> mSearchList = new ArrayList();
    @InjectView(2131427868)
    RecyclerView rvRecommend;
    @InjectView(2131427874)
    RecyclerView rvSearch;
    @InjectView(2131427876)
    TextView     tvSearchAlert;
    private View viewClear;
    @InjectView(2131427613)
    View         viewContent;
    @InjectView(2131427862)
    View         viewLoadMore;
    @InjectView(2131427873)
    TextView     viewNoResult;
    @InjectView(2131427867)
    LinearLayout viewRecommend;
    private View viewScan;
    @InjectView(2131427872)
    FrameLayout  viewSearch;
    @InjectView(2131427875)
    LinearLayout viewSearchAlert;
    @InjectView(2131427866)
    LinearLayout viewWelcome;

    private class RecommendAdapter extends Adapter<ViewHolder> {
        private List<String> mDataList;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
                implements OnClickListener {
            public TextView tvContent;

            public ViewHolder(View view) {
                super(view);
                this.tvContent = (TextView) view.findViewById(R.id.tv_hot_key);
                view.setOnClickListener(this);
            }

            public void onClick(View v) {
                CharSequence hotKey = ((TextView) v).getText();
                if (!TextUtils.isEmpty(hotKey)) {
                    SearcherActivity.this.etSearch.setText(hotKey);
                    SearcherActivity.this.sendSearchRequest(false);
                }
            }
        }

        public RecommendAdapter(List<String> dataList) {
            this.mDataList = dataList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.iv,
                    parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            String hotKey = (String) this.mDataList.get(position);
            if (!TextUtils.isEmpty(hotKey)) {
                holder.tvContent.setText(hotKey);
            }
        }

        public int getItemCount() {
            return this.mDataList.size();
        }
    }

    private class SearchAdapter extends Adapter<ViewHolder> {
        private List<FoodWithUnit> mDataList;

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
                implements OnClickListener {
            public View     parentView;
            public TextView tvCalory;
            public TextView tvName;

            public ViewHolder(View view) {
                super(view);
                this.tvName = (TextView) view.findViewById(R.id.tv_name);
                this.tvCalory = (TextView) view.findViewById(R.id.tv_calory);
                this.parentView = view;
                this.parentView.setOnClickListener(this);
            }

            public void onClick(View v) {
                FoodWithUnit food = (FoodWithUnit) v.getTag();
                if (food != null) {
                    FoodDetailActivity.comeOnBaby(v.getContext(), food.code, true);
                }
            }
        }

        public SearchAdapter(List<FoodWithUnit> foodList) {
            this.mDataList = foodList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.it,
                    parent, false));
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            FoodWithUnit food = (FoodWithUnit) this.mDataList.get(position);
            if (food != null) {
                holder.parentView.setTag(food);
                holder.tvName.setText(food.name);
                holder.tvCalory.setText(String.format("%.0f 千卡/100克", new Object[]{Float.valueOf
                        (food.calory)}));
            }
        }

        public int getItemCount() {
            return this.mDataList.size();
        }
    }

    @OnClick({2131427869, 2131427870, 2131427871, 2131427876, 2131427862})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_load_more:
                sendSearchRequest(false);
                return;
            case R.id.view_search_topic:
                SearcherOtherActivity.comeOnBaby(this.ctx, Type.topic);
                MobclickAgent.onEvent(this.ctx, Event.other_clickSearchTopic);
                return;
            case R.id.view_search_article:
                MobclickAgent.onEvent(this.ctx, Event.other_clickSearchContent);
                SearcherOtherActivity.comeOnBaby(this.ctx, Type.article);
                return;
            case R.id.view_search_user:
                MobclickAgent.onEvent(this.ctx, Event.ohter_clickSearchUser);
                SearchFriendsActivity.comeOnBaby(this.ctx);
                return;
            case R.id.tv_search_alert:
                sendSearchRequest(false);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cw);
        ButterKnife.inject((Activity) this);
        initToolbar();
        initRecommend();
        initSearch();
    }

    private void initToolbar() {
        View toolbar = LayoutInflater.from(this).inflate(R.layout.mo, null);
        this.etSearch = (EditText) toolbar.findViewById(R.id.et_search);
        showKeyboard(this.etSearch);
        this.viewClear = toolbar.findViewById(R.id.view_clear);
        this.viewScan = toolbar.findViewById(R.id.view_scan);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(toolbar, new LayoutParams(-1, -1));
        this.viewClear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearcherActivity.this.etSearch.setText("");
                SearcherActivity.this.viewWelcome.setVisibility(0);
                SearcherActivity.this.viewSearch.setVisibility(8);
                SearcherActivity.this.mSearchList.clear();
            }
        });
        this.viewScan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ScannerActivity.startScannerForResult(SearcherActivity.this.activity, 175);
            }
        });
        this.etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                SearcherActivity.this.viewSearchAlert.setVisibility(0);
                SearcherActivity.this.tvSearchAlert.setText(SearcherActivity.this.searchAlert(s
                        .toString()));
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearcherActivity.this.tvSearchAlert.setText(SearcherActivity.this.searchAlert(s
                        .toString()));
            }

            public void afterTextChanged(Editable s) {
                boolean hasContent;
                int i;
                int i2 = 0;
                if (TextUtils.isEmpty(s.toString().trim())) {
                    hasContent = false;
                } else {
                    hasContent = true;
                }
                View access$300 = SearcherActivity.this.viewClear;
                if (hasContent) {
                    i = 0;
                } else {
                    i = 8;
                }
                access$300.setVisibility(i);
                LinearLayout linearLayout = SearcherActivity.this.viewWelcome;
                if (hasContent) {
                    i = 8;
                } else {
                    i = 0;
                }
                linearLayout.setVisibility(i);
                FrameLayout frameLayout = SearcherActivity.this.viewSearch;
                if (!hasContent) {
                    i2 = 8;
                }
                frameLayout.setVisibility(i2);
                if (!hasContent) {
                    SearcherActivity.this.mSearchList.clear();
                    SearcherActivity.this.mSearchAdapter.notifyDataSetChanged();
                }
                SearcherActivity.this.quickSearch(s.toString());
            }
        });
        this.etSearch.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SearcherActivity.this.sendSearchRequest(false);
                return true;
            }
        });
    }

    private void quickSearch(String key) {
        if (key == null) {
            this.viewSearchAlert.setVisibility(8);
            return;
        }
        if (key.trim().length() == 0) {
            this.viewSearchAlert.setVisibility(8);
            return;
        }
        this.isQuickSearch = true;
        BooheeClient.build(BooheeClient.FOOD).get(String.format("/fb/v1/foods/fast_search?q=%s",
                new Object[]{key}), new JsonCallback(this) {
            public void ok(JSONObject object) {
                try {
                    List<FoodWithUnit> foodList = JSON.parseArray(object.optString("foods"),
                            FoodWithUnit.class);
                    if (foodList == null || foodList.size() <= 0) {
                        SearcherActivity.this.viewSearchAlert.setVisibility(0);
                        SearcherActivity.this.viewNoResult.setVisibility(0);
                        SearcherActivity.this.viewContent.setVisibility(8);
                        SearcherActivity.this.viewLoadMore.setVisibility(8);
                        return;
                    }
                    SearcherActivity.this.viewSearchAlert.setVisibility(8);
                    SearcherActivity.this.viewNoResult.setVisibility(8);
                    SearcherActivity.this.viewContent.setVisibility(0);
                    SearcherActivity.this.viewLoadMore.setVisibility(0);
                    SearcherActivity.this.mSearchList.clear();
                    SearcherActivity.this.mSearchList.addAll(foodList);
                    SearcherActivity.this.mSearchAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
            }
        }, this.ctx);
    }

    private String searchAlert(String key) {
        return "搜索：" + key;
    }

    private void showKeyboard(final EditText etSearch) {
        etSearch.postDelayed(new Runnable() {
            public void run() {
                etSearch.requestFocus();
            }
        }, 300);
    }

    private void initRecommend() {
        this.rvRecommend.setHasFixedSize(true);
        this.mRecommendManager = new GridLayoutManager(this, 3);
        this.rvRecommend.setLayoutManager(this.mRecommendManager);
        this.mRecommendAdapter = new RecommendAdapter(this.mRecommendList);
        this.rvRecommend.setAdapter(this.mRecommendAdapter);
        BooheeClient.build("ifood").get("/v2/ifoods/keywords", new JsonCallback(this) {
            public void ok(JSONObject object) {
                try {
                    List<String> foods = JSON.parseArray(object.optString("keywords"), String
                            .class);
                    if (foods != null && foods.size() > 0) {
                        SearcherActivity.this.viewRecommend.setVisibility(0);
                        SearcherActivity.this.mRecommendList.clear();
                        SearcherActivity.this.mRecommendList.addAll(foods);
                        SearcherActivity.this.mRecommendAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
            }
        }, this.ctx);
    }

    private void initSearch() {
        this.rvSearch.setHasFixedSize(true);
        this.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        this.mSearchAdapter = new SearchAdapter(this.mSearchList);
        this.rvSearch.setAdapter(this.mSearchAdapter);
        this.rvSearch.addItemDecoration(new DividerItemDecoration(this, 1));
        this.rvSearch.addOnScrollListener(new OnRecyclerLoadMoreListener() {
            public void onLoadMore() {
                SearcherActivity.this.sendSearchRequest(true);
            }
        });
        this.rvSearch.addOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (SearcherActivity.this.isQuickSearch) {
                    KeyBoardUtils.closeAll(SearcherActivity.this.ctx);
                }
            }
        });
    }

    private void sendSearchRequest(final boolean isLoadMore) {
        this.viewSearchAlert.setVisibility(8);
        String searchKey = this.etSearch.getText().toString();
        if (!TextUtils.isEmpty(searchKey)) {
            String encodeKey = searchKey;
            try {
                encodeKey = URLEncoder.encode(searchKey, "UTF-8");
                if (!isLoadMore) {
                    this.mPageIndex = 1;
                    this.mSearchList.clear();
                    this.mSearchAdapter.notifyDataSetChanged();
                }
                this.isQuickSearch = false;
                this.viewLoadMore.setVisibility(8);
                showLoading();
                FoodApi.getFoodsSearch(this, encodeKey, this.mPageIndex, new JsonCallback(this) {
                    public void ok(JSONObject object) {
                        List<FoodWithUnit> foodList = FastJsonUtils.parseList(object.optString
                                ("foods"), FoodWithUnit.class);
                        if (foodList != null && foodList.size() > 0) {
                            SearcherActivity.this.mSearchList.addAll(foodList);
                            SearcherActivity.this.mSearchAdapter.notifyDataSetChanged();
                            SearcherActivity.this.mPageIndex = SearcherActivity.this.mPageIndex + 1;
                        } else if (isLoadMore) {
                            Helper.showToast((CharSequence) "没有更多了!");
                        } else {
                            SearcherActivity.this.viewNoResult.setVisibility(0);
                            SearcherActivity.this.viewContent.setVisibility(8);
                        }
                    }

                    public void onFinish() {
                        SearcherActivity.this.dismissLoading();
                        if (SearcherActivity.this.mSearchList.size() > 0) {
                            SearcherActivity.this.viewNoResult.setVisibility(8);
                            SearcherActivity.this.viewContent.setVisibility(0);
                        }
                        KeyBoardUtils.closeKeybord(SearcherActivity.this.ctx, SearcherActivity
                                .this.etSearch);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchFoodWithCode(final String code) {
        if (!TextUtils.isEmpty(code)) {
            showLoading();
            FoodApi.getFoodWithBarcode(code, this.activity, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    List<FoodInfo> fooInfos = FastJsonUtils.parseList(object.optString("foods"),
                            FoodInfo.class);
                    if (fooInfos == null || fooInfos.size() <= 0) {
                        SearcherActivity.this.showUploadDialog(code);
                        return;
                    }
                    FoodDetailActivity.comeOnBaby(SearcherActivity.this.ctx, ((FoodInfo) fooInfos
                            .get(0)).code, true);
                }

                public void onFinish() {
                    SearcherActivity.this.dismissLoading();
                }
            });
        }
    }

    private void showUploadDialog(final String code) {
        LightAlertDialog.create(this.ctx, (int) R.string.ab8).setNegativeButton((int) R.string
                .ab9, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton((int) R.string.ab_, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MobclickAgent.onEvent(SearcherActivity.this.ctx, Event.tool_searchfood_assistadd);
                UploadFoodActivity.comeOnBabyWithCode(SearcherActivity.this.ctx, code);
            }
        }).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mRecommendList.clear();
        this.mRecommendList = null;
    }

    public void onBackPressed() {
        try {
            String searchKey = this.etSearch.getText().toString();
            if (TextUtil.isEmpty(searchKey)) {
                super.onBackPressed();
                return;
            }
            this.etSearch.setText("");
            this.viewSearch.setVisibility(8);
        } catch (Exception e) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && 175 == requestCode) {
            searchFoodWithCode(data.getStringExtra(ScannerActivity.CODE_DATA));
        }
    }

    public static void comeOnBaby(Context context) {
        if (context == null) {
            Helper.showToast((CharSequence) "Start SearcherActivity fail, context is null!");
        } else {
            context.startActivity(new Intent(context, SearcherActivity.class));
        }
    }
}
