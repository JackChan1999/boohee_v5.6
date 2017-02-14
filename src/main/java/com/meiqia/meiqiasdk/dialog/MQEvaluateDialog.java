package com.meiqia.meiqiasdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.util.MQUtils;

public class MQEvaluateDialog extends Dialog implements OnClickListener, OnCheckedChangeListener {
    private Callback mCallback;
    private TextView mConfirmTv;
    private EditText   mContentEt = ((EditText) findViewById(R.id.et_evaluate_content));
    private RadioGroup mContentRg = ((RadioGroup) findViewById(R.id.rg_evaluate_content));

    public interface Callback {
        void executeEvaluate(int i, String str);
    }

    public MQEvaluateDialog(Activity activity) {
        super(activity, R.style.MQDialog);
        setContentView(R.layout.mq_dialog_evaluate);
        getWindow().setLayout(-1, -2);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        this.mContentRg.setOnCheckedChangeListener(this);
        findViewById(R.id.tv_evaluate_cancel).setOnClickListener(this);
        this.mConfirmTv = (TextView) findViewById(R.id.tv_evaluate_confirm);
        this.mConfirmTv.setOnClickListener(this);
    }

    public void onClick(View v) {
        MQUtils.closeKeyboard((Dialog) this);
        dismiss();
        if (v.getId() == R.id.tv_evaluate_confirm && this.mCallback != null) {
            int level = 2;
            int checkedId = this.mContentRg.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_evaluate_medium) {
                level = 1;
            } else if (checkedId == R.id.rb_evaluate_bad) {
                level = 0;
            }
            this.mCallback.executeEvaluate(level, this.mContentEt.getText().toString().trim());
        }
        this.mContentEt.setText("");
        this.mContentEt.clearFocus();
        this.mContentRg.check(R.id.rb_evaluate_good);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        this.mContentEt.clearFocus();
        MQUtils.closeKeyboard((Dialog) this);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
