package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

/* loaded from: classes.dex */
public class PieChart extends PieRadarChartBase<PieData> {
    private float[] mAbsoluteAngles;
    private CharSequence mCenterText;
    private MPPointF mCenterTextOffset;
    private float mCenterTextRadiusPercent;
    private RectF mCircleBox;
    private float[] mDrawAngles;
    private boolean mDrawCenterText;
    private boolean mDrawEntryLabels;
    private boolean mDrawHole;
    private boolean mDrawRoundedSlices;
    private boolean mDrawSlicesUnderHole;
    private float mHoleRadiusPercent;
    protected float mMaxAngle;
    protected float mTransparentCircleRadiusPercent;
    private boolean mUsePercentValues;

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase
    protected float getRequiredBaseOffset() {
        return 0.0f;
    }

    public PieChart(Context context) {
        super(context);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }

    public PieChart(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }

    public PieChart(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCircleBox = new RectF();
        this.mDrawEntryLabels = true;
        this.mDrawAngles = new float[1];
        this.mAbsoluteAngles = new float[1];
        this.mDrawHole = true;
        this.mDrawSlicesUnderHole = false;
        this.mUsePercentValues = false;
        this.mDrawRoundedSlices = false;
        this.mCenterText = "";
        this.mCenterTextOffset = MPPointF.getInstance(0.0f, 0.0f);
        this.mHoleRadiusPercent = 50.0f;
        this.mTransparentCircleRadiusPercent = 55.0f;
        this.mDrawCenterText = true;
        this.mCenterTextRadiusPercent = 100.0f;
        this.mMaxAngle = 360.0f;
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase, com.github.mikephil.charting.charts.Chart
    protected void init() {
        super.init();
        this.mRenderer = new PieChartRenderer(this, this.mAnimator, this.mViewPortHandler);
        this.mXAxis = null;
        this.mHighlighter = new PieHighlighter(this);
    }

    @Override // com.github.mikephil.charting.charts.Chart, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mData == 0) {
            return;
        }
        this.mRenderer.drawData(canvas);
        if (valuesToHighlight()) {
            this.mRenderer.drawHighlighted(canvas, this.mIndicesToHighlight);
        }
        this.mRenderer.drawExtras(canvas);
        this.mRenderer.drawValues(canvas);
        this.mLegendRenderer.renderLegend(canvas);
        drawDescription(canvas);
        drawMarkers(canvas);
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase, com.github.mikephil.charting.charts.Chart
    public void calculateOffsets() {
        super.calculateOffsets();
        if (this.mData == 0) {
            return;
        }
        float diameter = getDiameter() / 2.0f;
        MPPointF centerOffsets = getCenterOffsets();
        float selectionShift = ((PieData) this.mData).getDataSet().getSelectionShift();
        this.mCircleBox.set((centerOffsets.x - diameter) + selectionShift, (centerOffsets.y - diameter) + selectionShift, (centerOffsets.x + diameter) - selectionShift, (centerOffsets.y + diameter) - selectionShift);
        MPPointF.recycleInstance(centerOffsets);
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase, com.github.mikephil.charting.charts.Chart
    protected void calcMinMax() {
        calcAngles();
    }

    @Override // com.github.mikephil.charting.charts.Chart
    protected float[] getMarkerPosition(Highlight highlight) {
        MPPointF centerCircleBox = getCenterCircleBox();
        float radius = getRadius();
        float f = (radius / 10.0f) * 3.6f;
        if (isDrawHoleEnabled()) {
            f = (radius - ((radius / 100.0f) * getHoleRadius())) / 2.0f;
        }
        float f2 = radius - f;
        float rotationAngle = getRotationAngle();
        float f3 = this.mDrawAngles[(int) highlight.getX()] / 2.0f;
        double d = f2;
        float cos = (float) ((Math.cos(Math.toRadians(((this.mAbsoluteAngles[r11] + rotationAngle) - f3) * this.mAnimator.getPhaseY())) * d) + centerCircleBox.x);
        float sin = (float) ((d * Math.sin(Math.toRadians(((rotationAngle + this.mAbsoluteAngles[r11]) - f3) * this.mAnimator.getPhaseY()))) + centerCircleBox.y);
        MPPointF.recycleInstance(centerCircleBox);
        return new float[]{cos, sin};
    }

    private void calcAngles() {
        int entryCount = ((PieData) this.mData).getEntryCount();
        if (this.mDrawAngles.length != entryCount) {
            this.mDrawAngles = new float[entryCount];
        } else {
            for (int i = 0; i < entryCount; i++) {
                this.mDrawAngles[i] = 0.0f;
            }
        }
        if (this.mAbsoluteAngles.length != entryCount) {
            this.mAbsoluteAngles = new float[entryCount];
        } else {
            for (int i2 = 0; i2 < entryCount; i2++) {
                this.mAbsoluteAngles[i2] = 0.0f;
            }
        }
        float yValueSum = ((PieData) this.mData).getYValueSum();
        List<IPieDataSet> dataSets = ((PieData) this.mData).getDataSets();
        int i3 = 0;
        for (int i4 = 0; i4 < ((PieData) this.mData).getDataSetCount(); i4++) {
            IPieDataSet iPieDataSet = dataSets.get(i4);
            for (int i5 = 0; i5 < iPieDataSet.getEntryCount(); i5++) {
                this.mDrawAngles[i3] = calcAngle(Math.abs(iPieDataSet.getEntryForIndex(i5).getY()), yValueSum);
                if (i3 == 0) {
                    this.mAbsoluteAngles[i3] = this.mDrawAngles[i3];
                } else {
                    float[] fArr = this.mAbsoluteAngles;
                    fArr[i3] = fArr[i3 - 1] + this.mDrawAngles[i3];
                }
                i3++;
            }
        }
    }

    public boolean needsHighlight(int i) {
        if (!valuesToHighlight()) {
            return false;
        }
        for (int i2 = 0; i2 < this.mIndicesToHighlight.length; i2++) {
            if (((int) this.mIndicesToHighlight[i2].getX()) == i) {
                return true;
            }
        }
        return false;
    }

    private float calcAngle(float f) {
        return calcAngle(f, ((PieData) this.mData).getYValueSum());
    }

    private float calcAngle(float f, float f2) {
        return (f / f2) * this.mMaxAngle;
    }

    @Override // com.github.mikephil.charting.charts.Chart
    @Deprecated
    public XAxis getXAxis() {
        throw new RuntimeException("PieChart has no XAxis");
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase
    public int getIndexForAngle(float f) {
        float normalizedAngle = Utils.getNormalizedAngle(f - getRotationAngle());
        int i = 0;
        while (true) {
            float[] fArr = this.mAbsoluteAngles;
            if (i >= fArr.length) {
                return -1;
            }
            if (fArr[i] > normalizedAngle) {
                return i;
            }
            i++;
        }
    }

    public int getDataSetIndexForIndex(int i) {
        List<IPieDataSet> dataSets = ((PieData) this.mData).getDataSets();
        for (int i2 = 0; i2 < dataSets.size(); i2++) {
            if (dataSets.get(i2).getEntryForXValue(i, Float.NaN) != null) {
                return i2;
            }
        }
        return -1;
    }

    public float[] getDrawAngles() {
        return this.mDrawAngles;
    }

    public float[] getAbsoluteAngles() {
        return this.mAbsoluteAngles;
    }

    public void setHoleColor(int i) {
        ((PieChartRenderer) this.mRenderer).getPaintHole().setColor(i);
    }

    public void setDrawSlicesUnderHole(boolean z) {
        this.mDrawSlicesUnderHole = z;
    }

    public boolean isDrawSlicesUnderHoleEnabled() {
        return this.mDrawSlicesUnderHole;
    }

    public void setDrawHoleEnabled(boolean z) {
        this.mDrawHole = z;
    }

    public boolean isDrawHoleEnabled() {
        return this.mDrawHole;
    }

    public void setCenterText(CharSequence charSequence) {
        if (charSequence == null) {
            this.mCenterText = "";
        } else {
            this.mCenterText = charSequence;
        }
    }

    public CharSequence getCenterText() {
        return this.mCenterText;
    }

    public void setDrawCenterText(boolean z) {
        this.mDrawCenterText = z;
    }

    public boolean isDrawCenterTextEnabled() {
        return this.mDrawCenterText;
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase
    protected float getRequiredLegendOffset() {
        return this.mLegendRenderer.getLabelPaint().getTextSize() * 2.0f;
    }

    @Override // com.github.mikephil.charting.charts.PieRadarChartBase
    public float getRadius() {
        RectF rectF = this.mCircleBox;
        if (rectF == null) {
            return 0.0f;
        }
        return Math.min(rectF.width() / 2.0f, this.mCircleBox.height() / 2.0f);
    }

    public RectF getCircleBox() {
        return this.mCircleBox;
    }

    public MPPointF getCenterCircleBox() {
        return MPPointF.getInstance(this.mCircleBox.centerX(), this.mCircleBox.centerY());
    }

    public void setCenterTextTypeface(Typeface typeface) {
        ((PieChartRenderer) this.mRenderer).getPaintCenterText().setTypeface(typeface);
    }

    public void setCenterTextSize(float f) {
        ((PieChartRenderer) this.mRenderer).getPaintCenterText().setTextSize(Utils.convertDpToPixel(f));
    }

    public void setCenterTextSizePixels(float f) {
        ((PieChartRenderer) this.mRenderer).getPaintCenterText().setTextSize(f);
    }

    public void setCenterTextOffset(float f, float f2) {
        this.mCenterTextOffset.x = Utils.convertDpToPixel(f);
        this.mCenterTextOffset.y = Utils.convertDpToPixel(f2);
    }

    public MPPointF getCenterTextOffset() {
        return MPPointF.getInstance(this.mCenterTextOffset.x, this.mCenterTextOffset.y);
    }

    public void setCenterTextColor(int i) {
        ((PieChartRenderer) this.mRenderer).getPaintCenterText().setColor(i);
    }

    public void setHoleRadius(float f) {
        this.mHoleRadiusPercent = f;
    }

    public float getHoleRadius() {
        return this.mHoleRadiusPercent;
    }

    public void setTransparentCircleColor(int i) {
        Paint paintTransparentCircle = ((PieChartRenderer) this.mRenderer).getPaintTransparentCircle();
        int alpha = paintTransparentCircle.getAlpha();
        paintTransparentCircle.setColor(i);
        paintTransparentCircle.setAlpha(alpha);
    }

    public void setTransparentCircleRadius(float f) {
        this.mTransparentCircleRadiusPercent = f;
    }

    public float getTransparentCircleRadius() {
        return this.mTransparentCircleRadiusPercent;
    }

    public void setTransparentCircleAlpha(int i) {
        ((PieChartRenderer) this.mRenderer).getPaintTransparentCircle().setAlpha(i);
    }

    @Deprecated
    public void setDrawSliceText(boolean z) {
        this.mDrawEntryLabels = z;
    }

    public void setDrawEntryLabels(boolean z) {
        this.mDrawEntryLabels = z;
    }

    public boolean isDrawEntryLabelsEnabled() {
        return this.mDrawEntryLabels;
    }

    public void setEntryLabelColor(int i) {
        ((PieChartRenderer) this.mRenderer).getPaintEntryLabels().setColor(i);
    }

    public void setEntryLabelTypeface(Typeface typeface) {
        ((PieChartRenderer) this.mRenderer).getPaintEntryLabels().setTypeface(typeface);
    }

    public void setEntryLabelTextSize(float f) {
        ((PieChartRenderer) this.mRenderer).getPaintEntryLabels().setTextSize(Utils.convertDpToPixel(f));
    }

    public boolean isDrawRoundedSlicesEnabled() {
        return this.mDrawRoundedSlices;
    }

    public void setUsePercentValues(boolean z) {
        this.mUsePercentValues = z;
    }

    public boolean isUsePercentValuesEnabled() {
        return this.mUsePercentValues;
    }

    public void setCenterTextRadiusPercent(float f) {
        this.mCenterTextRadiusPercent = f;
    }

    public float getCenterTextRadiusPercent() {
        return this.mCenterTextRadiusPercent;
    }

    public float getMaxAngle() {
        return this.mMaxAngle;
    }

    public void setMaxAngle(float f) {
        if (f > 360.0f) {
            f = 360.0f;
        }
        if (f < 90.0f) {
            f = 90.0f;
        }
        this.mMaxAngle = f;
    }

    @Override // com.github.mikephil.charting.charts.Chart, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        if (this.mRenderer != null && (this.mRenderer instanceof PieChartRenderer)) {
            ((PieChartRenderer) this.mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
