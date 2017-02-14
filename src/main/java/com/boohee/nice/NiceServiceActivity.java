package com.boohee.nice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.boohee.api.NiceApi;
import com.boohee.main.GestureActivity;
import com.boohee.nice.fragment.NiceIntroduceFragment;
import com.boohee.nice.fragment.NiceMainFragment;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;

import org.json.JSONObject;

public class NiceServiceActivity extends GestureActivity {
    private static final int HAVE_NICE_SERVICE = 1;
    private static final int NO_NICE_SERVICE   = 0;
    @Nullable
    private NiceIntroduceFragment introduceFragment;
    @Nullable
    private NiceMainFragment      mainFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ce);
        getNiceStatus();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNiceStatus();
    }

    private void getNiceStatus() {
        showLoading();
        NiceApi.getNiceStatus(this.activity, new JsonCallback(this.activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                int status = object.optInt("user_status");
                if (status == 0) {
                    NiceServiceActivity.this.showNiceIntroduceFragment();
                } else if (status == 1) {
                    NiceServiceActivity.this.showNiceMainFragment();
                }
            }

            public void onFinish() {
                super.onFinish();
                NiceServiceActivity.this.dismissLoading();
            }
        });
    }

    private void showNiceMainFragment() {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.mainFragment == null) {
                this.mainFragment = NiceMainFragment.newInstance();
                transaction.add((int) R.id.fragment_container, this.mainFragment);
            } else if (this.mainFragment != null) {
                if (this.mainFragment.isHidden()) {
                    transaction.show(this.mainFragment);
                }
                this.mainFragment.refreshStatus();
            }
            if (!(this.introduceFragment == null || this.introduceFragment.isHidden())) {
                transaction.hide(this.introduceFragment);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNiceIntroduceFragment() {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (this.introduceFragment == null) {
                this.introduceFragment = NiceIntroduceFragment.newInstance();
                transaction.add((int) R.id.fragment_container, this.introduceFragment);
            } else if (this.introduceFragment != null && this.introduceFragment.isHidden()) {
                transaction.show(this.introduceFragment);
            }
            if (!(this.mainFragment == null || this.mainFragment.isHidden())) {
                transaction.hide(this.mainFragment);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, NiceServiceActivity.class));
    }
}
