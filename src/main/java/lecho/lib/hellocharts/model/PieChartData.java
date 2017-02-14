package lecho.lib.hellocharts.model;

import android.graphics.Typeface;
import com.boohee.myview.IntFloatWheelView;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.PieChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimplePieChartValueFormatter;

public class PieChartData extends AbstractChartData {
    public static final float DEFAULT_CENTER_CIRCLE_SCALE = 0.6f;
    public static final int DEFAULT_CENTER_TEXT1_SIZE_SP = 42;
    public static final int DEFAULT_CENTER_TEXT2_SIZE_SP = 16;
    private static final int DEFAULT_SLICE_SPACING_DP = 2;
    private int centerCircleColor = 0;
    private float centerCircleScale = DEFAULT_CENTER_CIRCLE_SCALE;
    private String centerText1;
    private int centerText1Color = -16777216;
    private int centerText1FontSize = 42;
    private Typeface centerText1Typeface;
    private String centerText2;
    private int centerText2Color = -16777216;
    private int centerText2FontSize = 16;
    private Typeface centerText2Typeface;
    private PieChartValueFormatter formatter = new SimplePieChartValueFormatter();
    private boolean hasCenterCircle = false;
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private boolean hasLabelsOutside = false;
    private int slicesSpacing = 2;
    private List<SliceValue> values = new ArrayList();

    public PieChartData() {
        setAxisXBottom(null);
        setAxisYLeft(null);
    }

    public PieChartData(List<SliceValue> values) {
        setValues(values);
        setAxisXBottom(null);
        setAxisYLeft(null);
    }

    public PieChartData(PieChartData data) {
        super(data);
        this.formatter = data.formatter;
        this.hasLabels = data.hasLabels;
        this.hasLabelsOnlyForSelected = data.hasLabelsOnlyForSelected;
        this.hasLabelsOutside = data.hasLabelsOutside;
        this.hasCenterCircle = data.hasCenterCircle;
        this.centerCircleColor = data.centerCircleColor;
        this.centerCircleScale = data.centerCircleScale;
        this.centerText1Color = data.centerText1Color;
        this.centerText1FontSize = data.centerText1FontSize;
        this.centerText1Typeface = data.centerText1Typeface;
        this.centerText1 = data.centerText1;
        this.centerText2Color = data.centerText2Color;
        this.centerText2FontSize = data.centerText2FontSize;
        this.centerText2Typeface = data.centerText2Typeface;
        this.centerText2 = data.centerText2;
        for (SliceValue sliceValue : data.values) {
            this.values.add(new SliceValue(sliceValue));
        }
    }

    public static PieChartData generateDummyData() {
        PieChartData data = new PieChartData();
        List<SliceValue> values = new ArrayList(4);
        values.add(new SliceValue(40.0f));
        values.add(new SliceValue(20.0f));
        values.add(new SliceValue(30.0f));
        values.add(new SliceValue(IntFloatWheelView.DEFAULT_VALUE));
        data.setValues(values);
        return data;
    }

    public void update(float scale) {
        for (SliceValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (SliceValue value : this.values) {
            value.finish();
        }
    }

    public void setAxisXBottom(Axis axisX) {
        super.setAxisXBottom(null);
    }

    public void setAxisYLeft(Axis axisY) {
        super.setAxisYLeft(null);
    }

    public List<SliceValue> getValues() {
        return this.values;
    }

    public PieChartData setValues(List<SliceValue> values) {
        if (values == null) {
            this.values = new ArrayList();
        } else {
            this.values = values;
        }
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public PieChartData setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        if (hasLabels) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public PieChartData setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected;
        if (hasLabelsOnlyForSelected) {
            this.hasLabels = false;
        }
        return this;
    }

    public boolean hasLabelsOutside() {
        return this.hasLabelsOutside;
    }

    public PieChartData setHasLabelsOutside(boolean hasLabelsOutside) {
        this.hasLabelsOutside = hasLabelsOutside;
        return this;
    }

    public boolean hasCenterCircle() {
        return this.hasCenterCircle;
    }

    public PieChartData setHasCenterCircle(boolean hasCenterCircle) {
        this.hasCenterCircle = hasCenterCircle;
        return this;
    }

    public int getCenterCircleColor() {
        return this.centerCircleColor;
    }

    public PieChartData setCenterCircleColor(int centerCircleColor) {
        this.centerCircleColor = centerCircleColor;
        return this;
    }

    public float getCenterCircleScale() {
        return this.centerCircleScale;
    }

    public PieChartData setCenterCircleScale(float centerCircleScale) {
        this.centerCircleScale = centerCircleScale;
        return this;
    }

    public int getCenterText1Color() {
        return this.centerText1Color;
    }

    public PieChartData setCenterText1Color(int centerText1Color) {
        this.centerText1Color = centerText1Color;
        return this;
    }

    public int getCenterText1FontSize() {
        return this.centerText1FontSize;
    }

    public PieChartData setCenterText1FontSize(int centerText1FontSize) {
        this.centerText1FontSize = centerText1FontSize;
        return this;
    }

    public Typeface getCenterText1Typeface() {
        return this.centerText1Typeface;
    }

    public PieChartData setCenterText1Typeface(Typeface text1Typeface) {
        this.centerText1Typeface = text1Typeface;
        return this;
    }

    public String getCenterText1() {
        return this.centerText1;
    }

    public PieChartData setCenterText1(String centerText1) {
        this.centerText1 = centerText1;
        return this;
    }

    public String getCenterText2() {
        return this.centerText2;
    }

    public PieChartData setCenterText2(String centerText2) {
        this.centerText2 = centerText2;
        return this;
    }

    public int getCenterText2Color() {
        return this.centerText2Color;
    }

    public PieChartData setCenterText2Color(int centerText2Color) {
        this.centerText2Color = centerText2Color;
        return this;
    }

    public int getCenterText2FontSize() {
        return this.centerText2FontSize;
    }

    public PieChartData setCenterText2FontSize(int centerText2FontSize) {
        this.centerText2FontSize = centerText2FontSize;
        return this;
    }

    public Typeface getCenterText2Typeface() {
        return this.centerText2Typeface;
    }

    public PieChartData setCenterText2Typeface(Typeface text2Typeface) {
        this.centerText2Typeface = text2Typeface;
        return this;
    }

    public int getSlicesSpacing() {
        return this.slicesSpacing;
    }

    public PieChartData setSlicesSpacing(int sliceSpacing) {
        this.slicesSpacing = sliceSpacing;
        return this;
    }

    public PieChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public PieChartData setFormatter(PieChartValueFormatter formatter) {
        if (formatter != null) {
            this.formatter = formatter;
        }
        return this;
    }
}
