package com.boohee.one.ui;

import android.os.Bundle;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.PreferenceFragment;

public class PreferenceActivity extends GestureActivity {
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setTitle(R.string.ap);
        setContentView(R.layout.cj);
        getFragmentManager().beginTransaction().replace(R.id.container, new PreferenceFragment())
                .commitAllowingStateLoss();
    }
}
