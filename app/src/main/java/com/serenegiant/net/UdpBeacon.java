package com.serenegiant.net;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.serenegiant.utils.HandlerThreadHandler;
import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class UdpBeacon {
    public static final int BEACON_SIZE = 23;
    private static final int BEACON_UDP_PORT = 9999;
    private static final byte BEACON_VERSION = 1;
    private static final Charset CHARSET = Charset.forName(Key.STRING_CHARSET_NAME);
    private static final long DEFAULT_BEACON_SEND_INTERVALS_MS = 3000;
    private static final int SO_TIMEOUT_MS = 2000;
    private static final String TAG = "UdpBeacon";
    private final byte[] beaconBytes;
    private Handler mAsyncHandler;
    private final long mBeaconIntervalsMs;
    private final Runnable mBeaconTask;
    private Thread mBeaconThread;
    private final CopyOnWriteArraySet<UdpBeaconCallback> mCallbacks;
    private volatile boolean mIsRunning;
    private final long mRcvMinIntervalsMs;
    private boolean mReceiveOnly;
    private volatile boolean mReleased;
    private final Object mSync;
    private final UUID uuid;

    public interface UdpBeaconCallback {
        void onError(Exception exc);

        void onReceiveBeacon(UUID uuid, String str, int i);
    }

    private static class Beacon {
        public static final String BEACON_IDENTITY = "SAKI";
        private final int extraBytes;
        private final ByteBuffer extras;
        private final int listenPort;
        private final UUID uuid;

        public Beacon(ByteBuffer byteBuffer) {
            this.uuid = new UUID(byteBuffer.getLong(), byteBuffer.getLong());
            int i = byteBuffer.getShort();
            this.listenPort = i < 0 ? i & 65535 : i;
            if (byteBuffer.remaining() > 1) {
                int i2 = byteBuffer.get() & UByte.MAX_VALUE;
                this.extraBytes = i2;
                if (i2 > 0) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
                    this.extras = allocateDirect;
                    allocateDirect.put(byteBuffer);
                    this.extras.flip();
                    return;
                }
                this.extras = null;
                return;
            }
            this.extraBytes = 0;
            this.extras = null;
        }

        public Beacon(UUID uuid, int i) {
            this(uuid, i, 0);
        }

        public Beacon(UUID uuid, int i, int i2) {
            this.uuid = uuid;
            this.listenPort = i;
            int i3 = i2 & 255;
            this.extraBytes = i3;
            if (i3 > 0) {
                this.extras = ByteBuffer.allocateDirect(i3);
            } else {
                this.extras = null;
            }
        }

        public byte[] asBytes() {
            int i = this.extraBytes;
            byte[] bArr = new byte[(i > 0 ? i + 1 : 0) + 23];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            wrap.put(BEACON_IDENTITY.getBytes());
            wrap.put(UdpBeacon.BEACON_VERSION);
            wrap.putLong(this.uuid.getMostSignificantBits());
            wrap.putLong(this.uuid.getLeastSignificantBits());
            wrap.putShort((short) this.listenPort);
            int i2 = this.extraBytes;
            if (i2 > 0) {
                wrap.put((byte) i2);
                this.extras.clear();
                this.extras.flip();
                wrap.put(this.extras);
            }
            wrap.flip();
            return bArr;
        }

        public ByteBuffer extra() {
            return this.extras;
        }

        public void extra(byte[] bArr) {
            extra(ByteBuffer.wrap(bArr));
        }

        public void extra(ByteBuffer byteBuffer) {
            int remaining = byteBuffer != null ? byteBuffer.remaining() : -1;
            int i = this.extraBytes;
            if (i <= 0 || i > remaining) {
                return;
            }
            this.extras.clear();
            this.extras.put(byteBuffer);
            this.extras.flip();
        }

        public String toString() {
            return String.format(Locale.US, "Beacon(%s,port=%d,extra=%d)", this.uuid.toString(), Integer.valueOf(this.listenPort), Integer.valueOf(this.extraBytes));
        }
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback) {
        this(udpBeaconCallback, BEACON_UDP_PORT, DEFAULT_BEACON_SEND_INTERVALS_MS, false, 0L);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, long j) {
        this(udpBeaconCallback, BEACON_UDP_PORT, j, false, 0L);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, boolean z) {
        this(udpBeaconCallback, BEACON_UDP_PORT, DEFAULT_BEACON_SEND_INTERVALS_MS, false, 0L);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, boolean z, long j) {
        this(udpBeaconCallback, BEACON_UDP_PORT, DEFAULT_BEACON_SEND_INTERVALS_MS, false, j);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, long j, boolean z) {
        this(udpBeaconCallback, BEACON_UDP_PORT, j, z, 0L);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, long j, boolean z, long j2) {
        this(udpBeaconCallback, BEACON_UDP_PORT, j, z, j2);
    }

    public UdpBeacon(UdpBeaconCallback udpBeaconCallback, int i, long j, boolean z, long j2) {
        this.mSync = new Object();
        this.mCallbacks = new CopyOnWriteArraySet<>();
        this.mBeaconTask = new Runnable() { // from class: com.serenegiant.net.UdpBeacon.2
            /* JADX WARN: Finally extract failed */
            @Override // java.lang.Runnable
            public void run() {
                ByteBuffer.allocateDirect(256);
                try {
                    UdpSocket udpSocket = new UdpSocket(UdpBeacon.BEACON_UDP_PORT);
                    udpSocket.setReceiveBufferSize(256);
                    udpSocket.setReuseAddress(true);
                    udpSocket.setSoTimeout(2000);
                    Thread thread = new Thread(new ReceiverTask(udpSocket));
                    thread.start();
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    while (UdpBeacon.this.mIsRunning && !UdpBeacon.this.mReleased) {
                        try {
                            if (UdpBeacon.this.mReceiveOnly) {
                                if (UdpBeacon.this.waitWithoutException(this, 2000L)) {
                                    break;
                                }
                            } else {
                                long elapsedRealtime2 = elapsedRealtime - SystemClock.elapsedRealtime();
                                if (UdpBeacon.this.mReceiveOnly || elapsedRealtime2 > 0) {
                                    if (UdpBeacon.this.waitWithoutException(this, elapsedRealtime2)) {
                                        break;
                                    }
                                } else {
                                    elapsedRealtime = SystemClock.elapsedRealtime() + UdpBeacon.this.mBeaconIntervalsMs;
                                    UdpBeacon.this.sendBeacon(udpSocket);
                                }
                            }
                        } catch (Throwable th) {
                            UdpBeacon.this.mIsRunning = false;
                            udpSocket.release();
                            try {
                                thread.interrupt();
                            } catch (Exception e) {
                                Log.w(UdpBeacon.TAG, e);
                            }
                            throw th;
                        }
                    }
                    UdpBeacon.this.mIsRunning = false;
                    udpSocket.release();
                    try {
                        thread.interrupt();
                    } catch (Exception e2) {
                        Log.w(UdpBeacon.TAG, e2);
                    }
                } catch (Exception e3) {
                    UdpBeacon.this.callOnError(e3);
                }
                UdpBeacon.this.mIsRunning = false;
                synchronized (UdpBeacon.this.mSync) {
                    UdpBeacon.this.mBeaconThread = null;
                }
            }
        };
        if (udpBeaconCallback != null) {
            this.mCallbacks.add(udpBeaconCallback);
        }
        this.mAsyncHandler = HandlerThreadHandler.createHandler("UdpBeaconAsync");
        UUID randomUUID = UUID.randomUUID();
        this.uuid = randomUUID;
        this.beaconBytes = new Beacon(randomUUID, i).asBytes();
        this.mBeaconIntervalsMs = j;
        this.mReceiveOnly = z;
        this.mRcvMinIntervalsMs = j2;
    }

    public void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public void release() {
        if (this.mReleased) {
            return;
        }
        this.mReleased = true;
        stop();
        this.mCallbacks.clear();
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                try {
                    this.mAsyncHandler.getLooper().quit();
                } catch (Exception unused) {
                }
                this.mAsyncHandler = null;
            }
        }
    }

    public void addCallback(UdpBeaconCallback udpBeaconCallback) {
        if (udpBeaconCallback != null) {
            this.mCallbacks.add(udpBeaconCallback);
        }
    }

    public void removeCallback(UdpBeaconCallback udpBeaconCallback) {
        this.mCallbacks.remove(udpBeaconCallback);
    }

    public void start() {
        checkReleased();
        synchronized (this.mSync) {
            if (this.mBeaconThread == null) {
                this.mIsRunning = true;
                Thread thread = new Thread(this.mBeaconTask, "UdpBeaconTask");
                this.mBeaconThread = thread;
                thread.start();
            }
        }
    }

    public void stop() {
        Thread thread;
        this.mIsRunning = false;
        synchronized (this.mSync) {
            thread = this.mBeaconThread;
            this.mBeaconThread = null;
            this.mSync.notifyAll();
        }
        if (thread == null || !thread.isAlive()) {
            return;
        }
        thread.interrupt();
        try {
            thread.join();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void shot() throws IllegalStateException {
        shot(1);
    }

    public void shot(int i) throws IllegalStateException {
        checkReleased();
        synchronized (this.mSync) {
            new Thread(new BeaconShotTask(i), "UdpOneShotBeaconTask").start();
        }
    }

    public boolean isActive() {
        return this.mIsRunning;
    }

    public void setReceiveOnly(boolean z) throws IllegalStateException {
        checkReleased();
        synchronized (this.mSync) {
            if (this.mIsRunning) {
                throw new IllegalStateException("beacon is already active");
            }
            this.mReceiveOnly = z;
        }
    }

    public boolean isReceiveOnly() {
        return this.mReceiveOnly;
    }

    private void checkReleased() throws IllegalStateException {
        if (this.mReleased) {
            throw new IllegalStateException("already released");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callOnError(final Exception exc) {
        if (this.mReleased) {
            Log.w(TAG, exc);
            return;
        }
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.net.UdpBeacon.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Iterator it = UdpBeacon.this.mCallbacks.iterator();
                        while (it.hasNext()) {
                            UdpBeaconCallback udpBeaconCallback = (UdpBeaconCallback) it.next();
                            try {
                                udpBeaconCallback.onError(exc);
                            } catch (Exception e) {
                                UdpBeacon.this.mCallbacks.remove(udpBeaconCallback);
                                Log.w(UdpBeacon.TAG, e);
                            }
                        }
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitWithoutException(Object obj, long j) {
        boolean z;
        synchronized (obj) {
            try {
                obj.wait(j);
                z = false;
            } catch (InterruptedException unused) {
                z = true;
            }
        }
        return z;
    }

    private final class BeaconShotTask implements Runnable {
        private final int shotNums;

        public BeaconShotTask(int i) {
            this.shotNums = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                UdpSocket udpSocket = new UdpSocket(UdpBeacon.BEACON_UDP_PORT);
                udpSocket.setReuseAddress(true);
                udpSocket.setSoTimeout(2000);
                for (int i = 0; i < this.shotNums && !UdpBeacon.this.mReleased; i++) {
                    try {
                        UdpBeacon.this.sendBeacon(udpSocket);
                        if (UdpBeacon.this.waitWithoutException(this, UdpBeacon.this.mBeaconIntervalsMs)) {
                            break;
                        }
                    } catch (Throwable th) {
                        udpSocket.release();
                        throw th;
                    }
                }
                udpSocket.release();
            } catch (SocketException e) {
                UdpBeacon.this.callOnError(e);
            }
        }
    }

    private class ReceiverTask implements Runnable {
        private final UdpSocket mUdpSocket;

        private ReceiverTask(UdpSocket udpSocket) {
            this.mUdpSocket = udpSocket;
        }

        @Override // java.lang.Runnable
        public void run() {
            int receive;
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(256);
            UdpSocket udpSocket = this.mUdpSocket;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            while (UdpBeacon.this.mIsRunning && !UdpBeacon.this.mReleased) {
                if (UdpBeacon.this.mRcvMinIntervalsMs > 0) {
                    long elapsedRealtime2 = elapsedRealtime - SystemClock.elapsedRealtime();
                    if (elapsedRealtime2 > 0 && UdpBeacon.this.waitWithoutException(this, elapsedRealtime2)) {
                        return;
                    } else {
                        elapsedRealtime = SystemClock.elapsedRealtime() + UdpBeacon.this.mRcvMinIntervalsMs;
                    }
                }
                try {
                    try {
                        allocateDirect.clear();
                        receive = udpSocket.receive(allocateDirect);
                    } catch (IllegalStateException | ClosedChannelException unused) {
                        return;
                    }
                } catch (IOException unused2) {
                } catch (Exception e) {
                    Log.w(UdpBeacon.TAG, e);
                    return;
                }
                if (!UdpBeacon.this.mIsRunning) {
                    return;
                }
                allocateDirect.rewind();
                if (receive == 23 && allocateDirect.get() == 83 && allocateDirect.get() == 65 && allocateDirect.get() == 75 && allocateDirect.get() == 73 && allocateDirect.get() == 1) {
                    final Beacon beacon = new Beacon(allocateDirect);
                    if (UdpBeacon.this.uuid.equals(beacon.uuid)) {
                        continue;
                    } else {
                        final String remote = udpSocket.remote();
                        final int remotePort = udpSocket.remotePort();
                        synchronized (UdpBeacon.this.mSync) {
                            if (UdpBeacon.this.mAsyncHandler == null) {
                                return;
                            } else {
                                UdpBeacon.this.mAsyncHandler.post(new Runnable() { // from class: com.serenegiant.net.UdpBeacon.ReceiverTask.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Iterator it = UdpBeacon.this.mCallbacks.iterator();
                                        while (it.hasNext()) {
                                            UdpBeaconCallback udpBeaconCallback = (UdpBeaconCallback) it.next();
                                            try {
                                                udpBeaconCallback.onReceiveBeacon(beacon.uuid, remote, remotePort);
                                            } catch (Exception e2) {
                                                UdpBeacon.this.mCallbacks.remove(udpBeaconCallback);
                                                Log.w(UdpBeacon.TAG, e2);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBeacon(UdpSocket udpSocket) {
        try {
            udpSocket.broadcast(this.beaconBytes);
        } catch (IOException e) {
            Log.w(TAG, e);
        }
    }
}
