package com.baidu.platform.comapi.map;

import android.os.Message;

/* loaded from: classes.dex */
class z {
    private static final String a = z.class.getSimpleName();
    private y b;

    z() {
    }

    void a(Message message) {
        if (message.what != 65289) {
            return;
        }
        int i = message.arg1;
        if (i != 12 && i != 101 && i != 102) {
            switch (i) {
            }
            return;
        }
        y yVar = this.b;
        if (yVar != null) {
            yVar.a(message.arg1, message.arg2);
        }
    }

    void a(y yVar) {
        this.b = yVar;
    }

    void b(y yVar) {
        this.b = null;
    }
}
