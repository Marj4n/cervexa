package com.jieli.stream.dv.running2.ui.widget.media;

import android.view.View;
import android.widget.MediaController;

/* loaded from: classes.dex */
public interface IMediaController {
    void hide();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean z);

    void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl);

    void show();

    void show(int i);

    void showOnce(View view);
}
