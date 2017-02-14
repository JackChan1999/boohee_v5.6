package com.boohee.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.baidu.location.aj;
import com.boohee.utility.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class LineGraph extends View {
    private final int LINE_COLOR  = -2826241;
    private       int LINE_WIDTH  = 1;
    private final int POINT_COLOR = -8874003;
    private       int POINT_WIDTH = 3;
    private Context context;
    private int     height;
    private Paint   linePaint;
    private PathEffect pathEffect = new DashPathEffect(new float[]{aj.hA, aj.hA}, 0.0f);
    private float       pixelPerKg;
    private Paint       pointPaint;
    private List<Point> points;
    private int         width;
    private List<Float> yPositions = new ArrayList();

    public static class Point {
        public final boolean isDotted;
        public final float   value;

        public Point(float value, boolean isDotted) {
            this.value = value;
            this.isDotted = isDotted;
        }
    }

    public LineGraph(Context context) {
        super(context);
        init(context);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(1, null);
        this.LINE_WIDTH = DensityUtil.dip2px(context, (float) this.LINE_WIDTH);
        this.POINT_WIDTH = DensityUtil.dip2px(context, (float) this.POINT_WIDTH);
        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(-2826241);
        this.linePaint.setStyle(Style.STROKE);
        this.linePaint.setStrokeWidth((float) this.LINE_WIDTH);
        this.pointPaint = new Paint();
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setColor(-8874003);
        this.pointPaint.setStrokeCap(Cap.ROUND);
        this.pointPaint.setStyle(Style.STROKE);
        this.pointPaint.setStrokeWidth((float) this.POINT_WIDTH);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            this.points = new ArrayList();
            Point p1 = new Point(53.4f, true);
            Point p2 = new Point(53.4f, true);
            Point p3 = new Point(53.4f, true);
            Point p4 = new Point(53.4f, false);
            Point p5 = new Point(55.4f, false);
            Point p6 = new Point(53.1f, false);
            Point p7 = new Point(53.9f, false);
            this.points.add(p1);
            this.points.add(p2);
            this.points.add(p3);
            this.points.add(p4);
            this.points.add(p5);
            this.points.add(p6);
            this.points.add(p7);
        }
    }

    public void setPoints(List<Point> points) {
        this.points = points;
        this.yPositions.clear();
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.height = h;
        this.width = w;
        this.pixelPerKg = ((float) h) / 10.0f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.points != null && this.points.size() != 0) {
            if (this.yPositions.size() != this.points.size()) {
                processPoint(this.points);
            }
            float lastX = 0.0f;
            float lastY = 0.0f;
            boolean isDotted = false;
            for (int i = 0; i < this.points.size(); i++) {
                int size;
                int i2 = this.POINT_WIDTH / 2;
                int i3 = (this.width - this.POINT_WIDTH) * i;
                if (this.points.size() > 1) {
                    size = this.points.size() - 1;
                } else {
                    size = 1;
                }
                float x = (float) ((i3 / size) + i2);
                float y = ((Float) this.yPositions.get(i)).floatValue();
                drawPoint(x, ((Float) this.yPositions.get(i)).floatValue(), canvas);
                if (i > 0) {
                    drawLine(lastX, lastY, x, y, isDotted, canvas);
                }
                lastX = x;
                lastY = y;
                isDotted = ((Point) this.points.get(i)).isDotted;
            }
        }
    }

    private void processPoint(List<Point> points) {
        this.yPositions.clear();
        float sum = 0.0f;
        for (Point point : points) {
            sum += point.value;
        }
        float average = sum / ((float) points.size());
        for (Point point2 : points) {
            float yPosition = ((average - point2.value) * this.pixelPerKg) + (((float) this
                    .height) / 2.0f);
            if (yPosition > ((float) this.height) - (((float) this.POINT_WIDTH) / 2.0f)) {
                yPosition = ((float) this.height) - (((float) this.POINT_WIDTH) / 2.0f);
            }
            if (yPosition < ((float) this.POINT_WIDTH) / 2.0f) {
                yPosition = ((float) this.POINT_WIDTH) / 2.0f;
            }
            this.yPositions.add(Float.valueOf(yPosition));
        }
    }

    private void drawPoint(float x, float y, Canvas canvas) {
        canvas.drawPoint(x, y, this.pointPaint);
    }

    private void drawLine(float x1, float y1, float x2, float y2, boolean isDotted, Canvas canvas) {
        float percent = (float) (((double) this.POINT_WIDTH) / Math.sqrt((double) (((x1 - x2) *
                (x1 - x2)) + ((y1 - y2) * (y1 - y2)))));
        float newX1 = x1 + ((x2 - x1) * percent);
        float newY1 = y1 + ((y2 - y1) * percent);
        float newX2 = x1 + ((x2 - x1) * (1.0f - percent));
        float newY2 = y1 + ((y2 - y1) * (1.0f - percent));
        if (isDotted) {
            this.linePaint.setPathEffect(this.pathEffect);
        } else {
            this.linePaint.setPathEffect(null);
        }
        this.linePaint.setStrokeWidth((float) this.LINE_WIDTH);
        this.linePaint.setXfermode(null);
        canvas.drawLine(newX1, newY1, newX2, newY2, this.linePaint);
    }
}
