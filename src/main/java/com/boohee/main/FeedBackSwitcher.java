package com.boohee.main;

import java.util.Calendar;

public class FeedBackSwitcher {
    public static boolean isFeedbackTime() {
        int hour = Calendar.getInstance().get(11);
        if (hour < 9 || hour >= 23) {
            return false;
        }
        return true;
    }
}
