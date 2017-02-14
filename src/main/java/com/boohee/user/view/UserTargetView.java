package com.boohee.user.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.boohee.account.UserInitActivity;
import com.boohee.account.UserProfileActivity;
import com.boohee.database.OnePreference;
import com.boohee.one.R;
import com.boohee.utils.Helper;

public class UserTargetView extends FrameLayout {
    static final String TAG = UserTargetView.class.getName();
    Context ctx;
    private float defaultWeight;
    private boolean fromProfile = false;
    Handler handler;
    public float targetWeight;

    public UserTargetView(Context context, boolean fromProfile) {
        super(context);
        this.fromProfile = fromProfile;
        if (UserProfileActivity.user != null) {
            this.defaultWeight = UserProfileActivity.user.target_weight;
        } else {
            this.defaultWeight = 0.0f;
        }
        init();
    }

    public UserTargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserTargetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void init() {
        this.ctx = getContext();
        this.targetWeight = this.defaultWeight;
        LayoutInflater.from(this.ctx).inflate(R.layout.o3, this);
        RadioButton lose_weight = (RadioButton) findViewById(R.id.lose_weight);
        RadioButton keep_weight = (RadioButton) findViewById(R.id.keep_weight);
        ((RadioGroup) findViewById(R.id.choose_group)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                OnePreference onePreference = OnePreference.getInstance(UserTargetView.this.ctx);
                switch (checkedId) {
                    case R.id.lose_weight:
                        Helper.showLog(UserTargetView.TAG, "---1");
                        if (UserTargetView.this.fromProfile) {
                            if (UserTargetView.this.defaultWeight > 0.0f) {
                                UserTargetView.this.targetWeight = UserTargetView.this
                                        .defaultWeight;
                            } else {
                                UserTargetView.this.targetWeight = 0.0f;
                            }
                        } else if (UserTargetView.this.handler != null) {
                            UserTargetView.this.handler.sendEmptyMessage(UserInitActivity
                                    .ACTION_NEXT);
                        } else {
                            return;
                        }
                        onePreference.putInt("target_mode", 1);
                        return;
                    case R.id.keep_weight:
                        Helper.showLog(UserTargetView.TAG, "---2");
                        if (UserTargetView.this.fromProfile) {
                            UserTargetView.this.targetWeight = -1.0f;
                        } else if (UserTargetView.this.handler != null) {
                            UserTargetView.this.handler.sendEmptyMessage(UserInitActivity
                                    .ACTION_SAVE);
                        } else {
                            return;
                        }
                        onePreference.putInt("target_mode", 0);
                        return;
                    default:
                        return;
                }
            }
        });
        if (this.targetWeight == -1.0f) {
            keep_weight.setChecked(true);
            return;
        }
        this.targetWeight = this.defaultWeight;
        lose_weight.setChecked(true);
    }

    public float getUserTargetWeight() {
        return this.targetWeight;
    }
}
