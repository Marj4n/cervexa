package com.serenegiant.bluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import com.serenegiant.utils.HandlerThreadHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
public class BluetoothManager {
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    public static final int STATE_RELEASED = -1;
    private static final String TAG = BluetoothManager.class.getSimpleName();
    public static final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter mAdapter;
    private Handler mAsyncHandler;
    private final BroadcastReceiver mBroadcastReceiver;
    private final Set<BluetoothManagerCallback> mCallbacks;
    private ConnectingThread mConnectingThread;
    private final List<BluetoothDeviceInfo> mDiscoveredDeviceList;
    private ListeningThread mInSecureListeningThread;
    private final UUID mInSecureProfileUUID;
    private final String mName;
    private ReceiverThread mReceiverThread;
    private ListeningThread mSecureListeningThread;
    private final UUID mSecureProfileUUID;
    private volatile int mState;
    private final Object mSync;
    private final WeakReference<Context> mWeakContext;

    public interface BluetoothManagerCallback {
        void onConnect(String str, String str2);

        void onDisconnect();

        void onDiscover(Collection<BluetoothDeviceInfo> collection);

        void onFailed();

        void onReceive(byte[] bArr, int i);
    }

    public static boolean isAvailable() {
        try {
            return BluetoothAdapter.getDefaultAdapter() != null;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }

    public static boolean isEnabled() {
        try {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter != null) {
                return defaultAdapter.isEnabled();
            }
            return false;
        } catch (Exception e) {
            Log.w(TAG, e);
            return false;
        }
    }

    public static boolean requestBluetoothEnable(Activity activity, int i) throws SecurityException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.isEnabled()) {
            activity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), i);
        }
        return defaultAdapter != null && defaultAdapter.isEnabled();
    }

    public static boolean requestBluetoothEnable(Fragment fragment, int i) throws SecurityException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.isEnabled()) {
            fragment.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), i);
        }
        return defaultAdapter != null && defaultAdapter.isEnabled();
    }

    public static boolean requestBluetoothEnable(androidx.fragment.app.Fragment fragment, int i) throws SecurityException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.isEnabled()) {
            fragment.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), i);
        }
        return defaultAdapter != null && defaultAdapter.isEnabled();
    }

    public static Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            return null;
        }
        return defaultAdapter.getBondedDevices();
    }

    public static boolean requestDiscoverable(Activity activity, int i) throws IllegalStateException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
        if (defaultAdapter.getScanMode() != 23) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", i);
            activity.startActivity(intent);
        }
        return defaultAdapter.getScanMode() == 23;
    }

    public static boolean requestDiscoverable(Fragment fragment, int i) throws IllegalStateException {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
        if (defaultAdapter.getScanMode() != 23) {
            Intent intent = new Intent("android.bluetooth.adapter.action.REQUEST_DISCOVERABLE");
            if (i > 0 && i <= 300) {
                intent.putExtra("android.bluetooth.adapter.extra.DISCOVERABLE_DURATION", i);
            }
            fragment.startActivity(intent);
        }
        return defaultAdapter.getScanMode() == 23;
    }

    public BluetoothManager(Context context, String str, UUID uuid, BluetoothManagerCallback bluetoothManagerCallback) {
        this(context, str, uuid, null, bluetoothManagerCallback);
    }

    public BluetoothManager(Context context, String str, UUID uuid, UUID uuid2, BluetoothManagerCallback bluetoothManagerCallback) {
        this.mSync = new Object();
        this.mCallbacks = new CopyOnWriteArraySet();
        this.mDiscoveredDeviceList = new ArrayList();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.serenegiant.bluetooth.BluetoothManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent != null ? intent.getAction() : null;
                if ("android.bluetooth.device.action.FOUND".equals(action)) {
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (bluetoothDevice.getBondState() != 12) {
                        synchronized (BluetoothManager.this.mDiscoveredDeviceList) {
                            BluetoothManager.this.mDiscoveredDeviceList.add(new BluetoothDeviceInfo(bluetoothDevice));
                        }
                        BluetoothManager.this.callOnDiscover();
                        return;
                    }
                    return;
                }
                if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    BluetoothManager.this.callOnDiscover();
                }
            }
        };
        this.mWeakContext = new WeakReference<>(context);
        if (TextUtils.isEmpty(str)) {
            str = Build.MODEL + "_" + Build.ID;
        }
        this.mName = str;
        this.mSecureProfileUUID = uuid;
        this.mInSecureProfileUUID = uuid2 != null ? uuid2 : uuid;
        if (bluetoothManagerCallback != null) {
            this.mCallbacks.add(bluetoothManagerCallback);
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mAdapter = defaultAdapter;
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            throw new IllegalStateException("bluetoothに対応していないか無効になっている");
        }
        this.mState = 0;
        this.mAsyncHandler = HandlerThreadHandler.createHandler(TAG);
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        context.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void release() {
        this.mCallbacks.clear();
        synchronized (this.mSync) {
            if (this.mState != -1) {
                this.mState = -1;
                stop();
                if (this.mAsyncHandler != null) {
                    try {
                        this.mAsyncHandler.getLooper().quit();
                    } catch (Exception unused) {
                    }
                    this.mAsyncHandler = null;
                }
                try {
                    getContext().unregisterReceiver(this.mBroadcastReceiver);
                } catch (Exception unused2) {
                }
            }
        }
    }

    public void addCallback(BluetoothManagerCallback bluetoothManagerCallback) {
        if (bluetoothManagerCallback != null) {
            this.mCallbacks.add(bluetoothManagerCallback);
        }
    }

    public void removeCallback(BluetoothManagerCallback bluetoothManagerCallback) {
        this.mCallbacks.remove(bluetoothManagerCallback);
    }

    public Collection<BluetoothDeviceInfo> getPairedDevices() {
        checkReleased();
        ArrayList arrayList = new ArrayList();
        synchronized (this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }
            Set<BluetoothDevice> bondedDevices = this.mAdapter.getBondedDevices();
            if (bondedDevices.size() > 0) {
                Iterator<BluetoothDevice> it = bondedDevices.iterator();
                while (it.hasNext()) {
                    arrayList.add(new BluetoothDeviceInfo(it.next()));
                }
            }
        }
        return arrayList;
    }

    public void startDiscovery() throws IllegalStateException {
        synchronized (this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }
            Set<BluetoothDevice> bondedDevices = this.mAdapter.getBondedDevices();
            synchronized (this.mDiscoveredDeviceList) {
                this.mDiscoveredDeviceList.clear();
                if (bondedDevices.size() > 0) {
                    Iterator<BluetoothDevice> it = bondedDevices.iterator();
                    while (it.hasNext()) {
                        this.mDiscoveredDeviceList.add(new BluetoothDeviceInfo(it.next()));
                    }
                    callOnDiscover();
                }
            }
            this.mAdapter.startDiscovery();
        }
    }

    public void stopDiscovery() {
        synchronized (this.mSync) {
            if (this.mAdapter.isDiscovering()) {
                this.mAdapter.cancelDiscovery();
            }
        }
    }

    public void startListen() {
        synchronized (this.mSync) {
            checkReleased();
            internalStartListen();
        }
    }

    public void connect(BluetoothDeviceInfo bluetoothDeviceInfo) throws IllegalStateException {
        checkReleased();
        connect(this.mAdapter.getRemoteDevice(bluetoothDeviceInfo.address));
    }

    public void connect(String str) throws IllegalArgumentException, IllegalStateException {
        checkReleased();
        connect(this.mAdapter.getRemoteDevice(str));
    }

    public void connect(BluetoothDevice bluetoothDevice) throws IllegalStateException {
        synchronized (this.mSync) {
            checkReleased();
            internalCancel(2, false);
            try {
                try {
                    this.mConnectingThread = new ConnectingThread(bluetoothDevice, true);
                } catch (IOException unused) {
                    this.mConnectingThread = new ConnectingThread(bluetoothDevice, false);
                }
                this.mConnectingThread.start();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void stop() {
        synchronized (this.mSync) {
            internalCancel(0, true);
        }
    }

    public void send(byte[] bArr) throws IllegalStateException {
        synchronized (this.mSync) {
            checkReleased();
            if (this.mReceiverThread != null) {
                this.mReceiverThread.write(bArr);
            }
        }
    }

    public void send(byte[] bArr, int i, int i2) throws IllegalStateException {
        synchronized (this.mSync) {
            checkReleased();
            if (this.mReceiverThread != null) {
                this.mReceiverThread.write(bArr, i, i2);
            }
        }
    }

    public int getState() {
        int i;
        synchronized (this.mSync) {
            i = this.mState;
        }
        return i;
    }

    public boolean isReleased() {
        boolean z;
        synchronized (this.mSync) {
            z = this.mState == -1;
        }
        return z;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mSync) {
            z = this.mState == 3;
        }
        return z;
    }

    public boolean isListening() {
        boolean z;
        synchronized (this.mSync) {
            z = true;
            if (this.mState != 1) {
                z = false;
            }
        }
        return z;
    }

    protected Context getContext() {
        return this.mWeakContext.get();
    }

    private void checkReleased() throws IllegalStateException {
        if (this.mState == -1) {
            throw new IllegalStateException("already released");
        }
    }

    private void internalStartListen() {
        synchronized (this.mSync) {
            internalCancel(1, false);
            if (isReleased()) {
                return;
            }
            if (this.mSecureListeningThread == null) {
                ListeningThread listeningThread = new ListeningThread(true);
                this.mSecureListeningThread = listeningThread;
                listeningThread.start();
            }
            if (this.mInSecureListeningThread == null) {
                ListeningThread listeningThread2 = new ListeningThread(false);
                this.mInSecureListeningThread = listeningThread2;
                listeningThread2.start();
            }
        }
    }

    private void internalCancel(int i, boolean z) {
        if (this.mAdapter.isDiscovering()) {
            this.mAdapter.cancelDiscovery();
        }
        ConnectingThread connectingThread = this.mConnectingThread;
        if (connectingThread != null) {
            connectingThread.cancel();
            this.mConnectingThread = null;
        }
        ReceiverThread receiverThread = this.mReceiverThread;
        if (receiverThread != null) {
            receiverThread.cancel();
            this.mReceiverThread = null;
        }
        if (this.mState == -1 || z) {
            ListeningThread listeningThread = this.mSecureListeningThread;
            if (listeningThread != null) {
                listeningThread.cancel();
                this.mSecureListeningThread = null;
            }
            ListeningThread listeningThread2 = this.mInSecureListeningThread;
            if (listeningThread2 != null) {
                listeningThread2.cancel();
                this.mInSecureListeningThread = null;
            }
        }
        setState(i);
    }

    protected void callOnDiscover() {
        final ArrayList arrayList;
        synchronized (this.mDiscoveredDeviceList) {
            arrayList = new ArrayList(this.mDiscoveredDeviceList);
        }
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.bluetooth.-$$Lambda$BluetoothManager$aq-ZAjQqydCJxal-0PDMEjLFE2Y
                    @Override // java.lang.Runnable
                    public final void run() {
                        BluetoothManager.this.lambda$callOnDiscover$0$BluetoothManager(arrayList);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$callOnDiscover$0$BluetoothManager(List list) {
        for (BluetoothManagerCallback bluetoothManagerCallback : this.mCallbacks) {
            try {
                bluetoothManagerCallback.onDiscover(list);
            } catch (Exception e) {
                this.mCallbacks.remove(bluetoothManagerCallback);
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnConnect(final BluetoothDevice bluetoothDevice) throws IllegalStateException {
        synchronized (this.mSync) {
            if (isReleased()) {
                return;
            }
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.bluetooth.-$$Lambda$BluetoothManager$szEVXQv64iZS7lvZIvIx-YwetxU
                    @Override // java.lang.Runnable
                    public final void run() {
                        BluetoothManager.this.lambda$callOnConnect$1$BluetoothManager(bluetoothDevice);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$callOnConnect$1$BluetoothManager(BluetoothDevice bluetoothDevice) {
        for (BluetoothManagerCallback bluetoothManagerCallback : this.mCallbacks) {
            try {
                bluetoothManagerCallback.onConnect(bluetoothDevice.getName(), bluetoothDevice.getAddress());
            } catch (Exception e) {
                this.mCallbacks.remove(bluetoothManagerCallback);
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnDisConnect() {
        synchronized (this.mSync) {
            if (isReleased()) {
                return;
            }
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.bluetooth.-$$Lambda$BluetoothManager$FBgsvbmziOwyzz08q67BJF_cCOo
                    @Override // java.lang.Runnable
                    public final void run() {
                        BluetoothManager.this.lambda$callOnDisConnect$2$BluetoothManager();
                    }
                });
            }
            if (isReleased()) {
                return;
            }
            internalStartListen();
        }
    }

    public /* synthetic */ void lambda$callOnDisConnect$2$BluetoothManager() {
        for (BluetoothManagerCallback bluetoothManagerCallback : this.mCallbacks) {
            try {
                bluetoothManagerCallback.onDisconnect();
            } catch (Exception e) {
                this.mCallbacks.remove(bluetoothManagerCallback);
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnFailed() {
        synchronized (this.mSync) {
            if (isReleased()) {
                return;
            }
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.bluetooth.-$$Lambda$BluetoothManager$SPq2NTSgCJsXSumNRxJqJjvtdaI
                    @Override // java.lang.Runnable
                    public final void run() {
                        BluetoothManager.this.lambda$callOnFailed$3$BluetoothManager();
                    }
                });
            }
            if (isReleased()) {
                return;
            }
            internalStartListen();
        }
    }

    public /* synthetic */ void lambda$callOnFailed$3$BluetoothManager() {
        for (BluetoothManagerCallback bluetoothManagerCallback : this.mCallbacks) {
            try {
                bluetoothManagerCallback.onFailed();
            } catch (Exception e) {
                this.mCallbacks.remove(bluetoothManagerCallback);
                Log.w(TAG, e);
            }
        }
    }

    protected void callOnReceive(byte[] bArr, final int i) {
        final byte[] bArr2 = new byte[i];
        System.arraycopy(bArr, 0, bArr2, 0, i);
        synchronized (this.mSync) {
            if (isReleased()) {
                return;
            }
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.bluetooth.-$$Lambda$BluetoothManager$zAxwzMqMSIKFOq5eTMY0WOToGls
                    @Override // java.lang.Runnable
                    public final void run() {
                        BluetoothManager.this.lambda$callOnReceive$4$BluetoothManager(bArr2, i);
                    }
                });
            }
        }
    }

    public /* synthetic */ void lambda$callOnReceive$4$BluetoothManager(byte[] bArr, int i) {
        for (BluetoothManagerCallback bluetoothManagerCallback : this.mCallbacks) {
            try {
                bluetoothManagerCallback.onReceive(bArr, i);
            } catch (Exception e) {
                this.mCallbacks.remove(bluetoothManagerCallback);
                Log.w(TAG, e);
            }
        }
    }

    private void setState(int i) {
        synchronized (this.mSync) {
            if (this.mState != -1) {
                this.mState = i;
            }
        }
    }

    protected void onConnect(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice) {
        synchronized (this.mSync) {
            internalCancel(3, true);
            ReceiverThread receiverThread = new ReceiverThread(bluetoothSocket);
            this.mReceiverThread = receiverThread;
            receiverThread.start();
            callOnConnect(bluetoothDevice);
        }
    }

    private static abstract class BluetoothSocketThread extends Thread {
        protected volatile boolean mIsCanceled;
        protected final BluetoothSocket mmSocket;

        public BluetoothSocketThread(String str, BluetoothSocket bluetoothSocket) {
            super(str);
            this.mmSocket = bluetoothSocket;
        }

        public void cancel() {
            this.mIsCanceled = true;
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothManager.TAG, "failed to call BluetoothSocket#close", e);
            }
        }
    }

    private class ReceiverThread extends BluetoothSocketThread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ReceiverThread(BluetoothSocket bluetoothSocket) {
            super("ReceiverThread:" + BluetoothManager.this.mName, bluetoothSocket);
            InputStream inputStream;
            OutputStream outputStream = null;
            try {
                inputStream = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e = e;
                inputStream = null;
            }
            try {
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e2) {
                e = e2;
                Log.e(BluetoothManager.TAG, "temp sockets not created", e);
                this.mmInStream = inputStream;
                this.mmOutStream = outputStream;
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] bArr = new byte[1024];
            while (BluetoothManager.this.mState == 3) {
                try {
                    int read = this.mmInStream.read(bArr);
                    if (read > 0) {
                        BluetoothManager.this.callOnReceive(bArr, read);
                    }
                } catch (IOException e) {
                    if (!this.mIsCanceled) {
                        Log.d(BluetoothManager.TAG, "disconnected", e);
                    }
                    BluetoothManager.this.callOnDisConnect();
                    return;
                }
            }
        }

        public void write(byte[] bArr) throws IllegalStateException {
            if (BluetoothManager.this.mState != 3) {
                throw new IllegalStateException("already disconnected");
            }
            try {
                this.mmOutStream.write(bArr);
            } catch (IOException e) {
                if (!this.mIsCanceled) {
                    throw new IllegalStateException(e);
                }
            }
        }

        public void write(byte[] bArr, int i, int i2) throws IllegalStateException {
            if (BluetoothManager.this.mState != 3) {
                throw new IllegalStateException("already disconnected");
            }
            try {
                this.mmOutStream.write(bArr, i, i2);
            } catch (IOException e) {
                if (!this.mIsCanceled) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    private class ListeningThread extends Thread {
        private volatile boolean mIsCanceled;
        private final BluetoothServerSocket mmServerSocket;

        public ListeningThread(boolean z) {
            super("ListeningThread:" + BluetoothManager.this.mName);
            BluetoothServerSocket bluetoothServerSocket;
            try {
                bluetoothServerSocket = z ? BluetoothManager.this.mAdapter.listenUsingRfcommWithServiceRecord(BluetoothManager.this.mName, BluetoothManager.this.mSecureProfileUUID) : BluetoothManager.this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(BluetoothManager.this.mName, BluetoothManager.this.mInSecureProfileUUID);
            } catch (IOException e) {
                Log.w(BluetoothManager.TAG, e);
                bluetoothServerSocket = null;
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (BluetoothManager.this.mState != 3) {
                try {
                    BluetoothSocket accept = this.mmServerSocket.accept();
                    if (accept != null) {
                        int i = BluetoothManager.this.mState;
                        if (i != 0) {
                            if (i == 1 || i == 2) {
                                BluetoothManager.this.onConnect(accept, accept.getRemoteDevice());
                                return;
                            } else if (i != 3) {
                            }
                        }
                        try {
                            accept.close();
                        } catch (IOException e) {
                            Log.w(BluetoothManager.TAG, "Could not close unwanted socket", e);
                        }
                    }
                } catch (IOException e2) {
                    if (this.mIsCanceled) {
                        return;
                    }
                    Log.d(BluetoothManager.TAG, e2.getMessage());
                    return;
                }
            }
        }

        public void cancel() {
            this.mIsCanceled = true;
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothManager.TAG, "close() of server failed", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BluetoothSocket createBluetoothSocket(BluetoothDevice bluetoothDevice, boolean z) throws IOException {
        if (z) {
            return bluetoothDevice.createRfcommSocketToServiceRecord(this.mSecureProfileUUID);
        }
        return bluetoothDevice.createInsecureRfcommSocketToServiceRecord(this.mInSecureProfileUUID);
    }

    private class ConnectingThread extends BluetoothSocketThread {
        private final BluetoothDevice mmDevice;

        public ConnectingThread(BluetoothDevice bluetoothDevice, boolean z) throws IOException {
            super("ConnectingThread:" + BluetoothManager.this.mName, BluetoothManager.this.createBluetoothSocket(bluetoothDevice, z));
            this.mmDevice = bluetoothDevice;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (BluetoothManager.this.mAdapter.isDiscovering()) {
                BluetoothManager.this.mAdapter.cancelDiscovery();
            }
            try {
                this.mmSocket.connect();
                synchronized (BluetoothManager.this.mSync) {
                    BluetoothManager.this.mConnectingThread = null;
                }
                BluetoothManager.this.onConnect(this.mmSocket, this.mmDevice);
            } catch (IOException e) {
                Log.w(BluetoothManager.TAG, e);
                try {
                    this.mmSocket.close();
                } catch (IOException e2) {
                    if (!this.mIsCanceled) {
                        Log.w(BluetoothManager.TAG, "failed to close socket", e2);
                    }
                }
                BluetoothManager.this.callOnFailed();
            }
        }
    }
}
