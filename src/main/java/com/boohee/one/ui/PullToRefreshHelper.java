package com.boohee.one.ui;

import android.app.Activity;
import android.os.Handler;
import android.widget.ListView;

import com.boohee.one.R;
import com.boohee.utils.ListViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PullToRefreshHelper {
    public static void loadFirst(Activity activity) {
        final PullToRefreshListView listView = (PullToRefreshListView) activity.findViewById(R.id
                .listview);
        if (listView != null) {
            ListViewUtils.smoothScrollListViewToTop((ListView) listView.getRefreshableView());
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    listView.setRefreshing(true);
                }
            }, 500);
        }
    }
}
