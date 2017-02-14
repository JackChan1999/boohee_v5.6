package com.meiqia.core.callback;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQMessage;

import java.util.List;

public interface OnClientOnlineCallback extends OnFailureCallBack {
    void onSuccess(MQAgent mQAgent, String str, List<MQMessage> list);
}
