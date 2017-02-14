package com.boohee.utils;

import android.graphics.Color;

import com.boohee.model.TodayDiets;
import com.boohee.model.TodayNutrients;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartAnalysisHelper {
    public static final int[] MEALS_COLORS     = new int[]{Color.parseColor("#06AAF6"), Color
            .parseColor("#1BD1A4"), Color.parseColor("#FE7502")};
    public static final int[] NUTRIENTS_COLORS = new int[]{Color.parseColor("#4CD963"), Color
            .parseColor("#FE7502"), Color.parseColor("#FECB02")};

    public static void initMealsPie(PieChartView pieChartView, TodayDiets todayDiets) {
        List<SliceValue> values = new ArrayList();
        if (todayDiets.breakfast != null && todayDiets.breakfast.percentage > 0.0f) {
            values.add(new SliceValue(todayDiets.breakfast.percentage, MEALS_COLORS[0]).setLabel(
                    ((int) (todayDiets.breakfast.percentage * 100.0f)) + "%早餐"));
        }
        if (todayDiets.lunch != null && todayDiets.lunch.percentage > 0.0f) {
            values.add(new SliceValue(todayDiets.lunch.percentage, MEALS_COLORS[1]).setLabel((
                    (int) (todayDiets.lunch.percentage * 100.0f)) + "%午餐"));
        }
        if (todayDiets.dinner != null && todayDiets.dinner.percentage > 0.0f) {
            values.add(new SliceValue(todayDiets.dinner.percentage, MEALS_COLORS[2]).setLabel((
                    (int) (todayDiets.dinner.percentage * 100.0f)) + "%晚餐"));
        }
        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(false);
        data.setSlicesSpacing(3);
        data.setValueLabelBackgroundColor(0);
        data.setValueLabelBackgroundEnabled(true);
        pieChartView.setValueSelectionEnabled(true);
        pieChartView.setPieChartData(data);
        pieChartView.startDataAnimation();
    }

    public static void initNutrientsPie(PieChartView pieChartView, TodayNutrients todayNutrients) {
        List<SliceValue> values = new ArrayList();
        if (todayNutrients.carbohydrate != null && todayNutrients.carbohydrate.percentage > 0.0f) {
            values.add(new SliceValue(todayNutrients.carbohydrate.percentage,
                    NUTRIENTS_COLORS[0]).setLabel((todayNutrients.carbohydrate.percentage < 0.0f
                    ? "0" : Integer.valueOf((int) (todayNutrients.carbohydrate.percentage * 100
            .0f))) + "% 碳水"));
        }
        if (todayNutrients.fat != null && todayNutrients.fat.percentage > 0.0f) {
            values.add(new SliceValue(todayNutrients.fat.percentage, NUTRIENTS_COLORS[1])
                    .setLabel((todayNutrients.fat.percentage < 0.0f ? "0" : Integer.valueOf((int)
                            (todayNutrients.fat.percentage * 100.0f))) + "% 脂肪"));
        }
        if (todayNutrients.protein != null && todayNutrients.protein.percentage > 0.0f) {
            values.add(new SliceValue(todayNutrients.protein.percentage, NUTRIENTS_COLORS[2])
                    .setLabel(todayNutrients.protein.percentage < 0.0f ? "0" : ((int)
                            (todayNutrients.protein.percentage * 100.0f)) + "% 蛋白质"));
        }
        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(false);
        data.setHasLabelsOutside(false);
        data.setHasCenterCircle(false);
        data.setSlicesSpacing(3);
        pieChartView.setValueSelectionEnabled(true);
        data.setValueLabelBackgroundColor(0);
        pieChartView.setPieChartData(data);
        pieChartView.startDataAnimation();
    }
}
