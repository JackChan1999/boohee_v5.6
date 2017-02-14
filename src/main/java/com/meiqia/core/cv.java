package com.meiqia.core;

import com.meiqia.core.bean.MQAgent;
import com.meiqia.core.bean.MQConversation;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnFailureCallBack;

import java.util.List;

public interface cv extends OnFailureCallBack {
    void a(MQAgent mQAgent, MQConversation mQConversation, List<MQMessage> list);
}
