package com.baidu.mapapi.utils;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.baidu.mapframework.open.aidl.IComOpenClient;
import com.baidu.mapframework.open.aidl.b;

/* loaded from: classes.dex */
final class b extends b.a {
    final /* synthetic */ int a;

    b(int i) {
        this.a = i;
    }

    @Override // com.baidu.mapframework.open.aidl.b
    public void a(IBinder iBinder) throws RemoteException {
        String str;
        IComOpenClient iComOpenClient;
        str = a.c;
        Log.d(str, "onClientReady");
        iComOpenClient = a.e;
        if (iComOpenClient != null) {
            IComOpenClient unused = a.e = null;
        }
        IComOpenClient unused2 = a.e = IComOpenClient.a.a(iBinder);
        a.a(this.a);
        boolean unused3 = a.t = true;
    }
}
