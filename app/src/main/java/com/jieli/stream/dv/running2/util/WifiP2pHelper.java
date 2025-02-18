package com.jieli.stream.dv.running2.util;

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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class WifiP2pHelper {
    private static WifiP2pHelper instance;
    private WiFiDirectBroadcastReceiver mBroadcastReceiver;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pDevice mConnectWifiDevice;
    private Context mMainContext;
    private WifiP2pManager mP2pManager;
    private Set<IWifiDirectListener> mWifiDirectListeners;
    private MyWifiHandler mWifiHandler;
    private List<WifiP2pDevice> peers = new ArrayList();
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.7
        @Override // android.net.wifi.p2p.WifiP2pManager.PeerListListener
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            WifiP2pHelper.this.peers.clear();
            if (wifiP2pDeviceList != null) {
                WifiP2pHelper.this.peers.addAll(wifiP2pDeviceList.getDeviceList());
                WifiP2pHelper wifiP2pHelper = WifiP2pHelper.this;
                wifiP2pHelper.notifyP2pPeerListChanged(wifiP2pHelper.peers);
            }
        }
    };
    private WifiP2pManager.ConnectionInfoListener mConnectionInfoListener = new WifiP2pManager.ConnectionInfoListener() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.8
        @Override // android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            WifiP2pHelper.this.notifyP2pConnectionChanged(wifiP2pInfo);
        }
    };

    private WifiP2pHelper(Context context) {
        if (context == null) {
            throw new NullPointerException("Context can not be empty.");
        }
        this.mMainContext = context;
        WifiP2pManager wifiP2pManager = (WifiP2pManager) context.getSystemService("wifip2p");
        this.mP2pManager = wifiP2pManager;
        Context context2 = this.mMainContext;
        this.mChannel = wifiP2pManager.initialize(context2, context2.getMainLooper(), null);
        this.mWifiHandler = new MyWifiHandler(this.mMainContext.getMainLooper());
    }

    public static WifiP2pHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (WifiP2pHelper.class) {
                if (instance == null) {
                    instance = new WifiP2pHelper(context);
                }
            }
        }
        return instance;
    }

    public void registerBroadcastReceiver(IWifiDirectListener iWifiDirectListener) {
        if (this.mWifiDirectListeners == null) {
            this.mWifiDirectListeners = new HashSet();
        }
        this.mWifiDirectListeners.add(iWifiDirectListener);
        if (this.mBroadcastReceiver != null || this.mMainContext == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver();
        this.mBroadcastReceiver = wiFiDirectBroadcastReceiver;
        this.mMainContext.registerReceiver(wiFiDirectBroadcastReceiver, intentFilter);
    }

    public void unregisterBroadcastReceiver(IWifiDirectListener iWifiDirectListener) {
        WiFiDirectBroadcastReceiver wiFiDirectBroadcastReceiver;
        Set<IWifiDirectListener> set = this.mWifiDirectListeners;
        if (set != null) {
            set.remove(iWifiDirectListener);
        }
        Context context = this.mMainContext;
        if (context == null || (wiFiDirectBroadcastReceiver = this.mBroadcastReceiver) == null) {
            return;
        }
        context.unregisterReceiver(wiFiDirectBroadcastReceiver);
        this.mBroadcastReceiver = null;
    }

    public void startDiscoverPeers(WifiP2pManager.ActionListener actionListener) {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.discoverPeers(channel, actionListener);
    }

    public void stopDiscoverPeers(WifiP2pManager.ActionListener actionListener) {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.stopPeerDiscovery(channel, actionListener);
    }

    public void connectP2pDevice(WifiP2pDevice wifiP2pDevice, WifiP2pManager.ActionListener actionListener) {
        if (this.mP2pManager == null || wifiP2pDevice == null) {
            return;
        }
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
        wifiP2pConfig.wps.setup = 0;
        this.mP2pManager.connect(this.mChannel, wifiP2pConfig, actionListener);
    }

    public void connectP2pDeviceForGroup(final WifiP2pDevice wifiP2pDevice, final WifiP2pManager.ActionListener actionListener) {
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || wifiP2pDevice == null) {
            return;
        }
        wifiP2pManager.createGroup(this.mChannel, new WifiP2pManager.ActionListener() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.1
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
                wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
                wifiP2pConfig.wps.setup = 0;
                WifiP2pHelper.this.mP2pManager.connect(WifiP2pHelper.this.mChannel, wifiP2pConfig, actionListener);
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int i) {
                WifiP2pManager.ActionListener actionListener2 = actionListener;
                if (actionListener2 != null) {
                    actionListener2.onFailure(i);
                }
            }
        });
    }

    public void disconnectP2pDevice(WifiP2pManager.ActionListener actionListener) {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.cancelConnect(channel, actionListener);
    }

    public void disconnectP2pForGroup(final WifiP2pManager.ActionListener actionListener) {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.2
            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onSuccess() {
                WifiP2pHelper.this.disconnectP2pDevice(actionListener);
            }

            @Override // android.net.wifi.p2p.WifiP2pManager.ActionListener
            public void onFailure(int i) {
                WifiP2pManager.ActionListener actionListener2 = actionListener;
                if (actionListener2 != null) {
                    actionListener2.onFailure(i);
                }
            }
        });
    }

    public List<WifiP2pDevice> getPeerList() {
        return this.peers;
    }

    public void requestConnectionInfo() {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.requestConnectionInfo(channel, this.mConnectionInfoListener);
    }

    public void requestPeerList() {
        WifiP2pManager.Channel channel;
        WifiP2pManager wifiP2pManager = this.mP2pManager;
        if (wifiP2pManager == null || (channel = this.mChannel) == null) {
            return;
        }
        wifiP2pManager.requestPeers(channel, this.peerListListener);
    }

    public void setConnectWifiDevice(WifiP2pDevice wifiP2pDevice) {
        this.mConnectWifiDevice = wifiP2pDevice;
    }

    public WifiP2pDevice getConnectWifiDevice() {
        return this.mConnectWifiDevice;
    }

    public void release() {
        MyWifiHandler myWifiHandler = this.mWifiHandler;
        if (myWifiHandler != null) {
            myWifiHandler.removeCallbacksAndMessages(null);
        }
        stopDiscoverPeers(null);
        Set<IWifiDirectListener> set = this.mWifiDirectListeners;
        if (set != null) {
            set.clear();
        }
        List<WifiP2pDevice> list = this.peers;
        if (list != null) {
            list.clear();
        }
        this.mConnectWifiDevice = null;
        this.mMainContext = null;
        this.mP2pManager = null;
        this.mChannel = null;
    }

    private class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
        private WiFiDirectBroadcastReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            char c;
            NetworkInfo networkInfo;
            if (intent != null) {
                String action = intent.getAction();
                if (TextUtils.isEmpty(action)) {
                    return;
                }
                switch (action.hashCode()) {
                    case -1772632330:
                        if (action.equals("android.net.wifi.p2p.CONNECTION_STATE_CHANGE")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1566767901:
                        if (action.equals("android.net.wifi.p2p.THIS_DEVICE_CHANGED")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1394739139:
                        if (action.equals("android.net.wifi.p2p.PEERS_CHANGED")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1695662461:
                        if (action.equals("android.net.wifi.p2p.STATE_CHANGED")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    int intExtra = intent.getIntExtra("wifi_p2p_state", -1);
                    WifiP2pHelper.this.notifyP2pStateChanged(intExtra);
                    if (intExtra != 1 || WifiP2pHelper.this.mConnectWifiDevice == null) {
                        return;
                    }
                    WifiP2pHelper.this.mConnectWifiDevice = null;
                    return;
                }
                if (c == 1) {
                    WifiP2pHelper.this.requestPeerList();
                    return;
                }
                if (c != 2) {
                    if (c != 3) {
                        return;
                    }
                    WifiP2pHelper.this.notifyP2pDeviceChanged((WifiP2pDevice) intent.getParcelableExtra("wifiP2pDevice"));
                } else {
                    if (WifiP2pHelper.this.mP2pManager == null || (networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo")) == null || !networkInfo.isConnected()) {
                        return;
                    }
                    WifiP2pHelper.this.mP2pManager.requestConnectionInfo(WifiP2pHelper.this.mChannel, WifiP2pHelper.this.mConnectionInfoListener);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyP2pStateChanged(final int i) {
        MyWifiHandler myWifiHandler = this.mWifiHandler;
        if (myWifiHandler == null || this.mWifiDirectListeners == null) {
            return;
        }
        myWifiHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.3
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = WifiP2pHelper.this.mWifiDirectListeners.iterator();
                while (it.hasNext()) {
                    ((IWifiDirectListener) it.next()).onCallP2pStateChanged(i);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyP2pPeerListChanged(final List<WifiP2pDevice> list) {
        MyWifiHandler myWifiHandler = this.mWifiHandler;
        if (myWifiHandler == null || this.mWifiDirectListeners == null) {
            return;
        }
        myWifiHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.4
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = WifiP2pHelper.this.mWifiDirectListeners.iterator();
                while (it.hasNext()) {
                    ((IWifiDirectListener) it.next()).onCallP2pPeersChanged(list);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyP2pConnectionChanged(final WifiP2pInfo wifiP2pInfo) {
        MyWifiHandler myWifiHandler = this.mWifiHandler;
        if (myWifiHandler == null || this.mWifiDirectListeners == null) {
            return;
        }
        myWifiHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.5
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = WifiP2pHelper.this.mWifiDirectListeners.iterator();
                while (it.hasNext()) {
                    ((IWifiDirectListener) it.next()).onCallP2pConnectionChanged(wifiP2pInfo);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyP2pDeviceChanged(final WifiP2pDevice wifiP2pDevice) {
        MyWifiHandler myWifiHandler = this.mWifiHandler;
        if (myWifiHandler == null || this.mWifiDirectListeners == null) {
            return;
        }
        myWifiHandler.post(new Runnable() { // from class: com.jieli.stream.dv.running2.util.WifiP2pHelper.6
            @Override // java.lang.Runnable
            public void run() {
                Iterator it = WifiP2pHelper.this.mWifiDirectListeners.iterator();
                while (it.hasNext()) {
                    ((IWifiDirectListener) it.next()).onCallP2pDeviceChanged(wifiP2pDevice);
                }
            }
        });
    }

    private class MyWifiHandler extends Handler {
        private MyWifiHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }
}
