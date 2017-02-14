package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.http.IPCheck;
import com.boohee.one.http.RequestManager;
import com.boohee.one.ui.fragment.LoadingFragment;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.AppUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.ViewFinder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity activity;
    protected Context      ctx;
    protected ViewFinder   finder;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private   LoadingFragment mLoading;
    private   LinearLayout    rootLayout;
    protected Toolbar         toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView((int) R.layout.af);
        this.ctx = this;
        this.activity = this;
        Helper.showLog(toString());
        this.finder = new ViewFinder((Activity) this);
    }

    public void setContentView(int layoutId) {
        this.rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (this.rootLayout != null) {
            this.rootLayout.addView(View.inflate(this, layoutId, null), new LayoutParams(-1, -1));
            initToolbar();
        }
    }

    private void initToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (this.toolbar == null) {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (MyApplication.getIsMainOpened()) {
            super.onBackPressed();
            return;
        }
        if (AccountUtils.isReleaseUser()) {
            MainActivity.comeOnBaby(this);
        } else {
            WelcomeActivity.comeOnBaby(this);
        }
        finish();
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!AppUtils.isAppActive) {
            AppUtils.isAppActive = true;
            IPCheck.ipTest();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void showLoading() {
        try {
            if (this.mLoading == null) {
                this.mLoading = LoadingFragment.newInstance();
            }
            if (!this.mLoading.isAdded()) {
                getSupportFragmentManager().beginTransaction().remove(this.mLoading)
                        .commitAllowingStateLoss();
                this.mLoading.show(getSupportFragmentManager(), "loading");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissLoading() {
        try {
            if (this.mLoading != null && this.mLoading.isAdded()) {
                this.mLoading.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        if (!AppUtils.isForeground(this)) {
            AppUtils.isAppActive = false;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected String getStringExtra(String name) {
        return getIntent().getStringExtra(name);
    }

    protected int getIntExtra(String name) {
        return getIntent().getIntExtra(name, -1);
    }

    public void comeOnBaby(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
