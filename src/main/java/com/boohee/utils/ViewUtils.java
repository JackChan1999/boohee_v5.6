package com.boohee.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boohee.model.Alarm;
import com.boohee.modeldao.AlarmDao;
import com.boohee.more.RemindService;
import com.boohee.myview.TimePickerWheelView;
import com.boohee.myview.risenumber.RiseNumberTextView;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.utility.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewUtils {
    static final String TAG = ViewUtils.class.getSimpleName();
    private static long lastClickTime;

    public static void setSelection(EditText... edits) {
        for (EditText edit : edits) {
            Selection.setSelection(edit.getText(), edit.length());
        }
    }

    public static void setViewScaleHeight(Context context, View view, int width, int height) {
        if (context != null) {
            view.getLayoutParams().height = ResolutionUtils.getHeight(context, width, height);
        }
    }

    public static void setViewScaleHeightBaseSelf(Context context, View view, int width, int
            height) {
        if (context != null) {
            int originWidth = getViewWidth(context, view);
            view.getLayoutParams().height = ResolutionUtils.getHeight(context, originWidth,
                    width, height);
        }
    }

    public static void setBatchViewScaleHeight(Context context, int width, int height, View...
            views) {
        for (View view : views) {
            setViewScaleHeight(context, view, width, height);
        }
    }

    public static void setViewScaleWidth(View view, int width, int height, int baseHieght) {
        view.getLayoutParams().width = ResolutionUtils.getWidthBasedHeight(width, height,
                baseHieght);
    }

    public static int getViewWidth(Context context, View view) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        view.measure(displaymetrics.widthPixels, displaymetrics.heightPixels);
        return view.getMeasuredWidth();
    }

    public static int getViewHeight(Context context, View view) {
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        view.measure(displaymetrics.widthPixels, displaymetrics.heightPixels);
        return view.getMeasuredHeight();
    }

    public static void initImageView(Context context, Uri mUri, ImageView preImage) {
        if (mUri != null && preImage != null && context != null) {
            ImageLoader.getInstance().displayImage(Uri.decode(mUri.toString()), preImage,
                    ImageLoaderOptions.color(R.color.h6));
        }
    }

    public static void showTimeDialog(Context context, Alarm alarm, AlarmDao alarmDao, TextView
            text_view) {
        final TimePickerWheelView timePickerView = new TimePickerWheelView(context, alarm.hour,
                alarm.minute);
        final Alarm alarm2 = alarm;
        final TextView textView = text_view;
        final AlarmDao alarmDao2 = alarmDao;
        final Context context2 = context;
        new Builder(context).setView(timePickerView).setPositiveButton(R.string.y8, new
                OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alarm2.hour = timePickerView.getHour();
                alarm2.minute = timePickerView.getMinute();
                textView.setText(alarm2.formatTime());
                alarmDao2.update(alarm2);
                if (alarm2.is_open()) {
                    RemindService.start(alarm2, context2);
                }
            }
        }).show();
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

    public static boolean isFastDoubleClick(long timeMillis) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < timeMillis) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void startRiseNumber(RiseNumberTextView riseNumberTextView, int number) {
        riseNumberTextView.withNumber(number).setDuration(1500).start();
    }

    public static int getActionBarHeight(Activity activity) {
        try {
            return activity.findViewById(R.id.toolbar).getHeight();
        } catch (Exception e) {
            return dip2px(MyApplication.getContext(), 56.0f);
        }
    }
}
