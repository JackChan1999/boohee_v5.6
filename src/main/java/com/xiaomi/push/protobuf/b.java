package com.xiaomi.push.protobuf;

import com.google.protobuf.micro.d;

public final class b {

    public static final class a extends d {
        private boolean a;
        private boolean b = false;
        private boolean c;
        private int d = 0;
        private boolean e;
        private int f = 0;
        private boolean g;
        private int h = 0;
        private int i = -1;

        public static a b(byte[] bArr) {
            return (a) new a().a(bArr);
        }

        public int a() {
            int i = 0;
            if (d()) {
                i = 0 + com.google.protobuf.micro.b.b(1, c());
            }
            if (f()) {
                i += com.google.protobuf.micro.b.c(3, e());
            }
            if (h()) {
                i += com.google.protobuf.micro.b.c(4, g());
            }
            if (j()) {
                i += com.google.protobuf.micro.b.c(5, i());
            }
            this.i = i;
            return i;
        }

        public /* synthetic */ d a(com.google.protobuf.micro.a aVar) {
            return b(aVar);
        }

        public a a(int i) {
            this.c = true;
            this.d = i;
            return this;
        }

        public a a(boolean z) {
            this.a = true;
            this.b = z;
            return this;
        }

        public void a(com.google.protobuf.micro.b bVar) {
            if (d()) {
                bVar.a(1, c());
            }
            if (f()) {
                bVar.a(3, e());
            }
            if (h()) {
                bVar.a(4, g());
            }
            if (j()) {
                bVar.a(5, i());
            }
        }

        public a b(int i) {
            this.e = true;
            this.f = i;
            return this;
        }

        public a b(com.google.protobuf.micro.a aVar) {
            while (true) {
                int a = aVar.a();
                switch (a) {
                    case 0:
                        break;
                    case 8:
                        a(aVar.d());
                        continue;
                    case 24:
                        a(aVar.c());
                        continue;
                    case 32:
                        b(aVar.c());
                        continue;
                    case 40:
                        c(aVar.c());
                        continue;
                    default:
                        if (!a(aVar, a)) {
                            break;
                        }
                        continue;
                }
                return this;
            }
        }

        public a c(int i) {
            this.g = true;
            this.h = i;
            return this;
        }

        public boolean c() {
            return this.b;
        }

        public boolean d() {
            return this.a;
        }

        public int e() {
            return this.d;
        }

        public boolean f() {
            return this.c;
        }

        public int g() {
            return this.f;
        }

        public boolean h() {
            return this.e;
        }

        public int i() {
            return this.h;
        }

        public boolean j() {
            return this.g;
        }
    }
}
