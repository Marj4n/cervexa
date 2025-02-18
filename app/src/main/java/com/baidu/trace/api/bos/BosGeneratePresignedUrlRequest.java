package com.baidu.trace.api.bos;

/* loaded from: classes.dex */
public final class BosGeneratePresignedUrlRequest extends BosObjectRequest {
    private ImageProcessCommand a;
    private ImageWatermarkCommand b;
    private TextWatermarkCommand c;

    public BosGeneratePresignedUrlRequest() {
        this.a = null;
        this.b = null;
        this.c = null;
    }

    public BosGeneratePresignedUrlRequest(int i, long j) {
        super(i, j);
        this.a = null;
        this.b = null;
        this.c = null;
    }

    public BosGeneratePresignedUrlRequest(int i, long j, String str, BosObjectType bosObjectType) {
        super(i, j, str, bosObjectType);
        this.a = null;
        this.b = null;
        this.c = null;
    }

    public BosGeneratePresignedUrlRequest(int i, long j, String str, BosObjectType bosObjectType, ImageProcessCommand imageProcessCommand, ImageWatermarkCommand imageWatermarkCommand, TextWatermarkCommand textWatermarkCommand) {
        this(i, j, str, bosObjectType);
        this.a = imageProcessCommand;
        this.b = imageWatermarkCommand;
        this.c = textWatermarkCommand;
    }

    public final ImageProcessCommand getImageProcessCommand() {
        return this.a;
    }

    public final ImageWatermarkCommand getImageWatermarkCommand() {
        return this.b;
    }

    public final TextWatermarkCommand getTextWatermarkCommand() {
        return this.c;
    }

    public final void setImageProcessCommand(ImageProcessCommand imageProcessCommand) {
        this.a = imageProcessCommand;
    }

    public final void setImageWatermarkCommand(ImageWatermarkCommand imageWatermarkCommand) {
        this.b = imageWatermarkCommand;
    }

    public final void setTextWatermarkCommand(TextWatermarkCommand textWatermarkCommand) {
        this.c = textWatermarkCommand;
    }

    @Override // com.baidu.trace.api.bos.BosObjectRequest
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("BosGeneratePresignedUrlRequest{");
        stringBuffer.append("imageProcessCommand=");
        stringBuffer.append(this.a);
        stringBuffer.append(", imageWatermarkCommand=");
        stringBuffer.append(this.b);
        stringBuffer.append(", textWatermarkCommand=");
        stringBuffer.append(this.c);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
