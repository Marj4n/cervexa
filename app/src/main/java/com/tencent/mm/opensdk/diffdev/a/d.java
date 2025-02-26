package com.tencent.mm.opensdk.diffdev.a;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import com.tencent.mm.opensdk.diffdev.OAuthErrCode;
import com.tencent.mm.opensdk.diffdev.OAuthListener;
import com.tencent.mm.opensdk.utils.Log;
import org.json.JSONObject;

import java.io.IOException;

/* loaded from: classes2.dex */
public final class d extends AsyncTask<Void, Void, d.a> {
    private static final String h = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg/oauth_qrcode.png";
    private static String i;
    private String appId;
    private String j;
    private String k;
    private OAuthListener l;
    private f m;
    private String scope;
    private String signature;

    static class a {
        public OAuthErrCode n;
        public String o;
        public String p;
        public String q;
        public int r;
        public String s;
        public byte[] t;

        private a() {
        }

        public static a a(byte[] bArr) {
            OAuthErrCode oAuthErrCode = null;
            String format;
            a aVar = new a();
            if (bArr == null || bArr.length == 0) {
                Log.e("MicroMsg.SDK.GetQRCodeResult", "parse fail, buf is null");
                oAuthErrCode = OAuthErrCode.WechatAuth_Err_NetworkErr;
            } else {
                try {
                    try {
                        JSONObject jSONObject = new JSONObject(new String(bArr, "utf-8"));
                        int i = jSONObject.getInt("errcode");
                        if (i != 0) {
                            Log.e("MicroMsg.SDK.GetQRCodeResult", String.format("resp errcode = %d", Integer.valueOf(i)));
                            aVar.n = OAuthErrCode.WechatAuth_Err_NormalErr;
                            aVar.r = i;
                            aVar.s = jSONObject.optString("errmsg");
                            return aVar;
                        }
                        String string = jSONObject.getJSONObject("qrcode").getString("qrcodebase64");
                        if (string != null && string.length() != 0) {
                            byte[] decode = Base64.decode(string, 0);
                            if (decode != null && decode.length != 0) {
                                aVar.n = OAuthErrCode.WechatAuth_Err_OK;
                                aVar.t = decode;
                                aVar.o = jSONObject.getString("uuid");
                                String string2 = jSONObject.getString("appname");
                                aVar.p = string2;
                                Log.d("MicroMsg.SDK.GetQRCodeResult", String.format("parse succ, save in memory, uuid = %s, appname = %s, imgBufLength = %d", aVar.o, string2, Integer.valueOf(aVar.t.length)));
                                return aVar;
                            }
                            Log.e("MicroMsg.SDK.GetQRCodeResult", "parse fail, qrcodeBuf is null");
                            aVar.n = OAuthErrCode.WechatAuth_Err_JsonDecodeErr;
                            return aVar;
                        }
                        Log.e("MicroMsg.SDK.GetQRCodeResult", "parse fail, qrcodeBase64 is null");
                        aVar.n = OAuthErrCode.WechatAuth_Err_JsonDecodeErr;
                        return aVar;
                    } catch (Exception e) {
                        format = String.format("parse json fail, ex = %s", e.getMessage());
                        Log.e("MicroMsg.SDK.GetQRCodeResult", format);
                        oAuthErrCode = OAuthErrCode.WechatAuth_Err_NormalErr;
                        aVar.n = oAuthErrCode;
                        return aVar;
                    }
                } catch (Exception e2) {
                    format = String.format("parse fail, build String fail, ex = %s", e2.getMessage());
                }
            }
            aVar.n = oAuthErrCode;
            return aVar;
        }
    }

    static {
        i = null;
        i = "https://open.weixin.qq.com/connect/sdk/qrconnect?appid=%s&noncestr=%s&timestamp=%s&scope=%s&signature=%s";
    }

    public d(String str, String str2, String str3, String str4, String str5, OAuthListener oAuthListener) {
        this.appId = str;
        this.scope = str2;
        this.j = str3;
        this.k = str4;
        this.signature = str5;
        this.l = oAuthListener;
    }

    public final boolean a() {
        Log.i("MicroMsg.SDK.GetQRCodeTask", "cancelTask");
        f fVar = this.m;
        return fVar == null ? cancel(true) : fVar.cancel(true);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ a doInBackground(Void... voidArr) {
        Log.i("MicroMsg.SDK.GetQRCodeTask", "external storage available = false");
        String format = String.format(i, this.appId, this.j, this.k, this.scope, this.signature);
        long currentTimeMillis = System.currentTimeMillis();
        byte[] a2 = null;
        try {
            a2 = e.a(format, -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("MicroMsg.SDK.GetQRCodeTask", String.format("doInBackground, url = %s, time consumed = %d(ms)", format, Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
        return a.a(a2);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(a aVar) {
        a aVar2 = aVar;
        if (aVar2.n != OAuthErrCode.WechatAuth_Err_OK) {
            Log.e("MicroMsg.SDK.GetQRCodeTask", String.format("onPostExecute, get qrcode fail, OAuthErrCode = %s", aVar2.n));
            this.l.onAuthFinish(aVar2.n, null);
            return;
        }
        Log.d("MicroMsg.SDK.GetQRCodeTask", "onPostExecute, get qrcode success");
        this.l.onAuthGotQrcode(aVar2.q, aVar2.t);
        f fVar = new f(aVar2.o, this.l);
        this.m = fVar;
        if (Build.VERSION.SDK_INT >= 11) {
            fVar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            fVar.execute(new Void[0]);
        }
    }
}
