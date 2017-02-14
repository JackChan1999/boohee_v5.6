package me.dm7.barcodescanner.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.umeng.socialize.common.SocializeConstants;

public class ViewFinderView extends View implements IViewFinder {
    private static final long ANIMATION_DELAY = 80;
    private static final float LANDSCAPE_HEIGHT_RATIO = 0.625f;
    private static final int LANDSCAPE_MAX_FRAME_HEIGHT = 675;
    private static final int LANDSCAPE_MAX_FRAME_WIDTH = 1200;
    private static final float LANDSCAPE_WIDTH_RATIO = 0.625f;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int POINT_SIZE = 10;
    private static final float PORTRAIT_HEIGHT_RATIO = 0.375f;
    private static final int PORTRAIT_MAX_FRAME_HEIGHT = 720;
    private static final int PORTRAIT_MAX_FRAME_WIDTH = 945;
    private static final float PORTRAIT_WIDTH_RATIO = 0.875f;
    private static final int[] SCANNER_ALPHA = new int[]{0, 64, 128, 192, 255, 192, 128, 64};
    private static final String TAG = "ViewFinderView";
    protected int mBorderLineLength;
    protected Paint mBorderPaint;
    private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
    private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);
    private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
    private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
    private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
    protected Paint mFinderMaskPaint;
    private Rect mFramingRect;
    protected Paint mLaserPaint;
    private int scannerAlpha;

    public ViewFinderView(Context context) {
        super(context);
        init();
    }

    public ViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.mLaserPaint = new Paint();
        this.mLaserPaint.setColor(this.mDefaultLaserColor);
        this.mLaserPaint.setStyle(Style.FILL);
        this.mFinderMaskPaint = new Paint();
        this.mFinderMaskPaint.setColor(this.mDefaultMaskColor);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setColor(this.mDefaultBorderColor);
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setStrokeWidth((float) this.mDefaultBorderStrokeWidth);
        this.mBorderLineLength = this.mDefaultBorderLineLength;
    }

    public void setLaserColor(int laserColor) {
        this.mLaserPaint.setColor(laserColor);
    }

    public void setMaskColor(int maskColor) {
        this.mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor) {
        this.mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth) {
        this.mBorderPaint.setStrokeWidth((float) borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        this.mBorderLineLength = borderLineLength;
    }

    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    public Rect getFramingRect() {
        return this.mFramingRect;
    }

    public void onDraw(Canvas canvas) {
        if (this.mFramingRect != null) {
            drawViewFinderMask(canvas);
            drawViewFinderBorder(canvas);
            drawLaser(canvas);
        }
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0.0f, 0.0f, (float) width, (float) this.mFramingRect.top, this.mFinderMaskPaint);
        canvas.drawRect(0.0f, (float) this.mFramingRect.top, (float) this.mFramingRect.left, (float) (this.mFramingRect.bottom + 1), this.mFinderMaskPaint);
        canvas.drawRect((float) (this.mFramingRect.right + 1), (float) this.mFramingRect.top, (float) width, (float) (this.mFramingRect.bottom + 1), this.mFinderMaskPaint);
        canvas.drawRect(0.0f, (float) (this.mFramingRect.bottom + 1), (float) width, (float) height, this.mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        canvas.drawLine((float) (this.mFramingRect.left - 1), (float) (this.mFramingRect.top - 1), (float) (this.mFramingRect.left - 1), (float) ((this.mFramingRect.top - 1) + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.left - 1), (float) (this.mFramingRect.top - 1), (float) ((this.mFramingRect.left - 1) + this.mBorderLineLength), (float) (this.mFramingRect.top - 1), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.left - 1), (float) (this.mFramingRect.bottom + 1), (float) (this.mFramingRect.left - 1), (float) ((this.mFramingRect.bottom + 1) - this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.left - 1), (float) (this.mFramingRect.bottom + 1), (float) ((this.mFramingRect.left - 1) + this.mBorderLineLength), (float) (this.mFramingRect.bottom + 1), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.right + 1), (float) (this.mFramingRect.top - 1), (float) (this.mFramingRect.right + 1), (float) ((this.mFramingRect.top - 1) + this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.right + 1), (float) (this.mFramingRect.top - 1), (float) ((this.mFramingRect.right + 1) - this.mBorderLineLength), (float) (this.mFramingRect.top - 1), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.right + 1), (float) (this.mFramingRect.bottom + 1), (float) (this.mFramingRect.right + 1), (float) ((this.mFramingRect.bottom + 1) - this.mBorderLineLength), this.mBorderPaint);
        canvas.drawLine((float) (this.mFramingRect.right + 1), (float) (this.mFramingRect.bottom + 1), (float) ((this.mFramingRect.right + 1) - this.mBorderLineLength), (float) (this.mFramingRect.bottom + 1), this.mBorderPaint);
    }

    public void drawLaser(Canvas canvas) {
        this.mLaserPaint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
        this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
        int middle = (this.mFramingRect.height() / 2) + this.mFramingRect.top;
        canvas.drawRect((float) (this.mFramingRect.left + 2), (float) (middle - 1), (float) (this.mFramingRect.right - 1), (float) (middle + 2), this.mLaserPaint);
        postInvalidateDelayed(ANIMATION_DELAY, this.mFramingRect.left - 10, this.mFramingRect.top - 10, this.mFramingRect.right + 10, this.mFramingRect.bottom + 10);
    }

    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        int width;
        int height;
        Point viewResolution = new Point(getWidth(), getHeight());
        if (DisplayUtils.getScreenOrientation(getContext()) != 1) {
            width = findDesiredDimensionInRange(0.625f, viewResolution.x, SocializeConstants.MASK_USER_CENTER_HIDE_AREA, LANDSCAPE_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(0.625f, viewResolution.y, SocializeConstants.MASK_USER_CENTER_HIDE_AREA, LANDSCAPE_MAX_FRAME_HEIGHT);
        } else {
            width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO, viewResolution.x, SocializeConstants.MASK_USER_CENTER_HIDE_AREA, PORTRAIT_MAX_FRAME_WIDTH);
            height = findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO, viewResolution.y, SocializeConstants.MASK_USER_CENTER_HIDE_AREA, PORTRAIT_MAX_FRAME_HEIGHT);
        }
        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        this.mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
    }

    private static int findDesiredDimensionInRange(float ratio, int resolution, int hardMin, int hardMax) {
        int dim = (int) (((float) resolution) * ratio);
        if (dim < hardMin) {
            return hardMin;
        }
        if (dim > hardMax) {
            return hardMax;
        }
        return dim;
    }
}
