package com.boohee.nice;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.api.ShopApi;
import com.boohee.main.GestureActivity;
import com.boohee.myview.MultiViewPager;
import com.boohee.nice.fragment.NiceGoodsFragment;
import com.boohee.nice.model.NiceServices;
import com.boohee.nice.model.NiceServices.ServicesBean;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.FastJsonUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

public class NiceSellActivity extends GestureActivity {
    public static final String        KEY_NICE_TYPE      = "KEY_NICE_TYPE";
    public static final String        NICE_SERVICE       = "nice_plus";
    public static final String        RENEW_NICE_SERVICE = "renice_plus";
    public static final int[]         pager_colors       = new int[]{R.color.go, R.color.gq, R
            .color.gp};
    private             ArgbEvaluator argbEvaluator      = new ArgbEvaluator();
    private GoodsPagerAdapter goodsAdapter;
    @InjectView(2131427825)
    CirclePageIndicator indicator;
    private NiceServices niceServices;
    private String type = "";
    @InjectView(2131427463)
    MultiViewPager viewpager;

    public class GoodsPagerAdapter extends FragmentPagerAdapter {
        NiceServices niceServices;

        public GoodsPagerAdapter(NiceServices niceServices, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.niceServices = niceServices;
        }

        public int getCount() {
            return this.niceServices.services == null ? 0 : this.niceServices.services.size();
        }

        public Fragment getItem(int position) {
            return NiceGoodsFragment.newInstance((ServicesBean) this.niceServices.services.get
                    (position), NiceSellActivity.pager_colors[position % NiceSellActivity
                    .pager_colors.length], NiceSellActivity.this.type);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(View.inflate(this, R.layout.cd, null));
        ButterKnife.inject((Activity) this);
        handleIntent();
        initToolBar();
        requestNiceServiceList();
    }

    private void handleIntent() {
        this.type = getIntent().getStringExtra(KEY_NICE_TYPE);
    }

    private void initToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBarColor(17170445);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    public void setToolBarColor(final int color) {
        new Handler().post(new Runnable() {
            public void run() {
                NiceSellActivity.this.toolbar.setBackgroundColor(ContextCompat.getColor
                        (NiceSellActivity.this.ctx, color));
            }
        });
    }

    private void requestNiceServiceList() {
        ShopApi.getNiceServiceList(this.type, this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                NiceSellActivity.this.niceServices = (NiceServices) FastJsonUtils.fromJson
                        (object, NiceServices.class);
                if (NiceSellActivity.this.niceServices != null) {
                    NiceSellActivity.this.initViewPager();
                }
            }
        });
    }

    private void initViewPager() {
        this.goodsAdapter = new GoodsPagerAdapter(this.niceServices, getSupportFragmentManager());
        this.viewpager.setAdapter(this.goodsAdapter);
        this.indicator.setViewPager(this.viewpager);
        this.indicator.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                NiceSellActivity.this.viewpager.setBackgroundColor(((Integer) NiceSellActivity
                        .this.argbEvaluator.evaluate(positionOffset, Integer.valueOf
                        (NiceSellActivity.this.getResources().getColor(NiceSellActivity
                                .pager_colors[position % NiceSellActivity.pager_colors.length])),
                        Integer.valueOf(NiceSellActivity.this.getResources().getColor
                                (NiceSellActivity.pager_colors[(position + 1) % NiceSellActivity
                                        .pager_colors.length])))).intValue());
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public static void startActivity(Context context, String type) {
        Intent i = new Intent(context, NiceSellActivity.class);
        i.putExtra(KEY_NICE_TYPE, type);
        context.startActivity(i);
    }
}
