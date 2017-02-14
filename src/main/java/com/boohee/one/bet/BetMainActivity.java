package com.boohee.one.bet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;

public class BetMainActivity extends GestureActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new
                BetFragment()).commitAllowingStateLoss();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "我的").setShowAsAction(2);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(this.ctx, BetMineActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
