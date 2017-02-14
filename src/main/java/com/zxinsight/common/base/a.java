package com.zxinsight.common.base;

import android.content.Intent;

public interface a {
    void onActivityResult(int i, int i2, Intent intent);

    void onBackPressed();

    void onCreate();

    void onDestroy();

    void onNewIntent(Intent intent);

    void onPause();

    void onRestart();

    void onResume();
}
