package com.shizhefei.view.indicator;

import android.view.View;
import android.view.ViewGroup;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public interface Indicator {

    public interface DataSetObserver {
        void onChange();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int i, int i2);
    }

    public interface OnTransitionListener {
        void onTransition(View view, int i, float f);
    }

    int getCurrentItem();

    IndicatorAdapter getIndicatorAdapter();

    View getItemView(int i);

    OnItemSelectedListener getOnItemSelectListener();

    OnTransitionListener getOnTransitionListener();

    int getPreSelectItem();

    boolean isItemClickable();

    void onPageScrollStateChanged(int i);

    void onPageScrolled(int i, float f, int i2);

    void setAdapter(IndicatorAdapter indicatorAdapter);

    void setCurrentItem(int i);

    void setCurrentItem(int i, boolean z);

    void setItemClickable(boolean z);

    void setOnItemSelectListener(OnItemSelectedListener onItemSelectedListener);

    void setOnTransitionListener(OnTransitionListener onTransitionListener);

    void setScrollBar(ScrollBar scrollBar);

    public static abstract class IndicatorAdapter {
        private boolean isLoop;
        private Set<DataSetObserver> observers = new LinkedHashSet(2);

        public abstract int getCount();

        public abstract View getView(int i, View view, ViewGroup viewGroup);

        boolean isLoop() {
            return this.isLoop;
        }

        void setIsLoop(boolean z) {
            this.isLoop = z;
        }

        public void notifyDataSetChanged() {
            Iterator<DataSetObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onChange();
            }
        }

        public void registDataSetObserver(DataSetObserver dataSetObserver) {
            this.observers.add(dataSetObserver);
        }

        public void unRegistDataSetObserver(DataSetObserver dataSetObserver) {
            this.observers.remove(dataSetObserver);
        }
    }
}
