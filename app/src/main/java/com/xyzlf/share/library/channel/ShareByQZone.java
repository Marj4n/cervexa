package com.xyzlf.share.library.channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.util.ToastUtil;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ShareByQZone extends ShareByQQ {
    public ShareByQZone(Context context) {
        super(context);
    }

    @Override // com.xyzlf.share.library.channel.ShareByQQ, com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, final OnShareListener onShareListener) {
        if (shareEntity == null || this.context == null || !(this.context instanceof Activity)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("req_type", 1);
        bundle.putString("title", shareEntity.getTitle());
        bundle.putString("summary", shareEntity.getContent());
        bundle.putString("targetUrl", shareEntity.getUrl());
        ArrayList<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(shareEntity.getImgUrl())) {
            arrayList.add(shareEntity.getImgUrl());
        }
        bundle.putStringArrayList("imageUrl", arrayList);
        this.mTencent.shareToQzone((Activity) this.context, bundle, new IUiListener() { // from class: com.xyzlf.share.library.channel.ShareByQZone.1
            @Override // com.tencent.tauth.IUiListener
            public void onComplete(Object obj) {
                OnShareListener onShareListener2 = onShareListener;
                if (onShareListener2 != null) {
                    onShareListener2.onShare(16, 1);
                }
                ToastUtil.showToast(ShareByQZone.this.context, R.string.share_success, true);
            }

            @Override // com.tencent.tauth.IUiListener
            public void onError(UiError uiError) {
                OnShareListener onShareListener2 = onShareListener;
                if (onShareListener2 != null) {
                    onShareListener2.onShare(16, 2);
                }
                if (uiError != null) {
                    ToastUtil.showToast(ShareByQZone.this.context, uiError.errorMessage, true);
                }
            }

            @Override // com.tencent.tauth.IUiListener
            public void onCancel() {
                OnShareListener onShareListener2 = onShareListener;
                if (onShareListener2 != null) {
                    onShareListener2.onShare(16, 3);
                }
                ToastUtil.showToast(ShareByQZone.this.context, R.string.share_cancel, true);
            }
        });
    }
}
