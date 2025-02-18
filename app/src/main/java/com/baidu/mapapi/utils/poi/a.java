package com.baidu.mapapi.utils.poi;

import android.content.Context;
import android.util.Log;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.platform.comapi.pano.a;
import com.baidu.platform.comapi.pano.c;

/* loaded from: classes.dex */
final class a implements a.InterfaceC0013a<c> {
    final /* synthetic */ Context a;

    a(Context context) {
        this.a = context;
    }

    @Override // com.baidu.platform.comapi.pano.a.InterfaceC0013a
    public void a(HttpClient.HttpStateError httpStateError) {
        String str;
        int i = BaiduMapPoiSearch.AnonymousClass1.b[httpStateError.ordinal()];
        if (i == 1) {
            str = "current network is not available";
        } else if (i != 2) {
            return;
        } else {
            str = "network inner error, please check network";
        }
        Log.d("baidumapsdk", str);
    }

    @Override // com.baidu.platform.comapi.pano.a.InterfaceC0013a
    public void a(c cVar) {
        String str;
        if (cVar == null) {
            Log.d("baidumapsdk", "pano info is null");
            return;
        }
        int i = BaiduMapPoiSearch.AnonymousClass1.a[cVar.a().ordinal()];
        if (i == 1) {
            str = "pano uid is error, please check param poi uid";
        } else if (i == 2) {
            str = "pano id not found for this poi point";
        } else if (i == 3) {
            str = "please check ak for permission";
        } else {
            if (i != 4) {
                return;
            }
            if (cVar.c() == 1) {
                try {
                    BaiduMapPoiSearch.b(cVar.b(), this.a);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            str = "this point do not support for pano show";
        }
        Log.d("baidumapsdk", str);
    }
}
