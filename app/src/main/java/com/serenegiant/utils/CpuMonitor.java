package com.serenegiant.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/* loaded from: classes2.dex */
public final class CpuMonitor {
    private static final int SAMPLE_SAVE_NUMBER = 10;
    private static final String TAG = "CpuMonitor";
    private int cpuAvg3;
    private int cpuAvgAll;
    private int cpuCurrent;
    private long[] cpuFreq;
    private int cpusPresent;
    private String[] curPath;
    private String[] maxPath;
    private int[] percentVec = new int[10];
    private int sum3 = 0;
    private int sum10 = 0;
    private double lastPercentFreq = -1.0d;
    private boolean initialized = false;
    private final ProcStat lastProcStat = new ProcStat(0, 0);
    private final Map<String, Integer> mCpuTemps = new HashMap();
    private int mTempNum = 0;
    private float tempAve = 0.0f;

    private static final class ProcStat {
        private long idleTime;
        private long runTime;

        private ProcStat(long j, long j2) {
            this.runTime = j;
            this.idleTime = j2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(long j, long j2) {
            this.runTime = j;
            this.idleTime = j2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(ProcStat procStat) {
            this.runTime = procStat.runTime;
            this.idleTime = procStat.idleTime;
        }
    }

    private void init() {
        try {
            FileReader fileReader = new FileReader("/sys/devices/system/cpu/present");
            try {
                try {
                    Scanner useDelimiter = new Scanner(new BufferedReader(fileReader)).useDelimiter("[-\n]");
                    useDelimiter.nextInt();
                    this.cpusPresent = useDelimiter.nextInt() + 1;
                    useDelimiter.close();
                } catch (Exception unused) {
                    Log.e(TAG, "Cannot do CPU stats due to /sys/devices/system/cpu/present parsing problem");
                }
            } finally {
                fileReader.close();
            }
        } catch (FileNotFoundException unused2) {
            Log.e(TAG, "Cannot do CPU stats since /sys/devices/system/cpu/present is missing");
        } catch (IOException unused3) {
            Log.e(TAG, "Error closing file");
        }
        int i = this.cpusPresent;
        this.cpuFreq = new long[i];
        this.maxPath = new String[i];
        this.curPath = new String[i];
        for (int i2 = 0; i2 < this.cpusPresent; i2++) {
            this.cpuFreq[i2] = 0;
            this.maxPath[i2] = "/sys/devices/system/cpu/cpu" + i2 + "/cpufreq/cpuinfo_max_freq";
            this.curPath[i2] = "/sys/devices/system/cpu/cpu" + i2 + "/cpufreq/scaling_cur_freq";
        }
        this.lastProcStat.set(0L, 0L);
        this.mCpuTemps.clear();
        this.mTempNum = 0;
        for (int i3 = 0; i3 < 50; i3++) {
            String str = "/sys/class/hwmon/hwmon" + i3;
            File file = new File(str);
            if (file.exists() && file.canRead()) {
                this.mCpuTemps.put(str, 0);
                this.mTempNum++;
            }
        }
        this.initialized = true;
    }

    public boolean sampleCpuUtilization() {
        if (!this.initialized) {
            init();
        }
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        for (int i = 0; i < this.cpusPresent; i++) {
            long[] jArr = this.cpuFreq;
            if (jArr[i] == 0) {
                long readFreqFromFile = readFreqFromFile(this.maxPath[i]);
                if (readFreqFromFile > 0) {
                    this.cpuFreq[i] = readFreqFromFile;
                    this.maxPath[i] = null;
                    j3 = readFreqFromFile;
                }
            } else {
                j3 = jArr[i];
            }
            j2 += readFreqFromFile(this.curPath[i]);
            j += j3;
        }
        if (j == 0) {
            Log.e(TAG, "Could not read max frequency for any CPU");
            return false;
        }
        double d = (j2 * 100.0d) / j;
        double d2 = this.lastPercentFreq;
        double d3 = d2 > 1e-10 ? (d2 + d) * 0.5d : d;
        this.lastPercentFreq = d;
        ProcStat readIdleAndRunTime = readIdleAndRunTime();
        if (readIdleAndRunTime == null) {
            return false;
        }
        long j4 = readIdleAndRunTime.runTime - this.lastProcStat.runTime;
        long j5 = readIdleAndRunTime.idleTime - this.lastProcStat.idleTime;
        this.lastProcStat.set(readIdleAndRunTime);
        long j6 = j5 + j4;
        int max = Math.max(0, Math.min(j6 == 0 ? 0 : (int) Math.round((d3 * j4) / j6), 100));
        int i2 = this.sum3;
        int[] iArr = this.percentVec;
        this.sum3 = i2 + (max - iArr[2]);
        this.sum10 += max - iArr[9];
        for (int i3 = 9; i3 > 0; i3--) {
            int[] iArr2 = this.percentVec;
            iArr2[i3] = iArr2[i3 - 1];
        }
        this.percentVec[0] = max;
        this.cpuCurrent = max;
        this.cpuAvg3 = this.sum3 / 3;
        this.cpuAvgAll = this.sum10 / 10;
        this.tempAve = 0.0f;
        float f = 0.0f;
        for (String str : this.mCpuTemps.keySet()) {
            File file = new File(str);
            if (file.exists() && file.canRead()) {
                File file2 = new File(file, "temp1_input");
                if (file2.exists() && file2.canRead()) {
                    int readFreqFromFile2 = (int) readFreqFromFile(file2.getAbsolutePath());
                    this.mCpuTemps.put(str, Integer.valueOf(readFreqFromFile2));
                    if (readFreqFromFile2 > 0) {
                        f += 1.0f;
                        this.tempAve += readFreqFromFile2 > 1000 ? readFreqFromFile2 / 1000.0f : readFreqFromFile2;
                    }
                }
            }
        }
        if (f <= 0.0f) {
            return true;
        }
        this.tempAve /= f;
        return true;
    }

    public int getCpuCurrent() {
        return this.cpuCurrent;
    }

    public int getCpuAvg3() {
        return this.cpuAvg3;
    }

    public int getCpuAvgAll() {
        return this.cpuAvgAll;
    }

    public int getTempNum() {
        return this.mTempNum;
    }

    public int getTemp(int i) {
        if (i >= 0 && i < this.mTempNum) {
            String str = "/sys/class/hwmon/hwmon" + i;
            if (this.mCpuTemps.containsKey(str)) {
                return this.mCpuTemps.get(str).intValue();
            }
        }
        return 0;
    }

    public float getTempAve() {
        return this.tempAve;
    }

    private long readFreqFromFile(String str) {
        long j = 0;
        try {
            FileReader fileReader = new FileReader(str);
            try {
                Scanner scanner = new Scanner(new BufferedReader(fileReader));
                j = scanner.nextLong();
                scanner.close();
            } catch (Exception unused) {
            } catch (Throwable th) {
                fileReader.close();
                throw th;
            }
            fileReader.close();
        } catch (FileNotFoundException unused2) {
        } catch (IOException unused3) {
            Log.e(TAG, "Error closing file");
        }
        return j;
    }

    private ProcStat readIdleAndRunTime() {
        try {
            FileReader fileReader = new FileReader("/proc/stat");
            try {
                Scanner scanner = new Scanner(new BufferedReader(fileReader));
                scanner.next();
                long nextLong = scanner.nextLong() + scanner.nextLong() + scanner.nextLong();
                long nextLong2 = scanner.nextLong();
                scanner.close();
                return new ProcStat(nextLong, nextLong2);
            } catch (Exception unused) {
                Log.e(TAG, "Problems parsing /proc/stat");
                return null;
            } finally {
                fileReader.close();
            }
        } catch (FileNotFoundException unused2) {
            Log.e(TAG, "Cannot open /proc/stat for reading");
            return null;
        } catch (IOException unused3) {
            Log.e(TAG, "Problems reading /proc/stat");
            return null;
        }
    }
}
