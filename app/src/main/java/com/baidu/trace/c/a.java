package com.baidu.trace.c;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.baidu.trace.LBSTraceService;
import com.baidu.trace.aa;
import com.baidu.trace.api.analysis.OnAnalysisListener;
import com.baidu.trace.api.bos.OnBosListener;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.ProtocolType;
import com.baidu.trace.model.StatusCodes;
import com.bumptech.glide.load.Key;
import com.tencent.connect.common.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/* loaded from: classes.dex */
public final class a {
    private static final Pattern c = Pattern.compile("[0-9A-Fa-f]{4}");
    private static HandlerC0017a d = null;
    private static b e = null;
    private static WeakReference<Context> f = null;
    private static boolean g = false;
    public static ExecutorService a = null;
    public static ProtocolType b = ProtocolType.HTTPS;
    private static int h = 5;
    private static int i = Integer.MAX_VALUE;
    private static long j = 60;

    /* renamed from: com.baidu.trace.c.a$1, reason: invalid class name */
    public class AnonymousClass1 {
    }

    /* renamed from: com.baidu.trace.c.a$a, reason: collision with other inner class name */
    static class HandlerC0017a extends Handler {
        HandlerC0017a() {
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    static class b implements HostnameVerifier {
        private b() {
        }

        /* synthetic */ b(byte b) {
            this();
        }

        @Override // javax.net.ssl.HostnameVerifier
        public final boolean verify(String str, SSLSession sSLSession) {
            return "api.map.baidu.com".equals(str);
        }
    }

    /* JADX WARN: $VALUES field not found */
    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    public static final class c {
        public static final c a = new c("GET", 0);
        public static final c b = new c(Constants.HTTP_POST, 1);

        private c(String str, int i) {
        }
    }

    static class d implements Runnable {
        protected int a;
        protected int b;
        protected String c;
        protected c d;
        protected String e;
        protected BaseRequest f;
        protected Object g;

        d() {
        }

        private String a(ProtocolType protocolType, String str) throws Exception {
            URL a = a(str);
            if (a == null) {
                return "";
            }
            HttpURLConnection httpURLConnection = ProtocolType.HTTPS == protocolType ? (HttpsURLConnection) a.openConnection() : (HttpURLConnection) a.openConnection();
            return a(httpURLConnection) ? b(httpURLConnection) : "";
        }

        private URL a(String str) {
            URL url;
            try {
                if (c.a == this.d) {
                    url = new URL(str + "?" + this.e);
                } else {
                    if (c.b != this.d) {
                        return null;
                    }
                    url = new URL(str);
                }
                return url;
            } catch (MalformedURLException unused) {
                return null;
            }
        }

        static /* synthetic */ void a(int i, BaseRequest baseRequest, int i2, String str, int i3, Object obj) {
            if (i == 0) {
                com.baidu.trace.a.a(baseRequest, i3, false, i2, str, (OnFenceListener) obj);
                return;
            }
            if (i == 1) {
                aa.a(baseRequest, i3, false, i2, str, (OnEntityListener) obj);
                return;
            }
            if (i == 2) {
                com.baidu.trace.a.a(baseRequest, i3, false, i2, str, (OnTrackListener) obj);
            } else if (i == 3) {
                com.baidu.trace.h.a(baseRequest, i3, false, i2, str, (OnBosListener) obj, a.d);
            } else {
                if (i != 4) {
                    return;
                }
                com.baidu.trace.a.a(baseRequest, i3, false, i2, str, (OnAnalysisListener) obj);
            }
        }

        static /* synthetic */ void a(int i, BaseRequest baseRequest, String str, int i2, Object obj) {
            if (i == 0) {
                try {
                    com.baidu.trace.a.a(baseRequest, i2, true, 0, URLDecoder.decode(str, Key.STRING_CHARSET_NAME), (OnFenceListener) obj);
                } catch (Exception unused) {
                    com.baidu.trace.a.a(baseRequest, i2, false, StatusCodes.PARSE_RESPONSE_FAILED, StatusCodes.MSG_PARSE_RESPONSE_FAILED, (OnFenceListener) obj);
                }
            } else {
                if (i == 1) {
                    aa.a(baseRequest, i2, true, 0, a.a(str), (OnEntityListener) obj);
                    return;
                }
                if (i == 2) {
                    com.baidu.trace.a.a(baseRequest, i2, true, 0, a.a(str), (OnTrackListener) obj);
                } else if (i == 3) {
                    com.baidu.trace.h.a(baseRequest, i2, true, 0, a.a(str), (OnBosListener) obj, null);
                } else {
                    if (i != 4) {
                        return;
                    }
                    com.baidu.trace.a.a(baseRequest, i2, true, 0, a.a(str), (OnAnalysisListener) obj);
                }
            }
        }

        private boolean a(HttpURLConnection httpURLConnection) {
            try {
                httpURLConnection.setRequestMethod(this.d.name());
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(StatusCodes.NOT_EXIST_FENCE);
                httpURLConnection.setUseCaches(false);
                return true;
            } catch (ProtocolException unused) {
                return false;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x00a6  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x009d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private java.lang.String b(java.net.HttpURLConnection r10) {
            /*
                Method dump skipped, instructions count: 226
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.c.a.d.b(java.net.HttpURLConnection):java.lang.String");
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x008c  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0090  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void run() {
            /*
                r10 = this;
                com.baidu.trace.model.BaseRequest r0 = r10.f
                boolean r0 = r0.isCanceled()
                java.lang.String r1 = "request has been canceled!"
                if (r0 == 0) goto Le
                com.baidu.trace.a.a(r1)
                return
            Le:
                java.lang.ref.WeakReference r0 = com.baidu.trace.c.a.b()
                if (r0 == 0) goto L3b
                java.lang.ref.WeakReference r0 = com.baidu.trace.c.a.b()
                java.lang.Object r0 = r0.get()
                if (r0 == 0) goto L3b
                java.lang.ref.WeakReference r0 = com.baidu.trace.c.a.b()
                java.lang.Object r0 = r0.get()
                android.content.Context r0 = (android.content.Context) r0
                boolean r0 = com.baidu.trace.b.d.a(r0)
                if (r0 != 0) goto L3b
                com.baidu.trace.c.a$a r0 = com.baidu.trace.c.a.c()
                com.baidu.trace.c.b r1 = new com.baidu.trace.c.b
                r1.<init>(r10)
                r0.post(r1)
                return
            L3b:
                java.lang.StringBuffer r0 = new java.lang.StringBuffer
                r0.<init>()
                r2 = 3
                com.baidu.trace.model.ProtocolType r3 = com.baidu.trace.model.ProtocolType.HTTPS     // Catch: java.lang.Exception -> L7f
                com.baidu.trace.model.ProtocolType r4 = com.baidu.trace.c.a.b     // Catch: java.lang.Exception -> L7f
                if (r3 == r4) goto L6c
                int r3 = r10.a     // Catch: java.lang.Exception -> L7f
                if (r2 != r3) goto L4c
                goto L6c
            L4c:
                com.baidu.trace.model.ProtocolType r3 = com.baidu.trace.model.ProtocolType.HTTP     // Catch: java.lang.Exception -> L7f
                com.baidu.trace.model.ProtocolType r4 = com.baidu.trace.c.a.b     // Catch: java.lang.Exception -> L7f
                if (r3 != r4) goto L84
                com.baidu.trace.model.ProtocolType r3 = com.baidu.trace.model.ProtocolType.HTTP     // Catch: java.lang.Exception -> L7f
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L7f
                java.lang.String r5 = "http://api.map.baidu.com/sdkproxy/v2/lbs_tracesdk/trace/v3/"
                r4.<init>(r5)     // Catch: java.lang.Exception -> L7f
                java.lang.String r5 = r10.c     // Catch: java.lang.Exception -> L7f
                r4.append(r5)     // Catch: java.lang.Exception -> L7f
                java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L7f
            L64:
                java.lang.String r3 = r10.a(r3, r4)     // Catch: java.lang.Exception -> L7f
                r0.append(r3)     // Catch: java.lang.Exception -> L7f
                goto L84
            L6c:
                com.baidu.trace.model.ProtocolType r3 = com.baidu.trace.model.ProtocolType.HTTPS     // Catch: java.lang.Exception -> L7f
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L7f
                java.lang.String r5 = "https://api.map.baidu.com/sdkproxy/v2/lbs_tracesdk/trace/v3/"
                r4.<init>(r5)     // Catch: java.lang.Exception -> L7f
                java.lang.String r5 = r10.c     // Catch: java.lang.Exception -> L7f
                r4.append(r5)     // Catch: java.lang.Exception -> L7f
                java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L7f
                goto L64
            L7f:
                java.lang.String r3 = "http request failed"
                com.baidu.trace.a.a(r3)
            L84:
                com.baidu.trace.model.BaseRequest r3 = r10.f
                boolean r3 = r3.isCanceled()
                if (r3 == 0) goto L90
                com.baidu.trace.a.a(r1)
                return
            L90:
                boolean r1 = android.text.TextUtils.isEmpty(r0)
                if (r1 != 0) goto Lb9
                int r1 = r10.a
                if (r2 != r1) goto Lb9
                int r1 = r10.b
                if (r2 == r1) goto Lb9
                com.baidu.trace.model.BaseRequest r3 = r10.f
                java.lang.String r0 = r0.toString()
                int r4 = r10.b
                java.lang.Object r1 = r10.g
                r8 = r1
                com.baidu.trace.api.bos.OnBosListener r8 = (com.baidu.trace.api.bos.OnBosListener) r8
                com.baidu.trace.c.a$a r9 = com.baidu.trace.c.a.c()
                r5 = 1
                r6 = 0
                java.lang.String r7 = com.baidu.trace.c.a.a(r0)
                com.baidu.trace.h.a(r3, r4, r5, r6, r7, r8, r9)
                return
            Lb9:
                com.baidu.trace.c.a$a r1 = com.baidu.trace.c.a.c()
                com.baidu.trace.c.c r2 = new com.baidu.trace.c.c
                r2.<init>(r10, r0)
                r1.post(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.trace.c.a.d.run():void");
        }

        public final String toString() {
            return "TaskRunnable [tag=" + this.f.getTag() + ", serviceTag=" + this.a + ", operateType=" + this.b + ", action=" + this.c + ", requestType=" + this.d + ", parameters=" + this.e + "]";
        }
    }

    static /* synthetic */ String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            char charAt = str.charAt(i2);
            if (charAt != '\\' || i2 >= length - 1) {
                sb.append(charAt);
            } else {
                i2++;
                char charAt2 = str.charAt(i2);
                if (charAt2 == 'u' && i2 <= length - 5) {
                    String substring = str.substring(i2 + 1, i2 + 5);
                    if (c.matcher(substring).find()) {
                        sb.append((char) Integer.parseInt(substring, 16));
                        i2 += 4;
                    }
                }
                sb.append(charAt);
                sb.append(charAt2);
            }
            i2++;
        }
        return sb.toString();
    }

    public static synchronized void a() {
        synchronized (a.class) {
            if (f != null) {
                f.clear();
                f = null;
            }
            if (!g || a == null) {
                return;
            }
            try {
                if (!a.isShutdown()) {
                    a.shutdownNow();
                }
            } catch (Exception unused) {
            }
            d = null;
            e = null;
            a = null;
            g = false;
        }
    }

    public static synchronized void a(Context context, Class<?> cls) {
        synchronized (a.class) {
            if (f == null) {
                f = new WeakReference<>(context);
            }
            if (!g || a == null) {
                d = new HandlerC0017a();
                e = new b((byte) 0);
                a = LBSTraceService.class.getSimpleName().equals(cls.getSimpleName()) ? Executors.newSingleThreadExecutor() : new ThreadPoolExecutor(h, i, j, TimeUnit.SECONDS, new LinkedBlockingQueue());
                g = true;
            }
        }
    }

    public static void a(BaseRequest baseRequest, int i2, String str, c cVar, int i3, String str2, Object obj) {
        d dVar = new d();
        dVar.a = i2;
        dVar.b = i3;
        dVar.c = str;
        dVar.d = cVar;
        dVar.e = str2;
        dVar.f = baseRequest;
        dVar.g = obj;
        ExecutorService executorService = a;
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        a.execute(dVar);
    }

    public static synchronized boolean a(int i2, int i3, long j2) {
        boolean z;
        synchronized (a.class) {
            if (i2 < 0 || i3 <= 0 || i3 < i2 || j2 < 0) {
                z = false;
            } else {
                if (a instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) a;
                    threadPoolExecutor.setCorePoolSize(i2);
                    threadPoolExecutor.setMaximumPoolSize(i3);
                    threadPoolExecutor.setKeepAliveTime(j2, TimeUnit.SECONDS);
                } else {
                    h = i2;
                    i = i3;
                    j = j2;
                }
                z = true;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            try {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    stringBuffer.append(readLine);
                } catch (IOException unused) {
                }
            } catch (Exception unused2) {
                bufferedReader.close();
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (IOException unused3) {
                }
                throw th;
            }
        }
        bufferedReader.close();
        return stringBuffer.toString();
    }
}
