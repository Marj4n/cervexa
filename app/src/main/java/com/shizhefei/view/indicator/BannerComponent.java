package com.shizhefei.view.indicator;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import androidx.viewpager.widget.ViewPager;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.DurationScroller;
import com.shizhefei.view.viewpager.SViewPager;
import java.lang.reflect.Field;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

/* loaded from: classes2.dex */
public class BannerComponent extends IndicatorViewPager {
    private final Handler handler;
    private boolean isRunning;
    private IndicatorViewPager.LoopAdapter mAdapter;
    private View.OnTouchListener onTouchListener;
    private DurationScroller scroller;
    private long time;

    public BannerComponent(Indicator indicator, ViewPager viewPager, boolean z) {
        super(indicator, viewPager, z);
        this.time = 3000L;
        this.onTouchListener = new View.OnTouchListener() { // from class: com.shizhefei.view.indicator.BannerComponent.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action != 0) {
                    if ((action != 1 && action != 3) || !BannerComponent.this.isRunning) {
                        return false;
                    }
                    BannerComponent.this.handler.removeCallbacksAndMessages(null);
                    BannerComponent.this.handler.sendEmptyMessageDelayed(1, BannerComponent.this.time);
                    return false;
                }
                BannerComponent.this.handler.removeCallbacksAndMessages(null);
                return false;
            }
        };
        this.handler = new AutoPlayHandler(Looper.getMainLooper());
        viewPager.setOnTouchListener(this.onTouchListener);
        initViewPagerScroll();
    }

    private void initViewPagerScroll() {
        try {
            Field declaredField = ViewPager.class.getDeclaredField("mScroller");
            declaredField.setAccessible(true);
            this.scroller = new DurationScroller(this.viewPager.getContext());
            declaredField.set(this.viewPager, this.scroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        }
    }

    public void setScrollDuration(int i) {
        DurationScroller durationScroller = this.scroller;
        if (durationScroller != null) {
            durationScroller.setScrollDuration(i);
        }
    }

    @Override // com.shizhefei.view.indicator.IndicatorViewPager
    protected void iniOnItemSelectedListener() {
        this.indicatorView.setOnItemSelectListener(new Indicator.OnItemSelectedListener() { // from class: com.shizhefei.view.indicator.BannerComponent.1
            @Override // com.shizhefei.view.indicator.Indicator.OnItemSelectedListener
            public void onItemSelected(View view, int i, int i2) {
                if (BannerComponent.this.viewPager instanceof SViewPager) {
                    BannerComponent bannerComponent = BannerComponent.this;
                    bannerComponent.setCurrentItem(i, ((SViewPager) bannerComponent.viewPager).isCanScroll());
                } else {
                    BannerComponent.this.setCurrentItem(i, true);
                }
            }
        });
    }

    @Override // com.shizhefei.view.indicator.IndicatorViewPager
    protected void iniOnPageChangeListener() {
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.shizhefei.view.indicator.BannerComponent.2
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                BannerComponent.this.indicatorView.setCurrentItem(BannerComponent.this.mAdapter.getRealPosition(i), true);
                if (BannerComponent.this.onIndicatorPageChangeListener != null) {
                    BannerComponent.this.onIndicatorPageChangeListener.onIndicatorPageChange(BannerComponent.this.indicatorView.getPreSelectItem(), BannerComponent.this.mAdapter.getRealPosition(i));
                }
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                BannerComponent.this.indicatorView.onPageScrolled(BannerComponent.this.mAdapter.getRealPosition(i), f, i2);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                BannerComponent.this.indicatorView.onPageScrollStateChanged(i);
            }
        });
    }

    @Override // com.shizhefei.view.indicator.IndicatorViewPager
    public void setCurrentItem(int i, boolean z) {
        int i2;
        int count = this.mAdapter.getCount();
        if (count > 0) {
            int currentItem = this.viewPager.getCurrentItem();
            int realPosition = this.mAdapter.getRealPosition(currentItem);
            if (i > realPosition) {
                i2 = (i - realPosition) % count;
            } else {
                i2 = -((realPosition - i) % count);
            }
            if (Math.abs(i2) > this.viewPager.getOffscreenPageLimit() && this.viewPager.getOffscreenPageLimit() != count) {
                this.viewPager.setOffscreenPageLimit(count);
            }
            this.viewPager.setCurrentItem(currentItem + i2, z);
            this.indicatorView.setCurrentItem(i, z);
        }
    }

    @Override // com.shizhefei.view.indicator.IndicatorViewPager
    public void setAdapter(IndicatorViewPager.IndicatorPagerAdapter indicatorPagerAdapter) {
        if (!(indicatorPagerAdapter instanceof IndicatorViewPager.LoopAdapter)) {
            throw new RuntimeException("请设置继承于IndicatorViewPagerAdapter或者IndicatorViewPagerAdapter的adapter");
        }
        IndicatorViewPager.LoopAdapter loopAdapter = (IndicatorViewPager.LoopAdapter) indicatorPagerAdapter;
        this.mAdapter = loopAdapter;
        loopAdapter.setLoop(true);
        super.setAdapter(indicatorPagerAdapter);
        int count = this.mAdapter.getCount();
        int i = LockFreeTaskQueueCore.MAX_CAPACITY_MASK;
        if (count > 0) {
            i = LockFreeTaskQueueCore.MAX_CAPACITY_MASK - (LockFreeTaskQueueCore.MAX_CAPACITY_MASK % count);
        }
        this.viewPager.setCurrentItem(i, false);
    }

    public void setAutoPlayTime(long j) {
        this.time = j;
    }

    public void startAutoPlay() {
        this.isRunning = true;
        this.handler.removeCallbacksAndMessages(null);
        this.handler.sendEmptyMessageDelayed(1, this.time);
    }

    public void stopAutoPlay() {
        this.isRunning = false;
        this.handler.removeCallbacksAndMessages(null);
    }

    private class AutoPlayHandler extends Handler {
        public AutoPlayHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            BannerComponent.this.viewPager.setCurrentItem(BannerComponent.this.viewPager.getCurrentItem() + 1, true);
            if (BannerComponent.this.isRunning) {
                BannerComponent.this.handler.sendEmptyMessageDelayed(1, BannerComponent.this.time);
            }
        }
    }
}
