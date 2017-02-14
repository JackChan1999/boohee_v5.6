package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import de.greenrobot.event.EventBus;

public class ErrorDialogManager {
    public static final String KEY_EVENT_TYPE_ON_CLOSE = "de.greenrobot.eventbus.errordialog.event_type_on_close";
    public static final String KEY_FINISH_AFTER_DIALOG = "de.greenrobot.eventbus.errordialog.finish_after_dialog";
    public static final String KEY_ICON_ID = "de.greenrobot.eventbus.errordialog.icon_id";
    public static final String KEY_MESSAGE = "de.greenrobot.eventbus.errordialog.message";
    public static final String KEY_TITLE = "de.greenrobot.eventbus.errordialog.title";
    protected static final String TAG_ERROR_DIALOG = "de.greenrobot.eventbus.error_dialog";
    protected static final String TAG_ERROR_DIALOG_MANAGER = "de.greenrobot.eventbus.error_dialog_manager";
    public static ErrorDialogFragmentFactory<?> factory;

    @TargetApi(11)
    public static class HoneycombManagerFragment extends Fragment {
        protected Bundle argumentsForErrorDialog;
        private EventBus eventBus;
        private Object executionScope;
        protected boolean finishAfterDialog;

        public void onResume() {
            super.onResume();
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this);
        }

        public void onPause() {
            this.eventBus.unregister(this);
            super.onPause();
        }

        public void onEventMainThread(ThrowableFailureEvent event) {
            if (ErrorDialogManager.isInExecutionScope(this.executionScope, event)) {
                ErrorDialogManager.checkLogException(event);
                FragmentManager fm = getFragmentManager();
                fm.executePendingTransactions();
                DialogFragment existingFragment = (DialogFragment) fm.findFragmentByTag(ErrorDialogManager.TAG_ERROR_DIALOG);
                if (existingFragment != null) {
                    existingFragment.dismiss();
                }
                DialogFragment errorFragment = (DialogFragment) ErrorDialogManager.factory.prepareErrorFragment(event, this.finishAfterDialog, this.argumentsForErrorDialog);
                if (errorFragment != null) {
                    errorFragment.show(fm, ErrorDialogManager.TAG_ERROR_DIALOG);
                }
            }
        }

        public static void attachTo(Activity activity, Object executionScope, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
            FragmentManager fm = activity.getFragmentManager();
            HoneycombManagerFragment fragment = (HoneycombManagerFragment) fm.findFragmentByTag(ErrorDialogManager.TAG_ERROR_DIALOG_MANAGER);
            if (fragment == null) {
                fragment = new HoneycombManagerFragment();
                fm.beginTransaction().add(fragment, ErrorDialogManager.TAG_ERROR_DIALOG_MANAGER).commit();
                fm.executePendingTransactions();
            }
            fragment.finishAfterDialog = finishAfterDialog;
            fragment.argumentsForErrorDialog = argumentsForErrorDialog;
            fragment.executionScope = executionScope;
        }
    }

    public static class SupportManagerFragment extends android.support.v4.app.Fragment {
        protected Bundle argumentsForErrorDialog;
        private EventBus eventBus;
        private Object executionScope;
        protected boolean finishAfterDialog;
        private boolean skipRegisterOnNextResume;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this);
            this.skipRegisterOnNextResume = true;
        }

        public void onResume() {
            super.onResume();
            if (this.skipRegisterOnNextResume) {
                this.skipRegisterOnNextResume = false;
                return;
            }
            this.eventBus = ErrorDialogManager.factory.config.getEventBus();
            this.eventBus.register(this);
        }

        public void onPause() {
            this.eventBus.unregister(this);
            super.onPause();
        }

        public void onEventMainThread(ThrowableFailureEvent event) {
            if (ErrorDialogManager.isInExecutionScope(this.executionScope, event)) {
                ErrorDialogManager.checkLogException(event);
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                fm.executePendingTransactions();
                android.support.v4.app.DialogFragment existingFragment = (android.support.v4.app.DialogFragment) fm.findFragmentByTag(ErrorDialogManager.TAG_ERROR_DIALOG);
                if (existingFragment != null) {
                    existingFragment.dismiss();
                }
                android.support.v4.app.DialogFragment errorFragment = (android.support.v4.app.DialogFragment) ErrorDialogManager.factory.prepareErrorFragment(event, this.finishAfterDialog, this.argumentsForErrorDialog);
                if (errorFragment != null) {
                    errorFragment.show(fm, ErrorDialogManager.TAG_ERROR_DIALOG);
                }
            }
        }

        public static void attachTo(Activity activity, Object executionScope, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
            android.support.v4.app.FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            SupportManagerFragment fragment = (SupportManagerFragment) fm.findFragmentByTag(ErrorDialogManager.TAG_ERROR_DIALOG_MANAGER);
            if (fragment == null) {
                fragment = new SupportManagerFragment();
                fm.beginTransaction().add(fragment, ErrorDialogManager.TAG_ERROR_DIALOG_MANAGER).commit();
                fm.executePendingTransactions();
            }
            fragment.finishAfterDialog = finishAfterDialog;
            fragment.argumentsForErrorDialog = argumentsForErrorDialog;
            fragment.executionScope = executionScope;
        }
    }

    public static void attachTo(Activity activity) {
        attachTo(activity, false, null);
    }

    public static void attachTo(Activity activity, boolean finishAfterDialog) {
        attachTo(activity, finishAfterDialog, null);
    }

    public static void attachTo(Activity activity, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
        attachTo(activity, activity.getClass(), finishAfterDialog, argumentsForErrorDialog);
    }

    public static void attachTo(Activity activity, Object executionScope, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
        if (factory == null) {
            throw new RuntimeException("You must set the static factory field to configure error dialogs for your app.");
        } else if (isSupportActivity(activity)) {
            SupportManagerFragment.attachTo(activity, executionScope, finishAfterDialog, argumentsForErrorDialog);
        } else {
            HoneycombManagerFragment.attachTo(activity, executionScope, finishAfterDialog, argumentsForErrorDialog);
        }
    }

    private static boolean isSupportActivity(Activity activity) {
        Class<?> c = activity.getClass().getSuperclass();
        while (c != null) {
            String name = c.getName();
            if (name.equals("android.support.v4.app.FragmentActivity")) {
                return true;
            }
            if (name.startsWith("com.actionbarsherlock.app") && (name.endsWith(".SherlockActivity") || name.endsWith(".SherlockListActivity") || name.endsWith(".SherlockPreferenceActivity"))) {
                throw new RuntimeException("Please use SherlockFragmentActivity. Illegal activity: " + name);
            } else if (!name.equals("android.app.Activity")) {
                c = c.getSuperclass();
            } else if (VERSION.SDK_INT >= 11) {
                return false;
            } else {
                throw new RuntimeException("Illegal activity without fragment support. Either use Android 3.0+ or android.support.v4.app.FragmentActivity.");
            }
        }
        throw new RuntimeException("Illegal activity type: " + activity.getClass());
    }

    protected static void checkLogException(ThrowableFailureEvent event) {
        if (factory.config.logExceptions) {
            String tag = factory.config.tagForLoggingExceptions;
            if (tag == null) {
                tag = EventBus.TAG;
            }
            Log.i(tag, "Error dialog manager received exception", event.throwable);
        }
    }

    private static boolean isInExecutionScope(Object executionScope, ThrowableFailureEvent event) {
        if (event != null) {
            Object eventExecutionScope = event.getExecutionScope();
            if (!(eventExecutionScope == null || eventExecutionScope.equals(executionScope))) {
                return false;
            }
        }
        return true;
    }
}
