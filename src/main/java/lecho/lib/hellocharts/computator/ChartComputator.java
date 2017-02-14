package lecho.lib.hellocharts.computator;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import lecho.lib.hellocharts.listener.DummyVieportChangeListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Viewport;

public class ChartComputator {
    protected static final float DEFAULT_MAXIMUM_ZOOM = 20.0f;
    protected int chartHeight;
    protected int chartWidth;
    protected Rect contentRectMinusAllMargins = new Rect();
    protected Rect contentRectMinusAxesMargins = new Rect();
    protected Viewport currentViewport = new Viewport();
    protected Rect maxContentRect = new Rect();
    protected Viewport maxViewport = new Viewport();
    protected float maxZoom = DEFAULT_MAXIMUM_ZOOM;
    protected float minViewportHeight;
    protected float minViewportWidth;
    protected ViewportChangeListener viewportChangeListener = new DummyVieportChangeListener();

    public void setContentRect(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.chartWidth = width;
        this.chartHeight = height;
        this.maxContentRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        this.contentRectMinusAxesMargins.set(this.maxContentRect);
        this.contentRectMinusAllMargins.set(this.maxContentRect);
    }

    public void resetContentRect() {
        this.contentRectMinusAxesMargins.set(this.maxContentRect);
        this.contentRectMinusAllMargins.set(this.maxContentRect);
    }

    public void insetContentRect(int deltaLeft, int deltaTop, int deltaRight, int deltaBottom) {
        this.contentRectMinusAxesMargins.left += deltaLeft;
        this.contentRectMinusAxesMargins.top += deltaTop;
        this.contentRectMinusAxesMargins.right -= deltaRight;
        this.contentRectMinusAxesMargins.bottom -= deltaBottom;
        insetContentRectByInternalMargins(deltaLeft, deltaTop, deltaRight, deltaBottom);
    }

    public void insetContentRectByInternalMargins(int deltaLeft, int deltaTop, int deltaRight, int deltaBottom) {
        this.contentRectMinusAllMargins.left += deltaLeft;
        this.contentRectMinusAllMargins.top += deltaTop;
        this.contentRectMinusAllMargins.right -= deltaRight;
        this.contentRectMinusAllMargins.bottom -= deltaBottom;
    }

    public void constrainViewport(float left, float top, float right, float bottom) {
        if (right - left < this.minViewportWidth) {
            right = left + this.minViewportWidth;
            if (left < this.maxViewport.left) {
                left = this.maxViewport.left;
                right = left + this.minViewportWidth;
            } else if (right > this.maxViewport.right) {
                right = this.maxViewport.right;
                left = right - this.minViewportWidth;
            }
        }
        if (top - bottom < this.minViewportHeight) {
            bottom = top - this.minViewportHeight;
            if (top > this.maxViewport.top) {
                top = this.maxViewport.top;
                bottom = top - this.minViewportHeight;
            } else if (bottom < this.maxViewport.bottom) {
                bottom = this.maxViewport.bottom;
                top = bottom + this.minViewportHeight;
            }
        }
        this.currentViewport.left = Math.max(this.maxViewport.left, left);
        this.currentViewport.top = Math.min(this.maxViewport.top, top);
        this.currentViewport.right = Math.min(this.maxViewport.right, right);
        this.currentViewport.bottom = Math.max(this.maxViewport.bottom, bottom);
        this.viewportChangeListener.onViewportChanged(this.currentViewport);
    }

    public void setViewportTopLeft(float left, float top) {
        float curWidth = this.currentViewport.width();
        float curHeight = this.currentViewport.height();
        left = Math.max(this.maxViewport.left, Math.min(left, this.maxViewport.right - curWidth));
        top = Math.max(this.maxViewport.bottom + curHeight, Math.min(top, this.maxViewport.top));
        constrainViewport(left, top, left + curWidth, top - curHeight);
    }

    public float computeRawX(float valueX) {
        return ((float) this.contentRectMinusAllMargins.left) + ((valueX - this.currentViewport.left) * (((float) this.contentRectMinusAllMargins.width()) / this.currentViewport.width()));
    }

    public float computeRawY(float valueY) {
        return ((float) this.contentRectMinusAllMargins.bottom) - ((valueY - this.currentViewport.bottom) * (((float) this.contentRectMinusAllMargins.height()) / this.currentViewport.height()));
    }

    public float computeRawDistanceX(float distance) {
        return (((float) this.contentRectMinusAllMargins.width()) / this.currentViewport.width()) * distance;
    }

    public float computeRawDistanceY(float distance) {
        return (((float) this.contentRectMinusAllMargins.height()) / this.currentViewport.height()) * distance;
    }

    public boolean rawPixelsToDataPoint(float x, float y, PointF dest) {
        if (!this.contentRectMinusAllMargins.contains((int) x, (int) y)) {
            return false;
        }
        dest.set(this.currentViewport.left + (((x - ((float) this.contentRectMinusAllMargins.left)) * this.currentViewport.width()) / ((float) this.contentRectMinusAllMargins.width())), this.currentViewport.bottom + (((y - ((float) this.contentRectMinusAllMargins.bottom)) * this.currentViewport.height()) / ((float) (-this.contentRectMinusAllMargins.height()))));
        return true;
    }

    public void computeScrollSurfaceSize(Point out) {
        out.set((int) ((this.maxViewport.width() * ((float) this.contentRectMinusAllMargins.width())) / this.currentViewport.width()), (int) ((this.maxViewport.height() * ((float) this.contentRectMinusAllMargins.height())) / this.currentViewport.height()));
    }

    public boolean isWithinContentRect(float x, float y, float precision) {
        if (x < ((float) this.contentRectMinusAllMargins.left) - precision || x > ((float) this.contentRectMinusAllMargins.right) + precision || y > ((float) this.contentRectMinusAllMargins.bottom) + precision || y < ((float) this.contentRectMinusAllMargins.top) - precision) {
            return false;
        }
        return true;
    }

    public Rect getContentRectMinusAllMargins() {
        return this.contentRectMinusAllMargins;
    }

    public Rect getContentRectMinusAxesMargins() {
        return this.contentRectMinusAxesMargins;
    }

    public Viewport getCurrentViewport() {
        return this.currentViewport;
    }

    public void setCurrentViewport(Viewport viewport) {
        constrainViewport(viewport.left, viewport.top, viewport.right, viewport.bottom);
    }

    public void setCurrentViewport(float left, float top, float right, float bottom) {
        constrainViewport(left, top, right, bottom);
    }

    public Viewport getMaximumViewport() {
        return this.maxViewport;
    }

    public void setMaxViewport(Viewport maxViewport) {
        setMaxViewport(maxViewport.left, maxViewport.top, maxViewport.right, maxViewport.bottom);
    }

    public void setMaxViewport(float left, float top, float right, float bottom) {
        this.maxViewport.set(left, top, right, bottom);
        computeMinimumWidthAndHeight();
    }

    public Viewport getVisibleViewport() {
        return this.currentViewport;
    }

    public void setVisibleViewport(Viewport visibleViewport) {
        setCurrentViewport(visibleViewport);
    }

    public float getMinimumViewportWidth() {
        return this.minViewportWidth;
    }

    public float getMinimumViewportHeight() {
        return this.minViewportHeight;
    }

    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        if (viewportChangeListener == null) {
            this.viewportChangeListener = new DummyVieportChangeListener();
        } else {
            this.viewportChangeListener = viewportChangeListener;
        }
    }

    public int getChartWidth() {
        return this.chartWidth;
    }

    public int getChartHeight() {
        return this.chartHeight;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public void setMaxZoom(float maxZoom) {
        if (maxZoom < 1.0f) {
            maxZoom = 1.0f;
        }
        this.maxZoom = maxZoom;
        computeMinimumWidthAndHeight();
        setCurrentViewport(this.currentViewport);
    }

    private void computeMinimumWidthAndHeight() {
        this.minViewportWidth = this.maxViewport.width() / this.maxZoom;
        this.minViewportHeight = this.maxViewport.height() / this.maxZoom;
    }
}
