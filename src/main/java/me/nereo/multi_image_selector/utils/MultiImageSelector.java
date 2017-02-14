package me.nereo.multi_image_selector.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MultiImageSelector {
    private static MultiImageLoader mImageLoader;

    public static void init(MultiImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new RuntimeException("imageLoader is null !");
        }
        mImageLoader = imageLoader;
    }

    public static MultiImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static void show(Activity activity, boolean needShowTakePhoto, int maxSelectNum, int requestCode) {
        show(activity, needShowTakePhoto, maxSelectNum, null, requestCode);
    }

    public static void show(Activity activity, boolean needShowTakePhoto, int maxSelectNum, ArrayList<String> selectPath, int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(createIntent(activity, needShowTakePhoto, maxSelectNum, selectPath), requestCode);
        }
    }

    public static void show(Fragment fragment, boolean needShowTakePhoto, int maxSelectNum, int requestCode) {
        show(fragment, needShowTakePhoto, maxSelectNum, null, requestCode);
    }

    public static void show(Fragment fragment, boolean needShowTakePhoto, int maxSelectNum, ArrayList<String> selectPath, int requestCode) {
        if (fragment != null) {
            fragment.startActivityForResult(createIntent(fragment.getActivity(), needShowTakePhoto, maxSelectNum, selectPath), requestCode);
        }
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

    private static Intent createIntent(Context context, boolean needShowTakePhoto, int maxSelectNum) {
        return createIntent(context, needShowTakePhoto, maxSelectNum, null);
    }

    private static Intent createIntent(Context context, boolean needShowTakePhoto, int maxSelectNum, ArrayList<String> selectPath) {
        int selectedMode = 1;
        checkInit();
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
        intent.putExtra("show_camera", needShowTakePhoto);
        intent.putExtra("max_select_count", maxSelectNum);
        if (maxSelectNum == 1) {
            selectedMode = 0;
        }
        intent.putExtra("select_count_mode", selectedMode);
        if (selectPath != null && selectPath.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selectPath);
        }
        return intent;
    }

    private static void checkInit() {
        if (mImageLoader == null) {
            throw new RuntimeException("请先初始化！");
        }
    }
}
