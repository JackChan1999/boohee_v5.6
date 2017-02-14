package com.boohee.model;

import java.util.ArrayList;

public class Banners extends ModelBase {
    public ArrayList<Goods> goodsList;
    public String           type;

    public Banners(ArrayList<Goods> goodsList, int id, String type) {
        this.goodsList = goodsList;
        this.id = id;
        this.type = type;
    }
}
