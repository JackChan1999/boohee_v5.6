package com.boohee.user.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boohee.database.UserPreference;
import com.boohee.myview.IntWheelView;
import com.boohee.one.R;
import com.boohee.one.mine.McInitActivity;
import com.boohee.utils.Helper;

public class UserMcCircleView extends FrameLayout {
    static final String TAG = UserMcCircleView.class.getName();
    private Context      ctx;
    private int          defaultValue;
    private int          fromWhere;
    private IntWheelView wheelView;

    public UserMcCircleView(Context context, int fromWhere, int defaultValue) {
        super(context);
        this.fromWhere = fromWhere;
        this.defaultValue = defaultValue;
        init();
    }

    public UserMcCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public UserMcCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.ctx = getContext();
        addView(LayoutInflater.from(this.ctx).inflate(R.layout.nz, null));
        setUserProperty();
        setBottomPicker();
    }

    private void setUserProperty() {
        ((TextView) findViewById(R.id.user_property_text)).setText(this.ctx.getString(R.string
                .mc_circle_unit));
    }

    public void setBottomPicker() {
        LinearLayout picker_layout = (LinearLayout) findViewById(R.id.picker_layout);
        picker_layout.removeAllViews();
        if (this.fromWhere == 0) {
            this.defaultValue = McInitActivity.defaultMcdays;
        }
        Helper.showLog(TAG, "fromWhere =" + this.fromWhere + "default=" + this.defaultValue);
        this.wheelView = new IntWheelView(this.ctx, 90, this.defaultValue, UserPreference
                .getInstance(this.ctx).getInt("cycle"));
        picker_layout.addView(this.wheelView);
    }

    public int getMcCircle() {
        return this.wheelView.getCurrentNum();
    }
}
