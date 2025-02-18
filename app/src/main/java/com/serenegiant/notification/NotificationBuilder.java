package com.serenegiant.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.serenegiant.utils.BuildCheck;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public abstract class NotificationBuilder extends NotificationCompat.Builder {
    private static final boolean DEBUG = false;
    private static final int PRIORITY_UNSPECIFIED = -3;
    private static final String TAG = NotificationBuilder.class.getSimpleName();
    private final ChannelBuilder mChannelBuilder;
    private PendingIntent mContentIntent;
    private final Context mContext;
    private PendingIntent mDeleteIntent;
    private PendingIntent mFullScreenIntent;
    private boolean mHighPriorityFullScreenIntent;
    private int mPriority;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BadgeIconType {
    }

    public static class DefaultIntentFactory implements NotificationBuilderFactory {
        @Override // com.serenegiant.notification.NotificationBuilder.IntentFactory
        public PendingIntent createContentIntent(PendingIntent pendingIntent) {
            return pendingIntent;
        }

        @Override // com.serenegiant.notification.NotificationBuilder.IntentFactory
        public PendingIntent createDeleteIntent(PendingIntent pendingIntent) {
            return pendingIntent;
        }

        @Override // com.serenegiant.notification.NotificationBuilder.IntentFactory
        public PendingIntent createFullScreenIntent(PendingIntent pendingIntent) {
            return pendingIntent;
        }

        @Override // com.serenegiant.notification.NotificationBuilder.IntentFactory
        public boolean isHighPriorityFullScreenIntent(boolean z) {
            return z;
        }

        @Override // com.serenegiant.notification.NotificationBuilder.NotificationBuilderFactory
        public void setupBuilder(NotificationBuilder notificationBuilder) {
        }
    }

    public interface IntentFactory {
        PendingIntent createContentIntent(PendingIntent pendingIntent);

        PendingIntent createDeleteIntent(PendingIntent pendingIntent);

        PendingIntent createFullScreenIntent(PendingIntent pendingIntent);

        boolean isHighPriorityFullScreenIntent(boolean z);
    }

    public interface NotificationBuilderFactory extends IntentFactory {
        void setupBuilder(NotificationBuilder notificationBuilder);
    }

    protected abstract PendingIntent createContentIntent();

    public static void showNotification(Context context, int i, String str, String str2, String str3, int i2, IntentFactory intentFactory) {
        showNotification(context, null, i, str, str2, str3, i2, intentFactory);
    }

    public static void showNotification(Context context, String str, int i, String str2, String str3, String str4, int i2, final IntentFactory intentFactory) {
        NotificationBuilder notificationBuilder = new NotificationBuilder(context, str2, i2) { // from class: com.serenegiant.notification.NotificationBuilder.1
            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder addAction(int i3, CharSequence charSequence, PendingIntent pendingIntent) {
                return super.addAction(i3, charSequence, pendingIntent);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder addAction(NotificationCompat.Action action) {
                return super.addAction(action);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder addExtras(Bundle bundle) {
                return super.addExtras(bundle);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder addPerson(String str5) {
                return super.addPerson(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder extend(NotificationCompat.Extender extender) {
                return super.extend(extender);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setAutoCancel(boolean z) {
                return super.setAutoCancel(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setBadgeIconType(int i3) {
                return super.setBadgeIconType(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setCategory(String str5) {
                return super.setCategory(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setChannelId(String str5) {
                return super.setChannelId(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setColor(int i3) {
                return super.setColor(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setColorized(boolean z) {
                return super.setColorized(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setContent(RemoteViews remoteViews) {
                return super.setContent(remoteViews);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setContentInfo(CharSequence charSequence) {
                return super.setContentInfo(charSequence);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setContentIntent(PendingIntent pendingIntent) {
                return super.setContentIntent(pendingIntent);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setContentText(CharSequence charSequence) {
                return super.setContentText(charSequence);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setContentTitle(CharSequence charSequence) {
                return super.setContentTitle(charSequence);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setCustomBigContentView(RemoteViews remoteViews) {
                return super.setCustomBigContentView(remoteViews);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setCustomContentView(RemoteViews remoteViews) {
                return super.setCustomContentView(remoteViews);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setCustomHeadsUpContentView(RemoteViews remoteViews) {
                return super.setCustomHeadsUpContentView(remoteViews);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setDefaults(int i3) {
                return super.setDefaults(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setDeleteIntent(PendingIntent pendingIntent) {
                return super.setDeleteIntent(pendingIntent);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setExtras(Bundle bundle) {
                return super.setExtras(bundle);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setFullScreenIntent(PendingIntent pendingIntent, boolean z) {
                return super.setFullScreenIntent(pendingIntent, z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setGroup(String str5) {
                return super.setGroup(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setGroupAlertBehavior(int i3) {
                return super.setGroupAlertBehavior(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setGroupSummary(boolean z) {
                return super.setGroupSummary(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setLargeIcon(Bitmap bitmap) {
                return super.setLargeIcon(bitmap);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setLights(int i3, int i4, int i5) {
                return super.setLights(i3, i4, i5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setLocalOnly(boolean z) {
                return super.setLocalOnly(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setNumber(int i3) {
                return super.setNumber(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setOngoing(boolean z) {
                return super.setOngoing(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setOnlyAlertOnce(boolean z) {
                return super.setOnlyAlertOnce(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setPriority(int i3) {
                return super.setPriority(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setProgress(int i3, int i4, boolean z) {
                return super.setProgress(i3, i4, z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setPublicVersion(Notification notification) {
                return super.setPublicVersion(notification);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setRemoteInputHistory(CharSequence[] charSequenceArr) {
                return super.setRemoteInputHistory(charSequenceArr);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setShortcutId(String str5) {
                return super.setShortcutId(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setShowWhen(boolean z) {
                return super.setShowWhen(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSmallIcon(int i3) {
                return super.setSmallIcon(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSmallIcon(int i3, int i4) {
                return super.setSmallIcon(i3, i4);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSortKey(String str5) {
                return super.setSortKey(str5);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSound(Uri uri) {
                return super.setSound(uri);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSound(Uri uri, int i3) {
                return super.setSound(uri, i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setStyle(NotificationCompat.Style style) {
                return super.setStyle(style);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setSubText(CharSequence charSequence) {
                return super.setSubText(charSequence);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setTicker(CharSequence charSequence) {
                return super.setTicker(charSequence);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setTicker(CharSequence charSequence, RemoteViews remoteViews) {
                return super.setTicker(charSequence, remoteViews);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setTimeoutAfter(long j) {
                return super.setTimeoutAfter(j);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setUsesChronometer(boolean z) {
                return super.setUsesChronometer(z);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setVibrate(long[] jArr) {
                return super.setVibrate(jArr);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setVisibility(int i3) {
                return super.setVisibility(i3);
            }

            @Override // com.serenegiant.notification.NotificationBuilder, androidx.core.app.NotificationCompat.Builder
            public /* bridge */ /* synthetic */ NotificationCompat.Builder setWhen(long j) {
                return super.setWhen(j);
            }

            @Override // com.serenegiant.notification.NotificationBuilder
            protected PendingIntent createContentIntent() {
                IntentFactory intentFactory2 = intentFactory;
                if (intentFactory2 != null) {
                    return intentFactory2.createContentIntent(getContentIntent());
                }
                return null;
            }

            @Override // com.serenegiant.notification.NotificationBuilder
            protected PendingIntent createDeleteIntent() {
                IntentFactory intentFactory2 = intentFactory;
                if (intentFactory2 != null) {
                    return intentFactory2.createDeleteIntent(getDeleteIntent());
                }
                return super.createDeleteIntent();
            }

            @Override // com.serenegiant.notification.NotificationBuilder
            protected PendingIntent createFullScreenIntent() {
                IntentFactory intentFactory2 = intentFactory;
                if (intentFactory2 != null) {
                    return intentFactory2.createFullScreenIntent(getFullScreenIntent());
                }
                return super.createFullScreenIntent();
            }

            @Override // com.serenegiant.notification.NotificationBuilder
            public boolean isHighPriorityFullScreenIntent() {
                boolean isHighPriorityFullScreenIntent = super.isHighPriorityFullScreenIntent();
                IntentFactory intentFactory2 = intentFactory;
                return intentFactory2 != null ? intentFactory2.isHighPriorityFullScreenIntent(isHighPriorityFullScreenIntent) : isHighPriorityFullScreenIntent;
            }
        };
        notificationBuilder.setContentTitle((CharSequence) str3).setContentText((CharSequence) str4);
        if (intentFactory instanceof NotificationBuilderFactory) {
            ((NotificationBuilderFactory) intentFactory).setupBuilder(notificationBuilder);
        }
        notificationBuilder.notify(i);
    }

    public NotificationBuilder(Context context, String str, int i) {
        super(context, str);
        this.mPriority = -3;
        this.mContext = context;
        this.mChannelBuilder = ChannelBuilder.getBuilder(context, str);
        setSmallIcon(i);
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public Notification build() {
        if (this.mChannelBuilder.getImportance() == 0) {
            int i = this.mPriority;
            if (i == -2) {
                this.mChannelBuilder.setImportance(1);
            } else if (i == -1) {
                this.mChannelBuilder.setImportance(2);
            } else if (i == 0) {
                this.mChannelBuilder.setImportance(3);
            } else if (i == 1) {
                this.mChannelBuilder.setImportance(4);
            } else if (i == 2) {
                this.mChannelBuilder.setImportance(4);
            }
        }
        this.mChannelBuilder.build();
        super.setContentIntent(createContentIntent());
        super.setDeleteIntent(createDeleteIntent());
        super.setFullScreenIntent(createFullScreenIntent(), isHighPriorityFullScreenIntent());
        return super.build();
    }

    public NotificationBuilder notify(int i) {
        notify(this.mContext, null, i, build());
        return this;
    }

    public NotificationBuilder notify(String str, int i) {
        notify(this.mContext, str, i, build());
        return this;
    }

    public NotificationBuilder notifyForeground(Service service, int i) {
        return notifyForeground(service, null, i);
    }

    public NotificationBuilder notifyForeground(Service service, String str, int i) {
        Notification build = build();
        service.startForeground(i, build);
        notify(service, str, i, build);
        return this;
    }

    public ChannelBuilder getChannelBuilder() {
        return this.mChannelBuilder;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setChannelId(String str) {
        super.setChannelId(str);
        this.mChannelBuilder.setId(str);
        return this;
    }

    public String getChannelId() {
        return this.mChannelBuilder.getId();
    }

    public NotificationBuilder setChannelName(CharSequence charSequence) {
        this.mChannelBuilder.setName(charSequence);
        return this;
    }

    public CharSequence getChannelName() {
        return this.mChannelBuilder.getName();
    }

    public NotificationBuilder setImportance(int i) {
        this.mChannelBuilder.setImportance(i);
        return this;
    }

    public int getImportance() {
        return this.mChannelBuilder.getImportance();
    }

    public NotificationBuilder setLockscreenVisibility(int i) {
        this.mChannelBuilder.setLockscreenVisibility(i);
        return this;
    }

    public int getLockscreenVisibility() {
        return this.mChannelBuilder.getLockscreenVisibility();
    }

    public NotificationBuilder setBypassDnd(boolean z) {
        this.mChannelBuilder.setBypassDnd(z);
        return this;
    }

    public NotificationBuilder setDescription(String str) {
        this.mChannelBuilder.setDescription(str);
        return this;
    }

    public NotificationBuilder setLightColor(int i) {
        this.mChannelBuilder.setLightColor(i);
        return this;
    }

    public NotificationBuilder enableLights(boolean z) {
        this.mChannelBuilder.enableLights(z);
        return this;
    }

    public NotificationBuilder setShowBadge(boolean z) {
        this.mChannelBuilder.setShowBadge(z);
        return this;
    }

    public NotificationBuilder setVibrationPattern(long[] jArr) {
        this.mChannelBuilder.setVibrationPattern(jArr);
        return this;
    }

    public NotificationBuilder enableVibration(boolean z) {
        this.mChannelBuilder.enableVibration(z);
        return this;
    }

    public NotificationBuilder setSound(Uri uri, AudioAttributes audioAttributes) {
        super.setSound(uri);
        this.mChannelBuilder.setSound(uri, audioAttributes);
        return this;
    }

    public NotificationBuilder setChannelGroup(String str) {
        ChannelBuilder channelBuilder = this.mChannelBuilder;
        channelBuilder.setGroup(str, channelBuilder.getGroupName());
        return this;
    }

    public NotificationBuilder setChannelGroup(String str, String str2) {
        this.mChannelBuilder.setGroup(str, str2);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setContentIntent(PendingIntent pendingIntent) {
        super.setContentIntent(this.mContentIntent);
        this.mContentIntent = pendingIntent;
        return this;
    }

    public PendingIntent getContentIntent() {
        return this.mContentIntent;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setDeleteIntent(PendingIntent pendingIntent) {
        super.setDeleteIntent(pendingIntent);
        this.mDeleteIntent = pendingIntent;
        return this;
    }

    public PendingIntent getDeleteIntent() {
        return this.mDeleteIntent;
    }

    protected PendingIntent createDeleteIntent() {
        return this.mDeleteIntent;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setWhen(long j) {
        super.setWhen(j);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setShowWhen(boolean z) {
        super.setShowWhen(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setUsesChronometer(boolean z) {
        super.setUsesChronometer(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSmallIcon(int i) {
        super.setSmallIcon(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSmallIcon(int i, int i2) {
        super.setSmallIcon(i, i2);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setContentTitle(CharSequence charSequence) {
        super.setContentTitle(charSequence);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setContentText(CharSequence charSequence) {
        super.setContentText(charSequence);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSubText(CharSequence charSequence) {
        super.setSubText(charSequence);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setRemoteInputHistory(CharSequence[] charSequenceArr) {
        super.setRemoteInputHistory(charSequenceArr);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setNumber(int i) {
        super.setNumber(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setContentInfo(CharSequence charSequence) {
        super.setContentInfo(charSequence);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setProgress(int i, int i2, boolean z) {
        super.setProgress(i, i2, z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setContent(RemoteViews remoteViews) {
        super.setContent(remoteViews);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setFullScreenIntent(PendingIntent pendingIntent, boolean z) {
        super.setFullScreenIntent(pendingIntent, z);
        this.mFullScreenIntent = pendingIntent;
        this.mHighPriorityFullScreenIntent = z;
        return this;
    }

    public PendingIntent getFullScreenIntent() {
        return this.mFullScreenIntent;
    }

    public boolean isHighPriorityFullScreenIntent() {
        return this.mHighPriorityFullScreenIntent;
    }

    protected PendingIntent createFullScreenIntent() {
        return this.mFullScreenIntent;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setTicker(CharSequence charSequence) {
        super.setTicker(charSequence);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setTicker(CharSequence charSequence, RemoteViews remoteViews) {
        super.setTicker(charSequence, remoteViews);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setLargeIcon(Bitmap bitmap) {
        super.setLargeIcon(bitmap);
        return this;
    }

    public NotificationBuilder setLargeIcon(int i) {
        super.setLargeIcon(BitmapFactory.decodeResource(this.mContext.getResources(), i));
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSound(Uri uri) {
        super.setSound(uri);
        this.mChannelBuilder.setSound(uri, null);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSound(Uri uri, int i) {
        super.setSound(uri, i);
        this.mChannelBuilder.setSound(uri, BuildCheck.isLollipop() ? new AudioAttributes.Builder().setLegacyStreamType(i).build() : null);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setVibrate(long[] jArr) {
        super.setVibrate(jArr);
        this.mChannelBuilder.setVibrationPattern(jArr);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setLights(int i, int i2, int i3) {
        super.setLights(i, i2, i3);
        this.mChannelBuilder.setLightColor(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setOngoing(boolean z) {
        super.setOngoing(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setColorized(boolean z) {
        super.setColorized(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setOnlyAlertOnce(boolean z) {
        super.setOnlyAlertOnce(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setAutoCancel(boolean z) {
        super.setAutoCancel(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setLocalOnly(boolean z) {
        super.setLocalOnly(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setCategory(String str) {
        super.setCategory(str);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setDefaults(int i) {
        super.setDefaults(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setPriority(int i) {
        super.setPriority(i);
        this.mPriority = i;
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder addPerson(String str) {
        super.addPerson(str);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setGroup(String str) {
        super.setGroup(str);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setGroupSummary(boolean z) {
        super.setGroupSummary(z);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setSortKey(String str) {
        super.setSortKey(str);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder addExtras(Bundle bundle) {
        super.addExtras(bundle);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setExtras(Bundle bundle) {
        super.setExtras(bundle);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder addAction(int i, CharSequence charSequence, PendingIntent pendingIntent) {
        super.addAction(i, charSequence, pendingIntent);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder addAction(NotificationCompat.Action action) {
        super.addAction(action);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setStyle(NotificationCompat.Style style) {
        super.setStyle(style);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setColor(int i) {
        super.setColor(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setVisibility(int i) {
        super.setVisibility(i);
        this.mChannelBuilder.setLockscreenVisibility(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setPublicVersion(Notification notification) {
        super.setPublicVersion(notification);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setCustomContentView(RemoteViews remoteViews) {
        super.setCustomContentView(remoteViews);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setCustomBigContentView(RemoteViews remoteViews) {
        super.setCustomBigContentView(remoteViews);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setCustomHeadsUpContentView(RemoteViews remoteViews) {
        super.setCustomHeadsUpContentView(remoteViews);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setTimeoutAfter(long j) {
        super.setTimeoutAfter(j);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setShortcutId(String str) {
        super.setShortcutId(str);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setBadgeIconType(int i) {
        super.setBadgeIconType(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder setGroupAlertBehavior(int i) {
        super.setGroupAlertBehavior(i);
        return this;
    }

    @Override // androidx.core.app.NotificationCompat.Builder
    public NotificationBuilder extend(NotificationCompat.Extender extender) {
        super.extend(extender);
        return this;
    }

    private static void notify(Context context, String str, int i, Notification notification) {
        NotificationManagerCompat.from(context).notify(str, i, notification);
    }
}
