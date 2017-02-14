package cn.sharesdk.onekeyshare.theme.classic;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity;
import com.mob.tools.utils.R;
import java.util.ArrayList;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class PlatformListPage extends PlatformListFakeActivity implements OnClickListener {
    private Animation animHide;
    private Animation animShow;
    private Button btnCancel;
    private boolean finishing;
    private FrameLayout flPage;
    private PlatformGridView grid;
    private LinearLayout llPage;

    public void onCreate() {
        super.onCreate();
        this.finishing = false;
        initPageView();
        initAnim();
        this.activity.setContentView(this.flPage);
        this.grid.setData(this.shareParamsMap, this.silent);
        this.grid.setHiddenPlatforms(this.hiddenPlatforms);
        this.grid.setCustomerLogos(this.customerLogos);
        this.grid.setParent(this);
        this.btnCancel.setOnClickListener(this);
        this.llPage.clearAnimation();
        this.llPage.startAnimation(this.animShow);
    }

    private void initPageView() {
        this.flPage = new FrameLayout(getContext());
        this.flPage.setOnClickListener(this);
        this.flPage.setBackgroundDrawable(new ColorDrawable(1426063360));
        this.llPage = new LinearLayout(getContext()) {
            public boolean onTouchEvent(MotionEvent event) {
                return true;
            }
        };
        this.llPage.setOrientation(1);
        this.llPage.setBackgroundDrawable(new ColorDrawable(-1));
        LayoutParams lpLl = new LayoutParams(-1, -2);
        lpLl.gravity = 80;
        this.llPage.setLayoutParams(lpLl);
        this.flPage.addView(this.llPage);
        this.grid = new PlatformGridView(getContext());
        this.grid.setEditPageBackground(getBackgroundView());
        this.grid.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.llPage.addView(this.grid);
        this.btnCancel = new Button(getContext());
        this.btnCancel.setTextColor(AbstractWheelTextAdapter.DEFAULT_TEXT_COLOR);
        this.btnCancel.setTextSize(1, 20.0f);
        int resId = R.getStringRes(getContext(), "cancel");
        if (resId > 0) {
            this.btnCancel.setText(resId);
        }
        this.btnCancel.setPadding(0, 0, 0, R.dipToPx(getContext(), 5));
        resId = R.getBitmapRes(getContext(), "classic_platform_corners_bg");
        if (resId > 0) {
            this.btnCancel.setBackgroundResource(resId);
        } else {
            this.btnCancel.setBackgroundDrawable(new ColorDrawable(-1));
        }
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(-1, R.dipToPx(getContext(), 45));
        int dp_10 = R.dipToPx(getContext(), 10);
        lpBtn.setMargins(dp_10, dp_10, dp_10, dp_10);
        this.btnCancel.setLayoutParams(lpBtn);
        this.llPage.addView(this.btnCancel);
    }

    private void initAnim() {
        this.animShow = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 1.0f, 1, 0.0f);
        this.animShow.setDuration(300);
        this.animHide = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.0f);
        this.animHide.setDuration(300);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.grid != null) {
            this.grid.onConfigurationChanged();
        }
    }

    public boolean onFinish() {
        if (this.finishing) {
            return super.onFinish();
        }
        if (this.animHide == null) {
            this.finishing = true;
            return false;
        }
        this.finishing = true;
        this.animHide.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PlatformListPage.this.flPage.setVisibility(8);
                PlatformListPage.this.finish();
            }
        });
        this.llPage.clearAnimation();
        this.llPage.startAnimation(this.animHide);
        return true;
    }

    public void onClick(View v) {
        if (v.equals(this.flPage) || v.equals(this.btnCancel)) {
            setCanceled(true);
            finish();
        }
    }

    public void onPlatformIconClick(View v, ArrayList<Object> platforms) {
        onShareButtonClick(v, platforms);
    }
}
