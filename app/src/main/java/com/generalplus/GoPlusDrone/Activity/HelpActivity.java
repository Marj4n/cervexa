package com.generalplus.GoPlusDrone.Activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.generalplus.GoPlusDrone.R;

/* loaded from: classes.dex */
public class HelpActivity extends Activity {
    private TextView h_text1;
    private TextView h_text10;
    private TextView h_text11;
    private TextView h_text12;
    private TextView h_text2;
    private TextView h_text3;
    private TextView h_text4;
    private TextView h_text5;
    private TextView h_text6;
    private TextView h_text7;
    private TextView h_text8;
    private TextView h_text9;
    private LinearLayout layout;
    private LinearLayout layout1;
    private int mScreenWidth;
    private int mScreenheight;
    private TextView version_t;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.help_activity);
        this.layout = (LinearLayout) findViewById(R.id.help_layout);
        this.layout1 = (LinearLayout) findViewById(R.id.help_layout1);
        this.h_text1 = (TextView) findViewById(R.id.h_text1);
        this.h_text2 = (TextView) findViewById(R.id.h_text2);
        this.h_text3 = (TextView) findViewById(R.id.h_text3);
        this.h_text4 = (TextView) findViewById(R.id.h_text4);
        this.h_text5 = (TextView) findViewById(R.id.h_text5);
        this.h_text6 = (TextView) findViewById(R.id.h_text6);
        this.h_text7 = (TextView) findViewById(R.id.h_text7);
        this.h_text8 = (TextView) findViewById(R.id.h_text8);
        this.h_text9 = (TextView) findViewById(R.id.h_text9);
        this.h_text10 = (TextView) findViewById(R.id.h_text10);
        this.h_text11 = (TextView) findViewById(R.id.h_text11);
        this.h_text12 = (TextView) findViewById(R.id.h_text12);
        this.version_t = (TextView) findViewById(R.id.version);
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        this.mScreenheight = getResources().getDisplayMetrics().heightPixels;
        int i = this.mScreenWidth;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((i * 9) / 10, i / 15);
        layoutParams.topMargin = this.mScreenWidth / 20;
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundResource(R.drawable.text_style);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(16);
        linearLayout.setOrientation(7);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = this.mScreenWidth / 25;
        TextView textView = new TextView(this);
        String version = getVersion();
        textView.setText(getResources().getString(R.string.language3) + version + "  " + getResources().getString(R.string.language4) + version);
        textView.setLayoutParams(layoutParams2);
        linearLayout.addView(textView);
        this.layout.addView(linearLayout);
        this.layout1.getLayoutParams().width = (this.mScreenWidth * 9) / 10;
        this.layout1.getLayoutParams().height = -2;
        this.layout1.setGravity(16);
        this.h_text1.setText(Html.fromHtml(getResources().getString(R.string.language31)));
        this.h_text2.setText(Html.fromHtml(getResources().getString(R.string.language32)));
        this.h_text3.setText(Html.fromHtml(getResources().getString(R.string.language34)));
        this.h_text4.setText(Html.fromHtml(getResources().getString(R.string.language35)));
        this.h_text5.setText(Html.fromHtml(getResources().getString(R.string.language36)));
        this.h_text6.setText(Html.fromHtml(getResources().getString(R.string.language37)));
        this.h_text7.setText(Html.fromHtml(getResources().getString(R.string.language38)));
        this.h_text8.setText(Html.fromHtml(getResources().getString(R.string.language39)));
        this.h_text9.setText(Html.fromHtml(getResources().getString(R.string.language49)));
        this.h_text10.setText(Html.fromHtml(getResources().getString(R.string.language60)));
        this.h_text11.setText(Html.fromHtml(getResources().getString(R.string.language61)));
        this.h_text12.setText(Html.fromHtml(getResources().getString(R.string.language62)));
        this.version_t.setText(getResources().getString(R.string.language295) + getVersion());
    }

    public String getVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void back(View view) {
        finish();
    }
}
