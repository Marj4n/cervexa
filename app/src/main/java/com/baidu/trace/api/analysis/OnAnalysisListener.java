package com.baidu.trace.api.analysis;

/* loaded from: classes.dex */
public interface OnAnalysisListener {
    void onDrivingBehaviorCallback(DrivingBehaviorResponse drivingBehaviorResponse);

    void onStayPointCallback(StayPointResponse stayPointResponse);
}
