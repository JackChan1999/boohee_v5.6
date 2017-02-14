package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boohee.model.User;
import com.boohee.one.R;

public class UserGenderView extends FrameLayout {
    static final String TAG = UserGenderView.class.getName();
    Context    ctx;
    RadioGroup group;
    User       user;

    public UserGenderView(Context context) {
        super(context);
        init();
    }

    public UserGenderView(Context context, User user) {
        super(context);
        this.user = user;
        init();
    }

    public UserGenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserGenderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.nx, null));
        setDefaultChecked();
    }

    private void setDefaultChecked() {
        if (this.user != null && this.user.isMale()) {
            ((RadioButton) findViewById(R.id.male)).setChecked(true);
        }
    }

    public String getSexType() {
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.gender_group);
        return (genderGroup.indexOfChild(genderGroup.findViewById(genderGroup
                .getCheckedRadioButtonId())) + 1) + "";
    }
}
