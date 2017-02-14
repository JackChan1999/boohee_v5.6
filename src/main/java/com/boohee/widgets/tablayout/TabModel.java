package com.boohee.widgets.tablayout;

public class TabModel implements TabModelInterface {
    public String tabDes;
    public String tabName;

    public TabModel(String name, String desc) {
        this.tabName = name;
        this.tabDes = desc;
    }

    public String getTabName() {
        return this.tabName;
    }

    public String getDes() {
        return this.tabDes;
    }
}
