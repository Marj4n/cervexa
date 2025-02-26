package com.jieli.lib.dv.control.base;

import com.jieli.lib.dv.control.player.JlLibLoader;
import com.jieli.lib.dv.control.player.PlayerConstants;

/* loaded from: classes.dex */
public abstract class AbstractClient {
    private static volatile boolean mIsLibLoaded = false;
    public static final JlLibLoader sLocalLibLoader = new JlLibLoader() { // from class: com.jieli.lib.dv.control.base.AbstractClient.1
        @Override // com.jieli.lib.dv.control.player.JlLibLoader
        public void loadLibrary(String str) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(str);
        }
    };

    protected abstract boolean close();

    protected abstract boolean create();

    public static void loadLibrariesOnce(JlLibLoader jlLibLoader) {
        synchronized (AbstractClient.class) {
            if (!mIsLibLoaded) {
                if (jlLibLoader == null) {
                    jlLibLoader = sLocalLibLoader;
                }
                jlLibLoader.loadLibrary(PlayerConstants.JL_PLAYER);
                mIsLibLoaded = true;
            }
        }
    }
}
