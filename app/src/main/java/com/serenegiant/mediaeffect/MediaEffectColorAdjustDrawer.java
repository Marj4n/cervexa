package com.serenegiant.mediaeffect;

import android.opengl.GLES20;
import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.mediaeffect.MediaEffectDrawer;

/* loaded from: classes2.dex */
public class MediaEffectColorAdjustDrawer extends MediaEffectDrawer.MediaEffectSingleDrawer {
    private float mColorAdjust;
    private int muColorAdjustLoc;

    public MediaEffectColorAdjustDrawer(String str) {
        this(false, ShaderConst.VERTEX_SHADER, str);
    }

    public MediaEffectColorAdjustDrawer(boolean z, String str) {
        this(z, ShaderConst.VERTEX_SHADER, str);
    }

    public MediaEffectColorAdjustDrawer(boolean z, String str, String str2) {
        super(z, str, str2);
        int glGetUniformLocation = GLES20.glGetUniformLocation(getProgram(), "uColorAdjust");
        this.muColorAdjustLoc = glGetUniformLocation;
        if (glGetUniformLocation < 0) {
            this.muColorAdjustLoc = -1;
        }
    }

    public void setColorAdjust(float f) {
        synchronized (this.mSync) {
            this.mColorAdjust = f;
        }
    }

    @Override // com.serenegiant.mediaeffect.MediaEffectDrawer
    protected void preDraw(int[] iArr, float[] fArr, int i) {
        super.preDraw(iArr, fArr, i);
        if (this.muColorAdjustLoc >= 0) {
            synchronized (this.mSync) {
                GLES20.glUniform1f(this.muColorAdjustLoc, this.mColorAdjust);
            }
        }
    }
}
