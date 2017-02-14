package com.yalantis.ucrop;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.yalantis.ucrop.UCropActivity.1;

class UCropActivity$1$1 implements AnimationListener {
    final /* synthetic */ 1this$1;

    UCropActivity$1$1(1 1) {
        this.this$1 = 1;
    }

    public void onAnimationStart(Animation animation) {
        UCropActivity.access$200(this.this$1.this$0).setVisibility(0);
    }

    public void onAnimationEnd(Animation animation) {
    }

    public void onAnimationRepeat(Animation animation) {
    }
}
