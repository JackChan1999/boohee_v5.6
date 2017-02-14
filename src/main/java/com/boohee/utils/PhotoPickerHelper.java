package com.boohee.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import me.nereo.multi_image_selector.utils.MultiImageSelector;

public class PhotoPickerHelper {
    public static void show(Activity activity, boolean needShowTakePhoto, int maxSelectNum, int
            requestCode) {
        show(activity, needShowTakePhoto, maxSelectNum, null, requestCode);
    }

    public static void show(Activity activity, boolean needShowTakePhoto, int maxSelectNum,
                            ArrayList<String> selectPath, int requestCode) {
        MultiImageSelector.show(activity, needShowTakePhoto, maxSelectNum, (ArrayList)
                selectPath, requestCode);
    }

    public static void show(Fragment fragment, boolean needShowTakePhoto, int maxSelectNum, int
            requestCode) {
        show(fragment, needShowTakePhoto, maxSelectNum, null, requestCode);
    }

    public static void show(Fragment fragment, boolean needShowTakePhoto, int maxSelectNum,
                            ArrayList<String> selectPath, int requestCode) {
        MultiImageSelector.show(fragment, needShowTakePhoto, maxSelectNum, (ArrayList)
                selectPath, requestCode);
    }

    public static void show(Activity activity, int requestCode) {
        show(activity, true, 1, requestCode);
    }

    public static void show(Activity activity, ArrayList<String> selectPath, int requestCode) {
        show(activity, true, 1, (ArrayList) selectPath, requestCode);
    }

    public static void show(Fragment fragment, int requestCode) {
        show(fragment, true, 1, requestCode);
    }

    public static void show(Fragment fragment, ArrayList<String> selectPath, int requestCode) {
        show(fragment, true, 1, (ArrayList) selectPath, requestCode);
    }
}
