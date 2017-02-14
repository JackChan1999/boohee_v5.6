package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.widget.AutoScrollHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.baidu.location.aj;
import com.boohee.widgets.PathListView;
import lecho.lib.hellocharts.R;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue.SelectedValueType;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.util.DateUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;
    private static final float LINE_SMOOTHNESS = 0.16f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private float baseValue;
    private int checkPrecision;
    private LineChartDataProvider dataProvider;
    private LineChartView lineChartView;
    private Paint linePaint = new Paint();
    private Path path = new Path();
    private int pointIndex = -1;
    private Paint pointPaint = new Paint();
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas = new Canvas();
    private float targetWeight = 0.0f;
    private Viewport tempMaximumViewport = new Viewport();
    private int touchToleranceMargin;
    private Paint weightLinePaint = new Paint();
    private Paint weightStrPaint = new Paint();

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider dataProvider) {
        super(context, chart);
        this.dataProvider = dataProvider;
        this.touchToleranceMargin = ChartUtils.dp2px(this.density, 4);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Style.STROKE);
        this.linePaint.setStrokeCap(Cap.ROUND);
        this.linePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 3));
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setStyle(Style.FILL);
        this.weightStrPaint.setAntiAlias(true);
        this.weightStrPaint.setColor(-1);
        this.weightStrPaint.setTextSize((float) ChartUtils.dp2px(this.density, 12));
        this.weightLinePaint.setAntiAlias(true);
        this.weightLinePaint.setStyle(Style.STROKE);
        this.weightLinePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 1));
        this.weightLinePaint.setColor(ChartUtils.DEFAULT_TARGET_COLOR);
        this.checkPrecision = ChartUtils.dp2px(this.density, 2);
    }

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider dataProvider, LineChartView lineChartView) {
        super(context, chart);
        this.dataProvider = dataProvider;
        this.lineChartView = lineChartView;
        this.touchToleranceMargin = ChartUtils.dp2px(this.density, 4);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Style.STROKE);
        this.linePaint.setStrokeCap(Cap.ROUND);
        this.linePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 3));
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setStyle(Style.FILL);
        this.weightStrPaint.setAntiAlias(true);
        this.weightStrPaint.setColor(-1);
        this.weightStrPaint.setTextSize((float) ChartUtils.dp2px(this.density, 12));
        this.weightLinePaint.setAntiAlias(true);
        this.weightLinePaint.setStyle(Style.STROKE);
        this.weightLinePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 1));
        this.weightLinePaint.setColor(ChartUtils.DEFAULT_TARGET_COLOR);
        this.checkPrecision = ChartUtils.dp2px(this.density, 2);
    }

    public void onChartSizeChanged() {
        int internalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);
        if (this.computator.getChartWidth() > 0 && this.computator.getChartHeight() > 0) {
            this.softwareBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Config.ARGB_8888);
            this.softwareCanvas.setBitmap(this.softwareBitmap);
        }
    }

    public void onChartDataChanged() {
        super.onChartDataChanged();
        int internalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);
        this.baseValue = this.dataProvider.getLineChartData().getBaseValue();
        onChartViewportChanged();
    }

    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            calculateMaxViewport();
            this.computator.setMaxViewport(this.tempMaximumViewport);
            this.computator.setCurrentViewport(this.computator.getMaximumViewport());
        }
    }

    public void draw(Canvas canvas) {
        Canvas drawCanvas;
        LineChartData data = this.dataProvider.getLineChartData();
        if (this.softwareBitmap != null) {
            drawCanvas = this.softwareCanvas;
            drawCanvas.drawColor(0, Mode.CLEAR);
        } else {
            drawCanvas = canvas;
        }
        this.targetWeight = this.lineChartView.getTargetWeight();
        if (this.targetWeight > 0.0f) {
            drawTargetWeightPath(drawCanvas);
        }
        for (Line line : data.getLines()) {
            if (line.hasLines()) {
                if (line.isCubic()) {
                    drawSmoothPath(drawCanvas, line);
                } else if (line.isSquare()) {
                    drawSquarePath(drawCanvas, line);
                } else {
                    drawPath(drawCanvas, line);
                }
            }
        }
        if (this.softwareBitmap != null) {
            canvas.drawBitmap(this.softwareBitmap, 0.0f, 0.0f, null);
        }
    }

    private void drawTargetWeightPath(Canvas drawCanvas) {
        float currentLeft = 0.0f;
        float currentRight = 0.0f;
        float target = this.targetWeight;
        float rectPadding = (float) ChartUtils.dp2px(this.density, 2);
        prepareWeightLinePaint();
        Viewport currentViewport = this.computator.getCurrentViewport();
        if (currentViewport != null) {
            currentLeft = currentViewport.left;
            currentRight = currentViewport.right;
        }
        float rawX1 = this.computator.computeRawX(currentLeft);
        float rawX2 = this.computator.computeRawX(currentRight);
        float y = this.computator.computeRawY(target);
        Path path1 = new Path();
        path1.moveTo(rawX1, y);
        path1.lineTo(rawX2, y);
        drawCanvas.drawPath(path1, this.weightLinePaint);
        String targetStr = "目标: " + target + "kg";
        float textWidth = this.weightStrPaint.measureText(targetStr);
        float textX = ((rawX1 + rawX2) / 2.0f) - (textWidth / 2.0f);
        float textY = y + (getTextHeight() / aj.hA);
        this.weightLinePaint.setStyle(Style.FILL);
        float corner = (float) ChartUtils.dp2px(this.density, 3);
        drawCanvas.drawRoundRect(new RectF((float) ((int) (textX - rectPadding)), (float) ((int) ((y - (getTextHeight() / 2.0f)) - rectPadding)), (float) ((int) ((textX + textWidth) + rectPadding)), (float) ((int) (((getTextHeight() / 2.0f) + y) + rectPadding))), corner, corner, this.weightLinePaint);
        drawCanvas.drawText(targetStr, textX, textY, this.weightStrPaint);
    }

    private void prepareWeightLinePaint() {
        this.weightLinePaint.setStyle(Style.STROKE);
        this.weightLinePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 1));
    }

    private float getTextHeight() {
        FontMetrics fm = this.weightStrPaint.getFontMetrics();
        return ((float) Math.ceil((double) (fm.descent - fm.top))) + 2.0f;
    }

    public void drawUnclipped(Canvas canvas) {
        int lineIndex = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            drawPoints(canvas, line, lineIndex, 0);
            lineIndex++;
        }
        if (isTouched() && this.selectedValue.getSelectedType() == 0) {
            highlightPoints(canvas);
        }
    }

    private boolean checkIfShouldDrawPoints(Line line) {
        return line.hasPoints() || line.getValues().size() == 1;
    }

    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        LineChartData data = this.dataProvider.getLineChartData();
        int lineIndex = 0;
        float imageValueY = (float) (this.lineChartView.getMeasuredHeight() - ChartUtils.dp2px(this.density, 30));
        int imgRadius = ChartUtils.dp2px(this.density, data.getImgRadius()) + 10;
        for (Line line : data.getLines()) {
            int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
            int valueIndex = 0;
            for (PointValue pointValue : line.getValues()) {
                float rawValueX = this.computator.computeRawX(pointValue.getX());
                float rawValueY = this.computator.computeRawY(pointValue.getY());
                if (isInArea(rawValueX, rawValueY, touchX, touchY, (float) (this.touchToleranceMargin + pointRadius))) {
                    this.pointIndex = valueIndex;
                    this.selectedValue.set(lineIndex, valueIndex, SelectedValueType.LINE, 0);
                } else {
                    if (isInArea(rawValueX, imageValueY, touchX, touchY, (float) imgRadius)) {
                        this.selectedValue.set(lineIndex, valueIndex, SelectedValueType.LINE, 1);
                    } else {
                        if (isInArea(rawValueX, rawValueY - ((float) ChartUtils.dp2px(this.density, 40)), touchX, touchY, (float) ChartUtils.dp2px(this.density, 40)) && this.pointIndex == valueIndex) {
                            this.selectedValue.set(lineIndex, valueIndex, SelectedValueType.LINE, 2);
                        }
                    }
                }
                valueIndex++;
            }
            lineIndex++;
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(AutoScrollHelper.NO_MAX, Float.MIN_VALUE, Float.MIN_VALUE, AutoScrollHelper.NO_MAX);
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            for (PointValue pointValue : line.getValues()) {
                if (pointValue.getX() < this.tempMaximumViewport.left) {
                    this.tempMaximumViewport.left = pointValue.getX();
                }
                if (pointValue.getX() > this.tempMaximumViewport.right) {
                    this.tempMaximumViewport.right = pointValue.getX();
                }
                if (pointValue.getY() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = pointValue.getY();
                }
                if (pointValue.getY() > this.tempMaximumViewport.top) {
                    this.tempMaximumViewport.top = pointValue.getY();
                }
            }
        }
    }

    private int calculateContentRectInternalMargin() {
        int contentAreaMargin = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int margin = line.getPointRadius() + 4;
                if (margin > contentAreaMargin) {
                    contentAreaMargin = margin;
                }
            }
        }
        return ChartUtils.dp2px(this.density, contentAreaMargin);
    }

    private void drawPath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int valueIndex = 0;
        for (PointValue pointValue : line.getValues()) {
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (valueIndex == 0) {
                this.path.moveTo(rawX, rawY);
            } else {
                this.path.lineTo(rawX, rawY);
            }
            valueIndex++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSquarePath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int valueIndex = 0;
        float previousRawY = 0.0f;
        for (PointValue pointValue : line.getValues()) {
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (valueIndex == 0) {
                this.path.moveTo(rawX, rawY);
            } else {
                this.path.lineTo(rawX, previousRawY);
                this.path.lineTo(rawX, rawY);
            }
            previousRawY = rawY;
            valueIndex++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSmoothPath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int lineSize = line.getValues().size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        for (int valueIndex = 0; valueIndex < lineSize; valueIndex++) {
            float nextPointX;
            float nextPointY;
            if (Float.isNaN(currentPointX)) {
                PointValue linePoint = (PointValue) line.getValues().get(valueIndex);
                currentPointX = this.computator.computeRawX(linePoint.getX());
                currentPointY = this.computator.computeRawY(linePoint.getY());
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    linePoint = (PointValue) line.getValues().get(valueIndex - 1);
                    previousPointX = this.computator.computeRawX(linePoint.getX());
                    previousPointY = this.computator.computeRawY(linePoint.getY());
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }
            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    linePoint = (PointValue) line.getValues().get(valueIndex - 2);
                    prePreviousPointX = this.computator.computeRawX(linePoint.getX());
                    prePreviousPointY = this.computator.computeRawY(linePoint.getY());
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }
            if (valueIndex < lineSize - 1) {
                linePoint = (PointValue) line.getValues().get(valueIndex + 1);
                nextPointX = this.computator.computeRawX(linePoint.getX());
                nextPointY = this.computator.computeRawY(linePoint.getY());
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }
            if (valueIndex == 0) {
                this.path.moveTo(currentPointX, currentPointY);
            } else {
                this.path.cubicTo(previousPointX + (LINE_SMOOTHNESS * (currentPointX - prePreviousPointX)), previousPointY + (LINE_SMOOTHNESS * (currentPointY - prePreviousPointY)), currentPointX - (LINE_SMOOTHNESS * (nextPointX - previousPointX)), currentPointY - (LINE_SMOOTHNESS * (nextPointY - previousPointY)), currentPointX, currentPointY);
            }
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void prepareLinePaint(Line line) {
        this.linePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, line.getStrokeWidth()));
        this.linePaint.setColor(line.getColor());
        this.linePaint.setPathEffect(line.getPathEffect());
    }

    private void drawPoints(Canvas canvas, Line line, int lineIndex, int mode) {
        this.pointPaint.setColor(line.getColor());
        int valueIndex = 0;
        if (this.pointIndex == -1 && line.hasLabels()) {
            this.pointIndex = line.getValues().size() - 1;
        }
        for (PointValue pointValue : line.getValues()) {
            int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (this.computator.isWithinContentRect(rawX, rawY, (float) this.checkPrecision)) {
                if (mode == 0) {
                    if (line.hasPoints()) {
                        drawPoint(canvas, line, pointValue, rawX, rawY, (float) pointRadius);
                    }
                    if (line.hasLabels() && valueIndex == this.pointIndex && pointValue.getData() != null) {
                        drawLabel(canvas, line, pointValue, rawX, rawY, (float) (this.labelOffset + pointRadius));
                    }
                } else if (1 == mode) {
                    highlightPoint(canvas, line, pointValue, rawX, rawY, lineIndex, valueIndex);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + mode);
                }
            }
            valueIndex++;
        }
    }

    private void drawPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float pointRadius) {
        if (ValueShape.SQUARE.equals(line.getShape())) {
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, this.pointPaint);
        } else if (ValueShape.CIRCLE.equals(line.getShape())) {
            canvas.drawCircle(rawX, rawY, pointRadius, this.pointPaint);
        } else if (ValueShape.DIAMOND.equals(line.getShape())) {
            canvas.save();
            canvas.rotate(45.0f, rawX, rawY);
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, this.pointPaint);
            canvas.restore();
        } else {
            throw new IllegalArgumentException("Invalid point shape: " + line.getShape());
        }
    }

    private void highlightPoints(Canvas canvas) {
        int lineIndex = this.selectedValue.getFirstIndex();
        drawPoints(canvas, (Line) this.dataProvider.getLineChartData().getLines().get(lineIndex), lineIndex, 1);
    }

    private void highlightPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, int lineIndex, int valueIndex) {
        if (this.selectedValue.getFirstIndex() == lineIndex && this.selectedValue.getSecondIndex() == valueIndex) {
            int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
            this.pointPaint.setColor(line.getDarkenColor());
            if (line.hasPoints()) {
                drawPoint(canvas, line, pointValue, rawX, rawY, (float) (this.touchToleranceMargin + pointRadius));
            }
        }
    }

    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float offset) {
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        int numChars = line.getFormatter().formatChartValue(this.labelBuffer, pointValue);
        if (numChars != 0) {
            float top;
            float bottom;
            float labelWidth = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - numChars, numChars);
            int labelHeight = Math.abs(this.fontMetrics.ascent);
            float left = (rawX - (labelWidth / 2.0f)) - ((float) this.labelMargin);
            float right = ((labelWidth / 2.0f) + rawX) + ((float) this.labelMargin);
            if (pointValue.getY() >= this.baseValue) {
                top = ((rawY - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = rawY - offset;
            } else {
                top = rawY + offset;
                bottom = ((rawY + offset) + ((float) labelHeight)) + ((float) (this.labelMargin * 2));
            }
            if (top < ((float) contentRect.top)) {
                top = rawY + offset;
                bottom = ((rawY + offset) + ((float) labelHeight)) + ((float) (this.labelMargin * 2));
            }
            if (bottom > ((float) contentRect.bottom)) {
                top = ((rawY - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = rawY - offset;
            }
            if (left < ((float) contentRect.left)) {
                left = rawX;
                right = (rawX + labelWidth) + ((float) (this.labelMargin * 2));
            }
            if (right > ((float) contentRect.right)) {
                left = (rawX - labelWidth) - ((float) (this.labelMargin * 2));
                right = rawX;
            }
            this.labelBackgroundRect.set(left, top, right, bottom);
            View view = getMarkView(this.context, pointValue);
            if (view != null) {
                drawLabelTextAndBackgroundForMarkView(canvas, this.labelBuffer, this.labelBuffer.length - numChars, numChars, line.getColor(), line.getLabelColor(), ChartUtils.getBitmapFromView(view));
            }
        }
    }

    private void drawArea(Canvas canvas, Line line) {
        int lineSize = line.getValues().size();
        if (lineSize >= 2) {
            Rect contentRect = this.computator.getContentRectMinusAllMargins();
            float baseRawValue = Math.min((float) contentRect.bottom, Math.max(this.computator.computeRawY(this.baseValue), (float) contentRect.top));
            float left = Math.max(this.computator.computeRawX(((PointValue) line.getValues().get(0)).getX()), (float) contentRect.left);
            this.path.lineTo(Math.min(this.computator.computeRawX(((PointValue) line.getValues().get(lineSize - 1)).getX()), (float) contentRect.right), baseRawValue);
            this.path.lineTo(left, baseRawValue);
            this.path.close();
            this.linePaint.setStyle(Style.FILL);
            this.linePaint.setAlpha(line.getAreaTransparency());
            this.linePaint.setColor(line.getAreaColor());
            canvas.drawPath(this.path, this.linePaint);
            this.linePaint.setStyle(Style.STROKE);
        }
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        return Math.pow((double) (touchX - x), PathListView.ZOOM_X2) + Math.pow((double) (touchY - y), PathListView.ZOOM_X2) <= Math.pow((double) radius, PathListView.ZOOM_X2) * PathListView.ZOOM_X2;
    }

    public View getMarkView(Context context, PointValue pointValue) {
        String record_on = "";
        View view = LayoutInflater.from(context).inflate(R.layout.view_mark_weight, null);
        TextView txt_date = (TextView) view.findViewById(R.id.txt_date);
        ((TextView) view.findViewById(R.id.txt_value)).setText(pointValue.getY() + (TextUtils.isEmpty(this.lineChartView.getUnit()) ? "" : this.lineChartView.getUnit()));
        if (pointValue.getData() != null) {
            txt_date.setText(DateUtils.date2string(DateUtils.string2date(pointValue.getData().toString(), "yyyy-MM-dd"), "M月d日"));
        }
        return view;
    }

    public void resetPointIndex() {
        this.pointIndex = -1;
    }
}
