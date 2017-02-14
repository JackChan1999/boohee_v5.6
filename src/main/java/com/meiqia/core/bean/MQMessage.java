package com.meiqia.core.bean;

public class MQMessage {
    public static final String STATE_ARRIVE       = "arrived";
    public static final String STATE_FAILED       = "failed";
    public static final String STATE_SENDING      = "sending";
    public static final String TYPE_AUTO_REPLY    = "auto_reply";
    public static final String TYPE_CONTENT_FILE  = "file";
    public static final String TYPE_CONTENT_PHOTO = "photo";
    public static final String TYPE_CONTENT_TEXT  = "text";
    public static final String TYPE_CONTENT_VOICE = "audio";
    public static final String TYPE_ENDING        = "ending";
    public static final String TYPE_FROM_AGENT    = "agent";
    public static final String TYPE_FROM_CLIENT   = "client";
    public static final String TYPE_INTERNAL      = "internal";
    public static final String TYPE_MESSAGE       = "message";
    public static final String TYPE_PROMOTION     = "promotion";
    public static final String TYPE_REMARK        = "remark";
    public static final String TYPE_REPLY         = "reply";
    public static final String TYPE_WELCOME       = "welcome";
    private String  agent_id;
    private String  agent_nickname;
    private String  avatar;
    private String  content;
    private String  content_type;
    private long    conversation_id;
    private long    created_on;
    private long    enterprise_id;
    private String  extra;
    private String  from_type;
    private long    id;
    private boolean is_read;
    private String  media_url;
    private String  status;
    private String  track_id;
    private String  type;

    public MQMessage() {
        this("text");
    }

    public MQMessage(String str) {
        this.status = "arrived";
        this.id = System.currentTimeMillis();
        this.content_type = str;
        this.created_on = System.currentTimeMillis();
        this.is_read = true;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MQMessage)) {
            return false;
        }
        return this.id == ((MQMessage) obj).getId();
    }

    public String getAgent_id() {
        return this.agent_id;
    }

    public String getAgent_nickname() {
        return this.agent_nickname;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getContent() {
        return this.content;
    }

    public String getContent_type() {
        return this.content_type;
    }

    public long getConversation_id() {
        return this.conversation_id;
    }

    public long getCreated_on() {
        return this.created_on;
    }

    public long getEnterprise_id() {
        return this.enterprise_id;
    }

    public String getExtra() {
        return this.extra;
    }

    public String getFrom_type() {
        return this.from_type;
    }

    public long getId() {
        return this.id;
    }

    public String getMedia_url() {
        return this.media_url;
    }

    public String getStatus() {
        return this.status;
    }

    public String getTrack_id() {
        return this.track_id;
    }

    public String getType() {
        return this.type;
    }

    public boolean is_read() {
        return this.is_read;
    }

    public void setAgent_id(String str) {
        this.agent_id = str;
    }

    public void setAgent_nickname(String str) {
        this.agent_nickname = str;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setContent_type(String str) {
        this.content_type = str;
    }

    public void setConversation_id(long j) {
        this.conversation_id = j;
    }

    public void setCreated_on(long j) {
        this.created_on = j;
    }

    public void setEnterprise_id(long j) {
        this.enterprise_id = j;
    }

    public void setExtra(String str) {
        this.extra = str;
    }

    public void setFrom_type(String str) {
        this.from_type = str;
    }

    public void setId(long j) {
        this.id = j;
    }

    public void setIs_read(boolean z) {
        this.is_read = z;
    }

    public void setMedia_url(String str) {
        this.media_url = str;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public void setTrack_id(String str) {
        this.track_id = str;
    }

    public void setType(String str) {
        this.type = str;
    }
}
