package com.serenegiant.utils;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class ObjectHelper {
    public static boolean asBoolean(Object obj, boolean z) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue() != 0;
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue() != 0;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue() != 0;
        }
        if (obj instanceof Long) {
            return ((Long) obj).longValue() != 0;
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue() != 0.0f;
        }
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue() != 0.0d;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0.0d;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return Integer.parseInt(str.substring(2), 16) != 0;
                    } catch (Exception unused) {
                        return Long.parseLong(str.substring(2), 16) != 0;
                    }
                }
                try {
                    try {
                        try {
                            return Double.parseDouble(str) != 1e-10;
                        } catch (Exception unused2) {
                            return Integer.parseInt(str, 16) != 0;
                        }
                    } catch (Exception unused3) {
                        return Long.parseLong(str, 16) != 0;
                    }
                } catch (Exception unused4) {
                    return Boolean.parseBoolean(str);
                }
            }
        }
        return z;
    }

    public static byte asByte(Object obj, byte b) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? (byte) 1 : (byte) 0;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).byteValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).byteValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).byteValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).byteValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).byteValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).byteValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return (byte) Integer.parseInt(str.substring(2), 16);
                    } catch (Exception unused) {
                        return (byte) Long.parseLong(str.substring(2), 16);
                    }
                }
                try {
                    try {
                        try {
                            return Double.valueOf(Double.parseDouble(str)).byteValue();
                        } catch (Exception unused2) {
                            return Boolean.parseBoolean(str) ? (byte) 1 : (byte) 0;
                        }
                    } catch (Exception unused3) {
                        return Integer.valueOf(Integer.parseInt(str, 16)).byteValue();
                    }
                } catch (Exception unused4) {
                    return Long.valueOf(Long.parseLong(str, 16)).byteValue();
                }
            }
        }
        return b;
    }

    public static short asShort(Object obj, short s) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? (short) 1 : (short) 0;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).shortValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).shortValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).shortValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).shortValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return (short) Integer.parseInt(str.substring(2), 16);
                    } catch (Exception unused) {
                        return (short) Long.parseLong(str.substring(2), 16);
                    }
                }
                try {
                    try {
                        try {
                            return Double.valueOf(Double.parseDouble(str)).shortValue();
                        } catch (Exception unused2) {
                            return Boolean.parseBoolean(str) ? (short) 1 : (short) 0;
                        }
                    } catch (Exception unused3) {
                        return Integer.valueOf(Integer.parseInt(str, 16)).shortValue();
                    }
                } catch (Exception unused4) {
                    return Long.valueOf(Long.parseLong(str, 16)).shortValue();
                }
            }
        }
        return s;
    }

    public static int asInt(Object obj, int i) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1 : 0;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).intValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).intValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).intValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return Integer.parseInt(str.substring(2), 16);
                    } catch (Exception unused) {
                        return Long.valueOf(Long.parseLong(str.substring(2), 16)).intValue();
                    }
                }
                try {
                    try {
                        try {
                            return Double.valueOf(Double.parseDouble(str)).intValue();
                        } catch (Exception unused2) {
                            return Long.valueOf(Long.parseLong(str, 16)).intValue();
                        }
                    } catch (Exception unused3) {
                        return Boolean.parseBoolean(str) ? 1 : 0;
                    }
                } catch (Exception unused4) {
                    return Integer.parseInt(str, 16);
                }
            }
        }
        return i;
    }

    public static long asLong(Object obj, long j) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1L : 0L;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).longValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).longValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return Long.parseLong(str.substring(2), 16);
                    } catch (Exception unused) {
                    }
                }
                try {
                    try {
                        return Double.valueOf(Double.parseDouble(str)).longValue();
                    } catch (Exception unused2) {
                        return Boolean.parseBoolean(str) ? 1L : 0L;
                    }
                } catch (Exception unused3) {
                    return Long.parseLong(str, 16);
                }
            }
        }
        return j;
    }

    public static float asFloat(Object obj, float f) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue() ? 1.0f : 0.0f;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).floatValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).floatValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).floatValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return Long.valueOf(Long.parseLong(str.substring(2), 16)).floatValue();
                    } catch (Exception unused) {
                    }
                }
                try {
                    try {
                        return Double.valueOf(Double.parseDouble(str)).floatValue();
                    } catch (Exception unused2) {
                        return Long.valueOf(Long.parseLong(str, 16)).floatValue();
                    }
                } catch (Exception unused3) {
                    return Boolean.parseBoolean(str) ? 1.0f : 0.0f;
                }
            }
        }
        return f;
    }

    public static double asDouble(Object obj, double d) {
        if (obj instanceof Boolean) {
            if (((Boolean) obj).booleanValue()) {
                return 1.0d;
            }
            return 1e-10;
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).doubleValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                if (str.startsWith("0x") || str.startsWith("0X")) {
                    try {
                        return Long.valueOf(Long.parseLong(str.substring(2), 16)).doubleValue();
                    } catch (Exception unused) {
                    }
                }
                try {
                    try {
                        return Double.parseDouble(str);
                    } catch (Exception unused2) {
                        return Long.valueOf(Long.parseLong(str, 16)).doubleValue();
                    }
                } catch (Exception unused3) {
                    if (Boolean.parseBoolean(str)) {
                        return 1.0d;
                    }
                    return 1e-10;
                }
            }
        }
        return d;
    }
}
