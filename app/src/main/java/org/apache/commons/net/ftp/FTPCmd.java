package org.apache.commons.net.ftp;

/* loaded from: classes2.dex */
public enum FTPCmd {
    ABOR,
    ACCT,
    ALLO,
    APPE,
    CDUP,
    CWD,
    DELE,
    EPRT,
    EPSV,
    FEAT,
    HELP,
    LIST,
    MDTM,
    MFMT,
    MKD,
    MLSD,
    MLST,
    MODE,
    NLST,
    NOOP,
    PASS,
    PASV,
    PORT,
    PWD,
    QUIT,
    REIN,
    REST,
    RETR,
    RMD,
    RNFR,
    RNTO,
    SITE,
    SMNT,
    STAT,
    STOR,
    STOU,
    STRU,
    SYST,
    TYPE,
    USER;

    public static final FTPCmd ABORT;
    public static final FTPCmd ACCOUNT;
    public static final FTPCmd ALLOCATE;
    public static final FTPCmd APPEND;
    public static final FTPCmd CHANGE_TO_PARENT_DIRECTORY;
    public static final FTPCmd CHANGE_WORKING_DIRECTORY;
    public static final FTPCmd DATA_PORT;
    public static final FTPCmd DELETE;
    public static final FTPCmd FEATURES;
    public static final FTPCmd FILE_STRUCTURE;
    public static final FTPCmd GET_MOD_TIME;
    public static final FTPCmd LOGOUT;
    public static final FTPCmd MAKE_DIRECTORY;
    public static final FTPCmd MOD_TIME;
    public static final FTPCmd NAME_LIST;
    public static final FTPCmd PASSIVE;
    public static final FTPCmd PASSWORD;
    public static final FTPCmd PRINT_WORKING_DIRECTORY;
    public static final FTPCmd REINITIALIZE;
    public static final FTPCmd REMOVE_DIRECTORY;
    public static final FTPCmd RENAME_FROM;
    public static final FTPCmd RENAME_TO;
    public static final FTPCmd REPRESENTATION_TYPE;
    public static final FTPCmd RESTART;
    public static final FTPCmd RETRIEVE;
    public static final FTPCmd SET_MOD_TIME;
    public static final FTPCmd SITE_PARAMETERS;
    public static final FTPCmd STATUS;
    public static final FTPCmd STORE;
    public static final FTPCmd STORE_UNIQUE;
    public static final FTPCmd STRUCTURE_MOUNT;
    public static final FTPCmd SYSTEM;
    public static final FTPCmd TRANSFER_MODE;
    public static final FTPCmd USERNAME;

    static {
        FTPCmd fTPCmd = USER;
        FTPCmd fTPCmd2 = ABOR;
        FTPCmd fTPCmd3 = ACCT;
        FTPCmd fTPCmd4 = ALLO;
        FTPCmd fTPCmd5 = APPE;
        FTPCmd fTPCmd6 = CDUP;
        FTPCmd fTPCmd7 = CWD;
        FTPCmd fTPCmd8 = DELE;
        FTPCmd fTPCmd9 = FEAT;
        FTPCmd fTPCmd10 = MDTM;
        FTPCmd fTPCmd11 = MFMT;
        FTPCmd fTPCmd12 = MKD;
        FTPCmd fTPCmd13 = MODE;
        FTPCmd fTPCmd14 = NLST;
        FTPCmd fTPCmd15 = PASS;
        FTPCmd fTPCmd16 = PASV;
        FTPCmd fTPCmd17 = PORT;
        FTPCmd fTPCmd18 = PWD;
        FTPCmd fTPCmd19 = QUIT;
        FTPCmd fTPCmd20 = REIN;
        FTPCmd fTPCmd21 = REST;
        FTPCmd fTPCmd22 = RETR;
        FTPCmd fTPCmd23 = RMD;
        FTPCmd fTPCmd24 = RNFR;
        FTPCmd fTPCmd25 = RNTO;
        FTPCmd fTPCmd26 = SITE;
        FTPCmd fTPCmd27 = SMNT;
        FTPCmd fTPCmd28 = STAT;
        FTPCmd fTPCmd29 = STOR;
        FTPCmd fTPCmd30 = STOU;
        FTPCmd fTPCmd31 = STRU;
        FTPCmd fTPCmd32 = SYST;
        FTPCmd fTPCmd33 = TYPE;
        ABORT = fTPCmd2;
        ACCOUNT = fTPCmd3;
        ALLOCATE = fTPCmd4;
        APPEND = fTPCmd5;
        CHANGE_TO_PARENT_DIRECTORY = fTPCmd6;
        CHANGE_WORKING_DIRECTORY = fTPCmd7;
        DATA_PORT = fTPCmd17;
        DELETE = fTPCmd8;
        FEATURES = fTPCmd9;
        FILE_STRUCTURE = fTPCmd31;
        GET_MOD_TIME = fTPCmd10;
        LOGOUT = fTPCmd19;
        MAKE_DIRECTORY = fTPCmd12;
        MOD_TIME = fTPCmd10;
        NAME_LIST = fTPCmd14;
        PASSIVE = fTPCmd16;
        PASSWORD = fTPCmd15;
        PRINT_WORKING_DIRECTORY = fTPCmd18;
        REINITIALIZE = fTPCmd20;
        REMOVE_DIRECTORY = fTPCmd23;
        RENAME_FROM = fTPCmd24;
        RENAME_TO = fTPCmd25;
        REPRESENTATION_TYPE = fTPCmd33;
        RESTART = fTPCmd21;
        RETRIEVE = fTPCmd22;
        SET_MOD_TIME = fTPCmd11;
        SITE_PARAMETERS = fTPCmd26;
        STATUS = fTPCmd28;
        STORE = fTPCmd29;
        STORE_UNIQUE = fTPCmd30;
        STRUCTURE_MOUNT = fTPCmd27;
        SYSTEM = fTPCmd32;
        TRANSFER_MODE = fTPCmd13;
        USERNAME = fTPCmd;
    }

    public final String getCommand() {
        return name();
    }
}
