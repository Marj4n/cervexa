package com.tencent.mm.opensdk.diffdev.a;

import android.os.AsyncTask;
import com.baidu.mapapi.UIMsg;
import com.hoho.android.usbserial.driver.UsbId;
import com.tencent.mm.opensdk.diffdev.OAuthErrCode;
import com.tencent.mm.opensdk.diffdev.OAuthListener;
import com.tencent.mm.opensdk.utils.Log;
import org.json.JSONObject;

/* loaded from: classes2.dex */
final class f extends AsyncTask<Void, Void, a> {
    private OAuthListener l;
    private String o;
    private int u;
    private String url;

    static class a {
        public OAuthErrCode n;
        public String v;
        public int w;

        a() {
        }

        public static a b(byte[] bArr) {
            OAuthErrCode oAuthErrCode;
            String format;
            OAuthErrCode oAuthErrCode2;
            a aVar = new a();
            if (bArr != null && bArr.length != 0) {
                try {
                } catch (Exception e) {
                    format = String.format("parse fail, build String fail, ex = %s", e.getMessage());
                }
                try {
                    JSONObject jSONObject = new JSONObject(new String(bArr, "utf-8"));
                    int i = jSONObject.getInt("wx_errcode");
                    aVar.w = i;
                    Log.d("MicroMsg.SDK.NoopingResult", String.format("nooping uuidStatusCode = %d", Integer.valueOf(i)));
                    int i2 = aVar.w;
                    if (i2 == 408) {
                        oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_OK;
                    } else if (i2 != 500) {
                        switch (i2) {
                            case 402:
                                oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_Timeout;
                                break;
                            case 403:
                                oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_Cancel;
                                break;
                            case UIMsg.l_ErrorNo.NETWORK_ERROR_404 /* 404 */:
                                oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_OK;
                                break;
                            case 405:
                                aVar.n = OAuthErrCode.WechatAuth_Err_OK;
                                aVar.v = jSONObject.getString("wx_code");
                                return aVar;
                            default:
                                oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_NormalErr;
                                break;
                        }
                    } else {
                        oAuthErrCode2 = OAuthErrCode.WechatAuth_Err_NormalErr;
                    }
                    aVar.n = oAuthErrCode2;
                    return aVar;
                } catch (Exception e2) {
                    format = String.format("parse json fail, ex = %s", e2.getMessage());
                    Log.e("MicroMsg.SDK.NoopingResult", format);
                    oAuthErrCode = OAuthErrCode.WechatAuth_Err_NormalErr;
                    aVar.n = oAuthErrCode;
                    return aVar;
                }
            }
            Log.e("MicroMsg.SDK.NoopingResult", "parse fail, buf is null");
            oAuthErrCode = OAuthErrCode.WechatAuth_Err_NetworkErr;
            aVar.n = oAuthErrCode;
            return aVar;
        }
    }

    public f(String str, OAuthListener oAuthListener) {
        this.o = str;
        this.l = oAuthListener;
        this.url = String.format("https://long.open.weixin.qq.com/connect/l/qrconnect?f=json&uuid=%s", str);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ a doInBackground(Void[] voidArr) {
        a aVar;
        OAuthErrCode oAuthErrCode;
        String str;
        String str2 = this.o;
        if (str2 == null || str2.length() == 0) {
            Log.e("MicroMsg.SDK.NoopingTask", "run fail, uuid is null");
            aVar = new a();
            oAuthErrCode = OAuthErrCode.WechatAuth_Err_NormalErr;
        } else {
            while (!isCancelled()) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.url);
                if (this.u == 0) {
                    str = "";
                } else {
                    str = "&last=" + this.u;
                }
                sb.append(str);
                String sb2 = sb.toString();
                long currentTimeMillis = System.currentTimeMillis();
                byte[] a2 = e.a(sb2, UsbId.SILABS_CP2102);
                long currentTimeMillis2 = System.currentTimeMillis();
                a b = a.b(a2);
                Log.d("MicroMsg.SDK.NoopingTask", String.format("nooping, url = %s, errCode = %s, uuidStatusCode = %d, time consumed = %d(ms)", sb2, b.n.toString(), Integer.valueOf(b.w), Long.valueOf(currentTimeMillis2 - currentTimeMillis)));
                if (b.n != OAuthErrCode.WechatAuth_Err_OK) {
                    Log.e("MicroMsg.SDK.NoopingTask", String.format("nooping fail, errCode = %s, uuidStatusCode = %d", b.n.toString(), Integer.valueOf(b.w)));
                    return b;
                }
                this.u = b.w;
                if (b.w == g.UUID_SCANED.getCode()) {
                    this.l.onQrcodeScanned();
                } else if (b.w != g.UUID_KEEP_CONNECT.getCode() && b.w == g.UUID_CONFIRM.getCode()) {
                    if (b.v == null || b.v.length() == 0) {
                        Log.e("MicroMsg.SDK.NoopingTask", "nooping fail, confirm with an empty code!!!");
                        b.n = OAuthErrCode.WechatAuth_Err_NormalErr;
                    }
                    return b;
                }
            }
            Log.i("MicroMsg.SDK.NoopingTask", "IDiffDevOAuth.stopAuth / detach invoked");
            aVar = new a();
            oAuthErrCode = OAuthErrCode.WechatAuth_Err_Auth_Stopped;
        }
        aVar.n = oAuthErrCode;
        return aVar;
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(a aVar) {
        a aVar2 = aVar;
        this.l.onAuthFinish(aVar2.n, aVar2.v);
    }
}
