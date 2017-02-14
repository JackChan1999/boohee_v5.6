package com.qiniu.android.storage;

import com.qiniu.android.http.Client;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration.Builder;
import com.qiniu.android.utils.AsyncRun;

import java.io.File;

public final class UploadManager {
    private final Client        client;
    private final Configuration config;

    public UploadManager() {
        this(new Builder().build());
    }

    public UploadManager(Configuration config) {
        this.config = config;
        this.client = new Client(config.proxy, config.connectTimeout, config.responseTimeout,
                config.urlConverter, config.dns);
    }

    public UploadManager(Recorder recorder, KeyGenerator keyGen) {
        this(new Builder().recorder(recorder, keyGen).build());
    }

    public UploadManager(Recorder recorder) {
        this(recorder, null);
    }

    private static boolean areInvalidArg(String key, byte[] data, File f, String token,
                                         UpCompletionHandler completionHandler) {
        if (completionHandler == null) {
            throw new IllegalArgumentException("no UpCompletionHandler");
        }
        String message = null;
        if (f == null && data == null) {
            message = "no input data";
        } else if (token == null || token.equals("")) {
            message = "no token";
        }
        ResponseInfo info = null;
        if (message != null) {
            info = ResponseInfo.invalidArgument(message);
        }
        if ((f != null && f.length() == 0) || (data != null && data.length == 0)) {
            info = ResponseInfo.zeroSize();
        }
        if (info == null) {
            return false;
        }
        AsyncRun.run(new 1 (completionHandler, key, info));
        return true;
    }

    public void put(byte[] data, String key, String token, UpCompletionHandler completionHandler,
                    UploadOptions options) {
        if (!areInvalidArg(key, data, null, token, completionHandler)) {
            UpToken decodedToken = UpToken.parse(token);
            if (decodedToken == null) {
                AsyncRun.run(new 2
                (this, completionHandler, key, ResponseInfo.invalidToken("invalid token")));
            } else {
                AsyncRun.run(new 3 (this, data, key, decodedToken, completionHandler, options));
            }
        }
    }

    public void put(String filePath, String key, String token, UpCompletionHandler
            completionHandler, UploadOptions options) {
        put(new File(filePath), key, token, completionHandler, options);
    }

    public void put(File file, String key, String token, UpCompletionHandler completionHandler,
                    UploadOptions options) {
        if (!areInvalidArg(key, null, file, token, completionHandler)) {
            UpToken decodedToken = UpToken.parse(token);
            if (decodedToken == null) {
                AsyncRun.run(new 4
                (this, completionHandler, key, ResponseInfo.invalidToken("invalid token")));
            } else if (file.length() <= ((long) this.config.putThreshold)) {
                FormUploader.upload(this.client, this.config, file, key, decodedToken,
                        completionHandler, options);
            } else {
                AsyncRun.run(new ResumeUploader(this.client, this.config, file, key,
                        decodedToken, completionHandler, options, this.config.keyGen.gen(key, file)));
            }
        }
    }
}
