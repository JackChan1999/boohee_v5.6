package com.boohee.one.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;
import com.boohee.utility.Event;
import com.google.zxing.Result;
import com.umeng.analytics.MobclickAgent;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

public class ScannerActivity extends BaseActivity implements ResultHandler {
    public static final String CODE_DATA    = "CODE_DATA";
    public static final int    REQUEST_CODE = 175;
    @InjectView(2131427860)
    FrameLayout flScanner;
    private ZXingScannerView mScannerView;

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT         = "请将红线与条形码垂直，即可自动扫描";
        public static final int    TRADE_MARK_TEXT_SIZE_SP = 14;
        public final        Paint  PAINT                   = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            this.PAINT.setColor(-1);
            this.PAINT.setAntiAlias(true);
            this.PAINT.setTextSize(TypedValue.applyDimension(2, 14.0f, getResources()
                    .getDisplayMetrics()));
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            float tradeMarkTop;
            float tradeMarkLeft;
            float tradeMarkRight;
            float offset;
            Rect framingRect = getFramingRect();
            if (framingRect != null) {
                tradeMarkTop = (((float) framingRect.bottom) + this.PAINT.getTextSize()) + 10.0f;
                tradeMarkLeft = (float) framingRect.left;
                tradeMarkRight = (float) framingRect.right;
            } else {
                tradeMarkTop = 10.0f;
                tradeMarkLeft = (((float) canvas.getHeight()) - this.PAINT.getTextSize()) - 10.0f;
                tradeMarkRight = (((float) canvas.getWidth()) - this.PAINT.getTextSize()) - 10.0f;
            }
            float textWidth = this.PAINT.measureText(TRADE_MARK_TEXT);
            if ((tradeMarkRight - tradeMarkLeft) - textWidth > 0.0f) {
                offset = tradeMarkLeft + (((tradeMarkRight - tradeMarkLeft) - textWidth) / 2.0f);
            } else {
                offset = tradeMarkLeft;
            }
            canvas.drawText(TRADE_MARK_TEXT, offset, tradeMarkTop, this.PAINT);
        }
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        MobclickAgent.onEvent(this.ctx, Event.tool_searchfood_scan);
        setContentView(R.layout.ct);
        ButterKnife.inject((Activity) this);
        this.mScannerView = new ZXingScannerView(this) {
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        this.flScanner.addView(this.mScannerView, new LayoutParams(-1, -1));
    }

    public void onResume() {
        super.onResume();
        this.mScannerView.setResultHandler(this);
        this.mScannerView.startCamera();
    }

    public void onPause() {
        super.onPause();
        this.mScannerView.stopCamera();
    }

    public void handleResult(Result rawResult) {
        Intent data = new Intent();
        data.putExtra(CODE_DATA, rawResult.getText());
        setResult(175, data);
        finish();
    }

    public static void startScannerForResult(Context context, int requestCode) {
        if (context != null && (context instanceof Activity)) {
            ((Activity) context).startActivityForResult(new Intent(context, ScannerActivity
                    .class), requestCode);
            ((Activity) context).overridePendingTransition(R.anim.o, R.anim.r);
        }
    }
}
