package com.meiqia.core.callback;

public interface OnProgressCallback extends OnFailureCallBack {
    void onProgress(int i);

    void onSuccess();
}
