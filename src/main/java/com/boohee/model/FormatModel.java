package com.boohee.model;

public class FormatModel extends ModelBase {
    public String  code;
    public boolean isChecked;
    public String  name;

    public FormatModel(boolean isChecked, String name) {
        this.isChecked = isChecked;
        this.name = name;
    }
}
