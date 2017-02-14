package com.boohee.myview;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

public class HomePopView$$ViewInjector<T extends HomePopView> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        View view = (View) finder.findRequiredView(source, R.id.iv_wallpaper, "field " +
                "'ivWallpaper' and method 'onClick'");
        target.ivWallpaper = (ImageView) finder.castView(view, R.id.iv_wallpaper, "field " +
                "'ivWallpaper'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_share, "field 'ivShare' and method " +
                "'onClick'");
        target.ivShare = (ImageView) finder.castView(view, R.id.iv_share, "field 'ivShare'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        view = (View) finder.findRequiredView(source, R.id.iv_download, "field 'ivDownload' and " +
                "method 'onClick'");
        target.ivDownload = (ImageView) finder.castView(view, R.id.iv_download, "field " +
                "'ivDownload'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.llDownloadShare = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_download_share, "field 'llDownloadShare'"), R.id
                .ll_download_share, "field 'llDownloadShare'");
        target.homePopSlidingLayout = (HomePopSlidingLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.sl_top, "field 'homePopSlidingLayout'"), R.id
                .sl_top, "field 'homePopSlidingLayout'");
        view = (View) finder.findRequiredView(source, R.id.btn_wall_left, "field 'btnWallLeft' " +
                "and method 'onClick'");
        target.btnWallLeft = (ImageView) finder.castView(view, R.id.btn_wall_left, "field " +
                "'btnWallLeft'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvWallDate = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_wall_date, "field 'tvWallDate'"), R.id.tv_wall_date, "field 'tvWallDate'");
        view = (View) finder.findRequiredView(source, R.id.btn_wall_right, "field 'btnWallRight' " +
                "and method 'onClick'");
        target.btnWallRight = (ImageView) finder.castView(view, R.id.btn_wall_right, "field " +
                "'btnWallRight'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.llWallTitle = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.ll_wall_title, "field 'llWallTitle'"), R.id.ll_wall_title, "field " +
                "'llWallTitle'");
    }

    public void reset(T target) {
        target.viewpager = null;
        target.ivWallpaper = null;
        target.ivShare = null;
        target.ivDownload = null;
        target.llDownloadShare = null;
        target.homePopSlidingLayout = null;
        target.btnWallLeft = null;
        target.tvWallDate = null;
        target.btnWallRight = null;
        target.llWallTitle = null;
    }
}
