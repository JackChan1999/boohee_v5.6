package com.umeng.analytics.social;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.a;

public abstract class UMSocialService {
    private static void a(Context context, b bVar, String str, UMPlatformData...
            uMPlatformDataArr) {
        int i = 0;
        if (uMPlatformDataArr != null) {
            try {
                int length = uMPlatformDataArr.length;
                while (i < length) {
                    if (uMPlatformDataArr[i].isValid()) {
                        i++;
                    } else {
                        throw new a("parameter is not valid.");
                    }
                }
            } catch (Throwable e) {
                Log.e(a.e, "unable send event.", e);
                return;
            } catch (Throwable e2) {
                Log.e(a.e, "", e2);
                return;
            }
        }
        new a(f.a(context, str, uMPlatformDataArr), bVar, uMPlatformDataArr).execute(new Void[0]);
    }

    public static void share(Context context, String str, UMPlatformData... uMPlatformDataArr) {
        a(context, null, str, uMPlatformDataArr);
    }

    public static void share(Context context, UMPlatformData... uMPlatformDataArr) {
        a(context, null, null, uMPlatformDataArr);
    }
}
