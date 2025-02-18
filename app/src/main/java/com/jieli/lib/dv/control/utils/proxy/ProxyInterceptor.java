package com.jieli.lib.dv.control.utils.proxy;

import java.lang.reflect.Method;

/* loaded from: classes.dex */
public interface ProxyInterceptor {
    boolean onIntercept(Object obj, Method method, Object[] objArr);
}
