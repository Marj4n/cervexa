package com.xyzlf.share.library.channel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;

/* loaded from: classes2.dex */
public class ShareByEmail extends ShareBase {
    public ShareByEmail(Context context) {
        super(context);
    }

    @Override // com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, OnShareListener onShareListener) {
        if (shareEntity == null || TextUtils.isEmpty(shareEntity.getContent())) {
            ToastUtil.showToast(this.context, R.string.share_empty_tip, true);
            return;
        }
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse("mailto:"));
        if (!TextUtils.isEmpty(shareEntity.getTitle())) {
            intent.putExtra("android.intent.extra.SUBJECT", shareEntity.getTitle());
        }
        intent.putExtra("android.intent.extra.TEXT", shareEntity.getContent() + shareEntity.getUrl());
        if (ShareUtil.startActivity(this.context, intent)) {
            if (onShareListener != null) {
                onShareListener.onShare(64, 1);
            }
        } else if (onShareListener != null) {
            onShareListener.onShare(64, 2);
        }
    }
}
