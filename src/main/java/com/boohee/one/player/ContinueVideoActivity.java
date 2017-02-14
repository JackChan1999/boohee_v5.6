package com.boohee.one.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.one.R;

public class ContinueVideoActivity extends AppCompatActivity {
    public static final String TAG       = "ContinueVideoActivity";
    public static final String VIDEO_URL = "VIDEO_URL";
    private View        mBtnBack;
    private View        mHeaderView;
    private String      mUri;
    private ExVideoView mVideoView;

    public static void startActivity(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent i = new Intent(context, ContinueVideoActivity.class);
            i.putExtra("VIDEO_URL", url);
            PlayerManager.getInstance().prepareFullScreen();
            context.startActivity(i);
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
        this.mVideoView.getControllerView().addBindView(this.mHeaderView);
        this.mBtnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ContinueVideoActivity.this.onBackPressed();
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        if (this.mUri != null && !PlayerManager.getInstance().isFullScreenManualQuit()) {
            this.mVideoView.getVideoView().pause();
            this.mVideoView.getControllerView().hide();
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.mUri != null) {
            if (PlayerManager.getInstance().isFullScreenManualQuit()) {
                PlayerManager.getInstance().resumeCheck();
                PlayerManager.getInstance().openPrevVideo();
                return;
            }
            PlayerManager.getInstance().releaseAll();
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mUri != null) {
            this.mVideoView.quitFullScreen();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
