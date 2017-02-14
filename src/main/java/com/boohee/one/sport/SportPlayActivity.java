package com.boohee.one.sport;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.one.R;
import com.boohee.one.player.ExVideoView;
import com.boohee.one.player.PlayerManager;

public class SportPlayActivity extends AppCompatActivity {
    public static final String  TAG            = "SportPlayActivity";
    public static final int     VIDEO_COMPLETE = 1;
    public static final String  VIDEO_URL      = "VIDEO_URL";
    private             boolean isFirstShow    = true;
    private   View        mBtnBack;
    private   View        mHeaderView;
    private   String      mUri;
    protected ExVideoView mVideoView;

    public static void startActivity(Activity context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent i = new Intent(context, SportPlayActivity.class);
            i.putExtra("VIDEO_URL", url);
            PlayerManager.getInstance().prepareFullScreen();
            context.startActivityForResult(i, 1);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView((int) R.layout.aw);
        initVariables();
        initViews();
    }

    private void initVariables() {
        PlayerManager.getInstance().resetManualQuitState();
        PlayerManager.getInstance().resetStartFullScreenState();
        if (TextUtils.isEmpty(getIntent().getStringExtra("VIDEO_URL"))) {
            finish();
        } else {
            this.mUri = getIntent().getStringExtra("VIDEO_URL");
        }
    }

    protected void initViews() {
        this.mVideoView = (ExVideoView) findViewById(R.id.video);
        this.mHeaderView = findViewById(R.id.header);
        this.mBtnBack = findViewById(R.id.btn_back);
        this.mVideoView.setExpandState(true);
        this.mVideoView.setStatus(2);
        this.mVideoView.setContinueVideo(this.mUri);
        this.mVideoView.getVideoView().setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                SportPlayActivity.this.setResult(-1);
                SportPlayActivity.this.finish();
            }
        });
        this.mVideoView.getControllerView().addBindView(this.mHeaderView);
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SportPlayActivity.this.onBackPressed();
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        if (this.mUri != null) {
            if (this.isFirstShow) {
                this.isFirstShow = false;
            } else if (PlayerManager.getInstance().isPlayingBefore()) {
                this.mVideoView.getVideoView().start();
                this.mVideoView.getControllerView().show();
            } else {
                this.mVideoView.getControllerView().show(0);
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mUri != null && !PlayerManager.getInstance().isFullScreenManualQuit()) {
            PlayerManager.getInstance().savePlayingState(this.mVideoView.getVideoView().isPlaying
                    ());
            this.mVideoView.getVideoView().pause();
            this.mVideoView.getControllerView().hide();
        }
    }

    protected void onStop() {
        super.onStop();
        PlayerManager.getInstance().openPrevVideo();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (PlayerManager.getInstance().isFullScreenManualQuit()) {
            PlayerManager.getInstance().resumeCheck();
        } else {
            PlayerManager.getInstance().releaseAll();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mUri != null) {
            this.mVideoView.quitFullScreen();
        }
    }
}
