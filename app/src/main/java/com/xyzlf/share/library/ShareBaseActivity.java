package com.xyzlf.share.library;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.xyzlf.share.library.interfaces.ShareConstant;

/* loaded from: classes2.dex */
public abstract class ShareBaseActivity extends FragmentActivity {
    protected int channel;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getIntent() == null) {
            finish();
        } else {
            initChannel();
        }
    }

    protected void initChannel() {
        try {
            this.channel = getIntent().getIntExtra(ShareConstant.EXTRA_SHARE_CHANNEL, -1);
        } catch (Exception unused) {
        }
    }

    public void finishWithResult(int i, int i2) {
        Intent intent = new Intent();
        intent.putExtra(ShareConstant.EXTRA_SHARE_CHANNEL, i);
        intent.putExtra(ShareConstant.EXTRA_SHARE_STATUS, i2);
        setResult(-1, intent);
        finish();
    }
}
