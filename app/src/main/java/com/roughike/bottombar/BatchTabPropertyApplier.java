package com.roughike.bottombar;

/* loaded from: classes.dex */
class BatchTabPropertyApplier {
    private final BottomBar bottomBar;

    interface TabPropertyUpdater {
        void update(BottomBarTab bottomBarTab);
    }

    BatchTabPropertyApplier(BottomBar bottomBar) {
        this.bottomBar = bottomBar;
    }

    void applyToAllTabs(TabPropertyUpdater tabPropertyUpdater) {
        int tabCount = this.bottomBar.getTabCount();
        if (tabCount > 0) {
            for (int i = 0; i < tabCount; i++) {
                tabPropertyUpdater.update(this.bottomBar.getTabAtPosition(i));
            }
        }
    }
}
