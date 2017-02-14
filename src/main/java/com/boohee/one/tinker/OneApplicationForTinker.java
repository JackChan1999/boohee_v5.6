package com.boohee.one.tinker;

import com.tencent.tinker.loader.app.TinkerApplication;

public class OneApplicationForTinker extends TinkerApplication {
    public OneApplicationForTinker() {
        super(7, "com.boohee.one.MyApplication", "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
