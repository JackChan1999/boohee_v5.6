package com.boohee.one.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.boohee.one.MyApplication;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.RequestManager;
import com.boohee.one.ui.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Field;

public abstract class BaseFragment extends Fragment {
    static final String TAG = BaseFragment.class.getName();
    private Activity activity;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    protected FileCache mCache;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.mCache = FileCache.get(getActivity());
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

    public void loadFirst() {
    }
}
