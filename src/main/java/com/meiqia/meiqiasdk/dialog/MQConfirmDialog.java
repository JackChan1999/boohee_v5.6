package com.meiqia.meiqiasdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;

public class MQConfirmDialog extends Dialog implements OnClickListener {
    private TextView         mContentTv;
    private OnDialogCallback mOnDialogCallback;
    private TextView         mTitleTv;

    public interface OnDialogCallback {
        void onClickCancel();

        void onClickConfirm();
    }

    public MQConfirmDialog(Activity activity, @StringRes int titleResId, @StringRes int
            contentResId, OnDialogCallback onDialogCallback) {
        this(activity, activity.getString(titleResId), activity.getString(contentResId),
                onDialogCallback);
    }

    public MQConfirmDialog(Activity activity, String title, String content, OnDialogCallback
            onDialogCallback) {
        super(activity, R.style.MQDialog);
        getWindow().setLayout(-1, -2);
        setContentView(R.layout.mq_dialog_confirm);
        this.mTitleTv = (TextView) findViewById(R.id.tv_comfirm_title);
        this.mContentTv = (TextView) findViewById(R.id.tv_comfirm_content);
        findViewById(R.id.tv_confirm_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm_confirm).setOnClickListener(this);
        setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                MQConfirmDialog.this.mOnDialogCallback.onClickCancel();
            }
        });
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        this.mOnDialogCallback = onDialogCallback;
        this.mTitleTv.setText(title);
        this.mContentTv.setText(content);
    }

    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.tv_confirm_cancel) {
            this.mOnDialogCallback.onClickCancel();
        } else if (v.getId() == R.id.tv_confirm_confirm) {
            this.mOnDialogCallback.onClickConfirm();
        }
    }

    public void setTitle(String title) {
        this.mTitleTv.setText(title);
    }

    public void setTtitle(@StringRes int titleResId) {
        this.mTitleTv.setText(titleResId);
    }

    public void setContent(String content) {
        this.mContentTv.setText(content);
    }

    public void setContent(@StringRes int contentResId) {
        this.mContentTv.setText(contentResId);
    }
}
