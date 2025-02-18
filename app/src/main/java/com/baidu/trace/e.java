package com.baidu.trace;

import com.baidu.lbsapi.auth.tracesdk.LBSAuthManagerListener;

/* loaded from: classes.dex */
final class e implements LBSAuthManagerListener {
    e() {
    }

    @Override // com.baidu.lbsapi.auth.tracesdk.LBSAuthManagerListener
    public final void onAuthResult(int i, String str) {
        if (i == 0) {
            a.b(str);
        }
    }
}
