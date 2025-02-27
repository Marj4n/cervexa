package com.serenegiant.glutils;

import com.serenegiant.glutils.AbstractRendererHolder;
import com.serenegiant.glutils.EGLBase;

/* loaded from: classes.dex */
public class RendererHolder extends AbstractRendererHolder {
    private static final String TAG = RendererHolder.class.getSimpleName();

    public RendererHolder(int i, int i2, RenderHolderCallback renderHolderCallback) {
        this(i, i2, 3, null, 2, renderHolderCallback);
    }

    public RendererHolder(int i, int i2, int i3, EGLBase.IContext iContext, int i4, RenderHolderCallback renderHolderCallback) {
        super(i, i2, i3, iContext, i4, renderHolderCallback);
    }

    @Override // com.serenegiant.glutils.AbstractRendererHolder
    protected RendererTask createRendererTask(int i, int i2, int i3, EGLBase.IContext iContext, int i4) {
        return new MyRendererTask(this, i, i2, i3, iContext, i4);
    }

    protected static final class MyRendererTask extends RendererTask {
        public MyRendererTask(RendererHolder rendererHolder, int i, int i2) {
            super(rendererHolder, i, i2);
        }

        public MyRendererTask(AbstractRendererHolder abstractRendererHolder, int i, int i2, int i3, EGLBase.IContext iContext, int i4) {
            super(abstractRendererHolder, i, i2, i3, iContext, i4);
        }
    }
}
