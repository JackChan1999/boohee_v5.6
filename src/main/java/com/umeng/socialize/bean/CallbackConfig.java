package com.umeng.socialize.bean;

import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.Log;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class CallbackConfig {
    public static final  int                               CALLBACK_LISTENER_FLAG_SDK    = 1;
    public static final  int                               CALLBACK_LISTENER_FLAG_SINGEL = 16;
    public static final  Map<ICallbackListener, Integer[]> STRONG_LISTENERS              =
            Collections.synchronizedMap(new HashMap());
    public static final  Map<ICallbackListener, Integer[]> WEAK_LISTENERS                =
            Collections.synchronizedMap(new WeakHashMap());
    private static final int                               b                             = 30;
    private static final int                               c                             = 15;
    private static final int                               d                             = 240;
    private static final int                               e                             = 256;
    private static final int                               f                             = 512;
    private static final int                               g                             = 768;
    private static final int                               h                             = 1024;
    private static final int                               i                             = 3840;
    protected            boolean                           a                             = true;

    public interface ICallbackListener {
    }

    public synchronized boolean registerWeakRefListener(ICallbackListener iCallbackListener, int
            i) throws SocializeException {
        return a(iCallbackListener, true, i);
    }

    public synchronized boolean registerWeakRefListener(ICallbackListener iCallbackListener)
            throws SocializeException {
        return a(iCallbackListener, true, 0);
    }

    public synchronized boolean registerListener(ICallbackListener iCallbackListener, int i)
            throws SocializeException {
        return a(iCallbackListener, false, i);
    }

    public synchronized boolean registerListener(ICallbackListener iCallbackListener) throws
            SocializeException {
        return a(iCallbackListener, false, 0);
    }

    private synchronized boolean a(ICallbackListener iCallbackListener, boolean z, int i) throws
            SocializeException {
        boolean z2 = false;
        synchronized (this) {
            Log.e("--->", " regist listener :  " + iCallbackListener);
            if (iCallbackListener != null) {
                Map map = z ? WEAK_LISTENERS : STRONG_LISTENERS;
                int a = a(iCallbackListener) | i;
                if (a(iCallbackListener, i)) {
                    throw new SocializeException("该类型监听器已经超过最大使用量,请注销不使用的监听器再试。");
                }
                a(a);
                Object obj = new Integer[2];
                obj[0] = Integer.valueOf(a);
                if (contains(iCallbackListener) >= 0) {
                    Log.i("com.umeng.socialize", "The callback-listener has exist in the pool," +
                            "resgister will update permission flag.");
                    map.put(iCallbackListener, obj);
                } else {
                    z2 = true;
                }
            }
        }
        return z2;
    }

    private boolean a(ICallbackListener iCallbackListener, int i) throws SocializeException {
        Class cls;
        if (iCallbackListener instanceof SnsPostListener) {
            cls = SnsPostListener.class;
        } else if (iCallbackListener instanceof SocializeClientListener) {
            cls = SocializeClientListener.class;
        } else if (iCallbackListener instanceof MulStatusListener) {
            cls = MulStatusListener.class;
        } else if (iCallbackListener instanceof UMAuthListener) {
            cls = UMAuthListener.class;
        } else {
            throw new SocializeException("unknow listener`s class.");
        }
        if (29 < getListener(cls).length) {
            return true;
        }
        return false;
    }

    public synchronized int contains(ICallbackListener iCallbackListener) {
        int i;
        i = 0;
        if (WEAK_LISTENERS.containsKey(iCallbackListener)) {
            i = 1;
        }
        if (STRONG_LISTENERS.containsKey(iCallbackListener)) {
            i += 2;
        }
        return i;
    }

    private synchronized boolean a(int i) {
        boolean z;
        if ((i & 240) == 16) {
            Integer[] numArr;
            boolean a;
            for (ICallbackListener iCallbackListener : WEAK_LISTENERS.keySet()) {
                numArr = (Integer[]) WEAK_LISTENERS.get(iCallbackListener);
                if (numArr != null) {
                    a = a(i, numArr[0].intValue());
                    if (a) {
                        WEAK_LISTENERS.remove(iCallbackListener);
                    }
                    z = a;
                }
            }
            for (ICallbackListener iCallbackListener2 : STRONG_LISTENERS.keySet()) {
                numArr = (Integer[]) STRONG_LISTENERS.get(iCallbackListener2);
                if (numArr != null) {
                    a = a(i, numArr[0].intValue());
                    if (a) {
                        STRONG_LISTENERS.remove(iCallbackListener2);
                    }
                    z = a;
                }
            }
        }
        z = false;
        return z;
    }

    private boolean a(int i, int i2) {
        if (((i & 15) == (i2 & 15)) && (i2 & 240) == 16 && (i2 & i) == (i & i)) {
            return true;
        }
        return false;
    }

    private int a(ICallbackListener iCallbackListener) throws SocializeException {
        if (iCallbackListener instanceof SnsPostListener) {
            return 512;
        }
        if (iCallbackListener instanceof SocializeClientListener) {
            return 768;
        }
        if (iCallbackListener instanceof MulStatusListener) {
            return 256;
        }
        if (iCallbackListener instanceof UMAuthListener) {
            return 1024;
        }
        throw new SocializeException("unknow params");
    }

    public void unregisterLisreners(ICallbackListener... iCallbackListenerArr) {
        if (iCallbackListenerArr != null) {
            for (ICallbackListener unregisterListener : iCallbackListenerArr) {
                unregisterListener(unregisterListener);
            }
        }
    }

    public boolean cleanListeners() {
        try {
            WEAK_LISTENERS.clear();
            STRONG_LISTENERS.clear();
            return true;
        } catch (Exception e) {
            Log.e("com.umeng.socialize", "", e);
            return false;
        }
    }

    public boolean unregisterListener(ICallbackListener iCallbackListener) {
        Integer[] numArr = null;
        try {
            if (STRONG_LISTENERS.containsKey(iCallbackListener)) {
                numArr = (Integer[]) STRONG_LISTENERS.remove(iCallbackListener);
            }
            if (WEAK_LISTENERS.containsKey(iCallbackListener) && r0 == null) {
                numArr = (Integer[]) WEAK_LISTENERS.remove(iCallbackListener);
            }
            return numArr != null;
        } catch (Exception e) {
            Log.w("com.umeng.socialize", "", e);
            return false;
        }
    }

    public <T> T[] getListener(Class<T> cls) throws SocializeException {
        Set hashSet = new HashSet();
        if (a((Class) cls, ICallbackListener.class)) {
            try {
                for (ICallbackListener iCallbackListener : WEAK_LISTENERS.keySet()) {
                    if (cls.isInstance(iCallbackListener)) {
                        hashSet.add(iCallbackListener);
                    }
                }
                for (ICallbackListener iCallbackListener2 : STRONG_LISTENERS.keySet()) {
                    if (cls.isInstance(iCallbackListener2)) {
                        hashSet.add(iCallbackListener2);
                    }
                }
            } catch (Exception e) {
                Log.w("com.umeng.socialize", "", e);
            }
            return hashSet.toArray((Object[]) Array.newInstance(cls, hashSet.size()));
        }
        throw new SocializeException("The param is not implements ICallbackLister.");
    }

    private boolean a(Class<?> cls, Class<?> cls2) {
        Class[] interfaces = cls.getInterfaces();
        if (interfaces == null) {
            return false;
        }
        for (Class<?> cls3 : interfaces) {
            if (cls3 == cls2) {
                return true;
            }
        }
        return false;
    }
}
