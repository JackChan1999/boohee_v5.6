package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.PointValue;

public interface LineChartOnValueSelectListener extends OnValueDeselectListener {
    void onImageSelected(int i, int i2, PointValue pointValue);

    void onPopSelected(int i, int i2, PointValue pointValue);

    void onValueSelected(int i, int i2, PointValue pointValue);
}
