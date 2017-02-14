package de.greenrobot.event.util;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;

public abstract class ErrorDialogFragmentFactory<T> {
    protected final ErrorDialogConfig config;

    @TargetApi(11)
    public static class Honeycomb extends ErrorDialogFragmentFactory<Fragment> {
        public Honeycomb(ErrorDialogConfig config) {
            super(config);
        }

        protected Fragment createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            de.greenrobot.event.util.ErrorDialogFragments.Honeycomb errorFragment = new de.greenrobot.event.util.ErrorDialogFragments.Honeycomb();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }
    }

    public static class Support extends ErrorDialogFragmentFactory<android.support.v4.app.Fragment> {
        public Support(ErrorDialogConfig config) {
            super(config);
        }

        protected android.support.v4.app.Fragment createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            de.greenrobot.event.util.ErrorDialogFragments.Support errorFragment = new de.greenrobot.event.util.ErrorDialogFragments.Support();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }
    }

    protected abstract T createErrorFragment(ThrowableFailureEvent throwableFailureEvent, Bundle bundle);

    protected ErrorDialogFragmentFactory(ErrorDialogConfig config) {
        this.config = config;
    }

    protected T prepareErrorFragment(ThrowableFailureEvent event, boolean finishAfterDialog, Bundle argumentsForErrorDialog) {
        if (event.isSuppressErrorUi()) {
            return null;
        }
        Bundle bundle;
        if (argumentsForErrorDialog != null) {
            bundle = (Bundle) argumentsForErrorDialog.clone();
        } else {
            bundle = new Bundle();
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_TITLE)) {
            bundle.putString(ErrorDialogManager.KEY_TITLE, getTitleFor(event, bundle));
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_MESSAGE)) {
            bundle.putString(ErrorDialogManager.KEY_MESSAGE, getMessageFor(event, bundle));
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG)) {
            bundle.putBoolean(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG, finishAfterDialog);
        }
        if (!(bundle.containsKey(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE) || this.config.defaultEventTypeOnDialogClosed == null)) {
            bundle.putSerializable(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE, this.config.defaultEventTypeOnDialogClosed);
        }
        if (!(bundle.containsKey(ErrorDialogManager.KEY_ICON_ID) || this.config.defaultDialogIconId == 0)) {
            bundle.putInt(ErrorDialogManager.KEY_ICON_ID, this.config.defaultDialogIconId);
        }
        return createErrorFragment(event, bundle);
    }

    protected String getTitleFor(ThrowableFailureEvent event, Bundle arguments) {
        return this.config.resources.getString(this.config.defaultTitleId);
    }

    protected String getMessageFor(ThrowableFailureEvent event, Bundle arguments) {
        return this.config.resources.getString(this.config.getMessageIdForThrowable(event.throwable));
    }
}
