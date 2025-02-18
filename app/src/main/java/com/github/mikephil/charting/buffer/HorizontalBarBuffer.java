package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

/* loaded from: classes.dex */
public class HorizontalBarBuffer extends BarBuffer {
    public HorizontalBarBuffer(int i, int i2, boolean z) {
        super(i, i2, z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.buffer.BarBuffer, com.github.mikephil.charting.buffer.AbstractBuffer
    public void feed(IBarDataSet iBarDataSet) {
        float f;
        float abs;
        float abs2;
        float f2;
        float entryCount = iBarDataSet.getEntryCount() * this.phaseX;
        float f3 = this.mBarWidth / 2.0f;
        for (int i = 0; i < entryCount; i++) {
            BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForIndex(i);
            if (barEntry != null) {
                float x = barEntry.getX();
                float y = barEntry.getY();
                float[] yVals = barEntry.getYVals();
                if (!this.mContainsStacks || yVals == null) {
                    float f4 = x - f3;
                    float f5 = x + f3;
                    if (this.mInverted) {
                        f = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                    } else {
                        float f6 = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                        float f7 = y;
                        y = f6;
                        f = f7;
                    }
                    if (y > 0.0f) {
                        y *= this.phaseY;
                    } else {
                        f *= this.phaseY;
                    }
                    addBar(f, f5, y, f4);
                } else {
                    float f8 = -barEntry.getNegativeSum();
                    int i2 = 0;
                    float f9 = 0.0f;
                    while (i2 < yVals.length) {
                        float f10 = yVals[i2];
                        if (f10 >= 0.0f) {
                            abs = f10 + f9;
                            abs2 = f8;
                            f8 = f9;
                            f9 = abs;
                        } else {
                            abs = Math.abs(f10) + f8;
                            abs2 = Math.abs(f10) + f8;
                        }
                        float f11 = x - f3;
                        float f12 = x + f3;
                        if (this.mInverted) {
                            f2 = f8 >= abs ? f8 : abs;
                            if (f8 > abs) {
                                f8 = abs;
                            }
                        } else {
                            float f13 = f8 >= abs ? f8 : abs;
                            if (f8 > abs) {
                                f8 = abs;
                            }
                            float f14 = f8;
                            f8 = f13;
                            f2 = f14;
                        }
                        addBar(f2 * this.phaseY, f12, f8 * this.phaseY, f11);
                        i2++;
                        f8 = abs2;
                    }
                }
            }
        }
        reset();
    }
}
