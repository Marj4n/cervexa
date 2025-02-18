package com.baidu.trace.model;

import java.util.Map;

/* loaded from: classes.dex */
public interface OnCustomAttributeListener {
    Map<String, String> onTrackAttributeCallback();

    Map<String, String> onTrackAttributeCallback(long j);
}
