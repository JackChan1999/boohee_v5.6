package com.kitnew.ble;

import com.kitnew.ble.utils.NumberUtils;

public class QNItemData {
    public String name;
    public int    type;
    public float  value;
    public String valueStr;

    public QNItemData(int type, String valueStr) {
        this.type = type;
        this.name = QNData.getNameWithType(type);
        this.valueStr = valueStr;
    }

    public QNItemData(int type, float value) {
        this.type = type;
        this.name = QNData.getNameWithType(type);
        if (type == 2) {
            try {
                this.value = NumberUtils.getTwoPrecision(value);
                return;
            } catch (NumberFormatException e) {
                this.value = 0.0f;
                return;
            }
        }
        this.value = NumberUtils.getOnePrecision(value);
    }

    public QNItemData(int type, double value) {
        this(type, (float) value);
    }
}
