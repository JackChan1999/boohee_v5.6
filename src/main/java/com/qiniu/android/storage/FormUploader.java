package com.qiniu.android.storage;

import com.boohee.widgets.PathListView;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.PostArgs;
import com.qiniu.android.http.ProgressHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.Crc32;
import com.qiniu.android.utils.StringMap;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.json.JSONObject;

final class FormUploader {
    FormUploader() {
    }

    static void upload(Client httpManager, Configuration config, byte[] data, String key, UpToken
            token, UpCompletionHandler completionHandler, UploadOptions options) {
        post(data, null, key, token, completionHandler, options, httpManager, config);
    }

    static void upload(Client client, Configuration config, File file, String key, UpToken token,
                       UpCompletionHandler completionHandler, UploadOptions options) {
        post(null, file, key, token, completionHandler, options, client, config);
    }

    private static void post(byte[] data, File file, String k, UpToken token, UpCompletionHandler
            completionHandler, UploadOptions optionsIn, Client client, Configuration config) {
        final String key = k;
        StringMap params = new StringMap();
        final PostArgs args = new PostArgs();
        if (k != null) {
            params.put("key", key);
            args.fileName = key;
        } else {
            args.fileName = "?";
        }
        if (file != null) {
            args.fileName = file.getName();
        }
        params.put("token", token.token);
        final UploadOptions options = optionsIn != null ? optionsIn : UploadOptions
                .defaultOptions();
        params.putFileds(options.params);
        if (options.checkCrc) {
            long crc = 0;
            if (file != null) {
                try {
                    crc = Crc32.file(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                crc = Crc32.bytes(data);
            }
            params.put("crc32", "" + crc);
        }
        final ProgressHandler progress = new ProgressHandler() {
            public void onProgress(int bytesWritten, int totalSize) {
                double percent = ((double) bytesWritten) / ((double) totalSize);
                if (percent > 0.95d) {
                    percent = 0.95d;
                }
                options.progressHandler.progress(key, percent);
            }
        };
        args.data = data;
        args.file = file;
        args.mimeType = options.mimeType;
        args.params = params;
        final UpCompletionHandler upCompletionHandler = completionHandler;
        final UpToken upToken = token;
        final Configuration configuration = config;
        final Client client2 = client;
        client.asyncMultipartPost(config.up.address.toString(), args, progress, new
                CompletionHandler() {
            public void complete(ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    options.progressHandler.progress(key, PathListView.NO_ZOOM);
                    upCompletionHandler.complete(key, info, response);
                } else if (options.cancellationSignal.isCancelled()) {
                    upCompletionHandler.complete(key, ResponseInfo.cancelled(), null);
                } else if (info.needRetry() || (info.isNotQiniu() && !upToken.hasReturnUrl())) {
                    CompletionHandler retried = new CompletionHandler() {
                        public void complete(ResponseInfo info, JSONObject response) {
                            if (info.isOK()) {
                                options.progressHandler.progress(key, PathListView.NO_ZOOM);
                            }
                            upCompletionHandler.complete(key, info, response);
                        }
                    };
                    URI u = configuration.up.address;
                    if (info.needSwitchServer() || info.isNotQiniu()) {
                        u = configuration.upBackup.address;
                    }
                    client2.asyncMultipartPost(u.toString(), args, progress, retried, options
                            .cancellationSignal);
                } else {
                    upCompletionHandler.complete(key, info, response);
                }
            }
        }, options.cancellationSignal);
    }
}
