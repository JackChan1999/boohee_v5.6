package com.joooonho;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SelectableRoundedImageView extends ImageView {
    private static final int         DEFAULT_BORDER_COLOR = -16777216;
    public static final  String      TAG                  = "SelectableRoundedImageView";
    private static final ScaleType[] sScaleTypeArray      = new ScaleType[]{ScaleType.MATRIX,
            ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END,
            ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    private boolean        isOval;
    private ColorStateList mBorderColor;
    private float          mBorderWidth;
    private Drawable       mDrawable;
    private float          mLeftBottomCornerRadius;
    private float          mLeftTopCornerRadius;
    private float[]        mRadii;
    private int            mResource;
    private float          mRightBottomCornerRadius;
    private float          mRightTopCornerRadius;
    private ScaleType      mScaleType;

    static class SelectableRoundedCornerDrawable extends Drawable {
        private static final int    DEFAULT_BORDER_COLOR = -16777216;
        private static final String TAG                  = "SelectableRoundedCornerDrawable";
        private       Bitmap mBitmap;
        private final int    mBitmapHeight;
        private final Paint  mBitmapPaint;
        private final RectF mBitmapRect = new RectF();
        private       BitmapShader mBitmapShader;
        private final int          mBitmapWidth;
        private RectF          mBorderBounds = new RectF();
        private ColorStateList mBorderColor  = ColorStateList.valueOf(-16777216);
        private final Paint mBorderPaint;
        private float[]   mBorderRadii      = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0
                .0f, 0.0f};
        private float     mBorderWidth      = 0.0f;
        private RectF     mBounds           = new RectF();
        private boolean   mBoundsConfigured = false;
        private boolean   mOval             = false;
        private Path      mPath             = new Path();
        private float[]   mRadii            = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0
                .0f, 0.0f};
        private ScaleType mScaleType        = ScaleType.FIT_CENTER;

        public SelectableRoundedCornerDrawable(Bitmap bitmap, Resources r) {
            this.mBitmap = bitmap;
            this.mBitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
            if (bitmap != null) {
                this.mBitmapWidth = bitmap.getScaledWidth(r.getDisplayMetrics());
                this.mBitmapHeight = bitmap.getScaledHeight(r.getDisplayMetrics());
            } else {
                this.mBitmapHeight = -1;
                this.mBitmapWidth = -1;
            }
            this.mBitmapRect.set(0.0f, 0.0f, (float) this.mBitmapWidth, (float) this.mBitmapHeight);
            this.mBitmapPaint = new Paint(1);
            this.mBitmapPaint.setStyle(Style.FILL);
            this.mBitmapPaint.setShader(this.mBitmapShader);
            this.mBorderPaint = new Paint(1);
            this.mBorderPaint.setStyle(Style.STROKE);
            this.mBorderPaint.setColor(this.mBorderColor.getColorForState(getState(), -16777216));
            this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
        }

        public static SelectableRoundedCornerDrawable fromBitmap(Bitmap bitmap, Resources r) {
            if (bitmap != null) {
                return new SelectableRoundedCornerDrawable(bitmap, r);
            }
            return null;
        }

        public static Drawable fromDrawable(Drawable drawable, Resources r) {
            if (drawable == null || (drawable instanceof SelectableRoundedCornerDrawable)) {
                return drawable;
            }
            if (drawable instanceof LayerDrawable) {
                Drawable ld = (LayerDrawable) drawable;
                int num = ld.getNumberOfLayers();
                for (int i = 0; i < num; i++) {
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(ld.getDrawable(i), r));
                }
                return ld;
            }
            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new SelectableRoundedCornerDrawable(bm, r);
            }
            Log.w(TAG, "Failed to create bitmap from drawable!");
            return drawable;
        }

        public static Bitmap drawableToBitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
            try {
                Bitmap bitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 2),
                        Math.max(drawable.getIntrinsicHeight(), 2), Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        public boolean isStateful() {
            return this.mBorderColor.isStateful();
        }

        protected boolean onStateChange(int[] state) {
            int newColor = this.mBorderColor.getColorForState(state, 0);
            if (this.mBorderPaint.getColor() == newColor) {
                return super.onStateChange(state);
            }
            this.mBorderPaint.setColor(newColor);
            return true;
        }

        private void configureBounds(Canvas canvas) {
            Rect clipBounds = canvas.getClipBounds();
            Matrix canvasMatrix = canvas.getMatrix();
            if (ScaleType.CENTER == this.mScaleType) {
                this.mBounds.set(clipBounds);
            } else if (ScaleType.CENTER_CROP == this.mScaleType) {
                applyScaleToRadii(canvasMatrix);
                this.mBounds.set(clipBounds);
            } else if (ScaleType.FIT_XY == this.mScaleType) {
                Matrix m = new Matrix();
                m.setRectToRect(this.mBitmapRect, new RectF(clipBounds), ScaleToFit.FILL);
                this.mBitmapShader.setLocalMatrix(m);
                this.mBounds.set(clipBounds);
            } else if (ScaleType.FIT_START == this.mScaleType || ScaleType.FIT_END == this
                    .mScaleType || ScaleType.FIT_CENTER == this.mScaleType || ScaleType
                    .CENTER_INSIDE == this.mScaleType) {
                applyScaleToRadii(canvasMatrix);
                this.mBounds.set(this.mBitmapRect);
            } else if (ScaleType.MATRIX == this.mScaleType) {
                applyScaleToRadii(canvasMatrix);
                this.mBounds.set(this.mBitmapRect);
            }
        }

        private void applyScaleToRadii(Matrix m) {
            float[] values = new float[9];
            m.getValues(values);
            for (int i = 0; i < this.mRadii.length; i++) {
                this.mRadii[i] = this.mRadii[i] / values[0];
            }
        }

        private void adjustCanvasForBorder(Canvas canvas) {
            float[] values = new float[9];
            canvas.getMatrix().getValues(values);
            float scaleFactorX = values[0];
            float scaleFactorY = values[4];
            float translateX = values[2];
            float translateY = values[5];
            float newScaleX = this.mBounds.width() / ((this.mBounds.width() + this.mBorderWidth)
                    + this.mBorderWidth);
            float newScaleY = this.mBounds.height() / ((this.mBounds.height() + this
                    .mBorderWidth) + this.mBorderWidth);
            canvas.scale(newScaleX, newScaleY);
            if (ScaleType.FIT_START == this.mScaleType || ScaleType.FIT_END == this.mScaleType ||
                    ScaleType.FIT_XY == this.mScaleType || ScaleType.FIT_CENTER == this
                    .mScaleType || ScaleType.CENTER_INSIDE == this.mScaleType || ScaleType.MATRIX
                    == this.mScaleType) {
                canvas.translate(this.mBorderWidth, this.mBorderWidth);
            } else if (ScaleType.CENTER == this.mScaleType || ScaleType.CENTER_CROP == this
                    .mScaleType) {
                canvas.translate((-translateX) / (newScaleX * scaleFactorX), (-translateY) /
                        (newScaleY * scaleFactorY));
                canvas.translate(-(this.mBounds.left - this.mBorderWidth), -(this.mBounds.top -
                        this.mBorderWidth));
            }
        }

        private void adjustBorderWidthAndBorderBounds(Canvas canvas) {
            float[] values = new float[9];
            canvas.getMatrix().getValues(values);
            this.mBorderWidth = (this.mBorderWidth * this.mBounds.width()) / ((this.mBounds.width
                    () * values[0]) - (this.mBorderWidth * 2.0f));
            this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
            this.mBorderBounds.set(this.mBounds);
            this.mBorderBounds.inset((-this.mBorderWidth) / 2.0f, (-this.mBorderWidth) / 2.0f);
        }

        private void setBorderRadii() {
            for (int i = 0; i < this.mRadii.length; i++) {
                if (this.mRadii[i] > 0.0f) {
                    this.mBorderRadii[i] = this.mRadii[i];
                    this.mRadii[i] = this.mRadii[i] - this.mBorderWidth;
                }
            }
        }

        public void draw(Canvas canvas) {
            canvas.save();
            if (!this.mBoundsConfigured) {
                configureBounds(canvas);
                if (this.mBorderWidth > 0.0f) {
                    adjustBorderWidthAndBorderBounds(canvas);
                    setBorderRadii();
                }
                this.mBoundsConfigured = true;
            }
            if (this.mOval) {
                if (this.mBorderWidth > 0.0f) {
                    adjustCanvasForBorder(canvas);
                    this.mPath.addOval(this.mBounds, Direction.CW);
                    canvas.drawPath(this.mPath, this.mBitmapPaint);
                    this.mPath.reset();
                    this.mPath.addOval(this.mBorderBounds, Direction.CW);
                    canvas.drawPath(this.mPath, this.mBorderPaint);
                } else {
                    this.mPath.addOval(this.mBounds, Direction.CW);
                    canvas.drawPath(this.mPath, this.mBitmapPaint);
                }
            } else if (this.mBorderWidth > 0.0f) {
                adjustCanvasForBorder(canvas);
                this.mPath.addRoundRect(this.mBounds, this.mRadii, Direction.CW);
                canvas.drawPath(this.mPath, this.mBitmapPaint);
                this.mPath.reset();
                this.mPath.addRoundRect(this.mBorderBounds, this.mBorderRadii, Direction.CW);
                canvas.drawPath(this.mPath, this.mBorderPaint);
            } else {
                this.mPath.addRoundRect(this.mBounds, this.mRadii, Direction.CW);
                canvas.drawPath(this.mPath, this.mBitmapPaint);
            }
            canvas.restore();
        }

        public void setCornerRadii(float[] radii) {
            if (radii != null) {
                if (radii.length != 8) {
                    throw new ArrayIndexOutOfBoundsException("radii[] needs 8 values");
                }
                for (int i = 0; i < radii.length; i++) {
                    this.mRadii[i] = radii[i];
                }
            }
        }

        public int getOpacity() {
            return (this.mBitmap == null || this.mBitmap.hasAlpha() || this.mBitmapPaint.getAlpha
                    () < 255) ? -3 : -1;
        }

        public void setAlpha(int alpha) {
            this.mBitmapPaint.setAlpha(alpha);
            invalidateSelf();
        }

        public void setColorFilter(ColorFilter cf) {
            this.mBitmapPaint.setColorFilter(cf);
            invalidateSelf();
        }

        public void setDither(boolean dither) {
            this.mBitmapPaint.setDither(dither);
            invalidateSelf();
        }

        public void setFilterBitmap(boolean filter) {
            this.mBitmapPaint.setFilterBitmap(filter);
            invalidateSelf();
        }

        public int getIntrinsicWidth() {
            return this.mBitmapWidth;
        }

        public int getIntrinsicHeight() {
            return this.mBitmapHeight;
        }

        public float getBorderWidth() {
            return this.mBorderWidth;
        }

        public void setBorderWidth(float width) {
            this.mBorderWidth = width;
            this.mBorderPaint.setStrokeWidth(width);
        }

        public int getBorderColor() {
            return this.mBorderColor.getDefaultColor();
        }

        public void setBorderColor(int color) {
            setBorderColor(ColorStateList.valueOf(color));
        }

        public ColorStateList getBorderColors() {
            return this.mBorderColor;
        }

        public void setBorderColor(ColorStateList colors) {
            if (colors == null) {
                this.mBorderWidth = 0.0f;
                this.mBorderColor = ColorStateList.valueOf(0);
                this.mBorderPaint.setColor(0);
                return;
            }
            this.mBorderColor = colors;
            this.mBorderPaint.setColor(this.mBorderColor.getColorForState(getState(), -16777216));
        }

        public boolean isOval() {
            return this.mOval;
        }

        public void setOval(boolean oval) {
            this.mOval = oval;
        }

        public ScaleType getScaleType() {
            return this.mScaleType;
        }

        public void setScaleType(ScaleType scaleType) {
            if (scaleType != null) {
                this.mScaleType = scaleType;
            }
        }
    }

    public SelectableRoundedImageView(Context context) {
        super(context);
        this.mResource = 0;
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mLeftTopCornerRadius = 0.0f;
        this.mRightTopCornerRadius = 0.0f;
        this.mLeftBottomCornerRadius = 0.0f;
        this.mRightBottomCornerRadius = 0.0f;
        this.mBorderWidth = 0.0f;
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.isOval = false;
        this.mRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    }

    public SelectableRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mResource = 0;
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mLeftTopCornerRadius = 0.0f;
        this.mRightTopCornerRadius = 0.0f;
        this.mLeftBottomCornerRadius = 0.0f;
        this.mRightBottomCornerRadius = 0.0f;
        this.mBorderWidth = 0.0f;
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.isOval = false;
        this.mRadii = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                .SelectableRoundedImageView, defStyle, 0);
        int index = a.getInt(R.styleable.SelectableRoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            setScaleType(sScaleTypeArray[index]);
        }
        this.mLeftTopCornerRadius = (float) a.getDimensionPixelSize(R.styleable
                .SelectableRoundedImageView_sriv_left_top_corner_radius, 0);
        this.mRightTopCornerRadius = (float) a.getDimensionPixelSize(R.styleable
                .SelectableRoundedImageView_sriv_right_top_corner_radius, 0);
        this.mLeftBottomCornerRadius = (float) a.getDimensionPixelSize(R.styleable
                .SelectableRoundedImageView_sriv_left_bottom_corner_radius, 0);
        this.mRightBottomCornerRadius = (float) a.getDimensionPixelSize(R.styleable
                .SelectableRoundedImageView_sriv_right_bottom_corner_radius, 0);
        if (this.mLeftTopCornerRadius < 0.0f || this.mRightTopCornerRadius < 0.0f || this
                .mLeftBottomCornerRadius < 0.0f || this.mRightBottomCornerRadius < 0.0f) {
            throw new IllegalArgumentException("radius values cannot be negative.");
        }
        this.mRadii = new float[]{this.mLeftTopCornerRadius, this.mLeftTopCornerRadius, this
                .mRightTopCornerRadius, this.mRightTopCornerRadius, this
                .mRightBottomCornerRadius, this.mRightBottomCornerRadius, this
                .mLeftBottomCornerRadius, this.mLeftBottomCornerRadius};
        this.mBorderWidth = (float) a.getDimensionPixelSize(R.styleable
                .SelectableRoundedImageView_sriv_border_width, 0);
        if (this.mBorderWidth < 0.0f) {
            throw new IllegalArgumentException("border width cannot be negative.");
        }
        this.mBorderColor = a.getColorStateList(R.styleable
                .SelectableRoundedImageView_sriv_border_color);
        if (this.mBorderColor == null) {
            this.mBorderColor = ColorStateList.valueOf(-16777216);
        }
        this.isOval = a.getBoolean(R.styleable.SelectableRoundedImageView_sriv_oval, false);
        a.recycle();
        updateDrawable();
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        this.mScaleType = scaleType;
        updateDrawable();
    }

    public void setImageDrawable(Drawable drawable) {
        this.mResource = 0;
        this.mDrawable = SelectableRoundedCornerDrawable.fromDrawable(drawable, getResources());
        super.setImageDrawable(this.mDrawable);
        updateDrawable();
    }

    public void setImageBitmap(Bitmap bm) {
        this.mResource = 0;
        this.mDrawable = SelectableRoundedCornerDrawable.fromBitmap(bm, getResources());
        super.setImageDrawable(this.mDrawable);
        updateDrawable();
    }

    public void setImageResource(int resId) {
        if (this.mResource != resId) {
            this.mResource = resId;
            this.mDrawable = resolveResource();
            super.setImageDrawable(this.mDrawable);
            updateDrawable();
        }
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setImageDrawable(getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }
        Drawable d = null;
        if (this.mResource != 0) {
            try {
                d = rsrc.getDrawable(this.mResource);
            } catch (NotFoundException e) {
                Log.w(TAG, "Unable to find resource: " + this.mResource, e);
                this.mResource = 0;
            }
        }
        return SelectableRoundedCornerDrawable.fromDrawable(d, getResources());
    }

    private void updateDrawable() {
        if (this.mDrawable != null) {
            ((SelectableRoundedCornerDrawable) this.mDrawable).setScaleType(this.mScaleType);
            ((SelectableRoundedCornerDrawable) this.mDrawable).setCornerRadii(this.mRadii);
            ((SelectableRoundedCornerDrawable) this.mDrawable).setBorderWidth(this.mBorderWidth);
            ((SelectableRoundedCornerDrawable) this.mDrawable).setBorderColor(this.mBorderColor);
            ((SelectableRoundedCornerDrawable) this.mDrawable).setOval(this.isOval);
        }
    }

    public float getCornerRadius() {
        return this.mLeftTopCornerRadius;
    }

    public void setCornerRadiiDP(float leftTop, float rightTop, float leftBottom, float
            rightBottom) {
        float density = getResources().getDisplayMetrics().density;
        float lt = leftTop * density;
        float rt = rightTop * density;
        float lb = leftBottom * density;
        float rb = rightBottom * density;
        this.mRadii = new float[]{lt, lt, rt, rt, rb, rb, lb, lb};
        updateDrawable();
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidthDP(float width) {
        float scaledWidth = getResources().getDisplayMetrics().density * width;
        if (this.mBorderWidth != scaledWidth) {
            this.mBorderWidth = scaledWidth;
            updateDrawable();
            invalidate();
        }
    }

    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public void setBorderColor(int color) {
        setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (!this.mBorderColor.equals(colors)) {
            if (colors == null) {
                colors = ColorStateList.valueOf(-16777216);
            }
            this.mBorderColor = colors;
            updateDrawable();
            if (this.mBorderWidth > 0.0f) {
                invalidate();
            }
        }
    }

    public boolean isOval() {
        return this.isOval;
    }

    public void setOval(boolean oval) {
        this.isOval = oval;
        updateDrawable();
        invalidate();
    }
}
