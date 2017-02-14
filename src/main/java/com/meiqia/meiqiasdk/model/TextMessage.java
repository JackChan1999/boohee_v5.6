package com.meiqia.meiqiasdk.model;

public class TextMessage extends BaseMessage {
    public TextMessage() {
        setItemViewType(0);
        setContentType("text");
    }

    public TextMessage(String content) {
        this();
        setContent(content);
    }
}
