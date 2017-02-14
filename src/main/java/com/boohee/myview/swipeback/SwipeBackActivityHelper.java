package com.boohee.myview.swipeback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;

import com.boohee.myview.swipeback.SwipeBackLayout.SwipeListener;
import com.boohee.one.R;

import java.lang.reflect.Method;

public class SwipeBackActivityHelper {
    private Activity        mActivity;
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void onActivityCreate() {
        this.mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        this.mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this.mActivity).inflate(R
                .layout.mw, null);
        this.mSwipeBackLayout.addSwipeListener(new SwipeListener() {
            public void onScrollStateChange(int state, float scrollPercent) {
                if (state == 0 && scrollPercent == 0.0f) {
                    SwipeBackActivityHelper.this.convertActivityFromTranslucent();
                }
            }

            public void onEdgeTouch(int edgeFlag) {
                SwipeBackActivityHelper.this.convertActivityToTranslucent();
            }

            public void onScrollOverThreshold() {
            }
        });
    }

    public void onPostCreate() {
        this.mSwipeBackLayout.attachToActivity(this.mActivity);
        convertActivityFromTranslucent();
    }

    public View findViewById(int id) {
        if (this.mSwipeBackLayout != null) {
            return this.mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return this.mSwipeBackLayout;
    }

    public void convertActivityFromTranslucent() {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent", new
                    Class[0]);
            method.setAccessible(true);
            method.invoke(this.mActivity, new Object[0]);
        } catch (Throwable th) {
        }
    }

    @SuppressLint({"NewApi"})
    public void convertActivityToTranslucent() {
        try {
            Class<?> translucentConversionListenerClazz = null;
            for (Class<?> clazz : Activity.class.getDeclaredClasses()) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method method;
            if (VERSION.SDK_INT > 19) {
                method = Activity.class.getDeclaredMethod("convertToTranslucent", new
                        Class[]{translucentConversionListenerClazz, ActivityOptions.class});
                method.setAccessible(true);
                method.invoke(this.mActivity, new Object[]{null, null});
                return;
            }
            method = Activity.class.getDeclaredMethod("convertToTranslucent", new
                    Class[]{translucentConversionListenerClazz});
            method.setAccessible(true);
            method.invoke(this.mActivity, new Object[]{null});
        } catch (Throwable th) {
        }
    }
}
