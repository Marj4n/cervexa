package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class LegendRenderer extends Renderer {
    protected List<LegendEntry> computedEntries;
    protected Paint.FontMetrics legendFontMetrics;
    protected Legend mLegend;
    protected Paint mLegendFormPaint;
    protected Paint mLegendLabelPaint;
    private Path mLineFormPath;

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);
        this.computedEntries = new ArrayList(16);
        this.legendFontMetrics = new Paint.FontMetrics();
        this.mLineFormPath = new Path();
        this.mLegend = legend;
        Paint paint = new Paint(1);
        this.mLegendLabelPaint = paint;
        paint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Paint.Align.LEFT);
        Paint paint2 = new Paint(1);
        this.mLegendFormPaint = paint2;
        paint2.setStyle(Paint.Style.FILL);
    }

    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }

    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    /* JADX WARN: Type inference failed for: r7v1, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    public void computeLegend(ChartData<?> chartData) {
        ChartData<?> chartData2;
        ChartData<?> chartData3 = chartData;
        if (!this.mLegend.isLegendCustom()) {
            this.computedEntries.clear();
            int i = 0;
            while (i < chartData.getDataSetCount()) {
                IDataSet<? extends Entry> dataSetByIndex = chartData3.getDataSetByIndex(i);
                List<Integer> colors = dataSetByIndex.getColors();
                int entryCount = dataSetByIndex.getEntryCount();
                if (dataSetByIndex instanceof IBarDataSet) {
                    IBarDataSet iBarDataSet = (IBarDataSet) dataSetByIndex;
                    if (iBarDataSet.isStacked()) {
                        String[] stackLabels = iBarDataSet.getStackLabels();
                        for (int i2 = 0; i2 < colors.size() && i2 < iBarDataSet.getStackSize(); i2++) {
                            this.computedEntries.add(new LegendEntry(stackLabels[i2 % stackLabels.length], dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i2).intValue()));
                        }
                        if (iBarDataSet.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                        }
                        chartData2 = chartData3;
                        i++;
                        chartData3 = chartData2;
                    }
                }
                if (dataSetByIndex instanceof IPieDataSet) {
                    IPieDataSet iPieDataSet = (IPieDataSet) dataSetByIndex;
                    for (int i3 = 0; i3 < colors.size() && i3 < entryCount; i3++) {
                        this.computedEntries.add(new LegendEntry(iPieDataSet.getEntryForIndex(i3).getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i3).intValue()));
                    }
                    if (iPieDataSet.getLabel() != null) {
                        this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                    }
                } else {
                    if (dataSetByIndex instanceof ICandleDataSet) {
                        ICandleDataSet iCandleDataSet = (ICandleDataSet) dataSetByIndex;
                        if (iCandleDataSet.getDecreasingColor() != 1122867) {
                            int decreasingColor = iCandleDataSet.getDecreasingColor();
                            int increasingColor = iCandleDataSet.getIncreasingColor();
                            this.computedEntries.add(new LegendEntry(null, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), decreasingColor));
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), increasingColor));
                        }
                    }
                    int i4 = 0;
                    while (i4 < colors.size() && i4 < entryCount) {
                        this.computedEntries.add(new LegendEntry((i4 >= colors.size() + (-1) || i4 >= entryCount + (-1)) ? chartData.getDataSetByIndex(i).getLabel() : null, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i4).intValue()));
                        i4++;
                    }
                }
                chartData2 = chartData;
                i++;
                chartData3 = chartData2;
            }
            if (this.mLegend.getExtraEntries() != null) {
                Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
            }
            this.mLegend.setEntries(this.computedEntries);
        }
        Typeface typeface = this.mLegend.getTypeface();
        if (typeface != null) {
            this.mLegendLabelPaint.setTypeface(typeface);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }

    public void renderLegend(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4 = 0;
        float f5;
        float f6;
        List<Boolean> list;
        float f7;
        List<FSize> list2;
        Canvas canvas2;
        int i;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float contentTop;
        float f14;
        float f15;
        Legend.LegendDirection legendDirection;
        LegendEntry legendEntry;
        float f16;
        float contentBottom;
        float contentRight;
        float contentLeft;
        double d;
        if (this.mLegend.isEnabled()) {
            Typeface typeface = this.mLegend.getTypeface();
            if (typeface != null) {
                this.mLegendLabelPaint.setTypeface(typeface);
            }
            this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
            this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
            float lineHeight = Utils.getLineHeight(this.mLegendLabelPaint, this.legendFontMetrics);
            float lineSpacing = Utils.getLineSpacing(this.mLegendLabelPaint, this.legendFontMetrics) + Utils.convertDpToPixel(this.mLegend.getYEntrySpace());
            float calcTextHeight = lineHeight - (Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0f);
            LegendEntry[] entries = this.mLegend.getEntries();
            float convertDpToPixel = Utils.convertDpToPixel(this.mLegend.getFormToTextSpace());
            float convertDpToPixel2 = Utils.convertDpToPixel(this.mLegend.getXEntrySpace());
            Legend.LegendOrientation orientation = this.mLegend.getOrientation();
            Legend.LegendHorizontalAlignment horizontalAlignment = this.mLegend.getHorizontalAlignment();
            Legend.LegendVerticalAlignment verticalAlignment = this.mLegend.getVerticalAlignment();
            Legend.LegendDirection direction = this.mLegend.getDirection();
            float convertDpToPixel3 = Utils.convertDpToPixel(this.mLegend.getFormSize());
            float convertDpToPixel4 = Utils.convertDpToPixel(this.mLegend.getStackSpace());
            float yOffset = this.mLegend.getYOffset();
            float xOffset = this.mLegend.getXOffset();
            int i2 = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[horizontalAlignment.ordinal()];
            float f17 = convertDpToPixel4;
            float f18 = convertDpToPixel2;
            if (i2 != 1) {
                if (i2 == 2) {
                    f = lineHeight;
                    f2 = lineSpacing;
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        contentRight = this.mViewPortHandler.getChartWidth();
                    } else {
                        contentRight = this.mViewPortHandler.contentRight();
                    }
                    f4 = contentRight - xOffset;
                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        f4 -= this.mLegend.mNeededWidth;
                    }
                } else if (i2 != 3) {
                    f = lineHeight;
                    f2 = lineSpacing;
                    f3 = 0.0f;
                } else {
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        contentLeft = this.mViewPortHandler.getChartWidth() / 2.0f;
                    } else {
                        contentLeft = this.mViewPortHandler.contentLeft() + (this.mViewPortHandler.contentWidth() / 2.0f);
                    }
                    f4 = contentLeft + (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? xOffset : -xOffset);
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        f2 = lineSpacing;
                        double d2 = f4;
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            f = lineHeight;
                            d = ((-this.mLegend.mNeededWidth) / 2.0d) + xOffset;
                        } else {
                            f = lineHeight;
                            d = (this.mLegend.mNeededWidth / 2.0d) - xOffset;
                        }
                        f4 = (float) (d2 + d);
                    } else {
                        f = lineHeight;
                        f2 = lineSpacing;
                    }
                }
                f3 = f4;
            } else {
                f = lineHeight;
                f2 = lineSpacing;
                if (orientation != Legend.LegendOrientation.VERTICAL) {
                    xOffset += this.mViewPortHandler.contentLeft();
                }
                if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                    f4 = this.mLegend.mNeededWidth + xOffset;
                    f3 = f4;
                } else {
                    f3 = xOffset;
                }
            }
            int i3 = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[orientation.ordinal()];
            if (i3 != 1) {
                if (i3 != 2) {
                    return;
                }
                int i4 = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[verticalAlignment.ordinal()];
                if (i4 == 1) {
                    contentTop = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? 0.0f : this.mViewPortHandler.contentTop()) + yOffset;
                } else if (i4 == 2) {
                    if (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER) {
                        contentBottom = this.mViewPortHandler.getChartHeight();
                    } else {
                        contentBottom = this.mViewPortHandler.contentBottom();
                    }
                    contentTop = contentBottom - (this.mLegend.mNeededHeight + yOffset);
                } else {
                    contentTop = i4 != 3 ? 0.0f : ((this.mViewPortHandler.getChartHeight() / 2.0f) - (this.mLegend.mNeededHeight / 2.0f)) + this.mLegend.getYOffset();
                }
                float f19 = contentTop;
                boolean z = false;
                int i5 = 0;
                float f20 = 0.0f;
                while (i5 < entries.length) {
                    LegendEntry legendEntry2 = entries[i5];
                    boolean z2 = legendEntry2.form != Legend.LegendForm.NONE;
                    float convertDpToPixel5 = Float.isNaN(legendEntry2.formSize) ? convertDpToPixel3 : Utils.convertDpToPixel(legendEntry2.formSize);
                    if (z2) {
                        f16 = direction == Legend.LegendDirection.LEFT_TO_RIGHT ? f3 + f20 : f3 - (convertDpToPixel5 - f20);
                        f15 = f17;
                        f14 = calcTextHeight;
                        legendDirection = direction;
                        drawForm(canvas, f16, f19 + calcTextHeight, legendEntry2, this.mLegend);
                        if (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            f16 += convertDpToPixel5;
                        }
                        legendEntry = legendEntry2;
                    } else {
                        f14 = calcTextHeight;
                        f15 = f17;
                        legendDirection = direction;
                        legendEntry = legendEntry2;
                        f16 = f3;
                    }
                    if (legendEntry.label != null) {
                        if (z2 && !z) {
                            f16 += legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT ? convertDpToPixel : -convertDpToPixel;
                        } else if (z) {
                            f16 = f3;
                        }
                        if (legendDirection == Legend.LegendDirection.RIGHT_TO_LEFT) {
                            f16 -= Utils.calcTextWidth(this.mLegendLabelPaint, legendEntry.label);
                        }
                        float f21 = f16;
                        if (!z) {
                            drawLabel(canvas, f21, f19 + f, legendEntry.label);
                        } else {
                            f19 += f + f2;
                            drawLabel(canvas, f21, f19 + f, legendEntry.label);
                        }
                        f19 += f + f2;
                        f20 = 0.0f;
                    } else {
                        f20 += convertDpToPixel5 + f15;
                        z = true;
                    }
                    i5++;
                    f17 = f15;
                    direction = legendDirection;
                    calcTextHeight = f14;
                }
                return;
            }
            float f22 = f17;
            List<FSize> calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
            List<FSize> calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
            List<Boolean> calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
            int i6 = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[verticalAlignment.ordinal()];
            if (i6 != 1) {
                if (i6 == 2) {
                    yOffset = (this.mViewPortHandler.getChartHeight() - yOffset) - this.mLegend.mNeededHeight;
                } else {
                    yOffset = i6 != 3 ? 0.0f : yOffset + ((this.mViewPortHandler.getChartHeight() - this.mLegend.mNeededHeight) / 2.0f);
                }
            }
            int length = entries.length;
            float f23 = f3;
            int i7 = 0;
            int i8 = 0;
            while (i7 < length) {
                float f24 = f22;
                LegendEntry legendEntry3 = entries[i7];
                int i9 = length;
                boolean z3 = legendEntry3.form != Legend.LegendForm.NONE;
                float convertDpToPixel6 = Float.isNaN(legendEntry3.formSize) ? convertDpToPixel3 : Utils.convertDpToPixel(legendEntry3.formSize);
                if (i7 >= calculatedLabelBreakPoints.size() || !calculatedLabelBreakPoints.get(i7).booleanValue()) {
                    f5 = f23;
                    f6 = yOffset;
                } else {
                    f6 = yOffset + f + f2;
                    f5 = f3;
                }
                if (f5 == f3 && horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && i8 < calculatedLineSizes.size()) {
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        f13 = calculatedLineSizes.get(i8).width;
                    } else {
                        f13 = -calculatedLineSizes.get(i8).width;
                    }
                    f5 += f13 / 2.0f;
                    i8++;
                }
                int i10 = i8;
                boolean z4 = legendEntry3.label == null;
                if (z3) {
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        f5 -= convertDpToPixel6;
                    }
                    float f25 = f5;
                    f7 = f3;
                    i = i7;
                    list = calculatedLabelBreakPoints;
                    list2 = calculatedLineSizes;
                    canvas2 = canvas;
                    drawForm(canvas, f25, f6 + calcTextHeight, legendEntry3, this.mLegend);
                    f5 = direction == Legend.LegendDirection.LEFT_TO_RIGHT ? f25 + convertDpToPixel6 : f25;
                } else {
                    list = calculatedLabelBreakPoints;
                    f7 = f3;
                    list2 = calculatedLineSizes;
                    canvas2 = canvas;
                    i = i7;
                }
                if (!z4) {
                    if (z3) {
                        f5 += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -convertDpToPixel : convertDpToPixel;
                    }
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        f5 -= calculatedLabelSizes.get(i).width;
                    }
                    drawLabel(canvas2, f5, f6 + f, legendEntry3.label);
                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        f5 += calculatedLabelSizes.get(i).width;
                    }
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        f8 = f18;
                        f12 = -f8;
                    } else {
                        f8 = f18;
                        f12 = f8;
                    }
                    f11 = f5 + f12;
                    f9 = f24;
                } else {
                    f8 = f18;
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        f9 = f24;
                        f10 = -f9;
                    } else {
                        f9 = f24;
                        f10 = f9;
                    }
                    f11 = f5 + f10;
                }
                f18 = f8;
                f22 = f9;
                i7 = i + 1;
                yOffset = f6;
                i8 = i10;
                f3 = f7;
                calculatedLabelBreakPoints = list;
                calculatedLineSizes = list2;
                f23 = f11;
                length = i9;
            }
        }
    }

    protected void drawForm(Canvas canvas, float f, float f2, LegendEntry legendEntry, Legend legend) {
        if (legendEntry.formColor == 1122868 || legendEntry.formColor == 1122867 || legendEntry.formColor == 0) {
            return;
        }
        int save = canvas.save();
        Legend.LegendForm legendForm = legendEntry.form;
        if (legendForm == Legend.LegendForm.DEFAULT) {
            legendForm = legend.getForm();
        }
        this.mLegendFormPaint.setColor(legendEntry.formColor);
        float convertDpToPixel = Utils.convertDpToPixel(Float.isNaN(legendEntry.formSize) ? legend.getFormSize() : legendEntry.formSize);
        float f3 = convertDpToPixel / 2.0f;
        int i = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[legendForm.ordinal()];
        if (i == 3 || i == 4) {
            this.mLegendFormPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(f + f3, f2, f3, this.mLegendFormPaint);
        } else if (i == 5) {
            this.mLegendFormPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(f, f2 - f3, f + convertDpToPixel, f2 + f3, this.mLegendFormPaint);
        } else if (i == 6) {
            float convertDpToPixel2 = Utils.convertDpToPixel(Float.isNaN(legendEntry.formLineWidth) ? legend.getFormLineWidth() : legendEntry.formLineWidth);
            DashPathEffect formLineDashEffect = legendEntry.formLineDashEffect == null ? legend.getFormLineDashEffect() : legendEntry.formLineDashEffect;
            this.mLegendFormPaint.setStyle(Paint.Style.STROKE);
            this.mLegendFormPaint.setStrokeWidth(convertDpToPixel2);
            this.mLegendFormPaint.setPathEffect(formLineDashEffect);
            this.mLineFormPath.reset();
            this.mLineFormPath.moveTo(f, f2);
            this.mLineFormPath.lineTo(f + convertDpToPixel, f2);
            canvas.drawPath(this.mLineFormPath, this.mLegendFormPaint);
        }
        canvas.restoreToCount(save);
    }

    /* renamed from: com.github.mikephil.charting.renderer.LegendRenderer$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm;
        static final /* synthetic */ int[] $SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment;
        static final /* synthetic */ int[] $SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation;
        static final /* synthetic */ int[] $SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment;

        static {
            int[] iArr = new int[Legend.LegendForm.values().length];
            $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm = iArr;
            try {
                iArr[Legend.LegendForm.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[Legend.LegendForm.EMPTY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[Legend.LegendForm.DEFAULT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[Legend.LegendForm.CIRCLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[Legend.LegendForm.SQUARE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendForm[Legend.LegendForm.LINE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr2 = new int[Legend.LegendOrientation.values().length];
            $SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation = iArr2;
            try {
                iArr2[Legend.LegendOrientation.HORIZONTAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendOrientation[Legend.LegendOrientation.VERTICAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            int[] iArr3 = new int[Legend.LegendVerticalAlignment.values().length];
            $SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment = iArr3;
            try {
                iArr3[Legend.LegendVerticalAlignment.TOP.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[Legend.LegendVerticalAlignment.BOTTOM.ordinal()] = 2;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendVerticalAlignment[Legend.LegendVerticalAlignment.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused11) {
            }
            int[] iArr4 = new int[Legend.LegendHorizontalAlignment.values().length];
            $SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment = iArr4;
            try {
                iArr4[Legend.LegendHorizontalAlignment.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[Legend.LegendHorizontalAlignment.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$components$Legend$LegendHorizontalAlignment[Legend.LegendHorizontalAlignment.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused14) {
            }
        }
    }

    protected void drawLabel(Canvas canvas, float f, float f2, String str) {
        canvas.drawText(str, f, f2, this.mLegendLabelPaint);
    }
}
