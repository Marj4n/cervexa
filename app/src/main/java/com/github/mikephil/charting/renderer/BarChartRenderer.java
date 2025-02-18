package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tencent.connect.common.Constants;
import java.util.List;

/* loaded from: classes.dex */
public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {
    protected Paint mBarBorderPaint;
    protected BarBuffer[] mBarBuffers;
    protected RectF mBarRect;
    private RectF mBarShadowRectBuffer;
    protected BarDataProvider mChart;
    protected Paint mShadowPaint;

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawExtras(Canvas canvas) {
    }

    public BarChartRenderer(BarDataProvider barDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mBarRect = new RectF();
        this.mBarShadowRectBuffer = new RectF();
        this.mChart = barDataProvider;
        this.mHighlightPaint = new Paint(1);
        this.mHighlightPaint.setStyle(Paint.Style.FILL);
        this.mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        this.mHighlightPaint.setAlpha(120);
        Paint paint = new Paint(1);
        this.mShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint(1);
        this.mBarBorderPaint = paint2;
        paint2.setStyle(Paint.Style.STROKE);
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new BarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new BarBuffer(iBarDataSet.getEntryCount() * 4 * (iBarDataSet.isStacked() ? iBarDataSet.getStackSize() : 1), barData.getDataSetCount(), iBarDataSet.isStacked());
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawData(Canvas canvas) {
        BarData barData = this.mChart.getBarData();
        for (int i = 0; i < barData.getDataSetCount(); i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            if (iBarDataSet.isVisible()) {
                drawDataSet(canvas, iBarDataSet, i);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void drawDataSet(Canvas canvas, IBarDataSet iBarDataSet, int i) {
        Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
        this.mBarBorderPaint.setColor(iBarDataSet.getBarBorderColor());
        this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(iBarDataSet.getBarBorderWidth()));
        boolean z = iBarDataSet.getBarBorderWidth() > 0.0f;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        if (this.mChart.isDrawBarShadowEnabled()) {
            this.mShadowPaint.setColor(iBarDataSet.getBarShadowColor());
            float barWidth = this.mChart.getBarData().getBarWidth() / 2.0f;
            int min = Math.min((int) Math.ceil(iBarDataSet.getEntryCount() * phaseX), iBarDataSet.getEntryCount());
            for (int i2 = 0; i2 < min; i2++) {
                float x = ((BarEntry) iBarDataSet.getEntryForIndex(i2)).getX();
                this.mBarShadowRectBuffer.left = x - barWidth;
                this.mBarShadowRectBuffer.right = x + barWidth;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsLeft(this.mBarShadowRectBuffer.right)) {
                    if (!this.mViewPortHandler.isInBoundsRight(this.mBarShadowRectBuffer.left)) {
                        break;
                    }
                    this.mBarShadowRectBuffer.top = this.mViewPortHandler.contentTop();
                    this.mBarShadowRectBuffer.bottom = this.mViewPortHandler.contentBottom();
                    canvas.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
                }
            }
        }
        BarBuffer barBuffer = this.mBarBuffers[i];
        barBuffer.setPhases(phaseX, phaseY);
        barBuffer.setDataSet(i);
        barBuffer.setInverted(this.mChart.isInverted(iBarDataSet.getAxisDependency()));
        barBuffer.setBarWidth(this.mChart.getBarData().getBarWidth());
        barBuffer.feed(iBarDataSet);
        transformer.pointValuesToPixel(barBuffer.buffer);
        boolean z2 = iBarDataSet.getColors().size() == 1;
        if (z2) {
            this.mRenderPaint.setColor(iBarDataSet.getColor());
        }
        for (int i3 = 0; i3 < barBuffer.size(); i3 += 4) {
            int i4 = i3 + 2;
            if (this.mViewPortHandler.isInBoundsLeft(barBuffer.buffer[i4])) {
                if (!this.mViewPortHandler.isInBoundsRight(barBuffer.buffer[i3])) {
                    return;
                }
                if (!z2) {
                    this.mRenderPaint.setColor(iBarDataSet.getColor(i3 / 4));
                }
                int i5 = i3 + 1;
                int i6 = i3 + 3;
                canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i5], barBuffer.buffer[i4], barBuffer.buffer[i6], this.mRenderPaint);
                if (z) {
                    canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i5], barBuffer.buffer[i4], barBuffer.buffer[i6], this.mBarBorderPaint);
                }
            }
        }
    }

    protected void prepareBarHighlight(float f, float f2, float f3, float f4, Transformer transformer) {
        this.mBarRect.set(f - f4, f2, f + f4, f3);
        transformer.rectToPixelPhase(this.mBarRect, this.mAnimator.getPhaseY());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawValues(Canvas canvas) {
        int i;
        List list;
        float f;
        float[] fArr;
        Transformer transformer;
        int i2;
        float[] fArr2;
        int i3;
        float f2;
        int i4;
        BarBuffer barBuffer;
        if (isDrawingValuesAllowed(this.mChart)) {
            List dataSets = this.mChart.getBarData().getDataSets();
            float convertDpToPixel = Utils.convertDpToPixel(4.5f);
            boolean isDrawValueAboveBarEnabled = this.mChart.isDrawValueAboveBarEnabled();
            int i5 = 0;
            while (i5 < this.mChart.getBarData().getDataSetCount()) {
                IBarDataSet iBarDataSet = (IBarDataSet) dataSets.get(i5);
                if (shouldDrawValues(iBarDataSet)) {
                    applyValueTextStyle(iBarDataSet);
                    boolean isInverted = this.mChart.isInverted(iBarDataSet.getAxisDependency());
                    float calcTextHeight = Utils.calcTextHeight(this.mValuePaint, Constants.VIA_SHARE_TYPE_PUBLISHVIDEO);
                    float f3 = isDrawValueAboveBarEnabled ? -convertDpToPixel : calcTextHeight + convertDpToPixel;
                    float f4 = isDrawValueAboveBarEnabled ? calcTextHeight + convertDpToPixel : -convertDpToPixel;
                    if (isInverted) {
                        f3 = (-f3) - calcTextHeight;
                        f4 = (-f4) - calcTextHeight;
                    }
                    float f5 = f3;
                    float f6 = f4;
                    BarBuffer barBuffer2 = this.mBarBuffers[i5];
                    float phaseY = this.mAnimator.getPhaseY();
                    if (iBarDataSet.isStacked()) {
                        Transformer transformer2 = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
                        int i6 = 0;
                        int i7 = 0;
                        while (i6 < iBarDataSet.getEntryCount() * this.mAnimator.getPhaseX()) {
                            BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForIndex(i6);
                            float[] yVals = barEntry.getYVals();
                            float f7 = (barBuffer2.buffer[i7] + barBuffer2.buffer[i7 + 2]) / 2.0f;
                            int valueTextColor = iBarDataSet.getValueTextColor(i6);
                            if (yVals == null) {
                                if (!this.mViewPortHandler.isInBoundsRight(f7)) {
                                    break;
                                }
                                int i8 = i7 + 1;
                                if (this.mViewPortHandler.isInBoundsY(barBuffer2.buffer[i8]) && this.mViewPortHandler.isInBoundsLeft(f7)) {
                                    list = dataSets;
                                    fArr = yVals;
                                    i = i6;
                                    f = convertDpToPixel;
                                    transformer = transformer2;
                                    drawValue(canvas, iBarDataSet.getValueFormatter(), barEntry.getY(), barEntry, i5, f7, barBuffer2.buffer[i8] + (barEntry.getY() >= 0.0f ? f5 : f6), valueTextColor);
                                } else {
                                    transformer2 = transformer2;
                                    convertDpToPixel = convertDpToPixel;
                                    dataSets = dataSets;
                                    i6 = i6;
                                }
                            } else {
                                float f8 = f7;
                                i = i6;
                                list = dataSets;
                                f = convertDpToPixel;
                                fArr = yVals;
                                transformer = transformer2;
                                int length = fArr.length * 2;
                                float[] fArr3 = new float[length];
                                float f9 = -barEntry.getNegativeSum();
                                int i9 = 0;
                                int i10 = 0;
                                float f10 = 0.0f;
                                while (i9 < length) {
                                    float f11 = fArr[i10];
                                    if (f11 == 0.0f && (f10 == 0.0f || f9 == 0.0f)) {
                                        float f12 = f9;
                                        f9 = f11;
                                        f2 = f12;
                                    } else if (f11 >= 0.0f) {
                                        f10 += f11;
                                        f2 = f9;
                                        f9 = f10;
                                    } else {
                                        f2 = f9 - f11;
                                    }
                                    fArr3[i9 + 1] = f9 * phaseY;
                                    i9 += 2;
                                    i10++;
                                    f9 = f2;
                                }
                                transformer.pointValuesToPixel(fArr3);
                                int i11 = 0;
                                while (i11 < length) {
                                    int i12 = i11 / 2;
                                    float f13 = fArr[i12];
                                    float f14 = fArr3[i11 + 1] + (((f13 > 0.0f ? 1 : (f13 == 0.0f ? 0 : -1)) == 0 && (f9 > 0.0f ? 1 : (f9 == 0.0f ? 0 : -1)) == 0 && (f10 > 0.0f ? 1 : (f10 == 0.0f ? 0 : -1)) > 0) || (f13 > 0.0f ? 1 : (f13 == 0.0f ? 0 : -1)) < 0 ? f6 : f5);
                                    float f15 = f8;
                                    if (!this.mViewPortHandler.isInBoundsRight(f15)) {
                                        break;
                                    }
                                    if (this.mViewPortHandler.isInBoundsY(f14) && this.mViewPortHandler.isInBoundsLeft(f15)) {
                                        f8 = f15;
                                        i2 = i11;
                                        fArr2 = fArr3;
                                        i3 = length;
                                        drawValue(canvas, iBarDataSet.getValueFormatter(), fArr[i12], barEntry, i5, f8, f14, valueTextColor);
                                    } else {
                                        f8 = f15;
                                        i2 = i11;
                                        fArr2 = fArr3;
                                        i3 = length;
                                    }
                                    i11 = i2 + 2;
                                    length = i3;
                                    fArr3 = fArr2;
                                }
                            }
                            i7 = fArr == null ? i7 + 4 : i7 + (fArr.length * 4);
                            i6 = i + 1;
                            transformer2 = transformer;
                            convertDpToPixel = f;
                            dataSets = list;
                        }
                    } else {
                        int i13 = 0;
                        while (i13 < barBuffer2.buffer.length * this.mAnimator.getPhaseX()) {
                            float f16 = (barBuffer2.buffer[i13] + barBuffer2.buffer[i13 + 2]) / 2.0f;
                            if (!this.mViewPortHandler.isInBoundsRight(f16)) {
                                break;
                            }
                            int i14 = i13 + 1;
                            if (this.mViewPortHandler.isInBoundsY(barBuffer2.buffer[i14]) && this.mViewPortHandler.isInBoundsLeft(f16)) {
                                int i15 = i13 / 4;
                                Entry entry = (BarEntry) iBarDataSet.getEntryForIndex(i15);
                                float y = entry.getY();
                                i4 = i13;
                                barBuffer = barBuffer2;
                                drawValue(canvas, iBarDataSet.getValueFormatter(), y, entry, i5, f16, y >= 0.0f ? barBuffer2.buffer[i14] + f5 : barBuffer2.buffer[i13 + 3] + f6, iBarDataSet.getValueTextColor(i15));
                            } else {
                                i4 = i13;
                                barBuffer = barBuffer2;
                            }
                            i13 = i4 + 4;
                            barBuffer2 = barBuffer;
                        }
                    }
                }
                i5++;
                convertDpToPixel = convertDpToPixel;
                dataSets = dataSets;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        float y;
        float f;
        BarData barData = this.mChart.getBarData();
        for (Highlight highlight : highlightArr) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(highlight.getDataSetIndex());
            if (iBarDataSet != null && iBarDataSet.isHighlightEnabled()) {
                BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForXValue(highlight.getX(), highlight.getY());
                if (isInBoundsX(barEntry, iBarDataSet)) {
                    Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
                    this.mHighlightPaint.setColor(iBarDataSet.getHighLightColor());
                    this.mHighlightPaint.setAlpha(iBarDataSet.getHighLightAlpha());
                    if (highlight.getStackIndex() >= 0 && barEntry.isStacked()) {
                        if (this.mChart.isHighlightFullBarEnabled()) {
                            float positiveSum = barEntry.getPositiveSum();
                            f = -barEntry.getNegativeSum();
                            y = positiveSum;
                        } else {
                            Range range = barEntry.getRanges()[highlight.getStackIndex()];
                            y = range.from;
                            f = range.to;
                        }
                    } else {
                        y = barEntry.getY();
                        f = 0.0f;
                    }
                    prepareBarHighlight(barEntry.getX(), y, f, barData.getBarWidth() / 2.0f, transformer);
                    setHighlightDrawPos(highlight, this.mBarRect);
                    canvas.drawRect(this.mBarRect, this.mHighlightPaint);
                }
            }
        }
    }

    protected void setHighlightDrawPos(Highlight highlight, RectF rectF) {
        highlight.setDraw(rectF.centerX(), rectF.top);
    }
}
