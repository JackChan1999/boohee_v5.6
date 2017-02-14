package com.meiqia.meiqiasdk.callback;

import com.meiqia.meiqiasdk.model.BaseMessage;

public interface OnMessageSendCallback {
    void onFailure(BaseMessage baseMessage, int i, String str);

    void onSuccess(BaseMessage baseMessage, int i);
}
