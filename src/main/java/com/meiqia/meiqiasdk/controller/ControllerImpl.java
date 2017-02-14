package com.meiqia.meiqiasdk.controller;

import android.content.Context;
import android.text.TextUtils;

import com.meiqia.core.MQManager;
import com.meiqia.meiqiasdk.callback.OnClientOnlineCallback;
import com.meiqia.meiqiasdk.callback.OnDownloadFileCallback;
import com.meiqia.meiqiasdk.callback.OnGetMessageListCallBack;
import com.meiqia.meiqiasdk.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.callback.SimpleCallback;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.meiqia.meiqiasdk.util.MQUtils;

import java.util.Map;

public class ControllerImpl implements MQController {
    public Context context;

    public ControllerImpl(Context context) {
        this.context = context;
    }

    public void sendMessage(BaseMessage message, OnMessageSendCallback onMessageSendCallback) {
        com.meiqia.core.callback.OnMessageSendCallback onMQMessageSendCallback = new 1
        (this, message, onMessageSendCallback);
        if ("text".equals(message.getContentType())) {
            MQManager.getInstance(this.context).sendMQTextMessage(message.getContent(),
                    onMQMessageSendCallback);
        } else if ("photo".equals(message.getContentType())) {
            MQManager.getInstance(this.context).sendMQPhotoMessage(((PhotoMessage) message)
                    .getLocalPath(), onMQMessageSendCallback);
        } else if ("audio".equals(message.getContentType())) {
            MQManager.getInstance(this.context).sendMQVoiceMessage(((VoiceMessage) message)
                    .getLocalPath(), onMQMessageSendCallback);
        }
    }

    public void resendMessage(BaseMessage baseMessage, OnMessageSendCallback
            onMessageSendCallback) {
        sendMessage(baseMessage, new 2 (this, onMessageSendCallback, baseMessage.getId()));
    }

    public void getMessageFromService(long lastMessageCreateOn, int length,
                                      OnGetMessageListCallBack onGetMessageListCallBack) {
        MQManager.getInstance(this.context).getMQMessageFromService(lastMessageCreateOn, length,
                new 3
        (this, onGetMessageListCallBack));
    }

    public void getMessagesFromDatabase(long lastMessageCreateOn, int length,
                                        OnGetMessageListCallBack onGetMessageListCallBack) {
        MQManager.getInstance(this.context).getMQMessageFromDatabase(lastMessageCreateOn, length,
                new 4
        (this, onGetMessageListCallBack));
    }

    public void setCurrentClientOnline(String clientId, String customizedId,
                                       OnClientOnlineCallback onClientOnlineCallback) {
        com.meiqia.core.callback.OnClientOnlineCallback onlineCallback = new 5
        (this, onClientOnlineCallback);
        if (!TextUtils.isEmpty(clientId)) {
            MQManager.getInstance(this.context).setClientOnlineWithClientId(clientId,
                    onlineCallback);
        } else if (TextUtils.isEmpty(customizedId)) {
            MQManager.getInstance(this.context).setCurrentClientOnline(onlineCallback);
        } else {
            MQManager.getInstance(this.context).setClientOnlineWithCustomizedId(customizedId,
                    onlineCallback);
        }
    }

    public void setClientInfo(Map<String, String> clientInfo, SimpleCallback onClientInfoCallback) {
        MQManager.getInstance(this.context).setClientInfo(clientInfo, new 6
        (this, onClientInfoCallback));
    }

    public void sendClientInputtingWithContent(String content) {
        MQManager.getInstance(this.context).sendClientInputtingWithContent(content);
    }

    public void executeEvaluate(String conversationId, int level, String content, SimpleCallback
            simpleCallback) {
        MQManager.getInstance(this.context).executeEvaluate(conversationId, level, content, new 7
        (this, simpleCallback));
    }

    public Agent getCurrentAgent() {
        return MQUtils.parseMQAgentToAgent(MQManager.getInstance(this.context).getCurrentAgent());
    }

    public void updateMessage(long messageId, boolean isRead) {
        MQManager.getInstance(this.context).updateMessage(messageId, isRead);
    }

    public void saveConversationOnStopTime(long stopTime) {
        MQManager.getInstance(this.context).saveConversationOnStopTime(stopTime);
    }

    public void downloadFile(BaseMessage fileMessage, OnDownloadFileCallback
            onDownloadFileCallback) {
        MQManager.getInstance(this.context).downloadFile(MQUtils.parseBaseMessageToMQMessage
                (fileMessage), new 8
        (this, onDownloadFileCallback));
    }

    public void cancelDownload(String url) {
        MQManager.getInstance(this.context).cancelDownload(url);
    }

    public void closeService() {
        MQManager.getInstance(this.context).closeMeiqiaService();
    }
}
