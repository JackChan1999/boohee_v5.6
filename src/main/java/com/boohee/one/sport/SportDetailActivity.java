package com.boohee.one.sport;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.main.GestureActivity;
import com.boohee.model.status.AttachMent;
import com.boohee.one.R;
import com.boohee.one.event.DownloadEvent;
import com.boohee.one.http.JsonCallback;
import com.boohee.one.player.ExVideoView;
import com.boohee.one.player.PlayerManager;
import com.boohee.one.player.PureVideoView;
import com.boohee.one.sport.DownloadHelper.VideoSizeCallback;
import com.boohee.one.sport.model.DownloadRecord;
import com.boohee.one.sport.model.SportDetail;
import com.boohee.one.ui.StatusPostTextActivity;
import com.boohee.utility.Event;
import com.boohee.utils.FastJsonUtils;
import com.boohee.utils.Helper;
import com.boohee.utils.HttpUtils;
import com.boohee.utils.WheelUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

import org.json.JSONObject;

public class SportDetailActivity extends GestureActivity {
    public static final String COURSE_ID     = "course_id";
    public static final String DOWNLOAD_INFO = "download_info";
    public static final String VIDEO_URL     = "video_url";
    private int courseId;
    @InjectView(2131427895)
    LinearLayout llTick;
    private DownloadRecord mRecord;
    private MenuItem       menuDownload;
    private boolean        sendCompleteRequest;
    @InjectView(2131427898)
    View     shareArea;
    @InjectView(2131427896)
    View     tickArea;
    @InjectView(2131427468)
    TextView tvCalorie;
    @InjectView(2131427893)
    TextView tvDescription;
    @InjectView(2131427891)
    TextView tvDuration;
    @InjectView(2131427890)
    TextView tvHint;
    @InjectView(2131427892)
    TextView tvName;
    @InjectView(2131427899)
    TextView tvShare;
    @InjectView(2131427894)
    TextView tvSportInfo;
    @InjectView(2131427897)
    TextView tvTick;
    private String videoUrl;
    @InjectView(2131427564)
    ExVideoView videoView;

    public static void startActivity(Context context, int id, String videoUrl) {
        Intent i = new Intent(context, SportDetailActivity.class);
        i.putExtra(COURSE_ID, id);
        i.putExtra(VIDEO_URL, videoUrl);
        context.startActivity(i);
    }

    public static void startActivity(Context context, DownloadRecord record) {
        Intent i = new Intent(context, SportDetailActivity.class);
        i.putExtra(DOWNLOAD_INFO, record);
        context.startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d6);
        ButterKnife.inject((Activity) this);
        MobclickAgent.onEvent(this, Event.bingo_viewCourseDetail);
        initVariable();
        initView();
        initData();
    }

    private void initVariable() {
        this.courseId = getIntent().getIntExtra(COURSE_ID, -1);
        this.mRecord = (DownloadRecord) getIntent().getParcelableExtra(DOWNLOAD_INFO);
        this.videoUrl = getIntent().getStringExtra(VIDEO_URL);
    }

    private void initView() {
        this.videoView.getVideoView().setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                SportDetailActivity.this.showCompleteDialog();
                PlayerManager.getInstance().releaseAll();
            }
        });
        this.videoView.getControllerView().setExpandListener(new OnClickListener() {
            public void onClick(View v) {
                PureVideoView pureVideoView = SportDetailActivity.this.videoView.getVideoView();
                PlayerManager.getInstance().savePlayingState(pureVideoView.isPlaying());
                pureVideoView.pause();
                SportPlayActivity.startActivity(SportDetailActivity.this, SportDetailActivity
                        .this.videoView.getVideoUrl());
            }
        });
        if (isFromDownload()) {
            this.llTick.setVisibility(8);
            this.tvHint.setVisibility(0);
            return;
        }
        this.llTick.setVisibility(0);
        this.tvHint.setVisibility(8);
        this.tickArea.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SportDetailActivity.this.recordRequest();
            }
        });
        this.shareArea.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SportDetailActivity.this.share();
            }
        });
    }

    public void recordRequest() {
        if (this.mRecord != null && this.mRecord.sport != null) {
            JsonCallback callback = new JsonCallback(this) {
                public void ok(JSONObject object) {
                    super.ok(object);
                    SportDetailActivity.this.mRecord.sport.is_finish = !SportDetailActivity.this
                            .mRecord.sport.is_finish;
                    SportDetailActivity.this.updateTick(SportDetailActivity.this.mRecord);
                }

                public void onFinish() {
                    super.onFinish();
                    SportDetailActivity.this.dismissLoading();
                    if (SportDetailActivity.this.sendCompleteRequest) {
                        SportDetailActivity.this.showCompleteDialog();
                    }
                }
            };
            showLoading();
            if (this.mRecord.sport.is_finish) {
                MobclickAgent.onEvent(this, Event.bingo_uncheckSportCourse);
                SportV3Api.deleteSportRecord(this, this.courseId, callback);
                return;
            }
            MobclickAgent.onEvent(this, Event.bingo_checkSportCourse);
            SportV3Api.addSportRecord(this, this.courseId, callback);
        }
    }

    private void initData() {
        if (isFromDownload()) {
            updateView(this.mRecord);
        } else if (HttpUtils.isNetworkAvailable(this) || DownloadHelper.getInstance().getRecord
                (this.videoUrl) == null) {
            sportDetailRequest();
        } else {
            this.mRecord = DownloadHelper.getInstance().getRecord(this.videoUrl);
            updateView(this.mRecord);
        }
    }

    private void sportDetailRequest() {
        showLoading();
        SportV3Api.getSportDetail(this, this.courseId, new JsonCallback(this) {
            public void onFinish() {
                super.onFinish();
                SportDetailActivity.this.dismissLoading();
            }

            public void ok(JSONObject object) {
                super.ok(object);
                SportDetail sport = (SportDetail) FastJsonUtils.fromJson(object, SportDetail.class);
                SportDetailActivity.this.mRecord = DownloadHelper.getInstance().getRecord(sport
                        .video_url);
                if (SportDetailActivity.this.mRecord == null) {
                    SportDetailActivity.this.mRecord = new DownloadRecord();
                }
                SportDetailActivity.this.mRecord.sport = sport;
                DownloadHelper.getInstance().updateRecord(SportDetailActivity.this.mRecord);
                SportDetailActivity.this.updateView(SportDetailActivity.this.mRecord);
                if (SportDetailActivity.this.mRecord.videoSize == 0) {
                    DownloadHelper.getInstance().getVideoSize(sport.video_url, new
                            VideoSizeCallback() {
                        public void onResponse(int response) {
                            SportDetailActivity.this.mRecord.videoSize = response / 1048576;
                            SportDetailActivity.this.updateView(SportDetailActivity.this.mRecord);
                        }
                    });
                }
            }
        });
    }

    private void updateView(DownloadRecord record) {
        if (record != null && record.sport != null) {
            SportDetail sport = record.sport;
            if (TextUtils.isEmpty(record.savedPath) || !record.inComplete()) {
                this.videoView.setData(sport.pic_url, sport.video_url, this.courseId);
            } else {
                this.videoView.setData(sport.pic_url, "file://" + record.savedPath, this.courseId);
            }
            setTitle(sport.name);
            this.tvName.setText(sport.name);
            this.tvDescription.setText(sport.description);
            this.tvCalorie.setText(String.format("消耗：%d千卡", new Object[]{Integer.valueOf(sport
                    .calory)}));
            this.tvDuration.setText(String.format("时长：%d分钟", new Object[]{Integer.valueOf(sport
                    .duration)}));
            this.tvSportInfo.setText(Html.fromHtml(sport.info));
            updateTick(record);
            updateDownloadStatus(record);
        }
    }

    private void updateTick(DownloadRecord record) {
        if (record != null && record.sport != null) {
            SportDetail sport = record.sport;
            if (!isFromDownload()) {
                if (sport.is_finish) {
                    this.tvTick.setText("已完成");
                    this.tvTick.setTextColor(getResources().getColor(R.color.hb));
                    this.tickArea.setBackgroundColor(getResources().getColor(R.color.i2));
                    this.tvTick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a2a, 0, 0, 0);
                    this.shareArea.setVisibility(8);
                    return;
                }
                this.tvTick.setText("完成运动请打勾");
                this.tvTick.setTextColor(getResources().getColor(R.color.ju));
                this.tickArea.setBackgroundColor(getResources().getColor(R.color.hb));
                this.tvTick.setCompoundDrawablesWithIntrinsicBounds(R.drawable.a27, 0, 0, 0);
                this.shareArea.setVisibility(8);
            }
        }
    }

    private void updateDownloadStatus(DownloadRecord record) {
        if (record != null) {
            if (record.downloadStatus == 6) {
                this.tvName.setText(record.sport.name + "（已下载）");
                this.videoView.updateVideoUrl("file://" + record.savedPath);
            }
            updateMenu();
        }
    }

    public void share() {
        if (this.mRecord != null && this.mRecord.sport != null) {
            AttachMent attachMent = new AttachMent();
            attachMent.title = String.format("已完成%s\n已消耗%d千卡", new Object[]{this.mRecord.sport
                    .name, Integer.valueOf(this.mRecord.sport.calory)});
            attachMent.type = "show";
            attachMent.pic = "http://up.boohee.cn/house/u/one/yetisport/ic_special_share.png";
            StatusPostTextActivity.comeWithAttachmentAndExtraText(this.ctx, "#薄荷健身#", attachMent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuDownload = menu.add(0, 1, 1, R.string.jj);
        this.menuDownload.setShowAsAction(2);
        updateMenu();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (WheelUtils.isFastDoubleClick()) {
            return true;
        }
        if (this.mRecord == null || this.mRecord.sport == null) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case 1:
                if (this.mRecord.canDownload()) {
                    prepareDownload();
                    return true;
                } else if (!this.mRecord.inConnectAndDownload()) {
                    return true;
                } else {
                    this.menuDownload.setTitle("继续下载");
                    DownloadService.intentPause(this, this.mRecord.sport.video_url);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareDownload() {
        if (!HttpUtils.isNetworkAvailable(this)) {
            Helper.showToast((CharSequence) "当前处于无网环境，无法下载");
        } else if (HttpUtils.isWifiConnection(this)) {
            this.menuDownload.setTitle("下载中");
            DownloadService.intentDownload(this, this.mRecord);
        } else {
            new Builder(this).setMessage(getString(R.string.jn)).setPositiveButton((CharSequence)
                    "继续下载", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SportDetailActivity.this.menuDownload.setTitle("下载中");
                    DownloadService.intentDownload(SportDetailActivity.this, SportDetailActivity
                            .this.mRecord);
                }
            }).setNegativeButton((CharSequence) "取消", null).show();
        }
    }

    private void updateMenu() {
        if (this.menuDownload != null) {
            this.menuDownload.setTitle(downloadInfo());
        }
    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    protected void onResume() {
        super.onResume();
        if (DownloadHelper.getInstance().hasVideoUrl(this.mRecord)) {
            DownloadRecord record = DownloadHelper.getInstance().getRecord(this.mRecord.sport
                    .video_url);
            if (record != null) {
                this.mRecord.savedPath = record.savedPath;
                this.mRecord.downloadStatus = record.downloadStatus;
                this.mRecord.progress = record.progress;
                updateDownloadStatus(this.mRecord);
            }
        }
        if (this.videoView.getVideoView().getVisibility() != 0) {
            return;
        }
        if (PlayerManager.getInstance().isPlayingBefore()) {
            this.videoView.getVideoView().start();
            this.videoView.getControllerView().show();
            return;
        }
        this.videoView.getControllerView().show(0);
    }

    protected void onPause() {
        super.onPause();
        if (this.videoView.getVideoView().getVisibility() == 0 && !PlayerManager.getInstance()
                .isStartFullScreen()) {
            PlayerManager.getInstance().savePlayingState(this.videoView.getVideoView().isPlaying());
            this.videoView.getVideoView().pause();
            this.videoView.getControllerView().hide();
        }
    }

    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (!PlayerManager.getInstance().isStartFullScreen()) {
            PlayerManager.getInstance().releaseAll();
        }
    }

    public void onEventMainThread(DownloadEvent event) {
        DownloadRecord record = event.record;
        if (DownloadHelper.getInstance().hasVideoUrl(this.mRecord) && DownloadHelper.getInstance
                ().hasVideoUrl(record) && this.mRecord.sport.video_url.equals(record.sport
                .video_url)) {
            this.mRecord = record;
            updateDownloadStatus(this.mRecord);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            showCompleteDialog();
        }
    }

    private void showCompleteDialog() {
        boolean showDiamond = true;
        if (this.mRecord != null && this.mRecord.sport != null) {
            if (this.mRecord.sport.is_finish || isFromDownload() || this.sendCompleteRequest) {
                if (!(!isFromDownload() && this.sendCompleteRequest && this.mRecord.sport
                        .is_finish)) {
                    showDiamond = false;
                }
                SportCompleteFragment.newInstance(this.mRecord.sport, showDiamond).show
                        (getSupportFragmentManager(), "SportCompleteFragment");
                this.sendCompleteRequest = false;
                return;
            }
            this.sendCompleteRequest = true;
            recordRequest();
        }
    }

    private String downloadInfo() {
        if (this.mRecord == null || this.mRecord.sport == null) {
            return "下载";
        }
        switch (this.mRecord.downloadStatus) {
            case 1:
            case 3:
                return String.format("下载中(%d%%)", new Object[]{Integer.valueOf(this.mRecord
                        .progress)});
            case 2:
            case 4:
            case 5:
                return "继续下载";
            case 6:
                return "";
            case 7:
                return "等待中";
            default:
                if (this.mRecord.videoSize <= 0) {
                    return "下载";
                }
                return String.format("下载(%dM)", new Object[]{Integer.valueOf(this.mRecord
                        .videoSize)});
        }
    }

    private boolean isFromDownload() {
        return this.courseId == -1;
    }
}
