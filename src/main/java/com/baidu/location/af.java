package com.baidu.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class af implements ax, n, SensorEventListener {
    private static float g5;
    private static af g9;
    private boolean g3;
    float[] g4;
    SensorManager g6;
    float[] g7 = new float[9];
    float[] g8;

    af() {
    }

    public static af bw() {
        if (g9 == null) {
            g9 = new af();
        }
        return g9;
    }

    public float bu() {
        return g5;
    }

    public synchronized void bv() {
        if (this.g6 != null) {
            this.g6.unregisterListener(this);
            this.g6 = null;
        }
    }

    public synchronized void bx() {
        if (this.g6 == null) {
            this.g6 = (SensorManager) f.getServiceContext().getSystemService("sensor");
        }
        this.g6.registerListener(this, this.g6.getDefaultSensor(1), 3);
        this.g6.registerListener(this, this.g6.getDefaultSensor(2), 3);
    }

    public boolean by() {
        return this.g3;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case 1:
                this.g4 = sensorEvent.values;
                break;
            case 2:
                this.g8 = sensorEvent.values;
                break;
        }
        if (this.g4 != null && this.g8 != null) {
            float[] fArr = new float[9];
            if (SensorManager.getRotationMatrix(fArr, null, this.g4, this.g8)) {
                float[] fArr2 = new float[3];
                SensorManager.getOrientation(fArr, fArr2);
                g5 = (float) Math.toDegrees((double) fArr2[0]);
                g5 = (float) Math.floor(g5 >= 0.0f ? (double) g5 : (double) (g5 + 360.0f));
            }
        }
    }

    public void try(boolean z) {
        this.g3 = z;
    }
}
