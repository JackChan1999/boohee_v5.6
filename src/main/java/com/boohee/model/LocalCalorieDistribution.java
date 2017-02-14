package com.boohee.model;

public class LocalCalorieDistribution {
    public float  breakfastCalory;
    public float  dinnerCalory;
    public float  lunchCalory;
    public String record_on;
    public float  snackCalory;
    public float  sportCalory;

    public LocalCalorieDistribution(String record_on, float breakfastCalory, float lunchCalory,
                                    float dinnerCalory, float snackCalory, float sportCalory) {
        this.record_on = record_on;
        this.breakfastCalory = breakfastCalory;
        this.lunchCalory = lunchCalory;
        this.dinnerCalory = dinnerCalory;
        this.snackCalory = snackCalory;
        this.sportCalory = sportCalory;
    }
}
