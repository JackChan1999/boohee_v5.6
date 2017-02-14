package com.boohee.account;

import android.content.Context;

import com.boohee.modeldao.ModelDaoBase;

public class UserRecordUtil {
    public static boolean isNeedImport(Context ctx) {
        if (new ModelDaoBase(ctx).getUnregisterRecordCount() > 0) {
            return true;
        }
        return false;
    }

    public static void importData(Context ctx, String user_key) {
        new ModelDaoBase(ctx).importData(user_key);
    }
}
