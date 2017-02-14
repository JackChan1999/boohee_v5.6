package com.xiaomi.auth.service.talker;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.xiaomi.account.IXiaomiAuthService.Stub;
import com.xiaomi.auth.AuthConstants;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import miui.net.IXiaomiAuthService;

public abstract class ServiceTalker {
    private static final String ACTION_FOR_AUTH_SERVICE       = "android.intent.action" +
            ".XIAOMI_ACCOUNT_AUTHORIZE";
    private static final String PACKAGE_NAME_FOR_AUTH_SERVICE = "com.xiaomi.account";
    static final         String TAG                           = "XiaomiAuthUtil";

    static class AsyncFuture<V> extends FutureTask<V> {
        public AsyncFuture() {
            super(new Callable<V>() {
                public V call() throws Exception {
                    throw new IllegalStateException("this should never be called");
                }
            });
        }

        public void setResult(V v) {
            set(v);
        }
    }

    protected abstract Bundle talkWithServiceV5(Account account, Bundle bundle,
                                                IXiaomiAuthService iXiaomiAuthService) throws
            RemoteException;

    protected abstract Bundle talkWithServiceV6(Account account, Bundle bundle, com.xiaomi
            .account.IXiaomiAuthService iXiaomiAuthService) throws RemoteException;

    public Bundle talk(Context context, final Account account, final Bundle options) {
        ensureNotOnMainThread(context);
        final AsyncFuture<Bundle> future = new AsyncFuture();
        return talkAndWaitForResult(context, future, new ServiceConnection() {
            public void onServiceDisconnected(ComponentName name) {
            }

            public void onServiceConnected(ComponentName name, IBinder service) {
                final Account account = account;
                final Bundle bundle = options;
                final AsyncFuture asyncFuture = future;
                final IBinder iBinder = service;
                new Thread() {
                    public void run() {
                        Bundle bundle = null;
                        try {
                            bundle = ServiceTalker.this.tryTalkAsV6OrV5(account, bundle, iBinder);
                        } catch (RemoteException e) {
                            Log.e(ServiceTalker.TAG, "failed to talked with Auth Service", e);
                        }
                        asyncFuture.setResult(bundle);
                    }
                }.start();
            }
        });
    }

    protected Bundle tryTalkAsV6OrV5(Account account, Bundle options, IBinder service) throws
            RemoteException {
        try {
            return talkWithServiceV6(account, options, Stub.asInterface(service));
        } catch (SecurityException e) {
            try {
                return talkWithServiceV5(account, options, IXiaomiAuthService.Stub.asInterface
                        (service));
            } catch (SecurityException e2) {
                Log.e(TAG, "failed to talked with Auth Service", e2);
                return null;
            }
        }
    }

    private static void ensureNotOnMainThread(Context context) {
        Looper looper = Looper.myLooper();
        if (looper != null && looper == context.getMainLooper()) {
            throw new IllegalStateException("calling this from your main thread can lead to " +
                    "deadlock");
        }
    }

    private Bundle talkAndWaitForResult(Context context, AsyncFuture<Bundle> future,
                                        ServiceConnection conn) {
        Bundle resBundle = null;
        if (context.bindService(getAuthServiceIntent(), conn, 1)) {
            try {
                resBundle = (Bundle) future.get();
                context.unbindService(conn);
                return resBundle;
            } catch (InterruptedException e) {
                context.unbindService(conn);
                Thread.currentThread().interrupt();
                e.printStackTrace();
                return resBundle;
            } catch (Exception e2) {
                e2.printStackTrace();
                return resBundle;
            }
        }
        resBundle = new Bundle();
        resBundle.putInt(AuthConstants.EXTRA_ERROR_CODE, AuthConstants.ERROR_CONNECT_FAILED);
        resBundle.putString(AuthConstants.EXTRA_ERROR_DESCRIPTION, "cannot connect to auth " +
                "service");
        return resBundle;
    }

    protected String getSeriveAction() {
        return ACTION_FOR_AUTH_SERVICE;
    }

    public static Intent getAuthServiceIntent() {
        Intent intent = new Intent(ACTION_FOR_AUTH_SERVICE);
        if (VERSION.SDK_INT >= 21) {
            intent.setPackage(PACKAGE_NAME_FOR_AUTH_SERVICE);
        }
        return intent;
    }
}
