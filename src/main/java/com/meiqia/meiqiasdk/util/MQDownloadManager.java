package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

public class MQDownloadManager {
    private static MQDownloadManager sInstance;
    private        Context           mContext;
    private OkHttpClient mOkHttpClient = new OkHttpClient();

    public interface Callback {
        void onFailure();

        void onSuccess(File file);
    }

    private MQDownloadManager(Context context) {
        this.mContext = context;
    }

    public static MQDownloadManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MQDownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new MQDownloadManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public void downloadVoice(final String url, final Callback callback) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            this.mOkHttpClient.newCall(new Builder().url(url).build()).enqueue(new com.squareup
                    .okhttp.Callback() {
                public void onFailure(Request request, IOException e) {
                    if (callback != null) {
                        callback.onFailure();
                    }
                }

                public void onResponse(Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            File voiceFile = MQAudioRecorderManager.getCachedVoiceFileByUrl
                                    (MQDownloadManager.this.mContext, url);
                            BufferedSink sink = Okio.buffer(Okio.sink(voiceFile));
                            sink.writeAll(response.body().source());
                            sink.close();
                            if (callback != null) {
                                callback.onSuccess(voiceFile);
                            }
                        } catch (IOException e) {
                            if (callback != null) {
                                callback.onFailure();
                            }
                        }
                    } else if (callback != null) {
                        callback.onFailure();
                    }
                }
            });
        } else if (callback != null) {
            callback.onFailure();
        }
    }
}
