package com.boohee.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.one.R;

public class AlertDialogBuilder {
    private Button    btn_neg;
    private Button    btn_pos;
    private Context   context;
    private Dialog    dialog;
    private ImageView img_line;
    private boolean showMsg    = false;
    private boolean showNegBtn = false;
    private boolean showPosBtn = false;
    private boolean showTitle  = false;
    private TextView txt_msg;
    private TextView txt_title;

    public AlertDialogBuilder(Context context) {
        this.context = context;
    }

    public AlertDialogBuilder builder() {
        View view = LayoutInflater.from(this.context).inflate(R.layout.o5, null);
        this.txt_title = (TextView) view.findViewById(R.id.txt_title);
        this.txt_title.setVisibility(8);
        this.txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        this.txt_msg.setVisibility(8);
        this.btn_neg = (Button) view.findViewById(R.id.btn_neg);
        this.btn_neg.setVisibility(8);
        this.btn_pos = (Button) view.findViewById(R.id.btn_pos);
        this.btn_pos.setVisibility(8);
        this.img_line = (ImageView) view.findViewById(R.id.img_line);
        this.img_line.setVisibility(8);
        this.dialog = new Dialog(this.context, R.style.d);
        this.dialog.setContentView(view);
        return this;
    }

    public AlertDialogBuilder setTitle(String title) {
        this.showTitle = true;
        if ("".equals(title)) {
            this.txt_title.setText("标题");
        } else {
            this.txt_title.setText(title);
        }
        return this;
    }

    public AlertDialogBuilder setMsg(String msg) {
        this.showMsg = true;
        if ("".equals(msg)) {
            this.txt_msg.setText("消息");
        } else {
            this.txt_msg.setText(msg);
        }
        return this;
    }

    public AlertDialogBuilder setCancelable(boolean cancel) {
        this.dialog.setCancelable(cancel);
        return this;
    }

    public AlertDialogBuilder setPositiveButton(String text, final OnClickListener listener) {
        this.showPosBtn = true;
        if ("".equals(text)) {
            this.btn_pos.setText("确定");
        } else {
            this.btn_pos.setText(text);
        }
        this.btn_pos.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listener.onClick(v);
                AlertDialogBuilder.this.dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialogBuilder setNegativeButton(String text, final OnClickListener listener) {
        this.showNegBtn = true;
        if ("".equals(text)) {
            this.btn_neg.setText("取消");
        } else {
            this.btn_neg.setText(text);
        }
        this.btn_neg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                listener.onClick(v);
                AlertDialogBuilder.this.dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!(this.showTitle || this.showMsg)) {
            this.txt_title.setText("标题ʾ");
            this.txt_title.setVisibility(0);
        }
        if (this.showTitle) {
            this.txt_title.setVisibility(0);
        }
        if (this.showMsg) {
            this.txt_msg.setVisibility(0);
        }
        if (!(this.showPosBtn || this.showNegBtn)) {
            this.btn_pos.setText("确定");
            this.btn_pos.setVisibility(0);
            this.btn_pos.setBackgroundResource(R.drawable.bf);
            this.btn_pos.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    AlertDialogBuilder.this.dialog.dismiss();
                }
            });
        }
        if (this.showPosBtn && this.showNegBtn) {
            this.btn_pos.setVisibility(0);
            this.btn_pos.setBackgroundResource(R.drawable.bh);
            this.btn_neg.setVisibility(0);
            this.btn_neg.setBackgroundResource(R.drawable.bg);
            this.img_line.setVisibility(0);
        }
        if (this.showPosBtn && !this.showNegBtn) {
            this.btn_pos.setVisibility(0);
            this.btn_pos.setBackgroundResource(R.drawable.bf);
        }
        if (!this.showPosBtn && this.showNegBtn) {
            this.btn_neg.setVisibility(0);
            this.btn_neg.setBackgroundResource(R.drawable.bf);
        }
    }

    public void show() {
        setLayout();
        this.dialog.show();
    }
}
