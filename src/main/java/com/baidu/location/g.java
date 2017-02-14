package com.baidu.location;

import java.text.SimpleDateFormat;

class g implements ax, n {
    private static g br = null;
    private long bo = 0;
    public long bp = 0;
    private long bq = 0;
    public boolean bs = false;

    private g() {
    }

    public static g e() {
        if (br == null) {
            br = new g();
        }
        return br;
    }

    public void h() {
        if (!this.bs) {
            this.bo = System.currentTimeMillis();
        }
    }

    public long new(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    public void try(String str) {
        if (!this.bs) {
            this.bq = System.currentTimeMillis();
            long j = (this.bq - this.bo) / 2;
            if (j <= 3000 && j >= 0) {
                long j2 = new(str);
                if (j2 > 0) {
                    this.bp = (j + j2) - System.currentTimeMillis();
                    this.bs = false;
                }
            }
        }
    }
}
