package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import com.baidu.location.aj;
import java.util.List;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SelectedValue.SelectedValueType;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class ColumnChartRenderer extends AbstractChartRenderer {
    public static final int DEFAULT_COLUMN_TOUCH_ADDITIONAL_WIDTH_DP = 4;
    public static final int DEFAULT_SUBCOLUMN_SPACING_DP = 1;
    private static final int MODE_CHECK_TOUCH = 1;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 2;
    private int aboveTargetLineColor;
    private float baseValue;
    private Paint caloryLinePaint = new Paint();
    private Paint caloryTextPaint = new Paint();
    private Paint columnPaint = new Paint();
    private int cornerRound;
    private ColumnChartDataProvider dataProvider;
    private RectF drawRect = new RectF();
    private float fillRatio;
    private boolean isHigllightAboveTarget;
    private boolean isRound;
    private float maxCaloryLimit = 0.0f;
    private int subcolumnSpacing;
    private int targetCalory = 0;
    private int targetCaloryTextColor;
    private Viewport tempMaximumViewport = new Viewport();
    private int touchAdditionalWidth;
    private PointF touchedPoint = new PointF();

    public ColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider dataProvider) {
        super(context, chart);
        this.dataProvider = dataProvider;
        this.subcolumnSpacing = ChartUtils.dp2px(this.density, 1);
        this.touchAdditionalWidth = ChartUtils.dp2px(this.density, 4);
        this.columnPaint.setAntiAlias(true);
        this.columnPaint.setStyle(Style.FILL);
        this.columnPaint.setStrokeCap(Cap.SQUARE);
        this.caloryLinePaint.setAntiAlias(true);
        this.caloryLinePaint.setStyle(Style.STROKE);
        this.caloryLinePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 1));
        this.caloryLinePaint.setColor(ChartUtils.DEFAULT_TARGET_COLOR);
        this.caloryTextPaint.setAntiAlias(true);
        this.caloryTextPaint.setStyle(Style.FILL);
        this.caloryTextPaint.setTextSize((float) ChartUtils.dp2px(this.density, 12));
        this.caloryTextPaint.setColor(ChartUtils.COLOR_BACKGROUND);
    }

    public void onChartSizeChanged() {
    }

    public void onChartDataChanged() {
        super.onChartDataChanged();
        ColumnChartData data = this.dataProvider.getColumnChartData();
        this.fillRatio = data.getFillRatio();
        this.baseValue = data.getBaseValue();
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
        if (this.dataProvider.getColumnChartData().isStacked()) {
            drawColumnForStacked(canvas);
            if (isTouched()) {
                highlightColumnForStacked(canvas);
                return;
            }
            return;
        }
        drawColumnsForSubcolumns(canvas);
        if (isTouched()) {
            highlightColumnsForSubcolumns(canvas);
        }
    }

    public void drawUnclipped(Canvas canvas) {
    }

    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        if (this.dataProvider.getColumnChartData().isStacked()) {
            checkTouchForStacked(touchX, touchY);
        } else {
            checkTouchForSubcolumns(touchX, touchY);
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        ColumnChartData data = this.dataProvider.getColumnChartData();
        this.tempMaximumViewport.set(-0.5f, this.baseValue, ((float) data.getColumns().size()) - 0.5f, this.baseValue);
        if (data.isStacked()) {
            calculateMaxViewportForStacked(data);
        } else {
            calculateMaxViewportForSubcolumns(data);
        }
    }

    private void calculateMaxViewportForSubcolumns(ColumnChartData data) {
        for (Column column : data.getColumns()) {
            for (SubcolumnValue columnValue : column.getValues()) {
                if (columnValue.getValue() >= this.baseValue && columnValue.getValue() > this.tempMaximumViewport.top) {
                    this.tempMaximumViewport.top = columnValue.getValue();
                }
                if (columnValue.getValue() < this.baseValue && columnValue.getValue() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = columnValue.getValue();
                }
            }
        }
    }

    private void calculateMaxViewportForStacked(ColumnChartData data) {
        for (Column column : data.getColumns()) {
            float sumPositive = this.baseValue;
            float sumNegative = this.baseValue;
            for (SubcolumnValue columnValue : column.getValues()) {
                if (columnValue.getValue() >= this.baseValue) {
                    sumPositive += columnValue.getValue();
                } else {
                    sumNegative += columnValue.getValue();
                }
            }
            if (sumPositive > this.tempMaximumViewport.top) {
                this.tempMaximumViewport.top = sumPositive;
            }
            if (sumNegative < this.tempMaximumViewport.bottom) {
                this.tempMaximumViewport.bottom = sumNegative;
            }
        }
    }

    private void drawColumnsForSubcolumns(Canvas canvas) {
        ColumnChartData data = this.dataProvider.getColumnChartData();
        float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForSubcolumns(canvas, column, columnWidth, columnIndex, 0);
            columnIndex++;
        }
    }

    private void highlightColumnsForSubcolumns(Canvas canvas) {
        ColumnChartData data = this.dataProvider.getColumnChartData();
        Column column = (Column) data.getColumns().get(this.selectedValue.getFirstIndex());
        processColumnForSubcolumns(canvas, column, calculateColumnWidth(), this.selectedValue.getFirstIndex(), 2);
    }

    private void checkTouchForSubcolumns(float touchX, float touchY) {
        this.touchedPoint.x = touchX;
        this.touchedPoint.y = touchY;
        ColumnChartData data = this.dataProvider.getColumnChartData();
        float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForSubcolumns(null, column, columnWidth, columnIndex, 1);
            columnIndex++;
        }
    }

    private void processColumnForSubcolumns(Canvas canvas, Column column, float columnWidth, int columnIndex, int mode) {
        float subcolumnWidth = (columnWidth - ((float) (this.subcolumnSpacing * (column.getValues().size() - 1)))) / ((float) column.getValues().size());
        if (subcolumnWidth < 1.0f) {
            subcolumnWidth = 1.0f;
        }
        float rawX = this.computator.computeRawX((float) columnIndex);
        float halfColumnWidth = columnWidth / 2.0f;
        float baseRawY = this.computator.computeRawY(this.baseValue);
        float subcolumnRawX = rawX - halfColumnWidth;
        int valueIndex = 0;
        if (column.getValues().size() > 1 && ((SubcolumnValue) column.getValues().get(column.getValues().size() - 1)).getValue() == 0.0f) {
            subcolumnRawX += subcolumnWidth / 2.0f;
        }
        List<SubcolumnValue> subcolumnValueList = column.getValues();
        for (int i = 0; i < subcolumnValueList.size(); i++) {
            SubcolumnValue columnValue = (SubcolumnValue) subcolumnValueList.get(i);
            if (columnValue.getValue() != 0.0f) {
                if (!this.isHigllightAboveTarget || columnValue.getValue() <= ((float) this.targetCalory)) {
                    this.columnPaint.setColor(columnValue.getColor());
                } else {
                    this.columnPaint.setColor(this.aboveTargetLineColor);
                }
                if (subcolumnRawX <= rawX + halfColumnWidth) {
                    calculateRectToDraw(columnValue, subcolumnRawX, subcolumnRawX + subcolumnWidth, baseRawY, this.computator.computeRawY(getColumnValue(columnValue)));
                    switch (mode) {
                        case 0:
                            drawSubcolumn(canvas, column, columnValue, false);
                            break;
                        case 1:
                            checkRectToDraw(columnIndex, valueIndex);
                            break;
                        case 2:
                            highlightSubcolumn(canvas, column, columnValue, valueIndex, false);
                            break;
                        default:
                            throw new IllegalStateException("Cannot process column in mode: " + mode);
                    }
                    subcolumnRawX += ((float) this.subcolumnSpacing) + subcolumnWidth;
                    valueIndex++;
                } else {
                    return;
                }
            } else if (i == 0) {
                subcolumnRawX += subcolumnWidth / 2.0f;
            }
        }
    }

    private void drawColumnForStacked(Canvas canvas) {
        ColumnChartData data = this.dataProvider.getColumnChartData();
        float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForStacked(canvas, column, columnWidth, columnIndex, 0);
            columnIndex++;
        }
    }

    private void highlightColumnForStacked(Canvas canvas) {
        ColumnChartData data = this.dataProvider.getColumnChartData();
        Column column = (Column) data.getColumns().get(this.selectedValue.getFirstIndex());
        processColumnForStacked(canvas, column, calculateColumnWidth(), this.selectedValue.getFirstIndex(), 2);
    }

    private void checkTouchForStacked(float touchX, float touchY) {
        this.touchedPoint.x = touchX;
        this.touchedPoint.y = touchY;
        ColumnChartData data = this.dataProvider.getColumnChartData();
        float columnWidth = calculateColumnWidth();
        int columnIndex = 0;
        for (Column column : data.getColumns()) {
            processColumnForStacked(null, column, columnWidth, columnIndex, 1);
            columnIndex++;
        }
    }

    private void processColumnForStacked(Canvas canvas, Column column, float columnWidth, int columnIndex, int mode) {
        float rawX = this.computator.computeRawX((float) columnIndex);
        float halfColumnWidth = columnWidth / 2.0f;
        float mostPositiveValue = this.baseValue;
        float mostNegativeValue = this.baseValue;
        float subcolumnBaseValue = this.baseValue;
        int valueIndex = 0;
        for (SubcolumnValue columnValue : column.getValues()) {
            if (this.isHigllightAboveTarget) {
                this.columnPaint.setColor(this.aboveTargetLineColor);
            } else {
                this.columnPaint.setColor(columnValue.getColor());
            }
            if (columnValue.getValue() >= this.baseValue) {
                subcolumnBaseValue = mostPositiveValue;
                mostPositiveValue += columnValue.getValue();
            } else {
                subcolumnBaseValue = mostNegativeValue;
                mostNegativeValue += columnValue.getValue();
            }
            calculateRectToDraw(columnValue, rawX - halfColumnWidth, rawX + halfColumnWidth, this.computator.computeRawY(subcolumnBaseValue), this.computator.computeRawY(columnValue.getValue() + subcolumnBaseValue));
            switch (mode) {
                case 0:
                    drawSubcolumn(canvas, column, columnValue, true);
                    break;
                case 1:
                    checkRectToDraw(columnIndex, valueIndex);
                    break;
                case 2:
                    highlightSubcolumn(canvas, column, columnValue, valueIndex, true);
                    break;
                default:
                    throw new IllegalStateException("Cannot process column in mode: " + mode);
            }
            valueIndex++;
        }
    }

    private void drawSubcolumn(Canvas canvas, Column column, SubcolumnValue columnValue, boolean isStacked) {
        if (this.isRound) {
            canvas.drawRoundRect(this.drawRect, (float) this.cornerRound, (float) this.cornerRound, this.columnPaint);
        } else {
            canvas.drawRect(this.drawRect, this.columnPaint);
        }
        if (column.hasLabels()) {
            drawLabel(canvas, column, columnValue, isStacked, (float) this.labelOffset);
        }
    }

    private void highlightSubcolumn(Canvas canvas, Column column, SubcolumnValue columnValue, int valueIndex, boolean isStacked) {
        if (this.selectedValue.getSecondIndex() == valueIndex) {
            this.columnPaint.setColor(columnValue.getDarkenColor());
            Canvas canvas2 = canvas;
            canvas2.drawRect(this.drawRect.left - ((float) this.touchAdditionalWidth), this.drawRect.top, ((float) this.touchAdditionalWidth) + this.drawRect.right, this.drawRect.bottom, this.columnPaint);
            if (column.hasLabels() || column.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, column, columnValue, isStacked, (float) this.labelOffset);
            }
        }
    }

    private void checkRectToDraw(int columnIndex, int valueIndex) {
        if (this.drawRect.contains(this.touchedPoint.x, this.touchedPoint.y)) {
            this.selectedValue.set(columnIndex, valueIndex, SelectedValueType.COLUMN);
        }
    }

    private float calculateColumnWidth() {
        float columnWidth = (this.fillRatio * ((float) this.computator.getContentRectMinusAllMargins().width())) / this.computator.getVisibleViewport().width();
        if (columnWidth < 2.0f) {
            return 2.0f;
        }
        return columnWidth;
    }

    private void calculateRectToDraw(SubcolumnValue columnValue, float left, float right, float rawBaseY, float rawY) {
        this.drawRect.left = left;
        this.drawRect.right = right;
        if (columnValue.getValue() >= this.baseValue) {
            this.drawRect.top = rawY;
            this.drawRect.bottom = rawBaseY - ((float) this.subcolumnSpacing);
            return;
        }
        this.drawRect.bottom = rawY;
        this.drawRect.top = ((float) this.subcolumnSpacing) + rawBaseY;
    }

    private void drawLabel(Canvas canvas, Column column, SubcolumnValue columnValue, boolean isStacked, float offset) {
        int numChars = column.getFormatter().formatChartValue(this.labelBuffer, columnValue);
        if (numChars != 0) {
            float top;
            float bottom;
            float labelWidth = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - numChars, numChars);
            int labelHeight = Math.abs(this.fontMetrics.ascent);
            float left = (this.drawRect.centerX() - (labelWidth / 2.0f)) - ((float) this.labelMargin);
            float right = (this.drawRect.centerX() + (labelWidth / 2.0f)) + ((float) this.labelMargin);
            if (!isStacked || ((float) labelHeight) >= this.drawRect.height() - ((float) (this.labelMargin * 2))) {
                if (!isStacked) {
                    if (columnValue.getValue() >= this.baseValue) {
                        top = ((this.drawRect.top - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                        if (top < ((float) this.computator.getContentRectMinusAllMargins().top)) {
                            top = this.drawRect.top + offset;
                            bottom = ((this.drawRect.top + offset) + ((float) labelHeight)) + ((float) (this.labelMargin * 2));
                        } else {
                            bottom = this.drawRect.top - offset;
                        }
                    } else {
                        bottom = ((this.drawRect.bottom + offset) + ((float) labelHeight)) + ((float) (this.labelMargin * 2));
                        if (bottom > ((float) this.computator.getContentRectMinusAllMargins().bottom)) {
                            top = ((this.drawRect.bottom - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                            bottom = this.drawRect.bottom - offset;
                        } else {
                            top = this.drawRect.bottom + offset;
                        }
                    }
                } else {
                    return;
                }
            } else if (columnValue.getValue() >= this.baseValue) {
                top = this.drawRect.top;
                bottom = (this.drawRect.top + ((float) labelHeight)) + ((float) (this.labelMargin * 2));
            } else {
                top = (this.drawRect.bottom - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = this.drawRect.bottom;
            }
            this.labelBackgroundRect.set(left, top, right, bottom);
            drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - numChars, numChars, columnValue.getDarkenColor());
        }
    }

    public void setTargetCalory(float targetCalory) {
        this.targetCalory = new Float(targetCalory).intValue();
    }

    public void setMaxCaloryLimit(float maxCalory) {
        this.maxCaloryLimit = maxCalory;
    }

    public void setTargetCalory(float targetCalory, int lineColor, int textColor) {
        if (lineColor != 0) {
            this.caloryLinePaint.setColor(lineColor);
        }
        if (textColor != 0) {
            this.targetCaloryTextColor = textColor;
        }
        setTargetCalory(targetCalory);
    }

    public void setHighlightAboveTarget(boolean b, @ColorInt int aboveTargetColor) {
        this.isHigllightAboveTarget = b;
        this.aboveTargetLineColor = aboveTargetColor;
    }

    public void setIsRound(boolean b, int cornerRound) {
        this.isRound = b;
        this.cornerRound = cornerRound;
    }

    public void drawTargetCaloryLine(Canvas drawCanvas) {
        if (this.targetCalory > 0) {
            float currentLeft = 0.0f;
            float currentRight = 0.0f;
            int target = this.targetCalory;
            Viewport currentViewport = this.computator.getCurrentViewport();
            if (currentViewport != null) {
                currentLeft = currentViewport.left;
                currentRight = currentViewport.right;
            }
            float rawX1 = this.computator.computeRawX(currentLeft);
            float rawX2 = this.computator.computeRawX(currentRight);
            float y = this.computator.computeRawY((float) target);
            Path path1 = new Path();
            path1.moveTo(rawX1, y);
            path1.lineTo(rawX2, y);
            drawCanvas.drawPath(path1, this.caloryLinePaint);
        }
    }

    public void drawTargetCaloryText(Canvas drawCanvas) {
        if (this.targetCalory > 0) {
            float currentLeft = 0.0f;
            Viewport currentViewport = this.computator.getCurrentViewport();
            if (currentViewport != null) {
                currentLeft = currentViewport.left;
            }
            float rawX1 = this.computator.computeRawX(currentLeft);
            float y = this.computator.computeRawY((float) this.targetCalory);
            String targetStr = String.valueOf(this.targetCalory);
            float textWidth = this.caloryTextPaint.measureText(targetStr);
            float textX = (rawX1 - textWidth) - ((float) ChartUtils.dp2px(this.density, 2));
            float textY = y + (getTextHeight() / aj.hA);
            RectF rect = new RectF(textX, y - (getTextHeight() / 2.0f), textX + textWidth, (getTextHeight() / 2.0f) + y);
            this.caloryTextPaint.setColor(ChartUtils.COLOR_BACKGROUND);
            drawCanvas.drawRoundRect(rect, 5.0f, 5.0f, this.caloryTextPaint);
            if (this.targetCaloryTextColor != 0) {
                this.caloryTextPaint.setColor(this.targetCaloryTextColor);
            } else {
                this.caloryTextPaint.setColor(ChartUtils.DEFAULT_TARGET_COLOR);
            }
            drawCanvas.drawText(targetStr, textX, textY, this.caloryTextPaint);
        }
    }

    private float getTextHeight() {
        FontMetrics fm = this.caloryTextPaint.getFontMetrics();
        return ((float) Math.ceil((double) (fm.descent - fm.top))) + 2.0f;
    }

    private float getColumnValue(SubcolumnValue columnValue) {
        if (this.maxCaloryLimit > 0.0f) {
            return columnValue.getValue() > this.maxCaloryLimit ? this.maxCaloryLimit : columnValue.getValue();
        } else {
            return columnValue.getValue();
        }
    }
}
