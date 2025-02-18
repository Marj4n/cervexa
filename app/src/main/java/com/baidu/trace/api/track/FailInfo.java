package com.baidu.trace.api.track;

import java.util.List;

/* loaded from: classes.dex */
public class FailInfo {
    private List<ParamError> a;
    private List<InternalError> b;

    public class InternalError {
        String a;
        TrackPoint b;

        public InternalError(FailInfo failInfo) {
        }

        public InternalError(FailInfo failInfo, String str, TrackPoint trackPoint) {
            this.a = str;
            this.b = trackPoint;
        }

        public String getEntityName() {
            return this.a;
        }

        public TrackPoint getTrackPoint() {
            return this.b;
        }

        public void setEntityName(String str) {
            this.a = str;
        }

        public void setTrackPoint(TrackPoint trackPoint) {
            this.b = trackPoint;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("InternalError{");
            stringBuffer.append("entityName='");
            stringBuffer.append(this.a);
            stringBuffer.append('\'');
            stringBuffer.append(", trackPoint=");
            stringBuffer.append(this.b);
            stringBuffer.append('}');
            return stringBuffer.toString();
        }
    }

    public class ParamError extends InternalError {
        private String c;

        public ParamError(FailInfo failInfo) {
            super(failInfo);
        }

        public ParamError(FailInfo failInfo, String str, TrackPoint trackPoint, String str2) {
            super(failInfo, str, trackPoint);
            this.c = str2;
        }

        public String getError() {
            return this.c;
        }

        public void setError(String str) {
            this.c = str;
        }

        @Override // com.baidu.trace.api.track.FailInfo.InternalError
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("ParamError{");
            stringBuffer.append("entityName='");
            stringBuffer.append(this.a);
            stringBuffer.append('\'');
            stringBuffer.append(", trackPoint=");
            stringBuffer.append(this.b);
            stringBuffer.append(", error='");
            stringBuffer.append(this.c);
            stringBuffer.append('\'');
            stringBuffer.append('}');
            return stringBuffer.toString();
        }
    }

    public FailInfo() {
    }

    public FailInfo(List<ParamError> list, List<InternalError> list2) {
        this.a = list;
        this.b = list2;
    }

    public List<InternalError> getInternalErrors() {
        return this.b;
    }

    public List<ParamError> getParamErrors() {
        return this.a;
    }

    public void setInternalErrors(List<InternalError> list) {
        this.b = list;
    }

    public void setParamErrors(List<ParamError> list) {
        this.a = list;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("FailInfo{");
        stringBuffer.append("paramErrors=");
        stringBuffer.append(this.a);
        stringBuffer.append(", internalErrors=");
        stringBuffer.append(this.b);
        stringBuffer.append('}');
        return stringBuffer.toString();
    }
}
