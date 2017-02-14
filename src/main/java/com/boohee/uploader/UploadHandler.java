package com.boohee.uploader;

import java.util.List;

public abstract class UploadHandler {
    private boolean isCancled;

    public abstract void onError(String str);

    public abstract void onSuccess(List<QiniuModel> list);

    public void cancel() {
        this.isCancled = true;
    }

    public boolean isCancled() {
        return this.isCancled;
    }

    public void onProgress(String filePath, double percent) {
    }

    public void onFinish() {
    }
}
