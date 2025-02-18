package org.apache.commons.net.ftp.parser;

import java.util.Calendar;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: classes2.dex */
public class EnterpriseUnixFTPEntryParser extends RegexFTPFileEntryParserImpl {
    private static final String MONTHS = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
    private static final String REGEX = "(([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z]))(\\S*)\\s*(\\S+)\\s*(\\S*)\\s*(\\d*)\\s*(\\d*)\\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*((?:[012]\\d*)|(?:3[01]))\\s*((\\d\\d\\d\\d)|((?:[01]\\d)|(?:2[0123])):([012345]\\d))\\s(\\S*)(\\s*.*)";

    public EnterpriseUnixFTPEntryParser() {
        super(REGEX);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String str) {
        FTPFile fTPFile = new FTPFile();
        fTPFile.setRawListing(str);
        if (!matches(str)) {
            return null;
        }
        String group = group(14);
        String group2 = group(15);
        String group3 = group(16);
        String group4 = group(17);
        String group5 = group(18);
        String group6 = group(20);
        String group7 = group(21);
        String group8 = group(22);
        String group9 = group(23);
        fTPFile.setType(0);
        fTPFile.setUser(group);
        fTPFile.setGroup(group2);
        try {
            fTPFile.setSize(Long.parseLong(group3));
        } catch (NumberFormatException unused) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(14, 0);
        int i = 13;
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(11, 0);
        int indexOf = MONTHS.indexOf(group4) / 4;
        try {
            if (group6 != null) {
                calendar.set(1, Integer.parseInt(group6));
                i = 11;
            } else {
                int i2 = calendar.get(1);
                if (calendar.get(2) < indexOf) {
                    i2--;
                }
                calendar.set(1, i2);
                calendar.set(11, Integer.parseInt(group7));
                calendar.set(12, Integer.parseInt(group8));
            }
            calendar.set(2, indexOf);
            calendar.set(5, Integer.parseInt(group5));
            calendar.clear(i);
            fTPFile.setTimestamp(calendar);
        } catch (NumberFormatException unused2) {
        }
        fTPFile.setName(group9);
        return fTPFile;
    }
}
