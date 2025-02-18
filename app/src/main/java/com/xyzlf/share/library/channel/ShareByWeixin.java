package com.xyzlf.share.library.channel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xyzlf.com.share.library.R;
import com.xyzlf.share.library.bean.ShareEntity;
import com.xyzlf.share.library.interfaces.OnShareListener;
import com.xyzlf.share.library.interfaces.ShareConstant;
import com.xyzlf.share.library.request.BitmapAsyncTask;
import com.xyzlf.share.library.util.ManifestUtil;
import com.xyzlf.share.library.util.ToastUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;

/* loaded from: classes2.dex */
public class ShareByWeixin extends ShareBase {
    private static final int THUMB_SIZE = 250;
    private IWXAPI api;
    private int channel;
    private ShareEntity data;
    private OnShareListener listener;
    private WeixinShareReceiver receiver;
    private String tag;

    public ShareByWeixin(Context context, int i) {
        super(context);
        this.tag = getClass().getSimpleName();
        this.context = context.getApplicationContext();
        this.channel = i;
        this.api = WXAPIFactory.createWXAPI(context.getApplicationContext(), ManifestUtil.getWeixinKey(this.context));
    }

    public void registerWeixinReceiver() {
        this.receiver = new WeixinShareReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ShareConstant.ACTION_WEIXIN_CALLBACK);
        this.context.registerReceiver(this.receiver, intentFilter);
    }

    public void unregisterWeixinReceiver() {
        if (this.context == null || this.receiver == null) {
            return;
        }
        this.context.unregisterReceiver(this.receiver);
    }

    @Override // com.xyzlf.share.library.interfaces.IShareBase
    public void share(ShareEntity shareEntity, OnShareListener onShareListener) {
        this.data = shareEntity;
        this.listener = onShareListener;
        if (shareEntity == null) {
            return;
        }
        start();
    }

    private void start() {
        if (this.api.isWXAppInstalled()) {
            String imgUrl = this.data.getImgUrl();
            if (!TextUtils.isEmpty(imgUrl)) {
                if (imgUrl.startsWith("http")) {
                    Log.e(this.tag, "net pic");
                    new BitmapAsyncTask(imgUrl, new BitmapAsyncTask.OnBitmapListener() { // from class: com.xyzlf.share.library.channel.ShareByWeixin.1
                        @Override // com.xyzlf.share.library.request.BitmapAsyncTask.OnBitmapListener
                        public void onSuccess(Bitmap bitmap) {
                            if (ShareByWeixin.this.data.isShareBigImg()) {
                                Log.e(ShareByWeixin.this.tag, "share img");
                                ShareByWeixin shareByWeixin = ShareByWeixin.this;
                                shareByWeixin.shareImg(bitmap, shareByWeixin.listener);
                            } else {
                                Log.e(ShareByWeixin.this.tag, "Send bitmap");
                                ShareByWeixin.this.send(bitmap);
                            }
                        }

                        @Override // com.xyzlf.share.library.request.BitmapAsyncTask.OnBitmapListener
                        public void onException(Exception exc) {
                            Log.e(ShareByWeixin.this.tag, "Send null");
                            ShareByWeixin.this.send();
                        }
                    }).execute(new Void[0]);
                    return;
                }
                Log.e(this.tag, "locale pic: is big img=" + this.data.isShareBigImg());
                if (this.data.isShareBigImg()) {
                    shareImg(getLoacalBitmap(imgUrl), this.listener);
                    return;
                } else {
                    send(getLoacalBitmap(imgUrl));
                    return;
                }
            }
            if (this.data.getDrawableId() != 0) {
                Log.e(this.tag, "resource pic");
                BitmapDrawable bitmapDrawable = null;
                try {
                    bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(this.context, this.data.getDrawableId());
                } catch (Exception unused) {
                }
                if (bitmapDrawable != null) {
                    send(bitmapDrawable.getBitmap());
                    return;
                } else {
                    send();
                    return;
                }
            }
            send();
            return;
        }
        OnShareListener onShareListener = this.listener;
        if (onShareListener != null) {
            onShareListener.onShare(this.channel, 2);
        }
        ToastUtil.showToast(this.context, R.string.share_no_weixin_client, true);
    }

    public Bitmap getLoacalBitmap(String str) {
        if (new File(str).exists()) {
            try {
                return BitmapFactory.decodeFile(str);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return BitmapFactory.decodeResource(this.context.getResources(), R.drawable.share_default);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send() {
        send(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(Bitmap bitmap) {
        Log.e(this.tag, "Bitmap w=" + bitmap.getWidth() + ", h=" + bitmap.getHeight());
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = buildWXMediaMessage(bitmap);
        int i = this.channel;
        if (i == 1) {
            req.scene = 0;
        } else if (i == 2) {
            req.scene = 1;
        }
        this.api.sendReq(req);
    }

    private WXMediaMessage buildWXMediaMessage(Bitmap bitmap) {
        WXMediaMessage wXMediaMessage = new WXMediaMessage();
        wXMediaMessage.title = this.data.getTitle();
        wXMediaMessage.description = this.data.getContent();
        Log.e(this.tag, "Title=" + this.data.getTitle() + ", description=" + this.data.getContent());
        if (TextUtils.isEmpty(this.data.getUrl())) {
            wXMediaMessage.mediaObject = new WXTextObject(this.data.getContent());
        } else {
            if (bitmap != null) {
                wXMediaMessage.setThumbImage(getWxShareBitmap(bitmap));
            } else {
                Bitmap decodeResource = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.share_default);
                if (decodeResource != null) {
                    wXMediaMessage.setThumbImage(getWxShareBitmap(decodeResource));
                }
            }
            wXMediaMessage.mediaObject = new WXWebpageObject(this.data.getUrl());
        }
        return wXMediaMessage;
    }

    protected Bitmap getWxShareBitmap(Bitmap bitmap) {
        float min = Math.min(150.0f / bitmap.getWidth(), 150.0f / bitmap.getHeight());
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * min), (int) (min * bitmap.getHeight()), false);
    }

    private class WeixinShareReceiver extends BroadcastReceiver {
        private WeixinShareReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(ShareConstant.EXTRA_WEIXIN_RESULT)) {
                if ((intent.getIntExtra(ShareConstant.EXTRA_WEIXIN_RESULT, -2) == 0 ? (char) 65535 : (char) 0) == 65535) {
                    if (ShareByWeixin.this.listener != null) {
                        ShareByWeixin.this.listener.onShare(ShareByWeixin.this.channel, 1);
                    }
                    ToastUtil.showToast(context, R.string.share_success, true);
                } else if (ShareByWeixin.this.listener != null) {
                    ShareByWeixin.this.listener.onShare(ShareByWeixin.this.channel, 2);
                }
            }
        }
    }

    public void shareImg(Bitmap bitmap, OnShareListener onShareListener) {
        if (bitmap == null) {
            if (onShareListener != null) {
                onShareListener.onShare(this.channel, 2);
                return;
            }
            return;
        }
        if (this.api.isWXAppInstalled()) {
            if (this.api.isWXAppSupportAPI()) {
                WXMediaMessage wXMediaMessage = new WXMediaMessage();
                wXMediaMessage.mediaObject = new WXImageObject(bitmap);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                while (width * height > 62500) {
                    width /= 2;
                    height /= 2;
                }
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
                    wXMediaMessage.thumbData = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                } catch (Exception unused) {
                }
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = wXMediaMessage;
                int i = this.channel;
                if (1 == i) {
                    req.scene = 0;
                } else if (2 == i) {
                    req.scene = 1;
                }
                this.api.sendReq(req);
                return;
            }
            return;
        }
        if (onShareListener != null) {
            onShareListener.onShare(this.channel, 2);
        }
        ToastUtil.showToast(this.context, R.string.share_no_weixin_client, true);
    }
}
