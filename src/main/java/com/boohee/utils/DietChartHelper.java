package com.boohee.utils;

import android.content.Context;
import android.content.res.Resources;

import com.boohee.model.mine.DietRecord;
import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class DietChartHelper {
    private List<AxisValue> axisValues = new ArrayList();
    private ColumnChartView chartView;
    private List<Column> columns = new ArrayList();
    private Context context;
    private List<DietRecord> dietRecords   = new ArrayList();
    private List<Float>      mRecordValues = new ArrayList();
    private float            max           = 2800.0f;
    private float            min           = 0.0f;
    private Resources resources;
    private float viewportLeft  = 0.0f;
    private float viewportRight = 0.0f;

    public void initLine(Context context, ColumnChartView chartView, List<DietRecord>
            dietRecords, float viewportLeft, float viewportRight) {
        this.context = context;
        this.chartView = chartView;
        this.dietRecords = dietRecords;
        this.viewportLeft = viewportLeft;
        this.viewportRight = viewportRight;
        this.resources = context.getResources();
        this.columns = getColumnsData(dietRecords);
        if (this.columns != null && this.columns.size() != 0) {
            this.axisValues = getXals(dietRecords);
            if (this.axisValues != null && this.axisValues.size() != 0) {
                setViewport();
                setColumnData();
            }
        }
    }

    private List<Column> getColumnsData(List<DietRecord> records) {
        if (records == null || records.size() == 0) {
            return null;
        }
        this.mRecordValues.clear();
        List<Column> columnList = new ArrayList();
        for (int i = 0; i < records.size(); i++) {
            List<SubcolumnValue> values = new ArrayList();
            values.add(new SubcolumnValue(((DietRecord) records.get(i)).eating_calory, this
                    .resources.getColor(R.color.cq)));
            values.add(new SubcolumnValue(((DietRecord) records.get(i)).activity_calory, this
                    .resources.getColor(R.color.i0)));
            this.mRecordValues.add(Float.valueOf(((DietRecord) records.get(i)).eating_calory));
            this.mRecordValues.add(Float.valueOf(((DietRecord) records.get(i)).activity_calory));
            Column column = new Column(values);
            column.setHasLabels(true);
            columnList.add(column);
        }
        return columnList;
    }

    private List<AxisValue> getXals(List<DietRecord> records) {
        if (records == null || records.size() == 0) {
            return null;
        }
        List<AxisValue> axisValues = new ArrayList();
        for (int i = 0; i < records.size(); i++) {
            axisValues.add(new AxisValue((float) i, (((DietRecord) records.get(i)).getMonth() +
                    "/" + ((DietRecord) records.get(i)).getDay()).toCharArray()));
        }
        return axisValues;
    }

    private void setViewport() {
        int axisSize = this.axisValues.size();
        if (this.viewportLeft == 0.0f && this.viewportRight == 0.0f) {
            if (axisSize < 9) {
                this.viewportLeft = -1.0f;
                this.viewportRight = 8.0f;
            } else {
                this.viewportLeft = (float) (axisSize - 9);
                this.viewportRight = (float) axisSize;
            }
        }
        Viewport v_max = new Viewport(-1.0f, this.max, (float) this.axisValues.size(), this.min);
        Viewport v_current = new Viewport(this.viewportLeft, this.max, this.viewportRight - 0.2f,
                this.min);
        this.chartView.setMaximumViewport(v_max);
        this.chartView.setCurrentViewport(v_current);
    }

    private void setColumnData() {
        ColumnChartData data = new ColumnChartData(this.columns);
        data.setAxisXBottom(new Axis(this.axisValues));
        Axis axis = new Axis();
        data.setAxisYLeft(Axis.generateAxisFromRange(0.0f, 2800.0f, 500.0f).setHasLines(true)
                .setMaxLabelChars(4));
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundColor(this.resources.getColor(R.color.in));
        data.setValueLabelsTextColor(this.resources.getColor(R.color.du));
        this.chartView.setViewportCalculationEnabled(false);
        this.chartView.setZoomEnabled(false);
        this.chartView.setColumnChartData(data);
        this.chartView.setVisibility(0);
    }
}
