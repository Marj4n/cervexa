package com.serenegiant.utils;

import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OpenGLInfo {
    private static final int EGL_CLIENT_APIS = 12429;

    /* JADX WARN: Can't wrap try/catch for region: R(14:0|1|(3:2|3|4)|(5:(42:5|6|7|8|9|10|11|12|13|14|(2:15|16)|17|(2:18|19)|20|21|22|23|(2:24|25)|26|27|28|29|30|31|32|(2:33|34)|35|36|37|38|39|40|41|42|43|44|45|46|47|48|49|50)|(2:51|52)|61|62|63)|53|54|55|56|57|58|59|60|(22:64|65|66|67|68|69|70|71|72|73|74|75|76|77|78|79|80|81|82|83|84|85)|(1:(0))) */
    /* JADX WARN: Can't wrap try/catch for region: R(29:124|125|53|54|55|56|57|58|59|60|61|62|63|(3:64|65|66)|(2:67|68)|69|70|71|72|73|74|75|(3:76|77|78)|(2:79|80)|81|82|83|84|85) */
    /* JADX WARN: Can't wrap try/catch for region: R(32:124|125|53|54|55|56|57|58|59|60|61|62|63|64|65|66|67|68|69|70|71|72|73|74|75|(3:76|77|78)|(2:79|80)|81|82|83|84|85) */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x01f5, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01f6, code lost:
    
        r4 = "EGL_VERSION";
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01b3, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01b8, code lost:
    
        r5.put(r1, r0.getMessage());
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01b5, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01b6, code lost:
    
        r1 = "GL_EXTENSIONS";
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0227, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0228, code lost:
    
        r5.put("EGL_EXTENSIONS", r0.getMessage());
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x01f3, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01f8, code lost:
    
        r5.put(r4, r0.getMessage());
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.json.JSONObject get() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 602
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.utils.OpenGLInfo.get():org.json.JSONObject");
    }

    private static final JSONObject formatExtensions(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        String[] split = str.split(" ");
        Arrays.sort(split);
        for (int i = 0; i < split.length; i++) {
            jSONObject.put(Integer.toString(i), split[i]);
        }
        return jSONObject;
    }
}
