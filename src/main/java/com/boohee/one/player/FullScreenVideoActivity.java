package com.boohee.one.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.boohee.one.R;
import com.boohee.one.ui.BaseActivity;

public class FullScreenVideoActivity extends BaseActivity {
    public static final String  TAG         = "FullScreenActivity";
    public static final String  VIDEO_URL   = "VIDEO_URL";
    private             boolean isFirstShow = true;
    private View        mBtnClose;
    private View        mHeaderView;
    private String      mUri;
    private ExVideoView mVideoView;

    public static void startActivity(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent i = new Intent(context, FullScreenVideoActivity.class);
            i.putExtra("VIDEO_URL", url);
            context.startActivity(i);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDelegate().setContentView((int) R.layout.bc);
        initVariables();
        initViews();
    }

    private void initVariables() {
        PlayerManager.getInstance().resetManualQuitState();
        if (TextUtils.isEmpty(getIntent().getStringExtra("VIDEO_URL"))) {
            finish();
        } else {
            this.mUri = getIntent().getStringExtra("VIDEO_URL");
        }
    }

    private void initViews() {
        this.mVideoView = (ExVideoView) findViewById(R.id.video);
        this.mBtnClose = findViewById(R.id.btn_close);
        this.mHeaderView = findViewById(R.id.header);
        this.mVideoView.getControllerView().addBindView(this.mHeaderView);
        this.mVideoView.setVideo(this.mUri);
        this.mBtnClose.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FullScreenVideoActivity.this.mVideoView.getVideoView().pause();
                FullScreenVideoActivity.this.finish();
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
        if (this.mUri != null) {
            PlayerManager.getInstance().savePlayingState(this.mVideoView.getVideoView().isPlaying
                    ());
            this.mVideoView.getVideoView().pause();
            this.mVideoView.getControllerView().hide();
        }
    }

    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mUri != null) {
            this.mVideoView.getVideoView().pause();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mUri != null) {
            PlayerManager.getInstance().releaseAll();
        }
    }
}
