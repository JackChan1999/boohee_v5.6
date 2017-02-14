package com.meiqia.meiqiasdk.model;

public class BaseMessage {
    public static final int    MAX_TYPE           = 5;
    public static final String STATE_ARRIVE       = "arrived";
    public static final String STATE_FAILED       = "failed";
    public static final String STATE_SENDING      = "sending";
    public static final int    TYPE_AGENT         = 1;
    public static final int    TYPE_CLIENT        = 0;
    public static final String TYPE_CONTENT_FILE  = "file";
    public static final String TYPE_CONTENT_PHOTO = "photo";
    public static final String TYPE_CONTENT_TEXT  = "text";
    public static final String TYPE_CONTENT_VOICE = "audio";
    public static final String TYPE_ENDING        = "ending";
    public static final int    TYPE_EVALUATE      = 4;
    public static final String TYPE_FROM_AGENT    = "agent";
    public static final String TYPE_FROM_CLIENT   = "client";
    public static final String TYPE_INTERNAL      = "internal";
    public static final String TYPE_MESSAGE       = "message";
    public static final String TYPE_REMARK        = "remark";
    public static final String TYPE_REPLY         = "reply";
    public static final int    TYPE_TIME          = 2;
    public static final int    TYPE_TIP           = 3;
    public static final String TYPE_WELCOME       = "welcome";
    private String agentNickname;
    private String avatar;
    private String content;
    private String contentType;
    private long   conversationId;
    private long createdOn = System.currentTimeMillis();
    private long    id;
    private boolean isRead;
    private int     itemViewType;
    private String  status;
    private String  type;

    public int getItemViewType() {
        return this.itemViewType;
    }

    public void setItemViewType(int itemViewType) {
        this.itemViewType = itemViewType;
    }

    public long getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getAgentNickname() {
        return this.agentNickname;
    }

    public void setAgentNickname(String agentNickname) {
        this.agentNickname = agentNickname;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public long getConversationId() {
        return this.conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public boolean equals(Object o) {
        if (!(o instanceof BaseMessage)) {
            return false;
        }
        if (this.id == ((BaseMessage) o).getId()) {
            return true;
        }
        return false;
    }
}
