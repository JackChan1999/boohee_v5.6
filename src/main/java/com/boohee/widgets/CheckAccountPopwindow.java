package com.boohee.widgets;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.boohee.one.R;
import com.boohee.one.ui.CheckPhoneActivity;
import com.boohee.one.ui.NewLoginAndRegisterActivity;
import com.boohee.utils.ViewFinder;

public class CheckAccountPopwindow implements OnClickListener {
    public static final int T_CHECK_PHONE = 1;
    public static final int T_VISITOR     = 0;
    private static Animation inAnim;
    private static CheckAccountPopwindow instance = new CheckAccountPopwindow();
    private static Context      mContext;
    private static LinearLayout popLayout;
    private static PopupWindow  popWindow;
    private        Button       checkPhoneBtn;
    private        View         contentView;
    private        ViewFinder   finder;
    private        Button       loginBtn;
    private        Button       registerBtn;
    private        View         registerBtnUnderLine;

    private CheckAccountPopwindow() {
    }

    private void createPopWindow(Context context, int type) {
        if (mContext != context) {
            mContext = context;
            popWindow = new PopupWindow(createContentView(type), -1, -2, true);
            popWindow.setTouchable(true);
            popWindow.setOutsideTouchable(true);
            inAnim = AnimationUtils.loadAnimation(mContext, R.anim.s);
        }
    }

    private View createContentView(int type) {
        this.contentView = View.inflate(mContext, R.layout.oh, null);
        this.finder = new ViewFinder(this.contentView);
        findView();
        addListener();
        setUpUI(type);
        return this.contentView;
    }

    private void findView() {
        this.registerBtnUnderLine = this.finder.find(R.id.registerBtnUnderLine);
        popLayout = (LinearLayout) this.finder.find(R.id.popLayout);
        this.registerBtn = (Button) this.finder.find(R.id.registerBtn);
        this.loginBtn = (Button) this.finder.find(R.id.loginBtn);
        this.checkPhoneBtn = (Button) this.finder.find(R.id.checkPhoneBtn);
    }

    private void addListener() {
        this.registerBtn.setOnClickListener(this);
        this.loginBtn.setOnClickListener(this);
        this.checkPhoneBtn.setOnClickListener(this);
        this.finder.find(R.id.cancelBtn).setOnClickListener(this);
        this.finder.find(R.id.mask).setOnClickListener(this);
    }

    private void setUpUI(int type) {
        switch (type) {
            case 0:
                this.checkPhoneBtn.setVisibility(8);
                return;
            case 1:
                this.registerBtnUnderLine.setVisibility(8);
                this.registerBtn.setVisibility(8);
                this.loginBtn.setVisibility(8);
                this.checkPhoneBtn.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.loginBtn:
                intent = new Intent(mContext, NewLoginAndRegisterActivity.class);
                intent.putExtra(NewLoginAndRegisterActivity.LAU_KEY, 1);
                mContext.startActivity(intent);
                dismiss();
                return;
            case R.id.mask:
                dismiss();
                return;
            case R.id.registerBtn:
                intent = new Intent(mContext, NewLoginAndRegisterActivity.class);
                intent.putExtra(NewLoginAndRegisterActivity.LAU_KEY, 2);
                mContext.startActivity(intent);
                dismiss();
                return;
            case R.id.checkPhoneBtn:
                intent = new Intent(mContext, CheckPhoneActivity.class);
                intent.putExtra(CheckPhoneActivity.KEY, 2);
                mContext.startActivity(intent);
                dismiss();
                return;
            case R.id.cancelBtn:
                dismiss();
                return;
            default:
                return;
        }
    }

    private static void show(Context context) {
        if (popWindow != null && !popWindow.isShowing()) {
            popWindow.showAtLocation(new View(context), 80, 0, 0);
            popLayout.startAnimation(inAnim);
        }
    }

    public static synchronized boolean isShowing() {
        boolean z;
        synchronized (CheckAccountPopwindow.class) {
            z = popWindow != null && popWindow.isShowing();
        }
        return z;
    }

    public static synchronized void dismiss() {
        synchronized (CheckAccountPopwindow.class) {
            if (mContext != null) {
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        }
    }

    public static synchronized void showVisitorPopWindow(Context context) {
        synchronized (CheckAccountPopwindow.class) {
            instance.createPopWindow(context, 0);
            show(context);
        }
    }

    public static synchronized void showCheckPhonePopWindow(Context context) {
        synchronized (CheckAccountPopwindow.class) {
            instance.createPopWindow(context, 1);
            show(context);
        }
    }
}
