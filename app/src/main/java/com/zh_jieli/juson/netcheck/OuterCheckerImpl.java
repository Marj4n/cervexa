package com.zh_jieli.juson.netcheck;

import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class OuterCheckerImpl {
    private boolean result = false;
    List<HashMap<String, Object>> mList = new ArrayList();

    private interface ResultCallback {
        void onResult(boolean z);
    }

    public OuterCheckerImpl() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap.put("host", "125.39.240.113");
        hashMap.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        this.mList.add(hashMap);
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap2.put("host", "125.39.240.113");
        hashMap2.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 443);
        this.mList.add(hashMap2);
        HashMap<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap3.put("host", "61.135.157.156");
        hashMap3.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 80);
        this.mList.add(hashMap3);
        HashMap<String, Object> hashMap4 = new HashMap<>();
        hashMap4.put(BaseFragment.DATA_NAME, "qq.com");
        hashMap4.put("host", "61.135.157.156");
        hashMap4.put(IjkMediaPlayer.OnNativeInvokeListener.ARG_PORT, 443);
        this.mList.add(hashMap4);
    }

    public static boolean check(List<HashMap> list, long j) {
        try {
            return new OuterCheckerImpl().realCheck(list, j);
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean check(long j) {
        try {
            OuterCheckerImpl outerCheckerImpl = new OuterCheckerImpl();
            return outerCheckerImpl.realCheck(outerCheckerImpl.mList, j);
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean realCheck(List list, long j) throws Exception {
        this.result = false;
        Thread thread = new Thread(new InternetCheckRunner(list, j, new ResultCallback() { // from class: com.zh_jieli.juson.netcheck.OuterCheckerImpl.1
            @Override // com.zh_jieli.juson.netcheck.OuterCheckerImpl.ResultCallback
            public void onResult(boolean z) {
                OuterCheckerImpl.this.result = z;
            }
        }));
        thread.start();
        thread.join();
        return this.result;
    }

    private class InternetCheckRunner implements Runnable {
        private List<HashMap<String, Object>> mList;
        ResultCallback mResultCallback;
        private long mTimeout;

        public InternetCheckRunner(List<HashMap<String, Object>> list, long j, ResultCallback resultCallback) {
            this.mList = list;
            this.mTimeout = j;
            this.mResultCallback = resultCallback;
        }

        /* JADX WARN: Removed duplicated region for block: B:79:0x0144 A[Catch: IOException -> 0x0140, TryCatch #5 {IOException -> 0x0140, blocks: (B:96:0x013c, B:79:0x0144, B:81:0x0147, B:83:0x014b, B:85:0x014e, B:88:0x0151), top: B:95:0x013c }] */
        /* JADX WARN: Removed duplicated region for block: B:95:0x013c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 347
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zh_jieli.juson.netcheck.OuterCheckerImpl.InternetCheckRunner.run():void");
        }
    }
}
