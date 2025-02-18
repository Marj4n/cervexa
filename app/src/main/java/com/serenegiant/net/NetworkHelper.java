package com.serenegiant.net;

import android.util.Log;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class NetworkHelper {
    private static final String TAG = NetworkHelper.class.getSimpleName();

    public static String getLocalIPv4Address() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
                while (it2.hasNext()) {
                    InetAddress inetAddress = (InetAddress) it2.next();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            Iterator it3 = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it3.hasNext()) {
                Iterator<InterfaceAddress> it4 = ((NetworkInterface) it3.next()).getInterfaceAddresses().iterator();
                while (it4.hasNext()) {
                    InetAddress address = it4.next().getAddress();
                    if (!address.isLoopbackAddress() && !(address instanceof Inet4Address)) {
                        return address.getHostAddress();
                    }
                }
            }
            return null;
        } catch (NullPointerException | SocketException e) {
            Log.e(TAG, "getLocalIPv4Address", e);
            return null;
        }
    }

    public static String getLocalIPv6Address() {
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                Iterator it2 = Collections.list(((NetworkInterface) it.next()).getInetAddresses()).iterator();
                while (it2.hasNext()) {
                    InetAddress inetAddress = (InetAddress) it2.next();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet6Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return null;
        } catch (NullPointerException | SocketException e) {
            Log.w(TAG, "getLocalIPv6Address", e);
            return null;
        }
    }
}
