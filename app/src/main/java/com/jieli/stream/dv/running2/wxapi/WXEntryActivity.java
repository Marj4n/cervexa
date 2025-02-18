package com.jieli.stream.dv.running2.wxapi;

import android.content.Intent;
import android.os.Bundle;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import com.jieli.stream.dv.running2.util.Dbug;
import com.jieli.stream.dv.running2.util.ManifestUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/* loaded from: classes.dex */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final int TIMELINE_SUPPORTED_VERSION = 553779201;
    private IWXAPI api;
    private String tag = getClass().getSimpleName();

    @Override // com.jieli.stream.dv.running2.ui.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        IWXAPI createWXAPI = WXAPIFactory.createWXAPI(this, ManifestUtil.getWeixinKey(this), false);
        this.api = createWXAPI;
        createWXAPI.handleIntent(getIntent(), this);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Dbug.e(this.tag, "onNewIntent---");
        setIntent(intent);
        this.api.handleIntent(intent, this);
    }

    @Override // com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
    public void onReq(BaseReq baseReq) {
        Dbug.e(this.tag, "onReq-----");
    }

    @Override // com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
    public void onResp(BaseResp baseResp) {
        Dbug.e(this.tag, "onResp------");
        finish();
    }
}
