package com.serenegiant.utils;

import android.util.Log;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes2.dex */
public abstract class MessageTask implements Runnable {
    protected static final int REQUEST_TASK_NON = 0;
    protected static final int REQUEST_TASK_QUIT = -9;
    protected static final int REQUEST_TASK_RUN = -1;
    protected static final int REQUEST_TASK_RUN_AND_WAIT = -2;
    protected static final int REQUEST_TASK_START = -8;
    private static final String TAG = MessageTask.class.getSimpleName();
    private volatile boolean mFinished;
    private volatile boolean mIsRunning;
    private final int mMaxRequest;
    private final LinkedBlockingQueue<Request> mRequestPool;
    private final LinkedBlockingDeque<Request> mRequestQueue;
    private final Object mSync;
    private Thread mWorkerThread;

    public static class TaskBreak extends RuntimeException {
    }

    protected void onBeforeStop() {
    }

    protected boolean onError(Exception exc) {
        return true;
    }

    protected abstract void onInit(int i, int i2, Object obj);

    protected abstract void onRelease();

    protected abstract void onStart();

    protected abstract void onStop();

    protected abstract Object processRequest(int i, int i2, int i3, Object obj) throws TaskBreak;

    protected static final class Request {
        int arg1;
        int arg2;
        Object obj;
        int request;
        int request_for_result;
        Object result;

        private Request() {
            this.request_for_result = 0;
            this.request = 0;
        }

        public Request(int i, int i2, int i3, Object obj) {
            this.request = i;
            this.arg1 = i2;
            this.arg2 = i3;
            this.obj = obj;
            this.request_for_result = 0;
        }

        public void setResult(Object obj) {
            synchronized (this) {
                this.result = obj;
                this.request_for_result = 0;
                this.request = 0;
                notifyAll();
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Request)) {
                return super.equals(obj);
            }
            Request request = (Request) obj;
            return this.request == request.request && this.request_for_result == request.request_for_result && this.arg1 == request.arg1 && this.arg2 == request.arg2 && this.obj == request.obj;
        }
    }

    public MessageTask() {
        this.mSync = new Object();
        this.mMaxRequest = -1;
        this.mRequestPool = new LinkedBlockingQueue<>();
        this.mRequestQueue = new LinkedBlockingDeque<>();
    }

    public MessageTask(int i) {
        this.mSync = new Object();
        this.mMaxRequest = -1;
        this.mRequestPool = new LinkedBlockingQueue<>();
        this.mRequestQueue = new LinkedBlockingDeque<>();
        for (int i2 = 0; i2 < i && this.mRequestPool.offer(new Request()); i2++) {
        }
    }

    public MessageTask(int i, int i2) {
        this.mSync = new Object();
        this.mMaxRequest = i;
        this.mRequestPool = new LinkedBlockingQueue<>(i);
        this.mRequestQueue = new LinkedBlockingDeque<>(i);
        for (int i3 = 0; i3 < i2 && this.mRequestPool.offer(new Request()); i3++) {
        }
    }

    protected void init(int i, int i2, Object obj) {
        this.mFinished = false;
        this.mRequestQueue.offer(obtain(-8, i, i2, obj));
    }

    protected Request takeRequest() throws InterruptedException {
        return this.mRequestQueue.take();
    }

    public boolean waitReady() {
        boolean z;
        synchronized (this.mSync) {
            while (!this.mIsRunning && !this.mFinished) {
                try {
                    this.mSync.wait(500L);
                } catch (InterruptedException unused) {
                }
            }
            z = this.mIsRunning;
        }
        return z;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    @Override // java.lang.Runnable
    public void run() {
        Request request;
        this.mIsRunning = true;
        try {
            request = this.mRequestQueue.take();
        } catch (InterruptedException unused) {
            this.mIsRunning = false;
            this.mFinished = true;
            request = null;
        }
        synchronized (this.mSync) {
            if (this.mIsRunning) {
                this.mWorkerThread = Thread.currentThread();
                try {
                    onInit(request.arg1, request.arg2, request.obj);
                } catch (Exception e) {
                    Log.w(TAG, e);
                    this.mIsRunning = false;
                    this.mFinished = true;
                }
            }
            this.mSync.notifyAll();
        }
        if (this.mIsRunning) {
            try {
                onStart();
            } catch (Exception e2) {
                if (callOnError(e2)) {
                    this.mIsRunning = false;
                    this.mFinished = true;
                }
            }
        }
        while (this.mIsRunning) {
            try {
                Request takeRequest = takeRequest();
                int i = takeRequest.request;
                if (i == -9) {
                    break;
                }
                if (i == -2) {
                    try {
                        takeRequest.setResult(processRequest(takeRequest.request_for_result, takeRequest.arg1, takeRequest.arg2, takeRequest.obj));
                    } catch (TaskBreak unused2) {
                        takeRequest.setResult(null);
                    } catch (Exception e3) {
                        takeRequest.setResult(null);
                        if (callOnError(e3)) {
                            break;
                        }
                    }
                    takeRequest.request_for_result = 0;
                    takeRequest.request = 0;
                    this.mRequestPool.offer(takeRequest);
                } else if (i == -1) {
                    if (takeRequest.obj instanceof Runnable) {
                        try {
                            ((Runnable) takeRequest.obj).run();
                        } catch (Exception e4) {
                            if (callOnError(e4)) {
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                    takeRequest.request_for_result = 0;
                    takeRequest.request = 0;
                    this.mRequestPool.offer(takeRequest);
                } else {
                    if (i != 0) {
                        try {
                            processRequest(takeRequest.request, takeRequest.arg1, takeRequest.arg2, takeRequest.obj);
                        } catch (Exception e5) {
                            if (callOnError(e5)) {
                                break;
                            }
                        }
                    } else {
                        continue;
                    }
                    takeRequest.request_for_result = 0;
                    takeRequest.request = 0;
                    this.mRequestPool.offer(takeRequest);
                }
            } catch (TaskBreak | InterruptedException unused3) {
            }
        }
        boolean interrupted = Thread.interrupted();
        synchronized (this.mSync) {
            this.mWorkerThread = null;
            this.mIsRunning = false;
            this.mFinished = true;
        }
        if (!interrupted) {
            try {
                onBeforeStop();
                onStop();
            } catch (Exception e6) {
                callOnError(e6);
            }
        }
        try {
            onRelease();
        } catch (Exception unused4) {
        }
        synchronized (this.mSync) {
            this.mSync.notifyAll();
        }
    }

    protected boolean callOnError(Exception exc) {
        try {
            return onError(exc);
        } catch (Exception unused) {
            return true;
        }
    }

    protected Request obtain(int i, int i2, int i3, Object obj) {
        Request poll = this.mRequestPool.poll();
        if (poll != null) {
            poll.request = i;
            poll.arg1 = i2;
            poll.arg2 = i3;
            poll.obj = obj;
            return poll;
        }
        return new Request(i, i2, i3, obj);
    }

    public boolean offer(int i, int i2, int i3, Object obj) {
        return !this.mFinished && this.mRequestQueue.offer(obtain(i, i2, i3, obj));
    }

    public boolean offer(int i, int i2, Object obj) {
        return !this.mFinished && this.mRequestQueue.offer(obtain(i, i2, 0, obj));
    }

    public boolean offer(int i, int i2, int i3) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, i2, i3, null));
    }

    public boolean offer(int i, int i2) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, i2, 0, null));
    }

    public boolean offer(int i) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, 0, 0, null));
    }

    public boolean offer(int i, Object obj) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offer(obtain(i, 0, 0, obj));
    }

    public boolean offerFirst(int i, int i2, int i3, Object obj) {
        return !this.mFinished && this.mIsRunning && this.mRequestQueue.offerFirst(obtain(i, i2, i3, obj));
    }

    public Object offerAndWait(int i, int i2, int i3, Object obj) {
        if (this.mFinished || i <= 0) {
            return null;
        }
        Request obtain = obtain(-2, i2, i3, obj);
        synchronized (obtain) {
            obtain.request_for_result = i;
            obtain.result = null;
            this.mRequestQueue.offer(obtain);
            while (this.mIsRunning && obtain.request_for_result != 0) {
                try {
                    obtain.wait(100L);
                } catch (InterruptedException unused) {
                }
            }
        }
        return obtain.result;
    }

    public boolean queueEvent(Runnable runnable) {
        return (this.mFinished || runnable == null || !offer(-1, runnable)) ? false : true;
    }

    public void removeRequest(Request request) {
        Iterator<Request> it = this.mRequestQueue.iterator();
        while (it.hasNext()) {
            Request next = it.next();
            if (!this.mIsRunning || this.mFinished) {
                return;
            }
            if (next.equals(request)) {
                this.mRequestQueue.remove(next);
                this.mRequestPool.offer(next);
            }
        }
    }

    public void removeRequest(int i) {
        Iterator<Request> it = this.mRequestQueue.iterator();
        while (it.hasNext()) {
            Request next = it.next();
            if (!this.mIsRunning || this.mFinished) {
                return;
            }
            if (next.request == i) {
                this.mRequestQueue.remove(next);
                this.mRequestPool.offer(next);
            }
        }
    }

    public void release() {
        release(false);
    }

    public void release(boolean z) {
        boolean z2 = this.mIsRunning;
        this.mIsRunning = false;
        if (this.mFinished) {
            return;
        }
        this.mRequestQueue.clear();
        this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
        synchronized (this.mSync) {
            if (z2) {
                long id = Thread.currentThread().getId();
                if ((this.mWorkerThread != null ? this.mWorkerThread.getId() : id) != id) {
                    if (z && this.mWorkerThread != null) {
                        this.mWorkerThread.interrupt();
                    }
                    while (!this.mFinished) {
                        try {
                            this.mSync.wait(300L);
                        } catch (InterruptedException unused) {
                        }
                    }
                }
            }
        }
    }

    public void releaseSelf() {
        this.mIsRunning = false;
        if (this.mFinished) {
            return;
        }
        this.mRequestQueue.clear();
        this.mRequestQueue.offerFirst(obtain(-9, 0, 0, null));
    }

    public void userBreak() throws TaskBreak {
        throw new TaskBreak();
    }
}
