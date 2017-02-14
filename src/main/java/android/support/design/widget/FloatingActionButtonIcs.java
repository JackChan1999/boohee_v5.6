package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;

class FloatingActionButtonIcs extends FloatingActionButtonEclairMr1 {
    private boolean mIsHiding;

    FloatingActionButtonIcs(VisibilityAwareImageButton view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void onPreDraw() {
        updateFromViewRotation(this.mView.getRotation());
    }

    void hide(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (this.mIsHiding || this.mView.getVisibility() != 0) {
            if (listener != null) {
                listener.onHidden();
            }
        } else if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
            this.mView.internalSetVisibility(8, fromUser);
            if (listener != null) {
                listener.onHidden();
            }
        } else {
            this.mView.animate().cancel();
            this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
                private boolean mCancelled;

                public void onAnimationStart(Animator animation) {
                    FloatingActionButtonIcs.this.mIsHiding = true;
                    this.mCancelled = false;
                    FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
                }

                public void onAnimationCancel(Animator animation) {
                    FloatingActionButtonIcs.this.mIsHiding = false;
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animation) {
                    FloatingActionButtonIcs.this.mIsHiding = false;
                    if (!this.mCancelled) {
                        FloatingActionButtonIcs.this.mView.internalSetVisibility(8, fromUser);
                        if (listener != null) {
                            listener.onHidden();
                        }
                    }
                }
            });
        }
    }

    void show(@Nullable final InternalVisibilityChangedListener listener, final boolean fromUser) {
        if (!this.mIsHiding && this.mView.getVisibility() == 0) {
            return;
        }
        if (!ViewCompat.isLaidOut(this.mView) || this.mView.isInEditMode()) {
            this.mView.internalSetVisibility(0, fromUser);
            this.mView.setAlpha(1.0f);
            this.mView.setScaleY(1.0f);
            this.mView.setScaleX(1.0f);
            if (listener != null) {
                listener.onShown();
                return;
            }
            return;
        }
        this.mView.animate().cancel();
        if (this.mView.getVisibility() != 0) {
            this.mView.setAlpha(0.0f);
            this.mView.setScaleY(0.0f);
            this.mView.setScaleX(0.0f);
        }
        this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                FloatingActionButtonIcs.this.mView.internalSetVisibility(0, fromUser);
            }

            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onShown();
                }
            }
        });
    }

    private void updateFromViewRotation(float rotation) {
        if (this.mShadowDrawable != null) {
            this.mShadowDrawable.setRotation(-rotation);
        }
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setRotation(-rotation);
        }
    }
}
