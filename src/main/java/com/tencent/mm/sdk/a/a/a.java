package com.tencent.mm.sdk.a.a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.b.e;
import com.tencent.mm.sdk.constants.ConstantsAPI;

public final class a {

    public static class a {
        public String m;
        public Bundle n;
        public String o;
        public String p;
    }

    public static boolean a(Context context, a aVar) {
        if (context == null || aVar == null) {
            com.tencent.mm.sdk.b.a.a("MicroMsg.SDK.MMessage", "send fail, invalid argument");
            return false;
        } else if (e.j(aVar.p)) {
            com.tencent.mm.sdk.b.a.a("MicroMsg.SDK.MMessage", "send fail, action is null");
            return false;
        } else {
            String str = null;
            if (!e.j(aVar.o)) {
                str = aVar.o + ".permission.MM_MESSAGE";
            }
            Intent intent = new Intent(aVar.p);
            if (aVar.n != null) {
                intent.putExtras(aVar.n);
            }
            String packageName = context.getPackageName();
            intent.putExtra(ConstantsAPI.SDK_VERSION, 570490883);
            intent.putExtra(ConstantsAPI.APP_PACKAGE, packageName);
            intent.putExtra(ConstantsAPI.CONTENT, aVar.m);
            intent.putExtra(ConstantsAPI.CHECK_SUM, b.a(aVar.m, 570490883, packageName));
            context.sendBroadcast(intent, str);
            com.tencent.mm.sdk.b.a.d("MicroMsg.SDK.MMessage", "send mm message, intent=" + intent
                    + ", perm=" + str);
            return true;
        }
    }
}
