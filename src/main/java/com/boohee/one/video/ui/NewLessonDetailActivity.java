package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.ui.BrowserActivity;
import com.boohee.one.video.adapter.MentionRecyclerAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.download.VideoDownloadHelper.OnDownloadListener;
import com.boohee.one.video.entity.Lesson;
import com.boohee.one.video.entity.Mention;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.LightAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class NewLessonDetailActivity extends GestureActivity {
    public static final String       LESSON_ID       = "LESSON_ID";
    private final       int          DOWN_LOAD_RETRY = 5;
    private             List<String> audioUrls       = new ArrayList();
    @InjectView(2131427741)
    RelativeLayout bottomLayout;
    private int downloadRetry = 0;
    private FileCache fileCache;
    private int       index;
    private boolean isDownloaded = true;
    @InjectView(2131428625)
    ImageView ivQuestion;
    @InjectView(2131427735)
    ImageView ivTop;
    @InjectView(2131427959)
    View      layout;
    private Lesson lesson;
    private int    lessonId;
    @InjectView(2131429337)
    LinearLayout llLessonNumber;
    @InjectView(2131429334)
    LinearLayout llLessonTime;
    private List<Mention> mentions = new ArrayList();
    @InjectView(2131427743)
    ProgressBar  progressBarDownload;
    @InjectView(2131428807)
    RecyclerView recyclerViewTrain;
    @InjectView(2131428805)
    RecyclerView recyclerViewWarmUp;
    private MentionRecyclerAdapter trainAdapter;
    @InjectView(2131427744)
    TextView tvDownloadStatus;
    @InjectView(2131429336)
    TextView tvLessonCalory;
    @InjectView(2131429335)
    TextView tvLessonNumber;
    @InjectView(2131429338)
    TextView tvLessonTime;
    @InjectView(2131427740)
    TextView tvMentionDes;
    @InjectView(2131427742)
    TextView tvStart;
    @InjectView(2131428175)
    TextView tvTitle;
    @InjectView(2131428806)
    TextView tvTrain;
    @InjectView(2131428804)
    TextView tvWarmUp;
    private VideoDownloadHelper videoDownloadHelper = VideoDownloadHelper.getInstance();
    private List<String>        videoUrls           = new ArrayList();
    private MentionRecyclerAdapter warmUpAdapter;

    static /* synthetic */ int access$404(NewLessonDetailActivity x0) {
        int i = x0.downloadRetry + 1;
        x0.downloadRetry = i;
        return i;
    }

    public static void comeOn(Context context, int lessonId) {
        Intent intent = new Intent(context, NewLessonDetailActivity.class);
        intent.putExtra("LESSON_ID", lessonId);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lf);
        ButterKnife.inject((Activity) this);
        this.fileCache = FileCache.get(this.ctx);
        initView();
        initData();
        getLessonData();
    }

    private void initView() {
        this.layout.setVisibility(8);
        this.recyclerViewWarmUp.setHasFixedSize(true);
        this.recyclerViewWarmUp.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.recyclerViewWarmUp.setItemAnimator(new DefaultItemAnimator());
        LayoutManager manager2 = new LinearLayoutManager(this, 0, false);
        this.recyclerViewTrain.setHasFixedSize(true);
        this.recyclerViewTrain.setLayoutManager(manager2);
        this.recyclerViewTrain.setItemAnimator(new DefaultItemAnimator());
        ViewUtils.setViewScaleHeight(this.ctx, this.ivTop, 2, 1);
        this.bottomLayout.setEnabled(false);
    }

    private void initData() {
        if (getIntent() != null) {
            this.lessonId = getIntent().getIntExtra("LESSON_ID", -1);
        }
    }

    private void getLessonData() {
        if (this.lessonId >= 0) {
            showLoading();
            SportApi.getExerciseDetail(this, this.lessonId, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    NewLessonDetailActivity.this.layout.setVisibility(0);
                    NewLessonDetailActivity.this.handleData(object);
                    NewLessonDetailActivity.this.fileCache.put("sport_lesson_detail", object);
                }

                public void fail(String message) {
                    super.fail(message);
                    NewLessonDetailActivity.this.handleData(NewLessonDetailActivity.this
                            .fileCache.getAsJSONObject("sport_lesson_detail"));
                }

                public void onFinish() {
                    super.onFinish();
                    NewLessonDetailActivity.this.bottomLayout.setEnabled(true);
                    NewLessonDetailActivity.this.dismissLoading();
                }
            });
        }
    }

    private void handleData(JSONObject object) {
        try {
            this.lesson = (Lesson) FastJsonUtils.fromJson(object, Lesson.class);
            this.mentions.clear();
            this.mentions.addAll(this.lesson.mentions_warm);
            this.mentions.addAll(this.lesson.mentions_train);
            this.lesson.mentions = this.mentions;
            parseDownloadPath();
            refreshView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDownloadPath() {
        if (this.lesson != null && this.lesson.mentions != null) {
            for (int i = 0; i < this.lesson.mentions.size(); i++) {
                int mentionId = ((Mention) this.lesson.mentions.get(i)).id;
                String videoPath = ((Mention) this.lesson.mentions.get(i)).video_url;
                String audioPath = ((Mention) this.lesson.mentions.get(i)).audio_url;
                if (!TextUtils.isEmpty(videoPath)) {
                    this.videoUrls.add(videoPath);
                }
                if (!TextUtils.isEmpty(audioPath)) {
                    this.audioUrls.add(audioPath);
                }
                if (!isFileExist(this.videoDownloadHelper.getVideoName(this.ctx, mentionId)) ||
                        !isFileExist(this.videoDownloadHelper.getAudioName(this.ctx, mentionId))) {
                    this.isDownloaded = false;
                }
            }
        }
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        return file != null && file.exists() && file.length() > 0;
    }

    private void refreshView() {
        if (this.lesson != null && this.lesson.mentions != null && this.lesson.mentions_warm !=
                null && this.lesson.mentions_train != null) {
            setTitle(this.lesson.name);
            if (this.isDownloaded) {
                this.tvDownloadStatus.setText(R.string.jt);
            } else {
                this.tvDownloadStatus.setText("");
            }
            ViewUtils.setViewScaleHeight(this.ctx, this.ivTop, 2, 1);
            this.imageLoader.displayImage(this.lesson.pic_url, this.ivTop);
            this.warmUpAdapter = new MentionRecyclerAdapter(this, this.lesson.mentions_warm);
            this.recyclerViewWarmUp.setAdapter(this.warmUpAdapter);
            this.trainAdapter = new MentionRecyclerAdapter(this, this.lesson.mentions_train);
            this.recyclerViewTrain.setAdapter(this.trainAdapter);
            this.tvLessonNumber.setText(String.valueOf(this.lesson.mentions_warm.size() + this
                    .lesson.mentions_train.size()));
            this.tvLessonCalory.setText(String.valueOf(this.lesson.today_calorie));
            this.tvLessonTime.setText(String.valueOf(this.lesson.total_time));
            this.tvMentionDes.setText(String.valueOf(this.lesson.description));
        }
    }

    @OnClick({2131427741, 2131428625})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_layout:
                if (this.lesson != null) {
                    checkDownloadEnv();
                    return;
                }
                return;
            case R.id.iv_question:
                BrowserActivity.comeOnBaby(this.ctx, null, SportApi.URL_QUESTION);
                return;
            default:
                return;
        }
    }

    private void checkDownloadEnv() {
        if (!this.isDownloaded || HttpUtils.isWifiConnection(this.ctx)) {
            performDownload();
            return;
        }
        final LightAlertDialog dialog = LightAlertDialog.create(this.ctx, String.format(getString
                (R.string.js), new Object[]{Float.valueOf(this.lesson.stream)}));
        dialog.setNegativeButton((int) R.string.eq, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton((CharSequence) "确定", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NewLessonDetailActivity.this.performDownload();
            }
        });
        dialog.show();
    }

    private void performDownload() {
        if (this.lesson != null && this.lesson.mentions != null && this.lesson.mentions.size() !=
                0) {
            if (this.isDownloaded) {
                downloadFinish();
                return;
            }
            this.bottomLayout.setEnabled(false);
            this.progressBarDownload.setVisibility(0);
            this.tvStart.setVisibility(8);
            this.tvDownloadStatus.setText(R.string.ju);
            download();
        }
    }

    private void download() {
        this.videoDownloadHelper.downloadFiles(this.ctx, this.lesson.mentions, this
                .progressBarDownload, new OnDownloadListener() {
            public void onDownloadFinish() {
                NewLessonDetailActivity.this.downloadFinish();
            }

            public void onDownloadFail() {
                if (NewLessonDetailActivity.access$404(NewLessonDetailActivity.this) > 5) {
                    Helper.showToast((int) R.string.jm);
                    NewLessonDetailActivity.this.downloadRetry = 0;
                    NewLessonDetailActivity.this.tvDownloadStatus.setText("");
                    NewLessonDetailActivity.this.progressBarDownload.setVisibility(8);
                    NewLessonDetailActivity.this.tvStart.setVisibility(0);
                    NewLessonDetailActivity.this.bottomLayout.setEnabled(true);
                    return;
                }
                NewLessonDetailActivity.this.download();
            }
        });
    }

    private void downloadFinish() {
        if (this.ctx != null && this.lesson != null) {
            SportApi.postLessonProgress(this.ctx, this.lesson.id, null);
            this.bottomLayout.setEnabled(true);
            this.isDownloaded = true;
            this.tvDownloadStatus.setText(R.string.jt);
            this.tvStart.setVisibility(0);
            this.progressBarDownload.setVisibility(8);
            VideoPlayActivity.comeOn(this.ctx, this.lesson);
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.videoDownloadHelper.getClient().cancelRequests(this.ctx, true);
    }
}
