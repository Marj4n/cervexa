package com.jieli.stream.dv.running2.util;

import com.jieli.lib.dv.control.utils.ClientContext;
import com.jieli.media.codec.bean.MediaMeta;
import com.jieli.stream.dv.running2.interfaces.OnAviThumbListener;
import kotlin.UByte;

/* loaded from: classes.dex */
public class AviThumbUtil {
    private static final String TAG = "AviThumbUtil";
    private static OnAviThumbListener mOnAviThumbListener;

    private static void dispenseOnCompletedEvent(final byte[] bArr, final MediaMeta mediaMeta) {
        ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.AviThumbUtil.1
            @Override // java.lang.Runnable
            public void run() {
                if (AviThumbUtil.mOnAviThumbListener != null) {
                    AviThumbUtil.mOnAviThumbListener.onCompleted(bArr, mediaMeta);
                }
            }
        });
    }

    private static void dispenseOnErrorEvent(final String str) {
        ClientContext.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.AviThumbUtil.2
            @Override // java.lang.Runnable
            public void run() {
                if (AviThumbUtil.mOnAviThumbListener != null) {
                    AviThumbUtil.mOnAviThumbListener.onError(str);
                }
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:75:0x01c6, code lost:
    
        dispenseOnErrorEvent("thumbnail is null.");
     */
    /* JADX WARN: Removed duplicated region for block: B:81:0x026f A[Catch: IOException -> 0x0273, TRY_ENTER, TRY_LEAVE, TryCatch #3 {IOException -> 0x0273, blocks: (B:65:0x024e, B:81:0x026f), top: B:9:0x003f }] */
    /* JADX WARN: Removed duplicated region for block: B:82:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x027b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:94:? A[SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:107:0x0274 -> B:65:0x0285). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void getRecordVideoThumb(java.lang.String r34, com.jieli.stream.dv.running2.interfaces.OnAviThumbListener r35) {
        /*
            Method dump skipped, instructions count: 646
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jieli.stream.dv.running2.util.AviThumbUtil.getRecordVideoThumb(java.lang.String, com.jieli.stream.dv.running2.interfaces.OnAviThumbListener):void");
    }

    public static long getLong(byte[] bArr, boolean z) {
        if (bArr == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (bArr.length > 8) {
            throw new IllegalArgumentException("byte array size > 8 !");
        }
        long j = 0;
        if (z) {
            for (int length = bArr.length - 1; length >= 0; length--) {
                j = (j << 8) | (bArr[length] & UByte.MAX_VALUE);
            }
        } else {
            for (byte b : bArr) {
                j = (j << 8) | (b & UByte.MAX_VALUE);
            }
        }
        return j;
    }
}
