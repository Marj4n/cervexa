package com.baidu.mapapi.map.offline;

import com.baidu.platform.comapi.map.u;
import com.baidu.platform.comapi.map.y;
import java.util.ArrayList;

/* loaded from: classes.dex */
class a implements y {
    final /* synthetic */ MKOfflineMap a;

    a(MKOfflineMap mKOfflineMap) {
        this.a = mKOfflineMap;
    }

    @Override // com.baidu.platform.comapi.map.y
    public void a(int i, int i2) {
        MKOfflineMapListener mKOfflineMapListener;
        MKOfflineMapListener mKOfflineMapListener2;
        MKOfflineMapListener mKOfflineMapListener3;
        u uVar;
        if (i == 4) {
            ArrayList<MKOLUpdateElement> allUpdateInfo = this.a.getAllUpdateInfo();
            if (allUpdateInfo != null) {
                for (MKOLUpdateElement mKOLUpdateElement : allUpdateInfo) {
                    if (mKOLUpdateElement.update) {
                        mKOfflineMapListener = this.a.c;
                        mKOfflineMapListener.onGetOfflineMapState(4, mKOLUpdateElement.cityID);
                    }
                }
                return;
            }
            return;
        }
        int i3 = 6;
        if (i == 6) {
            mKOfflineMapListener2 = this.a.c;
        } else if (i == 8) {
            int i4 = 65535 & (i2 >> 8);
            mKOfflineMapListener3 = this.a.c;
            mKOfflineMapListener3.onGetOfflineMapState(0, i4);
            return;
        } else {
            if (i != 10) {
                if (i != 12) {
                    return;
                }
                uVar = this.a.b;
                uVar.a(true, false);
                return;
            }
            mKOfflineMapListener2 = this.a.c;
            i3 = 2;
        }
        mKOfflineMapListener2.onGetOfflineMapState(i3, i2);
    }
}
