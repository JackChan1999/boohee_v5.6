package com.prolificinteractive.materialcalendarview;

public interface DayViewDecorator {
    void decorate(DayViewFacade dayViewFacade);

    boolean shouldDecorate(CalendarDay calendarDay);
}
