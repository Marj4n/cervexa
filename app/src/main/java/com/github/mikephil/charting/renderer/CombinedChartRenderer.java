package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class CombinedChartRenderer extends DataRenderer {
    protected WeakReference<Chart> mChart;
    protected List<Highlight> mHighlightBuffer;
    protected List<DataRenderer> mRenderers;

    public CombinedChartRenderer(CombinedChart combinedChart, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mRenderers = new ArrayList(5);
        this.mHighlightBuffer = new ArrayList();
        this.mChart = new WeakReference<>(combinedChart);
        createRenderers();
    }

    public void createRenderers() {
        this.mRenderers.clear();
        CombinedChart combinedChart = (CombinedChart) this.mChart.get();
        if (combinedChart == null) {
            return;
        }
        for (CombinedChart.DrawOrder drawOrder : combinedChart.getDrawOrder()) {
            int i = AnonymousClass1.$SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[drawOrder.ordinal()];
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i == 4) {
                            if (combinedChart.getCandleData() != null) {
                                this.mRenderers.add(new CandleStickChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                            }
                        } else if (i == 5 && combinedChart.getScatterData() != null) {
                            this.mRenderers.add(new ScatterChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                        }
                    } else if (combinedChart.getLineData() != null) {
                        this.mRenderers.add(new LineChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                    }
                } else if (combinedChart.getBubbleData() != null) {
                    this.mRenderers.add(new BubbleChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
                }
            } else if (combinedChart.getBarData() != null) {
                this.mRenderers.add(new BarChartRenderer(combinedChart, this.mAnimator, this.mViewPortHandler));
            }
        }
    }

    /* renamed from: com.github.mikephil.charting.renderer.CombinedChartRenderer$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder;

        static {
            int[] iArr = new int[CombinedChart.DrawOrder.values().length];
            $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder = iArr;
            try {
                iArr[CombinedChart.DrawOrder.BAR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[CombinedChart.DrawOrder.BUBBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[CombinedChart.DrawOrder.LINE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[CombinedChart.DrawOrder.CANDLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$github$mikephil$charting$charts$CombinedChart$DrawOrder[CombinedChart.DrawOrder.SCATTER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void initBuffers() {
        Iterator<DataRenderer> it = this.mRenderers.iterator();
        while (it.hasNext()) {
            it.next().initBuffers();
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawData(Canvas canvas) {
        Iterator<DataRenderer> it = this.mRenderers.iterator();
        while (it.hasNext()) {
            it.next().drawData(canvas);
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawValues(Canvas canvas) {
        Iterator<DataRenderer> it = this.mRenderers.iterator();
        while (it.hasNext()) {
            it.next().drawValues(canvas);
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawExtras(Canvas canvas) {
        Iterator<DataRenderer> it = this.mRenderers.iterator();
        while (it.hasNext()) {
            it.next().drawExtras(canvas);
        }
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        Chart chart = this.mChart.get();
        if (chart == null) {
            return;
        }
        for (DataRenderer dataRenderer : this.mRenderers) {
            Object obj = null;
            if (dataRenderer instanceof BarChartRenderer) {
                obj = ((BarChartRenderer) dataRenderer).mChart.getBarData();
            } else if (dataRenderer instanceof LineChartRenderer) {
                obj = ((LineChartRenderer) dataRenderer).mChart.getLineData();
            } else if (dataRenderer instanceof CandleStickChartRenderer) {
                obj = ((CandleStickChartRenderer) dataRenderer).mChart.getCandleData();
            } else if (dataRenderer instanceof ScatterChartRenderer) {
                obj = ((ScatterChartRenderer) dataRenderer).mChart.getScatterData();
            } else if (dataRenderer instanceof BubbleChartRenderer) {
                obj = ((BubbleChartRenderer) dataRenderer).mChart.getBubbleData();
            }
            int indexOf = obj == null ? -1 : ((CombinedData) chart.getData()).getAllData().indexOf(obj);
            this.mHighlightBuffer.clear();
            for (Highlight highlight : highlightArr) {
                if (highlight.getDataIndex() == indexOf || highlight.getDataIndex() == -1) {
                    this.mHighlightBuffer.add(highlight);
                }
            }
            List<Highlight> list = this.mHighlightBuffer;
            dataRenderer.drawHighlighted(canvas, (Highlight[]) list.toArray(new Highlight[list.size()]));
        }
    }

    public DataRenderer getSubRenderer(int i) {
        if (i >= this.mRenderers.size() || i < 0) {
            return null;
        }
        return this.mRenderers.get(i);
    }

    public List<DataRenderer> getSubRenderers() {
        return this.mRenderers;
    }

    public void setSubRenderers(List<DataRenderer> list) {
        this.mRenderers = list;
    }
}
