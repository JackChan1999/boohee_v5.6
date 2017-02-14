package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.TextUtils;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.StatusFavoriteFragment;
import com.boohee.one.ui.fragment.WebFavoriteFragment;
import com.boohee.utils.FileUtil;
import com.boohee.utils.Helper;
import com.boohee.widgets.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteActivity extends GestureActivity {
    public static final String TAG = MyFavoriteActivity.class.getSimpleName();
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList();
    String imageUrl;
    @InjectView(2131427462)
    PagerSlidingTabStrip slidingTabs;
    StatusFavoriteFragment statusFavoriteFragment;
    @InjectView(2131427463)
    ViewPager viewpager;
    WebFavoriteFragment webFavoriteFragment;

    class CollectionPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        private String[] mTitles = new String[]{"动态", "文章"};

        public CollectionPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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

    private class SaveImageTask extends AsyncTask<Bitmap, String, String> {
        private SaveImageTask() {
        }

        protected String doInBackground(Bitmap... params) {
            if (params[0] == null || TextUtils.isEmpty(MyFavoriteActivity.this.imageUrl)) {
                return null;
            }
            return FileUtil.downloadImage2Gallery(MyFavoriteActivity.this.ctx, params[0], new
                    Md5FileNameGenerator().generate(MyFavoriteActivity.this.imageUrl));
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (TextUtils.isEmpty(result)) {
                Helper.showToast((CharSequence) "保存图片失败，请重新保存~~");
            } else {
                Helper.showToast("图片已保存到" + result);
            }
        }
    }

    public static void comeOnBaby(Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, MyFavoriteActivity.class));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c6);
        ButterKnife.inject((Activity) this);
        setTitle("我的收藏");
        initView();
    }

    private void loadBooheePic() {
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    MyFavoriteActivity.this.imageUrl = String.format("http://up.boohee" +
                            ".cn/house/u/one/reminder/%d.jpg", new Object[]{Integer.valueOf(i)});
                    MyFavoriteActivity.this.imageLoader.loadImage(MyFavoriteActivity.this
                            .imageUrl, new SimpleImageLoadingListener() {
                        public void onLoadingComplete(String imageUri, View view, Bitmap
                                loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            new SaveImageTask().execute(new Bitmap[]{loadedImage});
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initView() {
        initFragments();
        this.adapter = new CollectionPagerAdapter(getSupportFragmentManager(), this.fragmentList);
        this.viewpager.setAdapter(this.adapter);
        this.viewpager.setOffscreenPageLimit(2);
        this.slidingTabs.setViewPager(this.viewpager);
        this.slidingTabs.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                if (position == 1 && MyFavoriteActivity.this.webFavoriteFragment != null &&
                        MyFavoriteActivity.this.webFavoriteFragment.isFirstLoad()) {
                    MyFavoriteActivity.this.webFavoriteFragment.pullToRefresh();
                    MyFavoriteActivity.this.webFavoriteFragment.setIsFirstLoad(false);
                }
                super.onPageSelected(position);
            }
        });
    }

    private void initFragments() {
        this.webFavoriteFragment = new WebFavoriteFragment();
        this.statusFavoriteFragment = new StatusFavoriteFragment();
        this.fragmentList.add(this.statusFavoriteFragment);
        this.fragmentList.add(this.webFavoriteFragment);
    }
}
