package com.generalplus.GoPlusDrone.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.generalplus.GoPlusDrone.R;
import com.generalplus.ffmpegLib.ZoomableSurfaceView;
import com.generalplus.ffmpegLib.ffmpegWrapper;
import com.jiangdg.usbcamera.UVCCameraHelper;
import generalplus.com.GPCamLib.CamWrapper;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;

/* loaded from: classes.dex */
public class CameraActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
    private static final int CAMERA_AMP = 8;
    private static final int CAMERA_AUTO = 5;
    private static final int CAMERA_BACK = 3;
    private static final int CAMERA_FILE = 10;
    private static final int CAMERA_NARROW = 7;
    private static final int CAMERA_PHOTO = 1;
    private static final int CAMERA_SUB = 4;
    private static final int CAMERA_SUM = 6;
    private static final int CAMERA_VEDIO = 2;
    private static final int Msg_btnTip = 9;
    private static final String TAG = "ControlFragment";
    private static boolean bCheckConnectStatus = false;
    public static ImageButton btnTakeRecorder = null;
    public static ImageView imageView = null;
    static ImageView imgTip = null;
    private static boolean isDevicePass = true;
    public static boolean isHave = false;
    static boolean isLeft = true;
    static boolean isOff = true;
    static boolean isR = false;
    public static boolean isT = false;
    public static boolean isT2 = false;
    static boolean isTime = false;
    static LinearLayout layout;
    static LinearLayout linearLayoutR;
    static int numLeft;
    static int numRight;
    static RelativeLayout relative;
    static RelativeLayout relativeLayout;
    static RelativeLayout relativeLayoutR;
    static MHorizontal scrollView;
    public static int text;
    private ImageButton ampButton;
    private ImageButton autoButton;
    private ImageButton backButton;
    private ImageButton btnLeftMenu;
    private ImageButton btnSnapshot;
    private ImageButton btnSwitch;
    private ImageButton btnTip;
    private int buttonW;
    private Timer exitTimer;
    private ImageButton fileButton;
    RelativeLayout linearLayout;
    private GestureDetector mDetector;
    private int mScreenHigth;
    private int mScreenWidth;
    private ZoomableSurfaceView mSurfaceView;
    private CamWrapper m_CamWrapper;
    private ImageButton modeButton;
    private ImageButton narrowButton;
    private SharedPreferences sharedPreferences;
    private ImageView shizi2ImageView;
    protected Long startTime;
    private String strSaveDirectory;
    private ImageButton subButton;
    private ImageButton sumButton;
    private TextView textMenu12;
    private TextView textMenu13;
    private TextView textMenu14;
    byte[] data = null;
    protected ImageButton mBtnTest = null;
    private ImageButton m_bnHide = null;
    private RelativeLayout m_rlControlLayout = null;
    protected TextView m_tvRecordTime = null;
    private ImageView m_ivBroken = null;
    private boolean mSaveVideo = false;
    private boolean mIsStart = false;
    private int m_iControlLayout = 0;
    private boolean m_bShowControlLayout = false;
    private boolean m_bPause = false;
    private TextView m_tvSpeed = null;
    private Timer timer = new Timer(true);
    private Thread mTcpStart = null;
    private Thread mTcpRecv = null;
    private Socket mSocket = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    RelativeLayout rlRightMenu = null;
    private long mStartTime = 0;
    float x1 = 0.0f;
    float x2 = 0.0f;
    float y1 = 0.0f;
    float y2 = 0.0f;
    private final boolean isDebug = false;
    private boolean isTcpStart = false;
    int m_iRetry = 0;
    private Handler m_StatusHandler = new Handler() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.8
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0 && message.what == 1) {
                CameraActivity.this.mIsStart = false;
            }
            Log.e(CameraActivity.TAG, "m_StatusHandler" + message.what);
        }
    };
    private boolean isDelayStopVideo = false;
    int SendRet = 0;
    protected Runnable updateTimer = new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.11
        @SuppressLint("SetTextI18n")
        @Override // java.lang.Runnable
        public void run() {
            Long valueOf = Long.valueOf(System.currentTimeMillis() - CameraActivity.this.startTime.longValue());
            Long valueOf2 = Long.valueOf(((valueOf.longValue() / 1000) / 60) / 60);
            Long valueOf3 = Long.valueOf(((valueOf.longValue() / 1000) / 60) % 60);
            Long valueOf4 = Long.valueOf((valueOf.longValue() / 1000) % 60);
            CameraActivity.this.m_tvRecordTime.setText(
                    String.format("%02d", valueOf2) + ":" +
                            String.format("%02d", valueOf3) + ":" +
                            String.format("%02d", valueOf4)
            );
            CameraActivity.this.handler.postDelayed(this, 1000L);
        }
    };
    private int rig = 0;
    public boolean isThtead2 = true;
    private int mSaveType = 3;
    boolean isS = true;
    int a = 0;
    private int mWidth = -1;
    private int mHeight = -1;
    private float mScale = 1.0f;
    boolean isImgTipShow = false;
    Handler handler = new Handler() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.15
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100) {
                CameraActivity.this.btnLeftMenu.setVisibility(4);
            }
            if (message.what == -1 && CameraActivity.isR) {
                CameraActivity.isR = false;
                new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.15.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            System.out.println("CameraActivity onPause() stop isHave=" + CameraActivity.isHave);
                        } catch (Exception unused) {
                            System.out.println("CameraActivity onPause() 异常断开连接");
                        }
                    }
                }).start();
                CameraActivity cameraActivity = CameraActivity.this;
                Toast.makeText(cameraActivity, cameraActivity.getResources().getString(R.string.language22), 0).show();
                CameraActivity.btnTakeRecorder.setBackgroundResource(R.drawable.vedio_button);
                CameraActivity.imageView.setVisibility(4);
            }
        }
    };
    TimeThread timeThread = null;

    private void smode() {
    }

    private class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private ExceptionHandler() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            Log.e(CameraActivity.TAG, "uncaught_exception_handler: uncaught exception in thread " + thread.getName(), th);
            if (th instanceof RuntimeException) {
                throw ((RuntimeException) th);
            }
            if (th instanceof Error) {
                throw ((Error) th);
            }
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            String obj = stringWriter.toString();
            printWriter.close();
            Log.e(CameraActivity.TAG, "uncaught_exception handler: unable to rethrow checked exception\ntrace: " + obj);
        }
    }

    private void checkStopRecode() {
        if (this.mSaveVideo) {
            stopRecode(true);
            this.mSaveVideo = true ^ this.mSaveVideo;
        }
    }

    public static long getTick() {
        return SystemClock.uptimeMillis();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.camera_activity);
        this.mStartTime = getTick();
        getWindow().addFlags(128);
        this.btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        this.btnLeftMenu = (ImageButton) findViewById(R.id.btnLeftMenu);
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        this.rlRightMenu = (RelativeLayout) findViewById(R.id.rlRightMenu);
        this.textMenu12 = (TextView) findViewById(R.id.textMenu12);
        this.textMenu13 = (TextView) findViewById(R.id.textMenu13);
        this.textMenu14 = (TextView) findViewById(R.id.textMenu14);
        text(12);
        this.textMenu12.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraActivity.this.mSaveType = 3;
                CameraActivity.this.text(12);
                CameraActivity.this.textSave(12);
                CameraActivity.this.littleShow();
                CameraActivity.this.rlRightMenu.setVisibility(8);
            }
        });
        this.textMenu13.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraActivity.this.mSaveType = 2;
                CameraActivity.this.text(13);
                CameraActivity.this.textSave(13);
                CameraActivity.this.littleShow();
                CameraActivity.this.rlRightMenu.setVisibility(8);
            }
        });
        this.textMenu14.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraActivity.this.mSaveType = 1;
                CameraActivity.this.text(14);
                CameraActivity.this.textSave(14);
                CameraActivity.this.littleShow();
                CameraActivity.this.rlRightMenu.setVisibility(8);
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.imgTip);
        imgTip = imageView2;
        imageView2.setBackgroundResource(0);
        this.isImgTipShow = false;
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        this.mScreenHigth = getResources().getDisplayMetrics().heightPixels;
        relative = (RelativeLayout) findViewById(R.id.container);
        isT = false;
        isT2 = false;
        initView();
        this.btnLeftMenu.setVisibility(4);
        this.btnSwitch.setOnClickListener(this);
        this.btnLeftMenu.setOnClickListener(this);
        relativeLayout.setOnTouchListener(this);
        this.backButton.setOnClickListener(this);
        this.subButton.setOnClickListener(this);
        this.autoButton.setOnClickListener(this);
        this.sumButton.setOnClickListener(this);
        this.narrowButton.setOnClickListener(this);
        this.ampButton.setOnClickListener(this);
        this.btnTip.setOnClickListener(this);
        this.btnSnapshot.setOnClickListener(this);
        btnTakeRecorder.setOnClickListener(this);
        this.fileButton.setOnClickListener(this);
        if (this.data == null) {
            this.data = new byte[3686400];
        }
        initOff();
        initGL2JNIVIEW();
        if (isR) {
            imageView.setVisibility(0);
            btnTakeRecorder.setBackgroundResource(R.drawable.videobutton_3);
        }
        isHave = false;
        exit();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        if (this.m_CamWrapper == null) {
            this.m_CamWrapper = new CamWrapper();
        }
        this.m_tvRecordTime = (TextView) findViewById(R.id.tvRecordTime);
        ZoomableSurfaceView zoomableSurfaceView = (ZoomableSurfaceView) findViewById(R.id.surfaceView);
        this.mSurfaceView = zoomableSurfaceView;
        zoomableSurfaceView.setEGLContextClientVersion(2);
        this.mSurfaceView.setRenderer(ffmpegWrapper.getInstance());
        this.mSurfaceView.setKeepScreenOn(true);
        this.mSurfaceView.setOnTouchListener(new View.OnTouchListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        this.strSaveDirectory = Environment.getExternalStorageDirectory().getPath() + "/GoPlus_Drone";
        new File(this.strSaveDirectory).mkdir();
        new File(this.strSaveDirectory + "/Photo/").mkdir();
        new File(this.strSaveDirectory + "/Video/").mkdir();
        new File(this.strSaveDirectory + "/Video/thumbnails/").mkdir();
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetDebugMessage(true);
        ffmpegWrapper.getInstance().SetViewHandler(this.m_StatusHandler);
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetDecodeOptions("flags=low_delay");
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetScaleMode(0);
        ImageView imageView3 = (ImageView) findViewById(R.id.ivBroken);
        this.m_ivBroken = imageView3;
        imageView3.setVisibility(4);
        TextView textView = (TextView) findViewById(R.id.tvSpeed);
        this.m_tvSpeed = textView;
        textView.setVisibility(8);
        setPlayStatus(false);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.x1 = motionEvent.getX();
            this.y1 = motionEvent.getY();
        }
        if (motionEvent.getAction() == 1) {
            this.x2 = motionEvent.getX();
            float y = motionEvent.getY();
            this.y2 = y;
            float f = this.y1 - y;
            if (f < 0.0f) {
                f = -f;
            }
            float f2 = this.x1 - this.x2;
            if (f2 < 0.0f) {
                f2 = -f2;
            }
            if ((f <= f2 || this.y1 - this.y2 <= 50.0f) && (f <= f2 || this.y2 - this.y1 <= 50.0f)) {
                float f3 = this.x1;
                float f4 = this.x2;
                if (f3 - f4 > 50.0f) {
                    this.rlRightMenu.setVisibility(0);
                } else if (f4 - f3 > 50.0f) {
                    this.rlRightMenu.setVisibility(8);
                } else if (this.rlRightMenu.getVisibility() != 8 && motionEvent.getRawX() < this.rlRightMenu.getLeft()) {
                    this.rlRightMenu.setVisibility(8);
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void startTcp(String str) {
        if (!this.isTcpStart) {
            Log.e(TAG, "StartTcp-TcpStoped(" + str + ")");
            return;
        }
        Log.e(TAG, "StartTcp-SocketThread(" + str + ")");
        Thread thread = this.mTcpStart;
        if (thread != null) {
            if (thread.isInterrupted()) {
                this.mTcpStart = null;
            } else {
                this.mTcpStart.interrupt();
                Thread thread2 = this.mTcpRecv;
                if (thread2 != null) {
                    thread2.interrupt();
                }
            }
        }
        Thread thread3 = new Thread(new AnonymousClass5());
        this.mTcpStart = thread3;
        thread3.start();
    }

    /* renamed from: com.generalplus.GoPlusDrone.Activity.CameraActivity$5, reason: invalid class name */
    class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Log.e(CameraActivity.TAG, "StartTcp-SocketTryConnect");
                if (CameraActivity.this.mSocket != null) {
                    try {
                        CameraActivity.this.mSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CameraActivity.this.mSocket = null;
                }
                CameraActivity.this.mSocket = new Socket(CamWrapper.COMMAND_URL, CamWrapper.COMMAN_PORT);
                CameraActivity.this.mSocket.setSoTimeout(0);
                if (!CameraActivity.this.mSocket.isClosed() && CameraActivity.this.mSocket.isConnected()) {
                    Log.e("SocketTcp", "Socket Conntcted!!!");
                    CameraActivity.this.mInputStream = CameraActivity.this.mSocket.getInputStream();
                    CameraActivity.this.mOutputStream = CameraActivity.this.mSocket.getOutputStream();
                    if (CameraActivity.this.mTcpRecv != null) {
                        CameraActivity.this.mTcpRecv.interrupt();
                    }
                    CameraActivity.this.mTcpRecv = new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.e("SocketTcp", "Socket Start Thread!!!");
                            while (true) {
                                byte[] bArr = new byte[256];
                                try {
                                    Log.d("SocketTcp", "Socket Read Data!!!");
                                    int read = CameraActivity.this.mInputStream.read(bArr);
                                    if (read > 0) {
                                        Log.e("Socket-Recv", CameraActivity.bytesToHex_HasSpace(bArr, 0, read));
                                        if (bArr[0] == 71 && bArr[1] == 80) {
                                            Log.e("Recv", "Recv1");
                                            if (!CameraActivity.isDevicePass || bArr[10] != 2) {
                                                if (CameraActivity.isDevicePass && bArr[10] == 1) {
                                                    Log.e("Recv", "录像");
                                                    CameraActivity.btnTakeRecorder.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.5.1.3
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            CameraActivity.btnTakeRecorder.callOnClick();
                                                        }
                                                    });
                                                } else if (bArr[10] == 3 && !CameraActivity.isDevicePass) {
                                                    boolean unused = CameraActivity.isDevicePass = true;
                                                    CameraActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.5.1.4
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            CameraActivity.this.timer = new Timer(true);
                                                            CameraActivity.this.timer.schedule(CameraActivity.this.new MyTimerTask(), 0L, 1000L);
                                                        }
                                                    });
                                                }
                                            } else {
                                                Log.e("Recv", "拍照");
                                                CameraActivity.this.btnSnapshot.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.5.1.2
                                                    @Override // java.lang.Runnable
                                                    public void run() {
                                                        CameraActivity.this.btnSnapshot.callOnClick();
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } catch (Exception e2) {
                                    Log.e("SocketError", "Socket Recv Error!!!" + e2.getMessage());
                                    e2.printStackTrace();
                                    try {
                                        Thread.sleep(3000L);
                                    } catch (InterruptedException e3) {
                                        e3.printStackTrace();
                                    }
                                    CameraActivity.this.startTcp("接收出错重开始");
                                    Log.e("SocketError", "!!!Socket Closed!!!");
                                    return;
                                }
                            }
                        }
                    });
                    CameraActivity.this.mTcpRecv.start();
                    CameraActivity.this.sendCmd(new byte[]{71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, -112, 0, 0, 0, 0, 1});
                    CameraActivity.this.sendCmd(new byte[]{71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 0, 0, 0, 0, 0, 1});
                    return;
                }
                Log.e("SocketError", "Socket NotConntcted!!!");
                CameraActivity.this.mInputStream = null;
                CameraActivity.this.mOutputStream = null;
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                CameraActivity.this.startTcp("侦测连接不上重开始");
            } catch (Exception e3) {
                Log.e("SocketError", "Try Error!!!");
                e3.printStackTrace();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                CameraActivity.this.startTcp("主连接出错重开始");
            }
        }
    }

    private void closeTcp() {
        Log.e("Socket", "closeTcp!!!");
        Thread thread = this.mTcpStart;
        if (thread != null) {
            if (thread.isInterrupted()) {
                this.mTcpStart = null;
            } else {
                this.mTcpStart.interrupt();
                Thread thread2 = this.mTcpRecv;
                if (thread2 != null) {
                    thread2.interrupt();
                }
            }
        }
        Socket socket = this.mSocket;
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mSocket = null;
        }
    }

    public static String bytesToHex_HasSpace(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            Log.e("bytesToHex_HasSpace", "Null Bytes!");
            return null;
        }
        if (i2 < 1) {
            i2 = bArr.length - i;
        }
        String str = "";
        for (int i3 = i; i3 < i + i2; i3++) {
            String hexString = Integer.toHexString(bArr[i3] & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            str = str + hexString.toUpperCase() + " ";
        }
        return str;
    }

    public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            CameraActivity.this.tryPlay();
        }
    }

    public void tryPlay() {
        try {
            Log.e(TAG, "StartPlay-try");
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                final Integer valueOf = Integer.valueOf(connectionInfo.getLinkSpeed());
                boolean z = false;
                if (!this.mIsStart) {
                    int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
                    if (ipAddress == 0) {
                        return;
                    }
                    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                        ipAddress = Integer.reverseBytes(ipAddress);
                    }
                    byte[] byteArray = BigInteger.valueOf(ipAddress).toByteArray();
                    ffmpegWrapper.getInstance();
                    ffmpegWrapper.naSetStreaming(true);
                    String str = "rtsp://" + (byteArray[0] & UByte.MAX_VALUE) + "." + (byteArray[1] & UByte.MAX_VALUE) + "." + (byteArray[2] & UByte.MAX_VALUE) + ".1:8080/?action=camstream";
                    getTick();
                    ffmpegWrapper.getInstance();
                    ffmpegWrapper.naInitAndPlay(str, "");
                    this.mIsStart = true;
                }
                ffmpegWrapper.getInstance();
                if (ffmpegWrapper.naStatus() != ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal()) {
                    ffmpegWrapper.getInstance();
                    ffmpegWrapper.naPlay();
                    this.m_iRetry++;
                    int ordinal = ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal();
                    ffmpegWrapper.getInstance();
                    if (ordinal == ffmpegWrapper.naStatus()) {
                        z = true;
                    }
                }
                boolean finalZ = z;
                runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (finalZ) {
                            Toast.makeText(CameraActivity.this, "Replay.", 0).show();
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("m_tvSpeed = ");
                        sb.append(valueOf);
                        sb.append("  naStatus = ");
                        ffmpegWrapper.getInstance();
                        sb.append(ffmpegWrapper.naStatus());
                        Log.e(CameraActivity.TAG, sb.toString());
                        CameraActivity.this.m_tvSpeed.setText("Retry = " + CameraActivity.this.m_iRetry + "," + valueOf);
                        int ordinal2 = ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal();
                        ffmpegWrapper.getInstance();
                        if (ordinal2 != ffmpegWrapper.naStatus()) {
                            CameraActivity.this.setPlayStatus(false);
                        } else {
                            CameraActivity.this.setPlayStatus(true);
                        }
                    }
                });
                return;
            }
            runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.7
                @Override // java.lang.Runnable
                public void run() {
                    CameraActivity.this.m_tvSpeed.setText("Wifi null");
                    Toast.makeText(CameraActivity.this, "Not WIFI!", 0).show();
                }
            });
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayStatus(boolean z) {
        bCheckConnectStatus = z;
        if (z) {
            this.m_ivBroken.setVisibility(4);
            boolean z2 = this.mSaveVideo;
        } else {
            checkStopRecode();
            this.m_ivBroken.setVisibility(0);
        }
    }

    protected void StreamingOff() {
        if (this.mIsStart) {
            ffmpegWrapper.getInstance();
            ffmpegWrapper.naStop();
            this.mIsStart = false;
        }
    }

    private String getTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }

    public void pressHide(View view) {
        if (this.m_rlControlLayout.getVisibility() == 0) {
            this.m_rlControlLayout.setVisibility(8);
        } else {
            this.m_rlControlLayout.setVisibility(0);
        }
        this.m_iControlLayout = this.m_rlControlLayout.getVisibility();
    }

    public void pressCapture(View view) {
        Log.e(TAG, "pressCapture");
        if (!bCheckConnectStatus) {
            Toast.makeText(this, "The camera is not connected", 0).show();
            return;
        }
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSaveSnapshot(this.strSaveDirectory + "/Photo/" + getTime() + UVCCameraHelper.SUFFIX_JPEG, this.mSaveType);
        Toast.makeText(this, R.string.capture_successfully, 0).show();
    }

    private int stopRecode(boolean z) {
        this.handler.removeCallbacks(this.updateTimer);
        this.m_tvRecordTime.setVisibility(4);
        this.m_tvRecordTime.setText("00:00:00");
        if (z || this.mSaveType < 3) {
            ffmpegWrapper.getInstance();
            return ffmpegWrapper.naStopSaveVideo();
        }
        this.isDelayStopVideo = true;
        new Timer().schedule(new TimerTask() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.9
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (CameraActivity.this.isDelayStopVideo) {
                    ffmpegWrapper.getInstance();
                    ffmpegWrapper.naStopSaveVideo();
                }
            }
        }, 3000L);
        return 0;
    }

    public void pressRecord(View view) {
        Log.e(TAG, "pressRecord");
        if (!bCheckConnectStatus) {
            Toast.makeText(this, "The camera is not connected", 0).show();
            return;
        }
        if (this.m_bPause) {
            return;
        }
        if (!this.mSaveVideo) {
            if (this.isDelayStopVideo) {
                this.isDelayStopVideo = false;
                ffmpegWrapper.getInstance();
                ffmpegWrapper.naStopSaveVideo();
            }
            Toast.makeText(this, R.string.start_recorder, 0).show();
            String time = getTime();
            ffmpegWrapper.getInstance();
            ffmpegWrapper.naSaveSnapshot(this.strSaveDirectory + "/Video/thumbnails/" + time + UVCCameraHelper.SUFFIX_JPEG, 1);
            ffmpegWrapper.getInstance();
            byte[] bArr = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 3, 1, 0, 0, 0, 0};
            bArr[17] = (byte) ffmpegWrapper.naSaveVideo(this.strSaveDirectory + "/Video/" + time, this.mSaveType);
            sendCmd(bArr);
            this.m_tvRecordTime.setVisibility(0);
            this.startTime = Long.valueOf(System.currentTimeMillis());
            this.handler.removeCallbacks(this.updateTimer);
            this.handler.postDelayed(this.updateTimer, 1000L);
        } else {
            Toast.makeText(this, R.string.stop_recorder, 0).show();
            byte[] bArr2 = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 3, 2, 0, 0, 0, 0};
            bArr2[17] = (byte) stopRecode(false);
            sendCmd(bArr2);
        }
        this.mSaveVideo = !this.mSaveVideo;
        Log.e(TAG, "End pressRecord");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int sendCmd(final byte[] bArr) {
        if (bArr == null) {
            Log.e("Socket-sendCmd", "命令为空!");
            return -1;
        }
        this.SendRet = 0;
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.10
            @Override // java.lang.Runnable
            public void run() {
                if (CameraActivity.this.mOutputStream != null && CameraActivity.this.mSocket != null && CameraActivity.this.mSocket.isConnected()) {
                    try {
                        CameraActivity.this.mOutputStream.write(bArr);
                        CameraActivity.this.mOutputStream.flush();
                        Log.e("Socket-sendCmd", CameraActivity.bytesToHex_HasSpace(bArr, 0, 0));
                        CameraActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.10.1
                            @Override // java.lang.Runnable
                            public void run() {
                            }
                        });
                        CameraActivity.this.SendRet = 1;
                        return;
                    } catch (Exception e) {
                        Log.e("Socket-sendCmd", "发送出错:" + CameraActivity.bytesToHex_HasSpace(bArr, 0, 0));
                        e.printStackTrace();
                        return;
                    }
                }
                Log.e("Socket-sendCmd", "未连接Socket:" + CameraActivity.bytesToHex_HasSpace(bArr, 0, 0));
                CameraActivity.this.SendRet = -1;
            }
        }).start();
        return 1;
    }

    public void exit() {
        Timer timer = new Timer();
        this.exitTimer = timer;
        timer.schedule(new TimerTask() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.12
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                CameraActivity.isHave = false;
                try {
                    Thread.sleep(8000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean z = CameraActivity.isHave;
            }
        }, 200L, 8500L);
    }

    private void initGL2JNIVIEW() {
        if (!isOff && !this.isS) {
            this.isS = true;
        }
        if (isOff) {
            imageView = (ImageView) findViewById(R.id.camera_video_dot);
            new RelativeLayout.LayoutParams((this.mScreenWidth * 99) / 100, this.mScreenHigth).leftMargin = this.mScreenWidth / 200;
            relative.setGravity(17);
            this.btnSwitch.setVisibility(4);
            return;
        }
        this.btnSwitch.setVisibility(0);
    }

    private void rightView() {
        if (isLeft) {
            if (this.rig == 1) {
                RelativeLayout relativeLayout2 = this.linearLayout;
                if (relativeLayout2 != null) {
                    relativeLayout2.setVisibility(4);
                }
            } else {
                new RelativeLayout.LayoutParams(this.mScreenWidth, this.mScreenHigth).leftMargin = this.mScreenWidth / 4;
                relative.removeView(imageView);
                imageView = new ImageView(this);
                int i = this.mScreenWidth;
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i / 30, i / 30);
                layoutParams.leftMargin = (this.mScreenWidth * 16) / 30;
                layoutParams.topMargin = this.mScreenWidth / 30;
                imageView.setImageResource(R.drawable.cicle);
                imageView.setVisibility(4);
                relative.addView(imageView, layoutParams);
                this.rig = 1;
            }
        } else {
            RelativeLayout relativeLayout3 = this.linearLayout;
            if (relativeLayout3 != null) {
                relativeLayout3.setVisibility(0);
            } else {
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.mScreenWidth / 2, this.mScreenHigth);
                this.linearLayout = new RelativeLayout(this);
                layoutParams2.leftMargin = this.mScreenWidth / 2;
                relative.addView(this.linearLayout, layoutParams2);
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-2, this.mScreenHigth);
                final MHorizontal mHorizontal = new MHorizontal(this);
                this.linearLayout.addView(mHorizontal, layoutParams3);
                this.handler.postDelayed(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.13
                    @Override // java.lang.Runnable
                    public void run() {
                        mHorizontal.smoothScrollBy((CameraActivity.this.mScreenWidth * 3) / 8, 0);
                    }
                }, 200L);
                mHorizontal.addView(new RelativeLayout(this), new RelativeLayout.LayoutParams(this.mScreenWidth, this.mScreenHigth));
                RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(-2, this.mScreenHigth);
                layoutParams4.addRule(11);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                linearLayout.setGravity(16);
                this.linearLayout.addView(linearLayout, layoutParams4);
            }
        }
        int i2 = this.mScreenWidth;
        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(i2 / 4, i2 / 4);
        layoutParams5.leftMargin = (this.mScreenWidth * 5) / 8;
        int i3 = this.mScreenHigth - (this.mScreenWidth / 4);
        if (i3 < 0) {
            i3 = 0 - i3;
        }
        layoutParams5.topMargin = i3 / 2;
        ImageView imageView2 = this.shizi2ImageView;
        if (imageView2 != null) {
            if (!this.isS) {
                imageView2.setVisibility(4);
            }
            this.shizi2ImageView = null;
        }
        ImageView imageView3 = new ImageView(this);
        this.shizi2ImageView = imageView3;
        imageView3.setLayoutParams(layoutParams5);
        this.shizi2ImageView.setBackgroundResource(R.drawable.shizi);
        this.shizi2ImageView.setVisibility(4);
        if (!this.isS) {
            this.shizi2ImageView.setVisibility(0);
        } else {
            this.shizi2ImageView.setVisibility(4);
        }
        relative.addView(this.shizi2ImageView);
    }

    private void initView() {
        this.buttonW = this.mScreenWidth / 13;
        int i = this.buttonW;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, i);
        ImageButton imageButton = new ImageButton(this);
        this.backButton = imageButton;
        imageButton.setBackgroundResource(R.drawable.back_button);
        this.backButton.setId(3);
        this.backButton.setLayoutParams(layoutParams);
        layout.addView(this.backButton);
        int i2 = this.buttonW;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(i2, i2);
        layoutParams2.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton2 = new ImageButton(this);
        this.subButton = imageButton2;
        imageButton2.setBackgroundResource(R.drawable.sub_button);
        this.subButton.setId(4);
        this.subButton.setLayoutParams(layoutParams2);
        layout.addView(this.subButton);
        int i3 = this.buttonW;
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(i3, i3);
        ImageButton imageButton3 = new ImageButton(this);
        this.autoButton = imageButton3;
        imageButton3.setBackgroundResource(R.drawable.auto_button);
        this.autoButton.setId(5);
        this.autoButton.setLayoutParams(layoutParams3);
        layout.addView(this.autoButton);
        int i4 = this.buttonW;
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(i4, i4);
        ImageButton imageButton4 = new ImageButton(this);
        this.sumButton = imageButton4;
        imageButton4.setBackgroundResource(R.drawable.sum_button);
        this.sumButton.setId(6);
        this.sumButton.setLayoutParams(layoutParams4);
        layout.addView(this.sumButton);
        int i5 = this.buttonW;
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams(i5, i5);
        layoutParams5.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton5 = new ImageButton(this);
        this.narrowButton = imageButton5;
        imageButton5.setBackgroundResource(R.drawable.narrow_button);
        this.narrowButton.setId(7);
        this.narrowButton.setLayoutParams(layoutParams5);
        layout.addView(this.narrowButton);
        int i6 = this.buttonW;
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams(i6, i6);
        ImageButton imageButton6 = new ImageButton(this);
        this.ampButton = imageButton6;
        imageButton6.setBackgroundResource(R.drawable.amp_button);
        this.ampButton.setId(8);
        this.ampButton.setLayoutParams(layoutParams6);
        layout.addView(this.ampButton);
        int i7 = this.buttonW;
        LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams(i7, i7);
        layoutParams7.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton7 = new ImageButton(this);
        this.btnTip = imageButton7;
        imageButton7.setBackgroundResource(R.drawable.btntip);
        this.btnTip.setId(9);
        this.btnTip.setLayoutParams(layoutParams7);
        layout.addView(this.btnTip);
        int i8 = this.buttonW;
        LinearLayout.LayoutParams layoutParams8 = new LinearLayout.LayoutParams(i8, i8);
        layoutParams8.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton8 = new ImageButton(this);
        this.btnSnapshot = imageButton8;
        imageButton8.setBackgroundResource(R.drawable.photo_button);
        this.btnSnapshot.setId(1);
        this.btnSnapshot.setLayoutParams(layoutParams8);
        layout.addView(this.btnSnapshot);
        int i9 = this.buttonW;
        LinearLayout.LayoutParams layoutParams9 = new LinearLayout.LayoutParams(i9, i9);
        layoutParams9.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton9 = new ImageButton(this);
        btnTakeRecorder = imageButton9;
        imageButton9.setBackgroundResource(R.drawable.vedio_button);
        btnTakeRecorder.setId(2);
        btnTakeRecorder.setLayoutParams(layoutParams9);
        layout.addView(btnTakeRecorder);
        int i10 = this.buttonW;
        LinearLayout.LayoutParams layoutParams10 = new LinearLayout.LayoutParams(i10, i10);
        layoutParams10.leftMargin = this.mScreenWidth / 30;
        ImageButton imageButton10 = new ImageButton(this);
        this.fileButton = imageButton10;
        imageButton10.setBackgroundResource(R.drawable.file_button);
        this.fileButton.setId(10);
        this.fileButton.setLayoutParams(layoutParams10);
        layout.addView(this.fileButton);
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        exit();
        initOff();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mSurfaceView.onResume();
        this.m_bPause = false;
        this.isTcpStart = true;
        startTcp("创建开始");
        if (isDevicePass) {
            Timer timer = new Timer(true);
            this.timer = timer;
            timer.schedule(new MyTimerTask(), 0L, 1000L);
        }
    }

    public void textSave(int i) {
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putInt("text", i);
        edit.commit();
    }

    public void text(int i) {
        switch (i) {
            case 12:
                this.textMenu12.setBackgroundColor(-11119018);
                this.textMenu13.setBackgroundColor(-2629656);
                this.textMenu14.setBackgroundColor(-2629656);
                break;
            case 13:
                this.textMenu12.setBackgroundColor(-2629656);
                this.textMenu13.setBackgroundColor(-11119018);
                this.textMenu14.setBackgroundColor(-2629656);
                break;
            case 14:
                this.textMenu12.setBackgroundColor(-2629656);
                this.textMenu13.setBackgroundColor(-2629656);
                this.textMenu14.setBackgroundColor(-11119018);
                break;
        }
    }

    private void initOff() {
        SharedPreferences sharedPreferences = getSharedPreferences("config", 0);
        this.sharedPreferences = sharedPreferences;
        isOff = sharedPreferences.getBoolean("isOff", true);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        this.isThtead2 = false;
        Timer timer = this.exitTimer;
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naStop();
        this.isThtead2 = false;
        System.gc();
    }

    public void littleShow() {
        WindowManager windowManager = (WindowManager) getSystemService("window");
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = this.mSurfaceView.getLayoutParams();
        if (this.mWidth < 0 || this.mHeight < 0) {
            Log.e("Scale", "-Up-,mWidthStart:" + this.mWidth + ",mHeightStart:" + this.mHeight);
            this.mWidth = width;
            this.mHeight = height;
        }
        Log.e("Scale", "-Up-,mWidthOld:" + this.mWidth + ",mHeightOld:" + this.mHeight + ",mScale:" + this.mScale);
        float f = this.mScale - 0.15f;
        this.mScale = f;
        layoutParams.width = (int) (((float) this.mWidth) * f);
        Log.e("Scale", "-Up-,mWidthNew:" + layoutParams.width + ",mHeightNew:" + layoutParams.height + ",mScale:" + this.mScale);
        this.mSurfaceView.setPivotX((float) (width / 2));
        Log.e("onDraw", "onDrawonDrawonDraw2222");
        float f2 = (float) this.mWidth;
        float f3 = this.mScale;
        this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (f2 * f3), (int) (((float) this.mHeight) * f3), 17));
        Log.e("onDraw", "onDrawonDrawonDraw3333");
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.14
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CameraActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        WindowManager windowManager2 = (WindowManager) CameraActivity.this.getSystemService("window");
                        int width2 = windowManager2.getDefaultDisplay().getWidth();
                        int height2 = windowManager2.getDefaultDisplay().getHeight();
                        ViewGroup.LayoutParams layoutParams2 = CameraActivity.this.mSurfaceView.getLayoutParams();
                        if (CameraActivity.this.mWidth < 0 || CameraActivity.this.mHeight < 0) {
                            Log.e("Scale", "-Down-,mWidthStart:" + CameraActivity.this.mWidth + ",mHeightStart:" + CameraActivity.this.mHeight);
                            CameraActivity.this.mWidth = width2;
                            CameraActivity.this.mHeight = height2;
                        }
                        Log.e("Scale", "-Down-,mWidthOld:" + CameraActivity.this.mWidth + ",mHeightOld:" + CameraActivity.this.mHeight + ",mScale:" + CameraActivity.this.mScale);
                        CameraActivity.this.mScale = CameraActivity.this.mScale + 0.15f;
                        layoutParams2.width = (int) (((float) CameraActivity.this.mWidth) * CameraActivity.this.mScale);
                        Log.e("Scale", "-Down-,mWidthNew:" + layoutParams2.width + ",mHeightNew:" + layoutParams2.height + ",mScale:" + CameraActivity.this.mScale);
                        CameraActivity.this.mSurfaceView.setPivotX((float) (width2 / 2));
                        CameraActivity.this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (((float) CameraActivity.this.mWidth) * CameraActivity.this.mScale), (int) (((float) CameraActivity.this.mHeight) * CameraActivity.this.mScale), 17));
                    }
                });
            }
        }).start();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case 1:
                pressCapture(view);
                break;
            case 2:
                pressRecord(view);
                break;
            case 3:
                isT = false;
                isT2 = false;
                finish();
                break;
            case 4:
                byte[] bArr = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 1};
                if (isDevicePass) {
                    sendCmd(bArr);
                    break;
                }
                break;
            case 5:
                byte[] bArr2 = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 2};
                if (isDevicePass) {
                    sendCmd(bArr2);
                    break;
                }
                break;
            case 6:
                byte[] bArr3 = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 3};
                if (isDevicePass) {
                    sendCmd(bArr3);
                    break;
                }
                break;
            case 7:
                int i = this.a;
                if (i <= 0) {
                    this.a = 0;
                } else {
                    this.a = i - 1;
                }
                WindowManager windowManager = (WindowManager) getSystemService("window");
                int width = windowManager.getDefaultDisplay().getWidth();
                int height = windowManager.getDefaultDisplay().getHeight();
                ViewGroup.LayoutParams layoutParams = this.mSurfaceView.getLayoutParams();
                if (this.mWidth < 0 || this.mHeight < 0) {
                    Log.e("Scale", "-Down-,mWidthStart:" + this.mWidth + ",mHeightStart:" + this.mHeight);
                    this.mWidth = width;
                    this.mHeight = height;
                }
                Log.e("Scale", "-Down-,mWidthOld:" + this.mWidth + ",mHeightOld:" + this.mHeight + ",mScale:" + this.mScale);
                float f = this.mScale - 0.25f;
                this.mScale = f;
                if (f > 4.0f) {
                    this.mScale = 4.0f;
                } else if (f < 1.0f) {
                    this.mScale = 1.0f;
                }
                layoutParams.width = (int) (this.mWidth * this.mScale);
                layoutParams.height = (int) (this.mHeight * this.mScale);
                Log.e("Scale", "-Down-,mWidthNew:" + layoutParams.width + ",mHeightNew:" + layoutParams.height + ",mScale:" + this.mScale);
                this.mSurfaceView.setPivotX((float) (width / 2));
                this.mSurfaceView.setPivotY((float) (height / 2));
                float f2 = (float) this.mWidth;
                float f3 = this.mScale;
                this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (f2 * f3), (int) (((float) this.mHeight) * f3), 17));
                break;
            case 8:
                int i2 = this.a;
                if (i2 >= 10) {
                    this.a = 10;
                } else {
                    this.a = i2 + 1;
                }
                WindowManager windowManager2 = (WindowManager) getSystemService("window");
                int width2 = windowManager2.getDefaultDisplay().getWidth();
                int height2 = windowManager2.getDefaultDisplay().getHeight();
                ViewGroup.LayoutParams layoutParams2 = this.mSurfaceView.getLayoutParams();
                if (this.mWidth < 0 || this.mHeight < 0) {
                    Log.e("Scale", "-Up-,mWidthStart:" + this.mWidth + ",mHeightStart:" + this.mHeight);
                    this.mWidth = width2;
                    this.mHeight = height2;
                }
                Log.e("Scale", "-Up-,mWidthOld:" + this.mWidth + ",mHeightOld:" + this.mHeight + ",mScale:" + this.mScale);
                float f4 = this.mScale + 0.25f;
                this.mScale = f4;
                if (f4 > 4.0f) {
                    this.mScale = 4.0f;
                } else if (f4 < 1.0f) {
                    this.mScale = 1.0f;
                }
                layoutParams2.width = (int) (this.mWidth * this.mScale);
                layoutParams2.height = (int) (this.mHeight * this.mScale);
                Log.e("Scale", "-Up-,mWidthNew:" + layoutParams2.width + ",mHeightNew:" + layoutParams2.height + ",mScale:" + this.mScale);
                this.mSurfaceView.setPivotX((float) (width2 / 2));
                this.mSurfaceView.setPivotY((float) (height2 / 2));
                Log.e("onDraw", "onDrawonDrawonDraw2222");
                float f5 = (float) this.mWidth;
                float f6 = this.mScale;
                this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (f5 * f6), (int) (((float) this.mHeight) * f6), 17));
                Log.e("onDraw", "onDrawonDrawonDraw3333");
                break;
            case 9:
                boolean z = !this.isImgTipShow;
                this.isImgTipShow = z;
                if (z) {
                    imgTip.setBackgroundResource(R.drawable.shizi);
                    break;
                } else {
                    imgTip.setBackgroundResource(0);
                    break;
                }
            case 10:
                startActivity(new Intent(this, (Class<?>) GalleryActivity.class));
                break;
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        TimeThread timeThread = this.timeThread;
        if (timeThread != null) {
            timeThread.interrupt();
            this.timeThread = null;
        }
        this.handler.postDelayed(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraActivity.16
            @Override // java.lang.Runnable
            public void run() {
                CameraActivity.this.btnLeftMenu.setVisibility(0);
            }
        }, 20L);
        TimeThread timeThread2 = new TimeThread();
        this.timeThread = timeThread2;
        timeThread2.start();
        return false;
    }

    class TimeThread extends Thread {
        TimeThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            try {
                sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CameraActivity.this.handler.sendEmptyMessage(100);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            isT = false;
            isT2 = false;
            finish();
            return false;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.isTcpStart = false;
        closeTcp();
        this.m_bPause = true;
        checkStopRecode();
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        StreamingOff();
        this.mSurfaceView.onPause();
        System.out.println("CameraActivity onPause() isR =" + isR);
        this.rig = 0;
        isLeft = true;
        SharedPreferences.Editor edit = this.sharedPreferences.edit();
        edit.putBoolean("isOff", isOff);
        edit.commit();
        this.handler.sendEmptyMessage(-1);
    }
}
