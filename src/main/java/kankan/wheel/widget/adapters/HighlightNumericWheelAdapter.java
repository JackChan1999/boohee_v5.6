package kankan.wheel.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HighlightNumericWheelAdapter extends NumericWheelAdapter {
    int currentItem;
    int currentValue;

    public HighlightNumericWheelAdapter(Context context, int minValue, int maxValue, int current) {
        super(context, minValue, maxValue);
        this.currentValue = current;
        setTextSize(16);
    }

    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        if (this.currentItem == this.currentValue) {
            view.setTextColor(-35584);
            view.setGravity(17);
        }
    }

    public View getItem(int index, View cachedView, ViewGroup parent) {
        this.currentItem = index;
        return super.getItem(index, cachedView, parent);
    }
}
