package com.serenegiant.usb;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import tencent.open.SocialConstants;

/* loaded from: classes2.dex */
public final class DeviceFilter {
    private static final String TAG = "DeviceFilter";
    public final boolean isExclude;
    public final int mClass;
    public final String mManufacturerName;
    public final int mProductId;
    public final String mProductName;
    public final int mProtocol;
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    public DeviceFilter(int i, int i2, int i3, int i4, int i5, String str, String str2, String str3) {
        this(i, i2, i3, i4, i5, str, str2, str3, false);
    }

    public DeviceFilter(int i, int i2, int i3, int i4, int i5, String str, String str2, String str3, boolean z) {
        this.mVendorId = i;
        this.mProductId = i2;
        this.mClass = i3;
        this.mSubclass = i4;
        this.mProtocol = i5;
        this.mManufacturerName = str;
        this.mProductName = str2;
        this.mSerialNumber = str3;
        this.isExclude = z;
    }

    public DeviceFilter(UsbDevice usbDevice) {
        this(usbDevice, false);
    }

    public DeviceFilter(UsbDevice usbDevice, boolean z) {
        this.mVendorId = usbDevice.getVendorId();
        this.mProductId = usbDevice.getProductId();
        this.mClass = usbDevice.getDeviceClass();
        this.mSubclass = usbDevice.getDeviceSubclass();
        this.mProtocol = usbDevice.getDeviceProtocol();
        this.mManufacturerName = null;
        this.mProductName = null;
        this.mSerialNumber = null;
        this.isExclude = z;
    }

    public static List<DeviceFilter> getDeviceFilters(Context context, int i) {
        XmlResourceParser xml = context.getResources().getXml(i);
        ArrayList arrayList = new ArrayList();
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 2) {
                    DeviceFilter readEntryOne = readEntryOne(context, xml);
                    if (readEntryOne != null) {
                        arrayList.add(readEntryOne);
                    }
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        } catch (XmlPullParserException e2) {
            Log.d(TAG, "XmlPullParserException", e2);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static final int getAttributeInteger(Context context, XmlPullParser xmlPullParser, String str, String str2, int i) {
        try {
            String attributeValue = xmlPullParser.getAttributeValue(str, str2);
            if (!TextUtils.isEmpty(attributeValue) && attributeValue.startsWith("@")) {
                int identifier = context.getResources().getIdentifier(attributeValue.substring(1), null, context.getPackageName());
                return identifier > 0 ? context.getResources().getInteger(identifier) : i;
            }
            int i2 = 10;
            if (attributeValue != null && attributeValue.length() > 2 && attributeValue.charAt(0) == '0' && (attributeValue.charAt(1) == 'x' || attributeValue.charAt(1) == 'X')) {
                i2 = 16;
                attributeValue = attributeValue.substring(2);
            }
            return Integer.parseInt(attributeValue, i2);
        } catch (Resources.NotFoundException | NullPointerException | NumberFormatException unused) {
            return i;
        }
    }

    private static final boolean getAttributeBoolean(Context context, XmlPullParser xmlPullParser, String str, String str2, boolean z) {
        try {
            String attributeValue = xmlPullParser.getAttributeValue(str, str2);
            if ("TRUE".equalsIgnoreCase(attributeValue)) {
                return true;
            }
            if ("FALSE".equalsIgnoreCase(attributeValue)) {
                return false;
            }
            if (!TextUtils.isEmpty(attributeValue) && attributeValue.startsWith("@")) {
                int identifier = context.getResources().getIdentifier(attributeValue.substring(1), null, context.getPackageName());
                return identifier > 0 ? context.getResources().getBoolean(identifier) : z;
            }
            int i = 10;
            if (attributeValue != null && attributeValue.length() > 2 && attributeValue.charAt(0) == '0' && (attributeValue.charAt(1) == 'x' || attributeValue.charAt(1) == 'X')) {
                i = 16;
                attributeValue = attributeValue.substring(2);
            }
            return Integer.parseInt(attributeValue, i) != 0;
        } catch (Resources.NotFoundException | NullPointerException | NumberFormatException unused) {
            return z;
        }
    }

    private static final String getAttributeString(Context context, XmlPullParser xmlPullParser, String str, String str2, String str3) {
        try {
            String attributeValue = xmlPullParser.getAttributeValue(str, str2);
            if (attributeValue == null) {
                attributeValue = str3;
            }
            if (!TextUtils.isEmpty(attributeValue) && attributeValue.startsWith("@")) {
                int identifier = context.getResources().getIdentifier(attributeValue.substring(1), null, context.getPackageName());
                if (identifier > 0) {
                    return context.getResources().getString(identifier);
                }
            }
            return attributeValue;
        } catch (Resources.NotFoundException | NullPointerException | NumberFormatException unused) {
            return str3;
        }
    }

    public static DeviceFilter readEntryOne(Context context, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int eventType = xmlPullParser.getEventType();
        String str = null;
        String str2 = null;
        String str3 = null;
        boolean z = false;
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        int i5 = -1;
        boolean z2 = false;
        while (eventType != 1) {
            String name = xmlPullParser.getName();
            if (!TextUtils.isEmpty(name) && name.equalsIgnoreCase("usb-device")) {
                if (eventType == 2) {
                    int attributeInteger = getAttributeInteger(context, xmlPullParser, null, "vendor-id", -1);
                    if (attributeInteger == -1 && (attributeInteger = getAttributeInteger(context, xmlPullParser, null, "vendorId", -1)) == -1) {
                        attributeInteger = getAttributeInteger(context, xmlPullParser, null, "venderId", -1);
                    }
                    i = attributeInteger;
                    int attributeInteger2 = getAttributeInteger(context, xmlPullParser, null, "product-id", -1);
                    if (attributeInteger2 == -1) {
                        attributeInteger2 = getAttributeInteger(context, xmlPullParser, null, "productId", -1);
                    }
                    i2 = attributeInteger2;
                    i3 = getAttributeInteger(context, xmlPullParser, null, "class", -1);
                    i4 = getAttributeInteger(context, xmlPullParser, null, "subclass", -1);
                    i5 = getAttributeInteger(context, xmlPullParser, null, "protocol", -1);
                    String attributeString = getAttributeString(context, xmlPullParser, null, "manufacturer-name", null);
                    if (TextUtils.isEmpty(attributeString)) {
                        attributeString = getAttributeString(context, xmlPullParser, null, "manufacture", null);
                    }
                    str = attributeString;
                    String attributeString2 = getAttributeString(context, xmlPullParser, null, "product-name", null);
                    if (TextUtils.isEmpty(attributeString2)) {
                        attributeString2 = getAttributeString(context, xmlPullParser, null, "product", null);
                    }
                    str2 = attributeString2;
                    String attributeString3 = getAttributeString(context, xmlPullParser, null, "serial-number", null);
                    if (TextUtils.isEmpty(attributeString3)) {
                        attributeString3 = getAttributeString(context, xmlPullParser, null, "serial", null);
                    }
                    str3 = attributeString3;
                    z2 = getAttributeBoolean(context, xmlPullParser, null, SocialConstants.PARAM_EXCLUDE, false);
                    z = true;
                } else if (eventType == 3 && z) {
                    return new DeviceFilter(i, i2, i3, i4, i5, str, str2, str3, z2);
                }
            }
            eventType = xmlPullParser.next();
        }
        return null;
    }

    private boolean matches(int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = this.mClass;
        return (i6 == -1 || i == i6) && ((i4 = this.mSubclass) == -1 || i2 == i4) && ((i5 = this.mProtocol) == -1 || i3 == i5);
    }

    public boolean matches(UsbDevice usbDevice) {
        if (this.mVendorId != -1 && usbDevice.getVendorId() != this.mVendorId) {
            return false;
        }
        if (this.mProductId != -1 && usbDevice.getProductId() != this.mProductId) {
            return false;
        }
        if (matches(usbDevice.getDeviceClass(), usbDevice.getDeviceSubclass(), usbDevice.getDeviceProtocol())) {
            return true;
        }
        int interfaceCount = usbDevice.getInterfaceCount();
        for (int i = 0; i < interfaceCount; i++) {
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (matches(usbInterface.getInterfaceClass(), usbInterface.getInterfaceSubclass(), usbInterface.getInterfaceProtocol())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExclude(UsbDevice usbDevice) {
        return this.isExclude && matches(usbDevice);
    }

    public boolean matches(DeviceFilter deviceFilter) {
        String str;
        String str2;
        String str3;
        if (this.isExclude != deviceFilter.isExclude) {
            return false;
        }
        int i = this.mVendorId;
        if (i != -1 && deviceFilter.mVendorId != i) {
            return false;
        }
        int i2 = this.mProductId;
        if (i2 != -1 && deviceFilter.mProductId != i2) {
            return false;
        }
        if (deviceFilter.mManufacturerName != null && this.mManufacturerName == null) {
            return false;
        }
        if (deviceFilter.mProductName != null && this.mProductName == null) {
            return false;
        }
        if (deviceFilter.mSerialNumber != null && this.mSerialNumber == null) {
            return false;
        }
        String str4 = this.mManufacturerName;
        if (str4 != null && (str3 = deviceFilter.mManufacturerName) != null && !str4.equals(str3)) {
            return false;
        }
        String str5 = this.mProductName;
        if (str5 != null && (str2 = deviceFilter.mProductName) != null && !str5.equals(str2)) {
            return false;
        }
        String str6 = this.mSerialNumber;
        if (str6 == null || (str = deviceFilter.mSerialNumber) == null || str6.equals(str)) {
            return matches(deviceFilter.mClass, deviceFilter.mSubclass, deviceFilter.mProtocol);
        }
        return false;
    }

    public boolean equals(Object obj) {
        int i;
        int i2;
        int i3;
        int i4;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        int i5 = this.mVendorId;
        if (i5 != -1 && (i = this.mProductId) != -1 && (i2 = this.mClass) != -1 && (i3 = this.mSubclass) != -1 && (i4 = this.mProtocol) != -1) {
            if (obj instanceof DeviceFilter) {
                DeviceFilter deviceFilter = (DeviceFilter) obj;
                if (deviceFilter.mVendorId != i5 || deviceFilter.mProductId != i || deviceFilter.mClass != i2 || deviceFilter.mSubclass != i3 || deviceFilter.mProtocol != i4) {
                    return false;
                }
                if ((deviceFilter.mManufacturerName != null && this.mManufacturerName == null) || ((deviceFilter.mManufacturerName == null && this.mManufacturerName != null) || ((deviceFilter.mProductName != null && this.mProductName == null) || ((deviceFilter.mProductName == null && this.mProductName != null) || ((deviceFilter.mSerialNumber != null && this.mSerialNumber == null) || (deviceFilter.mSerialNumber == null && this.mSerialNumber != null)))))) {
                    return false;
                }
                String str6 = deviceFilter.mManufacturerName;
                return (str6 == null || (str5 = this.mManufacturerName) == null || str5.equals(str6)) && ((str = deviceFilter.mProductName) == null || (str4 = this.mProductName) == null || str4.equals(str)) && (((str2 = deviceFilter.mSerialNumber) == null || (str3 = this.mSerialNumber) == null || str3.equals(str2)) && deviceFilter.isExclude != this.isExclude);
            }
            if (obj instanceof UsbDevice) {
                UsbDevice usbDevice = (UsbDevice) obj;
                if (!this.isExclude && usbDevice.getVendorId() == this.mVendorId && usbDevice.getProductId() == this.mProductId && usbDevice.getDeviceClass() == this.mClass && usbDevice.getDeviceSubclass() == this.mSubclass && usbDevice.getDeviceProtocol() == this.mProtocol) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        return "DeviceFilter[mVendorId=" + this.mVendorId + ",mProductId=" + this.mProductId + ",mClass=" + this.mClass + ",mSubclass=" + this.mSubclass + ",mProtocol=" + this.mProtocol + ",mManufacturerName=" + this.mManufacturerName + ",mProductName=" + this.mProductName + ",mSerialNumber=" + this.mSerialNumber + ",isExclude=" + this.isExclude + "]";
    }
}
