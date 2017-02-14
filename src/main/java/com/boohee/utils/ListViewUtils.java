package com.boohee.utils;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

@SuppressLint({"NewApi"})
public class ListViewUtils {
    private ListViewUtils() {
    }

    public static void smoothScrollListViewToTop(final ListView listView) {
        if (listView != null) {
            smoothScrollListView(listView, 0);
            listView.postDelayed(new Runnable() {
                public void run() {
                    listView.setSelection(0);
                }
            }, 200);
        }
    }

    public static void smoothScrollListView(ListView listView, int position) {
        if (VERSION.SDK_INT >= 11) {
            listView.smoothScrollToPositionFromTop(0, 0);
        } else if (VERSION.SDK_INT >= 8) {
            listView.smoothScrollToPosition(0);
        } else {
            listView.setSelection(position);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) +
                    totalHeight;
            listView.setLayoutParams(params);
        }
    }
}
