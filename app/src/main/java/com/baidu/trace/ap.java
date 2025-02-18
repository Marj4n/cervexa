package com.baidu.trace;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.baidu.trace.IService;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.model.StatusCodes;
import java.io.PrintWriter;
import java.io.StringWriter;

/* loaded from: classes.dex */
final class ap implements ServiceConnection {
    private /* synthetic */ LBSTraceClient a;

    ap(LBSTraceClient lBSTraceClient) {
        this.a = lBSTraceClient;
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LBSTraceClient.ClientListener clientListener;
        this.a.d = IService.Stub.asInterface(iBinder);
        try {
            try {
                IService iService = this.a.d;
                clientListener = this.a.e;
                iService.registerListener(clientListener);
                if (this.a.h != null) {
                    this.a.h.onBindServiceCallback(0, StatusCodes.MSG_SUCCESS);
                }
            } catch (Exception e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                a.a("BaiduTraceSDK", "register client listener failed, Exception : " + stringWriter.toString());
                if (this.a.h != null) {
                    this.a.h.onBindServiceCallback(1, StatusCodes.MSG_FAILED);
                }
            }
        } catch (Throwable th) {
            if (this.a.h != null) {
                this.a.h.onBindServiceCallback(0, StatusCodes.MSG_SUCCESS);
            }
            throw th;
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.a.d = null;
    }
}
