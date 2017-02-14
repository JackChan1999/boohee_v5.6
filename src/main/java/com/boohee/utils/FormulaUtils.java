package com.boohee.utils;

public class FormulaUtils {
    public static float computeSport(float sportCalorie) {
        return (float) (((double) sportCalorie) * 0.9d);
    }

    public static int calorieType(float targetCalorie, float dietCalorie, float sportCalorie) {
        if (((double) dietCalorie) < 0.9d * ((double) (computeSport(sportCalorie) +
                targetCalorie))) {
            return -1;
        }
        if (((double) dietCalorie) <= 1.1d * ((double) (computeSport(sportCalorie) +
                targetCalorie))) {
            return 0;
        }
        return 1;
    }

    public static float needCalorie(float targetCalorie, float dietCalorie, float sportCalorie) {
        return (computeSport(sportCalorie) + targetCalorie) - dietCalorie;
    }

    public static int[] calorieLimit(float targetCalorie, int type) {
        int[] limit = new int[2];
        switch (type) {
            case 1:
            case 3:
                limit[0] = (int) ((((double) targetCalorie) * 0.3d) * 0.9d);
                limit[1] = (int) ((((double) targetCalorie) * 0.3d) * 1.1d);
                break;
            case 2:
                limit[0] = (int) ((((double) targetCalorie) * 0.4d) * 0.9d);
                limit[1] = (int) ((((double) targetCalorie) * 0.4d) * 1.1d);
                break;
            case 6:
            case 7:
            case 8:
                limit[1] = 0;
                break;
        }
        return limit;
    }

    public static float calcCalorie(float mets, float weight) {
        return (float) ((1.34d * ((double) (mets - 1.0f))) * ((double) weight));
    }
}
