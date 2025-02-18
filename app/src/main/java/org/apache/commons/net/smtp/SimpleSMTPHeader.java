package org.apache.commons.net.smtp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes2.dex */
public class SimpleSMTPHeader {
    private StringBuffer __cc;
    private final String __from;
    private final StringBuffer __headerFields;
    private final String __subject;
    private final String __to;
    private boolean hasHeaderDate;

    public SimpleSMTPHeader(String str, String str2, String str3) {
        if (str == null) {
            throw new IllegalArgumentException("From cannot be null");
        }
        this.__to = str2;
        this.__from = str;
        this.__subject = str3;
        this.__headerFields = new StringBuffer();
        this.__cc = null;
    }

    public void addHeaderField(String str, String str2) {
        if (!this.hasHeaderDate && "Date".equals(str)) {
            this.hasHeaderDate = true;
        }
        this.__headerFields.append(str);
        this.__headerFields.append(": ");
        this.__headerFields.append(str2);
        this.__headerFields.append('\n');
    }

    public void addCC(String str) {
        StringBuffer stringBuffer = this.__cc;
        if (stringBuffer == null) {
            this.__cc = new StringBuffer();
        } else {
            stringBuffer.append(", ");
        }
        this.__cc.append(str);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        if (!this.hasHeaderDate) {
            addHeaderField("Date", simpleDateFormat.format(new Date()));
        }
        if (this.__headerFields.length() > 0) {
            sb.append(this.__headerFields.toString());
        }
        sb.append("From: ");
        sb.append(this.__from);
        sb.append("\n");
        if (this.__to != null) {
            sb.append("To: ");
            sb.append(this.__to);
            sb.append("\n");
        }
        if (this.__cc != null) {
            sb.append("Cc: ");
            sb.append(this.__cc.toString());
            sb.append("\n");
        }
        if (this.__subject != null) {
            sb.append("Subject: ");
            sb.append(this.__subject);
            sb.append("\n");
        }
        sb.append('\n');
        return sb.toString();
    }
}
