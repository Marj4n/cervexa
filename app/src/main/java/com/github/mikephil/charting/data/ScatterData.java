package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet> {
    public ScatterData() {
    }

    public ScatterData(List<IScatterDataSet> list) {
        super(list);
    }

    public ScatterData(IScatterDataSet... iScatterDataSetArr) {
        super(iScatterDataSetArr);
    }

    public float getGreatestShapeSize() {
        Iterator it = this.mDataSets.iterator();
        float f = 0.0f;
        while (it.hasNext()) {
            float scatterShapeSize = ((IScatterDataSet) it.next()).getScatterShapeSize();
            if (scatterShapeSize > f) {
                f = scatterShapeSize;
            }
        }
        return f;
    }
}
