package com.boohee.model.mine;

import com.boohee.utils.ArithmeticUtil;
import com.kitnew.ble.QNData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeightRecord extends BaseRecord implements Serializable {
    public static final int BY_HAND  = 0;
    public static final int BY_QINIU = 1;
    public String bmi;
    public String bmr;
    public String bodyfat;
    public String bone;
    public String cachePhotos;
    public String created_at;
    public String measure_time;
    public String muscle;
    public List<WeightPhoto> photos = new ArrayList();
    public String protein;
    public int    source;
    public String subfat;
    public String visfat;
    public String water;
    public String weight;

    public WeightRecord(String weight, String record_on, List<WeightPhoto> photos) {
        this.weight = weight;
        this.record_on = record_on;
        this.photos = photos;
    }

    public WeightRecord(String weight, String record_on, String created_at) {
        this.weight = weight;
        this.record_on = record_on;
        this.created_at = created_at;
    }

    public WeightRecord(String weight, String record_on, String created_at, String cachePhotos) {
        this.weight = weight;
        this.record_on = record_on;
        this.created_at = created_at;
        this.cachePhotos = cachePhotos;
    }

    public WeightRecord(QNData data, String record_on) {
        this.weight = String.valueOf(ArithmeticUtil.round(data.getWeight() + 0.001f, 1));
        this.bmi = String.valueOf(data.getFloatValue(3));
        this.bodyfat = String.valueOf(data.getFloatValue(4));
        this.subfat = String.valueOf(data.getFloatValue(5));
        this.visfat = String.valueOf(data.getFloatValue(6));
        this.water = String.valueOf(data.getFloatValue(7));
        this.bmr = String.valueOf(data.getFloatValue(12));
        this.muscle = String.valueOf(data.getFloatValue(9));
        this.bone = String.valueOf(data.getFloatValue(10));
        this.protein = String.valueOf(data.getFloatValue(11));
        this.measure_time = String.valueOf(data.getCreateTime().getTime());
        this.record_on = record_on;
        this.source = 1;
    }

    public boolean isByHand() {
        return this.source == 0;
    }
}
