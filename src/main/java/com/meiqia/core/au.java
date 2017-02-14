package com.meiqia.core;

import com.meiqia.core.b.i;

class au implements da {
    final /* synthetic */ as a;

    au(as asVar) {
        this.a = asVar;
    }

    public void a(String str, long j) {
        long a = i.a(str);
        long id = this.a.a.getId();
        this.a.a.setCreated_on(a);
        this.a.a.setId(j);
        this.a.a.setStatus("arrived");
        b.b(this.a.c).a(this.a.a, id);
        b.a(this.a.c, new av(this));
    }

    public void onFailure(int i, String str) {
        this.a.a.setStatus("failed");
        b.b(this.a.c).a(this.a.a);
        if (this.a.b != null) {
            this.a.b.onFailure(this.a.a, i, str);
        }
    }
}
