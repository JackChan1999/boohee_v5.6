package kankan.wheel.widget.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kankan.wheel.widget.DensityUtil;

public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {
    public static final int DEFAULT_TEXT_COLOR = -6710887;
    public static final int DEFAULT_TEXT_PADDING = 30;
    public static final int DEFAULT_TEXT_SIZE = 24;
    public static final int LABEL_COLOR = -9437072;
    protected static final int NO_RESOURCE = 0;
    public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
    protected Context context;
    protected int emptyItemResourceId;
    protected LayoutInflater inflater;
    protected int itemResourceId;
    protected int itemTextResourceId;
    private int textColor;
    private int textPadding;
    private int textSize;

    protected abstract CharSequence getItemText(int i);

    protected AbstractWheelTextAdapter(Context context) {
        this(context, -1);
    }

    protected AbstractWheelTextAdapter(Context context, int itemResource) {
        this(context, itemResource, 0);
    }

    protected AbstractWheelTextAdapter(Context context, int itemResource, int itemTextResource) {
        this.textColor = DEFAULT_TEXT_COLOR;
        this.textSize = 24;
        this.textPadding = 30;
        this.context = context;
        this.itemResourceId = itemResource;
        this.itemTextResourceId = itemTextResource;
        this.textPadding = DensityUtil.dip2px(context, 15.0f);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getItemResource() {
        return this.itemResourceId;
    }

    public void setItemResource(int itemResourceId) {
        this.itemResourceId = itemResourceId;
    }

    public int getItemTextResource() {
        return this.itemTextResourceId;
    }

    public void setItemTextResource(int itemTextResourceId) {
        this.itemTextResourceId = itemTextResourceId;
    }

    public int getEmptyItemResource() {
        return this.emptyItemResourceId;
    }

    public void setEmptyItemResource(int emptyItemResourceId) {
        this.emptyItemResourceId = emptyItemResourceId;
    }

    public View getItem(int index, View convertView, ViewGroup parent) {
        if (index < 0 || index >= getItemsCount()) {
            return null;
        }
        if (convertView == null) {
            convertView = getView(this.itemResourceId, parent);
        }
        TextView textView = getTextView(convertView, this.itemTextResourceId);
        if (textView != null) {
            CharSequence text = getItemText(index);
            if (text == null) {
                text = "";
            }
            textView.setText(text);
            textView.setPadding(0, this.textPadding, 0, this.textPadding);
            textView.setGravity(17);
            if (this.itemResourceId == -1) {
                configureTextView(textView);
            }
        }
        return convertView;
    }

    public View getEmptyItem(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getView(this.emptyItemResourceId, parent);
        }
        if (this.emptyItemResourceId == -1 && (convertView instanceof TextView)) {
            configureTextView((TextView) convertView);
        }
        return convertView;
    }

    protected void configureTextView(TextView view) {
        view.setTextColor(this.textColor);
        view.setGravity(17);
        view.setTextSize((float) this.textSize);
        view.setLines(1);
    }

    private TextView getTextView(View view, int textResource) {
        if (textResource == 0) {
            try {
                if (view instanceof TextView) {
                    return (TextView) view;
                }
            } catch (ClassCastException e) {
                Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", e);
            }
        }
        if (textResource != 0) {
            return (TextView) view.findViewById(textResource);
        }
        return null;
    }

    private View getView(int resource, ViewGroup parent) {
        switch (resource) {
            case -1:
                return new TextView(this.context);
            case 0:
                return null;
            default:
                return this.inflater.inflate(resource, parent, false);
        }
    }
}
