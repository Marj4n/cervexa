package com.serenegiant.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.ComponentUtils;

@Deprecated
/* loaded from: classes2.dex */
public class NetworkChangedReceiver extends BroadcastReceiver {
    public static final String ACTION_GLOBAL_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_LOCAL_CONNECTIVITY_CHANGE = "com.serenegiant.net.CONNECTIVITY_CHANGE";
    private static final boolean DEBUG = false;
    public static final String KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK = "KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK";
    public static final String KEY_NETWORK_CHANGED_IS_CONNECTED = "KEY_NETWORK_CHANGED_IS_CONNECTED";
    public static final String KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING = "KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING";
    private static final int NETWORK_MASK_INTERNET_WIFI = 706;
    public static final int NETWORK_TYPE_BLUETOOTH = 128;
    public static final int NETWORK_TYPE_ETHERNET = 512;
    public static final int NETWORK_TYPE_MOBILE = 1;
    public static final int NETWORK_TYPE_MOBILE_DUN = 16;
    public static final int NETWORK_TYPE_MOBILE_HIPRI = 32;
    public static final int NETWORK_TYPE_MOBILE_MMS = 4;
    public static final int NETWORK_TYPE_MOBILE_SUPL = 8;
    public static final int NETWORK_TYPE_WIFI = 2;
    public static final int NETWORK_TYPE_WIMAX = 64;
    private static int sGlobalReceiverNum;
    private OnNetworkChangedListener mListener;
    private static final String TAG = NetworkChangedReceiver.class.getSimpleName();
    private static final Object sSync = new Object();
    private static int sIsConnectedOrConnecting = 0;
    private static int sIsConnected = 0;
    private static int sActiveNetworkMask = 0;
    private static final int[] NETWORKS = {0, 1, 1, 2, 2, 4, 3, 8, 4, 16, 5, 32, 6, 64, 7, 128, 9, 512};

    public interface OnNetworkChangedListener {
        void onNetworkChanged(int i, int i2, int i3);
    }

    @Deprecated
    public static void enable(Context context) {
        ComponentUtils.enable(context, NetworkChangedReceiver.class);
    }

    @Deprecated
    public static void disable(Context context) {
        ComponentUtils.disable(context, NetworkChangedReceiver.class);
    }

    public static NetworkChangedReceiver registerGlobal(Context context) {
        return registerGlobal(context, null);
    }

    public static NetworkChangedReceiver registerGlobal(Context context, OnNetworkChangedListener onNetworkChangedListener) {
        NetworkChangedReceiver networkChangedReceiver = new NetworkChangedReceiver(onNetworkChangedListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GLOBAL_CONNECTIVITY_CHANGE);
        synchronized (sSync) {
            context.registerReceiver(networkChangedReceiver, intentFilter);
            sGlobalReceiverNum++;
        }
        return networkChangedReceiver;
    }

    public static boolean isGlobalRegistered() {
        boolean z;
        synchronized (sSync) {
            z = sGlobalReceiverNum > 0;
        }
        return z;
    }

    public static void unregisterGlobal(Context context, NetworkChangedReceiver networkChangedReceiver) {
        synchronized (sSync) {
            if (networkChangedReceiver != null) {
                sGlobalReceiverNum--;
                try {
                    context.unregisterReceiver(networkChangedReceiver);
                } catch (Exception unused) {
                }
            }
        }
    }

    public NetworkChangedReceiver registerLocal(Context context, OnNetworkChangedListener onNetworkChangedListener) {
        Handler handler;
        final int i;
        final int i2;
        final int i3;
        NetworkChangedReceiver networkChangedReceiver = new NetworkChangedReceiver(onNetworkChangedListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_LOCAL_CONNECTIVITY_CHANGE);
        LocalBroadcastManager.getInstance(context.getApplicationContext()).registerReceiver(networkChangedReceiver, intentFilter);
        try {
            handler = new Handler(Looper.getMainLooper());
        } catch (Exception unused) {
            handler = null;
        }
        synchronized (sSync) {
            i = sIsConnectedOrConnecting;
            i2 = sIsConnected;
            i3 = sActiveNetworkMask;
        }
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.serenegiant.net.NetworkChangedReceiver.1
                @Override // java.lang.Runnable
                public void run() {
                    NetworkChangedReceiver.this.callOnNetworkChanged(i, i2, i3);
                }
            });
        } else {
            networkChangedReceiver.callOnNetworkChanged(i, i2, i3);
        }
        return networkChangedReceiver;
    }

    public static void unregisterLocal(Context context, NetworkChangedReceiver networkChangedReceiver) {
        try {
            LocalBroadcastManager.getInstance(context.getApplicationContext()).unregisterReceiver(networkChangedReceiver);
        } catch (Exception unused) {
        }
    }

    @Deprecated
    public NetworkChangedReceiver() {
        this.mListener = null;
    }

    private NetworkChangedReceiver(OnNetworkChangedListener onNetworkChangedListener) {
        this.mListener = onNetworkChangedListener;
    }

    public void unregister(Context context) {
        try {
            unregisterGlobal(context, this);
        } catch (Exception unused) {
        }
        try {
            unregisterLocal(context, this);
        } catch (Exception unused2) {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (ACTION_GLOBAL_CONNECTIVITY_CHANGE.equals(action)) {
            onReceiveGlobal(context, intent);
        } else if (ACTION_LOCAL_CONNECTIVITY_CHANGE.equals(action)) {
            onReceiveLocal(context, intent);
        }
    }

    private void onReceiveGlobal(Context context, Intent intent) {
        int i;
        int i2;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        if (BuildCheck.isAndroid5()) {
            Network[] allNetworks = connectivityManager.getAllNetworks();
            if (allNetworks != null) {
                i = 0;
                i2 = 0;
                for (Network network : allNetworks) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo != null) {
                        i |= networkInfo.isConnectedOrConnecting() ? 1 << networkInfo.getType() : 0;
                        i2 |= networkInfo.isConnected() ? 1 << networkInfo.getType() : 0;
                    }
                }
            } else {
                i = 0;
                i2 = 0;
            }
        } else {
            int length = NETWORKS.length;
            i = 0;
            i2 = 0;
            for (int i3 = 0; i3 < length; i3 += 2) {
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(NETWORKS[i3]);
                if (networkInfo2 != null) {
                    i |= networkInfo2.isConnectedOrConnecting() ? NETWORKS[i3 + 1] : 0;
                    i2 |= networkInfo2.isConnected() ? NETWORKS[i3 + 1] : 0;
                }
            }
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        int type = activeNetworkInfo != null ? 1 << activeNetworkInfo.getType() : 0;
        synchronized (sSync) {
            sIsConnectedOrConnecting = i;
            sIsConnected = i2;
            sActiveNetworkMask = type;
            sSync.notifyAll();
        }
        callOnNetworkChanged(i, i2, type);
        Intent intent2 = new Intent(ACTION_LOCAL_CONNECTIVITY_CHANGE);
        intent2.putExtra(KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING, i);
        intent2.putExtra(KEY_NETWORK_CHANGED_IS_CONNECTED, i2);
        intent2.putExtra(KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK, type);
        localBroadcastManager.sendBroadcast(intent2);
    }

    private void onReceiveLocal(Context context, Intent intent) {
        callOnNetworkChanged(intent.getIntExtra(KEY_NETWORK_CHANGED_IS_CONNECTED_OR_CONNECTING, 0), intent.getIntExtra(KEY_NETWORK_CHANGED_IS_CONNECTED, 0), intent.getIntExtra(KEY_NETWORK_CHANGED_ACTIVE_NETWORK_MASK, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callOnNetworkChanged(int i, int i2, int i3) {
        OnNetworkChangedListener onNetworkChangedListener = this.mListener;
        if (onNetworkChangedListener != null) {
            try {
                onNetworkChangedListener.onNetworkChanged(i, i2, i3);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    public static boolean isWifiNetworkReachable() {
        int i;
        synchronized (sSync) {
            i = sIsConnectedOrConnecting & sActiveNetworkMask;
        }
        return (i & NETWORK_MASK_INTERNET_WIFI) != 0;
    }

    public static boolean isWifiNetworkReachable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return false;
        }
        int type = activeNetworkInfo.getType();
        return type == 1 || type == 6 || type == 7 || type == 9;
    }

    public static boolean isMobileNetworkReachable() {
        int i;
        synchronized (sSync) {
            i = sIsConnectedOrConnecting & sActiveNetworkMask;
        }
        return (i & 1) != 0;
    }

    public static boolean isMobileNetworkReachable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting() && activeNetworkInfo.getType() == 0;
    }

    public static boolean isNetworkReachable() {
        int i;
        synchronized (sSync) {
            i = sIsConnectedOrConnecting & sActiveNetworkMask;
        }
        return i != 0;
    }

    public static boolean isNetworkReachable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
