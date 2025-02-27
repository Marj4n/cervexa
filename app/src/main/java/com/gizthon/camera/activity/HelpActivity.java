package com.gizthon.camera.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.databinding.DataBindingUtil;

import com.gizthon.camera.R;
import com.gizthon.camera.databinding.HelpActivityBinding;
import com.jaeger.library.StatusBarUtil;

/* loaded from: classes.dex */
public class HelpActivity extends CameraBaseActivity {
    private HelpActivityBinding binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) HelpActivity.class));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (HelpActivityBinding) DataBindingUtil.setContentView(this, R.layout.help_activity);
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#09B0F3"));
        this.binding.ivR.setOnClickListener(new View.OnClickListener() { // from class: com.gizthon.camera.activity.HelpActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HelpActivity.this.binding.llLayout1.setVisibility(8);
                HelpActivity.this.binding.llLayout2.setVisibility(0);
            }
        });
        this.binding.ivL.setOnClickListener(new View.OnClickListener() { // from class: com.gizthon.camera.activity.HelpActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HelpActivity.this.binding.llLayout2.setVisibility(8);
                HelpActivity.this.binding.llLayout1.setVisibility(0);
            }
        });
        this.binding.back.setOnClickListener(new View.OnClickListener() { // from class: com.gizthon.camera.activity.HelpActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HelpActivity.this.finish();
            }
        });
        String language = getResources().getConfiguration().locale.getLanguage();
        if (language.contains("zh")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2);
            return;
        }
        if (language.contains("de")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_de);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_de);
            return;
        }
        if (language.contains("fr")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_fr);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_fr);
            return;
        }
        if (language.contains("es")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_es);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_es);
        } else if (language.contains("it")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_it);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_it);
        } else if (language.contains("ja")) {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_ja);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_ja);
        } else {
            this.binding.ivHelp1.setImageResource(R.mipmap.icon_help_bg_en);
            this.binding.ivHelp2.setImageResource(R.mipmap.icon_help_bg2_en);
        }
    }
}
