package com.jieli.stream.dv.running2.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;

/* loaded from: classes.dex */
public class UserAgreementActivity extends BaseActivity {
    private final String USER_PROTOCOL = "http://cam.jieli.net:28111/app/app.user.service.protocol.html";

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        WebView webView = new WebView(this);
        webView.loadUrl("http://cam.jieli.net:28111/app/app.user.service.protocol.html");
        setContentView(webView);
    }
}
