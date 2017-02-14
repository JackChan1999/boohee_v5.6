package com.boohee.one.bet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class BetAllActivity extends GestureActivity {
    public static void comeOn(Context context, String type) {
        Intent intent = new Intent(context, BetAllActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad);
        String type = getIntent().getStringExtra("type");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, BetListFragment.newInstance(1, type));
        transaction.commitAllowingStateLoss();
    }
}
