package com.boohee.one.player;

import android.widget.MediaController.MediaPlayerControl;

public interface IMediaController {
    void hide();

    boolean isShowing();

    void setEnabled(boolean z);

    void setMediaPlayer(MediaPlayerControl mediaPlayerControl);

    void show();

    void show(int i);
}
