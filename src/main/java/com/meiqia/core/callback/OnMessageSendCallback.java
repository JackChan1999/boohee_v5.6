package com.meiqia.core.callback;

import com.meiqia.core.bean.MQMessage;

public interface OnMessageSendCallback {
    void onFailure(MQMessage mQMessage, int i, String str);

    void onSuccess(MQMessage mQMessage, int i);
}
