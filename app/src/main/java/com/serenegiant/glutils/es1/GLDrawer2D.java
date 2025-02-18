package com.serenegiant.glutils.es1;

import android.opengl.GLES10;
import android.opengl.Matrix;
import com.serenegiant.glutils.IDrawer2D;
import com.serenegiant.glutils.ITexture;
import com.serenegiant.glutils.ShaderConst;
import com.serenegiant.glutils.TextureOffscreen;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes.dex */
public class GLDrawer2D implements IDrawer2D {
    private static final int FLOAT_SZ = 4;
    private static final int VERTEX_NUM = 4;
    private static final int VERTEX_SZ = 8;
    private final float[] mMvpMatrix = new float[16];
    private final int mTexTarget;
    private final FloatBuffer pTexCoord;
    private final FloatBuffer pVertex;
    private static final float[] VERTICES = {1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f};
    private static final float[] TEXCOORD = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};

    @Override // com.serenegiant.glutils.IDrawer2D
    public void release() {
    }

    public GLDrawer2D(boolean z) {
        this.mTexTarget = z ? ShaderConst.GL_TEXTURE_EXTERNAL_OES : ShaderConst.GL_TEXTURE_2D;
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pVertex = asFloatBuffer;
        asFloatBuffer.put(VERTICES);
        this.pVertex.flip();
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.pTexCoord = asFloatBuffer2;
        asFloatBuffer2.put(TEXCOORD);
        this.pTexCoord.flip();
        Matrix.setIdentityM(this.mMvpMatrix, 0);
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public float[] getMvpMatrix() {
        return this.mMvpMatrix;
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public IDrawer2D setMvpMatrix(float[] fArr, int i) {
        System.arraycopy(fArr, i, this.mMvpMatrix, 0, 16);
        return this;
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public void getMvpMatrix(float[] fArr, int i) {
        System.arraycopy(this.mMvpMatrix, 0, fArr, i, 16);
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public void draw(int i, float[] fArr, int i2) {
        GLES10.glEnableClientState(32884);
        this.pVertex.position(0);
        GLES10.glVertexPointer(2, 5126, 8, this.pVertex);
        GLES10.glEnableClientState(32888);
        this.pTexCoord.position(0);
        GLES10.glTexCoordPointer(4, 5126, 8, this.pTexCoord);
        GLES10.glActiveTexture(33984);
        GLES10.glBindTexture(this.mTexTarget, i);
        GLES10.glDrawArrays(5, 0, 4);
        GLES10.glBindTexture(this.mTexTarget, 0);
        GLES10.glDisableClientState(32888);
        GLES10.glDisableClientState(32884);
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public void draw(ITexture iTexture) {
        draw(iTexture.getTexture(), iTexture.getTexMatrix(), 0);
    }

    @Override // com.serenegiant.glutils.IDrawer2D
    public void draw(TextureOffscreen textureOffscreen) {
        draw(textureOffscreen.getTexture(), textureOffscreen.getTexMatrix(), 0);
    }
}
