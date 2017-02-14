package com.zxinsight;

import com.zxinsight.common.http.u;
import com.zxinsight.common.util.l;

class e implements u<String> {
    final /* synthetic */ MLink a;

    e(MLink mLink) {
        this.a = mLink;
    }

    public void a(String str) {
        if (l.b(str)) {
            this.a.saveMLinks(str);
        }
    }

    public void a(Exception exception) {
    }
}
