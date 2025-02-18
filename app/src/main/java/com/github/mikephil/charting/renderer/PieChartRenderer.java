package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import androidx.core.view.ViewCompat;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes.dex */
public class PieChartRenderer extends DataRenderer {
    protected Canvas mBitmapCanvas;
    private RectF mCenterTextLastBounds;
    private CharSequence mCenterTextLastValue;
    private StaticLayout mCenterTextLayout;
    private TextPaint mCenterTextPaint;
    protected PieChart mChart;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Path mDrawCenterTextPathBuffer;
    protected RectF mDrawHighlightedRectF;
    private Paint mEntryLabelsPaint;
    private Path mHoleCirclePath;
    protected Paint mHolePaint;
    private RectF mInnerRectBuffer;
    private Path mPathBuffer;
    private RectF[] mRectBuffer;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void initBuffers() {
    }

    public PieChartRenderer(PieChart pieChart, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mCenterTextLastBounds = new RectF();
        this.mRectBuffer = new RectF[]{new RectF(), new RectF(), new RectF()};
        this.mPathBuffer = new Path();
        this.mInnerRectBuffer = new RectF();
        this.mHoleCirclePath = new Path();
        this.mDrawCenterTextPathBuffer = new Path();
        this.mDrawHighlightedRectF = new RectF();
        this.mChart = pieChart;
        Paint paint = new Paint(1);
        this.mHolePaint = paint;
        paint.setColor(-1);
        this.mHolePaint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint(1);
        this.mTransparentCirclePaint = paint2;
        paint2.setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        TextPaint textPaint = new TextPaint(1);
        this.mCenterTextPaint = textPaint;
        textPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        Paint paint3 = new Paint(1);
        this.mEntryLabelsPaint = paint3;
        paint3.setColor(-1);
        this.mEntryLabelsPaint.setTextAlign(Paint.Align.CENTER);
        this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0f));
        Paint paint4 = new Paint(1);
        this.mValueLinePaint = paint4;
        paint4.setStyle(Paint.Style.STROKE);
    }

    public Paint getPaintHole() {
        return this.mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return this.mEntryLabelsPaint;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawData(Canvas canvas) {
        int chartWidth = (int) this.mViewPortHandler.getChartWidth();
        int chartHeight = (int) this.mViewPortHandler.getChartHeight();
        WeakReference<Bitmap> weakReference = this.mDrawBitmap;
        if (weakReference == null || weakReference.get().getWidth() != chartWidth || this.mDrawBitmap.get().getHeight() != chartHeight) {
            if (chartWidth <= 0 || chartHeight <= 0) {
                return;
            }
            this.mDrawBitmap = new WeakReference<>(Bitmap.createBitmap(chartWidth, chartHeight, Bitmap.Config.ARGB_4444));
            this.mBitmapCanvas = new Canvas(this.mDrawBitmap.get());
        }
        this.mDrawBitmap.get().eraseColor(0);
        for (IPieDataSet iPieDataSet : ((PieData) this.mChart.getData()).getDataSets()) {
            if (iPieDataSet.isVisible() && iPieDataSet.getEntryCount() > 0) {
                drawDataSet(canvas, iPieDataSet);
            }
        }
    }

    protected float calculateMinimumRadiusForSpacedSlice(MPPointF mPPointF, float f, float f2, float f3, float f4, float f5, float f6) {
        double d = (f5 + f6) * 0.017453292f;
        float cos = mPPointF.x + (((float) Math.cos(d)) * f);
        float sin = mPPointF.y + (((float) Math.sin(d)) * f);
        double d2 = (f5 + (f6 / 2.0f)) * 0.017453292f;
        return (float) ((f - ((float) ((Math.sqrt(Math.pow(cos - f3, 2.0d) + Math.pow(sin - f4, 2.0d)) / 2.0d) * Math.tan(((180.0d - f2) / 2.0d) * 0.017453292519943295d)))) - Math.sqrt(Math.pow((mPPointF.x + (((float) Math.cos(d2)) * f)) - ((cos + f3) / 2.0f), 2.0d) + Math.pow((mPPointF.y + (((float) Math.sin(d2)) * f)) - ((sin + f4) / 2.0f), 2.0d)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected float getSliceSpace(IPieDataSet iPieDataSet) {
        if (!iPieDataSet.isAutomaticallyDisableSliceSpacingEnabled()) {
            return iPieDataSet.getSliceSpace();
        }
        if (iPieDataSet.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension() > (iPieDataSet.getYMin() / ((PieData) this.mChart.getData()).getYValueSum()) * 2.0f) {
            return 0.0f;
        }
        return iPieDataSet.getSliceSpace();
    }

    protected void drawDataSet(Canvas canvas, IPieDataSet iPieDataSet) {
        int i;
        float f;
        float f2;
        float f3;
        RectF rectF;
        int i2;
        float[] fArr;
        int i3;
        float f4;
        MPPointF mPPointF;
        float f5;
        float f6;
        MPPointF mPPointF2;
        float f7;
        int i4;
        PieChartRenderer pieChartRenderer = this;
        IPieDataSet iPieDataSet2 = iPieDataSet;
        float rotationAngle = pieChartRenderer.mChart.getRotationAngle();
        float phaseX = pieChartRenderer.mAnimator.getPhaseX();
        float phaseY = pieChartRenderer.mAnimator.getPhaseY();
        RectF circleBox = pieChartRenderer.mChart.getCircleBox();
        int entryCount = iPieDataSet.getEntryCount();
        float[] drawAngles = pieChartRenderer.mChart.getDrawAngles();
        MPPointF centerCircleBox = pieChartRenderer.mChart.getCenterCircleBox();
        float radius = pieChartRenderer.mChart.getRadius();
        boolean z = pieChartRenderer.mChart.isDrawHoleEnabled() && !pieChartRenderer.mChart.isDrawSlicesUnderHoleEnabled();
        float holeRadius = z ? (pieChartRenderer.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        int i5 = 0;
        for (int i6 = 0; i6 < entryCount; i6++) {
            if (Math.abs(iPieDataSet2.getEntryForIndex(i6).getY()) > Utils.FLOAT_EPSILON) {
                i5++;
            }
        }
        float sliceSpace = i5 <= 1 ? 0.0f : pieChartRenderer.getSliceSpace(iPieDataSet2);
        int i7 = 0;
        float f8 = 0.0f;
        while (i7 < entryCount) {
            float f9 = drawAngles[i7];
            if (Math.abs(iPieDataSet2.getEntryForIndex(i7).getY()) <= Utils.FLOAT_EPSILON || pieChartRenderer.mChart.needsHighlight(i7)) {
                i = i7;
                f = radius;
                f2 = rotationAngle;
                f3 = phaseX;
                rectF = circleBox;
                i2 = entryCount;
                fArr = drawAngles;
                i3 = i5;
                f4 = holeRadius;
                mPPointF = centerCircleBox;
            } else {
                boolean z2 = sliceSpace > 0.0f && f9 <= 180.0f;
                pieChartRenderer.mRenderPaint.setColor(iPieDataSet2.getColor(i7));
                float f10 = i5 == 1 ? 0.0f : sliceSpace / (radius * 0.017453292f);
                float f11 = rotationAngle + ((f8 + (f10 / 2.0f)) * phaseY);
                float f12 = (f9 - f10) * phaseY;
                if (f12 < 0.0f) {
                    f12 = 0.0f;
                }
                pieChartRenderer.mPathBuffer.reset();
                int i8 = i7;
                int i9 = i5;
                double d = f11 * 0.017453292f;
                i2 = entryCount;
                fArr = drawAngles;
                float cos = centerCircleBox.x + (((float) Math.cos(d)) * radius);
                float sin = centerCircleBox.y + (((float) Math.sin(d)) * radius);
                if (f12 >= 360.0f && f12 % 360.0f <= Utils.FLOAT_EPSILON) {
                    f3 = phaseX;
                    pieChartRenderer.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, radius, Path.Direction.CW);
                } else {
                    f3 = phaseX;
                    pieChartRenderer.mPathBuffer.moveTo(cos, sin);
                    pieChartRenderer.mPathBuffer.arcTo(circleBox, f11, f12);
                }
                float f13 = f12;
                pieChartRenderer.mInnerRectBuffer.set(centerCircleBox.x - holeRadius, centerCircleBox.y - holeRadius, centerCircleBox.x + holeRadius, centerCircleBox.y + holeRadius);
                if (!z) {
                    f4 = holeRadius;
                    f2 = rotationAngle;
                    f5 = f13;
                    f = radius;
                    mPPointF = centerCircleBox;
                    rectF = circleBox;
                    i3 = i9;
                    i = i8;
                    f6 = 360.0f;
                } else if (holeRadius > 0.0f || z2) {
                    if (z2) {
                        f7 = f13;
                        rectF = circleBox;
                        i3 = i9;
                        i = i8;
                        f4 = holeRadius;
                        i4 = 1;
                        f = radius;
                        mPPointF2 = centerCircleBox;
                        float calculateMinimumRadiusForSpacedSlice = calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, f9 * phaseY, cos, sin, f11, f7);
                        if (calculateMinimumRadiusForSpacedSlice < 0.0f) {
                            calculateMinimumRadiusForSpacedSlice = -calculateMinimumRadiusForSpacedSlice;
                        }
                        holeRadius = Math.max(f4, calculateMinimumRadiusForSpacedSlice);
                    } else {
                        f4 = holeRadius;
                        mPPointF2 = centerCircleBox;
                        f7 = f13;
                        i4 = 1;
                        f = radius;
                        rectF = circleBox;
                        i3 = i9;
                        i = i8;
                    }
                    float f14 = (i3 == i4 || holeRadius == 0.0f) ? 0.0f : sliceSpace / (holeRadius * 0.017453292f);
                    float f15 = ((f8 + (f14 / 2.0f)) * phaseY) + rotationAngle;
                    float f16 = (f9 - f14) * phaseY;
                    if (f16 < 0.0f) {
                        f16 = 0.0f;
                    }
                    float f17 = f15 + f16;
                    if (f12 >= 360.0f && f7 % 360.0f <= Utils.FLOAT_EPSILON) {
                        pieChartRenderer = this;
                        pieChartRenderer.mPathBuffer.addCircle(mPPointF2.x, mPPointF2.y, holeRadius, Path.Direction.CCW);
                        f2 = rotationAngle;
                    } else {
                        pieChartRenderer = this;
                        double d2 = f17 * 0.017453292f;
                        f2 = rotationAngle;
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF2.x + (((float) Math.cos(d2)) * holeRadius), mPPointF2.y + (holeRadius * ((float) Math.sin(d2))));
                        pieChartRenderer.mPathBuffer.arcTo(pieChartRenderer.mInnerRectBuffer, f17, -f16);
                    }
                    mPPointF = mPPointF2;
                    pieChartRenderer.mPathBuffer.close();
                    pieChartRenderer.mBitmapCanvas.drawPath(pieChartRenderer.mPathBuffer, pieChartRenderer.mRenderPaint);
                } else {
                    f4 = holeRadius;
                    f2 = rotationAngle;
                    f5 = f13;
                    f6 = 360.0f;
                    f = radius;
                    mPPointF = centerCircleBox;
                    rectF = circleBox;
                    i3 = i9;
                    i = i8;
                }
                if (f5 % f6 > Utils.FLOAT_EPSILON) {
                    if (z2) {
                        float calculateMinimumRadiusForSpacedSlice2 = calculateMinimumRadiusForSpacedSlice(mPPointF, f, f9 * phaseY, cos, sin, f11, f5);
                        double d3 = (f11 + (f5 / 2.0f)) * 0.017453292f;
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF.x + (((float) Math.cos(d3)) * calculateMinimumRadiusForSpacedSlice2), mPPointF.y + (calculateMinimumRadiusForSpacedSlice2 * ((float) Math.sin(d3))));
                    } else {
                        pieChartRenderer.mPathBuffer.lineTo(mPPointF.x, mPPointF.y);
                    }
                }
                pieChartRenderer.mPathBuffer.close();
                pieChartRenderer.mBitmapCanvas.drawPath(pieChartRenderer.mPathBuffer, pieChartRenderer.mRenderPaint);
            }
            f8 += f9 * f3;
            i7 = i + 1;
            iPieDataSet2 = iPieDataSet;
            centerCircleBox = mPPointF;
            i5 = i3;
            holeRadius = f4;
            circleBox = rectF;
            entryCount = i2;
            drawAngles = fArr;
            phaseX = f3;
            radius = f;
            rotationAngle = f2;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawValues(Canvas canvas) {
        int i;
        List<IPieDataSet> list;
        float f;
        float f2;
        float[] fArr;
        float[] fArr2;
        PieDataSet.ValuePosition valuePosition;
        float f3;
        PieDataSet.ValuePosition valuePosition2;
        float f4;
        float f5;
        float f6;
        float f7;
        int i2;
        int i3;
        IPieDataSet iPieDataSet;
        float f8;
        IPieDataSet iPieDataSet2;
        float[] fArr3;
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float holeRadius = this.mChart.getHoleRadius() / 100.0f;
        float f9 = (radius / 10.0f) * 3.6f;
        if (this.mChart.isDrawHoleEnabled()) {
            f9 = (radius - (radius * holeRadius)) / 2.0f;
        }
        float f10 = radius - f9;
        PieData pieData = (PieData) this.mChart.getData();
        List<IPieDataSet> dataSets = pieData.getDataSets();
        float yValueSum = pieData.getYValueSum();
        boolean isDrawEntryLabelsEnabled = this.mChart.isDrawEntryLabelsEnabled();
        canvas.save();
        float convertDpToPixel = Utils.convertDpToPixel(5.0f);
        int i4 = 0;
        int i5 = 0;
        while (i5 < dataSets.size()) {
            IPieDataSet iPieDataSet3 = dataSets.get(i5);
            boolean isDrawValuesEnabled = iPieDataSet3.isDrawValuesEnabled();
            if (isDrawValuesEnabled || isDrawEntryLabelsEnabled) {
                PieDataSet.ValuePosition xValuePosition = iPieDataSet3.getXValuePosition();
                PieDataSet.ValuePosition yValuePosition = iPieDataSet3.getYValuePosition();
                applyValueTextStyle(iPieDataSet3);
                float calcTextHeight = Utils.calcTextHeight(this.mValuePaint, "Q") + Utils.convertDpToPixel(4.0f);
                IValueFormatter valueFormatter = iPieDataSet3.getValueFormatter();
                int entryCount = iPieDataSet3.getEntryCount();
                this.mValueLinePaint.setColor(iPieDataSet3.getValueLineColor());
                this.mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(iPieDataSet3.getValueLineWidth()));
                float sliceSpace = getSliceSpace(iPieDataSet3);
                int i6 = i4;
                int i7 = 0;
                while (i7 < entryCount) {
                    PieEntry entryForIndex = iPieDataSet3.getEntryForIndex(i7);
                    float f11 = (((i6 == 0 ? 0.0f : absoluteAngles[i6 - 1] * phaseX) + ((drawAngles[i6] - ((sliceSpace / (f10 * 0.017453292f)) / 2.0f)) / 2.0f)) * phaseY) + rotationAngle;
                    float y = this.mChart.isUsePercentValuesEnabled() ? (entryForIndex.getY() / yValueSum) * 100.0f : entryForIndex.getY();
                    int i8 = i7;
                    double d = f11 * 0.017453292f;
                    int i9 = i5;
                    List<IPieDataSet> list2 = dataSets;
                    float cos = (float) Math.cos(d);
                    float f12 = rotationAngle;
                    float[] fArr4 = drawAngles;
                    float sin = (float) Math.sin(d);
                    boolean z = isDrawEntryLabelsEnabled && xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    boolean z2 = isDrawValuesEnabled && yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                    int i10 = entryCount;
                    boolean z3 = isDrawEntryLabelsEnabled && xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    boolean z4 = isDrawValuesEnabled && yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                    if (z || z2) {
                        float valueLinePart1Length = iPieDataSet3.getValueLinePart1Length();
                        float valueLinePart2Length = iPieDataSet3.getValueLinePart2Length();
                        float valueLinePart1OffsetPercentage = iPieDataSet3.getValueLinePart1OffsetPercentage() / 100.0f;
                        valuePosition = yValuePosition;
                        if (this.mChart.isDrawHoleEnabled()) {
                            float f13 = radius * holeRadius;
                            f3 = ((radius - f13) * valueLinePart1OffsetPercentage) + f13;
                        } else {
                            f3 = radius * valueLinePart1OffsetPercentage;
                        }
                        float abs = iPieDataSet3.isValueLineVariableLength() ? valueLinePart2Length * f10 * ((float) Math.abs(Math.sin(d))) : valueLinePart2Length * f10;
                        float f14 = (f3 * cos) + centerCircleBox.x;
                        float f15 = (f3 * sin) + centerCircleBox.y;
                        float f16 = (valueLinePart1Length + 1.0f) * f10;
                        valuePosition2 = xValuePosition;
                        float f17 = (f16 * cos) + centerCircleBox.x;
                        float f18 = (f16 * sin) + centerCircleBox.y;
                        double d2 = f11 % 360.0d;
                        if (d2 >= 90.0d && d2 <= 270.0d) {
                            float f19 = f17 - abs;
                            this.mValuePaint.setTextAlign(Paint.Align.RIGHT);
                            if (z) {
                                this.mEntryLabelsPaint.setTextAlign(Paint.Align.RIGHT);
                            }
                            f4 = f19;
                            f5 = f19 - convertDpToPixel;
                        } else {
                            f4 = f17 + abs;
                            this.mValuePaint.setTextAlign(Paint.Align.LEFT);
                            if (z) {
                                this.mEntryLabelsPaint.setTextAlign(Paint.Align.LEFT);
                            }
                            f5 = f4 + convertDpToPixel;
                        }
                        if (iPieDataSet3.getValueLineColor() != 1122867) {
                            i3 = i10;
                            f7 = radius;
                            i2 = i8;
                            f6 = f5;
                            canvas.drawLine(f14, f15, f17, f18, this.mValueLinePaint);
                            canvas.drawLine(f17, f18, f4, f18, this.mValueLinePaint);
                        } else {
                            f6 = f5;
                            f7 = radius;
                            i2 = i8;
                            i3 = i10;
                        }
                        if (z && z2) {
                            iPieDataSet = iPieDataSet3;
                            f8 = cos;
                            drawValue(canvas, valueFormatter, y, entryForIndex, 0, f6, f18, iPieDataSet3.getValueTextColor(i2));
                            if (i2 < pieData.getEntryCount() && entryForIndex.getLabel() != null) {
                                drawEntryLabel(canvas, entryForIndex.getLabel(), f6, f18 + calcTextHeight);
                            }
                        } else {
                            iPieDataSet = iPieDataSet3;
                            f8 = cos;
                            float f20 = f6;
                            if (z) {
                                if (i2 < pieData.getEntryCount() && entryForIndex.getLabel() != null) {
                                    drawEntryLabel(canvas, entryForIndex.getLabel(), f20, f18 + (calcTextHeight / 2.0f));
                                }
                            } else if (z2) {
                                iPieDataSet2 = iPieDataSet;
                                drawValue(canvas, valueFormatter, y, entryForIndex, 0, f20, f18 + (calcTextHeight / 2.0f), iPieDataSet2.getValueTextColor(i2));
                            }
                        }
                        iPieDataSet2 = iPieDataSet;
                    } else {
                        valuePosition = yValuePosition;
                        valuePosition2 = xValuePosition;
                        iPieDataSet2 = iPieDataSet3;
                        f8 = cos;
                        f7 = radius;
                        i2 = i8;
                        i3 = i10;
                    }
                    if (z3 || z4) {
                        float f21 = (f10 * f8) + centerCircleBox.x;
                        float f22 = (sin * f10) + centerCircleBox.y;
                        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
                        if (z3 && z4) {
                            fArr3 = absoluteAngles;
                            drawValue(canvas, valueFormatter, y, entryForIndex, 0, f21, f22, iPieDataSet2.getValueTextColor(i2));
                            if (i2 < pieData.getEntryCount() && entryForIndex.getLabel() != null) {
                                drawEntryLabel(canvas, entryForIndex.getLabel(), f21, f22 + calcTextHeight);
                            }
                        } else {
                            fArr3 = absoluteAngles;
                            if (z3) {
                                if (i2 < pieData.getEntryCount() && entryForIndex.getLabel() != null) {
                                    drawEntryLabel(canvas, entryForIndex.getLabel(), f21, f22 + (calcTextHeight / 2.0f));
                                }
                            } else if (z4) {
                                drawValue(canvas, valueFormatter, y, entryForIndex, 0, f21, f22 + (calcTextHeight / 2.0f), iPieDataSet2.getValueTextColor(i2));
                            }
                        }
                    } else {
                        fArr3 = absoluteAngles;
                    }
                    i6++;
                    i7 = i2 + 1;
                    iPieDataSet3 = iPieDataSet2;
                    entryCount = i3;
                    dataSets = list2;
                    i5 = i9;
                    rotationAngle = f12;
                    drawAngles = fArr4;
                    radius = f7;
                    absoluteAngles = fArr3;
                    xValuePosition = valuePosition2;
                    yValuePosition = valuePosition;
                }
                i = i5;
                list = dataSets;
                f = radius;
                f2 = rotationAngle;
                fArr = drawAngles;
                fArr2 = absoluteAngles;
                i4 = i6;
            } else {
                i = i5;
                list = dataSets;
                f = radius;
                f2 = rotationAngle;
                fArr = drawAngles;
                fArr2 = absoluteAngles;
            }
            i5 = i + 1;
            dataSets = list;
            rotationAngle = f2;
            drawAngles = fArr;
            radius = f;
            absoluteAngles = fArr2;
        }
        MPPointF.recycleInstance(centerCircleBox);
        canvas.restore();
    }

    protected void drawEntryLabel(Canvas canvas, String str, float f, float f2) {
        canvas.drawText(str, f, f2, this.mEntryLabelsPaint);
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawExtras(Canvas canvas) {
        drawHole(canvas);
        canvas.drawBitmap(this.mDrawBitmap.get(), 0.0f, 0.0f, (Paint) null);
        drawCenterText(canvas);
    }

    protected void drawHole(Canvas canvas) {
        if (!this.mChart.isDrawHoleEnabled() || this.mBitmapCanvas == null) {
            return;
        }
        float radius = this.mChart.getRadius();
        float holeRadius = (this.mChart.getHoleRadius() / 100.0f) * radius;
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        if (Color.alpha(this.mHolePaint.getColor()) > 0) {
            this.mBitmapCanvas.drawCircle(centerCircleBox.x, centerCircleBox.y, holeRadius, this.mHolePaint);
        }
        if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
            int alpha = this.mTransparentCirclePaint.getAlpha();
            float transparentCircleRadius = radius * (this.mChart.getTransparentCircleRadius() / 100.0f);
            this.mTransparentCirclePaint.setAlpha((int) (alpha * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
            this.mHoleCirclePath.reset();
            this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, transparentCircleRadius, Path.Direction.CW);
            this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, holeRadius, Path.Direction.CCW);
            this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
            this.mTransparentCirclePaint.setAlpha(alpha);
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    protected void drawCenterText(Canvas canvas) {
        float radius;
        MPPointF mPPointF;
        CharSequence centerText = this.mChart.getCenterText();
        if (!this.mChart.isDrawCenterTextEnabled() || centerText == null) {
            return;
        }
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        MPPointF centerTextOffset = this.mChart.getCenterTextOffset();
        float f = centerCircleBox.x + centerTextOffset.x;
        float f2 = centerCircleBox.y + centerTextOffset.y;
        if (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
            radius = this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f);
        } else {
            radius = this.mChart.getRadius();
        }
        RectF rectF = this.mRectBuffer[0];
        rectF.left = f - radius;
        rectF.top = f2 - radius;
        rectF.right = f + radius;
        rectF.bottom = f2 + radius;
        RectF rectF2 = this.mRectBuffer[1];
        rectF2.set(rectF);
        float centerTextRadiusPercent = this.mChart.getCenterTextRadiusPercent() / 100.0f;
        if (centerTextRadiusPercent > Utils.DOUBLE_EPSILON) {
            rectF2.inset((rectF2.width() - (rectF2.width() * centerTextRadiusPercent)) / 2.0f, (rectF2.height() - (rectF2.height() * centerTextRadiusPercent)) / 2.0f);
        }
        if (centerText.equals(this.mCenterTextLastValue) && rectF2.equals(this.mCenterTextLastBounds)) {
            mPPointF = centerTextOffset;
        } else {
            this.mCenterTextLastBounds.set(rectF2);
            this.mCenterTextLastValue = centerText;
            mPPointF = centerTextOffset;
            this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int) Math.max(Math.ceil(this.mCenterTextLastBounds.width()), 1.0d), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        }
        float height = this.mCenterTextLayout.getHeight();
        canvas.save();
        if (Build.VERSION.SDK_INT >= 18) {
            Path path = this.mDrawCenterTextPathBuffer;
            path.reset();
            path.addOval(rectF, Path.Direction.CW);
            canvas.clipPath(path);
        }
        canvas.translate(rectF2.left, rectF2.top + ((rectF2.height() - height) / 2.0f));
        this.mCenterTextLayout.draw(canvas);
        canvas.restore();
        MPPointF.recycleInstance(centerCircleBox);
        MPPointF.recycleInstance(mPPointF);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        int i;
        RectF rectF;
        float f;
        float f2;
        float[] fArr;
        float[] fArr2;
        IPieDataSet dataSetByIndex;
        float f3;
        int i2;
        float f4;
        float f5;
        int i3;
        int i4;
        float f6;
        float f7;
        float f8;
        Highlight[] highlightArr2 = highlightArr;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        boolean z = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float holeRadius = z ? (this.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        RectF rectF2 = this.mDrawHighlightedRectF;
        rectF2.set(0.0f, 0.0f, 0.0f, 0.0f);
        int i5 = 0;
        while (i5 < highlightArr2.length) {
            int x = (int) highlightArr2[i5].getX();
            if (x < drawAngles.length && (dataSetByIndex = ((PieData) this.mChart.getData()).getDataSetByIndex(highlightArr2[i5].getDataSetIndex())) != null && dataSetByIndex.isHighlightEnabled()) {
                int entryCount = dataSetByIndex.getEntryCount();
                int i6 = 0;
                for (int i7 = 0; i7 < entryCount; i7++) {
                    if (Math.abs(dataSetByIndex.getEntryForIndex(i7).getY()) > Utils.FLOAT_EPSILON) {
                        i6++;
                    }
                }
                if (x == 0) {
                    i2 = 1;
                    f3 = 0.0f;
                } else {
                    f3 = absoluteAngles[x - 1] * phaseX;
                    i2 = 1;
                }
                float sliceSpace = i6 <= i2 ? 0.0f : dataSetByIndex.getSliceSpace();
                float f9 = drawAngles[x];
                float selectionShift = dataSetByIndex.getSelectionShift();
                float f10 = radius + selectionShift;
                int i8 = i5;
                rectF2.set(this.mChart.getCircleBox());
                float f11 = -selectionShift;
                rectF2.inset(f11, f11);
                boolean z2 = sliceSpace > 0.0f && f9 <= 180.0f;
                this.mRenderPaint.setColor(dataSetByIndex.getColor(x));
                float f12 = i6 == 1 ? 0.0f : sliceSpace / (radius * 0.017453292f);
                float f13 = i6 == 1 ? 0.0f : sliceSpace / (f10 * 0.017453292f);
                float f14 = rotationAngle + ((f3 + (f12 / 2.0f)) * phaseY);
                float f15 = (f9 - f12) * phaseY;
                float f16 = f15 < 0.0f ? 0.0f : f15;
                float f17 = ((f3 + (f13 / 2.0f)) * phaseY) + rotationAngle;
                float f18 = (f9 - f13) * phaseY;
                if (f18 < 0.0f) {
                    f18 = 0.0f;
                }
                this.mPathBuffer.reset();
                if (f16 >= 360.0f && f16 % 360.0f <= Utils.FLOAT_EPSILON) {
                    this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, f10, Path.Direction.CW);
                    f4 = holeRadius;
                    f2 = phaseX;
                    fArr = drawAngles;
                    fArr2 = absoluteAngles;
                } else {
                    f4 = holeRadius;
                    f2 = phaseX;
                    double d = f17 * 0.017453292f;
                    fArr = drawAngles;
                    fArr2 = absoluteAngles;
                    this.mPathBuffer.moveTo(centerCircleBox.x + (((float) Math.cos(d)) * f10), centerCircleBox.y + (f10 * ((float) Math.sin(d))));
                    this.mPathBuffer.arcTo(rectF2, f17, f18);
                }
                if (z2) {
                    double d2 = f14 * 0.017453292f;
                    i = i8;
                    f5 = f4;
                    f6 = 0.0f;
                    i3 = i6;
                    rectF = rectF2;
                    i4 = 1;
                    f7 = calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, f9 * phaseY, (((float) Math.cos(d2)) * radius) + centerCircleBox.x, centerCircleBox.y + (((float) Math.sin(d2)) * radius), f14, f16);
                } else {
                    f5 = f4;
                    rectF = rectF2;
                    i3 = i6;
                    i = i8;
                    i4 = 1;
                    f6 = 0.0f;
                    f7 = 0.0f;
                }
                this.mInnerRectBuffer.set(centerCircleBox.x - f5, centerCircleBox.y - f5, centerCircleBox.x + f5, centerCircleBox.y + f5);
                if (!z || (f5 <= f6 && !z2)) {
                    f = f5;
                    if (f16 % 360.0f > Utils.FLOAT_EPSILON) {
                        if (z2) {
                            double d3 = (f14 + (f16 / 2.0f)) * 0.017453292f;
                            this.mPathBuffer.lineTo(centerCircleBox.x + (((float) Math.cos(d3)) * f7), centerCircleBox.y + (f7 * ((float) Math.sin(d3))));
                        } else {
                            this.mPathBuffer.lineTo(centerCircleBox.x, centerCircleBox.y);
                        }
                    }
                } else {
                    if (z2) {
                        if (f7 < f6) {
                            f7 = -f7;
                        }
                        f8 = Math.max(f5, f7);
                    } else {
                        f8 = f5;
                    }
                    float f19 = (i3 == i4 || f8 == f6) ? 0.0f : sliceSpace / (f8 * 0.017453292f);
                    float f20 = rotationAngle + ((f3 + (f19 / 2.0f)) * phaseY);
                    float f21 = (f9 - f19) * phaseY;
                    if (f21 < f6) {
                        f21 = 0.0f;
                    }
                    float f22 = f20 + f21;
                    if (f16 >= 360.0f && f16 % 360.0f <= Utils.FLOAT_EPSILON) {
                        this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, f8, Path.Direction.CCW);
                        f = f5;
                    } else {
                        double d4 = f22 * 0.017453292f;
                        f = f5;
                        this.mPathBuffer.lineTo(centerCircleBox.x + (((float) Math.cos(d4)) * f8), centerCircleBox.y + (f8 * ((float) Math.sin(d4))));
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, f22, -f21);
                    }
                }
                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
            } else {
                i = i5;
                rectF = rectF2;
                f = holeRadius;
                f2 = phaseX;
                fArr = drawAngles;
                fArr2 = absoluteAngles;
            }
            i5 = i + 1;
            rectF2 = rectF;
            highlightArr2 = highlightArr;
            holeRadius = f;
            phaseX = f2;
            drawAngles = fArr;
            absoluteAngles = fArr2;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void drawRoundedSlices(Canvas canvas) {
        float f;
        float[] fArr;
        float f2;
        if (this.mChart.isDrawRoundedSlicesEnabled()) {
            IPieDataSet dataSet = ((PieData) this.mChart.getData()).getDataSet();
            if (dataSet.isVisible()) {
                float phaseX = this.mAnimator.getPhaseX();
                float phaseY = this.mAnimator.getPhaseY();
                MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
                float radius = this.mChart.getRadius();
                float holeRadius = (radius - ((this.mChart.getHoleRadius() * radius) / 100.0f)) / 2.0f;
                float[] drawAngles = this.mChart.getDrawAngles();
                float rotationAngle = this.mChart.getRotationAngle();
                int i = 0;
                while (i < dataSet.getEntryCount()) {
                    float f3 = drawAngles[i];
                    if (Math.abs(dataSet.getEntryForIndex(i).getY()) > Utils.FLOAT_EPSILON) {
                        double d = radius - holeRadius;
                        double d2 = (rotationAngle + f3) * phaseY;
                        f = phaseY;
                        fArr = drawAngles;
                        f2 = rotationAngle;
                        float cos = (float) (centerCircleBox.x + (Math.cos(Math.toRadians(d2)) * d));
                        float sin = (float) ((d * Math.sin(Math.toRadians(d2))) + centerCircleBox.y);
                        this.mRenderPaint.setColor(dataSet.getColor(i));
                        this.mBitmapCanvas.drawCircle(cos, sin, holeRadius, this.mRenderPaint);
                    } else {
                        f = phaseY;
                        fArr = drawAngles;
                        f2 = rotationAngle;
                    }
                    rotationAngle = f2 + (f3 * phaseX);
                    i++;
                    phaseY = f;
                    drawAngles = fArr;
                }
                MPPointF.recycleInstance(centerCircleBox);
            }
        }
    }

    public void releaseBitmap() {
        Canvas canvas = this.mBitmapCanvas;
        if (canvas != null) {
            canvas.setBitmap(null);
            this.mBitmapCanvas = null;
        }
        WeakReference<Bitmap> weakReference = this.mDrawBitmap;
        if (weakReference != null) {
            weakReference.get().recycle();
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
