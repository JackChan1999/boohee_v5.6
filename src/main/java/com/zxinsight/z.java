package com.zxinsight;

class z implements Runnable {
    final /* synthetic */ TrackAgent a;

    z(TrackAgent trackAgent) {
        this.a = trackAgent;
    }

    public void run() {
        if (TrackAgent.access$000(this.a)) {
            TrackAgent.access$100(this.a);
        }
    }
}
