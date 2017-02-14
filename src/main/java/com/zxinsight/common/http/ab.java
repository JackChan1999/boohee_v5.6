package com.zxinsight.common.http;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

class ab extends b<z> {
    final /* synthetic */ InputStream  a;
    final /* synthetic */ OutputStream b;
    final /* synthetic */ z            c;

    ab(z zVar, Closeable closeable, boolean z, InputStream inputStream, OutputStream outputStream) {
        this.c = zVar;
        this.a = inputStream;
        this.b = outputStream;
        super(closeable, z);
    }

    public /* synthetic */ Object b() {
        return c();
    }

    public z c() {
        byte[] bArr = new byte[this.c.j];
        while (true) {
            int read = this.a.read(bArr);
            if (read == -1) {
                return this.c;
            }
            this.b.write(bArr, 0, read);
        }
    }
}
