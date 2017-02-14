package cn.sharesdk.onekeyshare;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mob.tools.FakeActivity;

public class PicViewer extends FakeActivity implements OnTouchListener {
    static final int DRAG = 1;
    static final float MAX_SCALE = 10.0f;
    static final int NONE = 0;
    static final int ZOOM = 2;
    float dist = 1.0f;
    DisplayMetrics dm;
    private ImageView ivViewer;
    Matrix matrix = new Matrix();
    PointF mid = new PointF();
    float minScaleR = 1.0f;
    int mode = 0;
    private Bitmap pic;
    PointF prev = new PointF();
    Matrix savedMatrix = new Matrix();

    public void setImageBitmap(Bitmap pic) {
        this.pic = pic;
        if (this.ivViewer != null) {
            this.ivViewer.setImageBitmap(pic);
        }
    }

    public void onCreate() {
        this.ivViewer = new ImageView(this.activity);
        this.ivViewer.setScaleType(ScaleType.MATRIX);
        this.ivViewer.setBackgroundColor(-1073741824);
        this.ivViewer.setOnTouchListener(this);
        if (!(this.pic == null || this.pic.isRecycled())) {
            this.ivViewer.setImageBitmap(this.pic);
        }
        this.dm = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(this.dm);
        minZoom();
        CheckView();
        this.ivViewer.setImageMatrix(this.matrix);
        this.activity.setContentView(this.ivViewer);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                this.savedMatrix.set(this.matrix);
                this.prev.set(event.getX(), event.getY());
                this.mode = 1;
                break;
            case 1:
            case 6:
                this.mode = 0;
                break;
            case 2:
                if (this.mode != 1) {
                    if (this.mode == 2) {
                        float newDist = spacing(event);
                        if (newDist > 10.0f) {
                            this.matrix.set(this.savedMatrix);
                            float tScale = newDist / this.dist;
                            this.matrix.postScale(tScale, tScale, this.mid.x, this.mid.y);
                            break;
                        }
                    }
                }
                this.matrix.set(this.savedMatrix);
                this.matrix.postTranslate(event.getX() - this.prev.x, event.getY() - this.prev.y);
                break;
                break;
            case 5:
                this.dist = spacing(event);
                if (spacing(event) > 10.0f) {
                    this.savedMatrix.set(this.matrix);
                    midPoint(this.mid, event);
                    this.mode = 2;
                    break;
                }
                break;
        }
        this.ivViewer.setImageMatrix(this.matrix);
        CheckView();
        return true;
    }

    private void CheckView() {
        float[] p = new float[9];
        this.matrix.getValues(p);
        if (this.mode == 2) {
            if (p[0] < this.minScaleR) {
                this.matrix.setScale(this.minScaleR, this.minScaleR);
            }
            if (p[0] > 10.0f) {
                this.matrix.set(this.savedMatrix);
            }
        }
        center();
    }

    private void minZoom() {
        this.minScaleR = Math.min(((float) this.dm.widthPixels) / ((float) this.pic.getWidth()), ((float) this.dm.heightPixels) / ((float) this.pic.getHeight()));
        this.matrix.setScale(this.minScaleR, this.minScaleR);
    }

    private void center() {
        center(true, true);
    }

    protected void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(this.matrix);
        RectF rect = new RectF(0.0f, 0.0f, (float) this.pic.getWidth(), (float) this.pic.getHeight());
        m.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        if (vertical) {
            int screenHeight = this.dm.heightPixels;
            if (height < ((float) screenHeight)) {
                deltaY = ((((float) screenHeight) - height) / 2.0f) - rect.top;
            } else if (rect.top > 0.0f) {
                deltaY = -rect.top;
            } else if (rect.bottom < ((float) screenHeight)) {
                deltaY = ((float) this.ivViewer.getHeight()) - rect.bottom;
            }
        }
        if (horizontal) {
            int screenWidth = this.dm.widthPixels;
            if (width < ((float) screenWidth)) {
                deltaX = ((((float) screenWidth) - width) / 2.0f) - rect.left;
            } else if (rect.left > 0.0f) {
                deltaX = -rect.left;
            } else if (rect.right < ((float) screenWidth)) {
                deltaX = ((float) this.ivViewer.getWidth()) - rect.right;
            }
        }
        this.matrix.postTranslate(deltaX, deltaY);
    }

    @SuppressLint({"FloatMath"})
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }
}
