package com.meiqia.meiqiasdk.model;

public class VoiceMessage extends BaseMessage {
    public static final int NO_DURATION = -1;
    private int    duration;
    private String localPath;
    private String url;

    public VoiceMessage() {
        this.duration = -1;
        setItemViewType(0);
        setContentType("audio");
    }

    public VoiceMessage(String url) {
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

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
