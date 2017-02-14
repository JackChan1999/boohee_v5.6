package com.boohee.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.DashPathEffect;
import android.text.TextUtils;

import com.baidu.location.aj;
import com.boohee.model.mine.WeightPhoto;
import com.boohee.model.mine.WeightRecord;
import com.boohee.one.R;
import com.boohee.utility.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartHelper {
    public static final int TYPE_MODE_MONTH = 1;
    public static final int TYPE_MODE_WEEK  = 0;
    public static final int TYPE_MODE_YEAR  = 2;
    private LineChartView lineChart;
    private List<AxisValue> mAxisValues = new ArrayList();
    private String  mBeginDate;
    private Context mContext;
    private String  mEndDate;
    private Map<Integer, String> mHasLabelMaps = new HashMap();
    private boolean mIsLandscape;
    private List<PointValue> mPointValues  = new ArrayList();
    private List<Float>      mRecordValues = new ArrayList();
    private String mTargetDate;
    private float  mTargetWeight;
    private int    mTypeMode;
    private float              mViewportLeft  = 0.0f;
    private float              mViewportRight = 0.0f;
    private List<WeightRecord> mWeightRecords = new ArrayList();
    private List<String> mXDates;
    private Resources    resources;

    public void clear() {
        this.mRecordValues.clear();
        this.mHasLabelMaps.clear();
        this.mAxisValues.clear();
        this.mPointValues.clear();
        this.mWeightRecords.clear();
        if (this.lineChart != null) {
            this.lineChart = null;
        }
    }

    public void initLine(Context context, LineChartView lineChart, String beginDate, String
            endDate, List<WeightRecord> weightRecords, float targetWeight, String targetDate,
                         float viewportLeft, float viewportRight, int typeMode, boolean
                                 isLandscape) {
        this.mContext = context;
        this.mBeginDate = beginDate;
        this.mEndDate = endDate;
        this.mWeightRecords = weightRecords;
        this.lineChart = lineChart;
        this.mViewportLeft = viewportLeft;
        this.mViewportRight = viewportRight;
        this.mTypeMode = typeMode;
        this.mIsLandscape = isLandscape;
        this.mTargetWeight = targetWeight;
        this.mTargetDate = targetDate;
        this.resources = context.getResources();
        if (this.lineChart != null) {
            this.mXDates = DateFormatUtils.getEveryday(beginDate, endDate);
            if (this.mXDates != null && this.mXDates.size() != 0) {
                if (this.mWeightRecords == null || this.mWeightRecords.size() == 0) {
                    this.mPointValues = getYValsForNone(this.mXDates.size());
                } else {
                    this.mPointValues = getYVals(this.mWeightRecords);
                }
                if (this.mPointValues != null && this.mPointValues.size() != 0) {
                    this.mAxisValues = getXals();
                    if (this.mAxisValues != null && this.mAxisValues.size() != 0) {
                        setViewport();
                        setLineData();
                    }
                }
            }
        }
    }

    private void setLineData() {
        LineChartData lineData = new LineChartData(getLines());
        lineData.setImgRadius(15.0f);
        lineData.setAxisXBottom(new Axis(this.mAxisValues).setHasLines(true).setTextColor(this
                .mContext.getResources().getColor(R.color.jo)).setLineColor(this.mContext
                .getResources().getColor(R.color.jp)));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
        this.lineChart.setPadding(0, 0, 0, this.mIsLandscape ? DensityUtil.dip2px(this.mContext,
                10.0f) : DensityUtil.dip2px(this.mContext, (lineData.getImgRadius() * 2.0f) + 15
        .0f));
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
        Line target = getTargetLine();
        if (target != null) {
            lines.add(target);
        }
        lines.add(line);
        return lines;
    }

    private Line getTargetLine() {
        if (!needDrawTargetLine()) {
            return null;
        }
        PointValue lastRecordPoint = (PointValue) this.mPointValues.get(this.mPointValues.size()
                - 1);
        PointValue targetPoint = new PointValue(((AxisValue) this.mAxisValues.get(this
                .mAxisValues.size() - 1)).getValue(), this.mTargetWeight);
        List<PointValue> points = new ArrayList();
        points.add(lastRecordPoint);
        points.add(targetPoint);
        Line line = new Line(points);
        line.setColor(this.resources.getColor(R.color.da));
        line.setPathEffect(new DashPathEffect(new float[]{15.0f, 15.0f, 15.0f, 15.0f}, 0.0f));
        line.setHasLabels(false);
        if (this.mTypeMode > 0) {
            line.setHasPoints(false);
            return line;
        }
        line.setHasPoints(true);
        return line;
    }

    private void setViewport() {
        float lastRecordX = ((PointValue) this.mPointValues.get(this.mPointValues.size() - 1))
                .getX();
        if (this.mViewportLeft == 0.0f && this.mViewportRight == 0.0f) {
            if (lastRecordX >= 7.0f) {
                switch (this.mTypeMode) {
                    case 0:
                        if (!this.mIsLandscape) {
                            this.mViewportLeft = lastRecordX - 7.0f;
                            this.mViewportRight = lastRecordX + 1.0f;
                            break;
                        }
                        this.mViewportLeft = lastRecordX - 14.0f;
                        this.mViewportRight = lastRecordX + 1.0f;
                        break;
                    case 1:
                        this.mViewportLeft = lastRecordX - 30.0f;
                        this.mViewportRight = aj.hA + lastRecordX;
                        break;
                    case 2:
                        this.mViewportLeft = lastRecordX - 185.0f;
                        this.mViewportRight = 8.0f + lastRecordX;
                        break;
                    default:
                        break;
                }
            }
            this.mViewportLeft = -1.0f;
            this.mViewportRight = 6.0f;
        }
        float maxY = ((Float) Collections.max(this.mRecordValues)).floatValue();
        float minY = ((Float) Collections.min(this.mRecordValues)).floatValue();
        if (this.mTargetWeight > 0.0f) {
            maxY = Math.max(this.mTargetWeight, maxY);
            minY = Math.min(this.mTargetWeight, minY);
        }
        Viewport v_max = new Viewport(-1.0f, maxY + 5.0f, (float) this.mAxisValues.size(), minY -
                5.0f);
        if (this.mTypeMode > 0) {
            v_max = new Viewport(-1.0f, maxY + 5.0f, (float) (this.mAxisValues.size() + 4), minY
                    - 5.0f);
        }
        Viewport v_corrent = new Viewport(this.mViewportLeft, maxY + 5.0f, this.mViewportRight,
                minY - 5.0f);
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

    private List<PointValue> getYVals(List<WeightRecord> weightRecords) {
        this.mRecordValues.clear();
        ArrayList<PointValue> yVals = new ArrayList();
        if (weightRecords != null && weightRecords.size() > 0) {
            yVals.add(new PointValue(-2.0f, Float.parseFloat(((WeightRecord) weightRecords.get(0)
            ).weight)));
            for (int i = 0; i < weightRecords.size(); i++) {
                int xIndex = getXIndex(this.mXDates, ((WeightRecord) weightRecords.get(i))
                        .record_on);
                yVals.add(new PointValue((float) xIndex, Float.parseFloat(((WeightRecord)
                        weightRecords.get(i)).weight), ((WeightRecord) weightRecords.get(i))
                        .record_on));
                this.mRecordValues.add(Float.valueOf(Float.parseFloat(((WeightRecord)
                        weightRecords.get(i)).weight)));
                addPhoto((WeightRecord) weightRecords.get(i), xIndex);
            }
        }
        return yVals;
    }

    private List<AxisValue> getXals() {
        List<AxisValue> axisValues = new ArrayList();
        String endDate = this.mEndDate;
        if (needDrawTargetLine()) {
            endDate = this.mTargetDate;
        }
        ArrayList<String> xVals = DateFormatUtils.getEveryMonthDay(this.mBeginDate, endDate);
        if (xVals == null) {
            return null;
        }
        int size = xVals.size();
        int i = 0;
        while (i < size) {
            if (this.mHasLabelMaps == null || !this.mHasLabelMaps.containsKey(Integer.valueOf(i))) {
                axisValues.add(new AxisValue((float) i, ((String) xVals.get(i)).toCharArray()));
            } else {
                axisValues.add(new AxisValue((float) i, ((String) xVals.get(i)).toCharArray(),
                        this.mHasLabelMaps.get(Integer.valueOf(i))));
            }
            i++;
        }
        this.mHasLabelMaps.clear();
        return axisValues;
    }

    private boolean needDrawTargetLine() {
        boolean needDrawTargetLine;
        if (this.mTargetWeight <= 0.0f || this.mTargetDate == null || this.mEndDate.compareTo
                (this.mTargetDate) >= 0) {
            needDrawTargetLine = false;
        } else {
            needDrawTargetLine = true;
        }
        if (this.mWeightRecords == null || this.mWeightRecords.size() <= 0) {
            return needDrawTargetLine;
        }
        float recentWeight = Float.parseFloat(((WeightRecord) this.mWeightRecords.get(this
                .mWeightRecords.size() - 1)).weight);
        if (!needDrawTargetLine || recentWeight <= this.mTargetWeight) {
            return false;
        }
        return true;
    }

    private void addPhoto(WeightRecord record, int xIndex) {
        List<WeightPhoto> photos = record.photos;
        if (photos != null && photos.size() > 0) {
            this.mHasLabelMaps.put(Integer.valueOf(xIndex), ((WeightPhoto) photos.get(0))
                    .thumb_photo_url);
        }
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
