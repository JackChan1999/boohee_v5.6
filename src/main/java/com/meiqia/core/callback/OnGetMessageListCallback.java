package com.meiqia.core.callback;

import com.meiqia.core.bean.MQMessage;

import java.util.List;

public interface OnGetMessageListCallback extends OnFailureCallBack {
    void onSuccess(List<MQMessage> list);
}
