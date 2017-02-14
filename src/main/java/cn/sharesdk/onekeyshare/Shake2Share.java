package cn.sharesdk.onekeyshare;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.R;

public class Shake2Share extends FakeActivity implements SensorEventListener {
    private static final int SHAKE_THRESHOLD = 1500;
    private static final int UPDATE_INTERVAL = 100;
    private OnShakeListener listener;
    private long mLastUpdateTime;
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    private SensorManager mSensorManager;
    private boolean shaken;

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        int resId = R.getBitmapRes(activity, "ssdk_oks_shake_to_share_back");
        if (resId > 0) {
            activity.setTheme(16973835);
            activity.requestWindowFeature(1);
            activity.getWindow().setBackgroundDrawableResource(resId);
        }
    }

    public void onCreate() {
        startSensor();
        int resId = R.getBitmapRes(this.activity, "ssdk_oks_yaoyiyao");
        if (resId > 0) {
            ImageView iv = new ImageView(this.activity);
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            iv.setImageResource(resId);
            this.activity.setContentView(iv);
        }
        resId = R.getStringRes(this.activity, "shake2share");
        if (resId > 0) {
            Toast.makeText(this.activity, resId, 0).show();
        }
    }

    private void startSensor() {
        this.mSensorManager = (SensorManager) this.activity.getSystemService("sensor");
        if (this.mSensorManager == null) {
            throw new UnsupportedOperationException();
        }
        Sensor sensor = this.mSensorManager.getDefaultSensor(1);
        if (sensor == null) {
            throw new UnsupportedOperationException();
        } else if (!this.mSensorManager.registerListener(this, sensor, 1)) {
            throw new UnsupportedOperationException();
        }
    }

    public void onDestroy() {
        stopSensor();
    }

    private void stopSensor() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
            this.mSensorManager = null;
        }
    }

    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - this.mLastUpdateTime;
        if (diffTime > 100) {
            if (this.mLastUpdateTime != 0) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                float deltaX = x - this.mLastX;
                float deltaY = y - this.mLastY;
                float deltaZ = z - this.mLastZ;
                if ((((float) Math.sqrt((double) (((deltaX * deltaX) + (deltaY * deltaY)) + (deltaZ * deltaZ)))) / ((float) diffTime)) * 10000.0f > 1500.0f) {
                    if (!this.shaken) {
                        this.shaken = true;
                        finish();
                    }
                    if (this.listener != null) {
                        this.listener.onShake();
                    }
                }
                this.mLastX = x;
                this.mLastY = y;
                this.mLastZ = z;
            }
            this.mLastUpdateTime = currentTime;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
