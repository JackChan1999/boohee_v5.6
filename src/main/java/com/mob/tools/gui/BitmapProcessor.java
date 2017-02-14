package com.mob.tools.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.mob.tools.network.NetworkHelper;
import com.mob.tools.network.RawNetworkCallback;
import com.mob.tools.utils.BitmapHelper;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.Ln;
import com.mob.tools.utils.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.WeakHashMap;

public class BitmapProcessor {
    private static final int CAPACITY      = 3;
    private static final int MAX_REQ_TIME  = 200;
    private static final int MAX_SIZE      = 40;
    private static final int OVERFLOW_SIZE = 50;
    private static BitmapProcessor instance;
    private        File            cacheDir;
    private WeakHashMap<String, Bitmap> cacheMap = new WeakHashMap();
    private ManagerThread manager;
    private Vector<ImageReq> netReqTPS = new Vector();
    private Vector<ImageReq> reqList   = new Vector();
    private boolean work;
    private WorkerThread[] workerList = new WorkerThread[3];

    public interface BitmapCallback {
        void onImageGot(String str, Bitmap bitmap);
    }

    public static class ImageReq {
        private BitmapCallback callback;
        private Bitmap         image;
        private long reqTime = System.currentTimeMillis();
        private String       url;
        private WorkerThread worker;

        private void throwComplete(Bitmap bitmap) {
            this.image = bitmap;
            if (this.callback != null) {
                this.callback.onImageGot(this.url, this.image);
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("url=").append(this.url);
            stringBuilder.append("time=").append(this.reqTime);
            stringBuilder.append("worker=").append(this.worker.getName()).append(" (").append
                    (this.worker.getId()).append("");
            return stringBuilder.toString();
        }
    }

    private static class ManagerThread extends Timer {
        private BitmapProcessor processor;

        public ManagerThread(BitmapProcessor bitmapProcessor) {
            this.processor = bitmapProcessor;
            schedule(new TimerTask() {
                private int counter;

                public void run() {
                    if (ManagerThread.this.processor.work) {
                        this.counter--;
                        if (this.counter <= 0) {
                            this.counter = 100;
                            ManagerThread.this.scan();
                        }
                    }
                }
            }, 0, 200);
        }

        private void scan() {
            if (this.processor.work) {
                long currentTimeMillis = System.currentTimeMillis();
                int i = 0;
                while (i < this.processor.workerList.length) {
                    if (this.processor.workerList[i] == null) {
                        this.processor.workerList[i] = new WorkerThread(this.processor);
                        this.processor.workerList[i].setName("worker " + i);
                        this.processor.workerList[i].localType = i == 0;
                        this.processor.workerList[i].start();
                    } else if (currentTimeMillis - this.processor.workerList[i].lastReport >
                            20000) {
                        this.processor.workerList[i].interrupt();
                        boolean access$600 = this.processor.workerList[i].localType;
                        this.processor.workerList[i] = new WorkerThread(this.processor);
                        this.processor.workerList[i].setName("worker " + i);
                        this.processor.workerList[i].localType = access$600;
                        this.processor.workerList[i].start();
                    }
                    i++;
                }
            }
        }
    }

    private static class PatchInputStream extends FilterInputStream {
        InputStream in;

        protected PatchInputStream(InputStream inputStream) {
            super(inputStream);
            this.in = inputStream;
        }

        public long skip(long j) throws IOException {
            long j2 = 0;
            while (j2 < j) {
                long skip = this.in.skip(j - j2);
                if (skip == 0) {
                    break;
                }
                j2 += skip;
            }
            return j2;
        }
    }

    private static class WorkerThread extends Thread {
        private ImageReq curReq;
        private long lastReport = System.currentTimeMillis();
        private boolean         localType;
        private BitmapProcessor processor;

        public WorkerThread(BitmapProcessor bitmapProcessor) {
            this.processor = bitmapProcessor;
        }

        private void doLocalTask() throws Throwable {
            int size = this.processor.reqList.size();
            ImageReq imageReq = size > 0 ? (ImageReq) this.processor.reqList.remove(size - 1) :
                    null;
            if (imageReq != null) {
                Bitmap bitmap = (Bitmap) this.processor.cacheMap.get(imageReq.url);
                if (bitmap != null) {
                    this.curReq = imageReq;
                    this.curReq.worker = this;
                    imageReq.throwComplete(bitmap);
                } else if (new File(this.processor.cacheDir, Data.MD5(imageReq.url)).exists()) {
                    doTask(imageReq);
                    this.lastReport = System.currentTimeMillis();
                    return;
                } else {
                    if (this.processor.netReqTPS.size() > 40) {
                        while (this.processor.reqList.size() > 0) {
                            this.processor.reqList.remove(0);
                        }
                        this.processor.netReqTPS.remove(0);
                    }
                    this.processor.netReqTPS.add(imageReq);
                }
                this.lastReport = System.currentTimeMillis();
                return;
            }
            this.lastReport = System.currentTimeMillis();
            Thread.sleep(30);
        }

        private void doNetworkTask() throws Throwable {
            ImageReq imageReq;
            Bitmap bitmap;
            ImageReq imageReq2 = null;
            if (this.processor.netReqTPS.size() > 0) {
                imageReq2 = (ImageReq) this.processor.netReqTPS.remove(0);
            }
            if (imageReq2 == null) {
                int size = this.processor.reqList.size();
                if (size > 0) {
                    imageReq = (ImageReq) this.processor.reqList.remove(size - 1);
                    if (imageReq == null) {
                        bitmap = (Bitmap) this.processor.cacheMap.get(imageReq.url);
                        if (bitmap == null) {
                            this.curReq = imageReq;
                            this.curReq.worker = this;
                            imageReq.throwComplete(bitmap);
                        } else {
                            doTask(imageReq);
                        }
                        this.lastReport = System.currentTimeMillis();
                    }
                    this.lastReport = System.currentTimeMillis();
                    Thread.sleep(30);
                    return;
                }
            }
            imageReq = imageReq2;
            if (imageReq == null) {
                this.lastReport = System.currentTimeMillis();
                Thread.sleep(30);
                return;
            }
            bitmap = (Bitmap) this.processor.cacheMap.get(imageReq.url);
            if (bitmap == null) {
                doTask(imageReq);
            } else {
                this.curReq = imageReq;
                this.curReq.worker = this;
                imageReq.throwComplete(bitmap);
            }
            this.lastReport = System.currentTimeMillis();
        }

        private void doTask(final ImageReq imageReq) throws Throwable {
            Bitmap bitmap;
            this.curReq = imageReq;
            this.curReq.worker = this;
            final boolean z = imageReq.url.toLowerCase().endsWith("png") || imageReq.url
                    .toLowerCase().endsWith("gif");
            final File file = new File(this.processor.cacheDir, Data.MD5(imageReq.url));
            if (file.exists()) {
                bitmap = BitmapHelper.getBitmap(file.getAbsolutePath());
                if (bitmap != null) {
                    this.processor.cacheMap.put(imageReq.url, bitmap);
                    imageReq.throwComplete(bitmap);
                }
                this.curReq = null;
            } else {
                new NetworkHelper().rawGet(imageReq.url, new RawNetworkCallback() {
                    public void onResponse(InputStream inputStream) throws Throwable {
                        Bitmap bitmap = BitmapHelper.getBitmap(new PatchInputStream(inputStream),
                                1);
                        if (bitmap == null || bitmap.isRecycled()) {
                            WorkerThread.this.curReq = null;
                            return;
                        }
                        WorkerThread.this.saveFile(bitmap, file, z);
                        if (bitmap != null) {
                            WorkerThread.this.processor.cacheMap.put(imageReq.url, bitmap);
                            imageReq.throwComplete(bitmap);
                        }
                        WorkerThread.this.curReq = null;
                    }
                });
                bitmap = null;
            }
            if (bitmap != null) {
                this.processor.cacheMap.put(imageReq.url, bitmap);
                imageReq.throwComplete(bitmap);
            }
            this.curReq = null;
        }

        private void saveFile(Bitmap bitmap, File file, boolean z) {
            try {
                if (file.exists()) {
                    file.delete();
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                CompressFormat compressFormat = z ? CompressFormat.PNG : CompressFormat.JPEG;
                OutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(compressFormat, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Throwable th) {
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        public void interrupt() {
            try {
                super.interrupt();
            } catch (Throwable th) {
            }
        }

        public void run() {
            while (this.processor.work) {
                try {
                    if (this.localType) {
                        doLocalTask();
                    } else {
                        doNetworkTask();
                    }
                } catch (Throwable th) {
                    Ln.w(th);
                }
            }
        }
    }

    private BitmapProcessor(Context context) {
        this.cacheDir = new File(R.getImageCachePath(context));
        this.manager = new ManagerThread(this);
    }

    public static Bitmap getBitmapFromCache(String str) {
        if (instance != null) {
            return (Bitmap) instance.cacheMap.get(str);
        }
        throw new RuntimeException("Call BitmapProcessor.prepare(String) before start");
    }

    public static synchronized void prepare(Context context) {
        synchronized (BitmapProcessor.class) {
            if (instance == null) {
                instance = new BitmapProcessor(context.getApplicationContext());
            }
        }
    }

    public static void process(String str, BitmapCallback bitmapCallback) {
        if (instance == null) {
            throw new RuntimeException("Call BitmapProcessor.prepare(String) before start");
        } else if (str != null) {
            ImageReq imageReq = new ImageReq();
            imageReq.url = str;
            imageReq.callback = bitmapCallback;
            instance.reqList.add(imageReq);
            if (instance.reqList.size() > 50) {
                while (instance.reqList.size() > 40) {
                    instance.reqList.remove(0);
                }
            }
            start();
        }
    }

    public static void start() {
        if (instance == null) {
            throw new RuntimeException("Call BitmapProcessor.prepare(String) before start");
        }
        instance.work = true;
    }

    public static void stop() {
        int i = 0;
        if (instance != null) {
            instance.work = false;
            instance.reqList.clear();
            instance.manager.cancel();
            while (i < instance.workerList.length) {
                if (instance.workerList[i] != null) {
                    instance.workerList[i].interrupt();
                }
                i++;
            }
            instance = null;
        }
    }
}
