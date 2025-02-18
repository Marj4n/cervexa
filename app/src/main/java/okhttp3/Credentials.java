package okhttp3;

import java.nio.charset.Charset;
import okio.ByteString;
import org.apache.commons.net.ftp.FTP;

/* loaded from: classes2.dex */
public final class Credentials {
    private Credentials() {
    }

    public static String basic(String str, String str2) {
        return basic(str, str2, Charset.forName(FTP.DEFAULT_CONTROL_ENCODING));
    }

    public static String basic(String str, String str2, Charset charset) {
        return "Basic " + ByteString.of((str + ":" + str2).getBytes(charset)).base64();
    }
}
