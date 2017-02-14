package com.qiniu.android.storage.persistent;

import com.qiniu.android.storage.Recorder;
import com.qiniu.android.utils.UrlSafeBase64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public final class FileRecorder implements Recorder {
    public String directory;

    public FileRecorder(String directory) throws IOException {
        this.directory = directory;
        File f = new File(directory);
        if (f.exists()) {
            if (!f.isDirectory()) {
                throw new IOException("does not mkdir");
            }
        } else if (!f.mkdirs()) {
            throw new IOException("mkdir failed");
        }
    }

    public void set(String key, byte[] data) {
        IOException e;
        FileOutputStream fo = null;
        try {
            FileOutputStream fo2 = new FileOutputStream(new File(this.directory, UrlSafeBase64
                    .encodeToString(key)));
            try {
                fo2.write(data);
                fo = fo2;
            } catch (IOException e2) {
                e = e2;
                fo = fo2;
                e.printStackTrace();
                if (fo == null) {
                    try {
                        fo.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return;
                    }
                }
            }
        } catch (IOException e4) {
            e3 = e4;
            e3.printStackTrace();
            if (fo == null) {
                fo.close();
            }
        }
        if (fo == null) {
            fo.close();
        }
    }

    public byte[] get(String key) {
        IOException e;
        File f = new File(this.directory, UrlSafeBase64.encodeToString(key));
        FileInputStream fi = null;
        byte[] data = null;
        int read = 0;
        try {
            if (outOfDate(f)) {
                f.delete();
                return null;
            }
            data = new byte[((int) f.length())];
            FileInputStream fi2 = new FileInputStream(f);
            try {
                read = fi2.read(data);
                fi = fi2;
            } catch (IOException e2) {
                e = e2;
                fi = fi2;
                e.printStackTrace();
                if (fi != null) {
                    try {
                        fi.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                if (read != 0) {
                    return null;
                }
                return data;
            }
            if (fi != null) {
                fi.close();
            }
            if (read != 0) {
                return data;
            }
            return null;
        } catch (IOException e4) {
            e3 = e4;
            e3.printStackTrace();
            if (fi != null) {
                fi.close();
            }
            if (read != 0) {
                return data;
            }
            return null;
        }
    }

    private boolean outOfDate(File f) {
        return f.lastModified() + 172800000 < new Date().getTime();
    }

    public void del(String key) {
        new File(this.directory, UrlSafeBase64.encodeToString(key)).delete();
    }
}
