package com.xyzlf.share.library.channel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.util.ManifestUtil;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;
import java.util.Iterator;


/* loaded from: classes2.dex */
public class ShareByQQ extends ShareBase {

    public ShareByQQ(Context context) {
        super(context);
    }

    @Override // com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, final OnShareListener onShareListener) {
        String str;
        if (shareEntity == null || this.context == null) {
            return;
        }
        if (shareEntity.isShareBigImg() && !TextUtils.isEmpty(shareEntity.getImgUrl()) && !shareEntity.getImgUrl().startsWith("http")) {
            Bundle bundle = new Bundle();
            bundle.putString("imageLocalUrl", shareEntity.getImgUrl());
            bundle.putInt("req_type", 5);
            bundle.putInt("cflag", 2);
            return;
        }
        if (!TextUtils.isEmpty(shareEntity.getUrl()) && !TextUtils.isEmpty(shareEntity.getTitle())) {
            if (this.context instanceof Activity) {
                Bundle bundle2 = new Bundle();
                bundle2.putInt("req_type", 1);
                bundle2.putString("title", shareEntity.getTitle());
                bundle2.putString("summary", shareEntity.getContent());
                bundle2.putString("targetUrl", shareEntity.getUrl());
                if (!TextUtils.isEmpty(shareEntity.getImgUrl())) {
                    bundle2.putString("imageUrl", shareEntity.getImgUrl());
                }
                return;
            }
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        Iterator<ResolveInfo> it = this.context.getPackageManager().queryIntentActivities(intent, 0).iterator();
        while (true) {
            if (!it.hasNext()) {
                str = "";
                break;
            }
            ResolveInfo next = it.next();
            if (TextUtils.equals("com.tencent.mobileqq", next.activityInfo.packageName)) {
                str = next.activityInfo.name;
                break;
            }
        }
        Intent intent2 = new Intent("android.intent.action.SEND");
        intent2.setType("text/plain");
        intent2.putExtra("android.intent.extra.SUBJECT", shareEntity.getTitle());
        intent2.setClassName("com.tencent.mobileqq", str);
        intent2.putExtra("android.intent.extra.TEXT", shareEntity.getContent());
        if (ShareUtil.startActivity(this.context, intent2)) {
            if (onShareListener != null) {
                onShareListener.onShare(8, 1);
            }
        } else if (onShareListener != null) {
            onShareListener.onShare(8, 2);
        }
    }
}
