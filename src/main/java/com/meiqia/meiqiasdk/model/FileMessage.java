package com.meiqia.meiqiasdk.model;

public class FileMessage extends BaseMessage {
    public static final int FILE_STATE_DOWNLOADING = 1;
    public static final int FILE_STATE_EXPIRED     = 4;
    public static final int FILE_STATE_FAILED      = 3;
    public static final int FILE_STATE_FINISH      = 0;
    public static final int FILE_STATE_NOT_EXIST   = 2;
    private String extra;
    private int    fileState;
    private String localPath;
    private int    progress;
    private String url;

    public FileMessage() {
        setItemViewType(0);
        setContentType("file");
        this.fileState = 2;
    }

    public FileMessage(String url) {
        this();
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getFileState() {
        return this.fileState;
    }

    public void setFileState(int fileState) {
        this.fileState = fileState;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
