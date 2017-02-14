package com.xiaomi.push.protobuf;

import com.google.protobuf.micro.b;
import com.google.protobuf.micro.d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class a {

    public static final class a extends d {
        private boolean a;
        private int b = 0;
        private boolean c;
        private boolean d = false;
        private boolean e;
        private int f = 0;
        private boolean g;
        private boolean      h = false;
        private List<String> i = Collections.emptyList();
        private int          j = -1;

        public static a b(byte[] bArr) {
            return (a) new a().a(bArr);
        }

        public static a c(com.google.protobuf.micro.a aVar) {
            return new a().b(aVar);
        }

        public int a() {
            int i = 0;
            int d = d() ? b.d(1, c()) + 0 : 0;
            if (f()) {
                d += b.b(2, e());
            }
            if (h()) {
                d += b.c(3, g());
            }
            int b = j() ? d + b.b(4, i()) : d;
            for (String b2 : k()) {
                i += b.b(b2);
            }
            d = (b + i) + (k().size() * 1);
            this.j = d;
            return d;
        }

        public /* synthetic */ d a(com.google.protobuf.micro.a aVar) {
            return b(aVar);
        }

        public a a(int i) {
            this.a = true;
            this.b = i;
            return this;
        }

        public a a(String str) {
            if (str == null) {
                throw new NullPointerException();
            }
            if (this.i.isEmpty()) {
                this.i = new ArrayList();
            }
            this.i.add(str);
            return this;
        }

        public a a(boolean z) {
            this.c = true;
            this.d = z;
            return this;
        }

        public void a(b bVar) {
            if (d()) {
                bVar.b(1, c());
            }
            if (f()) {
                bVar.a(2, e());
            }
            if (h()) {
                bVar.a(3, g());
            }
            if (j()) {
                bVar.a(4, i());
            }
            for (String a : k()) {
                bVar.a(5, a);
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
                        a(aVar.f());
                        continue;
                    case 16:
                        a(aVar.d());
                        continue;
                    case 24:
                        b(aVar.c());
                        continue;
                    case 32:
                        b(aVar.d());
                        continue;
                    case 42:
                        a(aVar.e());
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

        public a b(boolean z) {
            this.g = true;
            this.h = z;
            return this;
        }

        public int c() {
            return this.b;
        }

        public boolean d() {
            return this.a;
        }

        public boolean e() {
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

        public boolean i() {
            return this.h;
        }

        public boolean j() {
            return this.g;
        }

        public List<String> k() {
            return this.i;
        }

        public int l() {
            return this.i.size();
        }
    }
}
