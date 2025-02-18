package org.apache.commons.net.ftp.parser;

import com.jieli.lib.dv.control.utils.TopicKey;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: classes2.dex */
public class MVSFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm";
    static final String FILE_LIST_REGEX = "\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*";
    static final int FILE_LIST_TYPE = 0;
    static final String JES_LEVEL_1_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*";
    static final int JES_LEVEL_1_LIST_TYPE = 3;
    static final String JES_LEVEL_2_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";
    static final int JES_LEVEL_2_LIST_TYPE = 4;
    static final String MEMBER_LIST_REGEX = "(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*";
    static final int MEMBER_LIST_TYPE = 1;
    static final int UNIX_LIST_TYPE = 2;
    static final int UNKNOWN_LIST_TYPE = -1;
    private int isType;
    private UnixFTPEntryParser unixFTPEntryParser;

    public MVSFTPEntryParser() {
        super("");
        this.isType = -1;
        super.configure(null);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String str) {
        boolean parseJeslevel2List;
        FTPFile fTPFile = new FTPFile();
        int i = this.isType;
        if (i == 0) {
            parseJeslevel2List = parseFileList(fTPFile, str);
        } else if (i == 1) {
            boolean parseMemberList = parseMemberList(fTPFile, str);
            parseJeslevel2List = !parseMemberList ? parseSimpleEntry(fTPFile, str) : parseMemberList;
        } else if (i == 2) {
            parseJeslevel2List = parseUnixList(fTPFile, str);
        } else if (i == 3) {
            parseJeslevel2List = parseJeslevel1List(fTPFile, str);
        } else {
            parseJeslevel2List = i == 4 ? parseJeslevel2List(fTPFile, str) : false;
        }
        if (parseJeslevel2List) {
            return fTPFile;
        }
        return null;
    }

    private boolean parseFileList(FTPFile fTPFile, String str) {
        if (!matches(str)) {
            return false;
        }
        fTPFile.setRawListing(str);
        String group = group(2);
        String group2 = group(1);
        fTPFile.setName(group);
        if ("PS".equals(group2)) {
            fTPFile.setType(0);
        } else {
            if (!"PO".equals(group2) && !"PO-E".equals(group2)) {
                return false;
            }
            fTPFile.setType(1);
        }
        return true;
    }

    private boolean parseMemberList(FTPFile fTPFile, String str) {
        if (matches(str)) {
            fTPFile.setRawListing(str);
            String group = group(1);
            String str2 = group(2) + " " + group(3);
            fTPFile.setName(group);
            fTPFile.setType(0);
            try {
                fTPFile.setTimestamp(super.parseTimestamp(str2));
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean parseSimpleEntry(FTPFile fTPFile, String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }
        fTPFile.setRawListing(str);
        fTPFile.setName(str.split(" ")[0]);
        fTPFile.setType(0);
        return true;
    }

    private boolean parseUnixList(FTPFile fTPFile, String str) {
        return this.unixFTPEntryParser.parseFTPEntry(str) != null;
    }

    private boolean parseJeslevel1List(FTPFile fTPFile, String str) {
        if (!matches(str) || !group(3).equalsIgnoreCase("OUTPUT")) {
            return false;
        }
        fTPFile.setRawListing(str);
        fTPFile.setName(group(2));
        fTPFile.setType(0);
        return true;
    }

    private boolean parseJeslevel2List(FTPFile fTPFile, String str) {
        if (!matches(str) || !group(4).equalsIgnoreCase("OUTPUT")) {
            return false;
        }
        fTPFile.setRawListing(str);
        fTPFile.setName(group(2));
        fTPFile.setType(0);
        return true;
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParserImpl, org.apache.commons.net.ftp.FTPFileEntryParser
    public List<String> preParse(List<String> list) {
        if (list != null && list.size() > 0) {
            String str = list.get(0);
            if (str.indexOf("Volume") >= 0 && str.indexOf("Dsname") >= 0) {
                setType(0);
                super.setRegex(FILE_LIST_REGEX);
            } else if (str.indexOf("Name") >= 0 && str.indexOf("Id") >= 0) {
                setType(1);
                super.setRegex(MEMBER_LIST_REGEX);
            } else if (str.indexOf(TopicKey.TOTAL) == 0) {
                setType(2);
                this.unixFTPEntryParser = new UnixFTPEntryParser();
            } else if (str.indexOf("Spool Files") >= 30) {
                setType(3);
                super.setRegex(JES_LEVEL_1_LIST_REGEX);
            } else if (str.indexOf("JOBNAME") == 0 && str.indexOf("JOBID") > 8) {
                setType(4);
                super.setRegex(JES_LEVEL_2_LIST_REGEX);
            } else {
                setType(-1);
            }
            if (this.isType != 3) {
                list.remove(0);
            }
        }
        return list;
    }

    void setType(int i) {
        this.isType = i;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_MVS, DEFAULT_DATE_FORMAT, null, null, null, null);
    }
}
