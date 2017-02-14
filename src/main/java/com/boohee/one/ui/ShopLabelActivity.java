package com.boohee.one.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.ui.fragment.ShopLabelFragment;

public class ShopLabelActivity extends GestureActivity {
    public static final String EXTRA_LABEL_ID = "extra_label_id";

    public static void start(Context context, int label_id) {
        Intent starter = new Intent(context, ShopLabelActivity.class);
        starter.putExtra("extra_label_id", label_id);
        context.startActivity(starter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cj);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, ShopLabelFragment
                .newInstance(getIntent().getIntExtra("extra_label_id", 0)))
                .commitAllowingStateLoss();
    }
}
