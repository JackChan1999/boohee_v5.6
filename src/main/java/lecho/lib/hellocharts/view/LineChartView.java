package lecho.lib.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.DummyLineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.LineChartRenderer;

public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected LineChartOnValueSelectListener onValueTouchListener;
    protected float targetWeight;
    protected String unit;

    public LineChartView(Context context) {
        this(context, null, 0);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyLineChartOnValueSelectListener();
        this.chartRenderer = new LineChartRenderer(context, this, this, this);
        resetRendererAndTouchHandler();
        setLineChartData(LineChartData.generateDummyData());
    }

    public void setLineChartData(LineChartData data) {
        if (data == null) {
            this.data = LineChartData.generateDummyData();
        } else {
            this.data = data;
        }
        super.onChartDataChange();
    }

    public LineChartData getLineChartData() {
        return this.data;
    }

    public ChartData getChartData() {
        return this.data;
    }

    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            Line line = (Line) this.data.getLines().get(selectedValue.getFirstIndex());
            PointValue point = (PointValue) line.getValues().get(selectedValue.getSecondIndex());
            switch (selectedValue.getSelectedType()) {
                case 0:
                    this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
                    return;
                case 1:
                    this.onValueTouchListener.onImageSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
                    return;
                case 2:
                    if (line.hasLabels()) {
                        this.onValueTouchListener.onPopSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), point);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setTargetWeight(float targetWeight) {
        this.targetWeight = targetWeight;
    }

    public float getTargetWeight() {
        return this.targetWeight;
    }
}
