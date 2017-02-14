package com.boohee.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.boohee.one.R;

public class SelectSportDataSrcDialog extends Dialog {
    private OnClickListener bindLeDongLiL;
    private OnClickListener bindMiBandL;
    private Button          leDongLiBtn;
    private Button          miBandBtn;

    private class LeDongLiBindL implements OnClickListener {
        private LeDongLiBindL() {
        }

        public void onClick(View v) {
            if (SelectSportDataSrcDialog.this.bindLeDongLiL != null) {
                SelectSportDataSrcDialog.this.bindLeDongLiL.onClick(v);
            }
        }
    }

    private class MiBandBindL implements OnClickListener {
        private MiBandBindL() {
        }

        public void onClick(View v) {
            if (SelectSportDataSrcDialog.this.bindMiBandL != null) {
                SelectSportDataSrcDialog.this.bindMiBandL.onClick(v);
            }
        }
    }

    public SelectSportDataSrcDialog(Context context) {
        super(context, R.style.fp);
        init();
        initView();
    }

    private void init() {
        setContentView(R.layout.qt);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    private void initView() {
        findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SelectSportDataSrcDialog.this.dismiss();
            }
        });
        this.miBandBtn = (Button) findViewById(R.id.btn_miband);
        this.leDongLiBtn = (Button) findViewById(R.id.btn_ledongli);
        this.miBandBtn.setOnClickListener(new MiBandBindL());
        this.leDongLiBtn.setOnClickListener(new LeDongLiBindL());
    }

    public void setBindMiBandListener(OnClickListener listener) {
        this.bindMiBandL = listener;
    }

    public void setBindLeDongLiListener(OnClickListener listener) {
        this.bindLeDongLiL = listener;
    }

    public void setLedongLiVisibility(boolean show) {
        if (this.leDongLiBtn != null) {
            this.leDongLiBtn.setVisibility(show ? 0 : 8);
        }
    }

    public void setMiBandVisibility(boolean show) {
        if (this.miBandBtn != null) {
            this.miBandBtn.setVisibility(show ? 0 : 8);
        }
    }
}
