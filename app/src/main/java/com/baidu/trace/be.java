package com.baidu.trace;

import android.content.Context;
import android.os.Handler;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.QueryCacheTrackRequest;
import com.baidu.trace.api.track.QueryCacheTrackResponse;
import com.baidu.trace.model.StatusCodes;
import java.util.ArrayList;

/* loaded from: classes.dex */
final class be implements Runnable {
    final /* synthetic */ QueryCacheTrackResponse a;
    final /* synthetic */ OnTrackListener b;
    private /* synthetic */ Context c;
    private /* synthetic */ QueryCacheTrackRequest d;
    private /* synthetic */ Handler e;

    be(Context context, QueryCacheTrackRequest queryCacheTrackRequest, QueryCacheTrackResponse queryCacheTrackResponse, Handler handler, OnTrackListener onTrackListener) {
        this.c = context;
        this.d = queryCacheTrackRequest;
        this.a = queryCacheTrackResponse;
        this.e = handler;
        this.b = onTrackListener;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ArrayList arrayList = new ArrayList();
        if (!am.a(this.c, this.d.getEntityName(), arrayList)) {
            this.a.setStatus(StatusCodes.REQUEST_FAILED);
            this.a.setMessage(StatusCodes.MSG_REQUEST_FAILED);
        }
        this.a.setResult(arrayList);
        this.e.post(new bf(this));
    }
}
