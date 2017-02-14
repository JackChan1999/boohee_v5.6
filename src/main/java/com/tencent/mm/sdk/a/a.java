package com.tencent.mm.sdk.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.a.a.b;
import com.tencent.mm.sdk.b.e;
import com.tencent.mm.sdk.constants.ConstantsAPI;

public final class a {

    public static class a {
        public int flags = -1;
        public String k;
        public String l;
        public String m;
        public Bundle n;
    }

    public static boolean a(Context context, a aVar) {
        if (context == null || aVar == null) {
            com.tencent.mm.sdk.b.a.a("MicroMsg.SDK.MMessageAct", "send fail, invalid argument");
            return false;
        } else if (e.j(aVar.k)) {
            com.tencent.mm.sdk.b.a.a("MicroMsg.SDK.MMessageAct", "send fail, invalid " +
                    "targetPkgName, targetPkgName = " + aVar.k);
            return false;
        } else {
            if (e.j(aVar.l)) {
                aVar.l = aVar.k + ".wxapi.WXEntryActivity";
            }
            com.tencent.mm.sdk.b.a.d("MicroMsg.SDK.MMessageAct", "send, targetPkgName = " + aVar
                    .k + ", targetClassName = " + aVar.l);
            Intent intent = new Intent();
            intent.setClassName(aVar.k, aVar.l);
            if (aVar.n != null) {
                intent.putExtras(aVar.n);
            }
            String packageName = context.getPackageName();
            intent.putExtra(ConstantsAPI.SDK_VERSION, 570490883);
            intent.putExtra(ConstantsAPI.APP_PACKAGE, packageName);
            intent.putExtra(ConstantsAPI.CONTENT, aVar.m);
            intent.putExtra(ConstantsAPI.CHECK_SUM, b.a(aVar.m, 570490883, packageName));
            if (aVar.flags == -1) {
                intent.addFlags(268435456).addFlags(134217728);
            } else {
                intent.setFlags(aVar.flags);
            }
            try {
                context.startActivity(intent);
                com.tencent.mm.sdk.b.a.d("MicroMsg.SDK.MMessageAct", "send mm message, intent=" +
                        intent);
                return true;
            } catch (Exception e) {
                com.tencent.mm.sdk.b.a.a("MicroMsg.SDK.MMessageAct", "send fail, ex = %s", e
                        .getMessage());
                return false;
            }
        }
    }
}
