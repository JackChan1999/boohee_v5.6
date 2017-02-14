package com.boohee.one.video.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.ui.fragment.BaseDialogFragment;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.entity.Mention;
import com.boohee.widgets.ProgressWheel;

public class RestVideoPlayFragment extends BaseDialogFragment {
    @InjectView(2131429366)
    LinearLayout breathLayout;
    @InjectView(2131429364)
    LinearLayout commonErrorLayout;
    private Context context;
    @InjectView(2131429358)
    LinearLayout essentials1Layout;
    @InjectView(2131429360)
    LinearLayout essentials2Layout;
    @InjectView(2131429362)
    LinearLayout essentials3Layout;
    private Mention      mention;
    private OnRestFinish onRestFinish;
    @InjectView(2131427773)
    ProgressWheel progressBar;
    @InjectView(2131428335)
    VideoView     restVideoView;
    @InjectView(2131429367)
    TextView      tvBreath;
    @InjectView(2131429365)
    TextView      tvCommonError;
    @InjectView(2131429359)
    TextView      tvEssentials1;
    @InjectView(2131429361)
    TextView      tvEssentials2;
    @InjectView(2131429363)
    TextView      tvEssentials3;
    @InjectView(2131427514)
    TextView      tvName;
    @InjectView(2131429357)
    TextView      tvNextMention;
    private String videoPath;

    public interface OnRestFinish {
        void restFinish();
    }

    public static RestVideoPlayFragment newInstance(Context context) {
        RestVideoPlayFragment fragment = new RestVideoPlayFragment();
        fragment.context = context;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(0, 16973834);
    }

    public void setMention(Mention mention) {
        this.mention = mention;
        initData();
    }

    private void initData() {
        if (this.mention != null) {
            this.videoPath = VideoDownloadHelper.getInstance().getVideoName(this.context, this
                    .mention.id);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gh, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        this.tvNextMention.setVisibility(0);
        if (this.mention != null) {
            this.tvName.setText(this.mention.name);
            VideoDownloadHelper helper = VideoDownloadHelper.getInstance();
            helper.isNeedShowInfoText(this.essentials1Layout, this.tvEssentials1, this.mention
                    .info.summary1);
            helper.isNeedShowInfoText(this.essentials2Layout, this.tvEssentials2, this.mention
                    .info.summary2);
            helper.isNeedShowInfoText(this.essentials3Layout, this.tvEssentials3, this.mention
                    .info.summary3);
            helper.isNeedShowInfoText(this.commonErrorLayout, this.tvCommonError, this.mention
                    .info.common_errors);
            helper.isNeedShowInfoText(this.breathLayout, this.tvBreath, this.mention.info.breath);
        }
    }

    public void onResume() {
        super.onResume();
        playVideo();
    }

    private void playVideo() {
        if (!TextUtils.isEmpty(this.videoPath)) {
            this.restVideoView.setVideoPath(this.videoPath);
            this.restVideoView.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            this.restVideoView.start();
        }
    }

    @OnClick({2131428336})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                if (this.onRestFinish != null) {
                    this.onRestFinish.restFinish();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void stopRest() {
        this.restVideoView.stopPlayback();
        this.restVideoView.clearFocus();
        dismiss();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setOnRestFinish(OnRestFinish onRestFinish) {
        this.onRestFinish = onRestFinish;
    }

    public void setCount(String restTime, int restTimeCount) {
        if (restTimeCount == 0) {
            throw new IllegalArgumentException("restTimeCount zero");
        }
        int progress = (Integer.valueOf(restTime).intValue() * 360) / restTimeCount;
        this.progressBar.setTextInCenter(restTime);
        this.progressBar.setProgress(progress);
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
