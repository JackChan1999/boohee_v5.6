package lecho.lib.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyCompoLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SelectedValue.SelectedValueType;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.ComboLineColumnChartRenderer;

public class ComboLineColumnChartView extends AbstractChartView implements ComboLineColumnChartDataProvider {
    private static final String TAG = "ComboLineColumnChartView";
    protected ColumnChartDataProvider columnChartDataProvider;
    protected ComboLineColumnChartData data;
    protected LineChartDataProvider lineChartDataProvider;
    protected ComboLineColumnChartOnValueSelectListener onValueTouchListener;

    private class ComboColumnChartDataProvider implements ColumnChartDataProvider {
        private ComboColumnChartDataProvider() {
        }

        public ColumnChartData getColumnChartData() {
            return ComboLineColumnChartView.this.data.getColumnChartData();
        }

        public void setColumnChartData(ColumnChartData data) {
            ComboLineColumnChartView.this.data.setColumnChartData(data);
        }
    }

    private class ComboLineChartDataProvider implements LineChartDataProvider {
        private ComboLineChartDataProvider() {
        }

        public LineChartData getLineChartData() {
            return ComboLineColumnChartView.this.data.getLineChartData();
        }

        public void setLineChartData(LineChartData data) {
            ComboLineColumnChartView.this.data.setLineChartData(data);
        }
    }

    public ComboLineColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.columnChartDataProvider = new ComboColumnChartDataProvider();
        this.lineChartDataProvider = new ComboLineChartDataProvider();
        this.onValueTouchListener = new DummyCompoLineColumnChartOnValueSelectListener();
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, this.columnChartDataProvider, this.lineChartDataProvider));
        setComboLineColumnChartData(ComboLineColumnChartData.generateDummyData());
    }

    public ComboLineColumnChartData getComboLineColumnChartData() {
        return this.data;
    }

    public void setComboLineColumnChartData(ComboLineColumnChartData data) {
        if (data == null) {
            this.data = null;
        } else {
            this.data = data;
        }
        super.onChartDataChange();
    }

    public ChartData getChartData() {
        return this.data;
    }

    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (!selectedValue.isSet()) {
            this.onValueTouchListener.onValueDeselected();
        } else if (SelectedValueType.COLUMN.equals(selectedValue.getType())) {
            this.onValueTouchListener.onColumnValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), (SubcolumnValue) ((Column) this.data.getColumnChartData().getColumns().get(selectedValue.getFirstIndex())).getValues().get(selectedValue.getSecondIndex()));
        } else if (SelectedValueType.LINE.equals(selectedValue.getType())) {
            this.onValueTouchListener.onPointValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), (PointValue) ((Line) this.data.getLineChartData().getLines().get(selectedValue.getFirstIndex())).getValues().get(selectedValue.getSecondIndex()));
        } else {
            throw new IllegalArgumentException("Invalid selected value type " + selectedValue.getType().name());
        }
    }

    public ComboLineColumnChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(ComboLineColumnChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }
}
