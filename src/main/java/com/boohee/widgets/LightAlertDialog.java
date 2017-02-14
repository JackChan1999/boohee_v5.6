package com.boohee.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;

public class LightAlertDialog extends AlertDialog {
    protected LightAlertDialog(Context context) {
        super(context);
    }

    protected LightAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LightAlertDialog create(Context context) {
        if (VERSION.SDK_INT >= 14) {
            return new LightAlertDialog(context, 3);
        }
        LightAlertDialog dialog = new LightAlertDialog(context);
        dialog.setInverseBackgroundForced(true);
        return dialog;
    }

    public static LightAlertDialog create(Context context, String title, String message) {
        LightAlertDialog dialog = create(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }

    public static LightAlertDialog create(Context context, int title, int resId) {
        LightAlertDialog dialog = create(context);
        dialog.setTitle(title);
        dialog.setMessage(context.getString(resId));
        return dialog;
    }

    public static LightAlertDialog create(Context context, int resId) {
        LightAlertDialog dialog = create(context);
        dialog.setMessage(context.getString(resId));
        return dialog;
    }

    public static LightAlertDialog create(Context context, String message) {
        LightAlertDialog dialog = create(context);
        dialog.setMessage(message);
        return dialog;
    }

    public LightAlertDialog setPositiveButton(int redId, OnClickListener listener) {
        return setPositiveButton(getContext().getString(redId), listener);
    }

    public LightAlertDialog setPositiveButton(OnClickListener listener) {
        return setPositiveButton(17039370, listener);
    }

    public LightAlertDialog setNeutralButton(int redId, OnClickListener listener) {
        setButton(-3, getContext().getString(redId), listener);
        return this;
    }

    public LightAlertDialog setPositiveButton(CharSequence text, OnClickListener listener) {
        setButton(-1, text, listener);
        return this;
    }

    public LightAlertDialog setNegativeButton(int resId, OnClickListener listener) {
        return setNegativeButton(getContext().getString(resId), listener);
    }

    public LightAlertDialog setNegativeButton(OnClickListener listener) {
        return setNegativeButton(17039360, listener);
    }

    public LightAlertDialog setNegativeButton(CharSequence text, OnClickListener listener) {
        setButton(-2, text, listener);
        return this;
    }
}
