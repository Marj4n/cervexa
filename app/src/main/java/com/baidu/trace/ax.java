package com.baidu.trace;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class ax {
    private static int a;
    private static Map<Short, com.baidu.trace.c.g> b;

    protected static void a() {
        if (b == null) {
            b = new HashMap();
        }
    }

    public static boolean a(DataInputStream dataInputStream) throws Exception {
        com.baidu.trace.c.g gVar;
        byte[] bArr = {0};
        boolean z = true;
        while (true) {
            if (!z) {
                break;
            }
            if (bArr[0] == 0 && dataInputStream.read(bArr) <= 0) {
                int i = a + 1;
                a = i;
                if (i >= 10) {
                    a = 0;
                    return false;
                }
            } else if (bArr[0] != -1) {
                int i2 = a + 1;
                a = i2;
                if (i2 >= 10) {
                    a = 0;
                    return false;
                }
            } else {
                a = 0;
                byte[] bArr2 = new byte[4];
                if (dataInputStream.read(bArr2) < 4) {
                    break;
                }
                if ("YING".equals(new String(bArr2))) {
                    byte[] bArr3 = new byte[2];
                    if (dataInputStream.read(bArr3) >= 2) {
                        short a2 = com.baidu.trace.c.d.a(bArr3, 0);
                        if (a2 > 2) {
                            short s = (short) (a2 - 2);
                            int i3 = s + 1;
                            byte[] bArr4 = new byte[i3];
                            int read = dataInputStream.read(bArr4);
                            if (read < s) {
                                break;
                            }
                            if (read <= s || bArr4[s] != -1) {
                                z = false;
                            }
                            byte[] bArr5 = new byte[s + 2 + 4 + 1];
                            bArr5[0] = bArr[0];
                            if (z) {
                                bArr[0] = bArr4[s];
                            } else {
                                bArr[0] = 0;
                            }
                            int i4 = 0;
                            int i5 = 1;
                            while (i4 < 4) {
                                bArr5[i5] = bArr2[i4];
                                i4++;
                                i5++;
                            }
                            int i6 = 0;
                            while (i6 < 2) {
                                bArr5[i5] = bArr3[i6];
                                i6++;
                                i5++;
                            }
                            int i7 = 0;
                            while (i7 < i3 && i7 < s) {
                                bArr5[i5] = bArr4[i7];
                                i7++;
                                i5++;
                            }
                            byte[] parseResponseProtocolData = TraceJniInterface.a().parseResponseProtocolData(bArr5);
                            if (parseResponseProtocolData == null || parseResponseProtocolData.length == 0) {
                                break;
                            }
                            try {
                                JSONObject jSONObject = new JSONObject(new String(parseResponseProtocolData));
                                short s2 = (short) jSONObject.getInt("business_id");
                                if (b == null || !b.containsKey(Short.valueOf(s2))) {
                                    com.baidu.trace.c.g gVar2 = (com.baidu.trace.c.g) Class.forName(n.a.get(Short.valueOf(s2))).newInstance();
                                    b.put(Short.valueOf(s2), gVar2);
                                    gVar = gVar2;
                                } else {
                                    gVar = b.get(Short.valueOf(s2));
                                }
                                gVar.a(jSONObject);
                            } catch (Exception unused) {
                                return false;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    public static byte[] a(short s, int i) {
        com.baidu.trace.c.g gVar;
        Map<Short, com.baidu.trace.c.g> map = b;
        byte[] bArr = null;
        if (map == null || !map.containsKey(Short.valueOf(s))) {
            try {
                gVar = (com.baidu.trace.c.g) Class.forName(n.a.get(Short.valueOf(s))).newInstance();
            } catch (Exception unused) {
            }
        } else {
            gVar = b.get(Short.valueOf(s));
        }
        gVar.a();
        try {
            bArr = TraceJniInterface.a().buildRequestProtocolData(s, i);
        } catch (Exception unused2) {
        }
        if (bArr != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArr) {
                stringBuffer.append((int) b2);
            }
        }
        return bArr;
    }

    protected static void b() {
        Map<Short, com.baidu.trace.c.g> map = b;
        if (map != null) {
            map.clear();
            b = null;
        }
    }
}
