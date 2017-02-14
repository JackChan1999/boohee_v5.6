package com.boohee.one.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.model.FilterSyncBean;
import com.boohee.one.R;
import com.boohee.utils.BitmapUtil;
import com.boohee.utils.FilterDataSyncUtil;

import java.io.File;

public class ImageFilterActivity extends GestureActivity implements OnClickListener {
    public static final String KEY_POST_DATA = "KEY_POST_DATA";
    public static final String KEY_POST_TAG  = "KEY_POST_TAG";
    private Fragment currentFragment;
    private TextView currentTab;
    private Fragment eatFragment;
    private TextView eatTab;
    private Fragment figureFragment;
    private TextView figureTab;
    private FragmentManager fm = null;
    private View           line;
    private Handler        mHandler;
    private Uri            mUri;
    private Fragment       sleepFragment;
    private TextView       sleepTab;
    private Fragment       sportFragment;
    private TextView       sportTab;
    private FilterSyncBean syncData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("滤镜");
        setContentView(R.layout.br);
        findView();
        addListener();
        init();
        setUpFragment();
    }

    private void setUpFragment() {
        FragmentTransaction ft = this.fm.beginTransaction();
        this.eatFragment = new FilterEatFragment();
        this.currentFragment = this.eatFragment;
        ft.replace(R.id.filterContent, this.eatFragment).commit();
    }

    private void findView() {
        this.eatTab = (TextView) findViewById(R.id.eatText);
        this.sportTab = (TextView) findViewById(R.id.sportText);
        this.figureTab = (TextView) findViewById(R.id.figureText);
        this.sleepTab = (TextView) findViewById(R.id.sleepText);
        this.line = findViewById(R.id.filterTextLine);
    }

    private void addListener() {
        this.eatTab.setOnClickListener(this);
        this.sportTab.setOnClickListener(this);
        this.figureTab.setOnClickListener(this);
        this.sleepTab.setOnClickListener(this);
    }

    private void init() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mUri = getIntent().getData();
        this.currentTab = this.eatTab;
        this.currentTab.setBackgroundColor(0);
        this.line.setBackgroundColor(getResources().getColor(R.color.h2));
        this.fm = getSupportFragmentManager();
        this.syncData = new FilterSyncBean();
        initFragment();
    }

    private void initFragment() {
        this.eatFragment = new FilterEatFragment();
        this.sportFragment = new FilterSportFragment();
        this.figureFragment = new FilterFigureFragment();
        this.sleepFragment = new FilterSleepFragment();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.a2n).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            postAction();
            return true;
        } else if (item.getItemId() != 16908332) {
            return super.onOptionsItemSelected(item);
        } else {
            leftAction();
            return true;
        }
    }

    private void leftAction() {
        FilterDataSyncUtil.removeFilterData(this.ctx);
        finish();
    }

    public void postAction() {
        final Bitmap bmp = BitmapUtil.viewToBitmap(this.currentFragment.getView());
        if (bmp != null) {
            showLoading();
            saveSyncData(this.currentFragment);
            new Thread() {
                public void run() {
                    String fileName = ImageFilterActivity.this.getExternalCacheDir().toString() +
                            File.separatorChar + System.currentTimeMillis() + ".png";
                    BitmapUtil.saveBitmap(fileName, bmp);
                    ImageFilterActivity.this.mUri = Uri.fromFile(new File(fileName));
                    ImageFilterActivity.this.mHandler.post(new Runnable() {
                        public void run() {
                            ImageFilterActivity.this.dismissLoading();
                            ImageFilterActivity.this.setResult(-1, new Intent(ImageFilterActivity
                                    .this.ctx, StatusPostTextActivity.class).setData
                                    (ImageFilterActivity.this.mUri));
                            ImageFilterActivity.this.finish();
                        }
                    });
                }
            }.start();
        }
    }

    private void saveSyncData(Fragment currentFragment) {
        if (currentFragment instanceof FilterEatFragment) {
            ((FilterEatFragment) currentFragment).saveSyncData(this.syncData);
        } else if (currentFragment instanceof FilterSportFragment) {
            ((FilterSportFragment) currentFragment).saveSyncData(this.syncData);
        } else if (currentFragment instanceof FilterFigureFragment) {
            ((FilterFigureFragment) currentFragment).saveSyncData(this.syncData);
        }
        if (!this.syncData.isNull()) {
            UserPreference.getInstance(this.ctx).putString(KEY_POST_DATA, this.syncData.toString());
        }
    }

    public void onClick(View v) {
        if (this.currentTab != v) {
            updateTab((TextView) v);
            switch (v.getId()) {
                case R.id.eatText:
                    changeFragment(this.eatFragment);
                    this.line.setBackgroundColor(getResources().getColor(R.color.h2));
                    return;
                case R.id.sportText:
                    changeFragment(this.sportFragment);
                    this.line.setBackgroundColor(-16711936);
                    return;
                case R.id.figureText:
                    changeFragment(this.figureFragment);
                    this.line.setBackgroundColor(getResources().getColor(R.color.dt));
                    return;
                case R.id.sleepText:
                    changeFragment(this.sleepFragment);
                    this.line.setBackgroundColor(getResources().getColor(R.color.hj));
                    return;
                default:
                    return;
            }
        }
    }

    private void updateTab(TextView selectedTab) {
        this.currentTab.setBackgroundColor(-1);
        this.currentTab = selectedTab;
        this.currentTab.setBackgroundColor(0);
    }

    private void changeFragment(Fragment fragment) {
        if (this.currentFragment != fragment) {
            FragmentTransaction ft = this.fm.beginTransaction();
            ft.remove(this.currentFragment);
            ft.replace(R.id.filterContent, fragment);
            ft.commitAllowingStateLoss();
            this.currentFragment = fragment;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            UserPreference.getInstance(this.ctx).remove(KEY_POST_TAG);
        }
        return super.onKeyDown(keyCode, event);
    }
}
