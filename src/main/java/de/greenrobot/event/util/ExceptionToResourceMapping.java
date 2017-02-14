package de.greenrobot.event.util;

import android.util.Log;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExceptionToResourceMapping {
    public final Map<Class<? extends Throwable>, Integer> throwableToMsgIdMap = new HashMap();

    public Integer mapThrowable(Throwable throwable) {
        Throwable throwableToCheck = throwable;
        int depthToGo = 20;
        do {
            Integer resId = mapThrowableFlat(throwableToCheck);
            if (resId != null) {
                return resId;
            }
            throwableToCheck = throwableToCheck.getCause();
            depthToGo--;
            if (depthToGo <= 0 || throwableToCheck == throwable) {
                Log.d(EventBus.TAG, "No specific message ressource ID found for " + throwable);
            }
        } while (throwableToCheck != null);
        Log.d(EventBus.TAG, "No specific message ressource ID found for " + throwable);
        return null;
    }

    protected Integer mapThrowableFlat(Throwable throwable) {
        Class<? extends Throwable> throwableClass = throwable.getClass();
        Integer resId = (Integer) this.throwableToMsgIdMap.get(throwableClass);
        if (resId == null) {
            Class<? extends Throwable> closestClass = null;
            for (Entry<Class<? extends Throwable>, Integer> mapping : this.throwableToMsgIdMap.entrySet()) {
                Class<? extends Throwable> candidate = (Class) mapping.getKey();
                if (candidate.isAssignableFrom(throwableClass) && (closestClass == null || closestClass.isAssignableFrom(candidate))) {
                    closestClass = candidate;
                    resId = (Integer) mapping.getValue();
                }
            }
        }
        return resId;
    }

    public ExceptionToResourceMapping addMapping(Class<? extends Throwable> clazz, int msgId) {
        this.throwableToMsgIdMap.put(clazz, Integer.valueOf(msgId));
        return this;
    }
}
