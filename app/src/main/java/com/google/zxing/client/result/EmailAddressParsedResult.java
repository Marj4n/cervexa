package com.google.zxing.client.result;

/* loaded from: classes.dex */
public final class EmailAddressParsedResult extends ParsedResult {
    private final String body;
    private final String emailAddress;
    private final String mailtoURI;
    private final String subject;

    EmailAddressParsedResult(String str, String str2, String str3, String str4) {
        super(ParsedResultType.EMAIL_ADDRESS);
        this.emailAddress = str;
        this.subject = str2;
        this.body = str3;
        this.mailtoURI = str4;
    }

    public String getBody() {
        return this.body;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuffer stringBuffer = new StringBuffer(30);
        maybeAppend(this.emailAddress, stringBuffer);
        maybeAppend(this.subject, stringBuffer);
        maybeAppend(this.body, stringBuffer);
        return stringBuffer.toString();
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getMailtoURI() {
        return this.mailtoURI;
    }

    public String getSubject() {
        return this.subject;
    }
}
