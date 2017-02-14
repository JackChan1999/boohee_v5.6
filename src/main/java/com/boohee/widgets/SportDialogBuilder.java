package com.boohee.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;

public class SportDialogBuilder {
    @InjectView(2131429209)
    Button btnNeg;
    @InjectView(2131429211)
    Button btnPos;
    private Context context;
    private Dialog  dialog;
    @InjectView(2131429210)
    ImageView      imgLine;
    @InjectView(2131428431)
    ImageView      ivBackground;
    @InjectView(2131427481)
    ImageView      ivIcon;
    @InjectView(2131429432)
    LinearLayout   llBg;
    @InjectView(2131428096)
    RelativeLayout llTop;
    private boolean showImageBg = false;
    private boolean showNegBtn  = false;
    private boolean showPosBtn  = false;
    @InjectView(2131427461)
    TextView tvCalory;
    @InjectView(2131427736)
    TextView tvTime;
    @InjectView(2131428175)
    TextView tvTitle;

    public SportDialogBuilder(Context context) {
        this.context = context;
    }

    public SportDialogBuilder builder() {
        View view = LayoutInflater.from(this.context).inflate(R.layout.qu, null);
        ButterKnife.inject((Object) this, view);
        this.btnNeg.setVisibility(8);
        this.imgLine.setVisibility(8);
        this.btnPos.setVisibility(8);
        this.ivBackground.setVisibility(8);
        this.dialog = new Dialog(this.context, R.style.d);
        this.dialog.setContentView(view);
        this.llBg.setLayoutParams(new LayoutParams((int) (((double) this.context.getResources()
                .getDisplayMetrics().widthPixels) * 0.5d), -2));
        return this;
    }

    public SportDialogBuilder setRedBcakground() {
        this.llTop.setBackgroundResource(R.drawable.h5);
        this.ivIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.vv));
        return this;
    }

    public SportDialogBuilder setYellowBcakground() {
        this.llTop.setBackgroundResource(R.drawable.iv);
        this.ivIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.vu));
        return this;
    }

    public SportDialogBuilder setGreenBackground() {
        this.llTop.setBackgroundResource(R.drawable.em);
        this.ivIcon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.vx));
        this.showImageBg = true;
        return this;
    }

    public SportDialogBuilder setShowMsg(String title, int time, int calory) {
        TextView textView = this.tvTitle;
        if (title == null) {
            title = "";
        }
        textView.setText(title);
        this.tvTime.setText(String.valueOf(time));
        this.tvCalory.setText(String.valueOf(calory));
        return this;
    }

    public SportDialogBuilder setPositiveButton(String text, final OnClickListener listener) {
        this.showPosBtn = true;
        if ("".equals(text)) {
            this.btnPos.setText("确定");
        } else {
            this.btnPos.setText(text);
        }
        this.btnPos.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listener.onClick(v);
                SportDialogBuilder.this.dialog.dismiss();
            }
        });
        return this;
    }

    public SportDialogBuilder setNegativeButton(String text, final OnClickListener listener) {
        this.showNegBtn = true;
        if ("".equals(text)) {
            this.btnNeg.setText("取消");
        } else {
            this.btnNeg.setText(text);
        }
        this.btnNeg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listener.onClick(v);
                SportDialogBuilder.this.dialog.dismiss();
            }
        });
        return this;
    }

    public SportDialogBuilder setOnlyOneButton(String text, final OnClickListener listener) {
        this.showNegBtn = false;
        this.showPosBtn = false;
        if (TextUtils.isEmpty(text)) {
            this.btnPos.setText("确定");
        } else {
            this.btnPos.setText(text);
        }
        this.btnPos.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listener.onClick(v);
                SportDialogBuilder.this.dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!(this.showPosBtn || this.showNegBtn)) {
            this.btnPos.setVisibility(0);
            this.btnPos.setBackgroundResource(R.drawable.bf);
        }
        if (this.showPosBtn && this.showNegBtn) {
            this.btnPos.setVisibility(0);
            this.btnPos.setBackgroundResource(R.drawable.bh);
            this.btnNeg.setVisibility(0);
            this.btnNeg.setBackgroundResource(R.drawable.bg);
            this.imgLine.setVisibility(0);
        }
        if (this.showPosBtn && !this.showNegBtn) {
            this.btnPos.setVisibility(0);
            this.btnPos.setBackgroundResource(R.drawable.bf);
        }
        if (!this.showPosBtn && this.showNegBtn) {
            this.btnNeg.setVisibility(0);
            this.btnNeg.setBackgroundResource(R.drawable.bf);
        }
        if (this.showImageBg) {
            this.ivBackground.setVisibility(0);
        }
    }

    public SportDialogBuilder setCancelable(boolean cancelable) {
        this.dialog.setCancelable(cancelable);
        return this;
    }

    public SportDialogBuilder setCanceldOnTouchOutSide(boolean cancelable) {
        this.dialog.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public void show() {
        setLayout();
        this.dialog.show();
    }
}
