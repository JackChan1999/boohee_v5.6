package com.boohee.one.event;

import com.boohee.model.RecordFood;
import com.boohee.model.RecordPhoto;

import java.util.List;

public class RefreshCalorieEvent {
    public List<RecordFood>  recordFoods;
    public List<RecordPhoto> recordPhotos;

    public RefreshCalorieEvent(List<RecordFood> recordFoods, List<RecordPhoto> recordPhotos) {
        this.recordFoods = recordFoods;
        this.recordPhotos = recordPhotos;
    }
}
