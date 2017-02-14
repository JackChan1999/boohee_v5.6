package com.meiqia.meiqiasdk.callback;

import java.io.File;

public interface OnDownloadFileCallback extends OnFailureCallBack {
    void onProgress(int i);

    void onSuccess(File file);
}
