package com.google.zxing.client.result;

import kotlin.text.Typography;

/* loaded from: classes.dex */
public final class SMSParsedResult extends ParsedResult {
    private final String body;
    private final String[] numbers;
    private final String subject;
    private final String[] vias;

    public SMSParsedResult(String str, String str2, String str3, String str4) {
        super(ParsedResultType.SMS);
        this.numbers = new String[]{str};
        this.vias = new String[]{str2};
        this.subject = str3;
        this.body = str4;
    }

    public SMSParsedResult(String[] strArr, String[] strArr2, String str, String str2) {
        super(ParsedResultType.SMS);
        this.numbers = strArr;
        this.vias = strArr2;
        this.subject = str;
        this.body = str2;
    }

    public String getBody() {
        return this.body;
    }

    @Override // com.google.zxing.client.result.ParsedResult
    public String getDisplayResult() {
        StringBuffer stringBuffer = new StringBuffer(100);
        maybeAppend(this.numbers, stringBuffer);
        maybeAppend(this.subject, stringBuffer);
        maybeAppend(this.body, stringBuffer);
        return stringBuffer.toString();
    }

    public String[] getNumbers() {
        return this.numbers;
    }

    public String getSMSURI() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("sms:");
        boolean z = true;
        for (int i = 0; i < this.numbers.length; i++) {
            if (z) {
                z = false;
            } else {
                stringBuffer.append(',');
            }
            stringBuffer.append(this.numbers[i]);
            if (this.vias[i] != null) {
                stringBuffer.append(";via=");
                stringBuffer.append(this.vias[i]);
            }
        }
        boolean z2 = this.body != null;
        boolean z3 = this.subject != null;
        if (z2 || z3) {
            stringBuffer.append('?');
            if (z2) {
                stringBuffer.append("body=");
                stringBuffer.append(this.body);
            }
            if (z3) {
                if (z2) {
                    stringBuffer.append(Typography.amp);
                }
                stringBuffer.append("subject=");
                stringBuffer.append(this.subject);
            }
        }
        return stringBuffer.toString();
    }

    public String getSubject() {
        return this.subject;
    }

    public String[] getVias() {
        return this.vias;
    }
}
