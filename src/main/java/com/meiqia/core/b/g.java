package com.meiqia.core.b;

import com.meiqia.core.bean.MQMessage;

import java.util.Comparator;

public class g implements Comparator<MQMessage> {
    public int a(MQMessage mQMessage, MQMessage mQMessage2) {
        return mQMessage.getCreated_on() < mQMessage2.getCreated_on() ? -1 : 1;
    }

    public /* synthetic */ int compare(Object obj, Object obj2) {
        return a((MQMessage) obj, (MQMessage) obj2);
    }
}
