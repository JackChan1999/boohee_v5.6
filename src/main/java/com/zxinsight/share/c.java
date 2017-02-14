package com.zxinsight.share;

import android.os.AsyncTask;

import com.zxinsight.share.domain.b;

class c extends AsyncTask<Void, Void, b> {
    private b a;

    protected /* synthetic */ Object doInBackground(Object[] objArr) {
        return a((Void[]) objArr);
    }

    protected /* synthetic */ void onPostExecute(Object obj) {
        a((b) obj);
    }

    public c(b bVar) {
        this.a = bVar;
    }

    protected b a(Void... voidArr) {
        return a.c(this.a);
    }

    protected void a(b bVar) {
        super.onPostExecute(bVar);
    }
}
