package org.apache.commons.net.imap;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import kotlin.UByte;
import org.apache.commons.net.imap.IMAP;
import org.apache.commons.net.util.Base64;

/* loaded from: classes2.dex */
public class AuthenticatingIMAPClient extends IMAPSClient {
    public AuthenticatingIMAPClient() {
        this(IMAPSClient.DEFAULT_PROTOCOL, false);
    }

    public AuthenticatingIMAPClient(boolean z) {
        this(IMAPSClient.DEFAULT_PROTOCOL, z);
    }

    public AuthenticatingIMAPClient(String str) {
        this(str, false);
    }

    public AuthenticatingIMAPClient(String str, boolean z) {
        this(str, z, null);
    }

    public AuthenticatingIMAPClient(String str, boolean z, SSLContext sSLContext) {
        super(str, z, sSLContext);
    }

    public AuthenticatingIMAPClient(boolean z, SSLContext sSLContext) {
        this(IMAPSClient.DEFAULT_PROTOCOL, z, sSLContext);
    }

    public AuthenticatingIMAPClient(SSLContext sSLContext) {
        this(false, sSLContext);
    }

    public boolean authenticate(AUTH_METHOD auth_method, String str, String str2) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        return auth(auth_method, str, str2);
    }

    public boolean auth(AUTH_METHOD auth_method, String str, String str2) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        if (!IMAPReply.isContinuation(sendCommand(IMAPCommand.AUTHENTICATE, auth_method.getAuthName()))) {
            return false;
        }
        int i = AnonymousClass1.$SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD[auth_method.ordinal()];
        if (i == 1) {
            int sendData = sendData(Base64.encodeBase64StringUnChunked(("\u0000" + str + "\u0000" + str2).getBytes(getCharset())));
            if (sendData == 0) {
                setState(IMAP.IMAPState.AUTH_STATE);
            }
            return sendData == 0;
        }
        if (i != 2) {
            if (i != 3) {
                if (i != 4) {
                    return false;
                }
                int sendData2 = sendData(str);
                if (sendData2 == 0) {
                    setState(IMAP.IMAPState.AUTH_STATE);
                }
                return sendData2 == 0;
            }
            if (sendData(Base64.encodeBase64StringUnChunked(str.getBytes(getCharset()))) != 3) {
                return false;
            }
            int sendData3 = sendData(Base64.encodeBase64StringUnChunked(str2.getBytes(getCharset())));
            if (sendData3 == 0) {
                setState(IMAP.IMAPState.AUTH_STATE);
            }
            return sendData3 == 0;
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
        int sendData4 = sendData(Base64.encodeBase64StringUnChunked(bArr));
        if (sendData4 == 0) {
            setState(IMAP.IMAPState.AUTH_STATE);
        }
        return sendData4 == 0;
    }

    /* renamed from: org.apache.commons.net.imap.AuthenticatingIMAPClient$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD;

        static {
            int[] iArr = new int[AUTH_METHOD.values().length];
            $SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD = iArr;
            try {
                iArr[AUTH_METHOD.PLAIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD[AUTH_METHOD.CRAM_MD5.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD[AUTH_METHOD.LOGIN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$apache$commons$net$imap$AuthenticatingIMAPClient$AUTH_METHOD[AUTH_METHOD.XOAUTH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
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
        CRAM_MD5("CRAM-MD5"),
        LOGIN("LOGIN"),
        XOAUTH("XOAUTH");

        private final String authName;

        AUTH_METHOD(String str) {
            this.authName = str;
        }

        public final String getAuthName() {
            return this.authName;
        }
    }
}
