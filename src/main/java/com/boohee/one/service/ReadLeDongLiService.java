package com.boohee.one.service;

import android.app.IntentService;
import android.content.Intent;

import com.boohee.utils.LeDongLiHelper;

public class ReadLeDongLiService extends IntentService {
    public static final String QUERY_STRING = "step";

    public ReadLeDongLiService() {
        super("");
    }

    protected void onHandleIntent(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey("step")) {
            int step = intent.getExtras().getInt("step");
            long time = intent.getExtras().getLong("time");
            Intent broadcastIntent = new Intent(LeDongLiHelper.BROADCAST_ACTION);
            broadcastIntent.putExtra("step", step);
            broadcastIntent.putExtra("time", time);
            sendBroadcast(broadcastIntent);
        }
    }
}
