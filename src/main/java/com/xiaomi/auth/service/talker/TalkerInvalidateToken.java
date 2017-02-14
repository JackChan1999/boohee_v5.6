package com.xiaomi.auth.service.talker;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.xiaomi.auth.AuthConstants;

import miui.net.IXiaomiAuthService;

class TalkerInvalidateToken extends ServiceTalker {
    TalkerInvalidateToken() {
    }

    public Bundle talk(final Context context, final Account account, final Bundle options) {
        boolean binded = context.bindService(ServiceTalker.getAuthServiceIntent(), new
                ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    TalkerInvalidateToken.this.tryTalkAsV6OrV5(account, options, service);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                context.unbindService(this);
            }
        }, 1);
        Bundle bundle = new Bundle();
        bundle.putBoolean(AuthConstants.EXTRA_ERROR_CODE, binded);
        return bundle;
    }

    protected Bundle talkWithServiceV5(Account account, Bundle options, IXiaomiAuthService
            accountService) throws RemoteException {
        accountService.invalidateAccessToken(account, options);
        Bundle bundle = new Bundle();
        bundle.putInt(AuthConstants.EXTRA_ERROR_CODE, 0);
        return bundle;
    }

    protected Bundle talkWithServiceV6(Account account, Bundle options, com.xiaomi.account
            .IXiaomiAuthService accountService) throws RemoteException {
        accountService.invalidateAccessToken(account, options);
        new Bundle().putInt(AuthConstants.EXTRA_ERROR_CODE, 0);
        return null;
    }
}
