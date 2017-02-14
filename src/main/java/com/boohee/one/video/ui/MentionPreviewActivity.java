package com.boohee.one.video.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.boohee.one.R;
import com.boohee.one.ui.BaseNoToolbarActivity;
import com.boohee.one.video.adapter.MentionPreviewPagerAdapter;
import com.boohee.one.video.download.VideoDownloadHelper;
import com.boohee.one.video.entity.Mention;
import com.boohee.utils.Helper;
import com.boohee.widgets.ProgressWheel;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

public class MentionPreviewActivity extends BaseNoToolbarActivity {
    public static final String MENTION_LIST = "MENTION_LIST";
    public static final String POSITION     = "POSITION";
    @InjectView(2131427686)
    ImageView btnClose;
    private Mention currentMention;
    private int           downloadRetry = 0;
    private List<Mention> mentionList   = new ArrayList();
    private MentionPreviewPagerAdapter pagerAdapter;
    private int                        position;
    @InjectView(2131427773)
    ProgressWheel  progressBar;
    @InjectView(2131427746)
    RelativeLayout progressLayout;
    @InjectView(2131427774)
    TextView       tvIndex;
    private VideoDownloadHelper videoDownloadHelper = VideoDownloadHelper.getInstance();
    private String videoPath;
    @InjectView(2131427772)
    VideoView videoView;
    @InjectView(2131427350)
    ViewPager viewPager;

    static /* synthetic */ int access$504(MentionPreviewActivity x0) {
        int i = x0.downloadRetry + 1;
        x0.downloadRetry = i;
        return i;
    }

    @OnClick({2131427686})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.c1);
        ButterKnife.inject((Activity) this);
        getWindow().setFlags(1024, 1024);
        handleIntent();
        initView();
    }

    private void handleIntent() {
        if (getIntent() != null) {
            this.mentionList = getIntent().getParcelableArrayListExtra(MENTION_LIST);
            this.position = getIntent().getIntExtra(POSITION, 0);
            this.currentMention = (Mention) this.mentionList.get(this.position);
        }
    }

    private void initView() {
        if (this.mentionList != null && this.mentionList.size() != 0) {
            this.pagerAdapter = new MentionPreviewPagerAdapter(getSupportFragmentManager(), this
                    .mentionList);
            this.viewPager.setAdapter(this.pagerAdapter);
            this.viewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    MentionPreviewActivity.this.videoView.stopPlayback();
                    MentionPreviewActivity.this.currentMention = (Mention) MentionPreviewActivity
                            .this.mentionList.get(position);
                    MentionPreviewActivity.this.setCurrentIndex(position);
                }
            });
            this.viewPager.setCurrentItem(this.position);
            setCurrentIndex(this.position);
        }
    }

    private void initVideo() {
        if (this.currentMention == null) {
            throw new IllegalStateException("currnt mention null");
        }
        downloadFile();
    }

    private void downloadFile() {
        if (this.videoDownloadHelper.checkVideoDownload(this.ctx, this.currentMention.id)) {
            playVideo();
            return;
        }
        this.progressLayout.setVisibility(0);
        disableViewpagerScroll();
        this.videoDownloadHelper.getClient().get(this.ctx, this.currentMention.video_url, new
                FileAsyncHttpResponseHandler(this) {
            public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                MentionPreviewActivity.this.videoDownloadHelper.downloadFileFailure
                        (MentionPreviewActivity.this.ctx, MentionPreviewActivity.this
                                .currentMention.id, file);
                MentionPreviewActivity.this.downloadRetry = MentionPreviewActivity.this
                        .downloadRetry + 1;
                if (MentionPreviewActivity.access$504(MentionPreviewActivity.this) > 3) {
                    Helper.showToast((int) R.string.jm);
                    MentionPreviewActivity.this.downloadRetry = 0;
                    return;
                }
                MentionPreviewActivity.this.downloadFile();
            }

            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                MentionPreviewActivity.this.progressBar.setProgress((int) ((360 * bytesWritten) /
                        totalSize));
            }

            public void onSuccess(int i, Header[] headers, File file) {
                MentionPreviewActivity.this.videoDownloadHelper.copyFile(file, new File
                        (MentionPreviewActivity.this.videoDownloadHelper.getVideoName
                                (MentionPreviewActivity.this.ctx, MentionPreviewActivity.this
                                        .currentMention.id)));
                MentionPreviewActivity.this.playVideo();
                MentionPreviewActivity.this.ennableViewpagerScroll();
            }
        });
    }

    private void disableViewpagerScroll() {
        this.viewPager.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void ennableViewpagerScroll() {
        this.viewPager.setOnTouchListener(null);
    }

    private void playVideo() {
        this.videoPath = this.videoDownloadHelper.getVideoName(this.ctx, this.currentMention.id);
        this.videoView.setVideoPath(this.videoPath);
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                MentionPreviewActivity.this.progressLayout.setVisibility(8);
                mp.setLooping(true);
            }
        });
        this.videoView.start();
    }

    private void setCurrentIndex(int position) {
        this.tvIndex.setText(String.format(getResources().getString(R.string.adg), new
                Object[]{Integer.valueOf(position + 1), Integer.valueOf(this.mentionList.size())}));
        initVideo();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.videoView.stopPlayback();
        this.videoDownloadHelper.getClient().cancelRequests(this.ctx, true);
    }

    public static void comeOnBaby(Context context, List<Mention> mentionList, int position) {
        if (context != null && mentionList != null && mentionList.size() != 0) {
            Intent intent = new Intent(context, MentionPreviewActivity.class);
            intent.putParcelableArrayListExtra(MENTION_LIST, (ArrayList) mentionList);
            intent.putExtra(POSITION, position);
            context.startActivity(intent);
        }
    }
}
