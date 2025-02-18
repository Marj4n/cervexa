package org.apache.commons.net.nntp;

/* loaded from: classes2.dex */
public class SimpleNNTPHeader {
    private final String __from;
    private final String __subject;
    private final StringBuilder __newsgroups = new StringBuilder();
    private final StringBuilder __headerFields = new StringBuilder();
    private int __newsgroupCount = 0;

    public SimpleNNTPHeader(String str, String str2) {
        this.__from = str;
        this.__subject = str2;
    }

    public void addNewsgroup(String str) {
        int i = this.__newsgroupCount;
        this.__newsgroupCount = i + 1;
        if (i > 0) {
            this.__newsgroups.append(',');
        }
        this.__newsgroups.append(str);
    }

    public void addHeaderField(String str, String str2) {
        this.__headerFields.append(str);
        this.__headerFields.append(": ");
        this.__headerFields.append(str2);
        this.__headerFields.append('\n');
    }

    public String getFromAddress() {
        return this.__from;
    }

    public String getSubject() {
        return this.__subject;
    }

    public String getNewsgroups() {
        return this.__newsgroups.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From: ");
        sb.append(this.__from);
        sb.append("\nNewsgroups: ");
        sb.append(this.__newsgroups.toString());
        sb.append("\nSubject: ");
        sb.append(this.__subject);
        sb.append('\n');
        if (this.__headerFields.length() > 0) {
            sb.append(this.__headerFields.toString());
        }
        sb.append('\n');
        return sb.toString();
    }
}
