package com.xiaomi.auth;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;

import com.xiaomi.auth.service.talker.ServiceChecker;
import com.xiaomi.auth.service.talker.ServiceTalkerFactory;
import com.xiaomi.auth.service.talker.ServiceTalkerFactory.talkerType;

public class XiaomiAuthUtil {
    public static boolean isServiceSupport(Context context) {
        return ServiceChecker.isServiceSupport(context);
    }

    public static Bundle getXiaomiUserInfo(Context context, Account account, Bundle options) {
        return ServiceTalkerFactory.getTalker(talkerType.UserInfo).talk(context, account, options);
    }

    public static Bundle getAccessToken(Context context, Account account, Bundle options) {
        return ServiceTalkerFactory.getTalker(talkerType.GetToken).talk(context, account, options);
    }

    public static boolean invalidateAccessToken(Context context, Account account, Bundle options) {
        Bundle bundle = ServiceTalkerFactory.getTalker(talkerType.InvalidateToken).talk(context,
                account, options);
        return bundle != null && bundle.getInt(AuthConstants.EXTRA_ERROR_CODE) == 0;
    }
}
