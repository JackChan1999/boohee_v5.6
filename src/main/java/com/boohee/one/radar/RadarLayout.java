package com.boohee.one.radar;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.one.R;
import com.boohee.one.radar.RadarView.AnimFinishListener;
import com.boohee.one.radar.entity.Radar;
import com.umeng.socialize.common.SocializeConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;

public class RadarLayout extends FrameLayout {
    private final float MAX = 1000.0f;
    private TextView addBalance;
    private TextView addNutrition;
    private TextView addSocial;
    private TextView addSpirit;
    private TextView addSport;
    private List<TextView> addTvList = new ArrayList();
    private TextView  emptyRadar;
    private RadarView radarView;
    private boolean   showAddAnim;
    private TextView  tagBalance;
    private TextView  tagNutrition;
    private TextView  tagSocial;
    private TextView  tagSpirit;
    private TextView  tagSport;

    public RadarLayout(Context context) {
        super(context);
        init(context);
    }

    public RadarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.RadarLayout, this);
        this.radarView = (RadarView) findViewById(R.id.razor);
        this.radarView.setAnimFinishListener(new AnimFinishListener() {
            public void onAnimFinish() {
                if (RadarLayout.this.showAddAnim) {
                    RadarLayout.this.showAddAnim();
                }
            }
        });
        this.tagNutrition = (TextView) findViewById(R.id.tag_nutrition);
        this.tagSocial = (TextView) findViewById(R.id.tag_social);
        this.tagSpirit = (TextView) findViewById(R.id.tag_mind);
        this.tagSport = (TextView) findViewById(R.id.tag_sport);
        this.tagBalance = (TextView) findViewById(R.id.tag_balance);
        this.addNutrition = (TextView) findViewById(R.id.add_nutrition);
        this.addSocial = (TextView) findViewById(R.id.add_social);
        this.addSpirit = (TextView) findViewById(R.id.add_mind);
        this.addSport = (TextView) findViewById(R.id.add_sport);
        this.addBalance = (TextView) findViewById(R.id.add_balance);
        this.emptyRadar = (TextView) findViewById(R.id.empty_radar);
        this.addTvList.add(this.addNutrition);
        this.addTvList.add(this.addSocial);
        this.addTvList.add(this.addSpirit);
        this.addTvList.add(this.addSport);
        this.addTvList.add(this.addBalance);
    }

    public void setData(Radar data) {
        if (data != null) {
            this.emptyRadar.setVisibility(8);
            if (data.increment != null) {
                this.addNutrition.setText(String.valueOf(SocializeConstants.OP_DIVIDER_PLUS +
                        getValue("nutrition", data.increment)));
                this.addSpirit.setText(String.valueOf(SocializeConstants.OP_DIVIDER_PLUS +
                        getValue("spirit", data.increment)));
                this.addBalance.setText(String.valueOf(SocializeConstants.OP_DIVIDER_PLUS +
                        getValue("balance", data.increment)));
                this.addSport.setText(String.valueOf(SocializeConstants.OP_DIVIDER_PLUS +
                        getValue("sports", data.increment)));
                this.addSocial.setText(String.valueOf(SocializeConstants.OP_DIVIDER_PLUS +
                        getValue("social", data.increment)));
            }
            if (data.total != null) {
                this.radarView.setTarget(new float[]{calculatePercent(data.total.nutrition),
                        calculatePercent(data.total.spirit), calculatePercent(data.total.balance)
                        , calculatePercent(data.total.sports), calculatePercent(data.total
                        .social)});
            }
        }
    }

    public void showLoading() {
        this.emptyRadar.setVisibility(0);
        this.emptyRadar.setText("加载中");
    }

    private float calculatePercent(int data) {
        float percent;
        data *= 5;
        float[] gap = new float[]{0.0f, 0.0f, 25.0f, 300.0f, 600.0f, 1000.0f};
        if (data == 0) {
            percent = 0.2f;
        } else if (((float) data) > gap[1] && ((float) data) <= gap[2]) {
            percent = 0.2f + (((((float) data) - gap[1]) / (gap[2] - gap[1])) * 0.2f);
        } else if (((float) data) > gap[2] && ((float) data) <= gap[3]) {
            percent = 0.4f + (((((float) data) - gap[2]) / (gap[3] - gap[2])) * 0.2f);
        } else if (((float) data) <= gap[3] || ((float) data) > gap[4]) {
            percent = 0.8f + (((((float) data) - gap[4]) / (gap[5] - gap[4])) * 0.2f);
        } else {
            percent = PieChartData.DEFAULT_CENTER_CIRCLE_SCALE + (((((float) data) - gap[3]) /
                    (gap[4] - gap[3])) * 0.2f);
        }
        if (percent > 1.0f) {
            return 1.0f;
        }
        return percent;
    }

    private int getValue(String value, Map<String, Integer> map) {
        if (map != null && map.containsKey(value)) {
            return ((Integer) map.get(value)).intValue();
        }
        return 0;
    }

    public void startAnim(boolean showAddAnim) {
        this.radarView.startAnim();
        this.showAddAnim = showAddAnim;
    }

    private void showAddAnim() {
        AnimatorSet showAnim = new AnimatorSet();
        List<Animator> animatorList = new ArrayList();
        for (TextView tv : this.addTvList) {
            tv.setVisibility(0);
            animatorList.add(ObjectAnimator.ofFloat(tv, "alpha", new float[]{0.0f, 1.0f}));
        }
        showAnim.setDuration(200);
        showAnim.playTogether(animatorList);
        showAnim.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                RadarLayout.this.showMoveAnim();
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        showAnim.start();
    }

    private void showMoveAnim() {
        AnimatorSet moveAnim = new AnimatorSet();
        List<Animator> animatorList = new ArrayList();
        for (TextView tv : this.addTvList) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(tv, "alpha", new float[]{1.0f, 0.0f});
            ObjectAnimator translationY = ObjectAnimator.ofFloat(tv, "translationY", new
                    float[]{0.0f, -20.0f});
            animatorList.add(alpha);
            animatorList.add(translationY);
        }
        moveAnim.setDuration(1000);
        moveAnim.playTogether(animatorList);
        moveAnim.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                for (TextView tv : RadarLayout.this.addTvList) {
                    tv.setVisibility(8);
                    tv.setTranslationY(0.0f);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        moveAnim.start();
    }
}
