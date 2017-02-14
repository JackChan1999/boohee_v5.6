package com.meiqia.meiqiasdk.callback;

import com.meiqia.meiqiasdk.model.FileMessage;

public interface FileStateCallback {
    void onFileMessageDownloadFailure(FileMessage fileMessage, int i, String str);

    void onFileMessageExpired(FileMessage fileMessage);
}
