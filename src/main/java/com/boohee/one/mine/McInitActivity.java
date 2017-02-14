package com.boohee.one.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.boohee.api.RecordApi;
import com.boohee.database.UserPreference;
import com.boohee.main.GestureActivity;
import com.boohee.modeldao.SportRecordDao;
import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.MainActivity;
import com.boohee.one.ui.PeriodActivity;
import com.boohee.user.view.UserMcCircleView;
import com.boohee.user.view.UserMcComeView;
import com.boohee.user.view.UserMcDaysView;
import com.boohee.user.view.UserMcGoView;

import org.json.JSONObject;

public class McInitActivity extends GestureActivity {
    private static final int    KEY_NEXT     = 1;
    private static final int    KEY_PREVIOUS = 0;
    private static final int    KEY_SAVE     = 2;
    static final         String TAG          = McInitActivity.class.getName();
    public static int defaultMcCome;
    public static int defaultMcdays;
    private       int count;
    private int currentIndex = 0;
    private int              cycle;
    private int              duration;
    private Menu             mMenu;
    private UserMcCircleView mcCircleView;
    private UserMcComeView   mcComeView;
    private UserMcDaysView   mcDaysView;
    private UserMcGoView     mcGoView;
    private ViewFlipper      viewFlipper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mc_init);
        setTitle(R.string.gj);
        findView();
    }

    private void findView() {
        this.viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        this.count = this.viewFlipper.getChildCount();
        this.mcDaysView = (UserMcDaysView) findViewById(R.id.mc_day_view);
        defaultMcdays = this.mcDaysView.getMcDays();
        this.mcCircleView = (UserMcCircleView) findViewById(R.id.mc_circle_view);
        this.mcComeView = (UserMcComeView) findViewById(R.id.mc_come_view);
        this.mcGoView = (UserMcGoView) findViewById(R.id.mc_go_view);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        menu.add(0, 0, 0, R.string.a03).setShowAsAction(2);
        menu.add(0, 1, 1, R.string.x3).setShowAsAction(2);
        menu.add(0, 2, 2, R.string.ge).setShowAsAction(2);
        refreshMenuItem();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                previousClick();
                return true;
            case 1:
                nextClick();
                return true;
            case 2:
                saveClick();
                return true;
            case 16908332:
                close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void previousClick() {
        this.viewFlipper.setInAnimation(this.ctx, R.anim.a2);
        this.viewFlipper.setOutAnimation(this.ctx, R.anim.a3);
        this.viewFlipper.showPrevious();
        this.currentIndex = this.viewFlipper.getDisplayedChild();
        refreshMenuItem();
    }

    private void nextClick() {
        this.viewFlipper.setInAnimation(this.ctx, R.anim.a_);
        this.viewFlipper.setOutAnimation(this.ctx, R.anim.aa);
        this.viewFlipper.showNext();
        this.currentIndex = this.viewFlipper.getDisplayedChild();
        refreshMenuItem();
    }

    private void saveClick() {
        changeMcProfile();
    }

    private void refreshMenuItem() {
        if (this.currentIndex == 0) {
            this.mMenu.getItem(0).setVisible(false);
            this.mMenu.getItem(1).setVisible(true);
            this.mMenu.getItem(2).setVisible(false);
        } else if (this.currentIndex == this.count - 1) {
            this.mMenu.getItem(0).setVisible(true);
            this.mMenu.getItem(1).setVisible(false);
            this.mMenu.getItem(2).setVisible(true);
        } else {
            this.mMenu.getItem(0).setVisible(true);
            this.mMenu.getItem(1).setVisible(true);
            this.mMenu.getItem(2).setVisible(false);
        }
        if (this.currentIndex == 1) {
            this.mcCircleView.setBottomPicker();
        }
    }

    private void changeMcProfile() {
        this.duration = this.mcDaysView.getMcDays();
        this.cycle = this.mcCircleView.getMcCircle();
        UserPreference userPreference = UserPreference.getInstance(this.ctx);
        userPreference.putInt(SportRecordDao.DURATION, this.duration);
        userPreference.putInt("cycle", this.cycle);
        RecordApi.updateMcUpdateSummaries(this.activity, this.duration, this.cycle, this
                .mcComeView.getLastCome(), this.mcGoView.getLastGo(), new JsonCallback(this
                .activity) {
            public void ok(JSONObject object) {
                super.ok(object);
                Intent intent = new Intent(McInitActivity.this.ctx, PeriodActivity.class);
                intent.addFlags(67108864);
                intent.addFlags(268435456);
                McInitActivity.this.startActivity(intent);
                McInitActivity.this.finish();
            }
        });
    }

    private void close() {
        Intent intent = new Intent(this.ctx, MainActivity.class);
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        startActivity(intent);
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            close();
        }
        return false;
    }
}
