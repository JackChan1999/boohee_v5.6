package com.boohee.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.boohee.api.StatusApi;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utility.Event;
import com.boohee.utils.AccountUtils;
import com.boohee.utils.Helper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class PraiseButton extends FrameLayout implements OnClickListener {
    static final String TAG = PraiseButton.class.getSimpleName();
    public  boolean          isPraised;
    private Context          mContext;
    private int              post_id;
    private Animation        praiseAnim;
    private OnPraiseListener praiseListener;
    private TextView         praisePlus;
    private Button           praiseText;

    public interface OnPraiseListener {
        void onPraise(boolean z);
    }

    public PraiseButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isPraised = false;
        this.mContext = context;
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PraiseButton);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
        }
        a.recycle();
    }

    public PraiseButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseButton(Context context) {
        this(context, null);
    }

    private void init() {
        View root = LayoutInflater.from(this.mContext).inflate(R.layout.lu, null);
        addView(root);
        this.praisePlus = (TextView) root.findViewById(R.id.praise_plus);
        this.praiseText = (Button) root.findViewById(R.id.praise_text);
        this.praiseText.setOnClickListener(this);
        this.praiseAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.a9);
    }

    public void setPraiseCount(int count) {
        this.praiseText.setText(count + "");
    }

    public void setPostId(int id) {
        this.post_id = id;
    }

    public void sendPraise(boolean isDelete) {
        if (isDelete) {
            StatusApi.deleteFeedbacks(this.mContext, this.post_id, new JsonCallback((Activity)
                    this.mContext) {
            });
        } else {
            StatusApi.putFeedbacks(this.mContext, this.post_id, new JsonCallback((Activity) this
                    .mContext) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    if (!TextUtils.isEmpty(object.optString("body"))) {
                        Helper.showToast(PraiseButton.this.getContext(), object.optString("body"));
                    }
                    MobclickAgent.onEvent(PraiseButton.this.mContext, Event.STATUS_ADD_ATTITUTE_OK);
                    MobclickAgent.onEvent(PraiseButton.this.mContext, Event.STATUS_ADD_INTERACT_OK);
                    MobclickAgent.onEvent(PraiseButton.this.mContext, Event.MINE_ALL_RECORD_OK);
                }
            });
        }
    }

    public void onClick(View v) {
        if (AccountUtils.isVisitorAccount(this.mContext)) {
            CheckAccountPopwindow.showVisitorPopWindow(this.mContext);
            return;
        }
        if (this.isPraised) {
            this.praiseText.setBackgroundResource(R.drawable.a0v);
            this.praiseText.setTextColor(getResources().getColor(R.color.ii));
            this.isPraised = false;
            sendPraise(true);
            setPlusCount(-1);
        } else {
            this.praiseText.setBackgroundResource(R.drawable.a0w);
            this.praiseText.setTextColor(getResources().getColor(R.color.ij));
            this.isPraised = true;
            sendPraise(false);
            setPlusCount(1);
            setPlusAnimation();
        }
        if (this.praiseListener != null) {
            this.praiseListener.onPraise(this.isPraised);
        }
    }

    private void setPlusCount(int count) {
        this.praiseText.setText((Integer.parseInt(this.praiseText.getText().toString().trim()) +
                count) + "");
    }

    public void setFeedback(String feedback) {
        if (feedback != null) {
            this.isPraised = true;
            this.praiseText.setBackgroundResource(R.drawable.a0w);
            this.praiseText.setTextColor(getResources().getColor(R.color.ij));
            return;
        }
        this.isPraised = false;
        this.praiseText.setBackgroundResource(R.drawable.a0v);
        this.praiseText.setTextColor(getResources().getColor(R.color.ii));
    }

    private void setPlusAnimation() {
        this.praisePlus.setVisibility(0);
        this.praisePlus.startAnimation(this.praiseAnim);
        this.praiseAnim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PraiseButton.this.praisePlus.setVisibility(8);
            }
        });
    }

    public void setOnPraiseLstener(OnPraiseListener listener) {
        this.praiseListener = listener;
    }
}
