package com.xyzlf.share.library.channel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.util.ShareUtil;
import com.xyzlf.share.library.util.ToastUtil;

/* loaded from: classes2.dex */
public class ShareBySystem extends ShareBase {
    public ShareBySystem(Context context) {
        super(context);
    }

    @Override // com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, OnShareListener onShareListener) {
        String str;
        if (shareEntity == null || TextUtils.isEmpty(shareEntity.getContent())) {
            ToastUtil.showToast(this.context, R.string.share_empty_tip, true);
            return;
        }
        if (TextUtils.isEmpty(shareEntity.getContent())) {
            str = shareEntity.getTitle() + shareEntity.getUrl();
        } else {
            str = shareEntity.getContent() + shareEntity.getUrl();
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.setType("text/plain");
        if (ShareUtil.startActivity(this.context, Intent.createChooser(intent, this.context.getString(R.string.share_to)))) {
            if (onShareListener != null) {
                onShareListener.onShare(1024, 1);
            }
        } else if (onShareListener != null) {
            onShareListener.onShare(1024, 2);
        }
    }
}
