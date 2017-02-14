package com.zxinsight.common.http;

import android.graphics.Bitmap;

import java.util.LinkedList;

class j {
    final /* synthetic */ g         a;
    private final         Request   b;
    private               Bitmap    c;
    private               Exception d;
    private final LinkedList<l> e = new LinkedList();

    public j(g gVar, Request request, l lVar) {
        this.a = gVar;
        this.b = request;
        this.e.add(lVar);
    }

    public void a(Exception exception) {
        this.d = exception;
    }

    public Exception a() {
        return this.d;
    }

    public void a(l lVar) {
        this.e.add(lVar);
    }
}
