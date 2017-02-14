package com.zxinsight.mlink;

import android.content.Context;
import android.content.pm.ActivityInfo;

import com.zxinsight.MLink;
import com.zxinsight.MagicWindowSDK;
import com.zxinsight.common.util.l;
import com.zxinsight.mlink.annotation.MLinkDefaultRouter;
import com.zxinsight.mlink.annotation.MLinkRouter;

import java.util.ArrayList;
import java.util.List;

public class a {
    public static void a(Context context) {
        List<String> b = b(context);
        MLink mLink = MagicWindowSDK.getMLink();
        if (mLink != null && l.b(b)) {
            for (String cls : b) {
                try {
                    a(mLink, Class.forName(cls));
                } catch (ClassNotFoundException e) {
                }
            }
        }
    }

    private static List<String> b(Context context) {
        List<String> arrayList = new ArrayList();
        try {
            Object obj = context.getPackageManager().getPackageInfo(context.getPackageName(), 1)
                    .activities;
            if (l.b(obj)) {
                for (ActivityInfo activityInfo : obj) {
                    arrayList.add(activityInfo.name);
                }
            }
        } catch (Exception e) {
        }
        return arrayList;
    }

    private static void a(MLink mLink, Class cls) {
        if (b(cls)) {
            mLink.registerDefault(new b(cls));
            return;
        }
        Object a = a(cls);
        if (l.b(a)) {
            for (String register : a) {
                mLink.register(register, new c(cls));
            }
        }
    }

    private static String[] a(Class cls) {
        MLinkRouter mLinkRouter = (MLinkRouter) cls.getAnnotation(MLinkRouter.class);
        if (mLinkRouter == null || mLinkRouter.keys().length <= 0) {
            return null;
        }
        return mLinkRouter.keys();
    }

    private static boolean b(Class cls) {
        return ((MLinkDefaultRouter) cls.getAnnotation(MLinkDefaultRouter.class)) != null;
    }
}
