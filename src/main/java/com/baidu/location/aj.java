package com.baidu.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Timer;
import java.util.TimerTask;

public class aj implements n, ax {
    public static final float hA = 4.0f;
    public static final float hj = 0.01f;
    private static final int hs = 6;
    private static final int hu = 20;
    private static final float hz = 0.8f;
    private int hB;
    private SensorManager hC;
    private boolean hD;
    private int he;
    private int hf;
    private int hg;
    private final long hh;
    private float[] hi;
    private int hk;
    private double hl;
    private double[] hm;
    public SensorEventListener hn;
    private int ho;
    private long hp;
    Timer hq;
    private int hr;
    private Sensor ht;
    private float[] hv;
    private volatile int hw;
    private int hx;
    private double[] hy;

    public aj(Context context) {
        this(context, 0);
    }

    private aj(Context context, int i) {
        this.hh = 30;
        this.hw = 0;
        this.hg = 1;
        this.hi = new float[3];
        this.hv = new float[]{0.0f, 0.0f, 0.0f};
        this.hx = 31;
        this.hy = new double[this.hx];
        this.hB = 0;
        this.hm = new double[6];
        this.ho = 0;
        this.hp = 0;
        this.hk = 0;
        this.hn = new SensorEventListener(this) {
            final /* synthetic */ aj a;

            {
                this.a = r1;
            }

            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                switch (sensorEvent.sensor.getType()) {
                    case 1:
                        float[] fArr = (float[]) sensorEvent.values.clone();
                        this.a.hv = (float[]) fArr.clone();
                        fArr = this.a.if(fArr[0], fArr[1], fArr[2]);
                        if (aj.do(this.a) >= 20) {
                            double d = (double) ((fArr[2] * fArr[2]) + ((fArr[0] * fArr[0]) + (fArr[1] * fArr[1])));
                            if (this.a.hw == 0) {
                                if (d > 4.0d) {
                                    this.a.hw = 1;
                                    return;
                                }
                                return;
                            } else if (d < 0.009999999776482582d) {
                                this.a.hw = 0;
                                return;
                            } else {
                                return;
                            }
                        }
                        return;
                    default:
                        return;
                }
            }
        };
        this.hl = 1.6d;
        this.hf = 440;
        try {
            this.hC = (SensorManager) context.getSystemService("sensor");
            this.hr = i;
            this.ht = this.hC.getDefaultSensor(1);
        } catch (Exception e) {
        }
    }

    private void bG() {
        if (this.he >= 20) {
            long currentTimeMillis = System.currentTimeMillis();
            Object obj = new float[3];
            System.arraycopy(this.hv, 0, obj, 0, 3);
            double sqrt = Math.sqrt((double) ((obj[2] * obj[2]) + ((obj[0] * obj[0]) + (obj[1] * obj[1]))));
            this.hy[this.hB] = sqrt;
            do(sqrt);
            this.hB++;
            if (this.hB == this.hx) {
                this.hB = 0;
                sqrt = if(this.hy);
                if (this.hw != 0 || sqrt >= 0.3d) {
                    case(1);
                    this.hw = 1;
                } else {
                    case(0);
                    this.hw = 0;
                }
            }
            if (currentTimeMillis - this.hp > ((long) this.hf) && if(this.hl)) {
                this.hk++;
                this.hp = currentTimeMillis;
            }
        }
    }

    private synchronized void case(int i) {
        this.hg |= i;
    }

    static /* synthetic */ int do(aj ajVar) {
        int i = ajVar.he + 1;
        ajVar.he = i;
        return i;
    }

    private void do(double d) {
        this.hm[this.ho % 6] = d;
        this.ho++;
        this.ho %= 6;
    }

    private double if(double[] dArr) {
        int i = 0;
        double d = 0.0d;
        double d2 = 0.0d;
        for (double d3 : dArr) {
            d2 += d3;
        }
        d2 /= (double) r6;
        while (i < r6) {
            d += (dArr[i] - d2) * (dArr[i] - d2);
            i++;
        }
        return d / ((double) (r6 - 1));
    }

    private boolean if(double d) {
        for (int i = 1; i <= 5; i++) {
            if (this.hm[((((this.ho - 1) - i) + 6) + 6) % 6] - this.hm[((this.ho - 1) + 6) % 6] > d) {
                return true;
            }
        }
        return false;
    }

    private float[] if(float f, float f2, float f3) {
        float[] fArr = new float[]{(this.hi[0] * hz) + (0.19999999f * f), (this.hi[1] * hz) + (0.19999999f * f2), (this.hi[2] * hz) + (0.19999999f * f3)};
        fArr[0] = f - this.hi[0];
        fArr[1] = f2 - this.hi[1];
        fArr[2] = f3 - this.hi[2];
        return fArr;
    }

    public synchronized int bD() {
        return this.he < 20 ? -1 : this.hk;
    }

    public synchronized void bE() {
        this.hg = 0;
    }

    public synchronized int bF() {
        return this.he < 20 ? 1 : this.hg;
    }

    public void bH() {
        if (!this.hD && this.ht != null) {
            try {
                this.hC.registerListener(this.hn, this.ht, this.hr);
            } catch (Exception e) {
            }
            this.hq = new Timer("UpdateData", false);
            this.hq.schedule(new TimerTask(this) {
                final /* synthetic */ aj a;

                {
                    this.a = r1;
                }

                public void run() {
                    this.a.bG();
                }
            }, 500, 30);
            this.hD = true;
        }
    }

    public void bI() {
        if (this.hD) {
            try {
                this.hC.unregisterListener(this.hn);
            } catch (Exception e) {
            }
            this.hq.cancel();
            this.hq.purge();
            this.hq = null;
            this.hD = false;
        }
    }
}
