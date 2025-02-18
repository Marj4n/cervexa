package com.gizthon.camera.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.generalplus.GoPlusDrone.View.MultiTouchZoomableImageView;
import com.gizthon.camera.dialog.ShareDialog;
import com.jieli.stream.dv.running2.util.IConstant;
import com.weioa.GoPlusDrone.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PreviewPhotoActivity extends Activity {
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    private FullImageAdapter adapter;
    private ArrayList<String> m_ayFilePath = null;
    private ImageView shareView;
    private ViewPager viewPager;

    public static void start(Context context, ArrayList<String> arrayList, int i) {
        Intent intent = new Intent(context, (Class<?>) PreviewPhotoActivity.class);
        intent.putStringArrayListExtra("FilePath", arrayList);
        intent.putExtra(IConstant.KEY_POSITION, i);
        context.startActivity(intent);
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preview_photo_activity);
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.shareView = (ImageView) findViewById(R.id.iv_share);
        Intent intent = getIntent();
        final int i = intent.getExtras().getInt(IConstant.KEY_POSITION);
        ArrayList<String> stringArrayListExtra = intent.getStringArrayListExtra("FilePath");
        this.m_ayFilePath = stringArrayListExtra;
        FullImageAdapter fullImageAdapter = new FullImageAdapter(this, stringArrayListExtra);
        this.adapter = fullImageAdapter;
        this.viewPager.setAdapter(fullImageAdapter);
        this.viewPager.setCurrentItem(i);
        this.shareView.setOnClickListener(new View.OnClickListener() { // from class: com.gizthon.camera.activity.PreviewPhotoActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ShareDialog.newInstance().setOnClickListener(new ShareDialog.OnClickListener() { // from class: com.gizthon.camera.activity.PreviewPhotoActivity.1.1
                    @Override // com.gizthon.camera.dialog.ShareDialog.OnClickListener
                    public void shareWchat() throws FileNotFoundException {
                        Uri fromFile;
                        if (PreviewPhotoActivity.this.isInstallApp("com.tencent.mm")) {
                            ArrayList arrayList = new ArrayList();
                            Intent intent2 = new Intent();
                            intent2.setAction("android.intent.action.SEND");
                            if (PreviewPhotoActivity.this.getApplicationInfo().targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
                                fromFile = Uri.parse(MediaStore.Images.Media.insertImage(PreviewPhotoActivity.this.getContentResolver(), new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)).getAbsolutePath(), "pangu", (String) null));
                            } else {
                                fromFile = Uri.fromFile(new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)));
                            }
                            arrayList.add(fromFile);
                            intent2.setFlags(268435456);
                            intent2.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
                            intent2.setType("image/*");
                            intent2.addFlags(3);
                            intent2.putExtra("android.intent.extra.STREAM", fromFile);
                            PreviewPhotoActivity.this.startActivity(Intent.createChooser(intent2, "分享"));
                            return;
                        }
                        Toast.makeText(PreviewPhotoActivity.this, "您需要安装微信客户端", 1).show();
                    }

                    @Override // com.gizthon.camera.dialog.ShareDialog.OnClickListener
                    public void shareQq() throws FileNotFoundException {
                        Uri fromFile;
                        if (PreviewPhotoActivity.this.isInstallApp("com.tencent.mobileqq")) {
                            ArrayList arrayList = new ArrayList();
                            Intent intent2 = new Intent();
                            intent2.setAction("android.intent.action.SEND");
                            if (PreviewPhotoActivity.this.getApplicationInfo().targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
                                fromFile = Uri.parse(MediaStore.Images.Media.insertImage(PreviewPhotoActivity.this.getContentResolver(), new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)).getAbsolutePath(), "pangu", (String) null));
                            } else {
                                fromFile = Uri.fromFile(new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)));
                            }
                            arrayList.add(fromFile);
                            intent2.setFlags(268435456);
                            intent2.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
                            intent2.setType("image/*");
                            intent2.addFlags(3);
                            intent2.putExtra("android.intent.extra.STREAM", fromFile);
                            PreviewPhotoActivity.this.startActivity(Intent.createChooser(intent2, "分享"));
                            return;
                        }
                        Toast.makeText(PreviewPhotoActivity.this, "您需要安装QQ客户端", 1).show();
                    }

                    @Override // com.gizthon.camera.dialog.ShareDialog.OnClickListener
                    public void shareFecebook() throws FileNotFoundException {
                        Uri fromFile;
                        ArrayList arrayList = new ArrayList();
                        Intent intent2 = new Intent();
                        intent2.setAction("android.intent.action.SEND");
                        if (PreviewPhotoActivity.this.getApplicationInfo().targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
                            fromFile = Uri.parse(MediaStore.Images.Media.insertImage(PreviewPhotoActivity.this.getContentResolver(), new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)).getAbsolutePath(), "pangu", (String) null));
                        } else {
                            fromFile = Uri.fromFile(new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)));
                        }
                        arrayList.add(fromFile);
                        intent2.setFlags(268435456);
                        intent2.setComponent(new ComponentName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"));
                        intent2.setType("image/*");
                        intent2.addFlags(3);
                        intent2.putExtra("android.intent.extra.STREAM", fromFile);
                        PreviewPhotoActivity.this.startActivity(Intent.createChooser(intent2, "分享"));
                    }

                    @Override // com.gizthon.camera.dialog.ShareDialog.OnClickListener
                    public void shareTwiter() throws FileNotFoundException {
                        Uri fromFile;
                        ArrayList arrayList = new ArrayList();
                        Intent intent2 = new Intent();
                        intent2.setAction("android.intent.action.SEND");
                        if (PreviewPhotoActivity.this.getApplicationInfo().targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
                            fromFile = Uri.parse(MediaStore.Images.Media.insertImage(PreviewPhotoActivity.this.getContentResolver(), new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)).getAbsolutePath(), "pangu", (String) null));
                        } else {
                            fromFile = Uri.fromFile(new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)));
                        }
                        arrayList.add(fromFile);
                        intent2.setFlags(268435456);
                        intent2.setComponent(new ComponentName("com.twitter.android", "com.twitter.composer.ComposerActivity"));
                        intent2.setType("image/*");
                        intent2.addFlags(3);
                        intent2.putExtra("android.intent.extra.STREAM", fromFile);
                        PreviewPhotoActivity.this.startActivity(Intent.createChooser(intent2, "分享"));
                    }

                    @Override // com.gizthon.camera.dialog.ShareDialog.OnClickListener
                    public void shareYoutube() throws FileNotFoundException {
                        Uri fromFile;
                        ArrayList arrayList = new ArrayList();
                        Intent intent2 = new Intent();
                        intent2.setAction("android.intent.action.SEND");
                        if (PreviewPhotoActivity.this.getApplicationInfo().targetSdkVersion >= 24 && Build.VERSION.SDK_INT >= 24) {
                            fromFile = Uri.parse(MediaStore.Images.Media.insertImage(PreviewPhotoActivity.this.getContentResolver(), new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)).getAbsolutePath(), "pangu", (String) null));
                        } else {
                            fromFile = Uri.fromFile(new File((String) PreviewPhotoActivity.this.m_ayFilePath.get(i)));
                        }
                        arrayList.add(fromFile);
                        intent2.setFlags(268435456);
                        intent2.setComponent(new ComponentName("com.google.android.youtube", "com.google.android.apps.youtube.app.application.Shell_UploadActivity"));
                        intent2.setType("image/*");
                        intent2.addFlags(3);
                        intent2.putExtra("android.intent.extra.STREAM", fromFile);
                        PreviewPhotoActivity.this.startActivity(Intent.createChooser(intent2, "分享"));
                    }
                }).show(PreviewPhotoActivity.this);
            }
        });
    }

    public class FullImageAdapter extends PagerAdapter {
        private Activity _activity;
        private ArrayList<String> _imagePaths;
        private LayoutInflater inflater;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public FullImageAdapter(Activity activity, ArrayList<String> arrayList) {
            this._activity = activity;
            this._imagePaths = arrayList;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this._imagePaths.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = this._activity.getLayoutInflater();
            this.inflater = layoutInflater;
            View inflate = layoutInflater.inflate(R.layout.layout_fullscreen_image, viewGroup, false);
            ((MultiTouchZoomableImageView) inflate.findViewById(R.id.imgDisplay)).setImageBitmap(BitmapFactory.decodeFile(this._imagePaths.get(i)));
            ((ViewPager) viewGroup).addView(inflate);
            return inflate;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            ((ViewPager) viewGroup).removeView((RelativeLayout) obj);
        }
    }

    public boolean isInstallApp(String str) {
        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        if (installedPackages != null) {
            for (int i = 0; i < installedPackages.size(); i++) {
                if (str.equals(installedPackages.get(i).packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
