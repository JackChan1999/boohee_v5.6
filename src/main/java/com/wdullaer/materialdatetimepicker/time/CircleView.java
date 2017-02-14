package com.wdullaer.materialdatetimepicker.time;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.wdullaer.materialdatetimepicker.R;

public class CircleView extends View {
    private static final String TAG = "CircleView";
    private float   mAmPmCircleRadiusMultiplier;
    private int     mCircleColor;
    private int     mCircleRadius;
    private float   mCircleRadiusMultiplier;
    private int     mDotColor;
    private boolean mDrawValuesReady;
    private boolean mIs24HourMode;
    private       boolean mIsInitialized = false;
    private final Paint   mPaint         = new Paint();
    private int mXCenter;
    private int mYCenter;

    public CircleView(Context context) {
        super(context);
    }

    public void initialize(Context context, TimePickerController controller) {
        if (this.mIsInitialized) {
            Log.e(TAG, "CircleView may only be initialized once.");
            return;
        }
        Resources res = context.getResources();
        this.mCircleColor = res.getColor(controller.isThemeDark() ? R.color
                .mdtp_circle_background_dark_theme : R.color.mdtp_circle_color);
        this.mDotColor = controller.getAccentColor();
        this.mPaint.setAntiAlias(true);
        this.mIs24HourMode = controller.is24HourMode();
        if (this.mIs24HourMode) {
            this.mCircleRadiusMultiplier = Float.parseFloat(res.getString(R.string
                    .mdtp_circle_radius_multiplier_24HourMode));
        } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(res.getString(R.string
                    .mdtp_circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(res.getString(R.string
                    .mdtp_ampm_circle_radius_multiplier));
        }
        this.mIsInitialized = true;
    }

    public void onDraw(Canvas canvas) {
        if (getWidth() != 0 && this.mIsInitialized) {
            if (!this.mDrawValuesReady) {
                this.mXCenter = getWidth() / 2;
                this.mYCenter = getHeight() / 2;
                this.mCircleRadius = (int) (((float) Math.min(this.mXCenter, this.mYCenter)) *
                        this.mCircleRadiusMultiplier);
                if (!this.mIs24HourMode) {
                    this.mYCenter = (int) (((double) this.mYCenter) - (((double) ((int) (((float)
                            this.mCircleRadius) * this.mAmPmCircleRadiusMultiplier))) * 0.75d));
                }
                this.mDrawValuesReady = true;
            }
            this.mPaint.setColor(this.mCircleColor);
            canvas.drawCircle((float) this.mXCenter, (float) this.mYCenter, (float) this
                    .mCircleRadius, this.mPaint);
            this.mPaint.setColor(this.mDotColor);
            canvas.drawCircle((float) this.mXCenter, (float) this.mYCenter, 8.0f, this.mPaint);
        }
    }
}
