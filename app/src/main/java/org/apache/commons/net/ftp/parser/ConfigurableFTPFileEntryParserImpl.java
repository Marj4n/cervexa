package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;

/* loaded from: classes2.dex */
public abstract class ConfigurableFTPFileEntryParserImpl extends RegexFTPFileEntryParserImpl implements Configurable {
    private final FTPTimestampParser timestampParser;

    protected abstract FTPClientConfig getDefaultConfiguration();

    public ConfigurableFTPFileEntryParserImpl(String str) {
        super(str);
        this.timestampParser = new FTPTimestampParserImpl();
    }

    public ConfigurableFTPFileEntryParserImpl(String str, int i) {
        super(str, i);
        this.timestampParser = new FTPTimestampParserImpl();
    }

    public Calendar parseTimestamp(String str) throws ParseException {
        return this.timestampParser.parseTimestamp(str);
    }

    @Override // org.apache.commons.net.ftp.Configurable
    public void configure(FTPClientConfig fTPClientConfig) {
        if (this.timestampParser instanceof Configurable) {
            FTPClientConfig defaultConfiguration = getDefaultConfiguration();
            if (fTPClientConfig != null) {
                if (fTPClientConfig.getDefaultDateFormatStr() == null) {
                    fTPClientConfig.setDefaultDateFormatStr(defaultConfiguration.getDefaultDateFormatStr());
                }
                if (fTPClientConfig.getRecentDateFormatStr() == null) {
                    fTPClientConfig.setRecentDateFormatStr(defaultConfiguration.getRecentDateFormatStr());
                }
                ((Configurable) this.timestampParser).configure(fTPClientConfig);
                return;
            }
            ((Configurable) this.timestampParser).configure(defaultConfiguration);
        }
    }
}
