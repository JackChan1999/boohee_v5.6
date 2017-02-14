package com.boohee.one.video.download;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boohee.one.video.entity.Mention;
import com.boohee.utils.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

public class VideoDownloadHelper {
    public static final  String   AUDIO_DOWNLOAD_PATH = "/.AUDIOS/";
    public static final  String   AUDIO_FORMAT        = ".mp3";
    public static final  String   AUDIO_NAME          = "Audio_";
    public static final  char[]   RMB_NUMS            = "0123456789".toCharArray();
    public static final  String   TAG                 = "VideoDownloadHelper";
    private static final String[] U1                  = new String[]{"", "unit_ten",
            "unit_hundred", "unit_thousand"};
    public static final  String   VIDEO_DOWNLOAD_PATH = "/.VIDEOS/";
    public static final  String   VIDEO_FORMAT        = ".mp4";
    public static final  String   VIDEO_NAME          = "Video_";
    private static AsyncHttpClient client;
    private        boolean         downloadAudioSuccess;
    private        boolean         downloadVideoSuccess;
    private        int             index;
    private        int             totalSize;

    public interface OnDownloadListener {
        void onDownloadFail();

        void onDownloadFinish();
    }

    private static class VideoDownloadHelperHolder {
        private static final VideoDownloadHelper INSTANCE = new VideoDownloadHelper();

        private VideoDownloadHelperHolder() {
        }
    }

    public static VideoDownloadHelper getInstance() {
        return VideoDownloadHelperHolder.INSTANCE;
    }

    private VideoDownloadHelper() {
        this.index = 0;
        this.totalSize = 0;
        this.downloadVideoSuccess = false;
        this.downloadAudioSuccess = false;
    }

    public AsyncHttpClient getClient() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }

    public String getVideoDownloadPath(Context context) {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return "";
        }
        String path = context.getExternalFilesDir(null) + VIDEO_DOWNLOAD_PATH;
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        file.mkdirs();
        return path;
    }

    public String getVideoName(Context context, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getVideoDownloadPath(context));
        stringBuilder.append(VIDEO_NAME);
        stringBuilder.append(id);
        stringBuilder.append(".mp4");
        return stringBuilder.toString();
    }

    public String getAudioDownloadPath(Context context) {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return "";
        }
        String path = context.getExternalFilesDir(null) + AUDIO_DOWNLOAD_PATH;
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        file.mkdirs();
        return path;
    }

    public String getAudioName(Context context, int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getAudioDownloadPath(context));
        stringBuilder.append(AUDIO_NAME);
        stringBuilder.append(id);
        stringBuilder.append(AUDIO_FORMAT);
        return stringBuilder.toString();
    }

    public void downloadFiles(Context context, List<Mention> list, ProgressBar progressBar,
                              OnDownloadListener onDownloadListener) {
        if (context != null && list != null && list.size() != 0) {
            this.totalSize = list.size();
            if (this.index < this.totalSize) {
                final Context context2;
                final List<Mention> list2;
                final OnDownloadListener onDownloadListener2;
                final ProgressBar progressBar2;
                Helper.showLog(TAG, "downloadFiles index : " + this.index + " id : " + ((Mention)
                        list.get(this.index)).id);
                if (checkVideoDownload(context, ((Mention) list.get(this.index)).id)) {
                    progressBar.setProgress((this.index * 100) / list.size());
                    this.downloadVideoSuccess = true;
                    isDownloadFinish(context, list, progressBar, onDownloadListener);
                } else {
                    Helper.showLog(TAG, "downloadVideo");
                    context2 = context;
                    list2 = list;
                    onDownloadListener2 = onDownloadListener;
                    progressBar2 = progressBar;
                    getClient().get(context, ((Mention) list.get(this.index)).video_url, new
                            FileAsyncHttpResponseHandler(context) {
                        public void onFailure(int i, Header[] headers, Throwable throwable, File
                                file) {
                            VideoDownloadHelper.this.downloadFileFailure(context2, ((Mention)
                                    list2.get(VideoDownloadHelper.this.index)).id, file);
                            if (onDownloadListener2 != null) {
                                onDownloadListener2.onDownloadFail();
                            }
                        }

                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            progressBar2.setProgress(((int) (((long) (VideoDownloadHelper.this
                                    .index * 100)) + ((100 * bytesWritten) / totalSize))) / list2
                                    .size());
                        }

                        public void onSuccess(int i, Header[] headers, File file) {
                            Helper.showLog(VideoDownloadHelper.TAG, "downloadVideo success");
                            VideoDownloadHelper.this.downloadVideoSuccess = true;
                            VideoDownloadHelper.this.copyFile(file, new File(VideoDownloadHelper
                                    .this.getVideoName(context2, ((Mention) list2.get
                                    (VideoDownloadHelper.this.index)).id)));
                            VideoDownloadHelper.this.isDownloadFinish(context2, list2,
                                    progressBar2, onDownloadListener2);
                        }
                    });
                }
                if (checkAudioDownload(context, ((Mention) list.get(this.index)).id)) {
                    this.downloadAudioSuccess = true;
                    isDownloadFinish(context, list, progressBar, onDownloadListener);
                    return;
                }
                Helper.showLog(TAG, "downloadAudio");
                context2 = context;
                list2 = list;
                onDownloadListener2 = onDownloadListener;
                progressBar2 = progressBar;
                getClient().get(context, ((Mention) list.get(this.index)).audio_url, new
                        FileAsyncHttpResponseHandler(context) {
                    public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                        VideoDownloadHelper.this.downloadFileFailure(context2, ((Mention) list2
                                .get(VideoDownloadHelper.this.index)).id, file);
                        if (onDownloadListener2 == null) {
                        }
                    }

                    public void onProgress(long bytesWritten, long totalSize) {
                        super.onProgress(bytesWritten, totalSize);
                    }

                    public void onSuccess(int i, Header[] headers, File file) {
                        Helper.showLog(VideoDownloadHelper.TAG, "downloadAudio success");
                        VideoDownloadHelper.this.downloadAudioSuccess = true;
                        VideoDownloadHelper.this.copyFile(file, new File(VideoDownloadHelper.this
                                .getAudioName(context2, ((Mention) list2.get(VideoDownloadHelper
                                        .this.index)).id)));
                        VideoDownloadHelper.this.isDownloadFinish(context2, list2, progressBar2,
                                onDownloadListener2);
                    }
                });
            }
        }
    }

    public void downloadFileFailure(Context context, int id, File file) {
        this.downloadVideoSuccess = false;
        this.downloadAudioSuccess = false;
        if (file != null) {
            file.delete();
        }
        File video = new File(getVideoName(context, id));
        if (video.exists()) {
            video.delete();
        }
        File audio = new File(getAudioName(context, id));
        if (audio.exists()) {
            audio.delete();
        }
    }

    private void isDownloadFinish(Context context, List<Mention> list, ProgressBar progressBar,
                                  OnDownloadListener onDownloadListener) {
        Helper.showLog(TAG, "isDownloadFinish downloadAudioSuccess : " + this
                .downloadAudioSuccess + " downloadVideoSuccess : " + this.downloadVideoSuccess);
        if (this.downloadAudioSuccess && this.downloadVideoSuccess) {
            this.downloadAudioSuccess = false;
            this.downloadVideoSuccess = false;
            this.index++;
            if (this.index >= this.totalSize) {
                this.index = 0;
                this.totalSize = 0;
                if (onDownloadListener != null) {
                    onDownloadListener.onDownloadFinish();
                    return;
                }
                return;
            }
            downloadFiles(context, list, progressBar, onDownloadListener);
        }
    }

    public void downloadVideo(String url, FileAsyncHttpResponseHandler handler) {
        getClient().get(url, handler);
    }

    public void downloadAudio(String url, FileAsyncHttpResponseHandler handler) {
        getClient().get(url, handler);
    }

    public boolean checkAudioDownload(Context context, int id) {
        return checkDownload(getAudioName(context, id));
    }

    public boolean checkVideoDownload(Context context, int id) {
        return checkDownload(getVideoName(context, id));
    }

    public boolean checkDownload(String path) {
        File file = new File(path);
        boolean isDownloaded = file != null && file.exists() && file.length() > 0;
        Helper.showLog(TAG, "checkDownload path : " + path + " isDownloaded : " + isDownloaded);
        return isDownloaded;
    }

    public void copyFile(File source, File target) {
        Exception e;
        Throwable th;
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            FileInputStream inputStream2 = new FileInputStream(source);
            try {
                FileOutputStream outputStream2 = new FileOutputStream(target);
                try {
                    in = inputStream2.getChannel();
                    out = outputStream2.getChannel();
                    in.transferTo(0, in.size(), out);
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            outputStream = outputStream2;
                            inputStream = inputStream2;
                            return;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            outputStream = outputStream2;
                            inputStream = inputStream2;
                            return;
                        }
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    if (outputStream2 != null) {
                        outputStream2.close();
                    }
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                } catch (Exception e4) {
                    e3 = e4;
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                    try {
                        e3.printStackTrace();
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                                return;
                            } catch (Exception e32) {
                                e32.printStackTrace();
                                return;
                            }
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream == null) {
                            outputStream.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                                throw th;
                            } catch (Exception e322) {
                                e322.printStackTrace();
                                throw th;
                            }
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = outputStream2;
                    inputStream = inputStream2;
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e322 = e5;
                inputStream = inputStream2;
                e322.printStackTrace();
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream == null) {
                    outputStream.close();
                }
            } catch (Throwable th4) {
                th = th4;
                inputStream = inputStream2;
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e322 = e6;
            e322.printStackTrace();
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream == null) {
                outputStream.close();
            }
        }
    }

    public String getDownloadFileSize(Header[] headers) {
        String size = "";
        if (headers == null) {
            return size;
        }
        for (Header header : headers) {
            if (TextUtils.equals(header.getName(), "Content-Length")) {
                size = header.getValue();
            }
        }
        return size.contains("Content-Length: ") ? size.replace("Content-Length: ", "") : size;
    }

    public void isNeedShowInfoText(View layout, TextView textView, String info) {
        if (TextUtils.isEmpty(info)) {
            layout.setVisibility(8);
        } else {
            textView.setText(info);
        }
    }

    public List<String> getNumberAudioSplit(int number) {
        List<String> strings = new ArrayList();
        int wan = number / 10000;
        int qian = (number / 1000) % 10;
        int bai = (number / 100) % 10;
        int shi = (number / 10) % 10;
        int ge = number % 10;
        if (wan != 0) {
            strings.add(String.valueOf(wan));
            strings.add("unit_ten_thousand");
        }
        if (qian != 0) {
            strings.add(String.valueOf(qian));
            strings.add("unit_thousand");
        }
        if (bai != 0) {
            strings.add(String.valueOf(bai));
            strings.add("unit_hundred");
        }
        if (shi != 0) {
            strings.add(String.valueOf(shi));
            strings.add("unit_ten");
        }
        if (ge != 0) {
            strings.add(String.valueOf(ge));
        }
        return strings;
    }

    private static String getNumberSplit(String integer) {
        StringBuilder buffer = new StringBuilder();
        int i = integer.length() - 1;
        int j = 0;
        while (i >= 0) {
            char n = integer.charAt(i);
            if (n == '0') {
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(RMB_NUMS[0]);
                }
                if (j % 4 == 0 && ((i > 0 && integer.charAt(i - 1) != '0') || ((i > 1 && integer
                        .charAt(i - 2) != '0') || (i > 2 && integer.charAt(i - 3) != '0')))) {
                    buffer.append("unit_ten_thousand");
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append("unit_ten_thousand");
                }
                buffer.append(U1[j % 4]);
                buffer.append(RMB_NUMS[n - 48]);
            }
            i--;
            j++;
        }
        return buffer.reverse().toString();
    }
}
