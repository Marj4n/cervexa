package com.xyzlf.share.library.channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
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
    }
}
