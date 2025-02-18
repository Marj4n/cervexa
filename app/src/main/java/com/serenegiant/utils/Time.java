package com.serenegiant.utils;

import android.os.SystemClock;

/* loaded from: classes2.dex */
public class Time {
    public static boolean prohibitElapsedRealtimeNanos = true;
    private static Time sTime;

    static {
        reset();
    }

    public static long nanoTime() {
        return sTime.timeNs();
    }

    public static void reset() {
        if (!prohibitElapsedRealtimeNanos && BuildCheck.isJellyBeanMr1()) {
            sTime = new TimeJellyBeanMr1();
        } else {
            sTime = new Time();
        }
    }

    private Time() {
    }

    private static class TimeJellyBeanMr1 extends Time {
        private TimeJellyBeanMr1() {
            super();
        }

        @Override // com.serenegiant.utils.Time
        public long timeNs() {
            return SystemClock.elapsedRealtimeNanos();
        }
    }

    protected long timeNs() {
        return System.nanoTime();
    }
}
