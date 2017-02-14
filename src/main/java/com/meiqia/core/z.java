package com.meiqia.core;

import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.SimpleCallback;

import java.util.List;
import java.util.Map;

class z implements dc {
    final /* synthetic */ int[]          a;
    final /* synthetic */ List           b;
    final /* synthetic */ List           c;
    final /* synthetic */ Map            d;
    final /* synthetic */ SimpleCallback e;
    final /* synthetic */ b              f;

    z(b bVar, int[] iArr, List list, List list2, Map map, SimpleCallback simpleCallback) {
        this.f = bVar;
        this.a = iArr;
        this.b = list;
        this.c = list2;
        this.d = map;
        this.e = simpleCallback;
    }

    public void a(String str, String str2) {
        this.a[0] = this.a[0] + 1;
        MQMessage mQMessage = new MQMessage("photo");
        mQMessage.setContent(str);
        mQMessage.setMedia_url(str2);
        this.b.add(mQMessage);
        if (this.a[0] + this.a[1] != this.c.size()) {
            return;
        }
        if (this.a[0] == this.c.size()) {
            this.f.a(this.b, this.d, this.e);
        } else if (this.e != null) {
            this.e.onFailure(20002, "upload photo failed");
        }
    }

    public void onFailure(int i, String str) {
        this.a[1] = this.a[1] + 1;
        if (this.a[0] + this.a[1] == this.c.size() && this.e != null) {
            this.e.onFailure(i, str);
        }
    }
}
