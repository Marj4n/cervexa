package com.serenegiant.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.generalplus.GoPlusDrone.Fragment.BaseFragment;
import com.serenegiant.utils.BuildCheck;
import com.serenegiant.utils.ObjectHelper;
import com.serenegiant.utils.XmlHelper;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes2.dex */
public class ChannelBuilder {
    private static final boolean DEBUG = false;
    public static final String DEFAULT_CHANNEL_ID = "miscellaneous";
    public static final Set<Integer> IMPORTANCE;
    public static final Set<Integer> NOTIFICATION_VISIBILITY;
    private static final String TAG = ChannelBuilder.class.getSimpleName();
    private int argb;
    private AudioAttributes audioAttributes;
    private boolean bypassDnd;
    private String channelId;
    private boolean createIfExists;
    private String description;
    private String groupId;
    private String groupName;
    private int importance;
    private boolean lights;
    private int lockscreenVisibility;
    private final Context mContext;
    private CharSequence name;
    private boolean showBadge;
    private Uri sound;
    private boolean vibration;
    private long[] vibrationPattern;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationVisibility {
    }

    protected NotificationChannel setupNotificationChannel(NotificationChannel notificationChannel) {
        return notificationChannel;
    }

    protected NotificationChannelGroup setupNotificationChannelGroup(NotificationChannelGroup notificationChannelGroup) {
        return notificationChannelGroup;
    }

    static {
        HashSet hashSet = new HashSet();
        IMPORTANCE = hashSet;
        Collections.addAll(hashSet, Integer.valueOf(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED), 0, 1, 2, 3, 4);
        HashSet hashSet2 = new HashSet();
        NOTIFICATION_VISIBILITY = hashSet2;
        Collections.addAll(hashSet2, 1, 0, -1);
    }

    public static ChannelBuilder getBuilder(Context context, String str) {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getNotificationChannel(str);
        }
        if (notificationChannel != null) {
            ChannelBuilder channelBuilder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelBuilder = new ChannelBuilder(context, str, notificationChannel.getName(), notificationChannel.getImportance());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channelBuilder.setLockscreenVisibility(notificationChannel.getLockscreenVisibility()).setBypassDnd(notificationChannel.canBypassDnd()).setShowBadge(notificationChannel.canShowBadge()).setDescription(notificationChannel.getDescription()).setLightColor(notificationChannel.getLightColor()).setVibrationPattern(notificationChannel.getVibrationPattern()).enableLights(notificationChannel.shouldShowLights()).enableVibration(notificationChannel.shouldVibrate()).setSound(notificationChannel.getSound(), notificationChannel.getAudioAttributes()).setGroup(notificationChannel.getGroup(), null).setCreateIfExists(true);
            }
            return channelBuilder;
        }
        return new ChannelBuilder(context, str, null, 0);
    }

    public static List<String> updateNotificationChannel(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        XmlResourceParser xml = context.getResources().getXml(i);
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 2) {
                    readEntryOne(context, xml, arrayList);
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        } catch (XmlPullParserException e2) {
            Log.d(TAG, "XmlPullParserException", e2);
        }
        return arrayList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static void readEntryOne(Context context, XmlPullParser xmlPullParser, List<String> list) throws XmlPullParserException, IOException {
        String str;
        ChannelBuilder channelBuilder;
        String str2;
        String str3;
        int i;
        int i2;
        char c;
        String str4;
        String str5;
        String str6;
        int eventType = xmlPullParser.getEventType();
        String str7 = null;
        ChannelBuilder channelBuilder2 = null;
        while (eventType != 1) {
            String name = xmlPullParser.getName();
            if (TextUtils.isEmpty(name) || !name.equalsIgnoreCase("notificationChannel")) {
                str = str7;
            } else if (eventType == 2) {
                String str8 = "";
                String attribute = XmlHelper.getAttribute(context, xmlPullParser, str7, "channelId", "");
                if (TextUtils.isEmpty(attribute)) {
                    str = str7;
                } else {
                    ChannelBuilder createIfExists = getBuilder(context, attribute).setCreateIfExists(false);
                    int i3 = 0;
                    for (int attributeCount = xmlPullParser.getAttributeCount(); i3 < attributeCount; attributeCount = i) {
                        String attributeName = xmlPullParser.getAttributeName(i3);
                        if (TextUtils.isEmpty(attributeName)) {
                            channelBuilder = createIfExists;
                            str2 = str7;
                            str3 = str8;
                            i = attributeCount;
                            i2 = i3;
                        } else {
                            i = attributeCount;
                            i2 = i3;
                            String str9 = str8;
                            ChannelBuilder channelBuilder3 = createIfExists;
                            int i4 = -1;
                            switch (attributeName.hashCode()) {
                                case -1930680666:
                                    if (attributeName.equals("showBadge")) {
                                        c = 4;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case -1898049152:
                                    if (attributeName.equals("enableLights")) {
                                        c = 7;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case -1724546052:
                                    if (attributeName.equals("description")) {
                                        c = 5;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case -1402300654:
                                    if (attributeName.equals("bypassDnd")) {
                                        c = 3;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 3373707:
                                    if (attributeName.equals(BaseFragment.DATA_NAME)) {
                                        c = 0;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 102970646:
                                    if (attributeName.equals("light")) {
                                        c = 6;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 109627663:
                                    if (attributeName.equals("sound")) {
                                        c = '\n';
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 906619038:
                                    if (attributeName.equals("vibrationPattern")) {
                                        c = '\b';
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 1363834825:
                                    if (attributeName.equals("lockscreenVisibility")) {
                                        c = 2;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 1654598901:
                                    if (attributeName.equals("createIfExists")) {
                                        c = 11;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 1988784207:
                                    if (attributeName.equals("enableVibration")) {
                                        c = '\t';
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                case 2125650548:
                                    if (attributeName.equals("importance")) {
                                        c = 1;
                                        break;
                                    }
                                    c = 65535;
                                    break;
                                default:
                                    c = 65535;
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    str3 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setName(XmlHelper.getAttribute(context, xmlPullParser, (String) null, BaseFragment.DATA_NAME, channelBuilder.getName()));
                                    break;
                                case 1:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setImportance(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "importance", channelBuilder.getImportance()));
                                    str3 = str4;
                                    break;
                                case 2:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    int attribute2 = XmlHelper.getAttribute(context, xmlPullParser, (String) null, "lockscreenVisibility", channelBuilder.getLockscreenVisibility());
                                    if (NOTIFICATION_VISIBILITY.contains(Integer.valueOf(attribute2))) {
                                        channelBuilder.setLockscreenVisibility(attribute2);
                                    }
                                    str3 = str4;
                                    break;
                                case 3:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setBypassDnd(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "bypassDnd", channelBuilder.canBypassDnd()));
                                    str3 = str4;
                                    break;
                                case 4:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setShowBadge(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "showBadge", channelBuilder.canShowBadge()));
                                    str3 = str4;
                                    break;
                                case 5:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setDescription(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "description", channelBuilder.getDescription()));
                                    str3 = str4;
                                    break;
                                case 6:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.setLightColor(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "light", channelBuilder.getLightColor()));
                                    channelBuilder.enableLights(XmlHelper.getAttribute(context, xmlPullParser, str2, "enableLights", channelBuilder.shouldShowLights()));
                                    str3 = str4;
                                    break;
                                case 7:
                                    str4 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    channelBuilder.enableLights(XmlHelper.getAttribute(context, xmlPullParser, str2, "enableLights", channelBuilder.shouldShowLights()));
                                    str3 = str4;
                                    break;
                                case '\b':
                                    channelBuilder = channelBuilder3;
                                    String attribute3 = XmlHelper.getAttribute(context, xmlPullParser, (String) null, "vibrationPattern", str9);
                                    if (!TextUtils.isEmpty(attribute3)) {
                                        String[] split = attribute3.trim().split(",");
                                        if (split.length > 0) {
                                            long[] jArr = new long[split.length];
                                            for (String str10 : split) {
                                                i4++;
                                                jArr[i4] = ObjectHelper.asLong(str10, 0L);
                                            }
                                            if (i4 >= 0) {
                                                channelBuilder.setVibrationPattern(Arrays.copyOf(jArr, i4 + 1));
                                            }
                                        }
                                    }
                                    str3 = str9;
                                    str2 = null;
                                    break;
                                case '\t':
                                    str5 = str9;
                                    channelBuilder = channelBuilder3;
                                    str6 = null;
                                    channelBuilder.enableVibration(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "enableVibration", channelBuilder.shouldVibrate()));
                                    str3 = str5;
                                    str2 = str6;
                                    break;
                                case '\n':
                                    str5 = str9;
                                    channelBuilder = channelBuilder3;
                                    str6 = null;
                                    String attribute4 = XmlHelper.getAttribute(context, xmlPullParser, (String) null, "sound", str5);
                                    if (!TextUtils.isEmpty(attribute4)) {
                                        channelBuilder.setSound(Uri.parse(attribute4), null);
                                    }
                                    str3 = str5;
                                    str2 = str6;
                                    break;
                                case 11:
                                    channelBuilder = channelBuilder3;
                                    channelBuilder.setCreateIfExists(XmlHelper.getAttribute(context, xmlPullParser, (String) null, "createIfExists", false));
                                    str2 = null;
                                    str3 = str9;
                                    break;
                                default:
                                    str3 = str9;
                                    channelBuilder = channelBuilder3;
                                    str2 = null;
                                    break;
                            }
                            i3 = i2 + 1;
                            str8 = str3;
                            str7 = str2;
                            createIfExists = channelBuilder;
                        }
                        i3 = i2 + 1;
                        str8 = str3;
                        str7 = str2;
                        createIfExists = channelBuilder;
                    }
                    str = str7;
                    channelBuilder2 = createIfExists;
                }
            } else {
                str = str7;
                if (eventType == 3 && channelBuilder2 != null) {
                    channelBuilder2.build();
                    list.add(channelBuilder2.getId());
                    return;
                }
            }
            eventType = xmlPullParser.next();
            str7 = str;
        }
    }

    public ChannelBuilder(Context context) {
        this(context, DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_ID, 0, null, null);
    }

    public ChannelBuilder(Context context, String str, CharSequence charSequence, int i) {
        this(context, str, charSequence, i, null, null);
    }

    public ChannelBuilder(Context context, String str, CharSequence charSequence, int i, String str2, String str3) {
        this.channelId = DEFAULT_CHANNEL_ID;
        this.importance = 0;
        this.lockscreenVisibility = 0;
        this.bypassDnd = false;
        this.showBadge = true;
        this.argb = 0;
        this.createIfExists = true;
        this.mContext = context;
        this.channelId = TextUtils.isEmpty(str) ? DEFAULT_CHANNEL_ID : str;
        this.name = charSequence;
        this.importance = i;
        this.groupId = str2;
        this.groupName = str3;
    }

    public String toString() {
        return "ChannelBuilder{channelId='" + this.channelId + "', name=" + ((Object) this.name) + ", importance=" + this.importance + ", lockscreenVisibility=" + this.lockscreenVisibility + ", bypassDnd=" + this.bypassDnd + ", showBadge=" + this.showBadge + ", description='" + this.description + "', argb=" + this.argb + ", lights=" + this.lights + ", vibrationPattern=" + Arrays.toString(this.vibrationPattern) + ", vibration=" + this.vibration + ", sound=" + this.sound + ", audioAttributes=" + this.audioAttributes + ", groupId='" + this.groupId + "', groupName='" + this.groupName + "', createIfExists=" + this.createIfExists + '}';
    }

    public NotificationChannel build() {
        if (BuildCheck.isOreo()) {
            return createNotificationChannel(this.mContext);
        }
        return null;
    }

    public ChannelBuilder setId(String str) {
        if (TextUtils.isEmpty(str)) {
            str = DEFAULT_CHANNEL_ID;
        }
        this.channelId = str;
        return this;
    }

    public String getId() {
        return this.channelId;
    }

    public ChannelBuilder setName(CharSequence charSequence) {
        this.name = charSequence;
        return this;
    }

    public CharSequence getName() {
        return this.name;
    }

    public ChannelBuilder setImportance(int i) {
        this.importance = i;
        return this;
    }

    public int getImportance() {
        return this.importance;
    }

    public ChannelBuilder setLockscreenVisibility(int i) {
        this.lockscreenVisibility = i;
        return this;
    }

    public int getLockscreenVisibility() {
        return this.lockscreenVisibility;
    }

    public ChannelBuilder setBypassDnd(boolean z) {
        this.bypassDnd = z;
        return this;
    }

    public boolean canBypassDnd() {
        return this.bypassDnd;
    }

    public ChannelBuilder setDescription(String str) {
        this.description = str;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public ChannelBuilder setLightColor(int i) {
        this.argb = i;
        return this;
    }

    public int getLightColor() {
        return this.argb;
    }

    public ChannelBuilder enableLights(boolean z) {
        this.lights = z;
        return this;
    }

    public boolean shouldShowLights() {
        return this.lights;
    }

    public ChannelBuilder setShowBadge(boolean z) {
        this.showBadge = z;
        return this;
    }

    public boolean canShowBadge() {
        return this.showBadge;
    }

    public ChannelBuilder setVibrationPattern(long[] jArr) {
        this.vibration = jArr != null && jArr.length > 0;
        this.vibrationPattern = jArr;
        return this;
    }

    public long[] getVibrationPattern() {
        return this.vibrationPattern;
    }

    public ChannelBuilder enableVibration(boolean z) {
        this.vibration = z;
        return this;
    }

    public boolean shouldVibrate() {
        long[] jArr;
        return this.vibration && (jArr = this.vibrationPattern) != null && jArr.length > 0;
    }

    public ChannelBuilder setSound(Uri uri, AudioAttributes audioAttributes) {
        this.sound = uri;
        this.audioAttributes = audioAttributes;
        return this;
    }

    public Uri getSound() {
        return this.sound;
    }

    public AudioAttributes getAudioAttributes() {
        return this.audioAttributes;
    }

    public ChannelBuilder setGroup(String str, String str2) {
        this.groupId = str;
        this.groupName = str2;
        return this;
    }

    public String getGroup() {
        return this.groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public ChannelBuilder setCreateIfExists(boolean z) {
        this.createIfExists = z;
        return this;
    }

    public boolean isCreateIfExists() {
        return this.createIfExists;
    }

    protected NotificationChannel createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = notificationManager.getNotificationChannel(this.channelId);
        }
        if (this.createIfExists || notificationChannel == null) {
            if (this.importance == 0) {
                this.importance = 3;
            }
            if (notificationChannel == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(this.channelId, this.name, this.importance);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setImportance(this.importance);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setLockscreenVisibility(this.lockscreenVisibility);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setBypassDnd(this.bypassDnd);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setShowBadge(this.showBadge);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setLightColor(this.argb);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.enableLights(this.lights);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setVibrationPattern(this.vibrationPattern);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.enableVibration(this.vibration);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setSound(this.sound, this.audioAttributes);
            }
        }
        if (notificationChannel != null) {
            if (!TextUtils.isEmpty(this.groupId)) {
                createNotificationChannelGroup(context, this.groupId, this.groupName);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setName(this.name);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setDescription(this.description);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel.setGroup(this.groupId);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(setupNotificationChannel(notificationChannel));
            }
        }
        return notificationChannel;
    }

    protected void createNotificationChannelGroup(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannelGroup notificationChannelGroup = null;
        Iterator<NotificationChannelGroup> it = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it = notificationManager.getNotificationChannelGroups().iterator();
        }
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            NotificationChannelGroup next = it.next();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (str.equals(next.getId())) {
                    notificationChannelGroup = next;
                    break;
                }
            }
        }
        if (notificationChannelGroup == null) {
            if (TextUtils.isEmpty(str2)) {
                str2 = str;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannelGroup(setupNotificationChannelGroup(new NotificationChannelGroup(str, str2)));
            }
        }
    }
}
