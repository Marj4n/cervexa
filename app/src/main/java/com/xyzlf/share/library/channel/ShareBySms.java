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
public class ShareBySms extends ShareBase {
    public ShareBySms(Context context) {
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
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("smsto:"));
        intent.putExtra("sms_body", str);
        intent.setType("vnd.android-dir/mms-sms");
        if (ShareUtil.startActivity(this.context, intent)) {
            if (onShareListener != null) {
                onShareListener.onShare(32, 1);
            }
        } else if (onShareListener != null) {
            onShareListener.onShare(32, 2);
        }
    }
}
