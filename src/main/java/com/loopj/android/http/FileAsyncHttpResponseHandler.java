package com.loopj.android.http;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public abstract class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
    static final /* synthetic */ boolean $assertionsDisabled = (!FileAsyncHttpResponseHandler
            .class.desiredAssertionStatus());
    private static final         String  LOG_TAG             = "FileAsyncHttpResponseHandler";
    protected final boolean append;
    protected final File    mFile;

    public abstract void onFailure(int i, Header[] headerArr, Throwable th, File file);

    public abstract void onSuccess(int i, Header[] headerArr, File file);

    public FileAsyncHttpResponseHandler(File file) {
        this(file, false);
    }

    public FileAsyncHttpResponseHandler(File file, boolean append) {
        boolean z = true;
        Utils.asserts(file != null, "File passed into FileAsyncHttpResponseHandler constructor " +
                "must not be null");
        if (file.isDirectory()) {
            z = false;
        }
        Utils.asserts(z, "File passed into FileAsyncHttpResponseHandler constructor must not " +
                "point to directory");
        if (!file.getParentFile().isDirectory()) {
            Utils.asserts(file.getParentFile().mkdirs(), "Cannot create parent directories for " +
                    "requested File location");
        }
        this.mFile = file;
        this.append = append;
    }

    public FileAsyncHttpResponseHandler(Context context) {
        this.mFile = getTemporaryFile(context);
        this.append = false;
    }

    public boolean deleteTargetFile() {
        return getTargetFile() != null && getTargetFile().delete();
    }

    protected File getTemporaryFile(Context context) {
        Utils.asserts(context != null, "Tried creating temporary file without having Context");
        try {
            if ($assertionsDisabled || context != null) {
                return File.createTempFile("temp_", "_handled", context.getCacheDir());
            }
            throw new AssertionError();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot create temporary file", e);
            return null;
        }
    }

    protected File getTargetFile() {
        if ($assertionsDisabled || this.mFile != null) {
            return this.mFile;
        }
        throw new AssertionError();
    }

    public final void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable
            throwable) {
        onFailure(statusCode, headers, throwable, getTargetFile());
    }

    public final void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
        onSuccess(statusCode, headers, getTargetFile());
    }

    protected byte[] getResponseData(HttpEntity entity) throws IOException {
        if (entity != null) {
            InputStream instream = entity.getContent();
            long contentLength = entity.getContentLength();
            FileOutputStream buffer = new FileOutputStream(getTargetFile(), this.append);
            if (instream != null) {
                try {
                    byte[] tmp = new byte[4096];
                    int count = 0;
                    while (true) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            AsyncHttpClient.silentCloseInputStream(instream);
                            buffer.flush();
                            AsyncHttpClient.silentCloseOutputStream(buffer);
                        } else {
                            count += l;
                            buffer.write(tmp, 0, l);
                            sendProgressMessage((long) count, contentLength);
                        }
                    }
                    AsyncHttpClient.silentCloseInputStream(instream);
                    buffer.flush();
                    AsyncHttpClient.silentCloseOutputStream(buffer);
                } catch (Throwable th) {
                    AsyncHttpClient.silentCloseInputStream(instream);
                    buffer.flush();
                    AsyncHttpClient.silentCloseOutputStream(buffer);
                }
            }
        }
        return null;
    }
}
