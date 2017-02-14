package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;

import java.util.List;
import java.util.Map;

class y implements OnGetMessageListCallback {
    final /* synthetic */ bc  a;
    final /* synthetic */ Map b;
    final /* synthetic */ cv  c;
    final /* synthetic */ b   d;

    y(b bVar, bc bcVar, Map map, cv cvVar) {
        this.d = bVar;
        this.a = bcVar;
        this.b = map;
        this.c = cvVar;
    }

    public void onFailure(int i, String str) {
        this.d.a(this.b, null, this.c);
    }

    public void onSuccess(List<MQMessage> list) {
        this.a.a((List) list);
        this.d.a(this.b, (List) list, this.c);
    }
}
