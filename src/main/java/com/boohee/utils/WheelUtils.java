package com.boohee.utils;

import android.content.Context;

import com.boohee.one.R;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.HighlightArrayWheelAdapter;
import kankan.wheel.widget.adapters.HighlightNumericWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class WheelUtils {
    private static long lastClickTime;

    public static String[] getNumInterger(int min, int max, String unit) {
        List<String> lists = new ArrayList();
        int size = max - min;
        for (int i = 0; i <= size; i++) {
            lists.add((i + min) + unit);
        }
        return (String[]) lists.toArray(new String[lists.size()]);
    }

    public static String[] getWeightDecimal() {
        List<String> lists = new ArrayList();
        for (int i = 0; i <= 9; i++) {
            lists.add("." + i);
        }
        return (String[]) lists.toArray(new String[lists.size()]);
    }

    public static void setCurrentItem(WheelView wheel, int value, int wheel_min, int defaultValue) {
        int index = value - wheel_min;
        if (index < 0) {
            index = defaultValue;
        }
        wheel.setCurrentItem(index, true);
    }

    public static void setWheelArrayText(WheelView wheel, Context context, String[] strings) {
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(context, strings);
        arrayWheelAdapter.setTextColor(context.getResources().getColor(R.color.jt));
        arrayWheelAdapter.setTextSize(17);
        wheel.setViewAdapter(arrayWheelAdapter);
    }

    public static void setWheelNumericText(WheelView wheel, Context context, int min, int max) {
        NumericWheelAdapter arrayWheelAdapter = new NumericWheelAdapter(context, min, max);
        arrayWheelAdapter.setTextColor(context.getResources().getColor(R.color.jt));
        arrayWheelAdapter.setTextSize(17);
        wheel.setViewAdapter(arrayWheelAdapter);
    }

    public static void setHighlightArray(WheelView wheel, Context context, String[] strings, int
            current) {
        HighlightArrayWheelAdapter arrayWheelAdapter = new HighlightArrayWheelAdapter(context,
                strings, current);
        arrayWheelAdapter.setTextColor(context.getResources().getColor(R.color.du));
        wheel.setViewAdapter(arrayWheelAdapter);
    }

    public static void setHighlightNumeric(WheelView wheel, Context context, int min, int max,
                                           int current) {
        HighlightNumericWheelAdapter arrayWheelAdapter = new HighlightNumericWheelAdapter
                (context, min, max, current);
        arrayWheelAdapter.setTextColor(context.getResources().getColor(R.color.du));
        wheel.setViewAdapter(arrayWheelAdapter);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
