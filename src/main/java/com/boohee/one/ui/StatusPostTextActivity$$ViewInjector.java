package com.boohee.one.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

import com.boohee.one.R;
import com.viewpagerindicator.CirclePageIndicator;

public class StatusPostTextActivity$$ViewInjector<T extends StatusPostTextActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.viewPagerEmoji = (ViewPager) finder.castView((View) finder.findRequiredView
                (source, R.id.view_pager_emoji, "field 'viewPagerEmoji'"), R.id.view_pager_emoji,
                "field 'viewPagerEmoji'");
        target.indicatorEmoji = (CirclePageIndicator) finder.castView((View) finder
                .findRequiredView(source, R.id.indicator_emoji, "field 'indicatorEmoji'"), R.id
                .indicator_emoji, "field 'indicatorEmoji'");
        target.lyEmoji = (KPSwitchPanelLinearLayout) finder.castView((View) finder
                .findRequiredView(source, R.id.ly_emoji, "field 'lyEmoji'"), R.id.ly_emoji,
                "field 'lyEmoji'");
        target.mPicGridView = (GridView) finder.castView((View) finder.findRequiredView(source, R
                .id.gv_pic, "field 'mPicGridView'"), R.id.gv_pic, "field 'mPicGridView'");
        target.syncFood = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .syncFoodCheckBox, "field 'syncFood'"), R.id.syncFoodCheckBox, "field 'syncFood'");
        View view = (View) finder.findRequiredView(source, R.id.status_post_text_editText, "field" +
                " 'editText' and method 'onClick'");
        target.editText = (EditText) finder.castView(view, R.id.status_post_text_editText, "field" +
                " 'editText'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        target.checkBox = (CheckBox) finder.castView((View) finder.findRequiredView(source, R.id
                .status_post_text_onlyMe_checkBox, "field 'checkBox'"), R.id
                .status_post_text_onlyMe_checkBox, "field 'checkBox'");
        target.charNumTextView = (TextView) finder.castView((View) finder.findRequiredView
                (source, R.id.status_post_text_numText, "field 'charNumTextView'"), R.id
                .status_post_text_numText, "field 'charNumTextView'");
        target.attachmentLayout = (LinearLayout) finder.castView((View) finder.findRequiredView
                (source, R.id.attachment_layout, "field 'attachmentLayout'"), R.id
                .attachment_layout, "field 'attachmentLayout'");
        target.ivAttachment = (ImageView) finder.castView((View) finder.findRequiredView(source,
                R.id.iv_attachment, "field 'ivAttachment'"), R.id.iv_attachment, "field " +
                "'ivAttachment'");
        target.tvAttachment = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_attachment, "field 'tvAttachment'"), R.id.tv_attachment, "field " +
                "'tvAttachment'");
        view = (View) finder.findRequiredView(source, R.id.status_post_text_emojiBtn, "field " +
                "'btnEmoji' and method 'onClick'");
        target.btnEmoji = (ImageButton) finder.castView(view, R.id.status_post_text_emojiBtn,
                "field 'btnEmoji'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.status_post_text_pictureBtn, "method " +
                "'onClick'")).setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.status_post_text_atBtn, "method 'onClick'"))
                .setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
        ((View) finder.findRequiredView(source, R.id.status_post_text_tagBtn, "method 'onClick'")
        ).setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.viewPagerEmoji = null;
        target.indicatorEmoji = null;
        target.lyEmoji = null;
        target.mPicGridView = null;
        target.syncFood = null;
        target.editText = null;
        target.checkBox = null;
        target.charNumTextView = null;
        target.attachmentLayout = null;
        target.ivAttachment = null;
        target.tvAttachment = null;
        target.btnEmoji = null;
    }
}
