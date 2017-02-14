package butterknife;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.Property;
import android.view.View;
import butterknife.internal.ButterKnifeProcessor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ButterKnife {
    static final Map<Class<?>, Injector<Object>> INJECTORS = new LinkedHashMap();
    static final Injector<Object> NOP_INJECTOR = new Injector<Object>() {
        public void inject(Finder finder, Object target, Object source) {
        }

        public void reset(Object target) {
        }
    };
    private static final String TAG = "ButterKnife";
    private static boolean debug = false;

    public interface Injector<T> {
        void inject(Finder finder, T t, Object obj);

        void reset(T t);
    }

    public interface Action<T extends View> {
        void apply(T t, int i);
    }

    public enum Finder {
        VIEW {
            protected View findView(Object source, int id) {
                return ((View) source).findViewById(id);
            }

            protected Context getContext(Object source) {
                return ((View) source).getContext();
            }
        },
        ACTIVITY {
            protected View findView(Object source, int id) {
                return ((Activity) source).findViewById(id);
            }

            protected Context getContext(Object source) {
                return (Activity) source;
            }
        },
        DIALOG {
            protected View findView(Object source, int id) {
                return ((Dialog) source).findViewById(id);
            }

            protected Context getContext(Object source) {
                return ((Dialog) source).getContext();
            }
        };

        protected abstract View findView(Object obj, int i);

        protected abstract Context getContext(Object obj);

        public static <T> T[] arrayOf(T... views) {
            return views;
        }

        public static <T> List<T> listOf(T... views) {
            return new ImmutableList(views);
        }

        public <T> T findRequiredView(Object source, int id, String who) {
            T view = findOptionalView(source, id, who);
            if (view != null) {
                return view;
            }
            throw new IllegalStateException("Required view '" + getContext(source).getResources().getResourceEntryName(id) + "' with ID " + id + " for " + who + " was not found. If this view is optional add '@Optional' annotation.");
        }

        public <T> T findOptionalView(Object source, int id, String who) {
            return castView(findView(source, id), id, who);
        }

        public <T> T castView(View view, int id, String who) {
            return view;
        }

        public <T> T castParam(Object value, String from, int fromPosition, String to, int toPosition) {
            return value;
        }
    }

    public interface Setter<T extends View, V> {
        void set(T t, V v, int i);
    }

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public static void setDebug(boolean debug) {
        debug = debug;
    }

    public static void inject(Activity target) {
        inject(target, target, Finder.ACTIVITY);
    }

    public static void inject(View target) {
        inject(target, target, Finder.VIEW);
    }

    public static void inject(Dialog target) {
        inject(target, target, Finder.DIALOG);
    }

    public static void inject(Object target, Activity source) {
        inject(target, source, Finder.ACTIVITY);
    }

    public static void inject(Object target, View source) {
        inject(target, source, Finder.VIEW);
    }

    public static void inject(Object target, Dialog source) {
        inject(target, source, Finder.DIALOG);
    }

    public static void reset(Object target) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view injector for " + targetClass.getName());
            }
            Injector<Object> injector = findInjectorForClass(targetClass);
            if (injector != null) {
                injector.reset(target);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException("Unable to reset views for " + target, e2);
        }
    }

    static void inject(Object target, Object source, Finder finder) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view injector for " + targetClass.getName());
            }
            Injector<Object> injector = findInjectorForClass(targetClass);
            if (injector != null) {
                injector.inject(finder, target, source);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException("Unable to inject views for " + target, e2);
        }
    }

    private static Injector<Object> findInjectorForClass(Class<?> cls) throws IllegalAccessException, InstantiationException {
        Injector<Object> injector = (Injector) INJECTORS.get(cls);
        if (injector != null) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in injector map.");
            }
            return injector;
        }
        String clsName = cls.getName();
        if (clsName.startsWith(ButterKnifeProcessor.ANDROID_PREFIX) || clsName.startsWith(ButterKnifeProcessor.JAVA_PREFIX)) {
            if (debug) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return NOP_INJECTOR;
        }
        try {
            injector = (Injector) Class.forName(clsName + ButterKnifeProcessor.SUFFIX).newInstance();
            if (debug) {
                Log.d(TAG, "HIT: Class loaded injection class.");
            }
        } catch (ClassNotFoundException e) {
            if (debug) {
                Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            }
            injector = findInjectorForClass(cls.getSuperclass());
        }
        INJECTORS.put(cls, injector);
        return injector;
    }

    public static <T extends View> void apply(List<T> list, Action<? super T> action) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            action.apply((View) list.get(i), i);
        }
    }

    public static <T extends View, V> void apply(List<T> list, Setter<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set((View) list.get(i), value, i);
        }
    }

    @TargetApi(14)
    public static <T extends View, V> void apply(List<T> list, Property<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set(list.get(i), value);
        }
    }

    public static <T extends View> T findById(View view, int id) {
        return view.findViewById(id);
    }

    public static <T extends View> T findById(Activity activity, int id) {
        return activity.findViewById(id);
    }

    public static <T extends View> T findById(Dialog dialog, int id) {
        return dialog.findViewById(id);
    }
}
