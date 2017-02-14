package com.boohee.one.event;

import com.boohee.model.CustomSport;

public class CustomSportEvent {
    private CustomSport customSport;

    public CustomSport getCustomSport() {
        return this.customSport;
    }

    public CustomSportEvent setCustomSport(CustomSport customSport) {
        this.customSport = customSport;
        return this;
    }
}
