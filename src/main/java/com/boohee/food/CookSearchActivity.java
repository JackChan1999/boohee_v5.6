package com.boohee.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.boohee.main.GestureActivity;
import com.boohee.model.FoodWithUnit;
import com.boohee.myview.NewBadgeView;
import com.boohee.one.R;
import com.boohee.one.event.CustomCookEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.fragment.AddMaterialFragment;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.KeyBoardUtils;
import com.boohee.utils.ViewUtils;

import de.greenrobot.event.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class CookSearchActivity extends GestureActivity implements OnItemClickListener,
        OnScrollListener {
    private static final String SEPARATOR = "##";
    EditText et_search;
    private List<String> hotSearchList = new ArrayList();
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
    private int          mAddCount;
    private NewBadgeView mCountBadge;
    private int mCurrentPage = 1;
    private View mCustomView;
    private List<String> mHistoryList = new ArrayList();
    private int          mLastindex   = 0;
    private int          mPage        = 1;
    private String            mQueryString;
    private SearchViewAdapter mResultAdapter;
    private List<FoodWithUnit> mResultList = new ArrayList();
    private int                mTotalcount = 0;
    @InjectView(2131427567)
    ScrollView sv_search;
    @InjectView(2131427573)
    TextView   tv_null;
    @InjectView(2131427574)
    TextView   tv_search_tip;

    private class SearchOnEditorActionListener implements OnEditorActionListener {
        private SearchOnEditorActionListener() {
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            CookSearchActivity.this.doSearch();
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
                CookSearchActivity.this.iv_clear.setVisibility(8);
                CookSearchActivity.this.tv_search_tip.setVisibility(8);
                CookSearchActivity.this.sv_search.setVisibility(0);
                CookSearchActivity.this.ll_result.setVisibility(8);
                return;
            }
            CookSearchActivity.this.iv_clear.setVisibility(0);
            CookSearchActivity.this.tv_search_tip.setVisibility(0);
            CookSearchActivity.this.sv_search.setVisibility(8);
            CookSearchActivity.this.tv_search_tip.setText(String.format("搜索 “%s”", new Object[]{s
                    .toString()}));
            CookSearchActivity.this.tv_null.setVisibility(8);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ax);
        ButterKnife.inject((Activity) this);
        EventBus.getDefault().register(this);
        initToolsBar();
        addListener();
        loadHistoryData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.j, menu);
        new Handler().post(new Runnable() {
            public void run() {
                CookSearchActivity.this.mCountBadge = new NewBadgeView(CookSearchActivity.this
                        .activity);
                CookSearchActivity.this.mCountBadge.setTargetView(CookSearchActivity.this
                        .findViewById(R.id.action_finish));
                CookSearchActivity.this.mCountBadge.setBadgeMargin(10, 5, 5, 0);
                CookSearchActivity.this.mCountBadge.setBadgeGravity(53);
                CookSearchActivity.this.mCountBadge.setTextColor(ContextCompat.getColor
                        (CookSearchActivity.this.ctx, R.color.ju));
                CookSearchActivity.this.mCountBadge.setBackground(10, ContextCompat.getColor
                        (CookSearchActivity.this.ctx, R.color.he));
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({2131427570, 2131427574})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_history_clear:
                this.ll_history.setVisibility(8);
                OnePreference.clearCookFoodSearchHistory();
                return;
            case R.id.search_tip:
                this.tv_search_tip.setVisibility(8);
                doSearch();
                return;
            default:
                return;
        }
    }

    private void initToolsBar() {
        this.mCustomView = LayoutInflater.from(this).inflate(R.layout.mo, null);
        this.et_search = (EditText) this.mCustomView.findViewById(R.id.et_search);
        this.iv_clear = (ImageView) this.mCustomView.findViewById(R.id.view_clear);
        this.iv_search = (ImageView) this.mCustomView.findViewById(R.id.view_scan);
        this.iv_search.setVisibility(8);
        this.et_search.setHint("请输入食材名称");
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(this.mCustomView, new LayoutParams(-1, -1));
    }

    private void addListener() {
        this.lv_result.setOnItemClickListener(this);
        this.lv_result.setOnScrollListener(this);
        this.et_search.addTextChangedListener(new SearchTextWatcher());
        this.et_search.setOnEditorActionListener(new SearchOnEditorActionListener());
        this.iv_clear.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CookSearchActivity.this.et_search.setText("");
                CookSearchActivity.this.ll_result.setVisibility(8);
                CookSearchActivity.this.sv_search.setVisibility(0);
            }
        });
    }

    private void loadHistoryData() {
        String historyString = OnePreference.getPrefCookFoodSearch();
        if (historyString == null && TextUtils.isEmpty(historyString)) {
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
                FoodApi.getFoodsMaterialSearch(this, encodeString, this.mPage, new JsonCallback
                        (this) {
                    public void ok(JSONObject object) {
                        super.ok(object);
                        CookSearchActivity.this.refreshData(FastJsonUtils.parseList(object
                                .optString("foods"), FoodWithUnit.class));
                    }

                    public void onFinish() {
                        CookSearchActivity.this.dismissLoading();
                        KeyBoardUtils.closeKeybord(CookSearchActivity.this.ctx,
                                CookSearchActivity.this.et_search);
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

    private void initHistory() {
        if (this.mHistoryList != null) {
            this.ll_history_content.removeAllViews();
            for (int i = 0; i < this.mHistoryList.size(); i++) {
                final String title = (String) this.mHistoryList.get(i);
                View view = LayoutInflater.from(this.ctx).inflate(R.layout.ir, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CookSearchActivity.this.mQueryString = title;
                        CookSearchActivity.this.doFastSearch(CookSearchActivity.this.mQueryString);
                    }
                });
                this.ll_history_content.addView(view);
            }
        }
    }

    private void saveSearchHistory(String keyString) {
        String historyString = OnePreference.getPrefCookFoodSearch();
        if (TextUtils.isEmpty(historyString)) {
            OnePreference.setPrefCookFoodSearch(keyString + SEPARATOR);
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
            OnePreference.setPrefCookFoodSearch(historyString);
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

    public static void comeOn(Context context) {
        context.startActivity(new Intent(context, CookSearchActivity.class));
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!ViewUtils.isFastDoubleClick() && this.activity != null) {
            switch (parent.getId()) {
                case R.id.lv_result:
                    Fragment addMaterialFragment = AddMaterialFragment.newInstance((
                            (FoodWithUnit) this.mResultAdapter.getItem(position)).code);
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    transaction.add(addMaterialFragment, "addMaterialFragment");
                    transaction.commitAllowingStateLoss();
                    return;
                default:
                    return;
            }
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

    public void onEventMainThread(CustomCookEvent cookEvent) {
        this.mAddCount++;
        this.mCountBadge.setBadgeCount(this.mAddCount);
        this.mCountBadge.setVisibility(this.mAddCount > 0 ? 0 : 8);
    }
}
