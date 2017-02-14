package com.alipay.android.phone.mrpc.core;

import com.loopj.android.http.AsyncHttpClient;
import com.qiniu.android.http.Client;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public final class j extends a {
    private g g;

    public j(g gVar, Method method, int i, String str, byte[] bArr, boolean z) {
        super(method, i, str, bArr, Client.FormMime, z);
        this.g = gVar;
    }

    public final Object a() {
        Throwable e;
        t oVar = new o(this.g.a());
        oVar.a(this.b);
        oVar.a(this.e);
        oVar.a(this.f);
        oVar.a("id", String.valueOf(this.d));
        oVar.a("operationType", this.c);
        oVar.a(AsyncHttpClient.ENCODING_GZIP, String.valueOf(this.g.d()));
        oVar.a(new BasicHeader("uuid", UUID.randomUUID().toString()));
        List<Header> b = this.g.c().b();
        if (!(b == null || b.isEmpty())) {
            for (Header a : b) {
                oVar.a(a);
            }
        }
        new StringBuilder("threadid = ").append(Thread.currentThread().getId()).append("; ").append(oVar.toString());
        try {
            u uVar = (u) this.g.b().a(oVar).get();
            if (uVar != null) {
                return uVar.b();
            }
            throw new RpcException(Integer.valueOf(9), "response is null");
        } catch (Throwable e2) {
            throw new RpcException(Integer.valueOf(13), "", e2);
        } catch (Throwable e22) {
            Throwable th = e22;
            e22 = th.getCause();
            if (e22 == null || !(e22 instanceof HttpException)) {
                throw new RpcException(Integer.valueOf(9), "", th);
            }
            HttpException httpException = (HttpException) e22;
            int code = httpException.getCode();
            switch (code) {
                case 1:
                    code = 2;
                    break;
                case 2:
                    code = 3;
                    break;
                case 3:
                    code = 4;
                    break;
                case 4:
                    code = 5;
                    break;
                case 5:
                    code = 6;
                    break;
                case 6:
                    code = 7;
                    break;
                case 7:
                    code = 8;
                    break;
                case 8:
                    code = 15;
                    break;
                case 9:
                    code = 16;
                    break;
            }
            throw new RpcException(Integer.valueOf(code), httpException.getMsg());
        } catch (Throwable e222) {
            throw new RpcException(Integer.valueOf(13), "", e222);
        }
    }
}
