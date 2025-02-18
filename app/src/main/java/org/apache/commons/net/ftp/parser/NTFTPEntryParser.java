package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: classes2.dex */
public class NTFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy hh:mma";
    private static final String DEFAULT_DATE_FORMAT2 = "MM-dd-yy kk:mm";
    private static final String REGEX = "(\\S+)\\s+(\\S+)\\s+(?:(<DIR>)|([0-9]+))\\s+(\\S.*)";
    private final FTPTimestampParser timestampParser;

    public NTFTPEntryParser() {
        this(null);
    }

    public NTFTPEntryParser(FTPClientConfig fTPClientConfig) {
        super(REGEX, 32);
        configure(fTPClientConfig);
        FTPClientConfig fTPClientConfig2 = new FTPClientConfig(FTPClientConfig.SYST_NT, DEFAULT_DATE_FORMAT2, null, null, null, null);
        fTPClientConfig2.setDefaultDateFormatStr(DEFAULT_DATE_FORMAT2);
        FTPTimestampParserImpl fTPTimestampParserImpl = new FTPTimestampParserImpl();
        this.timestampParser = fTPTimestampParserImpl;
        fTPTimestampParserImpl.configure(fTPClientConfig2);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String str) {
        FTPFile fTPFile = new FTPFile();
        fTPFile.setRawListing(str);
        if (matches(str)) {
            String str2 = group(1) + " " + group(2);
            String group = group(3);
            String group2 = group(4);
            String group3 = group(5);
            try {
                try {
                    fTPFile.setTimestamp(super.parseTimestamp(str2));
                } catch (ParseException unused) {
                    fTPFile.setTimestamp(this.timestampParser.parseTimestamp(str2));
                }
            } catch (ParseException unused2) {
            }
            if (group3 != null && !group3.equals(".") && !group3.equals("..")) {
                fTPFile.setName(group3);
                if ("<DIR>".equals(group)) {
                    fTPFile.setType(1);
                    fTPFile.setSize(0L);
                } else {
                    fTPFile.setType(0);
                    if (group2 != null) {
                        fTPFile.setSize(Long.parseLong(group2));
                    }
                }
                return fTPFile;
            }
        }
        return null;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    public FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_NT, DEFAULT_DATE_FORMAT, null, null, null, null);
    }
}
