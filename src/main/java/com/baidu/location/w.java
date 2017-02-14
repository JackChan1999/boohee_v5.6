package com.baidu.location;

import android.location.Location;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

public class w implements ax, n {
    private static final int e0 = 1000;
    private static final int e2 = 100;
    private static int e4 = 0;
    private static w e5 = null;
    private static long e6 = 0;
    private static final int eI = 12;
    private static StringBuffer eJ = null;
    private static final int eK = 5;
    private static File eL = new File(I, eV);
    private static final int eM = 3600;
    private static int eN = 0;
    private static long eO = 0;
    private static long eP = 0;
    private static boolean eQ = true;
    private static final int eR = 1024;
    private static int eS = 0;
    private static double eT = 0.0d;
    private static double eU = 0.0d;
    private static String eV = "Temp_in.dat";
    private static int eW = 0;
    private static int eX = 0;
    private static final int eY = 5;
    private static final int eZ = 750;
    private String e1 = null;
    private boolean e3 = true;

    private w(String str) {
        if (str == null) {
            str = "";
        } else if (str.length() > 100) {
            str = str.substring(0, 100);
        }
        this.e1 = str;
    }

    private static boolean aH() {
        if (eL.exists()) {
            eL.delete();
        }
        if (!eL.getParentFile().exists()) {
            eL.getParentFile().mkdirs();
        }
        try {
            eL.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(eL, "rw");
            randomAccessFile.seek(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(1);
            randomAccessFile.close();
            aJ();
            return eL.exists();
        } catch (IOException e) {
            return false;
        }
    }

    private void aI() {
        if (eJ != null && eJ.length() >= 100) {
            h(eJ.toString());
        }
        aJ();
    }

    private static void aJ() {
        eQ = true;
        eJ = null;
        eW = 0;
        e4 = 0;
        eP = 0;
        eO = 0;
        e6 = 0;
        eT = 0.0d;
        eU = 0.0d;
        eN = 0;
        eS = 0;
        eX = 0;
    }

    public static w aK() {
        if (e5 == null) {
            e5 = new w(az.cn().cj());
        }
        return e5;
    }

    private void aL() {
    }

    public static String aM() {
        if (eL == null) {
            return null;
        }
        if (!eL.exists()) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(eL, "rw");
            randomAccessFile.seek(0);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            if (!if(readInt, readInt2, readInt3)) {
                randomAccessFile.close();
                aH();
                return null;
            } else if (readInt2 == 0 || readInt2 == readInt3) {
                randomAccessFile.close();
                return null;
            } else {
                long j = 0 + ((long) (((readInt2 - 1) * 1024) + 12));
                randomAccessFile.seek(j);
                int readInt4 = randomAccessFile.readInt();
                byte[] bArr = new byte[readInt4];
                randomAccessFile.seek(j + 4);
                for (readInt3 = 0; readInt3 < readInt4; readInt3++) {
                    bArr[readInt3] = randomAccessFile.readByte();
                }
                String str = new String(bArr);
                readInt3 = readInt < c.ay ? readInt2 + 1 : readInt2 == c.ay ? 1 : readInt2 + 1;
                randomAccessFile.seek(4);
                randomAccessFile.writeInt(readInt3);
                randomAccessFile.close();
                return str;
            }
        } catch (IOException e) {
            return null;
        }
    }

    private boolean aN() {
        if (eL.exists()) {
            eL.delete();
        }
        aJ();
        return !eL.exists();
    }

    private String for(int i) {
        if (!eL.exists()) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(eL, "rw");
            randomAccessFile.seek(0);
            int readInt = randomAccessFile.readInt();
            if (!if(readInt, randomAccessFile.readInt(), randomAccessFile.readInt())) {
                randomAccessFile.close();
                aH();
                return null;
            } else if (i == 0 || i == readInt + 1) {
                randomAccessFile.close();
                return null;
            } else {
                long j = (12 + 0) + ((long) ((i - 1) * 1024));
                randomAccessFile.seek(j);
                int readInt2 = randomAccessFile.readInt();
                byte[] bArr = new byte[readInt2];
                randomAccessFile.seek(j + 4);
                for (readInt = 0; readInt < readInt2; readInt++) {
                    bArr[readInt] = randomAccessFile.readByte();
                }
                randomAccessFile.close();
                return new String(bArr);
            }
        } catch (IOException e) {
            return null;
        }
    }

    private boolean h(String str) {
        if (str == null || !str.startsWith("&nr")) {
            return false;
        }
        if (!eL.exists() && !aH()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(eL, "rw");
            randomAccessFile.seek(0);
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            if (if(readInt, readInt2, readInt3)) {
                if (c.aZ) {
                    if (readInt == c.ay) {
                        if (str.equals(for(readInt3 == 1 ? c.ay : readInt3 - 1))) {
                            randomAccessFile.close();
                            return false;
                        }
                    } else if (readInt3 > 1 && str.equals(for(readInt3 - 1))) {
                        randomAccessFile.close();
                        return false;
                    }
                }
                randomAccessFile.seek(((long) (((readInt3 - 1) * 1024) + 12)) + 0);
                if (str.length() > eZ) {
                    randomAccessFile.close();
                    return false;
                }
                String i = Jni.i(str);
                int length = i.length();
                if (length > 1020) {
                    randomAccessFile.close();
                    return false;
                }
                randomAccessFile.writeInt(length);
                randomAccessFile.writeBytes(i);
                if (readInt == 0) {
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(1);
                    randomAccessFile.writeInt(1);
                    randomAccessFile.writeInt(2);
                } else if (readInt < c.ay - 1) {
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(readInt + 1);
                    randomAccessFile.seek(8);
                    randomAccessFile.writeInt(readInt + 2);
                } else if (readInt == c.ay - 1) {
                    randomAccessFile.seek(0);
                    randomAccessFile.writeInt(c.ay);
                    if (readInt2 == 0 || readInt2 == 1) {
                        randomAccessFile.writeInt(2);
                    }
                    randomAccessFile.seek(8);
                    randomAccessFile.writeInt(1);
                } else if (readInt3 == readInt2) {
                    readInt = readInt3 == c.ay ? 1 : readInt3 + 1;
                    r2 = readInt == c.ay ? 1 : readInt + 1;
                    randomAccessFile.seek(4);
                    randomAccessFile.writeInt(r2);
                    randomAccessFile.writeInt(readInt);
                } else {
                    readInt = readInt3 == c.ay ? 1 : readInt3 + 1;
                    if (readInt == readInt2) {
                        r2 = readInt == c.ay ? 1 : readInt + 1;
                        randomAccessFile.seek(4);
                        randomAccessFile.writeInt(r2);
                    }
                    randomAccessFile.seek(8);
                    randomAccessFile.writeInt(readInt);
                }
                randomAccessFile.close();
                return true;
            }
            randomAccessFile.close();
            aH();
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean if(int i, int i2, int i3) {
        return (i < 0 || i > c.ay) ? false : (i2 < 0 || i2 > i + 1) ? false : i3 >= 1 && i3 <= i + 1 && i3 <= c.ay;
    }

    private boolean if(Location location, int i, int i2) {
        if (location == null || !c.az || !this.e3 || !y.f8) {
            return false;
        }
        if (c.av < 5) {
            c.av = 5;
        } else if (c.av > 1000) {
            c.av = 1000;
        }
        if (c.as < 5) {
            c.as = 5;
        } else if (c.as > 3600) {
            c.as = 3600;
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        long time = location.getTime() / 1000;
        if (eQ) {
            eW = 1;
            eJ = new StringBuffer("");
            eJ.append(String.format(Locale.CHINA, "&nr=%s&traj=%d,%.5f,%.5f|", new Object[]{this.e1, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)}));
            e4 = eJ.length();
            eP = time;
            eT = longitude;
            eU = latitude;
            eO = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            e6 = (long) Math.floor((latitude * 100000.0d) + 0.5d);
            eQ = false;
            return true;
        }
        float[] fArr = new float[1];
        Location.distanceBetween(latitude, longitude, eU, eT, fArr);
        long j = time - eP;
        if (fArr[0] < ((float) c.av) && j < ((long) c.as)) {
            return false;
        }
        if (eJ == null) {
            eW++;
            e4 = 0;
            eJ = new StringBuffer("");
            eJ.append(String.format(Locale.CHINA, "&nr=%s&traj=%d,%.5f,%.5f|", new Object[]{this.e1, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)}));
            e4 = eJ.length();
            eP = time;
            eT = longitude;
            eU = latitude;
            eO = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            e6 = (long) Math.floor((latitude * 100000.0d) + 0.5d);
        } else {
            eT = longitude;
            eU = latitude;
            long floor = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            long floor2 = (long) Math.floor((latitude * 100000.0d) + 0.5d);
            eN = (int) (time - eP);
            eS = (int) (floor - eO);
            eX = (int) (floor2 - e6);
            eJ.append(String.format(Locale.CHINA, "%d,%d,%d|", new Object[]{Integer.valueOf(eN), Integer.valueOf(eS), Integer.valueOf(eX)}));
            e4 = eJ.length();
            eP = time;
            eO = floor;
            e6 = floor2;
        }
        if (e4 + 15 > eZ) {
            h(eJ.toString());
            eJ = null;
        }
        if (eW >= c.ay) {
            this.e3 = false;
        }
        return true;
    }

    public void aO() {
        aI();
    }

    public boolean do(Location location) {
        return if(location, c.av, c.as);
    }
}
