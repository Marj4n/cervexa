package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.track.CacheTrackInfo;
import com.baidu.trace.api.track.ClearCacheTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.StatusCodes;
import java.util.List;

/* loaded from: classes.dex */
final class bg implements Runnable {
    final /* synthetic */ ClearCacheTrackResponse a;
    final /* synthetic */ OnTrackListener b;
    private /* synthetic */ Context c;
    private /* synthetic */ List d;
    private /* synthetic */ List e;
    private /* synthetic */ Handler f;

    bg(Context context, List list, List list2, ClearCacheTrackResponse clearCacheTrackResponse, Handler handler, OnTrackListener onTrackListener) {
        this.c = context;
        this.d = list;
        this.e = list2;
        this.a = clearCacheTrackResponse;
        this.f = handler;
        this.b = onTrackListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (!am.a(this.c, (List<String>) this.d, (List<CacheTrackInfo>) this.e)) {
            this.a.setStatus(StatusCodes.REQUEST_FAILED);
            this.a.setMessage(StatusCodes.MSG_REQUEST_FAILED);
        }
        this.f.post(new bh(this));
    }
}
