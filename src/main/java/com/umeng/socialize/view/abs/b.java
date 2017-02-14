package com.umeng.socialize.view.abs;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.umeng.socialize.utils.Log;
import com.umeng.socialize.view.wigets.a;

/* compiled from: UMActionBoard */
public class b extends RelativeLayout {
    private static final int a = 150;
    private Context                           b;
    private com.umeng.socialize.view.wigets.b c;
    private Animation                         d;
    private View                              e;

    public b(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public b(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public b(Context context) {
        super(context);
        this.b = context;
        b();
    }

    private void b() {
        this.c = new com.umeng.socialize.view.wigets.b(this.b);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams.addRule(12);
        this.c.setLayoutParams(layoutParams);
        this.d = new TranslateAnimation(0.0f, 0.0f, 80.0f, 0.0f);
        this.d.setDuration(150);
        addView(this.c);
        this.e = new View(this.b);
        layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10);
        this.e.setLayoutParams(layoutParams);
        this.e.setBackgroundColor(Color.argb(50, 0, 0, 0));
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1500);
        this.e.setAnimation(alphaAnimation);
        addView(this.e);
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        Log.d("onMeasure", "ActionBoard, width = " + size + ", height = " + size2);
        LayoutParams layoutParams = this.c.getLayoutParams();
        layoutParams.height = this.c.e(size);
        layoutParams.width = size;
        LayoutParams layoutParams2 = this.e.getLayoutParams();
        layoutParams2.height = size2 - layoutParams.height;
        layoutParams2.width = size;
    }

    public void a(a aVar) {
        this.c.a(aVar);
    }

    public void a(OnClickListener onClickListener) {
        this.e.setOnClickListener(onClickListener);
    }

    public void a() {
        this.c.startAnimation(this.d);
    }
}
