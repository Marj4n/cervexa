package com.google.zxing.oned.rss.expanded.decoders;

/* loaded from: classes.dex */
final class BlockParsedResult {
    private final DecodedInformation decodedInformation;
    private final boolean finished;

    BlockParsedResult() {
        this.finished = true;
        this.decodedInformation = null;
    }

    BlockParsedResult(DecodedInformation decodedInformation, boolean z) {
        this.finished = z;
        this.decodedInformation = decodedInformation;
    }

    BlockParsedResult(boolean z) {
        this.finished = z;
        this.decodedInformation = null;
    }

    DecodedInformation getDecodedInformation() {
        return this.decodedInformation;
    }

    boolean isFinished() {
        return this.finished;
    }
}
