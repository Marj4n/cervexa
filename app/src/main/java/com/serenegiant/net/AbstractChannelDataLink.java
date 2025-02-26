package com.serenegiant.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.bumptech.glide.load.Key;
import com.serenegiant.io.ChannelHelper;
import com.serenegiant.utils.HandlerThreadHandler;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes2.dex */
public abstract class AbstractChannelDataLink {
    private static final boolean DEBUG = false;
    private static final int REQ_RELEASE = -9;
    private static final int TYPE_BOOL = 10;
    private static final int TYPE_BOOL_ARRAY = 31;
    private static final int TYPE_BYTE_ARRAY = 30;
    private static final int TYPE_BYTE_BUFFER = 1;
    private static final int TYPE_DOUBLE = 21;
    private static final int TYPE_DOUBLE_ARRAY = 41;
    private static final int TYPE_FLOAT = 20;
    private static final int TYPE_FLOAT_ARRAY = 40;
    private static final int TYPE_INT = 11;
    private static final int TYPE_INT_ARRAY = 32;
    private static final int TYPE_LONG = 12;
    private static final int TYPE_LONG_ARRAY = 33;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 2;
    private static final int TYPE_UNKNOWN = -1;
    private final Set<Callback> mCallbacks;
    private final Set<AbstractClient> mClients;
    private static final String TAG = AbstractChannelDataLink.class.getSimpleName();
    private static final Charset UTF8 = Charset.forName(Key.STRING_CHARSET_NAME);

    public interface Callback {
        void onConnect(AbstractClient abstractClient);

        void onDisconnect();

        void onError(AbstractClient abstractClient, Exception exc);

        void onReceive(AbstractClient abstractClient, Object obj);
    }

    public AbstractChannelDataLink() {
        this.mClients = new CopyOnWriteArraySet();
        this.mCallbacks = new CopyOnWriteArraySet();
    }

    public AbstractChannelDataLink(Callback callback) {
        this();
        add(callback);
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public void release() {
        Iterator<AbstractClient> it = this.mClients.iterator();
        while (it.hasNext()) {
            it.next().release();
        }
        this.mClients.clear();
    }

    public void add(Callback callback) {
        if (callback != null) {
            this.mCallbacks.add(callback);
        }
    }

    public void remove(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    protected void add(AbstractClient abstractClient) {
        if (abstractClient != null) {
            this.mClients.add(abstractClient);
        }
    }

    public void remove(AbstractClient abstractClient) {
        this.mClients.remove(abstractClient);
    }

    public static abstract class AbstractClient implements Runnable, Handler.Callback {
        protected ByteChannel mChannel;
        private volatile boolean mIsInit;
        private volatile boolean mIsRunning = true;
        private Handler mSenderHandler = HandlerThreadHandler.createHandler(this);
        private final WeakReference<AbstractChannelDataLink> mWeakParent;

        protected abstract void init() throws IOException;

        public AbstractClient(AbstractChannelDataLink abstractChannelDataLink, ByteChannel byteChannel) {
            this.mWeakParent = new WeakReference<>(abstractChannelDataLink);
            this.mChannel = byteChannel;
        }

        protected void finalize() throws Throwable {
            try {
                release(-1L);
            } finally {
                super.finalize();
            }
        }

        public void release() {
            release(500L);
        }

        public synchronized void release(long j) {
            if (this.mSenderHandler != null) {
                try {
                    if (j > 0) {
                        this.mSenderHandler.sendEmptyMessageDelayed(-9, j);
                    } else {
                        this.mSenderHandler.sendEmptyMessage(-9);
                    }
                } catch (Exception unused) {
                }
            }
        }

        protected void internalStart() {
            synchronized (this) {
                new Thread(this).start();
                while (this.mIsRunning && !this.mIsInit) {
                    try {
                        wait(300L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }

        private void internalRelease() {
            ByteChannel byteChannel;
            this.mIsInit = false;
            this.mIsRunning = false;
            synchronized (this) {
                byteChannel = this.mChannel;
                this.mChannel = null;
                notifyAll();
            }
            if (byteChannel != null) {
                try {
                    byteChannel.close();
                } catch (Exception unused) {
                }
            }
            synchronized (this) {
                if (this.mSenderHandler != null) {
                    try {
                        this.mSenderHandler.getLooper().quit();
                    } catch (Exception unused2) {
                    }
                    this.mSenderHandler = null;
                }
            }
        }

        public void send(boolean z) throws IOException {
            send(10, Boolean.valueOf(z));
        }

        public synchronized void send(int i) throws IOException {
            send(11, Integer.valueOf(i));
        }

        public void send(long j) throws IOException {
            send(12, Long.valueOf(j));
        }

        public void send(float f) throws IOException {
            send(20, Float.valueOf(f));
        }

        public void send(double d) throws IOException {
            send(21, Double.valueOf(d));
        }

        public void send(String str) throws IOException {
            send(2, str);
        }

        public void send(byte[] bArr) throws IOException {
            send(30, bArr);
        }

        public void sent(boolean[] zArr) throws IOException {
            send(31, zArr);
        }

        public void send(int[] iArr) throws IOException {
            send(32, iArr);
        }

        public void send(long[] jArr) throws IOException {
            send(33, jArr);
        }

        public void send(float[] fArr) throws IOException {
            send(40, fArr);
        }

        public void send(double[] dArr) throws IOException {
            send(41, dArr);
        }

        public void send(ByteBuffer byteBuffer) throws IOException {
            send(1, byteBuffer);
        }

        public void send(Object obj) throws IOException {
            if (obj == null) {
                send(0, null);
                return;
            }
            if (obj instanceof ByteBuffer) {
                send(1, obj);
                return;
            }
            if (obj instanceof String) {
                send(2, obj);
                return;
            }
            if (obj instanceof CharSequence) {
                send(2, obj.toString());
                return;
            }
            if (obj instanceof Boolean) {
                send(10, obj);
                return;
            }
            if (obj instanceof Integer) {
                send(11, obj);
                return;
            }
            if (obj instanceof Long) {
                send(12, obj);
                return;
            }
            if (obj instanceof Float) {
                send(20, obj);
                return;
            }
            if (obj instanceof Double) {
                send(21, obj);
                return;
            }
            if (obj instanceof byte[]) {
                send(30, obj);
                return;
            }
            if (obj instanceof boolean[]) {
                send(31, obj);
                return;
            }
            if (obj instanceof int[]) {
                send(32, obj);
                return;
            }
            if (obj instanceof long[]) {
                send(33, obj);
            } else if (obj instanceof float[]) {
                send(40, obj);
            } else {
                if (obj instanceof double[]) {
                    send(41, obj);
                    return;
                }
                throw new IOException("unknown type of object");
            }
        }

        private synchronized void send(int i, Object obj) throws IOException {
            if (this.mSenderHandler == null || !this.mIsRunning || !this.mIsInit) {
                throw new IOException();
            }
            this.mSenderHandler.sendMessage(this.mSenderHandler.obtainMessage(i, obj));
        }

        protected void setInit(boolean z) {
            this.mIsInit = z;
        }

        /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
        
            if (r2 == null) goto L14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:5:0x0019, code lost:
        
            if (r2 != null) goto L13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:6:0x0035, code lost:
        
            release(-1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:7:0x0038, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x002e, code lost:
        
            r2.mClients.remove(r4);
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                r4 = this;
                r0 = -1
                r2 = 0
                r4.init()     // Catch: java.lang.Throwable -> L1c java.lang.Exception -> L1e
                r4.callOnConnect()     // Catch: java.lang.Throwable -> L1c java.lang.Exception -> L1e
                r4.doReceiveLoop()     // Catch: java.lang.Throwable -> L1c java.lang.Exception -> L1e
                r4.callOnDisconnect()     // Catch: java.lang.Throwable -> L1c java.lang.Exception -> L1e
                r4.mIsRunning = r2
                java.lang.ref.WeakReference<com.serenegiant.net.AbstractChannelDataLink> r2 = r4.mWeakParent
                java.lang.Object r2 = r2.get()
                com.serenegiant.net.AbstractChannelDataLink r2 = (com.serenegiant.net.AbstractChannelDataLink) r2
                if (r2 == 0) goto L35
                goto L2e
            L1c:
                r3 = move-exception
                goto L39
            L1e:
                r3 = move-exception
                r4.callOnError(r3)     // Catch: java.lang.Throwable -> L1c
                r4.mIsRunning = r2
                java.lang.ref.WeakReference<com.serenegiant.net.AbstractChannelDataLink> r2 = r4.mWeakParent
                java.lang.Object r2 = r2.get()
                com.serenegiant.net.AbstractChannelDataLink r2 = (com.serenegiant.net.AbstractChannelDataLink) r2
                if (r2 == 0) goto L35
            L2e:
                java.util.Set r2 = com.serenegiant.net.AbstractChannelDataLink.access$000(r2)
                r2.remove(r4)
            L35:
                r4.release(r0)
                return
            L39:
                r4.mIsRunning = r2
                java.lang.ref.WeakReference<com.serenegiant.net.AbstractChannelDataLink> r2 = r4.mWeakParent
                java.lang.Object r2 = r2.get()
                com.serenegiant.net.AbstractChannelDataLink r2 = (com.serenegiant.net.AbstractChannelDataLink) r2
                if (r2 == 0) goto L4c
                java.util.Set r2 = com.serenegiant.net.AbstractChannelDataLink.access$000(r2)
                r2.remove(r4)
            L4c:
                r4.release(r0)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.serenegiant.net.AbstractChannelDataLink.AbstractClient.run():void");
        }

        private void doReceiveLoop() throws IOException, ClassNotFoundException {
            while (this.mIsRunning) {
                try {
                    int readInt = ChannelHelper.readInt(this.mChannel);
                    if (readInt == 0) {
                        callOnReceive(null);
                    } else if (readInt == 1) {
                        callOnReceive(ChannelHelper.readByteBuffer(this.mChannel));
                    } else if (readInt == 2) {
                        callOnReceive(ChannelHelper.readString(this.mChannel));
                    } else if (readInt == 20) {
                        callOnReceive(Float.valueOf(ChannelHelper.readFloat(this.mChannel)));
                    } else if (readInt == 21) {
                        callOnReceive(Double.valueOf(ChannelHelper.readDouble(this.mChannel)));
                    } else if (readInt == 40) {
                        callOnReceive(ChannelHelper.readFloatArray(this.mChannel));
                    } else if (readInt != 41) {
                        switch (readInt) {
                            case 10:
                                callOnReceive(Boolean.valueOf(ChannelHelper.readBoolean(this.mChannel)));
                                break;
                            case 11:
                                callOnReceive(Integer.valueOf(ChannelHelper.readInt(this.mChannel)));
                                break;
                            case 12:
                                callOnReceive(Long.valueOf(ChannelHelper.readLong(this.mChannel)));
                                break;
                            default:
                                switch (readInt) {
                                    case 30:
                                        callOnReceive(ChannelHelper.readByteArray(this.mChannel));
                                        break;
                                    case 31:
                                        callOnReceive(ChannelHelper.readBooleanArray(this.mChannel));
                                        break;
                                    case 32:
                                        callOnReceive(ChannelHelper.readIntArray(this.mChannel));
                                        break;
                                    case 33:
                                        callOnReceive(ChannelHelper.readLongArray(this.mChannel));
                                        break;
                                }
                        }
                    } else {
                        callOnReceive(ChannelHelper.readDoubleArray(this.mChannel));
                    }
                } catch (IOException unused) {
                    return;
                }
            }
        }

        protected void callOnConnect() {
            AbstractChannelDataLink abstractChannelDataLink = this.mWeakParent.get();
            if (abstractChannelDataLink != null) {
                for (Callback callback : abstractChannelDataLink.mCallbacks) {
                    try {
                        callback.onConnect(this);
                    } catch (Exception unused) {
                        abstractChannelDataLink.mCallbacks.remove(callback);
                    }
                }
            }
        }

        protected void callOnDisconnect() {
            AbstractChannelDataLink abstractChannelDataLink = this.mWeakParent.get();
            if (abstractChannelDataLink != null) {
                for (Callback callback : abstractChannelDataLink.mCallbacks) {
                    try {
                        callback.onDisconnect();
                    } catch (Exception unused) {
                        abstractChannelDataLink.mCallbacks.remove(callback);
                    }
                }
            }
        }

        protected void callOnReceive(Object obj) {
            AbstractChannelDataLink abstractChannelDataLink = this.mWeakParent.get();
            if (abstractChannelDataLink != null) {
                for (Callback callback : abstractChannelDataLink.mCallbacks) {
                    try {
                        callback.onReceive(this, obj);
                    } catch (Exception unused) {
                        abstractChannelDataLink.mCallbacks.remove(callback);
                    }
                }
            }
        }

        protected void callOnError(Exception exc) {
            AbstractChannelDataLink abstractChannelDataLink = this.mWeakParent.get();
            if (abstractChannelDataLink != null) {
                for (Callback callback : abstractChannelDataLink.mCallbacks) {
                    try {
                        callback.onError(this, exc);
                    } catch (Exception unused) {
                        abstractChannelDataLink.mCallbacks.remove(callback);
                    }
                }
            }
        }

        @Override // android.os.Handler.Callback
        public synchronized boolean handleMessage(Message message) {
            if (!this.mIsRunning || this.mChannel == null) {
                return false;
            }
            try {
            } catch (Exception e2) {
                Log.w(AbstractChannelDataLink.TAG, e2);
            }
            if (message.what == -9) {
                internalRelease();
                return true;
            }
            int i = message.what;
            if (i == 0) {
                try {
                    ChannelHelper.write(this.mChannel, 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            if (i == 1) {
                if (message.obj instanceof ByteBuffer) {
                    try {
                        ChannelHelper.write(this.mChannel, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ChannelHelper.write(this.mChannel, (ByteBuffer) message.obj);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return true;
            }
            if (i != 2) {
                if (i != 20) {
                    if (i != 21) {
                        if (i != 40) {
                            if (i != 41) {
                                switch (i) {
                                    case 10:
                                        if (message.obj instanceof Boolean) {
                                            try {
                                                ChannelHelper.write(this.mChannel, 10);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                ChannelHelper.write(this.mChannel, ((Boolean) message.obj).booleanValue());
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return true;
                                        }
                                        break;
                                    case 11:
                                        if (message.obj instanceof Integer) {
                                            try {
                                                ChannelHelper.write(this.mChannel, 11);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                ChannelHelper.write(this.mChannel, ((Integer) message.obj).intValue());
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return true;
                                        }
                                        break;
                                    case 12:
                                        if (message.obj instanceof Long) {
                                            try {
                                                ChannelHelper.write(this.mChannel, 12);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            try {
                                                ChannelHelper.write(this.mChannel, ((Long) message.obj).longValue());
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            return true;
                                        }
                                        break;
                                    default:
                                        switch (i) {
                                            case 30:
                                                if (message.obj instanceof byte[]) {
                                                    try {
                                                        ChannelHelper.write(this.mChannel, 30);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        ChannelHelper.write(this.mChannel, (byte[]) message.obj);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    return true;
                                                }
                                                break;
                                            case 31:
                                                if (message.obj instanceof boolean[]) {
                                                    try {
                                                        ChannelHelper.write(this.mChannel, 31);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        ChannelHelper.write(this.mChannel, (boolean[]) message.obj);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    return true;
                                                }
                                                break;
                                            case 32:
                                                if (message.obj instanceof int[]) {
                                                    try {
                                                        ChannelHelper.write(this.mChannel, 32);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        ChannelHelper.write(this.mChannel, (int[]) message.obj);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    return true;
                                                }
                                                break;
                                            case 33:
                                                if (message.obj instanceof long[]) {
                                                    try {
                                                        ChannelHelper.write(this.mChannel, 33);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    try {
                                                        ChannelHelper.write(this.mChannel, (long[]) message.obj);
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    return true;
                                                }
                                                break;
                                        }
                                }
                            } else if (message.obj instanceof double[]) {
                                try {
                                    ChannelHelper.write(this.mChannel, 41);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    ChannelHelper.write(this.mChannel, (double[]) message.obj);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                return true;
                            }
                        } else if (message.obj instanceof float[]) {
                            try {
                                ChannelHelper.write(this.mChannel, 40);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                ChannelHelper.write(this.mChannel, (float[]) message.obj);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        }
                    } else if (message.obj instanceof Double) {
                        try {
                            ChannelHelper.write(this.mChannel, 21);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            ChannelHelper.write(this.mChannel, ((Double) message.obj).doubleValue());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return true;
                    }
                } else if (message.obj instanceof Float) {
                    try {
                        ChannelHelper.write(this.mChannel, 20);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ChannelHelper.write(this.mChannel, ((Float) message.obj).floatValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            } else if (message.obj instanceof String) {
                try {
                    ChannelHelper.write(this.mChannel, 2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    ChannelHelper.write(this.mChannel, (String) message.obj);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            return false;
        }
    }
}
