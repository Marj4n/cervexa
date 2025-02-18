package com.jieli.stream.dv.running2.util;

import com.jieli.stream.dv.running2.ui.activity.MainActivity;
import com.jieli.stream.dv.running2.ui.base.BaseActivity;
import java.util.Iterator;
import java.util.Stack;

/* loaded from: classes.dex */
public final class ActivityManager {
    private static ActivityManager instance;
    private Stack<BaseActivity> activityStack;

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    public void pushActivity(BaseActivity baseActivity) {
        if (this.activityStack == null) {
            this.activityStack = new Stack<>();
        }
        this.activityStack.add(baseActivity);
    }

    public void popActivity(BaseActivity baseActivity) {
        Stack<BaseActivity> stack = this.activityStack;
        if (stack != null) {
            stack.remove(baseActivity);
        }
    }

    public BaseActivity getTopActivity() {
        Stack<BaseActivity> stack = this.activityStack;
        if (stack == null) {
            return null;
        }
        return stack.lastElement();
    }

    public void popAllActivity() {
        Stack<BaseActivity> stack = this.activityStack;
        if (stack == null) {
            return;
        }
        Iterator<BaseActivity> it = stack.iterator();
        while (it.hasNext()) {
            BaseActivity next = it.next();
            if (next != null) {
                next.finish();
            }
        }
        this.activityStack.clear();
    }

    public void popActivityOnlyMain() {
        Stack<BaseActivity> stack = this.activityStack;
        if (stack == null) {
            return;
        }
        Iterator<BaseActivity> it = stack.iterator();
        while (it.hasNext()) {
            BaseActivity next = it.next();
            if (next != null && !(next instanceof MainActivity)) {
                next.finish();
            }
        }
    }
}
