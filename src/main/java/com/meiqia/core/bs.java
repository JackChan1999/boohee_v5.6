package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;

class bs implements OnGetMessageListCallback {
    final /* synthetic */ MeiQiaService a;

    bs(MeiQiaService meiQiaService) {
        this.a = meiQiaService;
    }

    public void onFailure(int i, String str) {
    }

    public void onSuccess(List<MQMessage> list) {
        for (MQMessage a : list) {
            this.a.g.a(a);
        }
    }
}
