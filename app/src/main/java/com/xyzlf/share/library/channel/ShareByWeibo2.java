package com.xyzlf.share.library.channel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.request.AbstractAsyncTask;
import com.xyzlf.share.library.request.BitmapAsyncTask;
import com.xyzlf.share.library.util.ChannelUtil;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;

/* loaded from: classes2.dex */
public class ShareByWeibo2 extends ShareBase {
    private ShareEntity data;
    private OnShareListener listener;

    public ShareByWeibo2(Context context) {
        super(context);
        this.context = context.getApplicationContext();
    }

    @Override // com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, OnShareListener onShareListener) {
        BitmapDrawable bitmapDrawable;
        boolean isWeiboInstall = ChannelUtil.isWeiboInstall(this.context);
        boolean isWeiboLiteInstall = !isWeiboInstall ? ChannelUtil.isWeiboLiteInstall(this.context) : false;
        if (!isWeiboInstall && !isWeiboLiteInstall) {
            ToastUtil.showToast(this.context, R.string.share_no_weibo_client, true);
            onShareListener.onShare(4, 2);
            return;
        }
        if (shareEntity == null) {
            onShareListener.onShare(4, 2);
            return;
        }
        this.listener = onShareListener;
        this.data = shareEntity;
        if (!TextUtils.isEmpty(shareEntity.getImgUrl())) {
            if (shareEntity.getImgUrl().startsWith("http")) {
                new BitmapAsyncTask(shareEntity.getImgUrl(), new BitmapAsyncTask.OnBitmapListener() { // from class: com.xyzlf.share.library.channel.ShareByWeibo2.1
                    @Override // com.xyzlf.share.library.request.BitmapAsyncTask.OnBitmapListener
                    public void onSuccess(Bitmap bitmap) {
                        ShareByWeibo2.this.localSyncTask(bitmap);
                    }

                    @Override // com.xyzlf.share.library.request.BitmapAsyncTask.OnBitmapListener
                    public void onException(Exception exc) {
                        ShareByWeibo2.this.localSyncTask(null);
                    }
                }).execute(new Void[0]);
                return;
            } else {
                localSyncTask();
                return;
            }
        }
        if (shareEntity.getDrawableId() != 0) {
            try {
                bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(this.context, shareEntity.getDrawableId());
            } catch (Exception unused) {
                bitmapDrawable = null;
            }
            if (bitmapDrawable != null) {
                localSyncTask(bitmapDrawable.getBitmap());
                return;
            } else {
                localSyncTask(null);
                return;
            }
        }
        localSyncTask(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.xyzlf.share.library.channel.ShareByWeibo2$2] */
    public void localSyncTask(final Bitmap bitmap) {
        new AbstractAsyncTask<String>() { // from class: com.xyzlf.share.library.channel.ShareByWeibo2.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.xyzlf.share.library.request.AbstractAsyncTask
            public String doLoadData() throws Exception {
                String saveBitmapToSDCard;
                if (bitmap != null) {
                    saveBitmapToSDCard = ShareUtil.saveBitmapToSDCard(ShareByWeibo2.this.context, bitmap);
                } else {
                    Context context = ShareByWeibo2.this.context;
                    ShareByWeibo2 shareByWeibo2 = ShareByWeibo2.this;
                    saveBitmapToSDCard = ShareUtil.saveBitmapToSDCard(context, shareByWeibo2.getDefaultBitmap(shareByWeibo2.context));
                }
                ShareByWeibo2.this.weiboShare(saveBitmapToSDCard);
                return null;
            }
        }.execute(new Void[0]);
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [com.xyzlf.share.library.channel.ShareByWeibo2$3] */
    private void localSyncTask() {
        if (!TextUtils.isEmpty(this.data.getImgUrl()) && !this.data.getImgUrl().startsWith("http")) {
            new AbstractAsyncTask<String>() { // from class: com.xyzlf.share.library.channel.ShareByWeibo2.3
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.xyzlf.share.library.request.AbstractAsyncTask
                public String doLoadData() throws Exception {
                    ShareByWeibo2 shareByWeibo2 = ShareByWeibo2.this;
                    shareByWeibo2.weiboShare(shareByWeibo2.data.getImgUrl());
                    return null;
                }
            }.execute(new Void[0]);
        } else {
            localSyncTask(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void weiboShare(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setFlags(268435456);
        if (!TextUtils.isEmpty(str)) {
            intent.setType("image/*");
        } else {
            intent.setType("text/plain");
        }
        try {
            String str2 = "";
            ResolveInfo resolveInfo = null;
            for (ResolveInfo resolveInfo2 : this.context.getPackageManager().queryIntentActivities(intent, 65536)) {
                String str3 = resolveInfo2.activityInfo.applicationInfo.packageName;
                if (!ShareConstant.SINA_WEIBO_PACKAGE_NAME.equals(str3) && !ShareConstant.SINA_WEIBO_LITE_PACKAGE_NAME.equals(str3)) {
                    str2 = str3;
                }
                resolveInfo = resolveInfo2;
                str2 = str3;
            }
            if (resolveInfo != null) {
                intent.setClassName(str2, resolveInfo.activityInfo.name);
                StringBuilder sb = new StringBuilder();
                if (!TextUtils.isEmpty(this.data.getContent())) {
                    sb.append(this.data.getContent());
                }
                if (!TextUtils.isEmpty(this.data.getUrl())) {
                    sb.append(" ");
                    sb.append(this.data.getUrl());
                }
                if (!TextUtils.isEmpty(sb.toString())) {
                    intent.putExtra("android.intent.extra.TEXT", sb.toString());
                }
                if (!TextUtils.isEmpty(str)) {
                    intent.putExtra("android.intent.extra.STREAM", Uri.parse(str));
                }
                this.context.startActivity(intent);
                this.listener.onShare(4, 1);
                return;
            }
            this.listener.onShare(4, 2);
        } catch (Exception unused) {
            this.listener.onShare(4, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap getDefaultBitmap(Context context) {
        Drawable drawable;
        try {
            drawable = context.getPackageManager().getApplicationIcon(context.getPackageName());
        } catch (Exception unused) {
            drawable = null;
        }
        if (drawable != null && (drawable instanceof BitmapDrawable)) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.share_default);
    }
}
