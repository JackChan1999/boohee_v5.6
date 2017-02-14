package com.meiqia.core;

class bn implements Runnable {
    final /* synthetic */ MQManager a;

    bn(MQManager mQManager) {
        this.a = mQManager;
    }

    public void run() {
        this.a.g = true;
    }
}
