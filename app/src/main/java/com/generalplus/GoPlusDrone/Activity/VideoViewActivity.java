package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import com.generalplus.GoPlusDrone.R;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VideoViewActivity extends Activity {
    private VideoView m_videoView;
    private MediaController m_mediaController = null;
    private ArrayList<String> m_ayFilePath = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_videoview);
        this.m_videoView = (VideoView) findViewById(R.id.videoView);
        this.m_mediaController = new MediaController(this);
        Intent intent = getIntent();
        int i = intent.getExtras().getInt(IConstant.KEY_POSITION);
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("FilePath");
        this.m_ayFilePath = stringArrayListExtra;
        this.m_videoView.setVideoPath(stringArrayListExtra.get(i));
        this.m_videoView.setMediaController(this.m_mediaController);
        this.m_videoView.start();
        this.m_videoView.requestFocus();
    }
}
