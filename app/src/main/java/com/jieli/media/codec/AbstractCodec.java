package com.jieli.media.codec;

import tv.danmaku.ijk.media.player.IjkLibLoader;

/* loaded from: classes.dex */
abstract class AbstractCodec {
    static final IjkLibLoader sLocalLibLoader = new IjkLibLoader() { // from class: com.jieli.media.codec.AbstractCodec.1
        @Override // tv.danmaku.ijk.media.player.IjkLibLoader
        public void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(str);
        }
    };
    private static volatile boolean mIsLibLoaded = false;

    AbstractCodec() {
    }

    static void loadLibrariesOnce(IjkLibLoader ijkLibLoader) {
        synchronized (AbstractCodec.class) {
            if (!mIsLibLoaded) {
                if (ijkLibLoader == null) {
                    ijkLibLoader = sLocalLibLoader;
                }
                ijkLibLoader.loadLibrary("ijkffmpeg");
                ijkLibLoader.loadLibrary("jl_frm_codec");
                mIsLibLoaded = true;
            }
        }
    }
}
