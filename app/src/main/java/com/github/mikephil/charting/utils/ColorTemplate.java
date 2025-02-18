package com.github.mikephil.charting.utils;

import android.content.res.Resources;
import android.graphics.Color;
import androidx.core.view.ViewCompat;
import com.jieli.stream.dv.running2.util.IConstant;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: classes.dex */
public class ColorTemplate {
    public static final int COLOR_NONE = 1122867;
    public static final int COLOR_SKIP = 1122868;
    public static final int[] LIBERTY_COLORS = {Color.rgb(207, TelnetCommand.EL, TelnetCommand.AYT), Color.rgb(148, FTPReply.DIRECTORY_STATUS, FTPReply.DIRECTORY_STATUS), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)};
    public static final int[] JOYFUL_COLORS = {Color.rgb(217, 80, 138), Color.rgb(TelnetCommand.DONT, 149, 7), Color.rgb(TelnetCommand.DONT, TelnetCommand.EC, 120), Color.rgb(106, IConstant.OP_SHARE_FILES, 134), Color.rgb(53, 194, 209)};
    public static final int[] PASTEL_COLORS = {Color.rgb(64, 89, 128), Color.rgb(149, IConstant.OP_SELECT_ALL, 124), Color.rgb(217, 184, IConstant.OP_EXIT_EDIT_MODE), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)};
    public static final int[] COLORFUL_COLORS = {Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(TelnetCommand.AO, NNTPReply.DEBUG_OUTPUT, 0), Color.rgb(106, FTPReply.FILE_STATUS_OK, 31), Color.rgb(179, 100, 53)};
    public static final int[] VORDIPLOM_COLORS = {Color.rgb(192, 255, 140), Color.rgb(255, TelnetCommand.EC, 140), Color.rgb(255, 208, 140), Color.rgb(140, FTPReply.SECURITY_DATA_EXCHANGE_COMPLETE, 255), Color.rgb(255, 140, 157)};
    public static final int[] MATERIAL_COLORS = {rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")};

    public static int colorWithAlpha(int i, int i2) {
        return (i & ViewCompat.MEASURED_SIZE_MASK) | ((i2 & 255) << 24);
    }

    public static int rgb(String str) {
        int parseLong = (int) Long.parseLong(str.replace("#", ""), 16);
        return Color.rgb((parseLong >> 16) & 255, (parseLong >> 8) & 255, (parseLong >> 0) & 255);
    }

    public static int getHoloBlue() {
        return Color.rgb(51, 181, FTPReply.ENTERING_EPSV_MODE);
    }

    public static List<Integer> createColors(Resources resources, int[] iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(resources.getColor(i)));
        }
        return arrayList;
    }

    public static List<Integer> createColors(int[] iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }
}
