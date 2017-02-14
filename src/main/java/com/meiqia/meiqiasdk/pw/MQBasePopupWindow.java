package com.meiqia.meiqiasdk.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.PopupWindow;

public abstract class MQBasePopupWindow extends PopupWindow implements OnClickListener {
    protected Activity mActivity;
    protected View     mAnchorView;
    protected View     mWindowRootView;

    protected abstract void initView();

    protected abstract void processLogic();

    protected abstract void setListener();

    public abstract void show();

    public MQBasePopupWindow(Activity activity, @LayoutRes int layoutId, View anchorView, int
            width, int height) {
        super(View.inflate(activity, layoutId, null), width, height, true);
        init(activity, anchorView);
        initView();
        setListener();
        processLogic();
    }

    private void init(Activity activity, View anchorView) {
        getContentView().setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != 4) {
                    return false;
                }
                MQBasePopupWindow.this.dismiss();
                return true;
            }
        });
        setBackgroundDrawable(new ColorDrawable(0));
        this.mAnchorView = anchorView;
        this.mActivity = activity;
        this.mWindowRootView = activity.getWindow().peekDecorView();
    }

    public void onClick(View view) {
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return getContentView().findViewById(id);
    }
}
