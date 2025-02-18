package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.generalplus.GoPlusDrone.R;
import com.google.android.material.timepicker.TimeModel;
import com.jiangdg.usbcamera.UVCCameraHelper;
import com.jieli.stream.dv.running2.util.IConstant;
import generalplus.com.GPCamLib.CamWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import kotlin.UByte;

/* loaded from: classes.dex */
public class FilesActivity extends Activity {
    public static final int FileFlag_AVIStreaming = 1;
    public static final int FileFlag_JPGStreaming = 2;
    public static final int FileFlag_LocalFile = 3;
    public static final int FileFlag_Unknown = 0;
    private static final int FileTag_FileBroken = 166;
    public static final int FileTag_FileCount = 0;
    private static final int FileTag_FileDeviceInit = 160;
    private static final int FileTag_FileDeviceReady = 161;
    private static final int FileTag_FileDownload = 164;
    private static final int FileTag_FileGettingThumbnail = 162;
    private static final int FileTag_FileGotThumbnail = 163;
    private static final int FileTag_FileGotThumbnailEnd = 167;
    public static final int FileTag_FileName = 1;
    private static final int FileTag_FilePalying = 165;
    private static final String TAG = "FilesActivity";
    private static boolean _bSetModeDone = false;
    private static int _i32GettingThumbnailFileIndex = -1;
    private static boolean bIsStopDownload = false;
    private static boolean bIsStopUpdateThumbnail = false;
    private static boolean bSaveImageItem = false;
    private static ArrayList<HashMap<String, Object>> listImageItem = null;
    private static ProgressDialog m_DownloadDialog = null;
    private static FilesActivity m_FilesActivityInstance = null;
    private static Thread m_UpdateGridVierThread = null;
    private static Thread m_UpdateThumbnailThread = null;
    private static boolean m_bPendingGetThumbnail = false;
    private static boolean m_bRunCreateGridViewDone = false;
    private static int[] m_i32AryFileSize;
    private static int[] m_i32AryFileStatus;
    private static String[] m_strAryFileName;
    private static String[] m_strAryFilePath;
    private static String[] m_strAryFileTime;
    private long mLastClickTime;
    private Context m_Context;
    private GridView m_Gridview;
    private SimpleAdapter m_saImageItems;
    private Handler m_handler = null;
    private boolean _bUserLeaveHint = true;
    private boolean bIsCopingFile = false;
    private int i32GetThumbnailCount = 0;
    private String strDevicePICLocation = "";
    private int _i32CommandIndex = 0;
    private int _i32ErrorCount = 0;
    private int _i32WaitGettingThumbnailCount = 0;
    private int _i32GotThumbnailFileIndex = -1;
    private int _i32SelectedFirstItem = -1;
    private int _firstVisibleItem = 0;
    private int _scrollState = 0;
    private CharSequence[] CharSequenceItemsDefault = {"Play", IConstant.DIR_DOWNLOAD, "Info"};
    private CharSequence[] CharSequenceItemsDelete = {"Play", "Delete", "Info"};
    private Handler m_FromWrapperHandler = new Handler() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0) {
                return;
            }
            FilesActivity.this.ParseGPCamStatus(message.getData());
        }
    };

    static /* synthetic */ int access$3108(FilesActivity filesActivity) {
        int i = filesActivity._i32WaitGettingThumbnailCount;
        filesActivity._i32WaitGettingThumbnailCount = i + 1;
        return i;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_filelist);
        this.m_Context = this;
        getWindow().addFlags(128);
        this.strDevicePICLocation = Environment.getExternalStorageDirectory().getPath() + CamWrapper.SaveFileToDevicePath;
        if (this.m_handler == null) {
            this.m_handler = new Handler();
        }
        GridView gridView = (GridView) findViewById(R.id.gridView);
        this.m_Gridview = gridView;
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.1
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                FilesActivity.this._scrollState = i;
                if (i != 0 || FilesActivity.this._i32SelectedFirstItem == FilesActivity.this._firstVisibleItem) {
                    return;
                }
                FilesActivity filesActivity = FilesActivity.this;
                filesActivity._i32SelectedFirstItem = filesActivity._firstVisibleItem;
                FilesActivity.this.m_Gridview.setSelection(FilesActivity.this._i32SelectedFirstItem);
                boolean unused = FilesActivity.m_bRunCreateGridViewDone = false;
                if (FilesActivity.m_UpdateThumbnailThread != null) {
                    FilesActivity.m_UpdateThumbnailThread.interrupt();
                    Thread unused2 = FilesActivity.m_UpdateThumbnailThread = null;
                }
                Thread unused3 = FilesActivity.m_UpdateThumbnailThread = new Thread(FilesActivity.this.new UpdateThumbnailRunnable());
                FilesActivity.m_UpdateThumbnailThread.start();
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                FilesActivity.this._firstVisibleItem = i;
                Log.d("tag", "onScroll = " + i);
            }
        });
        this.m_Gridview.setOnItemClickListener(new AnonymousClass2());
        if (!bSaveImageItem) {
            if (m_i32AryFileStatus != null) {
                m_i32AryFileStatus = null;
            }
            if (m_strAryFileName != null) {
                m_strAryFileName = null;
            }
            if (m_strAryFilePath != null) {
                m_strAryFilePath = null;
            }
            if (m_i32AryFileSize != null) {
                m_i32AryFileSize = null;
            }
            if (m_strAryFileTime != null) {
                m_strAryFileTime = null;
            }
            if (listImageItem == null) {
                listImageItem = new ArrayList<>();
            }
            listImageItem.clear();
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _bSetModeDone = false;
            CamWrapper.getComWrapperInstance().GPCamSendSetMode(2);
            CamWrapper.getComWrapperInstance().GPCamSendGetFullFileList();
            return;
        }
        _bSetModeDone = false;
        CamWrapper.getComWrapperInstance().GPCamSendSetMode(2);
        CamWrapper.getComWrapperInstance().GPCamSendGetFullFileList();
        m_bRunCreateGridViewDone = false;
        if (m_UpdateThumbnailThread == null) {
            Thread thread = new Thread(new UpdateThumbnailRunnable());
            m_UpdateThumbnailThread = thread;
            thread.start();
        }
    }

    /* renamed from: com.generalplus.GoPlusDrone.Activity.FilesActivity$2, reason: invalid class name */
    class AnonymousClass2 implements AdapterView.OnItemClickListener {
        private AdapterView m_Paramet;
        private int m_i32Position;
        private long m_i64ID;
        private String strStreamFilePath = "";
        private CharSequence[] SetCharSequenceItems = null;

        AnonymousClass2() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            this.m_Paramet = adapterView;
            this.m_i32Position = i;
            this.m_i64ID = j;
            if (FilesActivity.m_i32AryFileStatus[this.m_i32Position] == 163 || FilesActivity.m_i32AryFileStatus[this.m_i32Position] == 167) {
                this.SetCharSequenceItems = FilesActivity.this.CharSequenceItemsDefault;
                this.strStreamFilePath = "";
                File file = new File(FilesActivity.this.strDevicePICLocation + FilesActivity.m_strAryFileName[this.m_i32Position]);
                if (file.exists() && file.length() / 1024 == FilesActivity.m_i32AryFileSize[this.m_i32Position]) {
                    this.SetCharSequenceItems = FilesActivity.this.CharSequenceItemsDelete;
                    this.strStreamFilePath = FilesActivity.this.strDevicePICLocation + FilesActivity.m_strAryFileName[this.m_i32Position];
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(FilesActivity.this.m_Context);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.cancel();
                    }
                });
                builder.setItems(this.SetCharSequenceItems, new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.2.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        if (AnonymousClass2.this.SetCharSequenceItems[i2].toString().contentEquals("Play")) {
                            FilesActivity.this._bUserLeaveHint = false;
                            boolean unused = FilesActivity.bIsStopUpdateThumbnail = true;
                            boolean unused2 = FilesActivity.bSaveImageItem = true;
                            if (!FilesActivity.m_strAryFileName[AnonymousClass2.this.m_i32Position].toString().contains(UVCCameraHelper.SUFFIX_JPEG) || AnonymousClass2.this.strStreamFilePath.isEmpty()) {
                                return;
                            }
                            ImageView imageView = new ImageView(FilesActivity.this.m_Context);
                            imageView.setImageURI(Uri.parse(AnonymousClass2.this.strStreamFilePath));
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(FilesActivity.this.m_Context);
                            builder2.setView(imageView);
                            builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.2.2.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialogInterface2, int i3) {
                                    boolean unused3 = FilesActivity.bIsStopUpdateThumbnail = false;
                                }
                            });
                            builder2.show();
                            return;
                        }
                        if (AnonymousClass2.this.SetCharSequenceItems[i2].toString().contentEquals(IConstant.DIR_DOWNLOAD)) {
                            boolean unused3 = FilesActivity.bIsStopDownload = false;
                            boolean unused4 = FilesActivity.m_bPendingGetThumbnail = true;
                            if (FilesActivity.m_DownloadDialog == null) {
                                ProgressDialog unused5 = FilesActivity.m_DownloadDialog = new ProgressDialog(FilesActivity.this.m_Context);
                                FilesActivity.m_DownloadDialog.setMessage("Prepare to download ...");
                                FilesActivity.m_DownloadDialog.setCanceledOnTouchOutside(false);
                                FilesActivity.m_DownloadDialog.setMax(100);
                                FilesActivity.m_DownloadDialog.setProgressStyle(1);
                                FilesActivity.m_DownloadDialog.setButton(-2, "Abort", new DialogInterface.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.2.2.2
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface2, int i3) {
                                        if (!FilesActivity.this.bIsCopingFile) {
                                            new File(FilesActivity.this.strDevicePICLocation + FilesActivity.m_strAryFileName[AnonymousClass2.this.m_i32Position]).delete();
                                        }
                                        ProgressDialog unused6 = FilesActivity.m_DownloadDialog = null;
                                        boolean unused7 = FilesActivity.bIsStopDownload = true;
                                    }
                                });
                                FilesActivity.m_DownloadDialog.show();
                            }
                            CamWrapper.getComWrapperInstance().GPCamSendGetFileRawdata(AnonymousClass2.this.m_i32Position);
                            return;
                        }
                        if (AnonymousClass2.this.SetCharSequenceItems[i2].toString().contentEquals("Info")) {
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(FilesActivity.this.m_Context);
                            String str = "Name: " + FilesActivity.m_strAryFileName[AnonymousClass2.this.m_i32Position] + "\nTime: " + ("20" + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(0, 2) + "/" + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(2, 4) + "/" + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(4, 6) + " " + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(6, 8) + ":" + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(8, 10) + ":" + FilesActivity.m_strAryFileTime[AnonymousClass2.this.m_i32Position].substring(10, 12)) + "\nSize: " + String.valueOf(FilesActivity.m_i32AryFileSize[AnonymousClass2.this.m_i32Position]);
                            builder3.setTitle("Info");
                            builder3.setMessage(str);
                            builder3.setCancelable(true);
                            builder3.show();
                            return;
                        }
                        if (AnonymousClass2.this.SetCharSequenceItems[i2].toString().contentEquals("Delete")) {
                            File file2 = new File(FilesActivity.this.strDevicePICLocation + FilesActivity.m_strAryFileName[AnonymousClass2.this.m_i32Position]);
                            file2.delete();
                            FilesActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + file2.getAbsolutePath())));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    public static FilesActivity getInstance() {
        return m_FilesActivityInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void UpdateGridView() {
        runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.3
            @Override // java.lang.Runnable
            public void run() {
                if (FilesActivity.this.m_saImageItems == null) {
                    if (FilesActivity.listImageItem == null) {
                        return;
                    }
                    FilesActivity.this.m_saImageItems = new SimpleAdapter(FilesActivity.this.m_Context, FilesActivity.listImageItem, R.layout.files_program_list, new String[]{"ThumbnailFilePath", "FileName", CamWrapper.GPFILECALLBACKTYPE_FILEINDEX}, new int[]{R.id.imageView1, R.id.textView1});
                    FilesActivity.this.m_Gridview.setAdapter((ListAdapter) FilesActivity.this.m_saImageItems);
                }
                FilesActivity.this.m_saImageItems.notifyDataSetChanged();
            }
        });
    }

    class UpdateGridViewRunnable implements Runnable {
        UpdateGridViewRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.e(FilesActivity.TAG, "UpdateGridViewRunnable ...");
            while (FilesActivity.m_UpdateThumbnailThread != null) {
                FilesActivity.this.m_handler.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.UpdateGridViewRunnable.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (FilesActivity.listImageItem.size() > 0) {
                            FilesActivity.this.UpdateGridView();
                        }
                    }
                });
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            FilesActivity.this.m_handler.post(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.UpdateGridViewRunnable.2
                @Override // java.lang.Runnable
                public void run() {
                    if (FilesActivity.listImageItem.size() > 0) {
                        FilesActivity.this.UpdateGridView();
                    }
                }
            });
            Log.e(FilesActivity.TAG, "UpdateGridViewRunnable ... Done");
            Thread unused = FilesActivity.m_UpdateGridVierThread = null;
        }
    }

    class UpdateThumbnailRunnable implements Runnable {
        UpdateThumbnailRunnable() {
            Log.e(FilesActivity.TAG, "Create UpdateThumbnailRunnable ... ");
            if (FilesActivity.m_UpdateGridVierThread == null) {
                Thread unused = FilesActivity.m_UpdateGridVierThread = new Thread(FilesActivity.this.new UpdateGridViewRunnable());
                FilesActivity.m_UpdateGridVierThread.start();
            }
            boolean unused2 = FilesActivity.bIsStopUpdateThumbnail = false;
            boolean unused3 = FilesActivity.m_bPendingGetThumbnail = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:49:0x006a, code lost:
        
            com.generalplus.GoPlusDrone.Activity.FilesActivity.access$3108(r9.this$0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0077, code lost:
        
            if (r9.this$0._i32WaitGettingThumbnailCount <= 100) goto L44;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x0079, code lost:
        
            r9.this$0._i32WaitGettingThumbnailCount = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0086, code lost:
        
            if (generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().getIsNewFile() == false) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x0090, code lost:
        
            if ((-1) != generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().GPCamGetFileIndex(r2)) goto L43;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x0092, code lost:
        
            generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().GPCamSetNextPlaybackFileListIndex(r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x0099, code lost:
        
            java.lang.Thread.sleep(2000);
         */
        /* JADX WARN: Code restructure failed: missing block: B:59:0x009d, code lost:
        
            r1 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x009e, code lost:
        
            r1.printStackTrace();
         */
        /* JADX WARN: Code restructure failed: missing block: B:70:0x0187, code lost:
        
            if (generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().getIsNewFile() == false) goto L69;
         */
        /* JADX WARN: Code restructure failed: missing block: B:72:0x0191, code lost:
        
            if ((-1) != generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().GPCamGetFileIndex(r2)) goto L69;
         */
        /* JADX WARN: Code restructure failed: missing block: B:73:0x0193, code lost:
        
            generalplus.com.GPCamLib.CamWrapper.getComWrapperInstance().GPCamSetNextPlaybackFileListIndex(r2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:75:0x019a, code lost:
        
            java.lang.Thread.sleep(2000);
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x019e, code lost:
        
            r1 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:78:0x019f, code lost:
        
            r1.printStackTrace();
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 484
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.generalplus.GoPlusDrone.Activity.FilesActivity.UpdateThumbnailRunnable.run():void");
        }
    }

    private boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - this.mLastClickTime;
        if (0 < j && j < 2000) {
            return true;
        }
        this.mLastClickTime = currentTimeMillis;
        return false;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed ...");
        if (_bSetModeDone && !isFastClick()) {
            if (m_UpdateThumbnailThread != null || m_UpdateGridVierThread != null) {
                bIsStopUpdateThumbnail = true;
                try {
                    Thread.sleep(800L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            m_UpdateThumbnailThread = null;
            m_UpdateGridVierThread = null;
            bSaveImageItem = false;
            CamWrapper.getComWrapperInstance().GPCamClearCommandQueue();
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        Log.e(TAG, "onResume ...");
        super.onResume();
        bIsStopUpdateThumbnail = false;
        CamWrapper.getComWrapperInstance().SetViewHandler(this.m_FromWrapperHandler, 2);
        if (m_i32AryFileStatus == null || m_UpdateThumbnailThread != null) {
            return;
        }
        Thread thread = new Thread(new UpdateThumbnailRunnable());
        m_UpdateThumbnailThread = thread;
        thread.start();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        Log.e(TAG, "onDestroy ...");
        m_DownloadDialog = null;
        if (!bSaveImageItem) {
            deleteDir(new File(Environment.getExternalStorageDirectory().getPath() + "/" + CamWrapper.CamDefaulFolderName));
            bIsStopDownload = true;
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        Log.e(TAG, "onUserLeaveHint ...");
        bIsStopDownload = true;
        if (this._bUserLeaveHint) {
            CamWrapper.getComWrapperInstance().GPCamSendSetMode(0);
            finish();
        }
        this._bUserLeaveHint = true;
        super.onUserLeaveHint();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private void copyFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + file.getAbsolutePath())));
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e2) {
            Log.e(TAG, e2.getMessage());
        }
    }

    public void clearApplicationData() {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.clear();
        edit.commit();
        File file = new File(getCacheDir().getParent());
        if (file.exists()) {
            for (String str : file.list()) {
                if (!str.equals("lib")) {
                    deleteDir(new File(file, str));
                }
            }
        }
    }

    public boolean deleteDir(File file) {
        if (file != null && file.isDirectory()) {
            String[] list = file.list();
            for (int i = 0; i < list.length; i++) {
                if (!list[i].toString().contentEquals(CamWrapper.ParameterFileName) && !list[i].toString().contains("Crash") && !list[i].toString().contains("Logcat") && !list[i].toString().contains(CamWrapper.SaveLogFileName) && !list[i].toString().contentEquals(CamWrapper.ConfigFileName) && !deleteDir(new File(file, list[i]))) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ParseGPCamStatus(Bundle bundle) {
        int i;
        String[] strArr;
        int i2 = bundle.getInt(CamWrapper.GPCALLBACKSTATUSTYPE_CMDINDEX);
        int i3 = bundle.getInt(CamWrapper.GPCALLBACKSTATUSTYPE_CMDTYPE);
        int i4 = bundle.getInt(CamWrapper.GPCALLBACKSTATUSTYPE_CMDMODE);
        int i5 = bundle.getInt(CamWrapper.GPCALLBACKSTATUSTYPE_CMDID);
        bundle.getInt(CamWrapper.GPCALLBACKSTATUSTYPE_DATASIZE);
        byte[] byteArray = bundle.getByteArray(CamWrapper.GPCALLBACKSTATUSTYPE_DATA);
        int i6 = 0;
        if (i3 != 2) {
            if (i3 == 3) {
                int i7 = (byteArray[0] & UByte.MAX_VALUE) + ((byteArray[1] & UByte.MAX_VALUE) << 8);
                switch (i7) {
                    case CamWrapper.Error_LostConnection /* 65472 */:
                        Log.e(TAG, "Error_LostConnection ...");
                        FinishToMainController();
                        return;
                    case CamWrapper.Error_SocketClosed /* 65473 */:
                        Log.e(TAG, "Error_SocketClosed ... ");
                        FinishToMainController();
                        return;
                    default:
                        switch (i7) {
                            case CamWrapper.Error_FullStorage /* 65527 */:
                                Log.e(TAG, "Error_FullStorage ... ");
                                return;
                            case CamWrapper.Error_GetThumbnailFail /* 65528 */:
                                Log.e(TAG, "Error_GetThumbnailFail ... ");
                                synchronized (m_i32AryFileStatus) {
                                    if (this._i32GotThumbnailFileIndex + 1 < m_i32AryFileStatus.length && !bIsStopDownload) {
                                        m_i32AryFileStatus[this._i32GotThumbnailFileIndex + 1] = 166;
                                    }
                                }
                                return;
                            case CamWrapper.Error_GetFileListFail /* 65529 */:
                                Log.e(TAG, "Error_GetFileListFail ... ");
                                return;
                            case CamWrapper.Error_WriteFail /* 65530 */:
                                Log.e(TAG, "Error_WriteFail ... ");
                                return;
                            case CamWrapper.Error_NoStorage /* 65531 */:
                                Log.e(TAG, "Error_NoStorage ... ");
                                runOnUiThread(new Runnable() { // from class: com.generalplus.GoPlusDrone.Activity.FilesActivity.5
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(FilesActivity.this.m_Context, "Failed. No Storage.", 0).show();
                                    }
                                });
                                return;
                            case CamWrapper.Error_ModeError /* 65532 */:
                                Log.e(TAG, "Error_ModeError ... ");
                                return;
                            case CamWrapper.Error_RequestTimeOut /* 65533 */:
                                Log.e(TAG, "Error_RequestTimeOut ... ");
                                return;
                            case CamWrapper.Error_InvalidCommand /* 65534 */:
                                Log.e(TAG, "Error_InvalidCommand ... ");
                                m_bPendingGetThumbnail = false;
                                synchronized (m_i32AryFileStatus) {
                                    if (this._i32GotThumbnailFileIndex + 1 < m_i32AryFileStatus.length && !bIsStopDownload) {
                                        m_i32AryFileStatus[this._i32GotThumbnailFileIndex + 1] = 166;
                                    }
                                }
                                return;
                            case 65535:
                                Log.e(TAG, "Error_ServerIsBusy ... ");
                                int i8 = this._i32ErrorCount + 1;
                                this._i32ErrorCount = i8;
                                if (i8 > 20) {
                                    this._i32ErrorCount = 0;
                                    bIsStopDownload = true;
                                    CamWrapper.getComWrapperInstance().GPCamAbort(this._i32CommandIndex);
                                    CamWrapper.getComWrapperInstance().GPCamSendGetStatus();
                                    Toast.makeText(this.m_Context, "Getting thumbnail is failed.", 0).show();
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                }
            }
            return;
        }
        if (i4 == 0) {
            if (i5 != 0) {
                return;
            }
            _bSetModeDone = true;
            Log.e(TAG, "_bSetModeDone = true");
            return;
        }
        if (i4 == 1) {
            Log.e(TAG, "GPSOCK_MODE_Record ... ");
            return;
        }
        if (i4 == 2) {
            Log.e(TAG, "GPSOCK_MODE_CapturePicture ... ");
            return;
        }
        if (i4 != 3) {
            if (i4 == 4) {
                Log.e(TAG, "GPSOCK_MODE_Menu ... ");
                return;
            } else {
                if (i4 != 255) {
                    return;
                }
                Log.e(TAG, "GPSOCK_MODE_Vendor ... ");
                return;
            }
        }
        Log.e(TAG, "GPSOCK_MODE_Playback ... ");
        if (i5 == 2) {
            if (!bSaveImageItem && (i = ((byteArray[1] & 255) << 8) | (byteArray[0] & 255)) > 0) {
                _i32GettingThumbnailFileIndex = -1;
                this._i32GotThumbnailFileIndex = -1;
                this.i32GetThumbnailCount = 0;
                if (m_i32AryFileStatus != null) {
                    m_i32AryFileStatus = null;
                }
                int[] iArr = new int[i];
                m_i32AryFileStatus = iArr;
                Arrays.fill(iArr, FileTag_FileDeviceInit);
                if (m_strAryFileName != null) {
                    m_strAryFileName = null;
                }
                m_strAryFileName = new String[i];
                if (m_strAryFilePath != null) {
                    m_strAryFilePath = null;
                }
                m_strAryFilePath = new String[i];
                if (m_i32AryFileSize != null) {
                    m_i32AryFileSize = null;
                }
                m_i32AryFileSize = new int[i];
                if (m_strAryFileTime != null) {
                    m_strAryFileTime = null;
                }
                m_strAryFileTime = new String[i];
                m_bRunCreateGridViewDone = false;
                bIsStopDownload = false;
                while (i6 < i) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("ThumbnailFilePath", Integer.valueOf(R.mipmap.loading));
                    hashMap.put("FileName", "Unknown");
                    hashMap.put(CamWrapper.GPFILECALLBACKTYPE_FILEINDEX, String.valueOf(i6));
                    hashMap.put("FileSize", "0");
                    hashMap.put("FileTime", "0");
                    listImageItem.add(hashMap);
                    i6++;
                }
                UpdateGridView();
                return;
            }
            return;
        }
        if (i5 == 3) {
            if (m_i32AryFileStatus == null || (strArr = m_strAryFileName) == null || m_strAryFilePath == null || m_i32AryFileSize == null || m_strAryFileTime == null) {
                m_bRunCreateGridViewDone = true;
                return;
            }
            this._i32CommandIndex = i2;
            int i9 = (byteArray[0] & UByte.MAX_VALUE) + ((byteArray[1] & UByte.MAX_VALUE) << 8);
            int i10 = byteArray[2] & UByte.MAX_VALUE;
            if (bIsStopUpdateThumbnail) {
                CamWrapper.getComWrapperInstance().GPCamAbort(this._i32CommandIndex);
                CamWrapper.getComWrapperInstance().GPCamSendGetStatus();
                return;
            }
            if (i9 + i10 > strArr.length) {
                i10 = strArr.length - i9;
            }
            synchronized (m_i32AryFileStatus) {
                for (int i11 = i9; i11 < i10 + i9; i11++) {
                    byte[] bArr = new byte[6];
                    m_strAryFileName[i11] = CamWrapper.getComWrapperInstance().GPCamGetFileName(i11);
                    m_i32AryFileSize[i11] = CamWrapper.getComWrapperInstance().GPCamGetFileSize(i11);
                    CamWrapper.getComWrapperInstance().GPCamGetFileTime(i11, bArr);
                    StringBuilder sb = new StringBuilder();
                    for (int i12 = 0; i12 < 6; i12++) {
                        sb.append(String.format(TimeModel.ZERO_LEADING_NUMBER_FORMAT, Byte.valueOf(bArr[i12])));
                    }
                    m_strAryFileTime[i11] = sb.toString();
                    if (m_i32AryFileStatus[i11] == FileTag_FileDeviceInit) {
                        m_i32AryFileStatus[i11] = 161;
                    }
                }
            }
            m_bRunCreateGridViewDone = false;
            if (m_UpdateThumbnailThread == null) {
                Thread thread = new Thread(new UpdateThumbnailRunnable());
                m_UpdateThumbnailThread = thread;
                thread.start();
                return;
            }
            return;
        }
        if (i5 == 4) {
            if (m_i32AryFileStatus == null || m_strAryFileName == null || m_strAryFilePath == null || m_i32AryFileSize == null || m_strAryFileTime == null) {
                m_bRunCreateGridViewDone = true;
                return;
            }
            int i13 = (byteArray[0] & UByte.MAX_VALUE) + ((byteArray[1] & UByte.MAX_VALUE) << 8);
            int i14 = (byteArray[2] & 255) + ((byteArray[3] & 255) << 8);
            char[] cArr = new char[i14];
            cArr[0] = 0;
            while (i6 < i14) {
                cArr[i6] = (char) (byteArray[i6 + 4] & UByte.MAX_VALUE);
                i6++;
            }
            this._i32GotThumbnailFileIndex = i13;
            this._i32CommandIndex = i2;
            if (bIsStopUpdateThumbnail) {
                CamWrapper.getComWrapperInstance().GPCamAbort(this._i32CommandIndex);
                CamWrapper.getComWrapperInstance().GPCamSendGetStatus();
                return;
            }
            synchronized (m_i32AryFileStatus) {
                if (m_i32AryFileStatus[this._i32GotThumbnailFileIndex] == 162) {
                    this.i32GetThumbnailCount++;
                }
                m_strAryFilePath[this._i32GotThumbnailFileIndex] = String.valueOf(cArr);
                m_i32AryFileStatus[this._i32GotThumbnailFileIndex] = 163;
            }
            return;
        }
        if (i5 != 5) {
            return;
        }
        this._i32CommandIndex = i2;
        if (bIsStopDownload) {
            CamWrapper.getComWrapperInstance().GPCamAbort(this._i32CommandIndex);
            CamWrapper.getComWrapperInstance().GPCamSendGetStatus();
            bIsStopDownload = false;
            return;
        }
        if ((byteArray[0] & UByte.MAX_VALUE) == 1) {
            int i15 = (byteArray[1] & UByte.MAX_VALUE) + ((byteArray[2] & UByte.MAX_VALUE) << 8);
            int i16 = (byteArray[3] & 255) + ((byteArray[4] & 255) << 8);
            char[] cArr2 = new char[i16];
            cArr2[0] = 0;
            for (int i17 = 0; i17 < i16; i17++) {
                cArr2[i17] = (char) (byteArray[i17 + 5] & UByte.MAX_VALUE);
            }
            String valueOf = String.valueOf(cArr2);
            ProgressDialog progressDialog = m_DownloadDialog;
            if (progressDialog != null) {
                progressDialog.setMessage("Copy file, please wait.");
            }
            this.bIsCopingFile = true;
            copyFile(valueOf, this.strDevicePICLocation + "/" + m_strAryFileName[i15]);
            ProgressDialog progressDialog2 = m_DownloadDialog;
            if (progressDialog2 != null) {
                progressDialog2.dismiss();
                m_DownloadDialog = null;
            }
            m_bPendingGetThumbnail = false;
            return;
        }
        this.bIsCopingFile = false;
        float f = byteArray[1] & UByte.MAX_VALUE;
        ProgressDialog progressDialog3 = m_DownloadDialog;
        if (progressDialog3 != null) {
            progressDialog3.setMessage("Downloading ... ");
            m_DownloadDialog.setProgress((int) f);
        }
    }

    private void FinishToMainController() {
        Log.e(TAG, "Finish ...");
        CamWrapper.getComWrapperInstance().GPCamDisconnect();
        finish();
    }
}
