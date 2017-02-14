package com.android.volley;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

public class NetworkDispatcher extends Thread {
    private final Cache mCache;
    private final ResponseDelivery mDelivery;
    private final Network mNetwork;
    private final BlockingQueue<Request<?>> mQueue;
    private volatile boolean mQuit = false;

    public NetworkDispatcher(BlockingQueue<Request<?>> queue, Network network, Cache cache, ResponseDelivery delivery) {
        this.mQueue = queue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = delivery;
    }

    public void quit() {
        this.mQuit = true;
        interrupt();
    }

    @TargetApi(14)
    private void addTrafficStatsTag(Request<?> request) {
        if (VERSION.SDK_INT >= 14) {
            TrafficStats.setThreadStatsTag(request.getTrafficStatsTag());
        }
    }

    public void run() {
        VolleyError volleyError;
        Process.setThreadPriority(10);
        while (true) {
            long startTimeMs = SystemClock.elapsedRealtime();
            try {
                Request<?> request = (Request) this.mQueue.take();
                try {
                    request.addMarker("network-queue-take");
                    if (request.isCanceled()) {
                        request.finish("network-discard-cancelled");
                    } else {
                        addTrafficStatsTag(request);
                        NetworkResponse networkResponse = this.mNetwork.performRequest(request);
                        request.addMarker("network-http-complete");
                        if (networkResponse.notModified && request.hasHadResponseDelivered()) {
                            request.finish("not-modified");
                        } else {
                            Response<?> response = request.parseNetworkResponse(networkResponse);
                            request.addMarker("network-parse-complete");
                            if (request.shouldCache() && response.cacheEntry != null) {
                                this.mCache.put(request.getCacheKey(), response.cacheEntry);
                                request.addMarker("network-cache-written");
                            }
                            request.markDelivered();
                            this.mDelivery.postResponse(request, response);
                        }
                    }
                } catch (VolleyError volleyError2) {
                    volleyError2.setNetworkTimeMs(SystemClock.elapsedRealtime() - startTimeMs);
                    parseAndDeliverNetworkError(request, volleyError2);
                } catch (Throwable e) {
                    VolleyLog.e(e, "Unhandled exception %s", e.toString());
                    volleyError2 = new VolleyError(e);
                    volleyError2.setNetworkTimeMs(SystemClock.elapsedRealtime() - startTimeMs);
                    this.mDelivery.postError(request, volleyError2);
                }
            } catch (InterruptedException e2) {
                if (this.mQuit) {
                    return;
                }
            }
        }
    }

    private void parseAndDeliverNetworkError(Request<?> request, VolleyError error) {
        this.mDelivery.postError(request, request.parseNetworkError(error));
    }
}
