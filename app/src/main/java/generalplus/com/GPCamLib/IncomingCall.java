package generalplus.com.GPCamLib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/* loaded from: classes2.dex */
public class IncomingCall extends BroadcastReceiver {
    private String TAG = "IncomingCall";
    private Context m_Context;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.m_Context = context;
        try {
            ((TelephonyManager) context.getSystemService("phone")).listen(new MyPhoneStateListener(), 32);
        } catch (Exception e) {
            Log.e(this.TAG, " " + e);
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        private MyPhoneStateListener() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            Log.d(IncomingCall.this.TAG, i + " incoming no:" + str);
        }
    }
}
