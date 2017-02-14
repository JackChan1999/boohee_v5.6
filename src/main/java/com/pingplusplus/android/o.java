package com.pingplusplus.android;

import android.os.AsyncTask;

class o extends AsyncTask {
    final /* synthetic */ l a;

    private o(l lVar) {
        this.a = lVar;
    }

    protected k a(n... nVarArr) {
        n nVar = nVarArr[0];
        return l.a(nVar.a, nVar.b, nVar.c, nVar.d);
    }

    protected void a(k kVar) {
        if (kVar != null) {
            PingppLog.a("status code: " + kVar.a);
            PingppLog.a(kVar.b);
            return;
        }
        PingppLog.a("response is null");
    }

    protected /* synthetic */ Object doInBackground(Object[] objArr) {
        return a((n[]) objArr);
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((k) obj);
    }
}
