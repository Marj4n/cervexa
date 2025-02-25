package com.generalplus.GoPlusDrone.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.generalplus.GoPlusDrone.R;
import com.generalplus.ffmpegLib.ZoomableSurfaceView;
import com.generalplus.ffmpegLib.ffmpegWrapper;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.serenegiant.usb.UVCCamera;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.UByte;

/* loaded from: classes.dex */
public class CameraNewActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
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
    private static boolean isDevicePass = true;
    public static boolean isHave = false;
    static boolean isLeft = true;
    static boolean isOff = true;
    static boolean isR = false;
    public static boolean isT = false;
    public static boolean isT2 = false;
    static boolean isTime = false;
    static LinearLayout linearLayoutR;
    static int numLeft;
    static int numRight;
    static RelativeLayout relativeLayoutR;
    static MHorizontal scrollView;
    public static int text;
    private int buttonW;
    private Timer exitTimer;
    private TextView ivBroken;
    private ImageView iv_focus_img;
    private ImageView iv_picture;
    private ImageView iv_record;
    private ImageView iv_resolution;
    private ImageView iv_zoom_focus;
    private ImageView iv_zoom_in;
    private ImageView iv_zoom_out;
    RelativeLayout linearLayout;
    private LinearLayout ll_content;
    private LinearLayout ll_resolution;
    private GestureDetector mDetector;
    private int mScreenHigth;
    private int mScreenWidth;
    private ZoomableSurfaceView mSurfaceView;
    private CamWrapper m_CamWrapper;
    private RecyclerView rc_resolution;
    private SharedPreferences sharedPreferences;
    private ImageView shizi2ImageView;
    protected Long startTime;
    private String strSaveDirectory;
    private TextView tv_record_time;
    byte[] data = null;
    protected ImageButton mBtnTest = null;
    private ImageButton m_bnHide = null;
    private RelativeLayout m_rlControlLayout = null;
    private boolean mSaveVideo = false;
    private boolean mIsStart = false;
    private int m_iControlLayout = 0;
    private boolean m_bShowControlLayout = false;
    private boolean m_bPause = false;
    private Timer timer = new Timer(true);
    private ImageView take_photo = null;
    private Thread mTcpStart = null;
    private Thread mTcpRecv = null;
    private Socket mSocket = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private long mStartTime = 0;
    int selectedWidth = UVCCamera.DEFAULT_PREVIEW_WIDTH;
    int selectedHeight = 480;
    float x1 = 0.0f;
    float x2 = 0.0f;
    float y1 = 0.0f;
    float y2 = 0.0f;
    private final boolean isDebug = false;
    private boolean isTcpStart = false;
    int m_iRetry = 0;
    private Handler m_StatusHandler = new Handler() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.15
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0 && message.what == 1) {
                CameraNewActivity.this.mIsStart = false;
            }
            Log.e(CameraNewActivity.TAG, "m_StatusHandler" + message.what);
        }
    };
    private boolean isDelayStopVideo = false;
    int SendRet = 0;
    protected Runnable updateTimer = new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.18
        @SuppressLint("DefaultLocale")
        @Override // java.lang.Runnable
        public void run() {
            Long valueOf = Long.valueOf(System.currentTimeMillis() - CameraNewActivity.this.startTime.longValue());
            Long valueOf2 = Long.valueOf(((valueOf.longValue() / 1000) / 60) / 60);
            Long valueOf3 = Long.valueOf(((valueOf.longValue() / 1000) / 60) % 60);
            Long valueOf4 = Long.valueOf((valueOf.longValue() / 1000) % 60);
            CameraNewActivity.this.tv_record_time.setText(
                    String.format("%02d", valueOf2) + ":" +
                            String.format("%02d", valueOf3) + ":" +
                            String.format("%02d", valueOf4)
            );
            CameraNewActivity.this.handler.postDelayed(this, 1000L);
        }
    };
    private int rig = 0;
    public boolean isThtead2 = true;
    private int mSaveType = 1;
    boolean isS = true;
    int a = 0;
    private int mWidth = -1;
    private int mHeight = -1;
    private float mScale = 1.0f;
    boolean isImgTipShow = false;
    Handler handler = new Handler() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.21
        @SuppressLint("HandlerLeak")
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 100 && message.what == -1 && CameraNewActivity.isR) {
                CameraNewActivity.isR = false;
                new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.21.1
                    @SuppressLint("HandlerLeak")
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            System.out.println("CameraActivity onPause() stop isHave=" + CameraNewActivity.isHave);
                        } catch (Exception unused) {
                            System.out.println("CameraActivity onPause() 异常断开连接");
                        }
                    }
                }).start();
                CameraNewActivity cameraNewActivity = CameraNewActivity.this;
                Toast.makeText(cameraNewActivity, cameraNewActivity.getResources().getString(R.string.language22), 0).show();
                CameraNewActivity.this.iv_record.setBackgroundResource(R.mipmap.icon_record);
            }
        }
    };
    TimeThread timeThread = null;

    private class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        private ExceptionHandler() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            Log.e(CameraNewActivity.TAG, "uncaught_exception_handler: uncaught exception in thread " + thread.getName(), th);
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
            Log.e(CameraNewActivity.TAG, "uncaught_exception handler: unable to rethrow checked exception\ntrace: " + obj);
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
        setContentView(R.layout.camera_activity_new);
        this.mStartTime = getTick();
        getWindow().addFlags(128);
        this.isImgTipShow = false;
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        this.mScreenHigth = getResources().getDisplayMetrics().heightPixels;
        isT = false;
        isT2 = false;
        initView();
        if (this.data == null) {
            this.data = new byte[3686400];
        }
        initOff();
        initGL2JNIVIEW();
        isHave = false;
        exit();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        if (this.m_CamWrapper == null) {
            this.m_CamWrapper = new CamWrapper();
        }
        ZoomableSurfaceView zoomableSurfaceView = (ZoomableSurfaceView) findViewById(R.id.surfaceView);
        this.mSurfaceView = zoomableSurfaceView;
        zoomableSurfaceView.setEGLContextClientVersion(2);
        this.mSurfaceView.setRenderer(ffmpegWrapper.getInstance());
        this.mSurfaceView.setKeepScreenOn(true);
        this.mSurfaceView.setOnTouchListener(new View.OnTouchListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        this.ivBroken = (TextView) findViewById(R.id.ivBroken);
        ImageView imageView = (ImageView) findViewById(R.id.iv_zoom_in);
        this.iv_zoom_in = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CameraNewActivity.this.a <= 0) {
                    CameraNewActivity.this.a = 0;
                } else {
                    CameraNewActivity cameraNewActivity = CameraNewActivity.this;
                    cameraNewActivity.a--;
                }
                WindowManager windowManager = (WindowManager) CameraNewActivity.this.getSystemService(Context.WINDOW_SERVICE);
                int width = windowManager.getDefaultDisplay().getWidth();
                int height = windowManager.getDefaultDisplay().getHeight();
                ViewGroup.LayoutParams layoutParams = CameraNewActivity.this.mSurfaceView.getLayoutParams();
                if (CameraNewActivity.this.mWidth < 0 || CameraNewActivity.this.mHeight < 0) {
                    Log.e("Scale", "-Down-,mWidthStart:" + CameraNewActivity.this.mWidth + ",mHeightStart:" + CameraNewActivity.this.mHeight);
                    CameraNewActivity.this.mWidth = width;
                    CameraNewActivity.this.mHeight = height;
                }
                Log.e("Scale", "-Down-,mWidthOld:" + CameraNewActivity.this.mWidth + ",mHeightOld:" + CameraNewActivity.this.mHeight + ",mScale:" + CameraNewActivity.this.mScale);
                CameraNewActivity cameraNewActivity2 = CameraNewActivity.this;
                cameraNewActivity2.mScale = cameraNewActivity2.mScale - 0.25f;
                if (CameraNewActivity.this.mScale > 4.0f) {
                    CameraNewActivity.this.mScale = 4.0f;
                } else if (CameraNewActivity.this.mScale < 1.0f) {
                    CameraNewActivity.this.mScale = 1.0f;
                }
                layoutParams.width = (int) (CameraNewActivity.this.mWidth * CameraNewActivity.this.mScale);
                layoutParams.height = (int) (CameraNewActivity.this.mHeight * CameraNewActivity.this.mScale);
                Log.e("Scale", "-Down-,mWidthNew:" + layoutParams.width + ",mHeightNew:" + layoutParams.height + ",mScale:" + CameraNewActivity.this.mScale);
                CameraNewActivity.this.mSurfaceView.setPivotX((float) (width / 2));
                CameraNewActivity.this.mSurfaceView.setPivotY((float) (height / 2));
                CameraNewActivity.this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (((float) CameraNewActivity.this.mWidth) * CameraNewActivity.this.mScale), (int) (((float) CameraNewActivity.this.mHeight) * CameraNewActivity.this.mScale), 17));
            }
        });
        ImageView imageView2 = (ImageView) findViewById(R.id.iv_zoom_out);
        this.iv_zoom_out = imageView2;
        imageView2.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CameraNewActivity.this.a >= 10) {
                    CameraNewActivity.this.a = 10;
                } else {
                    CameraNewActivity.this.a++;
                }
                WindowManager windowManager = (WindowManager) CameraNewActivity.this.getSystemService(Context.WINDOW_SERVICE);
                int width = windowManager.getDefaultDisplay().getWidth();
                int height = windowManager.getDefaultDisplay().getHeight();
                ViewGroup.LayoutParams layoutParams = CameraNewActivity.this.mSurfaceView.getLayoutParams();
                if (CameraNewActivity.this.mWidth < 0 || CameraNewActivity.this.mHeight < 0) {
                    Log.e("Scale", "-Up-,mWidthStart:" + CameraNewActivity.this.mWidth + ",mHeightStart:" + CameraNewActivity.this.mHeight);
                    CameraNewActivity.this.mWidth = width;
                    CameraNewActivity.this.mHeight = height;
                }
                Log.e("Scale", "-Up-,mWidthOld:" + CameraNewActivity.this.mWidth + ",mHeightOld:" + CameraNewActivity.this.mHeight + ",mScale:" + CameraNewActivity.this.mScale);
                CameraNewActivity cameraNewActivity = CameraNewActivity.this;
                cameraNewActivity.mScale = cameraNewActivity.mScale + 0.25f;
                if (CameraNewActivity.this.mScale > 4.0f) {
                    CameraNewActivity.this.mScale = 4.0f;
                } else if (CameraNewActivity.this.mScale < 1.0f) {
                    CameraNewActivity.this.mScale = 1.0f;
                }
                layoutParams.width = (int) (CameraNewActivity.this.mWidth * CameraNewActivity.this.mScale);
                layoutParams.height = (int) (CameraNewActivity.this.mHeight * CameraNewActivity.this.mScale);
                Log.e("Scale", "-Up-,mWidthNew:" + layoutParams.width + ",mHeightNew:" + layoutParams.height + ",mScale:" + CameraNewActivity.this.mScale);
                CameraNewActivity.this.mSurfaceView.setPivotX((float) (width / 2));
                CameraNewActivity.this.mSurfaceView.setPivotY((float) (height / 2));
                Log.e("onDraw", "onDrawonDrawonDraw2222");
                CameraNewActivity.this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (((float) CameraNewActivity.this.mWidth) * CameraNewActivity.this.mScale), (int) (((float) CameraNewActivity.this.mHeight) * CameraNewActivity.this.mScale), 17));
                Log.e("onDraw", "onDrawonDrawonDraw3333");
            }
        });
        this.iv_focus_img = (ImageView) findViewById(R.id.iv_focus_img);
        ImageView imageView3 = (ImageView) findViewById(R.id.iv_zoom_focus);
        this.iv_zoom_focus = imageView3;
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CameraNewActivity.this.iv_focus_img.getVisibility() == 0) {
                    CameraNewActivity.this.iv_focus_img.setVisibility(8);
                } else {
                    CameraNewActivity.this.iv_focus_img.setVisibility(0);
                }
            }
        });
        this.iv_record = (ImageView) findViewById(R.id.iv_record);
        this.tv_record_time = (TextView) findViewById(R.id.tv_record_time);
        this.iv_record.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraNewActivity.this.pressRecord(view);
            }
        });
        ImageView imageView4 = (ImageView) findViewById(R.id.iv_picture);
        this.iv_picture = imageView4;
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.gizthon.preview.gallery.list");
                CameraNewActivity.this.startActivity(intent);
            }
        });
        ArrayList arrayList = new ArrayList();
        ImageView imageView5 = (ImageView) findViewById(R.id.iv_resolution);
        this.iv_resolution = imageView5;
        imageView5.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraNewActivity.this.ll_resolution.setVisibility(0);
            }
        });
        this.rc_resolution = (RecyclerView) findViewById(R.id.rc_resolution);
        ResolutionAdapter resolutionAdapter = new ResolutionAdapter(this);
        this.mSaveType = 1;
        textSave(14);
        littleShow();
        arrayList.add("640*480");
        arrayList.add("1280*720");
        arrayList.add("1920*1080");
        resolutionAdapter.setData(arrayList);
        resolutionAdapter.setOnClickItem(new ResolutionAdapter.OnClickItem() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.8
            @Override // com.generalplus.GoPlusDrone.Activity.ResolutionAdapter.OnClickItem
            public void onClick(String str) {
                if (TextUtils.equals("1920*1080", str)) {
                    CameraNewActivity.this.mSaveType = 3;
                    CameraNewActivity.this.textSave(12);
                    CameraNewActivity.this.littleShow();
                } else if (TextUtils.equals("1280*720", str)) {
                    CameraNewActivity.this.mSaveType = 2;
                    CameraNewActivity.this.textSave(13);
                    CameraNewActivity.this.littleShow();
                } else if (TextUtils.equals("640*480", str)) {
                    CameraNewActivity.this.mSaveType = 1;
                    CameraNewActivity.this.textSave(14);
                    CameraNewActivity.this.littleShow();
                }
                String[] split = str.split("\\*");
                CameraNewActivity.this.selectedWidth = Integer.parseInt(split[0]);
                CameraNewActivity.this.selectedHeight = Integer.parseInt(split[1]);
                Toast.makeText(CameraNewActivity.this, CameraNewActivity.this.getResources().getString(R.string.haveto_switch) + str, 1).show();
            }
        });
        this.rc_resolution.setLayoutManager(new LinearLayoutManager(this));
        this.rc_resolution.setAdapter(resolutionAdapter);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_resolution);
        this.ll_resolution = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraNewActivity.this.ll_resolution.setVisibility(8);
            }
        });
        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraNewActivity.this.finish();
            }
        });
        ImageView imageView6 = (ImageView) findViewById(R.id.take_photo);
        this.take_photo = imageView6;
        imageView6.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CameraNewActivity.this.pressCapture(view);
            }
        });
        this.strSaveDirectory = Environment.getExternalStorageDirectory().getPath() + "/MergeCamera/Media";
        new File(this.strSaveDirectory).mkdirs();
        new File(this.strSaveDirectory + "/Photo/Screen/").mkdirs();
        new File(this.strSaveDirectory + "/Video/").mkdir();
        new File(this.strSaveDirectory + "/Video/thumbnails/").mkdir();
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetDebugMessage(true);
        ffmpegWrapper.getInstance().SetViewHandler(this.m_StatusHandler);
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetDecodeOptions("flags=low_delay");
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSetScaleMode(0);
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
                int i = ((this.x1 - this.x2) > 50.0f ? 1 : ((this.x1 - this.x2) == 50.0f ? 0 : -1));
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
        Thread thread3 = new Thread(new AnonymousClass12());
        this.mTcpStart = thread3;
        thread3.start();
    }

    /* renamed from: com.generalplus.GoPlusDrone.Activity.CameraNewActivity$12, reason: invalid class name */
    class AnonymousClass12 implements Runnable {
        AnonymousClass12() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Log.e(CameraNewActivity.TAG, "StartTcp-SocketTryConnect");
                if (CameraNewActivity.this.mSocket != null) {
                    try {
                        CameraNewActivity.this.mSocket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CameraNewActivity.this.mSocket = null;
                }
                CameraNewActivity.this.mSocket = new Socket(CamWrapper.COMMAND_URL, CamWrapper.COMMAN_PORT);
                CameraNewActivity.this.mSocket.setSoTimeout(0);
                if (!CameraNewActivity.this.mSocket.isClosed() && CameraNewActivity.this.mSocket.isConnected()) {
                    Log.e("SocketTcp", "Socket Conntcted!!!");
                    CameraNewActivity.this.mInputStream = CameraNewActivity.this.mSocket.getInputStream();
                    CameraNewActivity.this.mOutputStream = CameraNewActivity.this.mSocket.getOutputStream();
                    if (CameraNewActivity.this.mTcpRecv != null) {
                        CameraNewActivity.this.mTcpRecv.interrupt();
                    }
                    CameraNewActivity.this.mTcpRecv = new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.e("SocketTcp", "Socket Start Thread!!!");
                            while (true) {
                                byte[] bArr = new byte[256];
                                try {
                                    Log.d("SocketTcp", "Socket Read Data!!!");
                                    int read = CameraNewActivity.this.mInputStream.read(bArr);
                                    if (read > 0) {
                                        Log.e("Socket-Recv", CameraNewActivity.bytesToHex_HasSpace(bArr, 0, read));
                                        if (bArr[0] == 71 && bArr[1] == 80) {
                                            Log.e("Recv", "Recv1");
                                            if (!CameraNewActivity.isDevicePass || bArr[10] != 2) {
                                                if (CameraNewActivity.isDevicePass && bArr[10] == 1) {
                                                    Log.e("Recv", "录像");
                                                    CameraNewActivity.this.iv_record.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.12.1.3
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            CameraNewActivity.this.iv_record.callOnClick();
                                                        }
                                                    });
                                                } else if (bArr[10] == 3 && !CameraNewActivity.isDevicePass) {
                                                    boolean unused = CameraNewActivity.isDevicePass = true;
                                                    CameraNewActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.12.1.4
                                                        @Override // java.lang.Runnable
                                                        public void run() {
                                                            CameraNewActivity.this.timer = new Timer(true);
                                                            CameraNewActivity.this.timer.schedule(CameraNewActivity.this.new MyTimerTask(), 0L, 1000L);
                                                        }
                                                    });
                                                }
                                            } else {
                                                Log.e("Recv", "拍照");
                                                CameraNewActivity.this.take_photo.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.12.1.2
                                                    @Override // java.lang.Runnable
                                                    public void run() {
                                                        CameraNewActivity.this.take_photo.callOnClick();
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
                                    CameraNewActivity.this.startTcp("接收出错重开始");
                                    Log.e("SocketError", "!!!Socket Closed!!!");
                                    return;
                                }
                            }
                        }
                    });
                    CameraNewActivity.this.mTcpRecv.start();
                    CameraNewActivity.this.sendCmd(new byte[]{71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, -112, 0, 0, 0, 0, 1});
                    CameraNewActivity.this.sendCmd(new byte[]{71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 0, 0, 0, 0, 0, 1});
                    return;
                }
                Log.e("SocketError", "Socket NotConntcted!!!");
                CameraNewActivity.this.mInputStream = null;
                CameraNewActivity.this.mOutputStream = null;
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                CameraNewActivity.this.startTcp("侦测连接不上重开始");
            } catch (Exception e3) {
                Log.e("SocketError", "Try Error!!!");
                e3.printStackTrace();
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                CameraNewActivity.this.startTcp("主连接出错重开始");
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
            CameraNewActivity.this.tryPlay();
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
                runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.13
                    @Override // java.lang.Runnable
                    public void run() {
                        if (finalZ) {
                            Toast.makeText(CameraNewActivity.this, "Replay.", 0).show();
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("m_tvSpeed = ");
                        sb.append(valueOf);
                        sb.append("  naStatus = ");
                        ffmpegWrapper.getInstance();
                        sb.append(ffmpegWrapper.naStatus());
                        Log.e(CameraNewActivity.TAG, sb.toString());
                        int ordinal2 = ffmpegWrapper.ePlayerStatus.E_PlayerStatus_Playing.ordinal();
                        ffmpegWrapper.getInstance();
                        if (ordinal2 != ffmpegWrapper.naStatus()) {
                            CameraNewActivity.this.setPlayStatus(false);
                        } else {
                            CameraNewActivity.this.setPlayStatus(true);
                        }
                    }
                });
                return;
            }
            runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.14
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(CameraNewActivity.this, "Not WIFI!", 0).show();
                }
            });
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlayStatus(boolean z) {
        bCheckConnectStatus = z;
        if (z) {
            this.ivBroken.setVisibility(8);
            boolean z2 = this.mSaveVideo;
        } else {
            this.ivBroken.setVisibility(0);
            checkStopRecode();
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
        String str = this.strSaveDirectory + "/Photo/Screen/" + getTime() + UVCCameraHelper.SUFFIX_JPEG;
        ffmpegWrapper.getInstance();
        ffmpegWrapper.naSaveSnapshot(str, this.mSaveType);
        Toast.makeText(this, R.string.capture_successfully, 0).show();
        sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str)));
    }

    private int stopRecode(boolean z) {
        this.handler.removeCallbacks(this.updateTimer);
        this.tv_record_time.setVisibility(4);
        this.tv_record_time.setText("00:00:00");
        this.iv_record.setImageResource(R.mipmap.icon_record);
        if (z || this.mSaveType < 3) {
            ffmpegWrapper.getInstance();
            return ffmpegWrapper.naStopSaveVideo();
        }
        this.isDelayStopVideo = true;
        new Timer().schedule(new TimerTask() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.16
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (CameraNewActivity.this.isDelayStopVideo) {
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
            int naSaveVideo = ffmpegWrapper.naSaveVideo(this.strSaveDirectory + "/Video/" + time, this.mSaveType);
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + this.strSaveDirectory + "/Video/" + time + UVCCameraHelper.SUFFIX_MP4)));
            byte[] bArr = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 3, 1, 0, 0, 0, 0};
            bArr[17] = (byte) naSaveVideo;
            sendCmd(bArr);
            this.tv_record_time.setVisibility(0);
            this.iv_record.setImageResource(R.mipmap.icon_record_stop);
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
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.17
            @Override // java.lang.Runnable
            public void run() {
                if (CameraNewActivity.this.mOutputStream != null && CameraNewActivity.this.mSocket != null && CameraNewActivity.this.mSocket.isConnected()) {
                    try {
                        CameraNewActivity.this.mOutputStream.write(bArr);
                        CameraNewActivity.this.mOutputStream.flush();
                        Log.e("Socket-sendCmd", CameraNewActivity.bytesToHex_HasSpace(bArr, 0, 0));
                        CameraNewActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                            }
                        });
                        CameraNewActivity.this.SendRet = 1;
                        return;
                    } catch (Exception e) {
                        Log.e("Socket-sendCmd", "发送出错:" + CameraNewActivity.bytesToHex_HasSpace(bArr, 0, 0));
                        e.printStackTrace();
                        return;
                    }
                }
                Log.e("Socket-sendCmd", "未连接Socket:" + CameraNewActivity.bytesToHex_HasSpace(bArr, 0, 0));
                CameraNewActivity.this.SendRet = -1;
            }
        }).start();
        return 1;
    }

    public void exit() {
        Timer timer = new Timer();
        this.exitTimer = timer;
        timer.schedule(new TimerTask() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.19
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                CameraNewActivity.isHave = false;
                try {
                    Thread.sleep(8000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean z = CameraNewActivity.isHave;
            }
        }, 200L, 8500L);
    }

    private void initGL2JNIVIEW() {
        if (!isOff && !this.isS) {
            this.isS = true;
        }
        if (isOff) {
            new RelativeLayout.LayoutParams((this.mScreenWidth * 99) / 100, this.mScreenHigth).leftMargin = this.mScreenWidth / 200;
        }
    }

    private void initView() {
        this.buttonW = this.mScreenWidth / 13;
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
        new Thread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.20
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CameraNewActivity.this.runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.20.1
                    @Override // java.lang.Runnable
                    public void run() {
                        WindowManager windowManager2 = (WindowManager) CameraNewActivity.this.getSystemService("window");
                        int width2 = windowManager2.getDefaultDisplay().getWidth();
                        int height2 = windowManager2.getDefaultDisplay().getHeight();
                        ViewGroup.LayoutParams layoutParams2 = CameraNewActivity.this.mSurfaceView.getLayoutParams();
                        if (CameraNewActivity.this.mWidth < 0 || CameraNewActivity.this.mHeight < 0) {
                            Log.e("Scale", "-Down-,mWidthStart:" + CameraNewActivity.this.mWidth + ",mHeightStart:" + CameraNewActivity.this.mHeight);
                            CameraNewActivity.this.mWidth = width2;
                            CameraNewActivity.this.mHeight = height2;
                        }
                        Log.e("Scale", "-Down-,mWidthOld:" + CameraNewActivity.this.mWidth + ",mHeightOld:" + CameraNewActivity.this.mHeight + ",mScale:" + CameraNewActivity.this.mScale);
                        CameraNewActivity.this.mScale = CameraNewActivity.this.mScale + 0.15f;
                        layoutParams2.width = (int) (((float) CameraNewActivity.this.mWidth) * CameraNewActivity.this.mScale);
                        Log.e("Scale", "-Down-,mWidthNew:" + layoutParams2.width + ",mHeightNew:" + layoutParams2.height + ",mScale:" + CameraNewActivity.this.mScale);
                        CameraNewActivity.this.mSurfaceView.setPivotX((float) (width2 / 2));
                        CameraNewActivity.this.mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams((int) (((float) CameraNewActivity.this.mWidth) * CameraNewActivity.this.mScale), (int) (((float) CameraNewActivity.this.mHeight) * CameraNewActivity.this.mScale), 17));
                    }
                });
            }
        }).start();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnLeftMenu || id == R.id.btnSwitch) {
            return;
        }
        if (id == 3) {
            isT = false;
            isT2 = false;
            finish();
            return;
        }
        if (id == 4) {
            byte[] bArr = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 1};
            if (isDevicePass) {
                sendCmd(bArr);
                return;
            }
            return;
        }
        if (id == 5) {
            byte[] bArr2 = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 2};
            if (isDevicePass) {
                sendCmd(bArr2);
                return;
            }
            return;
        }
        if (id == 6) {
            byte[] bArr3 = {71, 80, 83, 79, 67, 75, 69, 84, 16, 0, 4, 1, 1, 0, 0, 0, 0, 3};
            if (isDevicePass) {
                sendCmd(bArr3);
                return;
            }
            return;
        }
        if (id == 7) {
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
            return;
        }
        if (id != 8) {
            if (id == 9) {
                this.isImgTipShow = !this.isImgTipShow;
                return;
            }
            if (id == 1) {
                pressCapture(view);
                return;
            } else if (id == 2) {
                pressRecord(view);
                return;
            } else {
                if (id == 10) {
                    startActivity(new Intent(this, (Class<?>) GalleryActivity.class));
                    return;
                }
                return;
            }
        }
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
        this.handler.postDelayed(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.CameraNewActivity.22
            @Override // java.lang.Runnable
            public void run() {
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
            CameraNewActivity.this.handler.sendEmptyMessage(100);
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
