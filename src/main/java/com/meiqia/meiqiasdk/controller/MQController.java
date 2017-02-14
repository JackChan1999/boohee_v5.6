package com.meiqia.meiqiasdk.controller;

import com.meiqia.meiqiasdk.callback.OnClientOnlineCallback;
import com.meiqia.meiqiasdk.callback.OnDownloadFileCallback;
import com.meiqia.meiqiasdk.callback.OnGetMessageListCallBack;
import com.meiqia.meiqiasdk.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.callback.SimpleCallback;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;

import java.util.Map;

public interface MQController {
    public static final String ACTION_AGENT_INPUTTING            = "agent_inputting_action";
    public static final String ACTION_AGENT_STATUS_UPDATE_EVENT  =
            "action_agent_status_update_event";
    public static final String ACTION_BLACK_ADD                  = "action_black_add";
    public static final String ACTION_CLIENT_IS_REDIRECTED_EVENT = "agent_change_action";
    public static final String ACTION_INVITE_EVALUATION          = "invite_evaluation";
    public static final String ACTION_NEW_MESSAGE_RECEIVED       = "new_msg_received_action";

    void cancelDownload(String str);

    void closeService();

    void downloadFile(BaseMessage baseMessage, OnDownloadFileCallback onDownloadFileCallback);

    void executeEvaluate(String str, int i, String str2, SimpleCallback simpleCallback);

    Agent getCurrentAgent();

    void getMessageFromService(long j, int i, OnGetMessageListCallBack onGetMessageListCallBack);

    void getMessagesFromDatabase(long j, int i, OnGetMessageListCallBack onGetMessageListCallBack);

    void resendMessage(BaseMessage baseMessage, OnMessageSendCallback onMessageSendCallback);

    void saveConversationOnStopTime(long j);

    void sendClientInputtingWithContent(String str);

    void sendMessage(BaseMessage baseMessage, OnMessageSendCallback onMessageSendCallback);

    void setClientInfo(Map<String, String> map, SimpleCallback simpleCallback);

    void setCurrentClientOnline(String str, String str2, OnClientOnlineCallback
            onClientOnlineCallback);

    void updateMessage(long j, boolean z);
}
