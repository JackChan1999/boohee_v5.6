package com.boohee.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.boohee.model.mine.Measure;
import com.boohee.one.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartMeasureHelper {
    public static final int TYPE_MODE_MONTH = 1;
    public static final int TYPE_MODE_WEEK  = 0;
    public static final int TYPE_MODE_YEAR  = 2;
    private LineChartView lineChart;
    private List<AxisValue> mAxisValues = new ArrayList();
    private String  mBeginDate;
    private Context mContext;
    private String  mEndDate;
    private List<PointValue> mPointValues  = new ArrayList();
    private List<Float>      mRecordValues = new ArrayList();
    private int mTypeMode;
    private float         mViewportLeft  = 0.0f;
    private float         mViewportRight = 0.0f;
    private List<Measure> mWeightRecords = new ArrayList();
    private List<String> mXDates;
    private Resources    resources;

    public void clear() {
        this.mRecordValues.clear();
        this.mAxisValues.clear();
        this.mPointValues.clear();
        this.mWeightRecords.clear();
        if (this.lineChart != null) {
            this.lineChart = null;
        }
    }

    public void initLine(Context context, LineChartView lineChart, String beginDate, String
            endDate, List<Measure> measures, float viewportLeft, float viewportRight, int
            typeMode, boolean isLandscape) {
        this.mContext = context;
        this.mBeginDate = beginDate;
        this.mEndDate = endDate;
        this.mWeightRecords = measures;
        this.lineChart = lineChart;
        this.mViewportLeft = viewportLeft;
        this.mViewportRight = viewportRight;
        this.mTypeMode = typeMode;
        this.resources = context.getResources();
        this.mXDates = DateFormatUtils.getEveryday(beginDate, endDate);
        if (this.mXDates != null && this.mXDates.size() != 0) {
            if (measures == null || measures.size() == 0) {
                this.mPointValues = getYValsForNone(this.mXDates.size());
            } else {
                this.mPointValues = getYVals(measures);
            }
            if (this.mPointValues != null && this.mPointValues.size() != 0) {
                this.mAxisValues = getXals();
                if (this.mAxisValues != null && this.mAxisValues.size() != 0) {
                    setViewport();
                    setLinData();
                }
            }
        }
    }

    private void setLinData() {
        LineChartData lineData = new LineChartData(getLines());
        lineData.setAxisXBottom(new Axis(this.mAxisValues).setHasLines(true).setTextColor(this
                .mContext.getResources().getColor(R.color.jo)).setLineColor(this.mContext
                .getResources().getColor(R.color.jp)));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        this.lineChart.setViewportCalculationEnabled(false);
        this.lineChart.setZoomEnabled(false);
        this.lineChart.setLineChartData(lineData);
        this.lineChart.setVisibility(0);
    }

    private List<Line> getLines() {
        Line line = new Line(this.mPointValues);
        line.setColor(this.resources.getColor(R.color.js));
        line.setLabelColor(this.resources.getColor(R.color.js));
        line.setFilled(true);
        line.setCubic(false);
        line.setHasLabels(true);
        if (this.mWeightRecords == null || this.mWeightRecords.size() == 0 || this.mTypeMode > 0) {
            line.setHasPoints(false);
        } else {
            line.setHasPoints(true);
        }
        line.setFormatter(new SimpleLineChartValueFormatter().setDecimalDigitsNumber(1));
        List<Line> lines = new ArrayList();
        lines.add(line);
        return lines;
    }

    private void setViewport() {
        int axisSize = this.mAxisValues.size();
        if (this.mViewportLeft == 0.0f && this.mViewportRight == 0.0f) {
            if (axisSize >= 7) {
                switch (this.mTypeMode) {
                    case 0:
                        this.mViewportLeft = (float) (axisSize - 7);
                        this.mViewportRight = (float) axisSize;
                        break;
                    case 1:
                        this.mViewportLeft = (float) (axisSize - 30);
                        this.mViewportRight = (float) (axisSize + 4);
                        break;
                    case 2:
                        this.mViewportLeft = (float) (axisSize - 182);
                        this.mViewportRight = (float) axisSize;
                        break;
                    default:
                        break;
                }
            }
            this.mViewportLeft = -1.0f;
            this.mViewportRight = 6.0f;
        }
        float max = ((Float) Collections.max(this.mRecordValues)).floatValue();
        float min = ((Float) Collections.min(this.mRecordValues)).floatValue();
        Viewport v_max = new Viewport(-1.0f, max + 5.0f, (float) this.mAxisValues.size(), min - 5
        .0f);
        if (this.mTypeMode > 0) {
            v_max = new Viewport(-1.0f, max + 5.0f, (float) (this.mAxisValues.size() + 4), min -
                    5.0f);
        }
        Viewport v_corrent = new Viewport(this.mViewportLeft, max + 5.0f, this.mViewportRight,
                min - 5.0f);
        this.lineChart.setMaximumViewport(v_max);
        this.lineChart.setCurrentViewport(v_corrent);
    }

    private List<PointValue> getYValsForNone(int size) {
        this.mRecordValues.clear();
        List<PointValue> pointValues = new ArrayList();
        pointValues.add(new PointValue(0.0f, 40.0f));
        pointValues.add(new PointValue((float) (size - 1), 40.0f));
        this.mRecordValues.add(Float.valueOf(30.0f));
        this.mRecordValues.add(Float.valueOf(60.0f));
        return pointValues;
    }

    private List<PointValue> getYVals(List<Measure> measures) {
        this.mRecordValues.clear();
        ArrayList<PointValue> yVals = new ArrayList();
        yVals.add(new PointValue(-2.0f, ((Measure) measures.get(0)).value));
        for (int i = 0; i < measures.size(); i++) {
            yVals.add(new PointValue((float) getXIndex(this.mXDates, ((Measure) measures.get(i))
                    .record_on), ((Measure) measures.get(i)).value, ((Measure) measures.get(i))
                    .record_on));
            this.mRecordValues.add(Float.valueOf(((Measure) measures.get(i)).value));
        }
        return yVals;
    }

    private List<AxisValue> getXals() {
        List<AxisValue> axisValues = new ArrayList();
        ArrayList<String> xVals = DateFormatUtils.getEveryMonthDay(this.mBeginDate, this.mEndDate);
        if (xVals == null) {
            return null;
        }
        int size = xVals.size();
        for (int i = 0; i < size; i++) {
            axisValues.add(new AxisValue((float) i, ((String) xVals.get(i)).toCharArray()));
        }
        return axisValues;
    }

    private int getXIndex(List<String> dates, String date) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        for (int i = 0; i < dates.size(); i++) {
            if (date.equals(dates.get(i))) {
                return i;
            }
        }
        return 0;
    }
}
