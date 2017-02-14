package com.xiaomi.auth.service.talker;

import android.accounts.Account;
import android.os.Bundle;
import android.os.RemoteException;

import miui.net.IXiaomiAuthService;

class TalkerUserInfo extends ServiceTalker {
    TalkerUserInfo() {
    }

    protected Bundle talkWithServiceV5(Account account, Bundle options, IXiaomiAuthService
            accountService) throws RemoteException {
        return accountService.getMiCloudUserInfo(account, options);
    }

    protected Bundle talkWithServiceV6(Account account, Bundle options, com.xiaomi.account
            .IXiaomiAuthService accountService) throws RemoteException {
        return accountService.getMiCloudUserInfo(account, options);
    }
}
