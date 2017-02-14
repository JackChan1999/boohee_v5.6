package com.boohee.widgets;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.api.StatusApi;
import com.boohee.database.OnePreference;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.utils.Helper;

import org.json.JSONObject;

public class CheckView extends RelativeLayout {
    @InjectView(2131429211)
    Button btnPos;
    private Context       mContext;
    private LayoutParams  mLp;
    private WindowManager mWindowManager;
    @InjectView(2131429208)
    TextView txtMsg;
    @InjectView(2131429207)
    TextView txtTitle;

    public CheckView(Context context) {
        this(context, null);
    }

    public CheckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        LayoutInflater.from(getContext()).inflate(R.layout.oi, this);
        ButterKnife.inject((View) this);
    }

    public void attachToWindow() {
        if (getParent() == null) {
            this.mLp = new LayoutParams();
            if (VERSION.SDK_INT >= 19) {
                this.mLp.type = 2005;
            } else {
                this.mLp.type = 2002;
            }
            this.mLp.format = 1;
            this.mLp.flags = 40;
            this.mLp.gravity = 8388659;
            this.mLp.width = -1;
            this.mLp.height = -2;
            this.mLp.x = 0;
            this.mLp.y = 0;
            this.mWindowManager.addView(this, this.mLp);
        }
    }

    @OnClick({2131429211})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pos:
                postCheckIn();
                return;
            default:
                return;
        }
    }

    public void dismiss() {
        if (getParent() != null && this.mWindowManager != null) {
            this.mWindowManager.removeView(this);
        }
    }

    public void setMsg(String str) {
        if (this.txtMsg != null && !TextUtils.isEmpty(str)) {
            this.txtMsg.setText(str);
        }
    }

    private void postCheckIn() {
        StatusApi.checkIn(this.mContext, new JsonCallback(this.mContext) {
            public void ok(JSONObject object) {
                super.ok(object);
                CharSequence message = object.optString("message");
                if (!TextUtils.isEmpty(message)) {
                    Helper.showToast(message);
                }
                OnePreference.setPrefSignRecord();
            }

            public void onFinish() {
                super.onFinish();
                CheckView.this.dismiss();
            }
        });
    }
}
