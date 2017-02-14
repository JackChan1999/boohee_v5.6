package com.mob.tools;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.mob.tools.utils.Ln;
import com.umeng.socialize.common.SocializeConstants;

import java.util.HashMap;

public class MobUIShell extends Activity {
    private static HashMap<String, FakeActivity> executors = new HashMap();
    private FakeActivity executor;

    static {
        Ln.d("===============================", new Object[0]);
        Ln.d("MobTools " + "2015-04-28".replace("-0", SocializeConstants.OP_DIVIDER_MINUS)
                .replace(SocializeConstants.OP_DIVIDER_MINUS, "."), new Object[0]);
        Ln.d("===============================", new Object[0]);
    }

    public static String registerExecutor(FakeActivity fakeActivity) {
        return registerExecutor(String.valueOf(System.currentTimeMillis()), fakeActivity);
    }

    public static String registerExecutor(String str, FakeActivity fakeActivity) {
        executors.put(str, fakeActivity);
        return str;
    }

    public void finish() {
        if (this.executor == null || !this.executor.onFinish()) {
            super.finish();
        }
    }

    public FakeActivity getExecutor() {
        return this.executor;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (this.executor != null) {
            this.executor.onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.executor != null) {
            this.executor.onConfigurationChanged(configuration);
        }
    }

    protected void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("launch_time");
        String stringExtra2 = intent.getStringExtra("executor_name");
        this.executor = (FakeActivity) executors.remove(stringExtra);
        if (this.executor == null) {
            this.executor = (FakeActivity) executors.remove(intent.getScheme());
            if (this.executor == null) {
                Ln.e(new RuntimeException("Executor lost! launchTime = " + stringExtra + ", " +
                        "executorName: " + stringExtra2));
                super.onCreate(bundle);
                finish();
                return;
            }
        }
        Ln.i("MobUIShell found executor: " + this.executor.getClass(), new Object[0]);
        this.executor.setActivity(this);
        super.onCreate(bundle);
        this.executor.onCreate();
    }

    protected void onDestroy() {
        if (this.executor != null) {
            this.executor.sendResult();
            this.executor.onDestroy();
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (this.executor != null) {
            z = this.executor.onKeyEvent(i, keyEvent);
        }
        return z ? true : super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (this.executor != null) {
            z = this.executor.onKeyEvent(i, keyEvent);
        }
        return z ? true : super.onKeyUp(i, keyEvent);
    }

    protected void onNewIntent(Intent intent) {
        if (this.executor == null) {
            super.onNewIntent(intent);
        } else {
            this.executor.onNewIntent(intent);
        }
    }

    protected void onPause() {
        if (this.executor != null) {
            this.executor.onPause();
        }
        super.onPause();
    }

    protected void onRestart() {
        if (this.executor != null) {
            this.executor.onRestart();
        }
        super.onRestart();
    }

    protected void onResume() {
        if (this.executor != null) {
            this.executor.onResume();
        }
        super.onResume();
    }

    protected void onStart() {
        if (this.executor != null) {
            this.executor.onStart();
        }
        super.onStart();
    }

    protected void onStop() {
        if (this.executor != null) {
            this.executor.onStop();
        }
        super.onStop();
    }

    public void setContentView(int i) {
        setContentView(LayoutInflater.from(this).inflate(i, null));
    }

    public void setContentView(View view) {
        if (view != null) {
            super.setContentView(view);
            if (this.executor != null) {
                this.executor.setContentView(view);
            }
        }
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        if (view != null) {
            if (layoutParams == null) {
                super.setContentView(view);
            } else {
                super.setContentView(view, layoutParams);
            }
            if (this.executor != null) {
                this.executor.setContentView(view);
            }
        }
    }
}
