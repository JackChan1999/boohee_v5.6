package com.umeng.socialize.controller.impl;

import android.content.Context;

import com.umeng.socialize.common.UMAsyncTask;

/* compiled from: ShareServiceImpl */
class r extends UMAsyncTask<Void> {
    final /* synthetic */ InitializeController a;
    final /* synthetic */ Context              b;
    final /* synthetic */ m                    c;

    r(m mVar, InitializeController initializeController, Context context) {
        this.c = mVar;
        this.a = initializeController;
        this.b = context;
    }

    protected /* synthetic */ Object doInBackground() {
        return a();
    }

    protected Void a() {
        if (this.a.uploadStatisticsData(this.b) == 200) {
            this.c.a.cleanStatisticsData(this.b, true);
        }
        return null;
    }
}
