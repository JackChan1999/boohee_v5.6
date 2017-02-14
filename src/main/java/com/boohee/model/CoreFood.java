package com.boohee.model;

import java.io.Serializable;

public class CoreFood extends ModelBase implements Serializable {
    private static final long serialVersionUID = 1;
    public float calcium;
    public float calory;
    public float carbohydrate;
    public float carotene;
    public float cholesterol;
    public float copper;
    public float eat_weight;
    public float fat;
    public float fiber_dietary;
    public int   food_id;
    public float iron;
    public float kalium;
    public float lactoflavin;
    public float magnesium;
    public float manganese;
    public float natrium;
    public float niacin;
    public float phosphor;
    public float protein;
    public float selenium;
    public float thiamine;
    public float vitamin_a;
    public float vitamin_c;
    public float vitamin_e;
    public float weight;
    public float zinc;

    public CoreFood(int id, int food_id, float weight, float eat_weight, float protein, float
            fat, float carbohydrate, float fiber_dietary) {
        this.id = id;
        this.food_id = food_id;
        this.weight = weight;
        this.eat_weight = eat_weight;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.fiber_dietary = fiber_dietary;
    }

    public CoreFood(int id, int food_id, float weight, float eat_weight, float protein, float
            fat, float carbohydrate, float fiber_dietary, float vitamin_a, float vitamin_c, float
            vitamin_e, float carotene, float thiamine, float lactoflavin, float niacin, float
            cholesterol, float magnesium, float calcium, float iron, float zinc, float copper,
                    float manganese, float kalium, float phosphor, float natrium, float selenium) {
        this(id, food_id, weight, eat_weight, protein, fat, carbohydrate, fiber_dietary);
        this.vitamin_a = vitamin_a;
        this.vitamin_c = vitamin_c;
        this.vitamin_e = vitamin_e;
        this.carotene = carotene;
        this.thiamine = thiamine;
        this.lactoflavin = lactoflavin;
        this.niacin = niacin;
        this.cholesterol = cholesterol;
        this.magnesium = magnesium;
        this.calcium = calcium;
        this.iron = iron;
        this.zinc = zinc;
        this.copper = copper;
        this.manganese = manganese;
        this.kalium = kalium;
        this.phosphor = phosphor;
        this.natrium = natrium;
        this.selenium = selenium;
    }

    public int eatCalory() {
        return Math.round((this.eat_weight / this.weight) * this.calory);
    }
}
