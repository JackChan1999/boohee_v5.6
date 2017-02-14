package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.ShopApi;
import com.boohee.model.Label;
import com.boohee.model.ShopList;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.adapter.BaseFragmentPagerAdapter;
import com.boohee.one.ui.fragment.ShopCategoryFragment;
import com.boohee.utils.FastJsonUtils;
import com.boohee.widgets.PagerSlidingTabStrip;

import java.util.List;

import org.json.JSONObject;

public class ShopCategoryActivity extends BaseActivity {
    public static final String EXTRA_LABEL_id = "extra_label_id";
    private int labelId;
    @InjectView(2131427462)
    PagerSlidingTabStrip mSlidingTab;
    @InjectView(2131427463)
    ViewPager            mViewPager;

    public static void start(Context context, int label_id) {
        Intent starter = new Intent(context, ShopCategoryActivity.class);
        starter.putExtra("extra_label_id", label_id);
        context.startActivity(starter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cz);
        ButterKnife.inject((Activity) this);
        handleIntent();
        requestLabelList();
    }

    private void initViewPager(ShopList shopList) {
        if (shopList != null) {
            if (!TextUtils.isEmpty(shopList.name)) {
                setTitle(shopList.name);
            }
            List<Label> labels = shopList.sub_labels;
            if (labels != null && labels.size() > 0) {
                final BaseFragmentPagerAdapter fragmentAdapter = new BaseFragmentPagerAdapter
                        (getSupportFragmentManager());
                for (int i = 0; i < labels.size(); i++) {
                    Label label = (Label) labels.get(i);
                    if (i == 0) {
                        fragmentAdapter.addFragment(ShopCategoryFragment.newInstance(this
                                .labelId, label.id, shopList), label.name);
                    } else {
                        fragmentAdapter.addFragment(ShopCategoryFragment.newInstance(this
                                .labelId, label.id), label.name);
                    }
                }
                this.mViewPager.setAdapter(fragmentAdapter);
                this.mViewPager.setOffscreenPageLimit(labels.size());
                this.mSlidingTab.setViewPager(this.mViewPager);
                if (labels != null && labels.size() <= 1) {
                    this.mSlidingTab.setVisibility(8);
                } else if (labels != null && labels.size() > 1) {
                    this.mSlidingTab.setVisibility(0);
                }
                this.mSlidingTab.setOnPageChangeListener(new SimpleOnPageChangeListener() {
                    public void onPageSelected(int position) {
                        ShopCategoryFragment shopLabelFragment = (ShopCategoryFragment)
                                fragmentAdapter.getItem(position);
                        if (position == 0 && shopLabelFragment.isFirstLoad) {
                            shopLabelFragment.loadAll();
                        } else if (shopLabelFragment.isFirstLoad) {
                            shopLabelFragment.loadFirst();
                        }
                    }
                });
            }
        }
    }

    private void requestLabelList() {
        if (this.labelId != -1) {
            ShopApi.getCatetgories(this.labelId, this.activity, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    ShopCategoryActivity.this.initViewPager((ShopList) FastJsonUtils.fromJson
                            (object, ShopList.class));
                }

                public void onFinish() {
                    super.onFinish();
                }
            });
        }
    }

    private void handleIntent() {
        this.labelId = getIntent().getIntExtra("extra_label_id", -1);
    }
}
