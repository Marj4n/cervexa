package com.serenegiant.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.util.Log;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.HandlerThreadHandler;
import java.lang.ref.WeakReference;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ConnectivityHelper {
    private static final String ACTION_GLOBAL_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final boolean DEBUG = false;
    public static final int NETWORK_TYPE_BLUETOOTH = 128;
    public static final int NETWORK_TYPE_ETHERNET = 512;
    public static final int NETWORK_TYPE_MOBILE = 1;
    public static final int NETWORK_TYPE_NON = 0;
    public static final int NETWORK_TYPE_WIFI = 2;
    private static final String TAG = ConnectivityHelper.class.getSimpleName();
    private final ConnectivityCallback mCallback;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private BroadcastReceiver mNetworkChangedReceiver;
    private ConnectivityManager.OnNetworkActiveListener mOnNetworkActiveListener;
    private final WeakReference<Context> mWeakContext;
    private final Object mSync = new Object();
    private int mActiveNetworkType = 0;
    private Handler mAsyncHandler = HandlerThreadHandler.createHandler(TAG);

    public interface ConnectivityCallback {
        void onError(Throwable th);

        void onNetworkChanged(int i);
    }

    public ConnectivityHelper(Context context, ConnectivityCallback connectivityCallback) {
        this.mWeakContext = new WeakReference<>(context);
        this.mCallback = connectivityCallback;
        init();
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public void release() {
        updateActiveNetwork(0);
        Context context = getContext();
        if (context != null) {
            if (BuildCheck.isLollipop()) {
                ConnectivityManager requireConnectivityManager = requireConnectivityManager();
                ConnectivityManager.OnNetworkActiveListener onNetworkActiveListener = this.mOnNetworkActiveListener;
                if (onNetworkActiveListener != null) {
                    try {
                        requireConnectivityManager.removeDefaultNetworkActiveListener(onNetworkActiveListener);
                    } catch (Exception e) {
                        Log.w(TAG, e);
                    }
                    this.mOnNetworkActiveListener = null;
                }
                ConnectivityManager.NetworkCallback networkCallback = this.mNetworkCallback;
                if (networkCallback != null) {
                    requireConnectivityManager.unregisterNetworkCallback(networkCallback);
                    this.mNetworkCallback = null;
                }
            }
            BroadcastReceiver broadcastReceiver = this.mNetworkChangedReceiver;
            if (broadcastReceiver != null) {
                try {
                    context.unregisterReceiver(broadcastReceiver);
                } catch (Exception e2) {
                    Log.w(TAG, e2);
                }
                this.mNetworkChangedReceiver = null;
            }
        }
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                try {
                    this.mAsyncHandler.removeCallbacksAndMessages(null);
                    this.mAsyncHandler.getLooper().quit();
                } catch (Exception e3) {
                    Log.w(TAG, e3);
                }
                this.mAsyncHandler = null;
            }
        }
    }

    public boolean isValid() {
        try {
            requireConnectivityManager();
            return true;
        } catch (IllegalStateException unused) {
            return false;
        }
    }

    private Context getContext() {
        Context context = this.mWeakContext.get();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("context is already released");
    }

    private Context requireContext() throws IllegalStateException {
        Context context = this.mWeakContext.get();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("context is already released");
    }

    private ConnectivityManager requireConnectivityManager() throws IllegalStateException {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService("connectivity");
        if (connectivityManager != null) {
            return connectivityManager;
        }
        throw new IllegalStateException("failed to get ConnectivityManager");
    }

    public int getActiveNetworkType() {
        int i;
        synchronized (this.mSync) {
            i = this.mActiveNetworkType;
        }
        return i;
    }

    private void init() {
        ConnectivityManager requireConnectivityManager = requireConnectivityManager();
        if (BuildCheck.isLollipop()) {
            MyOnNetworkActiveListener myOnNetworkActiveListener = new MyOnNetworkActiveListener();
            this.mOnNetworkActiveListener = myOnNetworkActiveListener;
            requireConnectivityManager.addDefaultNetworkActiveListener(myOnNetworkActiveListener);
            this.mNetworkCallback = new MyNetworkCallback();
            if (BuildCheck.isNougat()) {
                requireConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
                return;
            } else if (BuildCheck.isOreo()) {
                requireConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback, this.mAsyncHandler);
                return;
            } else {
                requireConnectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), this.mNetworkCallback);
                return;
            }
        }
        this.mNetworkChangedReceiver = new NetworkChangedReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        requireContext().registerReceiver(this.mNetworkChangedReceiver, intentFilter);
    }

    private void callOnNetworkChanged(final int i) {
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.net.-$$Lambda$ConnectivityHelper$LaPkCj0bTKQL4ectrRH4ABe14m4
                    @Override // java.lang.Runnable
                    public final void run() {
                        ConnectivityHelper.this.lambda$callOnNetworkChanged$0$ConnectivityHelper(i);
                    }
                });
            } else {
                Log.w(TAG, "already released?");
            }
        }
    }

    public /* synthetic */ void lambda$callOnNetworkChanged$0$ConnectivityHelper(int i) {
        try {
            this.mCallback.onNetworkChanged(i);
        } catch (Exception e) {
            callOnError(e);
        }
    }

    private void callOnError(final Throwable th) {
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.net.-$$Lambda$ConnectivityHelper$ckFVyOD7_323_SOoyeMXeSDNl6A
                    @Override // java.lang.Runnable
                    public final void run() {
                        ConnectivityHelper.this.lambda$callOnError$1$ConnectivityHelper(th);
                    }
                });
            } else {
                Log.w(TAG, "already released?");
            }
        }
    }

    public /* synthetic */ void lambda$callOnError$1$ConnectivityHelper(Throwable th) {
        try {
            this.mCallback.onError(th);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveNetwork(Network network) {
        int i = 0;
        ConnectivityManager requireConnectivityManager = requireConnectivityManager();
        NetworkCapabilities networkCapabilities = requireConnectivityManager.getNetworkCapabilities(network);
        NetworkInfo networkInfo = requireConnectivityManager.getNetworkInfo(network);
        if (networkCapabilities != null && networkInfo != null) {
            if (isWifiNetworkReachable(networkCapabilities, networkInfo)) {
                i = 2;
            } else if (isMobileNetworkReachable(networkCapabilities, networkInfo)) {
                i = 1;
            } else if (isBluetoothNetworkReachable(networkCapabilities, networkInfo)) {
                i = 128;
            } else if (isNetworkReachable(networkCapabilities, networkInfo)) {
                i = 512;
            }
            updateActiveNetwork(i);
        }
        i = 0;
        updateActiveNetwork(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveNetwork(NetworkInfo networkInfo) {
        int type = (networkInfo == null || !networkInfo.isConnectedOrConnecting()) ? -1 : networkInfo.getType();
        int i = 0;
        if (type == 0) {
            i = 1;
        } else if (type == 1) {
            i = 2;
        } else if (type == 9) {
            i = 512;
        }
        updateActiveNetwork(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActiveNetwork(int i) {
        synchronized (this.mSync) {
            if (this.mActiveNetworkType != i) {
                this.mActiveNetworkType = i;
                callOnNetworkChanged(i);
            }
        }
    }

    private class MyOnNetworkActiveListener implements ConnectivityManager.OnNetworkActiveListener {
        private final String TAG = MyOnNetworkActiveListener.class.getSimpleName();

        @Override // android.net.ConnectivityManager.OnNetworkActiveListener
        public void onNetworkActive() {
        }

        public MyOnNetworkActiveListener() {
        }
    }

    private class MyNetworkCallback extends ConnectivityManager.NetworkCallback {
        private final String TAG = MyNetworkCallback.class.getSimpleName();

        public MyNetworkCallback() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            ConnectivityHelper.this.updateActiveNetwork(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            ConnectivityHelper.this.updateActiveNetwork(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLosing(Network network, int i) {
            super.onLosing(network, i);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            super.onLost(network);
            ConnectivityHelper.this.updateActiveNetwork(network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            super.onUnavailable();
            ConnectivityHelper.this.updateActiveNetwork(0);
        }
    }

    private static class NetworkChangedReceiver extends BroadcastReceiver {
        private static final String TAG = NetworkChangedReceiver.class.getSimpleName();
        private final ConnectivityHelper mParent;

        public NetworkChangedReceiver(ConnectivityHelper connectivityHelper) {
            this.mParent = connectivityHelper;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent != null ? intent.getAction() : null)) {
                onReceiveGlobal(context, intent);
            }
        }

        private void onReceiveGlobal(Context context, Intent intent) {
            this.mParent.updateActiveNetwork(((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo());
        }
    }

    public static boolean isWifiNetworkReachable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (BuildCheck.isLollipop()) {
            if (BuildCheck.isMarshmallow()) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(activeNetwork);
                return (networkCapabilities == null || networkInfo == null || !isWifiNetworkReachable(networkCapabilities, networkInfo)) ? false : true;
            }
            for (Network network : connectivityManager.getAllNetworks()) {
                NetworkCapabilities networkCapabilities2 = connectivityManager.getNetworkCapabilities(network);
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(network);
                if (networkCapabilities2 != null && networkInfo2 != null && isWifiNetworkReachable(networkCapabilities2, networkInfo2)) {
                    return true;
                }
            }
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return false;
        }
        int type = activeNetworkInfo.getType();
        return type == 1 || type == 6 || type == 7 || type == 9;
    }

    public static boolean isMobileNetworkReachable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (BuildCheck.isLollipop()) {
            if (BuildCheck.isMarshmallow()) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(activeNetwork);
                return (networkCapabilities == null || networkInfo == null || !isMobileNetworkReachable(networkCapabilities, networkInfo)) ? false : true;
            }
            for (Network network : connectivityManager.getAllNetworks()) {
                NetworkCapabilities networkCapabilities2 = connectivityManager.getNetworkCapabilities(network);
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(network);
                if (networkCapabilities2 != null && networkInfo2 != null && isMobileNetworkReachable(networkCapabilities2, networkInfo2)) {
                    return true;
                }
            }
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting() && activeNetworkInfo.getType() == 0;
        }
        return false;
    }

    public static boolean isNetworkReachable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (BuildCheck.isLollipop()) {
            if (BuildCheck.isMarshmallow()) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(activeNetwork);
                return (networkCapabilities == null || networkInfo == null || !isNetworkReachable(networkCapabilities, networkInfo)) ? false : true;
            }
            for (Network network : connectivityManager.getAllNetworks()) {
                NetworkCapabilities networkCapabilities2 = connectivityManager.getNetworkCapabilities(network);
                NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(network);
                if (networkCapabilities2 != null && networkInfo2 != null && isNetworkReachable(networkCapabilities2, networkInfo2)) {
                    return true;
                }
            }
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private static boolean isWifiNetworkReachable(NetworkCapabilities networkCapabilities, NetworkInfo networkInfo) {
        return (!BuildCheck.isOreo() ? networkCapabilities.hasTransport(1) || networkCapabilities.hasTransport(3) : networkCapabilities.hasTransport(1) || networkCapabilities.hasTransport(3)) && isNetworkReachable(networkCapabilities, networkInfo);
    }

    private static boolean isMobileNetworkReachable(NetworkCapabilities networkCapabilities, NetworkInfo networkInfo) {
        boolean hasTransport;
        if (BuildCheck.isOreoMR1()) {
            hasTransport = networkCapabilities.hasTransport(0) || networkCapabilities.hasTransport(6);
        } else {
            hasTransport = networkCapabilities.hasTransport(0);
        }
        return hasTransport && isNetworkReachable(networkCapabilities, networkInfo);
    }

    private static boolean isBluetoothNetworkReachable(NetworkCapabilities networkCapabilities, NetworkInfo networkInfo) {
        return networkCapabilities.hasTransport(2) && isNetworkReachable(networkCapabilities, networkInfo);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
    
        if (r5.hasCapability(19) != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x004c, code lost:
    
        if (r5.hasCapability(16) != false) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean isNetworkReachable(NetworkCapabilities r5, NetworkInfo r6) {
        /*
            android.net.NetworkInfo$DetailedState r6 = r6.getDetailedState()
            android.net.NetworkInfo$DetailedState r0 = android.net.NetworkInfo.DetailedState.CONNECTED
            r1 = 0
            r2 = 1
            if (r6 == r0) goto L11
            android.net.NetworkInfo$DetailedState r0 = android.net.NetworkInfo.DetailedState.CONNECTING
            if (r6 != r0) goto Lf
            goto L11
        Lf:
            r6 = 0
            goto L12
        L11:
            r6 = 1
        L12:
            boolean r0 = com.serenegiant.utils.BuildCheck.isPie()
            r3 = 16
            r4 = 12
            if (r0 == 0) goto L3c
            boolean r0 = r5.hasCapability(r4)
            if (r0 == 0) goto L3a
            boolean r0 = r5.hasCapability(r3)
            if (r0 == 0) goto L3a
            r0 = 21
            boolean r0 = r5.hasCapability(r0)
            if (r0 != 0) goto L38
            r0 = 19
            boolean r5 = r5.hasCapability(r0)
            if (r5 == 0) goto L3a
        L38:
            r5 = 1
            goto L53
        L3a:
            r5 = 0
            goto L53
        L3c:
            boolean r0 = com.serenegiant.utils.BuildCheck.isMarshmallow()
            if (r0 == 0) goto L4f
            boolean r0 = r5.hasCapability(r4)
            if (r0 == 0) goto L3a
            boolean r5 = r5.hasCapability(r3)
            if (r5 == 0) goto L3a
            goto L38
        L4f:
            boolean r5 = r5.hasCapability(r4)
        L53:
            if (r6 == 0) goto L58
            if (r5 == 0) goto L58
            r1 = 1
        L58:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.net.ConnectivityHelper.isNetworkReachable(android.net.NetworkCapabilities, android.net.NetworkInfo):boolean");
    }

    public static String getNetworkTypeString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 128 ? i != 512 ? String.format(Locale.US, "UNKNOWN(%d)", Integer.valueOf(i)) : "ETHERNET" : "BLUETOOTH" : "WIFI" : "MOBILE" : "NON";
    }
}
