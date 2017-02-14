package com.boohee.one.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.boohee.one.R;
import com.boohee.one.cache.CacheKey;
import com.boohee.one.event.RefreshPostEvent;
import com.boohee.one.event.StatusUnreadCount;
import com.boohee.one.ui.MainActivity;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.utility.BuilderIntent;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PartnerFragment extends BaseFragment {
    public static final int DISCOVER = 0;
    public static final int FRIEND   = 1;
    private PartnerFragmentAdapter mAdapter;
    private FloatingActionButton   mFabButton;
    private List<Fragment> mFragmentList = new ArrayList();
    private TabLayout mTabLayout;
    private List<String> mTitleList = new ArrayList();
    private ViewPager mViewPager;

    public interface RefreshListener {
        void onRefresh();
    }

    public interface ShowHintListener {
        void showHint(String str);
    }

    private class PartnerFragmentAdapter extends FragmentPagerAdapter {
        public PartnerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return (Fragment) PartnerFragment.this.mFragmentList.get(position);
        }

        public int getCount() {
            return PartnerFragment.this.mFragmentList.size();
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) PartnerFragment.this.mTitleList.get(position);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.g9, null);
        this.mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        this.mFabButton = (FloatingActionButton) view.findViewById(R.id.fab_button);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mTabLayout = ((MainActivity) getActivity()).getPartnerTab();
        this.mTitleList.add("精选");
        this.mTitleList.add("好友圈");
        this.mFabButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!PartnerFragment.this.isDetached()) {
                    new BuilderIntent(PartnerFragment.this.getActivity(), StatusPostTextActivity
                            .class).startActivity();
                }
            }
        });
        this.mAdapter = new PartnerFragmentAdapter(getActivity().getSupportFragmentManager());
        this.mFragmentList.add(new PartnerDiscoverFragment());
        this.mFragmentList.add(new HomeTimelineFragment());
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                PartnerFragment.this.mCache.put(CacheKey.HOME_STATUS_TAB, String.valueOf(position));
            }
        });
        this.mTabLayout.setupWithViewPager(this.mViewPager);
        this.mTabLayout.setTabMode(1);
        EventBus.getDefault().register(this);
        this.mViewPager.post(new Runnable() {
            public void run() {
                try {
                    PartnerFragment.this.mViewPager.setCurrentItem(Integer.parseInt
                            (PartnerFragment.this.mCache.getAsString(CacheKey.HOME_STATUS_TAB)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void loadFirst() {
        if (this.mFragmentList != null && this.mViewPager != null) {
            ((RefreshListener) this.mFragmentList.get(this.mViewPager.getCurrentItem()))
                    .onRefresh();
        }
    }

    public void onEventMainThread(RefreshPostEvent event) {
        if (this.mViewPager.getCurrentItem() == 0) {
            this.mViewPager.setCurrentItem(1);
        }
        loadFirst();
    }

    public void onEventMainThread(StatusUnreadCount event) {
        String friend = event.friend_posts_count;
        if (!TextUtils.isEmpty(friend)) {
            ((ShowHintListener) this.mFragmentList.get(1)).showHint(friend);
        }
    }
}
