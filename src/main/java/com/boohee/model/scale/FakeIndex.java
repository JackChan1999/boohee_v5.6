package com.boohee.model.scale;

public class FakeIndex extends ScaleIndex {
    private String name;
    private String unit;
    private float  value;

    public FakeIndex(String name, float value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public int getColor() {
        return 0;
    }

    public String getUnit() {
        return this.unit;
    }

    public float getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return 0;
    }

    public String getLevelName() {
        return "";
    }

    public int getDes() {
        return 0;
    }
}
