package kankan.wheel.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HighlightArrayWheelAdapter extends ArrayWheelAdapter<String> {
    int currentItem;
    int currentValue;

    public HighlightArrayWheelAdapter(Context context, String[] items, int current) {
        super(context, items);
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
