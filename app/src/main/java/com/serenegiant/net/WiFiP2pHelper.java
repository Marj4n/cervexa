package com.serenegiant.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes2.dex */
public class WiFiP2pHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = WiFiP2pHelper.class.getSimpleName();
    private static WiFiP2pHelper sWiFiP2PHelper;
    private WifiP2pManager.Channel mChannel;
    private boolean mIsWifiP2pEnabled;
    private WiFiDirectBroadcastReceiver mReceiver;
    private int mRetryCount;
    private final WeakReference<Context> mWeakContext;
    private WifiP2pDevice mWifiP2pDevice;
    private final WifiP2pManager mWifiP2pManager;
    private final Set<WiFiP2pListener> mListeners = new CopyOnWriteArraySet();
    private final List<WifiP2pDevice> mAvailableDevices = new ArrayList();
    private final WifiP2pManager.ChannelListener mChannelListener = new WifiP2pManager.ChannelListener() { // from class: com.serenegiant.net.WiFiP2pHelper.4
        @Override // android.net.wifi.p2p.WifiP2pManager.ChannelListener
        public void onChannelDisconnected() {
            WiFiP2pHelper.this.setIsWifiP2pEnabled(false);
            WiFiP2pHelper.this.resetData();
            synchronized (WiFiP2pHelper.this) {
                WiFiP2pHelper.this.mChannel = null;
            }
            if (WiFiP2pHelper.this.mRetryCount == 0) {
                WiFiP2pHelper.access$308(WiFiP2pHelper.this);
                Context context = (Context) WiFiP2pHelper.this.mWeakContext.get();
                if ((WiFiP2pHelper.this.mReceiver == null) && (context != null)) {
                    WiFiP2pHelper wiFiP2pHelper = WiFiP2pHelper.this;
                    wiFiP2pHelper.mChannel = wiFiP2pHelper.mWifiP2pManager.initialize(context, context.getMainLooper(), WiFiP2pHelper.this.mChannelListener);
                }
            }
        }
    };
    private final WifiP2pManager.PeerListListener mPeerListListener = new WifiP2pManager.PeerListListener() { // from class: com.serenegiant.net.WiFiP2pHelper.5
        @Override // android.net.wifi.p2p.WifiP2pManager.PeerListListener
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            Collection<WifiP2pDevice> deviceList = wifiP2pDeviceList.getDeviceList();
            synchronized (WiFiP2pHelper.this.mAvailableDevices) {
                WiFiP2pHelper.this.mAvailableDevices.clear();
                WiFiP2pHelper.this.mAvailableDevices.addAll(deviceList);
            }
            WiFiP2pHelper wiFiP2pHelper = WiFiP2pHelper.this;
            wiFiP2pHelper.callOnUpdateDevices(wiFiP2pHelper.mAvailableDevices);
        }
    };
    private final WifiP2pManager.ConnectionInfoListener mConnectionInfoListener = new WifiP2pManager.ConnectionInfoListener() { // from class: com.serenegiant.net.WiFiP2pHelper.6
        @Override // android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            if (wifiP2pInfo != null) {
                WiFiP2pHelper.this.callOnConnect(wifiP2pInfo);
            }
        }
    };

    static /* synthetic */ int access$308(WiFiP2pHelper wiFiP2pHelper) {
        int i = wiFiP2pHelper.mRetryCount;
        wiFiP2pHelper.mRetryCount = i + 1;
        return i;
    }

    public static synchronized WiFiP2pHelper getInstance(Context context) {
        WiFiP2pHelper wiFiP2pHelper;
        synchronized (WiFiP2pHelper.class) {
            if (sWiFiP2PHelper == null || sWiFiP2PHelper.mWeakContext.get() == null) {
                sWiFiP2PHelper = new WiFiP2pHelper(context);
            }
            wiFiP2pHelper = sWiFiP2PHelper;
        }
        return wiFiP2pHelper;
    }

    public static synchronized void release() {
        synchronized (WiFiP2pHelper.class) {
            sWiFiP2PHelper = null;
        }
    }

    private WiFiP2pHelper(Context context) {
        this.mWeakContext = new WeakReference<>(context);
        this.mWifiP2pManager = (WifiP2pManager) context.getSystemService("wifip2p");
    }

    public synchronized void register() {
        Context context = this.mWeakContext.get();
        boolean z = true;
        boolean z2 = context != null;
        if (this.mReceiver != null) {
            z = false;
        }
        if (z & z2) {
            this.mChannel = this.mWifiP2pManager.initialize(context, context.getMainLooper(), this.mChannelListener);
            this.mReceiver = new WiFiDirectBroadcastReceiver(this.mWifiP2pManager, this.mChannel, this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
            intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
            intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
            intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
            context.registerReceiver(this.mReceiver, intentFilter);
        }
    }

    public synchronized void unregister() {
        this.mIsWifiP2pEnabled = false;
        this.mRetryCount = 0;
        internalDisconnect(null);
        if (this.mReceiver != null) {
            try {
                this.mWeakContext.get().unregisterReceiver(this.mReceiver);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
            this.mReceiver = null;
        }
    }

    public void add(WiFiP2pListener wiFiP2pListener) {
        this.mListeners.add(wiFiP2pListener);
    }

    public void remove(WiFiP2pListener wiFiP2pListener) {
        this.mListeners.remove(wiFiP2pListener);
    }

    public synchronized void startDiscovery() throws IllegalStateException {
        if (this.mChannel != null) {
            this.mWifiP2pManager.discoverPeers(this.mChannel, new WifiP2pManager.ActionListener() { // from class: com.serenegiant.net.WiFiP2pHelper.1
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i) {
                    WiFiP2pHelper.this.callOnError(new RuntimeException("failed to start discovery, reason=" + i));
                }
            });
        } else {
            throw new IllegalStateException("not registered");
        }
    }

    public void connect(String str) {
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = str;
        wifiP2pConfig.wps.setup = 0;
        connect(wifiP2pConfig);
    }

    public void connect(WifiP2pDevice wifiP2pDevice) {
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
        wifiP2pConfig.wps.setup = 0;
        connect(wifiP2pConfig);
    }

    public void connect(WifiP2pConfig wifiP2pConfig) throws IllegalStateException {
        WifiP2pManager.Channel channel = this.mChannel;
        if (channel != null) {
            this.mWifiP2pManager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() { // from class: com.serenegiant.net.WiFiP2pHelper.2
                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onSuccess() {
                }

                @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
                public void onFailure(int i) {
                    WiFiP2pHelper.this.callOnError(new RuntimeException("failed to connect, reason=" + i));
                }
            });
            return;
        }
        throw new IllegalStateException("not registered");
    }

    protected void internalDisconnect(WifiP2pManager.ActionListener actionListener) {
        if (this.mWifiP2pManager != null) {
            WifiP2pDevice wifiP2pDevice = this.mWifiP2pDevice;
            if (wifiP2pDevice == null || wifiP2pDevice.status == 0) {
                WifiP2pManager.Channel channel = this.mChannel;
                if (channel != null) {
                    this.mWifiP2pManager.removeGroup(channel, actionListener);
                    return;
                }
                return;
            }
            if (this.mWifiP2pDevice.status == 3 || this.mWifiP2pDevice.status == 1) {
                this.mWifiP2pManager.cancelConnect(this.mChannel, actionListener);
            }
        }
    }

    public synchronized void disconnect() {
        internalDisconnect(new WifiP2pManager.ActionListener() { // from class: com.serenegiant.net.WiFiP2pHelper.3
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int i) {
                WiFiP2pHelper.this.callOnError(new RuntimeException("failed to disconnect, reason=" + i));
            }
        });
    }

    public synchronized boolean isWiFiP2pEnabled() {
        boolean z = false;
        if (this.mChannel != null) {
            z = this.mIsWifiP2pEnabled;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setIsWifiP2pEnabled(boolean z) {
        this.mIsWifiP2pEnabled = z;
        callOnStateChanged(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetData() {
        if (isConnectedOrConnecting()) {
            callOnDisconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateDevice(WifiP2pDevice wifiP2pDevice) {
        this.mWifiP2pDevice = wifiP2pDevice;
    }

    public synchronized boolean isConnected() {
        boolean z = false;
        if (this.mWifiP2pDevice != null) {
            z = this.mWifiP2pDevice.status == 0;
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0010, code lost:
    
        if (r2.mWifiP2pDevice.status == 1) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isConnectedOrConnecting() {
        /*
            r2 = this;
            monitor-enter(r2)
            android.net.wifi.p2p.WifiP2pDevice r0 = r2.mWifiP2pDevice     // Catch: java.lang.Throwable -> L16
            r1 = 1
            if (r0 == 0) goto L13
            android.net.wifi.p2p.WifiP2pDevice r0 = r2.mWifiP2pDevice     // Catch: java.lang.Throwable -> L16
            int r0 = r0.status     // Catch: java.lang.Throwable -> L16
            if (r0 == 0) goto L14
            android.net.wifi.p2p.WifiP2pDevice r0 = r2.mWifiP2pDevice     // Catch: java.lang.Throwable -> L16
            int r0 = r0.status     // Catch: java.lang.Throwable -> L16
            if (r0 != r1) goto L13
            goto L14
        L13:
            r1 = 0
        L14:
            monitor-exit(r2)
            return r1
        L16:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.net.WiFiP2pHelper.isConnectedOrConnecting():boolean");
    }

    protected void callOnStateChanged(boolean z) {
        for (WiFiP2pListener wiFiP2pListener : this.mListeners) {
            try {
                wiFiP2pListener.onStateChanged(z);
            } catch (Exception e) {
                Log.w(TAG, e);
                this.mListeners.remove(wiFiP2pListener);
            }
        }
    }

    protected void callOnUpdateDevices(List<WifiP2pDevice> list) {
        for (WiFiP2pListener wiFiP2pListener : this.mListeners) {
            try {
                wiFiP2pListener.onUpdateDevices(list);
            } catch (Exception e) {
                Log.w(TAG, e);
                this.mListeners.remove(wiFiP2pListener);
            }
        }
    }

    protected void callOnConnect(WifiP2pInfo wifiP2pInfo) {
        for (WiFiP2pListener wiFiP2pListener : this.mListeners) {
            try {
                wiFiP2pListener.onConnect(wifiP2pInfo);
            } catch (Exception e) {
                Log.w(TAG, e);
                this.mListeners.remove(wiFiP2pListener);
            }
        }
    }

    protected void callOnDisconnect() {
        for (WiFiP2pListener wiFiP2pListener : this.mListeners) {
            try {
                wiFiP2pListener.onDisconnect();
            } catch (Exception e) {
                Log.w(TAG, e);
                this.mListeners.remove(wiFiP2pListener);
            }
        }
    }

    protected void callOnError(Exception exc) {
        for (WiFiP2pListener wiFiP2pListener : this.mListeners) {
            try {
                wiFiP2pListener.onError(exc);
            } catch (Exception e) {
                Log.w(TAG, e);
                this.mListeners.remove(wiFiP2pListener);
            }
        }
    }

    private static class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
        private WifiP2pManager.Channel mChannel;
        private WifiP2pManager mManager;
        private WiFiP2pHelper mParent;

        public WiFiDirectBroadcastReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, WiFiP2pHelper wiFiP2pHelper) {
            this.mManager = wifiP2pManager;
            this.mChannel = channel;
            this.mParent = wiFiP2pHelper;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent != null ? intent.getAction() : null;
            if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
                try {
                    if (intent.getIntExtra("wifi_p2p_state", -1) == 2) {
                        this.mParent.setIsWifiP2pEnabled(true);
                    } else {
                        this.mParent.setIsWifiP2pEnabled(false);
                        this.mParent.resetData();
                    }
                    return;
                } catch (Exception e) {
                    this.mParent.callOnError(e);
                    return;
                }
            }
            if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
                try {
                    this.mManager.requestPeers(this.mChannel, this.mParent.mPeerListListener);
                    return;
                } catch (Exception e2) {
                    this.mParent.callOnError(e2);
                    return;
                }
            }
            if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
                try {
                    if (((NetworkInfo) intent.getParcelableExtra("networkInfo")).isConnected()) {
                        this.mManager.requestConnectionInfo(this.mChannel, this.mParent.mConnectionInfoListener);
                    } else {
                        this.mParent.resetData();
                    }
                    return;
                } catch (Exception e3) {
                    this.mParent.callOnError(e3);
                    return;
                }
            }
            if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
                try {
                    this.mParent.updateDevice((WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice"));
                } catch (Exception e4) {
                    this.mParent.callOnError(e4);
                }
            }
        }
    }
}
