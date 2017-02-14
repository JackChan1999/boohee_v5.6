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
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.main.GestureActivity;
import com.boohee.one.MyApplication;
import com.boohee.one.R;
import com.boohee.one.cache.FileCache;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.video.adapter.MentionRecyclerAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.download.VideoDownloadHelper.OnDownloadListener;
import com.boohee.one.video.entity.Lesson;
import com.boohee.one.video.entity.Mention;
import com.boohee.one.video.view.HorizontalProgressView;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class LessonDetailActivity extends GestureActivity {
    public static final String LESSON_ID       = "LESSON_ID";
    private final       int    DOWN_LOAD_RETRY = 5;
    private MentionRecyclerAdapter adapter;
    private List<String> audioUrls = new ArrayList();
    @InjectView(2131427741)
    RelativeLayout bottomLayout;
    private int downloadRetry = 0;
    private FileCache fileCache;
    private int       index;
    private boolean isDownloaded = true;
    @InjectView(2131427735)
    ImageView ivTop;
    private Lesson lesson;
    private int    lessonId;
    @InjectView(2131427743)
    ProgressBar            progressBarDownload;
    @InjectView(2131427737)
    HorizontalProgressView progressBarHorizontal;
    private String progressTime;
    @InjectView(2131427637)
    RecyclerView recyclerView;
    private SpannableString spannableString;
    @InjectView(2131427461)
    TextView tvCalory;
    @InjectView(2131427744)
    TextView tvDownloadStatus;
    @InjectView(2131427740)
    TextView tvMentionDes;
    @InjectView(2131427739)
    TextView tvMentonCount;
    @InjectView(2131427738)
    TextView tvSportTime;
    @InjectView(2131427742)
    TextView tvStart;
    @InjectView(2131427736)
    TextView tvTime;
    private VideoDownloadHelper videoDownloadHelper = VideoDownloadHelper.getInstance();
    private List<String>        videoUrls           = new ArrayList();

    static /* synthetic */ int access$404(LessonDetailActivity x0) {
        int i = x0.downloadRetry + 1;
        x0.downloadRetry = i;
        return i;
    }

    public static void comeOn(Context context, int lessonId) {
        Intent intent = new Intent(context, LessonDetailActivity.class);
        intent.putExtra("LESSON_ID", lessonId);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bv);
        ButterKnife.inject((Activity) this);
        this.fileCache = FileCache.get(this.ctx);
        initView();
        initData();
        getLessonData();
    }

    private void initView() {
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        ViewUtils.setViewScaleHeight(this.ctx, this.ivTop, 750, 500);
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
            SportApi.getLessonDetail(this, this.lessonId, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    LessonDetailActivity.this.handleData(object);
                    LessonDetailActivity.this.fileCache.put("sport_lesson_detail", object);
                }

                public void fail(String message) {
                    super.fail(message);
                    LessonDetailActivity.this.handleData(LessonDetailActivity.this.fileCache
                            .getAsJSONObject("sport_lesson_detail"));
                }

                public void onFinish() {
                    super.onFinish();
                    LessonDetailActivity.this.bottomLayout.setEnabled(true);
                    LessonDetailActivity.this.dismissLoading();
                }
            });
        }
    }

    private void handleData(JSONObject object) {
        try {
            this.lesson = (Lesson) FastJsonUtils.fromJson(object, Lesson.class);
            parseDownloadPath();
            refreshView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDownloadPath() {
        if (this.lesson != null) {
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
        if (this.lesson != null) {
            if (this.isDownloaded) {
                this.tvDownloadStatus.setText(R.string.jt);
            } else {
                this.tvDownloadStatus.setText("");
            }
            this.adapter = new MentionRecyclerAdapter(this, this.lesson.mentions);
            this.recyclerView.setAdapter(this.adapter);
            this.tvSportTime.setText(String.valueOf(this.lesson.total_time));
            this.tvCalory.setText(String.valueOf(this.lesson.calorie));
            this.tvMentonCount.setText(String.valueOf(this.lesson.mentions.size()));
            this.tvMentionDes.setText(String.valueOf(this.lesson.description));
            this.progressBarHorizontal.setProgress(this.lesson.progress, this.lesson
                    .total_progress);
            this.progressBarHorizontal.setItemWidth(ViewUtils.dip2px(MyApplication.getContext(),
                    40.0f));
            this.progressTime = String.format(getString(R.string.ss), new Object[]{Integer
                    .valueOf(this.lesson.progress)});
            this.spannableString = new SpannableString(this.progressTime);
            this.spannableString.setSpan(new AbsoluteSizeSpan(14, true), 0, this.spannableString
                    .length(), 18);
            this.spannableString.setSpan(new AbsoluteSizeSpan(50, true), 1, this.spannableString
                    .length() - 1, 17);
            this.tvTime.setText(this.spannableString);
            ImageLoader.getInstance().displayImage(this.lesson.banner_url, this.ivTop,
                    ImageLoaderOptions.randomColor());
        }
    }

    @OnClick({2131427741})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_layout:
                if (this.lesson != null) {
                    checkDownloadEnv();
                    return;
                }
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
                LessonDetailActivity.this.performDownload();
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
                LessonDetailActivity.this.downloadFinish();
            }

            public void onDownloadFail() {
                if (LessonDetailActivity.access$404(LessonDetailActivity.this) > 5) {
                    Helper.showToast((int) R.string.jm);
                    LessonDetailActivity.this.downloadRetry = 0;
                    LessonDetailActivity.this.tvDownloadStatus.setText("");
                    LessonDetailActivity.this.progressBarDownload.setVisibility(8);
                    LessonDetailActivity.this.tvStart.setVisibility(0);
                    LessonDetailActivity.this.bottomLayout.setEnabled(true);
                    return;
                }
                LessonDetailActivity.this.download();
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
