package com.prolificinteractive.materialcalendarview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooheeCalendarDay {
    public Map<String, List<DayRange>> colorDayMap = new HashMap();
    public int                         mDrawableId = -1;
    public CalendarDay maxDrawableDay;
    public CalendarDay miniDrawableDay;

    public class DayRange {
        public CalendarDay endDay;
        public CalendarDay startDay;
    }

    public void setColorDayRange(CalendarDay startDay, CalendarDay endDay, int colorId) {
        List<DayRange> rangeList = (List) this.colorDayMap.get(String.valueOf(colorId));
        if (rangeList == null) {
            rangeList = new ArrayList();
            this.colorDayMap.put(String.valueOf(colorId), rangeList);
        }
        DayRange dayRange = new DayRange();
        dayRange.startDay = startDay;
        dayRange.endDay = endDay;
        rangeList.add(dayRange);
    }

    public void setDrawableDayRange(CalendarDay startDay, CalendarDay endDay, int drawableId) {
        this.miniDrawableDay = startDay;
        this.maxDrawableDay = endDay;
        this.mDrawableId = drawableId;
    }

    public int getColorId(CalendarDay day) {
        if (day == null || this.colorDayMap == null || this.colorDayMap.isEmpty()) {
            return -1;
        }
        for (String key : this.colorDayMap.keySet()) {
            List<DayRange> dayRangeList = (List) this.colorDayMap.get(key);
            if (!(dayRangeList == null || dayRangeList.size() == 0)) {
                for (DayRange dayRange : dayRangeList) {
                    if (day.isInRange(dayRange.startDay, dayRange.endDay)) {
                        return Integer.parseInt(key);
                    }
                }
                continue;
            }
        }
        return -1;
    }

    public int getDrawableId(CalendarDay day) {
        if (day == null || this.miniDrawableDay == null || this.maxDrawableDay == null || !day
                .isInRange(this.miniDrawableDay, this.maxDrawableDay)) {
            return -1;
        }
        return this.mDrawableId;
    }
}
