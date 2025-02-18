package com.xyzlf.share.library;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.tencent.tauth.Tencent;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.channel.ShareByEmail;
import com.xyzlf.share.library.channel.ShareByQQ;
import com.xyzlf.share.library.channel.ShareByQZone;
import com.xyzlf.share.library.channel.ShareBySms;
import com.xyzlf.share.library.channel.ShareBySystem;
import com.xyzlf.share.library.channel.ShareByWeibo2;
import com.xyzlf.share.library.channel.ShareByWeixin;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.interfaces.ShareConstant;

/* loaded from: classes2.dex */
public class ShareHandlerActivity extends ShareBaseActivity implements OnShareListener {
    protected ShareEntity data;
    protected ShareByWeibo2 shareByWeibo;
    protected ShareByWeixin shareByWeixin;
    private String tag = getClass().getSimpleName();
    protected boolean isInit = true;

    @Override // com.xyzlf.share.library.ShareBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        Parcelable parcelable;
        requestWindowFeature(1);
        super.onCreate(bundle);
        try {
            parcelable = getIntent().getParcelableExtra(ShareConstant.EXTRA_SHARE_DATA);
        } catch (Exception unused) {
            parcelable = null;
        }
        if (parcelable == null || !(parcelable instanceof ShareEntity)) {
            Log.e(this.tag, "object error");
            finish();
            return;
        }
        this.data = (ShareEntity) parcelable;
        if (bundle == null) {
            ShareByWeixin shareByWeixin = this.shareByWeixin;
            if (shareByWeixin != null) {
                shareByWeixin.unregisterWeixinReceiver();
                this.shareByWeixin = null;
            }
            int i = this.channel;
            if (i == 1) {
                ShareByWeixin shareByWeixin2 = new ShareByWeixin(this, 1);
                this.shareByWeixin = shareByWeixin2;
                shareByWeixin2.registerWeixinReceiver();
                this.shareByWeixin.share(this.data, this);
                Log.w(this.tag, "Share wx friend");
                return;
            }
            if (i == 2) {
                ShareByWeixin shareByWeixin3 = new ShareByWeixin(this, 2);
                this.shareByWeixin = shareByWeixin3;
                shareByWeixin3.registerWeixinReceiver();
                this.shareByWeixin.share(this.data, this);
                return;
            }
            if (i == 4) {
                ShareByWeibo2 shareByWeibo2 = new ShareByWeibo2(this);
                this.shareByWeibo = shareByWeibo2;
                shareByWeibo2.share(this.data, this);
                return;
            }
            if (i == 8) {
                new ShareByQQ(this).share(this.data, this);
                return;
            }
            if (i == 16) {
                new ShareByQZone(this).share(this.data, this);
                return;
            }
            if (i == 32) {
                new ShareBySms(this).share(this.data, this);
                return;
            }
            if (i == 64) {
                new ShareByEmail(this).share(this.data, this);
            } else if (i == 1024) {
                new ShareBySystem(this).share(this.data, this);
            } else {
                finishWithResult(this.channel, 4);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (!this.isInit) {
            finishWithResult(this.channel, 4);
        } else {
            this.isInit = false;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10103 || i == 10104) {
            Tencent.onActivityResultData(i, i2, intent, null);
        }
    }

    @Override // com.xyzlf.share.library.interfaces.OnShareListener
    public void onShare(int i, int i2) {
        finishWithResult(i, i2);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ShareByWeixin shareByWeixin = this.shareByWeixin;
        if (shareByWeixin != null) {
            shareByWeixin.unregisterWeixinReceiver();
        }
    }
}
