package com.qiniu.android.storage;

import com.boohee.widgets.PathListView;
import com.qiniu.android.http.Client;
import com.qiniu.android.http.CompletionHandler;
import com.qiniu.android.http.ProgressHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.utils.Crc32;
import com.qiniu.android.utils.StringMap;
import com.qiniu.android.utils.StringUtils;
import com.qiniu.android.utils.UrlSafeBase64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class ResumeUploader implements Runnable {
    private final byte[]              chunkBuffer;
    private final Client              client;
    private final UpCompletionHandler completionHandler;
    private final Configuration       config;
    private final String[]            contexts;
    private       long                crc32;
    private       File                f;
    private RandomAccessFile file = null;
    private final StringMap     headers;
    private final String        key;
    private final long          modifyTime;
    private final UploadOptions options;
    private final String        recorderKey;
    private final int           size;
    private       UpToken       token;

    ResumeUploader(Client client, Configuration config, File f, String key, UpToken token, final
    UpCompletionHandler completionHandler, UploadOptions options, String recorderKey) {
        this.client = client;
        this.config = config;
        this.f = f;
        this.recorderKey = recorderKey;
        this.size = (int) f.length();
        this.key = key;
        this.headers = new StringMap().put("Authorization", "UpToken " + token.token);
        this.completionHandler = new UpCompletionHandler() {
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (ResumeUploader.this.file != null) {
                    try {
                        ResumeUploader.this.file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                completionHandler.complete(key, info, response);
            }
        };
        if (options == null) {
            options = UploadOptions.defaultOptions();
        }
        this.options = options;
        this.chunkBuffer = new byte[config.chunkSize];
        this.contexts = new String[((int) ((long) (((this.size + Configuration.BLOCK_SIZE) - 1) /
                Configuration.BLOCK_SIZE)))];
        this.modifyTime = f.lastModified();
        this.token = token;
    }

    public void run() {
        int offset = recoveryFromRecord();
        try {
            this.file = new RandomAccessFile(this.f, "r");
            nextTask(offset, 0, this.config.up.address);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e), null);
        }
    }

    private void makeBlock(URI address, int offset, int blockSize, int chunkSize, ProgressHandler
            progress, CompletionHandler _completionHandler, UpCancellationSignal c) {
        String path = String.format(Locale.ENGLISH, "/mkblk/%d", new Object[]{Integer.valueOf
                (blockSize)});
        try {
            this.file.seek((long) offset);
            this.file.read(this.chunkBuffer, 0, chunkSize);
            this.crc32 = Crc32.bytes(this.chunkBuffer, 0, chunkSize);
            post(newURI(address, path), this.chunkBuffer, 0, chunkSize, progress,
                    _completionHandler, c);
        } catch (IOException e) {
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e), null);
        }
    }

    private void putChunk(URI address, int offset, int chunkSize, String context, ProgressHandler
            progress, CompletionHandler _completionHandler, UpCancellationSignal c) {
        int chunkOffset = offset % Configuration.BLOCK_SIZE;
        String path = String.format(Locale.ENGLISH, "/bput/%s/%d", new Object[]{context, Integer
                .valueOf(chunkOffset)});
        try {
            this.file.seek((long) offset);
            this.file.read(this.chunkBuffer, 0, chunkSize);
            this.crc32 = Crc32.bytes(this.chunkBuffer, 0, chunkSize);
            post(newURI(address, path), this.chunkBuffer, 0, chunkSize, progress,
                    _completionHandler, c);
        } catch (IOException e) {
            this.completionHandler.complete(this.key, ResponseInfo.fileError(e), null);
        }
    }

    private void makeFile(URI uri, CompletionHandler _completionHandler, UpCancellationSignal c) {
        String mime = String.format(Locale.ENGLISH, "/mimeType/%s/fname/%s", new
                Object[]{UrlSafeBase64.encodeToString(this.options.mimeType), UrlSafeBase64
                .encodeToString(this.f.getName())});
        String keyStr = "";
        if (this.key != null) {
            keyStr = String.format("/key/%s", new Object[]{UrlSafeBase64.encodeToString(this.key)});
        }
        String paramStr = "";
        if (this.options.params.size() != 0) {
            String[] str = new String[this.options.params.size()];
            int j = 0;
            for (Entry<String, String> i : this.options.params.entrySet()) {
                int j2 = j + 1;
                str[j] = String.format(Locale.ENGLISH, "%s/%s", new Object[]{i.getKey(),
                        UrlSafeBase64.encodeToString((String) i.getValue())});
                j = j2;
            }
            paramStr = "/" + StringUtils.join(str, "/");
        }
        URI address = uri;
        try {
            address = new URI(uri.getScheme(), uri.getHost(), String.format(Locale.ENGLISH,
                    "/mkfile/%d%s%s%s", new Object[]{Integer.valueOf(this.size), mime, keyStr,
                            paramStr}), null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        byte[] data = StringUtils.join(this.contexts, ",").getBytes();
        post(address, data, 0, data.length, null, _completionHandler, c);
    }

    private void post(URI uri, byte[] data, int offset, int size, ProgressHandler progress,
                      CompletionHandler completion, UpCancellationSignal c) {
        this.client.asyncPost(uri.toString(), data, offset, size, this.headers, progress,
                completion, c);
    }

    private int calcPutSize(int offset) {
        int left = this.size - offset;
        return left < this.config.chunkSize ? left : this.config.chunkSize;
    }

    private int calcBlockSize(int offset) {
        int left = this.size - offset;
        return left < Configuration.BLOCK_SIZE ? left : Configuration.BLOCK_SIZE;
    }

    private boolean isCancelled() {
        return this.options.cancellationSignal.isCancelled();
    }

    private void nextTask(int offset, int retried, URI address) {
        if (isCancelled()) {
            this.completionHandler.complete(this.key, ResponseInfo.cancelled(), null);
        } else if (offset == this.size) {
            r1 = retried;
            final int i = offset;
            makeFile(address, new CompletionHandler() {
                public void complete(ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        ResumeUploader.this.removeRecord();
                        ResumeUploader.this.options.progressHandler.progress(ResumeUploader.this
                                .key, PathListView.NO_ZOOM);
                        ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key,
                                info, response);
                    } else if ((ResumeUploader.this.isNotQiniu(info) || info.needRetry()) && r1 <
                            ResumeUploader.this.config.retryMax) {
                        ResumeUploader.this.nextTask(i, r1 + 1, ResumeUploader.this.config
                                .upBackup.address);
                    } else {
                        ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key,
                                info, response);
                    }
                }
            }, this.options.cancellationSignal);
        } else {
            final int chunkSize = calcPutSize(offset);
            r1 = offset;
            ProgressHandler progress = new ProgressHandler() {
                public void onProgress(int bytesWritten, int totalSize) {
                    double percent = ((double) (r1 + bytesWritten)) / ((double) ResumeUploader
                            .this.size);
                    if (percent > 0.95d) {
                        percent = 0.95d;
                    }
                    ResumeUploader.this.options.progressHandler.progress(ResumeUploader.this.key,
                            percent);
                }
            };
            final int i2 = retried;
            final int i3 = offset;
            final URI uri = address;
            CompletionHandler complete = new CompletionHandler() {
                public void complete(ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        String context = null;
                        if (response != null || i2 >= ResumeUploader.this.config.retryMax) {
                            long crc = 0;
                            try {
                                context = response.getString("ctx");
                                crc = response.getLong("crc32");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if ((context == null || crc != ResumeUploader.this.crc32) && i2 <
                                    ResumeUploader.this.config.retryMax) {
                                ResumeUploader.this.nextTask(i3, i2 + 1, ResumeUploader.this
                                        .config.upBackup.address);
                                return;
                            }
                            ResumeUploader.this.contexts[i3 / Configuration.BLOCK_SIZE] = context;
                            ResumeUploader.this.record(i3 + chunkSize);
                            ResumeUploader.this.nextTask(i3 + chunkSize, i2, uri);
                            return;
                        }
                        ResumeUploader.this.nextTask(i3, i2 + 1, ResumeUploader.this.config
                                .upBackup.address);
                    } else if (info.statusCode == 701 && i2 < ResumeUploader.this.config.retryMax) {
                        ResumeUploader.this.nextTask((i3 / Configuration.BLOCK_SIZE) *
                                Configuration.BLOCK_SIZE, i2 + 1, uri);
                    } else if ((ResumeUploader.this.isNotQiniu(info) || info.needRetry()) && i2 <
                            ResumeUploader.this.config.retryMax) {
                        ResumeUploader.this.nextTask(i3, i2 + 1, ResumeUploader.this.config
                                .upBackup.address);
                    } else {
                        ResumeUploader.this.completionHandler.complete(ResumeUploader.this.key,
                                info, response);
                    }
                }
            };
            if (offset % Configuration.BLOCK_SIZE == 0) {
                makeBlock(address, offset, calcBlockSize(offset), chunkSize, progress, complete,
                        this.options.cancellationSignal);
                return;
            }
            putChunk(address, offset, chunkSize, this.contexts[offset / Configuration
                    .BLOCK_SIZE], progress, complete, this.options.cancellationSignal);
        }
    }

    private int recoveryFromRecord() {
        if (this.config.recorder == null) {
            return 0;
        }
        byte[] data = this.config.recorder.get(this.recorderKey);
        if (data == null) {
            return 0;
        }
        try {
            JSONObject obj = new JSONObject(new String(data));
            int offset = obj.optInt("offset", 0);
            long modify = obj.optLong("modify_time", 0);
            int fSize = obj.optInt("size", 0);
            JSONArray array = obj.optJSONArray("contexts");
            if (offset == 0 || modify != this.modifyTime || fSize != this.size || array == null
                    || array.length() == 0) {
                return 0;
            }
            for (int i = 0; i < array.length(); i++) {
                this.contexts[i] = array.optString(i);
            }
            return offset;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void removeRecord() {
        if (this.config.recorder != null) {
            this.config.recorder.del(this.recorderKey);
        }
    }

    private void record(int offset) {
        if (this.config.recorder != null && offset != 0) {
            this.config.recorder.set(this.recorderKey, String.format(Locale.ENGLISH,
                    "{\"size\":%d,\"offset\":%d, \"modify_time\":%d, \"contexts\":[%s]}", new
                            Object[]{Integer.valueOf(this.size), Integer.valueOf(offset), Long
                            .valueOf(this.modifyTime), StringUtils.jsonJoin(this.contexts)})
                    .getBytes());
        }
    }

    private boolean isNotQiniu(ResponseInfo info) {
        return info.isNotQiniu() && !this.token.hasReturnUrl();
    }

    private URI newURI(URI uri, String path) {
        try {
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), path, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return uri;
        }
    }
}
