package com.meiqia.meiqiasdk.util;

import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.sys.a;
import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.activity.MQPhotoPreviewActivity;
import com.meiqia.meiqiasdk.model.AgentChangeMessage;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.EvaluateMessage;
import com.meiqia.meiqiasdk.model.FileMessage;
import com.meiqia.meiqiasdk.model.PhotoMessage;
import com.meiqia.meiqiasdk.model.VoiceMessage;
import com.meiqia.meiqiasdk.util.MQAudioPlayerManager.Callback;
import com.meiqia.meiqiasdk.util.MQConfig.ui;
import com.meiqia.meiqiasdk.util.MQImageLoader.MQDisplayImageListener;
import com.meiqia.meiqiasdk.widget.MQChatFileItem;
import com.meiqia.meiqiasdk.widget.MQImageView;

import java.io.File;
import java.util.List;

public class MQChatAdapter extends BaseAdapter {
    private static final int    NO_POSITION = -1;
    private static final String TAG         = MQChatAdapter.class.getSimpleName();
    private ListView listView;
    private int mCurrentDownloadingItemPosition = -1;
    private int mCurrentPlayingItemPosition     = -1;
    private int mImageHeight;
    private int mImageWidth;
    private int mMaxItemWidth;
    private int mMinItemWidth;
    private Runnable mNotifyDataSetChangedRunnable = new Runnable() {
        public void run() {
            MQChatAdapter.this.notifyDataSetChanged();
        }
    };
    private List<BaseMessage>      mcMessageList;
    private MQConversationActivity mqConversationActivity;

    static class EvaluateViewHolder {
        TextView  contentTv;
        View      levelBg;
        ImageView levelImg;
        TextView  levelTv;

        EvaluateViewHolder() {
        }
    }

    private class FailedMessageOnClickListener implements OnClickListener {
        private BaseMessage failedMessage;

        public FailedMessageOnClickListener(BaseMessage failedMessage) {
            this.failedMessage = failedMessage;
        }

        public void onClick(View v) {
            if (!MQUtils.isFastClick()) {
                this.failedMessage.setStatus("sending");
                MQChatAdapter.this.notifyDataSetChanged();
                MQChatAdapter.this.mqConversationActivity.resendMessage(this.failedMessage);
            }
        }
    }

    static class TimeViewHolder {
        TextView timeTv;

        TimeViewHolder() {
        }
    }

    static class TipViewHolder {
        TextView contentTv;

        TipViewHolder() {
        }
    }

    static class ViewHolder {
        MQChatFileItem chatFileItem;
        MQImageView    contentImage;
        TextView       contentText;
        ImageView      sendState;
        ProgressBar    sendingProgressBar;
        View           unreadCircle;
        MQImageView    usAvatar;
        ImageView      voiceAnimIv;
        View           voiceContainerRl;
        TextView       voiceContentTv;

        ViewHolder() {
        }
    }

    public MQChatAdapter(MQConversationActivity mqConversationActivity, List<BaseMessage>
            mcMessageList, ListView listView) {
        this.mqConversationActivity = mqConversationActivity;
        this.mcMessageList = mcMessageList;
        this.listView = listView;
        int screenWidth = MQUtils.getScreenWidth(listView.getContext());
        this.mMaxItemWidth = (int) (((float) screenWidth) * 0.5f);
        this.mMinItemWidth = (int) (((float) screenWidth) * 0.18f);
        this.mImageWidth = screenWidth / 3;
        this.mImageHeight = this.mImageWidth;
    }

    public void addMQMessage(BaseMessage baseMessage) {
        this.mcMessageList.add(baseMessage);
        notifyDataSetChanged();
    }

    public void addMQMessage(BaseMessage baseMessage, int location) {
        this.mcMessageList.add(location, baseMessage);
        notifyDataSetChanged();
    }

    public void loadMoreMessage(List<BaseMessage> baseMessages) {
        this.mcMessageList.addAll(0, baseMessages);
        notifyDataSetChanged();
        downloadAndNotifyDataSetChanged(baseMessages);
    }

    public int getItemViewType(int position) {
        return ((BaseMessage) this.mcMessageList.get(position)).getItemViewType();
    }

    public int getViewTypeCount() {
        return 5;
    }

    public int getCount() {
        return this.mcMessageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BaseMessage mcMessage = (BaseMessage) this.mcMessageList.get(position);
        ViewHolder viewHolder = null;
        TimeViewHolder timeViewHolder = null;
        TipViewHolder tipViewHolder = null;
        EvaluateViewHolder evaluateViewHolder = null;
        if (convertView != null) {
            switch (getItemViewType(position)) {
                case 0:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case 1:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                case 2:
                    timeViewHolder = (TimeViewHolder) convertView.getTag();
                    break;
                case 3:
                    tipViewHolder = (TipViewHolder) convertView.getTag();
                    break;
                case 4:
                    evaluateViewHolder = (EvaluateViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case 0:
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(this.mqConversationActivity).inflate(R.layout
                        .mq_item_chat_right, null);
                viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
                viewHolder.contentImage = (MQImageView) convertView.findViewById(R.id.content_pic);
                viewHolder.voiceContentTv = (TextView) convertView.findViewById(R.id
                        .tv_voice_content);
                viewHolder.voiceAnimIv = (ImageView) convertView.findViewById(R.id.iv_voice_anim);
                viewHolder.voiceContainerRl = convertView.findViewById(R.id.rl_voice_container);
                viewHolder.sendingProgressBar = (ProgressBar) convertView.findViewById(R.id
                        .progress_bar);
                viewHolder.sendState = (ImageView) convertView.findViewById(R.id.send_state);
                viewHolder.chatFileItem = (MQChatFileItem) convertView.findViewById(R.id
                        .file_container);
                configChatBubbleBg(viewHolder.contentText, false);
                configChatBubbleBg(viewHolder.voiceContentTv, false);
                configChatBubbleTextColor(viewHolder.contentText, false);
                configChatBubbleTextColor(viewHolder.voiceContentTv, false);
                convertView.setTag(viewHolder);
                break;
            case 1:
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(this.mqConversationActivity).inflate(R.layout
                        .mq_item_chat_left, null);
                viewHolder.contentText = (TextView) convertView.findViewById(R.id.content_text);
                viewHolder.contentImage = (MQImageView) convertView.findViewById(R.id.content_pic);
                viewHolder.voiceContentTv = (TextView) convertView.findViewById(R.id
                        .tv_voice_content);
                viewHolder.voiceAnimIv = (ImageView) convertView.findViewById(R.id.iv_voice_anim);
                viewHolder.voiceContainerRl = convertView.findViewById(R.id.rl_voice_container);
                viewHolder.usAvatar = (MQImageView) convertView.findViewById(R.id.us_avatar_iv);
                viewHolder.unreadCircle = convertView.findViewById(R.id.unread_view);
                viewHolder.chatFileItem = (MQChatFileItem) convertView.findViewById(R.id
                        .file_container);
                configChatBubbleBg(viewHolder.contentText, true);
                configChatBubbleBg(viewHolder.voiceContentTv, true);
                configChatBubbleTextColor(viewHolder.contentText, true);
                configChatBubbleTextColor(viewHolder.voiceContentTv, true);
                convertView.setTag(viewHolder);
                break;
            case 2:
                timeViewHolder = new TimeViewHolder();
                convertView = LayoutInflater.from(this.mqConversationActivity).inflate(R.layout
                        .mq_item_chat_time, null);
                timeViewHolder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
                convertView.setTag(timeViewHolder);
                break;
            case 3:
                tipViewHolder = new TipViewHolder();
                convertView = LayoutInflater.from(this.mqConversationActivity).inflate(R.layout
                        .mq_item_msg_tip, null, false);
                tipViewHolder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
                convertView.setTag(tipViewHolder);
                break;
            case 4:
                evaluateViewHolder = new EvaluateViewHolder();
                convertView = LayoutInflater.from(this.mqConversationActivity).inflate(R.layout
                        .mq_item_msg_evaluate, null, false);
                evaluateViewHolder.levelTv = (TextView) convertView.findViewById(R.id
                        .tv_msg_evaluate_level);
                evaluateViewHolder.levelBg = convertView.findViewById(R.id.view_msg_evaluate_level);
                evaluateViewHolder.levelImg = (ImageView) convertView.findViewById(R.id
                        .ic_msg_evaluate_level);
                evaluateViewHolder.contentTv = (TextView) convertView.findViewById(R.id
                        .tv_msg_evaluate_content);
                convertView.setTag(evaluateViewHolder);
                break;
        }
        if (getItemViewType(position) != 2) {
            if (getItemViewType(position) != 3) {
                if (getItemViewType(position) != 4) {
                    if (getItemViewType(position) == 1 || getItemViewType(position) == 0) {
                        holderState(viewHolder, mcMessage.getContentType());
                        String contentType = mcMessage.getContentType();
                        Object obj = -1;
                        switch (contentType.hashCode()) {
                            case 3143036:
                                if (contentType.equals("file")) {
                                    obj = 3;
                                    break;
                                }
                                break;
                            case 3556653:
                                if (contentType.equals("text")) {
                                    obj = null;
                                    break;
                                }
                                break;
                            case 93166550:
                                if (contentType.equals("audio")) {
                                    obj = 2;
                                    break;
                                }
                                break;
                            case 106642994:
                                if (contentType.equals("photo")) {
                                    obj = 1;
                                    break;
                                }
                                break;
                        }
                        switch (obj) {
                            case null:
                                if (!TextUtils.isEmpty(mcMessage.getContent())) {
                                    viewHolder.contentText.setText(MQEmotionUtil.getEmotionText
                                            (this.mqConversationActivity, mcMessage.getContent(),
                                                    20));
                                    break;
                                }
                                break;
                            case 1:
                                String url;
                                if (MQUtils.isFileExist(((PhotoMessage) mcMessage).getLocalPath()
                                )) {
                                    url = ((PhotoMessage) mcMessage).getLocalPath();
                                } else {
                                    url = ((PhotoMessage) mcMessage).getUrl();
                                }
                                MQConfig.getImageLoader(this.mqConversationActivity).displayImage
                                        (viewHolder.contentImage, url, R.drawable
                                                .mq_ic_holder_light, R.drawable
                                                .mq_ic_holder_light, this.mImageWidth, this
                                                .mImageHeight, new MQDisplayImageListener() {
                                    public void onSuccess(View view, final String url) {
                                        if (MQChatAdapter.this.listView.getLastVisiblePosition()
                                                == MQChatAdapter.this.getCount() - 1) {
                                            MQChatAdapter.this.listView.setSelection
                                                    (MQChatAdapter.this.getCount() - 1);
                                        }
                                        view.setOnClickListener(new OnClickListener() {
                                            public void onClick(View arg0) {
                                                MQChatAdapter.this.mqConversationActivity
                                                        .startActivity(MQPhotoPreviewActivity
                                                                .newIntent(MQChatAdapter.this
                                                                        .mqConversationActivity,
                                                                        MQUtils.getImageDir
                                                                                (MQChatAdapter
                                                                                        .this
                                                                                        .mqConversationActivity), url));
                                            }
                                        });
                                    }
                                });
                                break;
                            case 2:
                                handleBindVoiceItem(viewHolder, (VoiceMessage) mcMessage, position);
                                break;
                            case 3:
                                handleBindFileItem(viewHolder, (FileMessage) mcMessage);
                                break;
                        }
                        if (getItemViewType(position) != 1) {
                            if (getItemViewType(position) == 0 && viewHolder.sendingProgressBar
                                    != null) {
                                contentType = mcMessage.getStatus();
                                obj = -1;
                                switch (contentType.hashCode()) {
                                    case -1281977283:
                                        if (contentType.equals("failed")) {
                                            obj = 2;
                                            break;
                                        }
                                        break;
                                    case -734206867:
                                        if (contentType.equals("arrived")) {
                                            obj = 1;
                                            break;
                                        }
                                        break;
                                    case 1979923290:
                                        if (contentType.equals("sending")) {
                                            obj = null;
                                            break;
                                        }
                                        break;
                                }
                                switch (obj) {
                                    case null:
                                        viewHolder.sendingProgressBar.setVisibility(0);
                                        viewHolder.sendState.setVisibility(8);
                                        break;
                                    case 1:
                                        viewHolder.sendingProgressBar.setVisibility(8);
                                        viewHolder.sendState.setVisibility(8);
                                        break;
                                    case 2:
                                        viewHolder.sendingProgressBar.setVisibility(8);
                                        viewHolder.sendState.setVisibility(0);
                                        viewHolder.sendState.setBackgroundResource(R.drawable
                                                .mq_ic_msg_failed);
                                        viewHolder.sendState.setOnClickListener(new
                                                FailedMessageOnClickListener(mcMessage));
                                        viewHolder.sendState.setTag(Long.valueOf(mcMessage.getId
                                                ()));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        MQConfig.getImageLoader(this.mqConversationActivity).displayImage
                                (viewHolder.usAvatar, mcMessage.getAvatar(), R.drawable
                                        .mq_ic_holder_avatar, R.drawable.mq_ic_holder_avatar,
                                        100, 100, null);
                    }
                } else {
                    handleBindEvaluateItem(evaluateViewHolder, (EvaluateMessage) mcMessage);
                }
            } else if (mcMessage instanceof AgentChangeMessage) {
                setDirectionMessageContent(mcMessage.getAgentNickname(), tipViewHolder.contentTv);
            } else {
                tipViewHolder.contentTv.setText(mcMessage.getContent());
            }
        } else {
            timeViewHolder.timeTv.setText(MQTimeUtils.parseTime(mcMessage.getCreatedOn()));
        }
        return convertView;
    }

    private void setDirectionMessageContent(String agentNickName, TextView tipTv) {
        if (agentNickName != null) {
            String text = String.format(tipTv.getResources().getString(R.string
                    .mq_direct_content), new Object[]{agentNickName});
            int start = text.indexOf(agentNickName);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new ForegroundColorSpan(tipTv.getResources().getColor(R.color
                    .mq_chat_direct_agent_nickname_textColor)), start, agentNickName.length() +
                    start, 34);
            tipTv.setText(style);
        }
    }

    private void holderState(ViewHolder viewHolder, String state) {
        viewHolder.contentText.setVisibility(8);
        viewHolder.contentImage.setVisibility(8);
        viewHolder.voiceContainerRl.setVisibility(8);
        viewHolder.chatFileItem.setVisibility(8);
        int i = -1;
        switch (state.hashCode()) {
            case 3143036:
                if (state.equals("file")) {
                    i = 3;
                    break;
                }
                break;
            case 3556653:
                if (state.equals("text")) {
                    i = 0;
                    break;
                }
                break;
            case 93166550:
                if (state.equals("audio")) {
                    i = 2;
                    break;
                }
                break;
            case 106642994:
                if (state.equals("photo")) {
                    i = 1;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                viewHolder.contentText.setVisibility(0);
                return;
            case 1:
                viewHolder.contentImage.setVisibility(0);
                return;
            case 2:
                viewHolder.voiceContainerRl.setVisibility(0);
                return;
            case 3:
                viewHolder.chatFileItem.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void handleBindEvaluateItem(EvaluateViewHolder evaluateViewHolder, EvaluateMessage
            evaluateMessage) {
        switch (evaluateMessage.getLevel()) {
            case 0:
                evaluateViewHolder.levelImg.setImageResource(R.drawable.mq_ic_angry_face);
                evaluateViewHolder.levelTv.setText(R.string.mq_evaluate_bad);
                evaluateViewHolder.levelBg.setBackgroundResource(R.drawable
                        .mq_shape_evaluate_angry);
                break;
            case 1:
                evaluateViewHolder.levelImg.setImageResource(R.drawable.mq_ic_neutral_face);
                evaluateViewHolder.levelTv.setText(R.string.mq_evaluate_medium);
                evaluateViewHolder.levelBg.setBackgroundResource(R.drawable
                        .mq_shape_evaluate_neutral);
                break;
            case 2:
                evaluateViewHolder.levelImg.setImageResource(R.drawable.mq_ic_smiling_face);
                evaluateViewHolder.levelTv.setText(R.string.mq_evaluate_good);
                evaluateViewHolder.levelBg.setBackgroundResource(R.drawable
                        .mq_shape_evaluate_smiling);
                break;
        }
        String context = evaluateMessage.getContent();
        if (TextUtils.isEmpty(context)) {
            evaluateViewHolder.contentTv.setVisibility(8);
            return;
        }
        evaluateViewHolder.contentTv.setVisibility(0);
        evaluateViewHolder.contentTv.setText(context);
    }

    private void handleBindVoiceItem(ViewHolder viewHolder, final VoiceMessage voiceMessage,
                                     final int position) {
        viewHolder.voiceContainerRl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MQChatAdapter.this.handleClickVoiceBtn(voiceMessage, position);
            }
        });
        viewHolder.voiceContentTv.setText(voiceMessage.getDuration() == -1 ? "" : voiceMessage
                .getDuration() + "s");
        LayoutParams layoutParams = viewHolder.voiceContainerRl.getLayoutParams();
        if (voiceMessage.getDuration() == -1) {
            viewHolder.voiceContentTv.setText("");
            layoutParams.width = this.mMinItemWidth;
        } else {
            viewHolder.voiceContentTv.setText(voiceMessage.getDuration() + a.e);
            layoutParams.width = (int) (((float) this.mMinItemWidth) + ((((float) this
                    .mMaxItemWidth) / 60.0f) * ((float) voiceMessage.getDuration())));
        }
        viewHolder.voiceContainerRl.setLayoutParams(layoutParams);
        if (this.mCurrentPlayingItemPosition == position) {
            if (voiceMessage.getItemViewType() == 1) {
                viewHolder.voiceAnimIv.setImageResource(R.drawable.mq_anim_voice_left_playing);
            } else {
                viewHolder.voiceAnimIv.setImageResource(R.drawable.mq_anim_voice_right_playing);
            }
            ((AnimationDrawable) viewHolder.voiceAnimIv.getDrawable()).start();
        } else if (voiceMessage.getItemViewType() == 1) {
            viewHolder.voiceAnimIv.setImageResource(R.drawable.mq_voice_left_normal);
            viewHolder.voiceAnimIv.setColorFilter(this.mqConversationActivity.getResources()
                    .getColor(R.color.mq_chat_left_textColor));
        } else {
            viewHolder.voiceAnimIv.setImageResource(R.drawable.mq_voice_right_normal);
            viewHolder.voiceAnimIv.setColorFilter(this.mqConversationActivity.getResources()
                    .getColor(R.color.mq_chat_right_textColor));
        }
        if (viewHolder.unreadCircle == null) {
            return;
        }
        if (voiceMessage.isRead()) {
            viewHolder.unreadCircle.setVisibility(8);
        } else {
            viewHolder.unreadCircle.setVisibility(0);
        }
    }

    private void handleClickVoiceBtn(VoiceMessage voiceMessage, int position) {
        if (TextUtils.isEmpty(voiceMessage.getLocalPath())) {
            stopPlayVoice();
            downloadAndPlayVoice(voiceMessage, position);
        } else if (MQAudioPlayerManager.isPlaying() && this.mCurrentPlayingItemPosition ==
                position) {
            stopPlayVoice();
        } else {
            startPlayVoiceAndRefreshList(voiceMessage, position);
        }
    }

    public void stopPlayVoice() {
        MQAudioPlayerManager.stop();
        this.mCurrentPlayingItemPosition = -1;
        notifyDataSetChanged();
    }

    private void startPlayVoiceAndRefreshList(VoiceMessage voiceMessage, int position) {
        MQAudioPlayerManager.playSound(voiceMessage.getLocalPath(), new Callback() {
            public void onError() {
                MQChatAdapter.this.mCurrentPlayingItemPosition = -1;
                MQChatAdapter.this.notifyDataSetChanged();
            }

            public void onCompletion() {
                MQChatAdapter.this.mCurrentPlayingItemPosition = -1;
                MQChatAdapter.this.notifyDataSetChanged();
            }
        });
        voiceMessage.setIsRead(true);
        MQConfig.getController(this.mqConversationActivity).updateMessage(voiceMessage.getId(),
                true);
        this.mCurrentPlayingItemPosition = position;
        notifyDataSetChanged();
    }

    private void downloadAndPlayVoice(final VoiceMessage voiceMessage, final int position) {
        this.mCurrentDownloadingItemPosition = position;
        MQDownloadManager.getInstance(this.mqConversationActivity).downloadVoice(voiceMessage
                .getUrl(), new MQDownloadManager.Callback() {
            public void onSuccess(File file) {
                MQChatAdapter.this.setVoiceMessageDuration(voiceMessage, file.getAbsolutePath());
                MQChatAdapter.this.listView.post(new Runnable() {
                    public void run() {
                        if (MQChatAdapter.this.mCurrentDownloadingItemPosition == position) {
                            MQChatAdapter.this.startPlayVoiceAndRefreshList(voiceMessage, position);
                        }
                    }
                });
            }

            public void onFailure() {
                MQUtils.showSafe(MQChatAdapter.this.mqConversationActivity, R.string
                        .mq_download_audio_failure);
            }
        });
    }

    public void downloadAndNotifyDataSetChanged(List<BaseMessage> baseMessages) {
        for (BaseMessage baseMessage : baseMessages) {
            if (baseMessage instanceof VoiceMessage) {
                File voiceFile;
                final VoiceMessage voiceMessage = (VoiceMessage) baseMessage;
                File localFile = null;
                if (!TextUtils.isEmpty(voiceMessage.getLocalPath())) {
                    localFile = new File(voiceMessage.getLocalPath());
                }
                if (localFile == null || !localFile.exists()) {
                    voiceFile = MQAudioRecorderManager.getCachedVoiceFileByUrl(this
                            .mqConversationActivity, voiceMessage.getUrl());
                } else {
                    voiceFile = localFile;
                }
                if (voiceFile == null || !voiceFile.exists()) {
                    MQDownloadManager.getInstance(this.mqConversationActivity).downloadVoice
                            (voiceMessage.getUrl(), new MQDownloadManager.Callback() {
                        public void onSuccess(File file) {
                            MQChatAdapter.this.setVoiceMessageDuration(voiceMessage, file
                                    .getAbsolutePath());
                            MQChatAdapter.this.listView.post(MQChatAdapter.this
                                    .mNotifyDataSetChangedRunnable);
                        }

                        public void onFailure() {
                        }
                    });
                } else {
                    setVoiceMessageDuration(voiceMessage, voiceFile.getAbsolutePath());
                    notifyDataSetChanged();
                }
            }
        }
    }

    private void setVoiceMessageDuration(VoiceMessage voiceMessage, String audioFilePath) {
        voiceMessage.setLocalPath(audioFilePath);
        voiceMessage.setDuration(MQAudioPlayerManager.getDurationByFilePath(this
                .mqConversationActivity, audioFilePath));
    }

    private void handleBindFileItem(ViewHolder viewHolder, FileMessage fileMessage) {
        viewHolder.chatFileItem.setFileStateCallback(this.mqConversationActivity);
        viewHolder.chatFileItem.initFileItem(this, fileMessage);
        switch (fileMessage.getFileState()) {
            case 0:
                viewHolder.chatFileItem.downloadSuccessState();
                return;
            case 1:
                viewHolder.chatFileItem.downloadingState();
                viewHolder.chatFileItem.setProgress(fileMessage.getProgress());
                return;
            case 2:
                viewHolder.chatFileItem.downloadInitState();
                return;
            case 3:
                viewHolder.chatFileItem.downloadFailedState();
                return;
            default:
                return;
        }
    }

    private void configChatBubbleBg(View view, boolean isLeft) {
        if (isLeft) {
            MQUtils.applyCustomUITintDrawable(view, R.color.mq_chat_left_bubble_final, R.color
                    .mq_chat_left_bubble, ui.leftChatBubbleColorResId);
        } else {
            MQUtils.applyCustomUITintDrawable(view, R.color.mq_chat_right_bubble_final, R.color
                    .mq_chat_right_bubble, ui.rightChatBubbleColorResId);
        }
    }

    private void configChatBubbleTextColor(TextView textView, boolean isLeft) {
        if (isLeft) {
            MQUtils.applyCustomUITextAndImageColor(R.color.mq_chat_left_textColor, ui
                    .leftChatTextColorResId, null, textView);
            return;
        }
        MQUtils.applyCustomUITextAndImageColor(R.color.mq_chat_right_textColor, ui.rightChatTextColorResId, null, textView);
    }
}
