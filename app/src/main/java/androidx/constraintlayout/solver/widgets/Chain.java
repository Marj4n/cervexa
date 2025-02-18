package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Chain {
    private static final boolean DEBUG = false;
    public static final boolean USE_CHAIN_OPTIMIZATION = false;

    public static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ArrayList<ConstraintWidget> arrayList, int i) {
        ChainHead[] chainHeadArr;
        int i2;
        int i3;
        if (i == 0) {
            i2 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = 0;
        } else {
            int i4 = constraintWidgetContainer.mVerticalChainsSize;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
            i2 = i4;
            i3 = 2;
        }
        for (int i5 = 0; i5 < i2; i5++) {
            ChainHead chainHead = chainHeadArr[i5];
            chainHead.define();
            if (arrayList == null || (arrayList != null && arrayList.contains(chainHead.mFirst))) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i3, chainHead);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0035, code lost:
    
        if (r2.mHorizontalChainStyle == 2) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004c, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x004a, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0048, code lost:
    
        if (r2.mVerticalChainStyle == 2) goto L29;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x052d  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0541  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x053e  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0529  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x03c9  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x03ca A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x03e7  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x04bd  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x04f2  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01e9  */
    /* JADX WARN: Type inference failed for: r2v63, types: [androidx.constraintlayout.solver.widgets.ConstraintWidget] */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v2, types: [androidx.constraintlayout.solver.widgets.ConstraintWidget] */
    /* JADX WARN: Type inference failed for: r7v33 */
    /* JADX WARN: Type inference failed for: r7v34 */
    /* JADX WARN: Type inference failed for: r7v35 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static void applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer r36, androidx.constraintlayout.solver.LinearSystem r37, int r38, int r39, androidx.constraintlayout.solver.widgets.ChainHead r40) {
        /*
            Method dump skipped, instructions count: 1391
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Chain.applyChainConstraints(androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer, androidx.constraintlayout.solver.LinearSystem, int, int, androidx.constraintlayout.solver.widgets.ChainHead):void");
    }
}
