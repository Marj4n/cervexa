package com.baidu.mapapi.map;

import android.content.Context;
import android.os.Bundle;
import com.baidu.platform.comapi.map.L;
import java.util.concurrent.locks.Lock;

/* loaded from: classes.dex */
class d implements L {
    final /* synthetic */ BaiduMap a;

    d(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.platform.comapi.map.L
    public Bundle a(int i, int i2, int i3, Context context) {
        Lock lock;
        Lock lock2;
        TileOverlay tileOverlay;
        Lock lock3;
        TileOverlay tileOverlay2;
        lock = this.a.F;
        lock.lock();
        try {
            tileOverlay = this.a.C;
            if (tileOverlay != null) {
                tileOverlay2 = this.a.C;
                Tile a = tileOverlay2.a(i, i2, i3);
                if (a != null) {
                    return a.toBundle();
                }
            }
            lock3 = this.a.F;
            lock3.unlock();
            return null;
        } finally {
            lock2 = this.a.F;
            lock2.unlock();
        }
    }
}
