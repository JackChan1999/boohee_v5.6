package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AppCompatImageHelper {
    private final AppCompatDrawableManager mDrawableManager;
    private final ImageView mView;

    public AppCompatImageHelper(ImageView view, AppCompatDrawableManager drawableManager) {
        this.mView = view;
        this.mDrawableManager = drawableManager;
    }

    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), attrs, R.styleable.AppCompatImageView, defStyleAttr, 0);
        try {
            Drawable d = a.getDrawableIfKnown(R.styleable.AppCompatImageView_android_src);
            if (d != null) {
                this.mView.setImageDrawable(d);
            }
            int id = a.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
            if (id != -1) {
                d = this.mDrawableManager.getDrawable(this.mView.getContext(), id);
                if (d != null) {
                    this.mView.setImageDrawable(d);
                }
            }
            Drawable drawable = this.mView.getDrawable();
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            a.recycle();
        } catch (Throwable th) {
            a.recycle();
        }
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            Drawable d = this.mDrawableManager != null ? this.mDrawableManager.getDrawable(this.mView.getContext(), resId) : ContextCompat.getDrawable(this.mView.getContext(), resId);
            if (d != null) {
                DrawableUtils.fixDrawable(d);
            }
            this.mView.setImageDrawable(d);
            return;
        }
        this.mView.setImageDrawable(null);
    }
}
