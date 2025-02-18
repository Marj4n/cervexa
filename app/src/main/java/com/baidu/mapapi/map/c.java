package com.baidu.mapapi.map;

import android.os.Bundle;
import java.util.concurrent.locks.Lock;

/* loaded from: classes.dex */
class c implements com.baidu.platform.comapi.map.q {
    final /* synthetic */ BaiduMap a;

    c(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.platform.comapi.map.q
    public Bundle a(int i, int i2, int i3) {
        Lock lock;
        Lock lock2;
        HeatMap heatMap;
        Lock lock3;
        HeatMap heatMap2;
        lock = this.a.E;
        lock.lock();
        try {
            heatMap = this.a.D;
            if (heatMap != null) {
                heatMap2 = this.a.D;
                Tile a = heatMap2.a(i, i2, i3);
                if (a != null) {
                    return a.toBundle();
                }
            }
            lock3 = this.a.E;
            lock3.unlock();
            return null;
        } finally {
            lock2 = this.a.E;
            lock2.unlock();
        }
    }
}
