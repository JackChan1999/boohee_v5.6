package com.baidu.location;

import java.io.File;
import java.io.RandomAccessFile;

class e {
    static e if;
    int a = 20;
    int do = 0;
    int for = 3164;
    String int = "firll.dat";

    e() {
    }

    private long a(int i) {
        String str = c.goto();
        if (str == null) {
            return -1;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(str + File.separator + this.int, "rw");
            randomAccessFile.seek((long) i);
            int readInt = randomAccessFile.readInt();
            long readLong = randomAccessFile.readLong();
            int readInt2 = randomAccessFile.readInt();
            randomAccessFile.close();
            return readInt == readInt2 ? readLong : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    private void a(int i, long j) {
        String str = c.goto();
        if (str != null) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(str + File.separator + this.int, "rw");
                randomAccessFile.seek((long) i);
                randomAccessFile.writeInt(this.for);
                randomAccessFile.writeLong(j);
                randomAccessFile.writeInt(this.for);
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static e do() {
        if (if == null) {
            if = new e();
        }
        return if;
    }

    public long a() {
        return a(this.a);
    }

    public void a(long j) {
        a(this.do, j);
    }

    public long if() {
        return a(this.do);
    }

    public void if(long j) {
        a(this.a, j);
    }
}
