package com.boohee.status;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;

import com.boohee.one.R;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.fragment.FansFragment;
import com.boohee.one.ui.fragment.FriendFragment;
import com.boohee.utility.Event;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class FriendShipActivity extends BaseActivity {
    public static final String FRIENDSHIP_POSITION = "position";
    static final        String TAG                 = FriendShipActivity.class.getSimpleName();
    private List<Fragment>       mContentFragments;
    private View                 mCustomView;
    private PagerSlidingTabStrip mSlidingTab;
    private ViewPager            mViewPager;
    private int                  position;

    class MainPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        private String[] mTitles = new String[]{"好友", "粉丝"};

        public MainPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragments.get(position);
        }

        public int getCount() {
            return this.mFragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return this.mTitles[position];
        }
    }

    public void onCreate(Bundle ousState) {
        super.onCreate(ousState);
        setContentView(R.layout.gx);
        setTitle("好友&粉丝");
        MobclickAgent.onEvent(this, Event.STATUS_VIEW_FRIEND_PAGE);
        this.position = getIntent().getIntExtra(FRIENDSHIP_POSITION, 0);
        initFragments();
        initView();
    }

    private void initView() {
        this.mViewPager = (ViewPager) findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this
                .mContentFragments));
        this.mCustomView = LayoutInflater.from(this).inflate(R.layout.lz, null);
        this.mSlidingTab = (PagerSlidingTabStrip) this.mCustomView.findViewById(R.id.sliding_tabs);
        this.mSlidingTab.setViewPager(this.mViewPager);
        this.mViewPager.setCurrentItem(this.position);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        layoutParams.gravity = GravityCompat.END;
        getSupportActionBar().setCustomView(this.mCustomView, layoutParams);
    }

    private void initFragments() {
        this.mContentFragments = new ArrayList();
        this.mContentFragments.add(new FriendFragment());
        this.mContentFragments.add(new FansFragment());
    }
}
