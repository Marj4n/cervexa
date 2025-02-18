package com.baidu.mapapi.map;

import android.view.View;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.GestureDetectorOnDoubleTapListenerC0079j;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
class j implements View.OnClickListener {
    final /* synthetic */ MapView a;

    j(MapView mapView) {
        this.a = mapView;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j;
        GestureDetectorOnDoubleTapListenerC0079j gestureDetectorOnDoubleTapListenerC0079j2;
        gestureDetectorOnDoubleTapListenerC0079j = this.a.c;
        E D = gestureDetectorOnDoubleTapListenerC0079j.a().D();
        D.a -= 1.0f;
        gestureDetectorOnDoubleTapListenerC0079j2 = this.a.c;
        gestureDetectorOnDoubleTapListenerC0079j2.a().a(D, IjkMediaCodecInfo.RANK_SECURE);
    }
}
