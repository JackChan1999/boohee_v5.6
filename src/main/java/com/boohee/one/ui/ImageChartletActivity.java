package com.boohee.one.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.ChartletFragment;

public class ImageChartletActivity extends GestureActivity {
    private final int[]    EAT_IMG_IDS                 = new int[]{R.drawable.rw, R.drawable.rx,
            R.drawable.s8, R.drawable.s2, R.drawable.s3, R.drawable.s5, R.drawable.s9, R.drawable
            .s4};
    private final int[]    EAT_THUMBNAIL_IMG_IDS       = new int[]{R.drawable.o6, R.drawable.o7,
            R.drawable.o8, R.drawable.o9, R.drawable.o_, R.drawable.ob, R.drawable.oc, R.drawable
            .oa};
    private final int[]    ENCOURAGE_IMG_IDS           = new int[]{R.drawable.rt, R.drawable.ru,
            R.drawable.rv, R.drawable.ry, R.drawable.rz, R.drawable.s0, R.drawable.s1, R.drawable
            .s6};
    private final int[]    ENCOURAGE_THUMBNAIL_IMG_IDS = new int[]{R.drawable.oe, R.drawable.of,
            R.drawable.og, R.drawable.oh, R.drawable.oi, R.drawable.oj, R.drawable.ok, R.drawable
            .ol};
    private final int[]    QUTE_IMG_IDS                = new int[]{R.drawable.mr, R.drawable.n8,
            R.drawable.o0, R.drawable.ou, R.drawable.p3, R.drawable.vp, R.drawable.vt, R.drawable
            .wk, R.drawable.mt, R.drawable.nu, R.drawable.om, R.drawable.ov, R.drawable.vl, R
            .drawable.vq, R.drawable.w2, R.drawable.wm};
    private final String[] QUTE_TEXT_IDS               = new String[]{"怕怕", "挖鼻屎", "喷钻石", "吃饱了",
            "呼啦圈", "恩恩", "笑", "面壁", "怒摔", "哭", "崩溃", "嘚瑟", "伤心", "唉", "变身", "举重"};
    private final int[]    QUTE_THUMBNAIL_IMG_IDS      = new int[]{R.drawable.v0, R.drawable.v2,
            R.drawable.v4, R.drawable.v6, R.drawable.v8, R.drawable.v_, R.drawable.vb, R.drawable
            .vd, R.drawable.v1, R.drawable.v3, R.drawable.v5, R.drawable.v7, R.drawable.v9, R
            .drawable.va, R.drawable.vc, R.drawable.ve};
    @InjectView(2131427717)
    TextView currentTab;
    private ChartletFragment mCurrentFragment;
    private Uri              mUri;
    @InjectView(2131427716)
    View v_line;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp);
        setTitle("贴图");
        ButterKnife.inject((Activity) this);
        init();
    }

    private void init() {
        this.mUri = getIntent().getData();
        changeToEncourageFragment();
    }

    @OnClick({2131427717, 2131427718, 2131427719})
    public void onTabClick(View v) {
        int id = v.getId();
        if (this.currentTab.getId() != id) {
            switch (id) {
                case R.id.tv_tab_encourage:
                    changeToEncourageFragment();
                    refreshTab(v, R.color.h2);
                    return;
                case R.id.tv_tab_eat:
                    changeToEatFragment();
                    refreshTab(v, R.color.d0);
                    return;
                case R.id.tv_tab_qute:
                    changeToQuteFragment();
                    refreshTab(v, R.color.dt);
                    return;
                default:
                    return;
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.a2n).setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 1) {
            return super.onOptionsItemSelected(item);
        }
        if (this.mCurrentFragment == null) {
            return true;
        }
        this.mCurrentFragment.postAction();
        return true;
    }

    private void changeToEncourageFragment() {
        changeFragment(this.ENCOURAGE_THUMBNAIL_IMG_IDS, this.ENCOURAGE_IMG_IDS, null);
    }

    private void changeToEatFragment() {
        changeFragment(this.EAT_THUMBNAIL_IMG_IDS, this.EAT_IMG_IDS, null);
    }

    private void changeToQuteFragment() {
        changeFragment(this.QUTE_THUMBNAIL_IMG_IDS, this.QUTE_IMG_IDS, this.QUTE_TEXT_IDS);
    }

    private void changeFragment(int[] thumbnailIds, int[] drawables, String[] strings) {
        if (this.mUri != null) {
            this.mCurrentFragment = ChartletFragment.newInstance(thumbnailIds, drawables,
                    strings, this.mUri.toString());
            if (this.mCurrentFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, this
                        .mCurrentFragment).commitAllowingStateLoss();
            }
        }
    }

    private void refreshTab(View selectTab, int underlineColor) {
        this.currentTab.setBackgroundResource(R.color.ju);
        this.v_line.setBackgroundResource(underlineColor);
        selectTab.setBackgroundResource(R.color.in);
        this.currentTab = (TextView) selectTab;
    }
}
