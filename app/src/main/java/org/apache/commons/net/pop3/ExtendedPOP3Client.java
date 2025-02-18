package org.apache.commons.net.pop3;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;
import org.apache.commons.net.util.Base64;

/* loaded from: classes2.dex */
public class ExtendedPOP3Client extends POP3SClient {
    public boolean auth(AUTH_METHOD auth_method, String str, String str2) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        if (sendCommand(13, auth_method.getAuthName()) != 2) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$org$apache$commons$net$pop3$ExtendedPOP3Client$AUTH_METHOD[auth_method.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("\u0000");
            sb.append(str);
            sb.append("\u0000");
            sb.append(str2);
            return sendCommand(new String(Base64.encodeBase64(sb.toString().getBytes(getCharset())), getCharset())) == 0;
        }
        if (i != 2) {
            return false;
        }
        byte[] decodeBase64 = Base64.decodeBase64(getReplyString().substring(2).trim());
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(new SecretKeySpec(str2.getBytes(getCharset()), "HmacMD5"));
        byte[] bytes = _convertToHexString(mac.doFinal(decodeBase64)).getBytes(getCharset());
        byte[] bytes2 = str.getBytes(getCharset());
        byte[] bArr = new byte[bytes2.length + 1 + bytes.length];
        System.arraycopy(bytes2, 0, bArr, 0, bytes2.length);
        bArr[bytes2.length] = 32;
        System.arraycopy(bytes, 0, bArr, bytes2.length + 1, bytes.length);
        return sendCommand(Base64.encodeBase64StringUnChunked(bArr)) == 0;
    }

    /* renamed from: org.apache.commons.net.pop3.ExtendedPOP3Client$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$commons$net$pop3$ExtendedPOP3Client$AUTH_METHOD;

        static {
            int[] iArr = new int[AUTH_METHOD.values().length];
            $SwitchMap$org$apache$commons$net$pop3$ExtendedPOP3Client$AUTH_METHOD = iArr;
            try {
                iArr[AUTH_METHOD.PLAIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$apache$commons$net$pop3$ExtendedPOP3Client$AUTH_METHOD[AUTH_METHOD.CRAM_MD5.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private String _convertToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            int i = b & UByte.MAX_VALUE;
            if (i <= 15) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

    public enum AUTH_METHOD {
        PLAIN("PLAIN"),
        CRAM_MD5("CRAM-MD5");

        private final String methodName;

        AUTH_METHOD(String str) {
            this.methodName = str;
        }

        public final String getAuthName() {
            return this.methodName;
        }
    }
}
