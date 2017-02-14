package kankan.wheel.widget.adapters;

import android.content.Context;

public class NumericWheelAdapter extends AbstractWheelTextAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    public String format;
    public int maxValue;
    public int minValue;

    public NumericWheelAdapter(Context context) {
        this(context, 0, 9);
    }

    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }

    public NumericWheelAdapter(Context context, int minValue, int maxValue, String format) {
        super(context);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public CharSequence getItemText(int index) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        int value = this.minValue + index;
        if (this.format == null) {
            return Integer.toString(value);
        }
        return String.format(this.format, new Object[]{Integer.valueOf(value)});
    }

    public int getItemsCount() {
        return (this.maxValue - this.minValue) + 1;
    }
}
