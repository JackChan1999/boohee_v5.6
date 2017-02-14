package com.boohee.one.pedometer;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.boohee.database.StepsPreference;
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

public class StepChartHelper {
    private List<AxisValue> axisValues = new ArrayList();
    private ColumnChartView chartView;
    private List<Column> columns = new ArrayList();
    private Context context;
    private List<Integer> mRecordValues = new ArrayList();
    private float max;
    private float min = 0.0f;
    private Resources resources;
    private List<StepModel> stepsRecord   = new ArrayList();
    private float           viewportLeft  = 0.0f;
    private float           viewportRight = 0.0f;

    public void initLine(Context context, ColumnChartView chartView, List<StepModel> steps, float
            viewportLeft, float viewportRight) {
        this.context = context;
        this.chartView = chartView;
        this.stepsRecord = steps;
        this.viewportLeft = viewportLeft;
        this.viewportRight = viewportRight;
        this.resources = context.getResources();
        this.columns = getColumnsData(steps);
        if (this.columns != null && this.columns.size() != 0) {
            this.axisValues = getXals(steps);
            if (this.axisValues != null && this.axisValues.size() != 0) {
                this.max = (float) (((double) StepsPreference.getStepsTarget()) * 1.2d);
                setViewport();
                setColumnData();
            }
        }
    }

    private List<Column> getColumnsData(List<StepModel> steps) {
        if (steps == null || steps.size() == 0) {
            return null;
        }
        List<Column> columns = new ArrayList();
        for (int i = 0; i < steps.size(); i++) {
            StepModel step = (StepModel) steps.get(i);
            List<SubcolumnValue> values = new ArrayList();
            values.add(new SubcolumnValue((float) step.step, ContextCompat.getColor(this.context,
                    R.color.jv)));
            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);
        }
        return columns;
    }

    private List<AxisValue> getXals(List<StepModel> steps) {
        if (steps == null || steps.size() == 0) {
            return null;
        }
        List<AxisValue> axisValueList = new ArrayList();
        for (int i = 0; i < steps.size(); i++) {
            StepModel step = (StepModel) steps.get(i);
            axisValueList.add(new AxisValue((float) i, (step.getMonth() + "/" + step.getDay())
                    .toCharArray()));
        }
        return axisValueList;
    }

    private void setViewport() {
        Viewport v_max;
        int axisSize = this.axisValues.size();
        if (this.viewportLeft != 0.0f || this.viewportRight != 0.0f) {
            v_max = new Viewport(-1.0f, this.max, (float) this.axisValues.size(), this.min);
        } else if (((float) axisSize) < StepHistoryActivity.PER_PAGE) {
            this.viewportLeft = -1.0f;
            this.viewportRight = StepHistoryActivity.PER_PAGE - 1.0f;
            v_max = new Viewport(-1.0f, this.max, StepHistoryActivity.PER_PAGE, this.min);
        } else {
            this.viewportLeft = ((float) axisSize) - StepHistoryActivity.PER_PAGE;
            this.viewportRight = (float) axisSize;
            v_max = new Viewport(-1.0f, this.max, (float) this.axisValues.size(), this.min);
        }
        Viewport v_current = new Viewport(this.viewportLeft, this.max, this.viewportRight - 0.2f,
                this.min);
        this.chartView.setMaximumViewport(v_max);
        this.chartView.setCurrentViewport(v_current);
    }

    private void setColumnData() {
        ColumnChartData data = new ColumnChartData(this.columns);
        data.setAxisXBottom(new Axis(this.axisValues));
        Axis axis = new Axis();
        data.setAxisYLeft(Axis.generateAxisFromRange(0.0f, (float) StepsPreference.getStepsTarget
                (), 5000.0f).setHasLines(false).setMaxLabelChars(6));
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundColor(this.resources.getColor(R.color.in));
        data.setValueLabelsTextColor(this.resources.getColor(R.color.du));
        this.chartView.setViewportCalculationEnabled(false);
        this.chartView.setZoomEnabled(false);
        this.chartView.setColumnChartData(data);
        this.chartView.setVisibility(0);
        this.chartView.setValueTouchEnabled(false);
    }
}
