package org.apache.commons.net.ftp.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

/* loaded from: classes2.dex */
public class VMSFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "d-MMM-yyyy HH:mm:ss";
    private static final String REGEX = "(.*?;[0-9]+)\\s*(\\d+)/\\d+\\s*(\\S+)\\s+(\\S+)\\s+\\[(([0-9$A-Za-z_]+)|([0-9$A-Za-z_]+),([0-9$a-zA-Z_]+))\\]?\\s*\\([a-zA-Z]*,([a-zA-Z]*),([a-zA-Z]*),([a-zA-Z]*)\\)";

    protected boolean isVersioning() {
        return false;
    }

    public VMSFTPEntryParser() {
        this(null);
    }

    public VMSFTPEntryParser(FTPClientConfig fTPClientConfig) {
        super(REGEX);
        configure(fTPClientConfig);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String str) {
        String nextToken;
        String str2 = null;
        if (!matches(str)) {
            return null;
        }
        FTPFile fTPFile = new FTPFile();
        fTPFile.setRawListing(str);
        String group = group(1);
        String group2 = group(2);
        String str3 = group(3) + " " + group(4);
        String group3 = group(5);
        String[] strArr = {group(9), group(10), group(11)};
        try {
            fTPFile.setTimestamp(super.parseTimestamp(str3));
        } catch (ParseException unused) {
        }
        StringTokenizer stringTokenizer = new StringTokenizer(group3, ",");
        int countTokens = stringTokenizer.countTokens();
        if (countTokens == 1) {
            nextToken = stringTokenizer.nextToken();
        } else if (countTokens != 2) {
            nextToken = null;
        } else {
            str2 = stringTokenizer.nextToken();
            nextToken = stringTokenizer.nextToken();
        }
        if (group.lastIndexOf(".DIR") != -1) {
            fTPFile.setType(1);
        } else {
            fTPFile.setType(0);
        }
        if (isVersioning()) {
            fTPFile.setName(group);
        } else {
            fTPFile.setName(group.substring(0, group.lastIndexOf(";")));
        }
        fTPFile.setSize(Long.parseLong(group2) * 512);
        fTPFile.setGroup(str2);
        fTPFile.setUser(nextToken);
        for (int i = 0; i < 3; i++) {
            String str4 = strArr[i];
            fTPFile.setPermission(i, 0, str4.indexOf(82) >= 0);
            fTPFile.setPermission(i, 1, str4.indexOf(87) >= 0);
            fTPFile.setPermission(i, 2, str4.indexOf(69) >= 0);
        }
        return fTPFile;
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParserImpl, org.apache.commons.net.ftp.FTPFileEntryParser
    public String readNextEntry(BufferedReader bufferedReader) throws IOException {
        String readLine = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();
        while (readLine != null) {
            if (readLine.startsWith("Directory") || readLine.startsWith("Total")) {
                readLine = bufferedReader.readLine();
            } else {
                sb.append(readLine);
                if (readLine.trim().endsWith(")")) {
                    break;
                }
                readLine = bufferedReader.readLine();
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_VMS, DEFAULT_DATE_FORMAT, null, null, null, null);
    }

    @Deprecated
    public FTPFile[] parseFileList(InputStream inputStream) throws IOException {
        FTPListParseEngine fTPListParseEngine = new FTPListParseEngine(this);
        fTPListParseEngine.readServerList(inputStream, null);
        return fTPListParseEngine.getFiles();
    }
}
