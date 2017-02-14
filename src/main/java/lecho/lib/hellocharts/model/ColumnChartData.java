package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;

public class ColumnChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    public static final float DEFAULT_FILL_RATIO = 0.75f;
    private float baseValue = 0.0f;
    private List<Column> columns = new ArrayList();
    private float fillRatio = DEFAULT_FILL_RATIO;
    private boolean isStacked = false;

    public ColumnChartData(List<Column> columns) {
        setColumns(columns);
    }

    public ColumnChartData(ColumnChartData data) {
        super(data);
        this.isStacked = data.isStacked;
        this.fillRatio = data.fillRatio;
        for (Column column : data.columns) {
            this.columns.add(new Column(column));
        }
    }

    public static ColumnChartData generateDummyData() {
        ColumnChartData data = new ColumnChartData();
        List<Column> columns = new ArrayList(4);
        for (int i = 1; i <= 4; i++) {
            List values = new ArrayList(4);
            values.add(new SubcolumnValue((float) i));
            columns.add(new Column(values));
        }
        data.setColumns(columns);
        return data;
    }

    public void update(float scale) {
        for (Column column : this.columns) {
            column.update(scale);
        }
    }

    public void finish() {
        for (Column column : this.columns) {
            column.finish();
        }
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public ColumnChartData setColumns(List<Column> columns) {
        if (columns == null) {
            this.columns = new ArrayList();
        } else {
            this.columns = columns;
        }
        return this;
    }

    public boolean isStacked() {
        return this.isStacked;
    }

    public ColumnChartData setStacked(boolean isStacked) {
        this.isStacked = isStacked;
        return this;
    }

    public float getFillRatio() {
        return this.fillRatio;
    }

    public ColumnChartData setFillRatio(float fillRatio) {
        if (fillRatio < 0.0f) {
            fillRatio = 0.0f;
        }
        if (fillRatio > 1.0f) {
            fillRatio = 1.0f;
        }
        this.fillRatio = fillRatio;
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public ColumnChartData setBaseValue(float baseValue) {
        this.baseValue = baseValue;
        return this;
    }
}
