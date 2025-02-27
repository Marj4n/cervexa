package com.gizthon.camera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gizthon.camera.R;

public class ZoomableImageActivity extends AppCompatActivity {
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomable_image);

        PhotoView photoView = findViewById(R.id.photoView);
        ImageButton shareButton = findViewById(R.id.shareButton);
        Intent intent = getIntent();
        imageUri = intent.getData();

        if (imageUri != null) {
            Glide.with(this).load(imageUri).into(photoView);
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }

    private void shareImage() {
        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        }
    }
}