package com.meiqia.core.a.a.a;

import java.io.IOException;
import java.nio.ByteBuffer;

class c implements Runnable {
    final /* synthetic */ a a;

    private c(a aVar) {
        this.a = aVar;
    }

    public void run() {
        Thread.currentThread().setName("WebsocketWriteThread");
        while (!Thread.interrupted()) {
            try {
                ByteBuffer byteBuffer = (ByteBuffer) a.a(this.a).f.take();
                a.b(this.a).write(byteBuffer.array(), 0, byteBuffer.limit());
                a.b(this.a).flush();
            } catch (IOException e) {
                a.a(this.a).c();
                return;
            } catch (InterruptedException e2) {
                return;
            }
        }
    }
}
