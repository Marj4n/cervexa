package com.baidu.platform.comapi.pano;

import com.baidu.mapapi.http.HttpClient;
import com.baidu.platform.comapi.pano.a;

/* loaded from: classes.dex */
class b extends HttpClient.ProtoResultCallback {
    final /* synthetic */ a.InterfaceC0013a a;
    final /* synthetic */ a b;

    b(a aVar, a.InterfaceC0013a interfaceC0013a) {
        this.b = aVar;
        this.a = interfaceC0013a;
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onFailed(HttpClient.HttpStateError httpStateError) {
        this.a.a(httpStateError);
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onSuccess(String str) {
        c a;
        a.InterfaceC0013a interfaceC0013a = this.a;
        a = this.b.a(str);
        interfaceC0013a.a((a.InterfaceC0013a) a);
    }
}
