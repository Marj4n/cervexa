package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.generalplus.GoPlusDrone.R;
import java.io.File;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: classes.dex */
public class MenuActivity extends Activity {
    private static final int ID_CAM = 102;
    private static final int ID_FILE = 103;
    private static final int ID_HELP = 101;
    private Bitmap bitmap;
    private AlertDialog dialog;
    private int downX;
    private LinearLayout layout;
    private int mScreenHigth;
    private int mScreenWidth;
    private int upX;
    private WifiInfo wifiInfo;
    private WifiManager wifiManager;
    int n = 0;
    int[] images1 = {R.drawable.main_menu_help_1, R.drawable.main_menu_preview_1, R.drawable.main_menu_file_1, R.drawable.main_menu_setting_1};
    int[] images2 = {R.drawable.main_menu_help_2, R.drawable.main_menu_preview_2, R.drawable.main_menu_file_2, R.drawable.main_menu_setting_2};
    private int selectedTag = 0;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.menu);
        this.layout = (LinearLayout) findViewById(R.id.linlay);
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        this.mScreenHigth = getResources().getDisplayMetrics().heightPixels;
        setBitmap();
        initView();
        if (!shouldAskPermission() || ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
    }

    private void initView() {
        int i = this.mScreenWidth;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((i * 2) / 10, (i * 2) / 10);
        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.main_menu_help_1);
        button.setId(101);
        this.layout.addView(button, layoutParams);
        int i2 = this.mScreenWidth;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams((i2 * 2) / 10, (i2 * 2) / 10);
        layoutParams2.leftMargin = (this.mScreenWidth * 1) / 10;
        Button button2 = new Button(this);
        button2.setBackgroundResource(R.drawable.main_menu_preview_1);
        button2.setId(102);
        this.layout.addView(button2, layoutParams2);
        int i3 = this.mScreenWidth;
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((i3 * 2) / 10, (i3 * 2) / 10);
        layoutParams3.leftMargin = (this.mScreenWidth * 1) / 10;
        Button button3 = new Button(this);
        button3.setBackgroundResource(R.drawable.main_menu_file_1);
        button3.setId(103);
        this.layout.addView(button3, layoutParams3);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuActivity.this.startActivity(new Intent(MenuActivity.this, (Class<?>) HelpActivity.class));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("IsCard", false);
                intent.putExtras(bundle);
                intent.setClass(MenuActivity.this, CameraActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, GalleryActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });
    }

    private void setBitmap() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        int ceil = (int) Math.ceil(i3 / i);
        int ceil2 = (int) Math.ceil(i4 / i2);
        if (ceil > 1 || ceil2 > 1) {
            if (ceil <= ceil2) {
                ceil = ceil2;
            }
            options.inSampleSize = ceil;
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inInputShareable = true;
        options.inPurgeable = true;
        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_menu_bg, options);
        this.layout.setBackgroundDrawable(new BitmapDrawable(getResources(), this.bitmap));
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.bitmap == null) {
            setBitmap();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.bitmap.isRecycled() || this.bitmap == null) {
            return;
        }
        this.bitmap = null;
    }

    protected View getFileView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 2) / 3);
        layoutParams.gravity = 17;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackgroundResource(R.drawable.file_style);
        linearLayout.setOrientation(1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 3) / 15);
        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams2);
        textView.setGravity(17);
        textView.setText(R.string.language5);
        textView.setTextColor(-16645630);
        textView.setTextSize((this.mScreenWidth * 4) / 200);
        linearLayout.addView(textView);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 1) / FTPReply.FILE_STATUS_OK);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(layoutParams3);
        imageView.setBackgroundResource(R.drawable.lin1);
        linearLayout.addView(imageView);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 2) / 15);
        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(layoutParams4);
        textView2.setGravity(17);
        textView2.setText(R.string.language6);
        textView2.setTextColor(-16645630);
        textView2.setTextSize((this.mScreenWidth * 3) / 200);
        linearLayout.addView(textView2);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 1) / FTPReply.FILE_STATUS_OK);
        ImageView imageView2 = new ImageView(this);
        imageView2.setLayoutParams(layoutParams5);
        imageView2.setBackgroundResource(R.drawable.lin1);
        linearLayout.addView(imageView2);
        LinearLayout.LayoutParams layoutParams6 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 2) / 15);
        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(layoutParams6);
        textView3.setGravity(17);
        textView3.setText(R.string.language7);
        textView3.setTextColor(-16645630);
        textView3.setTextSize((this.mScreenWidth * 3) / 200);
        linearLayout.addView(textView3);
        LinearLayout.LayoutParams layoutParams7 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 1) / FTPReply.FILE_STATUS_OK);
        ImageView imageView3 = new ImageView(this);
        imageView3.setLayoutParams(layoutParams7);
        imageView3.setBackgroundResource(R.drawable.lin1);
        linearLayout.addView(imageView3);
        LinearLayout.LayoutParams layoutParams8 = new LinearLayout.LayoutParams((this.mScreenWidth * 3) / 7, (this.mScreenHigth * 2) / 15);
        TextView textView4 = new TextView(this);
        textView4.setLayoutParams(layoutParams8);
        textView4.setGravity(17);
        textView4.setText(R.string.language8);
        textView4.setTextColor(-16645630);
        textView4.setTextSize((this.mScreenWidth * 3) / 200);
        linearLayout.addView(textView4);
        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuActivity.this.dialog.dismiss();
                MenuActivity.this.isHaveFile("/camera");
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuActivity.this.dialog.dismiss();
                MenuActivity.this.isHaveFile("/video");
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() { // from class: com.generalplus.GoPlusDrone.Activity.MenuActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MenuActivity.this.dialog.dismiss();
            }
        });
        return linearLayout;
    }

    public void isHaveFile(String str) {
        String str2 = (Environment.getExternalStorageDirectory().toString() + "/hvcam") + str;
        Log.e("HVCAM", str2);
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(str2);
            if (file.exists()) {
                return;
            }
            file.mkdirs();
            return;
        }
        Toast.makeText(this, getResources().getString(R.string.language16), 1).show();
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 200) {
            return;
        }
        if ((iArr[0] == 0) || !shouldAskPermission()) {
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
    }

    private boolean shouldAskPermission() {
        return Build.VERSION.SDK_INT > 22;
    }

    class MyAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyAdapter() {
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return Integer.valueOf(MenuActivity.this.images2[i]);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(MenuActivity.this);
            imageView.setBackgroundResource(MenuActivity.this.images2[i % 4]);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new Gallery.LayoutParams(MenuActivity.this.mScreenWidth / 7, MenuActivity.this.mScreenWidth / 7));
            return imageView;
        }
    }
}
