package com.aspsine.multithreaddownload;

import java.io.File;

public class DownloadInfo {
    private File dir;
    private long finished;
    private long length;
    private String name;
    private int progress;
    private String uri;

    public DownloadInfo(String name, String uri, File dir) {
        this.name = name;
        this.uri = uri;
        this.dir = dir;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getDir() {
        return this.dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
