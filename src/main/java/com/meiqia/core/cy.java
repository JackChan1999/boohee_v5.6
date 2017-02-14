package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnFailureCallBack;

public interface cy extends OnFailureCallBack {
    void a(MQMessage mQMessage);
}
