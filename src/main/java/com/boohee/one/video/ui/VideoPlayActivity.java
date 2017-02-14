package com.boohee.one.video.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BaseActivity;
import com.boohee.one.ui.fragment.HomeNewFragment;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.entity.Lesson;
import com.boohee.one.video.entity.Mention;
import com.boohee.one.video.fragment.SpecialTrainPlanFragment;
import com.boohee.one.video.fragment.SportPlanFragment;
import com.boohee.one.video.manager.BGMPlayerManager;
import com.boohee.one.video.manager.DesPlayerManager;
import com.boohee.one.video.manager.DesPlayerManager.OnPrepareError;
import com.boohee.one.video.manager.VideoPreference;
import com.boohee.one.video.ui.RestVideoPlayFragment.OnRestFinish;
import com.boohee.one.video.view.ProgressBarHintView;
import com.boohee.utils.Helper;
import com.boohee.widgets.LightAlertDialog;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayActivity extends BaseActivity {
    public static final String KEY_LESSON  = "KEY_LESSON";
    private             String BGM_FIT     = "bgm_fit";
    private             String BGM_UP      = "bgm_up";
    private final       int    RETRY_COUNT = 5;
    private final       String TAG         = VideoPlayActivity.class.getSimpleName();
    @InjectView(2131427922)
    LinearLayout bgmControlLayout;
    private String           bgmName;
    private BGMPlayerManager bgmPlayerManager;
    @InjectView(2131429366)
    LinearLayout   breathLayout;
    @InjectView(2131427925)
    ImageView      btnCloseBgm;
    @InjectView(2131427918)
    ImageView      btnOpenBgmControl;
    @InjectView(2131429364)
    LinearLayout   commonErrorLayout;
    @InjectView(2131427908)
    RelativeLayout controllerView;
    private Runnable countDownRunnable = new Runnable() {
        public void run() {
            Helper.showLog(VideoPlayActivity.this.TAG, "countDown : playCountNum : " +
                    VideoPlayActivity.this.playCountNum);
            VideoPlayActivity.this.refreshMentionCount((VideoPlayActivity.this.playCountNum >= 10
                    ? Integer.valueOf(VideoPlayActivity.this.playCountNum) : "0" +
                    VideoPlayActivity.this.playCountNum) + "/" + VideoPlayActivity.this
                    .currentMention.number);
            VideoPlayActivity.this.desPlayerManager.playDesMusic(VideoPlayActivity.this.ctx,
                    String.valueOf(VideoPlayActivity.this.playCountNum), null);
            VideoPlayActivity.this.updateProgress();
        }
    };
    private Mention currentMention;
    private int     desMusicIndex;
    private ArrayList<String> desMusics = new ArrayList();
    private DesPlayerManager desPlayerManager;
    @InjectView(2131429358)
    LinearLayout essentials1Layout;
    @InjectView(2131429360)
    LinearLayout essentials2Layout;
    @InjectView(2131429362)
    LinearLayout essentials3Layout;
    private Handler handler   = new Handler();
    private boolean isBgmOpen = true;
    private boolean isControllerViewHide;
    private boolean isCounting;
    private boolean isPaused;
    private Lesson  lesson;
    private int     mentionIndex;
    private List<Mention> mentionList = new ArrayList();
    private long mentionTimePaused;
    @InjectView(2131427919)
    RelativeLayout pauseLayout;
    private int playCountNum = 1;
    private VideoPreference preference;
    private ObjectAnimator  progressAnimator;
    @InjectView(2131427773)
    ProgressBar         progressBar;
    @InjectView(2131427913)
    ProgressBarHintView progressBarDivider;
    Runnable restRunnable = new Runnable() {
        public void run() {
            if (VideoPlayActivity.this.restTime > 0) {
                VideoPlayActivity.this.restTime = VideoPlayActivity.this.restTime - 1;
                VideoPlayActivity.this.restVideoPlayFragment.setCount(String.valueOf
                        (VideoPlayActivity.this.restTime), VideoPlayActivity.this.currentMention
                        .rest);
                Helper.showLog(VideoPlayActivity.this.TAG, "mention rest time : " +
                        VideoPlayActivity.this.restTime);
                VideoPlayActivity.this.handler.postDelayed(VideoPlayActivity.this.restRunnable,
                        1000);
                return;
            }
            VideoPlayActivity.this.restFinish();
        }
    };
    private int                   restTime;
    private RestVideoPlayFragment restVideoPlayFragment;
    private int                   retry;
    @InjectView(2131427907)
    RelativeLayout rootVideo;
    private Runnable timeCountDownRunnable = new Runnable() {
        public void run() {
            if (!VideoPlayActivity.this.currentMention.is_times) {
                Helper.showLog(VideoPlayActivity.this.TAG, "chronometer : playCountNum : " +
                        VideoPlayActivity.this.playCountNum);
                if (VideoPlayActivity.this.isCounting) {
                    VideoPlayActivity.this.updateProgress();
                    if (VideoPlayActivity.this.playCountNum <= VideoPlayActivity.this
                            .currentMention.number) {
                        VideoPlayActivity.this.refreshMentionCount((VideoPlayActivity.this
                                .playCountNum >= 10 ? Integer.valueOf(VideoPlayActivity.this
                                .playCountNum) : "0" + VideoPlayActivity.this.playCountNum) + "/"
                                + VideoPlayActivity.this.currentMention.number);
                    }
                    if (VideoPlayActivity.this.currentMention.number < 30) {
                        VideoPlayActivity.this.desPlayerManager.playDesMusic(VideoPlayActivity
                                .this.ctx, "didi");
                    } else if (VideoPlayActivity.this.currentMention.number - VideoPlayActivity
                            .this.playCountNum == 12) {
                        VideoPlayActivity.this.desPlayerManager.playDesMusic(VideoPlayActivity
                                .this.ctx, "d_last_ten_seconds");
                    } else if (VideoPlayActivity.this.currentMention.number - VideoPlayActivity
                            .this.playCountNum != 11) {
                        VideoPlayActivity.this.desPlayerManager.playDesMusic(VideoPlayActivity
                                .this.ctx, "didi");
                    }
                }
                if (VideoPlayActivity.this.isMentionComplete()) {
                    VideoPlayActivity.this.handler.removeCallbacks(VideoPlayActivity.this
                            .timeCountDownRunnable);
                    return;
                }
                VideoPlayActivity.this.playCountNum = VideoPlayActivity.this.playCountNum + 1;
                VideoPlayActivity.this.handler.postDelayed(VideoPlayActivity.this
                        .timeCountDownRunnable, 1000);
            }
        }
    };
    private int  totalMetionCount;
    private long totalTimePaused;
    @InjectView(2131427927)
    TextView    tvBgmFit;
    @InjectView(2131427924)
    TextView    tvBgmState;
    @InjectView(2131427928)
    TextView    tvBgmUp;
    @InjectView(2131429367)
    TextView    tvBreath;
    @InjectView(2131429365)
    TextView    tvCommonError;
    @InjectView(2131429359)
    TextView    tvEssentials1;
    @InjectView(2131429361)
    TextView    tvEssentials2;
    @InjectView(2131429363)
    TextView    tvEssentials3;
    @InjectView(2131427915)
    TextView    tvGroupCount;
    @InjectView(2131427916)
    TextView    tvMentionCount;
    @InjectView(2131427914)
    Chronometer tvMentionTime;
    @InjectView(2131427909)
    TextView    tvMentionTitle;
    @InjectView(2131427926)
    TextView    tvMoreBgm;
    @InjectView(2131427514)
    TextView    tvName;
    @InjectView(2131427912)
    Chronometer tvTotalTime;
    private VideoDownloadHelper videoDownloadHelper = VideoDownloadHelper.getInstance();
    private String videoPath;
    @InjectView(2131427772)
    VideoView videoView;

    static /* synthetic */ int access$1604(VideoPlayActivity x0) {
        int i = x0.retry + 1;
        x0.retry = i;
        return i;
    }

    public static void comeOn(Context context, Lesson lesson) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("KEY_LESSON", lesson);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView(LayoutInflater.from(this.ctx).inflate(R.layout.da, null));
        ButterKnife.inject((Activity) this);
        getWindow().addFlags(128);
        this.bgmPlayerManager = BGMPlayerManager.getInstance();
        this.preference = new VideoPreference(this);
        initDesPlayerManager();
        initData();
        initView();
        initListener();
    }

    private void initDesPlayerManager() {
        this.desPlayerManager = DesPlayerManager.getInstance();
        this.desPlayerManager.setOnPrepareError(new OnPrepareError() {
            public void onPrepareError() {
                VideoPlayActivity.this.onPlayError();
            }
        });
    }

    private void initData() {
        if (getIntent() != null) {
            this.lesson = (Lesson) getIntent().getParcelableExtra("KEY_LESSON");
            if (this.lesson != null) {
                for (int i = 0; i < this.lesson.mentions.size(); i++) {
                    Mention mention = (Mention) this.lesson.mentions.get(i);
                    for (int groupIndex = 0; groupIndex < mention.group_count; groupIndex++) {
                        this.mentionList.add(mention);
                    }
                }
                this.totalMetionCount = this.mentionList.size();
            }
        }
    }

    private void initView() {
        if (this.totalMetionCount > 0) {
            this.progressBarDivider.setDrawCount(this.totalMetionCount);
        }
    }

    private void initDesMusic() {
        if (this.desMusics.size() > 0) {
            this.desPlayerManager.stopPlayback();
            this.desMusics.clear();
        }
        this.currentMention = (Mention) this.mentionList.get(this.mentionIndex);
        if (this.mentionIndex == 0) {
            this.desMusics.add("d_lesson_will_start");
            List<String> strings = this.videoDownloadHelper.getNumberAudioSplit(this.lesson
                    .basic_calorie);
            for (int i = 0; i < strings.size(); i++) {
                this.desMusics.add(strings.get(i));
            }
            this.desMusics.add("unit_calory");
            this.desMusics.add("d_first_action");
        } else if (this.mentionIndex == this.totalMetionCount - 1) {
            this.desMusics.add("d_last_action");
        } else {
            this.desMusics.add("d_next_action");
        }
        this.desMusics.add(VideoDownloadHelper.AUDIO_NAME + this.currentMention.id +
                VideoDownloadHelper.AUDIO_FORMAT);
        this.desMusics.add(String.valueOf(this.currentMention.number));
        if (this.currentMention.is_times) {
            this.desMusics.add("unit_piece");
        } else {
            this.desMusics.add("unit_second");
        }
        this.desMusics.add("blank_1s");
        this.desMusics.add("5");
        this.desMusics.add("4");
        this.desMusics.add("3");
        this.desMusics.add("2");
        this.desMusics.add("1");
        this.desMusics.add("d_start");
        this.desMusicIndex = 0;
    }

    private void initVideoData() {
        if (this.mentionIndex >= this.totalMetionCount) {
            lessonFinish();
            return;
        }
        this.currentMention = (Mention) this.mentionList.get(this.mentionIndex);
        this.videoPath = this.videoDownloadHelper.getVideoName(this.ctx, this.currentMention.id);
        this.videoView.setVideoPath(this.videoPath);
        this.videoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                VideoPlayActivity.this.onPlayError();
                return true;
            }
        });
        this.videoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (VideoPlayActivity.this.currentMention.is_times && !VideoPlayActivity.this
                        .isMentionComplete() && VideoPlayActivity.this.isCounting) {
                    VideoPlayActivity.this.handler.post(VideoPlayActivity.this.countDownRunnable);
                    VideoPlayActivity.this.playCountNum = VideoPlayActivity.this.playCountNum + 1;
                }
                VideoPlayActivity.this.videoView.seekTo(0);
                VideoPlayActivity.this.videoView.start();
            }
        });
    }

    private void onPlayError() {
        stopLesson();
        LightAlertDialog dialog = LightAlertDialog.create(this.ctx, (int) R.string.zi)
                .setPositiveButton((int) R.string.y8, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VideoPlayActivity.this.finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void updateProgress() {
        if (this.progressAnimator != null && this.progressAnimator.isRunning()) {
            this.progressAnimator.end();
        }
        double progress = (double) (((this.mentionIndex * 1000) + ((this.playCountNum * 1000) /
                this.currentMention.number)) / this.totalMetionCount);
        this.progressAnimator = ObjectAnimator.ofInt(this.progressBar, "progress", new int[]{
                (int) progress});
        if (this.currentMention.is_times) {
            this.progressAnimator.setDuration(((long) this.currentMention.rate) * 1000);
        } else {
            this.progressAnimator.setDuration(1000);
        }
        this.progressAnimator.setInterpolator(new LinearInterpolator());
        this.progressAnimator.start();
    }

    private boolean isMentionComplete() {
        if (this.playCountNum < this.currentMention.number) {
            return false;
        }
        this.isCounting = false;
        this.playCountNum = 1;
        this.tvMentionTime.stop();
        if (this.currentMention.rest > 0) {
            this.videoView.stopPlayback();
            this.videoView.clearFocus();
            this.restTime = this.currentMention.rest;
            if (takeRest()) {
                this.desPlayerManager.playDesMusic(this, "d_take_rest");
                return true;
            }
            playNextMention();
            return true;
        }
        playNextMention();
        return true;
    }

    private boolean takeRest() {
        if (this.mentionIndex + 1 > this.mentionList.size() - 1) {
            return false;
        }
        this.tvMentionTime.stop();
        if (this.restVideoPlayFragment == null) {
            this.restVideoPlayFragment = RestVideoPlayFragment.newInstance(this);
        }
        this.restVideoPlayFragment.setMention((Mention) this.mentionList.get(this.mentionIndex +
                1));
        if (!this.restVideoPlayFragment.isAdded()) {
            this.restVideoPlayFragment.show(getSupportFragmentManager(), "restDialog");
        }
        this.restVideoPlayFragment.setOnRestFinish(new OnRestFinish() {
            public void restFinish() {
                VideoPlayActivity.this.restFinish();
            }
        });
        this.handler.post(this.restRunnable);
        return true;
    }

    private void restFinish() {
        this.restVideoPlayFragment.stopRest();
        playNextMention();
    }

    protected void onResume() {
        super.onResume();
        initBgm();
        if (!this.isPaused) {
            this.tvTotalTime.setBase(SystemClock.elapsedRealtime());
            this.tvTotalTime.start();
            playMention();
        }
        AudioManager am = (AudioManager) getSystemService("audio");
    }

    private void initBgm() {
        this.bgmName = this.preference.getBgm();
        this.isBgmOpen = this.preference.isBgmOpen();
        changeBgm();
    }

    private void playMention() {
        this.isCounting = false;
        if (this.mentionIndex >= this.totalMetionCount) {
            lessonFinish();
        } else if (this.mentionIndex >= 0) {
            initDesMusic();
            initVideoData();
            playDesMusic();
            playVideo();
            this.playCountNum = 0;
            updateProgress();
            this.playCountNum = 1;
            refreshMentionCount((this.playCountNum >= 10 ? Integer.valueOf(this.playCountNum) :
                    "0" + this.playCountNum) + "/" + this.currentMention.number);
            this.tvMentionTitle.setText(this.currentMention.name);
            this.tvGroupCount.setText(String.format(getString(R.string.hd), new Object[]{Integer
                    .valueOf(this.mentionIndex + 1)}));
        }
    }

    private void refreshMentionCount(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#b5c2c1")), text
                .indexOf("/"), text.length(), 18);
        this.tvMentionCount.setText(spannableString);
    }

    private void playPrevMention() {
        if (this.mentionIndex != 0) {
            removeCallbacks();
            this.tvMentionTime.stop();
            this.mentionIndex--;
            playMention();
        }
    }

    private void playNextMention() {
        removeCallbacks();
        this.tvMentionTime.stop();
        this.mentionIndex++;
        playMention();
    }

    private void removeCallbacks() {
        this.handler.removeCallbacks(this.restRunnable);
        this.handler.removeCallbacks(this.countDownRunnable);
        this.handler.removeCallbacks(this.timeCountDownRunnable);
    }

    private void lessonFinish() {
        stopLesson();
        this.desPlayerManager.playDesMusic(this, "d_lesson_done", new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                VideoPlayActivity.this.removeCallbacks();
                VideoPlayActivity.this.stopLesson();
                VideoPlayActivity.this.postProgress();
            }
        });
    }

    private void postProgress() {
        showLoading();
        if (this.lesson.user_progress != null && this.lesson.user_progress.finish_section_count >
                0 && this.lesson.user_progress.finish_section_count == this.lesson.user_progress
                .total_section_count) {
            SportApi.postJournalFinish(this.ctx, this.lesson.id, null);
        }
        SportApi.postSportsJournals(this.ctx, this.lesson.id, new JsonCallback(this.ctx) {
            public void ok(String response) {
                super.ok(response);
                EventBus.getDefault().post(NewSportPlanActivity.REFERSH);
                EventBus.getDefault().post(SpecialTrainPlanFragment.REFRESH_SPECIAL_TRAIN);
                EventBus.getDefault().post(SportPlanFragment.REFRESH_SPORT_PLAN_FRAGMENT);
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                LessonFinishActivity.comeOn(VideoPlayActivity.this.ctx, VideoPlayActivity.this
                        .lesson, true);
                VideoPlayActivity.this.finish();
            }

            public void fail(String message) {
                super.fail(message);
                if (VideoPlayActivity.access$1604(VideoPlayActivity.this) <= 5) {
                    VideoPlayActivity.this.postProgress();
                    return;
                }
                EventBus.getDefault().post(NewSportPlanActivity.REFERSH);
                EventBus.getDefault().post(SpecialTrainPlanFragment.REFRESH_SPECIAL_TRAIN);
                EventBus.getDefault().post(SportPlanFragment.REFRESH_SPORT_PLAN_FRAGMENT);
                EventBus.getDefault().post(HomeNewFragment.REFRESH_ONE_KEY_STATUS);
                LessonFinishActivity.comeOn(VideoPlayActivity.this.ctx, VideoPlayActivity.this
                        .lesson, true);
                VideoPlayActivity.this.finish();
            }

            public void onFinish() {
                super.onFinish();
                VideoPlayActivity.this.dismissLoading();
            }
        });
    }

    private void playVideo() {
        if (!TextUtils.isEmpty(this.videoPath)) {
            this.videoView.seekTo(0);
            this.videoView.start();
        }
    }

    private void initListener() {
    }

    private int getChronometerTimeSecond(Chronometer chronometer) {
        return (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
    }

    private void playDesMusic() {
        if (this.desMusics.size() != 0 && this.desMusicIndex <= this.desMusics.size() - 1) {
            this.desPlayerManager.playDesMusic(this.ctx, (String) this.desMusics.get(this
                    .desMusicIndex), new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (VideoPlayActivity.this.desMusicIndex == VideoPlayActivity.this.desMusics
                            .size() - 1) {
                        VideoPlayActivity.this.desMusics.clear();
                        VideoPlayActivity.this.desMusicIndex = 0;
                        VideoPlayActivity.this.isCounting = true;
                        VideoPlayActivity.this.videoView.seekTo(0);
                        VideoPlayActivity.this.videoView.start();
                        VideoPlayActivity.this.startMentionTime();
                        if (VideoPlayActivity.this.currentMention.is_times) {
                            VideoPlayActivity.this.handler.post(VideoPlayActivity.this
                                    .countDownRunnable);
                        } else {
                            VideoPlayActivity.this.handler.post(VideoPlayActivity.this
                                    .timeCountDownRunnable);
                        }
                        VideoPlayActivity.this.refreshMentionCount((VideoPlayActivity.this
                                .playCountNum >= 10 ? Integer.valueOf(VideoPlayActivity.this
                                .playCountNum) : "0" + VideoPlayActivity.this.playCountNum) + "/"
                                + VideoPlayActivity.this.currentMention.number);
                        return;
                    }
                    VideoPlayActivity.this.desMusicIndex = VideoPlayActivity.this.desMusicIndex + 1;
                    VideoPlayActivity.this.playDesMusic();
                }
            });
        }
    }

    private void startMentionTime() {
        this.tvMentionTime.stop();
        this.tvMentionTime.setBase(SystemClock.elapsedRealtime());
        this.tvMentionTime.start();
    }

    protected void onPause() {
        super.onPause();
        pauseLesson();
    }

    @OnClick({2131427686, 2131427910, 2131427911, 2131427917, 2131427921, 2131427908, 2131427918,
            2131427923, 2131427928, 2131427927, 2131427925})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                performFinishLesson();
                return;
            case R.id.controller_view:
                hideOrShowControllView();
                return;
            case R.id.btn_prev:
                playPrevMention();
                return;
            case R.id.btn_next:
                if (this.mentionIndex + 1 <= this.mentionList.size() - 1) {
                    playNextMention();
                    return;
                }
                return;
            case R.id.btn_pause:
                pauseLesson();
                return;
            case R.id.btn_open_bgm_control:
                hideOrShowBgmControlView();
                return;
            case R.id.btn_resume:
                resumeLesson();
                return;
            case R.id.btn_close_bgm_control:
                hideOrShowBgmControlView();
                return;
            case R.id.btn_close_bgm:
                changeBgmState();
                return;
            case R.id.tv_bgm_fit:
                this.bgmName = this.BGM_FIT;
                changeBgm();
                return;
            case R.id.tv_bgm_up:
                this.bgmName = this.BGM_UP;
                changeBgm();
                return;
            default:
                return;
        }
    }

    private void changeBgmState() {
        this.isBgmOpen = !this.isBgmOpen;
        this.preference.putBgmState(this.isBgmOpen);
        changeBgm();
    }

    private void changeBgm() {
        if (this.isBgmOpen) {
            this.tvBgmFit.setEnabled(true);
            this.tvBgmUp.setEnabled(true);
            this.tvMoreBgm.setEnabled(true);
            this.tvBgmState.setText(R.string.fr);
            this.btnCloseBgm.setBackgroundResource(R.drawable.qx);
            this.btnOpenBgmControl.setImageResource(R.drawable.qy);
            this.bgmPlayerManager.stopBgm();
            if (TextUtils.equals(this.bgmName, this.BGM_UP) || TextUtils.isEmpty(this.bgmName)) {
                this.bgmName = this.BGM_UP;
                this.tvBgmUp.setTextColor(getResources().getColor(R.color.j6));
                this.tvBgmFit.setTextColor(getResources().getColor(R.color.ju));
            } else {
                this.bgmName = this.BGM_FIT;
                this.tvBgmFit.setTextColor(getResources().getColor(R.color.j6));
                this.tvBgmUp.setTextColor(getResources().getColor(R.color.ju));
            }
            this.preference.putBgm(this.bgmName);
            this.bgmPlayerManager.startBgm(this, this.bgmName);
            return;
        }
        this.tvBgmFit.setEnabled(false);
        this.tvBgmUp.setEnabled(false);
        this.tvBgmUp.setTextColor(getResources().getColor(R.color.du));
        this.tvBgmFit.setTextColor(getResources().getColor(R.color.du));
        this.tvMoreBgm.setEnabled(false);
        this.btnCloseBgm.setBackgroundResource(R.drawable.ra);
        this.tvBgmState.setText(R.string.yc);
        this.btnOpenBgmControl.setImageResource(R.drawable.rb);
        this.bgmPlayerManager.stopBgm();
    }

    private void hideOrShowBgmControlView() {
        if (this.bgmControlLayout.getVisibility() != 0) {
            pauseLesson();
            this.bgmControlLayout.setVisibility(0);
            this.pauseLayout.setVisibility(8);
            return;
        }
        resumeLesson();
        this.bgmControlLayout.setVisibility(8);
    }

    private void hideOrShowControllView() {
        boolean z = false;
        for (int i = 0; i < this.controllerView.getChildCount(); i++) {
            View view = this.controllerView.getChildAt(i);
            if (this.isControllerViewHide) {
                view.setVisibility(0);
            } else {
                view.setVisibility(8);
            }
        }
        if (!this.isControllerViewHide) {
            z = true;
        }
        this.isControllerViewHide = z;
    }

    private void performFinishLesson() {
        pauseLesson();
        LightAlertDialog.create((Context) this, getString(R.string.a65)).setPositiveButton((int)
                R.string.m1, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VideoPlayActivity.this.stopLesson();
                LessonFinishActivity.comeOn(VideoPlayActivity.this.ctx, VideoPlayActivity.this
                        .lesson, false);
                VideoPlayActivity.this.finish();
            }
        }).setNegativeButton((int) R.string.oi, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                VideoPlayActivity.this.resumeLesson();
            }
        }).show();
    }

    private void pauseLesson() {
        if (!this.isPaused) {
            this.isPaused = true;
            this.pauseLayout.setVisibility(0);
            refreshPauseView();
            this.totalTimePaused = this.tvTotalTime.getBase() - SystemClock.elapsedRealtime();
            this.mentionTimePaused = this.tvMentionTime.getBase() - SystemClock.elapsedRealtime();
            this.tvTotalTime.stop();
            this.tvMentionTime.stop();
            this.handler.removeCallbacks(this.timeCountDownRunnable);
            this.bgmPlayerManager.pauseBgm();
            this.desPlayerManager.pausePlayback();
            if (this.videoView.isPlaying()) {
                this.videoView.pause();
            }
        }
    }

    private void refreshPauseView() {
        if (this.currentMention != null) {
            this.tvName.setText(this.currentMention.name);
            VideoDownloadHelper helper = VideoDownloadHelper.getInstance();
            helper.isNeedShowInfoText(this.essentials1Layout, this.tvEssentials1, this
                    .currentMention.info.summary1);
            helper.isNeedShowInfoText(this.essentials2Layout, this.tvEssentials2, this
                    .currentMention.info.summary2);
            helper.isNeedShowInfoText(this.essentials3Layout, this.tvEssentials3, this
                    .currentMention.info.summary3);
            helper.isNeedShowInfoText(this.commonErrorLayout, this.tvCommonError, this
                    .currentMention.info.common_errors);
            helper.isNeedShowInfoText(this.breathLayout, this.tvBreath, this.currentMention.info
                    .breath);
        }
    }

    private void resumeLesson() {
        this.isPaused = false;
        this.pauseLayout.setVisibility(8);
        this.tvTotalTime.setBase(this.totalTimePaused + SystemClock.elapsedRealtime());
        this.tvTotalTime.start();
        this.totalTimePaused = 0;
        if (this.isCounting) {
            this.tvMentionTime.setBase(this.mentionTimePaused + SystemClock.elapsedRealtime());
            this.tvMentionTime.start();
            this.mentionTimePaused = 0;
            this.handler.post(this.timeCountDownRunnable);
        }
        this.bgmPlayerManager.resumeBgm();
        this.desPlayerManager.resumePlayback();
        if (!this.videoView.isPlaying() && this.videoView.getCurrentPosition() > 0) {
            this.videoView.start();
        }
    }

    private void stopLesson() {
        this.desMusics.clear();
        this.desPlayerManager.stopPlayback();
        this.bgmPlayerManager.stopBgm();
        this.videoView.stopPlayback();
        this.tvTotalTime.stop();
        this.tvMentionTime.stop();
        removeCallbacks();
    }

    protected void onStop() {
        super.onStop();
        removeCallbacks();
        this.desPlayerManager.stopPlayback();
        this.tvTotalTime.stop();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.desPlayerManager.release();
        this.desPlayerManager = null;
        this.bgmPlayerManager.release();
        this.bgmPlayerManager = null;
    }
}
