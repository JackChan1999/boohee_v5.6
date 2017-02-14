package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.boohee.one.MyApplication;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.RequestManager;
import com.boohee.one.ui.BaseActivity;
import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Field;

public abstract class BaseDialogFragment extends DialogFragment {
    private   Activity         activity;
    public    onChangeListener changeListener;
    protected FileCache        mCache;

    public interface onChangeListener {
        void onFinish();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.mCache = FileCache.get(getActivity());
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void showLoading() {
        if (this.activity != null && (this.activity instanceof BaseActivity)) {
            ((BaseActivity) this.activity).showLoading();
        }
    }

    public void dismissLoading() {
        if (this.activity != null && (this.activity instanceof BaseActivity)) {
            ((BaseActivity) this.activity).dismissLoading();
        }
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (IllegalStateException e3) {
        }
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add((Fragment) this, tag);
        transaction.commitAllowingStateLoss();
    }

    public void dismiss() {
        dismissAllowingStateLoss();
    }

    public boolean isRemoved() {
        return getActivity() == null || isDetached() || !isAdded();
    }

    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        if (refWatcher != null) {
            refWatcher.watch(this);
        }
        RequestManager.cancelAll(this);
    }

    public void setChangeListener(onChangeListener onChangeListener) {
        this.changeListener = onChangeListener;
    }

    public void callChangeListener() {
        if (this.changeListener != null) {
            this.changeListener.onFinish();
        }
    }
}
