package com.boohee.model;

import java.util.Arrays;
import java.util.List;

public class Light {
    public String element;
    public int    light;

    public Light(String element, int light) {
        this.element = element;
        this.light = light;
    }

    public static List<Light> getmainLightsList() {
        return Arrays.asList(new Light[]{new Light("fat", 0), new Light("saturated_fat", 0), new
                Light("sugar", 0), new Light("fiber_dietary", 0), new Light("natrium", 0)});
    }

    public static List<Light> getsubLightsList() {
        return Arrays.asList(new Light[]{new Light("probiotic", 0), new Light("vitamin_a", 0),
                new Light("thiamine", 0), new Light("lactoflavin", 0), new Light("vitamin_b6", 0)
                , new Light("vitamin_b12", 0), new Light("niacin", 0), new Light("folacin", 0),
                new Light("vitamin_c", 0), new Light("vitamin_d", 0), new Light("vitamin_e", 0),
                new Light("vitamin_k", 0), new Light("calcium", 0), new Light("phosphor", 0), new
                Light("kalium", 0), new Light("magnesium", 0), new Light("iron", 0), new Light
                ("zinc", 0), new Light("iodine", 0), new Light("selenium", 0), new Light
                ("copper", 0), new Light("fluorine", 0), new Light("manganese", 0)});
    }
}
