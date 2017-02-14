package rx.internal.producers;

import rx.Producer;

public final class ProducerArbiter implements Producer {
    static final Producer NULL_PRODUCER = new Producer() {
        public void request(long n) {
        }
    };
    Producer currentProducer;
    boolean emitting;
    long missedProduced;
    Producer missedProducer;
    long missedRequested;
    long requested;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void request(long r12) {
        /*
        r11 = this;
        r8 = 0;
        r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x000f;
    L_0x0006:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "n >= 0 required";
        r6.<init>(r7);
        throw r6;
    L_0x000f:
        r6 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
        if (r6 != 0) goto L_0x0014;
    L_0x0013:
        return;
    L_0x0014:
        monitor-enter(r11);
        r6 = r11.emitting;	 Catch:{ all -> 0x0020 }
        if (r6 == 0) goto L_0x0023;
    L_0x0019:
        r6 = r11.missedRequested;	 Catch:{ all -> 0x0020 }
        r6 = r6 + r12;
        r11.missedRequested = r6;	 Catch:{ all -> 0x0020 }
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        goto L_0x0013;
    L_0x0020:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        throw r6;
    L_0x0023:
        r6 = 1;
        r11.emitting = r6;	 Catch:{ all -> 0x0020 }
        monitor-exit(r11);	 Catch:{ all -> 0x0020 }
        r1 = 0;
        r2 = r11.requested;	 Catch:{ all -> 0x004d }
        r4 = r2 + r12;
        r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r6 >= 0) goto L_0x0035;
    L_0x0030:
        r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
    L_0x0035:
        r11.requested = r4;	 Catch:{ all -> 0x004d }
        r0 = r11.currentProducer;	 Catch:{ all -> 0x004d }
        if (r0 == 0) goto L_0x003e;
    L_0x003b:
        r0.request(r12);	 Catch:{ all -> 0x004d }
    L_0x003e:
        r11.emitLoop();	 Catch:{ all -> 0x004d }
        r1 = 1;
        if (r1 != 0) goto L_0x0013;
    L_0x0044:
        monitor-enter(r11);
        r6 = 0;
        r11.emitting = r6;	 Catch:{ all -> 0x004a }
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        goto L_0x0013;
    L_0x004a:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x004a }
        throw r6;
    L_0x004d:
        r6 = move-exception;
        if (r1 != 0) goto L_0x0055;
    L_0x0050:
        monitor-enter(r11);
        r7 = 0;
        r11.emitting = r7;	 Catch:{ all -> 0x0056 }
        monitor-exit(r11);	 Catch:{ all -> 0x0056 }
    L_0x0055:
        throw r6;
    L_0x0056:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0056 }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerArbiter.request(long):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void produced(long r12) {
        /*
        r11 = this;
        r8 = 0;
        r3 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
        if (r3 > 0) goto L_0x000f;
    L_0x0006:
        r3 = new java.lang.IllegalArgumentException;
        r6 = "n > 0 required";
        r3.<init>(r6);
        throw r3;
    L_0x000f:
        monitor-enter(r11);
        r3 = r11.emitting;	 Catch:{ all -> 0x0043 }
        if (r3 == 0) goto L_0x001b;
    L_0x0014:
        r6 = r11.missedProduced;	 Catch:{ all -> 0x0043 }
        r6 = r6 + r12;
        r11.missedProduced = r6;	 Catch:{ all -> 0x0043 }
        monitor-exit(r11);	 Catch:{ all -> 0x0043 }
    L_0x001a:
        return;
    L_0x001b:
        r3 = 1;
        r11.emitting = r3;	 Catch:{ all -> 0x0043 }
        monitor-exit(r11);	 Catch:{ all -> 0x0043 }
        r2 = 0;
        r0 = r11.requested;	 Catch:{ all -> 0x003a }
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r3 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r3 == 0) goto L_0x0048;
    L_0x002b:
        r4 = r0 - r12;
        r3 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r3 >= 0) goto L_0x0046;
    L_0x0031:
        r3 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x003a }
        r6 = "more items arrived than were requested";
        r3.<init>(r6);	 Catch:{ all -> 0x003a }
        throw r3;	 Catch:{ all -> 0x003a }
    L_0x003a:
        r3 = move-exception;
        if (r2 != 0) goto L_0x0042;
    L_0x003d:
        monitor-enter(r11);
        r6 = 0;
        r11.emitting = r6;	 Catch:{ all -> 0x0057 }
        monitor-exit(r11);	 Catch:{ all -> 0x0057 }
    L_0x0042:
        throw r3;
    L_0x0043:
        r3 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0043 }
        throw r3;
    L_0x0046:
        r11.requested = r4;	 Catch:{ all -> 0x003a }
    L_0x0048:
        r11.emitLoop();	 Catch:{ all -> 0x003a }
        r2 = 1;
        if (r2 != 0) goto L_0x001a;
    L_0x004e:
        monitor-enter(r11);
        r3 = 0;
        r11.emitting = r3;	 Catch:{ all -> 0x0054 }
        monitor-exit(r11);	 Catch:{ all -> 0x0054 }
        goto L_0x001a;
    L_0x0054:
        r3 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0054 }
        throw r3;
    L_0x0057:
        r3 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0057 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerArbiter.produced(long):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setProducer(rx.Producer r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r1 = r4.emitting;	 Catch:{ all -> 0x002a }
        if (r1 == 0) goto L_0x000d;
    L_0x0005:
        if (r5 != 0) goto L_0x0009;
    L_0x0007:
        r5 = NULL_PRODUCER;	 Catch:{ all -> 0x002a }
    L_0x0009:
        r4.missedProducer = r5;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
    L_0x000c:
        return;
    L_0x000d:
        r1 = 1;
        r4.emitting = r1;	 Catch:{ all -> 0x002a }
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        r0 = 0;
        r4.currentProducer = r5;	 Catch:{ all -> 0x002d }
        if (r5 == 0) goto L_0x001b;
    L_0x0016:
        r2 = r4.requested;	 Catch:{ all -> 0x002d }
        r5.request(r2);	 Catch:{ all -> 0x002d }
    L_0x001b:
        r4.emitLoop();	 Catch:{ all -> 0x002d }
        r0 = 1;
        if (r0 != 0) goto L_0x000c;
    L_0x0021:
        monitor-enter(r4);
        r1 = 0;
        r4.emitting = r1;	 Catch:{ all -> 0x0027 }
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        goto L_0x000c;
    L_0x0027:
        r1 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0027 }
        throw r1;
    L_0x002a:
        r1 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002a }
        throw r1;
    L_0x002d:
        r1 = move-exception;
        if (r0 != 0) goto L_0x0035;
    L_0x0030:
        monitor-enter(r4);
        r2 = 0;
        r4.emitting = r2;	 Catch:{ all -> 0x0036 }
        monitor-exit(r4);	 Catch:{ all -> 0x0036 }
    L_0x0035:
        throw r1;
    L_0x0036:
        r1 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0036 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerArbiter.setProducer(rx.Producer):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void emitLoop() {
        /*
        r14 = this;
    L_0x0000:
        monitor-enter(r14);
        r4 = r14.missedRequested;	 Catch:{ all -> 0x0053 }
        r0 = r14.missedProduced;	 Catch:{ all -> 0x0053 }
        r2 = r14.missedProducer;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x001a;
    L_0x000d:
        r12 = 0;
        r12 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x001a;
    L_0x0013:
        if (r2 != 0) goto L_0x001a;
    L_0x0015:
        r12 = 0;
        r14.emitting = r12;	 Catch:{ all -> 0x0053 }
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        return;
    L_0x001a:
        r12 = 0;
        r14.missedRequested = r12;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r14.missedProduced = r12;	 Catch:{ all -> 0x0053 }
        r12 = 0;
        r14.missedProducer = r12;	 Catch:{ all -> 0x0053 }
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        r6 = r14.requested;
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r12 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1));
        if (r12 == 0) goto L_0x0049;
    L_0x0031:
        r8 = r6 + r4;
        r12 = 0;
        r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r12 < 0) goto L_0x0042;
    L_0x0039:
        r12 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r12 != 0) goto L_0x0056;
    L_0x0042:
        r6 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r14.requested = r6;
    L_0x0049:
        if (r2 == 0) goto L_0x0071;
    L_0x004b:
        r12 = NULL_PRODUCER;
        if (r2 != r12) goto L_0x006b;
    L_0x004f:
        r12 = 0;
        r14.currentProducer = r12;
        goto L_0x0000;
    L_0x0053:
        r12 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0053 }
        throw r12;
    L_0x0056:
        r10 = r8 - r0;
        r12 = 0;
        r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r12 >= 0) goto L_0x0067;
    L_0x005e:
        r12 = new java.lang.IllegalStateException;
        r13 = "more produced than requested";
        r12.<init>(r13);
        throw r12;
    L_0x0067:
        r6 = r10;
        r14.requested = r10;
        goto L_0x0049;
    L_0x006b:
        r14.currentProducer = r2;
        r2.request(r6);
        goto L_0x0000;
    L_0x0071:
        r3 = r14.currentProducer;
        if (r3 == 0) goto L_0x0000;
    L_0x0075:
        r12 = 0;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 == 0) goto L_0x0000;
    L_0x007b:
        r3.request(r4);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.producers.ProducerArbiter.emitLoop():void");
    }
}
