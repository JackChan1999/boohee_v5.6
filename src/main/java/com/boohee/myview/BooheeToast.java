package com.boohee.myview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.boohee.utility.DensityUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BooheeToast {
    public static final  int                        LENGTH_LONG    = 1;
    public static final  int                        LENGTH_SHORT   = 0;
    static final         String                     TAG            = "BooheeToast";
    private static final Runnable                   mActivite      = new Runnable() {
        public void run() {
            BooheeToast.activeQueue();
        }
    };
    protected static     AtomicInteger              mAtomicInteger = new AtomicInteger(0);
    private static       Handler                    mHanlder       = new Handler();
    private static       BlockingQueue<BooheeToast> mQueue         = new LinkedBlockingQueue();
    final Context mContext;
    long mDuration;
    private final Runnable mHide = new Runnable() {
        public void run() {
            BooheeToast.this.handleHide();
        }
    };
    private LayoutParams mParams;
    private final Runnable mShow = new Runnable() {
        public void run() {
            BooheeToast.this.handleShow();
        }
    };
    View mView;
    private WindowManager mWM;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public BooheeToast(Context context) {
        this.mContext = context;
        this.mParams = new LayoutParams();
        this.mParams.height = -2;
        this.mParams.width = -2;
        this.mParams.format = -3;
        this.mParams.windowAnimations = 16973828;
        this.mParams.type = 2005;
        this.mParams.setTitle("Toast");
        this.mParams.flags = 152;
        this.mWM = (WindowManager) context.getSystemService("window");
        this.mParams.packageName = context.getPackageName();
    }

    public static BooheeToast makeText(Context context, CharSequence text, int duration) {
        return new BooheeToast(context).setText(text).setDuration(duration).setGravity(80, 0,
                DensityUtil.dip2px(context, 64.0f));
    }

    public static BooheeToast makeText(Context context, int resId, int duration) throws
            NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public BooheeToast setView(View view) {
        this.mView = view;
        return this;
    }

    public View getView() {
        return this.mView;
    }

    @TargetApi(17)
    public BooheeToast setGravity(int gravity, int xOffset, int yOffset) {
        int finalGravity = gravity;
        this.mParams.gravity = finalGravity;
        if ((finalGravity & 7) == 7) {
            this.mParams.horizontalWeight = 1.0f;
        }
        if ((finalGravity & 112) == 112) {
            this.mParams.verticalWeight = 1.0f;
        }
        this.mParams.y = yOffset;
        this.mParams.x = xOffset;
        return this;
    }

    public BooheeToast setDuration(int duration) {
        if (duration == 0) {
            this.mDuration = 2000;
        } else if (duration == 1) {
            this.mDuration = 3500;
        }
        return this;
    }

    public BooheeToast setText(int resId) {
        setText(this.mContext.getText(resId));
        return this;
    }

    public BooheeToast setText(CharSequence s) {
        View view = Toast.makeText(this.mContext, s, 0).getView();
        if (view != null) {
            ((TextView) view.findViewById(16908299)).setText(s);
            setView(view);
        }
        return this;
    }

    public void show() {
        mQueue.offer(this);
        if (mAtomicInteger.get() == 0) {
            mAtomicInteger.incrementAndGet();
            mHanlder.post(mActivite);
        }
    }

    public void cancel() {
        if (!(mAtomicInteger.get() == 0 && mQueue.isEmpty()) && equals(mQueue.peek())) {
            mHanlder.removeCallbacks(mActivite);
            mHanlder.post(this.mHide);
            mHanlder.post(mActivite);
        }
    }

    private void handleShow() {
        if (this.mView != null) {
            if (this.mView.getParent() != null) {
                this.mWM.removeView(this.mView);
            }
            this.mWM.addView(this.mView, this.mParams);
        }
    }

    private void handleHide() {
        if (this.mView != null) {
            if (this.mView.getParent() != null) {
                this.mWM.removeView(this.mView);
                mQueue.poll();
            }
            this.mView = null;
        }
    }

    private static void activeQueue() {
        BooheeToast toast = (BooheeToast) mQueue.peek();
        if (toast == null) {
            mAtomicInteger.decrementAndGet();
            return;
        }
        mHanlder.post(toast.mShow);
        mHanlder.postDelayed(toast.mHide, toast.mDuration);
        mHanlder.postDelayed(mActivite, toast.mDuration);
    }
}
