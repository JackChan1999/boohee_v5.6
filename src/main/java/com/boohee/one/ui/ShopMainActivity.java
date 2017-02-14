package com.boohee.one.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.ShopMainFragment;

public class ShopMainActivity extends GestureActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(View.inflate(this, R.layout.d0, null));
        initToolbar();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new
                ShopMainFragment()).commitAllowingStateLoss();
    }

    private void initToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
}
