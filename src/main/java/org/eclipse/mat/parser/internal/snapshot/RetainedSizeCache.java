package org.eclipse.mat.parser.internal.snapshot;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.mat.collect.HashMapIntLong;
import org.eclipse.mat.hprof.Messages;
import org.eclipse.mat.parser.model.XSnapshotInfo;

public class RetainedSizeCache {
    private String filename;
    private HashMapIntLong id2size;
    private boolean isDirty = false;

    public RetainedSizeCache(XSnapshotInfo snapshotInfo) {
        this.filename = snapshotInfo.getPrefix() + "i2sv2.index";
        readId2Size(snapshotInfo.getPrefix());
    }

    public long get(int key) {
        try {
            return this.id2size.get(key);
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public void put(int key, long value) {
        this.id2size.put(key, value);
        this.isDirty = true;
    }

    public void close() {
        if (this.isDirty) {
            try {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(this.filename)));
                for (int key : this.id2size.getAllKeys()) {
                    out.writeInt(key);
                    out.writeLong(this.id2size.get(key));
                }
                out.close();
                this.isDirty = false;
            } catch (IOException e) {
                Logger.getLogger(RetainedSizeCache.class.getName()).log(Level.WARNING, Messages.RetainedSizeCache_Warning_IgnoreError.pattern, e);
            }
        }
    }

    private void doRead(File file, boolean readOldFormat) {
        IOException e;
        Throwable th;
        DataInputStream dataInputStream = null;
        try {
            this.id2size = new HashMapIntLong(((int) file.length()) / 8);
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            while (in.available() > 0) {
                try {
                    int key = in.readInt();
                    long value = in.readLong();
                    if (value < 0 && readOldFormat) {
                        value = -(value - -9223372036854775807L);
                    }
                    this.id2size.put(key, value);
                } catch (IOException e2) {
                    e = e2;
                    dataInputStream = in;
                } catch (Throwable th2) {
                    th = th2;
                    dataInputStream = in;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
            }
            if (null != null) {
                try {
                    file.delete();
                } catch (RuntimeException e4) {
                    dataInputStream = in;
                    return;
                }
            }
            dataInputStream = in;
        } catch (IOException e5) {
            e = e5;
            try {
                Logger.getLogger(RetainedSizeCache.class.getName()).log(Level.WARNING, Messages.RetainedSizeCache_ErrorReadingRetainedSizes.pattern, e);
                this.id2size.clear();
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e6) {
                    }
                }
                if (true) {
                    try {
                        file.delete();
                    } catch (RuntimeException e7) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e8) {
                    }
                }
                if (null != null) {
                    try {
                        file.delete();
                    } catch (RuntimeException e9) {
                    }
                }
                throw th;
            }
        }
    }

    private void readId2Size(String prefix) {
        File file = new File(this.filename);
        if (file.exists()) {
            doRead(file, false);
            return;
        }
        File legacyFile = new File(prefix + "i2s.index");
        if (legacyFile.exists()) {
            doRead(legacyFile, true);
        } else {
            this.id2size = new HashMapIntLong();
        }
    }
}
