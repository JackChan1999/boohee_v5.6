package com.aspsine.multithreaddownload.architecture;

public interface DownloadStatusDelivery {
    void post(DownloadStatus downloadStatus);
}
