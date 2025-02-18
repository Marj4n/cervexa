package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;

/* loaded from: classes2.dex */
public interface FTPFileEntryParserFactory {
    FTPFileEntryParser createFileEntryParser(String str) throws ParserInitializationException;

    FTPFileEntryParser createFileEntryParser(FTPClientConfig fTPClientConfig) throws ParserInitializationException;
}
