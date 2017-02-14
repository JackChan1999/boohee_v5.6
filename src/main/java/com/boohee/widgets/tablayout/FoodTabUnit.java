package com.boohee.widgets.tablayout;

import com.booheee.view.keyboard.Unit;

public class FoodTabUnit extends Unit implements TabModelInterface {
    public FoodTabUnit(Unit unit) {
        this.id = unit.id;
        this.food_unit_id = unit.food_unit_id;
        this.weight = unit.weight;
        this.unit_name = unit.unit_name;
        this.eat_weight = unit.eat_weight;
    }

    public FoodTabUnit(String name) {
        this.unit_name = name;
    }

    public String getTabName() {
        return this.unit_name;
    }

    public String getDes() {
        return null;
    }
}
