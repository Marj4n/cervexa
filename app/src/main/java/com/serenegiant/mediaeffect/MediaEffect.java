package com.serenegiant.mediaeffect;

import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.text.TextUtils;
import com.serenegiant.glutils.TextureOffscreen;

/* loaded from: classes2.dex */
public class MediaEffect implements IEffect {
    protected Effect mEffect;
    protected final EffectContext mEffectContext;
    protected boolean mEnabled = true;

    @Override // com.serenegiant.mediaeffect.IEffect
    public MediaEffect resize(int i, int i2) {
        return this;
    }

    public MediaEffect(EffectContext effectContext, String str) {
        this.mEffectContext = effectContext;
        EffectFactory factory = effectContext.getFactory();
        if (TextUtils.isEmpty(str)) {
            this.mEffect = null;
        } else {
            this.mEffect = factory.createEffect(str);
        }
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, int i, int i2, int i3) {
        Effect effect;
        if (!this.mEnabled || (effect = this.mEffect) == null) {
            return;
        }
        effect.apply(iArr[0], i, i2, i3);
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(int[] iArr, TextureOffscreen textureOffscreen) {
        Effect effect;
        if (!this.mEnabled || (effect = this.mEffect) == null) {
            return;
        }
        effect.apply(iArr[0], textureOffscreen.getWidth(), textureOffscreen.getHeight(), textureOffscreen.getTexture());
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void apply(ISource iSource) {
        Effect effect;
        if (!this.mEnabled || (effect = this.mEffect) == null) {
            return;
        }
        effect.apply(iSource.getSourceTexId()[0], iSource.getWidth(), iSource.getHeight(), iSource.getOutputTexId());
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public void release() {
        Effect effect = this.mEffect;
        if (effect != null) {
            effect.release();
            this.mEffect = null;
        }
    }

    protected MediaEffect setParameter(String str, Object obj) {
        Effect effect = this.mEffect;
        if (effect != null && obj != null) {
            effect.setParameter(str, obj);
        }
        return this;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public boolean enabled() {
        return this.mEnabled;
    }

    @Override // com.serenegiant.mediaeffect.IEffect
    public IEffect setEnable(boolean z) {
        this.mEnabled = z;
        return this;
    }
}
