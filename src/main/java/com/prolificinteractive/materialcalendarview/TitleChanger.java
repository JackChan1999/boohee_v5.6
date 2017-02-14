package com.prolificinteractive.materialcalendarview;

import android.animation.Animator;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

class TitleChanger {
    public static final int DEFAULT_ANIMATION_DELAY  = 400;
    public static final int DEFAULT_Y_TRANSLATION_DP = 20;
    private final int animDelay;
    private final int animDuration;
    private final Interpolator interpolator  = new DecelerateInterpolator(2.0f);
    private       long         lastAnimTime  = 0;
    private       CalendarDay  previousMonth = null;
    private final TextView       title;
    private       TitleFormatter titleFormatter;
    private final int            yTranslate;

    public TitleChanger(TextView title) {
        this.title = title;
        Resources res = title.getResources();
        this.animDelay = 400;
        this.animDuration = res.getInteger(17694720) / 2;
        this.yTranslate = (int) TypedValue.applyDimension(1, 20.0f, res.getDisplayMetrics());
    }

    public void change(CalendarDay currentMonth) {
        long currentTime = System.currentTimeMillis();
        if (currentMonth != null) {
            if (TextUtils.isEmpty(this.title.getText()) || currentTime - this.lastAnimTime < (
                    (long) this.animDelay)) {
                doChange(currentTime, currentMonth, false);
            }
            if (!currentMonth.equals(this.previousMonth)) {
                doChange(currentTime, currentMonth, true);
            }
        }
    }

    private void doChange(long now, CalendarDay currentMonth, boolean animate) {
        this.title.animate().cancel();
        this.title.setTranslationY(0.0f);
        this.title.setAlpha(1.0f);
        this.lastAnimTime = now;
        final CharSequence newTitle = this.titleFormatter.format(currentMonth);
        if (animate) {
            final int yTranslation = this.yTranslate * (this.previousMonth.isBefore(currentMonth)
                    ? 1 : -1);
            this.title.animate().translationY((float) (yTranslation * -1)).alpha(0.0f)
                    .setDuration((long) this.animDuration).setInterpolator(this.interpolator)
                    .setListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                    TitleChanger.this.title.setTranslationY(0.0f);
                    TitleChanger.this.title.setAlpha(1.0f);
                }

                public void onAnimationEnd(Animator animator) {
                    TitleChanger.this.title.setText(newTitle);
                    TitleChanger.this.title.setTranslationY((float) yTranslation);
                    TitleChanger.this.title.animate().translationY(0.0f).alpha(1.0f).setDuration(
                            (long) TitleChanger.this.animDuration).setInterpolator(TitleChanger
                            .this.interpolator).setListener(new AnimatorListener()).start();
                }
            }).start();
        } else {
            this.title.setText(newTitle);
        }
        this.previousMonth = currentMonth;
    }

    public void setTitleFormatter(TitleFormatter titleFormatter) {
        this.titleFormatter = titleFormatter;
    }

    public void setPreviousMonth(CalendarDay previousMonth) {
        this.previousMonth = previousMonth;
    }
}
