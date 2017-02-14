package com.boohee.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boohee.one.R;

public class TurboLoadingFooter {
    private   long        mAnimationDuration;
    protected View        mLoadingFooter;
    protected TextView    mLoadingText;
    private   ProgressBar mProgress;
    protected State mState = State.Idle;

    public enum State {
        Idle,
        TheEnd,
        Loading
    }

    public TurboLoadingFooter(Context context) {
        this.mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.n_, null);
        this.mLoadingFooter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
        this.mProgress = (ProgressBar) this.mLoadingFooter.findViewById(R.id.progressBar);
        this.mLoadingText = (TextView) this.mLoadingFooter.findViewById(R.id.textView);
        this.mAnimationDuration = (long) context.getResources().getInteger(17694720);
        setState(State.Idle);
    }

    public View getView() {
        return this.mLoadingFooter;
    }

    public State getState() {
        return this.mState;
    }

    public void setState(final State state, long delay) {
        this.mLoadingFooter.postDelayed(new Runnable() {
            public void run() {
                TurboLoadingFooter.this.setState(state);
            }
        }, delay);
    }

    public void setLoadingText(String loadingText) {
        this.mLoadingText.setText(loadingText);
    }

    public void setState(State status) {
        if (this.mState != status) {
            this.mState = status;
            this.mLoadingFooter.setVisibility(0);
            switch (status) {
                case Loading:
                    this.mLoadingText.setVisibility(8);
                    this.mProgress.setVisibility(0);
                    return;
                case TheEnd:
                    this.mLoadingText.setVisibility(0);
                    AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(this.mAnimationDuration);
                    this.mLoadingText.startAnimation(animation);
                    this.mProgress.setVisibility(8);
                    return;
                case Idle:
                    this.mLoadingFooter.setVisibility(8);
                    return;
                default:
                    this.mLoadingFooter.setVisibility(8);
                    return;
            }
        }
    }
}
