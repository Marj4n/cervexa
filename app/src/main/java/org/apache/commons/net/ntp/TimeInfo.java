package org.apache.commons.net.ntp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TimeInfo {
    private List<String> _comments;
    private Long _delay;
    private boolean _detailsComputed;
    private final NtpV3Packet _message;
    private Long _offset;
    private final long _returnTime;

    public TimeInfo(NtpV3Packet ntpV3Packet, long j) {
        this(ntpV3Packet, j, null, true);
    }

    public TimeInfo(NtpV3Packet ntpV3Packet, long j, List<String> list) {
        this(ntpV3Packet, j, list, true);
    }

    public TimeInfo(NtpV3Packet ntpV3Packet, long j, boolean z) {
        this(ntpV3Packet, j, null, z);
    }

    public TimeInfo(NtpV3Packet ntpV3Packet, long j, List<String> list, boolean z) {
        if (ntpV3Packet == null) {
            throw new IllegalArgumentException("message cannot be null");
        }
        this._returnTime = j;
        this._message = ntpV3Packet;
        this._comments = list;
        if (z) {
            computeDetails();
        }
    }

    public void addComment(String str) {
        if (this._comments == null) {
            this._comments = new ArrayList();
        }
        this._comments.add(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void computeDetails() {
        /*
            Method dump skipped, instructions count: 258
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.net.ntp.TimeInfo.computeDetails():void");
    }

    public List<String> getComments() {
        return this._comments;
    }

    public Long getDelay() {
        return this._delay;
    }

    public Long getOffset() {
        return this._offset;
    }

    public NtpV3Packet getMessage() {
        return this._message;
    }

    public InetAddress getAddress() {
        DatagramPacket datagramPacket = this._message.getDatagramPacket();
        if (datagramPacket == null) {
            return null;
        }
        return datagramPacket.getAddress();
    }

    public long getReturnTime() {
        return this._returnTime;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeInfo timeInfo = (TimeInfo) obj;
        return this._returnTime == timeInfo._returnTime && this._message.equals(timeInfo._message);
    }

    public int hashCode() {
        return (((int) this._returnTime) * 31) + this._message.hashCode();
    }
}
