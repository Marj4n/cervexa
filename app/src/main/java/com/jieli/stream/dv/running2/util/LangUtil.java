package com.jieli.stream.dv.running2.util;

import java.util.Locale;

/* loaded from: classes.dex */
public class LangUtil {
    private static final String ARGS_LANG_AR_AR = "21";
    private static final String ARGS_LANG_CS_CZ = "12";
    private static final String ARGS_LANG_DA_DK = "13";
    private static final String ARGS_LANG_DE_DE = "7";
    private static final String ARGS_LANG_EN_US = "5";
    private static final String ARGS_LANG_ES_ES = "0";
    private static final String ARGS_LANG_FR_LU = "6";
    private static final String ARGS_LANG_HE_IL = "17";
    private static final String ARGS_LANG_HU_HU = "19";
    private static final String ARGS_LANG_IT_IT = "8";
    private static final String ARGS_LANG_JA_JP = "3";
    private static final String ARGS_LANG_KO_KR = "4";
    private static final String ARGS_LANG_NL_NL = "9";
    private static final String ARGS_LANG_PL_PL = "14";
    private static final String ARGS_LANG_PT_PT = "10";
    private static final String ARGS_LANG_RO_RO = "20";
    private static final String ARGS_LANG_RU_RU = "15";
    private static final String ARGS_LANG_SV_SE = "11";
    private static final String ARGS_LANG_TH_TH = "18";
    private static final String ARGS_LANG_TR_TR = "16";
    private static final String ARGS_LANG_ZH_CN = "1";
    private static final String ARGS_LANG_ZH_TW = "2";
    public static final Locale[] LOCALES = {Locale.SIMPLIFIED_CHINESE, Locale.CHINESE, Locale.JAPANESE, Locale.KOREAN, Locale.US, Locale.FRENCH};
    private static String tag = "LangUtil";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static Locale getLanguage(String str) {
        char c;
        int hashCode = str.hashCode();
        if (hashCode == 1598) {
            if (str.equals(ARGS_LANG_RO_RO)) {
                c = 20;
            }
            c = 65535;
        } else if (hashCode != 1599) {
            switch (hashCode) {
                case 48:
                    if (str.equals("0")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 49:
                    if (str.equals("1")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 50:
                    if (str.equals("2")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 51:
                    if (str.equals("3")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 52:
                    if (str.equals("4")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 53:
                    if (str.equals("5")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 54:
                    if (str.equals("6")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 55:
                    if (str.equals("7")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 56:
                    if (str.equals("8")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 57:
                    if (str.equals(ARGS_LANG_NL_NL)) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    switch (hashCode) {
                        case 1567:
                            if (str.equals("10")) {
                                c = '\n';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1568:
                            if (str.equals("11")) {
                                c = 11;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1569:
                            if (str.equals("12")) {
                                c = '\f';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1570:
                            if (str.equals("13")) {
                                c = '\r';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1571:
                            if (str.equals("14")) {
                                c = 14;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1572:
                            if (str.equals("15")) {
                                c = 15;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1573:
                            if (str.equals("16")) {
                                c = 16;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1574:
                            if (str.equals("17")) {
                                c = 17;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1575:
                            if (str.equals("18")) {
                                c = 18;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1576:
                            if (str.equals("19")) {
                                c = 19;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
            }
        } else {
            if (str.equals("21")) {
                c = 21;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
                return Locale.SIMPLIFIED_CHINESE;
            case 1:
                return Locale.TRADITIONAL_CHINESE;
            case 2:
                return Locale.US;
            case 3:
                return Locale.GERMANY;
            case 4:
                return Locale.JAPAN;
            case 5:
                return new Locale("es", "ES");
            case 6:
                return new Locale("ko", "KR");
            case 7:
                return new Locale("fr", "LU");
            case '\b':
                return new Locale("it", "IT");
            case '\t':
                return new Locale("nl", "KR");
            case '\n':
                return new Locale("pt", "PT");
            case 11:
                return new Locale("sv", "SE");
            case '\f':
                return new Locale("cs", "CZ");
            case '\r':
                return new Locale("da", "DK");
            case 14:
                return new Locale("pl", "PL");
            case 15:
                return new Locale("ru", "RU");
            case 16:
                return new Locale("tr", "TR");
            case 17:
                return new Locale("he", "IL");
            case 18:
                return new Locale("th", "TH");
            case 19:
                return new Locale("hu", "HU");
            case 20:
                return new Locale("ro", "RO");
            case 21:
                return new Locale("ar", "AR");
            default:
                Locale locale = Locale.SIMPLIFIED_CHINESE;
                Dbug.e(tag, "Unknown language code" + str);
                return locale;
        }
    }
}
