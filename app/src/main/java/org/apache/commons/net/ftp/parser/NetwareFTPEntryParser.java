package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: classes2.dex */
public class NetwareFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "MMM dd yyyy";
    private static final String DEFAULT_RECENT_DATE_FORMAT = "MMM dd HH:mm";
    private static final String REGEX = "(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)";

    public NetwareFTPEntryParser() {
        this(null);
    }

    public NetwareFTPEntryParser(FTPClientConfig fTPClientConfig) {
        super(REGEX);
        configure(fTPClientConfig);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String str) {
        FTPFile fTPFile = new FTPFile();
        if (!matches(str)) {
            return null;
        }
        String group = group(1);
        String group2 = group(2);
        String group3 = group(3);
        String group4 = group(4);
        String group5 = group(5);
        String group6 = group(9);
        try {
            fTPFile.setTimestamp(super.parseTimestamp(group5));
        } catch (ParseException unused) {
        }
        if (group.trim().equals("d")) {
            fTPFile.setType(1);
        } else {
            fTPFile.setType(0);
        }
        fTPFile.setUser(group3);
        fTPFile.setName(group6.trim());
        fTPFile.setSize(Long.parseLong(group4.trim()));
        if (group2.indexOf("R") != -1) {
            fTPFile.setPermission(0, 0, true);
        }
        if (group2.indexOf("W") != -1) {
            fTPFile.setPermission(0, 1, true);
        }
        return fTPFile;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_NETWARE, DEFAULT_DATE_FORMAT, DEFAULT_RECENT_DATE_FORMAT, null, null, null);
    }
}
