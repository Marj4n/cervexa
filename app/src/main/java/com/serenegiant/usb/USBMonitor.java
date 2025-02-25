package com.serenegiant.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.HandlerThreadHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.UByte;

/* loaded from: classes2.dex */
public final class USBMonitor {
    public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static final String ACTION_USB_PERMISSION_BASE = "com.serenegiant.USB_PERMISSION.";
    private static final boolean DEBUG = false;
    private static final String TAG = "USBMonitor";
    public static final String USBSTATECHANGEACTIION = "android.hardware.usb.action.USB_STATE";
    private static final int USB_DIR_IN = 128;
    private static final int USB_DIR_OUT = 0;
    private static final int USB_DT_BOS = 15;
    private static final int USB_DT_CONFIG = 2;
    private static final int USB_DT_CS_CONFIG = 34;
    private static final int USB_DT_CS_DEVICE = 33;
    private static final int USB_DT_CS_ENDPOINT = 37;
    private static final int USB_DT_CS_INTERFACE = 36;
    private static final int USB_DT_CS_RADIO_CONTROL = 35;
    private static final int USB_DT_CS_STRING = 35;
    private static final int USB_DT_DEBUG = 10;
    private static final int USB_DT_DEVICE = 1;
    private static final int USB_DT_DEVICE_CAPABILITY = 16;
    private static final int USB_DT_DEVICE_QUALIFIER = 6;
    private static final int USB_DT_DEVICE_SIZE = 18;
    private static final int USB_DT_ENCRYPTION_TYPE = 14;
    private static final int USB_DT_ENDPOINT = 5;
    private static final int USB_DT_INTERFACE = 4;
    private static final int USB_DT_INTERFACE_ASSOCIATION = 11;
    private static final int USB_DT_INTERFACE_POWER = 8;
    private static final int USB_DT_KEY = 13;
    private static final int USB_DT_OTG = 9;
    private static final int USB_DT_OTHER_SPEED_CONFIG = 7;
    private static final int USB_DT_PIPE_USAGE = 36;
    private static final int USB_DT_RPIPE = 34;
    private static final int USB_DT_SECURITY = 12;
    private static final int USB_DT_SS_ENDPOINT_COMP = 48;
    private static final int USB_DT_STRING = 3;
    private static final int USB_DT_WIRELESS_ENDPOINT_COMP = 17;
    private static final int USB_DT_WIRE_ADAPTER = 33;
    private static final int USB_RECIP_DEVICE = 0;
    private static final int USB_RECIP_ENDPOINT = 2;
    private static final int USB_RECIP_INTERFACE = 1;
    private static final int USB_RECIP_MASK = 31;
    private static final int USB_RECIP_OTHER = 3;
    private static final int USB_RECIP_PORT = 4;
    private static final int USB_RECIP_RPIPE = 5;
    private static final int USB_REQ_CLEAR_FEATURE = 1;
    private static final int USB_REQ_CS_DEVICE_GET = 160;
    private static final int USB_REQ_CS_DEVICE_SET = 32;
    private static final int USB_REQ_CS_ENDPOINT_GET = 162;
    private static final int USB_REQ_CS_ENDPOINT_SET = 34;
    private static final int USB_REQ_CS_INTERFACE_GET = 161;
    private static final int USB_REQ_CS_INTERFACE_SET = 33;
    private static final int USB_REQ_GET_CONFIGURATION = 8;
    private static final int USB_REQ_GET_DESCRIPTOR = 6;
    private static final int USB_REQ_GET_ENCRYPTION = 14;
    private static final int USB_REQ_GET_HANDSHAKE = 16;
    private static final int USB_REQ_GET_INTERFACE = 10;
    private static final int USB_REQ_GET_SECURITY_DATA = 19;
    private static final int USB_REQ_GET_STATUS = 0;
    private static final int USB_REQ_LOOPBACK_DATA_READ = 22;
    private static final int USB_REQ_LOOPBACK_DATA_WRITE = 21;
    private static final int USB_REQ_RPIPE_ABORT = 14;
    private static final int USB_REQ_RPIPE_RESET = 15;
    private static final int USB_REQ_SET_ADDRESS = 5;
    private static final int USB_REQ_SET_CONFIGURATION = 9;
    private static final int USB_REQ_SET_CONNECTION = 17;
    private static final int USB_REQ_SET_DESCRIPTOR = 7;
    private static final int USB_REQ_SET_ENCRYPTION = 13;
    private static final int USB_REQ_SET_FEATURE = 3;
    private static final int USB_REQ_SET_HANDSHAKE = 15;
    private static final int USB_REQ_SET_INTERFACE = 11;
    private static final int USB_REQ_SET_INTERFACE_DS = 23;
    private static final int USB_REQ_SET_ISOCH_DELAY = 49;
    private static final int USB_REQ_SET_SECURITY_DATA = 18;
    private static final int USB_REQ_SET_SEL = 48;
    private static final int USB_REQ_SET_WUSB_DATA = 20;
    private static final int USB_REQ_STANDARD_DEVICE_GET = 128;
    private static final int USB_REQ_STANDARD_DEVICE_SET = 0;
    private static final int USB_REQ_STANDARD_ENDPOINT_GET = 130;
    private static final int USB_REQ_STANDARD_ENDPOINT_SET = 2;
    private static final int USB_REQ_STANDARD_INTERFACE_GET = 129;
    private static final int USB_REQ_STANDARD_INTERFACE_SET = 1;
    private static final int USB_REQ_SYNCH_FRAME = 12;
    private static final int USB_REQ_VENDER_DEVICE_GET = 160;
    private static final int USB_REQ_VENDER_DEVICE_SET = 32;
    private static final int USB_REQ_VENDER_ENDPOINT_GET = 162;
    private static final int USB_REQ_VENDER_ENDPOINT_SET = 34;
    private static final int USB_REQ_VENDER_INTERFACE_GET = 161;
    private static final int USB_REQ_VENDER_INTERFACE_SET = 33;
    private static final int USB_TYPE_CLASS = 32;
    private static final int USB_TYPE_MASK = 96;
    private static final int USB_TYPE_RESERVED = 96;
    private static final int USB_TYPE_STANDARD = 0;
    private static final int USB_TYPE_VENDOR = 64;
    private volatile boolean destroyed;
    private final Handler mAsyncHandler;
    private final OnDeviceConnectListener mOnDeviceConnectListener;
    private final UsbManager mUsbManager;
    private final WeakReference<Context> mWeakContext;
    private final String ACTION_USB_PERMISSION = ACTION_USB_PERMISSION_BASE + hashCode();
    private final ConcurrentHashMap<UsbDevice, UsbControlBlock> mCtrlBlocks = new ConcurrentHashMap<>();
    private final SparseArray<WeakReference<UsbDevice>> mHasPermissions = new SparseArray<>();
    private PendingIntent mPermissionIntent = null;
    private List<DeviceFilter> mDeviceFilters = new ArrayList();
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() { // from class: com.serenegiant.usb.USBMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            UsbDevice usbDevice;
            if (USBMonitor.this.destroyed) {
                return;
            }
            String action = intent.getAction();
            if (USBMonitor.this.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (USBMonitor.this) {
                    UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false)) {
                        USBMonitor.this.processCancel(usbDevice2);
                    } else if (usbDevice2 != null) {
                        USBMonitor.this.processConnect(usbDevice2);
                    }
                }
                return;
            }
            if (USBMonitor.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbDevice usbDevice3 = (UsbDevice) intent.getParcelableExtra("device");
                USBMonitor uSBMonitor = USBMonitor.this;
                uSBMonitor.updatePermission(usbDevice3, uSBMonitor.hasPermission(usbDevice3));
                USBMonitor.this.processAttach(usbDevice3);
                return;
            }
            if (!"android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action) || (usbDevice = (UsbDevice) intent.getParcelableExtra("device")) == null) {
                return;
            }
            UsbControlBlock usbControlBlock = (UsbControlBlock) USBMonitor.this.mCtrlBlocks.remove(usbDevice);
            if (usbControlBlock != null) {
                usbControlBlock.close();
            }
            USBMonitor.this.mDeviceCounts = 0;
            USBMonitor.this.processDettach(usbDevice);
        }
    };
    private volatile int mDeviceCounts = 0;
    private final Runnable mDeviceCheckRunnable = new Runnable() { // from class: com.serenegiant.usb.USBMonitor.2
        @Override // java.lang.Runnable
        public void run() {
            int size;
            int size2;
            if (USBMonitor.this.destroyed) {
                return;
            }
            List<UsbDevice> deviceList = USBMonitor.this.getDeviceList();
            int size3 = deviceList.size();
            synchronized (USBMonitor.this.mHasPermissions) {
                size = USBMonitor.this.mHasPermissions.size();
                USBMonitor.this.mHasPermissions.clear();
                Iterator<UsbDevice> it = deviceList.iterator();
                while (it.hasNext()) {
                    USBMonitor.this.hasPermission(it.next());
                }
                size2 = USBMonitor.this.mHasPermissions.size();
            }
            if (size3 > USBMonitor.this.mDeviceCounts || size2 > size) {
                USBMonitor.this.mDeviceCounts = size3;
                if (USBMonitor.this.mOnDeviceConnectListener != null) {
                    for (int i = 0; i < size3; i++) {
                        final UsbDevice usbDevice = deviceList.get(i);
                        USBMonitor.this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.usb.USBMonitor.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                USBMonitor.this.mOnDeviceConnectListener.onAttach(usbDevice);
                            }
                        });
                    }
                }
            }
            USBMonitor.this.mAsyncHandler.postDelayed(this, 2000L);
        }
    };

    public interface OnDeviceConnectListener {
        void onAttach(UsbDevice usbDevice);

        void onCancel(UsbDevice usbDevice);

        void onConnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock, boolean z);

        void onDettach(UsbDevice usbDevice);

        void onDisconnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock);
    }

    public USBMonitor(Context context, OnDeviceConnectListener onDeviceConnectListener) {
        if (onDeviceConnectListener == null) {
            throw new IllegalArgumentException("OnDeviceConnectListener should not null.");
        }
        this.mWeakContext = new WeakReference<>(context);
        this.mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.mOnDeviceConnectListener = onDeviceConnectListener;
        this.mAsyncHandler = HandlerThreadHandler.createHandler(TAG);
        this.destroyed = false;
    }

    public void destroy() {
        unregister();
        if (this.destroyed) {
            return;
        }
        this.destroyed = true;
        Set<UsbDevice> keySet = this.mCtrlBlocks.keySet();
        if (keySet != null) {
            try {
                Iterator<UsbDevice> it = keySet.iterator();
                while (it.hasNext()) {
                    UsbControlBlock remove = this.mCtrlBlocks.remove(it.next());
                    if (remove != null) {
                        remove.close();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "destroy:", e);
            }
        }
        this.mCtrlBlocks.clear();
        try {
            this.mAsyncHandler.getLooper().quit();
        } catch (Exception e2) {
            Log.e(TAG, "destroy:", e2);
        }
    }

    public synchronized void register() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        if (this.mPermissionIntent == null) {
            Context context = this.mWeakContext.get();
            if (context != null) {
                this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(this.ACTION_USB_PERMISSION), 0);
                IntentFilter intentFilter = new IntentFilter(this.ACTION_USB_PERMISSION);
                intentFilter.addAction(ACTION_USB_DEVICE_ATTACHED);
                intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                intentFilter.addAction(USBSTATECHANGEACTIION);
                context.registerReceiver(this.mUsbReceiver, intentFilter);
            }
            this.mDeviceCounts = 0;
            this.mAsyncHandler.postDelayed(this.mDeviceCheckRunnable, 1000L);
        }
    }

    public synchronized void unregister() throws IllegalStateException {
        this.mDeviceCounts = 0;
        if (!this.destroyed) {
            this.mAsyncHandler.removeCallbacks(this.mDeviceCheckRunnable);
        }
        if (this.mPermissionIntent != null) {
            Context context = this.mWeakContext.get();
            if (context != null) {
                try {
                    context.unregisterReceiver(this.mUsbReceiver);
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
            this.mPermissionIntent = null;
        }
    }

    public synchronized boolean isRegistered() {
        boolean z = false;
        if (!this.destroyed) {
            z = this.mPermissionIntent != null;
        }
        return z;
    }

    public void setDeviceFilter(DeviceFilter deviceFilter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.add(deviceFilter);
    }

    public void addDeviceFilter(DeviceFilter deviceFilter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.add(deviceFilter);
    }

    public void removeDeviceFilter(DeviceFilter deviceFilter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.remove(deviceFilter);
    }

    public void setDeviceFilter(List<DeviceFilter> list) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.clear();
        this.mDeviceFilters.addAll(list);
    }

    public void addDeviceFilter(List<DeviceFilter> list) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.addAll(list);
    }

    public void removeDeviceFilter(List<DeviceFilter> list) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mDeviceFilters.removeAll(list);
    }

    public int getDeviceCount() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return getDeviceList().size();
    }

    public List<UsbDevice> getDeviceList() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return getDeviceList(this.mDeviceFilters);
    }

    public List<UsbDevice> getDeviceList(List<DeviceFilter> list) throws IllegalStateException {
        FileWriter fileWriter;
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/USBCamera/failed_devices.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e2) {
            e2.printStackTrace();
            fileWriter = null;
        }
        PrintWriter printWriter = fileWriter != null ? new PrintWriter(fileWriter) : null;
        ArrayList arrayList = new ArrayList();
        if (deviceList != null) {
            if (list == null || list.isEmpty()) {
                arrayList.addAll(deviceList.values());
            } else {
                for (UsbDevice usbDevice : deviceList.values()) {
                    for (DeviceFilter deviceFilter : list) {
                        if ((deviceFilter != null && deviceFilter.matches(usbDevice)) || (deviceFilter != null && deviceFilter.mSubclass == usbDevice.getDeviceSubclass())) {
                            if (!deviceFilter.isExclude) {
                                arrayList.add(usbDevice);
                            }
                        } else {
                            String str = Build.MODEL;
                            String str2 = Build.VERSION.RELEASE;
                            String valueOf = String.valueOf(usbDevice.getDeviceClass());
                            String valueOf2 = String.valueOf(usbDevice.getDeviceSubclass());
                            if (printWriter != null) {
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str);
                                    sb.append("/");
                                    sb.append(str2);
                                    sb.append(":");
                                    sb.append("class=" + valueOf + ", subclass=" + valueOf2);
                                    printWriter.println(sb.toString());
                                    printWriter.flush();
                                    fileWriter.flush();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        if (printWriter != null) {
            printWriter.close();
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        return arrayList;
    }

    public List<UsbDevice> getDeviceList(DeviceFilter deviceFilter) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        ArrayList arrayList = new ArrayList();
        if (deviceList != null) {
            for (UsbDevice usbDevice : deviceList.values()) {
                if (deviceFilter == null || (deviceFilter.matches(usbDevice) && !deviceFilter.isExclude)) {
                    arrayList.add(usbDevice);
                }
            }
        }
        return arrayList;
    }

    public Iterator<UsbDevice> getDevices() throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        if (deviceList != null) {
            return deviceList.values().iterator();
        }
        return null;
    }

    public final void dumpDevices() {
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        if (deviceList != null) {
            Set<String> keySet = deviceList.keySet();
            if (keySet != null && keySet.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (String str : keySet) {
                    UsbDevice usbDevice = deviceList.get(str);
                    int interfaceCount = usbDevice != null ? usbDevice.getInterfaceCount() : 0;
                    sb.setLength(0);
                    for (int i = 0; i < interfaceCount; i++) {
                        sb.append(String.format(Locale.US, "interface%d:%s", Integer.valueOf(i), usbDevice.getInterface(i).toString()));
                    }
                    Log.i(TAG, "key=" + str + ":" + usbDevice + ":" + sb.toString());
                }
                return;
            }
            Log.i(TAG, "no device");
            return;
        }
        Log.i(TAG, "no device");
    }

    public final boolean hasPermission(UsbDevice usbDevice) throws IllegalStateException {
        if (this.destroyed) {
            throw new IllegalStateException("already destroyed");
        }
        return updatePermission(usbDevice, usbDevice != null && this.mUsbManager.hasPermission(usbDevice));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updatePermission(UsbDevice usbDevice, boolean z) {
        int deviceKey = getDeviceKey(usbDevice, true);
        synchronized (this.mHasPermissions) {
            if (z) {
                if (this.mHasPermissions.get(deviceKey) == null) {
                    this.mHasPermissions.put(deviceKey, new WeakReference<>(usbDevice));
                }
            } else {
                this.mHasPermissions.remove(deviceKey);
            }
        }
        return z;
    }

    public synchronized boolean requestPermission(UsbDevice usbDevice) {
        boolean z;
        z = false;
        if (!isRegistered()) {
            processCancel(usbDevice);
        } else if (usbDevice != null) {
            if (this.mUsbManager.hasPermission(usbDevice)) {
                processConnect(usbDevice);
            } else {
                try {
                    this.mUsbManager.requestPermission(usbDevice, this.mPermissionIntent);
                } catch (Exception e) {
                    Log.w(TAG, e);
                    processCancel(usbDevice);
                }
            }
        } else {
            processCancel(usbDevice);
        }
        z = true;
        return z;
    }

    public UsbControlBlock openDevice(UsbDevice usbDevice) throws SecurityException {
        if (hasPermission(usbDevice)) {
            UsbControlBlock usbControlBlock = this.mCtrlBlocks.get(usbDevice);
            if (usbControlBlock != null) {
                return usbControlBlock;
            }
            UsbControlBlock usbControlBlock2 = new UsbControlBlock(this, usbDevice);
            this.mCtrlBlocks.put(usbDevice, usbControlBlock2);
            return usbControlBlock2;
        }
        throw new SecurityException("has no permission");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processConnect(final UsbDevice usbDevice) {
        if (this.destroyed) {
            return;
        }
        updatePermission(usbDevice, true);
        this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.usb.USBMonitor.3
            @Override // java.lang.Runnable
            public void run() {
                boolean z;
                UsbControlBlock usbControlBlock = (UsbControlBlock) USBMonitor.this.mCtrlBlocks.get(usbDevice);
                if (usbControlBlock == null) {
                    usbControlBlock = new UsbControlBlock(USBMonitor.this, usbDevice);
                    USBMonitor.this.mCtrlBlocks.put(usbDevice, usbControlBlock);
                    z = true;
                } else {
                    z = false;
                }
                if (USBMonitor.this.mOnDeviceConnectListener != null) {
                    USBMonitor.this.mOnDeviceConnectListener.onConnect(usbDevice, usbControlBlock, z);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processCancel(final UsbDevice usbDevice) {
        if (this.destroyed) {
            return;
        }
        updatePermission(usbDevice, false);
        if (this.mOnDeviceConnectListener != null) {
            this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.usb.USBMonitor.4
                @Override // java.lang.Runnable
                public void run() {
                    USBMonitor.this.mOnDeviceConnectListener.onCancel(usbDevice);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processAttach(final UsbDevice usbDevice) {
        if (this.destroyed || this.mOnDeviceConnectListener == null) {
            return;
        }
        this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.usb.USBMonitor.5
            @Override // java.lang.Runnable
            public void run() {
                USBMonitor.this.mOnDeviceConnectListener.onAttach(usbDevice);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processDettach(final UsbDevice usbDevice) {
        if (this.destroyed || this.mOnDeviceConnectListener == null) {
            return;
        }
        this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.usb.USBMonitor.6
            @Override // java.lang.Runnable
            public void run() {
                USBMonitor.this.mOnDeviceConnectListener.onDettach(usbDevice);
            }
        });
    }

    public static final String getDeviceKeyName(UsbDevice usbDevice) {
        return getDeviceKeyName(usbDevice, null, false);
    }

    public static final String getDeviceKeyName(UsbDevice usbDevice, boolean z) {
        return getDeviceKeyName(usbDevice, null, z);
    }

    public static final String getDeviceKeyName(UsbDevice usbDevice, String str, boolean z) {
        if (usbDevice == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(usbDevice.getVendorId());
        sb.append("#");
        sb.append(usbDevice.getProductId());
        sb.append("#");
        sb.append(usbDevice.getDeviceClass());
        sb.append("#");
        sb.append(usbDevice.getDeviceSubclass());
        sb.append("#");
        sb.append(usbDevice.getDeviceProtocol());
        if (!TextUtils.isEmpty(str)) {
            sb.append("#");
            sb.append(str);
        }
        if (z && BuildCheck.isAndroid5()) {
            sb.append("#");
            if (TextUtils.isEmpty(str)) {
                sb.append("#");
            }
            sb.append(usbDevice.getManufacturerName());
            sb.append("#");
            sb.append(usbDevice.getConfigurationCount());
            sb.append("#");
            if (BuildCheck.isMarshmallow()) {
                sb.append(usbDevice.getVersion());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    public static final int getDeviceKey(UsbDevice usbDevice) {
        if (usbDevice != null) {
            return getDeviceKeyName(usbDevice, null, false).hashCode();
        }
        return 0;
    }

    public static final int getDeviceKey(UsbDevice usbDevice, boolean z) {
        if (usbDevice != null) {
            return getDeviceKeyName(usbDevice, null, z).hashCode();
        }
        return 0;
    }

    public static final int getDeviceKey(UsbDevice usbDevice, String str, boolean z) {
        if (usbDevice != null) {
            return getDeviceKeyName(usbDevice, str, z).hashCode();
        }
        return 0;
    }

    public static class UsbDeviceInfo {
        public String manufacturer;
        public String product;
        public String serial;
        public String usb_version;
        public String version;

        /* JADX INFO: Access modifiers changed from: private */
        public void clear() {
            this.serial = null;
            this.version = null;
            this.product = null;
            this.manufacturer = null;
            this.usb_version = null;
        }

        public String toString() {
            Object[] objArr = new Object[5];
            String str = this.usb_version;
            if (str == null) {
                str = "";
            }
            objArr[0] = str;
            String str2 = this.manufacturer;
            if (str2 == null) {
                str2 = "";
            }
            objArr[1] = str2;
            String str3 = this.product;
            if (str3 == null) {
                str3 = "";
            }
            objArr[2] = str3;
            String str4 = this.version;
            if (str4 == null) {
                str4 = "";
            }
            objArr[3] = str4;
            String str5 = this.serial;
            objArr[4] = str5 != null ? str5 : "";
            return String.format("UsbDevice:usb_version=%s,manufacturer=%s,product=%s,version=%s,serial=%s", objArr);
        }
    }

    private static String getString(UsbDeviceConnection usbDeviceConnection, int i, int i2, byte[] bArr) {
        byte[] bArr2 = new byte[256];
        String str = null;
        for (int i3 = 1; i3 <= i2; i3++) {
            int controlTransfer = usbDeviceConnection.controlTransfer(128, 6, i | 768, bArr[i3], bArr2, 256, 0);
            if (controlTransfer > 2 && bArr2[0] == controlTransfer && bArr2[1] == 3) {
                try {
                    String str2 = new String(bArr2, 2, controlTransfer - 2, "UTF-16LE");
                    if (!"Љ".equals(str2)) {
                        return str2;
                    }
                    str = null;
                } catch (UnsupportedEncodingException unused2) {
                    continue;
                }
            }
        }
        return str;
    }

    public UsbDeviceInfo getDeviceInfo(UsbDevice usbDevice) {
        return updateDeviceInfo(this.mUsbManager, usbDevice, null);
    }

    public static UsbDeviceInfo getDeviceInfo(Context context, UsbDevice usbDevice) {
        return updateDeviceInfo((UsbManager) context.getSystemService(Context.USB_SERVICE), usbDevice, new UsbDeviceInfo());
    }

    public static UsbDeviceInfo updateDeviceInfo(UsbManager usbManager, UsbDevice usbDevice, UsbDeviceInfo usbDeviceInfo) {
        if (usbDeviceInfo == null) {
            usbDeviceInfo = new UsbDeviceInfo();
        }
        usbDeviceInfo.clear();
        if (usbDevice != null) {
            if (BuildCheck.isLollipop()) {
                usbDeviceInfo.manufacturer = usbDevice.getManufacturerName();
                usbDeviceInfo.product = usbDevice.getProductName();
                usbDeviceInfo.serial = usbDevice.getSerialNumber();
            }
            if (BuildCheck.isMarshmallow()) {
                usbDeviceInfo.usb_version = usbDevice.getVersion();
            }
            if (usbManager != null && usbManager.hasPermission(usbDevice)) {
                UsbDeviceConnection openDevice = usbManager.openDevice(usbDevice);
                if (openDevice == null) {
                    return null;
                }
                byte[] rawDescriptors = openDevice.getRawDescriptors();
                if (TextUtils.isEmpty(usbDeviceInfo.usb_version)) {
                    usbDeviceInfo.usb_version = String.format("%x.%02x", Integer.valueOf(rawDescriptors[3] & UByte.MAX_VALUE), Integer.valueOf(rawDescriptors[2] & UByte.MAX_VALUE));
                }
                if (TextUtils.isEmpty(usbDeviceInfo.version)) {
                    usbDeviceInfo.version = String.format("%x.%02x", Integer.valueOf(rawDescriptors[13] & UByte.MAX_VALUE), Integer.valueOf(rawDescriptors[12] & UByte.MAX_VALUE));
                }
                if (TextUtils.isEmpty(usbDeviceInfo.serial)) {
                    usbDeviceInfo.serial = openDevice.getSerial();
                }
                byte[] bArr = new byte[256];
                try {
                    int controlTransfer = openDevice.controlTransfer(128, 6, 768, 0, bArr, 256, 0);
                    int i = controlTransfer > 0 ? (controlTransfer - 2) / 2 : 0;
                    if (i > 0) {
                        if (TextUtils.isEmpty(usbDeviceInfo.manufacturer)) {
                            usbDeviceInfo.manufacturer = getString(openDevice, rawDescriptors[14], i, bArr);
                        }
                        if (TextUtils.isEmpty(usbDeviceInfo.product)) {
                            usbDeviceInfo.product = getString(openDevice, rawDescriptors[15], i, bArr);
                        }
                        if (TextUtils.isEmpty(usbDeviceInfo.serial)) {
                            usbDeviceInfo.serial = getString(openDevice, rawDescriptors[16], i, bArr);
                        }
                    }
                } finally {
                    openDevice.close();
                }
            }
            if (TextUtils.isEmpty(usbDeviceInfo.manufacturer)) {
                usbDeviceInfo.manufacturer = USBVendorId.vendorName(usbDevice.getVendorId());
            }
            if (TextUtils.isEmpty(usbDeviceInfo.manufacturer)) {
                usbDeviceInfo.manufacturer = String.format("%04x", Integer.valueOf(usbDevice.getVendorId()));
            }
            if (TextUtils.isEmpty(usbDeviceInfo.product)) {
                usbDeviceInfo.product = String.format("%04x", Integer.valueOf(usbDevice.getProductId()));
            }
        }
        return usbDeviceInfo;
    }

    public static final class UsbControlBlock implements Cloneable {
        private final int mBusNum;
        protected UsbDeviceConnection mConnection;
        private final int mDevNum;
        protected final UsbDeviceInfo mInfo;
        private final SparseArray<SparseArray<UsbInterface>> mInterfaces;
        private final WeakReference<UsbDevice> mWeakDevice;
        private final WeakReference<USBMonitor> mWeakMonitor;

        private UsbControlBlock(USBMonitor uSBMonitor, UsbDevice usbDevice) {
            int i;
            int i2;
            this.mInterfaces = new SparseArray<>();
            this.mWeakMonitor = new WeakReference<>(uSBMonitor);
            this.mWeakDevice = new WeakReference<>(usbDevice);
            this.mConnection = uSBMonitor.mUsbManager.openDevice(usbDevice);
            this.mInfo = USBMonitor.updateDeviceInfo(uSBMonitor.mUsbManager, usbDevice, null);
            String deviceName = usbDevice.getDeviceName();
            String[] split = TextUtils.isEmpty(deviceName) ? null : deviceName.split("/");
            if (split != null) {
                i2 = Integer.parseInt(split[split.length - 2]);
                i = Integer.parseInt(split[split.length - 1]);
            } else {
                i = 0;
                i2 = 0;
            }
            this.mBusNum = i2;
            this.mDevNum = i;
            UsbDeviceConnection usbDeviceConnection = this.mConnection;
            if (usbDeviceConnection != null) {
                int fileDescriptor = usbDeviceConnection.getFileDescriptor();
                Log.i(USBMonitor.TAG, String.format(Locale.US, "name=%s,desc=%d,busnum=%d,devnum=%d,rawDesc=", deviceName, Integer.valueOf(fileDescriptor), Integer.valueOf(i2), Integer.valueOf(i)) + this.mConnection.getRawDescriptors());
                return;
            }
            Log.e(USBMonitor.TAG, "could not connect to device " + deviceName);
        }

        private UsbControlBlock(UsbControlBlock usbControlBlock) throws IllegalStateException {
            this.mInterfaces = new SparseArray<>();
            USBMonitor uSBMonitor = usbControlBlock.getUSBMonitor();
            UsbDevice device = usbControlBlock.getDevice();
            if (device != null) {
                UsbDeviceConnection openDevice = uSBMonitor.mUsbManager.openDevice(device);
                this.mConnection = openDevice;
                if (openDevice != null) {
                    this.mInfo = USBMonitor.updateDeviceInfo(uSBMonitor.mUsbManager, device, null);
                    this.mWeakMonitor = new WeakReference<>(uSBMonitor);
                    this.mWeakDevice = new WeakReference<>(device);
                    this.mBusNum = usbControlBlock.mBusNum;
                    this.mDevNum = usbControlBlock.mDevNum;
                    return;
                }
                throw new IllegalStateException("device may already be removed or have no permission");
            }
            throw new IllegalStateException("device may already be removed");
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public UsbControlBlock m16clone() throws CloneNotSupportedException {
            try {
                return new UsbControlBlock(this);
            } catch (IllegalStateException e) {
                throw new CloneNotSupportedException(e.getMessage());
            }
        }

        public USBMonitor getUSBMonitor() {
            return this.mWeakMonitor.get();
        }

        public final UsbDevice getDevice() {
            return this.mWeakDevice.get();
        }

        public String getDeviceName() {
            UsbDevice usbDevice = this.mWeakDevice.get();
            return usbDevice != null ? usbDevice.getDeviceName() : "";
        }

        public int getDeviceId() {
            UsbDevice usbDevice = this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getDeviceId();
            }
            return 0;
        }

        public String getDeviceKeyName() {
            return USBMonitor.getDeviceKeyName(this.mWeakDevice.get());
        }

        public String getDeviceKeyName(boolean z) throws IllegalStateException {
            if (z) {
                checkConnection();
            }
            return USBMonitor.getDeviceKeyName(this.mWeakDevice.get(), this.mInfo.serial, z);
        }

        public int getDeviceKey() throws IllegalStateException {
            checkConnection();
            return USBMonitor.getDeviceKey(this.mWeakDevice.get());
        }

        public int getDeviceKey(boolean z) throws IllegalStateException {
            if (z) {
                checkConnection();
            }
            return USBMonitor.getDeviceKey(this.mWeakDevice.get(), this.mInfo.serial, z);
        }

        public String getDeviceKeyNameWithSerial() {
            return USBMonitor.getDeviceKeyName(this.mWeakDevice.get(), this.mInfo.serial, false);
        }

        public int getDeviceKeyWithSerial() {
            return getDeviceKeyNameWithSerial().hashCode();
        }

        public synchronized UsbDeviceConnection getConnection() {
            return this.mConnection;
        }

        public synchronized int getFileDescriptor() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getFileDescriptor();
        }

        public synchronized byte[] getRawDescriptors() throws IllegalStateException {
            checkConnection();
            return this.mConnection.getRawDescriptors();
        }

        public int getVenderId() {
            UsbDevice usbDevice = this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getVendorId();
            }
            return 0;
        }

        public int getProductId() {
            UsbDevice usbDevice = this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getProductId();
            }
            return 0;
        }

        public String getUsbVersion() {
            return this.mInfo.usb_version;
        }

        public String getManufacture() {
            return this.mInfo.manufacturer;
        }

        public String getProductName() {
            return this.mInfo.product;
        }

        public String getVersion() {
            return this.mInfo.version;
        }

        public String getSerial() {
            return this.mInfo.serial;
        }

        public int getBusNum() {
            return this.mBusNum;
        }

        public int getDevNum() {
            return this.mDevNum;
        }

        public synchronized UsbInterface getInterface(int i) throws IllegalStateException {
            return getInterface(i, 0);
        }

        public synchronized UsbInterface getInterface(int i, int i2) throws IllegalStateException {
            UsbInterface usbInterface;
            checkConnection();
            SparseArray<UsbInterface> sparseArray = this.mInterfaces.get(i);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                this.mInterfaces.put(i, sparseArray);
            }
            usbInterface = sparseArray.get(i2);
            if (usbInterface == null) {
                UsbDevice usbDevice = this.mWeakDevice.get();
                int interfaceCount = usbDevice.getInterfaceCount();
                int i3 = 0;
                while (true) {
                    if (i3 >= interfaceCount) {
                        break;
                    }
                    UsbInterface usbInterface2 = usbDevice.getInterface(i3);
                    if (usbInterface2.getId() == i && usbInterface2.getAlternateSetting() == i2) {
                        usbInterface = usbInterface2;
                        break;
                    }
                    i3++;
                }
                if (usbInterface != null) {
                    sparseArray.append(i2, usbInterface);
                }
            }
            return usbInterface;
        }

        public synchronized void claimInterface(UsbInterface usbInterface) {
            claimInterface(usbInterface, true);
        }

        public synchronized void claimInterface(UsbInterface usbInterface, boolean z) {
            checkConnection();
            this.mConnection.claimInterface(usbInterface, z);
        }

        public synchronized void releaseInterface(UsbInterface usbInterface) throws IllegalStateException {
            checkConnection();
            SparseArray<UsbInterface> sparseArray = this.mInterfaces.get(usbInterface.getId());
            if (sparseArray != null) {
                sparseArray.removeAt(sparseArray.indexOfValue(usbInterface));
                if (sparseArray.size() == 0) {
                    this.mInterfaces.remove(usbInterface.getId());
                }
            }
            this.mConnection.releaseInterface(usbInterface);
        }

        public synchronized void close() {
            if (this.mConnection != null) {
                int size = this.mInterfaces.size();
                for (int i = 0; i < size; i++) {
                    SparseArray<UsbInterface> valueAt = this.mInterfaces.valueAt(i);
                    if (valueAt != null) {
                        int size2 = valueAt.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            this.mConnection.releaseInterface(valueAt.valueAt(i2));
                        }
                        valueAt.clear();
                    }
                }
                this.mInterfaces.clear();
                this.mConnection.close();
                this.mConnection = null;
                USBMonitor uSBMonitor = this.mWeakMonitor.get();
                if (uSBMonitor != null) {
                    if (uSBMonitor.mOnDeviceConnectListener != null) {
                        uSBMonitor.mOnDeviceConnectListener.onDisconnect(this.mWeakDevice.get(), this);
                    }
                    uSBMonitor.mCtrlBlocks.remove(getDevice());
                }
            }
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof UsbControlBlock) {
                UsbDevice device = ((UsbControlBlock) obj).getDevice();
                if (device == null) {
                    return this.mWeakDevice.get() == null;
                }
                return device.equals(this.mWeakDevice.get());
            }
            if (obj instanceof UsbDevice) {
                return obj.equals(this.mWeakDevice.get());
            }
            return super.equals(obj);
        }

        private synchronized void checkConnection() throws IllegalStateException {
            if (this.mConnection == null) {
                throw new IllegalStateException("already closed");
            }
        }
    }
}
