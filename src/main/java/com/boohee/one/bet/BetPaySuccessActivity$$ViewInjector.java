package com.boohee.one.bet;

import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;
import butterknife.internal.DebouncingOnClickListener;

import com.boohee.one.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BetPaySuccessActivity$$ViewInjector<T extends BetPaySuccessActivity> implements
        Injector<T> {
    public void inject(Finder finder, final T target, Object source) {
        target.ivAvatar = (CircleImageView) finder.castView((View) finder.findRequiredView
                (source, R.id.iv_avatar, "field 'ivAvatar'"), R.id.iv_avatar, "field 'ivAvatar'");
        target.tvUserName = (TextView) finder.castView((View) finder.findRequiredView(source, R
                .id.tv_user_name, "field 'tvUserName'"), R.id.tv_user_name, "field 'tvUserName'");
        View view = (View) finder.findRequiredView(source, R.id.btn_upload_pic, "field " +
                "'btnUploadPic' and method 'onClick'");
        target.btnUploadPic = (TextView) finder.castView(view, R.id.btn_upload_pic, "field " +
                "'btnUploadPic'");
        view.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View p0) {
                target.onClick(p0);
            }
        });
    }

    public void reset(T target) {
        target.ivAvatar = null;
        target.tvUserName = null;
        target.btnUploadPic = null;
    }
}
