package com.boohee.widgets;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.boohee.one.R;

public class DraftPopwindow implements OnClickListener {
    private Animation         inAnim;
    private IPopClickListener listener;
    private Context           mContext;
    private LinearLayout      popLayout;
    private PopupWindow       popWindow;
    private Button            redoBtn;

    public interface IPopClickListener {
        void onRedoClick();

        void onSaveClick();

        void onUnSaveClick();
    }

    public DraftPopwindow(Context context, IPopClickListener listener) {
        this(context, listener, false);
    }

    public DraftPopwindow(Context context, IPopClickListener listener, boolean isSendText) {
        this.mContext = context;
        this.listener = listener;
        this.inAnim = AnimationUtils.loadAnimation(this.mContext, R.anim.s);
        init(isSendText);
    }

    public void show() {
        if (this.popWindow != null) {
            if (this.popWindow.isShowing()) {
                this.popWindow.dismiss();
                return;
            }
            this.popWindow.showAtLocation(new View(this.mContext), 48, 0, 0);
            this.popLayout.startAnimation(this.inAnim);
        }
    }

    public boolean isShowing() {
        if (this.popWindow == null) {
            return false;
        }
        return this.popWindow.isShowing();
    }

    public void dismiss() {
        if (this.popWindow != null) {
            this.popWindow.dismiss();
        }
    }

    private void init(boolean isSendText) {
        this.popWindow = new PopupWindow(getContentView(isSendText), -1, -2, true);
        this.popWindow.setTouchable(true);
        this.popWindow.setOutsideTouchable(true);
    }

    private View getContentView(boolean isSendText) {
        View contentView = View.inflate(this.mContext, R.layout.oz, null);
        if (!isSendText) {
            this.redoBtn = (Button) contentView.findViewById(R.id.redoBtn);
            this.redoBtn.setOnClickListener(this);
            this.redoBtn.setVisibility(0);
        }
        this.popLayout = (LinearLayout) contentView.findViewById(R.id.popLayout);
        contentView.findViewById(R.id.mask).setOnClickListener(this);
        contentView.findViewById(R.id.saveDraftBtn).setOnClickListener(this);
        contentView.findViewById(R.id.unsaveBtn).setOnClickListener(this);
        contentView.findViewById(R.id.cancelBtn).setOnClickListener(this);
        return contentView;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mask:
                this.popWindow.dismiss();
                return;
            case R.id.cancelBtn:
                this.popWindow.dismiss();
                return;
            case R.id.redoBtn:
                this.listener.onRedoClick();
                this.popWindow.dismiss();
                return;
            case R.id.saveDraftBtn:
                this.listener.onSaveClick();
                this.popWindow.dismiss();
                return;
            case R.id.unsaveBtn:
                this.listener.onUnSaveClick();
                this.popWindow.dismiss();
                return;
            default:
                return;
        }
    }

    public void setReDoBtnText(String text) {
        if (this.redoBtn != null) {
            this.redoBtn.setText(text);
        }
    }
}
