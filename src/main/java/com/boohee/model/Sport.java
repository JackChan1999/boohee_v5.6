package com.boohee.model;

import com.boohee.utils.FormulaUtils;

import java.io.Serializable;

public class Sport implements Serializable {
    public static final  int    UNIT_AMOUNT      = 60;
    public static final  String UNIT_NAME        = "分钟";
    private static final long   serialVersionUID = 396457142676489257L;
    public String big_photo_url;
    public int    group_id;
    public int    id;
    public String inice_photo_url;
    public String mets;
    public String name;
    public int    score;
    public String small_photo_url;
    public int    status;

    public int calcCalory(float weight) {
        return Math.round(FormulaUtils.calcCalorie(Float.parseFloat(this.mets), weight));
    }
}
