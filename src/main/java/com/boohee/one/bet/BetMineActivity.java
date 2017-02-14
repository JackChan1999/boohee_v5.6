package com.boohee.one.bet;

import android.os.Bundle;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class BetMineActivity extends GestureActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new
                BetMineFragment()).commitAllowingStateLoss();
    }
}
