package com.boohee.widgets;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

import com.boohee.one.R;

public class SportDialogBuilder$$ViewInjector<T extends SportDialogBuilder> implements Injector<T> {
    public void inject(Finder finder, T target, Object source) {
        target.tvTitle = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_title, "field 'tvTitle'"), R.id.tv_title, "field 'tvTitle'");
        target.tvTime = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_time, "field 'tvTime'"), R.id.tv_time, "field 'tvTime'");
        target.tvCalory = (TextView) finder.castView((View) finder.findRequiredView(source, R.id
                .tv_calory, "field 'tvCalory'"), R.id.tv_calory, "field 'tvCalory'");
        target.llTop = (RelativeLayout) finder.castView((View) finder.findRequiredView(source, R
                .id.ll_top, "field 'llTop'"), R.id.ll_top, "field 'llTop'");
        target.btnNeg = (Button) finder.castView((View) finder.findRequiredView(source, R.id
                .btn_neg, "field 'btnNeg'"), R.id.btn_neg, "field 'btnNeg'");
        target.imgLine = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .img_line, "field 'imgLine'"), R.id.img_line, "field 'imgLine'");
        target.btnPos = (Button) finder.castView((View) finder.findRequiredView(source, R.id
                .btn_pos, "field 'btnPos'"), R.id.btn_pos, "field 'btnPos'");
        target.ivIcon = (ImageView) finder.castView((View) finder.findRequiredView(source, R.id
                .iv_icon, "field 'ivIcon'"), R.id.iv_icon, "field 'ivIcon'");
        target.llBg = (LinearLayout) finder.castView((View) finder.findRequiredView(source, R.id
                .ll_bg, "field 'llBg'"), R.id.ll_bg, "field 'llBg'");
        target.ivBackground = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_bg, "field 'ivBackground'"), R.id.iv_bg, "field 'ivBackground'");
    }

    public void reset(T target) {
        target.tvTitle = null;
        target.tvTime = null;
        target.tvCalory = null;
        target.llTop = null;
        target.btnNeg = null;
        target.imgLine = null;
        target.btnPos = null;
        target.ivIcon = null;
        target.llBg = null;
        target.ivBackground = null;
    }
}
