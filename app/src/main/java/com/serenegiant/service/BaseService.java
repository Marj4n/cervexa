package com.serenegiant.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.serenegiant.common.R;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.HandlerThreadHandler;
import java.util.Iterator;

/* loaded from: classes2.dex */
public abstract class BaseService extends Service {
    private static final boolean DEBUG = false;
    private Handler mAsyncHandler;
    private volatile boolean mDestroyed;
    private LocalBroadcastManager mLocalBroadcastManager;
    private static final String TAG = BaseService.class.getSimpleName();
    private static final int NOTIFICATION_ID = R.string.service_name;
    protected final Object mSync = new Object();
    private final Handler mUIHandler = new Handler(Looper.getMainLooper());
    private final BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() { // from class: com.serenegiant.service.BaseService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                BaseService.this.onReceiveLocalBroadcast(context, intent);
            } catch (Exception e) {
                Log.w(BaseService.TAG, e);
            }
        }
    };

    protected abstract PendingIntent contextIntent();

    protected abstract IntentFilter createIntentFilter();

    protected abstract void onReceiveLocalBroadcast(Context context, Intent intent);

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        getApplicationContext();
        synchronized (this.mSync) {
            this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            IntentFilter createIntentFilter = createIntentFilter();
            if (createIntentFilter != null && createIntentFilter.countActions() > 0) {
                this.mLocalBroadcastManager.registerReceiver(this.mLocalBroadcastReceiver, createIntentFilter);
            }
            if (this.mAsyncHandler == null) {
                this.mAsyncHandler = HandlerThreadHandler.createHandler(getClass().getSimpleName());
            }
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        this.mDestroyed = true;
        synchronized (this.mSync) {
            this.mUIHandler.removeCallbacksAndMessages(null);
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.removeCallbacksAndMessages(null);
                try {
                    this.mAsyncHandler.getLooper().quit();
                } catch (Exception unused) {
                }
                this.mAsyncHandler = null;
            }
            if (this.mLocalBroadcastManager != null) {
                try {
                    this.mLocalBroadcastManager.unregisterReceiver(this.mLocalBroadcastReceiver);
                } catch (Exception unused2) {
                }
                this.mLocalBroadcastManager = null;
            }
        }
        super.onDestroy();
    }

    protected boolean isDestroyed() {
        return this.mDestroyed;
    }

    protected void sendLocalBroadcast(Intent intent) {
        synchronized (this.mSync) {
            if (this.mLocalBroadcastManager != null) {
                this.mLocalBroadcastManager.sendBroadcast(intent);
            }
        }
    }

    public static abstract class NotificationFactory {
        protected final String channelId;
        protected final String channelTitle;
        protected final String groupId;
        protected final String groupName;
        protected final int importance;
        protected final int largeIconId;
        protected final int smallIconId;

        protected abstract PendingIntent createContentIntent();

        protected PendingIntent createDeleteIntent() {
            return null;
        }

        protected boolean isForegroundService() {
            return true;
        }

        protected NotificationChannel setupNotificationChannel(NotificationChannel notificationChannel) {
            return notificationChannel;
        }

        protected NotificationChannelGroup setupNotificationChannelGroup(NotificationChannelGroup notificationChannelGroup) {
            return notificationChannelGroup;
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public NotificationFactory(String str, String str2, int i) {
            this(str, str, 0, null, null, i, R.drawable.ic_notification);
            BuildCheck.isAndroid7();
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public NotificationFactory(String str, String str2, int i, int i2) {
            this(str, str, 0, null, null, i, i2);
            BuildCheck.isAndroid7();
        }

        public NotificationFactory(String str, String str2, int i, String str3, String str4, int i2, int i3) {
            this.channelId = str;
            this.channelTitle = TextUtils.isEmpty(str2) ? str : str2;
            this.importance = i;
            this.groupId = str3;
            this.groupName = TextUtils.isEmpty(str4) ? str3 : str4;
            this.smallIconId = i2;
            this.largeIconId = i3;
        }

        protected Notification createNotification(Context context, CharSequence charSequence, CharSequence charSequence2) {
            if (BuildCheck.isOreo()) {
                createNotificationChannel(context);
            }
            return createNotificationBuilder(context, charSequence, charSequence2).build();
        }

        protected void createNotificationChannel(Context context) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager.getNotificationChannel(this.channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(this.channelId, this.channelTitle, this.importance);
                if (!TextUtils.isEmpty(this.groupId)) {
                    createNotificationChannelGroup(context, this.groupId, this.groupName);
                    notificationChannel.setGroup(this.groupId);
                }
                notificationChannel.setLockscreenVisibility(0);
                notificationManager.createNotificationChannel(setupNotificationChannel(notificationChannel));
            }
        }

        protected void createNotificationChannelGroup(Context context, String str, String str2) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            NotificationChannelGroup notificationChannelGroup = null;
            Iterator<NotificationChannelGroup> it = notificationManager.getNotificationChannelGroups().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                NotificationChannelGroup next = it.next();
                if (str.equals(next.getId())) {
                    notificationChannelGroup = next;
                    break;
                }
            }
            if (notificationChannelGroup == null) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = str;
                }
                notificationManager.createNotificationChannelGroup(setupNotificationChannelGroup(new NotificationChannelGroup(str, str2)));
            }
        }

        protected NotificationCompat.Builder createNotificationBuilder(Context context, CharSequence charSequence, CharSequence charSequence2) {
            NotificationCompat.Builder style = new NotificationCompat.Builder(context, this.channelId).setContentTitle(charSequence).setContentText(charSequence2).setSmallIcon(this.smallIconId).setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(charSequence).bigText(charSequence2).setSummaryText(charSequence2));
            PendingIntent createContentIntent = createContentIntent();
            if (createContentIntent != null) {
                style.setContentIntent(createContentIntent);
            }
            PendingIntent createDeleteIntent = createDeleteIntent();
            if (createDeleteIntent != null) {
                style.setDeleteIntent(createDeleteIntent);
            }
            if (!TextUtils.isEmpty(this.groupId)) {
                style.setGroup(this.groupId);
            }
            if (this.largeIconId != 0) {
                style.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), this.largeIconId));
            }
            return style;
        }
    }

    protected void showNotification(int i, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        showNotification(NOTIFICATION_ID, getString(R.string.service_name), null, null, i, R.drawable.ic_notification, charSequence, charSequence2, true, pendingIntent);
    }

    protected void showNotification(int i, CharSequence charSequence, CharSequence charSequence2, boolean z, PendingIntent pendingIntent) {
        showNotification(NOTIFICATION_ID, getString(R.string.service_name), null, null, i, R.drawable.ic_notification, charSequence, charSequence2, z, pendingIntent);
    }

    protected void showNotification(int i, String str, int i2, int i3, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        showNotification(i, str, null, null, i2, i3, charSequence, charSequence2, true, pendingIntent);
    }

    protected void showNotification(int i, String str, String str2, String str3, int i2, int i3, CharSequence charSequence, CharSequence charSequence2, PendingIntent pendingIntent) {
        showNotification(i, str, str2, str3, i2, i3, charSequence, charSequence2, true, pendingIntent);
    }

    protected void showNotification(int i, String str, String str2, String str3, int i2, int i3, CharSequence charSequence, CharSequence charSequence2, final boolean z, final PendingIntent pendingIntent) {
        showNotification(i, charSequence, charSequence2, new NotificationFactory(str, str, 0, str2, str3, i2, i3) { // from class: com.serenegiant.service.BaseService.2
            @Override // com.serenegiant.service.BaseService.NotificationFactory
            protected boolean isForegroundService() {
                return z;
            }

            @Override // com.serenegiant.service.BaseService.NotificationFactory
            protected PendingIntent createContentIntent() {
                return pendingIntent;
            }
        });
    }

    protected void showNotification(int i, CharSequence charSequence, CharSequence charSequence2, NotificationFactory notificationFactory) {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        Notification createNotification = notificationFactory.createNotification(this, charSequence2, charSequence);
        if (notificationFactory.isForegroundService()) {
            startForeground(i, createNotification);
        }
        notificationManager.notify(i, createNotification);
    }

    protected void releaseNotification() {
        releaseNotification(NOTIFICATION_ID, getString(R.string.service_name), R.drawable.ic_notification, R.drawable.ic_notification, getString(R.string.service_name), getString(R.string.service_stop));
    }

    protected void releaseNotification(int i, String str, int i2, int i3, CharSequence charSequence, CharSequence charSequence2) {
        showNotification(i, str, i2, i3, charSequence, charSequence2, null);
        releaseNotification(i, str);
    }

    protected void releaseNotification(int i, String str) {
        stopForeground(true);
        cancelNotification(i, str);
    }

    protected void cancelNotification(int i, String str) {
        ((NotificationManager) getSystemService("notification")).cancel(i);
        releaseNotificationChannel(str);
    }

    protected void cancelNotification(int i) {
        cancelNotification(i, null);
    }

    protected void releaseNotificationChannel(String str) {
        if (TextUtils.isEmpty(str) || !BuildCheck.isOreo()) {
            return;
        }
        try {
            ((NotificationManager) getSystemService("notification")).deleteNotificationChannel(str);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void releaseNotificationGroup(String str) {
        if (TextUtils.isEmpty(str) || !BuildCheck.isOreo()) {
            return;
        }
        try {
            ((NotificationManager) getSystemService("notification")).deleteNotificationChannelGroup(str);
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }

    protected void runOnUiThread(Runnable runnable) throws IllegalStateException {
        if (runnable == null) {
            return;
        }
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mUIHandler.removeCallbacks(runnable);
        this.mUIHandler.post(runnable);
    }

    protected void runOnUiThread(Runnable runnable, long j) throws IllegalStateException {
        if (runnable == null) {
            return;
        }
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        this.mUIHandler.removeCallbacks(runnable);
        if (j > 0) {
            this.mUIHandler.postDelayed(runnable, j);
        } else {
            this.mUIHandler.post(runnable);
        }
    }

    protected void removeFromUiThread(Runnable runnable) {
        this.mUIHandler.removeCallbacks(runnable);
    }

    protected void removeFromUiThreadAll(Object obj) {
        this.mUIHandler.removeCallbacksAndMessages(obj);
    }

    protected void queueEvent(Runnable runnable) throws IllegalStateException {
        if (runnable == null) {
            return;
        }
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        queueEvent(runnable, 0L);
    }

    protected void queueEvent(Runnable runnable, long j) throws IllegalStateException {
        if (runnable == null) {
            return;
        }
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.removeCallbacks(runnable);
                if (j > 0) {
                    this.mAsyncHandler.postDelayed(runnable, j);
                } else {
                    this.mAsyncHandler.post(runnable);
                }
            } else {
                throw new IllegalStateException("worker thread is not ready");
            }
        }
    }

    protected void removeEvent(Runnable runnable) {
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.removeCallbacks(runnable);
            }
        }
    }

    protected void removeEventAll(Object obj) {
        synchronized (this.mSync) {
            if (this.mAsyncHandler != null) {
                this.mAsyncHandler.removeCallbacksAndMessages(obj);
            }
        }
    }

    protected Handler getAsyncHandler() throws IllegalStateException {
        Handler handler;
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed");
        }
        synchronized (this.mSync) {
            handler = this.mAsyncHandler;
        }
        return handler;
    }
}
