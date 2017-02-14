package com.meiqia.core;

import android.content.Intent;

class an implements Runnable {
    final /* synthetic */ cv a;
    final /* synthetic */ b  b;

    an(b bVar, cv cvVar) {
        this.b = bVar;
        this.a = cvVar;
    }

    public void run() {
        if (this.a != null) {
            MQManager.getInstance(this.b.e).getMQMessageFromDatabase(Long.MAX_VALUE, 10, new ao
                    (this));
        }
        Intent intent = new Intent(this.b.e, MeiQiaService.class);
        intent.setAction("ACTION_OPEN_SOCKET");
        this.b.e.startService(intent);
    }
}
