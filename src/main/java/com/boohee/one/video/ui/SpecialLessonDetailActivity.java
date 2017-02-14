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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.boohee.one.http.client.BooheeClient;
import com.boohee.one.video.adapter.MentionRecyclerAdapter;
import com.boohee.one.video.api.SportApi;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.download.VideoDownloadHelper.OnDownloadListener;
import com.boohee.one.video.entity.Lesson;
import com.boohee.one.video.entity.Mention;
import com.boohee.one.video.fragment.SpecialTrainPlanFragment;
import com.boohee.utility.ImageLoaderOptions;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.ViewUtils;
import com.boohee.widgets.LightAlertDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class SpecialLessonDetailActivity extends GestureActivity {
    public static final String IS_JOINED       = "IS_JOINED";
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
    private boolean isJoined;
    @InjectView(2131427735)
    ImageView ivTop;
    private Lesson lesson;
    private int    lessonId;
    private Menu   menu;
    @InjectView(2131427743)
    ProgressBar progressBarDownload;
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
    @InjectView(2131427514)
    TextView tvName;
    @InjectView(2131427742)
    TextView tvStart;
    @InjectView(2131427736)
    TextView tvTime;
    private VideoDownloadHelper videoDownloadHelper = VideoDownloadHelper.getInstance();
    private List<String>        videoUrls           = new ArrayList();

    static /* synthetic */ int access$604(SpecialLessonDetailActivity x0) {
        int i = x0.downloadRetry + 1;
        x0.downloadRetry = i;
        return i;
    }

    public static void comeOn(Context context, int lessonId, boolean isJoined) {
        Intent intent = new Intent(context, SpecialLessonDetailActivity.class);
        intent.putExtra("LESSON_ID", lessonId);
        intent.putExtra(IS_JOINED, isJoined);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d1);
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
        ViewUtils.setViewScaleHeight(this.ctx, this.ivTop, 2, 1);
        this.bottomLayout.setEnabled(false);
    }

    private void initData() {
        if (getIntent() != null) {
            this.lessonId = getIntent().getIntExtra("LESSON_ID", -1);
            this.isJoined = getIntent().getBooleanExtra(IS_JOINED, false);
            if (this.isJoined) {
                this.tvStart.setText(R.string.a6j);
            } else {
                this.tvStart.setText(R.string.pp);
            }
            invalidateOptionsMenu();
        }
    }

    private void getLessonData() {
        if (this.lessonId >= 0) {
            showLoading();
            SportApi.getSpecialLessonDetail(this, this.lessonId, new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    SpecialLessonDetailActivity.this.handleData(object);
                    SpecialLessonDetailActivity.this.fileCache.put("sport_lesson_detail_" +
                            SpecialLessonDetailActivity.this.lessonId, object);
                }

                public void fail(String message) {
                    super.fail(message);
                    SpecialLessonDetailActivity.this.handleData(SpecialLessonDetailActivity.this
                            .fileCache.getAsJSONObject("sport_lesson_detail_" +
                                    SpecialLessonDetailActivity.this.lessonId));
                }

                public void onFinish() {
                    super.onFinish();
                    SpecialLessonDetailActivity.this.bottomLayout.setEnabled(true);
                    SpecialLessonDetailActivity.this.dismissLoading();
                }
            });
        }
    }

    private void handleData(JSONObject object) {
        try {
            this.lesson = (Lesson) FastJsonUtils.fromJson(object, Lesson.class);
            if (!(this.lesson == null || TextUtils.isEmpty(this.lesson.name))) {
                setTitle(this.lesson.name);
            }
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
            this.tvName.setText(this.lesson.name);
            this.tvTime.setText(String.format(getString(R.string.a5k), new Object[]{Integer
                    .valueOf(this.lesson.total_time)}));
            this.tvCalory.setText(String.format(getString(R.string.a5j), new Object[]{Integer
                    .valueOf(this.lesson.calorie)}));
            this.tvMentionDes.setText(String.valueOf(this.lesson.description));
            ImageLoader.getInstance().displayImage(this.lesson.banner_url, this.ivTop,
                    ImageLoaderOptions.randomColor());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q, menu);
        this.menu = menu;
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        setUpMenu();
        return true;
    }

    private void setUpMenu() {
        if (this.menu != null && this.menu.size() > 0) {
            MenuItem item = this.menu.findItem(R.id.action_more);
            if (item != null) {
                item.setVisible(this.isJoined);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit_train:
                quitTrain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void quitTrain() {
        if (this.lesson != null) {
            BooheeClient.build(BooheeClient.BINGO).delete("/api/v1/trainings/" + this.lesson.id,
                    null, new JsonCallback(this.ctx) {
                public void onFinish() {
                    super.onFinish();
                }

                public void ok(JSONObject object) {
                    super.ok(object);
                    SpecialLessonDetailActivity.this.isJoined = false;
                    SpecialLessonDetailActivity.this.tvStart.setText(R.string.pp);
                    Helper.showToast((CharSequence) "已退出该训练~");
                    EventBus.getDefault().post(SpecialTrainPlanFragment.REFRESH_SPECIAL_TRAIN);
                    EventBus.getDefault().post(AddSpecialLessonActivity.REFRESH_ADD_SPECIAL);
                    SpecialLessonDetailActivity.this.invalidateOptionsMenu();
                }
            }, this.ctx);
        }
    }

    @OnClick({2131427741})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_layout:
                if (this.lesson == null) {
                    return;
                }
                if (this.isJoined) {
                    checkDownloadEnv();
                    return;
                } else {
                    joinTrain();
                    return;
                }
            default:
                return;
        }
    }

    private void joinTrain() {
        if (this.lesson != null) {
            this.bottomLayout.setEnabled(false);
            this.tvStart.setText(R.string.pp);
            BooheeClient.build(BooheeClient.BINGO).put("/api/v1/trainings/" + this.lesson.id,
                    null, new JsonCallback(this.ctx) {
                public void onFinish() {
                    super.onFinish();
                    SpecialLessonDetailActivity.this.bottomLayout.setEnabled(true);
                }

                public void ok(JSONObject object) {
                    super.ok(object);
                    SpecialLessonDetailActivity.this.isJoined = true;
                    SpecialLessonDetailActivity.this.tvStart.setText(R.string.a6j);
                    Helper.showToast((CharSequence) "已参加该训练~");
                    SpecialLessonDetailActivity.this.invalidateOptionsMenu();
                    EventBus.getDefault().post(SpecialTrainPlanFragment.REFRESH_SPECIAL_TRAIN);
                    EventBus.getDefault().post(AddSpecialLessonActivity.REFRESH_ADD_SPECIAL);
                }
            }, this.ctx);
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
                SpecialLessonDetailActivity.this.performDownload();
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
                SpecialLessonDetailActivity.this.downloadFinish();
            }

            public void onDownloadFail() {
                if (SpecialLessonDetailActivity.access$604(SpecialLessonDetailActivity.this) > 5) {
                    Helper.showToast((int) R.string.jm);
                    SpecialLessonDetailActivity.this.downloadRetry = 0;
                    SpecialLessonDetailActivity.this.tvDownloadStatus.setText("");
                    SpecialLessonDetailActivity.this.progressBarDownload.setVisibility(8);
                    SpecialLessonDetailActivity.this.tvStart.setVisibility(0);
                    SpecialLessonDetailActivity.this.bottomLayout.setEnabled(true);
                    return;
                }
                SpecialLessonDetailActivity.this.download();
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
