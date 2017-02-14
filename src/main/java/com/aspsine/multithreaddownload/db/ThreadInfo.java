package com.aspsine.multithreaddownload.db;

public class ThreadInfo {
    private long end;
    private long finished;
    private int id;
    private long start;
    private String tag;
    private String uri;

    public ThreadInfo(int id, String tag, String uri, long finished) {
        this.id = id;
        this.tag = tag;
        this.uri = uri;
        this.finished = finished;
    }

    public ThreadInfo(int id, String tag, String uri, long start, long end, long finished) {
        this.id = id;
        this.tag = tag;
        this.uri = uri;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getStart() {
        return this.start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
