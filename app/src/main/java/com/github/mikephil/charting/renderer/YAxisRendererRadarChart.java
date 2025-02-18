package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Path;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

/* loaded from: classes.dex */
public class YAxisRendererRadarChart extends YAxisRenderer {
    private RadarChart mChart;
    private Path mRenderLimitLinesPathBuffer;

    public YAxisRendererRadarChart(ViewPortHandler viewPortHandler, YAxis yAxis, RadarChart radarChart) {
        super(viewPortHandler, yAxis, null);
        this.mRenderLimitLinesPathBuffer = new Path();
        this.mChart = radarChart;
    }

    @Override // com.github.mikephil.charting.renderer.AxisRenderer
    protected void computeAxisValues(float f, float f2) {
        int i;
        float f3 = f;
        int labelCount = this.mAxis.getLabelCount();
        double abs = Math.abs(f2 - f3);
        if (labelCount == 0 || abs <= Utils.DOUBLE_EPSILON || Double.isInfinite(abs)) {
            this.mAxis.mEntries = new float[0];
            this.mAxis.mCenteredEntries = new float[0];
            this.mAxis.mEntryCount = 0;
            return;
        }
        double roundToNextSignificant = Utils.roundToNextSignificant(abs / labelCount);
        if (this.mAxis.isGranularityEnabled() && roundToNextSignificant < this.mAxis.getGranularity()) {
            roundToNextSignificant = this.mAxis.getGranularity();
        }
        double roundToNextSignificant2 = Utils.roundToNextSignificant(Math.pow(10.0d, (int) Math.log10(roundToNextSignificant)));
        if (((int) (roundToNextSignificant / roundToNextSignificant2)) > 5) {
            roundToNextSignificant = Math.floor(roundToNextSignificant2 * 10.0d);
        }
        boolean isCenterAxisLabelsEnabled = this.mAxis.isCenterAxisLabelsEnabled();
        if (this.mAxis.isForceLabelsEnabled()) {
            float f4 = ((float) abs) / (labelCount - 1);
            this.mAxis.mEntryCount = labelCount;
            if (this.mAxis.mEntries.length < labelCount) {
                this.mAxis.mEntries = new float[labelCount];
            }
            for (int i2 = 0; i2 < labelCount; i2++) {
                this.mAxis.mEntries[i2] = f3;
                f3 += f4;
            }
        } else {
            double ceil = roundToNextSignificant == Utils.DOUBLE_EPSILON ? Utils.DOUBLE_EPSILON : Math.ceil(f3 / roundToNextSignificant) * roundToNextSignificant;
            if (isCenterAxisLabelsEnabled) {
                ceil -= roundToNextSignificant;
            }
            double nextUp = roundToNextSignificant == Utils.DOUBLE_EPSILON ? Utils.DOUBLE_EPSILON : Utils.nextUp(Math.floor(f2 / roundToNextSignificant) * roundToNextSignificant);
            if (roundToNextSignificant != Utils.DOUBLE_EPSILON) {
                i = isCenterAxisLabelsEnabled ? 1 : 0;
                for (double d = ceil; d <= nextUp; d += roundToNextSignificant) {
                    i++;
                }
            } else {
                i = isCenterAxisLabelsEnabled ? 1 : 0;
            }
            int i3 = i + 1;
            this.mAxis.mEntryCount = i3;
            if (this.mAxis.mEntries.length < i3) {
                this.mAxis.mEntries = new float[i3];
            }
            for (int i4 = 0; i4 < i3; i4++) {
                if (ceil == Utils.DOUBLE_EPSILON) {
                    ceil = 0.0d;
                }
                this.mAxis.mEntries[i4] = (float) ceil;
                ceil += roundToNextSignificant;
            }
            labelCount = i3;
        }
        if (roundToNextSignificant < 1.0d) {
            this.mAxis.mDecimals = (int) Math.ceil(-Math.log10(roundToNextSignificant));
        } else {
            this.mAxis.mDecimals = 0;
        }
        if (isCenterAxisLabelsEnabled) {
            if (this.mAxis.mCenteredEntries.length < labelCount) {
                this.mAxis.mCenteredEntries = new float[labelCount];
            }
            float f5 = (this.mAxis.mEntries[1] - this.mAxis.mEntries[0]) / 2.0f;
            for (int i5 = 0; i5 < labelCount; i5++) {
                this.mAxis.mCenteredEntries[i5] = this.mAxis.mEntries[i5] + f5;
            }
        }
        this.mAxis.mAxisMinimum = this.mAxis.mEntries[0];
        this.mAxis.mAxisMaximum = this.mAxis.mEntries[labelCount - 1];
        this.mAxis.mAxisRange = Math.abs(this.mAxis.mAxisMaximum - this.mAxis.mAxisMinimum);
    }

    @Override // com.github.mikephil.charting.renderer.YAxisRenderer, com.github.mikephil.charting.renderer.AxisRenderer
    public void renderAxisLabels(Canvas canvas) {
        if (this.mYAxis.isEnabled() && this.mYAxis.isDrawLabelsEnabled()) {
            this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
            this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());
            MPPointF centerOffsets = this.mChart.getCenterOffsets();
            MPPointF mPPointF = MPPointF.getInstance(0.0f, 0.0f);
            float factor = this.mChart.getFactor();
            int i = this.mYAxis.isDrawTopYLabelEntryEnabled() ? this.mYAxis.mEntryCount : this.mYAxis.mEntryCount - 1;
            for (int i2 = !this.mYAxis.isDrawBottomYLabelEntryEnabled() ? 1 : 0; i2 < i; i2++) {
                Utils.getPosition(centerOffsets, (this.mYAxis.mEntries[i2] - this.mYAxis.mAxisMinimum) * factor, this.mChart.getRotationAngle(), mPPointF);
                canvas.drawText(this.mYAxis.getFormattedLabel(i2), mPPointF.x + 10.0f, mPPointF.y, this.mAxisLabelPaint);
            }
            MPPointF.recycleInstance(centerOffsets);
            MPPointF.recycleInstance(mPPointF);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.YAxisRenderer, com.github.mikephil.charting.renderer.AxisRenderer
    public void renderLimitLines(Canvas canvas) {
        List<LimitLine> limitLines = this.mYAxis.getLimitLines();
        if (limitLines == null) {
            return;
        }
        float sliceAngle = this.mChart.getSliceAngle();
        float factor = this.mChart.getFactor();
        MPPointF centerOffsets = this.mChart.getCenterOffsets();
        MPPointF mPPointF = MPPointF.getInstance(0.0f, 0.0f);
        for (int i = 0; i < limitLines.size(); i++) {
            LimitLine limitLine = limitLines.get(i);
            if (limitLine.isEnabled()) {
                this.mLimitLinePaint.setColor(limitLine.getLineColor());
                this.mLimitLinePaint.setPathEffect(limitLine.getDashPathEffect());
                this.mLimitLinePaint.setStrokeWidth(limitLine.getLineWidth());
                float limit = (limitLine.getLimit() - this.mChart.getYChartMin()) * factor;
                Path path = this.mRenderLimitLinesPathBuffer;
                path.reset();
                for (int i2 = 0; i2 < ((RadarData) this.mChart.getData()).getMaxEntryCountSet().getEntryCount(); i2++) {
                    Utils.getPosition(centerOffsets, limit, (i2 * sliceAngle) + this.mChart.getRotationAngle(), mPPointF);
                    if (i2 == 0) {
                        path.moveTo(mPPointF.x, mPPointF.y);
                    } else {
                        path.lineTo(mPPointF.x, mPPointF.y);
                    }
                }
                path.close();
                canvas.drawPath(path, this.mLimitLinePaint);
            }
        }
        MPPointF.recycleInstance(centerOffsets);
        MPPointF.recycleInstance(mPPointF);
    }
}
