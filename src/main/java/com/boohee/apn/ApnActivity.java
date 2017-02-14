package com.boohee.apn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

public class ApnActivity extends GestureActivity {
    private static final String         IS_ASK       = "is_ask";
    private static final String         QUESTION_URL = "http://shop.boohee.com/store/pages/faq";
    private static final CharSequence[] TITLE        = new CharSequence[]{"联系客服", "常见问题"};
    private boolean            isAsk;
    private ApnFragmentAdapter mAdapter;
    private FragmentApn        mApn;
    private List<Fragment> mFragmentList = new ArrayList();
    private TabLayout tableLayout;
    private ViewPager viewPager;

    private class ApnFragmentAdapter extends FragmentPagerAdapter {
        public ApnFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return (Fragment) ApnActivity.this.mFragmentList.get(position);
        }

        public int getCount() {
            return ApnActivity.TITLE.length;
        }

        public CharSequence getPageTitle(int position) {
            return ApnActivity.TITLE[position];
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ae);
        this.isAsk = getIntent().getBooleanExtra(IS_ASK, true);
        this.mApn = new FragmentApn();
        this.mFragmentList.add(this.mApn);
        this.mFragmentList.add(FragmentBrowser.newInstance(QUESTION_URL));
        this.tableLayout = (TabLayout) findViewById(R.id.tableLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.mAdapter = new ApnFragmentAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.mAdapter);
        this.tableLayout.setupWithViewPager(this.viewPager);
        this.tableLayout.setTabMode(1);
        ViewPager viewPager = this.viewPager;
        if (this.isAsk) {
            i = 0;
        } else {
            i = 1;
        }
        viewPager.setCurrentItem(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mApn != null) {
            this.mApn.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void comeOnBaby(Context context, boolean isAsk) {
        if (context != null) {
            Intent intent = new Intent(context, ApnActivity.class);
            intent.putExtra(IS_ASK, isAsk);
            context.startActivity(intent);
        }
    }
}
