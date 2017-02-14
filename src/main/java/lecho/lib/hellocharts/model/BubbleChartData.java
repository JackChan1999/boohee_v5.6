package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.BubbleChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleBubbleChartValueFormatter;
import uk.co.senab.photoview.IPhotoView;

public class BubbleChartData extends AbstractChartData {
    public static final float DEFAULT_BUBBLE_SCALE = 1.0f;
    public static final int DEFAULT_MIN_BUBBLE_RADIUS_DP = 6;
    private float bubbleScale = 1.0f;
    private BubbleChartValueFormatter formatter = new SimpleBubbleChartValueFormatter();
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private int minBubbleRadius = 6;
    private List<BubbleValue> values = new ArrayList();

    public BubbleChartData(List<BubbleValue> values) {
        setValues(values);
    }

    public BubbleChartData(BubbleChartData data) {
        super(data);
        this.formatter = data.formatter;
        this.hasLabels = data.hasLabels;
        this.hasLabelsOnlyForSelected = data.hasLabelsOnlyForSelected;
        this.minBubbleRadius = data.minBubbleRadius;
        this.bubbleScale = data.bubbleScale;
        for (BubbleValue bubbleValue : data.getValues()) {
            this.values.add(new BubbleValue(bubbleValue));
        }
    }

    public static BubbleChartData generateDummyData() {
        BubbleChartData data = new BubbleChartData();
        List<BubbleValue> values = new ArrayList(4);
        values.add(new BubbleValue(0.0f, 20.0f, 15000.0f));
        values.add(new BubbleValue(IPhotoView.DEFAULT_MAX_SCALE, 22.0f, 20000.0f));
        values.add(new BubbleValue(5.0f, 25.0f, 5000.0f));
        values.add(new BubbleValue(7.0f, 30.0f, 30000.0f));
        values.add(new BubbleValue(11.0f, 22.0f, 10.0f));
        data.setValues(values);
        return data;
    }

    public void update(float scale) {
        for (BubbleValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (BubbleValue value : this.values) {
            value.finish();
        }
    }

    public List<BubbleValue> getValues() {
        return this.values;
    }

    public BubbleChartData setValues(List<BubbleValue> values) {
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

    public BubbleChartData setHasLabels(boolean hasLabels) {
        this.hasLabels = hasLabels;
        if (hasLabels) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public BubbleChartData setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected;
        if (hasLabelsOnlyForSelected) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getMinBubbleRadius() {
        return this.minBubbleRadius;
    }

    public void setMinBubbleRadius(int minBubbleRadius) {
        this.minBubbleRadius = minBubbleRadius;
    }

    public float getBubbleScale() {
        return this.bubbleScale;
    }

    public void setBubbleScale(float bubbleScale) {
        this.bubbleScale = bubbleScale;
    }

    public BubbleChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public BubbleChartData setFormatter(BubbleChartValueFormatter formatter) {
        if (formatter != null) {
            this.formatter = formatter;
        }
        return this;
    }
}
