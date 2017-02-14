package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper extends ContextWrapper {
    private static final ArrayList<WeakReference<TintContextWrapper>> sCache = new ArrayList();
    private Resources mResources;
    private final Theme mTheme = getResources().newTheme();

    public static Context wrap(@NonNull Context context) {
        if (!shouldWrap(context)) {
            return context;
        }
        TintContextWrapper wrapper;
        int count = sCache.size();
        for (int i = 0; i < count; i++) {
            WeakReference<TintContextWrapper> ref = (WeakReference) sCache.get(i);
            wrapper = ref != null ? (TintContextWrapper) ref.get() : null;
            if (wrapper != null && wrapper.getBaseContext() == context) {
                return wrapper;
            }
        }
        wrapper = new TintContextWrapper(context);
        sCache.add(new WeakReference(wrapper));
        return wrapper;
    }

    private static boolean shouldWrap(@NonNull Context context) {
        if ((context instanceof TintContextWrapper) || (context.getResources() instanceof TintResources)) {
            return false;
        }
        return true;
    }

    private TintContextWrapper(@NonNull Context base) {
        super(base);
        this.mTheme.setTo(base.getTheme());
    }

    public Theme getTheme() {
        return this.mTheme;
    }

    public void setTheme(int resid) {
        this.mTheme.applyStyle(resid, true);
    }

    public Resources getResources() {
        if (this.mResources == null) {
            this.mResources = new TintResources(this, super.getResources());
        }
        return this.mResources;
    }
}
