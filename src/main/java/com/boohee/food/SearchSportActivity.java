package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.boohee.api.FoodApi;
import com.boohee.database.OnePreference;
import com.boohee.model.RecordSport;
import com.boohee.model.Sport;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.fragment.AddSportFragment;
import com.boohee.one.ui.fragment.BaseDialogFragment.onChangeListener;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class SearchSportActivity extends BaseActivity implements OnItemClickListener,
        OnScrollListener {
    private static final String KEY_DATE  = "key_date";
    private static final String SEPARATOR = "##";
    EditText  et_search;
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
    private String                 mQueryString;
    private SearchSportViewAdapter mResultAdapter;
    private List<Sport> mResultList = new ArrayList();
    private int         mTotalcount = 0;
    private String record_on;
    @InjectView(2131427567)
    ScrollView sv_search;
    @InjectView(2131427573)
    TextView   tv_null;

    private class SearchOnEditorActionListener implements OnEditorActionListener {
        private SearchOnEditorActionListener() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            SearchSportActivity.this.doSearch();
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
            if (TextUtils.isEmpty(s.toString())) {
                SearchSportActivity.this.iv_clear.setVisibility(8);
            } else {
                SearchSportActivity.this.iv_clear.setVisibility(0);
            }
        }
    }

    @OnClick({2131427570, 2131427863})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_history_clear:
                this.ll_history.setVisibility(8);
                OnePreference.getInstance(this.ctx).clearSportHistory();
                return;
            case R.id.tv_search_null:
                AddCustomSportActivity.start(this.activity, this.record_on);
                return;
            default:
                return;
        }
    }

    public static void start(Context context, String record_on) {
        Intent starter = new Intent(context, SearchSportActivity.class);
        starter.putExtra("key_date", record_on);
        context.startActivity(starter);
        ((Activity) context).overridePendingTransition(R.anim.o, R.anim.l);
    }

    private void handleIntent() {
        this.record_on = getIntent().getStringExtra("key_date");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d8);
        ButterKnife.inject((Activity) this);
        initToolsBar();
        handleIntent();
        addListener();
        loadHistoryData();
    }

    private void initToolsBar() {
        this.mCustomView = LayoutInflater.from(this).inflate(R.layout.mo, null);
        this.et_search = (EditText) this.mCustomView.findViewById(R.id.et_search);
        this.iv_clear = (ImageView) this.mCustomView.findViewById(R.id.view_clear);
        this.iv_search = (ImageView) this.mCustomView.findViewById(R.id.view_scan);
        this.et_search.setHint(R.string.a3g);
        this.iv_search.setImageDrawable(getResources().getDrawable(R.drawable.a55));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(this.mCustomView, new LayoutParams(-1, -1));
    }

    private void addListener() {
        this.lv_result.setOnItemClickListener(this);
        this.lv_result.setOnScrollListener(this);
        this.et_search.addTextChangedListener(new SearchTextWatcher());
        this.et_search.setOnEditorActionListener(new SearchOnEditorActionListener());
        this.iv_search.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchSportActivity.this.doSearch();
            }
        });
        this.iv_clear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchSportActivity.this.et_search.setText("");
                SearchSportActivity.this.ll_result.setVisibility(8);
                SearchSportActivity.this.sv_search.setVisibility(0);
                SearchSportActivity.this.loadHistoryData();
            }
        });
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
        try {
            String encodeString = URLEncoder.encode(queryString, "UTF-8");
            if (!TextUtils.isEmpty(encodeString)) {
                showLoading();
                FoodApi.getActivitiesSearch(this, encodeString, this.mPage, new JsonCallback(this) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        MobclickAgent.onEvent(SearchSportActivity.this.activity, Event
                                .TOOL_FOODANDSPORT_SPORTSEARCH);
                        SearchSportActivity.this.refreshData(FastJsonUtils.parseList(object
                                .optString("activities"), Sport.class));
                    }

                    public void onFinish() {
                        SearchSportActivity.this.dismissLoading();
                        KeyBoardUtils.closeKeybord(SearchSportActivity.this.ctx,
                                SearchSportActivity.this.et_search);
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void refreshData(List<Sport> foodList) {
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
            this.mResultAdapter = new SearchSportViewAdapter(this.ctx, this.mResultList);
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
        String historyString = OnePreference.getInstance(this.ctx).getSportHistory();
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
                        SearchSportActivity.this.mQueryString = title;
                        SearchSportActivity.this.doFastSearch(SearchSportActivity.this
                                .mQueryString);
                    }
                });
                this.ll_history_content.addView(view);
            }
        }
    }

    private void saveSearchHistory(String keyString) {
        String historyString = OnePreference.getInstance(this.ctx).getSportHistory();
        if (TextUtils.isEmpty(historyString)) {
            OnePreference.getInstance(this.ctx).setSportHistory(keyString + SEPARATOR);
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
            OnePreference.getInstance(this.ctx).setSportHistory(historyString);
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

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!ViewUtils.isFastDoubleClick()) {
            Sport sport = (Sport) this.mResultAdapter.getItem(position);
            RecordSport recordSport = new RecordSport();
            recordSport.mets = Float.parseFloat(sport.mets);
            recordSport.activity_name = sport.name;
            recordSport.activity_id = sport.id;
            recordSport.thumb_img_url = sport.big_photo_url;
            recordSport.record_on = this.record_on;
            AddSportFragment addSportFragment = AddSportFragment.newInstance(0, recordSport);
            addSportFragment.show(getSupportFragmentManager(), "addSportFragment");
            addSportFragment.setChangeListener(new onChangeListener() {
                public void onFinish() {
                    SearchSportActivity.this.finish();
                }
            });
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
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
}
