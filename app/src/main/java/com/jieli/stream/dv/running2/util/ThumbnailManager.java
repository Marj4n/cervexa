package com.jieli.stream.dv.running2.util;

import com.jieli.lib.dv.control.player.VideoThumbnail;

/* loaded from: classes.dex */
public class ThumbnailManager {
    private static VideoThumbnail instance;

    public static VideoThumbnail getInstance() {
        if (instance == null) {
            synchronized (ThumbnailManager.class) {
                if (instance == null) {
                    instance = new VideoThumbnail();
                }
            }
        }
        return instance;
    }

    public static void release() {
        VideoThumbnail videoThumbnail = instance;
        if (videoThumbnail != null) {
            videoThumbnail.release();
            instance = null;
        }
    }
}
