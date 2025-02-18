package com.jieli.stream.dv.running2.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.baidu.mapapi.UIMsg;
import com.jieli.lib.dv.control.mssdp.Discovery;
import com.jieli.stream.dv.running2.interfaces.OnWifiCallBack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class WifiHelper implements IConstant {
    private static WifiHelper instance = null;
    private static String otherWifiSSID = null;
    private static String tag = "WifiHelper";
    private WifiManager mWifiManager;
    private Set<OnWifiCallBack> wifiCallBackSet;
    private WifiManager.WifiLock wifiLock;

    public enum NetState {
        NET_NO,
        NET_2G,
        NET_3G,
        NET_4G,
        NET_WIFI,
        NET_UNKNOWN
    }

    public enum WifiCipherType {
        NONE,
        IEEE8021XEAP,
        WEP,
        WPA,
        WPA2,
        WPAWPA2
    }

    public static WifiHelper getInstance(Context context) {
        synchronized (WifiHelper.class) {
            if (instance == null) {
                instance = new WifiHelper(context);
            }
        }
        return instance;
    }

    private WifiHelper(Context context) {
        if (context != null) {
            this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            this.wifiCallBackSet = new HashSet();
            startScan();
        }
    }

    public void registerOnWifiCallback(OnWifiCallBack onWifiCallBack) {
        Set<OnWifiCallBack> set = this.wifiCallBackSet;
        if (set == null || onWifiCallBack == null) {
            return;
        }
        set.add(onWifiCallBack);
    }

    public void unregisterOnWifiCallback(OnWifiCallBack onWifiCallBack) {
        Set<OnWifiCallBack> set = this.wifiCallBackSet;
        if (set == null || onWifiCallBack == null) {
            return;
        }
        set.remove(onWifiCallBack);
    }

    public void clearAllOnWifiCallback() {
        Set<OnWifiCallBack> set = this.wifiCallBackSet;
        if (set != null) {
            set.clear();
        }
    }

    public void notifyWifiConnect(WifiInfo wifiInfo) {
        Set<OnWifiCallBack> set = this.wifiCallBackSet;
        if (set == null || wifiInfo == null) {
            return;
        }
        Iterator<OnWifiCallBack> it = set.iterator();
        while (it.hasNext()) {
            it.next().onConnected(wifiInfo);
        }
    }

    public void notifyWifiError(int i) {
        Set<OnWifiCallBack> set = this.wifiCallBackSet;
        if (set != null) {
            Iterator<OnWifiCallBack> it = set.iterator();
            while (it.hasNext()) {
                it.next().onError(i);
            }
        }
    }

    public boolean isWifiOpen() {
        WifiManager wifiManager = this.mWifiManager;
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

    public static boolean isNetWorkConnectedType(Context context, int i) {
        ConnectivityManager connectivityManager;
        NetworkInfo[] allNetworkInfo;
        if (context != null && (connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")) != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    return networkInfo.getType() == i;
                }
            }
        }
        return false;
    }

    public boolean isOutSideWifi(String str) {
        WifiInfo wifiConnectionInfo = getWifiConnectionInfo();
        if (wifiConnectionInfo == null) {
            return false;
        }
        String formatSSID = formatSSID(wifiConnectionInfo.getSSID());
        return !TextUtils.isEmpty(formatSSID) && formatSSID.startsWith(str);
    }

    public int getWifiState(Context context) {
        NetworkInfo networkInfo;
        if (context == null || (networkInfo = getNetworkInfo(context)) == null) {
            return -1;
        }
        if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR || networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTING) {
            return 0;
        }
        return networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED ? 1 : 2;
    }

    public static String interceptChar0Before(String str) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charArray) {
            if (Character.valueOf(c).hashCode() == 0) {
                break;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public boolean isWifiClosed() {
        int wifiState = getWifiState();
        return wifiState == 1 || wifiState == 0;
    }

    public boolean isWifiOpened() {
        int wifiState = getWifiState();
        return wifiState == 3 || wifiState == 2;
    }

    public void openWifi() {
        if (this.mWifiManager == null || !isWifiClosed()) {
            return;
        }
        this.mWifiManager.setWifiEnabled(true);
    }

    public void closeWifi() {
        if (this.mWifiManager == null || !isWifiOpened()) {
            return;
        }
        this.mWifiManager.setWifiEnabled(false);
    }

    public int getWifiState() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            return wifiManager.getWifiState();
        }
        return 0;
    }

    public List<WifiConfiguration> getSavedWifiConfiguration() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            return wifiManager.getConfiguredNetworks();
        }
        return null;
    }

    public List<ScanResult> getWifiScanResult() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            return wifiManager.getScanResults();
        }
        return null;
    }

    public synchronized void startScan() {
        if (this.mWifiManager != null) {
            this.mWifiManager.startScan();
        }
    }

    public void connectionConfiguration(int i) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.disconnect();
            this.mWifiManager.enableNetwork(i, true);
        }
    }

    public void disconnectionConfiguration(int i) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.disableNetwork(i);
            this.mWifiManager.disconnect();
        }
    }

    private boolean configurationNetWorkIdCheck(int i) {
        List<WifiConfiguration> savedWifiConfiguration = getSavedWifiConfiguration();
        if (savedWifiConfiguration == null) {
            return false;
        }
        for (WifiConfiguration wifiConfiguration : savedWifiConfiguration) {
            if (wifiConfiguration != null && wifiConfiguration.networkId == i) {
                return true;
            }
        }
        return false;
    }

    public WifiInfo getWifiConnectionInfo() {
        return this.mWifiManager.getConnectionInfo();
    }

    public void createWifiLock(String str) {
        if (this.mWifiManager != null) {
            if (TextUtils.isEmpty(str)) {
                str = "wifiLock";
            }
            this.wifiLock = this.mWifiManager.createWifiLock(str);
        }
    }

    public void acquireWifiLock() {
        WifiManager.WifiLock wifiLock = this.wifiLock;
        if (wifiLock != null) {
            wifiLock.acquire();
            return;
        }
        createWifiLock("wifiLock");
        WifiManager.WifiLock wifiLock2 = this.wifiLock;
        if (wifiLock2 != null) {
            wifiLock2.acquire();
        }
    }

    public void releaseWifiLock() {
        WifiManager.WifiLock wifiLock = this.wifiLock;
        if (wifiLock == null || !wifiLock.isHeld()) {
            return;
        }
        this.wifiLock.acquire();
    }

    public int addNetWork(WifiConfiguration wifiConfiguration) {
        WifiManager wifiManager;
        return (wifiConfiguration == null || (wifiManager = this.mWifiManager) == null) ? UIMsg.m_AppUI.V_WM_ADDLISTUPDATE : wifiManager.addNetwork(wifiConfiguration);
    }

    public void addNetWorkAndConnect(WifiConfiguration wifiConfiguration) {
        int addNetWork = addNetWork(wifiConfiguration);
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null || addNetWork == -255) {
            return;
        }
        wifiManager.disconnect();
        this.mWifiManager.enableNetwork(addNetWork, true);
    }

    public int getConnectedWifiLevel() {
        WifiInfo wifiConnectionInfo = getWifiConnectionInfo();
        if (wifiConnectionInfo == null) {
            return 1;
        }
        String formatSSID = formatSSID(wifiConnectionInfo.getSSID());
        List<ScanResult> wifiScanResult = getWifiScanResult();
        if (wifiScanResult == null) {
            return 1;
        }
        for (ScanResult scanResult : wifiScanResult) {
            if (scanResult != null) {
                String formatSSID2 = formatSSID(scanResult.SSID);
                if (!TextUtils.isEmpty(formatSSID2) && formatSSID2.equals(formatSSID)) {
                    return scanResult.level;
                }
            }
        }
        return 1;
    }

    public boolean removeSavedNetWork(String str) {
        List<WifiConfiguration> savedWifiConfiguration;
        if (TextUtils.isEmpty(str) || this.mWifiManager == null || (savedWifiConfiguration = getSavedWifiConfiguration()) == null) {
            return false;
        }
        for (WifiConfiguration wifiConfiguration : savedWifiConfiguration) {
            if (wifiConfiguration != null) {
                String formatSSID = formatSSID(wifiConfiguration.SSID);
                str = formatSSID(str);
                if (!TextUtils.isEmpty(str) && str.equals(formatSSID)) {
                    return this.mWifiManager.removeNetwork(wifiConfiguration.networkId);
                }
            }
        }
        return false;
    }

    public void removeNetWork(int i) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            wifiManager.removeNetwork(i);
        }
    }

    public void addNetWorkAndConnect(String str, String str2, WifiCipherType wifiCipherType) {
        if (this.mWifiManager == null || wifiCipherType == null) {
            return;
        }
        WifiConfiguration isWifiConfigurationSaved = isWifiConfigurationSaved(str, wifiCipherType);
        if (isWifiConfigurationSaved != null) {
            connectionConfiguration(isWifiConfigurationSaved.networkId);
            return;
        }
        addNetWork(createWifiConfiguration(str, str2, wifiCipherType));
        List<WifiConfiguration> savedWifiConfiguration = getSavedWifiConfiguration();
        if (savedWifiConfiguration != null) {
            for (WifiConfiguration wifiConfiguration : savedWifiConfiguration) {
                if (wifiConfiguration != null) {
                    String formatSSID = formatSSID(wifiConfiguration.SSID);
                    str = formatSSID(str);
                    if (!TextUtils.isEmpty(formatSSID) && formatSSID.equals(str)) {
                        this.mWifiManager.disconnect();
                        this.mWifiManager.enableNetwork(wifiConfiguration.networkId, true);
                        return;
                    }
                }
            }
        }
    }

    private WifiConfiguration isWifiConfigurationSaved(String str, WifiCipherType wifiCipherType) {
        List<WifiConfiguration> savedWifiConfiguration = getSavedWifiConfiguration();
        if (savedWifiConfiguration == null) {
            return null;
        }
        String formatSSID = formatSSID(str);
        for (WifiConfiguration wifiConfiguration : savedWifiConfiguration) {
            if (wifiConfiguration != null) {
                String formatSSID2 = formatSSID(wifiConfiguration.SSID);
                if (!TextUtils.isEmpty(formatSSID2) && formatSSID2.equals(formatSSID)) {
                    String str2 = null;
                    for (int i = 0; i < wifiConfiguration.allowedKeyManagement.size(); i++) {
                        if (wifiConfiguration.allowedKeyManagement.get(i) && i < WifiConfiguration.KeyMgmt.strings.length) {
                            str2 = WifiConfiguration.KeyMgmt.strings[i];
                        }
                    }
                    Dbug.e(tag, "isWifiConfigurationSaved  keyMgmt = " + str2 + " , wifiCipherType : " + wifiCipherType);
                    if ((wifiCipherType == WifiCipherType.WPA && IConstant.KEY_WPA.equals(str2)) || (wifiCipherType == WifiCipherType.NONE && IConstant.KEY_NONE.equals(str2))) {
                        Dbug.e(tag, "isWifiConfigurationSaved return object, network id : " + wifiConfiguration.networkId);
                        return wifiConfiguration;
                    }
                }
            }
        }
        return null;
    }

    private WifiConfiguration createWifiConfiguration(String str, String str2, WifiCipherType wifiCipherType) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();
        wifiConfiguration.SSID = "\"" + str + "\"";
        int i = AnonymousClass2.$SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType[wifiCipherType.ordinal()];
        if (i == 1) {
            wifiConfiguration.allowedKeyManagement.set(0);
        } else if (i != 2) {
            if (i == 3) {
                wifiConfiguration.hiddenSSID = true;
                wifiConfiguration.wepKeys[0] = "\"" + str2 + "\"";
                wifiConfiguration.wepTxKeyIndex = 0;
                wifiConfiguration.allowedKeyManagement.set(0);
                wifiConfiguration.allowedGroupCiphers.set(0);
                wifiConfiguration.allowedGroupCiphers.set(2);
                wifiConfiguration.allowedGroupCiphers.set(3);
                wifiConfiguration.allowedGroupCiphers.set(1);
                wifiConfiguration.allowedAuthAlgorithms.set(1);
            } else if (i == 4) {
                wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
                wifiConfiguration.hiddenSSID = true;
                wifiConfiguration.allowedAuthAlgorithms.set(0);
                wifiConfiguration.allowedGroupCiphers.set(2);
                wifiConfiguration.allowedKeyManagement.set(1);
                wifiConfiguration.allowedPairwiseCiphers.set(1);
                wifiConfiguration.allowedGroupCiphers.set(3);
                wifiConfiguration.allowedPairwiseCiphers.set(2);
                wifiConfiguration.status = 2;
            } else {
                if (i != 5) {
                    return null;
                }
                wifiConfiguration.preSharedKey = "\"" + str2 + "\"";
                wifiConfiguration.allowedAuthAlgorithms.set(0);
                wifiConfiguration.allowedGroupCiphers.set(2);
                wifiConfiguration.allowedGroupCiphers.set(3);
                wifiConfiguration.allowedKeyManagement.set(1);
                wifiConfiguration.allowedPairwiseCiphers.set(1);
                wifiConfiguration.allowedPairwiseCiphers.set(2);
                wifiConfiguration.allowedProtocols.set(1);
                wifiConfiguration.status = 2;
            }
        }
        return wifiConfiguration;
    }

    /* renamed from: com.jieli.stream.dv.running2.util.WifiHelper$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType;

        static {
            int[] iArr = new int[WifiCipherType.values().length];
            $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType = iArr;
            try {
                iArr[WifiCipherType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType[WifiCipherType.IEEE8021XEAP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType[WifiCipherType.WEP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType[WifiCipherType.WPA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$jieli$stream$dv$running2$util$WifiHelper$WifiCipherType[WifiCipherType.WPA2.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public static int getNetWorkType(Context context) {
        if (!isNetWorkConnectedType(context, 1)) {
            return 0;
        }
        String typeName = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getTypeName();
        if (typeName.equalsIgnoreCase("WIFI")) {
            return 1;
        }
        if (!typeName.equalsIgnoreCase("MOBILE")) {
            return 0;
        }
        switch (((TelephonyManager) context.getSystemService("phone")).getNetworkType()) {
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
                return 1;
            case 4:
            case 7:
            case 11:
            default:
                return 0;
        }
    }

    public static NetState getConnectedType(Context context) {
        NetState netState = NetState.NET_NO;
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return netState;
        }
        int type = activeNetworkInfo.getType();
        if (type != 0) {
            if (type == 1) {
                return NetState.NET_WIFI;
            }
            return NetState.NET_UNKNOWN;
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return netState;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return NetState.NET_3G;
            case 13:
                return NetState.NET_4G;
            default:
                if (activeNetworkInfo.getSubtypeName().equalsIgnoreCase("TD-SCDMA") || activeNetworkInfo.getSubtypeName().equalsIgnoreCase("WCDMA") || activeNetworkInfo.getSubtypeName().equalsIgnoreCase("CDMA2000")) {
                    return NetState.NET_3G;
                }
                return NetState.NET_UNKNOWN;
        }
    }

    public static String getWifiIP(Context context) {
        String formatIpAddress = formatIpAddress(((WifiManager) context.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getIpAddress());
        if (TextUtils.isEmpty(formatIpAddress)) {
            return null;
        }
        if (formatIpAddress.equals("0.0.0.0")) {
            formatIpAddress = getLocalIpAddress();
            if (formatIpAddress.equals("0.0.0.0")) {
                Dbug.e(tag, "WIFI IP Error");
            }
        }
        return formatIpAddress;
    }

    private static String getLocalIpAddress() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                for (InetAddress inetAddress : Collections.list(((NetworkInterface) it.next()).getInetAddresses())) {
                    String hostAddress = inetAddress.getHostAddress();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return hostAddress;
                    }
                }
            }
            return "0.0.0.0";
        } catch (SocketException e) {
            e.printStackTrace();
            return "0.0.0.0";
        }
    }

    public String getGateWay(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            return null;
        }
        return Formatter.formatIpAddress(wifiManager.getDhcpInfo().gateway);
    }

    public static String getWifiIP(WifiInfo wifiInfo) {
        if (wifiInfo != null) {
            return formatIpAddress(wifiInfo.getIpAddress());
        }
        return null;
    }

    private static String formatIpAddress(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public List<ScanResult> getSpecifiedSSIDList(String str) {
        ArrayList arrayList = new ArrayList();
        startScan();
        List<ScanResult> wifiScanResult = getWifiScanResult();
        if (wifiScanResult == null) {
            Dbug.e(tag, "scanResultList is null");
            return null;
        }
        for (ScanResult scanResult : wifiScanResult) {
            String formatSSID = formatSSID(scanResult.SSID);
            if (!TextUtils.isEmpty(formatSSID) && formatSSID.startsWith(str)) {
                arrayList.add(scanResult);
            }
        }
        return arrayList;
    }

    public void connectOtherWifi(String str) {
        startScan();
        otherWifiSSID = null;
        List<WifiConfiguration> savedWifiConfiguration = getSavedWifiConfiguration();
        List<ScanResult> wifiScanResult = getWifiScanResult();
        if (wifiScanResult == null || savedWifiConfiguration == null) {
            Dbug.e(tag, "scanResultList or saveWifiList is null");
            return;
        }
        Iterator<ScanResult> it = wifiScanResult.iterator();
        boolean z = false;
        while (it.hasNext()) {
            String formatSSID = formatSSID(it.next().SSID);
            if (!TextUtils.isEmpty(formatSSID) && !formatSSID.startsWith(str)) {
                Iterator<WifiConfiguration> it2 = savedWifiConfiguration.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    WifiConfiguration next = it2.next();
                    String formatSSID2 = formatSSID(next.SSID);
                    if (!TextUtils.isEmpty(formatSSID2) && formatSSID.equals(formatSSID2)) {
                        Dbug.e(tag, "Save networkName-> " + formatSSID + " network_id -> " + next.networkId + " networkName : " + formatSSID2);
                        WifiManager wifiManager = this.mWifiManager;
                        if (wifiManager != null) {
                            wifiManager.disconnect();
                            z = this.mWifiManager.enableNetwork(next.networkId, true);
                        }
                        otherWifiSSID = next.SSID;
                    }
                }
                if (z) {
                    return;
                }
            }
        }
    }

    public String getOtherWifiSSID() {
        return otherWifiSSID;
    }

    public static String formatSSID(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.contains("\"")) {
            str = str.replace("\"", "");
        }
        return str.contains(" ") ? str.replace(" ", "") : str;
    }

    public void release() {
        instance = null;
        otherWifiSSID = null;
        releaseWifiLock();
        this.mWifiManager = null;
    }

    public boolean isWifiApEnabled() {
        try {
            Method method = this.mWifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]);
            method.setAccessible(true);
            return ((Boolean) method.invoke(this.mWifiManager, new Object[0])).booleanValue();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return false;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
        if (isWifiApEnabled()) {
            return InetAddress.getByName("192.168.43.255");
        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            return InetAddress.getByName(Discovery.BROADCAST_IP);
        }
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        if (dhcpInfo == null) {
            return InetAddress.getByName(Discovery.BROADCAST_IP);
        }
        int i = (~dhcpInfo.netmask) | (dhcpInfo.ipAddress & dhcpInfo.netmask);
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) ((i >> (i2 * 8)) & 255);
        }
        return InetAddress.getByAddress(bArr);
    }

    public void connectWifi(Context context, String str, String str2) {
        ConnectivityManager connectivityManager;
        if (TextUtils.isEmpty(str)) {
            Dbug.e(tag, "parameter is empty!");
            return;
        }
        String formatSSID = formatSSID(str);
        if (TextUtils.isEmpty(formatSSID) || (connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity")) == null) {
            return;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        WifiInfo wifiConnectionInfo = getWifiConnectionInfo();
        String formatSSID2 = wifiConnectionInfo != null ? formatSSID(wifiConnectionInfo.getSSID()) : null;
        if (activeNetworkInfo != null && activeNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED && formatSSID.equals(formatSSID2)) {
            notifyWifiError(IConstant.ERROR_WIFI_IS_CONNECTED);
        } else if (TextUtils.isEmpty(str2)) {
            addNetWorkAndConnect(formatSSID, "0", WifiCipherType.NONE);
        } else {
            addNetWorkAndConnect(formatSSID, str2, WifiCipherType.WPA);
        }
    }

    public void removeCurrentNetwork(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        if (wifiManager == null) {
            Dbug.e(tag, "WifiManager is null");
            return;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null || TextUtils.isEmpty(connectionInfo.getSSID())) {
            Dbug.e(tag, "-=-=-=wifiInfo is null or  wifiInfo.getSSID() is null");
            return;
        }
        String formatSSID = formatSSID(connectionInfo.getSSID());
        if (TextUtils.isEmpty(formatSSID) || !formatSSID.startsWith(WIFI_PREFIX)) {
            return;
        }
        removeNetWork(connectionInfo.getNetworkId());
        connectOtherWifi(WIFI_PREFIX);
    }

    public String getCurrentConnectedSsid() {
        WifiInfo connectionInfo;
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return null;
        }
        String ssid = connectionInfo.getSSID();
        if (ssid.length() > 2 && ssid.charAt(0) == '\"' && ssid.charAt(ssid.length() - 1) == '\"') {
            return ssid.substring(1, ssid.length() - 1);
        }
        return null;
    }

    public static void bindNetwork(Context context) {
        Dbug.i(tag, "SDK_INT=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 21) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(1);
            builder.removeCapability(12);
            builder.removeTransportType(0);
            NetworkRequest build = builder.build();
            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.jieli.stream.dv.running2.util.WifiHelper.1
                @Override // android.net.ConnectivityManager.NetworkCallback
                public void onAvailable(Network network) {
                    Dbug.i(WifiHelper.tag, "onAvailable.... ");
                    ConnectivityManager connectivityManager2 = connectivityManager;
                    if (connectivityManager2 != null) {
                        connectivityManager2.unregisterNetworkCallback(this);
                        if (Build.VERSION.SDK_INT >= 23) {
                            connectivityManager.bindProcessToNetwork(network);
                        } else {
                            ConnectivityManager.setProcessDefaultNetwork(network);
                        }
                    }
                }
            };
            if (connectivityManager != null) {
                Dbug.w(tag, "registerNetworkCallback");
                connectivityManager.registerNetworkCallback(build, networkCallback);
            }
        }
    }
}
