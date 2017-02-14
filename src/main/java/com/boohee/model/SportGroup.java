package com.boohee.model;

public class SportGroup extends ModelBase {
    public String name;
    public int    order_index;
    public int    status;

    public SportGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public SportGroup(int id, String name, int order_index) {
        this(id, name);
        this.order_index = order_index;
    }
}
