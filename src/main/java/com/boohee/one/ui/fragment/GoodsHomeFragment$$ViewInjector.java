package com.boohee.one.ui.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;
import com.viewpagerindicator.IconPageIndicator;

import de.hdodenhof.circleimageview.CircleImageView;

public class GoodsHomeFragment$$ViewInjector<T extends GoodsHomeFragment> implements Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.tv_title = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tv_title'"), R.id.tv_title, "field 'tv_title'");
        target.tv_price = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_price, "field 'tv_price'"), R.id.tv_price, "field 'tv_price'");
        target.tv_market_title = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_market_title, "field 'tv_market_title'"), R.id.tv_market_title,
                "field 'tv_market_title'");
        target.tv_market_price = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_market_price, "field 'tv_market_price'"), R.id.tv_market_price,
                "field 'tv_market_price'");
        target.tv_month_quantity = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_month_quantity, "field 'tv_month_quantity'"), R.id
                .tv_month_quantity, "field 'tv_month_quantity'");
        target.tv_good_title = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_good_title, "field 'tv_good_title'"), R.id.tv_good_title, "field " +
                "'tv_good_title'");
        target.viewpager = (ViewPager) finder.castView((View) finder.findRequiredView(source, R
                .id.viewpager, "field 'viewpager'"), R.id.viewpager, "field 'viewpager'");
        target.indicator = (IconPageIndicator) finder.castView((View) finder.findRequiredView
                (source, R.id.indicator, "field 'indicator'"), R.id.indicator, "field 'indicator'");
        target.iv_arrow_right = (ImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_arrow_right, "field 'iv_arrow_right'"), R.id.iv_arrow_right,
                "field 'iv_arrow_right'");
        target.tv_format = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_format, "field 'tv_format'"), R.id.tv_format, "field 'tv_format'");
        target.view_divide_format = (View) finder.findRequiredView(source, R.id
                .view_divide_format, "field 'view_divide_format'");
        View view = (View) finder.findRequiredView(source, R.id.view_format_tips, "field " +
                "'view_format_tips' and method 'onClick'");
        target.view_format_tips = view;
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.tvDescription = (TextView) finder.castView((View) finder.findRequiredView(source,
                R.id.tv_description, "field 'tvDescription'"), R.id.tv_description, "field " +
                "'tvDescription'");
        target.tvSale = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_sale, "field 'tvSale'"), R.id.tv_sale, "field 'tvSale'");
        target.tvEvaluateCount = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.tv_evaluate_count, "field 'tvEvaluateCount'"), R.id
                .tv_evaluate_count, "field 'tvEvaluateCount'");
        target.avatar = (CircleImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.avatar, "field 'avatar'"), R.id.avatar, "field 'avatar'");
        target.tvNickname = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_nickname, "field 'tvNickname'"), R.id.tv_nickname, "field 'tvNickname'");
        target.tvPostTime = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_post_time, "field 'tvPostTime'"), R.id.tv_post_time, "field 'tvPostTime'");
        target.tvEvaluate = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_evaluate, "field 'tvEvaluate'"), R.id.tv_evaluate, "field 'tvEvaluate'");
        target.viewEvaluate = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.view_evaluate, "field 'viewEvaluate'"), R.id.view_evaluate, "field " +
                "'viewEvaluate'");
        ((View) finder.findRequiredView(source, R.id.view_more, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.tv_title = null;
        target.tv_price = null;
        target.tv_market_title = null;
        target.tv_market_price = null;
        target.tv_month_quantity = null;
        target.tv_good_title = null;
        target.viewpager = null;
        target.indicator = null;
        target.iv_arrow_right = null;
        target.tv_format = null;
        target.view_divide_format = null;
        target.view_format_tips = null;
        target.tvDescription = null;
        target.tvSale = null;
        target.tvEvaluateCount = null;
        target.avatar = null;
        target.tvNickname = null;
        target.tvPostTime = null;
        target.tvEvaluate = null;
        target.viewEvaluate = null;
    }
}
