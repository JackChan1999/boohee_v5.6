package com.meiqia.meiqiasdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.callback.FileStateCallback;
import com.meiqia.meiqiasdk.callback.OnClientOnlineCallback;
import com.meiqia.meiqiasdk.callback.OnGetMessageListCallBack;
import com.meiqia.meiqiasdk.callback.OnMessageSendCallback;
import com.meiqia.meiqiasdk.callback.SimpleCallback;
import com.meiqia.meiqiasdk.controller.ControllerImpl;
import com.meiqia.meiqiasdk.controller.MQController;
import com.meiqia.meiqiasdk.dialog.MQEvaluateDialog;
import com.meiqia.meiqiasdk.dialog.MQEvaluateDialog.Callback;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.AgentChangeMessage;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.EvaluateMessage;
import com.meiqia.meiqiasdk.model.FileMessage;
import com.meiqia.meiqiasdk.model.LeaveTipMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.TextMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.meiqia.meiqiasdk.util.ErrorCode;
import com.meiqia.meiqiasdk.util.MQAudioPlayerManager;
import com.meiqia.meiqiasdk.util.MQAudioRecorderManager;
import com.meiqia.meiqiasdk.util.MQChatAdapter;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQConfig.ui;
import com.meiqia.meiqiasdk.util.MQSimpleTextWatcher;
import com.meiqia.meiqiasdk.util.MQSoundPoolManager;
import com.meiqia.meiqiasdk.util.MQTimeUtils;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.meiqia.meiqiasdk.widget.MQCustomKeyboardLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import uk.co.senab.photoview.IPhotoView;

public class MQConversationActivity extends Activity implements OnClickListener, Callback,
        MQCustomKeyboardLayout.Callback, OnTouchListener, FileStateCallback {
    private static final long   AUTO_DISMISS_TOP_TIP_TIME = 2000;
    public static final  String CLIENT_ID                 = "clientId";
    public static final  String CLIENT_INFO               = "clientInfo";
    public static final  String CUSTOMIZED_ID             = "customizedId";
    private static       int    MESSAGE_PAGE_COUNT        = 30;
    public static final  int    REQUEST_CODE_CAMERA       = 0;
    public static final  int    REQUEST_CODE_PHOTO        = 1;
    private static final String TAG                       = MQConversationActivity.class
            .getSimpleName();
    private Runnable       autoDismissTopTipRunnable;
    private RelativeLayout backRl;
    private TextWatcher inputTextWatcher = new MQSimpleTextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                if (VERSION.SDK_INT >= 21) {
                    MQConversationActivity.this.mSendTextBtn.setElevation(0.0f);
                }
                MQConversationActivity.this.mSendTextBtn.setImageResource(R.drawable
                        .mq_ic_send_icon_grey);
                MQConversationActivity.this.mSendTextBtn.setBackgroundResource(R.drawable
                        .mq_shape_send_back_normal);
                return;
            }
            MQConversationActivity.this.inputting(s.toString());
            if (VERSION.SDK_INT >= 21) {
                MQConversationActivity.this.mSendTextBtn.setElevation((float) MQUtils.dip2px
                        (MQConversationActivity.this, IPhotoView.DEFAULT_MAX_SCALE));
            }
            MQConversationActivity.this.mSendTextBtn.setImageResource(R.drawable
                    .mq_ic_send_icon_white);
            MQConversationActivity.this.mSendTextBtn.setBackgroundResource(R.drawable
                    .mq_shape_send_back_pressed);
        }
    };
    private boolean        isAddLeaveTip;
    private boolean        isBlackState;
    private boolean        isDestroy;
    private boolean        isPause;
    private ImageView      mBackIv;
    private TextView       mBackTv;
    private String         mCameraPicPath;
    private View           mCameraSelectBtn;
    private RelativeLayout mChatBodyRl;
    private List<BaseMessage> mChatMessageList = new ArrayList();
    private MQChatAdapter          mChatMsgAdapter;
    private MQController           mController;
    private String                 mConversationId;
    private ListView               mConversationListView;
    private Agent                  mCurrentAgent;
    private MQCustomKeyboardLayout mCustomKeyboardLayout;
    private View                   mEmojiSelectBtn;
    private ImageView              mEmojiSelectImg;
    private View                   mEmojiSelectIndicator;
    private View                   mEvaluateBtn;
    private MQEvaluateDialog       mEvaluateDialog;
    private Handler                mHandler;
    private boolean mHasLoadData = false;
    private EditText              mInputEt;
    private ProgressBar           mLoadProgressBar;
    private MessageReceiver       mMessageReceiver;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private View                  mPhotoSelectBtn;
    private ImageButton           mSendTextBtn;
    private MQSoundPoolManager    mSoundPoolManager;
    private SwipeRefreshLayout    mSwipeRefreshLayout;
    private RelativeLayout        mTitleRl;
    private TextView              mTitleTv;
    private TextView              mTopTipViewTv;
    private View                  mVoiceBtn;
    private ImageView             mVoiceSelectImg;
    private View                  mVoiceSelectIndicator;

    private class MessageReceiver extends com.meiqia.meiqiasdk.controller.MessageReceiver {
        private MessageReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }

        public void receiveNewMsg(BaseMessage message) {
            MQConversationActivity.this.receiveNewMsg(message);
        }

        public void changeTitleToInputting() {
            MQConversationActivity.this.changeTitleToInputting();
            MQConversationActivity.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    MQConversationActivity.this.changeTitleToAgentName(MQConversationActivity
                            .this.mCurrentAgent);
                }
            }, MQConversationActivity.AUTO_DISMISS_TOP_TIP_TIME);
        }

        public void changeTitleToAgentName(String agentNickname) {
            MQConversationActivity.this.changeTitleToAgentName(agentNickname);
        }

        public void addDirectAgentMessageTip(String agentNickname) {
            MQConversationActivity.this.addDirectAgentMessageTip(agentNickname);
        }

        public void setCurrentAgent(Agent agent) {
            MQConversationActivity.this.setCurrentAgent(agent);
        }

        public void inviteEvaluation() {
            MQConversationActivity.this.showEvaluateDialog();
        }

        public void setNewConversationId(String newConversationId) {
            MQConversationActivity.this.mConversationId = newConversationId;
            MQConversationActivity.this.removeLeaveMessageTip();
        }

        public void updateAgentOnlineOfflineStatus() {
            MQConversationActivity.this.updateAgentOnlineOfflineStatus();
        }

        public void blackAdd() {
            MQConversationActivity.this.isBlackState = true;
            MQConversationActivity.this.changeTitleToNoAgentState();
        }
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {
        private ConnectivityManager connectivityManager;
        private boolean             isFirstReceiveBroadcast;

        private NetworkChangeReceiver() {
            this.isFirstReceiveBroadcast = true;
        }

        public void onReceive(Context arg0, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                this.connectivityManager = (ConnectivityManager) MQConversationActivity.this
                        .getSystemService("connectivity");
                NetworkInfo info = this.connectivityManager.getActiveNetworkInfo();
                if (this.isFirstReceiveBroadcast) {
                    this.isFirstReceiveBroadcast = false;
                } else if (info == null || !info.isAvailable()) {
                    MQConversationActivity.this.changeTitleToNetErrorState();
                } else {
                    MQConversationActivity.this.mCurrentAgent = MQConversationActivity.this
                            .mController.getCurrentAgent();
                    MQConversationActivity.this.changeTitleToAgentName(MQConversationActivity
                            .this.mCurrentAgent);
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mController = MQConfig.getController(this);
        getWindow().addFlags(128);
        setContentView(R.layout.mq_activity_conversation);
        findViews();
        init();
        setListeners();
        applyCustomUIConfig();
        registerReceiver();
    }

    private void applyCustomUIConfig() {
        if (-1 != ui.backArrowIconResId) {
            this.mBackIv.setImageResource(ui.backArrowIconResId);
        }
        MQUtils.applyCustomUITintDrawable(this.mTitleRl, 17170443, R.color.mq_activity_title_bg,
                ui.titleBackgroundResId);
        MQUtils.applyCustomUITextAndImageColor(R.color.mq_activity_title_textColor, ui
                .titleTextColorResId, this.mBackIv, this.mBackTv, this.mTitleTv);
        MQUtils.applyCustomUITitleGravity(this.mBackTv, this.mTitleTv);
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.photo_select_iv), R.drawable
                .mq_ic_image_normal, R.drawable.mq_ic_image_active);
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.camera_select_iv), R.drawable
                .mq_ic_camera_normal, R.drawable.mq_ic_camera_active);
        MQUtils.tintPressedIndicator((ImageView) findViewById(R.id.evaluate_select_iv), R
                .drawable.mq_ic_evaluate_normal, R.drawable.mq_ic_evaluate_active);
    }

    protected void onResume() {
        super.onResume();
        setClientOnline();
        this.isPause = false;
    }

    protected void onPause() {
        super.onPause();
        this.isPause = true;
    }

    protected void onStop() {
        super.onStop();
        if (this.mChatMsgAdapter != null) {
            this.mChatMsgAdapter.stopPlayVoice();
            MQAudioPlayerManager.release();
        }
        if (this.mChatMessageList == null || this.mChatMessageList.size() <= 0) {
            this.mController.saveConversationOnStopTime(System.currentTimeMillis());
        } else {
            this.mController.saveConversationOnStopTime(((BaseMessage) this.mChatMessageList.get
                    (this.mChatMessageList.size() - 1)).getCreatedOn());
        }
    }

    protected void onDestroy() {
        MQUtils.closeKeyboard((Activity) this);
        try {
            this.mSoundPoolManager.release();
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mMessageReceiver);
            unregisterReceiver(this.mNetworkChangeReceiver);
        } catch (Exception e) {
        }
        this.isDestroy = true;
        cancelAllDownload();
        super.onDestroy();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || !this.mCustomKeyboardLayout.isEmotionKeyboardVisible()) {
            return super.onKeyUp(keyCode, event);
        }
        this.mCustomKeyboardLayout.closeEmotionKeyboard();
        return true;
    }

    private void init() {
        if (this.mController == null) {
            this.mController = new ControllerImpl(this);
        }
        MQTimeUtils.init(this);
        this.mHandler = new Handler();
        this.mSoundPoolManager = MQSoundPoolManager.getInstance(this);
        this.mChatMsgAdapter = new MQChatAdapter(this, this.mChatMessageList, this
                .mConversationListView);
        this.mConversationListView.setAdapter(this.mChatMsgAdapter);
        if (!MQConfig.isVoiceSwitchOpen) {
            this.mVoiceBtn.setVisibility(8);
        }
        this.mCustomKeyboardLayout.init(this, this.mInputEt, this);
        this.isDestroy = false;
    }

    private void findViews() {
        this.mTitleRl = (RelativeLayout) findViewById(R.id.title_rl);
        this.backRl = (RelativeLayout) findViewById(R.id.back_rl);
        this.mBackTv = (TextView) findViewById(R.id.back_tv);
        this.mBackIv = (ImageView) findViewById(R.id.back_iv);
        this.mChatBodyRl = (RelativeLayout) findViewById(R.id.chat_body_rl);
        this.mConversationListView = (ListView) findViewById(R.id.messages_lv);
        this.mInputEt = (EditText) findViewById(R.id.input_et);
        this.mEmojiSelectBtn = findViewById(R.id.emoji_select_btn);
        this.mCustomKeyboardLayout = (MQCustomKeyboardLayout) findViewById(R.id
                .customKeyboardLayout);
        this.mSendTextBtn = (ImageButton) findViewById(R.id.send_text_btn);
        this.mPhotoSelectBtn = findViewById(R.id.photo_select_btn);
        this.mCameraSelectBtn = findViewById(R.id.camera_select_btn);
        this.mVoiceBtn = findViewById(R.id.mic_select_btn);
        this.mEvaluateBtn = findViewById(R.id.evaluate_select_btn);
        this.mLoadProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        this.mTitleTv = (TextView) findViewById(R.id.title_tv);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        this.mEmojiSelectIndicator = findViewById(R.id.emoji_select_indicator);
        this.mEmojiSelectImg = (ImageView) findViewById(R.id.emoji_select_img);
        this.mVoiceSelectIndicator = findViewById(R.id.conversation_voice_indicator);
        this.mVoiceSelectImg = (ImageView) findViewById(R.id.conversation_voice_img);
    }

    private void setListeners() {
        this.backRl.setOnClickListener(this);
        this.mSendTextBtn.setOnClickListener(this);
        this.mPhotoSelectBtn.setOnClickListener(this);
        this.mCameraSelectBtn.setOnClickListener(this);
        this.mVoiceBtn.setOnClickListener(this);
        this.mEvaluateBtn.setOnClickListener(this);
        this.mInputEt.addTextChangedListener(this.inputTextWatcher);
        this.mInputEt.setOnTouchListener(this);
        this.mInputEt.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 6) {
                    return false;
                }
                MQConversationActivity.this.mSendTextBtn.performClick();
                MQUtils.closeKeyboard(MQConversationActivity.this);
                return true;
            }
        });
        this.mEmojiSelectBtn.setOnClickListener(this);
        this.mConversationListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == 0) {
                    MQConversationActivity.this.mCustomKeyboardLayout.closeAllKeyboard();
                    MQConversationActivity.this.hideEmojiSelectIndicator();
                    MQConversationActivity.this.hideVoiceSelectIndicator();
                }
                return false;
            }
        });
        this.mConversationListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View arg1, int arg2, long
                    arg3) {
                String content = ((BaseMessage) MQConversationActivity.this.mChatMessageList.get
                        (arg2)).getContent();
                if (TextUtils.isEmpty(content)) {
                    return false;
                }
                MQUtils.clip(MQConversationActivity.this, content);
                MQUtils.show(MQConversationActivity.this, R.string.mq_copy_success);
                return true;
            }
        });
        this.mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (MQConfig.isLoadMessagesFromNativeOpen) {
                    MQConversationActivity.this.loadMoreDataFromDatabase();
                } else {
                    MQConversationActivity.this.loadMoreDataFromService();
                }
            }
        });
    }

    private void registerReceiver() {
        this.mMessageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("agent_inputting_action");
        intentFilter.addAction("new_msg_received_action");
        intentFilter.addAction("agent_change_action");
        intentFilter.addAction("invite_evaluation");
        intentFilter.addAction("action_agent_status_update_event");
        intentFilter.addAction("action_black_add");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mMessageReceiver,
                intentFilter);
        this.mNetworkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.mNetworkChangeReceiver, mFilter);
    }

    protected void changeTitleToAgentName(String agentName) {
        this.mTitleTv.setText(agentName);
        updateAgentOnlineOfflineStatus();
    }

    protected void changeTitleToAgentName(Agent agent) {
        if (agent != null) {
            this.mTitleTv.setText(agent.getNickname());
            updateAgentOnlineOfflineStatus();
            return;
        }
        changeTitleToNoAgentState();
    }

    protected void changeTitleToInputting() {
        this.mTitleTv.setText(getResources().getString(R.string.mq_title_inputting));
        updateAgentOnlineOfflineStatus();
    }

    protected void changeTitleToAllocatingAgent() {
        this.mTitleTv.setText(getResources().getString(R.string.mq_allocate_agent));
        hiddenAgentStatusCircle();
    }

    protected void changeTitleToNoAgentState() {
        this.mTitleTv.setText(getResources().getString(R.string.mq_title_leave_msg));
        this.mEvaluateBtn.setVisibility(8);
        hiddenAgentStatusCircle();
    }

    protected void changeTitleToNetErrorState() {
        this.mTitleTv.setText(getResources().getString(R.string.mq_title_net_not_work));
        hiddenAgentStatusCircle();
    }

    protected void changeTitleToUnknownErrorState() {
        this.mTitleTv.setText(getResources().getString(R.string.mq_title_unknown_error));
        hiddenAgentStatusCircle();
    }

    protected void addDirectAgentMessageTip(String agentNickName) {
        this.mTitleTv.setText(agentNickName);
        AgentChangeMessage agentChangeMessage = new AgentChangeMessage();
        agentChangeMessage.setAgentNickname(agentNickName);
        this.mChatMsgAdapter.addMQMessage(agentChangeMessage);
        updateAgentOnlineOfflineStatus();
    }

    protected void addBlacklistTip(int blackTipRes) {
        this.isBlackState = true;
        changeTitleToNoAgentState();
        BaseMessage blacklistMessage = new BaseMessage();
        blacklistMessage.setItemViewType(3);
        blacklistMessage.setContent(getResources().getString(blackTipRes));
        this.mChatMsgAdapter.addMQMessage(blacklistMessage);
    }

    protected void addLeaveMessageTip() {
        this.mEvaluateBtn.setVisibility(8);
        if (!this.isAddLeaveTip) {
            changeTitleToNoAgentState();
            LeaveTipMessage leaveTip = new LeaveTipMessage();
            leaveTip.setContent(getResources().getString(R.string.mq_leave_msg_tips));
            int position = this.mChatMessageList.size();
            if (position != 0) {
                position--;
            }
            this.mChatMsgAdapter.addMQMessage(leaveTip, position);
            this.isAddLeaveTip = true;
        }
    }

    protected void removeLeaveMessageTip() {
        this.mEvaluateBtn.setVisibility(0);
        Iterator<BaseMessage> chatItemViewBaseIterator = this.mChatMessageList.iterator();
        while (chatItemViewBaseIterator.hasNext()) {
            if (((BaseMessage) chatItemViewBaseIterator.next()).getItemViewType() == 3) {
                chatItemViewBaseIterator.remove();
                this.mChatMsgAdapter.notifyDataSetChanged();
                return;
            }
        }
        this.isAddLeaveTip = false;
    }

    public void popTopTip(final int contentRes) {
        if (this.mTopTipViewTv == null) {
            this.mTopTipViewTv = (TextView) getLayoutInflater().inflate(R.layout.mq_top_pop_tip,
                    null);
            this.mTopTipViewTv.setText(contentRes);
            int height = getResources().getDimensionPixelOffset(R.dimen.mq_top_tip_height);
            this.mChatBodyRl.addView(this.mTopTipViewTv, -1, height);
            ViewCompat.setTranslationY(this.mTopTipViewTv, (float) (-height));
            ViewCompat.animate(this.mTopTipViewTv).translationY(0.0f).setDuration(300).start();
            if (this.autoDismissTopTipRunnable == null) {
                this.autoDismissTopTipRunnable = new Runnable() {
                    public void run() {
                        MQConversationActivity.this.popTopTip(contentRes);
                    }
                };
            }
            this.mHandler.postDelayed(this.autoDismissTopTipRunnable, AUTO_DISMISS_TOP_TIP_TIME);
            return;
        }
        this.mHandler.removeCallbacks(this.autoDismissTopTipRunnable);
        ViewCompat.animate(this.mTopTipViewTv).translationY((float) (-this.mTopTipViewTv
                .getHeight())).setListener(new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(View view) {
                MQConversationActivity.this.mChatBodyRl.removeView(MQConversationActivity.this
                        .mTopTipViewTv);
                MQConversationActivity.this.mTopTipViewTv = null;
            }
        }).setDuration(300).start();
    }

    private void setCurrentAgent(Agent agent) {
        this.mCurrentAgent = agent;
    }

    private void loadMoreDataFromService() {
        long lastMessageCreateOn = System.currentTimeMillis();
        if (this.mChatMessageList.size() > 0) {
            lastMessageCreateOn = ((BaseMessage) this.mChatMessageList.get(0)).getCreatedOn();
        }
        this.mController.getMessageFromService(lastMessageCreateOn, MESSAGE_PAGE_COUNT, new
                OnGetMessageListCallBack() {
            public void onSuccess(List<BaseMessage> messageList) {
                MQConversationActivity.this.cleanVoiceMessage(messageList);
                MQTimeUtils.refreshMQTimeItem(messageList);
                MQConversationActivity.this.mChatMsgAdapter.loadMoreMessage
                        (MQConversationActivity.this.cleanDupMessages(MQConversationActivity.this
                                .mChatMessageList, messageList));
                MQConversationActivity.this.mConversationListView.setSelection(messageList.size());
                MQConversationActivity.this.mSwipeRefreshLayout.setRefreshing(false);
                if (messageList.size() == 0) {
                    MQConversationActivity.this.mSwipeRefreshLayout.setEnabled(false);
                }
            }

            public void onFailure(int code, String responseString) {
                MQConversationActivity.this.mChatMsgAdapter.notifyDataSetChanged();
                MQConversationActivity.this.mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMoreDataFromDatabase() {
        long lastMessageCreateOn = System.currentTimeMillis();
        if (this.mChatMessageList.size() > 0) {
            lastMessageCreateOn = ((BaseMessage) this.mChatMessageList.get(0)).getCreatedOn();
        }
        this.mController.getMessagesFromDatabase(lastMessageCreateOn, MESSAGE_PAGE_COUNT, new
                OnGetMessageListCallBack() {
            public void onSuccess(List<BaseMessage> messageList) {
                MQConversationActivity.this.cleanVoiceMessage(messageList);
                MQTimeUtils.refreshMQTimeItem(messageList);
                MQConversationActivity.this.mChatMsgAdapter.loadMoreMessage
                        (MQConversationActivity.this.cleanDupMessages(MQConversationActivity.this
                                .mChatMessageList, messageList));
                MQConversationActivity.this.mConversationListView.setSelection(messageList.size());
                MQConversationActivity.this.mSwipeRefreshLayout.setRefreshing(false);
                if (messageList.size() == 0) {
                    MQConversationActivity.this.mSwipeRefreshLayout.setEnabled(false);
                }
            }

            public void onFailure(int code, String responseString) {
                MQConversationActivity.this.mChatMsgAdapter.notifyDataSetChanged();
                MQConversationActivity.this.mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<BaseMessage> cleanDupMessages(List<BaseMessage> messageList, List<BaseMessage>
            newMessageList) {
        Iterator<BaseMessage> iterator = newMessageList.iterator();
        while (iterator.hasNext()) {
            if (messageList.contains((BaseMessage) iterator.next())) {
                iterator.remove();
            }
        }
        return newMessageList;
    }

    private void setClientOnline() {
        if (this.mCurrentAgent == null) {
            changeTitleToAllocatingAgent();
            String clientId = null;
            String customizedId = null;
            HashMap<String, String> clientInfo = null;
            if (getIntent() != null) {
                clientId = getIntent().getStringExtra("clientId");
                customizedId = getIntent().getStringExtra(CUSTOMIZED_ID);
                Serializable clientInfoSerializable = getIntent().getSerializableExtra(CLIENT_INFO);
                if (clientInfoSerializable != null) {
                    clientInfo = (HashMap) clientInfoSerializable;
                }
            }
            final HashMap<String, String> finalClientInfo = clientInfo;
            this.mController.setCurrentClientOnline(clientId, customizedId, new
                    OnClientOnlineCallback() {
                public void onSuccess(Agent agent, String conversationId, List<BaseMessage>
                        conversationMessageList) {
                    MQConversationActivity.this.setCurrentAgent(agent);
                    MQConversationActivity.this.changeTitleToAgentName(agent);
                    MQConversationActivity.this.removeLeaveMessageTip();
                    MQConversationActivity.this.mConversationId = conversationId;
                    MQConversationActivity.this.mMessageReceiver.setConversationId(conversationId);
                    MQConversationActivity.this.cleanVoiceMessage(conversationMessageList);
                    MQConversationActivity.this.mChatMessageList.clear();
                    MQConversationActivity.this.mChatMessageList.addAll(conversationMessageList);
                    MQConversationActivity.this.loadData();
                    MQConversationActivity.this.onLoadDataComplete(MQConversationActivity.this,
                            agent);
                    if (finalClientInfo != null) {
                        MQConversationActivity.this.mController.setClientInfo(finalClientInfo,
                                null);
                    }
                }

                public void onFailure(int code, String message) {
                    if (ErrorCode.NET_NOT_WORK == code) {
                        MQConversationActivity.this.changeTitleToNetErrorState();
                    } else if (ErrorCode.NO_AGENT_ONLINE == code) {
                        MQConversationActivity.this.setCurrentAgent(null);
                        MQConversationActivity.this.changeTitleToNoAgentState();
                        if (finalClientInfo != null) {
                            MQConversationActivity.this.mController.setClientInfo
                                    (finalClientInfo, null);
                        }
                    } else if (ErrorCode.BLACKLIST == code) {
                        MQConversationActivity.this.changeTitleToNoAgentState();
                        MQConversationActivity.this.isBlackState = true;
                    } else {
                        MQConversationActivity.this.changeTitleToUnknownErrorState();
                        Toast.makeText(MQConversationActivity.this, "code = " + code + "\n" +
                                "message = " + message, 0).show();
                    }
                    if (!MQConversationActivity.this.mHasLoadData) {
                        MQConversationActivity.this.getMessageDataFromDatabaseAndLoad();
                        MQConversationActivity.this.onLoadDataComplete(MQConversationActivity
                                .this, null);
                    }
                }
            });
            return;
        }
        changeTitleToAgentName(this.mCurrentAgent);
    }

    private void getMessageDataFromDatabaseAndLoad() {
        this.mController.getMessagesFromDatabase(System.currentTimeMillis(), MESSAGE_PAGE_COUNT,
                new OnGetMessageListCallBack() {
            public void onSuccess(List<BaseMessage> messageList) {
                MQConversationActivity.this.cleanVoiceMessage(messageList);
                MQConversationActivity.this.mChatMessageList.addAll(messageList);
                MQConversationActivity.this.loadData();
            }

            public void onFailure(int code, String responseString) {
            }
        });
    }

    private void loadData() {
        MQTimeUtils.refreshMQTimeItem(this.mChatMessageList);
        this.mLoadProgressBar.setVisibility(8);
        Iterator<BaseMessage> messageIterator = this.mChatMessageList.iterator();
        while (messageIterator.hasNext()) {
            BaseMessage message = (BaseMessage) messageIterator.next();
            if ("sending".equals(message.getStatus())) {
                message.setStatus("arrived");
            } else if ("ending".equals(message.getType()) && this.isBlackState) {
                messageIterator.remove();
            }
        }
        if (this.isBlackState) {
            addBlacklistTip(R.string.mq_blacklist_online_tips);
        }
        MQUtils.scrollListViewToBottom(this.mConversationListView);
        this.mChatMsgAdapter.downloadAndNotifyDataSetChanged(this.mChatMessageList);
        this.mChatMsgAdapter.notifyDataSetChanged();
        this.mHasLoadData = true;
    }

    protected void onLoadDataComplete(MQConversationActivity mqConversationActivity, Agent agent) {
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_rl) {
            onBackPressed();
        } else if (id == R.id.emoji_select_btn) {
            if (this.mCustomKeyboardLayout.isEmotionKeyboardVisible()) {
                hideEmojiSelectIndicator();
            } else {
                showEmojiSelectIndicator();
            }
            hideVoiceSelectIndicator();
            this.mCustomKeyboardLayout.toggleEmotionOriginKeyboard();
        } else if (id == R.id.send_text_btn) {
            if (this.mHasLoadData) {
                createAndSendTextMessage();
            } else {
                MQUtils.show((Context) this, R.string.mq_data_is_loading);
            }
        } else if (id == R.id.photo_select_btn) {
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            chooseFromPhotoPicker();
        } else if (id == R.id.camera_select_btn) {
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            choosePhotoFromCamera();
        } else if (id == R.id.mic_select_btn) {
            if (this.mCustomKeyboardLayout.isVoiceKeyboardVisible()) {
                hideVoiceSelectIndicator();
            } else {
                showVoiceSelectIndicator();
            }
            hideEmojiSelectIndicator();
            this.mCustomKeyboardLayout.toggleVoiceOriginKeyboard();
        } else if (id == R.id.evaluate_select_btn) {
            hideEmojiSelectIndicator();
            hideVoiceSelectIndicator();
            showEvaluateDialog();
        }
    }

    private void showEvaluateDialog() {
        if (!this.mCustomKeyboardLayout.isRecording()) {
            this.mCustomKeyboardLayout.closeAllKeyboard();
            if (!TextUtils.isEmpty(this.mConversationId)) {
                if (this.mEvaluateDialog == null) {
                    this.mEvaluateDialog = new MQEvaluateDialog(this);
                    this.mEvaluateDialog.setCallback(this);
                }
                this.mEvaluateDialog.show();
            }
        }
    }

    private void showEmojiSelectIndicator() {
        this.mEmojiSelectIndicator.setVisibility(0);
        this.mEmojiSelectImg.setImageResource(R.drawable.mq_ic_emoji_active);
        this.mEmojiSelectImg.setColorFilter(getResources().getColor(R.color.mq_indicator_selected));
    }

    private void hideEmojiSelectIndicator() {
        this.mEmojiSelectIndicator.setVisibility(8);
        this.mEmojiSelectImg.setImageResource(R.drawable.mq_ic_emoji_normal);
        this.mEmojiSelectImg.clearColorFilter();
    }

    private void showVoiceSelectIndicator() {
        this.mVoiceSelectIndicator.setVisibility(0);
        this.mVoiceSelectImg.setImageResource(R.drawable.mq_ic_mic_active);
        this.mVoiceSelectImg.setColorFilter(getResources().getColor(R.color.mq_indicator_selected));
    }

    private void hideVoiceSelectIndicator() {
        this.mVoiceSelectIndicator.setVisibility(8);
        this.mVoiceSelectImg.setImageResource(R.drawable.mq_ic_mic_normal);
        this.mVoiceSelectImg.clearColorFilter();
    }

    private void chooseFromPhotoPicker() {
        if (this.mHasLoadData) {
            startActivityForResult(MQPhotoPickerActivity.newIntent(this, null, 6, null, getString
                    (R.string.mq_send)), 1);
        } else {
            MQUtils.show((Context) this, R.string.mq_data_is_loading);
        }
    }

    private void choosePhotoFromCamera() {
        if (this.mHasLoadData) {
            MQUtils.closeKeyboard((Activity) this);
            Intent camera = new Intent("android.media.action.IMAGE_CAPTURE");
            new File(MQUtils.getPicStorePath(this)).mkdirs();
            String path = MQUtils.getPicStorePath(this) + "/" + System.currentTimeMillis() + ".jpg";
            camera.putExtra("output", Uri.fromFile(new File(path)));
            this.mCameraPicPath = path;
            try {
                startActivityForResult(camera, 0);
                return;
            } catch (Exception e) {
                MQUtils.show((Context) this, R.string.mq_photo_not_support);
                return;
            }
        }
        MQUtils.show((Context) this, R.string.mq_data_is_loading);
    }

    private void createAndSendTextMessage() {
        String msg = this.mInputEt.getText().toString();
        if (!TextUtils.isEmpty(msg.trim())) {
            sendMessage(new TextMessage(msg));
        }
    }

    private void createAndSendImageMessage(File imageFile) {
        PhotoMessage imageMessage = new PhotoMessage();
        imageMessage.setLocalPath(imageFile.getAbsolutePath());
        sendMessage(imageMessage);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0) {
            File cameraPicFile = getCameraPicFile();
            if (cameraPicFile != null) {
                createAndSendImageMessage(cameraPicFile);
            }
        } else if (requestCode == 1) {
            Iterator it = MQPhotoPickerActivity.getSelectedImages(data).iterator();
            while (it.hasNext()) {
                createAndSendImageMessage(new File((String) it.next()));
            }
        }
    }

    public void startActivity(Intent intent) {
        if (intent.toString().contains("mailto")) {
            List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
            if (activities == null || activities.size() == 0) {
                return;
            }
        }
        super.startActivity(intent);
    }

    public File getCameraPicFile() {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        File imageFile = new File(this.mCameraPicPath);
        if (imageFile.exists()) {
            return imageFile;
        }
        return null;
    }

    private boolean checkAndPreSend(BaseMessage message) {
        if (this.mChatMsgAdapter == null) {
            return false;
        }
        message.setStatus("sending");
        this.mChatMessageList.add(message);
        this.mInputEt.setText("");
        MQTimeUtils.refreshMQTimeItem(this.mChatMessageList);
        this.mChatMsgAdapter.notifyDataSetChanged();
        return true;
    }

    public void sendMessage(BaseMessage message) {
        if (checkAndPreSend(message)) {
            this.mController.sendMessage(message, new OnMessageSendCallback() {
                public void onSuccess(BaseMessage message, int state) {
                    MQConversationActivity.this.renameVoiceFilename(message);
                    MQConversationActivity.this.mChatMsgAdapter.notifyDataSetChanged();
                    if (ErrorCode.NO_AGENT_ONLINE == state) {
                        MQConversationActivity.this.addLeaveMessageTip();
                    }
                    if (MQConfig.isSoundSwitchOpen) {
                        MQConversationActivity.this.mSoundPoolManager.playSound(R.raw
                                .mq_send_message);
                    }
                }

                public void onFailure(BaseMessage failureMessage, int code, String failureInfo) {
                    if (code == ErrorCode.BLACKLIST) {
                        MQConversationActivity.this.addBlacklistTip(R.string.mq_blacklist_msg_tips);
                    }
                    MQConversationActivity.this.mChatMsgAdapter.notifyDataSetChanged();
                }
            });
            MQUtils.scrollListViewToBottom(this.mConversationListView);
        }
    }

    public void resendMessage(BaseMessage message) {
        this.mController.resendMessage(message, new OnMessageSendCallback() {
            public void onSuccess(BaseMessage message, int state) {
                MQConversationActivity.this.renameVoiceFilename(message);
                MQConversationActivity.this.updateResendMessage(message, 0);
                if (ErrorCode.NO_AGENT_ONLINE == state) {
                    MQConversationActivity.this.addLeaveMessageTip();
                }
            }

            public void onFailure(BaseMessage failureMessage, int code, String failureInfo) {
                MQConversationActivity.this.updateResendMessage(failureMessage, code);
            }
        });
    }

    private void updateResendMessage(BaseMessage message, int code) {
        int messagePosition = this.mChatMessageList.indexOf(message);
        this.mChatMessageList.remove(message);
        if (this.isBlackState && this.mChatMessageList.size() > messagePosition && ((BaseMessage)
                this.mChatMessageList.get(messagePosition)).getItemViewType() == 3) {
            this.mChatMessageList.remove(messagePosition);
        }
        MQTimeUtils.refreshMQTimeItem(this.mChatMessageList);
        this.mChatMsgAdapter.addMQMessage(message);
        if (code == ErrorCode.BLACKLIST) {
            addBlacklistTip(R.string.mq_blacklist_msg_tips);
        }
        scrollContentToBottom();
    }

    private void renameVoiceFilename(BaseMessage message) {
        if (message instanceof VoiceMessage) {
            VoiceMessage voiceMessage = (VoiceMessage) message;
            MQAudioRecorderManager.renameVoiceFilename(this, voiceMessage.getLocalPath(),
                    voiceMessage.getContent());
            this.mChatMsgAdapter.downloadAndNotifyDataSetChanged(Arrays.asList(new
                    BaseMessage[]{message}));
        }
    }

    private void inputting(String content) {
        this.mController.sendClientInputtingWithContent(content);
    }

    private void cleanVoiceMessage(List<BaseMessage> messageList) {
        if (!MQConfig.isVoiceSwitchOpen && messageList.size() > 0) {
            Iterator<BaseMessage> baseMessageIterator = messageList.iterator();
            while (baseMessageIterator.hasNext()) {
                if ("audio".equals(((BaseMessage) baseMessageIterator.next()).getContentType())) {
                    baseMessageIterator.remove();
                }
            }
        }
    }

    protected void addEvaluateMessageTip(int level, String content) {
        this.mChatMsgAdapter.addMQMessage(new EvaluateMessage(level, content));
    }

    public void executeEvaluate(final int level, final String content) {
        this.mController.executeEvaluate(this.mConversationId, level, content, new SimpleCallback
                () {
            public void onFailure(int code, String message) {
                MQUtils.show(MQConversationActivity.this, R.string.mq_evaluate_failure);
            }

            public void onSuccess() {
                MQConversationActivity.this.addEvaluateMessageTip(level, content);
            }
        });
    }

    public void onAudioRecorderFinish(int time, String filePath) {
        VoiceMessage voiceMessage = new VoiceMessage();
        voiceMessage.setDuration(time);
        voiceMessage.setLocalPath(filePath);
        sendMessage(voiceMessage);
    }

    public void onAudioRecorderTooShort() {
        MQUtils.show((Context) this, R.string.mq_record_record_time_is_short);
    }

    public void scrollContentToBottom() {
        MQUtils.scrollListViewToBottom(this.mConversationListView);
    }

    public void onAudioRecorderNoPermission() {
        MQUtils.show((Context) this, R.string.mq_recorder_no_permission);
    }

    public boolean onTouch(View v, MotionEvent event) {
        hideEmojiSelectIndicator();
        hideVoiceSelectIndicator();
        return false;
    }

    private void updateAgentOnlineOfflineStatus() {
        Agent agent = this.mController.getCurrentAgent();
        if (agent == null) {
            hiddenAgentStatusCircle();
        } else if (!agent.isOnline()) {
            this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                    .mq_shape_agent_status_offline, 0);
        } else if (agent.isOffDuty()) {
            this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                    .mq_shape_agent_status_off_duty, 0);
        } else {
            this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                    .mq_shape_agent_status_online, 0);
        }
    }

    private void hiddenAgentStatusCircle() {
        this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void onFileMessageDownloadFailure(FileMessage fileMessage, int code, String message) {
        if (!this.isDestroy) {
            popTopTip(R.string.mq_download_error);
        }
    }

    public void onFileMessageExpired(FileMessage fileMessage) {
        if (!this.isDestroy) {
            popTopTip(R.string.mq_expired_top_tip);
        }
    }

    private void cancelAllDownload() {
        for (BaseMessage message : this.mChatMessageList) {
            if (message instanceof FileMessage) {
                MQConfig.getController(this).cancelDownload(((FileMessage) message).getUrl());
            }
        }
    }

    private void receiveNewMsg(BaseMessage baseMessage) {
        if (this.mChatMsgAdapter != null && !isDupMessage(baseMessage)) {
            if (!MQConfig.isVoiceSwitchOpen && "audio".equals(baseMessage.getContentType())) {
                return;
            }
            if (!"ending".equals(baseMessage.getType()) || !this.isBlackState) {
                this.mChatMessageList.add(baseMessage);
                MQTimeUtils.refreshMQTimeItem(this.mChatMessageList);
                if (baseMessage instanceof VoiceMessage) {
                    this.mChatMsgAdapter.downloadAndNotifyDataSetChanged(Arrays.asList(new
                            BaseMessage[]{baseMessage}));
                } else {
                    this.mChatMsgAdapter.notifyDataSetChanged();
                }
                if (this.mConversationListView.getLastVisiblePosition() == this.mChatMsgAdapter
                        .getCount() - 2) {
                    MQUtils.scrollListViewToBottom(this.mConversationListView);
                }
                if (!this.isPause && MQConfig.isSoundSwitchOpen) {
                    this.mSoundPoolManager.playSound(R.raw.mq_new_message);
                }
            }
        }
    }

    private boolean isDupMessage(BaseMessage baseMessage) {
        for (BaseMessage message : this.mChatMessageList) {
            if (message.equals(baseMessage)) {
                return true;
            }
        }
        return false;
    }
}
