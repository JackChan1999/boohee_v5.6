package kankan.wheel.widget.adapters;

import android.content.Context;
import kankan.wheel.widget.WheelAdapter;

public class AdapterWheel extends AbstractWheelTextAdapter {
    private WheelAdapter adapter;

    public AdapterWheel(Context context, WheelAdapter adapter) {
        super(context);
        this.adapter = adapter;
    }

    public WheelAdapter getAdapter() {
        return this.adapter;
    }

    public int getItemsCount() {
        return this.adapter.getItemsCount();
    }

    protected CharSequence getItemText(int index) {
        return this.adapter.getItem(index);
    }
}
