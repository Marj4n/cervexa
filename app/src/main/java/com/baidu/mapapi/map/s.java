package com.baidu.mapapi.map;

import android.view.View;
import com.baidu.platform.comapi.map.E;
import com.baidu.platform.comapi.map.F;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

/* loaded from: classes.dex */
class s implements View.OnClickListener {
    final /* synthetic */ TextureMapView a;

    s(TextureMapView textureMapView) {
        this.a = textureMapView;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        F f;
        F f2;
        f = this.a.b;
        E D = f.b().D();
        D.a += 1.0f;
        f2 = this.a.b;
        f2.b().a(D, IjkMediaCodecInfo.RANK_SECURE);
    }
}
