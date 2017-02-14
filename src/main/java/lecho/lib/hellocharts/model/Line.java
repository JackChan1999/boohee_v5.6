package lecho.lib.hellocharts.model;

import android.graphics.PathEffect;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.util.ChartUtils;

public class Line {
    private static final int DEFAULT_AREA_TRANSPARENCY = 64;
    private static final float DEFAULT_LINE_STROKE_WIDTH_DP = 1.5f;
    private static final int DEFAULT_POINT_RADIUS_DP = 6;
    private int AreaColor = ChartUtils.DEFAULT_AREA_COLOR;
    private int areaTransparency = 64;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private LineChartValueFormatter formatter = new SimpleLineChartValueFormatter();
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isCubic = false;
    private boolean isFilled = false;
    private boolean isSquare = false;
    private int labelColor = ChartUtils.DEFAULT_COLOR;
    private PathEffect pathEffect;
    private int pointRadius = 6;
    private ValueShape shape = ValueShape.CIRCLE;
    private float strokeWidth = DEFAULT_LINE_STROKE_WIDTH_DP;
    private List<PointValue> values = new ArrayList();

    public Line(List<PointValue> values) {
        setValues(values);
    }

    public Line(Line line) {
        this.color = line.color;
        this.darkenColor = line.color;
        this.areaTransparency = line.areaTransparency;
        this.strokeWidth = line.strokeWidth;
        this.pointRadius = line.pointRadius;
        this.hasPoints = line.hasPoints;
        this.hasLines = line.hasLines;
        this.hasLabels = line.hasLabels;
        this.hasLabelsOnlyForSelected = line.hasLabelsOnlyForSelected;
        this.isCubic = line.isCubic;
        this.isFilled = line.isFilled;
        this.shape = line.shape;
        this.pathEffect = line.pathEffect;
        this.formatter = line.formatter;
        for (PointValue pointValue : line.values) {
            this.values.add(new PointValue(pointValue));
        }
    }

    public void update(float scale) {
        for (PointValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (PointValue value : this.values) {
            value.finish();
        }
    }

    public List<PointValue> getValues() {
        return this.values;
    }

    public void setValues(List<PointValue> values) {
        if (values == null) {
            this.values = new ArrayList();
        } else {
            this.values = values;
        }
    }

    public int getColor() {
        return this.color;
    }

    public Line setColor(int color) {
        this.color = color;
        this.darkenColor = ChartUtils.darkenColor(color);
        return this;
    }

    public int getLabelColor() {
        return this.labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getAreaColor() {
        return this.AreaColor;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    public int getAreaTransparency() {
        return this.areaTransparency;
    }

    public Line setAreaTransparency(int areaTransparency) {
        this.areaTransparency = areaTransparency;
        return this;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public Line setStrokeWidth(int strokeWidth) {
        this.strokeWidth = (float) strokeWidth;
        return this;
    }

    public boolean hasPoints() {
        return this.hasPoints;
    }

    public Line setHasPoints(boolean hasPoints) {
        this.hasPoints = hasPoints;
        return this;
    }

    public boolean hasLines() {
        return this.hasLines;
    }

    public Line setHasLines(boolean hasLines) {
        this.hasLines = hasLines;
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public Line setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        if (hasLabels) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public Line setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected;
        if (hasLabelsOnlyForSelected) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getPointRadius() {
        return this.pointRadius;
    }

    public Line setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
        return this;
    }

    public boolean isCubic() {
        return this.isCubic;
    }

    public Line setCubic(boolean isCubic) {
        this.isCubic = isCubic;
        if (this.isSquare) {
            setSquare(false);
        }
        return this;
    }

    public boolean isSquare() {
        return this.isSquare;
    }

    public Line setSquare(boolean isSquare) {
        this.isSquare = isSquare;
        if (this.isCubic) {
            setCubic(false);
        }
        return this;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public Line setFilled(boolean isFilled) {
        this.isFilled = isFilled;
        return this;
    }

    public ValueShape getShape() {
        return this.shape;
    }

    public Line setShape(ValueShape shape) {
        this.shape = shape;
        return this;
    }

    public PathEffect getPathEffect() {
        return this.pathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
    }

    public LineChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public Line setFormatter(LineChartValueFormatter formatter) {
        if (formatter != null) {
            this.formatter = formatter;
        }
        return this;
    }
}
