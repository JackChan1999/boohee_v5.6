package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.boohee.api.FoodApi;
import com.boohee.database.OnePreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodInfo;
import com.boohee.model.FoodWithUnit;
import com.boohee.myview.highlight.HighLight;
import com.boohee.myview.highlight.HighLight.MarginInfo;
import com.boohee.myview.highlight.HighLight.OnHighLightClickListener;
import com.boohee.myview.highlight.HighLight.OnPosCallback;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.ui.ScannerActivity;
import com.boohee.one.ui.adapter.SimpleBaseAdapter;
import com.boohee.one.ui.adapter.SimpleBaseAdapter.ViewHolder;
import com.boohee.one.ui.fragment.AddDietFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.ExpandGridView;
import com.boohee.widgets.LightAlertDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class SearchFoodActivity extends GestureActivity implements OnItemClickListener,
        OnScrollListener {
    private static final String KEY_DATE        = "key_date";
    private static final String KEY_IS_ADD_DIET = "isAddDiet";
    private static final String KEY_TIME_TYPE   = "key_time_type";
    private static final String SEPARATOR       = "##";
    EditText et_search;
    @InjectView(2131427861)
    ExpandGridView gvHot;
    private List<String> hotSearchList = new ArrayList();
    private boolean      isAddDiet     = true;
    private boolean isQuickSearch;
    ImageView iv_clear;
    ImageView iv_search;
    @InjectView(2131427568)
    LinearLayout ll_history;
    @InjectView(2131427569)
    LinearLayout ll_history_content;
    @InjectView(2131427571)
    LinearLayout ll_result;
    @InjectView(2131427572)
    ListView     lv_result;
    private int mCurrentPage = 1;
    private View mCustomView;
    private List<String> mHistoryList = new ArrayList();
    private int          mLastindex   = 0;
    private int          mPage        = 1;
    private String            mQueryString;
    private SearchViewAdapter mResultAdapter;
    private List<FoodWithUnit> mResultList = new ArrayList();
    private int mTimeType;
    private int mTotalcount = 0;
    private String record_on;
    @InjectView(2131427567)
    ScrollView sv_search;
    @InjectView(2131427573)
    TextView   tv_null;
    @InjectView(2131427574)
    TextView   tv_search_tip;
    @InjectView(2131427862)
    TextView   viewLoadMore;

    public class HotListAdapter extends SimpleBaseAdapter<String> {
        public HotListAdapter(Context context, List<String> data) {
            super(context, data);
        }

        public int getItemResource() {
            return R.layout.is;
        }

        public View getItemView(int position, View convertView, ViewHolder holder) {
            TextView txt_title = (TextView) holder.getView(R.id.tv_title);
            if (!TextUtils.isEmpty((CharSequence) this.data.get(position))) {
                txt_title.setText((CharSequence) this.data.get(position));
            }
            return convertView;
        }
    }

    private class SearchOnEditorActionListener implements OnEditorActionListener {
        private SearchOnEditorActionListener() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            SearchFoodActivity.this.doSearch();
            return true;
        }
    }

    private class SearchTextWatcher implements TextWatcher {
        private SearchTextWatcher() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString().trim())) {
                SearchFoodActivity.this.iv_clear.setVisibility(8);
                SearchFoodActivity.this.tv_search_tip.setVisibility(8);
                SearchFoodActivity.this.sv_search.setVisibility(0);
                SearchFoodActivity.this.ll_result.setVisibility(8);
            } else {
                SearchFoodActivity.this.iv_clear.setVisibility(0);
                SearchFoodActivity.this.tv_search_tip.setVisibility(0);
                SearchFoodActivity.this.sv_search.setVisibility(8);
                SearchFoodActivity.this.tv_search_tip.setText(String.format("搜索 “%s”", new
                        Object[]{s.toString()}));
                SearchFoodActivity.this.tv_null.setVisibility(8);
            }
            SearchFoodActivity.this.quickSearch(s.toString());
        }
    }

    @OnClick({2131427570, 2131427863, 2131427574, 2131427862})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_history_clear:
                this.ll_history.setVisibility(8);
                OnePreference.getInstance(this.ctx).clearSearchHistory();
                return;
            case R.id.search_tip:
                this.tv_search_tip.setVisibility(8);
                doSearch();
                return;
            case R.id.view_load_more:
                doSearch();
                return;
            case R.id.tv_search_null:
                MobclickAgent.onEvent(this.ctx, Event.tool_searchfood_customfood);
                if (this.isAddDiet) {
                    AddCustomFoodActivity.start(this.activity, this.mTimeType, this.record_on);
                    return;
                } else {
                    AddCustomFoodActivity.comeWithoutAddDiet(this.ctx);
                    return;
                }
            default:
                return;
        }
    }

    public static void comeWithoutAddDiet(Context context) {
        Intent intent = new Intent(context, SearchFoodActivity.class);
        intent.putExtra(KEY_IS_ADD_DIET, false);
        context.startActivity(intent);
    }

    public static void start(Context context, int time_type, String record_on) {
        Intent starter = new Intent(context, SearchFoodActivity.class);
        starter.putExtra(KEY_TIME_TYPE, time_type);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
        ((Activity) context).overridePendingTransition(R.anim.o, R.anim.l);
    }

    private void handleIntent() {
        this.mTimeType = getIntent().getIntExtra(KEY_TIME_TYPE, -1);
        this.record_on = getIntent().getStringExtra("key_date");
        this.isAddDiet = getIntent().getBooleanExtra(KEY_IS_ADD_DIET, true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cu);
        ButterKnife.inject((Activity) this);
        initToolsBar();
        handleIntent();
        addListener();
        loadHistoryData();
        loadHotData();
        checkGuide();
    }

    private void loadHotData() {
        showLoading();
        BooheeClient.build("ifood").get("/v2/ifoods/keywords", new JsonCallback(this) {
            public void ok(JSONObject object) {
                super.ok(object);
                try {
                    List<String> foods = JSON.parseArray(object.optString("keywords"), String
                            .class);
                    if (foods != null && foods.size() > 0) {
                        SearchFoodActivity.this.hotSearchList.addAll(foods);
                        SearchFoodActivity.this.gvHot.setAdapter(new HotListAdapter
                                (SearchFoodActivity.this.ctx, SearchFoodActivity.this
                                        .hotSearchList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFinish() {
                super.onFinish();
                SearchFoodActivity.this.dismissLoading();
            }
        }, this.ctx);
    }

    private void initToolsBar() {
        this.mCustomView = LayoutInflater.from(this).inflate(R.layout.mo, null);
        this.et_search = (EditText) this.mCustomView.findViewById(R.id.et_search);
        this.iv_clear = (ImageView) this.mCustomView.findViewById(R.id.view_clear);
        this.iv_search = (ImageView) this.mCustomView.findViewById(R.id.view_scan);
        this.et_search.setHint(R.string.a3f);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(this.mCustomView, new LayoutParams(-1, -1));
    }

    private void addListener() {
        this.lv_result.setOnItemClickListener(this);
        this.lv_result.setOnScrollListener(this);
        this.gvHot.setOnItemClickListener(this);
        this.et_search.addTextChangedListener(new SearchTextWatcher());
        this.et_search.setOnEditorActionListener(new SearchOnEditorActionListener());
        this.iv_search.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchFoodActivity.this.doScan();
            }
        });
        this.iv_clear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchFoodActivity.this.et_search.setText("");
                SearchFoodActivity.this.ll_result.setVisibility(8);
                SearchFoodActivity.this.sv_search.setVisibility(0);
                SearchFoodActivity.this.loadHistoryData();
            }
        });
    }

    private void checkGuide() {
        if (!OnePreference.isSearchFoodGuide() && !this.isAddDiet) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SearchFoodActivity.this.showHighLight();
                }
            }, 500);
        }
    }

    private void showHighLight() {
        if (this.iv_search != null) {
            HighLight highLight = new HighLight(this.activity).addHighLight((int) R.id.iv_search,
                    (int) R.layout.qm, new OnPosCallback() {
                public void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo
                        marginInfo) {
                    marginInfo.rightMargin = (rectF.width() / 2.0f) + ((float) ViewUtils.dip2px
                            (SearchFoodActivity.this.activity, 10.0f));
                    marginInfo.bottomMargin = bottomMargin - ((float) ViewUtils.dip2px
                            (SearchFoodActivity.this.activity, 40.0f));
                }
            });
            highLight.show();
            highLight.setOnHighLightClickListener(new OnHighLightClickListener() {
                public void onClick() {
                    OnePreference.setSearchFoodGuide(true);
                }
            });
        }
    }

    private void doFastSearch(String queryString) {
        if (!TextUtils.isEmpty(queryString)) {
            this.mPage = 1;
            this.mCurrentPage = 1;
            this.et_search.setText(queryString);
            this.et_search.setSelection(queryString.length());
            loadData(queryString);
        }
    }

    private void doScan() {
        ScannerActivity.startScannerForResult(this.activity, 175);
    }

    private void doSearch() {
        this.mQueryString = this.et_search.getText().toString();
        if (TextUtils.isEmpty(this.mQueryString)) {
            Helper.showToast(getResources().getString(R.string.a3i));
            return;
        }
        this.mPage = 1;
        this.mCurrentPage = 1;
        loadData(this.mQueryString);
    }

    private void loadData(String queryString) {
        this.isQuickSearch = false;
        this.viewLoadMore.setVisibility(8);
        try {
            String encodeString = URLEncoder.encode(queryString, "UTF-8");
            if (!TextUtils.isEmpty(encodeString)) {
                showLoading();
                FoodApi.getFoodsSearch(this, encodeString, this.mPage, new JsonCallback(this) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        SearchFoodActivity.this.refreshData(FastJsonUtils.parseList(object
                                .optString("foods"), FoodWithUnit.class));
                    }

                    public void onFinish() {
                        SearchFoodActivity.this.dismissLoading();
                        KeyBoardUtils.closeKeybord(SearchFoodActivity.this.ctx,
                                SearchFoodActivity.this.et_search);
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void refreshData(List<FoodWithUnit> foodList) {
        if (this.mPage == 1) {
            if (foodList == null || foodList.size() <= 0) {
                this.ll_result.setVisibility(8);
                this.sv_search.setVisibility(8);
                this.tv_null.setVisibility(0);
                return;
            }
            this.mResultList.clear();
            this.mResultList.addAll(foodList);
            saveSearchHistory(this.mQueryString);
            this.mResultAdapter = new SearchViewAdapter(this.ctx, this.mResultList);
            this.lv_result.setAdapter(this.mResultAdapter);
            this.ll_result.setVisibility(0);
            this.tv_null.setVisibility(8);
            this.sv_search.setVisibility(8);
            this.mResultAdapter.notifyDataSetChanged();
            this.mPage++;
        } else if (foodList != null && foodList.size() > 0) {
            this.mResultList.addAll(foodList);
            this.mResultAdapter.notifyDataSetChanged();
            this.mPage++;
        }
    }

    private void loadHistoryData() {
        String historyString = OnePreference.getInstance(this.ctx).getSearchHistory();
        if (TextUtils.isEmpty(historyString)) {
            this.ll_history.setVisibility(8);
            return;
        }
        this.ll_history.setVisibility(0);
        List<String> arrayList = Arrays.asList(historyString.split(SEPARATOR));
        if (this.mHistoryList != null) {
            this.mHistoryList.clear();
            this.mHistoryList.addAll(arrayList);
            Collections.reverse(this.mHistoryList);
        }
        initHistory();
    }

    private void initHistory() {
        if (this.mHistoryList != null) {
            this.ll_history_content.removeAllViews();
            for (int i = 0; i < this.mHistoryList.size(); i++) {
                final String title = (String) this.mHistoryList.get(i);
                View view = LayoutInflater.from(this.ctx).inflate(R.layout.ir, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        SearchFoodActivity.this.mQueryString = title;
                        SearchFoodActivity.this.doFastSearch(SearchFoodActivity.this.mQueryString);
                    }
                });
                this.ll_history_content.addView(view);
            }
        }
    }

    private void saveSearchHistory(String keyString) {
        String historyString = OnePreference.getInstance(this.ctx).getSearchHistory();
        if (TextUtils.isEmpty(historyString)) {
            OnePreference.getInstance(this.ctx).setSearchHistory(keyString + SEPARATOR);
            return;
        }
        List<String> arrayList = arrayToList(historyString.split(SEPARATOR));
        if (!arrayList.contains(keyString)) {
            if (arrayList.size() >= 5) {
                arrayList.remove(0);
                arrayList.add(keyString);
                historyString = listToString(arrayList);
            } else {
                historyString = historyString + keyString + SEPARATOR;
            }
            OnePreference.getInstance(this.ctx).setSearchHistory(historyString);
        }
    }

    private String listToString(List<String> arrayList) {
        String historyString = "";
        if (arrayList == null || arrayList.size() == 0) {
            return historyString;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            historyString = historyString + ((String) arrayList.get(i)) + SEPARATOR;
        }
        return historyString.substring(0, historyString.lastIndexOf(SEPARATOR));
    }

    private List<String> arrayToList(String[] strings) {
        List<String> lists = new ArrayList();
        if (!(strings == null || strings.length == 0)) {
            for (Object add : strings) {
                lists.add(add);
            }
        }
        return lists;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!ViewUtils.isFastDoubleClick() && this.activity != null) {
            switch (parent.getId()) {
                case R.id.lv_result:
                    FoodWithUnit food = (FoodWithUnit) this.mResultAdapter.getItem(position);
                    if (this.isAddDiet) {
                        Fragment addDietFragment = AddDietFragment.newInstance(this.mTimeType,
                                this.record_on, food.code);
                        addDietFragment.setChangeListener(new onChangeListener() {
                            public void onFinish() {
                                SearchFoodActivity.this.finish();
                            }
                        });
                        FragmentTransaction transaction = getSupportFragmentManager()
                                .beginTransaction();
                        transaction.add(addDietFragment, "addDietFragment");
                        transaction.commitAllowingStateLoss();
                        return;
                    }
                    FoodDetailActivity.comeOnBaby(this.ctx, food.code, true);
                    return;
                case R.id.gv_hot:
                    this.mQueryString = (String) this.hotSearchList.get(position);
                    doFastSearch(this.mQueryString);
                    return;
                default:
                    return;
            }
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.isQuickSearch) {
            KeyBoardUtils.closeAll(this.ctx);
        }
        if (scrollState == 0 && this.mTotalcount == this.mLastindex && this.mPage <= 10 && this
                .mPage > this.mCurrentPage) {
            this.mCurrentPage = this.mPage;
            loadData(this.mQueryString);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {
        this.mLastindex = firstVisibleItem + visibleItemCount;
        this.mTotalcount = totalItemCount;
    }

    private void quickSearch(String key) {
        if (key != null) {
            if (key.trim().length() != 0) {
                this.isQuickSearch = true;
                this.viewLoadMore.setVisibility(0);
                BooheeClient.build(BooheeClient.FOOD).get(String.format
                        ("/fb/v1/foods/fast_search?q=%s", new Object[]{key}), new JsonCallback
                        (this) {
                    public void ok(JSONObject object) {
                        try {
                            List<FoodWithUnit> foodList = JSON.parseArray(object.optString
                                    ("foods"), FoodWithUnit.class);
                            if (foodList == null || foodList.size() <= 0) {
                                SearchFoodActivity.this.ll_result.setVisibility(8);
                                SearchFoodActivity.this.sv_search.setVisibility(8);
                                SearchFoodActivity.this.tv_null.setVisibility(0);
                                return;
                            }
                            SearchFoodActivity.this.ll_result.setVisibility(0);
                            SearchFoodActivity.this.sv_search.setVisibility(8);
                            SearchFoodActivity.this.tv_null.setVisibility(8);
                            SearchFoodActivity.this.mResultList.clear();
                            SearchFoodActivity.this.mResultList.addAll(foodList);
                            SearchFoodActivity.this.mResultAdapter = new SearchViewAdapter
                                    (SearchFoodActivity.this.ctx, SearchFoodActivity.this
                                            .mResultList);
                            SearchFoodActivity.this.lv_result.setAdapter(SearchFoodActivity.this
                                    .mResultAdapter);
                            SearchFoodActivity.this.mResultAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFinish() {
                    }
                }, this.ctx);
            }
        }
    }

    public void onPause() {
        super.onPause();
        KeyBoardUtils.closeKeybord(this.ctx, this.et_search);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.r);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mHistoryList != null) {
            this.mHistoryList.clear();
        }
        if (this.mResultList != null) {
            this.mResultList.clear();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case 175:
                    String contents = data.getStringExtra(ScannerActivity.CODE_DATA);
                    if (contents != null) {
                        searchFoodWithCode(contents);
                        break;
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchFoodWithCode(final String code) {
        if (!TextUtils.isEmpty(code)) {
            FoodApi.getFoodWithBarcode(code, this.activity, new JsonCallback(this.activity) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    List<FoodInfo> foods = FastJsonUtils.parseList(object.optString("foods"),
                            FoodInfo.class);
                    if (foods == null || foods.size() == 0) {
                        SearchFoodActivity.this.showUploadDialog(code);
                        return;
                    }
                    FoodInfo foodInfo = (FoodInfo) foods.get(0);
                    if (TextUtils.isEmpty(foodInfo.code)) {
                        SearchFoodActivity.this.showUploadDialog(code);
                    } else {
                        SearchFoodActivity.this.loadFoodWithCode(foodInfo.code);
                    }
                }
            });
        }
    }

    private void loadFoodWithCode(String code) {
        if (this.isAddDiet) {
            Fragment addDietFragment = AddDietFragment.newInstance(this.mTimeType, this
                    .record_on, code);
            addDietFragment.setChangeListener(new onChangeListener() {
                public void onFinish() {
                    SearchFoodActivity.this.finish();
                }
            });
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(addDietFragment, "addDietFragment");
            transaction.commitAllowingStateLoss();
            return;
        }
        FoodDetailActivity.comeOnBaby(this.ctx, code, true);
    }

    private void showUploadDialog(final String code) {
        LightAlertDialog.create(this.ctx, (int) R.string.ab8).setNegativeButton((int) R.string
                .ab9, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton((int) R.string.ab_, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MobclickAgent.onEvent(SearchFoodActivity.this.ctx, Event.tool_searchfood_assistadd);
                UploadFoodActivity.comeOnBabyWithCode(SearchFoodActivity.this.ctx, code);
            }
        }).show();
    }
}
