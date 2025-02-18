package com.jieli.lib.dv.control.utils;

import com.jieli.lib.dv.control.connect.listener.OnConnectStateListener;
import com.jieli.lib.dv.control.json.bean.NotifyInfo;
import com.jieli.lib.dv.control.receiver.listener.OnNotifyListener;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ListenerHelper {
    private static String a = "ListenerHelper";
    private static ListenerHelper b;
    private HashSet<OnConnectStateListener> c = new HashSet<>();
    private HashSet<OnNotifyListener> d = new HashSet<>();

    public static ListenerHelper getInstance() {
        if (b == null) {
            synchronized (ListenerHelper.class) {
                if (b == null) {
                    b = new ListenerHelper();
                }
            }
        }
        return b;
    }

    public boolean addConnectStateListener(OnConnectStateListener onConnectStateListener) {
        if (this.c.contains(onConnectStateListener)) {
            return false;
        }
        return this.c.add(onConnectStateListener);
    }

    public boolean removeConnectStateListener(OnConnectStateListener onConnectStateListener) {
        HashSet<OnConnectStateListener> hashSet = this.c;
        if (hashSet != null) {
            return hashSet.remove(onConnectStateListener);
        }
        return false;
    }

    public boolean addNotifyListener(OnNotifyListener onNotifyListener) {
        HashSet<OnNotifyListener> hashSet = this.d;
        if (hashSet == null || onNotifyListener == null || hashSet.contains(onNotifyListener)) {
            return false;
        }
        return this.d.add(onNotifyListener);
    }

    public boolean removeNotifyListener(OnNotifyListener onNotifyListener) {
        HashSet<OnNotifyListener> hashSet = this.d;
        if (hashSet != null) {
            return hashSet.remove(onNotifyListener);
        }
        return false;
    }

    public void dispatchDeviceConnectState(final int i) {
        HashSet<OnConnectStateListener> hashSet = this.c;
        if (hashSet == null) {
            Dlog.w(a, "OnConnectStateListener is null");
            return;
        }
        Iterator<OnConnectStateListener> it = hashSet.iterator();
        while (it.hasNext()) {
            final OnConnectStateListener next = it.next();
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.utils.ListenerHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    next.onStateChanged(Integer.valueOf(i));
                }
            });
        }
    }

    public void dispatchNotify(final NotifyInfo notifyInfo) {
        Iterator<OnNotifyListener> it = this.d.iterator();
        while (it.hasNext()) {
            final OnNotifyListener next = it.next();
            ClientContext.post(new Runnable() { // from class: com.jieli.lib.dv.control.utils.ListenerHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    next.onNotify(notifyInfo);
                }
            });
        }
    }

    public void release() {
        b = null;
        HashSet<OnConnectStateListener> hashSet = this.c;
        if (hashSet != null) {
            hashSet.clear();
        }
        HashSet<OnNotifyListener> hashSet2 = this.d;
        if (hashSet2 != null) {
            hashSet2.clear();
        }
    }
}
